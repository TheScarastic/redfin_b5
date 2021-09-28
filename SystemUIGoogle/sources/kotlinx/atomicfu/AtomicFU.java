package kotlinx.atomicfu;
/* compiled from: AtomicFU.kt */
/* loaded from: classes2.dex */
public final class AtomicFU {
    public static final <T> AtomicRef<T> atomic(T t) {
        return new AtomicRef<>(t);
    }
}
