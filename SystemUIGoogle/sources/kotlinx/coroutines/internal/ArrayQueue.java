package kotlinx.coroutines.internal;

import kotlin.TypeCastException;
import kotlin.collections.ArraysKt___ArraysJvmKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ArrayQueue.kt */
/* loaded from: classes2.dex */
public class ArrayQueue<T> {
    private Object[] elements = new Object[16];
    private int head;
    private int tail;

    public final boolean isEmpty() {
        return this.head == this.tail;
    }

    public final void addLast(T t) {
        Intrinsics.checkParameterIsNotNull(t, "element");
        Object[] objArr = this.elements;
        int i = this.tail;
        objArr[i] = t;
        int length = (objArr.length - 1) & (i + 1);
        this.tail = length;
        if (length == this.head) {
            ensureCapacity();
        }
    }

    public final T removeFirstOrNull() {
        int i = this.head;
        if (i == this.tail) {
            return null;
        }
        Object[] objArr = this.elements;
        T t = (T) objArr[i];
        objArr[i] = null;
        this.head = (i + 1) & (objArr.length - 1);
        if (t != null) {
            return t;
        }
        throw new TypeCastException("null cannot be cast to non-null type T");
    }

    private final void ensureCapacity() {
        Object[] objArr = this.elements;
        int length = objArr.length;
        Object[] objArr2 = new Object[length << 1];
        Object[] unused = ArraysKt___ArraysJvmKt.copyInto$default(objArr, objArr2, 0, this.head, 0, 10, (Object) null);
        Object[] objArr3 = this.elements;
        int length2 = objArr3.length;
        int i = this.head;
        Object[] unused2 = ArraysKt___ArraysJvmKt.copyInto$default(objArr3, objArr2, length2 - i, 0, i, 4, (Object) null);
        this.elements = objArr2;
        this.head = 0;
        this.tail = length;
    }
}
