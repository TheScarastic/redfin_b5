package com.google.android.systemui.elmyra.gates;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import com.android.systemui.Dependency;
import com.android.systemui.R$integer;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.google.android.systemui.elmyra.gates.Gate;
/* loaded from: classes2.dex */
public class KeyguardProximity extends Gate {
    private final Gate.Listener mGateListener;
    private final KeyguardVisibility mKeyguardGate;
    private final float mProximityThreshold;
    private final Sensor mSensor;
    private final SensorManager mSensorManager;
    private final SensorEventListener mSensorListener = new SensorEventListener() { // from class: com.google.android.systemui.elmyra.gates.KeyguardProximity.2
        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            boolean z = false;
            if (sensorEvent.values[0] < KeyguardProximity.this.mProximityThreshold) {
                z = true;
            }
            if (KeyguardProximity.this.mIsListening && z != KeyguardProximity.this.mProximityBlocked) {
                KeyguardProximity.this.mProximityBlocked = z;
                KeyguardProximity.this.notifyListener();
            }
        }
    };
    private boolean mIsListening = false;
    private boolean mProximityBlocked = false;

    public KeyguardProximity(Context context) {
        super(context);
        AnonymousClass1 r0 = new Gate.Listener() { // from class: com.google.android.systemui.elmyra.gates.KeyguardProximity.1
            @Override // com.google.android.systemui.elmyra.gates.Gate.Listener
            public void onGateChanged(Gate gate) {
                KeyguardProximity.this.updateProximityListener();
            }
        };
        this.mGateListener = r0;
        SensorManager sensorManager = (SensorManager) Dependency.get(AsyncSensorManager.class);
        this.mSensorManager = sensorManager;
        Sensor defaultSensor = sensorManager.getDefaultSensor(8);
        this.mSensor = defaultSensor;
        if (defaultSensor == null) {
            this.mProximityThreshold = 0.0f;
            this.mKeyguardGate = null;
            Log.e("Elmyra/KeyguardProximity", "Could not find any Sensor.TYPE_PROXIMITY");
            return;
        }
        this.mProximityThreshold = Math.min(defaultSensor.getMaximumRange(), (float) context.getResources().getInteger(R$integer.elmyra_keyguard_proximity_threshold));
        KeyguardVisibility keyguardVisibility = new KeyguardVisibility(context);
        this.mKeyguardGate = keyguardVisibility;
        keyguardVisibility.setListener(r0);
        updateProximityListener();
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected void onActivate() {
        if (this.mSensor != null) {
            this.mKeyguardGate.activate();
            updateProximityListener();
        }
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected void onDeactivate() {
        if (this.mSensor != null) {
            this.mKeyguardGate.deactivate();
            updateProximityListener();
        }
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected boolean isBlocked() {
        return this.mIsListening && this.mProximityBlocked;
    }

    /* access modifiers changed from: private */
    public void updateProximityListener() {
        if (this.mProximityBlocked) {
            this.mProximityBlocked = false;
            notifyListener();
        }
        if (!isActive() || !this.mKeyguardGate.isKeyguardShowing() || this.mKeyguardGate.isKeyguardOccluded()) {
            this.mSensorManager.unregisterListener(this.mSensorListener);
            this.mIsListening = false;
        } else if (!this.mIsListening) {
            this.mSensorManager.registerListener(this.mSensorListener, this.mSensor, 3);
            this.mIsListening = true;
        }
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public String toString() {
        return super.toString() + " [mIsListening -> " + this.mIsListening + "]";
    }
}
