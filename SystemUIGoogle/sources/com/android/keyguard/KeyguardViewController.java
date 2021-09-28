package com.android.keyguard;

import android.os.Bundle;
import android.view.ViewRootImpl;
/* loaded from: classes.dex */
public interface KeyguardViewController {
    void blockPanelExpansionFromCurrentTouch();

    boolean bouncerIsOrWillBeShowing();

    void dismissAndCollapse();

    ViewRootImpl getViewRootImpl();

    void hide(long j, long j2);

    boolean isBouncerShowing();

    boolean isGoingToNotificationShade();

    boolean isShowing();

    boolean isUnlockWithWallpaper();

    void keyguardGoingAway();

    void notifyKeyguardAuthenticated(boolean z);

    void onCancelClicked();

    @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
    default void onFinishedGoingToSleep() {
    }

    default void onScreenTurnedOn() {
    }

    default void onScreenTurningOn() {
    }

    @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
    default void onStartedGoingToSleep() {
    }

    @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
    default void onStartedWakingUp() {
    }

    void reset(boolean z);

    void setKeyguardGoingAwayState(boolean z);

    void setNeedsInput(boolean z);

    void setOccluded(boolean z, boolean z2);

    boolean shouldDisableWindowAnimationsForUnlock();

    boolean shouldSubtleWindowAnimationsForUnlock();

    void show(Bundle bundle);

    void showBouncer(boolean z);

    void startPreHideAnimation(Runnable runnable);
}
