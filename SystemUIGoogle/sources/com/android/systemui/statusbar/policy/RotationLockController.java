package com.android.systemui.statusbar.policy;
/* loaded from: classes.dex */
public interface RotationLockController extends CallbackController<RotationLockControllerCallback> {

    /* loaded from: classes.dex */
    public interface RotationLockControllerCallback {
        void onRotationLockStateChanged(boolean z, boolean z2);
    }

    int getRotationLockOrientation();

    boolean isRotationLocked();

    void setRotationLocked(boolean z);

    void setRotationLockedAtAngle(boolean z, int i);
}
