package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
/* loaded from: classes.dex */
public final class zzag implements PendingResult.zza {
    public final /* synthetic */ BasePendingResult zza;
    public final /* synthetic */ zzaf zzb;

    public zzag(zzaf zzaf, BasePendingResult basePendingResult) {
        this.zzb = zzaf;
        this.zza = basePendingResult;
    }

    @Override // com.google.android.gms.common.api.PendingResult.zza
    public final void zza(Status status) {
        this.zzb.zza.remove(this.zza);
    }
}
