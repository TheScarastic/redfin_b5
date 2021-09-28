package com.android.systemui.statusbar.notification.row;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* loaded from: classes.dex */
public interface NotificationRowContentBinder {

    /* loaded from: classes.dex */
    public static class BindParams {
        public boolean isLowPriority;
        public boolean usesIncreasedHeadsUpHeight;
        public boolean usesIncreasedHeight;
    }

    /* loaded from: classes.dex */
    public interface InflationCallback {
        void handleInflationException(NotificationEntry notificationEntry, Exception exc);

        void onAsyncInflationFinished(NotificationEntry notificationEntry);
    }

    void bindContent(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, int i, BindParams bindParams, boolean z, InflationCallback inflationCallback);

    void cancelBind(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow);

    void unbindContent(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, int i);
}
