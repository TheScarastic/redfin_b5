package com.google.android.apps.wallpaper.module;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import androidx.lifecycle.MethodCallsLogger;
import com.android.wallpaper.module.DailyLoggingAlarmScheduler;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.WallpaperPreferences;
import com.android.wallpaper.util.DiskBasedLogger;
import com.google.android.apps.wallpaper.backdrop.BackdropAlarmScheduler;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
@SuppressLint({"ServiceCast"})
/* loaded from: classes.dex */
public class GoogleAlarmInitializer extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Context applicationContext = context.getApplicationContext();
        InjectorProvider.getInjector().getWallpaperManagerCompat(applicationContext);
        if (!((WallpaperManager) context.getSystemService("wallpaper")).isWallpaperSupported()) {
            Log.e("GoogleAlarmInitializer", "Wallpapers are not supported in this context, not attempting to schedule a wallpaper rotation alarm");
            return;
        }
        String action = intent.getAction();
        if (action.equals("android.intent.action.BOOT_COMPLETED")) {
            DailyLoggingAlarmScheduler.setAlarm(applicationContext);
        }
        if ((action.equals("android.intent.action.BOOT_COMPLETED") || action.equals("android.intent.action.MY_PACKAGE_REPLACED")) && InjectorProvider.getInjector().getPreferences(applicationContext).getWallpaperPresentationMode() == 2) {
            DiskBasedLogger.e("GoogleAlarmInitializer", "Wallpaper rotation is in effect, setting an alarm for Backdrop rotation", applicationContext);
            WallpaperPreferences preferences = InjectorProvider.getInjector().getPreferences(applicationContext);
            long lastDailyRotationTimestamp = preferences.getLastDailyRotationTimestamp();
            Calendar instance = Calendar.getInstance();
            instance.set(10, 0);
            instance.set(12, 0);
            instance.set(9, 0);
            long timeInMillis = instance.getTimeInMillis();
            long dailyWallpaperEnabledTimestamp = preferences.getDailyWallpaperEnabledTimestamp();
            if (lastDailyRotationTimestamp >= timeInMillis || dailyWallpaperEnabledTimestamp >= timeInMillis) {
                Log.e("GoogleAlarmInitializer", "Set overnight alarm.");
                BackdropAlarmScheduler.setOvernightAlarm(applicationContext);
                return;
            }
            DiskBasedLogger.e("GoogleAlarmInitializer", "Set immediate alarm to update wallpaper since wallpaper should have been updated overnight but didn't.", applicationContext);
            MethodCallsLogger alarmManagerWrapper = InjectorProvider.getInjector().getAlarmManagerWrapper(applicationContext);
            PendingIntent createBackdropAlarmReceiverPendingIntent = BackdropAlarmScheduler.createBackdropAlarmReceiverPendingIntent(applicationContext);
            long convert = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS);
            ((AlarmManager) alarmManagerWrapper.mCalledMethods).setWindow(2, SystemClock.elapsedRealtime(), convert, createBackdropAlarmReceiverPendingIntent);
        }
    }
}
