package com.google.android.gms.common.data;

import android.database.CursorWindow;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.internal.zzbkw;
/* loaded from: classes.dex */
public final class zzf implements Parcelable.Creator<DataHolder> {
    /* Return type fixed from 'java.lang.Object' to match base method */
    @Override // android.os.Parcelable.Creator
    public final DataHolder createFromParcel(Parcel parcel) {
        int zza = zzbkw.zza(parcel);
        String[] strArr = null;
        int i = 0;
        CursorWindow[] cursorWindowArr = null;
        Bundle bundle = null;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < zza) {
            int readInt = parcel.readInt();
            int i4 = 65535 & readInt;
            if (i4 == 1) {
                strArr = zzbkw.zzaa(parcel, readInt);
            } else if (i4 == 2) {
                cursorWindowArr = (CursorWindow[]) zzbkw.zzb(parcel, readInt, CursorWindow.CREATOR);
            } else if (i4 == 3) {
                i3 = zzbkw.zzg(parcel, readInt);
            } else if (i4 == 4) {
                bundle = zzbkw.zzs(parcel, readInt);
            } else if (i4 != 1000) {
                zzbkw.zzb(parcel, readInt);
            } else {
                i2 = zzbkw.zzg(parcel, readInt);
            }
        }
        zzbkw.zzae(parcel, zza);
        DataHolder dataHolder = new DataHolder(i2, strArr, cursorWindowArr, i3, bundle);
        dataHolder.zzc = new Bundle();
        int i5 = 0;
        while (true) {
            String[] strArr2 = dataHolder.zzb;
            if (i5 >= strArr2.length) {
                break;
            }
            dataHolder.zzc.putInt(strArr2[i5], i5);
            i5++;
        }
        dataHolder.zzg = new int[dataHolder.zzd.length];
        int i6 = 0;
        while (true) {
            CursorWindow[] cursorWindowArr2 = dataHolder.zzd;
            if (i >= cursorWindowArr2.length) {
                return dataHolder;
            }
            dataHolder.zzg[i] = i6;
            i6 += dataHolder.zzd[i].getNumRows() - (i6 - cursorWindowArr2[i].getStartPosition());
            i++;
        }
    }

    /* Return type fixed from 'java.lang.Object[]' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ DataHolder[] newArray(int i) {
        return new DataHolder[i];
    }
}
