package com.android.systemui;

import android.content.Context;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ForegroundServiceNotificationListener_Factory implements Factory<ForegroundServiceNotificationListener> {
    private final Provider<Context> contextProvider;
    private final Provider<ForegroundServiceController> foregroundServiceControllerProvider;
    private final Provider<NotifPipeline> notifPipelineProvider;
    private final Provider<NotificationEntryManager> notificationEntryManagerProvider;
    private final Provider<SystemClock> systemClockProvider;

    public ForegroundServiceNotificationListener_Factory(Provider<Context> provider, Provider<ForegroundServiceController> provider2, Provider<NotificationEntryManager> provider3, Provider<NotifPipeline> provider4, Provider<SystemClock> provider5) {
        this.contextProvider = provider;
        this.foregroundServiceControllerProvider = provider2;
        this.notificationEntryManagerProvider = provider3;
        this.notifPipelineProvider = provider4;
        this.systemClockProvider = provider5;
    }

    @Override // javax.inject.Provider
    public ForegroundServiceNotificationListener get() {
        return newInstance(this.contextProvider.get(), this.foregroundServiceControllerProvider.get(), this.notificationEntryManagerProvider.get(), this.notifPipelineProvider.get(), this.systemClockProvider.get());
    }

    public static ForegroundServiceNotificationListener_Factory create(Provider<Context> provider, Provider<ForegroundServiceController> provider2, Provider<NotificationEntryManager> provider3, Provider<NotifPipeline> provider4, Provider<SystemClock> provider5) {
        return new ForegroundServiceNotificationListener_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static ForegroundServiceNotificationListener newInstance(Context context, ForegroundServiceController foregroundServiceController, NotificationEntryManager notificationEntryManager, NotifPipeline notifPipeline, SystemClock systemClock) {
        return new ForegroundServiceNotificationListener(context, foregroundServiceController, notificationEntryManager, notifPipeline, systemClock);
    }
}
