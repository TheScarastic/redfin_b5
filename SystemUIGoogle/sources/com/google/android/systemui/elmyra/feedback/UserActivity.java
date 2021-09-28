package com.google.android.systemui.elmyra.feedback;

import android.content.Context;
import android.os.PowerManager;
import android.os.SystemClock;
import com.android.systemui.Dependency;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
/* loaded from: classes2.dex */
public class UserActivity implements FeedbackEffect {
    private final PowerManager mPowerManager;
    private int mTriggerCount = 0;
    private int mLastStage = 0;
    private final KeyguardStateController mKeyguardStateController = (KeyguardStateController) Dependency.get(KeyguardStateController.class);

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onRelease() {
    }

    public UserActivity(Context context) {
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onProgress(float f, int i) {
        PowerManager powerManager;
        if (i != this.mLastStage && i == 2 && !this.mKeyguardStateController.isShowing() && (powerManager = this.mPowerManager) != null) {
            powerManager.userActivity(SystemClock.uptimeMillis(), 0, 0);
            this.mTriggerCount++;
        }
        this.mLastStage = i;
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onResolve(GestureSensor.DetectionProperties detectionProperties) {
        this.mTriggerCount--;
    }

    public String toString() {
        return super.toString() + " [mTriggerCount -> " + this.mTriggerCount + "]";
    }
}
