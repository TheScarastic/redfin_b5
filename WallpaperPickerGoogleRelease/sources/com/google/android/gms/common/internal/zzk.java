package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.internal.zzbkw;
/* loaded from: classes.dex */
public final class zzk implements Parcelable.Creator<GetServiceRequest> {
    /* Return type fixed from 'java.lang.Object' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ GetServiceRequest createFromParcel(Parcel parcel) {
        int zza = zzbkw.zza(parcel);
        int i = 0;
        String str = null;
        int i2 = 0;
        int i3 = 0;
        IBinder iBinder = null;
        Scope[] scopeArr = null;
        Bundle bundle = null;
        Account account = null;
        Feature[] featureArr = null;
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
                    i3 = zzbkw.zzg(parcel, readInt);
                    break;
                case 4:
                    str = zzbkw.zzq(parcel, readInt);
                    break;
                case 5:
                    iBinder = zzbkw.zzr(parcel, readInt);
                    break;
                case 6:
                    scopeArr = (Scope[]) zzbkw.zzb(parcel, readInt, Scope.CREATOR);
                    break;
                case 7:
                    bundle = zzbkw.zzs(parcel, readInt);
                    break;
                case 8:
                    account = (Account) zzbkw.zza(parcel, readInt, Account.CREATOR);
                    break;
                case 9:
                default:
                    zzbkw.zzb(parcel, readInt);
                    break;
                case 10:
                    featureArr = (Feature[]) zzbkw.zzb(parcel, readInt, Feature.CREATOR);
                    break;
            }
        }
        zzbkw.zzae(parcel, zza);
        return new GetServiceRequest(i, i2, i3, str, iBinder, scopeArr, bundle, account, featureArr);
    }

    /* Return type fixed from 'java.lang.Object[]' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ GetServiceRequest[] newArray(int i) {
        return new GetServiceRequest[i];
    }
}
