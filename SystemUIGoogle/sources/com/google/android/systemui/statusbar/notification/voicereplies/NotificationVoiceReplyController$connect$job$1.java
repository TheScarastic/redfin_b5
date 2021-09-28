package com.google.android.systemui.statusbar.notification.voicereplies;

import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$connect$job$1", f = "NotificationVoiceReplyManager.kt", l = {180}, m = "invokeSuspend")
/* loaded from: classes2.dex */
final class NotificationVoiceReplyController$connect$job$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ NotificationVoiceReplyController.Connection $connection;
    int label;
    private /* synthetic */ CoroutineScope p$;
    final /* synthetic */ NotificationVoiceReplyController this$0;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$connect$job$1(NotificationVoiceReplyController notificationVoiceReplyController, NotificationVoiceReplyController.Connection connection, Continuation<? super NotificationVoiceReplyController$connect$job$1> continuation) {
        super(2, continuation);
        this.this$0 = notificationVoiceReplyController;
        this.$connection = connection;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyController$connect$job$1 notificationVoiceReplyController$connect$job$1 = new NotificationVoiceReplyController$connect$job$1(this.this$0, this.$connection, continuation);
        notificationVoiceReplyController$connect$job$1.p$ = (CoroutineScope) obj;
        return notificationVoiceReplyController$connect$job$1;
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyController$connect$job$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object obj2 = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            NotificationVoiceReplyController notificationVoiceReplyController = this.this$0;
            NotificationVoiceReplyController.Connection connection = this.$connection;
            this.label = 1;
            if (notificationVoiceReplyController.stateMachine(connection, this) == obj2) {
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
