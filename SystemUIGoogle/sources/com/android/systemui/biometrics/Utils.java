package com.android.systemui.biometrics;

import android.content.Context;
import android.hardware.biometrics.PromptInfo;
import android.hardware.biometrics.SensorPropertiesInternal;
import android.os.UserManager;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.widget.LockPatternUtils;
import java.util.List;
/* loaded from: classes.dex */
public class Utils {
    /* access modifiers changed from: package-private */
    public static float dpToPixels(Context context, float f) {
        return f * (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
    }

    /* access modifiers changed from: package-private */
    public static void notifyAccessibilityContentChanged(AccessibilityManager accessibilityManager, ViewGroup viewGroup) {
        if (accessibilityManager.isEnabled()) {
            AccessibilityEvent obtain = AccessibilityEvent.obtain();
            obtain.setEventType(2048);
            obtain.setContentChangeTypes(1);
            viewGroup.sendAccessibilityEventUnchecked(obtain);
            viewGroup.notifySubtreeAccessibilityStateChanged(viewGroup, viewGroup, 1);
        }
    }

    /* access modifiers changed from: package-private */
    public static boolean isDeviceCredentialAllowed(PromptInfo promptInfo) {
        return (promptInfo.getAuthenticators() & 32768) != 0;
    }

    /* access modifiers changed from: package-private */
    public static boolean isBiometricAllowed(PromptInfo promptInfo) {
        return (promptInfo.getAuthenticators() & 255) != 0;
    }

    /* access modifiers changed from: package-private */
    public static int getCredentialType(Context context, int i) {
        int keyguardStoredPasswordQuality = new LockPatternUtils(context).getKeyguardStoredPasswordQuality(i);
        if (keyguardStoredPasswordQuality != 65536) {
            return (keyguardStoredPasswordQuality == 131072 || keyguardStoredPasswordQuality == 196608) ? 1 : 3;
        }
        return 2;
    }

    /* access modifiers changed from: package-private */
    public static boolean isManagedProfile(Context context, int i) {
        return ((UserManager) context.getSystemService(UserManager.class)).isManagedProfile(i);
    }

    /* access modifiers changed from: package-private */
    public static boolean containsSensorId(List<? extends SensorPropertiesInternal> list, int i) {
        if (list == null) {
            return false;
        }
        for (SensorPropertiesInternal sensorPropertiesInternal : list) {
            if (sensorPropertiesInternal.sensorId == i) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public static boolean isSystem(Context context, String str) {
        return (context.checkCallingOrSelfPermission("android.permission.USE_BIOMETRIC_INTERNAL") == 0) && "android".equals(str);
    }
}
