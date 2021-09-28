package com.android.systemui.shared.recents;

import android.graphics.Bitmap;
import android.graphics.Insets;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.MotionEvent;
import com.android.systemui.shared.recents.model.Task$TaskKey;
/* loaded from: classes.dex */
public interface ISystemUiProxy extends IInterface {
    void expandNotificationPanel() throws RemoteException;

    Rect getNonMinimizedSplitScreenSecondaryBounds() throws RemoteException;

    @Deprecated
    void handleImageAsScreenshot(Bitmap bitmap, Rect rect, Insets insets, int i) throws RemoteException;

    void handleImageBundleAsScreenshot(Bundle bundle, Rect rect, Insets insets, Task$TaskKey task$TaskKey) throws RemoteException;

    void notifyAccessibilityButtonClicked(int i) throws RemoteException;

    void notifyAccessibilityButtonLongClicked() throws RemoteException;

    void notifyPrioritizedRotation(int i) throws RemoteException;

    void notifySwipeToHomeFinished() throws RemoteException;

    void notifySwipeUpGestureStarted() throws RemoteException;

    void onAssistantGestureCompletion(float f) throws RemoteException;

    void onAssistantProgress(float f) throws RemoteException;

    void onBackPressed() throws RemoteException;

    void onOverviewShown(boolean z) throws RemoteException;

    void onStatusBarMotionEvent(MotionEvent motionEvent) throws RemoteException;

    void setHomeRotationEnabled(boolean z) throws RemoteException;

    void setNavBarButtonAlpha(float f, boolean z) throws RemoteException;

    void setSplitScreenMinimized(boolean z) throws RemoteException;

    void startAssistant(Bundle bundle) throws RemoteException;

    void startScreenPinning(int i) throws RemoteException;

