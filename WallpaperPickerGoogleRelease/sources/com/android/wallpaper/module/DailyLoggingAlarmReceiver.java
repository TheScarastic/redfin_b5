package com.android.wallpaper.module;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import com.android.wallpaper.model.WallpaperMetadata;
import com.android.wallpaper.module.WallpaperRefresher;
import com.android.wallpaper.util.DiskBasedLogger;
import com.android.wallpaper.util.DiskBasedLogger$$ExternalSyntheticLambda1;
import java.text.SimpleDateFormat;
import java.util.Calendar;
/* loaded from: classes.dex */
public class DailyLoggingAlarmReceiver extends BroadcastReceiver {
    public static void access$000(PowerManager.WakeLock wakeLock) {
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        final Context applicationContext = context.getApplicationContext();
        Injector injector = InjectorProvider.getInjector();
        UserEventLogger userEventLogger = injector.getUserEventLogger(applicationContext);
        WallpaperPreferences preferences = injector.getPreferences(applicationContext);
        userEventLogger.logNumDailyWallpaperRotationsInLastWeek();
        userEventLogger.logNumDailyWallpaperRotationsPreviousDay();
        userEventLogger.logWallpaperPresentationMode();
        userEventLogger.logSnapshot();
        preferences.setLastDailyLogTimestamp(System.currentTimeMillis());
        final PowerManager.WakeLock newWakeLock = ((PowerManager) applicationContext.getSystemService("power")).newWakeLock(1, "DailyLoggingAlarm");
        newWakeLock.acquire(10000);
        final Injector injector2 = InjectorProvider.getInjector();
        ((DefaultWallpaperRefresher) injector2.getWallpaperRefresher(applicationContext)).refresh(new WallpaperRefresher.RefreshListener(this) { // from class: com.android.wallpaper.module.DailyLoggingAlarmReceiver.1
            @Override // com.android.wallpaper.module.WallpaperRefresher.RefreshListener
            public void onRefreshed(WallpaperMetadata wallpaperMetadata, WallpaperMetadata wallpaperMetadata2, int i) {
                if (i == 2) {
                    WallpaperPreferences preferences2 = injector2.getPreferences(applicationContext);
                    long dailyWallpaperEnabledTimestamp = preferences2.getDailyWallpaperEnabledTimestamp();
                    if (dailyWallpaperEnabledTimestamp < 0) {
                        Log.e("DailyLoggingAlarm", "There's no valid daily wallpaper enabled timestamp");
                        return;
                    }
                    Calendar instance = Calendar.getInstance();
                    instance.add(5, -1);
                    instance.set(11, 0);
                    instance.set(12, 0);
                    if (dailyWallpaperEnabledTimestamp <= instance.getTimeInMillis()) {
                        try {
                            long dailyWallpaperLastRotationStatusTimestamp = preferences2.getDailyWallpaperLastRotationStatusTimestamp();
                            UserEventLogger userEventLogger2 = injector2.getUserEventLogger(applicationContext);
                            if (dailyWallpaperLastRotationStatusTimestamp > instance.getTimeInMillis()) {
                                int dailyWallpaperLastRotationStatus = preferences2.getDailyWallpaperLastRotationStatus();
                                userEventLogger2.logDailyWallpaperRotationStatus(dailyWallpaperLastRotationStatus);
                                if (5 == dailyWallpaperLastRotationStatus) {
                                    preferences2.incrementNumDaysDailyRotationFailed();
                                    userEventLogger2.logNumDaysDailyRotationFailed(preferences2.getNumDaysDailyRotationFailed());
                                } else {
                                    preferences2.resetNumDaysDailyRotationFailed();
                                }
                                preferences2.resetNumDaysDailyRotationNotAttempted();
                            } else {
                                userEventLogger2.logDailyWallpaperRotationStatus(0);
                                preferences2.incrementNumDaysDailyRotationNotAttempted();
                                userEventLogger2.logNumDaysDailyRotationNotAttempted(preferences2.getNumDaysDailyRotationNotAttempted());
                                preferences2.resetNumDaysDailyRotationFailed();
                            }
                        } finally {
                            DailyLoggingAlarmReceiver.access$000(newWakeLock);
                        }
                    }
                }
            }
        });
        SimpleDateFormat simpleDateFormat = DiskBasedLogger.DATE_FORMAT;
        String str = Build.TYPE;
        if (str.equals("eng") || str.equals("userdebug")) {
            Handler loggerThreadHandler = DiskBasedLogger.getLoggerThreadHandler();
            if (loggerThreadHandler == null) {
                Log.e("DiskBasedLogger", "Something went wrong creating the logger thread handler, quitting this logging operation");
            } else {
                loggerThreadHandler.post(new DiskBasedLogger$$ExternalSyntheticLambda1(applicationContext));
            }
        }
    }
}
