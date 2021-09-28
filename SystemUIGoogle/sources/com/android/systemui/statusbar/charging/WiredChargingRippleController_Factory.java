package com.android.systemui.statusbar.charging;

import android.content.Context;
import android.view.WindowManager;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WiredChargingRippleController_Factory implements Factory<WiredChargingRippleController> {
    private final Provider<BatteryController> batteryControllerProvider;
    private final Provider<CommandRegistry> commandRegistryProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<SystemClock> systemClockProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<WindowManager> windowManagerProvider;

    public WiredChargingRippleController_Factory(Provider<CommandRegistry> provider, Provider<BatteryController> provider2, Provider<ConfigurationController> provider3, Provider<FeatureFlags> provider4, Provider<Context> provider5, Provider<WindowManager> provider6, Provider<SystemClock> provider7, Provider<UiEventLogger> provider8) {
        this.commandRegistryProvider = provider;
        this.batteryControllerProvider = provider2;
        this.configurationControllerProvider = provider3;
        this.featureFlagsProvider = provider4;
        this.contextProvider = provider5;
        this.windowManagerProvider = provider6;
        this.systemClockProvider = provider7;
        this.uiEventLoggerProvider = provider8;
    }

    @Override // javax.inject.Provider
    public WiredChargingRippleController get() {
        return newInstance(this.commandRegistryProvider.get(), this.batteryControllerProvider.get(), this.configurationControllerProvider.get(), this.featureFlagsProvider.get(), this.contextProvider.get(), this.windowManagerProvider.get(), this.systemClockProvider.get(), this.uiEventLoggerProvider.get());
    }

    public static WiredChargingRippleController_Factory create(Provider<CommandRegistry> provider, Provider<BatteryController> provider2, Provider<ConfigurationController> provider3, Provider<FeatureFlags> provider4, Provider<Context> provider5, Provider<WindowManager> provider6, Provider<SystemClock> provider7, Provider<UiEventLogger> provider8) {
        return new WiredChargingRippleController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    public static WiredChargingRippleController newInstance(CommandRegistry commandRegistry, BatteryController batteryController, ConfigurationController configurationController, FeatureFlags featureFlags, Context context, WindowManager windowManager, SystemClock systemClock, UiEventLogger uiEventLogger) {
        return new WiredChargingRippleController(commandRegistry, batteryController, configurationController, featureFlags, context, windowManager, systemClock, uiEventLogger);
    }
}
