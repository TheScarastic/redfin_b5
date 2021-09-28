package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationStackScrollLayoutController$NotificationListContainerImpl$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ NotificationStackScrollLayoutController.NotificationListContainerImpl f$0;
    public final /* synthetic */ ExpandableNotificationRow f$1;

    public /* synthetic */ NotificationStackScrollLayoutController$NotificationListContainerImpl$$ExternalSyntheticLambda0(NotificationStackScrollLayoutController.NotificationListContainerImpl notificationListContainerImpl, ExpandableNotificationRow expandableNotificationRow) {
        this.f$0 = notificationListContainerImpl;
        this.f$1 = expandableNotificationRow;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        this.f$0.lambda$bindRow$0(this.f$1, (Boolean) obj);
    }
}
