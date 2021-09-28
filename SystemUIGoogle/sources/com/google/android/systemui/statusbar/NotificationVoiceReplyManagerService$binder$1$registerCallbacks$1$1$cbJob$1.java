package com.google.android.systemui.statusbar;

import com.android.systemui.statusbar.notification.people.Subscription;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$ObjectRef;
import kotlinx.coroutines.CoroutineScope;
/* compiled from: NotificationVoiceReplyManagerService.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1$cbJob$1", f = "NotificationVoiceReplyManagerService.kt", l = {65}, m = "invokeSuspend")
/* loaded from: classes2.dex */
final class NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1$cbJob$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ INotificationVoiceReplyServiceCallbacks $callbacks;
    final /* synthetic */ Ref$ObjectRef<Subscription> $onDeathSub;
    int label;
    private /* synthetic */ CoroutineScope p$;
    final /* synthetic */ NotificationVoiceReplyManagerService$binder$1 this$0;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1$cbJob$1(NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1, INotificationVoiceReplyServiceCallbacks iNotificationVoiceReplyServiceCallbacks, Ref$ObjectRef<Subscription> ref$ObjectRef, Continuation<? super NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1$cbJob$1> continuation) {
        super(2, continuation);
        this.this$0 = notificationVoiceReplyManagerService$binder$1;
        this.$callbacks = iNotificationVoiceReplyServiceCallbacks;
        this.$onDeathSub = ref$ObjectRef;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1$cbJob$1 notificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1$cbJob$1 = new NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1$cbJob$1(this.this$0, this.$callbacks, this.$onDeathSub, continuation);
        notificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1$cbJob$1.p$ = (CoroutineScope) obj;
        return notificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1$cbJob$1;
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1$cbJob$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object obj2 = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1 = this.this$0;
            INotificationVoiceReplyServiceCallbacks iNotificationVoiceReplyServiceCallbacks = this.$callbacks;
            this.label = 1;
            if (notificationVoiceReplyManagerService$binder$1.registerCallbacksWhenEnabled(iNotificationVoiceReplyServiceCallbacks, this) == obj2) {
                return obj2;
            }
        } else if (i == 1) {
            ResultKt.throwOnFailure(obj);
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        Subscription subscription = this.$onDeathSub.element;
        if (subscription != null) {
            subscription.unsubscribe();
            return Unit.INSTANCE;
        }
        Intrinsics.throwUninitializedPropertyAccessException("onDeathSub");
        throw null;
    }
}
