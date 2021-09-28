package com.android.systemui;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
/* loaded from: classes.dex */
public class UiOffloadThread {
    private final ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    public Future<?> execute(Runnable runnable) {
        return this.mExecutorService.submit(runnable);
    }
}
