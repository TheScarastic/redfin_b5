package com.google.android.systemui.statusbar;

import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
/* compiled from: NotificationVoiceReplyManagerService.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$onCreate$1", f = "NotificationVoiceReplyManagerService.kt", l = {169}, m = "invokeSuspend")
/* loaded from: classes2.dex */
final class NotificationVoiceReplyManagerService$onCreate$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    int label;
    private /* synthetic */ CoroutineScope p$;
    final /* synthetic */ NotificationVoiceReplyManagerService this$0;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerService$onCreate$1(NotificationVoiceReplyManagerService notificationVoiceReplyManagerService, Continuation<? super NotificationVoiceReplyManagerService$onCreate$1> continuation) {
        super(2, continuation);
        this.this$0 = notificationVoiceReplyManagerService;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyManagerService$onCreate$1 notificationVoiceReplyManagerService$onCreate$1 = new NotificationVoiceReplyManagerService$onCreate$1(this.this$0, continuation);
        notificationVoiceReplyManagerService$onCreate$1.p$ = (CoroutineScope) obj;
        return notificationVoiceReplyManagerService$onCreate$1;
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyManagerService$onCreate$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object obj2 = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            NotificationVoiceReplyManagerService notificationVoiceReplyManagerService = this.this$0;
            this.label = 1;
            if (notificationVoiceReplyManagerService.serializeIncomingIPCs(this) == obj2) {
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
