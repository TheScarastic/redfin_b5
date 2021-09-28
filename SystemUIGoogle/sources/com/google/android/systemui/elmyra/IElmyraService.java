package com.google.android.systemui.elmyra;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface IElmyraService extends IInterface {
    void registerGestureListener(IBinder iBinder, IBinder iBinder2) throws RemoteException;

    void registerServiceListener(IBinder iBinder, IBinder iBinder2) throws RemoteException;

    void triggerAction() throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IElmyraService {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.google.android.systemui.elmyra.IElmyraService");
        }

        public static IElmyraService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.systemui.elmyra.IElmyraService");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IElmyraService)) {
                return new Proxy(iBinder);
            }
            return (IElmyraService) queryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString("com.google.android.systemui.elmyra.IElmyraService");
                return true;
            } else if (i == 1) {
                parcel.enforceInterface("com.google.android.systemui.elmyra.IElmyraService");
                registerGestureListener(parcel.readStrongBinder(), parcel.readStrongBinder());
                return true;
            } else if (i == 2) {
                parcel.enforceInterface("com.google.android.systemui.elmyra.IElmyraService");
                triggerAction();
                return true;
            } else if (i != 3) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel.enforceInterface("com.google.android.systemui.elmyra.IElmyraService");
                registerServiceListener(parcel.readStrongBinder(), parcel.readStrongBinder());
                return true;
            }
        }

        /* access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IElmyraService {
            public static IElmyraService sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.google.android.systemui.elmyra.IElmyraService
            public void registerServiceListener(IBinder iBinder, IBinder iBinder2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.systemui.elmyra.IElmyraService");
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeStrongBinder(iBinder2);
                    if (!this.mRemote.transact(3, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerServiceListener(iBinder, iBinder2);
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static IElmyraService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
