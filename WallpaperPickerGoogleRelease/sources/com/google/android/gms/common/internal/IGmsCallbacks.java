package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.Handler;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import androidx.preference.R$layout;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.internal.zzfa;
import com.google.android.gms.internal.zzfb;
/* loaded from: classes.dex */
public interface IGmsCallbacks extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class zza extends zzfa implements IGmsCallbacks {
        public zza() {
            attachInterface(this, "com.google.android.gms.common.internal.IGmsCallbacks");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (zza(i, parcel, parcel2, i2)) {
                return true;
            }
            if (i == 1) {
                BaseGmsClient.zzd zzd = (BaseGmsClient.zzd) this;
                R$layout.zza(zzd.zza, "onPostInitComplete can be called only once per call to getRemoteService");
                BaseGmsClient baseGmsClient = zzd.zza;
                int i3 = zzd.zzb;
                Handler handler = baseGmsClient.zza;
                handler.sendMessage(handler.obtainMessage(1, i3, -1, new BaseGmsClient.zzg(parcel.readInt(), parcel.readStrongBinder(), (Bundle) zzfb.zza(parcel, Bundle.CREATOR))));
                zzd.zza = null;
            } else if (i != 2) {
                return false;
            } else {
                parcel.readInt();
                Bundle bundle = (Bundle) zzfb.zza(parcel, Bundle.CREATOR);
                Log.wtf("GmsClient", "received deprecated onAccountValidationComplete callback, ignoring", new Exception());
            }
            parcel2.writeNoException();
            return true;
        }
    }
}
