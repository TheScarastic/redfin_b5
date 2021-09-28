package com.android.wm.shell.onehanded;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface IOneHanded extends IInterface {
    void startOneHanded() throws RemoteException;

    void stopOneHanded() throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IOneHanded {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.android.wm.shell.onehanded.IOneHanded");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString("com.android.wm.shell.onehanded.IOneHanded");
                return true;
            } else if (i == 2) {
                parcel.enforceInterface("com.android.wm.shell.onehanded.IOneHanded");
                startOneHanded();
                return true;
            } else if (i != 3) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel.enforceInterface("com.android.wm.shell.onehanded.IOneHanded");
                stopOneHanded();
                return true;
            }
        }
    }
}
