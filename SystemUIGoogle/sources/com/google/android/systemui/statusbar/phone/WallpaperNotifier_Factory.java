package com.google.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WallpaperNotifier_Factory implements Factory<WallpaperNotifier> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;
    private final Provider<NotificationEntryManager> entryManagerProvider;

    public WallpaperNotifier_Factory(Provider<Context> provider, Provider<NotificationEntryManager> provider2, Provider<BroadcastDispatcher> provider3) {
        this.contextProvider = provider;
        this.entryManagerProvider = provider2;
        this.broadcastDispatcherProvider = provider3;
    }

    @Override // javax.inject.Provider
    public WallpaperNotifier get() {
        return newInstance(this.contextProvider.get(), this.entryManagerProvider.get(), this.broadcastDispatcherProvider.get());
    }

    public static WallpaperNotifier_Factory create(Provider<Context> provider, Provider<NotificationEntryManager> provider2, Provider<BroadcastDispatcher> provider3) {
        return new WallpaperNotifier_Factory(provider, provider2, provider3);
    }

    public static WallpaperNotifier newInstance(Context context, NotificationEntryManager notificationEntryManager, BroadcastDispatcher broadcastDispatcher) {
        return new WallpaperNotifier(context, notificationEntryManager, broadcastDispatcher);
    }
}
