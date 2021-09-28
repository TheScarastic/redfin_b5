package kotlinx.coroutines.internal;

import java.lang.Comparable;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.EventLoopImplBase;
import kotlinx.coroutines.internal.ThreadSafeHeapNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public class ThreadSafeHeap<T extends ThreadSafeHeapNode & Comparable<? super T>> {
    public volatile int _size = 0;
    public T[] a;

    static {
        AtomicIntegerFieldUpdater.newUpdater(ThreadSafeHeap.class, "_size");
    }

    public final void addImpl(@NotNull T t) {
        boolean z = DebugKt.DEBUG;
        ((EventLoopImplBase.DelayedTask) t).setHeap(this);
        T[] tArr = this.a;
        if (tArr == null) {
            tArr = (T[]) new ThreadSafeHeapNode[4];
            this.a = tArr;
        } else if (this._size >= tArr.length) {
            Object[] copyOf = Arrays.copyOf(tArr, this._size * 2);
            Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, newSize)");
            tArr = (T[]) ((ThreadSafeHeapNode[]) copyOf);
            this.a = tArr;
        }
        int i = this._size;
        this._size = i + 1;
        tArr[i] = t;
        siftUpFrom(i);
    }

    @Nullable
    public final T firstImpl() {
        T[] tArr = this.a;
        if (tArr != null) {
            return tArr[0];
        }
        return null;
    }

    @NotNull
    public final T removeAtImpl(int i) {
        boolean z = DebugKt.DEBUG;
        T[] tArr = this.a;
        if (tArr != null) {
            this._size--;
            if (i < this._size) {
                swap(i, this._size);
                int i2 = (i - 1) / 2;
                if (i > 0) {
                    T t = tArr[i];
                    if (t != null) {
                        Comparable comparable = (Comparable) t;
                        T t2 = tArr[i2];
                        if (t2 == null) {
                            Intrinsics.throwNpe();
                            throw null;
                        } else if (comparable.compareTo(t2) < 0) {
                            swap(i, i2);
                            siftUpFrom(i2);
                        }
                    } else {
                        Intrinsics.throwNpe();
                        throw null;
                    }
                }
                while (true) {
                    int i3 = (i * 2) + 1;
                    if (i3 >= this._size) {
                        break;
                    }
                    T[] tArr2 = this.a;
                    if (tArr2 != null) {
                        int i4 = i3 + 1;
                        if (i4 < this._size) {
                            T t3 = tArr2[i4];
                            if (t3 != null) {
                                Comparable comparable2 = (Comparable) t3;
                                T t4 = tArr2[i3];
                                if (t4 == null) {
                                    Intrinsics.throwNpe();
                                    throw null;
                                } else if (comparable2.compareTo(t4) < 0) {
                                    i3 = i4;
                                }
                            } else {
                                Intrinsics.throwNpe();
                                throw null;
                            }
                        }
                        T t5 = tArr2[i];
                        if (t5 != null) {
                            Comparable comparable3 = (Comparable) t5;
                            T t6 = tArr2[i3];
                            if (t6 == null) {
                                Intrinsics.throwNpe();
                                throw null;
                            } else if (comparable3.compareTo(t6) <= 0) {
                                break;
                            } else {
                                swap(i, i3);
                                i = i3;
                            }
                        } else {
                            Intrinsics.throwNpe();
                            throw null;
                        }
                    } else {
                        Intrinsics.throwNpe();
                        throw null;
                    }
                }
            }
            T t7 = tArr[this._size];
            if (t7 != null) {
                boolean z2 = DebugKt.DEBUG;
                t7.setHeap(null);
                t7.setIndex(-1);
                tArr[this._size] = null;
                return t7;
            }
            Intrinsics.throwNpe();
            throw null;
        }
        Intrinsics.throwNpe();
        throw null;
    }

    public final void siftUpFrom(int i) {
        while (i > 0) {
            T[] tArr = this.a;
            if (tArr != null) {
                int i2 = (i - 1) / 2;
                T t = tArr[i2];
                if (t != null) {
                    Comparable comparable = (Comparable) t;
                    T t2 = tArr[i];
                    if (t2 == null) {
                        Intrinsics.throwNpe();
                        throw null;
                    } else if (comparable.compareTo(t2) > 0) {
                        swap(i, i2);
                        i = i2;
                    } else {
                        return;
                    }
                } else {
                    Intrinsics.throwNpe();
                    throw null;
                }
            } else {
                Intrinsics.throwNpe();
                throw null;
            }
        }
    }

    public final void swap(int i, int i2) {
        T[] tArr = this.a;
        if (tArr != null) {
            T t = tArr[i2];
            if (t != null) {
                T t2 = tArr[i];
                if (t2 != null) {
                    tArr[i] = t;
                    tArr[i2] = t2;
                    t.setIndex(i);
                    t2.setIndex(i2);
                    return;
                }
                Intrinsics.throwNpe();
                throw null;
            }
            Intrinsics.throwNpe();
            throw null;
        }
        Intrinsics.throwNpe();
        throw null;
    }
}
