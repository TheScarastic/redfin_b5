package com.android.systemui.statusbar.notification;

import android.content.pm.LauncherApps;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ConversationNotificationProcessor_Factory implements Factory<ConversationNotificationProcessor> {
    private final Provider<ConversationNotificationManager> conversationNotificationManagerProvider;
    private final Provider<LauncherApps> launcherAppsProvider;

    public ConversationNotificationProcessor_Factory(Provider<LauncherApps> provider, Provider<ConversationNotificationManager> provider2) {
        this.launcherAppsProvider = provider;
        this.conversationNotificationManagerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public ConversationNotificationProcessor get() {
        return newInstance(this.launcherAppsProvider.get(), this.conversationNotificationManagerProvider.get());
    }

    public static ConversationNotificationProcessor_Factory create(Provider<LauncherApps> provider, Provider<ConversationNotificationManager> provider2) {
        return new ConversationNotificationProcessor_Factory(provider, provider2);
    }

    public static ConversationNotificationProcessor newInstance(LauncherApps launcherApps, ConversationNotificationManager conversationNotificationManager) {
        return new ConversationNotificationProcessor(launcherApps, conversationNotificationManager);
    }
}
