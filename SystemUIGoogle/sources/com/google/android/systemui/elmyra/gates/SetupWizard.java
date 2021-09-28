package com.google.android.systemui.elmyra.gates;

import android.content.Context;
import com.android.systemui.Dependency;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.google.android.systemui.elmyra.actions.Action;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class SetupWizard extends Gate {
    private final List<Action> mExceptions;
    private boolean mSetupComplete;
    private final DeviceProvisionedController.DeviceProvisionedListener mProvisionedListener = new DeviceProvisionedController.DeviceProvisionedListener() { // from class: com.google.android.systemui.elmyra.gates.SetupWizard.1
        @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
        public void onDeviceProvisionedChanged() {
            updateSetupComplete();
        }

        @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
        public void onUserSetupChanged() {
            updateSetupComplete();
        }

        private void updateSetupComplete() {
            boolean isSetupComplete = SetupWizard.this.isSetupComplete();
            if (isSetupComplete != SetupWizard.this.mSetupComplete) {
                SetupWizard.this.mSetupComplete = isSetupComplete;
                SetupWizard.this.notifyListener();
            }
        }
    };
    private final DeviceProvisionedController mProvisionedController = (DeviceProvisionedController) Dependency.get(DeviceProvisionedController.class);

    public SetupWizard(Context context, List<Action> list) {
        super(context);
        this.mExceptions = new ArrayList(list);
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected void onActivate() {
        this.mSetupComplete = isSetupComplete();
        this.mProvisionedController.addCallback(this.mProvisionedListener);
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected void onDeactivate() {
        this.mProvisionedController.removeCallback(this.mProvisionedListener);
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected boolean isBlocked() {
        for (int i = 0; i < this.mExceptions.size(); i++) {
            if (this.mExceptions.get(i).isAvailable()) {
                return false;
            }
        }
        return !this.mSetupComplete;
    }

    /* access modifiers changed from: private */
    public boolean isSetupComplete() {
        return this.mProvisionedController.isDeviceProvisioned() && this.mProvisionedController.isCurrentUserSetup();
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public String toString() {
        return super.toString() + " [isDeviceProvisioned -> " + this.mProvisionedController.isDeviceProvisioned() + "; isCurrentUserSetup -> " + this.mProvisionedController.isCurrentUserSetup() + "]";
    }
}