    void stopScreenPinning() throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ISystemUiProxy {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.android.systemui.shared.recents.ISystemUiProxy");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString("com.android.systemui.shared.recents.ISystemUiProxy");
                return true;
            } else if (i != 2) {
                MotionEvent motionEvent = null;
                Insets insets = null;
                Task$TaskKey task$TaskKey = null;
                Bundle bundle = null;
                if (i == 10) {
                    parcel.enforceInterface("com.android.systemui.shared.recents.ISystemUiProxy");
                    if (parcel.readInt() != 0) {
                        motionEvent = (MotionEvent) MotionEvent.CREATOR.createFromParcel(parcel);
                    }
                    onStatusBarMotionEvent(motionEvent);
                    parcel2.writeNoException();
                    return true;
                } else if (i != 26) {
                    boolean z = false;
                    if (i == 7) {
                        parcel.enforceInterface("com.android.systemui.shared.recents.ISystemUiProxy");
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        onOverviewShown(z);
                        parcel2.writeNoException();
                        return true;
                    } else if (i == 8) {
                        parcel.enforceInterface("com.android.systemui.shared.recents.ISystemUiProxy");
                        Rect nonMinimizedSplitScreenSecondaryBounds = getNonMinimizedSplitScreenSecondaryBounds();
                        parcel2.writeNoException();
                        if (nonMinimizedSplitScreenSecondaryBounds != null) {
                            parcel2.writeInt(1);
                            nonMinimizedSplitScreenSecondaryBounds.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    } else if (i == 13) {
                        parcel.enforceInterface("com.android.systemui.shared.recents.ISystemUiProxy");
                        onAssistantProgress(parcel.readFloat());
                        parcel2.writeNoException();
                        return true;
                    } else if (i == 14) {
                        parcel.enforceInterface("com.android.systemui.shared.recents.ISystemUiProxy");
                        if (parcel.readInt() != 0) {
                            bundle = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        startAssistant(bundle);
                        parcel2.writeNoException();
                        return true;
                    } else if (i == 29) {
                        parcel.enforceInterface("com.android.systemui.shared.recents.ISystemUiProxy");
                        Bundle bundle2 = parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null;
                        Rect rect = parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null;
                        Insets insets2 = parcel.readInt() != 0 ? (Insets) Insets.CREATOR.createFromParcel(parcel) : null;
                        if (parcel.readInt() != 0) {
                            task$TaskKey = Task$TaskKey.CREATOR.createFromParcel(parcel);
                        }
                        handleImageBundleAsScreenshot(bundle2, rect, insets2, task$TaskKey);
                        parcel2.writeNoException();
                        return true;
                    } else if (i != 30) {
                        switch (i) {
                            case 16:
                                parcel.enforceInterface("com.android.systemui.shared.recents.ISystemUiProxy");
                                notifyAccessibilityButtonClicked(parcel.readInt());
                                parcel2.writeNoException();
                                return true;
                            case 17:
                                parcel.enforceInterface("com.android.systemui.shared.recents.ISystemUiProxy");
                                notifyAccessibilityButtonLongClicked();
                                parcel2.writeNoException();
                                return true;
                            case 18:
                                parcel.enforceInterface("com.android.systemui.shared.recents.ISystemUiProxy");
                                stopScreenPinning();
                                parcel2.writeNoException();
                                return true;
                            case 19:
                                parcel.enforceInterface("com.android.systemui.shared.recents.ISystemUiProxy");
                                onAssistantGestureCompletion(parcel.readFloat());
                                parcel2.writeNoException();
                                return true;
                            case 20:
                                parcel.enforceInterface("com.android.systemui.shared.recents.ISystemUiProxy");
                                float readFloat = parcel.readFloat();
                                if (parcel.readInt() != 0) {
                                    z = true;
                                }
                                setNavBarButtonAlpha(readFloat, z);
                                parcel2.writeNoException();
                                return true;
                            default:
                                switch (i) {
                                    case 22:
                                        parcel.enforceInterface("com.android.systemui.shared.recents.ISystemUiProxy");
                                        Bitmap bitmap = parcel.readInt() != 0 ? (Bitmap) Bitmap.CREATOR.createFromParcel(parcel) : null;
                                        Rect rect2 = parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null;
                                        if (parcel.readInt() != 0) {
                                            insets = (Insets) Insets.CREATOR.createFromParcel(parcel);
                                        }
                                        handleImageAsScreenshot(bitmap, rect2, insets, parcel.readInt());
                                        parcel2.writeNoException();
                                        return true;
                                    case 23:
                                        parcel.enforceInterface("com.android.systemui.shared.recents.ISystemUiProxy");
                                        if (parcel.readInt() != 0) {
                                            z = true;
                                        }
                                        setSplitScreenMinimized(z);
                                        parcel2.writeNoException();
                                        return true;
                                    case 24:
                                        parcel.enforceInterface("com.android.systemui.shared.recents.ISystemUiProxy");
                                        notifySwipeToHomeFinished();
                                        parcel2.writeNoException();
                                        return true;
                                    default:
                                        switch (i) {
                                            case 45:
                                                parcel.enforceInterface("com.android.systemui.shared.recents.ISystemUiProxy");
                                                onBackPressed();
                                                parcel2.writeNoException();
                                                return true;
                                            case 46:
                                                parcel.enforceInterface("com.android.systemui.shared.recents.ISystemUiProxy");
                                                if (parcel.readInt() != 0) {
                                                    z = true;
                                                }
                                                setHomeRotationEnabled(z);
                                                parcel2.writeNoException();
                                                return true;
                                            case 47:
                                                parcel.enforceInterface("com.android.systemui.shared.recents.ISystemUiProxy");
                                                notifySwipeUpGestureStarted();
                                                return true;
                                            default:
                                                return super.onTransact(i, parcel, parcel2, i2);
                                        }
                                }
                        }
                    } else {
                        parcel.enforceInterface("com.android.systemui.shared.recents.ISystemUiProxy");
                        expandNotificationPanel();
                        parcel2.writeNoException();
                        return true;
                    }
                } else {
                    parcel.enforceInterface("com.android.systemui.shared.recents.ISystemUiProxy");
                    notifyPrioritizedRotation(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                }
            } else {
                parcel.enforceInterface("com.android.systemui.shared.recents.ISystemUiProxy");
                startScreenPinning(parcel.readInt());
                parcel2.writeNoException();
                return true;
            }
        }
    }
}
