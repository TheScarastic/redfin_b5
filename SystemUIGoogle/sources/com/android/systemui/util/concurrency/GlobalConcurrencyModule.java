package com.android.systemui.util.concurrency;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public abstract class GlobalConcurrencyModule {
    public static Looper provideMainLooper() {
        return Looper.getMainLooper();
    }

    public static Handler provideMainHandler(Looper looper) {
        return new Handler(looper);
    }

    public static Executor provideMainExecutor(Context context) {
        return context.getMainExecutor();
    }
}
