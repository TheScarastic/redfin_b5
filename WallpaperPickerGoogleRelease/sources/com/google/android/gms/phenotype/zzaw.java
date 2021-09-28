package com.google.android.gms.phenotype;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.internal.zzbkw;
/* loaded from: classes.dex */
public final class zzaw implements Parcelable.Creator<RegistrationInfo> {
    /* Return type fixed from 'java.lang.Object' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ RegistrationInfo createFromParcel(Parcel parcel) {
        int zza = zzbkw.zza(parcel);
        int i = 0;
        String str = null;
        boolean z = false;
        String[] strArr = null;
        byte[] bArr = null;
        int[] iArr = null;
        String str2 = null;
        while (parcel.dataPosition() < zza) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 1:
                    str = zzbkw.zzq(parcel, readInt);
                    break;
                case 2:
                    i = zzbkw.zzg(parcel, readInt);
                    break;
                case 3:
                    strArr = zzbkw.zzaa(parcel, readInt);
                    break;
                case 4:
                    bArr = zzbkw.zzt(parcel, readInt);
                    break;
                case 5:
                    z = zzbkw.zzc(parcel, readInt);
                    break;
                case 6:
                    iArr = zzbkw.zzw(parcel, readInt);
                    break;
                case 7:
                    str2 = zzbkw.zzq(parcel, readInt);
                    break;
                default:
                    zzbkw.zzb(parcel, readInt);
                    break;
            }
        }
        zzbkw.zzae(parcel, zza);
        return new RegistrationInfo(str, i, strArr, bArr, z, iArr, str2);
    }

    /* Return type fixed from 'java.lang.Object[]' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ RegistrationInfo[] newArray(int i) {
        return new RegistrationInfo[i];
    }
}
