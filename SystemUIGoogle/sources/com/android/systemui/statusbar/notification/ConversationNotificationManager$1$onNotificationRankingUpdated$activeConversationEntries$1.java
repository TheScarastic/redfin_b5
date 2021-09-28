package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
final class ConversationNotificationManager$1$onNotificationRankingUpdated$activeConversationEntries$1 extends Lambda implements Function1<String, NotificationEntry> {
    final /* synthetic */ ConversationNotificationManager this$0;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ConversationNotificationManager$1$onNotificationRankingUpdated$activeConversationEntries$1(ConversationNotificationManager conversationNotificationManager) {
        super(1);
        this.this$0 = conversationNotificationManager;
    }

    public final NotificationEntry invoke(String str) {
        Intrinsics.checkNotNullParameter(str, "it");
        return this.this$0.notificationEntryManager.getActiveNotificationUnfiltered(str);
    }
}
