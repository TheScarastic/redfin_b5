package com.google.android.gms.internal;

import android.content.Context;
/* loaded from: classes.dex */
public final class zzbmk {
    public static zzbmk zzb = new zzbmk();
    public zzbmj zza = null;

    public static zzbmj zza(Context context) {
        zzbmj zzbmj;
        zzbmk zzbmk = zzb;
        synchronized (zzbmk) {
            if (zzbmk.zza == null) {
                if (context.getApplicationContext() != null) {
                    context = context.getApplicationContext();
                }
                zzbmk.zza = new zzbmj(context);
            }
            zzbmj = zzbmk.zza;
        }
        return zzbmj;
    }
}
