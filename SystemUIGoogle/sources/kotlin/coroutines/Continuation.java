package kotlin.coroutines;
/* compiled from: Continuation.kt */
/* loaded from: classes2.dex */
public interface Continuation<T> {
    CoroutineContext getContext();

    void resumeWith(Object obj);
}
