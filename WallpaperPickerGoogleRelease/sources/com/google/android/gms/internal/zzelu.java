package com.google.android.gms.internal;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.internal.zzaa;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.zzav;
import com.google.android.gms.common.internal.zzl;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.signin.zzd;
/* loaded from: classes.dex */
public final class zzelu extends zzl<zzels> implements zzd {
    public final boolean zzc;
    public final ClientSettings zzd;
    public final Bundle zze;
    public Integer zzf;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public zzelu(Context context, Looper looper, ClientSettings clientSettings, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 44, clientSettings, connectionCallbacks, onConnectionFailedListener);
        SignInOptions signInOptions = clientSettings.zzi;
        Integer num = clientSettings.zzj;
        Bundle bundle = new Bundle();
        bundle.putParcelable("com.google.android.gms.signin.internal.clientRequestedAccount", clientSettings.zza);
        if (num != null) {
            bundle.putInt("com.google.android.gms.common.internal.ClientSettings.sessionId", num.intValue());
        }
        if (signInOptions != null) {
            bundle.putBoolean("com.google.android.gms.signin.internal.offlineAccessRequested", false);
            bundle.putBoolean("com.google.android.gms.signin.internal.idTokenRequested", false);
            bundle.putString("com.google.android.gms.signin.internal.serverClientId", null);
            bundle.putBoolean("com.google.android.gms.signin.internal.usePromptModeForAuthCode", true);
            bundle.putBoolean("com.google.android.gms.signin.internal.forceCodeForRefreshToken", false);
            bundle.putString("com.google.android.gms.signin.internal.hostedDomain", null);
            bundle.putBoolean("com.google.android.gms.signin.internal.waitForAccessTokenRefresh", false);
        }
        this.zzc = true;
        this.zzd = clientSettings;
        this.zze = bundle;
        this.zzf = clientSettings.zzj;
    }

    @Override // com.google.android.gms.common.internal.BaseGmsClient
    public final String getServiceDescriptor() {
        return "com.google.android.gms.signin.internal.ISignInService";
    }

    @Override // com.google.android.gms.common.internal.BaseGmsClient
    public final String getStartServiceAction() {
        return "com.google.android.gms.signin.service.START";
    }

    @Override // com.google.android.gms.common.internal.BaseGmsClient, com.google.android.gms.common.api.Api.Client
    public final boolean requiresSignIn() {
        return this.zzc;
    }

    @Override // com.google.android.gms.common.api.Api.Client
    public final int zza() {
        return 12529000;
    }

    @Override // com.google.android.gms.signin.zzd
    public final void zza(zzelq zzelq) {
        try {
            Account account = this.zzd.zza;
            if (account == null) {
                account = new Account("<<default account>>", "com.google");
            }
            GoogleSignInAccount googleSignInAccount = null;
            if ("<<default account>>".equals(account.name)) {
                googleSignInAccount = zzaa.zza(this.zzi).zza();
            }
            ((zzels) zzag()).zza(new zzelv(new zzav(account, this.zzf.intValue(), googleSignInAccount)), zzelq);
        } catch (RemoteException e) {
            Log.w("SignInClientImpl", "Remote service probably died when signIn is called");
            try {
                zzelq.zza(new zzelx());
            } catch (RemoteException unused) {
                Log.wtf("SignInClientImpl", "ISignInCallbacks#onSignInComplete should be executed from the same process, unexpected RemoteException.", e);
            }
        }
    }

    @Override // com.google.android.gms.common.internal.BaseGmsClient
    public final Bundle zzb() {
        if (!this.zzi.getPackageName().equals(this.zzd.zzg)) {
            this.zze.putString("com.google.android.gms.signin.internal.realClientPackageName", this.zzd.zzg);
        }
        return this.zze;
    }

    @Override // com.google.android.gms.signin.zzd
    public final void zzd() {
        connect(new BaseGmsClient.zzf());
    }

    @Override // com.google.android.gms.common.internal.BaseGmsClient
    public final /* synthetic */ IInterface zza(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.signin.internal.ISignInService");
        if (queryLocalInterface instanceof zzels) {
            return (zzels) queryLocalInterface;
        }
        return new zzelt(iBinder);
    }
}
