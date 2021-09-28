package com.android.systemui.statusbar;

import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationRemoteInputManager$3$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ NotificationRemoteInputManager.AnonymousClass3 f$0;
    public final /* synthetic */ NotificationEntry f$1;

    public /* synthetic */ NotificationRemoteInputManager$3$$ExternalSyntheticLambda0(NotificationRemoteInputManager.AnonymousClass3 r1, NotificationEntry notificationEntry) {
        this.f$0 = r1;
        this.f$1 = notificationEntry;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onRemoteInputSent$0(this.f$1);
    }
}
