package com.android.wm.shell.common;

import android.os.Handler;
/* loaded from: classes2.dex */
public class HandlerExecutor implements ShellExecutor {
    private final Handler mHandler;

    public HandlerExecutor(Handler handler) {
        this.mHandler = handler;
    }

    @Override // com.android.wm.shell.common.ShellExecutor, java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        if (this.mHandler.getLooper().isCurrentThread()) {
            runnable.run();
        } else if (!this.mHandler.post(runnable)) {
            throw new RuntimeException(this.mHandler + " is probably exiting");
        }
    }

    @Override // com.android.wm.shell.common.ShellExecutor
    public void executeDelayed(Runnable runnable, long j) {
        if (!this.mHandler.postDelayed(runnable, j)) {
            throw new RuntimeException(this.mHandler + " is probably exiting");
        }
    }

    @Override // com.android.wm.shell.common.ShellExecutor
    public void removeCallbacks(Runnable runnable) {
        this.mHandler.removeCallbacks(runnable);
    }

    @Override // com.android.wm.shell.common.ShellExecutor
    public boolean hasCallback(Runnable runnable) {
        return this.mHandler.hasCallbacks(runnable);
    }
}
