package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.notification.people.Subscription;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes2.dex */
public interface VoiceReplySubscription extends Subscription {
    Object startVoiceReply(int i, String str, Function0<Unit> function0, Function2<? super Session, ? super Continuation<? super Unit>, ? extends Object> function2, Continuation<? super Unit> continuation);
}
