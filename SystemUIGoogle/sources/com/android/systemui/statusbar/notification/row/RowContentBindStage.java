package com.android.systemui.statusbar.notification.row;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.BindStage;
import com.android.systemui.statusbar.notification.row.NotificationRowContentBinder;
/* loaded from: classes.dex */
public class RowContentBindStage extends BindStage<RowContentBindParams> {
    private final NotificationRowContentBinder mBinder;
    private final RowContentBindStageLogger mLogger;
    private final NotifInflationErrorManager mNotifInflationErrorManager;

    /* access modifiers changed from: package-private */
    public RowContentBindStage(NotificationRowContentBinder notificationRowContentBinder, NotifInflationErrorManager notifInflationErrorManager, RowContentBindStageLogger rowContentBindStageLogger) {
        this.mBinder = notificationRowContentBinder;
        this.mNotifInflationErrorManager = notifInflationErrorManager;
        this.mLogger = rowContentBindStageLogger;
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.row.BindStage
    public void executeStage(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, final BindStage.StageCallback stageCallback) {
        RowContentBindParams stageParams = getStageParams(notificationEntry);
        this.mLogger.logStageParams(notificationEntry.getKey(), stageParams.toString());
        int contentViews = stageParams.getContentViews();
        int dirtyContentViews = stageParams.getDirtyContentViews() & contentViews;
        this.mBinder.unbindContent(notificationEntry, expandableNotificationRow, contentViews ^ 15);
        NotificationRowContentBinder.BindParams bindParams = new NotificationRowContentBinder.BindParams();
        bindParams.isLowPriority = stageParams.useLowPriority();
        bindParams.usesIncreasedHeight = stageParams.useIncreasedHeight();
        bindParams.usesIncreasedHeadsUpHeight = stageParams.useIncreasedHeadsUpHeight();
        boolean needsReinflation = stageParams.needsReinflation();
        AnonymousClass1 r9 = new NotificationRowContentBinder.InflationCallback() { // from class: com.android.systemui.statusbar.notification.row.RowContentBindStage.1
            @Override // com.android.systemui.statusbar.notification.row.NotificationRowContentBinder.InflationCallback
            public void handleInflationException(NotificationEntry notificationEntry2, Exception exc) {
                RowContentBindStage.this.mNotifInflationErrorManager.setInflationError(notificationEntry2, exc);
            }

            @Override // com.android.systemui.statusbar.notification.row.NotificationRowContentBinder.InflationCallback
            public void onAsyncInflationFinished(NotificationEntry notificationEntry2) {
                RowContentBindStage.this.mNotifInflationErrorManager.clearInflationError(notificationEntry2);
                RowContentBindStage.this.getStageParams(notificationEntry2).clearDirtyContentViews();
                stageCallback.onStageFinished(notificationEntry2);
            }
        };
        this.mBinder.cancelBind(notificationEntry, expandableNotificationRow);
        this.mBinder.bindContent(notificationEntry, expandableNotificationRow, dirtyContentViews, bindParams, needsReinflation, r9);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.row.BindStage
    public void abortStage(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow) {
        this.mBinder.cancelBind(notificationEntry, expandableNotificationRow);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.row.BindStage
    public RowContentBindParams newStageParams() {
        return new RowContentBindParams();
    }
}
