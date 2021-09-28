package com.android.systemui.doze;

import android.hardware.TriggerEvent;
import com.android.systemui.doze.DozeSensors;
/* loaded from: classes.dex */
public final /* synthetic */ class DozeSensors$TriggerSensor$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ DozeSensors.TriggerSensor f$0;
    public final /* synthetic */ TriggerEvent f$1;

    public /* synthetic */ DozeSensors$TriggerSensor$$ExternalSyntheticLambda0(DozeSensors.TriggerSensor triggerSensor, TriggerEvent triggerEvent) {
        this.f$0 = triggerSensor;
        this.f$1 = triggerEvent;
    }

    @Override // java.lang.Runnable
    public final void run() {
        DozeSensors.TriggerSensor.$r8$lambda$a7rwa_WkvE66du18mwcePhKGgLY(this.f$0, this.f$1);
    }
}
