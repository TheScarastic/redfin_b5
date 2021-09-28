package com.android.systemui.shared.system.smartspace;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.systemui.shared.system.smartspace.ISmartspaceCallback;
/* loaded from: classes.dex */
public interface ISmartspaceTransitionController extends IInterface {
    void setSmartspace(ISmartspaceCallback iSmartspaceCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ISmartspaceTransitionController {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.android.systemui.shared.system.smartspace.ISmartspaceTransitionController");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString("com.android.systemui.shared.system.smartspace.ISmartspaceTransitionController");
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel.enforceInterface("com.android.systemui.shared.system.smartspace.ISmartspaceTransitionController");
                setSmartspace(ISmartspaceCallback.Stub.asInterface(parcel.readStrongBinder()));
                return true;
            }
        }
    }
}
