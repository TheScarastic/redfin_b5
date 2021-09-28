package com.google.android.apps.wallpaper.picker;

import android.app.Application;
import android.content.Context;
import com.android.wallpaper.module.Injector;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.WallpapersInjector;
import java.lang.Thread;
import java.util.Objects;
/* loaded from: classes.dex */
public class WallpapersApplication extends Application {
    public static final /* synthetic */ int $r8$clinit = 0;
    public Thread.UncaughtExceptionHandler mWrappedHandler;

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        WallpapersInjector wallpapersInjector = new WallpapersInjector();
        InjectorProvider.sInjector = wallpapersInjector;
        Objects.requireNonNull(wallpapersInjector.getFormFactorChecker(this));
        Context applicationContext = getApplicationContext();
        this.mWrappedHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(wallpapersInjector, applicationContext) { // from class: com.google.android.apps.wallpaper.picker.WallpapersApplication$$ExternalSyntheticLambda0
            public final /* synthetic */ Injector f$1;
            public final /* synthetic */ Context f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            /* JADX WARN: Type inference failed for: r1v1, types: [boolean, int] */
            /* JADX WARNING: Unknown variable types count: 1 */
            @Override // java.lang.Thread.UncaughtExceptionHandler
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public final void uncaughtException(java.lang.Thread r7, java.lang.Throwable r8) {
                /*
                    r6 = this;
                    com.google.android.apps.wallpaper.picker.WallpapersApplication r0 = com.google.android.apps.wallpaper.picker.WallpapersApplication.this
                    com.android.wallpaper.module.Injector r1 = r6.f$1
                    android.content.Context r6 = r6.f$2
                    int r2 = com.google.android.apps.wallpaper.picker.WallpapersApplication.$r8$clinit
                    java.util.Objects.requireNonNull(r0)
                    com.android.wallpaper.module.UserEventLogger r2 = r1.getUserEventLogger(r6)
                    com.android.wallpaper.module.WallpaperPreferences r6 = r1.getPreferences(r6)
                    boolean r1 = androidx.cardview.R$dimen.isOOM(r8)
                    int r3 = r6.getPendingWallpaperSetStatus()
                    r4 = 0
                    r5 = 1
                    if (r3 != r5) goto L_0x0028
                    r2.logWallpaperSetResult(r5)
                    r2.logWallpaperSetFailureReason(r1)
                    r6.setPendingWallpaperSetStatusSync(r4)
                L_0x0028:
                    int r3 = r6.getPendingDailyWallpaperUpdateStatus()
                    if (r3 != r5) goto L_0x0038
                    r3 = 4
                    r2.logDailyWallpaperSetNextWallpaperResult(r3)
                    r2.logDailyWallpaperSetNextWallpaperCrash(r1)
                    r6.setPendingDailyWallpaperUpdateStatusSync(r4)
                L_0x0038:
                    java.lang.Thread$UncaughtExceptionHandler r6 = r0.mWrappedHandler
                    r6.uncaughtException(r7, r8)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.google.android.apps.wallpaper.picker.WallpapersApplication$$ExternalSyntheticLambda0.uncaughtException(java.lang.Thread, java.lang.Throwable):void");
            }
        });
    }
}
