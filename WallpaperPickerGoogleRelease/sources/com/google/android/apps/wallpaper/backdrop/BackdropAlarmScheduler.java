package com.google.android.apps.wallpaper.backdrop;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import androidx.lifecycle.MethodCallsLogger;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.util.DiskBasedLogger;
import java.util.Calendar;
import java.util.Date;
/* loaded from: classes.dex */
public class BackdropAlarmScheduler {
    public static PendingIntent createBackdropAlarmReceiverPendingIntent(Context context) {
        return PendingIntent.getBroadcast(context, 0, new Intent(context, BackdropAlarmReceiver.class), 67108864);
    }

    public static void setOvernightAlarm(Context context) {
        MethodCallsLogger alarmManagerWrapper = InjectorProvider.getInjector().getAlarmManagerWrapper(context);
        PendingIntent createBackdropAlarmReceiverPendingIntent = createBackdropAlarmReceiverPendingIntent(context);
        Date date = new Date();
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(5, 1);
        instance.set(9, 0);
        instance.set(10, 2);
        instance.set(12, 0);
        long time = instance.getTime().getTime() - date.getTime();
        Calendar instance2 = Calendar.getInstance();
        instance2.setTime(date);
        instance2.add(5, 1);
        instance2.set(9, 0);
        instance2.set(10, 3);
        instance2.set(12, 30);
        long random = time + ((long) (Math.random() * ((double) ((instance2.getTime().getTime() - date.getTime()) - time))));
        Date date2 = new Date();
        Calendar instance3 = Calendar.getInstance();
        instance3.setTime(date2);
        instance3.add(5, 1);
        instance3.set(9, 0);
        instance3.set(10, 5);
        instance3.set(12, 0);
        DiskBasedLogger.e("BackdropAlarmScheduler", "Setting a new alarm to run " + random + "ms from now", context);
        long elapsedRealtime = SystemClock.elapsedRealtime() + random;
        ((AlarmManager) alarmManagerWrapper.mCalledMethods).setWindow(2, elapsedRealtime, (instance3.getTime().getTime() - date2.getTime()) - random, createBackdropAlarmReceiverPendingIntent);
    }

    public static void snoozeAlarm(Context context) {
        MethodCallsLogger alarmManagerWrapper = InjectorProvider.getInjector().getAlarmManagerWrapper(context);
        ((AlarmManager) alarmManagerWrapper.mCalledMethods).cancel(createBackdropAlarmReceiverPendingIntent(context));
        setOvernightAlarm(context);
    }
}
