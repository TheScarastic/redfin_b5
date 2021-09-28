package com.google.android.apps.wallpaper.module;

import android.text.TextUtils;
import androidx.core.R$attr;
import com.android.wallpaper.module.WallpaperPreferences;
import com.google.android.gms.clearcut.Counters;
import com.google.wireless.android.apps.wallpaper.WallpaperLogProto$WallpaperEvent;
import com.google.wireless.android.apps.wallpaper.WallpaperPickerSnapshotProto$WallpaperPickerSnapshot;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final /* synthetic */ class ClearcutUserEventLogger$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ ClearcutUserEventLogger f$0;

    public /* synthetic */ ClearcutUserEventLogger$$ExternalSyntheticLambda0(ClearcutUserEventLogger clearcutUserEventLogger, int i) {
        this.$r8$classId = i;
        if (i == 1 || i != 2) {
        }
        this.f$0 = clearcutUserEventLogger;
    }

    @Override // java.lang.Runnable
    public final void run() {
        List<Long> dailyRotationsPreviousDay;
        String str;
        switch (this.$r8$classId) {
            case 0:
                ClearcutUserEventLogger clearcutUserEventLogger = this.f$0;
                boolean z = clearcutUserEventLogger.mPreferences.getWallpaperPresentationMode() == 2;
                List<Long> dailyRotationsInLastWeek = clearcutUserEventLogger.mPreferences.getDailyRotationsInLastWeek();
                if (z && dailyRotationsInLastWeek != null) {
                    clearcutUserEventLogger.mCounters.getIntegerHistogram("DailyWallpaper.NumRotationsInLastWeek").increment(dailyRotationsInLastWeek.size());
                    Counters counters = clearcutUserEventLogger.mCounters;
                    counters.logAllAsync(counters.zzf);
                    return;
                }
                return;
            case 1:
                ClearcutUserEventLogger clearcutUserEventLogger2 = this.f$0;
                clearcutUserEventLogger2.mCounters.getCounter("DailyWallpaper.RotationEnabled").incrementBase(0, 1);
                Counters counters2 = clearcutUserEventLogger2.mCounters;
                counters2.logAllAsync(counters2.zzf);
                return;
            case 2:
                ClearcutUserEventLogger clearcutUserEventLogger3 = this.f$0;
                clearcutUserEventLogger3.mCounters.getIntegerHistogram("WallpaperPresentationMode").increment(clearcutUserEventLogger3.mPreferences.getWallpaperPresentationMode());
                Counters counters3 = clearcutUserEventLogger3.mCounters;
                counters3.logAllAsync(counters3.zzf);
                return;
            case 3:
                ClearcutUserEventLogger clearcutUserEventLogger4 = this.f$0;
                Objects.requireNonNull(clearcutUserEventLogger4);
                WallpaperLogProto$WallpaperEvent.Builder newBuilder = WallpaperLogProto$WallpaperEvent.newBuilder();
                WallpaperLogProto$WallpaperEvent.Type type = WallpaperLogProto$WallpaperEvent.Type.SNAPSHOT;
                newBuilder.copyOnWrite();
                WallpaperLogProto$WallpaperEvent.access$100((WallpaperLogProto$WallpaperEvent) newBuilder.instance, type);
                WallpaperPreferences wallpaperPreferences = clearcutUserEventLogger4.mPreferences;
                boolean isLockWallpaperSet = R$attr.isLockWallpaperSet(clearcutUserEventLogger4.mContext);
                WallpaperPickerSnapshotProto$WallpaperPickerSnapshot.Builder newBuilder2 = WallpaperPickerSnapshotProto$WallpaperPickerSnapshot.newBuilder();
                int appLaunchCount = wallpaperPreferences.getAppLaunchCount();
                newBuilder2.copyOnWrite();
                WallpaperPickerSnapshotProto$WallpaperPickerSnapshot.access$500((WallpaperPickerSnapshotProto$WallpaperPickerSnapshot) newBuilder2.instance, appLaunchCount);
                int firstLaunchDateSinceSetup = wallpaperPreferences.getFirstLaunchDateSinceSetup();
                if (firstLaunchDateSinceSetup != 0) {
                    newBuilder2.copyOnWrite();
                    WallpaperPickerSnapshotProto$WallpaperPickerSnapshot.access$100((WallpaperPickerSnapshotProto$WallpaperPickerSnapshot) newBuilder2.instance, firstLaunchDateSinceSetup);
                }
                int firstWallpaperApplyDateSinceSetup = wallpaperPreferences.getFirstWallpaperApplyDateSinceSetup();
                if (firstWallpaperApplyDateSinceSetup != 0) {
                    newBuilder2.copyOnWrite();
                    WallpaperPickerSnapshotProto$WallpaperPickerSnapshot.access$300((WallpaperPickerSnapshotProto$WallpaperPickerSnapshot) newBuilder2.instance, firstWallpaperApplyDateSinceSetup);
                }
                String homeWallpaperCollectionId = wallpaperPreferences.getHomeWallpaperCollectionId();
                if (homeWallpaperCollectionId != null) {
                    newBuilder2.copyOnWrite();
                    WallpaperPickerSnapshotProto$WallpaperPickerSnapshot.access$700((WallpaperPickerSnapshotProto$WallpaperPickerSnapshot) newBuilder2.instance, homeWallpaperCollectionId);
                }
                if (TextUtils.isEmpty(wallpaperPreferences.getHomeWallpaperRemoteId())) {
                    str = wallpaperPreferences.getHomeWallpaperServiceName();
                } else {
                    str = wallpaperPreferences.getHomeWallpaperRemoteId();
                }
                if (str != null) {
                    newBuilder2.copyOnWrite();
                    WallpaperPickerSnapshotProto$WallpaperPickerSnapshot.access$1000((WallpaperPickerSnapshotProto$WallpaperPickerSnapshot) newBuilder2.instance, str);
                }
                if (isLockWallpaperSet) {
                    homeWallpaperCollectionId = wallpaperPreferences.getLockWallpaperCollectionId();
                }
                if (homeWallpaperCollectionId != null) {
                    newBuilder2.copyOnWrite();
                    WallpaperPickerSnapshotProto$WallpaperPickerSnapshot.access$1300((WallpaperPickerSnapshotProto$WallpaperPickerSnapshot) newBuilder2.instance, homeWallpaperCollectionId);
                }
                if (isLockWallpaperSet) {
                    str = wallpaperPreferences.getLockWallpaperRemoteId();
                }
                if (str != null) {
                    newBuilder2.copyOnWrite();
                    WallpaperPickerSnapshotProto$WallpaperPickerSnapshot.access$1600((WallpaperPickerSnapshotProto$WallpaperPickerSnapshot) newBuilder2.instance, str);
                }
                newBuilder.copyOnWrite();
                WallpaperLogProto$WallpaperEvent.access$2600((WallpaperLogProto$WallpaperEvent) newBuilder.instance, newBuilder2.build());
                clearcutUserEventLogger4.log(newBuilder.build());
                return;
            default:
                ClearcutUserEventLogger clearcutUserEventLogger5 = this.f$0;
                if (clearcutUserEventLogger5.mPreferences.getWallpaperPresentationMode() == 2 && (dailyRotationsPreviousDay = clearcutUserEventLogger5.mPreferences.getDailyRotationsPreviousDay()) != null) {
                    clearcutUserEventLogger5.mCounters.getIntegerHistogram("DailyWallpaper.NumRotationsPreviousDay").increment(dailyRotationsPreviousDay.size());
                    Counters counters4 = clearcutUserEventLogger5.mCounters;
                    counters4.logAllAsync(counters4.zzf);
                    return;
                }
                return;
        }
    }
}
