package com.android.systemui.statusbar.notification.collection.legacy;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import com.android.wm.shell.bubbles.Bubbles;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationGroupManagerLegacy_Factory implements Factory<NotificationGroupManagerLegacy> {
    private final Provider<Optional<Bubbles>> bubblesOptionalProvider;
    private final Provider<PeopleNotificationIdentifier> peopleNotificationIdentifierProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public NotificationGroupManagerLegacy_Factory(Provider<StatusBarStateController> provider, Provider<PeopleNotificationIdentifier> provider2, Provider<Optional<Bubbles>> provider3) {
        this.statusBarStateControllerProvider = provider;
        this.peopleNotificationIdentifierProvider = provider2;
        this.bubblesOptionalProvider = provider3;
    }

    @Override // javax.inject.Provider
    public NotificationGroupManagerLegacy get() {
        return newInstance(this.statusBarStateControllerProvider.get(), DoubleCheck.lazy(this.peopleNotificationIdentifierProvider), this.bubblesOptionalProvider.get());
    }

    public static NotificationGroupManagerLegacy_Factory create(Provider<StatusBarStateController> provider, Provider<PeopleNotificationIdentifier> provider2, Provider<Optional<Bubbles>> provider3) {
        return new NotificationGroupManagerLegacy_Factory(provider, provider2, provider3);
    }

    public static NotificationGroupManagerLegacy newInstance(StatusBarStateController statusBarStateController, Lazy<PeopleNotificationIdentifier> lazy, Optional<Bubbles> optional) {
        return new NotificationGroupManagerLegacy(statusBarStateController, lazy, optional);
    }
}
