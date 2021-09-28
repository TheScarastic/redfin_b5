package com.google.android.apps.wallpaper.module;

import com.google.android.gms.clearcut.Counters;
import com.google.wireless.android.apps.wallpaper.WallpaperLogProto$WallpaperEvent;
import java.util.Objects;
/* loaded from: classes.dex */
public final /* synthetic */ class ClearcutUserEventLogger$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ ClearcutUserEventLogger f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ ClearcutUserEventLogger$$ExternalSyntheticLambda1(ClearcutUserEventLogger clearcutUserEventLogger, int i, int i2) {
        this.$r8$classId = i2;
        switch (i2) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            default:
                this.f$0 = clearcutUserEventLogger;
                this.f$1 = i;
                return;
        }
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                ClearcutUserEventLogger clearcutUserEventLogger = this.f$0;
                clearcutUserEventLogger.mCounters.getIntegerHistogram("DailyWallpaper.MetadataRequestFailureReason").incrementBase((long) this.f$1, 1);
                Counters counters = clearcutUserEventLogger.mCounters;
                counters.logAllAsync(counters.zzf);
                return;
            case 1:
                ClearcutUserEventLogger clearcutUserEventLogger2 = this.f$0;
                clearcutUserEventLogger2.mCounters.getIntegerHistogram("DailyWallpaper.NumDaysInFailedState").incrementBase((long) this.f$1, 1);
                Counters counters2 = clearcutUserEventLogger2.mCounters;
                counters2.logAllAsync(counters2.zzf);
                return;
            case 2:
                ClearcutUserEventLogger clearcutUserEventLogger3 = this.f$0;
                int i = this.f$1;
                clearcutUserEventLogger3.mCounters.getIntegerHistogram("DailyWallpaper.RotationStatus").incrementBase((long) i, 1);
                Counters.IntegerHistogram integerHistogram = clearcutUserEventLogger3.mCounters.getIntegerHistogram("DailyWallpaper.DailyRotationResult");
                if (2 == i || 3 == i || 4 == i) {
                    integerHistogram.increment(0);
                }
                if (i == 0 || 5 == i) {
                    integerHistogram.increment(1);
                }
                Counters counters3 = clearcutUserEventLogger3.mCounters;
                counters3.logAllAsync(counters3.zzf);
                return;
            case 3:
                ClearcutUserEventLogger clearcutUserEventLogger4 = this.f$0;
                int i2 = this.f$1;
                Objects.requireNonNull(clearcutUserEventLogger4);
                WallpaperLogProto$WallpaperEvent.Builder newBuilder = WallpaperLogProto$WallpaperEvent.newBuilder();
                newBuilder.setType(WallpaperLogProto$WallpaperEvent.Type.forNumber(i2));
                clearcutUserEventLogger4.log(newBuilder.build());
                return;
            case 4:
                ClearcutUserEventLogger clearcutUserEventLogger5 = this.f$0;
                clearcutUserEventLogger5.mCounters.getIntegerHistogram("IndividualWallpaperSetFailureReason").incrementBase((long) this.f$1, 1);
                Counters counters4 = clearcutUserEventLogger5.mCounters;
                counters4.logAllAsync(counters4.zzf);
                return;
            case 5:
                ClearcutUserEventLogger clearcutUserEventLogger6 = this.f$0;
                clearcutUserEventLogger6.mCounters.getIntegerHistogram("DailyWallpaper.SetNextWallpaperCrash").incrementBase((long) this.f$1, 1);
                Counters counters5 = clearcutUserEventLogger6.mCounters;
                counters5.logAllAsync(counters5.zzf);
                return;
            case 6:
                ClearcutUserEventLogger clearcutUserEventLogger7 = this.f$0;
                clearcutUserEventLogger7.mCounters.getIntegerHistogram("DailyWallpaper.NumDaysInNotAttemptedState").incrementBase((long) this.f$1, 1);
                Counters counters6 = clearcutUserEventLogger7.mCounters;
                counters6.logAllAsync(counters6.zzf);
                return;
            case 7:
                ClearcutUserEventLogger clearcutUserEventLogger8 = this.f$0;
                clearcutUserEventLogger8.mCounters.getIntegerHistogram("IndividualWallpaperSetResult").incrementBase((long) this.f$1, 1);
                Counters counters7 = clearcutUserEventLogger8.mCounters;
                counters7.logAllAsync(counters7.zzf);
                return;
            case 8:
                ClearcutUserEventLogger clearcutUserEventLogger9 = this.f$0;
                clearcutUserEventLogger9.mCounters.getIntegerHistogram("DailyWallpaper.RotationHourOfDay").incrementBase((long) this.f$1, 1);
                Counters counters8 = clearcutUserEventLogger9.mCounters;
                counters8.logAllAsync(counters8.zzf);
                return;
            default:
                ClearcutUserEventLogger clearcutUserEventLogger10 = this.f$0;
                clearcutUserEventLogger10.mCounters.getIntegerHistogram("DailyWallpaper.SetNextWallpaperResult").incrementBase((long) this.f$1, 1);
                Counters counters9 = clearcutUserEventLogger10.mCounters;
                counters9.logAllAsync(counters9.zzf);
                return;
        }
    }
}
