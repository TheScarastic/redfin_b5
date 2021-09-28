package kotlin.coroutines.jvm.internal;

import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DebugProbes.kt */
/* loaded from: classes2.dex */
public final class DebugProbesKt {
    /* JADX DEBUG: Multi-variable search result rejected for r1v0, resolved type: kotlin.coroutines.Continuation<? super T> */
    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> Continuation<T> probeCoroutineCreated(Continuation<? super T> continuation) {
        Intrinsics.checkNotNullParameter(continuation, "completion");
        return continuation;
    }

    public static final void probeCoroutineResumed(Continuation<?> continuation) {
        Intrinsics.checkNotNullParameter(continuation, "frame");
    }

    public static final void probeCoroutineSuspended(Continuation<?> continuation) {
        Intrinsics.checkNotNullParameter(continuation, "frame");
    }
}
