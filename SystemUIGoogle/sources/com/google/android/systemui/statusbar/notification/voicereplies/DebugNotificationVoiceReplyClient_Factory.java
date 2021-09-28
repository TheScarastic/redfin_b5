package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class DebugNotificationVoiceReplyClient_Factory implements Factory<DebugNotificationVoiceReplyClient> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<NotificationLockscreenUserManager> lockscreenUserManagerProvider;
    private final Provider<NotificationVoiceReplyManager.Initializer> voiceReplyInitializerProvider;

    public DebugNotificationVoiceReplyClient_Factory(Provider<BroadcastDispatcher> provider, Provider<NotificationLockscreenUserManager> provider2, Provider<NotificationVoiceReplyManager.Initializer> provider3) {
        this.broadcastDispatcherProvider = provider;
        this.lockscreenUserManagerProvider = provider2;
        this.voiceReplyInitializerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public DebugNotificationVoiceReplyClient get() {
        return newInstance(this.broadcastDispatcherProvider.get(), this.lockscreenUserManagerProvider.get(), this.voiceReplyInitializerProvider.get());
    }

    public static DebugNotificationVoiceReplyClient_Factory create(Provider<BroadcastDispatcher> provider, Provider<NotificationLockscreenUserManager> provider2, Provider<NotificationVoiceReplyManager.Initializer> provider3) {
        return new DebugNotificationVoiceReplyClient_Factory(provider, provider2, provider3);
    }

    public static DebugNotificationVoiceReplyClient newInstance(BroadcastDispatcher broadcastDispatcher, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationVoiceReplyManager.Initializer initializer) {
        return new DebugNotificationVoiceReplyClient(broadcastDispatcher, notificationLockscreenUserManager, initializer);
    }
}
