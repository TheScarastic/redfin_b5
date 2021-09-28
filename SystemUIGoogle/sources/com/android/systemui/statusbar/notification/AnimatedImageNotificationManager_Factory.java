package com.android.systemui.statusbar.notification;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AnimatedImageNotificationManager_Factory implements Factory<AnimatedImageNotificationManager> {
    private final Provider<HeadsUpManager> headsUpManagerProvider;
    private final Provider<NotificationEntryManager> notificationEntryManagerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public AnimatedImageNotificationManager_Factory(Provider<NotificationEntryManager> provider, Provider<HeadsUpManager> provider2, Provider<StatusBarStateController> provider3) {
        this.notificationEntryManagerProvider = provider;
        this.headsUpManagerProvider = provider2;
        this.statusBarStateControllerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public AnimatedImageNotificationManager get() {
        return newInstance(this.notificationEntryManagerProvider.get(), this.headsUpManagerProvider.get(), this.statusBarStateControllerProvider.get());
    }

    public static AnimatedImageNotificationManager_Factory create(Provider<NotificationEntryManager> provider, Provider<HeadsUpManager> provider2, Provider<StatusBarStateController> provider3) {
        return new AnimatedImageNotificationManager_Factory(provider, provider2, provider3);
    }

    public static AnimatedImageNotificationManager newInstance(NotificationEntryManager notificationEntryManager, HeadsUpManager headsUpManager, StatusBarStateController statusBarStateController) {
        return new AnimatedImageNotificationManager(notificationEntryManager, headsUpManager, statusBarStateController);
    }
}
