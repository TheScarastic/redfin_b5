package com.android.settingslib.development;

import android.content.Context;
import android.os.Build;
import android.os.UserManager;
import android.provider.Settings;
/* loaded from: classes.dex */
public class DevelopmentSettingsEnabler {
    public static boolean isDevelopmentSettingsEnabled(Context context) {
        UserManager userManager = (UserManager) context.getSystemService("user");
        return userManager.isAdminUser() && !userManager.hasUserRestriction("no_debugging_features") && (Settings.Global.getInt(context.getContentResolver(), "development_settings_enabled", Build.TYPE.equals("eng") ? 1 : 0) != 0 ? 1 : null) != null;
    }
}
