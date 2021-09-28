package kotlin.jvm.internal;

import java.io.Serializable;
/* loaded from: classes2.dex */
public final class Ref$ObjectRef<T> implements Serializable {
    public T element;

    @Override // java.lang.Object
    public String toString() {
        return String.valueOf(this.element);
    }
}
