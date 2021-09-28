package com.google.android.gms.common.api;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import androidx.slice.view.R$id;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.internal.zzbkv;
/* loaded from: classes.dex */
public final class Scope extends zzbkv implements ReflectedParcelable {
    public static final Parcelable.Creator<Scope> CREATOR = new zze();
    public final int zza;
    public final String zzb;

    public Scope(int i, String str) {
        if (!TextUtils.isEmpty(str)) {
            this.zza = i;
            this.zzb = str;
            return;
        }
        throw new IllegalArgumentException("scopeUri must not be null or empty");
    }

    @Override // java.lang.Object
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Scope)) {
            return false;
        }
        return this.zzb.equals(((Scope) obj).zzb);
    }

    @Override // java.lang.Object
    public final int hashCode() {
        return this.zzb.hashCode();
    }

    @Override // java.lang.Object
    public final String toString() {
        return this.zzb;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int zzb = R$id.zzb(parcel, 20293);
        int i2 = this.zza;
        R$id.zzb(parcel, 1, 4);
        parcel.writeInt(i2);
        R$id.zza(parcel, 2, this.zzb, false);
        R$id.zzc(parcel, zzb);
    }
}
