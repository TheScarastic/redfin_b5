package com.android.systemui.controls.management;

import android.view.View;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.management.ControlsListingController;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsFavoritingActivity.kt */
/* loaded from: classes.dex */
public final class ControlsFavoritingActivity$listingCallback$1 implements ControlsListingController.ControlsListingCallback {
    final /* synthetic */ ControlsFavoritingActivity this$0;

    /* access modifiers changed from: package-private */
    public ControlsFavoritingActivity$listingCallback$1(ControlsFavoritingActivity controlsFavoritingActivity) {
        this.this$0 = controlsFavoritingActivity;
    }

    @Override // com.android.systemui.controls.management.ControlsListingController.ControlsListingCallback
    public void onServicesUpdated(List<ControlsServiceInfo> list) {
        Intrinsics.checkNotNullParameter(list, "serviceInfos");
        if (list.size() > 1) {
            View view = this.this$0.otherAppsButton;
            if (view != null) {
                view.post(new Runnable(this.this$0) { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$listingCallback$1$onServicesUpdated$1
                    final /* synthetic */ ControlsFavoritingActivity this$0;

                    /* access modifiers changed from: package-private */
                    {
                        this.this$0 = r1;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        View view2 = this.this$0.otherAppsButton;
                        if (view2 != null) {
                            view2.setVisibility(0);
                        } else {
                            Intrinsics.throwUninitializedPropertyAccessException("otherAppsButton");
                            throw null;
                        }
                    }
                });
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("otherAppsButton");
                throw null;
            }
        }
    }
}
