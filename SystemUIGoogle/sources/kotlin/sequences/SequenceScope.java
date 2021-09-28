package kotlin.sequences;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
/* compiled from: SequenceBuilder.kt */
/* loaded from: classes2.dex */
public abstract class SequenceScope<T> {
    public abstract Object yield(T t, Continuation<? super Unit> continuation);
}
