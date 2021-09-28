package kotlinx.coroutines.internal;

import android.support.media.ExifInterface$$ExternalSyntheticOutline0;
import kotlin.ResultKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.AbstractCoroutine;
import kotlinx.coroutines.CompletedExceptionally;
import kotlinx.coroutines.DispatchedContinuation;
import kotlinx.coroutines.DispatchedKt;
import kotlinx.coroutines.EventLoop;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.ThreadLocalEventLoop;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public class ScopeCoroutine<T> extends AbstractCoroutine<T> implements CoroutineStackFrame {
    @NotNull
    public final Continuation<T> uCont;

    /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: kotlin.coroutines.Continuation<? super T> */
    /* JADX WARN: Multi-variable type inference failed */
    public ScopeCoroutine(@NotNull CoroutineContext coroutineContext, @NotNull Continuation<? super T> continuation) {
        super(coroutineContext, true);
        this.uCont = continuation;
    }

    /* JADX INFO: finally extract failed */
    @Override // kotlinx.coroutines.JobSupport
    public void afterCompletionInternal(@Nullable Object obj, int i) {
        CoroutineContext context;
        Object updateThreadContext;
        if (obj instanceof CompletedExceptionally) {
            Throwable th = ((CompletedExceptionally) obj).cause;
            if (i != 4) {
                th = StackTraceRecoveryKt.recoverStackTrace(th, this.uCont);
            }
            Continuation<T> continuation = this.uCont;
            Intrinsics.checkParameterIsNotNull(continuation, "$this$resumeUninterceptedWithExceptionMode");
            Intrinsics.checkParameterIsNotNull(th, "exception");
            if (i == 0) {
                IntrinsicsKt__IntrinsicsKt.intercepted(continuation).resumeWith(ResultKt.createFailure(th));
            } else if (i == 1) {
                Continuation intercepted = IntrinsicsKt__IntrinsicsKt.intercepted(continuation);
                Symbol symbol = DispatchedKt.UNDEFINED;
                Intrinsics.checkParameterIsNotNull(intercepted, "$this$resumeCancellableWithException");
                Intrinsics.checkParameterIsNotNull(th, "exception");
                if (intercepted instanceof DispatchedContinuation) {
                    DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) intercepted;
                    CoroutineContext context2 = dispatchedContinuation.continuation.getContext();
                    boolean z = false;
                    CompletedExceptionally completedExceptionally = new CompletedExceptionally(th, false, 2);
                    if (dispatchedContinuation.dispatcher.isDispatchNeeded(context2)) {
                        dispatchedContinuation._state = new CompletedExceptionally(th, false, 2);
                        dispatchedContinuation.resumeMode = 1;
                        dispatchedContinuation.dispatcher.dispatch(context2, dispatchedContinuation);
                        return;
                    }
                    ThreadLocalEventLoop threadLocalEventLoop = ThreadLocalEventLoop.INSTANCE;
                    EventLoop eventLoop$kotlinx_coroutines_core = ThreadLocalEventLoop.getEventLoop$kotlinx_coroutines_core();
                    if (eventLoop$kotlinx_coroutines_core.isUnconfinedLoopActive()) {
                        dispatchedContinuation._state = completedExceptionally;
                        dispatchedContinuation.resumeMode = 1;
                        eventLoop$kotlinx_coroutines_core.dispatchUnconfined(dispatchedContinuation);
                        return;
                    }
                    eventLoop$kotlinx_coroutines_core.incrementUseCount(true);
                    try {
                        Job job = (Job) dispatchedContinuation.getContext().get(Job.Key);
                        if (job != null && !job.isActive()) {
                            dispatchedContinuation.resumeWith(ResultKt.createFailure(job.getCancellationException()));
                            z = true;
                        }
                        if (!z) {
                            CoroutineContext context3 = dispatchedContinuation.getContext();
                            Object updateThreadContext2 = ThreadContextKt.updateThreadContext(context3, dispatchedContinuation.countOrElement);
                            Continuation<T> continuation2 = dispatchedContinuation.continuation;
                            continuation2.resumeWith(ResultKt.createFailure(StackTraceRecoveryKt.recoverStackTrace(th, continuation2)));
                            ThreadContextKt.restoreThreadContext(context3, updateThreadContext2);
                        }
                        do {
                        } while (eventLoop$kotlinx_coroutines_core.processUnconfinedEvent());
                    } finally {
                        try {
                            return;
                        } finally {
                        }
                    }
                    return;
                }
                intercepted.resumeWith(ResultKt.createFailure(StackTraceRecoveryKt.recoverStackTrace(th, intercepted)));
            } else if (i == 2) {
                continuation.resumeWith(ResultKt.createFailure(th));
            } else if (i == 3) {
                context = continuation.getContext();
                updateThreadContext = ThreadContextKt.updateThreadContext(context, null);
                try {
                    continuation.resumeWith(ResultKt.createFailure(th));
                } catch (Throwable th2) {
                    throw th2;
                }
            } else if (i != 4) {
                throw new IllegalStateException(ExifInterface$$ExternalSyntheticOutline0.m("Invalid mode ", i).toString());
            }
        } else {
            Continuation<T> continuation3 = this.uCont;
            Intrinsics.checkParameterIsNotNull(continuation3, "$this$resumeUninterceptedMode");
            if (i == 0) {
                IntrinsicsKt__IntrinsicsKt.intercepted(continuation3).resumeWith(obj);
            } else if (i == 1) {
                DispatchedKt.resumeCancellable(IntrinsicsKt__IntrinsicsKt.intercepted(continuation3), obj);
            } else if (i == 2) {
                continuation3.resumeWith(obj);
            } else if (i == 3) {
                context = continuation3.getContext();
                updateThreadContext = ThreadContextKt.updateThreadContext(context, null);
                try {
                    continuation3.resumeWith(obj);
                } finally {
                    ThreadContextKt.restoreThreadContext(context, updateThreadContext);
                }
            } else if (i != 4) {
                throw new IllegalStateException(ExifInterface$$ExternalSyntheticOutline0.m("Invalid mode ", i).toString());
            }
        }
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    @Nullable
    public final CoroutineStackFrame getCallerFrame() {
        return (CoroutineStackFrame) this.uCont;
    }

    @Override // kotlinx.coroutines.AbstractCoroutine
    public int getDefaultResumeMode$kotlinx_coroutines_core() {
        return 2;
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    @Nullable
    public final StackTraceElement getStackTraceElement() {
        return null;
    }

    @Override // kotlinx.coroutines.JobSupport
    public final boolean isScopedCoroutine() {
        return true;
    }
}
