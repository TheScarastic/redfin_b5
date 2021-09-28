package com.android.systemui.statusbar.notification.row;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationGutsManager$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ExpandableNotificationRow f$0;

    public /* synthetic */ NotificationGutsManager$1$$ExternalSyntheticLambda0(ExpandableNotificationRow expandableNotificationRow) {
        this.f$0 = expandableNotificationRow;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.onGutsOpened();
    }
}
