package com.google.android.systemui.columbus.gates;

import com.android.systemui.statusbar.policy.DeviceProvisionedController;
/* compiled from: SetupWizard.kt */
/* loaded from: classes2.dex */
public final class SetupWizard$provisionedListener$1 implements DeviceProvisionedController.DeviceProvisionedListener {
    final /* synthetic */ SetupWizard this$0;

    /* access modifiers changed from: package-private */
    public SetupWizard$provisionedListener$1(SetupWizard setupWizard) {
        this.this$0 = setupWizard;
    }

    @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
    public void onDeviceProvisionedChanged() {
        updateSetupComplete();
    }

    @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
    public void onUserSetupChanged() {
        updateSetupComplete();
    }

    private final void updateSetupComplete() {
        SetupWizard setupWizard = this.this$0;
        setupWizard.setupComplete = setupWizard.isSetupComplete();
        this.this$0.updateBlocking();
    }
}
