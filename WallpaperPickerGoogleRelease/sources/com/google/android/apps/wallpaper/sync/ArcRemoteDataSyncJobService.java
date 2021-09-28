package com.google.android.apps.wallpaper.sync;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.util.DiskBasedLogger;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
@TargetApi(24)
/* loaded from: classes.dex */
public class ArcRemoteDataSyncJobService extends JobService {
    public static final long THREAD_TIMEOUT_MILLIS = TimeUnit.MILLISECONDS.convert(2, TimeUnit.MINUTES);
    public Handler mHandler;
    public final Object mLock = new Object();
    public final Runnable mThreadCleanupRunnable = new Runnable() { // from class: com.google.android.apps.wallpaper.sync.ArcRemoteDataSyncJobService.1
        @Override // java.lang.Runnable
        public void run() {
            HandlerThread handlerThread = ArcRemoteDataSyncJobService.this.mWorkerThread;
            if (handlerThread != null && handlerThread.isAlive()) {
                if (!ArcRemoteDataSyncJobService.this.mWorkerThread.quitSafely()) {
                    Log.e("ArcRemoteDataSyncJob", "Unable to quit worker thread");
                }
                ArcRemoteDataSyncJobService arcRemoteDataSyncJobService = ArcRemoteDataSyncJobService.this;
                arcRemoteDataSyncJobService.mWorkerThread = null;
                arcRemoteDataSyncJobService.mHandler = null;
            }
        }
    };
    public HandlerThread mWorkerThread;

    @Override // android.app.job.JobService
    public boolean onStartJob(JobParameters jobParameters) {
        Objects.requireNonNull(InjectorProvider.getInjector().getFormFactorChecker(getApplicationContext()));
        jobFinished(jobParameters, false);
        return false;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters jobParameters) {
        Context applicationContext = getApplicationContext();
        try {
            new File(applicationContext.getFilesDir(), "temp_syncdata").delete();
            return true;
        } catch (SecurityException e) {
            DiskBasedLogger.e("ArcRemoteDataSyncJob", e.getMessage(), applicationContext);
            return true;
        }
    }
}
