package com.android.systemui.navigationbar;

import android.content.Context;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NavigationModeController_Factory implements Factory<NavigationModeController> {
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    private final Provider<Executor> uiBgExecutorProvider;

    public NavigationModeController_Factory(Provider<Context> provider, Provider<DeviceProvisionedController> provider2, Provider<ConfigurationController> provider3, Provider<Executor> provider4) {
        this.contextProvider = provider;
        this.deviceProvisionedControllerProvider = provider2;
        this.configurationControllerProvider = provider3;
        this.uiBgExecutorProvider = provider4;
    }

    @Override // javax.inject.Provider
    public NavigationModeController get() {
        return newInstance(this.contextProvider.get(), this.deviceProvisionedControllerProvider.get(), this.configurationControllerProvider.get(), this.uiBgExecutorProvider.get());
    }

    public static NavigationModeController_Factory create(Provider<Context> provider, Provider<DeviceProvisionedController> provider2, Provider<ConfigurationController> provider3, Provider<Executor> provider4) {
        return new NavigationModeController_Factory(provider, provider2, provider3, provider4);
    }

    public static NavigationModeController newInstance(Context context, DeviceProvisionedController deviceProvisionedController, ConfigurationController configurationController, Executor executor) {
        return new NavigationModeController(context, deviceProvisionedController, configurationController, executor);
    }
}
