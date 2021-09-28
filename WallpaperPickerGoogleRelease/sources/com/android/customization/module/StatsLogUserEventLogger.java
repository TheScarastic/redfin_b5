package com.android.customization.module;

import android.content.Intent;
import com.android.customization.model.grid.GridOption;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.wallpaper.module.NoOpUserEventLogger;
import java.util.Objects;
/* loaded from: classes.dex */
public class StatsLogUserEventLogger extends NoOpUserEventLogger implements ThemesUserEventLogger {
    public StatsLogUserEventLogger() {
        super(0);
    }

    public final int getCollectionIdHashCode(String str) {
        if (str != null) {
            return str.hashCode();
        }
        return 0;
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logActionClicked(String str, int i) {
        SysUiStatsLog.write(SysUiStatsLog.STYLE_UI_CHANGED, 8, 0, 0, 0, 0, 0, getCollectionIdHashCode(str), 0, 0, 0, 0, 0);
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.android.wallpaper.module.UserEventLogger
    public void logAppLaunched(Intent intent) {
        int i;
        int i2 = 1;
        if (intent.hasExtra("com.android.wallpaper.LAUNCH_SOURCE")) {
            String stringExtra = intent.getStringExtra("com.android.wallpaper.LAUNCH_SOURCE");
            Objects.requireNonNull(stringExtra);
            char c = 65535;
            switch (stringExtra.hashCode()) {
                case -1969438830:
                    if (stringExtra.equals("app_launched_settings")) {
                        c = 0;
                        break;
                    }
                    break;
                case -516353265:
                    if (stringExtra.equals("app_launched_launcher")) {
                        c = 1;
                        break;
                    }
                    break;
                case 134309415:
                    if (stringExtra.equals("app_launched_tips")) {
                        c = 2;
                        break;
                    }
                    break;
                case 1520130645:
                    if (stringExtra.equals("app_launched_deeplink")) {
                        c = 3;
                        break;
                    }
                    break;
                case 2082541958:
                    if (stringExtra.equals("app_launched_suw")) {
                        c = 4;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    i = 2;
                    break;
                case 1:
                    i = i2;
                    break;
                case 2:
                    i = 4;
                    break;
                case 3:
                    i2 = 7;
                    i = i2;
                    break;
                case 4:
                    i = 3;
                    break;
                default:
                    i = 0;
                    break;
            }
        } else {
            if (intent.hasExtra(":settings:fragment_args_key")) {
                i2 = 8;
            } else if (intent.getAction() == null || !intent.getAction().equals("android.service.wallpaper.CROP_AND_SET_WALLPAPER")) {
                if (intent.getCategories() != null && intent.getCategories().contains("android.intent.category.LAUNCHER")) {
                    i2 = 5;
                }
                i = 0;
            } else {
                i2 = 6;
            }
            i = i2;
        }
        SysUiStatsLog.write(SysUiStatsLog.STYLE_UI_CHANGED, 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, i);
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logCategorySelected(String str) {
        SysUiStatsLog.write(SysUiStatsLog.STYLE_UI_CHANGED, 5, 0, 0, 0, 0, 0, getCollectionIdHashCode(str), 0, 0, 0, 0, 0);
    }

    @Override // com.android.customization.module.ThemesUserEventLogger
    public void logColorApplied(int i, int i2) {
        SysUiStatsLog.write(SysUiStatsLog.STYLE_UI_CHANGED, i, 0, 0, 0, 0, 0, 0, 0, i2, 0, 0, 0);
    }

    @Override // com.android.customization.module.ThemesUserEventLogger
    public void logGridApplied(GridOption gridOption) {
        SysUiStatsLog.write(SysUiStatsLog.STYLE_UI_CHANGED, 4, 0, 0, 0, 0, gridOption.cols, 0, 0, 0, 0, 0, 0);
    }

    @Override // com.android.customization.module.ThemesUserEventLogger
    public void logGridSelected(GridOption gridOption) {
        SysUiStatsLog.write(SysUiStatsLog.STYLE_UI_CHANGED, 3, 0, 0, 0, 0, gridOption.cols, 0, 0, 0, 0, 0, 0);
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logIndividualWallpaperSelected(String str) {
        SysUiStatsLog.write(SysUiStatsLog.STYLE_UI_CHANGED, 6, 0, 0, 0, 0, 0, getCollectionIdHashCode(str), 0, 0, 0, 0, 0);
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logResumed(boolean z, boolean z2) {
        SysUiStatsLog.write(SysUiStatsLog.STYLE_UI_CHANGED, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logStopped() {
        SysUiStatsLog.write(SysUiStatsLog.STYLE_UI_CHANGED, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logWallpaperSet(String str, String str2) {
        SysUiStatsLog.write(SysUiStatsLog.STYLE_UI_CHANGED, 7, 0, 0, 0, 0, 0, getCollectionIdHashCode(str), str2 != null ? str2.hashCode() : 0, 0, 0, 0, 0);
    }
}
