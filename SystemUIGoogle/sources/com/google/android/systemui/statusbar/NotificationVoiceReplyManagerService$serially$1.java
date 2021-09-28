package com.google.android.systemui.statusbar;

import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.channels.Channel;
/* compiled from: NotificationVoiceReplyManagerService.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$serially$1", f = "NotificationVoiceReplyManagerService.kt", l = {185}, m = "invokeSuspend")
/* loaded from: classes2.dex */
final class NotificationVoiceReplyManagerService$serially$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function1<Continuation<? super Unit>, Object> $block;
    int label;
    private /* synthetic */ CoroutineScope p$;
    final /* synthetic */ NotificationVoiceReplyManagerService this$0;

    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> */
    /* JADX WARN: Multi-variable type inference failed */
    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerService$serially$1(NotificationVoiceReplyManagerService notificationVoiceReplyManagerService, Function1<? super Continuation<? super Unit>, ? extends Object> function1, Continuation<? super NotificationVoiceReplyManagerService$serially$1> continuation) {
        super(2, continuation);
        this.this$0 = notificationVoiceReplyManagerService;
        this.$block = function1;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyManagerService$serially$1 notificationVoiceReplyManagerService$serially$1 = new NotificationVoiceReplyManagerService$serially$1(this.this$0, this.$block, continuation);
        notificationVoiceReplyManagerService$serially$1.p$ = (CoroutineScope) obj;
        return notificationVoiceReplyManagerService$serially$1;
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyManagerService$serially$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object obj2 = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            Channel channel = this.this$0.serializer;
            Function1<Continuation<? super Unit>, Object> function1 = this.$block;
            this.label = 1;
            if (channel.send(function1, this) == obj2) {
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
