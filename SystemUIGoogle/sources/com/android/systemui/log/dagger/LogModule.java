package com.android.systemui.log.dagger;

import android.content.ContentResolver;
import android.os.Build;
import android.os.Looper;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import com.android.systemui.log.LogcatEchoTracker;
import com.android.systemui.log.LogcatEchoTrackerDebug;
import com.android.systemui.log.LogcatEchoTrackerProd;
/* loaded from: classes.dex */
public class LogModule {
    public static LogBuffer provideDozeLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("DozeLog", 100);
    }

    public static LogBuffer provideNotificationsLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("NotifLog", 1000);
    }

    public static LogBuffer provideNotificationSectionLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("NotifSectionLog", 1000);
    }

    public static LogBuffer provideNotifInteractionLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("NotifInteractionLog", 50);
    }

    public static LogBuffer provideQuickSettingsLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("QSLog", 500);
    }

    public static LogBuffer provideBroadcastDispatcherLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("BroadcastDispatcherLog", 500);
    }

    public static LogBuffer provideToastLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("ToastLog", 50);
    }

    public static LogBuffer providePrivacyLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("PrivacyLog", 100);
    }

    public static LogcatEchoTracker provideLogcatEchoTracker(ContentResolver contentResolver, Looper looper) {
        if (Build.IS_DEBUGGABLE) {
            return LogcatEchoTrackerDebug.create(contentResolver, looper);
        }
        return new LogcatEchoTrackerProd();
    }
}
