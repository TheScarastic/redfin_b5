package com.google.android.apps.wallpaper.sync;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.ParcelFileDescriptor;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.util.DiskBasedLogger;
import com.android.wallpaper.util.PreviewUtils$$ExternalSyntheticLambda1;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
@TargetApi(24)
/* loaded from: classes.dex */
public class ArcSyncDataProcessorJobService extends JobService {
    public static final long THREAD_TIMEOUT_MILLIS = TimeUnit.MILLISECONDS.convert(2, TimeUnit.MINUTES);
    public static final Queue<String> sContentUriQueue = new LinkedList();
    public Handler mHandler;
    public final Object mLock = new Object();
    public final Runnable mThreadCleanupRunnable = new Runnable() { // from class: com.google.android.apps.wallpaper.sync.ArcSyncDataProcessorJobService.1
        @Override // java.lang.Runnable
        public void run() {
            HandlerThread handlerThread = ArcSyncDataProcessorJobService.this.mWorkerThread;
            if (handlerThread != null && handlerThread.isAlive()) {
                if (!ArcSyncDataProcessorJobService.this.mWorkerThread.quitSafely()) {
                    DiskBasedLogger.e("ArcSyncDataProcessorJob", "Unable to quit worker thread", ArcSyncDataProcessorJobService.this.getApplicationContext());
                }
                ArcSyncDataProcessorJobService arcSyncDataProcessorJobService = ArcSyncDataProcessorJobService.this;
                arcSyncDataProcessorJobService.mWorkerThread = null;
                arcSyncDataProcessorJobService.mHandler = null;
            }
        }
    };
    public HandlerThread mWorkerThread;

    public final void doneProcessingCurrentSyncData(JobParameters jobParameters, ParcelFileDescriptor parcelFileDescriptor) {
        Context applicationContext = getApplicationContext();
        if (parcelFileDescriptor != null) {
            try {
                parcelFileDescriptor.close();
            } catch (IOException e) {
                DiskBasedLogger.e("ArcSyncDataProcessorJob", "Could not close parcel fd: " + e, applicationContext);
            }
        }
        Intent intent = new Intent("org.chromium.arc.intent_helper.ACTION_SYNC_SYSTEM_APP_DATA_COMPLETED");
        Queue<String> queue = sContentUriQueue;
        intent.setData(Uri.parse((String) ((LinkedList) queue).peek()));
        intent.setPackage("org.chromium.arc.intent_helper");
        sendBroadcast(intent, "org.chromium.arc.intent_helper.permission.SYNC_SYSTEM_APP_DATA");
        ((LinkedList) queue).remove();
        if (queue.isEmpty()) {
            jobFinished(jobParameters, false);
        } else {
            processCurrentSyncData(jobParameters);
        }
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(JobParameters jobParameters) {
        if (sContentUriQueue.isEmpty()) {
            jobFinished(jobParameters, false);
            return false;
        }
        processCurrentSyncData(jobParameters);
        return true;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    public final void processCurrentSyncData(JobParameters jobParameters) {
        Context applicationContext = getApplicationContext();
        synchronized (this.mLock) {
            if (this.mWorkerThread == null) {
                HandlerThread handlerThread = new HandlerThread("ArcSyncDataProcessorJobServiceThread", 0);
                this.mWorkerThread = handlerThread;
                handlerThread.start();
                this.mHandler = new Handler(this.mWorkerThread.getLooper());
            } else {
                this.mHandler.removeCallbacks(this.mThreadCleanupRunnable);
            }
        }
        this.mHandler.postDelayed(this.mThreadCleanupRunnable, THREAD_TIMEOUT_MILLIS);
        Handler handler = this.mHandler;
        this.mHandler = handler;
        if (handler == null) {
            DiskBasedLogger.e("ArcSyncDataProcessorJob", "Error fetching worker thread; won't sync.", applicationContext);
            jobFinished(jobParameters, false);
            return;
        }
        handler.post(new PreviewUtils$$ExternalSyntheticLambda1(this, applicationContext, jobParameters));
    }

    public final boolean setStreamAsWallpaper(InputStream inputStream) {
        Context applicationContext = getApplicationContext();
        try {
            InjectorProvider.getInjector().getWallpaperManagerCompat(applicationContext).setStream(inputStream, null, true, 1);
            inputStream.close();
            return true;
        } catch (IOException e) {
            DiskBasedLogger.e("ArcSyncDataProcessorJob", "Couldn't set wallpaper: " + e, applicationContext);
            return false;
        }
    }
}
