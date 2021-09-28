package kotlin;

import java.io.Serializable;
/* compiled from: Lazy.kt */
/* loaded from: classes2.dex */
public final class InitializedLazyImpl<T> implements Lazy<T>, Serializable {
    private final T value;

    public InitializedLazyImpl(T t) {
        this.value = t;
    }

    @Override // kotlin.Lazy
    public T getValue() {
        return this.value;
    }

    @Override // java.lang.Object
    public String toString() {
        return String.valueOf(getValue());
    }
}
