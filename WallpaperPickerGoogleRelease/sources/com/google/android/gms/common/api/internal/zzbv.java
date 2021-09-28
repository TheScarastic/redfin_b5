package com.google.android.gms.common.api.internal;

import androidx.preference.R$layout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.IAccountAccessor;
import java.util.Set;
/* loaded from: classes.dex */
public final class zzbv implements zzde, BaseGmsClient.ConnectionProgressReportCallbacks {
    public final /* synthetic */ zzbn zza;
    public final Api.Client zzb;
    public final zzi<?> zzc;
    public IAccountAccessor zzd = null;
    public Set<Scope> zze = null;
    public boolean zzf = false;

    public zzbv(zzbn zzbn, Api.Client client, zzi<?> zzi) {
        this.zza = zzbn;
        this.zzb = client;
        this.zzc = zzi;
    }

    @Override // com.google.android.gms.common.internal.BaseGmsClient.ConnectionProgressReportCallbacks
    public final void onReportServiceBinding(ConnectionResult connectionResult) {
        this.zza.zzq.post(new zzbw(this, connectionResult));
    }

    public final void zza(ConnectionResult connectionResult) {
        zzbp<?> zzbp = this.zza.zzm.get(this.zzc);
        R$layout.zza(zzbp.zza.zzq);
        zzbp.zzc.disconnect();
        zzbp.onConnectionFailed(connectionResult);
    }
}
