package com.android.systemui.globalactions;

import android.content.Context;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.plugins.ActivityStarter;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class GlobalActionsInfoProvider_Factory implements Factory<GlobalActionsInfoProvider> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<Context> contextProvider;
    private final Provider<ControlsController> controlsControllerProvider;
    private final Provider<QuickAccessWalletClient> walletClientProvider;

    public GlobalActionsInfoProvider_Factory(Provider<Context> provider, Provider<QuickAccessWalletClient> provider2, Provider<ControlsController> provider3, Provider<ActivityStarter> provider4) {
        this.contextProvider = provider;
        this.walletClientProvider = provider2;
        this.controlsControllerProvider = provider3;
        this.activityStarterProvider = provider4;
    }

    @Override // javax.inject.Provider
    public GlobalActionsInfoProvider get() {
        return newInstance(this.contextProvider.get(), this.walletClientProvider.get(), this.controlsControllerProvider.get(), this.activityStarterProvider.get());
    }

    public static GlobalActionsInfoProvider_Factory create(Provider<Context> provider, Provider<QuickAccessWalletClient> provider2, Provider<ControlsController> provider3, Provider<ActivityStarter> provider4) {
        return new GlobalActionsInfoProvider_Factory(provider, provider2, provider3, provider4);
    }

    public static GlobalActionsInfoProvider newInstance(Context context, QuickAccessWalletClient quickAccessWalletClient, ControlsController controlsController, ActivityStarter activityStarter) {
        return new GlobalActionsInfoProvider(context, quickAccessWalletClient, controlsController, activityStarter);
    }
}
