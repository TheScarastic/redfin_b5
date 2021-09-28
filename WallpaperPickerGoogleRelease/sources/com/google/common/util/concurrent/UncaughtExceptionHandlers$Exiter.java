package com.google.common.util.concurrent;

import java.lang.Thread;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
/* loaded from: classes.dex */
public final class UncaughtExceptionHandlers$Exiter implements Thread.UncaughtExceptionHandler {
    public static final Logger logger = Logger.getLogger(UncaughtExceptionHandlers$Exiter.class.getName());

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        try {
            logger.logp(Level.SEVERE, "com.google.common.util.concurrent.UncaughtExceptionHandlers$Exiter", "uncaughtException", String.format(Locale.ROOT, "Caught an exception in %s.  Shutting down.", thread), th);
            throw null;
        } catch (Throwable th2) {
            try {
                System.err.println(th.getMessage());
                System.err.println(th2.getMessage());
                throw null;
            } catch (Throwable unused) {
                throw null;
            }
        }
    }
}
