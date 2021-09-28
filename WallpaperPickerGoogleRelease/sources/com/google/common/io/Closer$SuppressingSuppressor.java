package com.google.common.io;
/* loaded from: classes.dex */
public final class Closer$SuppressingSuppressor implements Closer$Suppressor {
    static {
        new Closer$SuppressingSuppressor();
        try {
            Throwable.class.getMethod("addSuppressed", Throwable.class);
        } catch (Throwable unused) {
        }
    }
}
