package com.android.settingslib.applications;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.SystemProperties;
import com.android.settingslib.applications.instantapps.InstantAppDataProvider;
/* loaded from: classes.dex */
public class AppUtils {
    private static final Intent sBrowserIntent = new Intent().setAction("android.intent.action.VIEW").addCategory("android.intent.category.BROWSABLE").setData(Uri.parse("http:"));
    private static InstantAppDataProvider sInstantAppDataProvider;

    public static boolean isInstant(ApplicationInfo applicationInfo) {
        String[] split;
        InstantAppDataProvider instantAppDataProvider = sInstantAppDataProvider;
        if (instantAppDataProvider != null) {
            if (instantAppDataProvider.isInstantApp(applicationInfo)) {
                return true;
            }
        } else if (applicationInfo.isInstantApp()) {
            return true;
        }
        String str = SystemProperties.get("settingsdebug.instant.packages");
        if (!(str == null || str.isEmpty() || applicationInfo.packageName == null || (split = str.split(",")) == null)) {
            for (String str2 : split) {
                if (applicationInfo.packageName.contains(str2)) {
                    return true;
                }
            }
        }
        return false;
    }
}
