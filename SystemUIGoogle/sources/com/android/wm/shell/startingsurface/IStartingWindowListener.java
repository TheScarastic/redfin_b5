package com.android.wm.shell.startingsurface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface IStartingWindowListener extends IInterface {
    void onTaskLaunching(int i, int i2, int i3) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IStartingWindowListener {
        public static IStartingWindowListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.android.wm.shell.startingsurface.IStartingWindowListener");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IStartingWindowListener)) {
                return new Proxy(iBinder);
            }
            return (IStartingWindowListener) queryLocalInterface;
        }

        /* access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IStartingWindowListener {
            public static IStartingWindowListener sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.android.wm.shell.startingsurface.IStartingWindowListener
            public void onTaskLaunching(int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.android.wm.shell.startingsurface.IStartingWindowListener");
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    if (!this.mRemote.transact(1, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onTaskLaunching(i, i2, i3);
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static IStartingWindowListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
