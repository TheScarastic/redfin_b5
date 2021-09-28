package com.google.android.gms.gcm;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzez;
import com.google.android.gms.internal.zzfa;
/* loaded from: classes.dex */
public interface INetworkTaskCallback extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class Stub extends zzfa implements INetworkTaskCallback {
        public static final /* synthetic */ int $r8$clinit = 0;

        /* loaded from: classes.dex */
        public static class Proxy extends zzez implements INetworkTaskCallback {
            public Proxy(IBinder iBinder) {
                super(iBinder, "com.google.android.gms.gcm.INetworkTaskCallback");
            }

            @Override // com.google.android.gms.gcm.INetworkTaskCallback
            public void taskFinished(int i) throws RemoteException {
                Parcel a_ = a_();
                a_.writeInt(i);
                zzb(2, a_);
            }
        }
    }

    void taskFinished(int i) throws RemoteException;
}
