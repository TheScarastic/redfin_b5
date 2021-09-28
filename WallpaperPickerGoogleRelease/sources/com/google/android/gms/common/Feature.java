package com.google.android.gms.common;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.slice.view.R$id;
import com.google.android.gms.internal.zzbkv;
/* loaded from: classes.dex */
public class Feature extends zzbkv {
    public static final Parcelable.Creator<Feature> CREATOR = new zzc();
    public final String name;
    public final int version;

    public Feature(String str, int i) {
        this.name = str;
        this.version = i;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int zzb = R$id.zzb(parcel, 20293);
        R$id.zza(parcel, 1, this.name, false);
        int i2 = this.version;
        R$id.zzb(parcel, 2, 4);
        parcel.writeInt(i2);
        R$id.zzc(parcel, zzb);
    }
}
