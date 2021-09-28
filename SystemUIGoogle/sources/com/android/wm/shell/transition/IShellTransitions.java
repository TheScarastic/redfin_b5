package com.android.wm.shell.transition;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.window.IRemoteTransition;
import android.window.TransitionFilter;
/* loaded from: classes2.dex */
public interface IShellTransitions extends IInterface {
    void registerRemote(TransitionFilter transitionFilter, IRemoteTransition iRemoteTransition) throws RemoteException;

    void unregisterRemote(IRemoteTransition iRemoteTransition) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IShellTransitions {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.android.wm.shell.transition.IShellTransitions");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString("com.android.wm.shell.transition.IShellTransitions");
                return true;
            } else if (i == 2) {
                parcel.enforceInterface("com.android.wm.shell.transition.IShellTransitions");
                registerRemote(parcel.readInt() != 0 ? (TransitionFilter) TransitionFilter.CREATOR.createFromParcel(parcel) : null, IRemoteTransition.Stub.asInterface(parcel.readStrongBinder()));
                return true;
            } else if (i != 3) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel.enforceInterface("com.android.wm.shell.transition.IShellTransitions");
                unregisterRemote(IRemoteTransition.Stub.asInterface(parcel.readStrongBinder()));
                return true;
            }
        }
    }
}
