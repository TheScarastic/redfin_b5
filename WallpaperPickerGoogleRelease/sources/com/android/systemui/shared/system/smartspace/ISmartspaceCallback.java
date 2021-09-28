package com.android.systemui.shared.system.smartspace;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface ISmartspaceCallback extends IInterface {
    public static final String DESCRIPTOR = "com.android.systemui.shared.system.smartspace.ISmartspaceCallback";

    /* loaded from: classes.dex */
    public static class Default implements ISmartspaceCallback {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.android.systemui.shared.system.smartspace.ISmartspaceCallback
        public SmartspaceState getSmartspaceState() throws RemoteException {
            return null;
        }

        @Override // com.android.systemui.shared.system.smartspace.ISmartspaceCallback
        public void setSelectedPage(int i) throws RemoteException {
        }

        @Override // com.android.systemui.shared.system.smartspace.ISmartspaceCallback
        public void setVisibility(int i) throws RemoteException {
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ISmartspaceCallback {
        public static final int TRANSACTION_getSmartspaceState = 1;
        public static final int TRANSACTION_setSelectedPage = 2;
        public static final int TRANSACTION_setVisibility = 3;

        /* loaded from: classes.dex */
        public static class Proxy implements ISmartspaceCallback {
            public static ISmartspaceCallback sDefaultImpl;
            private IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ISmartspaceCallback.DESCRIPTOR;
            }

            @Override // com.android.systemui.shared.system.smartspace.ISmartspaceCallback
            public SmartspaceState getSmartspaceState() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISmartspaceCallback.DESCRIPTOR);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSmartspaceState();
                    }
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? SmartspaceState.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.system.smartspace.ISmartspaceCallback
            public void setSelectedPage(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISmartspaceCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(2, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSelectedPage(i);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.system.smartspace.ISmartspaceCallback
            public void setVisibility(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISmartspaceCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(3, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVisibility(i);
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, ISmartspaceCallback.DESCRIPTOR);
        }

        public static ISmartspaceCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(ISmartspaceCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ISmartspaceCallback)) {
                return new Proxy(iBinder);
            }
            return (ISmartspaceCallback) queryLocalInterface;
        }

        public static ISmartspaceCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(ISmartspaceCallback iSmartspaceCallback) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iSmartspaceCallback == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iSmartspaceCallback;
                return true;
            }
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString(ISmartspaceCallback.DESCRIPTOR);
                return true;
            } else if (i == 1) {
                parcel.enforceInterface(ISmartspaceCallback.DESCRIPTOR);
                SmartspaceState smartspaceState = getSmartspaceState();
                parcel2.writeNoException();
                if (smartspaceState != null) {
                    parcel2.writeInt(1);
                    smartspaceState.writeToParcel(parcel2, 1);
                } else {
                    parcel2.writeInt(0);
                }
                return true;
            } else if (i == 2) {
                parcel.enforceInterface(ISmartspaceCallback.DESCRIPTOR);
                setSelectedPage(parcel.readInt());
                return true;
            } else if (i != 3) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel.enforceInterface(ISmartspaceCallback.DESCRIPTOR);
                setVisibility(parcel.readInt());
                return true;
            }
        }
    }

    SmartspaceState getSmartspaceState() throws RemoteException;

    void setSelectedPage(int i) throws RemoteException;

    void setVisibility(int i) throws RemoteException;
}
