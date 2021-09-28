package com.google.android.systemui.assist;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class OpaEnabledReceiver_Factory implements Factory<OpaEnabledReceiver> {
    private final Provider<Executor> bgExecutorProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;
    private final Provider<Executor> fgExecutorProvider;
    private final Provider<OpaEnabledSettings> opaEnabledSettingsProvider;

    public OpaEnabledReceiver_Factory(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<Executor> provider3, Provider<Executor> provider4, Provider<OpaEnabledSettings> provider5) {
        this.contextProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.fgExecutorProvider = provider3;
        this.bgExecutorProvider = provider4;
        this.opaEnabledSettingsProvider = provider5;
    }

    @Override // javax.inject.Provider
    public OpaEnabledReceiver get() {
        return newInstance(this.contextProvider.get(), this.broadcastDispatcherProvider.get(), this.fgExecutorProvider.get(), this.bgExecutorProvider.get(), this.opaEnabledSettingsProvider.get());
    }

    public static OpaEnabledReceiver_Factory create(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<Executor> provider3, Provider<Executor> provider4, Provider<OpaEnabledSettings> provider5) {
        return new OpaEnabledReceiver_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static OpaEnabledReceiver newInstance(Context context, BroadcastDispatcher broadcastDispatcher, Executor executor, Executor executor2, OpaEnabledSettings opaEnabledSettings) {
        return new OpaEnabledReceiver(context, broadcastDispatcher, executor, executor2, opaEnabledSettings);
    }
}
