package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SharedCoordinatorLogger.kt */
/* loaded from: classes.dex */
public final class SharedCoordinatorLogger {
    private final LogBuffer buffer;

    public SharedCoordinatorLogger(LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logUserOrProfileChanged(int i, String str) {
        Intrinsics.checkNotNullParameter(str, "profiles");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        SharedCoordinatorLogger$logUserOrProfileChanged$2 sharedCoordinatorLogger$logUserOrProfileChanged$2 = SharedCoordinatorLogger$logUserOrProfileChanged$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotCurrentUserFilter", logLevel, sharedCoordinatorLogger$logUserOrProfileChanged$2);
            obtain.setInt1(i);
            obtain.setStr1(str);
            logBuffer.push(obtain);
        }
    }
}
