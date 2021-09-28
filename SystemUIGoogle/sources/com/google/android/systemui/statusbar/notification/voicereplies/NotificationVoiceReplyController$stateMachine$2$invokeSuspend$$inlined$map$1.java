package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
/* compiled from: SafeCollector.kt */
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyController$stateMachine$2$invokeSuspend$$inlined$map$1 implements Flow<NotificationEntry> {
    final /* synthetic */ Flow $this_unsafeTransform$inlined;

    public NotificationVoiceReplyController$stateMachine$2$invokeSuspend$$inlined$map$1(Flow flow) {
        this.$this_unsafeTransform$inlined = flow;
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [kotlinx.coroutines.flow.FlowCollector, kotlin.coroutines.Continuation] */
    @Override // kotlinx.coroutines.flow.Flow
    public Object collect(final FlowCollector<? super NotificationEntry> flowCollector, Continuation continuation) {
        Object collect = this.$this_unsafeTransform$inlined.collect(new FlowCollector<Pair<? extends NotificationEntry, ? extends String>>() { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$invokeSuspend$$inlined$map$1.2
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, kotlin.coroutines.Continuation] */
            @Override // kotlinx.coroutines.flow.FlowCollector
            public Object emit(Pair<? extends NotificationEntry, ? extends String> pair, Continuation continuation2) {
                Object emit = flowCollector.emit(pair.getFirst(), continuation2);
                return emit == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? emit : Unit.INSTANCE;
            }
        }, continuation);
        return collect == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? collect : Unit.INSTANCE;
    }
}
