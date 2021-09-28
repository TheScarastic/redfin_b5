package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.zzm;
import com.google.android.gms.internal.zzez;
/* loaded from: classes.dex */
public final class zzac extends zzez implements zzaa {
    public zzac(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.internal.ICertData");
    }

    @Override // com.google.android.gms.common.internal.zzaa
    public final IObjectWrapper zzb() throws RemoteException {
        IObjectWrapper iObjectWrapper;
        Parcel zza = zza(1, a_());
        IBinder readStrongBinder = zza.readStrongBinder();
        int i = IObjectWrapper.zza.$r8$clinit;
        if (readStrongBinder == null) {
            iObjectWrapper = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.dynamic.IObjectWrapper");
            if (queryLocalInterface instanceof IObjectWrapper) {
                iObjectWrapper = (IObjectWrapper) queryLocalInterface;
            } else {
                iObjectWrapper = new zzm(readStrongBinder);
            }
        }
        zza.recycle();
        return iObjectWrapper;
    }

    @Override // com.google.android.gms.common.internal.zzaa
    public final int zzc() throws RemoteException {
        Parcel zza = zza(2, a_());
        int readInt = zza.readInt();
        zza.recycle();
        return readInt;
    }
}
