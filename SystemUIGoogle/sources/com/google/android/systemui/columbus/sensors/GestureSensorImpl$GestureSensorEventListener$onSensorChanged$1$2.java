package com.google.android.systemui.columbus.sensors;

import com.google.android.systemui.columbus.sensors.GestureSensor;
/* compiled from: GestureSensorImpl.kt */
/* loaded from: classes2.dex */
final class GestureSensorImpl$GestureSensorEventListener$onSensorChanged$1$2 implements Runnable {
    final /* synthetic */ GestureSensorImpl this$0;

    /* access modifiers changed from: package-private */
    public GestureSensorImpl$GestureSensorEventListener$onSensorChanged$1$2(GestureSensorImpl gestureSensorImpl) {
        this.this$0 = gestureSensorImpl;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.this$0.reportGestureDetected(1, new GestureSensor.DetectionProperties(false));
    }
}
