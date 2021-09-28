package com.android.systemui.util.sensors;

import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.sensors.ThresholdSensor;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class ThresholdSensorImpl implements ThresholdSensor {
    private static final boolean DEBUG = Log.isLoggable("ThresholdSensor", 3);
    private final Execution mExecution;
    private Boolean mLastBelow;
    private List<ThresholdSensor.Listener> mListeners;
    private boolean mPaused;
    private boolean mRegistered;
    private final Sensor mSensor;
    private int mSensorDelay;
    private SensorEventListener mSensorEventListener;
    private final AsyncSensorManager mSensorManager;
    private String mTag;
    private final float mThreshold;
    private final float mThresholdLatch;

    private ThresholdSensorImpl(AsyncSensorManager asyncSensorManager, Sensor sensor, Execution execution, float f, float f2, int i) {
        this.mListeners = new ArrayList();
        this.mSensorEventListener = new SensorEventListener() { // from class: com.android.systemui.util.sensors.ThresholdSensorImpl.1
            @Override // android.hardware.SensorEventListener
            public void onAccuracyChanged(Sensor sensor2, int i2) {
            }

            @Override // android.hardware.SensorEventListener
            public void onSensorChanged(SensorEvent sensorEvent) {
                boolean z = true;
                boolean z2 = sensorEvent.values[0] < ThresholdSensorImpl.this.mThreshold;
                if (sensorEvent.values[0] < ThresholdSensorImpl.this.mThresholdLatch) {
                    z = false;
                }
                ThresholdSensorImpl.this.logDebug("Sensor value: " + sensorEvent.values[0]);
                ThresholdSensorImpl.this.onSensorEvent(z2, z, sensorEvent.timestamp);
            }
        };
        this.mSensorManager = asyncSensorManager;
        this.mExecution = execution;
        this.mSensor = sensor;
        this.mThreshold = f;
        this.mThresholdLatch = f2;
        this.mSensorDelay = i;
    }

    @Override // com.android.systemui.util.sensors.ThresholdSensor
    public void setTag(String str) {
        this.mTag = str;
    }

    @Override // com.android.systemui.util.sensors.ThresholdSensor
    public void setDelay(int i) {
        if (i != this.mSensorDelay) {
            this.mSensorDelay = i;
            if (isLoaded()) {
                unregisterInternal();
                registerInternal();
            }
        }
    }

    @Override // com.android.systemui.util.sensors.ThresholdSensor
    public boolean isLoaded() {
        return this.mSensor != null;
    }

    @VisibleForTesting
    boolean isRegistered() {
        return this.mRegistered;
    }

    @Override // com.android.systemui.util.sensors.ThresholdSensor
    public void register(ThresholdSensor.Listener listener) {
        this.mExecution.assertIsMainThread();
        if (!this.mListeners.contains(listener)) {
            this.mListeners.add(listener);
        }
        registerInternal();
    }

    @Override // com.android.systemui.util.sensors.ThresholdSensor
    public void pause() {
        this.mExecution.assertIsMainThread();
        this.mPaused = true;
        unregisterInternal();
    }

    @Override // com.android.systemui.util.sensors.ThresholdSensor
    public void resume() {
        this.mExecution.assertIsMainThread();
        this.mPaused = false;
        registerInternal();
    }

    private void alertListenersInternal(boolean z, long j) {
        new ArrayList(this.mListeners).forEach(new Consumer(z, j) { // from class: com.android.systemui.util.sensors.ThresholdSensorImpl$$ExternalSyntheticLambda0
            public final /* synthetic */ boolean f$0;
            public final /* synthetic */ long f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ThresholdSensorImpl.lambda$alertListenersInternal$0(this.f$0, this.f$1, (ThresholdSensor.Listener) obj);
            }
        });
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$alertListenersInternal$0(boolean z, long j, ThresholdSensor.Listener listener) {
        listener.onThresholdCrossed(new ThresholdSensor.ThresholdSensorEvent(z, j));
    }

    private void registerInternal() {
        this.mExecution.assertIsMainThread();
        if (!this.mRegistered && !this.mPaused && !this.mListeners.isEmpty()) {
            logDebug("Registering sensor listener");
            this.mSensorManager.registerListener(this.mSensorEventListener, this.mSensor, this.mSensorDelay);
            this.mRegistered = true;
        }
    }

    private void unregisterInternal() {
        this.mExecution.assertIsMainThread();
        if (this.mRegistered) {
            logDebug("Unregister sensor listener");
            this.mSensorManager.unregisterListener(this.mSensorEventListener);
            this.mRegistered = false;
            this.mLastBelow = null;
        }
    }

    /* access modifiers changed from: private */
    public void onSensorEvent(boolean z, boolean z2, long j) {
        this.mExecution.assertIsMainThread();
        if (this.mRegistered) {
            Boolean bool = this.mLastBelow;
            if (bool != null) {
                if (bool.booleanValue() && !z2) {
                    return;
                }
                if (!this.mLastBelow.booleanValue() && !z) {
                    return;
                }
            }
            this.mLastBelow = Boolean.valueOf(z);
            logDebug("Alerting below: " + z);
            alertListenersInternal(z, j);
        }
    }

    public String toString() {
        return String.format("{isLoaded=%s, registered=%s, paused=%s, threshold=%s, sensor=%s}", Boolean.valueOf(isLoaded()), Boolean.valueOf(this.mRegistered), Boolean.valueOf(this.mPaused), Float.valueOf(this.mThreshold), this.mSensor);
    }

    /* access modifiers changed from: private */
    public void logDebug(String str) {
        String str2;
        if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            if (this.mTag != null) {
                str2 = "[" + this.mTag + "] ";
            } else {
                str2 = "";
            }
            sb.append(str2);
            sb.append(str);
            Log.d("ThresholdSensor", sb.toString());
        }
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class Builder {
        private final Execution mExecution;
        private final Resources mResources;
        private Sensor mSensor;
        private int mSensorDelay = 3;
        private final AsyncSensorManager mSensorManager;
        private boolean mSensorSet;
        private float mThresholdLatchValue;
        private boolean mThresholdLatchValueSet;
        private boolean mThresholdSet;
        private float mThresholdValue;

        /* access modifiers changed from: package-private */
        public Builder(Resources resources, AsyncSensorManager asyncSensorManager, Execution execution) {
            this.mResources = resources;
            this.mSensorManager = asyncSensorManager;
            this.mExecution = execution;
        }

        /* access modifiers changed from: package-private */
        public Builder setSensorDelay(int i) {
            this.mSensorDelay = i;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder setSensorResourceId(int i, boolean z) {
            setSensorType(this.mResources.getString(i), z);
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder setThresholdResourceId(int i) {
            try {
                setThresholdValue(this.mResources.getFloat(i));
            } catch (Resources.NotFoundException unused) {
            }
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder setThresholdLatchResourceId(int i) {
            try {
                setThresholdLatchValue(this.mResources.getFloat(i));
            } catch (Resources.NotFoundException unused) {
            }
            return this;
        }

        Builder setSensorType(String str, boolean z) {
            Sensor findSensorByType = findSensorByType(str, z);
            if (findSensorByType != null) {
                setSensor(findSensorByType);
            }
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder setThresholdValue(float f) {
            this.mThresholdValue = f;
            this.mThresholdSet = true;
            if (!this.mThresholdLatchValueSet) {
                this.mThresholdLatchValue = f;
            }
            return this;
        }

        Builder setThresholdLatchValue(float f) {
            this.mThresholdLatchValue = f;
            this.mThresholdLatchValueSet = true;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder setSensor(Sensor sensor) {
            this.mSensor = sensor;
            this.mSensorSet = true;
            return this;
        }

        public ThresholdSensor build() {
            if (!this.mSensorSet) {
                throw new IllegalStateException("A sensor was not successfully set.");
            } else if (!this.mThresholdSet) {
                throw new IllegalStateException("A threshold was not successfully set.");
            } else if (this.mThresholdValue <= this.mThresholdLatchValue) {
                return new ThresholdSensorImpl(this.mSensorManager, this.mSensor, this.mExecution, this.mThresholdValue, this.mThresholdLatchValue, this.mSensorDelay);
            } else {
                throw new IllegalStateException("Threshold must be less than or equal to Threshold Latch");
            }
        }

        @VisibleForTesting
        Sensor findSensorByType(String str, boolean z) {
            Sensor sensor = null;
            if (str.isEmpty()) {
                return null;
            }
            for (Sensor sensor2 : this.mSensorManager.getSensorList(-1)) {
                if (str.equals(sensor2.getStringType())) {
                    if (!z || sensor2.isWakeUpSensor()) {
                        return sensor2;
                    }
                    sensor = sensor2;
                }
            }
            return sensor;
        }
    }
}
