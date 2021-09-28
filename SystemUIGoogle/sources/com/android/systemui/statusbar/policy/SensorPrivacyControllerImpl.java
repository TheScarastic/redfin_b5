package com.android.systemui.statusbar.policy;

import android.hardware.SensorPrivacyManager;
import com.android.systemui.statusbar.policy.SensorPrivacyController;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class SensorPrivacyControllerImpl implements SensorPrivacyController, SensorPrivacyManager.OnAllSensorPrivacyChangedListener {
    private final List<SensorPrivacyController.OnSensorPrivacyChangedListener> mListeners = new ArrayList(1);
    private Object mLock = new Object();
    private boolean mSensorPrivacyEnabled;
    private SensorPrivacyManager mSensorPrivacyManager;

    public SensorPrivacyControllerImpl(SensorPrivacyManager sensorPrivacyManager) {
        this.mSensorPrivacyManager = sensorPrivacyManager;
    }

    @Override // com.android.systemui.statusbar.policy.SensorPrivacyController
    public void init() {
        this.mSensorPrivacyEnabled = this.mSensorPrivacyManager.isAllSensorPrivacyEnabled();
        this.mSensorPrivacyManager.addAllSensorPrivacyListener(this);
    }

    @Override // com.android.systemui.statusbar.policy.SensorPrivacyController
    public boolean isSensorPrivacyEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mSensorPrivacyEnabled;
        }
        return z;
    }

    public void addCallback(SensorPrivacyController.OnSensorPrivacyChangedListener onSensorPrivacyChangedListener) {
        synchronized (this.mLock) {
            this.mListeners.add(onSensorPrivacyChangedListener);
            notifyListenerLocked(onSensorPrivacyChangedListener);
        }
    }

    public void removeCallback(SensorPrivacyController.OnSensorPrivacyChangedListener onSensorPrivacyChangedListener) {
        synchronized (this.mLock) {
            this.mListeners.remove(onSensorPrivacyChangedListener);
        }
    }

    public void onAllSensorPrivacyChanged(boolean z) {
        synchronized (this.mLock) {
            this.mSensorPrivacyEnabled = z;
            for (SensorPrivacyController.OnSensorPrivacyChangedListener onSensorPrivacyChangedListener : this.mListeners) {
                notifyListenerLocked(onSensorPrivacyChangedListener);
            }
        }
    }

    private void notifyListenerLocked(SensorPrivacyController.OnSensorPrivacyChangedListener onSensorPrivacyChangedListener) {
        onSensorPrivacyChangedListener.onSensorPrivacyChanged(this.mSensorPrivacyEnabled);
    }
}
