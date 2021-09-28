package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
/* loaded from: classes.dex */
public abstract class zzelr extends zzfa implements zzelq {
    public zzelr() {
        attachInterface(this, "com.google.android.gms.signin.internal.ISignInCallbacks");
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        if (i == 3) {
            ConnectionResult connectionResult = (ConnectionResult) zzfb.zza(parcel, ConnectionResult.CREATOR);
            zzeln zzeln = (zzeln) zzfb.zza(parcel, zzeln.CREATOR);
        } else if (i == 4) {
            Status status = (Status) zzfb.zza(parcel, Status.CREATOR);
        } else if (i == 6) {
            Status status2 = (Status) zzfb.zza(parcel, Status.CREATOR);
        } else if (i == 7) {
            Status status3 = (Status) zzfb.zza(parcel, Status.CREATOR);
            GoogleSignInAccount googleSignInAccount = (GoogleSignInAccount) zzfb.zza(parcel, GoogleSignInAccount.CREATOR);
        } else if (i != 8) {
            return false;
        } else {
            zza((zzelx) zzfb.zza(parcel, zzelx.CREATOR));
        }
        parcel2.writeNoException();
        return true;
    }
}
