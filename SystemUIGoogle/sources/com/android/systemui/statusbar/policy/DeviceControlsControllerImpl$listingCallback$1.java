package com.android.systemui.statusbar.policy;

import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.management.ControlsListingController;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DeviceControlsControllerImpl.kt */
/* loaded from: classes.dex */
public final class DeviceControlsControllerImpl$listingCallback$1 implements ControlsListingController.ControlsListingCallback {
    final /* synthetic */ DeviceControlsControllerImpl this$0;

    /* access modifiers changed from: package-private */
    public DeviceControlsControllerImpl$listingCallback$1(DeviceControlsControllerImpl deviceControlsControllerImpl) {
        this.this$0 = deviceControlsControllerImpl;
    }

    @Override // com.android.systemui.controls.management.ControlsListingController.ControlsListingCallback
    public void onServicesUpdated(List<ControlsServiceInfo> list) {
        Intrinsics.checkNotNullParameter(list, "serviceInfos");
        if (!list.isEmpty()) {
            this.this$0.seedFavorites(list);
        }
    }
}
