package com.google.android.systemui.statusbar;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.systemui.statusbar.INotificationVoiceReplyServiceCallbacks;
/* loaded from: classes2.dex */
public interface INotificationVoiceReplyService extends IInterface {
    void onVoiceAuthStateChanged(int i, int i2) throws RemoteException;

    void registerCallbacks(INotificationVoiceReplyServiceCallbacks iNotificationVoiceReplyServiceCallbacks) throws RemoteException;

    void setFeatureEnabled(int i) throws RemoteException;

    void startVoiceReply(int i, String str) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements INotificationVoiceReplyService {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.google.android.systemui.statusbar.INotificationVoiceReplyService");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString("com.google.android.systemui.statusbar.INotificationVoiceReplyService");
                return true;
            } else if (i == 1) {
                parcel.enforceInterface("com.google.android.systemui.statusbar.INotificationVoiceReplyService");
                registerCallbacks(INotificationVoiceReplyServiceCallbacks.Stub.asInterface(parcel.readStrongBinder()));
                return true;
            } else if (i == 2) {
                parcel.enforceInterface("com.google.android.systemui.statusbar.INotificationVoiceReplyService");
                startVoiceReply(parcel.readInt(), parcel.readString());
                return true;
            } else if (i == 3) {
                parcel.enforceInterface("com.google.android.systemui.statusbar.INotificationVoiceReplyService");
                onVoiceAuthStateChanged(parcel.readInt(), parcel.readInt());
                return true;
            } else if (i != 4) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel.enforceInterface("com.google.android.systemui.statusbar.INotificationVoiceReplyService");
                setFeatureEnabled(parcel.readInt());
                return true;
            }
        }
    }
}
