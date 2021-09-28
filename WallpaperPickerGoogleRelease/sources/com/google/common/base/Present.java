package com.google.common.base;

import androidx.viewpager2.widget.FakeDrag$$ExternalSyntheticOutline0;
/* loaded from: classes.dex */
public final class Present<T> extends Optional<T> {
    private static final long serialVersionUID = 0;
    private final T reference;

    public Present(T t) {
        this.reference = t;
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        if (obj instanceof Present) {
            return this.reference.equals(((Present) obj).reference);
        }
        return false;
    }

    @Override // com.google.common.base.Optional
    public T get() {
        return this.reference;
    }

    @Override // java.lang.Object
    public int hashCode() {
        return this.reference.hashCode() + 1502476572;
    }

    @Override // com.google.common.base.Optional
    public T or(T t) {
        return this.reference;
    }

    @Override // java.lang.Object
    public String toString() {
        String valueOf = String.valueOf(this.reference);
        return FakeDrag$$ExternalSyntheticOutline0.m(valueOf.length() + 13, "Optional.of(", valueOf, ")");
    }
}
