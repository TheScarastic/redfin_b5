package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.policy.NetworkController;
import com.android.systemui.statusbar.policy.SecurityController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.CarrierConfigTracker;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarSignalPolicy_Factory implements Factory<StatusBarSignalPolicy> {
    private final Provider<CarrierConfigTracker> carrierConfigTrackerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<StatusBarIconController> iconControllerProvider;
    private final Provider<NetworkController> networkControllerProvider;
    private final Provider<SecurityController> securityControllerProvider;
    private final Provider<TunerService> tunerServiceProvider;

    public StatusBarSignalPolicy_Factory(Provider<Context> provider, Provider<StatusBarIconController> provider2, Provider<CarrierConfigTracker> provider3, Provider<NetworkController> provider4, Provider<SecurityController> provider5, Provider<TunerService> provider6, Provider<FeatureFlags> provider7) {
        this.contextProvider = provider;
        this.iconControllerProvider = provider2;
        this.carrierConfigTrackerProvider = provider3;
        this.networkControllerProvider = provider4;
        this.securityControllerProvider = provider5;
        this.tunerServiceProvider = provider6;
        this.featureFlagsProvider = provider7;
    }

    @Override // javax.inject.Provider
    public StatusBarSignalPolicy get() {
        return newInstance(this.contextProvider.get(), this.iconControllerProvider.get(), this.carrierConfigTrackerProvider.get(), this.networkControllerProvider.get(), this.securityControllerProvider.get(), this.tunerServiceProvider.get(), this.featureFlagsProvider.get());
    }

    public static StatusBarSignalPolicy_Factory create(Provider<Context> provider, Provider<StatusBarIconController> provider2, Provider<CarrierConfigTracker> provider3, Provider<NetworkController> provider4, Provider<SecurityController> provider5, Provider<TunerService> provider6, Provider<FeatureFlags> provider7) {
        return new StatusBarSignalPolicy_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static StatusBarSignalPolicy newInstance(Context context, StatusBarIconController statusBarIconController, CarrierConfigTracker carrierConfigTracker, NetworkController networkController, SecurityController securityController, TunerService tunerService, FeatureFlags featureFlags) {
        return new StatusBarSignalPolicy(context, statusBarIconController, carrierConfigTracker, networkController, securityController, tunerService, featureFlags);
    }
}
