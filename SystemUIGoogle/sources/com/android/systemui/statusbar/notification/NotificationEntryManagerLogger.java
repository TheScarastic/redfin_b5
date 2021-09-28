package com.android.systemui.statusbar.notification;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationEntryManagerLogger.kt */
/* loaded from: classes.dex */
public final class NotificationEntryManagerLogger {
    private final LogBuffer buffer;

    public NotificationEntryManagerLogger(LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logNotifAdded(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotificationEntryManagerLogger$logNotifAdded$2 notificationEntryManagerLogger$logNotifAdded$2 = NotificationEntryManagerLogger$logNotifAdded$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logNotifAdded$2);
            obtain.setStr1(str);
            logBuffer.push(obtain);
        }
    }

    public final void logNotifUpdated(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotificationEntryManagerLogger$logNotifUpdated$2 notificationEntryManagerLogger$logNotifUpdated$2 = NotificationEntryManagerLogger$logNotifUpdated$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logNotifUpdated$2);
            obtain.setStr1(str);
            logBuffer.push(obtain);
        }
    }

    public final void logInflationAborted(String str, String str2, String str3) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(str2, "status");
        Intrinsics.checkNotNullParameter(str3, "reason");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationEntryManagerLogger$logInflationAborted$2 notificationEntryManagerLogger$logInflationAborted$2 = NotificationEntryManagerLogger$logInflationAborted$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logInflationAborted$2);
            obtain.setStr1(str);
            obtain.setStr2(str2);
            obtain.setStr3(str3);
            logBuffer.push(obtain);
        }
    }

    public final void logNotifInflated(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationEntryManagerLogger$logNotifInflated$2 notificationEntryManagerLogger$logNotifInflated$2 = NotificationEntryManagerLogger$logNotifInflated$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logNotifInflated$2);
            obtain.setStr1(str);
            obtain.setBool1(z);
            logBuffer.push(obtain);
        }
    }

    public final void logRemovalIntercepted(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotificationEntryManagerLogger$logRemovalIntercepted$2 notificationEntryManagerLogger$logRemovalIntercepted$2 = NotificationEntryManagerLogger$logRemovalIntercepted$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logRemovalIntercepted$2);
            obtain.setStr1(str);
            logBuffer.push(obtain);
        }
    }

    public final void logLifetimeExtended(String str, String str2, String str3) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(str2, "extenderName");
        Intrinsics.checkNotNullParameter(str3, "status");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotificationEntryManagerLogger$logLifetimeExtended$2 notificationEntryManagerLogger$logLifetimeExtended$2 = NotificationEntryManagerLogger$logLifetimeExtended$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logLifetimeExtended$2);
            obtain.setStr1(str);
            obtain.setStr2(str2);
            obtain.setStr3(str3);
            logBuffer.push(obtain);
        }
    }

    public final void logNotifRemoved(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotificationEntryManagerLogger$logNotifRemoved$2 notificationEntryManagerLogger$logNotifRemoved$2 = NotificationEntryManagerLogger$logNotifRemoved$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logNotifRemoved$2);
            obtain.setStr1(str);
            obtain.setBool1(z);
            logBuffer.push(obtain);
        }
    }

    public final void logFilterAndSort(String str) {
        Intrinsics.checkNotNullParameter(str, "reason");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotificationEntryManagerLogger$logFilterAndSort$2 notificationEntryManagerLogger$logFilterAndSort$2 = NotificationEntryManagerLogger$logFilterAndSort$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", logLevel, notificationEntryManagerLogger$logFilterAndSort$2);
            obtain.setStr1(str);
            logBuffer.push(obtain);
        }
    }
}
