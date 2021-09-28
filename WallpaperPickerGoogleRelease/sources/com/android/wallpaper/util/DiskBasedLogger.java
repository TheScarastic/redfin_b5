package com.android.wallpaper.util;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import com.android.wallpaper.compat.BuildCompat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public class DiskBasedLogger {
    public static Handler sHandler;
    public static HandlerThread sLoggerThread;
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE MMM dd HH:mm:ss.SSS z yyyy", Locale.US);
    public static final Object S_LOCK = new Object();
    public static final long THREAD_TIMEOUT_MILLIS = TimeUnit.MILLISECONDS.convert(2, TimeUnit.MINUTES);
    public static final Runnable THREAD_CLEANUP_RUNNABLE = new Runnable() { // from class: com.android.wallpaper.util.DiskBasedLogger.1
        @Override // java.lang.Runnable
        public void run() {
            boolean z;
            HandlerThread handlerThread = DiskBasedLogger.sLoggerThread;
            if (handlerThread != null && handlerThread.isAlive()) {
                if (BuildCompat.sSdk >= 18) {
                    z = DiskBasedLogger.sLoggerThread.quitSafely();
                } else {
                    z = DiskBasedLogger.sLoggerThread.quit();
                }
                if (!z) {
                    Log.e("DiskBasedLogger", "Unable to quit disk-based logger HandlerThread");
                }
                DiskBasedLogger.sLoggerThread = null;
                DiskBasedLogger.sHandler = null;
            }
        }
    };

    public static void copyLogsNewerThanDate(BufferedReader bufferedReader, OutputStream outputStream, Date date) {
        try {
            String readLine = bufferedReader.readLine();
            while (readLine != null) {
                try {
                    if (DATE_FORMAT.parse(readLine.split("/")[0]).after(date)) {
                        outputStream.write(readLine.getBytes(StandardCharsets.UTF_8));
                        outputStream.write("\n".getBytes(StandardCharsets.UTF_8));
                    }
                    readLine = bufferedReader.readLine();
                } catch (ParseException e) {
                    Log.e("DiskBasedLogger", "Error parsing date from previous logs", e);
                    return;
                }
            }
        } catch (IOException e2) {
            Log.e("DiskBasedLogger", "IO exception while reading line from buffered reader", e2);
        }
    }

    public static void e(String str, String str2, Context context) {
        Log.e(str, str2);
        String str3 = Build.TYPE;
        if (str3.equals("eng") || str3.equals("userdebug")) {
            Handler loggerThreadHandler = getLoggerThreadHandler();
            if (loggerThreadHandler == null) {
                Log.e("DiskBasedLogger", "Something went wrong creating the logger thread handler, quitting this logging operation");
            } else {
                loggerThreadHandler.post(new PreviewUtils$$ExternalSyntheticLambda1(context, str, str2));
            }
        }
    }

    public static Handler getHandler() {
        return sHandler;
    }

    public static Handler getLoggerThreadHandler() {
        Handler handler;
        synchronized (S_LOCK) {
            if (sLoggerThread == null) {
                HandlerThread handlerThread = new HandlerThread("DiskBasedLoggerThread", 10);
                sLoggerThread = handlerThread;
                handlerThread.start();
                sHandler = new Handler(sLoggerThread.getLooper());
            } else {
                sHandler.removeCallbacks(THREAD_CLEANUP_RUNNABLE);
            }
            sHandler.postDelayed(THREAD_CLEANUP_RUNNABLE, THREAD_TIMEOUT_MILLIS);
            handler = sHandler;
        }
        return handler;
    }
}
