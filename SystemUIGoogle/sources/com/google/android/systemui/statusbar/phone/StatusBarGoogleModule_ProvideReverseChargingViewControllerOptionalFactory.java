package com.google.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.policy.BatteryController;
import com.google.android.systemui.reversecharging.ReverseChargingViewController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class StatusBarGoogleModule_ProvideReverseChargingViewControllerOptionalFactory implements Factory<Optional<ReverseChargingViewController>> {
    private final Provider<BatteryController> batteryControllerProvider;
    private final Provider<ReverseChargingViewController> reverseChargingViewControllerLazyProvider;

    public StatusBarGoogleModule_ProvideReverseChargingViewControllerOptionalFactory(Provider<BatteryController> provider, Provider<ReverseChargingViewController> provider2) {
        this.batteryControllerProvider = provider;
        this.reverseChargingViewControllerLazyProvider = provider2;
    }

    @Override // javax.inject.Provider
    public Optional<ReverseChargingViewController> get() {
        return provideReverseChargingViewControllerOptional(this.batteryControllerProvider.get(), DoubleCheck.lazy(this.reverseChargingViewControllerLazyProvider));
    }

    public static StatusBarGoogleModule_ProvideReverseChargingViewControllerOptionalFactory create(Provider<BatteryController> provider, Provider<ReverseChargingViewController> provider2) {
        return new StatusBarGoogleModule_ProvideReverseChargingViewControllerOptionalFactory(provider, provider2);
    }

    public static Optional<ReverseChargingViewController> provideReverseChargingViewControllerOptional(BatteryController batteryController, Lazy<ReverseChargingViewController> lazy) {
        return (Optional) Preconditions.checkNotNullFromProvides(StatusBarGoogleModule.provideReverseChargingViewControllerOptional(batteryController, lazy));
    }
}
