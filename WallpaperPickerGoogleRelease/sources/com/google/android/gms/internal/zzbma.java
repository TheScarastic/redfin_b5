package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import androidx.slice.view.R$id;
import com.google.android.gms.common.server.response.FastJsonResponse;
import java.util.ArrayList;
import java.util.HashMap;
/* loaded from: classes.dex */
public final class zzbma extends zzbkv implements FastJsonResponse.FieldConverter<String, Integer> {
    public static final Parcelable.Creator<zzbma> CREATOR = new zzbmc();
    public final int zza;
    public final HashMap<String, Integer> zzb;
    public final SparseArray<String> zzc;

    public zzbma(int i, ArrayList<zzbmb> arrayList) {
        this.zza = i;
        this.zzb = new HashMap<>();
        this.zzc = new SparseArray<>();
        int size = arrayList.size();
        int i2 = 0;
        while (i2 < size) {
            zzbmb zzbmb = arrayList.get(i2);
            i2++;
            zzbmb zzbmb2 = zzbmb;
            String str = zzbmb2.zza;
            int i3 = zzbmb2.zzb;
            this.zzb.put(str, Integer.valueOf(i3));
            this.zzc.put(i3, str);
        }
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int zzb = R$id.zzb(parcel, 20293);
        int i2 = this.zza;
        R$id.zzb(parcel, 1, 4);
        parcel.writeInt(i2);
        ArrayList arrayList = new ArrayList();
        for (String str : this.zzb.keySet()) {
            arrayList.add(new zzbmb(str, this.zzb.get(str).intValue()));
        }
        R$id.zzc(parcel, 2, arrayList, false);
        R$id.zzc(parcel, zzb);
    }

    public zzbma() {
        this.zza = 1;
        this.zzb = new HashMap<>();
        this.zzc = new SparseArray<>();
    }
}
