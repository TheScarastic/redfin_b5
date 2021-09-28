package com.android.systemui.util.concurrency;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public interface ThreadFactory {
    DelayableExecutor buildDelayableExecutorOnHandler(Handler handler);

    Executor buildExecutorOnNewThread(String str);

    Handler buildHandlerOnNewThread(String str);

    Looper buildLooperOnNewThread(String str);
}
