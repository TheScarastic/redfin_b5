package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.ConversationNotificationManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ConversationNotifications.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ConversationNotificationManager$1$onEntryInflated$1 implements ExpandableNotificationRow.OnExpansionChangedListener {
    final /* synthetic */ NotificationEntry $entry;
    final /* synthetic */ ConversationNotificationManager this$0;

    /* access modifiers changed from: package-private */
    public ConversationNotificationManager$1$onEntryInflated$1(NotificationEntry notificationEntry, ConversationNotificationManager conversationNotificationManager) {
        this.$entry = notificationEntry;
        this.this$0 = conversationNotificationManager;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.OnExpansionChangedListener
    public final void onExpansionChanged(final boolean z) {
        ExpandableNotificationRow row = this.$entry.getRow();
        if (!Intrinsics.areEqual(row == null ? null : Boolean.valueOf(row.isShown()), Boolean.TRUE) || !z) {
            ConversationNotificationManager.AnonymousClass1.onEntryInflated$updateCount(this.this$0, this.$entry, z);
            return;
        }
        ExpandableNotificationRow row2 = this.$entry.getRow();
        final ConversationNotificationManager conversationNotificationManager = this.this$0;
        final NotificationEntry notificationEntry = this.$entry;
        row2.performOnIntrinsicHeightReached(new Runnable() { // from class: com.android.systemui.statusbar.notification.ConversationNotificationManager$1$onEntryInflated$1.1
            @Override // java.lang.Runnable
            public final void run() {
                ConversationNotificationManager.AnonymousClass1.onEntryInflated$updateCount(conversationNotificationManager, notificationEntry, z);
            }
        });
    }
}
