package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SmartspaceDedupingCoordinator.kt */
/* loaded from: classes.dex */
public final class SmartspaceDedupingCoordinator$collectionListener$1 implements NotifCollectionListener {
    final /* synthetic */ SmartspaceDedupingCoordinator this$0;

    /* access modifiers changed from: package-private */
    public SmartspaceDedupingCoordinator$collectionListener$1(SmartspaceDedupingCoordinator smartspaceDedupingCoordinator) {
        this.this$0 = smartspaceDedupingCoordinator;
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
    public void onEntryAdded(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        TrackedSmartspaceTarget trackedSmartspaceTarget = (TrackedSmartspaceTarget) this.this$0.trackedSmartspaceTargets.get(notificationEntry.getKey());
        if (trackedSmartspaceTarget != null) {
            boolean unused = this.this$0.updateFilterStatus(trackedSmartspaceTarget);
        }
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
    public void onEntryUpdated(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        TrackedSmartspaceTarget trackedSmartspaceTarget = (TrackedSmartspaceTarget) this.this$0.trackedSmartspaceTargets.get(notificationEntry.getKey());
        if (trackedSmartspaceTarget != null) {
            boolean unused = this.this$0.updateFilterStatus(trackedSmartspaceTarget);
        }
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
    public void onEntryRemoved(NotificationEntry notificationEntry, int i) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        TrackedSmartspaceTarget trackedSmartspaceTarget = (TrackedSmartspaceTarget) this.this$0.trackedSmartspaceTargets.get(notificationEntry.getKey());
        if (trackedSmartspaceTarget != null) {
            this.this$0.cancelExceptionTimeout(trackedSmartspaceTarget);
        }
    }
}
