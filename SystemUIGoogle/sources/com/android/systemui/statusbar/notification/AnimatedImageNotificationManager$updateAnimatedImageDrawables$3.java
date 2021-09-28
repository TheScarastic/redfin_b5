package com.android.systemui.statusbar.notification;

import android.view.View;
import com.android.internal.widget.MessagingGroup;
import com.android.internal.widget.MessagingLinearLayout;
import com.android.systemui.util.ConvenienceExtensionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.sequences.Sequence;
/* compiled from: ConversationNotifications.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class AnimatedImageNotificationManager$updateAnimatedImageDrawables$3 extends Lambda implements Function1<MessagingGroup, Sequence<? extends View>> {
    public static final AnimatedImageNotificationManager$updateAnimatedImageDrawables$3 INSTANCE = new AnimatedImageNotificationManager$updateAnimatedImageDrawables$3();

    AnimatedImageNotificationManager$updateAnimatedImageDrawables$3() {
        super(1);
    }

    public final Sequence<View> invoke(MessagingGroup messagingGroup) {
        MessagingLinearLayout messageContainer = messagingGroup.getMessageContainer();
        Intrinsics.checkNotNullExpressionValue(messageContainer, "messagingGroup.messageContainer");
        return ConvenienceExtensionsKt.getChildren(messageContainer);
    }
}
