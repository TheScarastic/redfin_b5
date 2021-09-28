package com.google.android.gms.common.api.internal;

import androidx.collection.ArrayMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.AvailabilityException;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.Map;
/* loaded from: classes.dex */
public final class zzk {
    public final ArrayMap<zzi<?>, ConnectionResult> zza;
    public final ArrayMap<zzi<?>, String> zzb;
    public final TaskCompletionSource<Map<zzi<?>, String>> zzc;
    public int zzd;
    public boolean zze;

    public final void zza(zzi<?> zzi, ConnectionResult connectionResult, String str) {
        this.zza.put(zzi, connectionResult);
        this.zzb.put(zzi, str);
        this.zzd--;
        if (!connectionResult.isSuccess()) {
            this.zze = true;
        }
        if (this.zzd != 0) {
            return;
        }
        if (this.zze) {
            this.zzc.setException(new AvailabilityException(this.zza));
            return;
        }
        this.zzc.setResult(this.zzb);
    }
}
