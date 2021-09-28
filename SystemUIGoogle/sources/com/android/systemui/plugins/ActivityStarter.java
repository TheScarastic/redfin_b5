package com.android.systemui.plugins;

import android.app.PendingIntent;
import android.content.Intent;
import android.view.View;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.plugins.annotations.ProvidesInterface;
@ProvidesInterface(version = 2)
/* loaded from: classes.dex */
public interface ActivityStarter {
    public static final int VERSION = 2;

    /* loaded from: classes.dex */
    public interface Callback {
        void onActivityStarted(int i);
    }

    /* loaded from: classes.dex */
    public interface OnDismissAction {
        boolean onDismiss();

        default boolean willRunAnimationOnKeyguard() {
            return false;
        }
    }

    void dismissKeyguardThenExecute(OnDismissAction onDismissAction, Runnable runnable, boolean z);

    void postQSRunnableDismissingKeyguard(Runnable runnable);

    void postStartActivityDismissingKeyguard(PendingIntent pendingIntent);

    void postStartActivityDismissingKeyguard(PendingIntent pendingIntent, ActivityLaunchAnimator.Controller controller);

    void postStartActivityDismissingKeyguard(Intent intent, int i);

    void postStartActivityDismissingKeyguard(Intent intent, int i, ActivityLaunchAnimator.Controller controller);

    void startActivity(Intent intent, boolean z);

    void startActivity(Intent intent, boolean z, ActivityLaunchAnimator.Controller controller);

    void startActivity(Intent intent, boolean z, Callback callback);

    void startActivity(Intent intent, boolean z, boolean z2);

    void startActivity(Intent intent, boolean z, boolean z2, int i);

    void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent);

    void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent, Runnable runnable);

    void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent, Runnable runnable, View view);

    void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent, Runnable runnable, ActivityLaunchAnimator.Controller controller);
}
