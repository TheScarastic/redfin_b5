package com.android.systemui.statusbar;

import com.android.systemui.statusbar.AlertingNotificationManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* loaded from: classes.dex */
public final /* synthetic */ class AlertingNotificationManager$AlertEntry$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ AlertingNotificationManager.AlertEntry f$0;
    public final /* synthetic */ NotificationEntry f$1;

    public /* synthetic */ AlertingNotificationManager$AlertEntry$$ExternalSyntheticLambda0(AlertingNotificationManager.AlertEntry alertEntry, NotificationEntry notificationEntry) {
        this.f$0 = alertEntry;
        this.f$1 = notificationEntry;
    }

    @Override // java.lang.Runnable
    public final void run() {
        AlertingNotificationManager.AlertEntry.$r8$lambda$fZ21nWQi22YOfd5Sut_ra0nax5o(this.f$0, this.f$1);
    }
}
