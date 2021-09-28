package kotlinx.coroutines;

import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
/* compiled from: CancellableContinuationImpl.kt */
/* loaded from: classes2.dex */
public class CancellableContinuationImpl<T> extends DispatchedTask<T> implements CancellableContinuation<T>, CoroutineStackFrame {
    private static final AtomicIntegerFieldUpdater _decision$FU = AtomicIntegerFieldUpdater.newUpdater(CancellableContinuationImpl.class, "_decision");
    private static final AtomicReferenceFieldUpdater _state$FU = AtomicReferenceFieldUpdater.newUpdater(CancellableContinuationImpl.class, Object.class, "_state");
    private volatile int _decision = 0;
    private volatile Object _state = Active.INSTANCE;
    private final CoroutineContext context;
    private final Continuation<T> delegate;
    private volatile DisposableHandle parentHandle;

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    public StackTraceElement getStackTraceElement() {
        return null;
    }

    public /* synthetic */ void initCancellability() {
    }

    protected String nameString() {
        return "CancellableContinuation";
    }

    @Override // kotlinx.coroutines.DispatchedTask
    public final Continuation<T> getDelegate$kotlinx_coroutines_core() {
        return this.delegate;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: kotlin.coroutines.Continuation<? super T> */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public CancellableContinuationImpl(Continuation<? super T> continuation, int i) {
        super(i);
        Intrinsics.checkParameterIsNotNull(continuation, "delegate");
        this.delegate = continuation;
        this.context = continuation.getContext();
    }

    @Override // kotlin.coroutines.Continuation
    public CoroutineContext getContext() {
        return this.context;
    }

    public final Object getState$kotlinx_coroutines_core() {
        return this._state;
    }

    public boolean isCompleted() {
        return !(getState$kotlinx_coroutines_core() instanceof NotCompleted);
    }

    private final void installParentCancellationHandler() {
        Job job;
        if (!isCompleted() && (job = (Job) this.delegate.getContext().get(Job.Key)) != null) {
            job.start();
            DisposableHandle invokeOnCompletion$default = Job.DefaultImpls.invokeOnCompletion$default(job, true, false, new ChildContinuation(job, this), 2, null);
            this.parentHandle = invokeOnCompletion$default;
            if (isCompleted()) {
                invokeOnCompletion$default.dispose();
                this.parentHandle = NonDisposableHandle.INSTANCE;
            }
        }
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    public CoroutineStackFrame getCallerFrame() {
        Continuation<T> continuation = this.delegate;
        if (!(continuation instanceof CoroutineStackFrame)) {
            continuation = null;
        }
        return (CoroutineStackFrame) continuation;
    }

    @Override // kotlinx.coroutines.DispatchedTask
    public Object takeState$kotlinx_coroutines_core() {
        return getState$kotlinx_coroutines_core();
    }

    @Override // kotlinx.coroutines.DispatchedTask
    public void cancelResult$kotlinx_coroutines_core(Object obj, Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "cause");
        if (obj instanceof CompletedWithCancellation) {
            try {
                ((CompletedWithCancellation) obj).onCancellation.invoke(th);
            } catch (Throwable th2) {
                CoroutineContext context = getContext();
                CoroutineExceptionHandlerKt.handleCoroutineException(context, new CompletionHandlerException("Exception in cancellation handler for " + this, th2));
            }
        }
    }

    public Throwable getContinuationCancellationCause(Job job) {
        Intrinsics.checkParameterIsNotNull(job, "parent");
        return job.getCancellationException();
    }

    public final Object getResult() {
        Job job;
        installParentCancellationHandler();
        if (trySuspend()) {
            return IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        }
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
            throw StackTraceRecoveryKt.recoverStackTrace(((CompletedExceptionally) state$kotlinx_coroutines_core).cause, this);
        } else if (this.resumeMode != 1 || (job = (Job) getContext().get(Job.Key)) == null || job.isActive()) {
            return getSuccessfulResult$kotlinx_coroutines_core(state$kotlinx_coroutines_core);
        } else {
            CancellationException cancellationException = job.getCancellationException();
            cancelResult$kotlinx_coroutines_core(state$kotlinx_coroutines_core, cancellationException);
            throw StackTraceRecoveryKt.recoverStackTrace(cancellationException, this);
        }
    }

    @Override // kotlin.coroutines.Continuation
    public void resumeWith(Object obj) {
        resumeImpl(CompletedExceptionallyKt.toState(obj), this.resumeMode);
    }

    public final CancelledContinuation resumeWithExceptionMode$kotlinx_coroutines_core(Throwable th, int i) {
        Intrinsics.checkParameterIsNotNull(th, "exception");
        return resumeImpl(new CompletedExceptionally(th, false, 2, null), i);
    }

    private final void multipleHandlersError(Function1<? super Throwable, Unit> function1, Object obj) {
        throw new IllegalStateException(("It's prohibited to register multiple handlers, tried to register " + function1 + ", already has " + obj).toString());
    }

    private final CancelHandler makeHandler(Function1<? super Throwable, Unit> function1) {
        return function1 instanceof CancelHandler ? (CancelHandler) function1 : new InvokeOnCancel(function1);
    }

    private final void dispatchResume(int i) {
        if (!tryResume()) {
            DispatchedKt.dispatch(this, i);
        }
    }

    private final void alreadyResumedError(Object obj) {
        throw new IllegalStateException(("Already resumed, but proposed with update " + obj).toString());
    }

