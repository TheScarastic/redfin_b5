package com.android.systemui.biometrics;

import android.content.Context;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AuthRippleController_Factory implements Factory<AuthRippleController> {
    private final Provider<AuthController> authControllerProvider;
    private final Provider<BiometricUnlockController> biometricUnlockControllerProvider;
    private final Provider<KeyguardBypassController> bypassControllerProvider;
    private final Provider<CommandRegistry> commandRegistryProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    private final Provider<AuthRippleView> rippleViewProvider;
    private final Provider<StatusBar> statusBarProvider;
    private final Provider<Context> sysuiContextProvider;

    public AuthRippleController_Factory(Provider<StatusBar> provider, Provider<Context> provider2, Provider<AuthController> provider3, Provider<ConfigurationController> provider4, Provider<KeyguardUpdateMonitor> provider5, Provider<CommandRegistry> provider6, Provider<NotificationShadeWindowController> provider7, Provider<KeyguardBypassController> provider8, Provider<BiometricUnlockController> provider9, Provider<AuthRippleView> provider10) {
        this.statusBarProvider = provider;
        this.sysuiContextProvider = provider2;
        this.authControllerProvider = provider3;
        this.configurationControllerProvider = provider4;
        this.keyguardUpdateMonitorProvider = provider5;
        this.commandRegistryProvider = provider6;
        this.notificationShadeWindowControllerProvider = provider7;
        this.bypassControllerProvider = provider8;
        this.biometricUnlockControllerProvider = provider9;
        this.rippleViewProvider = provider10;
    }

    @Override // javax.inject.Provider
    public AuthRippleController get() {
        return newInstance(this.statusBarProvider.get(), this.sysuiContextProvider.get(), this.authControllerProvider.get(), this.configurationControllerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.commandRegistryProvider.get(), this.notificationShadeWindowControllerProvider.get(), this.bypassControllerProvider.get(), this.biometricUnlockControllerProvider.get(), this.rippleViewProvider.get());
    }

    public static AuthRippleController_Factory create(Provider<StatusBar> provider, Provider<Context> provider2, Provider<AuthController> provider3, Provider<ConfigurationController> provider4, Provider<KeyguardUpdateMonitor> provider5, Provider<CommandRegistry> provider6, Provider<NotificationShadeWindowController> provider7, Provider<KeyguardBypassController> provider8, Provider<BiometricUnlockController> provider9, Provider<AuthRippleView> provider10) {
        return new AuthRippleController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static AuthRippleController newInstance(StatusBar statusBar, Context context, AuthController authController, ConfigurationController configurationController, KeyguardUpdateMonitor keyguardUpdateMonitor, CommandRegistry commandRegistry, NotificationShadeWindowController notificationShadeWindowController, KeyguardBypassController keyguardBypassController, BiometricUnlockController biometricUnlockController, AuthRippleView authRippleView) {
        return new AuthRippleController(statusBar, context, authController, configurationController, keyguardUpdateMonitor, commandRegistry, notificationShadeWindowController, keyguardBypassController, biometricUnlockController, authRippleView);
    }
}
