package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Ref$ObjectRef;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
/* compiled from: NotificationVoiceReplyManager.kt */
/* access modifiers changed from: package-private */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$collectLatest$2", f = "NotificationVoiceReplyManager.kt", l = {1014}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyManagerKt$collectLatest$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function2<T, Continuation<? super Unit>, Object> $collector;
    final /* synthetic */ Flow<T> $this_collectLatest;
    int label;
    private /* synthetic */ CoroutineScope p$;

    /* JADX DEBUG: Multi-variable search result rejected for r1v0, resolved type: kotlinx.coroutines.flow.Flow<? extends T> */
    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> */
    /* JADX WARN: Multi-variable type inference failed */
    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerKt$collectLatest$2(Flow<? extends T> flow, Function2<? super T, ? super Continuation<? super Unit>, ? extends Object> function2, Continuation<? super NotificationVoiceReplyManagerKt$collectLatest$2> continuation) {
        super(2, continuation);
        this.$this_collectLatest = flow;
        this.$collector = function2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyManagerKt$collectLatest$2 notificationVoiceReplyManagerKt$collectLatest$2 = new NotificationVoiceReplyManagerKt$collectLatest$2(this.$this_collectLatest, this.$collector, continuation);
        notificationVoiceReplyManagerKt$collectLatest$2.p$ = (CoroutineScope) obj;
        return notificationVoiceReplyManagerKt$collectLatest$2;
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyManagerKt$collectLatest$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object obj2 = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
            Flow<T> flow = this.$this_collectLatest;
            NotificationVoiceReplyManagerKt$collectLatest$2$invokeSuspend$$inlined$collect$1 notificationVoiceReplyManagerKt$collectLatest$2$invokeSuspend$$inlined$collect$1 = new FlowCollector<T>(ref$ObjectRef, this, this.$collector) { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$collectLatest$2$invokeSuspend$$inlined$collect$1
                final /* synthetic */ Function2 $collector$inlined;
                final /* synthetic */ Ref$ObjectRef $job$inlined;
                final /* synthetic */ NotificationVoiceReplyManagerKt$collectLatest$2 this$0;

                {
                    this.$job$inlined = r1;
                    this.this$0 = r2;
                    this.$collector$inlined = r3;
                }

                /* JADX WARN: Type inference failed for: r8v2, types: [T, kotlinx.coroutines.Job] */
                @Override // kotlinx.coroutines.flow.FlowCollector
                public Object emit(Object obj3, Continuation continuation) {
                    Job job = (Job) this.$job$inlined.element;
                    if (job != null) {
                        Job.DefaultImpls.cancel$default(job, null, 1, null);
                    }
                    this.$job$inlined.element = BuildersKt__Builders_commonKt.launch$default(this.this$0.p$, null, null, new NotificationVoiceReplyManagerKt$collectLatest$2$1$1(this.$collector$inlined, obj3, null), 3, null);
                    return Unit.INSTANCE;
                }
            };
            this.label = 1;
            if (flow.collect(notificationVoiceReplyManagerKt$collectLatest$2$invokeSuspend$$inlined$collect$1, this) == obj2) {
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
