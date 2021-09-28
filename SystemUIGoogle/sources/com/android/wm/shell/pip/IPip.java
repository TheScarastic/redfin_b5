package com.android.wm.shell.pip;

import android.app.PictureInPictureParams;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.SurfaceControl;
import com.android.wm.shell.pip.IPipAnimationListener;
/* loaded from: classes2.dex */
public interface IPip extends IInterface {
    void setPinnedStackAnimationListener(IPipAnimationListener iPipAnimationListener) throws RemoteException;

    void setShelfHeight(boolean z, int i) throws RemoteException;

    Rect startSwipePipToHome(ComponentName componentName, ActivityInfo activityInfo, PictureInPictureParams pictureInPictureParams, int i, int i2) throws RemoteException;

    void stopSwipePipToHome(ComponentName componentName, Rect rect, SurfaceControl surfaceControl) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IPip {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.android.wm.shell.pip.IPip");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i != 1598968902) {
                boolean z = false;
                PictureInPictureParams pictureInPictureParams = null;
                SurfaceControl surfaceControl = null;
                if (i == 2) {
                    parcel.enforceInterface("com.android.wm.shell.pip.IPip");
                    ComponentName componentName = parcel.readInt() != 0 ? (ComponentName) ComponentName.CREATOR.createFromParcel(parcel) : null;
                    ActivityInfo activityInfo = parcel.readInt() != 0 ? (ActivityInfo) ActivityInfo.CREATOR.createFromParcel(parcel) : null;
                    if (parcel.readInt() != 0) {
                        pictureInPictureParams = (PictureInPictureParams) PictureInPictureParams.CREATOR.createFromParcel(parcel);
                    }
                    Rect startSwipePipToHome = startSwipePipToHome(componentName, activityInfo, pictureInPictureParams, parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    if (startSwipePipToHome != null) {
                        parcel2.writeInt(1);
                        startSwipePipToHome.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                } else if (i == 3) {
                    parcel.enforceInterface("com.android.wm.shell.pip.IPip");
                    ComponentName componentName2 = parcel.readInt() != 0 ? (ComponentName) ComponentName.CREATOR.createFromParcel(parcel) : null;
                    Rect rect = parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null;
                    if (parcel.readInt() != 0) {
                        surfaceControl = (SurfaceControl) SurfaceControl.CREATOR.createFromParcel(parcel);
                    }
                    stopSwipePipToHome(componentName2, rect, surfaceControl);
                    return true;
                } else if (i == 4) {
                    parcel.enforceInterface("com.android.wm.shell.pip.IPip");
                    setPinnedStackAnimationListener(IPipAnimationListener.Stub.asInterface(parcel.readStrongBinder()));
                    return true;
                } else if (i != 5) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    parcel.enforceInterface("com.android.wm.shell.pip.IPip");
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    setShelfHeight(z, parcel.readInt());
                    return true;
                }
            } else {
                parcel2.writeString("com.android.wm.shell.pip.IPip");
                return true;
            }
        }
    }
}
