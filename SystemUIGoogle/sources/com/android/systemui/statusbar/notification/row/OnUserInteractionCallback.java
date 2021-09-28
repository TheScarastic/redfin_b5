package com.android.systemui.statusbar.notification.row;

import android.service.notification.NotificationListenerService;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* loaded from: classes.dex */
public interface OnUserInteractionCallback {
    NotificationEntry getGroupSummaryToDismiss(NotificationEntry notificationEntry);

    void onDismiss(NotificationEntry notificationEntry, @NotificationListenerService.NotificationCancelReason int i, NotificationEntry notificationEntry2);

    void onImportanceChanged(NotificationEntry notificationEntry);
}
