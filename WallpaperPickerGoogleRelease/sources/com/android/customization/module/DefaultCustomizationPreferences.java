package com.android.customization.module;

import android.content.Context;
import com.android.wallpaper.module.DefaultWallpaperPreferences;
/* loaded from: classes.dex */
public class DefaultCustomizationPreferences extends DefaultWallpaperPreferences implements CustomizationPreferences {
    public DefaultCustomizationPreferences(Context context) {
        super(context);
    }

    @Override // com.android.customization.module.CustomizationPreferences
    public boolean getThemedIconEnabled() {
        return this.mSharedPrefs.getBoolean("themepicker_themed_icon_enabled", false);
    }

    @Override // com.android.customization.module.CustomizationPreferences
    public void setThemedIconEnabled(boolean z) {
        this.mSharedPrefs.edit().putBoolean("themepicker_themed_icon_enabled", z).apply();
    }
}
