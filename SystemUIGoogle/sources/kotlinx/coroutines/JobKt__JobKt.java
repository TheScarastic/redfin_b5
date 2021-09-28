package kotlinx.coroutines;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.Job;
/* compiled from: Job.kt */
/* loaded from: classes2.dex */
public final /* synthetic */ class JobKt__JobKt {
    public static final CompletableJob Job(Job job) {
        return new JobImpl(job);
    }

    public static /* synthetic */ CompletableJob Job$default(Job job, int i, Object obj) {
        if ((i & 1) != 0) {
            job = null;
        }
        return JobKt.Job(job);
    }

    public static final Object cancelAndJoin(Job job, Continuation<? super Unit> continuation) {
        Job.DefaultImpls.cancel$default(job, null, 1, null);
        return job.join(continuation);
    }

    public static final void ensureActive(Job job) {
        Intrinsics.checkParameterIsNotNull(job, "$this$ensureActive");
        if (!job.isActive()) {
            throw job.getCancellationException();
        }
    }

    public static final void cancel(Job job, String str, Throwable th) {
        Intrinsics.checkParameterIsNotNull(job, "$this$cancel");
        Intrinsics.checkParameterIsNotNull(str, "message");
        job.cancel(ExceptionsKt.CancellationException(str, th));
    }

    public static /* synthetic */ void cancel$default(Job job, String str, Throwable th, int i, Object obj) {
        if ((i & 2) != 0) {
            th = null;
        }
        JobKt.cancel(job, str, th);
    }
}
