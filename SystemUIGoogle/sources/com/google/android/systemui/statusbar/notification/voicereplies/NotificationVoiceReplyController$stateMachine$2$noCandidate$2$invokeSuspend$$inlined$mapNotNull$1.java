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
public final class NotificationVoiceReplyController$stateMachine$2$noCandidate$2$invokeSuspend$$inlined$mapNotNull$1 implements Flow<VoiceReplyTarget> {
    final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine$inlined;
    final /* synthetic */ Flow $this_unsafeTransform$inlined;
    final /* synthetic */ NotificationVoiceReplyController this$0;

    public NotificationVoiceReplyController$stateMachine$2$noCandidate$2$invokeSuspend$$inlined$mapNotNull$1(Flow flow, NotificationVoiceReplyController notificationVoiceReplyController, NotificationVoiceReplyController.Connection connection) {
        this.$this_unsafeTransform$inlined = flow;
        this.this$0 = notificationVoiceReplyController;
        this.$this_stateMachine$inlined = connection;
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [kotlinx.coroutines.flow.FlowCollector, kotlin.coroutines.Continuation] */
    @Override // kotlinx.coroutines.flow.Flow
    public Object collect(final FlowCollector<? super VoiceReplyTarget> flowCollector, Continuation continuation) {
        Object collect = this.$this_unsafeTransform$inlined.collect(new FlowCollector<NotificationEntry>() { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$noCandidate$2$invokeSuspend$$inlined$mapNotNull$1.2
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, kotlin.coroutines.Continuation] */
            @Override // kotlinx.coroutines.flow.FlowCollector
            public Object emit(NotificationEntry notificationEntry, Continuation continuation2) {
                FlowCollector flowCollector2 = flowCollector;
                NotificationVoiceReplyController$stateMachine$2$noCandidate$2$invokeSuspend$$inlined$mapNotNull$1 notificationVoiceReplyController$stateMachine$2$noCandidate$2$invokeSuspend$$inlined$mapNotNull$1 = this;
                VoiceReplyTarget extractCandidate$default = NotificationVoiceReplyController.extractCandidate$default(notificationVoiceReplyController$stateMachine$2$noCandidate$2$invokeSuspend$$inlined$mapNotNull$1.this$0, notificationVoiceReplyController$stateMachine$2$noCandidate$2$invokeSuspend$$inlined$mapNotNull$1.$this_stateMachine$inlined, notificationEntry, 0, null, 6, null);
                if (extractCandidate$default == null) {
                    return extractCandidate$default == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? extractCandidate$default : Unit.INSTANCE;
                }
                Object emit = flowCollector2.emit(extractCandidate$default, continuation2);
                return emit == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? emit : Unit.INSTANCE;
            }
        }, continuation);
        return collect == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? collect : Unit.INSTANCE;
    }
}
