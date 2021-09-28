package com.google.android.systemui.columbus.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import com.android.internal.logging.UiEventLogger;
import com.google.android.systemui.columbus.ColumbusEvent;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: GestureSensorImpl.kt */
/* loaded from: classes2.dex */
public final class GestureSensorImpl extends GestureSensor {
    private final Sensor accelerometer;
    private final String deviceName;
    private final Sensor gyroscope;
    private final Handler handler;
    private boolean isListening;
    private final boolean isRunningInLowSamplingRate;
    private final SensorManager sensorManager;
    private final TapRT tap;
    private final UiEventLogger uiEventLogger;
    private final GestureSensorEventListener sensorEventListener = new GestureSensorEventListener(this);
    private final long samplingIntervalNs = 2400000;

    public GestureSensorImpl(Context context, UiEventLogger uiEventLogger, Handler handler) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(uiEventLogger, "uiEventLogger");
        Intrinsics.checkNotNullParameter(handler, "handler");
        this.uiEventLogger = uiEventLogger;
        this.handler = handler;
        Object systemService = context.getSystemService("sensor");
        Objects.requireNonNull(systemService, "null cannot be cast to non-null type android.hardware.SensorManager");
        SensorManager sensorManager = (SensorManager) systemService;
        this.sensorManager = sensorManager;
        this.accelerometer = sensorManager.getDefaultSensor(1);
        this.gyroscope = sensorManager.getDefaultSensor(4);
        String str = Build.MODEL;
        this.deviceName = str;
        this.tap = new TapRT(153600000, context.getAssets(), str);
    }

    @Override // com.google.android.systemui.columbus.sensors.Sensor
    public boolean isListening() {
        return this.isListening;
    }

    public void setListening(boolean z) {
        this.isListening = z;
    }

    @Override // com.google.android.systemui.columbus.sensors.Sensor
    public void startListening() {
        this.sensorEventListener.setListening(true, 0);
        this.tap.getLowpassAcc().setPara(1.0f);
        this.tap.getLowpassGyro().setPara(1.0f);
        this.tap.getHighpassAcc().setPara(0.05f);
        this.tap.getHighpassGyro().setPara(0.05f);
        this.tap.getPeakDetector().setMinNoiseTolerate(0.03f);
        this.tap.getPeakDetector().setWindowSize(64);
        this.tap.getValleyDetection().setMinNoiseTolerate(0.015f);
        this.tap.getValleyDetection().setWindowSize(64);
        this.tap.reset(false);
        this.uiEventLogger.log(ColumbusEvent.COLUMBUS_MODE_HIGH_POWER_ACTIVE);
    }

    @Override // com.google.android.systemui.columbus.sensors.Sensor
    public void stopListening() {
        this.sensorEventListener.setListening(false, 0);
        this.uiEventLogger.log(ColumbusEvent.COLUMBUS_MODE_INACTIVE);
    }

    /* compiled from: GestureSensorImpl.kt */
    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public final class GestureSensorEventListener implements SensorEventListener {
        final /* synthetic */ GestureSensorImpl this$0;

        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        /* JADX WARN: Incorrect args count in method signature: ()V */
        public GestureSensorEventListener(GestureSensorImpl gestureSensorImpl) {
            Intrinsics.checkNotNullParameter(gestureSensorImpl, "this$0");
            this.this$0 = gestureSensorImpl;
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent != null) {
                GestureSensorImpl gestureSensorImpl = this.this$0;
                TapRT tapRT = gestureSensorImpl.tap;
                int type = sensorEvent.sensor.getType();
                float[] fArr = sensorEvent.values;
                tapRT.updateData(type, fArr[0], fArr[1], fArr[2], sensorEvent.timestamp, gestureSensorImpl.samplingIntervalNs, gestureSensorImpl.isRunningInLowSamplingRate);
                int checkDoubleTapTiming = gestureSensorImpl.tap.checkDoubleTapTiming(sensorEvent.timestamp);
                if (checkDoubleTapTiming == 1) {
                    gestureSensorImpl.handler.post(new GestureSensorImpl$GestureSensorEventListener$onSensorChanged$1$1(gestureSensorImpl));
                } else if (checkDoubleTapTiming == 2) {
                    gestureSensorImpl.handler.post(new GestureSensorImpl$GestureSensorEventListener$onSensorChanged$1$2(gestureSensorImpl));
                }
            }
        }

        public final void setListening(boolean z, int i) {
            if (!z || this.this$0.accelerometer == null || this.this$0.gyroscope == null) {
                this.this$0.sensorManager.unregisterListener(this.this$0.sensorEventListener);
                this.this$0.setListening(false);
                return;
            }
            this.this$0.sensorManager.registerListener(this.this$0.sensorEventListener, this.this$0.accelerometer, i, this.this$0.handler);
            this.this$0.sensorManager.registerListener(this.this$0.sensorEventListener, this.this$0.gyroscope, i, this.this$0.handler);
            this.this$0.setListening(true);
        }
    }
}
