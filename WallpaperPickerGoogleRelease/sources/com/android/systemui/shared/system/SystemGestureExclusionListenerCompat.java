package com.android.systemui.shared.system;

import android.graphics.Region;
import android.os.RemoteException;
import android.util.Log;
import android.view.ISystemGestureExclusionListener;
import android.view.WindowManagerGlobal;
/* loaded from: classes.dex */
public abstract class SystemGestureExclusionListenerCompat {
    private static final String TAG = "SGEListenerCompat";
    private final int mDisplayId;
    private ISystemGestureExclusionListener mGestureExclusionListener = new ISystemGestureExclusionListener.Stub() { // from class: com.android.systemui.shared.system.SystemGestureExclusionListenerCompat.1
        public void onSystemGestureExclusionChanged(int i, Region region, Region region2) {
            if (i == SystemGestureExclusionListenerCompat.this.mDisplayId) {
                if (region2 == null) {
                    region2 = region;
                }
                SystemGestureExclusionListenerCompat.this.onExclusionChanged(region, region2);
            }
        }
    };
    private boolean mRegistered;

    public SystemGestureExclusionListenerCompat(int i) {
        this.mDisplayId = i;
    }

    public abstract void onExclusionChanged(Region region);

    public void onExclusionChanged(Region region, Region region2) {
        onExclusionChanged(region);
    }

    public void register() {
        if (!this.mRegistered) {
            try {
                WindowManagerGlobal.getWindowManagerService().registerSystemGestureExclusionListener(this.mGestureExclusionListener, this.mDisplayId);
                this.mRegistered = true;
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to register window manager callbacks", e);
            }
        }
    }

    public void unregister() {
        if (this.mRegistered) {
            try {
                WindowManagerGlobal.getWindowManagerService().unregisterSystemGestureExclusionListener(this.mGestureExclusionListener, this.mDisplayId);
                this.mRegistered = false;
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to unregister window manager callbacks", e);
            }
        }
    }
}
