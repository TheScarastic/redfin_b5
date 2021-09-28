package com.android.systemui.media;

import android.content.Context;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardMediaController_Factory implements Factory<KeyguardMediaController> {
    private final Provider<KeyguardBypassController> bypassControllerProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<MediaHost> mediaHostProvider;
    private final Provider<NotificationLockscreenUserManager> notifLockscreenUserManagerProvider;
    private final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;

    public KeyguardMediaController_Factory(Provider<MediaHost> provider, Provider<KeyguardBypassController> provider2, Provider<SysuiStatusBarStateController> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<FeatureFlags> provider5, Provider<Context> provider6, Provider<ConfigurationController> provider7) {
        this.mediaHostProvider = provider;
        this.bypassControllerProvider = provider2;
        this.statusBarStateControllerProvider = provider3;
        this.notifLockscreenUserManagerProvider = provider4;
        this.featureFlagsProvider = provider5;
        this.contextProvider = provider6;
        this.configurationControllerProvider = provider7;
    }

    @Override // javax.inject.Provider
    public KeyguardMediaController get() {
        return newInstance(this.mediaHostProvider.get(), this.bypassControllerProvider.get(), this.statusBarStateControllerProvider.get(), this.notifLockscreenUserManagerProvider.get(), this.featureFlagsProvider.get(), this.contextProvider.get(), this.configurationControllerProvider.get());
    }

    public static KeyguardMediaController_Factory create(Provider<MediaHost> provider, Provider<KeyguardBypassController> provider2, Provider<SysuiStatusBarStateController> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<FeatureFlags> provider5, Provider<Context> provider6, Provider<ConfigurationController> provider7) {
        return new KeyguardMediaController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static KeyguardMediaController newInstance(MediaHost mediaHost, KeyguardBypassController keyguardBypassController, SysuiStatusBarStateController sysuiStatusBarStateController, NotificationLockscreenUserManager notificationLockscreenUserManager, FeatureFlags featureFlags, Context context, ConfigurationController configurationController) {
        return new KeyguardMediaController(mediaHost, keyguardBypassController, sysuiStatusBarStateController, notificationLockscreenUserManager, featureFlags, context, configurationController);
    }
}
