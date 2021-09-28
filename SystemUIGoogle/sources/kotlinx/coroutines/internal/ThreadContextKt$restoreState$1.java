package kotlinx.coroutines.internal;

import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlinx.coroutines.ThreadContextElement;
/* compiled from: ThreadContext.kt */
/* loaded from: classes2.dex */
final class ThreadContextKt$restoreState$1 extends Lambda implements Function2<ThreadState, CoroutineContext.Element, ThreadState> {
    public static final ThreadContextKt$restoreState$1 INSTANCE = new ThreadContextKt$restoreState$1();

    ThreadContextKt$restoreState$1() {
        super(2);
    }

    public final ThreadState invoke(ThreadState threadState, CoroutineContext.Element element) {
        Intrinsics.checkParameterIsNotNull(threadState, "state");
        Intrinsics.checkParameterIsNotNull(element, "element");
        if (element instanceof ThreadContextElement) {
            ((ThreadContextElement) element).restoreThreadContext(threadState.getContext(), threadState.take());
        }
        return threadState;
    }
}
