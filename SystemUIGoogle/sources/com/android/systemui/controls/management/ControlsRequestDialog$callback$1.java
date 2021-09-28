package com.android.systemui.controls.management;

import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.management.ControlsListingController;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsRequestDialog.kt */
/* loaded from: classes.dex */
public final class ControlsRequestDialog$callback$1 implements ControlsListingController.ControlsListingCallback {
    @Override // com.android.systemui.controls.management.ControlsListingController.ControlsListingCallback
    public void onServicesUpdated(List<ControlsServiceInfo> list) {
        Intrinsics.checkNotNullParameter(list, "serviceInfos");
    }
}
