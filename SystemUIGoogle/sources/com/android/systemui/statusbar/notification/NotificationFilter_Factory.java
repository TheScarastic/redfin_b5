package com.android.systemui.statusbar.notification;

import com.android.systemui.ForegroundServiceController;
import com.android.systemui.media.MediaFeatureFlag;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationFilter_Factory implements Factory<NotificationFilter> {
    private final Provider<ForegroundServiceController> foregroundServiceControllerProvider;
    private final Provider<NotificationEntryManager.KeyguardEnvironment> keyguardEnvironmentProvider;
    private final Provider<MediaFeatureFlag> mediaFeatureFlagProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<NotificationLockscreenUserManager> userManagerProvider;

    public NotificationFilter_Factory(Provider<StatusBarStateController> provider, Provider<NotificationEntryManager.KeyguardEnvironment> provider2, Provider<ForegroundServiceController> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<MediaFeatureFlag> provider5) {
        this.statusBarStateControllerProvider = provider;
        this.keyguardEnvironmentProvider = provider2;
        this.foregroundServiceControllerProvider = provider3;
        this.userManagerProvider = provider4;
        this.mediaFeatureFlagProvider = provider5;
    }

    @Override // javax.inject.Provider
    public NotificationFilter get() {
        return newInstance(this.statusBarStateControllerProvider.get(), this.keyguardEnvironmentProvider.get(), this.foregroundServiceControllerProvider.get(), this.userManagerProvider.get(), this.mediaFeatureFlagProvider.get());
    }

    public static NotificationFilter_Factory create(Provider<StatusBarStateController> provider, Provider<NotificationEntryManager.KeyguardEnvironment> provider2, Provider<ForegroundServiceController> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<MediaFeatureFlag> provider5) {
        return new NotificationFilter_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static NotificationFilter newInstance(StatusBarStateController statusBarStateController, NotificationEntryManager.KeyguardEnvironment keyguardEnvironment, ForegroundServiceController foregroundServiceController, NotificationLockscreenUserManager notificationLockscreenUserManager, MediaFeatureFlag mediaFeatureFlag) {
        return new NotificationFilter(statusBarStateController, keyguardEnvironment, foregroundServiceController, notificationLockscreenUserManager, mediaFeatureFlag);
    }
}
