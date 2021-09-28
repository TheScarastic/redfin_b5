package kotlin.coroutines.jvm.internal;

import java.io.Serializable;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ContinuationImpl.kt */
/* loaded from: classes2.dex */
public abstract class BaseContinuationImpl implements Continuation<Object>, CoroutineStackFrame, Serializable {
    private final Continuation<Object> completion;

    protected abstract Object invokeSuspend(Object obj);

    protected void releaseIntercepted() {
    }

    public BaseContinuationImpl(Continuation<Object> continuation) {
        this.completion = continuation;
    }

    public final Continuation<Object> getCompletion() {
        return this.completion;
    }

    @Override // kotlin.coroutines.Continuation
    public final void resumeWith(Object obj) {
        Object invokeSuspend;
        while (true) {
            DebugProbesKt.probeCoroutineResumed(this);
            Continuation<Object> continuation = this.completion;
            Intrinsics.checkNotNull(continuation);
            try {
                invokeSuspend = this.invokeSuspend(obj);
            } catch (Throwable th) {
                Result.Companion companion = Result.Companion;
                obj = Result.m670constructorimpl(ResultKt.createFailure(th));
            }
            if (invokeSuspend != IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                Result.Companion companion2 = Result.Companion;
                obj = Result.m670constructorimpl(invokeSuspend);
                this.releaseIntercepted();
                if (continuation instanceof BaseContinuationImpl) {
                    this = (BaseContinuationImpl) continuation;
                } else {
                    continuation.resumeWith(obj);
                    return;
                }
            } else {
                return;
            }
        }
    }

    public Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkNotNullParameter(continuation, "completion");
        throw new UnsupportedOperationException("create(Any?;Continuation) has not been overridden");
    }

    @Override // java.lang.Object
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Continuation at ");
        Object stackTraceElement = getStackTraceElement();
        if (stackTraceElement == null) {
            stackTraceElement = getClass().getName();
        }
        sb.append(stackTraceElement);
        return sb.toString();
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    public CoroutineStackFrame getCallerFrame() {
        Continuation<Object> continuation = this.completion;
        if (!(continuation instanceof CoroutineStackFrame)) {
            continuation = null;
        }
        return (CoroutineStackFrame) continuation;
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    public StackTraceElement getStackTraceElement() {
        return DebugMetadataKt.getStackTraceElement(this);
    }
}
