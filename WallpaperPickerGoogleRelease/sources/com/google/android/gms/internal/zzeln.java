package com.google.android.gms.internal;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.slice.view.R$id;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
/* loaded from: classes.dex */
public final class zzeln extends zzbkv implements Result {
    public static final Parcelable.Creator<zzeln> CREATOR = new zzelo();
    public final int zza;
    public int zzb;
    public Intent zzc;

    public zzeln() {
        this.zza = 2;
        this.zzb = 0;
        this.zzc = null;
    }

    @Override // com.google.android.gms.common.api.Result
    public final Status getStatus() {
        if (this.zzb == 0) {
            return Status.zza;
        }
        return Status.zze;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int zzb = R$id.zzb(parcel, 20293);
        int i2 = this.zza;
        R$id.zzb(parcel, 1, 4);
        parcel.writeInt(i2);
        int i3 = this.zzb;
        R$id.zzb(parcel, 2, 4);
        parcel.writeInt(i3);
        R$id.zza(parcel, 3, this.zzc, i, false);
        R$id.zzc(parcel, zzb);
    }

    public zzeln(int i, int i2, Intent intent) {
        this.zza = i;
        this.zzb = i2;
        this.zzc = intent;
    }
}
