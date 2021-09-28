package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.internal.zzbkw;
/* loaded from: classes.dex */
public final class zzba implements Parcelable.Creator<zzaz> {
    /* Return type fixed from 'java.lang.Object' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzaz createFromParcel(Parcel parcel) {
        int zza = zzbkw.zza(parcel);
        int i = 0;
        Scope[] scopeArr = null;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < zza) {
            int readInt = parcel.readInt();
            int i4 = 65535 & readInt;
            if (i4 == 1) {
                i = zzbkw.zzg(parcel, readInt);
            } else if (i4 == 2) {
                i3 = zzbkw.zzg(parcel, readInt);
            } else if (i4 == 3) {
                i2 = zzbkw.zzg(parcel, readInt);
            } else if (i4 != 4) {
                zzbkw.zzb(parcel, readInt);
            } else {
                scopeArr = (Scope[]) zzbkw.zzb(parcel, readInt, Scope.CREATOR);
            }
        }
        zzbkw.zzae(parcel, zza);
        return new zzaz(i, i3, i2, scopeArr);
    }

    /* Return type fixed from 'java.lang.Object[]' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzaz[] newArray(int i) {
        return new zzaz[i];
    }
}
