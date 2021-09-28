package com.google.common.base;

import java.io.Serializable;
/* loaded from: classes.dex */
public class Suppliers$ExpiringMemoizingSupplier<T> implements Supplier<T>, Serializable {
    private static final long serialVersionUID = 0;
    public final Supplier<T> delegate;
    public final long durationNanos;
    public volatile transient long expirationNanos;
    public volatile transient T value;

    @Override // com.google.common.base.Supplier
    public T get() {
        long j = this.expirationNanos;
        int i = Platform.$r8$clinit;
        long nanoTime = System.nanoTime();
        if (j == 0 || nanoTime - j >= 0) {
            synchronized (this) {
                if (j == this.expirationNanos) {
                    T t = this.delegate.get();
                    this.value = t;
                    long j2 = nanoTime + this.durationNanos;
                    if (j2 == 0) {
                        j2 = 1;
                    }
                    this.expirationNanos = j2;
                    return t;
                }
            }
        }
        return this.value;
    }

    @Override // java.lang.Object
    public String toString() {
        String valueOf = String.valueOf(this.delegate);
        long j = this.durationNanos;
        StringBuilder sb = new StringBuilder(valueOf.length() + 62);
        sb.append("Suppliers.memoizeWithExpiration(");
        sb.append(valueOf);
        sb.append(", ");
        sb.append(j);
        sb.append(", NANOS)");
        return sb.toString();
    }
}
