package com.android.systemui.statusbar.phone;

import android.content.res.Resources;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.PowerManager;
import com.android.systemui.doze.AlwaysOnDisplayPolicy;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.tuner.TunerService;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeParameters_Factory implements Factory<DozeParameters> {
    private final Provider<AlwaysOnDisplayPolicy> alwaysOnDisplayPolicyProvider;
    private final Provider<AmbientDisplayConfiguration> ambientDisplayConfigurationProvider;
    private final Provider<BatteryController> batteryControllerProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<PowerManager> powerManagerProvider;
    private final Provider<Resources> resourcesProvider;
    private final Provider<TunerService> tunerServiceProvider;
    private final Provider<UnlockedScreenOffAnimationController> unlockedScreenOffAnimationControllerProvider;

    public DozeParameters_Factory(Provider<Resources> provider, Provider<AmbientDisplayConfiguration> provider2, Provider<AlwaysOnDisplayPolicy> provider3, Provider<PowerManager> provider4, Provider<BatteryController> provider5, Provider<TunerService> provider6, Provider<DumpManager> provider7, Provider<FeatureFlags> provider8, Provider<UnlockedScreenOffAnimationController> provider9) {
        this.resourcesProvider = provider;
        this.ambientDisplayConfigurationProvider = provider2;
        this.alwaysOnDisplayPolicyProvider = provider3;
        this.powerManagerProvider = provider4;
        this.batteryControllerProvider = provider5;
        this.tunerServiceProvider = provider6;
        this.dumpManagerProvider = provider7;
        this.featureFlagsProvider = provider8;
        this.unlockedScreenOffAnimationControllerProvider = provider9;
    }

    @Override // javax.inject.Provider
    public DozeParameters get() {
        return newInstance(this.resourcesProvider.get(), this.ambientDisplayConfigurationProvider.get(), this.alwaysOnDisplayPolicyProvider.get(), this.powerManagerProvider.get(), this.batteryControllerProvider.get(), this.tunerServiceProvider.get(), this.dumpManagerProvider.get(), this.featureFlagsProvider.get(), this.unlockedScreenOffAnimationControllerProvider.get());
    }

    public static DozeParameters_Factory create(Provider<Resources> provider, Provider<AmbientDisplayConfiguration> provider2, Provider<AlwaysOnDisplayPolicy> provider3, Provider<PowerManager> provider4, Provider<BatteryController> provider5, Provider<TunerService> provider6, Provider<DumpManager> provider7, Provider<FeatureFlags> provider8, Provider<UnlockedScreenOffAnimationController> provider9) {
        return new DozeParameters_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static DozeParameters newInstance(Resources resources, AmbientDisplayConfiguration ambientDisplayConfiguration, AlwaysOnDisplayPolicy alwaysOnDisplayPolicy, PowerManager powerManager, BatteryController batteryController, TunerService tunerService, DumpManager dumpManager, FeatureFlags featureFlags, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController) {
        return new DozeParameters(resources, ambientDisplayConfiguration, alwaysOnDisplayPolicy, powerManager, batteryController, tunerService, dumpManager, featureFlags, unlockedScreenOffAnimationController);
    }
}
