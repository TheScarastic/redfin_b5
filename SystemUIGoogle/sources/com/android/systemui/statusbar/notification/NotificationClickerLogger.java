package com.android.systemui.statusbar.notification;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationClickerLogger.kt */
/* loaded from: classes.dex */
public final class NotificationClickerLogger {
    private final LogBuffer buffer;

    public NotificationClickerLogger(LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logOnClick(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationClickerLogger$logOnClick$2 notificationClickerLogger$logOnClick$2 = NotificationClickerLogger$logOnClick$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationClicker", logLevel, notificationClickerLogger$logOnClick$2);
            obtain.setStr1(notificationEntry.getKey());
            obtain.setStr2(notificationEntry.getRanking().getChannel().getId());
            logBuffer.push(obtain);
        }
    }

    public final void logMenuVisible(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationClickerLogger$logMenuVisible$2 notificationClickerLogger$logMenuVisible$2 = NotificationClickerLogger$logMenuVisible$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationClicker", logLevel, notificationClickerLogger$logMenuVisible$2);
            obtain.setStr1(notificationEntry.getKey());
            logBuffer.push(obtain);
        }
    }

    public final void logParentMenuVisible(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationClickerLogger$logParentMenuVisible$2 notificationClickerLogger$logParentMenuVisible$2 = NotificationClickerLogger$logParentMenuVisible$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationClicker", logLevel, notificationClickerLogger$logParentMenuVisible$2);
            obtain.setStr1(notificationEntry.getKey());
            logBuffer.push(obtain);
        }
    }

    public final void logChildrenExpanded(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationClickerLogger$logChildrenExpanded$2 notificationClickerLogger$logChildrenExpanded$2 = NotificationClickerLogger$logChildrenExpanded$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationClicker", logLevel, notificationClickerLogger$logChildrenExpanded$2);
            obtain.setStr1(notificationEntry.getKey());
            logBuffer.push(obtain);
        }
    }

    public final void logGutsExposed(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationClickerLogger$logGutsExposed$2 notificationClickerLogger$logGutsExposed$2 = NotificationClickerLogger$logGutsExposed$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotificationClicker", logLevel, notificationClickerLogger$logGutsExposed$2);
            obtain.setStr1(notificationEntry.getKey());
            logBuffer.push(obtain);
        }
    }
}
