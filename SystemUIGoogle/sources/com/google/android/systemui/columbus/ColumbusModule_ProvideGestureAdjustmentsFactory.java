package com.google.android.systemui.columbus;

import com.google.android.systemui.columbus.sensors.config.Adjustment;
import com.google.android.systemui.columbus.sensors.config.LowSensitivitySettingAdjustment;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.List;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ColumbusModule_ProvideGestureAdjustmentsFactory implements Factory<List<Adjustment>> {
    private final Provider<LowSensitivitySettingAdjustment> lowSensitivitySettingAdjustmentProvider;

    public ColumbusModule_ProvideGestureAdjustmentsFactory(Provider<LowSensitivitySettingAdjustment> provider) {
        this.lowSensitivitySettingAdjustmentProvider = provider;
    }

    @Override // javax.inject.Provider
    public List<Adjustment> get() {
        return provideGestureAdjustments(this.lowSensitivitySettingAdjustmentProvider.get());
    }

    public static ColumbusModule_ProvideGestureAdjustmentsFactory create(Provider<LowSensitivitySettingAdjustment> provider) {
        return new ColumbusModule_ProvideGestureAdjustmentsFactory(provider);
    }

    public static List<Adjustment> provideGestureAdjustments(LowSensitivitySettingAdjustment lowSensitivitySettingAdjustment) {
        return (List) Preconditions.checkNotNullFromProvides(ColumbusModule.provideGestureAdjustments(lowSensitivitySettingAdjustment));
    }
}
