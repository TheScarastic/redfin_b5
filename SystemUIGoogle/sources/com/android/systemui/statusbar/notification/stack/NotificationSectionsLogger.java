package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.LogMessageImpl;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationSectionsLogger.kt */
/* loaded from: classes.dex */
public final class NotificationSectionsLogger {
    private final LogBuffer logBuffer;

    public NotificationSectionsLogger(LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "logBuffer");
        this.logBuffer = logBuffer;
    }

    public final void logStartSectionUpdate(String str) {
        Intrinsics.checkNotNullParameter(str, "reason");
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationSectionsLogger$logStartSectionUpdate$2 notificationSectionsLogger$logStartSectionUpdate$2 = new Function1<LogMessage, String>(str) { // from class: com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger$logStartSectionUpdate$2
            final /* synthetic */ String $reason;

            /* access modifiers changed from: package-private */
            {
                this.$reason = r1;
            }

            public final String invoke(LogMessage logMessage) {
                Intrinsics.checkNotNullParameter(logMessage, "$this$log");
                return Intrinsics.stringPlus("Updating section boundaries: ", this.$reason);
            }
        };
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifSections", logLevel, notificationSectionsLogger$logStartSectionUpdate$2);
            obtain.setStr1(str);
            logBuffer.push(obtain);
        }
    }

    public final void logIncomingHeader(int i) {
        logPosition(i, "INCOMING HEADER");
    }

    public final void logMediaControls(int i) {
        logPosition(i, "MEDIA CONTROLS");
    }

    public final void logConversationsHeader(int i) {
        logPosition(i, "CONVERSATIONS HEADER");
    }

    public final void logAlertingHeader(int i) {
        logPosition(i, "ALERTING HEADER");
    }

    public final void logSilentHeader(int i) {
        logPosition(i, "SILENT HEADER");
    }

    public final void logOther(int i, Class<?> cls) {
        Intrinsics.checkNotNullParameter(cls, "clazz");
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationSectionsLogger$logOther$2 notificationSectionsLogger$logOther$2 = NotificationSectionsLogger$logOther$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifSections", logLevel, notificationSectionsLogger$logOther$2);
            obtain.setInt1(i);
            obtain.setStr1(cls.getName());
            logBuffer.push(obtain);
        }
    }

    public final void logHeadsUp(int i, boolean z) {
        logPosition(i, "Heads Up", z);
    }

    public final void logConversation(int i, boolean z) {
        logPosition(i, "Conversation", z);
    }

    public final void logAlerting(int i, boolean z) {
        logPosition(i, "Alerting", z);
    }

    public final void logSilent(int i, boolean z) {
        logPosition(i, "Silent", z);
    }

    public final void logStr(String str) {
        Intrinsics.checkNotNullParameter(str, "str");
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationSectionsLogger$logStr$2 notificationSectionsLogger$logStr$2 = NotificationSectionsLogger$logStr$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifSections", logLevel, notificationSectionsLogger$logStr$2);
            obtain.setStr1(str);
            logBuffer.push(obtain);
        }
    }

    private final void logPosition(int i, String str, boolean z) {
        String str2 = z ? " (HUN)" : "";
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationSectionsLogger$logPosition$2 notificationSectionsLogger$logPosition$2 = NotificationSectionsLogger$logPosition$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifSections", logLevel, notificationSectionsLogger$logPosition$2);
            obtain.setInt1(i);
            obtain.setStr1(str);
            obtain.setStr2(str2);
            logBuffer.push(obtain);
        }
    }

    private final void logPosition(int i, String str) {
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationSectionsLogger$logPosition$4 notificationSectionsLogger$logPosition$4 = NotificationSectionsLogger$logPosition$4.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifSections", logLevel, notificationSectionsLogger$logPosition$4);
            obtain.setInt1(i);
            obtain.setStr1(str);
            logBuffer.push(obtain);
        }
    }
}
