package com.android.wallpaper.backup;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
@SuppressLint({"ServiceCast"})
/* loaded from: classes.dex */
public class MissingHashCodeGeneratorJobService extends JobService {
    public static final /* synthetic */ int $r8$clinit = 0;
    public Thread mWorkerThread;

    public Thread getWorkerThread() {
        return this.mWorkerThread;
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(final JobParameters jobParameters) {
        final Context applicationContext = getApplicationContext();
        final WallpaperManager wallpaperManager = (WallpaperManager) applicationContext.getSystemService("wallpaper");
        Thread thread = new Thread(new Runnable() { // from class: com.android.wallpaper.backup.MissingHashCodeGeneratorJobService.1
            /* JADX DEBUG: Failed to insert an additional move for type inference into block B:29:0x008f */
            /* JADX DEBUG: Failed to insert an additional move for type inference into block B:49:0x0066 */
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r4v0 */
            /* JADX WARN: Type inference failed for: r4v1 */
            /* JADX WARN: Type inference failed for: r4v2 */
            /* JADX WARN: Type inference failed for: r4v7 */
            /* JADX WARNING: Code restructure failed: missing block: B:36:0x009e, code lost:
                if (r4 == null) goto L_0x00a4;
             */
            @Override // java.lang.Runnable
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r11 = this;
                    java.lang.String r0 = "IO exception when closing input stream for lock screen wallpaper."
                    com.android.wallpaper.module.Injector r1 = com.android.wallpaper.module.InjectorProvider.getInjector()
                    android.content.Context r2 = r0
                    com.android.wallpaper.compat.WallpaperManagerCompatV16 r2 = r1.getWallpaperManagerCompat(r2)
                    android.content.Context r3 = r0
                    com.android.wallpaper.module.WallpaperPreferences r1 = r1.getPreferences(r3)
                    android.app.WallpaperManager r3 = r1
                    android.app.WallpaperInfo r3 = r3.getWallpaperInfo()
                    r4 = 1
                    r5 = 0
                    if (r3 == 0) goto L_0x001e
                    r3 = r4
                    goto L_0x001f
                L_0x001e:
                    r3 = r5
                L_0x001f:
                    r6 = 0
                    java.lang.String r8 = "MissingHashCodeGenerato"
                    if (r3 != 0) goto L_0x0054
                    long r9 = r1.getHomeWallpaperHashCode()
                    int r3 = (r9 > r6 ? 1 : (r9 == r6 ? 0 : -1))
                    if (r3 != 0) goto L_0x0054
                    android.app.WallpaperManager r3 = r1
                    r3.forgetLoadedWallpaper()
                    android.graphics.drawable.Drawable r3 = r2.getDrawable()
                    if (r3 != 0) goto L_0x0047
                    android.content.Context r0 = r0
                    java.lang.String r1 = "WallpaperManager#getDrawable returned null and there's no live wallpaper set"
                    com.android.wallpaper.util.DiskBasedLogger.e(r8, r1, r0)
                    com.android.wallpaper.backup.MissingHashCodeGeneratorJobService r0 = com.android.wallpaper.backup.MissingHashCodeGeneratorJobService.this
                    android.app.job.JobParameters r11 = r5
                    r0.jobFinished(r11, r5)
                    return
                L_0x0047:
                    android.graphics.drawable.BitmapDrawable r3 = (android.graphics.drawable.BitmapDrawable) r3
                    android.graphics.Bitmap r3 = r3.getBitmap()
                    long r9 = androidx.slice.view.R$id.generateHashCode(r3)
                    r1.setHomeWallpaperHashCode(r9)
                L_0x0054:
                    long r9 = r1.getLockWallpaperHashCode()
                    int r3 = (r9 > r6 ? 1 : (r9 == r6 ? 0 : -1))
                    if (r3 != 0) goto L_0x00c4
                    r3 = 2
                    android.os.ParcelFileDescriptor r2 = r2.getWallpaperFile(r3)
                    if (r2 == 0) goto L_0x0064
                    goto L_0x0065
                L_0x0064:
                    r4 = r5
                L_0x0065:
                    r3 = 0
                    if (r4 != 0) goto L_0x0079
                    long r6 = r1.getHomeWallpaperHashCode()
                    r1.setLockWallpaperHashCode(r6)
                    com.android.wallpaper.backup.MissingHashCodeGeneratorJobService r0 = com.android.wallpaper.backup.MissingHashCodeGeneratorJobService.this
                    r0.mWorkerThread = r3
                    android.app.job.JobParameters r11 = r5
                    r0.jobFinished(r11, r5)
                    return
                L_0x0079:
                    java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch: IOException -> 0x0096, all -> 0x0094
                    java.io.FileDescriptor r6 = r2.getFileDescriptor()     // Catch: IOException -> 0x0096, all -> 0x0094
                    r4.<init>(r6)     // Catch: IOException -> 0x0096, all -> 0x0094
                    android.graphics.Bitmap r6 = android.graphics.BitmapFactory.decodeStream(r4)     // Catch: IOException -> 0x0091, all -> 0x00b7
                    r2.close()     // Catch: IOException -> 0x008f, all -> 0x00b7
                L_0x0089:
                    r4.close()     // Catch: IOException -> 0x008d
                    goto L_0x00a4
                L_0x008d:
                    r2 = move-exception
                    goto L_0x00a1
                L_0x008f:
                    r2 = move-exception
                    goto L_0x0099
                L_0x0091:
                    r2 = move-exception
                    r6 = r3
                    goto L_0x0099
                L_0x0094:
                    r11 = move-exception
                    goto L_0x00b9
                L_0x0096:
                    r2 = move-exception
                    r4 = r3
                    r6 = r4
                L_0x0099:
                    java.lang.String r7 = "IO exception when closing the file descriptor."
                    android.util.Log.e(r8, r7, r2)     // Catch: all -> 0x00b7
                    if (r4 == 0) goto L_0x00a4
                    goto L_0x0089
                L_0x00a1:
                    android.util.Log.e(r8, r0, r2)
                L_0x00a4:
                    if (r6 == 0) goto L_0x00ad
                    long r6 = androidx.slice.view.R$id.generateHashCode(r6)
                    r1.setLockWallpaperHashCode(r6)
                L_0x00ad:
                    com.android.wallpaper.backup.MissingHashCodeGeneratorJobService r0 = com.android.wallpaper.backup.MissingHashCodeGeneratorJobService.this
                    r0.mWorkerThread = r3
                    android.app.job.JobParameters r11 = r5
                    r0.jobFinished(r11, r5)
                    goto L_0x00c4
                L_0x00b7:
                    r11 = move-exception
                    r3 = r4
                L_0x00b9:
                    if (r3 == 0) goto L_0x00c3
                    r3.close()     // Catch: IOException -> 0x00bf
                    goto L_0x00c3
                L_0x00bf:
                    r1 = move-exception
                    android.util.Log.e(r8, r0, r1)
                L_0x00c3:
                    throw r11
                L_0x00c4:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.wallpaper.backup.MissingHashCodeGeneratorJobService.AnonymousClass1.run():void");
            }
        });
        this.mWorkerThread = thread;
        thread.start();
        return true;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
