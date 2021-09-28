package com.google.android.gms.phenotype;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.internal.zzbkw;
/* loaded from: classes.dex */
public final class zzd implements Parcelable.Creator<DogfoodsToken> {
    /* Return type fixed from 'java.lang.Object' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ DogfoodsToken createFromParcel(Parcel parcel) {
        int zza = zzbkw.zza(parcel);
        byte[] bArr = null;
        while (parcel.dataPosition() < zza) {
            int readInt = parcel.readInt();
            if ((65535 & readInt) != 2) {
                zzbkw.zzb(parcel, readInt);
            } else {
                bArr = zzbkw.zzt(parcel, readInt);
            }
        }
        zzbkw.zzae(parcel, zza);
        return new DogfoodsToken(bArr);
    }

    /* Return type fixed from 'java.lang.Object[]' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ DogfoodsToken[] newArray(int i) {
        return new DogfoodsToken[i];
    }
}
