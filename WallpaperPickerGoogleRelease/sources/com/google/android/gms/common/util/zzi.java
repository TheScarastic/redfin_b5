package com.google.android.gms.common.util;

import android.annotation.TargetApi;
import android.content.Context;
/* loaded from: classes.dex */
public final class zzi {
    public static Boolean zzc;
    public static Boolean zzd;
    public static Boolean zze;

    @TargetApi(24)
    public static boolean zzb(Context context) {
        if (zzd == null) {
            zzd = Boolean.valueOf(context.getPackageManager().hasSystemFeature("cn.google"));
        }
        if (!zzd.booleanValue()) {
            return false;
        }
        if (zzc == null) {
            zzc = Boolean.valueOf(context.getPackageManager().hasSystemFeature("android.hardware.type.watch"));
        }
        return zzc.booleanValue();
    }
}
