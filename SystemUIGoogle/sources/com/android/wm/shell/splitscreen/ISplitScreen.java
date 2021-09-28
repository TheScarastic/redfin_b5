package com.android.wm.shell.splitscreen;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.UserHandle;
import android.window.IRemoteTransition;
import com.android.wm.shell.splitscreen.ISplitScreenListener;
/* loaded from: classes2.dex */
public interface ISplitScreen extends IInterface {
    void exitSplitScreen() throws RemoteException;

    void exitSplitScreenOnHide(boolean z) throws RemoteException;

    void registerSplitScreenListener(ISplitScreenListener iSplitScreenListener) throws RemoteException;

    void removeFromSideStage(int i) throws RemoteException;

    void setSideStageVisibility(boolean z) throws RemoteException;

    void startIntent(PendingIntent pendingIntent, Intent intent, int i, int i2, Bundle bundle) throws RemoteException;

    void startShortcut(String str, String str2, int i, int i2, Bundle bundle, UserHandle userHandle) throws RemoteException;

    void startTask(int i, int i2, int i3, Bundle bundle) throws RemoteException;

    void startTasks(int i, Bundle bundle, int i2, Bundle bundle2, int i3, IRemoteTransition iRemoteTransition) throws RemoteException;

    void unregisterSplitScreenListener(ISplitScreenListener iSplitScreenListener) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements ISplitScreen {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.android.wm.shell.splitscreen.ISplitScreen");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i != 1598968902) {
                boolean z = false;
                Bundle bundle = null;
                switch (i) {
                    case 2:
                        parcel.enforceInterface("com.android.wm.shell.splitscreen.ISplitScreen");
                        registerSplitScreenListener(ISplitScreenListener.Stub.asInterface(parcel.readStrongBinder()));
                        return true;
                    case 3:
                        parcel.enforceInterface("com.android.wm.shell.splitscreen.ISplitScreen");
                        unregisterSplitScreenListener(ISplitScreenListener.Stub.asInterface(parcel.readStrongBinder()));
                        return true;
                    case 4:
                        parcel.enforceInterface("com.android.wm.shell.splitscreen.ISplitScreen");
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        setSideStageVisibility(z);
                        return true;
                    case 5:
                        parcel.enforceInterface("com.android.wm.shell.splitscreen.ISplitScreen");
                        removeFromSideStage(parcel.readInt());
                        return true;
                    case 6:
                        parcel.enforceInterface("com.android.wm.shell.splitscreen.ISplitScreen");
                        exitSplitScreen();
                        return true;
                    case 7:
                        parcel.enforceInterface("com.android.wm.shell.splitscreen.ISplitScreen");
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        exitSplitScreenOnHide(z);
                        return true;
                    case 8:
                        parcel.enforceInterface("com.android.wm.shell.splitscreen.ISplitScreen");
                        int readInt = parcel.readInt();
                        int readInt2 = parcel.readInt();
                        int readInt3 = parcel.readInt();
                        if (parcel.readInt() != 0) {
                            bundle = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        startTask(readInt, readInt2, readInt3, bundle);
                        return true;
                    case 9:
                        parcel.enforceInterface("com.android.wm.shell.splitscreen.ISplitScreen");
                        startShortcut(parcel.readString(), parcel.readString(), parcel.readInt(), parcel.readInt(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? (UserHandle) UserHandle.CREATOR.createFromParcel(parcel) : null);
                        return true;
                    case 10:
                        parcel.enforceInterface("com.android.wm.shell.splitscreen.ISplitScreen");
                        PendingIntent pendingIntent = parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel) : null;
                        Intent intent = parcel.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(parcel) : null;
                        int readInt4 = parcel.readInt();
                        int readInt5 = parcel.readInt();
                        if (parcel.readInt() != 0) {
                            bundle = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        startIntent(pendingIntent, intent, readInt4, readInt5, bundle);
                        return true;
                    case 11:
                        parcel.enforceInterface("com.android.wm.shell.splitscreen.ISplitScreen");
                        int readInt6 = parcel.readInt();
                        Bundle bundle2 = parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null;
                        int readInt7 = parcel.readInt();
                        if (parcel.readInt() != 0) {
                            bundle = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        startTasks(readInt6, bundle2, readInt7, bundle, parcel.readInt(), IRemoteTransition.Stub.asInterface(parcel.readStrongBinder()));
                        return true;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
            } else {
                parcel2.writeString("com.android.wm.shell.splitscreen.ISplitScreen");
                return true;
            }
        }
    }
}
