package com.google.android.systemui.statusbar;

import androidx.appcompat.R$styleable;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
/* compiled from: NotificationVoiceReplyManagerService.kt */
/* access modifiers changed from: package-private */
@DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$4", f = "NotificationVoiceReplyManagerService.kt", l = {R$styleable.AppCompatTheme_windowActionModeOverlay}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$4 extends SuspendLambda implements Function2<Pair<? extends Integer, ? extends Integer>, Continuation<? super Unit>, Object> {
    final /* synthetic */ INotificationVoiceReplyServiceCallbacks $callbacks;
    /* synthetic */ Pair<Integer, Integer> $dstr$_u24__u24$setting;
    int label;
    final /* synthetic */ NotificationVoiceReplyManagerService$binder$1 this$0;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$4(NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1, INotificationVoiceReplyServiceCallbacks iNotificationVoiceReplyServiceCallbacks, Continuation<? super NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$4> continuation) {
        super(2, continuation);
        this.this$0 = notificationVoiceReplyManagerService$binder$1;
        this.$callbacks = iNotificationVoiceReplyServiceCallbacks;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$4 notificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$4 = new NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$4(this.this$0, this.$callbacks, continuation);
        notificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$4.$dstr$_u24__u24$setting = (Pair) obj;
        return notificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$4;
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
    @Override // kotlin.jvm.functions.Function2
    public /* bridge */ /* synthetic */ Object invoke(Pair<? extends Integer, ? extends Integer> pair, Continuation<? super Unit> continuation) {
        return invoke((Pair<Integer, Integer>) pair, continuation);
    }

    public final Object invoke(Pair<Integer, Integer> pair, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$4) create(pair, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object obj2 = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            int intValue = this.$dstr$_u24__u24$setting.component2().intValue();
            if (intValue != 0) {
                boolean z = intValue == 2;
                NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1 = this.this$0;
                INotificationVoiceReplyServiceCallbacks iNotificationVoiceReplyServiceCallbacks = this.$callbacks;
                this.label = 1;
                if (notificationVoiceReplyManagerService$binder$1.enableCallbacks(iNotificationVoiceReplyServiceCallbacks, z, this) == obj2) {
                    return obj2;
                }
            }
        } else if (i == 1) {
            ResultKt.throwOnFailure(obj);
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return Unit.INSTANCE;
    }
}
