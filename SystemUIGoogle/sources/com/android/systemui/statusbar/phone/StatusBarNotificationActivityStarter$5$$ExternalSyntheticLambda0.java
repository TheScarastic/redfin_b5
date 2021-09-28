package com.android.systemui.statusbar.phone;

import android.view.View;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter;
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBarNotificationActivityStarter$5$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ StatusBarNotificationActivityStarter.AnonymousClass5 f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ View f$2;
    public final /* synthetic */ boolean f$3;

    public /* synthetic */ StatusBarNotificationActivityStarter$5$$ExternalSyntheticLambda0(StatusBarNotificationActivityStarter.AnonymousClass5 r1, boolean z, View view, boolean z2) {
        this.f$0 = r1;
        this.f$1 = z;
        this.f$2 = view;
        this.f$3 = z2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onDismiss$1(this.f$1, this.f$2, this.f$3);
    }
}
