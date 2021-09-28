package com.google.android.gms.common;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.text.TextUtils;
import com.android.systemui.shared.system.QuickStepContract;
import com.google.android.gms.common.internal.zzs;
import com.google.android.gms.common.util.zzi;
import com.google.android.gms.internal.zzbmj;
import com.google.android.gms.internal.zzbmk;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes.dex */
public class GoogleApiAvailabilityLight {
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE = 12529000;

    static {
        AtomicBoolean atomicBoolean = GooglePlayServicesUtilLight.zza;
    }

    public Intent getErrorResolutionIntent(Context context, int i, String str) {
        if (i == 1 || i == 2) {
            if (context == null || !zzi.zzb(context)) {
                StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("gcore_");
                m.append(GOOGLE_PLAY_SERVICES_VERSION_CODE);
                m.append("-");
                if (!TextUtils.isEmpty(str)) {
                    m.append(str);
                }
                m.append("-");
                if (context != null) {
                    m.append(context.getPackageName());
                }
                m.append("-");
                if (context != null) {
                    try {
                        zzbmj zza = zzbmk.zza(context);
                        m.append(zza.zza.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
                    } catch (PackageManager.NameNotFoundException unused) {
                    }
                }
                String sb = m.toString();
                int i2 = zzs.$r8$clinit;
                Intent intent = new Intent("android.intent.action.VIEW");
                Uri.Builder appendQueryParameter = Uri.parse("market://details").buildUpon().appendQueryParameter("id", "com.google.android.gms");
                if (!TextUtils.isEmpty(sb)) {
                    appendQueryParameter.appendQueryParameter("pcampaignid", sb);
                }
                intent.setData(appendQueryParameter.build());
                intent.setPackage("com.android.vending");
                intent.addFlags(QuickStepContract.SYSUI_STATE_MAGNIFICATION_OVERLAP);
                return intent;
            }
            int i3 = zzs.$r8$clinit;
            Intent intent2 = new Intent("com.google.android.clockwork.home.UPDATE_ANDROID_WEAR_ACTION");
            intent2.setPackage("com.google.android.wearable.app");
            return intent2;
        } else if (i != 3) {
            return null;
        } else {
            int i4 = zzs.$r8$clinit;
            Uri fromParts = Uri.fromParts("package", "com.google.android.gms", null);
            Intent intent3 = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent3.setData(fromParts);
            return intent3;
        }
    }

    public PendingIntent getErrorResolutionPendingIntent(Context context, int i, int i2, String str) {
        Intent errorResolutionIntent = getErrorResolutionIntent(context, i, str);
        if (errorResolutionIntent == null) {
            return null;
        }
        return PendingIntent.getActivity(context, i2, errorResolutionIntent, 134217728);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(14:2|(2:114|3)|5|(4:9|2b|24|(2:26|(2:28|29))(2:30|31))|35|(4:37|(3:39|(1:44)(1:43)|45)|46|(13:48|(1:51)(1:52)|53|(2:108|55)|112|57|58|f7|72|(1:74)(1:(6:82|(1:84)(1:85)|(1:87)|(1:89)(4:90|(2:106|92)|95|(1:97)(1:98))|103|(1:105)(1:119))(1:80))|81|103|(0)(0)))|49|(0)(0)|53|(0)|112|57|58|f7) */
    /* JADX WARNING: Code restructure failed: missing block: B:102:0x019d, code lost:
        android.util.Log.w("GooglePlayServicesUtil", "Google Play services is missing.");
     */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x01aa A[ORIG_RETURN, RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:108:0x00dc A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x00f8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:119:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00cd  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00cf  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int isGooglePlayServicesAvailable(android.content.Context r11, int r12) {
        /*
        // Method dump skipped, instructions count: 429
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.GoogleApiAvailabilityLight.isGooglePlayServicesAvailable(android.content.Context, int):int");
    }

    public boolean isUserResolvableError(int i) {
        AtomicBoolean atomicBoolean = GooglePlayServicesUtilLight.zza;
        return i == 1 || i == 2 || i == 3 || i == 9;
    }
}
