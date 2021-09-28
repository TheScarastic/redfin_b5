package com.android.systemui.log.dagger;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LogModule_ProvideNotificationsLogBufferFactory implements Factory<LogBuffer> {
    private final Provider<LogBufferFactory> factoryProvider;

    public LogModule_ProvideNotificationsLogBufferFactory(Provider<LogBufferFactory> provider) {
        this.factoryProvider = provider;
    }

    @Override // javax.inject.Provider
    public LogBuffer get() {
        return provideNotificationsLogBuffer(this.factoryProvider.get());
    }

    public static LogModule_ProvideNotificationsLogBufferFactory create(Provider<LogBufferFactory> provider) {
        return new LogModule_ProvideNotificationsLogBufferFactory(provider);
    }

    public static LogBuffer provideNotificationsLogBuffer(LogBufferFactory logBufferFactory) {
        return (LogBuffer) Preconditions.checkNotNullFromProvides(LogModule.provideNotificationsLogBuffer(logBufferFactory));
    }
}
