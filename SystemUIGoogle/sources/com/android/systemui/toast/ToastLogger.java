package com.android.systemui.toast;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ToastLogger.kt */
/* loaded from: classes2.dex */
public final class ToastLogger {
    private final LogBuffer buffer;

    public ToastLogger(LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logOnShowToast(int i, String str, String str2, String str3) {
        Intrinsics.checkNotNullParameter(str, "packageName");
        Intrinsics.checkNotNullParameter(str2, "text");
        Intrinsics.checkNotNullParameter(str3, "token");
        LogLevel logLevel = LogLevel.DEBUG;
        ToastLogger$logOnShowToast$2 toastLogger$logOnShowToast$2 = ToastLogger$logOnShowToast$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("ToastLog", logLevel, toastLogger$logOnShowToast$2);
            obtain.setInt1(i);
            obtain.setStr1(str);
            obtain.setStr2(str2);
            obtain.setStr3(str3);
            logBuffer.push(obtain);
        }
    }

    public final void logOnHideToast(String str, String str2) {
        Intrinsics.checkNotNullParameter(str, "packageName");
        Intrinsics.checkNotNullParameter(str2, "token");
        LogLevel logLevel = LogLevel.DEBUG;
        ToastLogger$logOnHideToast$2 toastLogger$logOnHideToast$2 = ToastLogger$logOnHideToast$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("ToastLog", logLevel, toastLogger$logOnHideToast$2);
            obtain.setStr1(str);
            obtain.setStr2(str2);
            logBuffer.push(obtain);
        }
    }

    public final void logOrientationChange(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "text");
        LogLevel logLevel = LogLevel.DEBUG;
        ToastLogger$logOrientationChange$2 toastLogger$logOrientationChange$2 = ToastLogger$logOrientationChange$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("ToastLog", logLevel, toastLogger$logOrientationChange$2);
            obtain.setStr1(str);
            obtain.setBool1(z);
            logBuffer.push(obtain);
        }
    }
}
