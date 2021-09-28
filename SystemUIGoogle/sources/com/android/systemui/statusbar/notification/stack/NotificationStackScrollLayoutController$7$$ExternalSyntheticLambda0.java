package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationStackScrollLayoutController$7$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ NotificationStackScrollLayoutController.AnonymousClass7 f$0;
    public final /* synthetic */ ExpandableNotificationRow f$1;

    public /* synthetic */ NotificationStackScrollLayoutController$7$$ExternalSyntheticLambda0(NotificationStackScrollLayoutController.AnonymousClass7 r1, ExpandableNotificationRow expandableNotificationRow) {
        this.f$0 = r1;
        this.f$1 = expandableNotificationRow;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onHeadsUpUnPinned$0(this.f$1);
    }
}
