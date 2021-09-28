package com.android.systemui.qs.tiles;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tiles.UserDetailView;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class UserDetailView_Adapter_Factory implements Factory<UserDetailView.Adapter> {
    private final Provider<Context> contextProvider;
    private final Provider<UserSwitcherController> controllerProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public UserDetailView_Adapter_Factory(Provider<Context> provider, Provider<UserSwitcherController> provider2, Provider<UiEventLogger> provider3, Provider<FalsingManager> provider4) {
        this.contextProvider = provider;
        this.controllerProvider = provider2;
        this.uiEventLoggerProvider = provider3;
        this.falsingManagerProvider = provider4;
    }

    @Override // javax.inject.Provider
    public UserDetailView.Adapter get() {
        return newInstance(this.contextProvider.get(), this.controllerProvider.get(), this.uiEventLoggerProvider.get(), this.falsingManagerProvider.get());
    }

    public static UserDetailView_Adapter_Factory create(Provider<Context> provider, Provider<UserSwitcherController> provider2, Provider<UiEventLogger> provider3, Provider<FalsingManager> provider4) {
        return new UserDetailView_Adapter_Factory(provider, provider2, provider3, provider4);
    }

    public static UserDetailView.Adapter newInstance(Context context, UserSwitcherController userSwitcherController, UiEventLogger uiEventLogger, FalsingManager falsingManager) {
        return new UserDetailView.Adapter(context, userSwitcherController, uiEventLogger, falsingManager);
    }
}
