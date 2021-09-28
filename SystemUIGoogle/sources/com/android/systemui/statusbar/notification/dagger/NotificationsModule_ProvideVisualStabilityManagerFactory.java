package com.android.systemui.statusbar.notification.dagger;

import android.os.Handler;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationsModule_ProvideVisualStabilityManagerFactory implements Factory<VisualStabilityManager> {
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<NotificationEntryManager> notificationEntryManagerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public NotificationsModule_ProvideVisualStabilityManagerFactory(Provider<FeatureFlags> provider, Provider<NotificationEntryManager> provider2, Provider<Handler> provider3, Provider<StatusBarStateController> provider4, Provider<WakefulnessLifecycle> provider5) {
        this.featureFlagsProvider = provider;
        this.notificationEntryManagerProvider = provider2;
        this.handlerProvider = provider3;
        this.statusBarStateControllerProvider = provider4;
        this.wakefulnessLifecycleProvider = provider5;
    }

    @Override // javax.inject.Provider
    public VisualStabilityManager get() {
        return provideVisualStabilityManager(this.featureFlagsProvider.get(), this.notificationEntryManagerProvider.get(), this.handlerProvider.get(), this.statusBarStateControllerProvider.get(), this.wakefulnessLifecycleProvider.get());
    }

    public static NotificationsModule_ProvideVisualStabilityManagerFactory create(Provider<FeatureFlags> provider, Provider<NotificationEntryManager> provider2, Provider<Handler> provider3, Provider<StatusBarStateController> provider4, Provider<WakefulnessLifecycle> provider5) {
        return new NotificationsModule_ProvideVisualStabilityManagerFactory(provider, provider2, provider3, provider4, provider5);
    }

    public static VisualStabilityManager provideVisualStabilityManager(FeatureFlags featureFlags, NotificationEntryManager notificationEntryManager, Handler handler, StatusBarStateController statusBarStateController, WakefulnessLifecycle wakefulnessLifecycle) {
        return (VisualStabilityManager) Preconditions.checkNotNullFromProvides(NotificationsModule.provideVisualStabilityManager(featureFlags, notificationEntryManager, handler, statusBarStateController, wakefulnessLifecycle));
    }
}
