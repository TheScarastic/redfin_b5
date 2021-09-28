package com.google.common.util.concurrent;
/* loaded from: classes2.dex */
public class UncheckedExecutionException extends RuntimeException {
    private static final long serialVersionUID = 0;

    protected UncheckedExecutionException() {
    }

    protected UncheckedExecutionException(String str) {
        super(str);
    }

    public UncheckedExecutionException(Throwable th) {
        super(th);
    }
}
