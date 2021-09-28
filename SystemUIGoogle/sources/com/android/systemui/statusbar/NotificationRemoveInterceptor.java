package com.android.systemui.statusbar;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* loaded from: classes.dex */
public interface NotificationRemoveInterceptor {
    boolean onNotificationRemoveRequested(String str, NotificationEntry notificationEntry, int i);
}
