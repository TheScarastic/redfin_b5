package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.internal.zzbkw;
/* loaded from: classes.dex */
public final class zzay implements Parcelable.Creator<zzax> {
    /* Return type fixed from 'java.lang.Object' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzax createFromParcel(Parcel parcel) {
        int zza = zzbkw.zza(parcel);
        IBinder iBinder = null;
        int i = 0;
        ConnectionResult connectionResult = null;
        boolean z = false;
        boolean z2 = false;
        while (parcel.dataPosition() < zza) {
            int readInt = parcel.readInt();
            int i2 = 65535 & readInt;
            if (i2 == 1) {
                i = zzbkw.zzg(parcel, readInt);
            } else if (i2 == 2) {
                iBinder = zzbkw.zzr(parcel, readInt);
            } else if (i2 == 3) {
                connectionResult = (ConnectionResult) zzbkw.zza(parcel, readInt, ConnectionResult.CREATOR);
            } else if (i2 == 4) {
                z = zzbkw.zzc(parcel, readInt);
            } else if (i2 != 5) {
                zzbkw.zzb(parcel, readInt);
            } else {
                z2 = zzbkw.zzc(parcel, readInt);
            }
        }
        zzbkw.zzae(parcel, zza);
        return new zzax(i, iBinder, connectionResult, z, z2);
    }

    /* Return type fixed from 'java.lang.Object[]' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzax[] newArray(int i) {
        return new zzax[i];
    }
}
