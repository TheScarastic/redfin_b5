package com.android.systemui.shared.system.smartspace;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.systemui.shared.system.smartspace.ISmartspaceCallback;
/* loaded from: classes.dex */
public interface ISmartspaceTransitionController extends IInterface {
    public static final String DESCRIPTOR = "com.android.systemui.shared.system.smartspace.ISmartspaceTransitionController";

    /* loaded from: classes.dex */
    public static class Default implements ISmartspaceTransitionController {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.android.systemui.shared.system.smartspace.ISmartspaceTransitionController
        public void setSmartspace(ISmartspaceCallback iSmartspaceCallback) throws RemoteException {
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ISmartspaceTransitionController {
        public static final int TRANSACTION_setSmartspace = 1;

        /* loaded from: classes.dex */
        public static class Proxy implements ISmartspaceTransitionController {
            public static ISmartspaceTransitionController sDefaultImpl;
            private IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ISmartspaceTransitionController.DESCRIPTOR;
            }

            @Override // com.android.systemui.shared.system.smartspace.ISmartspaceTransitionController
            public void setSmartspace(ISmartspaceCallback iSmartspaceCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISmartspaceTransitionController.DESCRIPTOR);
                    obtain.writeStrongBinder(iSmartspaceCallback != null ? iSmartspaceCallback.asBinder() : null);
                    if (!this.mRemote.transact(1, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSmartspace(iSmartspaceCallback);
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, ISmartspaceTransitionController.DESCRIPTOR);
        }

        public static ISmartspaceTransitionController asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(ISmartspaceTransitionController.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ISmartspaceTransitionController)) {
                return new Proxy(iBinder);
            }
            return (ISmartspaceTransitionController) queryLocalInterface;
        }

        public static ISmartspaceTransitionController getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(ISmartspaceTransitionController iSmartspaceTransitionController) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iSmartspaceTransitionController == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iSmartspaceTransitionController;
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
                parcel2.writeString(ISmartspaceTransitionController.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel.enforceInterface(ISmartspaceTransitionController.DESCRIPTOR);
                setSmartspace(ISmartspaceCallback.Stub.asInterface(parcel.readStrongBinder()));
                return true;
            }
        }
    }

    void setSmartspace(ISmartspaceCallback iSmartspaceCallback) throws RemoteException;
}
