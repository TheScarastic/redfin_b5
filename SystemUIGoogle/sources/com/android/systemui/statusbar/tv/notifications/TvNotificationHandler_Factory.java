package com.android.systemui.statusbar.tv.notifications;

import android.content.Context;
import com.android.systemui.statusbar.NotificationListener;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TvNotificationHandler_Factory implements Factory<TvNotificationHandler> {
    private final Provider<Context> contextProvider;
    private final Provider<NotificationListener> notificationListenerProvider;

    public TvNotificationHandler_Factory(Provider<Context> provider, Provider<NotificationListener> provider2) {
        this.contextProvider = provider;
        this.notificationListenerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public TvNotificationHandler get() {
        return newInstance(this.contextProvider.get(), this.notificationListenerProvider.get());
    }

    public static TvNotificationHandler_Factory create(Provider<Context> provider, Provider<NotificationListener> provider2) {
        return new TvNotificationHandler_Factory(provider, provider2);
    }

    public static TvNotificationHandler newInstance(Context context, NotificationListener notificationListener) {
        return new TvNotificationHandler(context, notificationListener);
    }
}
