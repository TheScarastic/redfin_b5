package com.android.systemui.shared.system;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import android.view.IRotationWatcher;
import android.view.WindowManagerGlobal;
/* loaded from: classes.dex */
public abstract class RotationWatcher {
    private static final String TAG = "RotationWatcher";
    private final Context mContext;
    private final IRotationWatcher mWatcher = new IRotationWatcher.Stub() { // from class: com.android.systemui.shared.system.RotationWatcher.1
        public void onRotationChanged(int i) {
            RotationWatcher.this.onRotationChanged(i);
        }
    };
    private boolean mIsWatching = false;

    public RotationWatcher(Context context) {
        this.mContext = context;
    }

    public void disable() {
        if (this.mIsWatching) {
            try {
                WindowManagerGlobal.getWindowManagerService().removeRotationWatcher(this.mWatcher);
                this.mIsWatching = false;
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to remove rotation watcher", e);
            }
        }
    }

    public void enable() {
        if (!this.mIsWatching) {
            try {
                WindowManagerGlobal.getWindowManagerService().watchRotation(this.mWatcher, this.mContext.getDisplayId());
                this.mIsWatching = true;
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to set rotation watcher", e);
            }
        }
    }

    public abstract void onRotationChanged(int i);
}
