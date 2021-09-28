package com.google.android.systemui.statusbar;

import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
/* compiled from: NotificationVoiceReplyManagerService.kt */
/* access modifiers changed from: package-private */
@DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerServiceKt$startingWith$1$1", f = "NotificationVoiceReplyManagerService.kt", l = {204, 258}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyManagerServiceKt$startingWith$1$1 extends SuspendLambda implements Function2<FlowCollector<? super T>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Flow<T> $this_run;
    final /* synthetic */ T $value;
    int label;
    private /* synthetic */ FlowCollector<T> p$;

    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: kotlinx.coroutines.flow.Flow<? extends T> */
    /* JADX WARN: Multi-variable type inference failed */
    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerServiceKt$startingWith$1$1(T t, Flow<? extends T> flow, Continuation<? super NotificationVoiceReplyManagerServiceKt$startingWith$1$1> continuation) {
        super(2, continuation);
        this.$value = t;
        this.$this_run = flow;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyManagerServiceKt$startingWith$1$1 notificationVoiceReplyManagerServiceKt$startingWith$1$1 = new NotificationVoiceReplyManagerServiceKt$startingWith$1$1(this.$value, this.$this_run, continuation);
        notificationVoiceReplyManagerServiceKt$startingWith$1$1.p$ = (FlowCollector) obj;
        return notificationVoiceReplyManagerServiceKt$startingWith$1$1;
    }

    public final Object invoke(FlowCollector<? super T> flowCollector, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyManagerServiceKt$startingWith$1$1) create(flowCollector, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object obj2 = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            FlowCollector<T> flowCollector = this.p$;
            T t = this.$value;
            this.label = 1;
            if (flowCollector.emit(t, this) == obj2) {
                return obj2;
            }
        } else if (i == 1) {
            ResultKt.throwOnFailure(obj);
        } else if (i == 2) {
            ResultKt.throwOnFailure(obj);
            return Unit.INSTANCE;
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        Flow<T> flow = this.$this_run;
        NotificationVoiceReplyManagerServiceKt$startingWith$1$1$invokeSuspend$$inlined$collect$1 notificationVoiceReplyManagerServiceKt$startingWith$1$1$invokeSuspend$$inlined$collect$1 = new FlowCollector<T>(this.p$) { // from class: com.google.android.systemui.statusbar.NotificationVoiceReplyManagerServiceKt$startingWith$1$1$invokeSuspend$$inlined$collect$1
            final /* synthetic */ FlowCollector $receiver$inlined;

            {
                this.$receiver$inlined = r1;
            }

            @Override // kotlinx.coroutines.flow.FlowCollector
            public Object emit(Object obj3, Continuation continuation) {
                Object emit = this.$receiver$inlined.emit(obj3, continuation);
                return emit == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? emit : Unit.INSTANCE;
            }
        };
        this.label = 2;
        if (flow.collect(notificationVoiceReplyManagerServiceKt$startingWith$1$1$invokeSuspend$$inlined$collect$1, this) == obj2) {
            return obj2;
        }
        return Unit.INSTANCE;
    }
}
