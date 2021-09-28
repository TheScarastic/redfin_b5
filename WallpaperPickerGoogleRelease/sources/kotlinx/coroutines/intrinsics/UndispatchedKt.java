package kotlinx.coroutines.intrinsics;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlinx.coroutines.AbstractCoroutine;
import kotlinx.coroutines.CompletedExceptionally;
import kotlinx.coroutines.JobSupportKt;
import kotlinx.coroutines.internal.ScopesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class UndispatchedKt {
    @Nullable
    public static final <T, R> Object startUndispatchedOrReturn(@NotNull AbstractCoroutine<? super T> abstractCoroutine, R r, @NotNull Function2<? super R, ? super Continuation<? super T>, ? extends Object> function2) {
        Object obj;
        Intrinsics.checkParameterIsNotNull(function2, "block");
        abstractCoroutine.initParentJob$kotlinx_coroutines_core();
        try {
            TypeIntrinsics.beforeCheckcastToFunctionOfArity(function2, 2);
            obj = function2.invoke(r, abstractCoroutine);
        } catch (Throwable th) {
            obj = new CompletedExceptionally(th, false, 2);
        }
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        if (obj == coroutineSingletons || !abstractCoroutine.makeCompletingOnce$kotlinx_coroutines_core(obj, 4)) {
            return coroutineSingletons;
        }
        Object state$kotlinx_coroutines_core = abstractCoroutine.getState$kotlinx_coroutines_core();
        if (!(state$kotlinx_coroutines_core instanceof CompletedExceptionally)) {
            return JobSupportKt.unboxState(state$kotlinx_coroutines_core);
        }
        throw ScopesKt.tryRecover(abstractCoroutine, ((CompletedExceptionally) state$kotlinx_coroutines_core).cause);
    }
}
