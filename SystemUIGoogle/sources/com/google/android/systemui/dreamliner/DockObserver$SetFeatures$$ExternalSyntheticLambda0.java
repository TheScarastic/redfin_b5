package com.google.android.systemui.dreamliner;

import com.google.android.systemui.dreamliner.DockObserver;
import com.google.android.systemui.dreamliner.WirelessCharger;
/* loaded from: classes2.dex */
public final /* synthetic */ class DockObserver$SetFeatures$$ExternalSyntheticLambda0 implements WirelessCharger.SetFeaturesCallback {
    public final /* synthetic */ DockObserver.SetFeatures f$0;

    public /* synthetic */ DockObserver$SetFeatures$$ExternalSyntheticLambda0(DockObserver.SetFeatures setFeatures) {
        this.f$0 = setFeatures;
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger.SetFeaturesCallback
    public final void onCallback(int i) {
        this.f$0.lambda$run$0(i);
    }
}
