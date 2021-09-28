package com.android.systemui.log.dagger;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LogModule_ProvideDozeLogBufferFactory implements Factory<LogBuffer> {
    private final Provider<LogBufferFactory> factoryProvider;

    public LogModule_ProvideDozeLogBufferFactory(Provider<LogBufferFactory> provider) {
        this.factoryProvider = provider;
    }

    @Override // javax.inject.Provider
    public LogBuffer get() {
        return provideDozeLogBuffer(this.factoryProvider.get());
    }

    public static LogModule_ProvideDozeLogBufferFactory create(Provider<LogBufferFactory> provider) {
        return new LogModule_ProvideDozeLogBufferFactory(provider);
    }

    public static LogBuffer provideDozeLogBuffer(LogBufferFactory logBufferFactory) {
        return (LogBuffer) Preconditions.checkNotNullFromProvides(LogModule.provideDozeLogBuffer(logBufferFactory));
    }
}
