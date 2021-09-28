package com.android.systemui.media;

import android.content.Context;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaHierarchyManager_Factory implements Factory<MediaHierarchyManager> {
    private final Provider<KeyguardBypassController> bypassControllerProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<MediaCarouselController> mediaCarouselControllerProvider;
    private final Provider<NotificationLockscreenUserManager> notifLockscreenUserManagerProvider;
    private final Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
    private final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;
    private final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public MediaHierarchyManager_Factory(Provider<Context> provider, Provider<SysuiStatusBarStateController> provider2, Provider<KeyguardStateController> provider3, Provider<KeyguardBypassController> provider4, Provider<MediaCarouselController> provider5, Provider<NotificationLockscreenUserManager> provider6, Provider<ConfigurationController> provider7, Provider<WakefulnessLifecycle> provider8, Provider<StatusBarKeyguardViewManager> provider9) {
        this.contextProvider = provider;
        this.statusBarStateControllerProvider = provider2;
        this.keyguardStateControllerProvider = provider3;
        this.bypassControllerProvider = provider4;
        this.mediaCarouselControllerProvider = provider5;
        this.notifLockscreenUserManagerProvider = provider6;
        this.configurationControllerProvider = provider7;
        this.wakefulnessLifecycleProvider = provider8;
        this.statusBarKeyguardViewManagerProvider = provider9;
    }

    @Override // javax.inject.Provider
    public MediaHierarchyManager get() {
        return newInstance(this.contextProvider.get(), this.statusBarStateControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.bypassControllerProvider.get(), this.mediaCarouselControllerProvider.get(), this.notifLockscreenUserManagerProvider.get(), this.configurationControllerProvider.get(), this.wakefulnessLifecycleProvider.get(), this.statusBarKeyguardViewManagerProvider.get());
    }

    public static MediaHierarchyManager_Factory create(Provider<Context> provider, Provider<SysuiStatusBarStateController> provider2, Provider<KeyguardStateController> provider3, Provider<KeyguardBypassController> provider4, Provider<MediaCarouselController> provider5, Provider<NotificationLockscreenUserManager> provider6, Provider<ConfigurationController> provider7, Provider<WakefulnessLifecycle> provider8, Provider<StatusBarKeyguardViewManager> provider9) {
        return new MediaHierarchyManager_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static MediaHierarchyManager newInstance(Context context, SysuiStatusBarStateController sysuiStatusBarStateController, KeyguardStateController keyguardStateController, KeyguardBypassController keyguardBypassController, MediaCarouselController mediaCarouselController, NotificationLockscreenUserManager notificationLockscreenUserManager, ConfigurationController configurationController, WakefulnessLifecycle wakefulnessLifecycle, StatusBarKeyguardViewManager statusBarKeyguardViewManager) {
        return new MediaHierarchyManager(context, sysuiStatusBarStateController, keyguardStateController, keyguardBypassController, mediaCarouselController, notificationLockscreenUserManager, configurationController, wakefulnessLifecycle, statusBarKeyguardViewManager);
    }
}
