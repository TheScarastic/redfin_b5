package com.android.systemui.statusbar.phone;

import android.os.SystemClock;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.Dependency;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import com.android.systemui.statusbar.notification.row.RowContentBindParams;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class NotificationGroupAlertTransferHelper implements OnHeadsUpChangedListener, StatusBarStateController.StateListener {
    private NotificationEntryManager mEntryManager;
    private HeadsUpManager mHeadsUpManager;
    private boolean mIsDozing;
    private final RowContentBindStage mRowContentBindStage;
    private final ArrayMap<String, GroupAlertEntry> mGroupAlertEntries = new ArrayMap<>();
    private final ArrayMap<String, PendingAlertInfo> mPendingAlerts = new ArrayMap<>();
    private final NotificationGroupManagerLegacy mGroupManager = (NotificationGroupManagerLegacy) Dependency.get(NotificationGroupManagerLegacy.class);
    private final NotificationGroupManagerLegacy.OnGroupChangeListener mOnGroupChangeListener = new NotificationGroupManagerLegacy.OnGroupChangeListener() { // from class: com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper.1
        @Override // com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy.OnGroupChangeListener
        public void onGroupCreated(NotificationGroupManagerLegacy.NotificationGroup notificationGroup, String str) {
            NotificationGroupAlertTransferHelper.this.mGroupAlertEntries.put(str, new GroupAlertEntry(notificationGroup));
        }

        @Override // com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy.OnGroupChangeListener
        public void onGroupRemoved(NotificationGroupManagerLegacy.NotificationGroup notificationGroup, String str) {
            NotificationGroupAlertTransferHelper.this.mGroupAlertEntries.remove(str);
        }

        @Override // com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy.OnGroupChangeListener
        public void onGroupSuppressionChanged(NotificationGroupManagerLegacy.NotificationGroup notificationGroup, boolean z) {
            NotificationGroupAlertTransferHelper.this.onGroupChanged(notificationGroup, notificationGroup.alertOverride);
        }

        @Override // com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy.OnGroupChangeListener
        public void onGroupAlertOverrideChanged(NotificationGroupManagerLegacy.NotificationGroup notificationGroup, NotificationEntry notificationEntry, NotificationEntry notificationEntry2) {
            NotificationGroupAlertTransferHelper.this.onGroupChanged(notificationGroup, notificationEntry);
        }
    };
    private final NotificationEntryListener mNotificationEntryListener = new NotificationEntryListener() { // from class: com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper.2
        @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
        public void onPendingEntryAdded(NotificationEntry notificationEntry) {
            GroupAlertEntry groupAlertEntry = (GroupAlertEntry) NotificationGroupAlertTransferHelper.this.mGroupAlertEntries.get(NotificationGroupAlertTransferHelper.this.mGroupManager.getGroupKey(notificationEntry.getSbn()));
            if (groupAlertEntry != null && groupAlertEntry.mGroup.alertOverride == null) {
                NotificationGroupAlertTransferHelper.this.checkShouldTransferBack(groupAlertEntry);
            }
        }

        @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
        public void onEntryRemoved(NotificationEntry notificationEntry, NotificationVisibility notificationVisibility, boolean z, int i) {
            NotificationGroupAlertTransferHelper.this.mPendingAlerts.remove(notificationEntry.getKey());
        }
    };

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onStateChanged(int i) {
    }

    public NotificationGroupAlertTransferHelper(RowContentBindStage rowContentBindStage) {
        ((StatusBarStateController) Dependency.get(StatusBarStateController.class)).addCallback(this);
        this.mRowContentBindStage = rowContentBindStage;
    }

    public void bind(NotificationEntryManager notificationEntryManager, NotificationGroupManagerLegacy notificationGroupManagerLegacy) {
        if (this.mEntryManager == null) {
            this.mEntryManager = notificationEntryManager;
            notificationEntryManager.addNotificationEntryListener(this.mNotificationEntryListener);
            notificationGroupManagerLegacy.registerGroupChangeListener(this.mOnGroupChangeListener);
            return;
        }
        throw new IllegalStateException("Already bound.");
    }

    public void setHeadsUpManager(HeadsUpManager headsUpManager) {
        this.mHeadsUpManager = headsUpManager;
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onDozingChanged(boolean z) {
        if (this.mIsDozing != z) {
            for (GroupAlertEntry groupAlertEntry : this.mGroupAlertEntries.values()) {
                groupAlertEntry.mLastAlertTransferTime = 0;
                groupAlertEntry.mAlertSummaryOnNextAddition = false;
            }
        }
        this.mIsDozing = z;
    }

    /* access modifiers changed from: private */
    public void onGroupChanged(NotificationGroupManagerLegacy.NotificationGroup notificationGroup, NotificationEntry notificationEntry) {
        NotificationEntry notificationEntry2 = notificationGroup.summary;
        if (notificationEntry2 != null) {
            if (notificationGroup.suppressed || notificationGroup.alertOverride != null) {
                checkForForwardAlertTransfer(notificationEntry2, notificationEntry);
                return;
            }
            GroupAlertEntry groupAlertEntry = this.mGroupAlertEntries.get(this.mGroupManager.getGroupKey(notificationEntry2.getSbn()));
            if (groupAlertEntry.mAlertSummaryOnNextAddition) {
                if (!this.mHeadsUpManager.isAlerting(notificationGroup.summary.getKey())) {
                    alertNotificationWhenPossible(notificationGroup.summary);
                }
                groupAlertEntry.mAlertSummaryOnNextAddition = false;
                return;
            }
            checkShouldTransferBack(groupAlertEntry);
        }
    }

    @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
    public void onHeadsUpStateChanged(NotificationEntry notificationEntry, boolean z) {
        if (z && notificationEntry.getSbn().getNotification().isGroupSummary()) {
            checkForForwardAlertTransfer(notificationEntry, null);
        }
    }

    private void checkForForwardAlertTransfer(NotificationEntry notificationEntry, NotificationEntry notificationEntry2) {
        NotificationGroupManagerLegacy.NotificationGroup groupForSummary = this.mGroupManager.getGroupForSummary(notificationEntry.getSbn());
        if (groupForSummary != null && groupForSummary.alertOverride != null) {
            handleOverriddenSummaryAlerted(notificationEntry);
        } else if (this.mGroupManager.isSummaryOfSuppressedGroup(notificationEntry.getSbn())) {
            handleSuppressedSummaryAlerted(notificationEntry, notificationEntry2);
        }
    }

    private int getPendingChildrenNotAlerting(NotificationGroupManagerLegacy.NotificationGroup notificationGroup) {
        NotificationEntryManager notificationEntryManager = this.mEntryManager;
        int i = 0;
        if (notificationEntryManager == null) {
            return 0;
        }
        for (NotificationEntry notificationEntry : notificationEntryManager.getPendingNotificationsIterator()) {
            if (isPendingNotificationInGroup(notificationEntry, notificationGroup) && onlySummaryAlerts(notificationEntry)) {
                i++;
            }
        }
        return i;
    }

    private boolean pendingInflationsWillAddChildren(NotificationGroupManagerLegacy.NotificationGroup notificationGroup) {
        NotificationEntryManager notificationEntryManager = this.mEntryManager;
        if (notificationEntryManager == null) {
            return false;
        }
        for (NotificationEntry notificationEntry : notificationEntryManager.getPendingNotificationsIterator()) {
            if (isPendingNotificationInGroup(notificationEntry, notificationGroup)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPendingNotificationInGroup(NotificationEntry notificationEntry, NotificationGroupManagerLegacy.NotificationGroup notificationGroup) {
        return this.mGroupManager.isGroupChild(notificationEntry.getSbn()) && Objects.equals(this.mGroupManager.getGroupKey(notificationEntry.getSbn()), this.mGroupManager.getGroupKey(notificationGroup.summary.getSbn())) && !notificationGroup.children.containsKey(notificationEntry.getKey());
    }

    private void handleSuppressedSummaryAlerted(NotificationEntry notificationEntry, NotificationEntry notificationEntry2) {
        GroupAlertEntry groupAlertEntry = this.mGroupAlertEntries.get(this.mGroupManager.getGroupKey(notificationEntry.getSbn()));
        if (this.mGroupManager.isSummaryOfSuppressedGroup(notificationEntry.getSbn()) && groupAlertEntry != null) {
            boolean isAlerting = this.mHeadsUpManager.isAlerting(notificationEntry.getKey());
            boolean z = notificationEntry2 != null && this.mHeadsUpManager.isAlerting(notificationEntry2.getKey());
            if ((isAlerting || z) && !pendingInflationsWillAddChildren(groupAlertEntry.mGroup)) {
                NotificationEntry next = this.mGroupManager.getLogicalChildren(notificationEntry.getSbn()).iterator().next();
                if (isAlerting) {
                    tryTransferAlertState(notificationEntry, notificationEntry, next, groupAlertEntry);
                } else if (canStillTransferBack(groupAlertEntry)) {
                    tryTransferAlertState(notificationEntry, notificationEntry2, next, groupAlertEntry);
                }
            }
        }
    }

    private void handleOverriddenSummaryAlerted(NotificationEntry notificationEntry) {
        ArrayList<NotificationEntry> logicalChildren;
        GroupAlertEntry groupAlertEntry = this.mGroupAlertEntries.get(this.mGroupManager.getGroupKey(notificationEntry.getSbn()));
        NotificationGroupManagerLegacy.NotificationGroup groupForSummary = this.mGroupManager.getGroupForSummary(notificationEntry.getSbn());
        if (groupForSummary != null && groupForSummary.alertOverride != null && groupAlertEntry != null) {
            if (this.mHeadsUpManager.isAlerting(notificationEntry.getKey())) {
                tryTransferAlertState(notificationEntry, notificationEntry, groupForSummary.alertOverride, groupAlertEntry);
            } else if (canStillTransferBack(groupAlertEntry) && (logicalChildren = this.mGroupManager.getLogicalChildren(notificationEntry.getSbn())) != null) {
                logicalChildren.remove(groupForSummary.alertOverride);
                if (releaseChildAlerts(logicalChildren)) {
                    tryTransferAlertState(notificationEntry, null, groupForSummary.alertOverride, groupAlertEntry);
                }
            }
        }
    }

    private void tryTransferAlertState(NotificationEntry notificationEntry, NotificationEntry notificationEntry2, NotificationEntry notificationEntry3, GroupAlertEntry groupAlertEntry) {
        if (notificationEntry3 != null && !notificationEntry3.getRow().keepInParent() && !notificationEntry3.isRowRemoved() && !notificationEntry3.isRowDismissed()) {
            if (!this.mHeadsUpManager.isAlerting(notificationEntry3.getKey()) && onlySummaryAlerts(notificationEntry)) {
                groupAlertEntry.mLastAlertTransferTime = SystemClock.elapsedRealtime();
            }
            transferAlertState(notificationEntry2, notificationEntry3);
        }
    }

    private void transferAlertState(NotificationEntry notificationEntry, NotificationEntry notificationEntry2) {
        if (notificationEntry != null) {
            this.mHeadsUpManager.removeNotification(notificationEntry.getKey(), true);
        }
        alertNotificationWhenPossible(notificationEntry2);
    }

    /* access modifiers changed from: private */
    public void checkShouldTransferBack(GroupAlertEntry groupAlertEntry) {
        if (canStillTransferBack(groupAlertEntry)) {
            NotificationEntry notificationEntry = groupAlertEntry.mGroup.summary;
            if (onlySummaryAlerts(notificationEntry)) {
                ArrayList<NotificationEntry> logicalChildren = this.mGroupManager.getLogicalChildren(notificationEntry.getSbn());
                int size = logicalChildren.size();
                if (getPendingChildrenNotAlerting(groupAlertEntry.mGroup) + size > 1 && releaseChildAlerts(logicalChildren) && !this.mHeadsUpManager.isAlerting(notificationEntry.getKey())) {
                    if (size > 1) {
                        alertNotificationWhenPossible(notificationEntry);
                    } else {
                        groupAlertEntry.mAlertSummaryOnNextAddition = true;
                    }
                    groupAlertEntry.mLastAlertTransferTime = 0;
                }
            }
        }
    }

    private boolean canStillTransferBack(GroupAlertEntry groupAlertEntry) {
        return SystemClock.elapsedRealtime() - groupAlertEntry.mLastAlertTransferTime < 300;
    }

    private boolean releaseChildAlerts(List<NotificationEntry> list) {
        boolean z = false;
        for (int i = 0; i < list.size(); i++) {
            NotificationEntry notificationEntry = list.get(i);
            if (onlySummaryAlerts(notificationEntry) && this.mHeadsUpManager.isAlerting(notificationEntry.getKey())) {
                this.mHeadsUpManager.removeNotification(notificationEntry.getKey(), true);
                z = true;
            }
            if (this.mPendingAlerts.containsKey(notificationEntry.getKey())) {
                this.mPendingAlerts.get(notificationEntry.getKey()).mAbortOnInflation = true;
                z = true;
            }
        }
        return z;
    }

    private void alertNotificationWhenPossible(NotificationEntry notificationEntry) {
        int contentFlag = this.mHeadsUpManager.getContentFlag();
        RowContentBindParams stageParams = this.mRowContentBindStage.getStageParams(notificationEntry);
        if ((stageParams.getContentViews() & contentFlag) == 0) {
            this.mPendingAlerts.put(notificationEntry.getKey(), new PendingAlertInfo(notificationEntry));
            stageParams.requireContentViews(contentFlag);
            this.mRowContentBindStage.requestRebind(notificationEntry, new NotifBindPipeline.BindCallback(notificationEntry, contentFlag) { // from class: com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper$$ExternalSyntheticLambda0
                public final /* synthetic */ NotificationEntry f$1;
                public final /* synthetic */ int f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // com.android.systemui.statusbar.notification.row.NotifBindPipeline.BindCallback
                public final void onBindFinished(NotificationEntry notificationEntry2) {
                    NotificationGroupAlertTransferHelper.this.lambda$alertNotificationWhenPossible$0(this.f$1, this.f$2, notificationEntry2);
                }
            });
        } else if (this.mHeadsUpManager.isAlerting(notificationEntry.getKey())) {
            this.mHeadsUpManager.updateNotification(notificationEntry.getKey(), true);
        } else {
            this.mHeadsUpManager.showNotification(notificationEntry);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$alertNotificationWhenPossible$0(NotificationEntry notificationEntry, int i, NotificationEntry notificationEntry2) {
        PendingAlertInfo remove = this.mPendingAlerts.remove(notificationEntry.getKey());
        if (remove == null) {
            return;
        }
        if (remove.isStillValid()) {
            alertNotificationWhenPossible(notificationEntry);
            return;
        }
        this.mRowContentBindStage.getStageParams(notificationEntry).markContentViewsFreeable(i);
        this.mRowContentBindStage.requestRebind(notificationEntry, null);
    }

    private boolean onlySummaryAlerts(NotificationEntry notificationEntry) {
        return notificationEntry.getSbn().getNotification().getGroupAlertBehavior() == 1;
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class PendingAlertInfo {
        boolean mAbortOnInflation;
        final NotificationEntry mEntry;
        final StatusBarNotification mOriginalNotification;

        PendingAlertInfo(NotificationEntry notificationEntry) {
            this.mOriginalNotification = notificationEntry.getSbn();
            this.mEntry = notificationEntry;
        }

        /* access modifiers changed from: private */
        public boolean isStillValid() {
            if (!this.mAbortOnInflation && this.mEntry.getSbn().getGroupKey() == this.mOriginalNotification.getGroupKey() && this.mEntry.getSbn().getNotification().isGroupSummary() == this.mOriginalNotification.getNotification().isGroupSummary()) {
                return true;
            }
            return false;
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class GroupAlertEntry {
        boolean mAlertSummaryOnNextAddition;
        final NotificationGroupManagerLegacy.NotificationGroup mGroup;
        long mLastAlertTransferTime;

        GroupAlertEntry(NotificationGroupManagerLegacy.NotificationGroup notificationGroup) {
            this.mGroup = notificationGroup;
        }
    }
}
