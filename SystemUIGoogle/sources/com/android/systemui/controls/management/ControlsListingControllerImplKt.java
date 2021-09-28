package com.android.systemui.controls.management;

import android.content.Context;
import com.android.settingslib.applications.ServiceListing;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsListingControllerImpl.kt */
/* loaded from: classes.dex */
public final class ControlsListingControllerImplKt {
    /* access modifiers changed from: private */
    public static final ServiceListing createServiceListing(Context context) {
        ServiceListing.Builder builder = new ServiceListing.Builder(context);
        builder.setIntentAction("android.service.controls.ControlsProviderService");
        builder.setPermission("android.permission.BIND_CONTROLS");
        builder.setNoun("Controls Provider");
        builder.setSetting("controls_providers");
        builder.setTag("controls_providers");
        builder.setAddDeviceLockedFlags(true);
        ServiceListing build = builder.build();
        Intrinsics.checkNotNullExpressionValue(build, "Builder(context).apply {\n        setIntentAction(ControlsProviderService.SERVICE_CONTROLS)\n        setPermission(\"android.permission.BIND_CONTROLS\")\n        setNoun(\"Controls Provider\")\n        setSetting(\"controls_providers\")\n        setTag(\"controls_providers\")\n        setAddDeviceLockedFlags(true)\n    }.build()");
        return build;
    }
}
