package com.android.systemui.statusbar.phone;

import android.content.Intent;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter;
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBarNotificationActivityStarter$4$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ StatusBarNotificationActivityStarter.AnonymousClass4 f$0;
    public final /* synthetic */ ExpandableNotificationRow f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ Intent f$3;
    public final /* synthetic */ int f$4;

    public /* synthetic */ StatusBarNotificationActivityStarter$4$$ExternalSyntheticLambda0(StatusBarNotificationActivityStarter.AnonymousClass4 r1, ExpandableNotificationRow expandableNotificationRow, boolean z, Intent intent, int i) {
        this.f$0 = r1;
        this.f$1 = expandableNotificationRow;
        this.f$2 = z;
        this.f$3 = intent;
        this.f$4 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onDismiss$1(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
