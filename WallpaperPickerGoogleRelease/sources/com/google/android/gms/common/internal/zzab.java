package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.zzf;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.zzfa;
import com.google.android.gms.internal.zzfb;
/* loaded from: classes.dex */
public abstract class zzab extends zzfa implements zzaa {
    public static final /* synthetic */ int $r8$clinit = 0;

    public zzab() {
        attachInterface(this, "com.google.android.gms.common.internal.ICertData");
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        if (i == 1) {
            IObjectWrapper zzb = ((zzf) this).zzb();
            parcel2.writeNoException();
            zzfb.zza(parcel2, zzb);
        } else if (i != 2) {
            return false;
        } else {
            int i3 = ((zzf) this).zza;
            parcel2.writeNoException();
            parcel2.writeInt(i3);
        }
        return true;
    }
}
