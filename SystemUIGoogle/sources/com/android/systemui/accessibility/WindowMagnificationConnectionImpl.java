package com.android.systemui.accessibility;

import android.graphics.Rect;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.view.accessibility.IRemoteMagnificationAnimationCallback;
import android.view.accessibility.IWindowMagnificationConnection;
import android.view.accessibility.IWindowMagnificationConnectionCallback;
/* loaded from: classes.dex */
public class WindowMagnificationConnectionImpl extends IWindowMagnificationConnection.Stub {
    private IWindowMagnificationConnectionCallback mConnectionCallback;
    private final Handler mHandler;
    private final ModeSwitchesController mModeSwitchesController;
    private final WindowMagnification mWindowMagnification;

    public WindowMagnificationConnectionImpl(WindowMagnification windowMagnification, Handler handler, ModeSwitchesController modeSwitchesController) {
        this.mWindowMagnification = windowMagnification;
        this.mHandler = handler;
        this.mModeSwitchesController = modeSwitchesController;
    }

    public void enableWindowMagnification(int i, float f, float f2, float f3, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        this.mHandler.post(new Runnable(i, f, f2, f3, iRemoteMagnificationAnimationCallback) { // from class: com.android.systemui.accessibility.WindowMagnificationConnectionImpl$$ExternalSyntheticLambda3
            public final /* synthetic */ int f$1;
            public final /* synthetic */ float f$2;
            public final /* synthetic */ float f$3;
            public final /* synthetic */ float f$4;
            public final /* synthetic */ IRemoteMagnificationAnimationCallback f$5;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
                this.f$5 = r6;
            }

            @Override // java.lang.Runnable
            public final void run() {
                WindowMagnificationConnectionImpl.$r8$lambda$OC0g841N5ZhjHbyDFzBKBuF0IOo(WindowMagnificationConnectionImpl.this, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
            }
        });
    }

    public /* synthetic */ void lambda$enableWindowMagnification$0(int i, float f, float f2, float f3, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        this.mWindowMagnification.enableWindowMagnification(i, f, f2, f3, iRemoteMagnificationAnimationCallback);
    }

    public /* synthetic */ void lambda$setScale$1(int i, float f) {
        this.mWindowMagnification.setScale(i, f);
    }

