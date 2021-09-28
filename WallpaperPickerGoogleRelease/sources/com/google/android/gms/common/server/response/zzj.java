package com.google.android.gms.common.server.response;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.server.response.FieldMappingDictionary;
import com.google.android.gms.internal.zzbkw;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class zzj implements Parcelable.Creator<FieldMappingDictionary> {
    /* Return type fixed from 'java.lang.Object' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ FieldMappingDictionary createFromParcel(Parcel parcel) {
        int zza = zzbkw.zza(parcel);
        ArrayList arrayList = null;
        int i = 0;
        String str = null;
        while (parcel.dataPosition() < zza) {
            int readInt = parcel.readInt();
            int i2 = 65535 & readInt;
            if (i2 == 1) {
                i = zzbkw.zzg(parcel, readInt);
            } else if (i2 == 2) {
                arrayList = zzbkw.zzc(parcel, readInt, FieldMappingDictionary.Entry.CREATOR);
            } else if (i2 != 3) {
                zzbkw.zzb(parcel, readInt);
            } else {
                str = zzbkw.zzq(parcel, readInt);
            }
        }
        zzbkw.zzae(parcel, zza);
        return new FieldMappingDictionary(i, arrayList, str);
    }

    /* Return type fixed from 'java.lang.Object[]' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ FieldMappingDictionary[] newArray(int i) {
        return new FieldMappingDictionary[i];
    }
}
