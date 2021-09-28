package com.google.android.apps.wallpaper.backdrop;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.util.Log;
import com.android.wallpaper.model.WallpaperMetadata;
import com.android.wallpaper.module.DefaultWallpaperRefresher;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.WallpaperRefresher;
import com.google.android.apps.wallpaper.backdrop.BackdropWallpaperRotator;
import java.util.Calendar;
import java.util.Objects;
/* loaded from: classes.dex */
public class BackdropRotationJobService extends JobService {
    public static final /* synthetic */ int $r8$clinit = 0;

    @Override // android.app.job.JobService
    public boolean onStartJob(final JobParameters jobParameters) {
        final Context applicationContext = getApplicationContext();
        ((DefaultWallpaperRefresher) InjectorProvider.getInjector().getWallpaperRefresher(applicationContext)).refresh(new WallpaperRefresher.RefreshListener() { // from class: com.google.android.apps.wallpaper.backdrop.BackdropRotationJobService.1
            @Override // com.android.wallpaper.module.WallpaperRefresher.RefreshListener
            public void onRefreshed(WallpaperMetadata wallpaperMetadata, WallpaperMetadata wallpaperMetadata2, int i) {
                if (i == 1) {
                    Log.e("BackdropRotationJob", "Wallpaper presentation mode is static, cutting task short.");
                    BackdropRotationJobService.this.jobFinished(jobParameters, false);
                    return;
                }
                BackdropRotationJobService backdropRotationJobService = BackdropRotationJobService.this;
                Context context = applicationContext;
                JobParameters jobParameters2 = jobParameters;
                int i2 = BackdropRotationJobService.$r8$clinit;
                Objects.requireNonNull(backdropRotationJobService);
                BackdropPreferences instance = BackdropPreferences.getInstance(context);
                BackdropWallpaperRotator.updateWallpaper(context, instance.getCollectionId(), instance.getCollectionName(), instance.getResumeToken(), new BackdropWallpaperRotator.RotationListener(jobParameters2, context, instance) { // from class: com.google.android.apps.wallpaper.backdrop.BackdropRotationJobService.2
                    public final /* synthetic */ Context val$appContext;
                    public final /* synthetic */ BackdropPreferences val$backdropPreferences;
                    public final /* synthetic */ JobParameters val$params;

                    {
                        this.val$params = r2;
                        this.val$appContext = r3;
                        this.val$backdropPreferences = r4;
                    }

                    @Override // com.google.android.apps.wallpaper.backdrop.BackdropWallpaperRotator.RotationListener
                    public void onError() {
                        Log.e("BackdropRotationJob", "Updating the Backdrop wallpaper failed, rescheduling for later.");
                        InjectorProvider.getInjector().getPreferences(this.val$appContext).setDailyWallpaperRotationStatus(5, Calendar.getInstance().getTimeInMillis());
                        BackdropRotationJobService.this.jobFinished(this.val$params, true);
                    }

                    @Override // com.google.android.apps.wallpaper.backdrop.BackdropWallpaperRotator.RotationListener
                    public void onSuccess(String str) {
                        if (this.val$params.getJobId() == 1) {
                            Log.e("BackdropRotationJob", "Snoozing the Backdrop alarm since this is a periodic task.");
                            BackdropAlarmScheduler.snoozeAlarm(this.val$appContext);
                        }
                        Calendar instance2 = Calendar.getInstance();
                        InjectorProvider.getInjector().getPreferences(this.val$appContext).addDailyRotation(instance2.getTimeInMillis());
                        InjectorProvider.getInjector().getUserEventLogger(this.val$appContext).logDailyWallpaperRotationHour(instance2.get(11));
                        this.val$backdropPreferences.setResumeToken(str);
                        InjectorProvider.getInjector().getPreferences(this.val$appContext).setDailyWallpaperRotationStatus(4, Calendar.getInstance().getTimeInMillis());
                        BackdropRotationJobService.this.jobFinished(this.val$params, false);
                    }
                });
            }
        });
        return true;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
