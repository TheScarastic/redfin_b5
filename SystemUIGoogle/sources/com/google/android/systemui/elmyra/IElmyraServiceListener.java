package com.google.android.systemui.elmyra;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface IElmyraServiceListener extends IInterface {
    void setListener(IBinder iBinder, IBinder iBinder2) throws RemoteException;

    void triggerAction() throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IElmyraServiceListener {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.google.android.systemui.elmyra.IElmyraServiceListener");
        }

        public static IElmyraServiceListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.systemui.elmyra.IElmyraServiceListener");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IElmyraServiceListener)) {
                return new Proxy(iBinder);
            }
            return (IElmyraServiceListener) queryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString("com.google.android.systemui.elmyra.IElmyraServiceListener");
                return true;
            } else if (i == 1) {
                parcel.enforceInterface("com.google.android.systemui.elmyra.IElmyraServiceListener");
                setListener(parcel.readStrongBinder(), parcel.readStrongBinder());
                return true;
            } else if (i != 2) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel.enforceInterface("com.google.android.systemui.elmyra.IElmyraServiceListener");
                triggerAction();
                return true;
            }
        }

        /* access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IElmyraServiceListener {
            public static IElmyraServiceListener sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.google.android.systemui.elmyra.IElmyraServiceListener
            public void setListener(IBinder iBinder, IBinder iBinder2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.systemui.elmyra.IElmyraServiceListener");
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeStrongBinder(iBinder2);
                    if (!this.mRemote.transact(1, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setListener(iBinder, iBinder2);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.google.android.systemui.elmyra.IElmyraServiceListener
            public void triggerAction() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.systemui.elmyra.IElmyraServiceListener");
                    if (!this.mRemote.transact(2, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().triggerAction();
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static IElmyraServiceListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
