package com.android.systemui.statusbar.notification.init;

import com.android.systemui.statusbar.NotificationListener;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationsControllerStub_Factory implements Factory<NotificationsControllerStub> {
    private final Provider<NotificationListener> notificationListenerProvider;

    public NotificationsControllerStub_Factory(Provider<NotificationListener> provider) {
        this.notificationListenerProvider = provider;
    }

    @Override // javax.inject.Provider
    public NotificationsControllerStub get() {
        return newInstance(this.notificationListenerProvider.get());
    }

    public static NotificationsControllerStub_Factory create(Provider<NotificationListener> provider) {
        return new NotificationsControllerStub_Factory(provider);
    }

    public static NotificationsControllerStub newInstance(NotificationListener notificationListener) {
        return new NotificationsControllerStub(notificationListener);
    }
}
