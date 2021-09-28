package kotlinx.coroutines;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
/* loaded from: classes2.dex */
public final class JobKt {
    public static final CompletableJob Job(Job job) {
        return JobKt__JobKt.Job(job);
    }

    public static final void cancel(Job job, String str, Throwable th) {
        JobKt__JobKt.cancel(job, str, th);
    }

    public static final Object cancelAndJoin(Job job, Continuation<? super Unit> continuation) {
        return JobKt__JobKt.cancelAndJoin(job, continuation);
    }

    public static final void ensureActive(Job job) {
        JobKt__JobKt.ensureActive(job);
    }
}
