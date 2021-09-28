package com.google.common.base;
/* loaded from: classes.dex */
public final class Absent<T> extends Optional<T> {
    public static final Absent<Object> INSTANCE = new Absent<>();
    private static final long serialVersionUID = 0;

    private Object readResolve() {
        return INSTANCE;
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        return obj == this;
    }

    @Override // com.google.common.base.Optional
    public T get() {
        throw new IllegalStateException("Optional.get() cannot be called on an absent value");
    }

    @Override // java.lang.Object
    public int hashCode() {
        return 2040732332;
    }

    @Override // com.google.common.base.Optional
    public T or(T t) {
        return t;
    }

    @Override // java.lang.Object
    public String toString() {
        return "Optional.absent()";
    }
}
