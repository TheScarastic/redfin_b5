package com.android.systemui.statusbar.notification.collection.inflation;

import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import com.android.systemui.statusbar.notification.row.RowContentBindParams;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
/* loaded from: classes.dex */
public class LowPriorityInflationHelper {
    private final FeatureFlags mFeatureFlags;
    private final NotificationGroupManagerLegacy mGroupManager;
    private final RowContentBindStage mRowContentBindStage;

    /* access modifiers changed from: package-private */
    public LowPriorityInflationHelper(FeatureFlags featureFlags, NotificationGroupManagerLegacy notificationGroupManagerLegacy, RowContentBindStage rowContentBindStage) {
        this.mFeatureFlags = featureFlags;
        this.mGroupManager = notificationGroupManagerLegacy;
        this.mRowContentBindStage = rowContentBindStage;
    }

    public void recheckLowPriorityViewAndInflate(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow) {
        RowContentBindParams stageParams = this.mRowContentBindStage.getStageParams(notificationEntry);
        boolean shouldUseLowPriorityView = shouldUseLowPriorityView(notificationEntry);
        if (!expandableNotificationRow.isRemoved() && expandableNotificationRow.isLowPriority() != shouldUseLowPriorityView) {
            stageParams.setUseLowPriority(shouldUseLowPriorityView);
            this.mRowContentBindStage.requestRebind(notificationEntry, new NotifBindPipeline.BindCallback(shouldUseLowPriorityView) { // from class: com.android.systemui.statusbar.notification.collection.inflation.LowPriorityInflationHelper$$ExternalSyntheticLambda0
                public final /* synthetic */ boolean f$1;

                {
                    this.f$1 = r2;
                }

                @Override // com.android.systemui.statusbar.notification.row.NotifBindPipeline.BindCallback
                public final void onBindFinished(NotificationEntry notificationEntry2) {
                    ExpandableNotificationRow.this.setIsLowPriority(this.f$1);
                }
            });
        }
    }

    public boolean shouldUseLowPriorityView(NotificationEntry notificationEntry) {
        boolean z;
        if (this.mFeatureFlags.isNewNotifPipelineRenderingEnabled()) {
            z = notificationEntry.getParent() != GroupEntry.ROOT_ENTRY;
        } else {
            z = this.mGroupManager.isChildInGroup(notificationEntry);
        }
        return notificationEntry.isAmbient() && !z;
    }
}
