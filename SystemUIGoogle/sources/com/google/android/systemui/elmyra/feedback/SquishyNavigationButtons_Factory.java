package com.google.android.systemui.elmyra.feedback;

import android.content.Context;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.statusbar.phone.StatusBar;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SquishyNavigationButtons_Factory implements Factory<SquishyNavigationButtons> {
    private final Provider<Context> contextProvider;
    private final Provider<KeyguardViewMediator> keyguardViewMediatorProvider;
    private final Provider<NavigationModeController> navModeControllerProvider;
    private final Provider<StatusBar> statusBarProvider;

    public SquishyNavigationButtons_Factory(Provider<Context> provider, Provider<KeyguardViewMediator> provider2, Provider<StatusBar> provider3, Provider<NavigationModeController> provider4) {
        this.contextProvider = provider;
        this.keyguardViewMediatorProvider = provider2;
        this.statusBarProvider = provider3;
        this.navModeControllerProvider = provider4;
    }

    @Override // javax.inject.Provider
    public SquishyNavigationButtons get() {
        return newInstance(this.contextProvider.get(), this.keyguardViewMediatorProvider.get(), this.statusBarProvider.get(), this.navModeControllerProvider.get());
    }

    public static SquishyNavigationButtons_Factory create(Provider<Context> provider, Provider<KeyguardViewMediator> provider2, Provider<StatusBar> provider3, Provider<NavigationModeController> provider4) {
        return new SquishyNavigationButtons_Factory(provider, provider2, provider3, provider4);
    }

    public static SquishyNavigationButtons newInstance(Context context, KeyguardViewMediator keyguardViewMediator, StatusBar statusBar, NavigationModeController navigationModeController) {
        return new SquishyNavigationButtons(context, keyguardViewMediator, statusBar, navigationModeController);
    }
}
