package com.android.systemui.statusbar;

import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationMediaManager$3$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ String f$0;

    public /* synthetic */ NotificationMediaManager$3$$ExternalSyntheticLambda1(String str) {
        this.f$0 = str;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return NotificationMediaManager.AnonymousClass3.lambda$onMediaDataRemoved$0(this.f$0, (NotificationEntry) obj);
    }
}
