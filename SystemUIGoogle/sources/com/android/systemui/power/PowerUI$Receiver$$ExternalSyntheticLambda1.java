package com.android.systemui.power;

import com.android.systemui.power.PowerUI;
/* loaded from: classes.dex */
public final /* synthetic */ class PowerUI$Receiver$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ PowerUI.Receiver f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ PowerUI$Receiver$$ExternalSyntheticLambda1(PowerUI.Receiver receiver, boolean z, int i) {
        this.f$0 = receiver;
        this.f$1 = z;
        this.f$2 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onReceive$1(this.f$1, this.f$2);
    }
}
