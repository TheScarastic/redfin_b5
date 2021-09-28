package com.android.systemui.statusbar.notification;

import com.android.internal.widget.ConversationLayout;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
final class ConversationNotificationManager$1$onNotificationRankingUpdated$3 extends Lambda implements Function1<ConversationLayout, Boolean> {
    final /* synthetic */ boolean $important;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ConversationNotificationManager$1$onNotificationRankingUpdated$3(boolean z) {
        super(1);
        this.$important = z;
    }

    /* Return type fixed from 'java.lang.Object' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
    @Override // kotlin.jvm.functions.Function1
    public /* bridge */ /* synthetic */ Boolean invoke(ConversationLayout conversationLayout) {
        return Boolean.valueOf(invoke(conversationLayout));
    }

    public final boolean invoke(ConversationLayout conversationLayout) {
        Intrinsics.checkNotNullParameter(conversationLayout, "it");
        return conversationLayout.isImportantConversation() == this.$important;
    }
}
