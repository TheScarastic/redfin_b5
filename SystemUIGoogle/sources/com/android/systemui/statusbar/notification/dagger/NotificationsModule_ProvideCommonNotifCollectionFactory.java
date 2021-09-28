package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationsModule_ProvideCommonNotifCollectionFactory implements Factory<CommonNotifCollection> {
    private final Provider<NotificationEntryManager> entryManagerProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<NotifPipeline> pipelineProvider;

    public NotificationsModule_ProvideCommonNotifCollectionFactory(Provider<FeatureFlags> provider, Provider<NotifPipeline> provider2, Provider<NotificationEntryManager> provider3) {
        this.featureFlagsProvider = provider;
        this.pipelineProvider = provider2;
        this.entryManagerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public CommonNotifCollection get() {
        return provideCommonNotifCollection(this.featureFlagsProvider.get(), DoubleCheck.lazy(this.pipelineProvider), this.entryManagerProvider.get());
    }

    public static NotificationsModule_ProvideCommonNotifCollectionFactory create(Provider<FeatureFlags> provider, Provider<NotifPipeline> provider2, Provider<NotificationEntryManager> provider3) {
        return new NotificationsModule_ProvideCommonNotifCollectionFactory(provider, provider2, provider3);
    }

    public static CommonNotifCollection provideCommonNotifCollection(FeatureFlags featureFlags, Lazy<NotifPipeline> lazy, NotificationEntryManager notificationEntryManager) {
        return (CommonNotifCollection) Preconditions.checkNotNullFromProvides(NotificationsModule.provideCommonNotifCollection(featureFlags, lazy, notificationEntryManager));
    }
}
