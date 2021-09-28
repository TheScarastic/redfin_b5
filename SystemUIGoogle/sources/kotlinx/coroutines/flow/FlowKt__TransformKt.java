package kotlinx.coroutines.flow;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Transform.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final /* synthetic */ class FlowKt__TransformKt {
    public static final <T> Flow<T> onEach(Flow<? extends T> flow, Function2<? super T, ? super Continuation<? super Unit>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(flow, "$this$onEach");
        Intrinsics.checkParameterIsNotNull(function2, "action");
        return new FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1(flow, function2);
    }
}
