package com.android.systemui.statusbar.policy;
/* compiled from: DeviceControlsController.kt */
/* loaded from: classes.dex */
public interface DeviceControlsController {

    /* compiled from: DeviceControlsController.kt */
    /* loaded from: classes.dex */
    public interface Callback {
        void onControlsUpdate(Integer num);
    }

    void removeCallback();

    void setCallback(Callback callback);
}
