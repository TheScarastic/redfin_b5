package com.android.systemui.statusbar.policy;

import android.content.Context;
import com.android.systemui.qs.tiles.UserDetailView;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class UserSwitcherController_UserDetailAdapter_Factory implements Factory<UserSwitcherController.UserDetailAdapter> {
    private final Provider<Context> contextProvider;
    private final Provider<UserDetailView.Adapter> userDetailViewAdapterProvider;

    public UserSwitcherController_UserDetailAdapter_Factory(Provider<Context> provider, Provider<UserDetailView.Adapter> provider2) {
        this.contextProvider = provider;
        this.userDetailViewAdapterProvider = provider2;
    }

    @Override // javax.inject.Provider
    public UserSwitcherController.UserDetailAdapter get() {
        return newInstance(this.contextProvider.get(), this.userDetailViewAdapterProvider);
    }

    public static UserSwitcherController_UserDetailAdapter_Factory create(Provider<Context> provider, Provider<UserDetailView.Adapter> provider2) {
        return new UserSwitcherController_UserDetailAdapter_Factory(provider, provider2);
    }

    public static UserSwitcherController.UserDetailAdapter newInstance(Context context, Provider<UserDetailView.Adapter> provider) {
        return new UserSwitcherController.UserDetailAdapter(context, provider);
    }
}
