package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.zzav;
/* loaded from: classes.dex */
public final class zzelw implements Parcelable.Creator<zzelv> {
    /* Return type fixed from 'java.lang.Object' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzelv createFromParcel(Parcel parcel) {
        int zza = zzbkw.zza(parcel);
        int i = 0;
        zzav zzav = null;
        while (parcel.dataPosition() < zza) {
            int readInt = parcel.readInt();
            int i2 = 65535 & readInt;
            if (i2 == 1) {
                i = zzbkw.zzg(parcel, readInt);
            } else if (i2 != 2) {
                zzbkw.zzb(parcel, readInt);
            } else {
                zzav = (zzav) zzbkw.zza(parcel, readInt, zzav.CREATOR);
            }
        }
        zzbkw.zzae(parcel, zza);
        return new zzelv(i, zzav);
    }

    /* Return type fixed from 'java.lang.Object[]' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzelv[] newArray(int i) {
        return new zzelv[i];
    }
}
