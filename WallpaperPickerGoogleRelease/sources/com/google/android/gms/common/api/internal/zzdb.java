package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.preference.R$layout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.internal.zzelp;
import com.google.android.gms.internal.zzelx;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.signin.zza;
import com.google.android.gms.signin.zzd;
import java.util.Set;
/* loaded from: classes.dex */
public final class zzdb extends zzelp implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static Api.zza<? extends zzd, SignInOptions> zza = zza.zza;
    public final Context zzb;
    public final Handler zzc;
    public final Api.zza<? extends zzd, SignInOptions> zzd;
    public Set<Scope> zze;
    public ClientSettings zzf;
    public zzd zzg;
    public zzde zzh;

    public zzdb(Context context, Handler handler, ClientSettings clientSettings, Api.zza<? extends zzd, SignInOptions> zza2) {
        this.zzb = context;
        this.zzc = handler;
        R$layout.zza(clientSettings, "ClientSettings must not be null");
        this.zzf = clientSettings;
        this.zze = clientSettings.zzb;
        this.zzd = zza2;
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    public final void onConnected(Bundle bundle) {
        this.zzg.zza(this);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
    public final void onConnectionFailed(ConnectionResult connectionResult) {
        ((zzbv) this.zzh).zza(connectionResult);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    public final void onConnectionSuspended(int i) {
        this.zzg.disconnect();
    }

    @Override // com.google.android.gms.internal.zzelq
    public final void zza(zzelx zzelx) {
        this.zzc.post(new zzbs(this, zzelx));
    }
}
