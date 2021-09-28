package com.android.systemui.shared.system;

import android.os.RemoteException;
import android.util.Log;
import android.view.SurfaceControl;
import android.view.WindowManagerGlobal;
/* loaded from: classes.dex */
public class WindowManagerWrapper {
    private static final WindowManagerWrapper sInstance = new WindowManagerWrapper();

    public static WindowManagerWrapper getInstance() {
        return sInstance;
    }

    public void setNavBarVirtualKeyHapticFeedbackEnabled(boolean z) {
        try {
            WindowManagerGlobal.getWindowManagerService().setNavBarVirtualKeyHapticFeedbackEnabled(z);
        } catch (RemoteException e) {
            Log.w("WindowManagerWrapper", "Failed to enable or disable navigation bar button haptics: ", e);
        }
    }

    public boolean hasSoftNavigationBar(int i) {
        try {
            return WindowManagerGlobal.getWindowManagerService().hasNavigationBar(i);
        } catch (RemoteException unused) {
            return false;
        }
    }

    public SurfaceControl mirrorDisplay(int i) {
        try {
            SurfaceControl surfaceControl = new SurfaceControl();
            WindowManagerGlobal.getWindowManagerService().mirrorDisplay(i, surfaceControl);
            return surfaceControl;
        } catch (RemoteException e) {
            Log.e("WindowManagerWrapper", "Unable to reach window manager", e);
            return null;
        }
    }
}
