package com.android.systemui.qs.tiles;

import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.management.ControlsListingController;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DeviceControlsTile.kt */
/* loaded from: classes.dex */
public final class DeviceControlsTile$listingCallback$1 implements ControlsListingController.ControlsListingCallback {
    final /* synthetic */ DeviceControlsTile this$0;

    /* access modifiers changed from: package-private */
    public DeviceControlsTile$listingCallback$1(DeviceControlsTile deviceControlsTile) {
        this.this$0 = deviceControlsTile;
    }

    @Override // com.android.systemui.controls.management.ControlsListingController.ControlsListingCallback
    public void onServicesUpdated(List<ControlsServiceInfo> list) {
        Intrinsics.checkNotNullParameter(list, "serviceInfos");
        if (this.this$0.hasControlsApps.compareAndSet(list.isEmpty(), !list.isEmpty())) {
            this.this$0.refreshState();
        }
    }
}
