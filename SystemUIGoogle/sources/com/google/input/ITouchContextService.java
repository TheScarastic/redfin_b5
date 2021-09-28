package com.google.input;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface ITouchContextService extends IInterface {
    public static final String DESCRIPTOR = "com$google$input$ITouchContextService".replace('$', '.');

    void updateContext(ContextPacket contextPacket) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements ITouchContextService {
        public static ITouchContextService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(ITouchContextService.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ITouchContextService)) {
                return new Proxy(iBinder);
            }
            return (ITouchContextService) queryLocalInterface;
        }

        /* access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements ITouchContextService {
            public static ITouchContextService sDefaultImpl;
            private IBinder mRemote;
            private int mCachedVersion = -1;
            private String mCachedHash = "-1";

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.google.input.ITouchContextService
            public void updateContext(ContextPacket contextPacket) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITouchContextService.DESCRIPTOR);
                    if (contextPacket != null) {
                        obtain.writeInt(1);
                        contextPacket.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(1, obtain, null, 1)) {
                        return;
                    }
                    if (Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateContext(contextPacket);
                        return;
                    }
                    throw new RemoteException("Method updateContext is unimplemented.");
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static ITouchContextService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
