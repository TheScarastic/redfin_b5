package kotlinx.coroutines;

import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ThreadContextElement.kt */
/* loaded from: classes2.dex */
public interface ThreadContextElement<S> extends CoroutineContext.Element {

    /* compiled from: ThreadContextElement.kt */
    /* loaded from: classes2.dex */
    public static final class DefaultImpls {
        public static <S, R> R fold(ThreadContextElement<S> threadContextElement, R r, Function2<? super R, ? super CoroutineContext.Element, ? extends R> function2) {
            Intrinsics.checkParameterIsNotNull(function2, "operation");
            return (R) CoroutineContext.Element.DefaultImpls.fold(threadContextElement, r, function2);
        }

        public static <S, E extends CoroutineContext.Element> E get(ThreadContextElement<S> threadContextElement, CoroutineContext.Key<E> key) {
            Intrinsics.checkParameterIsNotNull(key, "key");
            return (E) CoroutineContext.Element.DefaultImpls.get(threadContextElement, key);
        }

        public static <S> CoroutineContext minusKey(ThreadContextElement<S> threadContextElement, CoroutineContext.Key<?> key) {
            Intrinsics.checkParameterIsNotNull(key, "key");
            return CoroutineContext.Element.DefaultImpls.minusKey(threadContextElement, key);
        }

        public static <S> CoroutineContext plus(ThreadContextElement<S> threadContextElement, CoroutineContext coroutineContext) {
            Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
            return CoroutineContext.Element.DefaultImpls.plus(threadContextElement, coroutineContext);
        }
    }

    void restoreThreadContext(CoroutineContext coroutineContext, S s);

    S updateThreadContext(CoroutineContext coroutineContext);
}
