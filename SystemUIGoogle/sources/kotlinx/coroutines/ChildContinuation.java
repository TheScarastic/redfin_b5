package kotlinx.coroutines;

import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: JobSupport.kt */
/* loaded from: classes2.dex */
public final class ChildContinuation extends JobCancellingNode<Job> {
    public final CancellableContinuationImpl<?> child;

    /* Return type fixed from 'java.lang.Object' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
    @Override // kotlin.jvm.functions.Function1
    public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
        invoke(th);
        return Unit.INSTANCE;
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ChildContinuation(Job job, CancellableContinuationImpl<?> cancellableContinuationImpl) {
        super(job);
        Intrinsics.checkParameterIsNotNull(job, "parent");
        Intrinsics.checkParameterIsNotNull(cancellableContinuationImpl, "child");
        this.child = cancellableContinuationImpl;
    }

    @Override // kotlinx.coroutines.CompletionHandlerBase
    public void invoke(Throwable th) {
        CancellableContinuationImpl<?> cancellableContinuationImpl = this.child;
        cancellableContinuationImpl.cancel(cancellableContinuationImpl.getContinuationCancellationCause(this.job));
    }

    @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
    public String toString() {
        return "ChildContinuation[" + this.child + ']';
    }
}
