package com.android.wallpaper.module;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.lifecycle.MethodCallsLogger;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public class DailyLoggingAlarmScheduler {
    public static void setAlarm(Context context) {
        long j;
        Injector injector = InjectorProvider.getInjector();
        MethodCallsLogger alarmManagerWrapper = injector.getAlarmManagerWrapper(context);
        long lastDailyLogTimestamp = injector.getPreferences(context).getLastDailyLogTimestamp();
        Calendar instance = Calendar.getInstance();
        long currentTimeMillis = System.currentTimeMillis();
        instance.setTimeInMillis(currentTimeMillis);
        instance.add(6, -1);
        if (lastDailyLogTimestamp == -1 || lastDailyLogTimestamp < instance.getTimeInMillis()) {
            Calendar instance2 = Calendar.getInstance();
            instance2.setTimeInMillis(currentTimeMillis);
            instance2.add(12, 1);
            j = instance2.getTimeInMillis();
        } else {
            Calendar instance3 = Calendar.getInstance();
            instance3.setTimeInMillis(lastDailyLogTimestamp);
            instance3.add(6, 1);
            j = instance3.getTimeInMillis();
        }
        ((AlarmManager) alarmManagerWrapper.mCalledMethods).cancel(PendingIntent.getBroadcast(context, 0, new Intent(context, DailyLoggingAlarmReceiver.class), 67108864));
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, new Intent(context, DailyLoggingAlarmReceiver.class), 67108864);
        ((AlarmManager) alarmManagerWrapper.mCalledMethods).setInexactRepeating(1, j, TimeUnit.MILLISECONDS.convert(24, TimeUnit.HOURS), broadcast);
    }
}
