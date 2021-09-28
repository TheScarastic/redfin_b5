package com.google.android.apps.wallpaper.module;

import android.content.Intent;
import com.android.customization.model.grid.GridOption;
import com.android.customization.module.ThemesUserEventLogger;
/* loaded from: classes.dex */
public class CompositeUserEventLogger implements ThemesUserEventLogger {
    public final ThemesUserEventLogger mLogger1;
    public final ThemesUserEventLogger mLogger2;

    public CompositeUserEventLogger(ThemesUserEventLogger themesUserEventLogger, ThemesUserEventLogger themesUserEventLogger2) {
        this.mLogger1 = themesUserEventLogger;
        this.mLogger2 = themesUserEventLogger2;
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logActionClicked(String str, int i) {
        this.mLogger1.logActionClicked(str, i);
        this.mLogger2.logActionClicked(str, i);
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logAppLaunched(Intent intent) {
        this.mLogger1.logAppLaunched(intent);
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logCategorySelected(String str) {
        this.mLogger1.logCategorySelected(str);
        this.mLogger2.logCategorySelected(str);
    }

    @Override // com.android.customization.module.ThemesUserEventLogger
    public void logColorApplied(int i, int i2) {
        this.mLogger1.logColorApplied(i, i2);
        this.mLogger2.logColorApplied(i, i2);
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logCurrentWallpaperPreviewed() {
        this.mLogger1.logCurrentWallpaperPreviewed();
        this.mLogger2.logCurrentWallpaperPreviewed();
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logDailyRefreshTurnedOn() {
        this.mLogger1.logDailyRefreshTurnedOn();
        this.mLogger2.logDailyRefreshTurnedOn();
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logDailyWallpaperMetadataRequestFailure(int i) {
        this.mLogger1.logDailyWallpaperMetadataRequestFailure(i);
        this.mLogger2.logDailyWallpaperMetadataRequestFailure(i);
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logDailyWallpaperRotationHour(int i) {
        this.mLogger1.logDailyWallpaperRotationHour(i);
        this.mLogger2.logDailyWallpaperRotationHour(i);
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logDailyWallpaperRotationStatus(int i) {
        this.mLogger1.logDailyWallpaperRotationStatus(i);
        this.mLogger2.logDailyWallpaperRotationStatus(i);
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logDailyWallpaperSetNextWallpaperCrash(int i) {
        this.mLogger1.logDailyWallpaperSetNextWallpaperCrash(i);
        this.mLogger2.logDailyWallpaperSetNextWallpaperCrash(i);
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logDailyWallpaperSetNextWallpaperResult(int i) {
        this.mLogger1.logDailyWallpaperSetNextWallpaperResult(i);
        this.mLogger2.logDailyWallpaperSetNextWallpaperResult(i);
    }

    @Override // com.android.customization.module.ThemesUserEventLogger
    public void logGridApplied(GridOption gridOption) {
        this.mLogger1.logGridApplied(gridOption);
        this.mLogger2.logGridApplied(gridOption);
    }

    @Override // com.android.customization.module.ThemesUserEventLogger
    public void logGridSelected(GridOption gridOption) {
        this.mLogger1.logGridSelected(gridOption);
        this.mLogger2.logGridSelected(gridOption);
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logIndividualWallpaperSelected(String str) {
        this.mLogger1.logIndividualWallpaperSelected(str);
        this.mLogger2.logIndividualWallpaperSelected(str);
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logNumDailyWallpaperRotationsInLastWeek() {
        this.mLogger1.logNumDailyWallpaperRotationsInLastWeek();
        this.mLogger2.logNumDailyWallpaperRotationsInLastWeek();
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logNumDailyWallpaperRotationsPreviousDay() {
        this.mLogger1.logNumDailyWallpaperRotationsPreviousDay();
        this.mLogger2.logNumDailyWallpaperRotationsPreviousDay();
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logNumDaysDailyRotationFailed(int i) {
        this.mLogger1.logNumDaysDailyRotationFailed(i);
        this.mLogger2.logNumDaysDailyRotationFailed(i);
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logNumDaysDailyRotationNotAttempted(int i) {
        this.mLogger1.logNumDaysDailyRotationNotAttempted(i);
        this.mLogger2.logNumDaysDailyRotationNotAttempted(i);
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logRefreshDailyWallpaperButtonClicked() {
        this.mLogger1.logRefreshDailyWallpaperButtonClicked();
        this.mLogger2.logRefreshDailyWallpaperButtonClicked();
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logRestored() {
        this.mLogger1.logRestored();
        this.mLogger2.logRestored();
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logResumed(boolean z, boolean z2) {
        this.mLogger1.logResumed(z, z2);
        this.mLogger2.logResumed(z, z2);
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logSnapshot() {
        this.mLogger1.logSnapshot();
        this.mLogger2.logSnapshot();
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logStandalonePreviewImageUriHasReadPermission(boolean z) {
        this.mLogger1.logStandalonePreviewImageUriHasReadPermission(z);
        this.mLogger2.logStandalonePreviewImageUriHasReadPermission(z);
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logStandalonePreviewLaunched() {
        this.mLogger1.logStandalonePreviewLaunched();
        this.mLogger2.logStandalonePreviewLaunched();
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logStandalonePreviewStorageDialogApproved(boolean z) {
        this.mLogger1.logStandalonePreviewImageUriHasReadPermission(z);
        this.mLogger2.logStandalonePreviewImageUriHasReadPermission(z);
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logStopped() {
        this.mLogger1.logStopped();
        this.mLogger2.logStopped();
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logWallpaperPresentationMode() {
        this.mLogger1.logWallpaperPresentationMode();
        this.mLogger2.logWallpaperPresentationMode();
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logWallpaperSet(String str, String str2) {
        this.mLogger1.logWallpaperSet(str, str2);
        this.mLogger2.logWallpaperSet(str, str2);
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logWallpaperSetFailureReason(int i) {
        this.mLogger1.logWallpaperSetFailureReason(i);
        this.mLogger2.logWallpaperSetFailureReason(i);
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logWallpaperSetResult(int i) {
        this.mLogger1.logWallpaperSetResult(i);
        this.mLogger2.logWallpaperSetResult(i);
    }
}
