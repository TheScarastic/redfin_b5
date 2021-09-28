package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBar$$ExternalSyntheticLambda37 implements Consumer {
    public static final /* synthetic */ StatusBar$$ExternalSyntheticLambda37 INSTANCE = new StatusBar$$ExternalSyntheticLambda37();

    private /* synthetic */ StatusBar$$ExternalSyntheticLambda37() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        StatusBar.lambda$maybeEscalateHeadsUp$14((NotificationEntry) obj);
    }
}
