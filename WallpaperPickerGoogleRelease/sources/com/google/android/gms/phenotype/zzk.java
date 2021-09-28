package com.google.android.gms.phenotype;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.internal.zzbkw;
/* loaded from: classes.dex */
public final class zzk implements Parcelable.Creator<Flag> {
    /* Return type fixed from 'java.lang.Object' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Flag createFromParcel(Parcel parcel) {
        int zza = zzbkw.zza(parcel);
        boolean z = false;
        String str = null;
        long j = 0;
        double d = 0.0d;
        int i = 0;
        int i2 = 0;
        String str2 = null;
        byte[] bArr = null;
        while (parcel.dataPosition() < zza) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 2:
                    str = zzbkw.zzq(parcel, readInt);
                    break;
                case 3:
                    j = zzbkw.zzi(parcel, readInt);
                    break;
                case 4:
                    z = zzbkw.zzc(parcel, readInt);
                    break;
                case 5:
                    d = zzbkw.zzn(parcel, readInt);
                    break;
                case 6:
                    str2 = zzbkw.zzq(parcel, readInt);
                    break;
                case 7:
                    bArr = zzbkw.zzt(parcel, readInt);
                    break;
                case 8:
                    i = zzbkw.zzg(parcel, readInt);
                    break;
                case 9:
                    i2 = zzbkw.zzg(parcel, readInt);
                    break;
                default:
                    zzbkw.zzb(parcel, readInt);
                    break;
            }
        }
        zzbkw.zzae(parcel, zza);
        return new Flag(str, j, z, d, str2, bArr, i, i2);
    }

    /* Return type fixed from 'java.lang.Object[]' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Flag[] newArray(int i) {
        return new Flag[i];
    }
}
