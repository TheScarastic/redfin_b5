package com.google.android.systemui.assist.uihints;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.android.systemui.assist.AssistManager;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import dagger.Lazy;
import java.util.concurrent.TimeUnit;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class TimeoutManager implements NgaMessageHandler.KeepAliveListener {
    private static final long SESSION_TIMEOUT_MS = TimeUnit.SECONDS.toMillis(10);
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Runnable mOnTimeout;
    private TimeoutCallback mTimeoutCallback;

    /* access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public interface TimeoutCallback {
        void onTimeout();
    }

    /* access modifiers changed from: package-private */
    public TimeoutManager(Lazy<AssistManager> lazy) {
        this.mOnTimeout = new Runnable(lazy) { // from class: com.google.android.systemui.assist.uihints.TimeoutManager$$ExternalSyntheticLambda0
            public final /* synthetic */ Lazy f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                TimeoutManager.this.lambda$new$0(this.f$1);
            }
        };
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Lazy lazy) {
        TimeoutCallback timeoutCallback = this.mTimeoutCallback;
        if (timeoutCallback != null) {
            timeoutCallback.onTimeout();
            return;
        }
        Log.e("TimeoutManager", "Timeout occurred, but there was no callback provided");
        ((AssistManager) lazy.get()).hideAssist();
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.KeepAliveListener
    public void onKeepAlive(String str) {
        resetTimeout();
    }

    /* access modifiers changed from: package-private */
    public void resetTimeout() {
        this.mHandler.removeCallbacks(this.mOnTimeout);
        this.mHandler.postDelayed(this.mOnTimeout, SESSION_TIMEOUT_MS);
    }

    /* access modifiers changed from: package-private */
    public void setTimeoutCallback(TimeoutCallback timeoutCallback) {
        this.mTimeoutCallback = timeoutCallback;
    }
}