    public void setScale(int i, float f) {
        this.mHandler.post(new Runnable(i, f) { // from class: com.android.systemui.accessibility.WindowMagnificationConnectionImpl$$ExternalSyntheticLambda1
            public final /* synthetic */ int f$1;
            public final /* synthetic */ float f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                WindowMagnificationConnectionImpl.$r8$lambda$Rzf78V9HDxPy9zmfsb_45kWo_MI(WindowMagnificationConnectionImpl.this, this.f$1, this.f$2);
            }
        });
    }

    public /* synthetic */ void lambda$disableWindowMagnification$2(int i, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        this.mWindowMagnification.disableWindowMagnification(i, iRemoteMagnificationAnimationCallback);
    }

    public void disableWindowMagnification(int i, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        this.mHandler.post(new Runnable(i, iRemoteMagnificationAnimationCallback) { // from class: com.android.systemui.accessibility.WindowMagnificationConnectionImpl$$ExternalSyntheticLambda5
            public final /* synthetic */ int f$1;
            public final /* synthetic */ IRemoteMagnificationAnimationCallback f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                WindowMagnificationConnectionImpl.$r8$lambda$ysVXK0NW7CCgoUQ2N8_EsDwhy8Q(WindowMagnificationConnectionImpl.this, this.f$1, this.f$2);
            }
        });
    }

    public void moveWindowMagnifier(int i, float f, float f2) {
        this.mHandler.post(new Runnable(i, f, f2) { // from class: com.android.systemui.accessibility.WindowMagnificationConnectionImpl$$ExternalSyntheticLambda2
            public final /* synthetic */ int f$1;
            public final /* synthetic */ float f$2;
            public final /* synthetic */ float f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // java.lang.Runnable
            public final void run() {
                WindowMagnificationConnectionImpl.$r8$lambda$XpOSryAGilW2pyj3FTKsILOXGj0(WindowMagnificationConnectionImpl.this, this.f$1, this.f$2, this.f$3);
            }
        });
    }

    public /* synthetic */ void lambda$moveWindowMagnifier$3(int i, float f, float f2) {
        this.mWindowMagnification.moveWindowMagnifier(i, f, f2);
    }

    public void showMagnificationButton(int i, int i2) {
        this.mHandler.post(new Runnable(i, i2) { // from class: com.android.systemui.accessibility.WindowMagnificationConnectionImpl$$ExternalSyntheticLambda4
            public final /* synthetic */ int f$1;
            public final /* synthetic */ int f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                WindowMagnificationConnectionImpl.$r8$lambda$oJLFfr1VLUoMb3QOWX7IRxp5Vms(WindowMagnificationConnectionImpl.this, this.f$1, this.f$2);
            }
        });
    }

    public /* synthetic */ void lambda$showMagnificationButton$4(int i, int i2) {
        this.mModeSwitchesController.showButton(i, i2);
    }

    public void removeMagnificationButton(int i) {
        this.mHandler.post(new Runnable(i) { // from class: com.android.systemui.accessibility.WindowMagnificationConnectionImpl$$ExternalSyntheticLambda0
            public final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                WindowMagnificationConnectionImpl.$r8$lambda$Bj8F8LRgtyHLOKWg_cvDZYLkCuA(WindowMagnificationConnectionImpl.this, this.f$1);
            }
        });
    }

    public /* synthetic */ void lambda$removeMagnificationButton$5(int i) {
        this.mModeSwitchesController.removeButton(i);
    }

    public void setConnectionCallback(IWindowMagnificationConnectionCallback iWindowMagnificationConnectionCallback) {
        this.mConnectionCallback = iWindowMagnificationConnectionCallback;
    }

    public void onWindowMagnifierBoundsChanged(int i, Rect rect) {
        IWindowMagnificationConnectionCallback iWindowMagnificationConnectionCallback = this.mConnectionCallback;
        if (iWindowMagnificationConnectionCallback != null) {
            try {
                iWindowMagnificationConnectionCallback.onWindowMagnifierBoundsChanged(i, rect);
            } catch (RemoteException e) {
                Log.e("WindowMagnificationConnectionImpl", "Failed to inform bounds changed", e);
            }
        }
    }

    public void onSourceBoundsChanged(int i, Rect rect) {
        IWindowMagnificationConnectionCallback iWindowMagnificationConnectionCallback = this.mConnectionCallback;
        if (iWindowMagnificationConnectionCallback != null) {
            try {
                iWindowMagnificationConnectionCallback.onSourceBoundsChanged(i, rect);
            } catch (RemoteException e) {
                Log.e("WindowMagnificationConnectionImpl", "Failed to inform source bounds changed", e);
            }
        }
    }

    public void onPerformScaleAction(int i, float f) {
        IWindowMagnificationConnectionCallback iWindowMagnificationConnectionCallback = this.mConnectionCallback;
        if (iWindowMagnificationConnectionCallback != null) {
            try {
                iWindowMagnificationConnectionCallback.onPerformScaleAction(i, f);
            } catch (RemoteException e) {
                Log.e("WindowMagnificationConnectionImpl", "Failed to inform performing scale action", e);
            }
        }
    }

    public void onAccessibilityActionPerformed(int i) {
        IWindowMagnificationConnectionCallback iWindowMagnificationConnectionCallback = this.mConnectionCallback;
        if (iWindowMagnificationConnectionCallback != null) {
            try {
                iWindowMagnificationConnectionCallback.onAccessibilityActionPerformed(i);
            } catch (RemoteException e) {
                Log.e("WindowMagnificationConnectionImpl", "Failed to inform an accessibility action is already performed", e);
            }
        }
    }
}
