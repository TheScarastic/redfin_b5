package com.google.android.gms.common.api.internal;

import android.os.DeadObjectException;
import android.os.RemoteException;
import android.os.TransactionTooLargeException;
import com.google.android.gms.common.api.Status;
/* loaded from: classes.dex */
public abstract class zzb {
    public zzb(int i) {
    }

    public static Status zza(RemoteException remoteException) {
        StringBuilder sb = new StringBuilder();
        if (remoteException instanceof TransactionTooLargeException) {
            sb.append("TransactionTooLargeException: ");
        }
        sb.append(remoteException.getLocalizedMessage());
        return new Status(8, sb.toString());
    }

    public abstract void zza(Status status);

    public abstract void zza(zzaf zzaf, boolean z);

    public abstract void zza(zzbp<?> zzbp) throws DeadObjectException;
}
