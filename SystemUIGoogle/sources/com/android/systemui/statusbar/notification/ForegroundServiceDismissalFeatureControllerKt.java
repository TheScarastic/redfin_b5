package com.android.systemui.statusbar.notification;

import com.android.systemui.util.DeviceConfigProxy;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ForegroundServiceDismissalFeatureController.kt */
/* loaded from: classes.dex */
public final class ForegroundServiceDismissalFeatureControllerKt {
    private static Boolean sIsEnabled;

    public static final boolean isEnabled(DeviceConfigProxy deviceConfigProxy) {
        if (sIsEnabled == null) {
            sIsEnabled = Boolean.valueOf(deviceConfigProxy.getBoolean("systemui", "notifications_allow_fgs_dismissal", false));
        }
        Boolean bool = sIsEnabled;
        Intrinsics.checkNotNull(bool);
        return bool.booleanValue();
    }
}
