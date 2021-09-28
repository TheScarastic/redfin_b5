package com.google.android.systemui.statusbar;

import kotlin.Pair;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
/* compiled from: SafeCollector.kt */
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$invokeSuspend$$inlined$map$1 implements Flow<Boolean> {
    final /* synthetic */ Flow $this_unsafeTransform$inlined;

    public NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$invokeSuspend$$inlined$map$1(Flow flow) {
        this.$this_unsafeTransform$inlined = flow;
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [kotlinx.coroutines.flow.FlowCollector, kotlin.coroutines.Continuation] */
    @Override // kotlinx.coroutines.flow.Flow
    public Object collect(final FlowCollector<? super Boolean> flowCollector, Continuation continuation) {
        Object collect = this.$this_unsafeTransform$inlined.collect(new FlowCollector<Pair<? extends Integer, ? extends Integer>>() { // from class: com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$invokeSuspend$$inlined$map$1.2
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, kotlin.coroutines.Continuation] */
            @Override // kotlinx.coroutines.flow.FlowCollector
            public Object emit(Pair<? extends Integer, ? extends Integer> pair, Continuation continuation2) {
                Object emit = flowCollector.emit(Boxing.boxBoolean(((Number) pair.getSecond()).intValue() == 2), continuation2);
                return emit == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? emit : Unit.INSTANCE;
            }
        }, continuation);
        return collect == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? collect : Unit.INSTANCE;
    }
}
