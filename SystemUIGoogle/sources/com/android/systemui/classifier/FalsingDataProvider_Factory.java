package com.android.systemui.classifier;

import android.util.DisplayMetrics;
import com.android.systemui.dock.DockManager;
import com.android.systemui.statusbar.policy.BatteryController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FalsingDataProvider_Factory implements Factory<FalsingDataProvider> {
    private final Provider<BatteryController> batteryControllerProvider;
    private final Provider<DisplayMetrics> displayMetricsProvider;
    private final Provider<DockManager> dockManagerProvider;

    public FalsingDataProvider_Factory(Provider<DisplayMetrics> provider, Provider<BatteryController> provider2, Provider<DockManager> provider3) {
        this.displayMetricsProvider = provider;
        this.batteryControllerProvider = provider2;
        this.dockManagerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public FalsingDataProvider get() {
        return newInstance(this.displayMetricsProvider.get(), this.batteryControllerProvider.get(), this.dockManagerProvider.get());
    }

    public static FalsingDataProvider_Factory create(Provider<DisplayMetrics> provider, Provider<BatteryController> provider2, Provider<DockManager> provider3) {
        return new FalsingDataProvider_Factory(provider, provider2, provider3);
    }

    public static FalsingDataProvider newInstance(DisplayMetrics displayMetrics, BatteryController batteryController, DockManager dockManager) {
        return new FalsingDataProvider(displayMetrics, batteryController, dockManager);
    }
}
