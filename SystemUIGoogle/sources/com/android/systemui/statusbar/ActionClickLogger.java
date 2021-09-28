package com.android.systemui.statusbar;

import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.service.notification.NotificationListenerService;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ActionClickLogger.kt */
/* loaded from: classes.dex */
public final class ActionClickLogger {
    private final LogBuffer buffer;

    public ActionClickLogger(LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logInitialClick(NotificationEntry notificationEntry, PendingIntent pendingIntent) {
        NotificationListenerService.Ranking ranking;
        NotificationChannel channel;
        Intrinsics.checkNotNullParameter(pendingIntent, "pendingIntent");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        ActionClickLogger$logInitialClick$2 actionClickLogger$logInitialClick$2 = ActionClickLogger$logInitialClick$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("ActionClickLogger", logLevel, actionClickLogger$logInitialClick$2);
            String str = null;
            obtain.setStr1(notificationEntry == null ? null : notificationEntry.getKey());
            if (notificationEntry == null) {
                ranking = null;
            } else {
                ranking = notificationEntry.getRanking();
            }
            if (!(ranking == null || (channel = ranking.getChannel()) == null)) {
                str = channel.getId();
            }
            obtain.setStr2(str);
            obtain.setStr3(pendingIntent.getIntent().toString());
            logBuffer.push(obtain);
        }
    }

    public final void logRemoteInputWasHandled(NotificationEntry notificationEntry) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        ActionClickLogger$logRemoteInputWasHandled$2 actionClickLogger$logRemoteInputWasHandled$2 = ActionClickLogger$logRemoteInputWasHandled$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("ActionClickLogger", logLevel, actionClickLogger$logRemoteInputWasHandled$2);
            obtain.setStr1(notificationEntry == null ? null : notificationEntry.getKey());
            logBuffer.push(obtain);
        }
    }

    public final void logStartingIntentWithDefaultHandler(NotificationEntry notificationEntry, PendingIntent pendingIntent) {
        Intrinsics.checkNotNullParameter(pendingIntent, "pendingIntent");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        ActionClickLogger$logStartingIntentWithDefaultHandler$2 actionClickLogger$logStartingIntentWithDefaultHandler$2 = ActionClickLogger$logStartingIntentWithDefaultHandler$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("ActionClickLogger", logLevel, actionClickLogger$logStartingIntentWithDefaultHandler$2);
            obtain.setStr1(notificationEntry == null ? null : notificationEntry.getKey());
            obtain.setStr2(pendingIntent.getIntent().toString());
            logBuffer.push(obtain);
        }
    }

    public final void logWaitingToCloseKeyguard(PendingIntent pendingIntent) {
        Intrinsics.checkNotNullParameter(pendingIntent, "pendingIntent");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        ActionClickLogger$logWaitingToCloseKeyguard$2 actionClickLogger$logWaitingToCloseKeyguard$2 = ActionClickLogger$logWaitingToCloseKeyguard$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("ActionClickLogger", logLevel, actionClickLogger$logWaitingToCloseKeyguard$2);
            obtain.setStr1(pendingIntent.getIntent().toString());
            logBuffer.push(obtain);
        }
    }

    public final void logKeyguardGone(PendingIntent pendingIntent) {
        Intrinsics.checkNotNullParameter(pendingIntent, "pendingIntent");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        ActionClickLogger$logKeyguardGone$2 actionClickLogger$logKeyguardGone$2 = ActionClickLogger$logKeyguardGone$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("ActionClickLogger", logLevel, actionClickLogger$logKeyguardGone$2);
            obtain.setStr1(pendingIntent.getIntent().toString());
            logBuffer.push(obtain);
        }
    }
}
