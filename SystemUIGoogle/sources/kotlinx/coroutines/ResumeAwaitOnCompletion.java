package kotlinx.coroutines;

import kotlin.Result;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: JobSupport.kt */
/* loaded from: classes2.dex */
final class ResumeAwaitOnCompletion<T> extends JobNode<JobSupport> {
    private final CancellableContinuationImpl<T> continuation;

    /* Return type fixed from 'java.lang.Object' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
    @Override // kotlin.jvm.functions.Function1
    public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
        invoke(th);
        return Unit.INSTANCE;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: kotlinx.coroutines.CancellableContinuationImpl<? super T> */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ResumeAwaitOnCompletion(JobSupport jobSupport, CancellableContinuationImpl<? super T> cancellableContinuationImpl) {
        super(jobSupport);
        Intrinsics.checkParameterIsNotNull(jobSupport, "job");
        Intrinsics.checkParameterIsNotNull(cancellableContinuationImpl, "continuation");
        this.continuation = cancellableContinuationImpl;
    }

    @Override // kotlinx.coroutines.CompletionHandlerBase
    public void invoke(Throwable th) {
        Object state$kotlinx_coroutines_core = ((JobSupport) this.job).getState$kotlinx_coroutines_core();
        if (DebugKt.getASSERTIONS_ENABLED() && !(!(state$kotlinx_coroutines_core instanceof Incomplete))) {
            throw new AssertionError();
        } else if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
            this.continuation.resumeWithExceptionMode$kotlinx_coroutines_core(((CompletedExceptionally) state$kotlinx_coroutines_core).cause, 0);
        } else {
            CancellableContinuationImpl<T> cancellableContinuationImpl = this.continuation;
            Object unboxState = JobSupportKt.unboxState(state$kotlinx_coroutines_core);
            Result.Companion companion = Result.Companion;
            cancellableContinuationImpl.resumeWith(Result.m670constructorimpl(unboxState));
        }
    }

    @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
    public String toString() {
        return "ResumeAwaitOnCompletion[" + this.continuation + ']';
    }
}
