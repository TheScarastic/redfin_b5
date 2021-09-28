package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public final class zzelt extends zzez implements zzels {
    public zzelt(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.signin.internal.ISignInService");
    }

    @Override // com.google.android.gms.internal.zzels
    public final void zza(zzelv zzelv, zzelq zzelq) throws RemoteException {
        Parcel a_ = a_();
        int i = zzfb.$r8$clinit;
        a_.writeInt(1);
        zzelv.writeToParcel(a_, 0);
        zzfb.zza(a_, zzelq);
        zzb(12, a_);
    }
}
