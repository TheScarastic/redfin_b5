package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.internal.zzbkw;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class zzd implements Parcelable.Creator<WakeLockEvent> {
    /* Return type fixed from 'java.lang.Object' to match base method */
    @Override // android.os.Parcelable.Creator
    public final WakeLockEvent createFromParcel(Parcel parcel) {
        int zza = zzbkw.zza(parcel);
        long j = 0;
        int i = 0;
        float f = 0.0f;
        long j2 = 0;
        long j3 = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        String str = null;
        ArrayList<String> arrayList = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        while (parcel.dataPosition() < zza) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 1:
                    i = zzbkw.zzg(parcel, readInt);
                    break;
                case 2:
                    j = zzbkw.zzi(parcel, readInt);
                    break;
                case 3:
                case 7:
                case 9:
                default:
                    zzbkw.zzb(parcel, readInt);
                    break;
                case 4:
                    str = zzbkw.zzq(parcel, readInt);
                    break;
                case 5:
                    i3 = zzbkw.zzg(parcel, readInt);
                    break;
                case 6:
                    int zza2 = zzbkw.zza(parcel, readInt);
                    int dataPosition = parcel.dataPosition();
                    if (zza2 != 0) {
                        ArrayList<String> createStringArrayList = parcel.createStringArrayList();
                        parcel.setDataPosition(dataPosition + zza2);
                        arrayList = createStringArrayList;
                        break;
                    } else {
                        arrayList = null;
                        break;
                    }
                case 8:
                    j2 = zzbkw.zzi(parcel, readInt);
                    break;
                case 10:
                    str3 = zzbkw.zzq(parcel, readInt);
                    break;
                case 11:
                    i2 = zzbkw.zzg(parcel, readInt);
                    break;
                case 12:
                    str2 = zzbkw.zzq(parcel, readInt);
                    break;
                case 13:
                    str4 = zzbkw.zzq(parcel, readInt);
                    break;
                case 14:
                    i4 = zzbkw.zzg(parcel, readInt);
                    break;
                case 15:
                    f = zzbkw.zzl(parcel, readInt);
                    break;
                case 16:
                    j3 = zzbkw.zzi(parcel, readInt);
                    break;
                case 17:
                    str5 = zzbkw.zzq(parcel, readInt);
                    break;
            }
        }
        zzbkw.zzae(parcel, zza);
        return new WakeLockEvent(i, j, i2, str, i3, arrayList, str2, j2, i4, str3, str4, f, j3, str5);
    }

    /* Return type fixed from 'java.lang.Object[]' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ WakeLockEvent[] newArray(int i) {
        return new WakeLockEvent[i];
    }
}
