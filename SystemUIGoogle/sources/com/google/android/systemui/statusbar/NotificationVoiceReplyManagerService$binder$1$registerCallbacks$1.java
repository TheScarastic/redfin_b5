package com.google.android.systemui.statusbar;

import android.os.IBinder;
import android.os.RemoteException;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$ObjectRef;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Job;
/* compiled from: NotificationVoiceReplyManagerService.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1", f = "NotificationVoiceReplyManagerService.kt", l = {}, m = "invokeSuspend")
/* loaded from: classes2.dex */
final class NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1 extends SuspendLambda implements Function1<Continuation<? super Unit>, Object> {
    final /* synthetic */ INotificationVoiceReplyServiceCallbacks $callbacks;
    int label;
    final /* synthetic */ NotificationVoiceReplyManagerService this$0;
    final /* synthetic */ NotificationVoiceReplyManagerService$binder$1 this$1;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1(NotificationVoiceReplyManagerService notificationVoiceReplyManagerService, NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1, INotificationVoiceReplyServiceCallbacks iNotificationVoiceReplyServiceCallbacks, Continuation<? super NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1> continuation) {
        super(1, continuation);
        this.this$0 = notificationVoiceReplyManagerService;
        this.this$1 = notificationVoiceReplyManagerService$binder$1;
        this.$callbacks = iNotificationVoiceReplyServiceCallbacks;
    }

    public final Continuation<Unit> create(Continuation<?> continuation) {
        return new NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1(this.this$0, this.this$1, this.$callbacks, continuation);
    }

    public final Object invoke(Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1) create(continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object unused = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (this.label == 0) {
            ResultKt.throwOnFailure(obj);
            this.this$0.logger.logRegisterCallbacks(this.this$1.getUserId());
            CoroutineScope coroutineScope = this.this$0.scope;
            final INotificationVoiceReplyServiceCallbacks iNotificationVoiceReplyServiceCallbacks = this.$callbacks;
            final NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1 = this.this$1;
            Job unused2 = BuildersKt__Builders_commonKt.launch$default(coroutineScope, null, null, new AnonymousClass1(null), 3, null);
            return Unit.INSTANCE;
        }
        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
    }

    /* compiled from: NotificationVoiceReplyManagerService.kt */
    /* access modifiers changed from: package-private */
    @DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1", f = "NotificationVoiceReplyManagerService.kt", l = {}, m = "invokeSuspend")
    /* renamed from: com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;
        private /* synthetic */ CoroutineScope p$;

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj2, Continuation<?> continuation) {
            AnonymousClass1 r0 = new AnonymousClass1(iNotificationVoiceReplyServiceCallbacks, notificationVoiceReplyManagerService$binder$1, continuation);
            r0.p$ = (CoroutineScope) obj2;
            return r0;
        }

        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Type inference failed for: r7v6, types: [com.android.systemui.statusbar.notification.people.Subscription, T] */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Object unused = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
            if (this.label == 0) {
                ResultKt.throwOnFailure(obj);
                Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
                final Job job = BuildersKt__Builders_commonKt.launch$default(this.p$, null, CoroutineStart.LAZY, new NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1$1$cbJob$1(notificationVoiceReplyManagerService$binder$1, iNotificationVoiceReplyServiceCallbacks, ref$ObjectRef, null), 1, null);
                try {
                    IBinder asBinder = iNotificationVoiceReplyServiceCallbacks.asBinder();
                    Intrinsics.checkNotNullExpressionValue(asBinder, "callbacks.asBinder()");
                    ref$ObjectRef.element = NotificationVoiceReplyManagerServiceKt.onDeath(asBinder, new Function0<Unit>() { // from class: com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService.binder.1.registerCallbacks.1.1.1
                        @Override // kotlin.jvm.functions.Function0
                        public final void invoke() {
                            Job.DefaultImpls.cancel$default(job, null, 1, null);
                        }
                    });
                    job.start();
                } catch (RemoteException unused2) {
                    Job.DefaultImpls.cancel$default(job, null, 1, null);
                }
                return Unit.INSTANCE;
            }
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
