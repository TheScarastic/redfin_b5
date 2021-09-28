package com.google.android.systemui.columbus.actions;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.google.android.systemui.columbus.actions.Action;
import com.google.android.systemui.columbus.feedback.FeedbackEffect;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Action.kt */
/* loaded from: classes2.dex */
public abstract class Action {
    private final Context context;
    private final Set<FeedbackEffect> feedbackEffects;
    private final Handler handler;
    private boolean isAvailable;
    private final Set<Listener> listeners;

    /* compiled from: Action.kt */
    /* loaded from: classes2.dex */
    public interface Listener {
        void onActionAvailabilityChanged(Action action);
    }

    public abstract String getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig();

    public abstract void onTrigger(GestureSensor.DetectionProperties detectionProperties);

    /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: java.util.Set<? extends com.google.android.systemui.columbus.feedback.FeedbackEffect> */
    /* JADX WARN: Multi-variable type inference failed */
    public Action(Context context, Set<? extends FeedbackEffect> set) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
        this.feedbackEffects = set;
        this.isAvailable = true;
        this.listeners = new LinkedHashSet();
        this.handler = new Handler(Looper.getMainLooper());
    }

    public /* synthetic */ Action(Context context, Set set, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i & 2) != 0 ? null : set);
    }

    /* access modifiers changed from: protected */
    public final Context getContext() {
        return this.context;
    }

    public boolean isAvailable() {
        return this.isAvailable;
    }

    public void setAvailable$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig(boolean z) {
        this.isAvailable = z;
    }

    public void registerListener(Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.listeners.add(listener);
    }

    public void unregisterListener(Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.listeners.remove(listener);
    }

    public void onGestureDetected(int i, GestureSensor.DetectionProperties detectionProperties) {
        updateFeedbackEffects(i, detectionProperties);
        if (i == 1) {
            Log.i(getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig(), "Triggering");
            onTrigger(detectionProperties);
        }
    }

    /* access modifiers changed from: protected */
    public final void setAvailable(boolean z) {
        if (isAvailable() != z) {
            setAvailable$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig(z);
            for (Listener listener : this.listeners) {
                this.handler.post(new Runnable(listener, this) { // from class: com.google.android.systemui.columbus.actions.Action$setAvailable$1$1
                    final /* synthetic */ Action.Listener $it;
                    final /* synthetic */ Action this$0;

                    /* access modifiers changed from: package-private */
                    {
                        this.$it = r1;
                        this.this$0 = r2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        this.$it.onActionAvailabilityChanged(this.this$0);
                    }
                });
            }
            if (!isAvailable()) {
                this.handler.post(new Runnable(this) { // from class: com.google.android.systemui.columbus.actions.Action$setAvailable$2
                    final /* synthetic */ Action this$0;

                    /* access modifiers changed from: package-private */
                    {
                        this.this$0 = r1;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        Action.updateFeedbackEffects$default(this.this$0, 0, null, 2, null);
                    }
                });
            }
        }
    }

    public static /* synthetic */ void updateFeedbackEffects$default(Action action, int i, GestureSensor.DetectionProperties detectionProperties, int i2, Object obj) {
        if (obj == null) {
            if ((i2 & 2) != 0) {
                detectionProperties = null;
            }
            action.updateFeedbackEffects(i, detectionProperties);
            return;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: updateFeedbackEffects");
    }

    public void updateFeedbackEffects(int i, GestureSensor.DetectionProperties detectionProperties) {
        Set<FeedbackEffect> set = this.feedbackEffects;
        if (set != null) {
            for (FeedbackEffect feedbackEffect : set) {
                feedbackEffect.onGestureDetected(i, detectionProperties);
            }
        }
    }

    public String toString() {
        String simpleName = getClass().getSimpleName();
        Intrinsics.checkNotNullExpressionValue(simpleName, "javaClass.simpleName");
        return simpleName;
    }
}
