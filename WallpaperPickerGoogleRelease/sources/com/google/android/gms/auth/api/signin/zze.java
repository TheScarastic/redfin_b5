package com.google.android.gms.auth.api.signin;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.auth.api.signin.internal.zzo;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.internal.zzbkw;
import java.util.ArrayList;
import java.util.HashMap;
/* loaded from: classes.dex */
public final class zze implements Parcelable.Creator<GoogleSignInOptions> {
    /* Return type fixed from 'java.lang.Object' to match base method */
    @Override // android.os.Parcelable.Creator
    public final GoogleSignInOptions createFromParcel(Parcel parcel) {
        int zza = zzbkw.zza(parcel);
        int i = 0;
        ArrayList<zzo> arrayList = null;
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        ArrayList arrayList2 = null;
        Account account = null;
        String str = null;
        String str2 = null;
        while (parcel.dataPosition() < zza) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 1:
                    i = zzbkw.zzg(parcel, readInt);
                    break;
                case 2:
                    arrayList2 = zzbkw.zzc(parcel, readInt, Scope.CREATOR);
                    break;
                case 3:
                    account = (Account) zzbkw.zza(parcel, readInt, Account.CREATOR);
                    break;
                case 4:
                    z = zzbkw.zzc(parcel, readInt);
                    break;
                case 5:
                    z2 = zzbkw.zzc(parcel, readInt);
                    break;
                case 6:
                    z3 = zzbkw.zzc(parcel, readInt);
                    break;
                case 7:
                    str = zzbkw.zzq(parcel, readInt);
                    break;
                case 8:
                    str2 = zzbkw.zzq(parcel, readInt);
                    break;
                case 9:
                    arrayList = zzbkw.zzc(parcel, readInt, zzo.CREATOR);
                    break;
                default:
                    zzbkw.zzb(parcel, readInt);
                    break;
            }
        }
        zzbkw.zzae(parcel, zza);
        HashMap hashMap = new HashMap();
        if (arrayList != null) {
            for (zzo zzo : arrayList) {
                hashMap.put(Integer.valueOf(zzo.zzb), zzo);
            }
        }
        return new GoogleSignInOptions(i, arrayList2, account, z, z2, z3, str, str2, hashMap);
    }

    /* Return type fixed from 'java.lang.Object[]' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ GoogleSignInOptions[] newArray(int i) {
        return new GoogleSignInOptions[i];
    }
}
