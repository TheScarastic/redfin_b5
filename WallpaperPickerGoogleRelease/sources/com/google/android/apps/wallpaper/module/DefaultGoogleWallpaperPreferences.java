package com.google.android.apps.wallpaper.module;

import android.content.Context;
import com.android.customization.module.DefaultCustomizationPreferences;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class DefaultGoogleWallpaperPreferences extends DefaultCustomizationPreferences implements GoogleWallpaperPreferences {
    public DefaultGoogleWallpaperPreferences(@Nullable Context context) {
        super(context);
    }

    @Override // com.google.android.apps.wallpaper.module.GoogleWallpaperPreferences
    public boolean isThemesSanitized() {
        return this.mNoBackupPrefs.getBoolean("themes_sanitized", false);
    }

    @Override // com.google.android.apps.wallpaper.module.GoogleWallpaperPreferences
    public void setThemesSanitized(boolean z) {
        this.mNoBackupPrefs.edit().putBoolean("themes_sanitized", z).apply();
    }
}
