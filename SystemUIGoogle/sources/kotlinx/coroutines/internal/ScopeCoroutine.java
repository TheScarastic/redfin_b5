package kotlinx.coroutines.internal;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.AbstractCoroutine;
import kotlinx.coroutines.CompletedExceptionally;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.ResumeModeKt;
/* compiled from: Scopes.kt */
/* loaded from: classes2.dex */
public class ScopeCoroutine<T> extends AbstractCoroutine<T> implements CoroutineStackFrame {
    public final Continuation<T> uCont;

    @Override // kotlinx.coroutines.AbstractCoroutine
    public int getDefaultResumeMode$kotlinx_coroutines_core() {
        return 2;
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    public final StackTraceElement getStackTraceElement() {
        return null;
    }

    @Override // kotlinx.coroutines.JobSupport
    protected final boolean isScopedCoroutine() {
        return true;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: kotlin.coroutines.Continuation<? super T> */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ScopeCoroutine(CoroutineContext coroutineContext, Continuation<? super T> continuation) {
        super(coroutineContext, true);
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(continuation, "uCont");
        this.uCont = continuation;
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    public final CoroutineStackFrame getCallerFrame() {
        return (CoroutineStackFrame) this.uCont;
    }

    public final Job getParent$kotlinx_coroutines_core() {
        return (Job) this.parentContext.get(Job.Key);
    }

    @Override // kotlinx.coroutines.JobSupport
    protected void afterCompletionInternal(Object obj, int i) {
        if (obj instanceof CompletedExceptionally) {
            Throwable th = ((CompletedExceptionally) obj).cause;
            if (i != 4) {
                th = StackTraceRecoveryKt.recoverStackTrace(th, this.uCont);
            }
            ResumeModeKt.resumeUninterceptedWithExceptionMode(this.uCont, th, i);
            return;
        }
        ResumeModeKt.resumeUninterceptedMode(this.uCont, obj, i);
    }
}
