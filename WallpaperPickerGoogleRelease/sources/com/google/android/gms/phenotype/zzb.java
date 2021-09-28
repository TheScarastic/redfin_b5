package com.google.android.gms.phenotype;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.internal.zzbkw;
/* loaded from: classes.dex */
public final class zzb implements Parcelable.Creator<Configuration> {
    /* Return type fixed from 'java.lang.Object' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Configuration createFromParcel(Parcel parcel) {
        int zza = zzbkw.zza(parcel);
        Flag[] flagArr = null;
        int i = 0;
        String[] strArr = null;
        while (parcel.dataPosition() < zza) {
            int readInt = parcel.readInt();
            int i2 = 65535 & readInt;
            if (i2 == 2) {
                i = zzbkw.zzg(parcel, readInt);
            } else if (i2 == 3) {
                flagArr = (Flag[]) zzbkw.zzb(parcel, readInt, Flag.CREATOR);
            } else if (i2 != 4) {
                zzbkw.zzb(parcel, readInt);
            } else {
                strArr = zzbkw.zzaa(parcel, readInt);
            }
        }
        zzbkw.zzae(parcel, zza);
        return new Configuration(i, flagArr, strArr);
    }

    /* Return type fixed from 'java.lang.Object[]' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Configuration[] newArray(int i) {
        return new Configuration[i];
    }
}
