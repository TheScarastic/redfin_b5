package com.android.systemui.log.dagger;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LogModule_ProvideBroadcastDispatcherLogBufferFactory implements Factory<LogBuffer> {
    private final Provider<LogBufferFactory> factoryProvider;

    public LogModule_ProvideBroadcastDispatcherLogBufferFactory(Provider<LogBufferFactory> provider) {
        this.factoryProvider = provider;
    }

    @Override // javax.inject.Provider
    public LogBuffer get() {
        return provideBroadcastDispatcherLogBuffer(this.factoryProvider.get());
    }

    public static LogModule_ProvideBroadcastDispatcherLogBufferFactory create(Provider<LogBufferFactory> provider) {
        return new LogModule_ProvideBroadcastDispatcherLogBufferFactory(provider);
    }

    public static LogBuffer provideBroadcastDispatcherLogBuffer(LogBufferFactory logBufferFactory) {
        return (LogBuffer) Preconditions.checkNotNullFromProvides(LogModule.provideBroadcastDispatcherLogBuffer(logBufferFactory));
    }
}
