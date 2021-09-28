package com.google.android.gms.phenotype;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.internal.zzbkw;
/* loaded from: classes.dex */
public final class zzc implements Parcelable.Creator<Configurations> {
    /* Return type fixed from 'java.lang.Object' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Configurations createFromParcel(Parcel parcel) {
        int zza = zzbkw.zza(parcel);
        String str = null;
        boolean z = false;
        long j = 0;
        String str2 = null;
        Configuration[] configurationArr = null;
        byte[] bArr = null;
        while (parcel.dataPosition() < zza) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 2:
                    str = zzbkw.zzq(parcel, readInt);
                    break;
                case 3:
                    str2 = zzbkw.zzq(parcel, readInt);
                    break;
                case 4:
                    configurationArr = (Configuration[]) zzbkw.zzb(parcel, readInt, Configuration.CREATOR);
                    break;
                case 5:
                    z = zzbkw.zzc(parcel, readInt);
                    break;
                case 6:
                    bArr = zzbkw.zzt(parcel, readInt);
                    break;
                case 7:
                    j = zzbkw.zzi(parcel, readInt);
                    break;
                default:
                    zzbkw.zzb(parcel, readInt);
                    break;
            }
        }
        zzbkw.zzae(parcel, zza);
        return new Configurations(str, str2, configurationArr, z, bArr, j);
    }

    /* Return type fixed from 'java.lang.Object[]' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Configurations[] newArray(int i) {
        return new Configurations[i];
    }
}
