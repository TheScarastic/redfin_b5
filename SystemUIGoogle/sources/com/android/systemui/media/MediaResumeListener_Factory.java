package com.android.systemui.media;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.tuner.TunerService;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaResumeListener_Factory implements Factory<MediaResumeListener> {
    private final Provider<Executor> backgroundExecutorProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<ResumeMediaBrowserFactory> mediaBrowserFactoryProvider;
    private final Provider<TunerService> tunerServiceProvider;

    public MediaResumeListener_Factory(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<Executor> provider3, Provider<TunerService> provider4, Provider<ResumeMediaBrowserFactory> provider5, Provider<DumpManager> provider6) {
        this.contextProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.backgroundExecutorProvider = provider3;
        this.tunerServiceProvider = provider4;
        this.mediaBrowserFactoryProvider = provider5;
        this.dumpManagerProvider = provider6;
    }

    @Override // javax.inject.Provider
    public MediaResumeListener get() {
        return newInstance(this.contextProvider.get(), this.broadcastDispatcherProvider.get(), this.backgroundExecutorProvider.get(), this.tunerServiceProvider.get(), this.mediaBrowserFactoryProvider.get(), this.dumpManagerProvider.get());
    }

    public static MediaResumeListener_Factory create(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<Executor> provider3, Provider<TunerService> provider4, Provider<ResumeMediaBrowserFactory> provider5, Provider<DumpManager> provider6) {
        return new MediaResumeListener_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static MediaResumeListener newInstance(Context context, BroadcastDispatcher broadcastDispatcher, Executor executor, TunerService tunerService, ResumeMediaBrowserFactory resumeMediaBrowserFactory, DumpManager dumpManager) {
        return new MediaResumeListener(context, broadcastDispatcher, executor, tunerService, resumeMediaBrowserFactory, dumpManager);
    }
}
