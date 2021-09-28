package com.google.android.systemui.elmyra.sensors.config;

import android.content.Context;
import android.os.PowerManager;
import android.util.TypedValue;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dependency;
import com.android.systemui.R$dimen;
/* loaded from: classes2.dex */
public class ScreenStateAdjustment extends Adjustment {
    private final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback;
    private final PowerManager mPowerManager = (PowerManager) getContext().getSystemService("power");
    private final float mScreenOffAdjustment;

    public ScreenStateAdjustment(Context context) {
        super(context);
        AnonymousClass1 r0 = new KeyguardUpdateMonitorCallback() { // from class: com.google.android.systemui.elmyra.sensors.config.ScreenStateAdjustment.1
            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onStartedWakingUp() {
                ScreenStateAdjustment.this.onSensitivityChanged();
            }

            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onFinishedGoingToSleep(int i) {
                ScreenStateAdjustment.this.onSensitivityChanged();
            }
        };
        this.mKeyguardUpdateMonitorCallback = r0;
        TypedValue typedValue = new TypedValue();
        context.getResources().getValue(R$dimen.elmyra_screen_off_adjustment, typedValue, true);
        this.mScreenOffAdjustment = typedValue.getFloat();
        ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).registerCallback(r0);
    }

    @Override // com.google.android.systemui.elmyra.sensors.config.Adjustment
    public float adjustSensitivity(float f) {
        return !this.mPowerManager.isInteractive() ? f + this.mScreenOffAdjustment : f;
    }
}
