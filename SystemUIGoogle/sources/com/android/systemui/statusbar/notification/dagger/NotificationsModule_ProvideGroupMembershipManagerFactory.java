package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationsModule_ProvideGroupMembershipManagerFactory implements Factory<GroupMembershipManager> {
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<NotificationGroupManagerLegacy> groupManagerLegacyProvider;

    public NotificationsModule_ProvideGroupMembershipManagerFactory(Provider<FeatureFlags> provider, Provider<NotificationGroupManagerLegacy> provider2) {
        this.featureFlagsProvider = provider;
        this.groupManagerLegacyProvider = provider2;
    }

    @Override // javax.inject.Provider
    public GroupMembershipManager get() {
        return provideGroupMembershipManager(this.featureFlagsProvider.get(), DoubleCheck.lazy(this.groupManagerLegacyProvider));
    }

    public static NotificationsModule_ProvideGroupMembershipManagerFactory create(Provider<FeatureFlags> provider, Provider<NotificationGroupManagerLegacy> provider2) {
        return new NotificationsModule_ProvideGroupMembershipManagerFactory(provider, provider2);
    }

    public static GroupMembershipManager provideGroupMembershipManager(FeatureFlags featureFlags, Lazy<NotificationGroupManagerLegacy> lazy) {
        return (GroupMembershipManager) Preconditions.checkNotNullFromProvides(NotificationsModule.provideGroupMembershipManager(featureFlags, lazy));
    }
}
