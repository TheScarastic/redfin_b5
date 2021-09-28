package com.android.systemui.util.concurrency;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
/* loaded from: classes2.dex */
public abstract class SysUIConcurrencyModule {
    public static Looper provideBgLooper() {
        HandlerThread handlerThread = new HandlerThread("SysUiBg", 10);
        handlerThread.start();
        return handlerThread.getLooper();
    }

    public static Looper provideLongRunningLooper() {
        HandlerThread handlerThread = new HandlerThread("SysUiLng", 10);
        handlerThread.start();
        return handlerThread.getLooper();
    }

    public static Handler provideBgHandler(Looper looper) {
        return new Handler(looper);
    }

    public static Executor provideExecutor(Looper looper) {
        return new ExecutorImpl(looper);
    }

    public static Executor provideLongRunningExecutor(Looper looper) {
        return new ExecutorImpl(looper);
    }

    public static Executor provideBackgroundExecutor(Looper looper) {
        return new ExecutorImpl(looper);
    }

    public static DelayableExecutor provideDelayableExecutor(Looper looper) {
        return new ExecutorImpl(looper);
    }

    public static DelayableExecutor provideBackgroundDelayableExecutor(Looper looper) {
        return new ExecutorImpl(looper);
    }

    public static DelayableExecutor provideMainDelayableExecutor(Looper looper) {
        return new ExecutorImpl(looper);
    }

    public static RepeatableExecutor provideBackgroundRepeatableExecutor(DelayableExecutor delayableExecutor) {
        return new RepeatableExecutorImpl(delayableExecutor);
    }

    public static Executor provideUiBackgroundExecutor() {
        return Executors.newSingleThreadExecutor();
    }
}
