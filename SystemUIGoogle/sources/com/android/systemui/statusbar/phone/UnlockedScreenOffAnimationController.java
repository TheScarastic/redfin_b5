package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.LightRevealScrim;
import com.android.systemui.statusbar.StatusBarStateControllerImpl;
import com.android.systemui.statusbar.notification.AnimatableProperty;
import com.android.systemui.statusbar.notification.PropertyAnimator;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.Lazy;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: UnlockedScreenOffAnimationController.kt */
/* loaded from: classes.dex */
public final class UnlockedScreenOffAnimationController implements WakefulnessLifecycle.Observer {
    private boolean aodUiAnimationPlaying;
    private final Context context;
    private Boolean decidedToAnimateGoingToSleep;
    private final Lazy<DozeParameters> dozeParameters;
    private final Handler handler = new Handler();
    private final KeyguardStateController keyguardStateController;
    private final Lazy<KeyguardViewMediator> keyguardViewMediatorLazy;
    private boolean lightRevealAnimationPlaying;
    private final ValueAnimator lightRevealAnimator;
    private LightRevealScrim lightRevealScrim;
    private StatusBar statusBar;
    private final StatusBarStateControllerImpl statusBarStateControllerImpl;
    private final WakefulnessLifecycle wakefulnessLifecycle;

