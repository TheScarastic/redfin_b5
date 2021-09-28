package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.slice.view.R$id;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.internal.zzbkv;
/* loaded from: classes.dex */
public final class zzav extends zzbkv {
    public static final Parcelable.Creator<zzav> CREATOR = new zzaw();
    public final int zza;
    public final Account zzb;
    public final int zzc;
    public final GoogleSignInAccount zzd;

    public zzav(int i, Account account, int i2, GoogleSignInAccount googleSignInAccount) {
        this.zza = i;
        this.zzb = account;
        this.zzc = i2;
        this.zzd = googleSignInAccount;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int zzb = R$id.zzb(parcel, 20293);
        int i2 = this.zza;
        R$id.zzb(parcel, 1, 4);
        parcel.writeInt(i2);
        R$id.zza(parcel, 2, this.zzb, i, false);
        int i3 = this.zzc;
        R$id.zzb(parcel, 3, 4);
        parcel.writeInt(i3);
        R$id.zza(parcel, 4, this.zzd, i, false);
        R$id.zzc(parcel, zzb);
    }

    public zzav(Account account, int i, GoogleSignInAccount googleSignInAccount) {
        this.zza = 2;
        this.zzb = account;
        this.zzc = i;
        this.zzd = googleSignInAccount;
    }
}
