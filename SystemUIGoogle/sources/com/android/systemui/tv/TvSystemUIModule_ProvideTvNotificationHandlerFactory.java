package com.android.systemui.tv;

import android.content.Context;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.tv.notifications.TvNotificationHandler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class TvSystemUIModule_ProvideTvNotificationHandlerFactory implements Factory<TvNotificationHandler> {
    private final Provider<Context> contextProvider;
    private final Provider<NotificationListener> notificationListenerProvider;

    public TvSystemUIModule_ProvideTvNotificationHandlerFactory(Provider<Context> provider, Provider<NotificationListener> provider2) {
        this.contextProvider = provider;
        this.notificationListenerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public TvNotificationHandler get() {
        return provideTvNotificationHandler(this.contextProvider.get(), this.notificationListenerProvider.get());
    }

    public static TvSystemUIModule_ProvideTvNotificationHandlerFactory create(Provider<Context> provider, Provider<NotificationListener> provider2) {
        return new TvSystemUIModule_ProvideTvNotificationHandlerFactory(provider, provider2);
    }

    public static TvNotificationHandler provideTvNotificationHandler(Context context, NotificationListener notificationListener) {
        return (TvNotificationHandler) Preconditions.checkNotNullFromProvides(TvSystemUIModule.provideTvNotificationHandler(context, notificationListener));
    }
}
