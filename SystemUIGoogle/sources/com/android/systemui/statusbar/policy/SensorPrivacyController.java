package com.android.systemui.statusbar.policy;
/* loaded from: classes.dex */
public interface SensorPrivacyController extends CallbackController<OnSensorPrivacyChangedListener> {

    /* loaded from: classes.dex */
    public interface OnSensorPrivacyChangedListener {
        void onSensorPrivacyChanged(boolean z);
    }

    void init();

    boolean isSensorPrivacyEnabled();
}
