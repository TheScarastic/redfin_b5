package com.google.common.base;

import java.io.Serializable;
/* loaded from: classes.dex */
public abstract class Optional<T> implements Serializable {
    private static final long serialVersionUID = 0;

    public abstract T get();

    public abstract T or(T t);
}