    private final void disposeParentHandle() {
        DisposableHandle disposableHandle = this.parentHandle;
        if (disposableHandle != null) {
            disposableHandle.dispose();
            this.parentHandle = NonDisposableHandle.INSTANCE;
        }
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public void completeResume(Object obj) {
        Intrinsics.checkParameterIsNotNull(obj, "token");
        dispatchResume(this.resumeMode);
    }

    /* JADX DEBUG: Multi-variable search result rejected for r1v0, resolved type: java.lang.Object */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlinx.coroutines.DispatchedTask
    public <T> T getSuccessfulResult$kotlinx_coroutines_core(Object obj) {
        if (obj instanceof CompletedIdempotentResult) {
            return (T) ((CompletedIdempotentResult) obj).result;
        }
        return obj instanceof CompletedWithCancellation ? (T) ((CompletedWithCancellation) obj).result : obj;
    }

    @Override // java.lang.Object
    public String toString() {
        return nameString() + '(' + DebugStringsKt.toDebugString(this.delegate) + "){" + getState$kotlinx_coroutines_core() + "}@" + DebugStringsKt.getHexAddress(this);
    }

    public boolean cancel(Throwable th) {
        Object obj;
        boolean z;
        do {
            obj = this._state;
            if (!(obj instanceof NotCompleted)) {
                return false;
            }
            z = obj instanceof CancelHandler;
        } while (!_state$FU.compareAndSet(this, obj, new CancelledContinuation(this, th, z)));
        if (z) {
            try {
                ((CancelHandler) obj).invoke(th);
            } catch (Throwable th2) {
                CoroutineContext context = getContext();
                CoroutineExceptionHandlerKt.handleCoroutineException(context, new CompletionHandlerException("Exception in cancellation handler for " + this, th2));
            }
        }
        disposeParentHandle();
        dispatchResume(0);
        return true;
    }

    private final boolean trySuspend() {
        do {
            int i = this._decision;
            if (i != 0) {
                if (i == 2) {
                    return false;
                }
                throw new IllegalStateException("Already suspended".toString());
            }
        } while (!_decision$FU.compareAndSet(this, 0, 1));
        return true;
    }

    private final boolean tryResume() {
        do {
            int i = this._decision;
            if (i != 0) {
                if (i == 1) {
                    return false;
                }
                throw new IllegalStateException("Already resumed".toString());
            }
        } while (!_decision$FU.compareAndSet(this, 0, 2));
        return true;
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public void invokeOnCancellation(Function1<? super Throwable, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "handler");
        Throwable th = null;
        CancelHandler cancelHandler = null;
        while (true) {
            Object obj = this._state;
            if (obj instanceof Active) {
                if (cancelHandler == null) {
                    cancelHandler = makeHandler(function1);
                }
                if (_state$FU.compareAndSet(this, obj, cancelHandler)) {
                    return;
                }
            } else if (obj instanceof CancelHandler) {
                multipleHandlersError(function1, obj);
            } else if (obj instanceof CancelledContinuation) {
                if (!((CancelledContinuation) obj).makeHandled()) {
                    multipleHandlersError(function1, obj);
                }
                try {
                    if (!(obj instanceof CompletedExceptionally)) {
                        obj = null;
                    }
                    CompletedExceptionally completedExceptionally = (CompletedExceptionally) obj;
                    if (completedExceptionally != null) {
                        th = completedExceptionally.cause;
                    }
                    function1.invoke(th);
                    return;
                } catch (Throwable th2) {
                    CoroutineExceptionHandlerKt.handleCoroutineException(getContext(), new CompletionHandlerException("Exception in cancellation handler for " + this, th2));
                    return;
                }
            } else {
                return;
            }
        }
    }

    private final CancelledContinuation resumeImpl(Object obj, int i) {
        while (true) {
            Object obj2 = this._state;
            if (!(obj2 instanceof NotCompleted)) {
                if (obj2 instanceof CancelledContinuation) {
                    CancelledContinuation cancelledContinuation = (CancelledContinuation) obj2;
                    if (cancelledContinuation.makeResumed()) {
                        return cancelledContinuation;
                    }
                }
                alreadyResumedError(obj);
            } else if (_state$FU.compareAndSet(this, obj2, obj)) {
                disposeParentHandle();
                dispatchResume(i);
                return null;
            }
        }
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public Object tryResume(T t, Object obj) {
        Object obj2;
        Object obj3;
        do {
            obj2 = this._state;
            if (obj2 instanceof NotCompleted) {
                if (obj == null) {
                    obj3 = t;
                } else {
                    obj3 = new CompletedIdempotentResult(obj, t, (NotCompleted) obj2);
                }
            } else if (!(obj2 instanceof CompletedIdempotentResult)) {
                return null;
            } else {
                CompletedIdempotentResult completedIdempotentResult = (CompletedIdempotentResult) obj2;
                if (completedIdempotentResult.idempotentResume != obj) {
                    return null;
                }
                if (DebugKt.getASSERTIONS_ENABLED()) {
                    if (!(completedIdempotentResult.result == t)) {
                        throw new AssertionError();
                    }
                }
                return completedIdempotentResult.token;
            }
        } while (!_state$FU.compareAndSet(this, obj2, obj3));
        disposeParentHandle();
        return obj2;
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public Object tryResumeWithException(Throwable th) {
        Object obj;
        Intrinsics.checkParameterIsNotNull(th, "exception");
        do {
            obj = this._state;
            if (!(obj instanceof NotCompleted)) {
                return null;
            }
        } while (!_state$FU.compareAndSet(this, obj, new CompletedExceptionally(th, false, 2, null)));
        disposeParentHandle();
        return obj;
    }
}
