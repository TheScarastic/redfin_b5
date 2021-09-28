package kotlinx.coroutines;

import java.util.concurrent.CancellationException;
import kotlin.ExceptionsKt;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public abstract class DispatchedTask<T> extends Task {
    public int resumeMode;

    public DispatchedTask(int i) {
        this.resumeMode = i;
    }

    @NotNull
    public abstract Continuation<T> getDelegate$kotlinx_coroutines_core();

    public final void handleFatalException$kotlinx_coroutines_core(@Nullable Throwable th, @Nullable Throwable th2) {
        if (th != null || th2 != null) {
            if (!(th == null || th2 == null)) {
                ExceptionsKt.addSuppressed(th, th2);
            }
            if (th == null) {
                th = th2;
            }
            String str = "Fatal exception in coroutines machinery for " + this + ". Please read KDoc to 'handleFatalException' method and report this incident to maintainers";
            if (th != null) {
                BuildersKt.handleCoroutineException(getDelegate$kotlinx_coroutines_core().getContext(), new CoroutinesInternalError(str, th));
                return;
            }
            Intrinsics.throwNpe();
            throw null;
        }
    }

    @Override // java.lang.Runnable
    public final void run() {
        Object obj;
        TaskContext taskContext = this.taskContext;
        try {
            Continuation<T> delegate$kotlinx_coroutines_core = getDelegate$kotlinx_coroutines_core();
            if (delegate$kotlinx_coroutines_core != null) {
                DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) delegate$kotlinx_coroutines_core;
                Continuation<T> continuation = dispatchedContinuation.continuation;
                CoroutineContext context = continuation.getContext();
                Object takeState$kotlinx_coroutines_core = takeState$kotlinx_coroutines_core();
                Object updateThreadContext = ThreadContextKt.updateThreadContext(context, dispatchedContinuation.countOrElement);
                CompletedExceptionally completedExceptionally = (CompletedExceptionally) (!(takeState$kotlinx_coroutines_core instanceof CompletedExceptionally) ? null : takeState$kotlinx_coroutines_core);
                Throwable th = completedExceptionally != null ? completedExceptionally.cause : null;
                boolean z = true;
                if (this.resumeMode != 1) {
                    z = false;
                }
                Job job = z ? (Job) context.get(Job.Key) : null;
                if (th == null && job != null && !job.isActive()) {
                    CancellationException cancellationException = job.getCancellationException();
                    Intrinsics.checkParameterIsNotNull(cancellationException, "cause");
                    continuation.resumeWith(ResultKt.createFailure(StackTraceRecoveryKt.recoverStackTrace(cancellationException, continuation)));
                } else if (th != null) {
                    continuation.resumeWith(ResultKt.createFailure(StackTraceRecoveryKt.recoverStackTrace(th, continuation)));
                } else {
                    continuation.resumeWith(takeState$kotlinx_coroutines_core);
                }
                Object obj2 = Unit.INSTANCE;
                ThreadContextKt.restoreThreadContext(context, updateThreadContext);
                try {
                    taskContext.afterTask();
                } catch (Throwable th2) {
                    obj2 = ResultKt.createFailure(th2);
                }
                handleFatalException$kotlinx_coroutines_core(null, Result.m23exceptionOrNullimpl(obj2));
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.DispatchedContinuation<T>");
        } catch (Throwable th3) {
            try {
                taskContext.afterTask();
                obj = Unit.INSTANCE;
            } catch (Throwable th4) {
                obj = ResultKt.createFailure(th4);
            }
            handleFatalException$kotlinx_coroutines_core(th3, Result.m23exceptionOrNullimpl(obj));
        }
    }

    @Nullable
    public abstract Object takeState$kotlinx_coroutines_core();
}
