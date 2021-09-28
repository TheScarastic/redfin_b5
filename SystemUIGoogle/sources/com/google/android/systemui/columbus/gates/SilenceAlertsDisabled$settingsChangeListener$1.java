package com.google.android.systemui.columbus.gates;

import com.google.android.systemui.columbus.ColumbusSettings;
/* compiled from: SilenceAlertsDisabled.kt */
/* loaded from: classes2.dex */
public final class SilenceAlertsDisabled$settingsChangeListener$1 implements ColumbusSettings.ColumbusSettingsChangeListener {
    final /* synthetic */ SilenceAlertsDisabled this$0;

    /* access modifiers changed from: package-private */
    public SilenceAlertsDisabled$settingsChangeListener$1(SilenceAlertsDisabled silenceAlertsDisabled) {
        this.this$0 = silenceAlertsDisabled;
    }

    @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
    public void onColumbusEnabledChange(boolean z) {
        ColumbusSettings.ColumbusSettingsChangeListener.DefaultImpls.onColumbusEnabledChange(this, z);
    }

    @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
    public void onLowSensitivityChange(boolean z) {
        ColumbusSettings.ColumbusSettingsChangeListener.DefaultImpls.onLowSensitivityChange(this, z);
    }

    @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
    public void onSelectedActionChange(String str) {
        ColumbusSettings.ColumbusSettingsChangeListener.DefaultImpls.onSelectedActionChange(this, str);
    }

    @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
    public void onSelectedAppChange(String str) {
        ColumbusSettings.ColumbusSettingsChangeListener.DefaultImpls.onSelectedAppChange(this, str);
    }

    @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
    public void onSelectedAppShortcutChange(String str) {
        ColumbusSettings.ColumbusSettingsChangeListener.DefaultImpls.onSelectedAppShortcutChange(this, str);
    }

    @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
    public void onUseApSensorChange(boolean z) {
        ColumbusSettings.ColumbusSettingsChangeListener.DefaultImpls.onUseApSensorChange(this, z);
    }

    @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
    public void onAlertSilenceEnabledChange(boolean z) {
        this.this$0.updateSilenceAlertsEnabled(z);
    }
}
