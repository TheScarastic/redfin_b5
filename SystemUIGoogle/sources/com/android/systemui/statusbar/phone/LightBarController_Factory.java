package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.policy.BatteryController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LightBarController_Factory implements Factory<LightBarController> {
    private final Provider<BatteryController> batteryControllerProvider;
    private final Provider<Context> ctxProvider;
    private final Provider<DarkIconDispatcher> darkIconDispatcherProvider;
    private final Provider<NavigationModeController> navModeControllerProvider;

    public LightBarController_Factory(Provider<Context> provider, Provider<DarkIconDispatcher> provider2, Provider<BatteryController> provider3, Provider<NavigationModeController> provider4) {
        this.ctxProvider = provider;
        this.darkIconDispatcherProvider = provider2;
        this.batteryControllerProvider = provider3;
        this.navModeControllerProvider = provider4;
    }

    @Override // javax.inject.Provider
    public LightBarController get() {
        return newInstance(this.ctxProvider.get(), this.darkIconDispatcherProvider.get(), this.batteryControllerProvider.get(), this.navModeControllerProvider.get());
    }

    public static LightBarController_Factory create(Provider<Context> provider, Provider<DarkIconDispatcher> provider2, Provider<BatteryController> provider3, Provider<NavigationModeController> provider4) {
        return new LightBarController_Factory(provider, provider2, provider3, provider4);
    }

    public static LightBarController newInstance(Context context, DarkIconDispatcher darkIconDispatcher, BatteryController batteryController, NavigationModeController navigationModeController) {
        return new LightBarController(context, darkIconDispatcher, batteryController, navigationModeController);
    }
}
