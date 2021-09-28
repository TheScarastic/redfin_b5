package com.google.common.base;

import androidx.viewpager2.widget.FakeDrag$$ExternalSyntheticOutline0;
import java.io.Serializable;
/* loaded from: classes.dex */
public class Suppliers$MemoizingSupplier<T> implements Supplier<T>, Serializable {
    private static final long serialVersionUID = 0;
    public final Supplier<T> delegate;
    public volatile transient boolean initialized;
    public transient T value;

    @Override // com.google.common.base.Supplier
    public T get() {
        if (!this.initialized) {
            synchronized (this) {
                if (!this.initialized) {
                    T t = this.delegate.get();
                    this.value = t;
                    this.initialized = true;
                    return t;
                }
            }
        }
        return this.value;
    }

    @Override // java.lang.Object
    public String toString() {
        Object obj;
        if (this.initialized) {
            String valueOf = String.valueOf(this.value);
            obj = FakeDrag$$ExternalSyntheticOutline0.m(valueOf.length() + 25, "<supplier that returned ", valueOf, ">");
        } else {
            obj = this.delegate;
        }
        String valueOf2 = String.valueOf(obj);
        return FakeDrag$$ExternalSyntheticOutline0.m(valueOf2.length() + 19, "Suppliers.memoize(", valueOf2, ")");
    }
}
