package com.android.systemui.power;

import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.power.PowerNotificationWarnings;
/* loaded from: classes.dex */
public final /* synthetic */ class PowerNotificationWarnings$1$$ExternalSyntheticLambda0 implements ActivityStarter.Callback {
    public final /* synthetic */ PowerNotificationWarnings.AnonymousClass1 f$0;

    public /* synthetic */ PowerNotificationWarnings$1$$ExternalSyntheticLambda0(PowerNotificationWarnings.AnonymousClass1 r1) {
        this.f$0 = r1;
    }

    @Override // com.android.systemui.plugins.ActivityStarter.Callback
    public final void onActivityStarted(int i) {
        this.f$0.lambda$onClick$0(i);
    }
}
