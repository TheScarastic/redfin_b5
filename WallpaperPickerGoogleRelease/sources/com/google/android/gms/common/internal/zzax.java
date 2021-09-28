package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.slice.view.R$id;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.internal.zzbkv;
/* loaded from: classes.dex */
public final class zzax extends zzbkv {
    public static final Parcelable.Creator<zzax> CREATOR = new zzay();
    public final int zza;
    public IBinder zzb;
    public ConnectionResult zzc;
    public boolean zzd;
    public boolean zze;

    public zzax(int i, IBinder iBinder, ConnectionResult connectionResult, boolean z, boolean z2) {
        this.zza = i;
        this.zzb = iBinder;
        this.zzc = connectionResult;
        this.zzd = z;
        this.zze = z2;
    }

    @Override // java.lang.Object
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzax)) {
            return false;
        }
        zzax zzax = (zzax) obj;
        return this.zzc.equals(zzax.zzc) && zza().equals(zzax.zza());
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int zzb = R$id.zzb(parcel, 20293);
        int i2 = this.zza;
        R$id.zzb(parcel, 1, 4);
        parcel.writeInt(i2);
        R$id.zza(parcel, 2, this.zzb);
        R$id.zza(parcel, 3, this.zzc, i, false);
        boolean z = this.zzd;
        R$id.zzb(parcel, 4, 4);
        parcel.writeInt(z ? 1 : 0);
        boolean z2 = this.zze;
        R$id.zzb(parcel, 5, 4);
        parcel.writeInt(z2 ? 1 : 0);
        R$id.zzc(parcel, zzb);
    }

    public final IAccountAccessor zza() {
        IBinder iBinder = this.zzb;
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.IAccountAccessor");
        if (queryLocalInterface instanceof IAccountAccessor) {
            return (IAccountAccessor) queryLocalInterface;
        }
        return new zzw(iBinder);
    }
}
