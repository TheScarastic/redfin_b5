package com.google.android.gms.auth.api.signin;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.internal.zzbkw;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class zzb implements Parcelable.Creator<GoogleSignInAccount> {
    /* Return type fixed from 'java.lang.Object' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ GoogleSignInAccount createFromParcel(Parcel parcel) {
        int zza = zzbkw.zza(parcel);
        String str = null;
        int i = 0;
        long j = 0;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        Uri uri = null;
        String str5 = null;
        String str6 = null;
        ArrayList arrayList = null;
        String str7 = null;
        String str8 = null;
        while (parcel.dataPosition() < zza) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 1:
                    i = zzbkw.zzg(parcel, readInt);
                    break;
                case 2:
                    str = zzbkw.zzq(parcel, readInt);
                    break;
                case 3:
                    str2 = zzbkw.zzq(parcel, readInt);
                    break;
                case 4:
                    str3 = zzbkw.zzq(parcel, readInt);
                    break;
                case 5:
                    str4 = zzbkw.zzq(parcel, readInt);
                    break;
                case 6:
                    uri = (Uri) zzbkw.zza(parcel, readInt, Uri.CREATOR);
                    break;
                case 7:
                    str5 = zzbkw.zzq(parcel, readInt);
                    break;
                case 8:
                    j = zzbkw.zzi(parcel, readInt);
                    break;
                case 9:
                    str6 = zzbkw.zzq(parcel, readInt);
                    break;
                case 10:
                    arrayList = zzbkw.zzc(parcel, readInt, Scope.CREATOR);
                    break;
                case 11:
                    str7 = zzbkw.zzq(parcel, readInt);
                    break;
                case 12:
                    str8 = zzbkw.zzq(parcel, readInt);
                    break;
                default:
                    zzbkw.zzb(parcel, readInt);
                    break;
            }
        }
        zzbkw.zzae(parcel, zza);
        return new GoogleSignInAccount(i, str, str2, str3, str4, uri, str5, j, str6, arrayList, str7, str8);
    }

    /* Return type fixed from 'java.lang.Object[]' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ GoogleSignInAccount[] newArray(int i) {
        return new GoogleSignInAccount[i];
    }
}
