package com.android.systemui.util.concurrency;

import android.os.Looper;
/* compiled from: Execution.kt */
/* loaded from: classes2.dex */
public final class ExecutionImpl implements Execution {
    private final Looper mainLooper = Looper.getMainLooper();

    @Override // com.android.systemui.util.concurrency.Execution
    public void assertIsMainThread() {
        if (!this.mainLooper.isCurrentThread()) {
            throw new IllegalStateException("should be called from the main thread. Main thread name=" + ((Object) this.mainLooper.getThread().getName()) + " Thread.currentThread()=" + ((Object) Thread.currentThread().getName()));
        }
    }
}
