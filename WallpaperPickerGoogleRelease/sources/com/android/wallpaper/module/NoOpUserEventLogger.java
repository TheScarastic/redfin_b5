package com.android.wallpaper.module;

import com.android.wallpaper.model.WallpaperInfo;
import com.google.android.apps.wallpaper.model.GoogleLiveWallpaperInfo;
import com.google.android.apps.wallpaper.model.MicropaperWallpaperInfo;
import wireless.android.learning.acmi.micropaper.frontend.MicropaperFrontend;
/* loaded from: classes.dex */
public class NoOpUserEventLogger implements UserEventLogger {
    public NoOpUserEventLogger(int i) {
    }

    public WallpaperInfo getLiveWallpaperInfo(android.app.WallpaperInfo wallpaperInfo) {
        if (wallpaperInfo.getComponent().equals(MicropaperFrontend.MICROPAPER_SERVICE)) {
            return new MicropaperWallpaperInfo(wallpaperInfo);
        }
        return new GoogleLiveWallpaperInfo(wallpaperInfo);
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logCurrentWallpaperPreviewed() {
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logDailyRefreshTurnedOn() {
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logDailyWallpaperMetadataRequestFailure(int i) {
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logDailyWallpaperRotationHour(int i) {
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logDailyWallpaperRotationStatus(int i) {
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logDailyWallpaperSetNextWallpaperCrash(int i) {
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logDailyWallpaperSetNextWallpaperResult(int i) {
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logNumDailyWallpaperRotationsInLastWeek() {
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logNumDailyWallpaperRotationsPreviousDay() {
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logNumDaysDailyRotationFailed(int i) {
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logNumDaysDailyRotationNotAttempted(int i) {
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logRefreshDailyWallpaperButtonClicked() {
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logRestored() {
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logSnapshot() {
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logStandalonePreviewImageUriHasReadPermission(boolean z) {
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logStandalonePreviewLaunched() {
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logStandalonePreviewStorageDialogApproved(boolean z) {
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logWallpaperPresentationMode() {
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logWallpaperSetFailureReason(int i) {
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logWallpaperSetResult(int i) {
    }
}
