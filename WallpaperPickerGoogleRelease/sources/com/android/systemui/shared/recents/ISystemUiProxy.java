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
import com.android.systemui.shared.recents.model.Task;
/* loaded from: classes.dex */
public interface ISystemUiProxy extends IInterface {
    public static final String DESCRIPTOR = "com.android.systemui.shared.recents.ISystemUiProxy";

    /* loaded from: classes.dex */
    public static class Default implements ISystemUiProxy {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.android.systemui.shared.recents.ISystemUiProxy
        public void expandNotificationPanel() throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.ISystemUiProxy
        public Rect getNonMinimizedSplitScreenSecondaryBounds() throws RemoteException {
            return null;
        }

        @Override // com.android.systemui.shared.recents.ISystemUiProxy
        public void handleImageAsScreenshot(Bitmap bitmap, Rect rect, Insets insets, int i) throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.ISystemUiProxy
        public void handleImageBundleAsScreenshot(Bundle bundle, Rect rect, Insets insets, Task.TaskKey taskKey) throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.ISystemUiProxy
        public void notifyAccessibilityButtonClicked(int i) throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.ISystemUiProxy
        public void notifyAccessibilityButtonLongClicked() throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.ISystemUiProxy
        public void notifyPrioritizedRotation(int i) throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.ISystemUiProxy
        public void notifySwipeToHomeFinished() throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.ISystemUiProxy
        public void notifySwipeUpGestureStarted() throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.ISystemUiProxy
        public void onAssistantGestureCompletion(float f) throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.ISystemUiProxy
        public void onAssistantProgress(float f) throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.ISystemUiProxy
        public void onBackPressed() throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.ISystemUiProxy
        public void onOverviewShown(boolean z) throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.ISystemUiProxy
        public void onStatusBarMotionEvent(MotionEvent motionEvent) throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.ISystemUiProxy
        public void setHomeRotationEnabled(boolean z) throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.ISystemUiProxy
        public void setNavBarButtonAlpha(float f, boolean z) throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.ISystemUiProxy
        public void setSplitScreenMinimized(boolean z) throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.ISystemUiProxy
        public void startAssistant(Bundle bundle) throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.ISystemUiProxy
        public void startScreenPinning(int i) throws RemoteException {
        }

