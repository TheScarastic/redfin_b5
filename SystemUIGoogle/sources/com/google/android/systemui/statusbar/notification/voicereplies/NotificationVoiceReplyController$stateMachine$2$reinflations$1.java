package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$reinflations$1", f = "NotificationVoiceReplyManager.kt", l = {}, m = "invokeSuspend")
/* loaded from: classes2.dex */
final class NotificationVoiceReplyController$stateMachine$2$reinflations$1 extends SuspendLambda implements Function2<Pair<? extends NotificationEntry, ? extends String>, Continuation<? super Unit>, Object> {
    /* synthetic */ Pair<NotificationEntry, String> $dstr$entry$reason;
    int label;
    final /* synthetic */ NotificationVoiceReplyController this$0;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$stateMachine$2$reinflations$1(NotificationVoiceReplyController notificationVoiceReplyController, Continuation<? super NotificationVoiceReplyController$stateMachine$2$reinflations$1> continuation) {
        super(2, continuation);
        this.this$0 = notificationVoiceReplyController;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyController$stateMachine$2$reinflations$1 notificationVoiceReplyController$stateMachine$2$reinflations$1 = new NotificationVoiceReplyController$stateMachine$2$reinflations$1(this.this$0, continuation);
        notificationVoiceReplyController$stateMachine$2$reinflations$1.$dstr$entry$reason = (Pair) obj;
        return notificationVoiceReplyController$stateMachine$2$reinflations$1;
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
    @Override // kotlin.jvm.functions.Function2
    public /* bridge */ /* synthetic */ Object invoke(Pair<? extends NotificationEntry, ? extends String> pair, Continuation<? super Unit> continuation) {
        return invoke((Pair<NotificationEntry, String>) pair, continuation);
    }

    public final Object invoke(Pair<NotificationEntry, String> pair, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyController$stateMachine$2$reinflations$1) create(pair, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object unused = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (this.label == 0) {
            ResultKt.throwOnFailure(obj);
            NotificationVoiceReplyLogger notificationVoiceReplyLogger = this.this$0.logger;
            String key = this.$dstr$entry$reason.component1().getKey();
            Intrinsics.checkNotNullExpressionValue(key, "entry.key");
            notificationVoiceReplyLogger.logReinflation(key, this.$dstr$entry$reason.component2());
            return Unit.INSTANCE;
        }
        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
    }
}
