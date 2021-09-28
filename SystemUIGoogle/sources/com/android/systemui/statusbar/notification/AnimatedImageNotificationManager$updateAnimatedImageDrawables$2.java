package com.android.systemui.statusbar.notification;

import android.view.View;
import com.android.internal.widget.ConversationLayout;
import com.android.internal.widget.MessagingGroup;
import com.android.internal.widget.MessagingLayout;
import java.util.ArrayList;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequencesKt;
/* compiled from: ConversationNotifications.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class AnimatedImageNotificationManager$updateAnimatedImageDrawables$2 extends Lambda implements Function1<View, Sequence<? extends MessagingGroup>> {
    public static final AnimatedImageNotificationManager$updateAnimatedImageDrawables$2 INSTANCE = new AnimatedImageNotificationManager$updateAnimatedImageDrawables$2();

    AnimatedImageNotificationManager$updateAnimatedImageDrawables$2() {
        super(1);
    }

    public final Sequence<MessagingGroup> invoke(View view) {
        ArrayList messagingGroups;
        ArrayList messagingGroups2;
        Sequence<MessagingGroup> sequence = null;
        ConversationLayout conversationLayout = view instanceof ConversationLayout ? (ConversationLayout) view : null;
        Sequence<MessagingGroup> sequence2 = (conversationLayout == null || (messagingGroups2 = conversationLayout.getMessagingGroups()) == null) ? null : CollectionsKt___CollectionsKt.asSequence(messagingGroups2);
        if (sequence2 != null) {
            return sequence2;
        }
        MessagingLayout messagingLayout = view instanceof MessagingLayout ? (MessagingLayout) view : null;
        if (!(messagingLayout == null || (messagingGroups = messagingLayout.getMessagingGroups()) == null)) {
            sequence = CollectionsKt___CollectionsKt.asSequence(messagingGroups);
        }
        return sequence == null ? SequencesKt__SequencesKt.emptySequence() : sequence;
    }
}
