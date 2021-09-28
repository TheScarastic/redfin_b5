package com.android.systemui.settings.brightness;

import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.brightness.BrightnessSlider;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class BrightnessSlider_Factory_Factory implements Factory<BrightnessSlider.Factory> {
    private final Provider<FalsingManager> falsingManagerProvider;

    public BrightnessSlider_Factory_Factory(Provider<FalsingManager> provider) {
        this.falsingManagerProvider = provider;
    }

    @Override // javax.inject.Provider
    public BrightnessSlider.Factory get() {
        return newInstance(this.falsingManagerProvider.get());
    }

    public static BrightnessSlider_Factory_Factory create(Provider<FalsingManager> provider) {
        return new BrightnessSlider_Factory_Factory(provider);
    }

    public static BrightnessSlider.Factory newInstance(FalsingManager falsingManager) {
        return new BrightnessSlider.Factory(falsingManager);
    }
}
