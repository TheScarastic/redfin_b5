package com.google.android.systemui.elmyra.gates;

import android.content.Context;
import android.os.PowerManager;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dependency;
/* loaded from: classes2.dex */
public class PowerState extends Gate {
    private final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.google.android.systemui.elmyra.gates.PowerState.1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onStartedWakingUp() {
            PowerState.this.notifyListener();
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onFinishedGoingToSleep(int i) {
            PowerState.this.notifyListener();
        }
    };
    private final PowerManager mPowerManager;

    public PowerState(Context context) {
        super(context);
        this.mPowerManager = (PowerManager) context.getSystemService("power");
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected void onActivate() {
        ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).registerCallback(this.mKeyguardUpdateMonitorCallback);
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected void onDeactivate() {
        ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).removeCallback(this.mKeyguardUpdateMonitorCallback);
    }

    /* access modifiers changed from: protected */
    @Override // com.google.android.systemui.elmyra.gates.Gate
    public boolean isBlocked() {
        return !this.mPowerManager.isInteractive();
    }
}
