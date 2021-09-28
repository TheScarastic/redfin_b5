package com.google.android.gms.common.internal;

import android.content.Context;
import android.util.SparseIntArray;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.Api;
import java.util.Objects;
/* loaded from: classes.dex */
public final class zzv {
    public final SparseIntArray zza = new SparseIntArray();
    public GoogleApiAvailabilityLight zzb;

    public zzv(GoogleApiAvailabilityLight googleApiAvailabilityLight) {
        Objects.requireNonNull(googleApiAvailabilityLight, "null reference");
        this.zzb = googleApiAvailabilityLight;
    }

    public final int zza(Context context, Api.Client client) {
        Objects.requireNonNull(context, "null reference");
        Objects.requireNonNull(client, "null reference");
        int i = 0;
        if (!client.requiresGooglePlayServices()) {
            return 0;
        }
        int zza = client.zza();
        int i2 = this.zza.get(zza, -1);
        if (i2 != -1) {
            return i2;
        }
        int i3 = 0;
        while (true) {
            if (i3 >= this.zza.size()) {
                i = i2;
                break;
            }
            int keyAt = this.zza.keyAt(i3);
            if (keyAt > zza && this.zza.get(keyAt) == 0) {
                break;
            }
            i3++;
        }
        if (i == -1) {
            i = this.zzb.isGooglePlayServicesAvailable(context, zza);
        }
        this.zza.put(zza, i);
        return i;
    }
}
