package com.google.android.gms.location.places;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.R$id;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.internal.zzbkv;
import com.google.android.gms.internal.zzfit;
import java.util.Arrays;
/* loaded from: classes.dex */
public class PlaceReport extends zzbkv implements ReflectedParcelable {
    public static final Parcelable.Creator<PlaceReport> CREATOR = new zzm();
    public final int zza;
    public final String zzb;
    public final String zzc;
    public final String zzd;

    public PlaceReport(int i, String str, String str2, String str3) {
        this.zza = i;
        this.zzb = str;
        this.zzc = str2;
        this.zzd = str3;
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        if (!(obj instanceof PlaceReport)) {
            return false;
        }
        PlaceReport placeReport = (PlaceReport) obj;
        if (!R$id.zza(this.zzb, placeReport.zzb) || !R$id.zza(this.zzc, placeReport.zzc) || !R$id.zza(this.zzd, placeReport.zzd)) {
            return false;
        }
        return true;
    }

    @Override // java.lang.Object
    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.zzb, this.zzc, this.zzd});
    }

    @Override // java.lang.Object
    public String toString() {
        zzfit zza = R$id.zza(this);
        zza.zza("placeId", this.zzb);
        zza.zza("tag", this.zzc);
        if (!"unknown".equals(this.zzd)) {
            zza.zza("source", this.zzd);
        }
        return zza.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int zzb = androidx.slice.view.R$id.zzb(parcel, 20293);
        int i2 = this.zza;
        androidx.slice.view.R$id.zzb(parcel, 1, 4);
        parcel.writeInt(i2);
        androidx.slice.view.R$id.zza(parcel, 2, this.zzb, false);
        androidx.slice.view.R$id.zza(parcel, 3, this.zzc, false);
        androidx.slice.view.R$id.zza(parcel, 4, this.zzd, false);
        androidx.slice.view.R$id.zzc(parcel, zzb);
    }
}
