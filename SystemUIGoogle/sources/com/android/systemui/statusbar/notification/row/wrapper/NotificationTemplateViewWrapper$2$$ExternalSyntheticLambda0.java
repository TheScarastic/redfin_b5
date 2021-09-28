package com.android.systemui.statusbar.notification.row.wrapper;

import android.app.PendingIntent;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationTemplateViewWrapper$2$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ PendingIntent f$0;
    public final /* synthetic */ PendingIntent.CancelListener f$1;

    public /* synthetic */ NotificationTemplateViewWrapper$2$$ExternalSyntheticLambda0(PendingIntent pendingIntent, PendingIntent.CancelListener cancelListener) {
        this.f$0 = pendingIntent;
        this.f$1 = cancelListener;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.unregisterCancelListener(this.f$1);
    }
}
