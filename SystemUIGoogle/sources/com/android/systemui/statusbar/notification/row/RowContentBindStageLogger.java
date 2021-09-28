package com.android.systemui.statusbar.notification.row;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: RowContentBindStageLogger.kt */
/* loaded from: classes.dex */
public final class RowContentBindStageLogger {
    private final LogBuffer buffer;

    public RowContentBindStageLogger(LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logStageParams(String str, String str2) {
        Intrinsics.checkNotNullParameter(str, "notifKey");
        Intrinsics.checkNotNullParameter(str2, "stageParams");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        RowContentBindStageLogger$logStageParams$2 rowContentBindStageLogger$logStageParams$2 = RowContentBindStageLogger$logStageParams$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("RowContentBindStage", logLevel, rowContentBindStageLogger$logStageParams$2);
            obtain.setStr1(str);
            obtain.setStr2(str2);
            logBuffer.push(obtain);
        }
    }
}
