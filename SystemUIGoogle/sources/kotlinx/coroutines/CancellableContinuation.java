package kotlinx.coroutines;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function1;
/* compiled from: CancellableContinuation.kt */
/* loaded from: classes2.dex */
public interface CancellableContinuation<T> extends Continuation<T> {
    void completeResume(Object obj);

    void invokeOnCancellation(Function1<? super Throwable, Unit> function1);

    Object tryResume(T t, Object obj);

    Object tryResumeWithException(Throwable th);

    /* compiled from: CancellableContinuation.kt */
    /* loaded from: classes2.dex */
    public static final class DefaultImpls {
        public static /* synthetic */ Object tryResume$default(CancellableContinuation cancellableContinuation, Object obj, Object obj2, int i, Object obj3) {
            if (obj3 == null) {
                if ((i & 2) != 0) {
                    obj2 = null;
                }
                return cancellableContinuation.tryResume(obj, obj2);
            }
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: tryResume");
        }
    }
}
