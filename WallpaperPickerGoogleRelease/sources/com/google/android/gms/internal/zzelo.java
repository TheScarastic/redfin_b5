package com.google.android.gms.internal;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public final class zzelo implements Parcelable.Creator<zzeln> {
    /* Return type fixed from 'java.lang.Object' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzeln createFromParcel(Parcel parcel) {
        int zza = zzbkw.zza(parcel);
        int i = 0;
        Intent intent = null;
        int i2 = 0;
        while (parcel.dataPosition() < zza) {
            int readInt = parcel.readInt();
            int i3 = 65535 & readInt;
            if (i3 == 1) {
                i = zzbkw.zzg(parcel, readInt);
            } else if (i3 == 2) {
                i2 = zzbkw.zzg(parcel, readInt);
            } else if (i3 != 3) {
                zzbkw.zzb(parcel, readInt);
            } else {
                intent = (Intent) zzbkw.zza(parcel, readInt, Intent.CREATOR);
            }
        }
        zzbkw.zzae(parcel, zza);
        return new zzeln(i, i2, intent);
    }

    /* Return type fixed from 'java.lang.Object[]' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzeln[] newArray(int i) {
        return new zzeln[i];
    }
}
