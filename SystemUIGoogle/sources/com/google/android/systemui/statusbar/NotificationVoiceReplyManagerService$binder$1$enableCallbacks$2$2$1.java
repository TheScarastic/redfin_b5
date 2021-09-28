package com.google.android.systemui.statusbar;

import com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplySubscription;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
/* compiled from: NotificationVoiceReplyManagerService.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$2$1", f = "NotificationVoiceReplyManagerService.kt", l = {142}, m = "invokeSuspend")
/* loaded from: classes2.dex */
final class NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$2$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ String $content;
    final /* synthetic */ CallbacksHandler $handler;
    final /* synthetic */ VoiceReplySubscription $registration;
    final /* synthetic */ int $token;
    int label;
    private /* synthetic */ CoroutineScope p$;
    final /* synthetic */ NotificationVoiceReplyManagerService$binder$1 this$0;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$2$1(NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1, VoiceReplySubscription voiceReplySubscription, CallbacksHandler callbacksHandler, int i, String str, Continuation<? super NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$2$1> continuation) {
        super(2, continuation);
        this.this$0 = notificationVoiceReplyManagerService$binder$1;
        this.$registration = voiceReplySubscription;
        this.$handler = callbacksHandler;
        this.$token = i;
        this.$content = str;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$2$1 notificationVoiceReplyManagerService$binder$1$enableCallbacks$2$2$1 = new NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$2$1(this.this$0, this.$registration, this.$handler, this.$token, this.$content, continuation);
        notificationVoiceReplyManagerService$binder$1$enableCallbacks$2$2$1.p$ = (CoroutineScope) obj;
        return notificationVoiceReplyManagerService$binder$1$enableCallbacks$2$2$1;
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$2$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object obj2 = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1 = this.this$0;
            VoiceReplySubscription voiceReplySubscription = this.$registration;
            CallbacksHandler callbacksHandler = this.$handler;
            int i2 = this.$token;
            String str = this.$content;
            this.label = 1;
            if (notificationVoiceReplyManagerService$binder$1.startVoiceReply(voiceReplySubscription, callbacksHandler, i2, str, this) == obj2) {
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
