package com.android.systemui.statusbar.notification.collection.coordinator;

import android.os.RemoteException;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.NotifInflater;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeFinalizeFilterListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.render.NotifViewBarn;
import com.android.systemui.statusbar.notification.row.NotifInflationErrorManager;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes.dex */
public class PreparationCoordinator implements Coordinator {
    private final int mChildBindCutoff;
    private final Set<NotificationEntry> mInflatingNotifs;
    private final NotifInflationErrorManager.NotifInflationErrorListener mInflationErrorListener;
    private final Map<NotificationEntry, Integer> mInflationStates;
    private final PreparationCoordinatorLogger mLogger;
    private final long mMaxGroupInflationDelay;
    private final NotifCollectionListener mNotifCollectionListener;
    private final NotifInflationErrorManager mNotifErrorManager;
    private final NotifInflater mNotifInflater;
    private final NotifFilter mNotifInflatingFilter;
    private final NotifFilter mNotifInflationErrorFilter;
    private final OnBeforeFinalizeFilterListener mOnBeforeFinalizeFilterListener;
    private final IStatusBarService mStatusBarService;
    private final NotifViewBarn mViewBarn;

    public PreparationCoordinator(PreparationCoordinatorLogger preparationCoordinatorLogger, NotifInflater notifInflater, NotifInflationErrorManager notifInflationErrorManager, NotifViewBarn notifViewBarn, IStatusBarService iStatusBarService) {
        this(preparationCoordinatorLogger, notifInflater, notifInflationErrorManager, notifViewBarn, iStatusBarService, 9, 500);
    }

