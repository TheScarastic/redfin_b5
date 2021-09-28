package com.android.systemui.shared.recents;

import android.graphics.Rect;
import android.graphics.Region;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IOverviewProxy extends IInterface {
    public static final String DESCRIPTOR = "com.android.systemui.shared.recents.IOverviewProxy";

    /* loaded from: classes.dex */
    public static class Default implements IOverviewProxy {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.android.systemui.shared.recents.IOverviewProxy
        public void onActiveNavBarRegionChanges(Region region) throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.IOverviewProxy
        public void onAssistantAvailable(boolean z) throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.IOverviewProxy
        public void onAssistantVisibilityChanged(float f) throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.IOverviewProxy
        public void onBackAction(boolean z, int i, int i2, boolean z2, boolean z3) throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.IOverviewProxy
        public void onImeWindowStatusChanged(int i, IBinder iBinder, int i2, int i3, boolean z) throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.IOverviewProxy
        public void onInitialize(Bundle bundle) throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.IOverviewProxy
        public void onOverviewHidden(boolean z, boolean z2) throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.IOverviewProxy
        public void onOverviewShown(boolean z) throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.IOverviewProxy
        public void onOverviewToggle() throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.IOverviewProxy
        public void onSplitScreenSecondaryBoundsChanged(Rect rect, Rect rect2) throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.IOverviewProxy
        public void onSystemUiStateChanged(int i) throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.IOverviewProxy
        public void onTip(int i, int i2) throws RemoteException {
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOverviewProxy {
        public static final int TRANSACTION_onActiveNavBarRegionChanges = 12;
        public static final int TRANSACTION_onAssistantAvailable = 14;
        public static final int TRANSACTION_onAssistantVisibilityChanged = 15;
        public static final int TRANSACTION_onBackAction = 16;
        public static final int TRANSACTION_onImeWindowStatusChanged = 19;
        public static final int TRANSACTION_onInitialize = 13;
        public static final int TRANSACTION_onOverviewHidden = 9;
        public static final int TRANSACTION_onOverviewShown = 8;
        public static final int TRANSACTION_onOverviewToggle = 7;
        public static final int TRANSACTION_onSplitScreenSecondaryBoundsChanged = 18;
        public static final int TRANSACTION_onSystemUiStateChanged = 17;
        public static final int TRANSACTION_onTip = 11;

        /* loaded from: classes.dex */
        public static class Proxy implements IOverviewProxy {
            public static IOverviewProxy sDefaultImpl;
            private IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOverviewProxy.DESCRIPTOR;
            }

            @Override // com.android.systemui.shared.recents.IOverviewProxy
            public void onActiveNavBarRegionChanges(Region region) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOverviewProxy.DESCRIPTOR);
                    if (region != null) {
                        obtain.writeInt(1);
                        region.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!this.mRemote.transact(12, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onActiveNavBarRegionChanges(region);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.IOverviewProxy
            public void onAssistantAvailable(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOverviewProxy.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    if (!this.mRemote.transact(14, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onAssistantAvailable(z);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.IOverviewProxy
            public void onAssistantVisibilityChanged(float f) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOverviewProxy.DESCRIPTOR);
                    obtain.writeFloat(f);
                    if (!this.mRemote.transact(15, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onAssistantVisibilityChanged(f);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.IOverviewProxy
            public void onBackAction(boolean z, int i, int i2, boolean z2, boolean z3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOverviewProxy.DESCRIPTOR);
                    int i3 = 0;
                    obtain.writeInt(z ? 1 : 0);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(z2 ? 1 : 0);
                    if (z3) {
                        i3 = 1;
                    }
                    obtain.writeInt(i3);
                    if (!this.mRemote.transact(16, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onBackAction(z, i, i2, z2, z3);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.IOverviewProxy
            public void onImeWindowStatusChanged(int i, IBinder iBinder, int i2, int i3, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOverviewProxy.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    obtain.writeInt(z ? 1 : 0);
                    if (!this.mRemote.transact(19, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onImeWindowStatusChanged(i, iBinder, i2, i3, z);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.IOverviewProxy
            public void onInitialize(Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOverviewProxy.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!this.mRemote.transact(13, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onInitialize(bundle);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.IOverviewProxy
            public void onOverviewHidden(boolean z, boolean z2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOverviewProxy.DESCRIPTOR);
                    int i = 0;
                    obtain.writeInt(z ? 1 : 0);
                    if (z2) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(9, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onOverviewHidden(z, z2);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.IOverviewProxy
            public void onOverviewShown(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOverviewProxy.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    if (!this.mRemote.transact(8, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onOverviewShown(z);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.IOverviewProxy
            public void onOverviewToggle() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOverviewProxy.DESCRIPTOR);
                    if (!this.mRemote.transact(7, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onOverviewToggle();
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.IOverviewProxy
            public void onSplitScreenSecondaryBoundsChanged(Rect rect, Rect rect2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOverviewProxy.DESCRIPTOR);
                    if (rect != null) {
                        obtain.writeInt(1);
                        rect.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (rect2 != null) {
                        obtain.writeInt(1);
                        rect2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!this.mRemote.transact(18, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onSplitScreenSecondaryBoundsChanged(rect, rect2);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.IOverviewProxy
            public void onSystemUiStateChanged(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOverviewProxy.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(17, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onSystemUiStateChanged(i);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.IOverviewProxy
            public void onTip(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOverviewProxy.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    if (!this.mRemote.transact(11, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onTip(i, i2);
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, IOverviewProxy.DESCRIPTOR);
        }

        public static IOverviewProxy asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IOverviewProxy.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IOverviewProxy)) {
                return new Proxy(iBinder);
            }
            return (IOverviewProxy) queryLocalInterface;
        }

        public static IOverviewProxy getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(IOverviewProxy iOverviewProxy) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iOverviewProxy == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iOverviewProxy;
                return true;
            }
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i != 1598968902) {
                Region region = null;
                Rect rect = null;
                Bundle bundle = null;
                boolean z = false;
                switch (i) {
                    case 7:
                        parcel.enforceInterface(IOverviewProxy.DESCRIPTOR);
                        onOverviewToggle();
                        return true;
                    case 8:
                        parcel.enforceInterface(IOverviewProxy.DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        onOverviewShown(z);
                        return true;
                    case 9:
                        parcel.enforceInterface(IOverviewProxy.DESCRIPTOR);
                        boolean z2 = parcel.readInt() != 0;
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        onOverviewHidden(z2, z);
                        return true;
                    case 10:
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                    case 11:
                        parcel.enforceInterface(IOverviewProxy.DESCRIPTOR);
                        onTip(parcel.readInt(), parcel.readInt());
                        return true;
                    case 12:
                        parcel.enforceInterface(IOverviewProxy.DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            region = (Region) Region.CREATOR.createFromParcel(parcel);
                        }
                        onActiveNavBarRegionChanges(region);
                        return true;
                    case 13:
                        parcel.enforceInterface(IOverviewProxy.DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            bundle = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        onInitialize(bundle);
                        return true;
                    case 14:
                        parcel.enforceInterface(IOverviewProxy.DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        onAssistantAvailable(z);
                        return true;
                    case 15:
                        parcel.enforceInterface(IOverviewProxy.DESCRIPTOR);
                        onAssistantVisibilityChanged(parcel.readFloat());
                        return true;
                    case 16:
                        parcel.enforceInterface(IOverviewProxy.DESCRIPTOR);
                        onBackAction(parcel.readInt() != 0, parcel.readInt(), parcel.readInt(), parcel.readInt() != 0, parcel.readInt() != 0);
                        return true;
                    case 17:
                        parcel.enforceInterface(IOverviewProxy.DESCRIPTOR);
                        onSystemUiStateChanged(parcel.readInt());
                        return true;
                    case 18:
                        parcel.enforceInterface(IOverviewProxy.DESCRIPTOR);
                        Rect rect2 = parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null;
                        if (parcel.readInt() != 0) {
                            rect = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        }
                        onSplitScreenSecondaryBoundsChanged(rect2, rect);
                        return true;
                    case 19:
                        parcel.enforceInterface(IOverviewProxy.DESCRIPTOR);
                        onImeWindowStatusChanged(parcel.readInt(), parcel.readStrongBinder(), parcel.readInt(), parcel.readInt(), parcel.readInt() != 0);
                        return true;
                }
            } else {
                parcel2.writeString(IOverviewProxy.DESCRIPTOR);
                return true;
            }
        }
    }

    void onActiveNavBarRegionChanges(Region region) throws RemoteException;

    void onAssistantAvailable(boolean z) throws RemoteException;

    void onAssistantVisibilityChanged(float f) throws RemoteException;

    void onBackAction(boolean z, int i, int i2, boolean z2, boolean z3) throws RemoteException;

    void onImeWindowStatusChanged(int i, IBinder iBinder, int i2, int i3, boolean z) throws RemoteException;

    void onInitialize(Bundle bundle) throws RemoteException;

    void onOverviewHidden(boolean z, boolean z2) throws RemoteException;

    void onOverviewShown(boolean z) throws RemoteException;

    void onOverviewToggle() throws RemoteException;

    void onSplitScreenSecondaryBoundsChanged(Rect rect, Rect rect2) throws RemoteException;

    void onSystemUiStateChanged(int i) throws RemoteException;

    void onTip(int i, int i2) throws RemoteException;
}
