package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Ref$ObjectRef;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
/* compiled from: NotificationVoiceReplyManager.kt */
/* access modifiers changed from: package-private */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$distinctUntilChanged$2", f = "NotificationVoiceReplyManager.kt", l = {1014}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyManagerKt$distinctUntilChanged$2 extends SuspendLambda implements Function2<FlowCollector<? super T>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function2<T, T, Boolean> $areEqual;
    final /* synthetic */ Flow<T> $this_distinctUntilChanged;
    int label;
    private /* synthetic */ FlowCollector<T> p$;

    /* JADX DEBUG: Multi-variable search result rejected for r1v0, resolved type: kotlinx.coroutines.flow.Flow<? extends T> */
    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: kotlin.jvm.functions.Function2<? super T, ? super T, java.lang.Boolean> */
    /* JADX WARN: Multi-variable type inference failed */
    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerKt$distinctUntilChanged$2(Flow<? extends T> flow, Function2<? super T, ? super T, Boolean> function2, Continuation<? super NotificationVoiceReplyManagerKt$distinctUntilChanged$2> continuation) {
        super(2, continuation);
        this.$this_distinctUntilChanged = flow;
        this.$areEqual = function2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyManagerKt$distinctUntilChanged$2 notificationVoiceReplyManagerKt$distinctUntilChanged$2 = new NotificationVoiceReplyManagerKt$distinctUntilChanged$2(this.$this_distinctUntilChanged, this.$areEqual, continuation);
        notificationVoiceReplyManagerKt$distinctUntilChanged$2.p$ = (FlowCollector) obj;
        return notificationVoiceReplyManagerKt$distinctUntilChanged$2;
    }

    public final Object invoke(FlowCollector<? super T> flowCollector, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyManagerKt$distinctUntilChanged$2) create(flowCollector, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [T, com.google.android.systemui.statusbar.notification.voicereplies.NO_VALUE] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object obj2 = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
            ref$ObjectRef.element = NO_VALUE.INSTANCE;
            Flow<T> flow = this.$this_distinctUntilChanged;
            NotificationVoiceReplyManagerKt$distinctUntilChanged$2$invokeSuspend$$inlined$collect$1 notificationVoiceReplyManagerKt$distinctUntilChanged$2$invokeSuspend$$inlined$collect$1 = new FlowCollector<T>(ref$ObjectRef, this.$areEqual, this) { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$distinctUntilChanged$2$invokeSuspend$$inlined$collect$1
                final /* synthetic */ Function2 $areEqual$inlined;
                final /* synthetic */ Ref$ObjectRef $prev$inlined;
                final /* synthetic */ NotificationVoiceReplyManagerKt$distinctUntilChanged$2 this$0;

                {
                    this.$prev$inlined = r1;
                    this.$areEqual$inlined = r2;
                    this.this$0 = r3;
                }

                /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: java.lang.Object */
                /* JADX WARN: Multi-variable type inference failed */
                @Override // kotlinx.coroutines.flow.FlowCollector
                public Object emit(Object obj3, Continuation continuation) {
                    T t = this.$prev$inlined.element;
                    if (t != NO_VALUE.INSTANCE && ((Boolean) this.$areEqual$inlined.invoke(t, obj3)).booleanValue()) {
                        return Unit.INSTANCE;
                    }
                    this.$prev$inlined.element = obj3;
                    Object emit = NotificationVoiceReplyManagerKt$distinctUntilChanged$2.access$getP$$p(this.this$0).emit(obj3, continuation);
                    return emit == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? emit : Unit.INSTANCE;
                }
            };
            this.label = 1;
            if (flow.collect(notificationVoiceReplyManagerKt$distinctUntilChanged$2$invokeSuspend$$inlined$collect$1, this) == obj2) {
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
