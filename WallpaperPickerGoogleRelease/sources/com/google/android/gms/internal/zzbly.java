package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.slice.view.R$id;
/* loaded from: classes.dex */
public final class zzbly extends zzbkv {
    public static final Parcelable.Creator<zzbly> CREATOR = new zzblz();
    public final int zza;
    public final zzbma zzb;

    public zzbly(int i, zzbma zzbma) {
        this.zza = i;
        this.zzb = zzbma;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int zzb = R$id.zzb(parcel, 20293);
        int i2 = this.zza;
        R$id.zzb(parcel, 1, 4);
        parcel.writeInt(i2);
        R$id.zza(parcel, 2, this.zzb, i, false);
        R$id.zzc(parcel, zzb);
    }

    public zzbly(zzbma zzbma) {
        this.zza = 1;
        this.zzb = zzbma;
    }
}
