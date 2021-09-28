package com.android.systemui.statusbar.notification.collection.inflation;

import android.os.SystemClock;
import android.service.notification.NotificationListenerService;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.row.OnUserInteractionCallback;
import com.android.systemui.statusbar.policy.HeadsUpManager;
/* loaded from: classes.dex */
public class OnUserInteractionCallbackImpl implements OnUserInteractionCallback {
    private final GroupMembershipManager mGroupMembershipManager;
    private final HeadsUpManager mHeadsUpManager;
    private final NotifCollection mNotifCollection;
    private final NotifPipeline mNotifPipeline;
    private final StatusBarStateController mStatusBarStateController;
    private final VisualStabilityCoordinator mVisualStabilityCoordinator;

    public OnUserInteractionCallbackImpl(NotifPipeline notifPipeline, NotifCollection notifCollection, HeadsUpManager headsUpManager, StatusBarStateController statusBarStateController, VisualStabilityCoordinator visualStabilityCoordinator, GroupMembershipManager groupMembershipManager) {
        this.mNotifPipeline = notifPipeline;
        this.mNotifCollection = notifCollection;
        this.mHeadsUpManager = headsUpManager;
        this.mStatusBarStateController = statusBarStateController;
        this.mVisualStabilityCoordinator = visualStabilityCoordinator;
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
        this.mNotifCollection.dismissNotification(notificationEntry, new DismissedByUserStats(i2, 1, NotificationVisibility.obtain(notificationEntry.getKey(), notificationEntry.getRanking().getRank(), this.mNotifPipeline.getShadeListCount(), true, NotificationLogger.getNotificationLocation(notificationEntry))));
    }

    @Override // com.android.systemui.statusbar.notification.row.OnUserInteractionCallback
    public void onImportanceChanged(NotificationEntry notificationEntry) {
        this.mVisualStabilityCoordinator.temporarilyAllowSectionChanges(notificationEntry, SystemClock.uptimeMillis());
    }

    @Override // com.android.systemui.statusbar.notification.row.OnUserInteractionCallback
    public NotificationEntry getGroupSummaryToDismiss(NotificationEntry notificationEntry) {
        if (notificationEntry.getParent() == null || notificationEntry.getParent().getSummary() == null || !this.mGroupMembershipManager.isOnlyChildInGroup(notificationEntry)) {
            return null;
        }
        NotificationEntry summary = notificationEntry.getParent().getSummary();
        if (summary.isClearable()) {
            return summary;
        }
        return null;
    }
}
