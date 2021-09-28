package com.android.wm.shell.common;

import android.os.RemoteException;
import android.view.IDisplayWindowRotationCallback;
import android.view.IDisplayWindowRotationController;
import android.view.IWindowManager;
import android.window.WindowContainerTransaction;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes2.dex */
public class DisplayChangeController {
    private final IDisplayWindowRotationController mControllerImpl;
    private final ShellExecutor mMainExecutor;
    private final ArrayList<OnDisplayChangingListener> mRotationListener = new ArrayList<>();
    private final ArrayList<OnDisplayChangingListener> mTmpListeners = new ArrayList<>();
    private final IWindowManager mWmService;

    /* loaded from: classes2.dex */
    public interface OnDisplayChangingListener {
        void onRotateDisplay(int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction);
    }

    public DisplayChangeController(IWindowManager iWindowManager, ShellExecutor shellExecutor) {
        this.mMainExecutor = shellExecutor;
        this.mWmService = iWindowManager;
        DisplayWindowRotationControllerImpl displayWindowRotationControllerImpl = new DisplayWindowRotationControllerImpl();
        this.mControllerImpl = displayWindowRotationControllerImpl;
        try {
            iWindowManager.setDisplayWindowRotationController(displayWindowRotationControllerImpl);
        } catch (RemoteException unused) {
            throw new RuntimeException("Unable to register rotation controller");
        }
    }

    public void addRotationListener(OnDisplayChangingListener onDisplayChangingListener) {
        synchronized (this.mRotationListener) {
            this.mRotationListener.add(onDisplayChangingListener);
        }
    }

    /* access modifiers changed from: private */
    public void onRotateDisplay(int i, int i2, int i3, IDisplayWindowRotationCallback iDisplayWindowRotationCallback) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        synchronized (this.mRotationListener) {
            this.mTmpListeners.clear();
            this.mTmpListeners.addAll(this.mRotationListener);
        }
        Iterator<OnDisplayChangingListener> it = this.mTmpListeners.iterator();
        while (it.hasNext()) {
            it.next().onRotateDisplay(i, i2, i3, windowContainerTransaction);
        }
        try {
            iDisplayWindowRotationCallback.continueRotateDisplay(i3, windowContainerTransaction);
        } catch (RemoteException unused) {
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class DisplayWindowRotationControllerImpl extends IDisplayWindowRotationController.Stub {
        private DisplayWindowRotationControllerImpl() {
        }

        public void onRotateDisplay(int i, int i2, int i3, IDisplayWindowRotationCallback iDisplayWindowRotationCallback) {
            DisplayChangeController.this.mMainExecutor.execute(new DisplayChangeController$DisplayWindowRotationControllerImpl$$ExternalSyntheticLambda0(this, i, i2, i3, iDisplayWindowRotationCallback));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onRotateDisplay$0(int i, int i2, int i3, IDisplayWindowRotationCallback iDisplayWindowRotationCallback) {
            DisplayChangeController.this.onRotateDisplay(i, i2, i3, iDisplayWindowRotationCallback);
        }
    }
}
