package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.LocationController;
import com.android.systemui.statusbar.policy.LocationControllerImpl;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class LocationControllerImpl$H$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ boolean f$0;

    public /* synthetic */ LocationControllerImpl$H$$ExternalSyntheticLambda1(boolean z) {
        this.f$0 = z;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        LocationControllerImpl.H.lambda$locationSettingsChanged$1(this.f$0, (LocationController.LocationChangeCallback) obj);
    }
}
