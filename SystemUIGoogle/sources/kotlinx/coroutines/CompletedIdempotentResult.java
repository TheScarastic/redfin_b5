package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: CancellableContinuationImpl.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class CompletedIdempotentResult {
    public final Object idempotentResume;
    public final Object result;
    public final NotCompleted token;

    public CompletedIdempotentResult(Object obj, Object obj2, NotCompleted notCompleted) {
        Intrinsics.checkParameterIsNotNull(notCompleted, "token");
        this.idempotentResume = obj;
        this.result = obj2;
        this.token = notCompleted;
    }

    public String toString() {
        return "CompletedIdempotentResult[" + this.result + ']';
    }
}
