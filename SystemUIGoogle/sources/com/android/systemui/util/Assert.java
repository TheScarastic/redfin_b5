package com.android.systemui.util;

import android.os.Looper;
/* loaded from: classes2.dex */
public class Assert {
    private static final Looper sMainLooper = Looper.getMainLooper();
    private static Thread sTestThread = null;

    public static void setTestableLooper(Looper looper) {
        setTestThread(looper == null ? null : looper.getThread());
    }

    public static void setTestThread(Thread thread) {
        sTestThread = thread;
    }

    public static void isMainThread() {
        Looper looper = sMainLooper;
        if (!looper.isCurrentThread()) {
            Thread thread = sTestThread;
            if (thread == null || thread != Thread.currentThread()) {
                throw new IllegalStateException("should be called from the main thread. sMainLooper.threadName=" + looper.getThread().getName() + " Thread.currentThread()=" + Thread.currentThread().getName());
            }
        }
    }

    public static void isNotMainThread() {
        if (sMainLooper.isCurrentThread()) {
            Thread thread = sTestThread;
            if (thread == null || thread == Thread.currentThread()) {
                throw new IllegalStateException("should not be called from the main thread.");
            }
        }
    }
}
