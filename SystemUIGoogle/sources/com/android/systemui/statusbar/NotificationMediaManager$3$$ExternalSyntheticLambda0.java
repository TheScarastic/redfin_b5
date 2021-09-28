package com.android.systemui.statusbar;

import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationMediaManager$3$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ NotificationMediaManager.AnonymousClass3 f$0;

    public /* synthetic */ NotificationMediaManager$3$$ExternalSyntheticLambda0(NotificationMediaManager.AnonymousClass3 r1) {
        this.f$0 = r1;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        this.f$0.lambda$onMediaDataRemoved$1((NotificationEntry) obj);
    }
}
