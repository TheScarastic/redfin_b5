package kotlinx.coroutines.intrinsics;

import kotlin.Result;
import kotlin.ResultKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlinx.coroutines.AbstractCoroutine;
import kotlinx.coroutines.CompletedExceptionally;
import kotlinx.coroutines.JobSupportKt;
import kotlinx.coroutines.internal.ScopesKt;
import kotlinx.coroutines.internal.ThreadContextKt;
/* compiled from: Undispatched.kt */
/* loaded from: classes2.dex */
public final class UndispatchedKt {
    public static final <R, T> void startCoroutineUndispatched(Function2<? super R, ? super Continuation<? super T>, ? extends Object> function2, R r, Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(function2, "$this$startCoroutineUndispatched");
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        Continuation probeCoroutineCreated = DebugProbesKt.probeCoroutineCreated(continuation);
        try {
            CoroutineContext context = continuation.getContext();
            Object updateThreadContext = ThreadContextKt.updateThreadContext(context, null);
            Object invoke = ((Function2) TypeIntrinsics.beforeCheckcastToFunctionOfArity(function2, 2)).invoke(r, probeCoroutineCreated);
            ThreadContextKt.restoreThreadContext(context, updateThreadContext);
            if (invoke != IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                Result.Companion companion = Result.Companion;
                probeCoroutineCreated.resumeWith(Result.m670constructorimpl(invoke));
            }
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            probeCoroutineCreated.resumeWith(Result.m670constructorimpl(ResultKt.createFailure(th)));
        }
    }

    public static final <T, R> Object startUndispatchedOrReturn(AbstractCoroutine<? super T> abstractCoroutine, R r, Function2<? super R, ? super Continuation<? super T>, ? extends Object> function2) {
        Object obj;
        Intrinsics.checkParameterIsNotNull(abstractCoroutine, "$this$startUndispatchedOrReturn");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        abstractCoroutine.initParentJob$kotlinx_coroutines_core();
        try {
            obj = ((Function2) TypeIntrinsics.beforeCheckcastToFunctionOfArity(function2, 2)).invoke(r, abstractCoroutine);
        } catch (Throwable th) {
            obj = new CompletedExceptionally(th, false, 2, null);
        }
        if (obj == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            return IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        }
        if (!abstractCoroutine.makeCompletingOnce$kotlinx_coroutines_core(obj, 4)) {
            return IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        }
        Object state$kotlinx_coroutines_core = abstractCoroutine.getState$kotlinx_coroutines_core();
        if (!(state$kotlinx_coroutines_core instanceof CompletedExceptionally)) {
            return JobSupportKt.unboxState(state$kotlinx_coroutines_core);
        }
        throw ScopesKt.tryRecover(abstractCoroutine, ((CompletedExceptionally) state$kotlinx_coroutines_core).cause);
    }
}
