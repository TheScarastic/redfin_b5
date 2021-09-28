package kotlinx.coroutines;

import kotlin.coroutines.Continuation;
/* compiled from: Deferred.kt */
/* loaded from: classes2.dex */
public interface Deferred<T> extends Job {
    Object await(Continuation<? super T> continuation);
}
