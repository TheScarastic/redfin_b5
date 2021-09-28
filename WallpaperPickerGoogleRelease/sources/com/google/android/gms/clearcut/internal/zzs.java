package com.google.android.gms.clearcut.internal;

import android.content.Context;
import android.net.Uri;
import com.google.android.gms.clearcut.ClearcutLogger;
import com.google.android.gms.internal.zzbmk;
import com.google.android.gms.internal.zzfis;
import com.google.android.gms.internal.zzfiz;
import com.google.android.gms.phenotype.Phenotype;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public final class zzs implements ClearcutLogger.LogSampler {
    public static final Charset zza = Charset.forName("UTF-8");
    public static final zzfiz zzb;
    public static Map<String, zzfis<String>> zzd;
    public static Boolean zze;
    public static Long zzf;
    public final Context zzc;

    static {
        int i = Phenotype.$r8$clinit;
        String valueOf = String.valueOf(Uri.encode("com.google.android.gms.clearcut.public"));
        zzb = new zzfiz(null, Uri.parse(valueOf.length() != 0 ? "content://com.google.android.gms.phenotype/".concat(valueOf) : new String("content://com.google.android.gms.phenotype/")), "gms:playlog:service:sampling_", "LogSampling__", false, false);
        zzd = null;
        zze = null;
        zzf = null;
    }

    public zzs(Context context) {
        Context applicationContext;
        this.zzc = context;
        if (zzd == null) {
            zzd = new HashMap();
        }
        if (context != null && zzfis.zzc == null && !zzfis.zzd) {
            synchronized (zzfis.zzb) {
                if (!context.isDeviceProtectedStorage() && (applicationContext = context.getApplicationContext()) != null) {
                    context = applicationContext;
                }
                if (zzfis.zzc != context) {
                    zzfis.zze = null;
                }
                zzfis.zzc = context;
            }
            zzfis.zzd = false;
        }
    }

    public static boolean zza(Context context) {
        if (zze == null) {
            zze = Boolean.valueOf(zzbmk.zza(context).zza.checkCallingOrSelfPermission("com.google.android.providers.gsf.permission.READ_GSERVICES") == 0);
        }
        return zze.booleanValue();
    }
}
