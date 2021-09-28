package com.google.android.systemui.statusbar;

import androidx.constraintlayout.widget.R$styleable;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.flow.MutableSharedFlow;
/* compiled from: NotificationVoiceReplyManagerService.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$startVoiceReply$1", f = "NotificationVoiceReplyManagerService.kt", l = {R$styleable.Constraint_layout_constraintVertical_weight}, m = "invokeSuspend")
/* loaded from: classes2.dex */
final class NotificationVoiceReplyManagerService$binder$1$startVoiceReply$1 extends SuspendLambda implements Function1<Continuation<? super Unit>, Object> {
    final /* synthetic */ int $sessionToken;
    final /* synthetic */ String $userMessageContent;
    int label;
    final /* synthetic */ NotificationVoiceReplyManagerService this$0;
    final /* synthetic */ NotificationVoiceReplyManagerService$binder$1 this$1;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerService$binder$1$startVoiceReply$1(NotificationVoiceReplyManagerService notificationVoiceReplyManagerService, NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1, int i, String str, Continuation<? super NotificationVoiceReplyManagerService$binder$1$startVoiceReply$1> continuation) {
        super(1, continuation);
        this.this$0 = notificationVoiceReplyManagerService;
        this.this$1 = notificationVoiceReplyManagerService$binder$1;
        this.$sessionToken = i;
        this.$userMessageContent = str;
    }

    public final Continuation<Unit> create(Continuation<?> continuation) {
        return new NotificationVoiceReplyManagerService$binder$1$startVoiceReply$1(this.this$0, this.this$1, this.$sessionToken, this.$userMessageContent, continuation);
    }

    public final Object invoke(Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyManagerService$binder$1$startVoiceReply$1) create(continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object obj2 = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            this.this$0.logger.logStartVoiceReply(this.this$1.getUserId(), this.$sessionToken, this.$userMessageContent);
            MutableSharedFlow<StartVoiceReplyData> startVoiceReplyFlow = this.this$1.getStartVoiceReplyFlow();
            StartVoiceReplyData startVoiceReplyData = new StartVoiceReplyData(this.this$1.getUserId(), this.$sessionToken, this.$userMessageContent);
            this.label = 1;
            if (startVoiceReplyFlow.emit(startVoiceReplyData, this) == obj2) {
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
