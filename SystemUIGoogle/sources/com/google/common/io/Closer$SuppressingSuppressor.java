package com.google.common.io;

import java.lang.reflect.Method;
/* loaded from: classes2.dex */
final class Closer$SuppressingSuppressor implements Closer$Suppressor {
    static final Closer$SuppressingSuppressor INSTANCE = new Closer$SuppressingSuppressor();
    static final Method addSuppressed = addSuppressedMethodOrNull();

    Closer$SuppressingSuppressor() {
    }

    private static Method addSuppressedMethodOrNull() {
        try {
            return Throwable.class.getMethod("addSuppressed", Throwable.class);
        } catch (Throwable unused) {
            return null;
        }
    }
}
