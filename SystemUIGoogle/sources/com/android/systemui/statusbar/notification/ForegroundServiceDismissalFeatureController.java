package com.android.systemui.statusbar.notification;

import android.content.Context;
import com.android.systemui.util.DeviceConfigProxy;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ForegroundServiceDismissalFeatureController.kt */
/* loaded from: classes.dex */
public final class ForegroundServiceDismissalFeatureController {
    private final Context context;
    private final DeviceConfigProxy proxy;

    public ForegroundServiceDismissalFeatureController(DeviceConfigProxy deviceConfigProxy, Context context) {
        Intrinsics.checkNotNullParameter(deviceConfigProxy, "proxy");
        Intrinsics.checkNotNullParameter(context, "context");
        this.proxy = deviceConfigProxy;
        this.context = context;
    }

    public final boolean isForegroundServiceDismissalEnabled() {
        return ForegroundServiceDismissalFeatureControllerKt.access$isEnabled(this.proxy);
    }
}
