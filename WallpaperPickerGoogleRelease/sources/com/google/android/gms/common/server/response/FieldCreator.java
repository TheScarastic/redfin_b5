package com.google.android.gms.common.server.response;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.server.response.FastJsonResponse;
import com.google.android.gms.internal.zzbkw;
import com.google.android.gms.internal.zzbly;
/* loaded from: classes.dex */
public class FieldCreator implements Parcelable.Creator<FastJsonResponse.Field> {
    /* Return type fixed from 'java.lang.Object' to match base method */
    @Override // android.os.Parcelable.Creator
    public FastJsonResponse.Field createFromParcel(Parcel parcel) {
        int zza = zzbkw.zza(parcel);
        String str = null;
        int i = 0;
        String str2 = null;
        zzbly zzbly = null;
        int i2 = 0;
        boolean z = false;
        int i3 = 0;
        boolean z2 = false;
        int i4 = 0;
        while (parcel.dataPosition() < zza) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 1:
                    i = zzbkw.zzg(parcel, readInt);
                    break;
                case 2:
                    i2 = zzbkw.zzg(parcel, readInt);
                    break;
                case 3:
                    z = zzbkw.zzc(parcel, readInt);
                    break;
                case 4:
                    i3 = zzbkw.zzg(parcel, readInt);
                    break;
                case 5:
                    z2 = zzbkw.zzc(parcel, readInt);
                    break;
                case 6:
                    str = zzbkw.zzq(parcel, readInt);
                    break;
                case 7:
                    i4 = zzbkw.zzg(parcel, readInt);
                    break;
                case 8:
                    str2 = zzbkw.zzq(parcel, readInt);
                    break;
                case 9:
                    zzbly = (zzbly) zzbkw.zza(parcel, readInt, zzbly.CREATOR);
                    break;
                default:
                    zzbkw.zzb(parcel, readInt);
                    break;
            }
        }
        zzbkw.zzae(parcel, zza);
        return new FastJsonResponse.Field(i, i2, z, i3, z2, str, i4, str2, zzbly);
    }

    /* Return type fixed from 'java.lang.Object[]' to match base method */
    @Override // android.os.Parcelable.Creator
    public FastJsonResponse.Field[] newArray(int i) {
        return new FastJsonResponse.Field[i];
    }
}
