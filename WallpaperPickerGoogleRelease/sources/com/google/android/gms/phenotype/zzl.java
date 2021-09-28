package com.google.android.gms.phenotype;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.internal.zzbkw;
/* loaded from: classes.dex */
public final class zzl implements Parcelable.Creator<FlagOverride> {
    /* Return type fixed from 'java.lang.Object' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ FlagOverride createFromParcel(Parcel parcel) {
        int zza = zzbkw.zza(parcel);
        String str = null;
        boolean z = false;
        Flag flag = null;
        String str2 = null;
        while (parcel.dataPosition() < zza) {
            int readInt = parcel.readInt();
            int i = 65535 & readInt;
            if (i == 2) {
                str = zzbkw.zzq(parcel, readInt);
            } else if (i == 3) {
                str2 = zzbkw.zzq(parcel, readInt);
            } else if (i == 4) {
                flag = (Flag) zzbkw.zza(parcel, readInt, Flag.CREATOR);
            } else if (i != 5) {
                zzbkw.zzb(parcel, readInt);
            } else {
                z = zzbkw.zzc(parcel, readInt);
            }
        }
        zzbkw.zzae(parcel, zza);
        return new FlagOverride(str, str2, flag, z);
    }

    /* Return type fixed from 'java.lang.Object[]' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ FlagOverride[] newArray(int i) {
        return new FlagOverride[i];
    }
}
