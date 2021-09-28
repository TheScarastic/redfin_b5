package com.google.android.systemui.elmyra.feedback;

import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.statusbar.phone.StatusBar;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class OpaHomeButton_Factory implements Factory<OpaHomeButton> {
    private final Provider<KeyguardViewMediator> keyguardViewMediatorProvider;
    private final Provider<NavigationModeController> navModeControllerProvider;
    private final Provider<StatusBar> statusBarProvider;

    public OpaHomeButton_Factory(Provider<KeyguardViewMediator> provider, Provider<StatusBar> provider2, Provider<NavigationModeController> provider3) {
        this.keyguardViewMediatorProvider = provider;
        this.statusBarProvider = provider2;
        this.navModeControllerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public OpaHomeButton get() {
        return newInstance(this.keyguardViewMediatorProvider.get(), this.statusBarProvider.get(), this.navModeControllerProvider.get());
    }

    public static OpaHomeButton_Factory create(Provider<KeyguardViewMediator> provider, Provider<StatusBar> provider2, Provider<NavigationModeController> provider3) {
        return new OpaHomeButton_Factory(provider, provider2, provider3);
    }

    public static OpaHomeButton newInstance(KeyguardViewMediator keyguardViewMediator, StatusBar statusBar, NavigationModeController navigationModeController) {
        return new OpaHomeButton(keyguardViewMediator, statusBar, navigationModeController);
    }
}
