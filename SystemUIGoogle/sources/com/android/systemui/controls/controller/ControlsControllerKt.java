package com.android.systemui.controls.controller;

import com.android.systemui.controls.ControlStatus;
import com.android.systemui.controls.controller.ControlsController;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsController.kt */
/* loaded from: classes.dex */
public final class ControlsControllerKt {
    public static /* synthetic */ ControlsController.LoadData createLoadDataObject$default(List list, List list2, boolean z, int i, Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        return createLoadDataObject(list, list2, z);
    }

    public static final ControlsController.LoadData createLoadDataObject(List<ControlStatus> list, List<String> list2, boolean z) {
        Intrinsics.checkNotNullParameter(list, "allControls");
        Intrinsics.checkNotNullParameter(list2, "favorites");
        return new ControlsController.LoadData(list, list2, z) { // from class: com.android.systemui.controls.controller.ControlsControllerKt$createLoadDataObject$1
            final /* synthetic */ List<ControlStatus> $allControls;
            final /* synthetic */ boolean $error;
            final /* synthetic */ List<String> $favorites;
            private final List<ControlStatus> allControls;
            private final boolean errorOnLoad;
            private final List<String> favoritesIds;

            /* access modifiers changed from: package-private */
            {
                this.$allControls = r1;
                this.$favorites = r2;
                this.$error = r3;
                this.allControls = r1;
                this.favoritesIds = r2;
                this.errorOnLoad = r3;
            }

            @Override // com.android.systemui.controls.controller.ControlsController.LoadData
            public List<ControlStatus> getAllControls() {
                return this.allControls;
            }

            @Override // com.android.systemui.controls.controller.ControlsController.LoadData
            public List<String> getFavoritesIds() {
                return this.favoritesIds;
            }

            @Override // com.android.systemui.controls.controller.ControlsController.LoadData
            public boolean getErrorOnLoad() {
                return this.errorOnLoad;
            }
        };
    }
}
