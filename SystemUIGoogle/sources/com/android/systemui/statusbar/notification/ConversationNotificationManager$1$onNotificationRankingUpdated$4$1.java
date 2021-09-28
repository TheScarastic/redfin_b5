package com.android.systemui.statusbar.notification;

import com.android.internal.widget.ConversationLayout;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
final class ConversationNotificationManager$1$onNotificationRankingUpdated$4$1 implements Runnable {
    final /* synthetic */ boolean $important;
    final /* synthetic */ ConversationLayout $layout;

    /* access modifiers changed from: package-private */
    public ConversationNotificationManager$1$onNotificationRankingUpdated$4$1(ConversationLayout conversationLayout, boolean z) {
        this.$layout = conversationLayout;
        this.$important = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.$layout.setIsImportantConversation(this.$important, true);
    }
}
