package com.android.systemui.shared.system;

import android.app.ActivityManager;
import android.os.SystemProperties;
import android.view.CrossWindowBlurListeners;
/* loaded from: classes.dex */
public abstract class BlurUtils {
    public static boolean supportsBlursOnWindows() {
        if (!CrossWindowBlurListeners.CROSS_WINDOW_BLUR_SUPPORTED || !ActivityManager.isHighEndGfx() || SystemProperties.getBoolean("persist.sysui.disableBlur", false)) {
            return false;
        }
        return true;
    }
}
