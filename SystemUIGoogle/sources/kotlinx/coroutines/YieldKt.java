package kotlinx.coroutines;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Yield.kt */
/* loaded from: classes2.dex */
public final class YieldKt {
    public static final Object yield(Continuation<? super Unit> continuation) {
        Object obj;
        CoroutineContext context = continuation.getContext();
        checkCompletion(context);
        Continuation continuation2 = IntrinsicsKt__IntrinsicsJvmKt.intercepted(continuation);
        if (!(continuation2 instanceof DispatchedContinuation)) {
            continuation2 = null;
        }
        DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) continuation2;
        if (dispatchedContinuation == null) {
            obj = Unit.INSTANCE;
        } else if (!dispatchedContinuation.dispatcher.isDispatchNeeded(context)) {
            obj = DispatchedKt.yieldUndispatched(dispatchedContinuation) ? IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() : Unit.INSTANCE;
        } else {
            dispatchedContinuation.dispatchYield$kotlinx_coroutines_core(Unit.INSTANCE);
            obj = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        }
        if (obj == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return obj;
    }

    public static final void checkCompletion(CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "$this$checkCompletion");
        Job job = (Job) coroutineContext.get(Job.Key);
        if (job != null && !job.isActive()) {
            throw job.getCancellationException();
        }
    }
}
