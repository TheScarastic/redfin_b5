package com.android.systemui.statusbar.dagger;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import com.android.systemui.statusbar.NotificationListener;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarDependenciesModule_ProvideNotificationListenerFactory implements Factory<NotificationListener> {
    private final Provider<Context> contextProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<NotificationManager> notificationManagerProvider;

    public StatusBarDependenciesModule_ProvideNotificationListenerFactory(Provider<Context> provider, Provider<NotificationManager> provider2, Provider<Handler> provider3) {
        this.contextProvider = provider;
        this.notificationManagerProvider = provider2;
        this.mainHandlerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public NotificationListener get() {
        return provideNotificationListener(this.contextProvider.get(), this.notificationManagerProvider.get(), this.mainHandlerProvider.get());
    }

    public static StatusBarDependenciesModule_ProvideNotificationListenerFactory create(Provider<Context> provider, Provider<NotificationManager> provider2, Provider<Handler> provider3) {
        return new StatusBarDependenciesModule_ProvideNotificationListenerFactory(provider, provider2, provider3);
    }

    public static NotificationListener provideNotificationListener(Context context, NotificationManager notificationManager, Handler handler) {
        return (NotificationListener) Preconditions.checkNotNullFromProvides(StatusBarDependenciesModule.provideNotificationListener(context, notificationManager, handler));
    }
}
