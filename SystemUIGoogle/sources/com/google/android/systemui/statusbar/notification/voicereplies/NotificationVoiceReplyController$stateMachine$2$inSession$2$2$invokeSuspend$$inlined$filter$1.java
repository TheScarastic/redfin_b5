package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
/* compiled from: SafeCollector.kt */
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyController$stateMachine$2$inSession$2$2$invokeSuspend$$inlined$filter$1 implements Flow<String> {
    final /* synthetic */ VoiceReplyTarget $target$inlined;
    final /* synthetic */ Flow $this_unsafeTransform$inlined;

    public NotificationVoiceReplyController$stateMachine$2$inSession$2$2$invokeSuspend$$inlined$filter$1(Flow flow, VoiceReplyTarget voiceReplyTarget) {
        this.$this_unsafeTransform$inlined = flow;
        this.$target$inlined = voiceReplyTarget;
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [kotlinx.coroutines.flow.FlowCollector, kotlin.coroutines.Continuation] */
    @Override // kotlinx.coroutines.flow.Flow
    public Object collect(final FlowCollector<? super String> flowCollector, Continuation continuation) {
        Object collect = this.$this_unsafeTransform$inlined.collect(new FlowCollector<String>() { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$inSession$2$2$invokeSuspend$$inlined$filter$1.2
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, kotlin.coroutines.Continuation] */
            @Override // kotlinx.coroutines.flow.FlowCollector
            public Object emit(String str, Continuation continuation2) {
                Object emit;
                return (!Boxing.boxBoolean(Intrinsics.areEqual(str, this.$target$inlined.getNotifKey())).booleanValue() || (emit = flowCollector.emit(str, continuation2)) != IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()) ? Unit.INSTANCE : emit;
            }
        }, continuation);
        return collect == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? collect : Unit.INSTANCE;
    }
}
