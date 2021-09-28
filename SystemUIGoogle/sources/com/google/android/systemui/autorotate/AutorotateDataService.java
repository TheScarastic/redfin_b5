package com.google.android.systemui.autorotate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.DeviceConfig;
import com.android.internal.util.LatencyTracker;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.Set;
import java.util.concurrent.TimeUnit;
/* loaded from: classes2.dex */
public class AutorotateDataService {
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final Context mContext;
    private Sensor mDebugSensor;
    private final DeviceConfigProxy mDeviceConfig;
    private LatencyTracker mLatencyTracker;
    private final DelayableExecutor mMainExecutor;
    private Sensor mPreindicationSensor;
    private boolean mRawSensorLoggingEnabled;
    private boolean mRotationPreindicationEnabled;
    private final DataLogger mSensorDataLogger;
    private final SensorManager mSensorManager;
    private boolean mServiceRunning = false;
    private int mLastPreIndication = -1;
    private SensorData[] mSensorData = new SensorData[600];
    private int mSensorDataIndex = 0;
    private final BroadcastReceiver mScreenEventBroadcastReceiver = new BroadcastReceiver() { // from class: com.google.android.systemui.autorotate.AutorotateDataService.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                AutorotateDataService.this.registerRequiredSensors();
            } else if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                AutorotateDataService.this.unregisterSensors();
                AutorotateDataService.this.mLastPreIndication = -1;
            }
        }
    };
    private final SensorListener mSensorListener = new SensorListener();

    static /* synthetic */ int access$608(AutorotateDataService autorotateDataService) {
        int i = autorotateDataService.mSensorDataIndex;
        autorotateDataService.mSensorDataIndex = i + 1;
        return i;
    }

    public AutorotateDataService(Context context, SensorManager sensorManager, DataLogger dataLogger, BroadcastDispatcher broadcastDispatcher, DeviceConfigProxy deviceConfigProxy, DelayableExecutor delayableExecutor) {
        this.mContext = context;
        this.mMainExecutor = delayableExecutor;
        this.mSensorDataLogger = dataLogger;
        this.mSensorManager = sensorManager;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mDeviceConfig = deviceConfigProxy;
    }

    public void init() {
        this.mLatencyTracker = LatencyTracker.getInstance(this.mContext);
        readFlagsToControlSensorLogging();
        this.mDeviceConfig.addOnPropertiesChangedListener("window_manager", this.mMainExecutor, new DeviceConfig.OnPropertiesChangedListener() { // from class: com.google.android.systemui.autorotate.AutorotateDataService$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                AutorotateDataService.this.lambda$init$0(properties);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$0(DeviceConfig.Properties properties) {
        Set keyset = properties.getKeyset();
        if (keyset.contains("log_raw_sensor_data") || keyset.contains("log_rotation_preindication")) {
            readFlagsToControlSensorLogging();
        }
    }

    private void readFlagsToControlSensorLogging() {
        Sensor sensor;
        Sensor sensor2;
        this.mRawSensorLoggingEnabled = DeviceConfig.getBoolean("window_manager", "log_raw_sensor_data", false);
        boolean z = DeviceConfig.getBoolean("window_manager", "log_rotation_preindication", false);
        this.mRotationPreindicationEnabled = z;
        boolean z2 = this.mRawSensorLoggingEnabled;
        if (z2 || z) {
            if (z2 || z) {
                startService();
                registerRequiredSensors();
            }
            if (!this.mRawSensorLoggingEnabled && (sensor2 = this.mDebugSensor) != null) {
                this.mSensorManager.unregisterListener(this.mSensorListener, sensor2);
            }
            if (!this.mRotationPreindicationEnabled && (sensor = this.mPreindicationSensor) != null) {
                this.mSensorManager.unregisterListener(this.mSensorListener, sensor);
                return;
            }
            return;
        }
        stopService();
        unregisterSensors();
    }

    /* loaded from: classes2.dex */
    private class SensorDataReadyRunnable implements Runnable {
        private int mRotation;
        private long mRotationTimestampMillis;

        SensorDataReadyRunnable(int i, long j) {
            this.mRotation = i;
            this.mRotationTimestampMillis = j;
        }

        @Override // java.lang.Runnable
        public void run() {
            AutorotateDataService.this.mSensorDataLogger.setDeviceRotatedData(AutorotateDataService.this.mSensorData, this.mRotation);
            AutorotateDataService.this.mSensorDataLogger.pushDeviceRotatedEvent(this.mRotationTimestampMillis, this.mRotation, 3);
        }
    }

    /* access modifiers changed from: private */
    public void registerRequiredSensors() {
        registerDeviceOrientationSensor();
        if (this.mRawSensorLoggingEnabled) {
            registerDebugSensor();
        }
        if (this.mRotationPreindicationEnabled) {
            registerPreindicationSensor();
        }
    }

    private void registerDebugSensor() {
        Sensor defaultSensor = this.mSensorManager.getDefaultSensor(65548);
        this.mDebugSensor = defaultSensor;
        this.mSensorManager.registerListener(this.mSensorListener, defaultSensor, 1);
    }

    private void registerPreindicationSensor() {
        Sensor defaultSensor = this.mSensorManager.getDefaultSensor(65553);
        this.mPreindicationSensor = defaultSensor;
        this.mSensorManager.registerListener(this.mSensorListener, defaultSensor, 1);
    }

    private void registerDeviceOrientationSensor() {
        this.mSensorManager.registerListener(this.mSensorListener, this.mSensorManager.getDefaultSensor(27), 1);
    }

    /* access modifiers changed from: private */
    public void unregisterSensors() {
        this.mSensorManager.unregisterListener(this.mSensorListener);
        this.mSensorDataLogger.clearData();
    }

    private void startService() {
        if (!this.mServiceRunning) {
            this.mSensorDataLogger.registerPullAtomCallback();
            listenForScreenEvents();
            this.mServiceRunning = true;
        }
    }

    private void stopService() {
        if (this.mServiceRunning) {
            this.mSensorDataLogger.unregisterPullAtomCallback();
            this.mBroadcastDispatcher.unregisterReceiver(this.mScreenEventBroadcastReceiver);
            unregisterSensors();
            this.mServiceRunning = false;
        }
    }

    private void listenForScreenEvents() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        this.mBroadcastDispatcher.registerReceiver(this.mScreenEventBroadcastReceiver, intentFilter, null);
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class SensorListener implements SensorEventListener {
        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        private SensorListener() {
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == 27) {
                int i = (int) sensorEvent.values[0];
                if (i >= 0 && i <= 3) {
                    AutorotateDataService.this.mSensorData = new SensorData[600];
                    AutorotateDataService.this.mSensorDataIndex = 0;
                    AutorotateDataService.this.mMainExecutor.executeDelayed(new SensorDataReadyRunnable(i, sensorEvent.timestamp / 1000000), 2300, TimeUnit.MILLISECONDS);
                    if (AutorotateDataService.this.mPreindicationSensor != null && i == AutorotateDataService.this.mLastPreIndication) {
                        AutorotateDataService.this.mLatencyTracker.onActionEnd(9);
                    }
                }
            } else if (sensorEvent.sensor.getType() == 65548) {
                if (AutorotateDataService.this.mSensorDataIndex < 600) {
                    SensorData[] sensorDataArr = AutorotateDataService.this.mSensorData;
                    int i2 = AutorotateDataService.this.mSensorDataIndex;
                    float[] fArr = sensorEvent.values;
                    sensorDataArr[i2] = new SensorData(fArr[0], fArr[1], fArr[2], (int) fArr[3], sensorEvent.timestamp / 1000000);
                    AutorotateDataService.access$608(AutorotateDataService.this);
                    return;
                }
                AutorotateDataService.this.mSensorDataIndex = 0;
            } else if (sensorEvent.sensor.getType() == 65553 && AutorotateDataService.this.mPreindicationSensor != null) {
                int i3 = (int) sensorEvent.values[0];
                AutorotateDataService.this.mLatencyTracker.onActionStart(9);
                AutorotateDataService.this.mLastPreIndication = i3;
                AutorotateDataService.this.mSensorDataLogger.pushDeviceRotatedEvent(sensorEvent.timestamp / 1000000, i3, 1);
            }
        }
    }
}