        @Override // com.android.systemui.shared.recents.ISystemUiProxy
        public void stopScreenPinning() throws RemoteException {
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ISystemUiProxy {
        public static final int TRANSACTION_expandNotificationPanel = 30;
        public static final int TRANSACTION_getNonMinimizedSplitScreenSecondaryBounds = 8;
        public static final int TRANSACTION_handleImageAsScreenshot = 22;
        public static final int TRANSACTION_handleImageBundleAsScreenshot = 29;
        public static final int TRANSACTION_notifyAccessibilityButtonClicked = 16;
        public static final int TRANSACTION_notifyAccessibilityButtonLongClicked = 17;
        public static final int TRANSACTION_notifyPrioritizedRotation = 26;
        public static final int TRANSACTION_notifySwipeToHomeFinished = 24;
        public static final int TRANSACTION_notifySwipeUpGestureStarted = 47;
        public static final int TRANSACTION_onAssistantGestureCompletion = 19;
        public static final int TRANSACTION_onAssistantProgress = 13;
        public static final int TRANSACTION_onBackPressed = 45;
        public static final int TRANSACTION_onOverviewShown = 7;
        public static final int TRANSACTION_onStatusBarMotionEvent = 10;
        public static final int TRANSACTION_setHomeRotationEnabled = 46;
        public static final int TRANSACTION_setNavBarButtonAlpha = 20;
        public static final int TRANSACTION_setSplitScreenMinimized = 23;
        public static final int TRANSACTION_startAssistant = 14;
        public static final int TRANSACTION_startScreenPinning = 2;
        public static final int TRANSACTION_stopScreenPinning = 18;

        /* loaded from: classes.dex */
        public static class Proxy implements ISystemUiProxy {
            public static ISystemUiProxy sDefaultImpl;
            private IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.android.systemui.shared.recents.ISystemUiProxy
            public void expandNotificationPanel() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISystemUiProxy.DESCRIPTOR);
                    if (this.mRemote.transact(30, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                    } else {
                        Stub.getDefaultImpl().expandNotificationPanel();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return ISystemUiProxy.DESCRIPTOR;
            }

            @Override // com.android.systemui.shared.recents.ISystemUiProxy
            public Rect getNonMinimizedSplitScreenSecondaryBounds() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISystemUiProxy.DESCRIPTOR);
                    if (!this.mRemote.transact(8, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getNonMinimizedSplitScreenSecondaryBounds();
                    }
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.ISystemUiProxy
            public void handleImageAsScreenshot(Bitmap bitmap, Rect rect, Insets insets, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISystemUiProxy.DESCRIPTOR);
                    if (bitmap != null) {
                        obtain.writeInt(1);
                        bitmap.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (rect != null) {
                        obtain.writeInt(1);
                        rect.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (insets != null) {
                        obtain.writeInt(1);
                        insets.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    if (this.mRemote.transact(22, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                    } else {
                        Stub.getDefaultImpl().handleImageAsScreenshot(bitmap, rect, insets, i);
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.ISystemUiProxy
            public void handleImageBundleAsScreenshot(Bundle bundle, Rect rect, Insets insets, Task.TaskKey taskKey) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISystemUiProxy.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (rect != null) {
                        obtain.writeInt(1);
                        rect.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (insets != null) {
                        obtain.writeInt(1);
                        insets.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (taskKey != null) {
                        obtain.writeInt(1);
                        taskKey.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(29, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                    } else {
                        Stub.getDefaultImpl().handleImageBundleAsScreenshot(bundle, rect, insets, taskKey);
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.ISystemUiProxy
            public void notifyAccessibilityButtonClicked(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISystemUiProxy.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (this.mRemote.transact(16, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                    } else {
                        Stub.getDefaultImpl().notifyAccessibilityButtonClicked(i);
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.ISystemUiProxy
            public void notifyAccessibilityButtonLongClicked() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISystemUiProxy.DESCRIPTOR);
                    if (this.mRemote.transact(17, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                    } else {
                        Stub.getDefaultImpl().notifyAccessibilityButtonLongClicked();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.ISystemUiProxy
            public void notifyPrioritizedRotation(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISystemUiProxy.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (this.mRemote.transact(26, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                    } else {
                        Stub.getDefaultImpl().notifyPrioritizedRotation(i);
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.ISystemUiProxy
            public void notifySwipeToHomeFinished() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISystemUiProxy.DESCRIPTOR);
                    if (this.mRemote.transact(24, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                    } else {
                        Stub.getDefaultImpl().notifySwipeToHomeFinished();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.ISystemUiProxy
            public void notifySwipeUpGestureStarted() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISystemUiProxy.DESCRIPTOR);
                    if (!this.mRemote.transact(47, obtain, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifySwipeUpGestureStarted();
                    }
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.ISystemUiProxy
            public void onAssistantGestureCompletion(float f) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISystemUiProxy.DESCRIPTOR);
                    obtain.writeFloat(f);
                    if (this.mRemote.transact(19, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                    } else {
                        Stub.getDefaultImpl().onAssistantGestureCompletion(f);
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.ISystemUiProxy
            public void onAssistantProgress(float f) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISystemUiProxy.DESCRIPTOR);
                    obtain.writeFloat(f);
                    if (this.mRemote.transact(13, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                    } else {
                        Stub.getDefaultImpl().onAssistantProgress(f);
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.ISystemUiProxy
            public void onBackPressed() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISystemUiProxy.DESCRIPTOR);
                    if (this.mRemote.transact(45, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                    } else {
                        Stub.getDefaultImpl().onBackPressed();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.ISystemUiProxy
            public void onOverviewShown(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISystemUiProxy.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    if (this.mRemote.transact(7, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                    } else {
                        Stub.getDefaultImpl().onOverviewShown(z);
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.ISystemUiProxy
            public void onStatusBarMotionEvent(MotionEvent motionEvent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISystemUiProxy.DESCRIPTOR);
                    if (motionEvent != null) {
                        obtain.writeInt(1);
                        motionEvent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(10, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                    } else {
                        Stub.getDefaultImpl().onStatusBarMotionEvent(motionEvent);
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.ISystemUiProxy
            public void setHomeRotationEnabled(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISystemUiProxy.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    if (this.mRemote.transact(46, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                    } else {
                        Stub.getDefaultImpl().setHomeRotationEnabled(z);
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.ISystemUiProxy
            public void setNavBarButtonAlpha(float f, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISystemUiProxy.DESCRIPTOR);
                    obtain.writeFloat(f);
                    obtain.writeInt(z ? 1 : 0);
                    if (this.mRemote.transact(20, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                    } else {
                        Stub.getDefaultImpl().setNavBarButtonAlpha(f, z);
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.ISystemUiProxy
            public void setSplitScreenMinimized(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISystemUiProxy.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    if (this.mRemote.transact(23, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                    } else {
                        Stub.getDefaultImpl().setSplitScreenMinimized(z);
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.ISystemUiProxy
            public void startAssistant(Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISystemUiProxy.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(14, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                    } else {
                        Stub.getDefaultImpl().startAssistant(bundle);
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.ISystemUiProxy
            public void startScreenPinning(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISystemUiProxy.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (this.mRemote.transact(2, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                    } else {
                        Stub.getDefaultImpl().startScreenPinning(i);
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.systemui.shared.recents.ISystemUiProxy
            public void stopScreenPinning() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISystemUiProxy.DESCRIPTOR);
                    if (this.mRemote.transact(18, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                    } else {
                        Stub.getDefaultImpl().stopScreenPinning();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, ISystemUiProxy.DESCRIPTOR);
        }

        public static ISystemUiProxy asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(ISystemUiProxy.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ISystemUiProxy)) {
                return new Proxy(iBinder);
            }
            return (ISystemUiProxy) queryLocalInterface;
        }

        public static ISystemUiProxy getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(ISystemUiProxy iSystemUiProxy) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iSystemUiProxy == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iSystemUiProxy;
                return true;
            }
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString(ISystemUiProxy.DESCRIPTOR);
                return true;
            } else if (i != 2) {
                MotionEvent motionEvent = null;
                Insets insets = null;
                Task.TaskKey taskKey = null;
                Bundle bundle = null;
                if (i == 10) {
                    parcel.enforceInterface(ISystemUiProxy.DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        motionEvent = (MotionEvent) MotionEvent.CREATOR.createFromParcel(parcel);
                    }
                    onStatusBarMotionEvent(motionEvent);
                    parcel2.writeNoException();
                    return true;
                } else if (i != 26) {
                    boolean z = false;
                    if (i == 7) {
                        parcel.enforceInterface(ISystemUiProxy.DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        onOverviewShown(z);
                        parcel2.writeNoException();
                        return true;
                    } else if (i == 8) {
                        parcel.enforceInterface(ISystemUiProxy.DESCRIPTOR);
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
                        parcel.enforceInterface(ISystemUiProxy.DESCRIPTOR);
                        onAssistantProgress(parcel.readFloat());
                        parcel2.writeNoException();
                        return true;
                    } else if (i == 14) {
                        parcel.enforceInterface(ISystemUiProxy.DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            bundle = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        startAssistant(bundle);
                        parcel2.writeNoException();
                        return true;
                    } else if (i == 29) {
                        parcel.enforceInterface(ISystemUiProxy.DESCRIPTOR);
                        Bundle bundle2 = parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null;
                        Rect rect = parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null;
                        Insets insets2 = parcel.readInt() != 0 ? (Insets) Insets.CREATOR.createFromParcel(parcel) : null;
                        if (parcel.readInt() != 0) {
                            taskKey = Task.TaskKey.CREATOR.createFromParcel(parcel);
                        }
                        handleImageBundleAsScreenshot(bundle2, rect, insets2, taskKey);
                        parcel2.writeNoException();
                        return true;
                    } else if (i != 30) {
                        switch (i) {
                            case 16:
                                parcel.enforceInterface(ISystemUiProxy.DESCRIPTOR);
                                notifyAccessibilityButtonClicked(parcel.readInt());
                                parcel2.writeNoException();
                                return true;
                            case 17:
                                parcel.enforceInterface(ISystemUiProxy.DESCRIPTOR);
                                notifyAccessibilityButtonLongClicked();
                                parcel2.writeNoException();
                                return true;
                            case 18:
                                parcel.enforceInterface(ISystemUiProxy.DESCRIPTOR);
                                stopScreenPinning();
                                parcel2.writeNoException();
                                return true;
                            case 19:
                                parcel.enforceInterface(ISystemUiProxy.DESCRIPTOR);
                                onAssistantGestureCompletion(parcel.readFloat());
                                parcel2.writeNoException();
                                return true;
                            case 20:
                                parcel.enforceInterface(ISystemUiProxy.DESCRIPTOR);
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
                                        parcel.enforceInterface(ISystemUiProxy.DESCRIPTOR);
                                        Bitmap bitmap = parcel.readInt() != 0 ? (Bitmap) Bitmap.CREATOR.createFromParcel(parcel) : null;
                                        Rect rect2 = parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null;
                                        if (parcel.readInt() != 0) {
                                            insets = (Insets) Insets.CREATOR.createFromParcel(parcel);
                                        }
                                        handleImageAsScreenshot(bitmap, rect2, insets, parcel.readInt());
                                        parcel2.writeNoException();
                                        return true;
                                    case 23:
                                        parcel.enforceInterface(ISystemUiProxy.DESCRIPTOR);
                                        if (parcel.readInt() != 0) {
                                            z = true;
                                        }
                                        setSplitScreenMinimized(z);
                                        parcel2.writeNoException();
                                        return true;
                                    case 24:
                                        parcel.enforceInterface(ISystemUiProxy.DESCRIPTOR);
                                        notifySwipeToHomeFinished();
                                        parcel2.writeNoException();
                                        return true;
                                    default:
                                        switch (i) {
                                            case TRANSACTION_onBackPressed /* 45 */:
                                                parcel.enforceInterface(ISystemUiProxy.DESCRIPTOR);
                                                onBackPressed();
                                                parcel2.writeNoException();
                                                return true;
                                            case TRANSACTION_setHomeRotationEnabled /* 46 */:
                                                parcel.enforceInterface(ISystemUiProxy.DESCRIPTOR);
                                                if (parcel.readInt() != 0) {
                                                    z = true;
                                                }
                                                setHomeRotationEnabled(z);
                                                parcel2.writeNoException();
                                                return true;
                                            case TRANSACTION_notifySwipeUpGestureStarted /* 47 */:
                                                parcel.enforceInterface(ISystemUiProxy.DESCRIPTOR);
                                                notifySwipeUpGestureStarted();
                                                return true;
                                            default:
                                                return super.onTransact(i, parcel, parcel2, i2);
                                        }
                                }
                        }
                    } else {
                        parcel.enforceInterface(ISystemUiProxy.DESCRIPTOR);
                        expandNotificationPanel();
                        parcel2.writeNoException();
                        return true;
                    }
                } else {
                    parcel.enforceInterface(ISystemUiProxy.DESCRIPTOR);
                    notifyPrioritizedRotation(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                }
            } else {
                parcel.enforceInterface(ISystemUiProxy.DESCRIPTOR);
                startScreenPinning(parcel.readInt());
                parcel2.writeNoException();
                return true;
            }
        }
    }

    void expandNotificationPanel() throws RemoteException;

    Rect getNonMinimizedSplitScreenSecondaryBounds() throws RemoteException;

    @Deprecated
    void handleImageAsScreenshot(Bitmap bitmap, Rect rect, Insets insets, int i) throws RemoteException;

    void handleImageBundleAsScreenshot(Bundle bundle, Rect rect, Insets insets, Task.TaskKey taskKey) throws RemoteException;

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
}
