package com.google.android.systemui.statusbar;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface INotificationVoiceReplyServiceCallbacks extends IInterface {
    void onNotifAvailableForReplyChanged(boolean z) throws RemoteException;

    void onVoiceReplyHandled(int i, int i2) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements INotificationVoiceReplyServiceCallbacks {
        public static INotificationVoiceReplyServiceCallbacks asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.systemui.statusbar.INotificationVoiceReplyServiceCallbacks");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof INotificationVoiceReplyServiceCallbacks)) {
                return new Proxy(iBinder);
            }
            return (INotificationVoiceReplyServiceCallbacks) queryLocalInterface;
        }

        /* access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements INotificationVoiceReplyServiceCallbacks {
            public static INotificationVoiceReplyServiceCallbacks sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.google.android.systemui.statusbar.INotificationVoiceReplyServiceCallbacks
            public void onNotifAvailableForReplyChanged(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.systemui.statusbar.INotificationVoiceReplyServiceCallbacks");
                    obtain.writeInt(z ? 1 : 0);
                    if (!this.mRemote.transact(1, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onNotifAvailableForReplyChanged(z);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.google.android.systemui.statusbar.INotificationVoiceReplyServiceCallbacks
            public void onVoiceReplyHandled(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.systemui.statusbar.INotificationVoiceReplyServiceCallbacks");
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    if (!this.mRemote.transact(2, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onVoiceReplyHandled(i, i2);
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static INotificationVoiceReplyServiceCallbacks getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
