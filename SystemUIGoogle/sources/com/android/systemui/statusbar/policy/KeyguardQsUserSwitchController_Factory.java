package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.res.Resources;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tiles.UserDetailView;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.phone.UserAvatarView;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardQsUserSwitchController_Factory implements Factory<KeyguardQsUserSwitchController> {
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DozeParameters> dozeParametersProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<Resources> resourcesProvider;
    private final Provider<ScreenLifecycle> screenLifecycleProvider;
    private final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;
    private final Provider<UnlockedScreenOffAnimationController> unlockedScreenOffAnimationControllerProvider;
    private final Provider<UserDetailView.Adapter> userDetailViewAdapterProvider;
    private final Provider<UserSwitcherController> userSwitcherControllerProvider;
    private final Provider<UserAvatarView> viewProvider;

    public KeyguardQsUserSwitchController_Factory(Provider<UserAvatarView> provider, Provider<Context> provider2, Provider<Resources> provider3, Provider<ScreenLifecycle> provider4, Provider<UserSwitcherController> provider5, Provider<KeyguardStateController> provider6, Provider<FalsingManager> provider7, Provider<ConfigurationController> provider8, Provider<SysuiStatusBarStateController> provider9, Provider<DozeParameters> provider10, Provider<UserDetailView.Adapter> provider11, Provider<UnlockedScreenOffAnimationController> provider12) {
        this.viewProvider = provider;
        this.contextProvider = provider2;
        this.resourcesProvider = provider3;
        this.screenLifecycleProvider = provider4;
        this.userSwitcherControllerProvider = provider5;
        this.keyguardStateControllerProvider = provider6;
        this.falsingManagerProvider = provider7;
        this.configurationControllerProvider = provider8;
        this.statusBarStateControllerProvider = provider9;
        this.dozeParametersProvider = provider10;
        this.userDetailViewAdapterProvider = provider11;
        this.unlockedScreenOffAnimationControllerProvider = provider12;
    }

    @Override // javax.inject.Provider
    public KeyguardQsUserSwitchController get() {
        return newInstance(this.viewProvider.get(), this.contextProvider.get(), this.resourcesProvider.get(), this.screenLifecycleProvider.get(), this.userSwitcherControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.falsingManagerProvider.get(), this.configurationControllerProvider.get(), this.statusBarStateControllerProvider.get(), this.dozeParametersProvider.get(), this.userDetailViewAdapterProvider, this.unlockedScreenOffAnimationControllerProvider.get());
    }

    public static KeyguardQsUserSwitchController_Factory create(Provider<UserAvatarView> provider, Provider<Context> provider2, Provider<Resources> provider3, Provider<ScreenLifecycle> provider4, Provider<UserSwitcherController> provider5, Provider<KeyguardStateController> provider6, Provider<FalsingManager> provider7, Provider<ConfigurationController> provider8, Provider<SysuiStatusBarStateController> provider9, Provider<DozeParameters> provider10, Provider<UserDetailView.Adapter> provider11, Provider<UnlockedScreenOffAnimationController> provider12) {
        return new KeyguardQsUserSwitchController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12);
    }

    public static KeyguardQsUserSwitchController newInstance(UserAvatarView userAvatarView, Context context, Resources resources, ScreenLifecycle screenLifecycle, UserSwitcherController userSwitcherController, KeyguardStateController keyguardStateController, FalsingManager falsingManager, ConfigurationController configurationController, SysuiStatusBarStateController sysuiStatusBarStateController, DozeParameters dozeParameters, Provider<UserDetailView.Adapter> provider, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController) {
        return new KeyguardQsUserSwitchController(userAvatarView, context, resources, screenLifecycle, userSwitcherController, keyguardStateController, falsingManager, configurationController, sysuiStatusBarStateController, dozeParameters, provider, unlockedScreenOffAnimationController);
    }
}
