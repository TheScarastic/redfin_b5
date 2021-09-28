package com.android.systemui.people;

import android.app.Notification;
import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationHelper$$ExternalSyntheticLambda0 implements Function {
    public static final /* synthetic */ NotificationHelper$$ExternalSyntheticLambda0 INSTANCE = new NotificationHelper$$ExternalSyntheticLambda0();

    private /* synthetic */ NotificationHelper$$ExternalSyntheticLambda0() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return Long.valueOf(((Notification.MessagingStyle.Message) obj).getTimestamp());
    }
}
