package com.android.wm.shell.splitscreen;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface ISplitScreenListener extends IInterface {
    void onStagePositionChanged(int i, int i2) throws RemoteException;

    void onTaskStageChanged(int i, int i2, boolean z) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements ISplitScreenListener {
        public static ISplitScreenListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.android.wm.shell.splitscreen.ISplitScreenListener");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ISplitScreenListener)) {
                return new Proxy(iBinder);
            }
            return (ISplitScreenListener) queryLocalInterface;
        }

        /* access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements ISplitScreenListener {
            public static ISplitScreenListener sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.android.wm.shell.splitscreen.ISplitScreenListener
            public void onStagePositionChanged(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.android.wm.shell.splitscreen.ISplitScreenListener");
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    if (!this.mRemote.transact(1, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onStagePositionChanged(i, i2);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.android.wm.shell.splitscreen.ISplitScreenListener
            public void onTaskStageChanged(int i, int i2, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.android.wm.shell.splitscreen.ISplitScreenListener");
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(z ? 1 : 0);
                    if (!this.mRemote.transact(2, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onTaskStageChanged(i, i2, z);
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static ISplitScreenListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
