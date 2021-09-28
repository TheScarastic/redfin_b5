package com.android.systemui.statusbar.notification.dagger;

import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.ForegroundServiceDismissalFeatureController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationEntryManagerLogger;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinder;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.util.leak.LeakDetector;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationsModule_ProvideNotificationEntryManagerFactory implements Factory<NotificationEntryManager> {
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<ForegroundServiceDismissalFeatureController> fgsFeatureControllerProvider;
    private final Provider<NotificationGroupManagerLegacy> groupManagerProvider;
    private final Provider<LeakDetector> leakDetectorProvider;
    private final Provider<NotificationEntryManagerLogger> loggerProvider;
    private final Provider<NotificationRemoteInputManager> notificationRemoteInputManagerLazyProvider;
    private final Provider<NotificationRowBinder> notificationRowBinderLazyProvider;
    private final Provider<IStatusBarService> statusBarServiceProvider;

    public NotificationsModule_ProvideNotificationEntryManagerFactory(Provider<NotificationEntryManagerLogger> provider, Provider<NotificationGroupManagerLegacy> provider2, Provider<FeatureFlags> provider3, Provider<NotificationRowBinder> provider4, Provider<NotificationRemoteInputManager> provider5, Provider<LeakDetector> provider6, Provider<ForegroundServiceDismissalFeatureController> provider7, Provider<IStatusBarService> provider8) {
        this.loggerProvider = provider;
        this.groupManagerProvider = provider2;
        this.featureFlagsProvider = provider3;
        this.notificationRowBinderLazyProvider = provider4;
        this.notificationRemoteInputManagerLazyProvider = provider5;
        this.leakDetectorProvider = provider6;
        this.fgsFeatureControllerProvider = provider7;
        this.statusBarServiceProvider = provider8;
    }

    @Override // javax.inject.Provider
    public NotificationEntryManager get() {
        return provideNotificationEntryManager(this.loggerProvider.get(), this.groupManagerProvider.get(), this.featureFlagsProvider.get(), DoubleCheck.lazy(this.notificationRowBinderLazyProvider), DoubleCheck.lazy(this.notificationRemoteInputManagerLazyProvider), this.leakDetectorProvider.get(), this.fgsFeatureControllerProvider.get(), this.statusBarServiceProvider.get());
    }

    public static NotificationsModule_ProvideNotificationEntryManagerFactory create(Provider<NotificationEntryManagerLogger> provider, Provider<NotificationGroupManagerLegacy> provider2, Provider<FeatureFlags> provider3, Provider<NotificationRowBinder> provider4, Provider<NotificationRemoteInputManager> provider5, Provider<LeakDetector> provider6, Provider<ForegroundServiceDismissalFeatureController> provider7, Provider<IStatusBarService> provider8) {
        return new NotificationsModule_ProvideNotificationEntryManagerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    public static NotificationEntryManager provideNotificationEntryManager(NotificationEntryManagerLogger notificationEntryManagerLogger, NotificationGroupManagerLegacy notificationGroupManagerLegacy, FeatureFlags featureFlags, Lazy<NotificationRowBinder> lazy, Lazy<NotificationRemoteInputManager> lazy2, LeakDetector leakDetector, ForegroundServiceDismissalFeatureController foregroundServiceDismissalFeatureController, IStatusBarService iStatusBarService) {
        return (NotificationEntryManager) Preconditions.checkNotNullFromProvides(NotificationsModule.provideNotificationEntryManager(notificationEntryManagerLogger, notificationGroupManagerLegacy, featureFlags, lazy, lazy2, leakDetector, foregroundServiceDismissalFeatureController, iStatusBarService));
    }
}
