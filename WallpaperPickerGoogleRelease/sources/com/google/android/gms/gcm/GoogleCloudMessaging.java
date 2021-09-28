package com.google.android.gms.gcm;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.google.android.gms.iid.Rpc;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public class GoogleCloudMessaging {
    static {
        new AtomicInteger(1);
    }

    public static int zza(Context context) {
        String findIidPackage = Rpc.findIidPackage(context);
        if (findIidPackage == null) {
            return -1;
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(findIidPackage, 0);
            if (packageInfo != null) {
                return packageInfo.versionCode;
            }
            return -1;
        } catch (PackageManager.NameNotFoundException unused) {
            return -1;
        }
    }
}
