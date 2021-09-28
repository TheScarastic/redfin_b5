package com.google.android.gms.common.api.internal;

import android.os.DeadObjectException;
import android.os.RemoteException;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.TaskCompletionSource;
/* loaded from: classes.dex */
public abstract class zzc<T> extends zzb {
    public final TaskCompletionSource<T> zza;

    public zzc(int i, TaskCompletionSource<T> taskCompletionSource) {
        super(i);
        this.zza = taskCompletionSource;
    }

    @Override // com.google.android.gms.common.api.internal.zzb
    public void zza(Status status) {
        this.zza.trySetException(new ApiException(status));
    }

    public abstract void zzb(zzbp<?> zzbp) throws RemoteException;

    public void zza(RuntimeException runtimeException) {
        this.zza.trySetException(runtimeException);
    }

    @Override // com.google.android.gms.common.api.internal.zzb
    public final void zza(zzbp<?> zzbp) throws DeadObjectException {
        try {
            zzb(zzbp);
        } catch (DeadObjectException e) {
            zza(zzb.zza(e));
            throw e;
        } catch (RemoteException e2) {
            zza(zzb.zza(e2));
        } catch (RuntimeException e3) {
            zza(e3);
        }
    }
}
