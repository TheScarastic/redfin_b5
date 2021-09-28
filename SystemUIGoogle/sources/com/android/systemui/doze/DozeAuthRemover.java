package com.android.systemui.doze;

import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.doze.DozeMachine;
/* loaded from: classes.dex */
public class DozeAuthRemover implements DozeMachine.Part {
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;

    public DozeAuthRemover(KeyguardUpdateMonitor keyguardUpdateMonitor) {
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        if (state2 == DozeMachine.State.DOZE || state2 == DozeMachine.State.DOZE_AOD) {
            if (this.mKeyguardUpdateMonitor.getUserUnlockedWithBiometric(KeyguardUpdateMonitor.getCurrentUser())) {
                this.mKeyguardUpdateMonitor.clearBiometricRecognized();
            }
        }
    }
}
