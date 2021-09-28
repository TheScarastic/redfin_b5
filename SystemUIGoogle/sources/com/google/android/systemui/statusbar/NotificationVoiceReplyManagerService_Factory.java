package com.google.android.systemui.statusbar;

import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyManagerService_Factory implements Factory<NotificationVoiceReplyManagerService> {
    private final Provider<NotificationVoiceReplyLogger> loggerProvider;
    private final Provider<NotificationVoiceReplyManager.Initializer> managerInitializerProvider;

    public NotificationVoiceReplyManagerService_Factory(Provider<NotificationVoiceReplyManager.Initializer> provider, Provider<NotificationVoiceReplyLogger> provider2) {
        this.managerInitializerProvider = provider;
        this.loggerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public NotificationVoiceReplyManagerService get() {
        return newInstance(this.managerInitializerProvider.get(), this.loggerProvider.get());
    }

    public static NotificationVoiceReplyManagerService_Factory create(Provider<NotificationVoiceReplyManager.Initializer> provider, Provider<NotificationVoiceReplyLogger> provider2) {
        return new NotificationVoiceReplyManagerService_Factory(provider, provider2);
    }

    public static NotificationVoiceReplyManagerService newInstance(NotificationVoiceReplyManager.Initializer initializer, NotificationVoiceReplyLogger notificationVoiceReplyLogger) {
        return new NotificationVoiceReplyManagerService(initializer, notificationVoiceReplyLogger);
    }
}
