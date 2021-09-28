package com.android.systemui.statusbar.notification.collection.legacy;

import android.service.notification.NotificationListenerService;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.row.OnUserInteractionCallback;
import com.android.systemui.statusbar.policy.HeadsUpManager;
/* loaded from: classes.dex */
public class OnUserInteractionCallbackImplLegacy implements OnUserInteractionCallback {
    private final GroupMembershipManager mGroupMembershipManager;
    private final HeadsUpManager mHeadsUpManager;
    private final NotificationEntryManager mNotificationEntryManager;
    private final StatusBarStateController mStatusBarStateController;
    private final VisualStabilityManager mVisualStabilityManager;

    public OnUserInteractionCallbackImplLegacy(NotificationEntryManager notificationEntryManager, HeadsUpManager headsUpManager, StatusBarStateController statusBarStateController, VisualStabilityManager visualStabilityManager, GroupMembershipManager groupMembershipManager) {
        this.mNotificationEntryManager = notificationEntryManager;
        this.mHeadsUpManager = headsUpManager;
        this.mStatusBarStateController = statusBarStateController;
        this.mVisualStabilityManager = visualStabilityManager;
        this.mGroupMembershipManager = groupMembershipManager;
    }

    @Override // com.android.systemui.statusbar.notification.row.OnUserInteractionCallback
    public void onDismiss(NotificationEntry notificationEntry, @NotificationListenerService.NotificationCancelReason int i, NotificationEntry notificationEntry2) {
        int i2;
        if (this.mHeadsUpManager.isAlerting(notificationEntry.getKey())) {
            i2 = 1;
        } else {
            i2 = this.mStatusBarStateController.isDozing() ? 2 : 3;
        }
        if (notificationEntry2 != null) {
            onDismiss(notificationEntry2, i, null);
        }
        this.mNotificationEntryManager.performRemoveNotification(notificationEntry.getSbn(), new DismissedByUserStats(i2, 1, NotificationVisibility.obtain(notificationEntry.getKey(), notificationEntry.getRanking().getRank(), this.mNotificationEntryManager.getActiveNotificationsCount(), true, NotificationLogger.getNotificationLocation(notificationEntry))), i);
    }

    @Override // com.android.systemui.statusbar.notification.row.OnUserInteractionCallback
    public void onImportanceChanged(NotificationEntry notificationEntry) {
        this.mVisualStabilityManager.temporarilyAllowReordering();
    }

    @Override // com.android.systemui.statusbar.notification.row.OnUserInteractionCallback
    public NotificationEntry getGroupSummaryToDismiss(NotificationEntry notificationEntry) {
        if (!this.mGroupMembershipManager.isOnlyChildInGroup(notificationEntry)) {
            return null;
        }
        NotificationEntry logicalGroupSummary = this.mGroupMembershipManager.getLogicalGroupSummary(notificationEntry);
        if (logicalGroupSummary.isClearable()) {
            return logicalGroupSummary;
        }
        return null;
    }
}
