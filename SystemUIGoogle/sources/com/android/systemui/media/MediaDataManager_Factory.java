package com.android.systemui.media;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaDataManager_Factory implements Factory<MediaDataManager> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<Executor> backgroundExecutorProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<SystemClock> clockProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<DelayableExecutor> foregroundExecutorProvider;
    private final Provider<MediaControllerFactory> mediaControllerFactoryProvider;
    private final Provider<MediaDataCombineLatest> mediaDataCombineLatestProvider;
    private final Provider<MediaDataFilter> mediaDataFilterProvider;
    private final Provider<MediaDeviceManager> mediaDeviceManagerProvider;
    private final Provider<MediaResumeListener> mediaResumeListenerProvider;
    private final Provider<MediaSessionBasedFilter> mediaSessionBasedFilterProvider;
    private final Provider<MediaTimeoutListener> mediaTimeoutListenerProvider;
    private final Provider<SmartspaceMediaDataProvider> smartspaceMediaDataProvider;
    private final Provider<TunerService> tunerServiceProvider;

    public MediaDataManager_Factory(Provider<Context> provider, Provider<Executor> provider2, Provider<DelayableExecutor> provider3, Provider<MediaControllerFactory> provider4, Provider<DumpManager> provider5, Provider<BroadcastDispatcher> provider6, Provider<MediaTimeoutListener> provider7, Provider<MediaResumeListener> provider8, Provider<MediaSessionBasedFilter> provider9, Provider<MediaDeviceManager> provider10, Provider<MediaDataCombineLatest> provider11, Provider<MediaDataFilter> provider12, Provider<ActivityStarter> provider13, Provider<SmartspaceMediaDataProvider> provider14, Provider<SystemClock> provider15, Provider<TunerService> provider16) {
        this.contextProvider = provider;
        this.backgroundExecutorProvider = provider2;
        this.foregroundExecutorProvider = provider3;
        this.mediaControllerFactoryProvider = provider4;
        this.dumpManagerProvider = provider5;
        this.broadcastDispatcherProvider = provider6;
        this.mediaTimeoutListenerProvider = provider7;
        this.mediaResumeListenerProvider = provider8;
        this.mediaSessionBasedFilterProvider = provider9;
        this.mediaDeviceManagerProvider = provider10;
        this.mediaDataCombineLatestProvider = provider11;
        this.mediaDataFilterProvider = provider12;
        this.activityStarterProvider = provider13;
        this.smartspaceMediaDataProvider = provider14;
        this.clockProvider = provider15;
        this.tunerServiceProvider = provider16;
    }

    @Override // javax.inject.Provider
    public MediaDataManager get() {
        return newInstance(this.contextProvider.get(), this.backgroundExecutorProvider.get(), this.foregroundExecutorProvider.get(), this.mediaControllerFactoryProvider.get(), this.dumpManagerProvider.get(), this.broadcastDispatcherProvider.get(), this.mediaTimeoutListenerProvider.get(), this.mediaResumeListenerProvider.get(), this.mediaSessionBasedFilterProvider.get(), this.mediaDeviceManagerProvider.get(), this.mediaDataCombineLatestProvider.get(), this.mediaDataFilterProvider.get(), this.activityStarterProvider.get(), this.smartspaceMediaDataProvider.get(), this.clockProvider.get(), this.tunerServiceProvider.get());
    }

    public static MediaDataManager_Factory create(Provider<Context> provider, Provider<Executor> provider2, Provider<DelayableExecutor> provider3, Provider<MediaControllerFactory> provider4, Provider<DumpManager> provider5, Provider<BroadcastDispatcher> provider6, Provider<MediaTimeoutListener> provider7, Provider<MediaResumeListener> provider8, Provider<MediaSessionBasedFilter> provider9, Provider<MediaDeviceManager> provider10, Provider<MediaDataCombineLatest> provider11, Provider<MediaDataFilter> provider12, Provider<ActivityStarter> provider13, Provider<SmartspaceMediaDataProvider> provider14, Provider<SystemClock> provider15, Provider<TunerService> provider16) {
        return new MediaDataManager_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16);
    }

    public static MediaDataManager newInstance(Context context, Executor executor, DelayableExecutor delayableExecutor, MediaControllerFactory mediaControllerFactory, DumpManager dumpManager, BroadcastDispatcher broadcastDispatcher, MediaTimeoutListener mediaTimeoutListener, MediaResumeListener mediaResumeListener, MediaSessionBasedFilter mediaSessionBasedFilter, MediaDeviceManager mediaDeviceManager, MediaDataCombineLatest mediaDataCombineLatest, MediaDataFilter mediaDataFilter, ActivityStarter activityStarter, SmartspaceMediaDataProvider smartspaceMediaDataProvider, SystemClock systemClock, TunerService tunerService) {
        return new MediaDataManager(context, executor, delayableExecutor, mediaControllerFactory, dumpManager, broadcastDispatcher, mediaTimeoutListener, mediaResumeListener, mediaSessionBasedFilter, mediaDeviceManager, mediaDataCombineLatest, mediaDataFilter, activityStarter, smartspaceMediaDataProvider, systemClock, tunerService);
    }
}
