package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes2.dex */
final class NotificationVoiceReplyController$notifyHandlersOfReplyAvailability$3 extends Lambda implements Function2<VoiceReplyTarget, VoiceReplyTarget, Boolean> {
    public static final NotificationVoiceReplyController$notifyHandlersOfReplyAvailability$3 INSTANCE = new NotificationVoiceReplyController$notifyHandlersOfReplyAvailability$3();

    NotificationVoiceReplyController$notifyHandlersOfReplyAvailability$3() {
        super(2);
    }

    /* Return type fixed from 'java.lang.Object' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
    @Override // kotlin.jvm.functions.Function2
    public /* bridge */ /* synthetic */ Boolean invoke(VoiceReplyTarget voiceReplyTarget, VoiceReplyTarget voiceReplyTarget2) {
        return Boolean.valueOf(invoke(voiceReplyTarget, voiceReplyTarget2));
    }

    public final boolean invoke(VoiceReplyTarget voiceReplyTarget, VoiceReplyTarget voiceReplyTarget2) {
        Integer num = null;
        Integer valueOf = voiceReplyTarget == null ? null : Integer.valueOf(voiceReplyTarget.getUserId());
        if (voiceReplyTarget2 != null) {
            num = Integer.valueOf(voiceReplyTarget2.getUserId());
        }
        return Intrinsics.areEqual(valueOf, num);
    }
}
