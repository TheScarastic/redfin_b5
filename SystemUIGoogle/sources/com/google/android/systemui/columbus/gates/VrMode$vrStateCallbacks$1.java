package com.google.android.systemui.columbus.gates;

import android.service.vr.IVrStateCallbacks;
/* compiled from: VrMode.kt */
/* loaded from: classes2.dex */
public final class VrMode$vrStateCallbacks$1 extends IVrStateCallbacks.Stub {
    final /* synthetic */ VrMode this$0;

    /* access modifiers changed from: package-private */
    public VrMode$vrStateCallbacks$1(VrMode vrMode) {
        this.this$0 = vrMode;
    }

    public void onVrStateChanged(boolean z) {
        VrMode.access$setInVrMode$p(this.this$0, z);
        VrMode.access$updateBlocking(this.this$0);
    }
}
