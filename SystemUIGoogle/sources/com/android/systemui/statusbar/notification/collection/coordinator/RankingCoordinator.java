package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
/* loaded from: classes.dex */
public class RankingCoordinator implements Coordinator {
    private final NodeController mAlertingHeaderController;
    private final HighPriorityProvider mHighPriorityProvider;
    private final NodeController mSilentHeaderController;
    private final StatusBarStateController mStatusBarStateController;
    private final NotifSectioner mAlertingNotifSectioner = new NotifSectioner("Alerting") { // from class: com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator.1
        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner
        public boolean isInSection(ListEntry listEntry) {
            return RankingCoordinator.this.mHighPriorityProvider.isHighPriority(listEntry);
        }

        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner
        public NodeController getHeaderNodeController() {
            return RankingCoordinator.this.mAlertingHeaderController;
        }
    };
    private final NotifSectioner mSilentNotifSectioner = new NotifSectioner("Silent") { // from class: com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator.2
        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner
        public boolean isInSection(ListEntry listEntry) {
            return !RankingCoordinator.this.mHighPriorityProvider.isHighPriority(listEntry);
        }

        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner
        public NodeController getHeaderNodeController() {
            return RankingCoordinator.this.mSilentHeaderController;
        }
    };
    private final NotifFilter mSuspendedFilter = new NotifFilter("IsSuspendedFilter") { // from class: com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator.3
        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter
        public boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            return notificationEntry.getRanking().isSuspended();
        }
    };
    private final NotifFilter mDndVisualEffectsFilter = new NotifFilter("DndSuppressingVisualEffects") { // from class: com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator.4
        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter
        public boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            if (RankingCoordinator.this.mStatusBarStateController.isDozing() && notificationEntry.shouldSuppressAmbient()) {
                return true;
            }
            if (RankingCoordinator.this.mStatusBarStateController.isDozing() || !notificationEntry.shouldSuppressNotificationList()) {
                return false;
            }
            return true;
        }
    };
    private final StatusBarStateController.StateListener mStatusBarStateCallback = new StatusBarStateController.StateListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator.5
        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public void onDozingChanged(boolean z) {
            RankingCoordinator.this.mDndVisualEffectsFilter.invalidateList();
        }
    };

    public RankingCoordinator(StatusBarStateController statusBarStateController, HighPriorityProvider highPriorityProvider, NodeController nodeController, NodeController nodeController2) {
        this.mStatusBarStateController = statusBarStateController;
        this.mHighPriorityProvider = highPriorityProvider;
        this.mAlertingHeaderController = nodeController;
        this.mSilentHeaderController = nodeController2;
    }

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public void attach(NotifPipeline notifPipeline) {
        this.mStatusBarStateController.addCallback(this.mStatusBarStateCallback);
        notifPipeline.addPreGroupFilter(this.mSuspendedFilter);
        notifPipeline.addPreGroupFilter(this.mDndVisualEffectsFilter);
    }

    public NotifSectioner getAlertingSectioner() {
        return this.mAlertingNotifSectioner;
    }

    public NotifSectioner getSilentSectioner() {
        return this.mSilentNotifSectioner;
    }
}
