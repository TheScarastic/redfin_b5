package com.google.android.systemui.elmyra;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface IElmyraServiceGestureListener extends IInterface {
    void onGestureDetected() throws RemoteException;

    void onGestureProgress(float f, int i) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IElmyraServiceGestureListener {
        public static IElmyraServiceGestureListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.systemui.elmyra.IElmyraServiceGestureListener");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IElmyraServiceGestureListener)) {
                return new Proxy(iBinder);
            }
            return (IElmyraServiceGestureListener) queryLocalInterface;
        }

        /* access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IElmyraServiceGestureListener {
            public static IElmyraServiceGestureListener sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.google.android.systemui.elmyra.IElmyraServiceGestureListener
            public void onGestureProgress(float f, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.systemui.elmyra.IElmyraServiceGestureListener");
                    obtain.writeFloat(f);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(1, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onGestureProgress(f, i);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.google.android.systemui.elmyra.IElmyraServiceGestureListener
            public void onGestureDetected() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.systemui.elmyra.IElmyraServiceGestureListener");
                    if (!this.mRemote.transact(2, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onGestureDetected();
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static IElmyraServiceGestureListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
