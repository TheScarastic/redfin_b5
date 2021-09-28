package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.internal.logging.UiEventLogger;
import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyLogger_Factory implements Factory<NotificationVoiceReplyLogger> {
    private final Provider<UiEventLogger> eventLoggerProvider;
    private final Provider<LogBuffer> logBufferProvider;

    public NotificationVoiceReplyLogger_Factory(Provider<LogBuffer> provider, Provider<UiEventLogger> provider2) {
        this.logBufferProvider = provider;
        this.eventLoggerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public NotificationVoiceReplyLogger get() {
        return newInstance(this.logBufferProvider.get(), this.eventLoggerProvider.get());
    }

    public static NotificationVoiceReplyLogger_Factory create(Provider<LogBuffer> provider, Provider<UiEventLogger> provider2) {
        return new NotificationVoiceReplyLogger_Factory(provider, provider2);
    }

    public static NotificationVoiceReplyLogger newInstance(LogBuffer logBuffer, UiEventLogger uiEventLogger) {
        return new NotificationVoiceReplyLogger(logBuffer, uiEventLogger);
    }
}
