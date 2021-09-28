package com.android.systemui.util.sensors;
/* loaded from: classes2.dex */
public final /* synthetic */ class ProximitySensor$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ThresholdSensor f$0;

    public /* synthetic */ ProximitySensor$1$$ExternalSyntheticLambda0(ThresholdSensor thresholdSensor) {
        this.f$0 = thresholdSensor;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.resume();
    }
}
