package com.google.android.systemui.elmyra.gates;

import android.content.Context;
import com.android.systemui.Dependency;
import com.android.systemui.statusbar.policy.KeyguardStateController;
/* loaded from: classes2.dex */
public class KeyguardVisibility extends Gate {
    private final KeyguardStateController.Callback mKeyguardMonitorCallback = new KeyguardStateController.Callback() { // from class: com.google.android.systemui.elmyra.gates.KeyguardVisibility.1
        @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
        public void onKeyguardShowingChanged() {
            KeyguardVisibility.this.notifyListener();
        }
    };
    private final KeyguardStateController mKeyguardStateController = (KeyguardStateController) Dependency.get(KeyguardStateController.class);

    public KeyguardVisibility(Context context) {
        super(context);
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected void onActivate() {
        this.mKeyguardStateController.addCallback(this.mKeyguardMonitorCallback);
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected void onDeactivate() {
        this.mKeyguardStateController.removeCallback(this.mKeyguardMonitorCallback);
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected boolean isBlocked() {
        return isKeyguardShowing();
    }

    public boolean isKeyguardShowing() {
        return this.mKeyguardStateController.isShowing();
    }

    public boolean isKeyguardOccluded() {
        return this.mKeyguardStateController.isOccluded();
    }
}
