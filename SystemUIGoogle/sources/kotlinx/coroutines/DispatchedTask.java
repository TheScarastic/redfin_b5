package kotlinx.coroutines;

import java.util.concurrent.CancellationException;
import kotlin.ExceptionsKt__ExceptionsKt;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import kotlinx.coroutines.internal.ThreadContextKt;
import kotlinx.coroutines.scheduling.Task;
import kotlinx.coroutines.scheduling.TaskContext;
/* compiled from: Dispatched.kt */
/* loaded from: classes2.dex */
public abstract class DispatchedTask<T> extends Task {
    public int resumeMode;

    public void cancelResult$kotlinx_coroutines_core(Object obj, Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "cause");
    }

    public abstract Continuation<T> getDelegate$kotlinx_coroutines_core();

    /* JADX DEBUG: Multi-variable search result rejected for r1v0, resolved type: java.lang.Object */
    /* JADX WARN: Multi-variable type inference failed */
    public <T> T getSuccessfulResult$kotlinx_coroutines_core(Object obj) {
        return obj;
    }

    public abstract Object takeState$kotlinx_coroutines_core();

    public final void handleFatalException$kotlinx_coroutines_core(Throwable th, Throwable th2) {
        if (th != null || th2 != null) {
            if (!(th == null || th2 == null)) {
                ExceptionsKt__ExceptionsKt.addSuppressed(th, th2);
            }
            if (th == null) {
                th = th2;
            }
            String str = "Fatal exception in coroutines machinery for " + this + ". Please read KDoc to 'handleFatalException' method and report this incident to maintainers";
            if (th == null) {
                Intrinsics.throwNpe();
            }
            CoroutineExceptionHandlerKt.handleCoroutineException(getDelegate$kotlinx_coroutines_core().getContext(), new CoroutinesInternalError(str, th));
        }
    }

    public DispatchedTask(int i) {
        this.resumeMode = i;
    }

    public final Throwable getExceptionalResult$kotlinx_coroutines_core(Object obj) {
        if (!(obj instanceof CompletedExceptionally)) {
            obj = null;
        }
        CompletedExceptionally completedExceptionally = (CompletedExceptionally) obj;
        if (completedExceptionally != null) {
            return completedExceptionally.cause;
        }
        return null;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Object obj;
        Object obj2;
        TaskContext taskContext = this.taskContext;
        try {
            Continuation<T> delegate$kotlinx_coroutines_core = getDelegate$kotlinx_coroutines_core();
            if (delegate$kotlinx_coroutines_core != null) {
                DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) delegate$kotlinx_coroutines_core;
                Continuation<T> continuation = dispatchedContinuation.continuation;
                CoroutineContext context = continuation.getContext();
                Object takeState$kotlinx_coroutines_core = takeState$kotlinx_coroutines_core();
                Object updateThreadContext = ThreadContextKt.updateThreadContext(context, dispatchedContinuation.countOrElement);
                Throwable exceptionalResult$kotlinx_coroutines_core = getExceptionalResult$kotlinx_coroutines_core(takeState$kotlinx_coroutines_core);
                Job job = ResumeModeKt.isCancellableMode(this.resumeMode) ? (Job) context.get(Job.Key) : null;
                if (exceptionalResult$kotlinx_coroutines_core == null && job != null && !job.isActive()) {
                    CancellationException cancellationException = job.getCancellationException();
                    cancelResult$kotlinx_coroutines_core(takeState$kotlinx_coroutines_core, cancellationException);
                    Result.Companion companion = Result.Companion;
                    continuation.resumeWith(Result.m670constructorimpl(ResultKt.createFailure(StackTraceRecoveryKt.recoverStackTrace(cancellationException, continuation))));
                } else if (exceptionalResult$kotlinx_coroutines_core != null) {
                    Result.Companion companion2 = Result.Companion;
                    continuation.resumeWith(Result.m670constructorimpl(ResultKt.createFailure(StackTraceRecoveryKt.recoverStackTrace(exceptionalResult$kotlinx_coroutines_core, continuation))));
                } else {
                    T successfulResult$kotlinx_coroutines_core = getSuccessfulResult$kotlinx_coroutines_core(takeState$kotlinx_coroutines_core);
                    Result.Companion companion3 = Result.Companion;
                    continuation.resumeWith(Result.m670constructorimpl(successfulResult$kotlinx_coroutines_core));
                }
                Unit unit = Unit.INSTANCE;
                ThreadContextKt.restoreThreadContext(context, updateThreadContext);
                try {
                    Result.Companion companion4 = Result.Companion;
                    taskContext.afterTask();
                    obj2 = Result.m670constructorimpl(unit);
                } catch (Throwable th) {
                    Result.Companion companion5 = Result.Companion;
                    obj2 = Result.m670constructorimpl(ResultKt.createFailure(th));
                }
                handleFatalException$kotlinx_coroutines_core(null, Result.m672exceptionOrNullimpl(obj2));
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.DispatchedContinuation<T>");
        } catch (Throwable th2) {
            try {
                Result.Companion companion6 = Result.Companion;
                taskContext.afterTask();
                obj = Result.m670constructorimpl(Unit.INSTANCE);
            } catch (Throwable th3) {
                Result.Companion companion7 = Result.Companion;
                obj = Result.m670constructorimpl(ResultKt.createFailure(th3));
            }
            handleFatalException$kotlinx_coroutines_core(th2, Result.m672exceptionOrNullimpl(obj));
        }
    }
}
