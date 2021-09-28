package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.RowContentBindParams;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class DynamicChildBindController {
    private final int mChildBindCutoff;
    private final RowContentBindStage mStage;

    public DynamicChildBindController(RowContentBindStage rowContentBindStage) {
        this(rowContentBindStage, 9);
    }

    DynamicChildBindController(RowContentBindStage rowContentBindStage, int i) {
        this.mStage = rowContentBindStage;
        this.mChildBindCutoff = i;
    }

    public void updateContentViews(Map<NotificationEntry, List<NotificationEntry>> map) {
        for (NotificationEntry notificationEntry : map.keySet()) {
            List<NotificationEntry> list = map.get(notificationEntry);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    NotificationEntry notificationEntry2 = list.get(i);
                    if (i >= this.mChildBindCutoff) {
                        if (hasContent(notificationEntry2)) {
                            freeContent(notificationEntry2);
                        }
                    } else if (!hasContent(notificationEntry2)) {
                        bindContent(notificationEntry2);
                    }
                }
            } else if (!hasContent(notificationEntry)) {
                bindContent(notificationEntry);
            }
        }
    }

    private boolean hasContent(NotificationEntry notificationEntry) {
        ExpandableNotificationRow row = notificationEntry.getRow();
        return (row.getPrivateLayout().getContractedChild() == null && row.getPrivateLayout().getExpandedChild() == null) ? false : true;
    }

    private void freeContent(NotificationEntry notificationEntry) {
        RowContentBindParams stageParams = this.mStage.getStageParams(notificationEntry);
        stageParams.markContentViewsFreeable(1);
        stageParams.markContentViewsFreeable(2);
        this.mStage.requestRebind(notificationEntry, null);
    }

    private void bindContent(NotificationEntry notificationEntry) {
        RowContentBindParams stageParams = this.mStage.getStageParams(notificationEntry);
        stageParams.requireContentViews(1);
        stageParams.requireContentViews(2);
        this.mStage.requestRebind(notificationEntry, null);
    }
}
