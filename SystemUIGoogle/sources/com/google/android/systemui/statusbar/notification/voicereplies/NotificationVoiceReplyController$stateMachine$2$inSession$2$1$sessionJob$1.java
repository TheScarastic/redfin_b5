package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$inSession$2$1$sessionJob$1", f = "NotificationVoiceReplyManager.kt", l = {466}, m = "invokeSuspend")
/* loaded from: classes2.dex */
final class NotificationVoiceReplyController$stateMachine$2$inSession$2$1$sessionJob$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function2<Session, Continuation<? super Unit>, Object> $block;
    final /* synthetic */ NotificationVoiceReplyController$stateMachine$2$inSession$2$1$session$1 $session;
    int label;
    private /* synthetic */ CoroutineScope p$;
    final /* synthetic */ NotificationVoiceReplyController this$0;

    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: kotlin.jvm.functions.Function2<? super com.google.android.systemui.statusbar.notification.voicereplies.Session, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> */
    /* JADX WARN: Multi-variable type inference failed */
    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$stateMachine$2$inSession$2$1$sessionJob$1(NotificationVoiceReplyController notificationVoiceReplyController, Function2<? super Session, ? super Continuation<? super Unit>, ? extends Object> function2, NotificationVoiceReplyController$stateMachine$2$inSession$2$1$session$1 notificationVoiceReplyController$stateMachine$2$inSession$2$1$session$1, Continuation<? super NotificationVoiceReplyController$stateMachine$2$inSession$2$1$sessionJob$1> continuation) {
        super(2, continuation);
        this.this$0 = notificationVoiceReplyController;
        this.$block = function2;
        this.$session = notificationVoiceReplyController$stateMachine$2$inSession$2$1$session$1;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyController$stateMachine$2$inSession$2$1$sessionJob$1 notificationVoiceReplyController$stateMachine$2$inSession$2$1$sessionJob$1 = new NotificationVoiceReplyController$stateMachine$2$inSession$2$1$sessionJob$1(this.this$0, this.$block, this.$session, continuation);
        notificationVoiceReplyController$stateMachine$2$inSession$2$1$sessionJob$1.p$ = (CoroutineScope) obj;
        return notificationVoiceReplyController$stateMachine$2$inSession$2$1$sessionJob$1;
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyController$stateMachine$2$inSession$2$1$sessionJob$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object obj2 = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            LogBuffer logBuffer = this.this$0.logger.getLogBuffer();
            LogLevel logLevel = LogLevel.DEBUG;
            NotificationVoiceReplyLogger$logStatic$2 notificationVoiceReplyLogger$logStatic$2 = new NotificationVoiceReplyLogger$logStatic$2("inSession: launching session block");
            if (!logBuffer.getFrozen()) {
                logBuffer.push(logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logStatic$2));
            }
            Function2<Session, Continuation<? super Unit>, Object> function2 = this.$block;
            NotificationVoiceReplyController$stateMachine$2$inSession$2$1$session$1 notificationVoiceReplyController$stateMachine$2$inSession$2$1$session$1 = this.$session;
            this.label = 1;
            if (function2.invoke(notificationVoiceReplyController$stateMachine$2$inSession$2$1$session$1, this) == obj2) {
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
