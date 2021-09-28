package com.android.systemui.dagger;

import android.content.Context;
import android.os.Looper;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserTracker;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvidesBroadcastDispatcherFactory implements Factory<BroadcastDispatcher> {
    private final Provider<Executor> backgroundExecutorProvider;
    private final Provider<Looper> backgroundLooperProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<BroadcastDispatcherLogger> loggerProvider;
    private final DependencyProvider module;
    private final Provider<UserTracker> userTrackerProvider;

    public DependencyProvider_ProvidesBroadcastDispatcherFactory(DependencyProvider dependencyProvider, Provider<Context> provider, Provider<Looper> provider2, Provider<Executor> provider3, Provider<DumpManager> provider4, Provider<BroadcastDispatcherLogger> provider5, Provider<UserTracker> provider6) {
        this.module = dependencyProvider;
        this.contextProvider = provider;
        this.backgroundLooperProvider = provider2;
        this.backgroundExecutorProvider = provider3;
        this.dumpManagerProvider = provider4;
        this.loggerProvider = provider5;
        this.userTrackerProvider = provider6;
    }

    @Override // javax.inject.Provider
    public BroadcastDispatcher get() {
        return providesBroadcastDispatcher(this.module, this.contextProvider.get(), this.backgroundLooperProvider.get(), this.backgroundExecutorProvider.get(), this.dumpManagerProvider.get(), this.loggerProvider.get(), this.userTrackerProvider.get());
    }

    public static DependencyProvider_ProvidesBroadcastDispatcherFactory create(DependencyProvider dependencyProvider, Provider<Context> provider, Provider<Looper> provider2, Provider<Executor> provider3, Provider<DumpManager> provider4, Provider<BroadcastDispatcherLogger> provider5, Provider<UserTracker> provider6) {
        return new DependencyProvider_ProvidesBroadcastDispatcherFactory(dependencyProvider, provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static BroadcastDispatcher providesBroadcastDispatcher(DependencyProvider dependencyProvider, Context context, Looper looper, Executor executor, DumpManager dumpManager, BroadcastDispatcherLogger broadcastDispatcherLogger, UserTracker userTracker) {
        return (BroadcastDispatcher) Preconditions.checkNotNullFromProvides(dependencyProvider.providesBroadcastDispatcher(context, looper, executor, dumpManager, broadcastDispatcherLogger, userTracker));
    }
}
