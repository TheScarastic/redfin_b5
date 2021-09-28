package com.android.customization.model;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
/* loaded from: classes.dex */
public abstract class ResourcesApkProvider {
    public final Context mContext;
    public final Resources mStubApkResources;
    public final String mStubPackageName;

    public ResourcesApkProvider(Context context, String str) {
        this.mContext = context;
        this.mStubPackageName = str;
        Resources resources = null;
        try {
            if (!TextUtils.isEmpty(str)) {
                try {
                    PackageManager packageManager = context.getPackageManager();
                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, 1048704);
                    if (applicationInfo != null) {
                        resources = packageManager.getResourcesForApplication(applicationInfo);
                    }
                } catch (PackageManager.NameNotFoundException unused) {
                    Log.w("ResourcesApkProvider", String.format("Stub APK for %s not found.", this.mStubPackageName));
                }
            }
        } finally {
            this.mStubApkResources = resources;
        }
    }
}
