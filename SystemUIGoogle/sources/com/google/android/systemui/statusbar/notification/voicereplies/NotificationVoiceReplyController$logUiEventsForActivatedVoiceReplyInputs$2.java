package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.policy.RemoteInputView;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.flow.Flow;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2", f = "NotificationVoiceReplyManager.kt", l = {1014}, m = "invokeSuspend")
/* loaded from: classes2.dex */
final class NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ Flow<Pair<String, RemoteInputView>> $remoteInputViewActivatedForVoiceReply;
    final /* synthetic */ NotificationVoiceReplyController.Connection $this_logUiEventsForActivatedVoiceReplyInputs;
    int label;
    private /* synthetic */ CoroutineScope p$;
    final /* synthetic */ NotificationVoiceReplyController this$0;

    /* JADX DEBUG: Multi-variable search result rejected for r1v0, resolved type: kotlinx.coroutines.flow.Flow<? extends kotlin.Pair<java.lang.String, ? extends com.android.systemui.statusbar.policy.RemoteInputView>> */
    /* JADX WARN: Multi-variable type inference failed */
    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2(Flow<? extends Pair<String, ? extends RemoteInputView>> flow, NotificationVoiceReplyController.Connection connection, NotificationVoiceReplyController notificationVoiceReplyController, Continuation<? super NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2> continuation) {
        super(2, continuation);
        this.$remoteInputViewActivatedForVoiceReply = flow;
        this.$this_logUiEventsForActivatedVoiceReplyInputs = connection;
        this.this$0 = notificationVoiceReplyController;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2 notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2 = new NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2(this.$remoteInputViewActivatedForVoiceReply, this.$this_logUiEventsForActivatedVoiceReplyInputs, this.this$0, continuation);
        notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2.p$ = (CoroutineScope) obj;
        return notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2;
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object obj2 = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            Flow<Pair<String, RemoteInputView>> flow = this.$remoteInputViewActivatedForVoiceReply;
            NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$invokeSuspend$$inlined$collect$1 notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$invokeSuspend$$inlined$collect$1 = new NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$invokeSuspend$$inlined$collect$1(this, this.$this_logUiEventsForActivatedVoiceReplyInputs, this.this$0, flow);
            this.label = 1;
            if (flow.collect(notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$invokeSuspend$$inlined$collect$1, this) == obj2) {
                return obj2;
            }
        } else if (i == 1) {
            ResultKt.throwOnFailure(obj);
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return Unit.INSTANCE;
    }
}
