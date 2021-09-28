package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ConversationCoordinator_Factory implements Factory<ConversationCoordinator> {
    private final Provider<NodeController> peopleHeaderControllerProvider;
    private final Provider<PeopleNotificationIdentifier> peopleNotificationIdentifierProvider;

    public ConversationCoordinator_Factory(Provider<PeopleNotificationIdentifier> provider, Provider<NodeController> provider2) {
        this.peopleNotificationIdentifierProvider = provider;
        this.peopleHeaderControllerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public ConversationCoordinator get() {
        return newInstance(this.peopleNotificationIdentifierProvider.get(), this.peopleHeaderControllerProvider.get());
    }

    public static ConversationCoordinator_Factory create(Provider<PeopleNotificationIdentifier> provider, Provider<NodeController> provider2) {
        return new ConversationCoordinator_Factory(provider, provider2);
    }

    public static ConversationCoordinator newInstance(PeopleNotificationIdentifier peopleNotificationIdentifier, NodeController nodeController) {
        return new ConversationCoordinator(peopleNotificationIdentifier, nodeController);
    }
}
