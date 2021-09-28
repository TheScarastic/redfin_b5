package com.google.common.util.concurrent;

import java.lang.Thread;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
/* loaded from: classes2.dex */
final class UncaughtExceptionHandlers$Exiter implements Thread.UncaughtExceptionHandler {
    private static final Logger logger = Logger.getLogger(UncaughtExceptionHandlers$Exiter.class.getName());
    private final Runtime runtime;

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        try {
            logger.log(Level.SEVERE, String.format(Locale.ROOT, "Caught an exception in %s.  Shutting down.", thread), th);
        } finally {
            try {
            } finally {
            }
        }
    }
}
