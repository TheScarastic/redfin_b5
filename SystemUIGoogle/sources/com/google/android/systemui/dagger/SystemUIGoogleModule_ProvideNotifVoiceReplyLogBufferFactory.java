package com.google.android.systemui.dagger;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SystemUIGoogleModule_ProvideNotifVoiceReplyLogBufferFactory implements Factory<LogBuffer> {
    private final Provider<LogBufferFactory> factoryProvider;

    public SystemUIGoogleModule_ProvideNotifVoiceReplyLogBufferFactory(Provider<LogBufferFactory> provider) {
        this.factoryProvider = provider;
    }

    @Override // javax.inject.Provider
    public LogBuffer get() {
        return provideNotifVoiceReplyLogBuffer(this.factoryProvider.get());
    }

    public static SystemUIGoogleModule_ProvideNotifVoiceReplyLogBufferFactory create(Provider<LogBufferFactory> provider) {
        return new SystemUIGoogleModule_ProvideNotifVoiceReplyLogBufferFactory(provider);
    }

    public static LogBuffer provideNotifVoiceReplyLogBuffer(LogBufferFactory logBufferFactory) {
        return (LogBuffer) Preconditions.checkNotNullFromProvides(SystemUIGoogleModule.provideNotifVoiceReplyLogBuffer(logBufferFactory));
    }
}
