package com.android.wallpaper.module;

import android.content.Intent;
/* loaded from: classes.dex */
public interface UserEventLogger {
    void logActionClicked(String str, int i);

    void logAppLaunched(Intent intent);

    void logCategorySelected(String str);

    void logCurrentWallpaperPreviewed();

    void logDailyRefreshTurnedOn();

    void logDailyWallpaperMetadataRequestFailure(int i);

    void logDailyWallpaperRotationHour(int i);

    void logDailyWallpaperRotationStatus(int i);

    void logDailyWallpaperSetNextWallpaperCrash(int i);

    void logDailyWallpaperSetNextWallpaperResult(int i);

    void logIndividualWallpaperSelected(String str);

    void logNumDailyWallpaperRotationsInLastWeek();

    void logNumDailyWallpaperRotationsPreviousDay();

    void logNumDaysDailyRotationFailed(int i);

    void logNumDaysDailyRotationNotAttempted(int i);

    void logRefreshDailyWallpaperButtonClicked();

    void logRestored();

    void logResumed(boolean z, boolean z2);

    void logSnapshot();

    void logStandalonePreviewImageUriHasReadPermission(boolean z);

    void logStandalonePreviewLaunched();

    void logStandalonePreviewStorageDialogApproved(boolean z);

    void logStopped();

    void logWallpaperPresentationMode();

    void logWallpaperSet(String str, String str2);

    void logWallpaperSetFailureReason(int i);

    void logWallpaperSetResult(int i);
}
