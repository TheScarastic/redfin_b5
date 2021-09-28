package com.google.android.systemui.statusbar;

import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
import kotlinx.coroutines.flow.MutableStateFlow;
/* compiled from: NotificationVoiceReplyManagerService.kt */
/* access modifiers changed from: package-private */
@DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerServiceKt$stateIn$1", f = "NotificationVoiceReplyManagerService.kt", l = {258}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyManagerServiceKt$stateIn$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ MutableStateFlow<T> $stateFlow;
    final /* synthetic */ Flow<T> $this_stateIn;
    int label;
    private /* synthetic */ CoroutineScope p$;

    /* JADX DEBUG: Multi-variable search result rejected for r1v0, resolved type: kotlinx.coroutines.flow.Flow<? extends T> */
    /* JADX WARN: Multi-variable type inference failed */
    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerServiceKt$stateIn$1(Flow<? extends T> flow, MutableStateFlow<T> mutableStateFlow, Continuation<? super NotificationVoiceReplyManagerServiceKt$stateIn$1> continuation) {
        super(2, continuation);
        this.$this_stateIn = flow;
        this.$stateFlow = mutableStateFlow;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyManagerServiceKt$stateIn$1 notificationVoiceReplyManagerServiceKt$stateIn$1 = new NotificationVoiceReplyManagerServiceKt$stateIn$1(this.$this_stateIn, this.$stateFlow, continuation);
        notificationVoiceReplyManagerServiceKt$stateIn$1.p$ = (CoroutineScope) obj;
        return notificationVoiceReplyManagerServiceKt$stateIn$1;
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyManagerServiceKt$stateIn$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object obj2 = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            Flow<T> flow = this.$this_stateIn;
            NotificationVoiceReplyManagerServiceKt$stateIn$1$invokeSuspend$$inlined$collect$1 notificationVoiceReplyManagerServiceKt$stateIn$1$invokeSuspend$$inlined$collect$1 = new FlowCollector<T>(this.$stateFlow) { // from class: com.google.android.systemui.statusbar.NotificationVoiceReplyManagerServiceKt$stateIn$1$invokeSuspend$$inlined$collect$1
                final /* synthetic */ MutableStateFlow $stateFlow$inlined;

                {
                    this.$stateFlow$inlined = r1;
                }

                @Override // kotlinx.coroutines.flow.FlowCollector
                public Object emit(Object obj3, Continuation continuation) {
                    this.$stateFlow$inlined.setValue(obj3);
                    return Unit.INSTANCE;
                }
            };
            this.label = 1;
            if (flow.collect(notificationVoiceReplyManagerServiceKt$stateIn$1$invokeSuspend$$inlined$collect$1, this) == obj2) {
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