    @VisibleForTesting
    PreparationCoordinator(PreparationCoordinatorLogger preparationCoordinatorLogger, NotifInflater notifInflater, NotifInflationErrorManager notifInflationErrorManager, NotifViewBarn notifViewBarn, IStatusBarService iStatusBarService, int i, long j) {
        this.mInflationStates = new ArrayMap();
        this.mInflatingNotifs = new ArraySet();
        this.mNotifCollectionListener = new NotifCollectionListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinator.1
            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onEntryInit(NotificationEntry notificationEntry) {
                PreparationCoordinator.this.mInflationStates.put(notificationEntry, 0);
            }

            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onEntryUpdated(NotificationEntry notificationEntry) {
                PreparationCoordinator.this.abortInflation(notificationEntry, "entryUpdated");
                int inflationState = PreparationCoordinator.this.getInflationState(notificationEntry);
                if (inflationState == 1) {
                    PreparationCoordinator.this.mInflationStates.put(notificationEntry, 2);
                } else if (inflationState == -1) {
                    PreparationCoordinator.this.mInflationStates.put(notificationEntry, 0);
                }
            }

            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onEntryRemoved(NotificationEntry notificationEntry, int i2) {
                PreparationCoordinator preparationCoordinator = PreparationCoordinator.this;
                preparationCoordinator.abortInflation(notificationEntry, "entryRemoved reason=" + i2);
            }

            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onEntryCleanUp(NotificationEntry notificationEntry) {
                PreparationCoordinator.this.mInflationStates.remove(notificationEntry);
                PreparationCoordinator.this.mViewBarn.removeViewForEntry(notificationEntry);
            }
        };
        this.mOnBeforeFinalizeFilterListener = new OnBeforeFinalizeFilterListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinator$$ExternalSyntheticLambda1
            @Override // com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeFinalizeFilterListener
            public final void onBeforeFinalizeFilter(List list) {
                PreparationCoordinator.this.lambda$new$0(list);
            }
        };
        this.mNotifInflationErrorFilter = new NotifFilter("PreparationCoordinatorInflationError") { // from class: com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinator.2
            @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter
            public boolean shouldFilterOut(NotificationEntry notificationEntry, long j2) {
                return PreparationCoordinator.this.getInflationState(notificationEntry) == -1;
            }
        };
        this.mNotifInflatingFilter = new NotifFilter("PreparationCoordinatorInflating") { // from class: com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinator.3
            private final Map<GroupEntry, Boolean> mIsDelayedGroupCache = new ArrayMap();

            @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter
            public boolean shouldFilterOut(NotificationEntry notificationEntry, long j2) {
                GroupEntry parent = notificationEntry.getParent();
                Objects.requireNonNull(parent);
                GroupEntry groupEntry = parent;
                Boolean bool = this.mIsDelayedGroupCache.get(groupEntry);
                if (bool == null) {
                    bool = Boolean.valueOf(PreparationCoordinator.this.shouldWaitForGroupToInflate(groupEntry, j2));
                    this.mIsDelayedGroupCache.put(groupEntry, bool);
                }
                return !PreparationCoordinator.this.isInflated(notificationEntry) || bool.booleanValue();
            }

            @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Pluggable
            public void onCleanup() {
                this.mIsDelayedGroupCache.clear();
            }
        };
        this.mInflationErrorListener = new NotifInflationErrorManager.NotifInflationErrorListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinator.4
            @Override // com.android.systemui.statusbar.notification.row.NotifInflationErrorManager.NotifInflationErrorListener
            public void onNotifInflationError(NotificationEntry notificationEntry, Exception exc) {
                PreparationCoordinator.this.mViewBarn.removeViewForEntry(notificationEntry);
                PreparationCoordinator.this.mInflationStates.put(notificationEntry, -1);
                try {
                    StatusBarNotification sbn = notificationEntry.getSbn();
                    PreparationCoordinator.this.mStatusBarService.onNotificationError(sbn.getPackageName(), sbn.getTag(), sbn.getId(), sbn.getUid(), sbn.getInitialPid(), exc.getMessage(), sbn.getUser().getIdentifier());
                } catch (RemoteException unused) {
                }
                PreparationCoordinator.this.mNotifInflationErrorFilter.invalidateList();
            }

            @Override // com.android.systemui.statusbar.notification.row.NotifInflationErrorManager.NotifInflationErrorListener
            public void onNotifInflationErrorCleared(NotificationEntry notificationEntry) {
                PreparationCoordinator.this.mNotifInflationErrorFilter.invalidateList();
            }
        };
        this.mLogger = preparationCoordinatorLogger;
        this.mNotifInflater = notifInflater;
        this.mNotifErrorManager = notifInflationErrorManager;
        this.mViewBarn = notifViewBarn;
        this.mStatusBarService = iStatusBarService;
        this.mChildBindCutoff = i;
        this.mMaxGroupInflationDelay = j;
    }

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public void attach(NotifPipeline notifPipeline) {
        this.mNotifErrorManager.addInflationErrorListener(this.mInflationErrorListener);
        notifPipeline.addCollectionListener(this.mNotifCollectionListener);
        notifPipeline.addOnBeforeFinalizeFilterListener(this.mOnBeforeFinalizeFilterListener);
        notifPipeline.addFinalizeFilter(this.mNotifInflationErrorFilter);
        notifPipeline.addFinalizeFilter(this.mNotifInflatingFilter);
    }

    /* access modifiers changed from: private */
    /* renamed from: inflateAllRequiredViews */
    public void lambda$new$0(List<ListEntry> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ListEntry listEntry = list.get(i);
            if (listEntry instanceof GroupEntry) {
                GroupEntry groupEntry = (GroupEntry) listEntry;
                groupEntry.setUntruncatedChildCount(groupEntry.getChildren().size());
                inflateRequiredGroupViews(groupEntry);
            } else {
                inflateRequiredNotifViews((NotificationEntry) listEntry);
            }
        }
    }

    private void inflateRequiredGroupViews(GroupEntry groupEntry) {
        NotificationEntry summary = groupEntry.getSummary();
        List<NotificationEntry> children = groupEntry.getChildren();
        inflateRequiredNotifViews(summary);
        int i = 0;
        while (i < children.size()) {
            NotificationEntry notificationEntry = children.get(i);
            if (i < this.mChildBindCutoff) {
                inflateRequiredNotifViews(notificationEntry);
            } else {
                if (this.mInflatingNotifs.contains(notificationEntry)) {
                    abortInflation(notificationEntry, "Past last visible group child");
                }
                if (isInflated(notificationEntry)) {
                    freeNotifViews(notificationEntry);
                }
            }
            i++;
        }
    }

    private void inflateRequiredNotifViews(NotificationEntry notificationEntry) {
        if (!this.mInflatingNotifs.contains(notificationEntry)) {
            int intValue = this.mInflationStates.get(notificationEntry).intValue();
            if (intValue == 0) {
                inflateEntry(notificationEntry, "entryAdded");
            } else if (intValue == 2) {
                rebind(notificationEntry, "entryUpdated");
            }
        }
    }

    private void inflateEntry(NotificationEntry notificationEntry, String str) {
        abortInflation(notificationEntry, str);
        this.mInflatingNotifs.add(notificationEntry);
        this.mNotifInflater.inflateViews(notificationEntry, new PreparationCoordinator$$ExternalSyntheticLambda0(this));
    }

    private void rebind(NotificationEntry notificationEntry, String str) {
        this.mInflatingNotifs.add(notificationEntry);
        this.mNotifInflater.rebindViews(notificationEntry, new PreparationCoordinator$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public void abortInflation(NotificationEntry notificationEntry, String str) {
        this.mLogger.logInflationAborted(notificationEntry.getKey(), str);
        notificationEntry.abortTask();
        this.mInflatingNotifs.remove(notificationEntry);
    }

    /* access modifiers changed from: private */
    public void onInflationFinished(NotificationEntry notificationEntry) {
        this.mLogger.logNotifInflated(notificationEntry.getKey());
        this.mInflatingNotifs.remove(notificationEntry);
        this.mViewBarn.registerViewForEntry(notificationEntry, notificationEntry.getRowController());
        this.mInflationStates.put(notificationEntry, 1);
        this.mNotifInflatingFilter.invalidateList();
    }

    private void freeNotifViews(NotificationEntry notificationEntry) {
        this.mViewBarn.removeViewForEntry(notificationEntry);
        notificationEntry.setRow(null);
        this.mInflationStates.put(notificationEntry, 0);
    }

    /* access modifiers changed from: private */
    public boolean isInflated(NotificationEntry notificationEntry) {
        int inflationState = getInflationState(notificationEntry);
        return inflationState == 1 || inflationState == 2;
    }

    /* access modifiers changed from: private */
    public int getInflationState(NotificationEntry notificationEntry) {
        Integer num = this.mInflationStates.get(notificationEntry);
        Objects.requireNonNull(num, "Asking state of a notification preparation coordinator doesn't know about");
        return num.intValue();
    }

    /* access modifiers changed from: private */
    public boolean shouldWaitForGroupToInflate(GroupEntry groupEntry, long j) {
        if (groupEntry != GroupEntry.ROOT_ENTRY && !groupEntry.hasBeenAttachedBefore()) {
            if (isBeyondGroupInitializationWindow(groupEntry, j)) {
                this.mLogger.logGroupInflationTookTooLong(groupEntry.getKey());
                return false;
            } else if (this.mInflatingNotifs.contains(groupEntry.getSummary())) {
                this.mLogger.logDelayingGroupRelease(groupEntry.getKey(), groupEntry.getSummary().getKey());
                return true;
            } else {
                for (NotificationEntry notificationEntry : groupEntry.getChildren()) {
                    if (this.mInflatingNotifs.contains(notificationEntry) && !notificationEntry.hasBeenAttachedBefore()) {
                        this.mLogger.logDelayingGroupRelease(groupEntry.getKey(), notificationEntry.getKey());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isBeyondGroupInitializationWindow(GroupEntry groupEntry, long j) {
        return j - groupEntry.getCreationTime() > this.mMaxGroupInflationDelay;
    }
}