    public UnlockedScreenOffAnimationController(Context context, WakefulnessLifecycle wakefulnessLifecycle, StatusBarStateControllerImpl statusBarStateControllerImpl, Lazy<KeyguardViewMediator> lazy, KeyguardStateController keyguardStateController, Lazy<DozeParameters> lazy2) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(wakefulnessLifecycle, "wakefulnessLifecycle");
        Intrinsics.checkNotNullParameter(statusBarStateControllerImpl, "statusBarStateControllerImpl");
        Intrinsics.checkNotNullParameter(lazy, "keyguardViewMediatorLazy");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        Intrinsics.checkNotNullParameter(lazy2, "dozeParameters");
        this.context = context;
        this.wakefulnessLifecycle = wakefulnessLifecycle;
        this.statusBarStateControllerImpl = statusBarStateControllerImpl;
        this.keyguardViewMediatorLazy = lazy;
        this.keyguardStateController = keyguardStateController;
        this.dozeParameters = lazy2;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
        ofFloat.setDuration(750L);
        ofFloat.setInterpolator(Interpolators.LINEAR);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this) { // from class: com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController$lightRevealAnimator$1$1
            final /* synthetic */ UnlockedScreenOffAnimationController this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                LightRevealScrim lightRevealScrim = this.this$0.lightRevealScrim;
                if (lightRevealScrim != null) {
                    Object animatedValue = valueAnimator.getAnimatedValue();
                    Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                    lightRevealScrim.setRevealAmount(((Float) animatedValue).floatValue());
                    return;
                }
                Intrinsics.throwUninitializedPropertyAccessException("lightRevealScrim");
                throw null;
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter(this) { // from class: com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController$lightRevealAnimator$1$2
            final /* synthetic */ UnlockedScreenOffAnimationController this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                LightRevealScrim lightRevealScrim = this.this$0.lightRevealScrim;
                if (lightRevealScrim != null) {
                    lightRevealScrim.setRevealAmount(1.0f);
                    this.this$0.lightRevealAnimationPlaying = false;
                    return;
                }
                Intrinsics.throwUninitializedPropertyAccessException("lightRevealScrim");
                throw null;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                this.this$0.lightRevealAnimationPlaying = false;
            }
        });
        Unit unit = Unit.INSTANCE;
        this.lightRevealAnimator = ofFloat;
    }

    public final void initialize(StatusBar statusBar, LightRevealScrim lightRevealScrim) {
        Intrinsics.checkNotNullParameter(statusBar, "statusBar");
        Intrinsics.checkNotNullParameter(lightRevealScrim, "lightRevealScrim");
        this.lightRevealScrim = lightRevealScrim;
        this.statusBar = statusBar;
        this.wakefulnessLifecycle.addObserver(this);
    }

    public final void animateInKeyguard(View view, Runnable runnable) {
        Intrinsics.checkNotNullParameter(view, "keyguardView");
        Intrinsics.checkNotNullParameter(runnable, "after");
        view.setAlpha(0.0f);
        view.setVisibility(0);
        float y = view.getY();
        view.setY(y - (((float) view.getHeight()) * 0.1f));
        AnimatableProperty animatableProperty = AnimatableProperty.Y;
        PropertyAnimator.cancelAnimation(view, animatableProperty);
        long j = (long) 500;
        PropertyAnimator.setProperty(view, animatableProperty, y, new AnimationProperties().setDuration(j), true);
        view.animate().setDuration(j).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).alpha(1.0f).withEndAction(new Runnable(this, runnable) { // from class: com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController$animateInKeyguard$1
            final /* synthetic */ Runnable $after;
            final /* synthetic */ UnlockedScreenOffAnimationController this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$after = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.this$0.aodUiAnimationPlaying = false;
                ((KeyguardViewMediator) this.this$0.keyguardViewMediatorLazy.get()).maybeHandlePendingLock();
                StatusBar statusBar = this.this$0.statusBar;
                if (statusBar != null) {
                    statusBar.updateIsKeyguard();
                    this.$after.run();
                    this.this$0.decidedToAnimateGoingToSleep = null;
                    return;
                }
                Intrinsics.throwUninitializedPropertyAccessException("statusBar");
                throw null;
            }
        }).start();
    }

    @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
    public void onStartedWakingUp() {
        this.decidedToAnimateGoingToSleep = null;
        this.lightRevealAnimator.cancel();
        this.handler.removeCallbacksAndMessages(null);
    }

    @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
    public void onFinishedWakingUp() {
        this.lightRevealAnimationPlaying = false;
        this.aodUiAnimationPlaying = false;
        if (this.dozeParameters.get().canControlUnlockedScreenOff()) {
            StatusBar statusBar = this.statusBar;
            if (statusBar != null) {
                statusBar.updateIsKeyguard(true);
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("statusBar");
                throw null;
            }
        }
    }

    @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
    public void onStartedGoingToSleep() {
        if (this.dozeParameters.get().shouldControlUnlockedScreenOff()) {
            this.decidedToAnimateGoingToSleep = Boolean.TRUE;
            this.lightRevealAnimationPlaying = true;
            this.lightRevealAnimator.start();
            this.handler.postDelayed(new Runnable(this) { // from class: com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController$onStartedGoingToSleep$1
                final /* synthetic */ UnlockedScreenOffAnimationController this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    this.this$0.aodUiAnimationPlaying = true;
                    StatusBar statusBar = this.this$0.statusBar;
                    if (statusBar != null) {
                        statusBar.getNotificationPanelViewController().showAodUi();
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException("statusBar");
                        throw null;
                    }
                }
            }, 600);
            return;
        }
        this.decidedToAnimateGoingToSleep = Boolean.FALSE;
    }

    public final boolean shouldPlayUnlockedScreenOffAnimation() {
        StatusBar statusBar;
        if (!Intrinsics.areEqual(this.decidedToAnimateGoingToSleep, Boolean.FALSE) && this.dozeParameters.get().canControlUnlockedScreenOff() && this.statusBarStateControllerImpl.getState() == 0 && (statusBar = this.statusBar) != null) {
            if (statusBar == null) {
                Intrinsics.throwUninitializedPropertyAccessException("statusBar");
                throw null;
            } else if (statusBar.getNotificationPanelViewController().isFullyCollapsed()) {
                if (this.keyguardStateController.isKeyguardScreenRotationAllowed() || this.context.getResources().getConfiguration().orientation == 1) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public final boolean isScreenOffAnimationPlaying() {
        return this.lightRevealAnimationPlaying || this.aodUiAnimationPlaying;
    }

    public final boolean isScreenOffLightRevealAnimationPlaying() {
        return this.lightRevealAnimationPlaying;
    }
}
