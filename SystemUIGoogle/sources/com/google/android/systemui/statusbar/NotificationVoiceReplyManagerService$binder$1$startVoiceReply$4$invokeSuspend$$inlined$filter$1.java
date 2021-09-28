package com.google.android.systemui.statusbar;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
/* compiled from: SafeCollector.kt */
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyManagerService$binder$1$startVoiceReply$4$invokeSuspend$$inlined$filter$1 implements Flow<OnVoiceAuthStateChangedData> {
    final /* synthetic */ Flow $this_unsafeTransform$inlined;
    final /* synthetic */ int $token$inlined;
    final /* synthetic */ NotificationVoiceReplyManagerService$binder$1 this$0;

    public NotificationVoiceReplyManagerService$binder$1$startVoiceReply$4$invokeSuspend$$inlined$filter$1(Flow flow, NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1, int i) {
        this.$this_unsafeTransform$inlined = flow;
        this.this$0 = notificationVoiceReplyManagerService$binder$1;
        this.$token$inlined = i;
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [kotlinx.coroutines.flow.FlowCollector, kotlin.coroutines.Continuation] */
    @Override // kotlinx.coroutines.flow.Flow
    public Object collect(final FlowCollector<? super OnVoiceAuthStateChangedData> flowCollector, Continuation continuation) {
        Object collect = this.$this_unsafeTransform$inlined.collect(new FlowCollector<OnVoiceAuthStateChangedData>() { // from class: com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$startVoiceReply$4$invokeSuspend$$inlined$filter$1.2
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, kotlin.coroutines.Continuation] */
            @Override // kotlinx.coroutines.flow.FlowCollector
            public Object emit(OnVoiceAuthStateChangedData onVoiceAuthStateChangedData, Continuation continuation2) {
                Object emit;
                OnVoiceAuthStateChangedData onVoiceAuthStateChangedData2 = onVoiceAuthStateChangedData;
                return (!Boxing.boxBoolean(onVoiceAuthStateChangedData2.getUserId() == this.this$0.getUserId() && onVoiceAuthStateChangedData2.getSessionToken() == this.$token$inlined).booleanValue() || (emit = flowCollector.emit(onVoiceAuthStateChangedData, continuation2)) != IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()) ? Unit.INSTANCE : emit;
            }
        }, continuation);
        return collect == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? collect : Unit.INSTANCE;
    }
}
