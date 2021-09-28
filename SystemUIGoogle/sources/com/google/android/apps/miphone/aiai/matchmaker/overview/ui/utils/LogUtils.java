package com.google.android.apps.miphone.aiai.matchmaker.overview.ui.utils;

import android.support.annotation.Nullable;
import android.util.Log;
/* loaded from: classes2.dex */
public final class LogUtils {
    private static final LoggingState loggingState = new LoggingState();

    public static void d(String str) {
        if (loggingState.loggingEnabled) {
            Log.d("AiAiSuggestUi", str);
        }
    }

    public static void i(String str) {
        Log.i("AiAiSuggestUi", str);
    }

    public static void e(String str) {
        e(str, null);
    }

    public static void e(String str, @Nullable Throwable th) {
        Log.e("AiAiSuggestUi", str, th);
    }

    /* loaded from: classes2.dex */
    private static class LoggingState {
        boolean loggingEnabled;

        private LoggingState() {
            this.loggingEnabled = false;
        }
    }
}
