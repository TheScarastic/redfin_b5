package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.IAccountAccessor;
import java.util.Collections;
/* loaded from: classes.dex */
public final class zzbw implements Runnable {
    public final /* synthetic */ ConnectionResult zza;
    public final /* synthetic */ zzbv zzb;

    public zzbw(zzbv zzbv, ConnectionResult connectionResult) {
        this.zzb = zzbv;
        this.zza = connectionResult;
    }

    @Override // java.lang.Runnable
    public final void run() {
        IAccountAccessor iAccountAccessor;
        if (this.zza.isSuccess()) {
            zzbv zzbv = this.zzb;
            zzbv.zzf = true;
            if (zzbv.zzb.requiresSignIn()) {
                zzbv zzbv2 = this.zzb;
                if (zzbv2.zzf && (iAccountAccessor = zzbv2.zzd) != null) {
                    zzbv2.zzb.getRemoteService(iAccountAccessor, zzbv2.zze);
                    return;
                }
                return;
            }
            this.zzb.zzb.getRemoteService(null, Collections.emptySet());
            return;
        }
        zzbv zzbv3 = this.zzb;
        zzbv3.zza.zzm.get(zzbv3.zzc).onConnectionFailed(this.zza);
    }
}
