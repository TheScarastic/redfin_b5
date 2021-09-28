package com.android.systemui.statusbar.notification;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ConversationNotificationManager_Factory implements Factory<ConversationNotificationManager> {
    private final Provider<Context> contextProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<NotificationEntryManager> notificationEntryManagerProvider;
    private final Provider<NotificationGroupManagerLegacy> notificationGroupManagerProvider;

    public ConversationNotificationManager_Factory(Provider<NotificationEntryManager> provider, Provider<NotificationGroupManagerLegacy> provider2, Provider<Context> provider3, Provider<Handler> provider4) {
        this.notificationEntryManagerProvider = provider;
        this.notificationGroupManagerProvider = provider2;
        this.contextProvider = provider3;
        this.mainHandlerProvider = provider4;
    }

    @Override // javax.inject.Provider
    public ConversationNotificationManager get() {
        return newInstance(this.notificationEntryManagerProvider.get(), this.notificationGroupManagerProvider.get(), this.contextProvider.get(), this.mainHandlerProvider.get());
    }

    public static ConversationNotificationManager_Factory create(Provider<NotificationEntryManager> provider, Provider<NotificationGroupManagerLegacy> provider2, Provider<Context> provider3, Provider<Handler> provider4) {
        return new ConversationNotificationManager_Factory(provider, provider2, provider3, provider4);
    }

    public static ConversationNotificationManager newInstance(NotificationEntryManager notificationEntryManager, NotificationGroupManagerLegacy notificationGroupManagerLegacy, Context context, Handler handler) {
        return new ConversationNotificationManager(notificationEntryManager, notificationGroupManagerLegacy, context, handler);
    }
}
