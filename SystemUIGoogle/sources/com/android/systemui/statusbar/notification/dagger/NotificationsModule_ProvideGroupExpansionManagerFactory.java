package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationsModule_ProvideGroupExpansionManagerFactory implements Factory<GroupExpansionManager> {
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<NotificationGroupManagerLegacy> groupManagerLegacyProvider;
    private final Provider<GroupMembershipManager> groupMembershipManagerProvider;

    public NotificationsModule_ProvideGroupExpansionManagerFactory(Provider<FeatureFlags> provider, Provider<GroupMembershipManager> provider2, Provider<NotificationGroupManagerLegacy> provider3) {
        this.featureFlagsProvider = provider;
        this.groupMembershipManagerProvider = provider2;
        this.groupManagerLegacyProvider = provider3;
    }

    @Override // javax.inject.Provider
    public GroupExpansionManager get() {
        return provideGroupExpansionManager(this.featureFlagsProvider.get(), DoubleCheck.lazy(this.groupMembershipManagerProvider), DoubleCheck.lazy(this.groupManagerLegacyProvider));
    }

    public static NotificationsModule_ProvideGroupExpansionManagerFactory create(Provider<FeatureFlags> provider, Provider<GroupMembershipManager> provider2, Provider<NotificationGroupManagerLegacy> provider3) {
        return new NotificationsModule_ProvideGroupExpansionManagerFactory(provider, provider2, provider3);
    }

    public static GroupExpansionManager provideGroupExpansionManager(FeatureFlags featureFlags, Lazy<GroupMembershipManager> lazy, Lazy<NotificationGroupManagerLegacy> lazy2) {
        return (GroupExpansionManager) Preconditions.checkNotNullFromProvides(NotificationsModule.provideGroupExpansionManager(featureFlags, lazy, lazy2));
    }
}
