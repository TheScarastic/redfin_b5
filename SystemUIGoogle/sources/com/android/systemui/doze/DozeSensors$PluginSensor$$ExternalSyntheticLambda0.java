package com.android.systemui.doze;

import com.android.systemui.doze.DozeSensors;
import com.android.systemui.plugins.SensorManagerPlugin;
/* loaded from: classes.dex */
public final /* synthetic */ class DozeSensors$PluginSensor$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ DozeSensors.PluginSensor f$0;
    public final /* synthetic */ SensorManagerPlugin.SensorEvent f$1;

    public /* synthetic */ DozeSensors$PluginSensor$$ExternalSyntheticLambda0(DozeSensors.PluginSensor pluginSensor, SensorManagerPlugin.SensorEvent sensorEvent) {
        this.f$0 = pluginSensor;
        this.f$1 = sensorEvent;
    }

    @Override // java.lang.Runnable
    public final void run() {
        DozeSensors.PluginSensor.m91$r8$lambda$JfmpgeLP4DiPTgosDukVPI0ViI(this.f$0, this.f$1);
    }
}
