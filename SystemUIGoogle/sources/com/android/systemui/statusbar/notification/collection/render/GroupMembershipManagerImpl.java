package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.List;
/* loaded from: classes.dex */
public class GroupMembershipManagerImpl implements GroupMembershipManager {
    @Override // com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager
    public boolean isGroupSummary(NotificationEntry notificationEntry) {
        return getGroupSummary(notificationEntry) == notificationEntry;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager
    public NotificationEntry getGroupSummary(NotificationEntry notificationEntry) {
        if (isEntryTopLevel(notificationEntry) || notificationEntry.getParent() == null) {
            return null;
        }
        return notificationEntry.getParent().getRepresentativeEntry();
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager
    public boolean isChildInGroup(NotificationEntry notificationEntry) {
        return !isEntryTopLevel(notificationEntry);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager
    public boolean isOnlyChildInGroup(NotificationEntry notificationEntry) {
        if (notificationEntry.getParent() != null && !isGroupSummary(notificationEntry) && notificationEntry.getParent().getChildren().size() == 1) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager
    public List<NotificationEntry> getChildren(ListEntry listEntry) {
        if (listEntry instanceof GroupEntry) {
            return ((GroupEntry) listEntry).getChildren();
        }
        if (isGroupSummary(listEntry.getRepresentativeEntry())) {
            return listEntry.getRepresentativeEntry().getParent().getChildren();
        }
        return null;
    }

    private boolean isEntryTopLevel(NotificationEntry notificationEntry) {
        return notificationEntry.getParent() == GroupEntry.ROOT_ENTRY;
    }
}
