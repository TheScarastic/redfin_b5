package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
/* loaded from: classes.dex */
public final /* synthetic */ class HeadsUpManagerPhone$HeadsUpEntryPhone$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ HeadsUpManagerPhone.HeadsUpEntryPhone f$0;
    public final /* synthetic */ NotificationEntry f$1;

    public /* synthetic */ HeadsUpManagerPhone$HeadsUpEntryPhone$$ExternalSyntheticLambda0(HeadsUpManagerPhone.HeadsUpEntryPhone headsUpEntryPhone, NotificationEntry notificationEntry) {
        this.f$0 = headsUpEntryPhone;
        this.f$1 = notificationEntry;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$setEntry$0(this.f$1);
    }
}
