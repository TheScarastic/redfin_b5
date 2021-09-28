package com.android.systemui.statusbar.notification.row;

import android.widget.RemoteViews;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* loaded from: classes.dex */
public interface NotifRemoteViewCache {
    void clearCache(NotificationEntry notificationEntry);

    RemoteViews getCachedView(NotificationEntry notificationEntry, int i);

    boolean hasCachedView(NotificationEntry notificationEntry, int i);

    void putCachedView(NotificationEntry notificationEntry, int i, RemoteViews remoteViews);

    void removeCachedView(NotificationEntry notificationEntry, int i);
}
