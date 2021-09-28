package com.google.android.gms.common.api.internal;

import android.os.RemoteException;
import com.google.android.gms.tasks.TaskCompletionSource;
/* loaded from: classes.dex */
public final class zzg extends zzc<Boolean> {
    public final zzcl<?> zzb;

    public zzg(zzcl<?> zzcl, TaskCompletionSource<Boolean> taskCompletionSource) {
        super(4, taskCompletionSource);
        this.zzb = zzcl;
    }

    @Override // com.google.android.gms.common.api.internal.zzb
    public final /* bridge */ /* synthetic */ void zza(zzaf zzaf, boolean z) {
    }

    @Override // com.google.android.gms.common.api.internal.zzc
    public final void zzb(zzbp<?> zzbp) throws RemoteException {
        if (zzbp.zzh.remove(this.zzb) == null) {
            this.zza.trySetResult(Boolean.FALSE);
            return;
        }
        throw null;
    }
}
