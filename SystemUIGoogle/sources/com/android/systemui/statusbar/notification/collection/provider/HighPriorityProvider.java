package com.android.systemui.statusbar.notification.collection.provider;

import android.app.Notification;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import java.util.List;
/* loaded from: classes.dex */
public class HighPriorityProvider {
    private final GroupMembershipManager mGroupMembershipManager;
    private final PeopleNotificationIdentifier mPeopleNotificationIdentifier;

    public HighPriorityProvider(PeopleNotificationIdentifier peopleNotificationIdentifier, GroupMembershipManager groupMembershipManager) {
        this.mPeopleNotificationIdentifier = peopleNotificationIdentifier;
        this.mGroupMembershipManager = groupMembershipManager;
    }

    public boolean isHighPriority(ListEntry listEntry) {
        NotificationEntry representativeEntry;
        if (listEntry == null || (representativeEntry = listEntry.getRepresentativeEntry()) == null) {
            return false;
        }
        if (representativeEntry.getRanking().getImportance() >= 3 || hasHighPriorityCharacteristics(representativeEntry) || hasHighPriorityChild(listEntry)) {
            return true;
        }
        return false;
    }

    private boolean hasHighPriorityChild(ListEntry listEntry) {
        List<NotificationEntry> children;
        if ((!(listEntry instanceof NotificationEntry) || this.mGroupMembershipManager.isGroupSummary((NotificationEntry) listEntry)) && (children = this.mGroupMembershipManager.getChildren(listEntry)) != null) {
            for (NotificationEntry notificationEntry : children) {
                if (notificationEntry != listEntry && isHighPriority(notificationEntry)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasHighPriorityCharacteristics(NotificationEntry notificationEntry) {
        return !hasUserSetImportance(notificationEntry) && (notificationEntry.getSbn().getNotification().hasMediaSession() || isPeopleNotification(notificationEntry) || isMessagingStyle(notificationEntry));
    }

    private boolean isMessagingStyle(NotificationEntry notificationEntry) {
        return notificationEntry.getSbn().getNotification().isStyle(Notification.MessagingStyle.class);
    }

    private boolean isPeopleNotification(NotificationEntry notificationEntry) {
        return this.mPeopleNotificationIdentifier.getPeopleNotificationType(notificationEntry) != 0;
    }

    private boolean hasUserSetImportance(NotificationEntry notificationEntry) {
        return notificationEntry.getRanking().getChannel() != null && notificationEntry.getRanking().getChannel().hasUserSetImportance();
    }
}
