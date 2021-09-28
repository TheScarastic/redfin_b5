package kotlinx.coroutines;

import kotlin.ExceptionsKt;
import kotlin.NoWhenBranchMatchedException;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.ContinuationInterceptor;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlinx.coroutines.CoroutineExceptionHandler;
import kotlinx.coroutines.internal.ScopeCoroutine;
import kotlinx.coroutines.internal.ThreadContextKt;
import kotlinx.coroutines.intrinsics.CancellableKt;
import kotlinx.coroutines.intrinsics.UndispatchedKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class BuildersKt {
    public static final void handleCoroutineException(@NotNull CoroutineContext coroutineContext, @NotNull Throwable th) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        try {
            int i = CoroutineExceptionHandler.$r8$clinit;
            CoroutineExceptionHandler coroutineExceptionHandler = (CoroutineExceptionHandler) coroutineContext.get(CoroutineExceptionHandler.Key.$$INSTANCE);
            if (coroutineExceptionHandler != null) {
                coroutineExceptionHandler.handleException(coroutineContext, th);
            } else {
                CoroutineExceptionHandlerImplKt.handleCoroutineExceptionImpl(coroutineContext, th);
            }
        } catch (Throwable th2) {
            if (th != th2) {
                RuntimeException runtimeException = new RuntimeException("Exception while trying to handle coroutine exception", th2);
                ExceptionsKt.addSuppressed(runtimeException, th);
                th = runtimeException;
            }
            CoroutineExceptionHandlerImplKt.handleCoroutineExceptionImpl(coroutineContext, th);
        }
    }

    public static Job launch$default(CoroutineScope coroutineScope, CoroutineContext coroutineContext, CoroutineStart coroutineStart, Function2 function2, int i, Object obj) {
        AbstractCoroutine abstractCoroutine;
        if ((i & 1) != 0) {
            coroutineContext = EmptyCoroutineContext.INSTANCE;
        }
        CoroutineStart coroutineStart2 = (i & 2) != 0 ? CoroutineStart.DEFAULT : null;
        Intrinsics.checkParameterIsNotNull(coroutineScope, "$this$launch");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(coroutineStart2, "start");
        boolean z = CoroutineContextKt.useCoroutinesScheduler;
        CoroutineContext plus = coroutineScope.getCoroutineContext().plus(coroutineContext);
        CoroutineContext plus2 = DebugKt.DEBUG ? plus.plus(new CoroutineId(DebugKt.COROUTINE_ID.incrementAndGet())) : plus;
        CoroutineDispatcher coroutineDispatcher = Dispatchers.Default;
        if (plus != coroutineDispatcher) {
            int i2 = ContinuationInterceptor.$r8$clinit;
            if (plus.get(ContinuationInterceptor.Key.$$INSTANCE) == null) {
                plus2 = plus2.plus(coroutineDispatcher);
            }
        }
        if (coroutineStart2 == CoroutineStart.LAZY) {
            abstractCoroutine = new LazyStandaloneCoroutine(plus2, function2);
        } else {
            abstractCoroutine = new StandaloneCoroutine(plus2, true);
        }
        abstractCoroutine.initParentJob$kotlinx_coroutines_core();
        int ordinal = coroutineStart2.ordinal();
        if (ordinal == 0) {
            CancellableKt.startCoroutineCancellable(function2, abstractCoroutine, abstractCoroutine);
        } else if (ordinal != 1) {
            if (ordinal == 2) {
                IntrinsicsKt__IntrinsicsKt.intercepted(IntrinsicsKt__IntrinsicsKt.createCoroutineUnintercepted(function2, abstractCoroutine, abstractCoroutine)).resumeWith(Unit.INSTANCE);
            } else if (ordinal == 3) {
                try {
                    CoroutineContext coroutineContext2 = abstractCoroutine.context;
                    Object updateThreadContext = ThreadContextKt.updateThreadContext(coroutineContext2, null);
                    TypeIntrinsics.beforeCheckcastToFunctionOfArity(function2, 2);
                    Object invoke = function2.invoke(abstractCoroutine, abstractCoroutine);
                    ThreadContextKt.restoreThreadContext(coroutineContext2, updateThreadContext);
                    if (invoke != CoroutineSingletons.COROUTINE_SUSPENDED) {
                        abstractCoroutine.resumeWith(invoke);
                    }
                } catch (Throwable th) {
                    abstractCoroutine.resumeWith(ResultKt.createFailure(th));
                }
            } else {
                throw new NoWhenBranchMatchedException();
            }
        }
        return abstractCoroutine;
    }

    @Nullable
    public static final <T> Object withContext(@NotNull CoroutineContext coroutineContext, @NotNull Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> function2, @NotNull Continuation<? super T> continuation) {
        boolean z;
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        CoroutineContext context = continuation.getContext();
        CoroutineContext plus = context.plus(coroutineContext);
        Intrinsics.checkParameterIsNotNull(plus, "$this$checkCompletion");
        Job job = (Job) plus.get(Job.Key);
        if (job != null && !job.isActive()) {
            throw job.getCancellationException();
        } else if (plus == context) {
            ScopeCoroutine scopeCoroutine = new ScopeCoroutine(plus, continuation);
            return UndispatchedKt.startUndispatchedOrReturn(scopeCoroutine, scopeCoroutine, function2);
        } else {
            int i = ContinuationInterceptor.$r8$clinit;
            ContinuationInterceptor.Key key = ContinuationInterceptor.Key.$$INSTANCE;
            if (Intrinsics.areEqual((ContinuationInterceptor) plus.get(key), (ContinuationInterceptor) context.get(key))) {
                UndispatchedCoroutine undispatchedCoroutine = new UndispatchedCoroutine(plus, continuation);
                Object updateThreadContext = ThreadContextKt.updateThreadContext(plus, null);
                try {
                    return UndispatchedKt.startUndispatchedOrReturn(undispatchedCoroutine, undispatchedCoroutine, function2);
                } finally {
                    ThreadContextKt.restoreThreadContext(plus, updateThreadContext);
                }
            } else {
                DispatchedCoroutine dispatchedCoroutine = new DispatchedCoroutine(plus, continuation);
                dispatchedCoroutine.initParentJob$kotlinx_coroutines_core();
                CancellableKt.startCoroutineCancellable(function2, dispatchedCoroutine, dispatchedCoroutine);
                while (true) {
                    int i2 = dispatchedCoroutine._decision;
                    z = false;
                    if (i2 == 0) {
                        if (DispatchedCoroutine._decision$FU.compareAndSet(dispatchedCoroutine, 0, 1)) {
                            z = true;
                            break;
                        }
                    } else if (i2 != 2) {
                        throw new IllegalStateException("Already suspended".toString());
                    }
                }
                if (z) {
                    return coroutineSingletons;
                }
                Object unboxState = JobSupportKt.unboxState(dispatchedCoroutine.getState$kotlinx_coroutines_core());
                if (!(unboxState instanceof CompletedExceptionally)) {
                    return unboxState;
                }
                throw ((CompletedExceptionally) unboxState).cause;
            }
        }
    }
}
