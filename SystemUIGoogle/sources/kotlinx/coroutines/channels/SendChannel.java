package kotlinx.coroutines.channels;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
/* compiled from: Channel.kt */
/* loaded from: classes2.dex */
public interface SendChannel<E> {
    Object send(E e, Continuation<? super Unit> continuation);
}
