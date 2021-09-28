package com.android.systemui.util.concurrency;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
/* loaded from: classes2.dex */
public class ExecutorImpl implements DelayableExecutor {
    private final Handler mHandler;

    /* access modifiers changed from: package-private */
    public ExecutorImpl(Looper looper) {
        this.mHandler = new Handler(looper, new Handler.Callback() { // from class: com.android.systemui.util.concurrency.ExecutorImpl$$ExternalSyntheticLambda0
            @Override // android.os.Handler.Callback
            public final boolean handleMessage(Message message) {
                return ExecutorImpl.m400$r8$lambda$3qnbdTwVm37hDTz8QRSMQTjR2A(ExecutorImpl.this, message);
            }
        });
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        if (!this.mHandler.post(runnable)) {
            throw new RejectedExecutionException(this.mHandler + " is shutting down");
        }
    }

    @Override // com.android.systemui.util.concurrency.DelayableExecutor
    public Runnable executeDelayed(Runnable runnable, long j, TimeUnit timeUnit) {
        ExecutionToken executionToken = new ExecutionToken(runnable);
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0, executionToken), timeUnit.toMillis(j));
        return executionToken;
    }

    @Override // com.android.systemui.util.concurrency.DelayableExecutor
    public Runnable executeAtTime(Runnable runnable, long j, TimeUnit timeUnit) {
        ExecutionToken executionToken = new ExecutionToken(runnable);
        this.mHandler.sendMessageAtTime(this.mHandler.obtainMessage(0, executionToken), timeUnit.toMillis(j));
        return executionToken;
    }

    /* access modifiers changed from: private */
    public boolean onHandleMessage(Message message) {
        if (message.what == 0) {
            ((ExecutionToken) message.obj).runnable.run();
            return true;
        }
        throw new IllegalStateException("Unrecognized message: " + message.what);
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class ExecutionToken implements Runnable {
        public final Runnable runnable;

        private ExecutionToken(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override // java.lang.Runnable
        public void run() {
            ExecutorImpl.this.mHandler.removeCallbacksAndMessages(this);
        }
    }
}
