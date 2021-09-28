package com.android.systemui.log.dagger;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LogModule_ProvideNotifInteractionLogBufferFactory implements Factory<LogBuffer> {
    private final Provider<LogBufferFactory> factoryProvider;

    public LogModule_ProvideNotifInteractionLogBufferFactory(Provider<LogBufferFactory> provider) {
        this.factoryProvider = provider;
    }

    @Override // javax.inject.Provider
    public LogBuffer get() {
        return provideNotifInteractionLogBuffer(this.factoryProvider.get());
    }

    public static LogModule_ProvideNotifInteractionLogBufferFactory create(Provider<LogBufferFactory> provider) {
        return new LogModule_ProvideNotifInteractionLogBufferFactory(provider);
    }

    public static LogBuffer provideNotifInteractionLogBuffer(LogBufferFactory logBufferFactory) {
        return (LogBuffer) Preconditions.checkNotNullFromProvides(LogModule.provideNotifInteractionLogBuffer(logBufferFactory));
    }
}
