package kotlinx.coroutines.internal;

import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlinx.coroutines.ThreadContextElement;
/* loaded from: classes.dex */
public final class ThreadContextKt$updateState$1 extends Lambda implements Function2<ThreadState, CoroutineContext.Element, ThreadState> {
    public static final ThreadContextKt$updateState$1 INSTANCE = new ThreadContextKt$updateState$1();

    public ThreadContextKt$updateState$1() {
        super(2);
    }

    /* Return type fixed from 'java.lang.Object' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
    @Override // kotlin.jvm.functions.Function2
    public ThreadState invoke(ThreadState threadState, CoroutineContext.Element element) {
        ThreadState threadState2 = threadState;
        CoroutineContext.Element element2 = element;
        Intrinsics.checkParameterIsNotNull(threadState2, "state");
        Intrinsics.checkParameterIsNotNull(element2, "element");
        if (element2 instanceof ThreadContextElement) {
            Object updateThreadContext = ((ThreadContextElement) element2).updateThreadContext(threadState2.context);
            Object[] objArr = threadState2.a;
            int i = threadState2.i;
            threadState2.i = i + 1;
            objArr[i] = updateThreadContext;
        }
        return threadState2;
    }
}
