package com.android.systemui.statusbar.notification.collection.coordinator;

import android.app.NotificationChannel;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ConversationCoordinator.kt */
/* loaded from: classes.dex */
public final class ConversationCoordinator$notificationPromoter$1 extends NotifPromoter {
    /* access modifiers changed from: package-private */
    public ConversationCoordinator$notificationPromoter$1() {
        super("ConversationCoordinator");
    }

    @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter
    public boolean shouldPromoteToTopLevel(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        NotificationChannel channel = notificationEntry.getChannel();
        return Intrinsics.areEqual(channel == null ? null : Boolean.valueOf(channel.isImportantConversation()), Boolean.TRUE);
    }
}
