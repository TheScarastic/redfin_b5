package com.android.systemui.statusbar.notification.people;

import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PeopleNotificationIdentifierImpl_Factory implements Factory<PeopleNotificationIdentifierImpl> {
    private final Provider<GroupMembershipManager> groupManagerProvider;
    private final Provider<NotificationPersonExtractor> personExtractorProvider;

    public PeopleNotificationIdentifierImpl_Factory(Provider<NotificationPersonExtractor> provider, Provider<GroupMembershipManager> provider2) {
        this.personExtractorProvider = provider;
        this.groupManagerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public PeopleNotificationIdentifierImpl get() {
        return newInstance(this.personExtractorProvider.get(), this.groupManagerProvider.get());
    }

    public static PeopleNotificationIdentifierImpl_Factory create(Provider<NotificationPersonExtractor> provider, Provider<GroupMembershipManager> provider2) {
        return new PeopleNotificationIdentifierImpl_Factory(provider, provider2);
    }

    public static PeopleNotificationIdentifierImpl newInstance(NotificationPersonExtractor notificationPersonExtractor, GroupMembershipManager groupMembershipManager) {
        return new PeopleNotificationIdentifierImpl(notificationPersonExtractor, groupMembershipManager);
    }
}
