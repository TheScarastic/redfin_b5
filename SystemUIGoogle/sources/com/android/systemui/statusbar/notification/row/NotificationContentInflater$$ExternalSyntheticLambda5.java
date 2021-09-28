package com.android.systemui.statusbar.notification.row;

import android.os.CancellationSignal;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationContentInflater$$ExternalSyntheticLambda5 implements Consumer {
    public static final /* synthetic */ NotificationContentInflater$$ExternalSyntheticLambda5 INSTANCE = new NotificationContentInflater$$ExternalSyntheticLambda5();

    private /* synthetic */ NotificationContentInflater$$ExternalSyntheticLambda5() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((CancellationSignal) obj).cancel();
    }
}
