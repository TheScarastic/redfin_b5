package com.google.android.systemui.columbus;

import com.google.android.systemui.columbus.ColumbusSettings;
/* compiled from: ColumbusServiceWrapper.kt */
/* loaded from: classes2.dex */
public final class ColumbusServiceWrapper$settingsChangeListener$1 implements ColumbusSettings.ColumbusSettingsChangeListener {
    final /* synthetic */ ColumbusServiceWrapper this$0;

    /* access modifiers changed from: package-private */
    public ColumbusServiceWrapper$settingsChangeListener$1(ColumbusServiceWrapper columbusServiceWrapper) {
        this.this$0 = columbusServiceWrapper;
    }

    @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
    public void onAlertSilenceEnabledChange(boolean z) {
        ColumbusSettings.ColumbusSettingsChangeListener.DefaultImpls.onAlertSilenceEnabledChange(this, z);
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
    public void onColumbusEnabledChange(boolean z) {
        if (z) {
            this.this$0.startService();
        }
    }
}
