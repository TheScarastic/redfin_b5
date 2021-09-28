package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.Dumpable;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
/* loaded from: classes.dex */
public interface GroupExpansionManager extends Dumpable {

    /* loaded from: classes.dex */
    public interface OnGroupExpansionChangeListener {
        void onGroupExpansionChange(ExpandableNotificationRow expandableNotificationRow, boolean z);
    }

    void collapseGroups();

    boolean isGroupExpanded(NotificationEntry notificationEntry);

    void registerGroupExpansionChangeListener(OnGroupExpansionChangeListener onGroupExpansionChangeListener);

    void setGroupExpanded(NotificationEntry notificationEntry, boolean z);

    boolean toggleGroupExpansion(NotificationEntry notificationEntry);
}
