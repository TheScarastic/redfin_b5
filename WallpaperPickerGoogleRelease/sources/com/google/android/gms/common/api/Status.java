package com.google.android.gms.common.api;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.appcompat.R$string;
import androidx.core.R$id;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.internal.zzbkv;
import com.google.android.gms.internal.zzfit;
import java.util.Arrays;
/* loaded from: classes.dex */
public final class Status extends zzbkv implements Result, ReflectedParcelable {
    public final int zzh;
    public final int zzi;
    public final String zzj;
    public final PendingIntent zzk;
    public static final Status zza = new Status(0);
    public static final Status zzb = new Status(14);
    public static final Status zzc = new Status(8);
    public static final Status zzd = new Status(15);
    public static final Status zze = new Status(16);
    public static final Parcelable.Creator<Status> CREATOR = new zzf();

    static {
        new Status(17);
        new Status(18);
    }

    public Status(int i, int i2, String str, PendingIntent pendingIntent) {
        this.zzh = i;
        this.zzi = i2;
        this.zzj = str;
        this.zzk = pendingIntent;
    }

    @Override // java.lang.Object
    public final boolean equals(Object obj) {
        if (!(obj instanceof Status)) {
            return false;
        }
        Status status = (Status) obj;
        if (this.zzh != status.zzh || this.zzi != status.zzi || !R$id.zza(this.zzj, status.zzj) || !R$id.zza(this.zzk, status.zzk)) {
            return false;
        }
        return true;
    }

    @Override // com.google.android.gms.common.api.Result
    public final Status getStatus() {
        return this;
    }

    @Override // java.lang.Object
    public final int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.zzh), Integer.valueOf(this.zzi), this.zzj, this.zzk});
    }

    public final boolean isSuccess() {
        return this.zzi <= 0;
    }

    @Override // java.lang.Object
    public final String toString() {
        zzfit zza2 = R$id.zza(this);
        String str = this.zzj;
        if (str == null) {
            str = R$string.getStatusCodeString(this.zzi);
        }
        zza2.zza("statusCode", str);
        zza2.zza("resolution", this.zzk);
        return zza2.toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int zzb2 = androidx.slice.view.R$id.zzb(parcel, 20293);
        int i2 = this.zzi;
        androidx.slice.view.R$id.zzb(parcel, 1, 4);
        parcel.writeInt(i2);
        androidx.slice.view.R$id.zza(parcel, 2, this.zzj, false);
        androidx.slice.view.R$id.zza(parcel, 3, this.zzk, i, false);
        int i3 = this.zzh;
        androidx.slice.view.R$id.zzb(parcel, 1000, 4);
        parcel.writeInt(i3);
        androidx.slice.view.R$id.zzc(parcel, zzb2);
    }

    public Status(int i) {
        this(1, i, null, null);
    }

    public Status(int i, String str) {
        this(1, i, str, null);
    }
}
