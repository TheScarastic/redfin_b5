package com.google.android.systemui.columbus.sensors.config;

import android.content.Context;
import com.google.android.systemui.columbus.ColumbusSettings;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: LowSensitivitySettingAdjustment.kt */
/* loaded from: classes2.dex */
public final class LowSensitivitySettingAdjustment extends Adjustment {
    private final SensorConfiguration sensorConfiguration;
    private final LowSensitivitySettingAdjustment$settingsChangeListener$1 settingsChangeListener;
    private boolean useLowSensitivity;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public LowSensitivitySettingAdjustment(Context context, ColumbusSettings columbusSettings, SensorConfiguration sensorConfiguration) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(columbusSettings, "columbusSettings");
        Intrinsics.checkNotNullParameter(sensorConfiguration, "sensorConfiguration");
        this.sensorConfiguration = sensorConfiguration;
        LowSensitivitySettingAdjustment$settingsChangeListener$1 lowSensitivitySettingAdjustment$settingsChangeListener$1 = new ColumbusSettings.ColumbusSettingsChangeListener(this) { // from class: com.google.android.systemui.columbus.sensors.config.LowSensitivitySettingAdjustment$settingsChangeListener$1
            final /* synthetic */ LowSensitivitySettingAdjustment this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
            public void onAlertSilenceEnabledChange(boolean z) {
                ColumbusSettings.ColumbusSettingsChangeListener.DefaultImpls.onAlertSilenceEnabledChange(this, z);
            }

            @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
            public void onColumbusEnabledChange(boolean z) {
                ColumbusSettings.ColumbusSettingsChangeListener.DefaultImpls.onColumbusEnabledChange(this, z);
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
            public void onLowSensitivityChange(boolean z) {
                if (this.this$0.useLowSensitivity != z) {
                    this.this$0.useLowSensitivity = z;
                    this.this$0.onSensitivityChanged();
                }
            }
        };
        this.settingsChangeListener = lowSensitivitySettingAdjustment$settingsChangeListener$1;
        columbusSettings.registerColumbusSettingsChangeListener(lowSensitivitySettingAdjustment$settingsChangeListener$1);
        this.useLowSensitivity = columbusSettings.useLowSensitivity();
        onSensitivityChanged();
    }

    @Override // com.google.android.systemui.columbus.sensors.config.Adjustment
    public float adjustSensitivity(float f) {
        return this.useLowSensitivity ? this.sensorConfiguration.lowSensitivityValue : f;
    }
}
