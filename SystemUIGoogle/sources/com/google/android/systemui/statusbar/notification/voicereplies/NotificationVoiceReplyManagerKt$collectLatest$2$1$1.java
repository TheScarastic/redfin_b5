package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$collectLatest$2$1$1", f = "NotificationVoiceReplyManager.kt", l = {876}, m = "invokeSuspend")
/* loaded from: classes2.dex */
final class NotificationVoiceReplyManagerKt$collectLatest$2$1$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function2<T, Continuation<? super Unit>, Object> $collector;
    final /* synthetic */ T $it;
    int label;
    private /* synthetic */ CoroutineScope p$;

    /* JADX DEBUG: Multi-variable search result rejected for r1v0, resolved type: kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> */
    /* JADX WARN: Multi-variable type inference failed */
    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerKt$collectLatest$2$1$1(Function2<? super T, ? super Continuation<? super Unit>, ? extends Object> function2, T t, Continuation<? super NotificationVoiceReplyManagerKt$collectLatest$2$1$1> continuation) {
        super(2, continuation);
        this.$collector = function2;
        this.$it = t;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyManagerKt$collectLatest$2$1$1 notificationVoiceReplyManagerKt$collectLatest$2$1$1 = new NotificationVoiceReplyManagerKt$collectLatest$2$1$1(this.$collector, this.$it, continuation);
        notificationVoiceReplyManagerKt$collectLatest$2$1$1.p$ = (CoroutineScope) obj;
        return notificationVoiceReplyManagerKt$collectLatest$2$1$1;
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyManagerKt$collectLatest$2$1$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object obj2 = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            Function2<T, Continuation<? super Unit>, Object> function2 = this.$collector;
            T t = this.$it;
            this.label = 1;
            if (function2.invoke(t, this) == obj2) {
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
