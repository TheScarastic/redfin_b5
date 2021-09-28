package com.android.systemui.statusbar.notification.collection.provider;

import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class HighPriorityProvider_Factory implements Factory<HighPriorityProvider> {
    private final Provider<GroupMembershipManager> groupManagerProvider;
    private final Provider<PeopleNotificationIdentifier> peopleNotificationIdentifierProvider;

    public HighPriorityProvider_Factory(Provider<PeopleNotificationIdentifier> provider, Provider<GroupMembershipManager> provider2) {
        this.peopleNotificationIdentifierProvider = provider;
        this.groupManagerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public HighPriorityProvider get() {
        return newInstance(this.peopleNotificationIdentifierProvider.get(), this.groupManagerProvider.get());
    }

    public static HighPriorityProvider_Factory create(Provider<PeopleNotificationIdentifier> provider, Provider<GroupMembershipManager> provider2) {
        return new HighPriorityProvider_Factory(provider, provider2);
    }

    public static HighPriorityProvider newInstance(PeopleNotificationIdentifier peopleNotificationIdentifier, GroupMembershipManager groupMembershipManager) {
        return new HighPriorityProvider(peopleNotificationIdentifier, groupMembershipManager);
    }
}
