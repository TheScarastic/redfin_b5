package kotlinx.coroutines.internal;

import kotlin.TypeCastException;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.ThreadContextElement;
/* compiled from: ThreadContext.kt */
/* loaded from: classes2.dex */
public final class ThreadContextKt {
    private static final Symbol ZERO = new Symbol("ZERO");
    private static final Function2<Object, CoroutineContext.Element, Object> countAll = ThreadContextKt$countAll$1.INSTANCE;
    private static final Function2<ThreadContextElement<?>, CoroutineContext.Element, ThreadContextElement<?>> findOne = ThreadContextKt$findOne$1.INSTANCE;
    private static final Function2<ThreadState, CoroutineContext.Element, ThreadState> updateState = ThreadContextKt$updateState$1.INSTANCE;
    private static final Function2<ThreadState, CoroutineContext.Element, ThreadState> restoreState = ThreadContextKt$restoreState$1.INSTANCE;

    public static final Object threadContextElements(CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Object fold = coroutineContext.fold(0, countAll);
        if (fold == null) {
            Intrinsics.throwNpe();
        }
        return fold;
    }

    public static final Object updateThreadContext(CoroutineContext coroutineContext, Object obj) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        if (obj == null) {
            obj = threadContextElements(coroutineContext);
        }
        if (obj == 0) {
            return ZERO;
        }
        if (obj instanceof Integer) {
            return coroutineContext.fold(new ThreadState(coroutineContext, ((Number) obj).intValue()), updateState);
        }
        if (obj != null) {
            return ((ThreadContextElement) obj).updateThreadContext(coroutineContext);
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.ThreadContextElement<kotlin.Any?>");
    }

    public static final void restoreThreadContext(CoroutineContext coroutineContext, Object obj) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        if (obj != ZERO) {
            if (obj instanceof ThreadState) {
                ((ThreadState) obj).start();
                coroutineContext.fold(obj, restoreState);
                return;
            }
            Object fold = coroutineContext.fold(null, findOne);
            if (fold != null) {
                ((ThreadContextElement) fold).restoreThreadContext(coroutineContext, obj);
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.ThreadContextElement<kotlin.Any?>");
        }
    }
}
