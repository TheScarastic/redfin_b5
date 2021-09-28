package kotlinx.coroutines;

import java.util.concurrent.CancellationException;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes.dex */
public final class JobKt {
    public static CompletableJob SupervisorJob$default(Job job, int i) {
        return new SupervisorJobImpl(null);
    }

    public static void cancel$default(CoroutineContext coroutineContext, CancellationException cancellationException, int i, Object obj) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "$this$cancel");
        Job job = (Job) coroutineContext.get(Job.Key);
        if (job != null) {
            job.cancel(null);
        }
    }
}
