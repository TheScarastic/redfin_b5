package com.google.android.gms.phenotype;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.slice.view.R$id;
import com.google.android.gms.internal.zzbkv;
/* loaded from: classes.dex */
public class DogfoodsToken extends zzbkv {
    public static final Parcelable.Creator<DogfoodsToken> CREATOR = new zzd();
    public final byte[] token;

    public DogfoodsToken(byte[] bArr) {
        this.token = bArr;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int zzb = R$id.zzb(parcel, 20293);
        R$id.zza(parcel, 2, this.token, false);
        R$id.zzc(parcel, zzb);
    }
}
