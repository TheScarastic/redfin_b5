package com.google.android.gms.common.api.internal;

import android.os.DeadObjectException;
import android.os.RemoteException;
import androidx.preference.R$layout;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
/* loaded from: classes.dex */
public abstract class zzn<R extends Result, A extends Api.zzb> extends BasePendingResult<R> {
    public final Api.zzc<A> zza;
    public final Api<?> zzb;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public zzn(Api<?> api, GoogleApiClient googleApiClient) {
        super(googleApiClient);
        R$layout.zza(googleApiClient, "GoogleApiClient must not be null");
        R$layout.zza(api, "Api must not be null");
        this.zza = (Api.zzc<A>) api.zzc();
        this.zzb = api;
    }

    public abstract void zza(A a) throws RemoteException;

    public final void zzb(A a) throws DeadObjectException {
        try {
            zza((zzn<R, A>) a);
        } catch (DeadObjectException e) {
            zzc(new Status(1, 8, e.getLocalizedMessage(), null));
            throw e;
        } catch (RemoteException e2) {
            zzc(new Status(1, 8, e2.getLocalizedMessage(), null));
        }
    }

    public final void zzc(Status status) {
        R$layout.zzb(!status.isSuccess(), "Failed result must not be success");
        zza((zzn<R, A>) zza(status));
    }
}
