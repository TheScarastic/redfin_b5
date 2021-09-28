package com.google.android.systemui.fingerprint;

import android.util.Log;
import android.view.Surface;
/* loaded from: classes2.dex */
public class UdfpsGhbmProvider {
    private static final String TAG = "UdfpsGhbmProvider";

    private native void disableGhbmNative(Surface surface);

    private native void enableGhbmNative(Surface surface);

    public void enableGhbm(Surface surface) {
        Log.v(TAG, "enableGhbm");
        enableGhbmNative(surface);
    }

    public void disableGhbm(Surface surface) {
        Log.v(TAG, "disableGhbm");
        disableGhbmNative(surface);
    }

    static {
        try {
            System.loadLibrary("udfps_ghbm_jni");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Failed to load udfps_ghbm_jni.so", e);
        }
    }
}
