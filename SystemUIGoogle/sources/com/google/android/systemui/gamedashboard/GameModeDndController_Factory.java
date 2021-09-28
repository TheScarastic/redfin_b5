package com.google.android.systemui.gamedashboard;

import android.app.NotificationManager;
import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class GameModeDndController_Factory implements Factory<GameModeDndController> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;
    private final Provider<NotificationManager> notificationManagerProvider;

    public GameModeDndController_Factory(Provider<Context> provider, Provider<NotificationManager> provider2, Provider<BroadcastDispatcher> provider3) {
        this.contextProvider = provider;
        this.notificationManagerProvider = provider2;
        this.broadcastDispatcherProvider = provider3;
    }

    @Override // javax.inject.Provider
    public GameModeDndController get() {
        return newInstance(this.contextProvider.get(), this.notificationManagerProvider.get(), this.broadcastDispatcherProvider.get());
    }

    public static GameModeDndController_Factory create(Provider<Context> provider, Provider<NotificationManager> provider2, Provider<BroadcastDispatcher> provider3) {
        return new GameModeDndController_Factory(provider, provider2, provider3);
    }

    public static GameModeDndController newInstance(Context context, NotificationManager notificationManager, BroadcastDispatcher broadcastDispatcher) {
        return new GameModeDndController(context, notificationManager, broadcastDispatcher);
    }
}
