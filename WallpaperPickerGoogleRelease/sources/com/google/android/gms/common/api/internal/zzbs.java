package com.google.android.gms.common.api.internal;

import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.IAccountAccessor;
import com.google.android.gms.common.internal.zzax;
import com.google.android.gms.internal.zzelx;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.signin.zzd;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes.dex */
public final class zzbs implements Runnable {
    public final /* synthetic */ int $r8$classId = 1;
    public final /* synthetic */ Object zza;
    public final /* synthetic */ Object zzb;

    public zzbs(zzdb zzdb, zzelx zzelx) {
        this.zzb = zzdb;
        this.zza = zzelx;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                ((zzbp) this.zzb).onConnectionFailed((ConnectionResult) this.zza);
                return;
            default:
                zzdb zzdb = (zzdb) this.zzb;
                zzelx zzelx = (zzelx) this.zza;
                Api.zza<? extends zzd, SignInOptions> zza = zzdb.zza;
                Objects.requireNonNull(zzdb);
                ConnectionResult connectionResult = zzelx.zzb;
                if (connectionResult.isSuccess()) {
                    zzax zzax = zzelx.zzc;
                    ConnectionResult connectionResult2 = zzax.zzc;
                    if (!connectionResult2.isSuccess()) {
                        String valueOf = String.valueOf(connectionResult2);
                        StringBuilder sb = new StringBuilder(valueOf.length() + 48);
                        sb.append("Sign-in succeeded with resolve account failure: ");
                        sb.append(valueOf);
                        Log.wtf("SignInCoordinator", sb.toString(), new Exception());
                        ((zzbv) zzdb.zzh).zza(connectionResult2);
                        zzdb.zzg.disconnect();
                        return;
                    }
                    zzde zzde = zzdb.zzh;
                    IAccountAccessor zza2 = zzax.zza();
                    Set<Scope> set = zzdb.zze;
                    zzbv zzbv = (zzbv) zzde;
                    Objects.requireNonNull(zzbv);
                    if (zza2 == null || set == null) {
                        Log.wtf("GoogleApiManager", "Received null response from onSignInSuccess", new Exception());
                        zzbv.zza(new ConnectionResult(4));
                    } else {
                        zzbv.zzd = zza2;
                        zzbv.zze = set;
                        if (zzbv.zzf) {
                            zzbv.zzb.getRemoteService(zza2, set);
                        }
                    }
                } else {
                    ((zzbv) zzdb.zzh).zza(connectionResult);
                }
                zzdb.zzg.disconnect();
                return;
        }
    }
}
