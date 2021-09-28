package com.android.systemui.statusbar;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* loaded from: classes.dex */
public interface NotificationLifetimeExtender {

    /* loaded from: classes.dex */
    public interface NotificationSafeToRemoveCallback {
        void onSafeToRemove(String str);
    }

    void setCallback(NotificationSafeToRemoveCallback notificationSafeToRemoveCallback);

    void setShouldManageLifetime(NotificationEntry notificationEntry, boolean z);

    boolean shouldExtendLifetime(NotificationEntry notificationEntry);

    default boolean shouldExtendLifetimeForPendingNotification(NotificationEntry notificationEntry) {
        return false;
    }
}
