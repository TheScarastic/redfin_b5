package com.android.wallpaper.module;

import android.annotation.TargetApi;
import java.util.List;
/* loaded from: classes.dex */
public interface WallpaperPreferences {
    void addDailyRotation(long j);

    void clearDailyRotations();

    void clearHomeWallpaperMetadata();

    void clearLockWallpaperMetadata();

    int getAppLaunchCount();

    List<Long> getDailyRotationsInLastWeek();

    List<Long> getDailyRotationsPreviousDay();

    long getDailyWallpaperEnabledTimestamp();

    int getDailyWallpaperLastRotationStatus();

    long getDailyWallpaperLastRotationStatusTimestamp();

    int getFirstLaunchDateSinceSetup();

    int getFirstWallpaperApplyDateSinceSetup();

    int getHomeWallpaperActionIconRes();

    int getHomeWallpaperActionLabelRes();

    String getHomeWallpaperActionUrl();

    List<String> getHomeWallpaperAttributions();

    String getHomeWallpaperBackingFileName();

    String getHomeWallpaperBaseImageUrl();

    String getHomeWallpaperCollectionId();

    long getHomeWallpaperHashCode();

    @TargetApi(24)
    int getHomeWallpaperManagerId();

    String getHomeWallpaperPackageName();

    String getHomeWallpaperRemoteId();

    String getHomeWallpaperServiceName();

    long getLastDailyLogTimestamp();

    long getLastDailyRotationTimestamp();

    long getLastSyncTimestamp();

    int getLockWallpaperActionIconRes();

    int getLockWallpaperActionLabelRes();

    String getLockWallpaperActionUrl();

    List<String> getLockWallpaperAttributions();

    String getLockWallpaperBackingFileName();

    String getLockWallpaperCollectionId();

    long getLockWallpaperHashCode();

    @TargetApi(24)
    int getLockWallpaperId();

    String getLockWallpaperRemoteId();

    int getNumDaysDailyRotationFailed();

    int getNumDaysDailyRotationNotAttempted();

    int getPendingDailyWallpaperUpdateStatus();

    int getPendingWallpaperSetStatus();

    int getWallpaperPresentationMode();

    void incrementAppLaunched();

    void incrementNumDaysDailyRotationFailed();

    void incrementNumDaysDailyRotationNotAttempted();

    void resetNumDaysDailyRotationFailed();

    void resetNumDaysDailyRotationNotAttempted();

    void setDailyWallpaperEnabledTimestamp(long j);

    void setDailyWallpaperRotationStatus(int i, long j);

    void setHomeWallpaperActionIconRes(int i);

    void setHomeWallpaperActionLabelRes(int i);

    void setHomeWallpaperActionUrl(String str);

    void setHomeWallpaperAttributions(List<String> list);

    void setHomeWallpaperBackingFileName(String str);

    void setHomeWallpaperBaseImageUrl(String str);

    void setHomeWallpaperCollectionId(String str);

    void setHomeWallpaperHashCode(long j);

    @TargetApi(24)
    void setHomeWallpaperManagerId(int i);

    void setHomeWallpaperPackageName(String str);

    void setHomeWallpaperRemoteId(String str);

    void setHomeWallpaperServiceName(String str);

    void setLastAppActiveTimestamp(long j);

    void setLastDailyLogTimestamp(long j);

    void setLastSyncTimestamp(long j);

    void setLockWallpaperActionIconRes(int i);

    void setLockWallpaperActionLabelRes(int i);

    void setLockWallpaperActionUrl(String str);

    void setLockWallpaperAttributions(List<String> list);

    void setLockWallpaperCollectionId(String str);

    void setLockWallpaperHashCode(long j);

    @TargetApi(24)
    void setLockWallpaperId(int i);

    void setLockWallpaperRemoteId(String str);

    void setPendingDailyWallpaperUpdateStatus(int i);

    void setPendingDailyWallpaperUpdateStatusSync(int i);

    void setPendingWallpaperSetStatus(int i);

    void setPendingWallpaperSetStatusSync(int i);

    void setWallpaperPresentationMode(int i);

    void updateDailyWallpaperSet(int i, String str, String str2);
}
