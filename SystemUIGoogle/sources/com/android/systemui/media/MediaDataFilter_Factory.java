package com.android.systemui.media;

import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaDataFilter_Factory implements Factory<MediaDataFilter> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Executor> executorProvider;
    private final Provider<NotificationLockscreenUserManager> lockscreenUserManagerProvider;
    private final Provider<MediaResumeListener> mediaResumeListenerProvider;
    private final Provider<SystemClock> systemClockProvider;

    public MediaDataFilter_Factory(Provider<BroadcastDispatcher> provider, Provider<MediaResumeListener> provider2, Provider<NotificationLockscreenUserManager> provider3, Provider<Executor> provider4, Provider<SystemClock> provider5) {
        this.broadcastDispatcherProvider = provider;
        this.mediaResumeListenerProvider = provider2;
        this.lockscreenUserManagerProvider = provider3;
        this.executorProvider = provider4;
        this.systemClockProvider = provider5;
    }

    @Override // javax.inject.Provider
    public MediaDataFilter get() {
        return newInstance(this.broadcastDispatcherProvider.get(), this.mediaResumeListenerProvider.get(), this.lockscreenUserManagerProvider.get(), this.executorProvider.get(), this.systemClockProvider.get());
    }

    public static MediaDataFilter_Factory create(Provider<BroadcastDispatcher> provider, Provider<MediaResumeListener> provider2, Provider<NotificationLockscreenUserManager> provider3, Provider<Executor> provider4, Provider<SystemClock> provider5) {
        return new MediaDataFilter_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static MediaDataFilter newInstance(BroadcastDispatcher broadcastDispatcher, MediaResumeListener mediaResumeListener, NotificationLockscreenUserManager notificationLockscreenUserManager, Executor executor, SystemClock systemClock) {
        return new MediaDataFilter(broadcastDispatcher, mediaResumeListener, notificationLockscreenUserManager, executor, systemClock);
    }
}
