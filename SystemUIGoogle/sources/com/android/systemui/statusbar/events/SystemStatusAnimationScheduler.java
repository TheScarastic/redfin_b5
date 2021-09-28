package com.android.systemui.statusbar.events;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Process;
import android.provider.DeviceConfig;
import android.view.View;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.phone.StatusBarWindowController;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.util.Assert;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SystemStatusAnimationScheduler.kt */
/* loaded from: classes.dex */
public final class SystemStatusAnimationScheduler implements CallbackController<SystemStatusAnimationCallback>, Dumpable {
    public static final Companion Companion = new Companion(null);
    private int animationState;
    private Runnable cancelExecutionRunnable;
    private final SystemEventChipAnimationController chipAnimationController;
    private final SystemEventCoordinator coordinator;
    private final DumpManager dumpManager;
    private final DelayableExecutor executor;
    private boolean hasPersistentDot;
    private StatusEvent scheduledEvent;
    private final StatusBarWindowController statusBarWindowController;
    private final SystemClock systemClock;
    private final Set<SystemStatusAnimationCallback> listeners = new LinkedHashSet();
    private final ValueAnimator.AnimatorUpdateListener systemUpdateListener = new ValueAnimator.AnimatorUpdateListener(this) { // from class: com.android.systemui.statusbar.events.SystemStatusAnimationScheduler$systemUpdateListener$1
        final /* synthetic */ SystemStatusAnimationScheduler this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
            SystemStatusAnimationScheduler systemStatusAnimationScheduler = this.this$0;
            Intrinsics.checkNotNullExpressionValue(valueAnimator, "anim");
            systemStatusAnimationScheduler.notifySystemAnimationUpdate(valueAnimator);
        }
    };
    private final SystemStatusAnimationScheduler$systemAnimatorAdapter$1 systemAnimatorAdapter = new SystemStatusAnimationScheduler$systemAnimatorAdapter$1(this);
    private final ValueAnimator.AnimatorUpdateListener chipUpdateListener = new ValueAnimator.AnimatorUpdateListener(this) { // from class: com.android.systemui.statusbar.events.SystemStatusAnimationScheduler$chipUpdateListener$1
        final /* synthetic */ SystemStatusAnimationScheduler this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
            SystemEventChipAnimationController systemEventChipAnimationController = this.this$0.chipAnimationController;
            Intrinsics.checkNotNullExpressionValue(valueAnimator, "anim");
            systemEventChipAnimationController.onChipAnimationUpdate(valueAnimator, this.this$0.getAnimationState());
        }
    };

    public SystemStatusAnimationScheduler(SystemEventCoordinator systemEventCoordinator, SystemEventChipAnimationController systemEventChipAnimationController, StatusBarWindowController statusBarWindowController, DumpManager dumpManager, SystemClock systemClock, DelayableExecutor delayableExecutor) {
        Intrinsics.checkNotNullParameter(systemEventCoordinator, "coordinator");
        Intrinsics.checkNotNullParameter(systemEventChipAnimationController, "chipAnimationController");
        Intrinsics.checkNotNullParameter(statusBarWindowController, "statusBarWindowController");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(systemClock, "systemClock");
        Intrinsics.checkNotNullParameter(delayableExecutor, "executor");
        this.coordinator = systemEventCoordinator;
        this.chipAnimationController = systemEventChipAnimationController;
        this.statusBarWindowController = statusBarWindowController;
        this.dumpManager = dumpManager;
        this.systemClock = systemClock;
        this.executor = delayableExecutor;
        systemEventCoordinator.attachScheduler(this);
        dumpManager.registerDumpable("SystemStatusAnimationScheduler", this);
    }

    /* compiled from: SystemStatusAnimationScheduler.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    private final boolean isImmersiveIndicatorEnabled() {
        return DeviceConfig.getBoolean("privacy", "enable_immersive_indicator", true);
    }

    public final int getAnimationState() {
        return this.animationState;
    }

    public final boolean getHasPersistentDot() {
        return this.hasPersistentDot;
    }

    public final void onStatusEvent(StatusEvent statusEvent) {
        int i;
        Intrinsics.checkNotNullParameter(statusEvent, "event");
        if (!isTooEarly() && isImmersiveIndicatorEnabled()) {
            Assert.isMainThread();
            int priority = statusEvent.getPriority();
            StatusEvent statusEvent2 = this.scheduledEvent;
            if (priority <= (statusEvent2 == null ? -1 : statusEvent2.getPriority()) || (i = this.animationState) == 3 || i == 4 || !statusEvent.getForceVisible()) {
                StatusEvent statusEvent3 = this.scheduledEvent;
                if (Intrinsics.areEqual(statusEvent3 == null ? null : Boolean.valueOf(statusEvent3.shouldUpdateFromEvent(statusEvent)), Boolean.TRUE)) {
                    StatusEvent statusEvent4 = this.scheduledEvent;
                    if (statusEvent4 != null) {
                        statusEvent4.updateFromEvent(statusEvent);
                    }
                    if (statusEvent.getForceVisible()) {
                        this.hasPersistentDot = true;
                        notifyTransitionToPersistentDot();
                        return;
                    }
                    return;
                }
                return;
            }
            scheduleEvent(statusEvent);
        }
    }

    private final void clearDotIfVisible() {
        notifyHidePersistentDot();
    }

    public final void setShouldShowPersistentPrivacyIndicator(boolean z) {
        if (this.hasPersistentDot != z && isImmersiveIndicatorEnabled()) {
            this.hasPersistentDot = z;
            if (!z) {
                clearDotIfVisible();
            }
        }
    }

    private final boolean isTooEarly() {
        return this.systemClock.uptimeMillis() - Process.getStartUptimeMillis() < 5000;
    }

    private final void scheduleEvent(StatusEvent statusEvent) {
        this.scheduledEvent = statusEvent;
        if (statusEvent.getForceVisible()) {
            this.hasPersistentDot = true;
        }
        if (statusEvent.getShowAnimation() || !statusEvent.getForceVisible()) {
            this.cancelExecutionRunnable = this.executor.executeDelayed(new Runnable(this) { // from class: com.android.systemui.statusbar.events.SystemStatusAnimationScheduler$scheduleEvent$1
                final /* synthetic */ SystemStatusAnimationScheduler this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    this.this$0.cancelExecutionRunnable = null;
                    this.this$0.animationState = 1;
                    this.this$0.statusBarWindowController.setForceStatusBarVisible(true);
                    ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
                    ofFloat.setDuration(250L);
                    ofFloat.addListener(this.this$0.systemAnimatorAdapter);
                    ofFloat.addUpdateListener(this.this$0.systemUpdateListener);
                    ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
                    ofFloat2.setDuration(250L);
                    SystemStatusAnimationScheduler systemStatusAnimationScheduler = this.this$0;
                    StatusEvent statusEvent2 = systemStatusAnimationScheduler.scheduledEvent;
                    Intrinsics.checkNotNull(statusEvent2);
                    ofFloat2.addListener(new SystemStatusAnimationScheduler.ChipAnimatorAdapter(systemStatusAnimationScheduler, 2, statusEvent2.getViewCreator()));
                    ofFloat2.addUpdateListener(this.this$0.chipUpdateListener);
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playSequentially(ofFloat, ofFloat2);
                    animatorSet.start();
                    DelayableExecutor delayableExecutor = this.this$0.executor;
                    final SystemStatusAnimationScheduler systemStatusAnimationScheduler2 = this.this$0;
                    delayableExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.statusbar.events.SystemStatusAnimationScheduler$scheduleEvent$1.1
                        @Override // java.lang.Runnable
                        public final void run() {
                            Animator animator;
                            systemStatusAnimationScheduler2.animationState = 3;
                            ValueAnimator ofFloat3 = ValueAnimator.ofFloat(0.0f, 1.0f);
                            ofFloat3.setDuration(250L);
                            ofFloat3.addListener(systemStatusAnimationScheduler2.systemAnimatorAdapter);
                            ofFloat3.addUpdateListener(systemStatusAnimationScheduler2.systemUpdateListener);
                            ValueAnimator ofFloat4 = ValueAnimator.ofFloat(1.0f, 0.0f);
                            ofFloat4.setDuration(250L);
                            int i = systemStatusAnimationScheduler2.getHasPersistentDot() ? 4 : 0;
                            SystemStatusAnimationScheduler systemStatusAnimationScheduler3 = systemStatusAnimationScheduler2;
                            StatusEvent statusEvent3 = systemStatusAnimationScheduler3.scheduledEvent;
                            Intrinsics.checkNotNull(statusEvent3);
                            ofFloat4.addListener(new SystemStatusAnimationScheduler.ChipAnimatorAdapter(systemStatusAnimationScheduler3, i, statusEvent3.getViewCreator()));
                            ofFloat4.addUpdateListener(systemStatusAnimationScheduler2.chipUpdateListener);
                            AnimatorSet animatorSet2 = new AnimatorSet();
                            animatorSet2.play(ofFloat4).before(ofFloat3);
                            if (systemStatusAnimationScheduler2.getHasPersistentDot() && (animator = systemStatusAnimationScheduler2.notifyTransitionToPersistentDot()) != null) {
                                animatorSet2.playTogether(ofFloat3, animator);
                            }
                            animatorSet2.start();
                            systemStatusAnimationScheduler2.statusBarWindowController.setForceStatusBarVisible(false);
                            systemStatusAnimationScheduler2.scheduledEvent = null;
                        }
                    }, 1500);
                }
            }, 0);
            return;
        }
        notifyTransitionToPersistentDot();
        this.scheduledEvent = null;
    }

    /* access modifiers changed from: private */
    public final Animator notifyTransitionToPersistentDot() {
        Set<SystemStatusAnimationCallback> set = this.listeners;
        ArrayList arrayList = new ArrayList();
        Iterator<T> it = set.iterator();
        while (true) {
            String str = null;
            if (!it.hasNext()) {
                break;
            }
            SystemStatusAnimationCallback systemStatusAnimationCallback = (SystemStatusAnimationCallback) it.next();
            StatusEvent statusEvent = this.scheduledEvent;
            if (statusEvent != null) {
                str = statusEvent.getContentDescription();
            }
            Animator onSystemStatusAnimationTransitionToPersistentDot = systemStatusAnimationCallback.onSystemStatusAnimationTransitionToPersistentDot(str);
            if (onSystemStatusAnimationTransitionToPersistentDot != null) {
                arrayList.add(onSystemStatusAnimationTransitionToPersistentDot);
            }
        }
        if (!(!arrayList.isEmpty())) {
            return null;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(arrayList);
        return animatorSet;
    }

    private final Animator notifyHidePersistentDot() {
        Set<SystemStatusAnimationCallback> set = this.listeners;
        ArrayList arrayList = new ArrayList();
        for (SystemStatusAnimationCallback systemStatusAnimationCallback : set) {
            Animator onHidePersistentDot = systemStatusAnimationCallback.onHidePersistentDot();
            if (onHidePersistentDot != null) {
                arrayList.add(onHidePersistentDot);
            }
        }
        if (this.animationState == 4) {
            this.animationState = 0;
        }
        if (!(!arrayList.isEmpty())) {
            return null;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(arrayList);
        return animatorSet;
    }

    /* access modifiers changed from: private */
    public final void notifySystemStart() {
        for (SystemStatusAnimationCallback systemStatusAnimationCallback : this.listeners) {
            systemStatusAnimationCallback.onSystemChromeAnimationStart();
        }
    }

    /* access modifiers changed from: private */
    public final void notifySystemFinish() {
        for (SystemStatusAnimationCallback systemStatusAnimationCallback : this.listeners) {
            systemStatusAnimationCallback.onSystemChromeAnimationEnd();
        }
    }

    /* access modifiers changed from: private */
    public final void notifySystemAnimationUpdate(ValueAnimator valueAnimator) {
        for (SystemStatusAnimationCallback systemStatusAnimationCallback : this.listeners) {
            systemStatusAnimationCallback.onSystemChromeAnimationUpdate(valueAnimator);
        }
    }

    public void addCallback(SystemStatusAnimationCallback systemStatusAnimationCallback) {
        Intrinsics.checkNotNullParameter(systemStatusAnimationCallback, "listener");
        Assert.isMainThread();
        if (this.listeners.isEmpty()) {
            this.coordinator.startObserving();
        }
        this.listeners.add(systemStatusAnimationCallback);
    }

    public void removeCallback(SystemStatusAnimationCallback systemStatusAnimationCallback) {
        Intrinsics.checkNotNullParameter(systemStatusAnimationCallback, "listener");
        Assert.isMainThread();
        this.listeners.remove(systemStatusAnimationCallback);
        if (this.listeners.isEmpty()) {
            this.coordinator.stopObserving();
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println(Intrinsics.stringPlus("Scheduled event: ", this.scheduledEvent));
        printWriter.println(Intrinsics.stringPlus("Has persistent privacy dot: ", Boolean.valueOf(this.hasPersistentDot)));
        printWriter.println(Intrinsics.stringPlus("Animation state: ", Integer.valueOf(this.animationState)));
        printWriter.println("Listeners:");
        if (this.listeners.isEmpty()) {
            printWriter.println("(none)");
            return;
        }
        for (SystemStatusAnimationCallback systemStatusAnimationCallback : this.listeners) {
            printWriter.println(Intrinsics.stringPlus("  ", systemStatusAnimationCallback));
        }
    }

    /* compiled from: SystemStatusAnimationScheduler.kt */
    /* loaded from: classes.dex */
    public final class ChipAnimatorAdapter extends AnimatorListenerAdapter {
        private final int endState;
        final /* synthetic */ SystemStatusAnimationScheduler this$0;
        private final Function1<Context, View> viewCreator;

        /* JADX DEBUG: Multi-variable search result rejected for r4v0, resolved type: kotlin.jvm.functions.Function1<? super android.content.Context, ? extends android.view.View> */
        /* JADX WARN: Multi-variable type inference failed */
        public ChipAnimatorAdapter(SystemStatusAnimationScheduler systemStatusAnimationScheduler, int i, Function1<? super Context, ? extends View> function1) {
            Intrinsics.checkNotNullParameter(systemStatusAnimationScheduler, "this$0");
            Intrinsics.checkNotNullParameter(function1, "viewCreator");
            this.this$0 = systemStatusAnimationScheduler;
            this.endState = i;
            this.viewCreator = function1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            int i;
            this.this$0.chipAnimationController.onChipAnimationEnd(this.this$0.getAnimationState());
            SystemStatusAnimationScheduler systemStatusAnimationScheduler = this.this$0;
            if (this.endState != 4 || systemStatusAnimationScheduler.getHasPersistentDot()) {
                i = this.endState;
            } else {
                i = 0;
            }
            systemStatusAnimationScheduler.animationState = i;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            this.this$0.chipAnimationController.onChipAnimationStart(this.viewCreator, this.this$0.getAnimationState());
        }
    }
}
