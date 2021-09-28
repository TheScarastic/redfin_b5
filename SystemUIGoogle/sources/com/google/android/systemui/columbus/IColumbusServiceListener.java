package com.google.android.systemui.columbus;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface IColumbusServiceListener extends IInterface {
    void setListener(IBinder iBinder, IBinder iBinder2) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IColumbusServiceListener {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.google.android.systemui.columbus.IColumbusServiceListener");
        }

        public static IColumbusServiceListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.systemui.columbus.IColumbusServiceListener");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IColumbusServiceListener)) {
                return new Proxy(iBinder);
            }
            return (IColumbusServiceListener) queryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString("com.google.android.systemui.columbus.IColumbusServiceListener");
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel.enforceInterface("com.google.android.systemui.columbus.IColumbusServiceListener");
                setListener(parcel.readStrongBinder(), parcel.readStrongBinder());
                return true;
            }
        }

        /* access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IColumbusServiceListener {
            public static IColumbusServiceListener sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.google.android.systemui.columbus.IColumbusServiceListener
            public void setListener(IBinder iBinder, IBinder iBinder2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.systemui.columbus.IColumbusServiceListener");
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeStrongBinder(iBinder2);
                    if (!this.mRemote.transact(1, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setListener(iBinder, iBinder2);
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static IColumbusServiceListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
