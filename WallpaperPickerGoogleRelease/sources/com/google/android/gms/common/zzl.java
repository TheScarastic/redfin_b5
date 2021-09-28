package com.google.android.gms.common;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import androidx.slice.view.R$id;
import com.google.android.gms.common.internal.zzaa;
import com.google.android.gms.common.internal.zzab;
import com.google.android.gms.common.internal.zzac;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.zzn;
import com.google.android.gms.internal.zzbkv;
import java.util.Objects;
/* loaded from: classes.dex */
public final class zzl extends zzbkv {
    public static final Parcelable.Creator<zzl> CREATOR = new zzm();
    public final String zza;
    public final zzf zzb;
    public final boolean zzc;

    public zzl(String str, IBinder iBinder, boolean z) {
        zzaa zzaa;
        byte[] bArr;
        this.zza = str;
        zzg zzg = null;
        if (iBinder != null) {
            try {
                int i = zzab.$r8$clinit;
                IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.ICertData");
                if (queryLocalInterface instanceof zzaa) {
                    zzaa = (zzaa) queryLocalInterface;
                } else {
                    zzaa = new zzac(iBinder);
                }
                IObjectWrapper zzb = zzaa.zzb();
                if (zzb == null) {
                    bArr = null;
                } else {
                    bArr = (byte[]) zzn.zza(zzb);
                }
                if (bArr != null) {
                    zzg = new zzg(bArr);
                } else {
                    Log.e("GoogleCertificatesQuery", "Could not unwrap certificate");
                }
            } catch (RemoteException e) {
                Log.e("GoogleCertificatesQuery", "Could not unwrap certificate", e);
            }
        }
        this.zzb = zzg;
        this.zzc = z;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int zzb = R$id.zzb(parcel, 20293);
        R$id.zza(parcel, 1, this.zza, false);
        zzf zzf = this.zzb;
        if (zzf == null) {
            Log.w("GoogleCertificatesQuery", "certificate binder is null");
            zzf = null;
        } else {
            Objects.requireNonNull(zzf);
        }
        R$id.zza(parcel, 2, zzf);
        boolean z = this.zzc;
        R$id.zzb(parcel, 3, 4);
        parcel.writeInt(z ? 1 : 0);
        R$id.zzc(parcel, zzb);
    }
}
