package com.google.android.systemui.elmyra.sensors;

import android.content.Context;
import android.content.res.Resources;
import android.provider.Settings;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dependency;
import com.android.systemui.R$raw;
import com.android.systemui.R$string;
import com.google.android.systemui.elmyra.proto.nano.ChassisProtos$Chassis;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import com.google.android.systemui.elmyra.sensors.config.GestureConfiguration;
import com.google.android.systemui.elmyra.sensors.config.SensorCalibration;
import com.google.protobuf.nano.MessageNano;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
/* loaded from: classes2.dex */
public class JNIGestureSensor implements GestureSensor {
    private static final String DISABLE_SETTING = "com.google.android.systemui.elmyra.disable_jni";
    private static final int SENSOR_RATE = 20000;
    private static final String TAG = "Elmyra/JNIGestureSensor";
    private static boolean sLibraryLoaded;
    private final Context mContext;
    private final AssistGestureController mController;
    private final GestureConfiguration mGestureConfiguration;
    private boolean mIsListening;
    private final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback;
    private long mNativeService;
    private int mSensorCount;
    private final String mSensorStringType;

    private native boolean createNativeService(byte[] bArr);

    private native void destroyNativeService();

    private native boolean setGestureDetector(byte[] bArr);

    private native boolean startListeningNative(String str, int i);

    private native void stopListeningNative();

    private void updateConfiguration() {
    }

    static {
        try {
            System.loadLibrary("elmyra");
            sLibraryLoaded = true;
        } catch (Throwable th) {
            Log.w(TAG, "Could not load JNI component: " + th);
            sLibraryLoaded = false;
        }
    }

    public static boolean isAvailable(Context context) {
        byte[] chassisAsset;
        return (Settings.Secure.getInt(context.getContentResolver(), DISABLE_SETTING, 0) == 1 || !sLibraryLoaded || (chassisAsset = getChassisAsset(context)) == null || chassisAsset.length == 0) ? false : true;
    }

    private static byte[] getChassisAsset(Context context) {
        try {
            return readAllBytes(context.getResources().openRawResource(R$raw.elmyra_chassis));
        } catch (Resources.NotFoundException | IOException e) {
            Log.e(TAG, "Could not load chassis resource", e);
            return null;
        }
    }

    private static byte[] readAllBytes(InputStream inputStream) throws IOException {
        int i = 1024;
        byte[] bArr = new byte[1024];
        int i2 = 0;
        while (true) {
            int read = inputStream.read(bArr, i2, i - i2);
            if (read > 0) {
                i2 += read;
            } else if (read < 0) {
                break;
            } else {
                i <<= 1;
                bArr = Arrays.copyOf(bArr, i);
            }
        }
        return i == i2 ? bArr : Arrays.copyOf(bArr, i2);
    }

    public JNIGestureSensor(Context context, GestureConfiguration gestureConfiguration) {
        AnonymousClass1 r2 = new KeyguardUpdateMonitorCallback() { // from class: com.google.android.systemui.elmyra.sensors.JNIGestureSensor.1
            private boolean mWasListening;

            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onStartedWakingUp() {
                JNIGestureSensor.this.mController.onGestureProgress(0.0f);
                if (this.mWasListening) {
                    JNIGestureSensor.this.startListening();
                }
            }

            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onFinishedGoingToSleep(int i) {
                JNIGestureSensor.this.mController.onGestureProgress(0.0f);
                this.mWasListening = JNIGestureSensor.this.isListening();
                JNIGestureSensor.this.stopListening();
            }
        };
        this.mKeyguardUpdateMonitorCallback = r2;
        this.mContext = context;
        this.mController = new AssistGestureController(context, this, gestureConfiguration);
        this.mSensorStringType = context.getResources().getString(R$string.elmyra_raw_sensor_string_type);
        this.mGestureConfiguration = gestureConfiguration;
        gestureConfiguration.setListener(new GestureConfiguration.Listener() { // from class: com.google.android.systemui.elmyra.sensors.JNIGestureSensor$$ExternalSyntheticLambda0
            @Override // com.google.android.systemui.elmyra.sensors.config.GestureConfiguration.Listener
            public final void onGestureConfigurationChanged(GestureConfiguration gestureConfiguration2) {
                JNIGestureSensor.this.lambda$new$0(gestureConfiguration2);
            }
        });
        ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).registerCallback(r2);
        byte[] chassisAsset = getChassisAsset(context);
        if (chassisAsset != null && chassisAsset.length != 0) {
            try {
                ChassisProtos$Chassis chassisProtos$Chassis = new ChassisProtos$Chassis();
                MessageNano.mergeFrom(chassisProtos$Chassis, chassisAsset);
                this.mSensorCount = chassisProtos$Chassis.sensors.length;
                for (int i = 0; i < this.mSensorCount; i++) {
                    SensorCalibration calibration = SensorCalibration.getCalibration(i);
                    if (calibration == null || !calibration.contains("touch_2_sensitivity")) {
                        Log.w(TAG, "Error reading calibration for sensor " + i);
                    } else {
                        chassisProtos$Chassis.sensors[i].sensitivity = 1.0f / calibration.get("touch_2_sensitivity");
                    }
                }
                createNativeService(chassisAsset);
            } catch (Exception e) {
                Log.e(TAG, "Error reading chassis file", e);
                this.mSensorCount = 0;
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(GestureConfiguration gestureConfiguration) {
        updateConfiguration();
    }

    protected void finalize() throws Throwable {
        super.finalize();
        destroyNativeService();
    }

    @Override // com.google.android.systemui.elmyra.sensors.GestureSensor
    public void setGestureListener(GestureSensor.Listener listener) {
        this.mController.setGestureListener(listener);
    }

    @Override // com.google.android.systemui.elmyra.sensors.Sensor
    public void startListening() {
        if (!this.mIsListening && startListeningNative(this.mSensorStringType, SENSOR_RATE)) {
            updateConfiguration();
            this.mIsListening = true;
        }
    }

    @Override // com.google.android.systemui.elmyra.sensors.Sensor
    public boolean isListening() {
        return this.mIsListening;
    }

    @Override // com.google.android.systemui.elmyra.sensors.Sensor
    public void stopListening() {
        if (this.mIsListening) {
            stopListeningNative();
            this.mIsListening = false;
        }
    }

    private void onGestureDetected() {
        this.mController.onGestureDetected(null);
    }

    private void onGestureProgress(float f) {
        this.mController.onGestureProgress(f);
    }
}
