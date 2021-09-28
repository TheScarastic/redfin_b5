package com.android.systemui.util.sensors;

import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.sensors.ThresholdSensor;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class ProximitySensor$ProximityCheck$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ ThresholdSensor.ThresholdSensorEvent f$0;

    public /* synthetic */ ProximitySensor$ProximityCheck$$ExternalSyntheticLambda1(ThresholdSensor.ThresholdSensorEvent thresholdSensorEvent) {
        this.f$0 = thresholdSensorEvent;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ProximitySensor.ProximityCheck.lambda$onProximityEvent$0(this.f$0, (Consumer) obj);
    }
}
