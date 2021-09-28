package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifDismissInterceptor;
import com.android.systemui.wmshell.BubblesManager;
import com.android.wm.shell.bubbles.Bubbles;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
/* loaded from: classes.dex */
public class BubbleCoordinator implements Coordinator {
    private final Optional<BubblesManager> mBubblesManagerOptional;
    private final Optional<Bubbles> mBubblesOptional;
    private final NotifCollection mNotifCollection;
    private NotifPipeline mNotifPipeline;
    private NotifDismissInterceptor.OnEndDismissInterception mOnEndDismissInterception;
    private final Set<String> mInterceptedDismissalEntries = new HashSet();
    private final NotifFilter mNotifFilter = new NotifFilter("BubbleCoordinator") { // from class: com.android.systemui.statusbar.notification.collection.coordinator.BubbleCoordinator.1
        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter
        public boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            return BubbleCoordinator.this.mBubblesOptional.isPresent() && ((Bubbles) BubbleCoordinator.this.mBubblesOptional.get()).isBubbleNotificationSuppressedFromShade(notificationEntry.getKey(), notificationEntry.getSbn().getGroupKey());
        }
    };
    private final NotifDismissInterceptor mDismissInterceptor = new NotifDismissInterceptor() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.BubbleCoordinator.2
        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifDismissInterceptor
        public String getName() {
            return "BubbleCoordinator";
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifDismissInterceptor
        public void setCallback(NotifDismissInterceptor.OnEndDismissInterception onEndDismissInterception) {
            BubbleCoordinator.this.mOnEndDismissInterception = onEndDismissInterception;
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifDismissInterceptor
        public boolean shouldInterceptDismissal(NotificationEntry notificationEntry) {
            if (!BubbleCoordinator.this.mBubblesManagerOptional.isPresent() || !((BubblesManager) BubbleCoordinator.this.mBubblesManagerOptional.get()).handleDismissalInterception(notificationEntry)) {
                BubbleCoordinator.this.mInterceptedDismissalEntries.remove(notificationEntry.getKey());
                return false;
            }
            BubbleCoordinator.this.mInterceptedDismissalEntries.add(notificationEntry.getKey());
            return true;
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifDismissInterceptor
        public void cancelDismissInterception(NotificationEntry notificationEntry) {
            BubbleCoordinator.this.mInterceptedDismissalEntries.remove(notificationEntry.getKey());
        }
    };
    private final BubblesManager.NotifCallback mNotifCallback = new BubblesManager.NotifCallback() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.BubbleCoordinator.3
        @Override // com.android.systemui.wmshell.BubblesManager.NotifCallback
        public void maybeCancelSummary(NotificationEntry notificationEntry) {
        }

        @Override // com.android.systemui.wmshell.BubblesManager.NotifCallback
        public void removeNotification(NotificationEntry notificationEntry, DismissedByUserStats dismissedByUserStats, int i) {
            if (BubbleCoordinator.this.isInterceptingDismissal(notificationEntry)) {
                BubbleCoordinator.this.mInterceptedDismissalEntries.remove(notificationEntry.getKey());
                BubbleCoordinator.this.mOnEndDismissInterception.onEndDismissInterception(BubbleCoordinator.this.mDismissInterceptor, notificationEntry, dismissedByUserStats);
            } else if (BubbleCoordinator.this.mNotifPipeline.getAllNotifs().contains(notificationEntry)) {
                BubbleCoordinator.this.mNotifCollection.dismissNotification(notificationEntry, dismissedByUserStats);
            }
        }

        @Override // com.android.systemui.wmshell.BubblesManager.NotifCallback
        public void invalidateNotifications(String str) {
            BubbleCoordinator.this.mNotifFilter.invalidateList();
        }
    };

    public BubbleCoordinator(Optional<BubblesManager> optional, Optional<Bubbles> optional2, NotifCollection notifCollection) {
        this.mBubblesManagerOptional = optional;
        this.mBubblesOptional = optional2;
        this.mNotifCollection = notifCollection;
    }

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public void attach(NotifPipeline notifPipeline) {
        this.mNotifPipeline = notifPipeline;
        notifPipeline.addNotificationDismissInterceptor(this.mDismissInterceptor);
        this.mNotifPipeline.addFinalizeFilter(this.mNotifFilter);
        if (this.mBubblesManagerOptional.isPresent()) {
            this.mBubblesManagerOptional.get().addNotifCallback(this.mNotifCallback);
        }
    }

    /* access modifiers changed from: private */
    public boolean isInterceptingDismissal(NotificationEntry notificationEntry) {
        return this.mInterceptedDismissalEntries.contains(notificationEntry.getKey());
    }
}
