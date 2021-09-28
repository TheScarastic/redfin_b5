package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
/* compiled from: SafeCollector.kt */
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$1 implements Flow<VoiceReplyState> {
    final /* synthetic */ VoiceReplyTarget $candidate$inlined;
    final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine$inlined;
    final /* synthetic */ Flow $this_unsafeTransform$inlined;
    final /* synthetic */ NotificationVoiceReplyController this$0;

    public NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$1(Flow flow, NotificationVoiceReplyController notificationVoiceReplyController, NotificationVoiceReplyController.Connection connection, VoiceReplyTarget voiceReplyTarget) {
        this.$this_unsafeTransform$inlined = flow;
        this.this$0 = notificationVoiceReplyController;
        this.$this_stateMachine$inlined = connection;
        this.$candidate$inlined = voiceReplyTarget;
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [kotlinx.coroutines.flow.FlowCollector, kotlin.coroutines.Continuation] */
    @Override // kotlinx.coroutines.flow.Flow
    public Object collect(final FlowCollector<? super VoiceReplyState> flowCollector, Continuation continuation) {
        Object collect = this.$this_unsafeTransform$inlined.collect(new FlowCollector<NotificationEntry>() { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$1.2
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, kotlin.coroutines.Continuation] */
            @Override // kotlinx.coroutines.flow.FlowCollector
            public Object emit(NotificationEntry notificationEntry, Continuation continuation2) {
                FlowCollector flowCollector2 = flowCollector;
                NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$1 notificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$1 = this;
                VoiceReplyState voiceReplyState = notificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$1.this$0.extractNewerCandidate(notificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$1.$this_stateMachine$inlined, notificationVoiceReplyController$stateMachine$2$hasCandidate$2$2$invokeSuspend$$inlined$mapNotNull$1.$candidate$inlined, notificationEntry);
                if (voiceReplyState == null) {
                    return voiceReplyState == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? voiceReplyState : Unit.INSTANCE;
                }
                Object emit = flowCollector2.emit(voiceReplyState, continuation2);
                return emit == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? emit : Unit.INSTANCE;
            }
        }, continuation);
        return collect == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? collect : Unit.INSTANCE;
    }
}
