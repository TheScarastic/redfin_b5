package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.ConversationNotificationManager;
import java.util.function.BiFunction;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ConversationNotifications.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ConversationNotificationManager$resetCount$1 implements BiFunction<String, ConversationNotificationManager.ConversationState, ConversationNotificationManager.ConversationState> {
    public static final ConversationNotificationManager$resetCount$1 INSTANCE = new ConversationNotificationManager$resetCount$1();

    ConversationNotificationManager$resetCount$1() {
    }

    public final ConversationNotificationManager.ConversationState apply(String str, ConversationNotificationManager.ConversationState conversationState) {
        Intrinsics.checkNotNullParameter(str, "$noName_0");
        if (conversationState == null) {
            return null;
        }
        return ConversationNotificationManager.ConversationState.copy$default(conversationState, 0, null, 2, null);
    }
}
