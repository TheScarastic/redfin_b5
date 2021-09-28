package com.google.android.systemui.gamedashboard;

import android.content.Context;
import android.view.WindowManager;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ToastController_Factory implements Factory<ToastController> {
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<NavigationModeController> navigationModeControllerProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<WindowManager> windowManagerProvider;

    public ToastController_Factory(Provider<Context> provider, Provider<ConfigurationController> provider2, Provider<WindowManager> provider3, Provider<UiEventLogger> provider4, Provider<NavigationModeController> provider5) {
        this.contextProvider = provider;
        this.configurationControllerProvider = provider2;
        this.windowManagerProvider = provider3;
        this.uiEventLoggerProvider = provider4;
        this.navigationModeControllerProvider = provider5;
    }

    @Override // javax.inject.Provider
    public ToastController get() {
        return newInstance(this.contextProvider.get(), this.configurationControllerProvider.get(), this.windowManagerProvider.get(), this.uiEventLoggerProvider.get(), this.navigationModeControllerProvider.get());
    }

    public static ToastController_Factory create(Provider<Context> provider, Provider<ConfigurationController> provider2, Provider<WindowManager> provider3, Provider<UiEventLogger> provider4, Provider<NavigationModeController> provider5) {
        return new ToastController_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static ToastController newInstance(Context context, ConfigurationController configurationController, WindowManager windowManager, UiEventLogger uiEventLogger, NavigationModeController navigationModeController) {
        return new ToastController(context, configurationController, windowManager, uiEventLogger, navigationModeController);
    }
}
