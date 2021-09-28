package com.google.android.systemui.elmyra.actions;

import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.os.UserManager;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dependency;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.phone.StatusBar;
import com.google.android.systemui.elmyra.gates.Gate;
import com.google.android.systemui.elmyra.gates.KeyguardDeferredSetup;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import java.util.Collections;
/* loaded from: classes2.dex */
public class SetupWizardAction extends Action {
    private boolean mDeviceInDemoMode;
    private final KeyguardDeferredSetup mKeyguardDeferredSetupGate;
    private final Gate.Listener mKeyguardDeferredSetupListener;
    private final LaunchOpa mLaunchOpa;
    private final SettingsAction mSettingsAction;
    private final String mSettingsPackageName;
    private final StatusBar mStatusBar;
    private boolean mUserCompletedSuw;
    private final KeyguardUpdateMonitorCallback mUserSwitchCallback;

    private SetupWizardAction(Context context, SettingsAction settingsAction, LaunchOpa launchOpa, StatusBar statusBar) {
        super(context, null);
        AnonymousClass1 r0 = new KeyguardUpdateMonitorCallback() { // from class: com.google.android.systemui.elmyra.actions.SetupWizardAction.1
            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onUserSwitching(int i) {
                SetupWizardAction setupWizardAction = SetupWizardAction.this;
                setupWizardAction.mDeviceInDemoMode = UserManager.isDeviceInDemoMode(setupWizardAction.getContext());
                SetupWizardAction.this.notifyListener();
            }
        };
        this.mUserSwitchCallback = r0;
        AnonymousClass2 r1 = new Gate.Listener() { // from class: com.google.android.systemui.elmyra.actions.SetupWizardAction.2
            @Override // com.google.android.systemui.elmyra.gates.Gate.Listener
            public void onGateChanged(Gate gate) {
                SetupWizardAction.this.mUserCompletedSuw = ((KeyguardDeferredSetup) gate).isSuwComplete();
                SetupWizardAction.this.notifyListener();
            }
        };
        this.mKeyguardDeferredSetupListener = r1;
        this.mSettingsPackageName = context.getResources().getString(R$string.settings_app_package_name);
        this.mSettingsAction = settingsAction;
        this.mLaunchOpa = launchOpa;
        this.mStatusBar = statusBar;
        ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).registerCallback(r0);
        KeyguardDeferredSetup keyguardDeferredSetup = new KeyguardDeferredSetup(context, Collections.emptyList());
        this.mKeyguardDeferredSetupGate = keyguardDeferredSetup;
        keyguardDeferredSetup.activate();
        keyguardDeferredSetup.setListener(r1);
        this.mUserCompletedSuw = keyguardDeferredSetup.isSuwComplete();
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public boolean isAvailable() {
        if (!this.mDeviceInDemoMode && this.mLaunchOpa.isAvailable() && !this.mUserCompletedSuw && !this.mSettingsAction.isAvailable()) {
            return true;
        }
        return false;
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public void onProgress(float f, int i) {
        updateFeedbackEffects(f, i);
    }

    /* access modifiers changed from: protected */
    @Override // com.google.android.systemui.elmyra.actions.Action
    public void updateFeedbackEffects(float f, int i) {
        super.updateFeedbackEffects(f, i);
        this.mLaunchOpa.updateFeedbackEffects(f, i);
    }

    /* access modifiers changed from: protected */
    @Override // com.google.android.systemui.elmyra.actions.Action
    public void triggerFeedbackEffects(GestureSensor.DetectionProperties detectionProperties) {
        super.triggerFeedbackEffects(detectionProperties);
        this.mLaunchOpa.triggerFeedbackEffects(detectionProperties);
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        this.mStatusBar.collapseShade();
        triggerFeedbackEffects(detectionProperties);
        if (!this.mUserCompletedSuw && !this.mSettingsAction.isAvailable()) {
            Intent intent = new Intent();
            intent.setAction("com.google.android.settings.ASSIST_GESTURE_TRAINING");
            intent.setPackage(this.mSettingsPackageName);
            intent.setFlags(268468224);
            getContext().startActivityAsUser(intent, UserHandle.of(-2));
        }
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        private final Context mContext;
        private LaunchOpa mLaunchOpa;
        private SettingsAction mSettingsAction;
        private final StatusBar mStatusBar;

        public Builder(Context context, StatusBar statusBar) {
            this.mContext = context;
            this.mStatusBar = statusBar;
        }

        public Builder setSettingsAction(SettingsAction settingsAction) {
            this.mSettingsAction = settingsAction;
            return this;
        }

        public Builder setLaunchOpa(LaunchOpa launchOpa) {
            this.mLaunchOpa = launchOpa;
            return this;
        }

        public SetupWizardAction build() {
            return new SetupWizardAction(this.mContext, this.mSettingsAction, this.mLaunchOpa, this.mStatusBar);
        }
    }
}
