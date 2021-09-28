package com.android.wm.shell.onehanded;

import com.android.wm.shell.common.ShellExecutor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
/* loaded from: classes2.dex */
public class OneHandedTimeoutHandler {
    private final ShellExecutor mMainExecutor;
    private int mTimeout = 8;
    private long mTimeoutMs = TimeUnit.SECONDS.toMillis((long) 8);
    private final Runnable mTimeoutRunnable = new Runnable() { // from class: com.android.wm.shell.onehanded.OneHandedTimeoutHandler$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            OneHandedTimeoutHandler.$r8$lambda$liZALMVFn2QIVxvMh2gzg4zzohU(OneHandedTimeoutHandler.this);
        }
    };
    private List<TimeoutListener> mListeners = new ArrayList();

    /* loaded from: classes2.dex */
    public interface TimeoutListener {
        void onTimeout(int i);
    }

    public OneHandedTimeoutHandler(ShellExecutor shellExecutor) {
        this.mMainExecutor = shellExecutor;
    }

    public void setTimeout(int i) {
        this.mTimeout = i;
        this.mTimeoutMs = TimeUnit.SECONDS.toMillis((long) i);
        resetTimer();
    }

    public void removeTimer() {
        this.mMainExecutor.removeCallbacks(this.mTimeoutRunnable);
    }

    public void resetTimer() {
        removeTimer();
        int i = this.mTimeout;
        if (i != 0 && i != 0) {
            this.mMainExecutor.executeDelayed(this.mTimeoutRunnable, this.mTimeoutMs);
        }
    }

    public void registerTimeoutListener(TimeoutListener timeoutListener) {
        this.mListeners.add(timeoutListener);
    }

    boolean hasScheduledTimeout() {
        return this.mMainExecutor.hasCallback(this.mTimeoutRunnable);
    }

    /* access modifiers changed from: private */
    public void onStop() {
        for (int size = this.mListeners.size() - 1; size >= 0; size--) {
            this.mListeners.get(size).onTimeout(this.mTimeout);
        }
    }

    /* access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter) {
        printWriter.println("OneHandedTimeoutHandler");
        printWriter.print("  sTimeout=");
        printWriter.println(this.mTimeout);
        printWriter.print("  sListeners=");
        printWriter.println(this.mListeners);
    }
}
