package com.android.systemui.statusbar.notification;

import android.app.Notification;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
public final class ConversationNotificationProcessor {
    private final ConversationNotificationManager conversationNotificationManager;
    private final LauncherApps launcherApps;

    public ConversationNotificationProcessor(LauncherApps launcherApps, ConversationNotificationManager conversationNotificationManager) {
        Intrinsics.checkNotNullParameter(launcherApps, "launcherApps");
        Intrinsics.checkNotNullParameter(conversationNotificationManager, "conversationNotificationManager");
        this.launcherApps = launcherApps;
        this.conversationNotificationManager = conversationNotificationManager;
    }

    public final void processNotification(NotificationEntry notificationEntry, Notification.Builder builder) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Intrinsics.checkNotNullParameter(builder, "recoveredBuilder");
        Notification.Style style = builder.getStyle();
        Notification.MessagingStyle messagingStyle = style instanceof Notification.MessagingStyle ? (Notification.MessagingStyle) style : null;
        if (messagingStyle != null) {
            messagingStyle.setConversationType(notificationEntry.getRanking().getChannel().isImportantConversation() ? 2 : 1);
            ShortcutInfo conversationShortcutInfo = notificationEntry.getRanking().getConversationShortcutInfo();
            if (conversationShortcutInfo != null) {
                messagingStyle.setShortcutIcon(this.launcherApps.getShortcutIcon(conversationShortcutInfo));
                CharSequence label = conversationShortcutInfo.getLabel();
                if (label != null) {
                    messagingStyle.setConversationTitle(label);
                }
            }
            messagingStyle.setUnreadMessageCount(this.conversationNotificationManager.getUnreadCount(notificationEntry, builder));
        }
    }
}
