package com.android.systemui.doze;

import android.app.ActivityManager;
import android.content.Context;
import android.database.ContentObserver;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.hardware.display.AmbientDisplayConfiguration;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.IndentingPrintWriter;
import android.util.Log;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.plugins.SensorManagerPlugin;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.sensors.ThresholdSensor;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.util.wakelock.WakeLock;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class DozeSensors {
    private static final boolean DEBUG = DozeService.DEBUG;
    private static final UiEventLogger UI_EVENT_LOGGER = new UiEventLoggerImpl();
    private final Callback mCallback;
    private final AmbientDisplayConfiguration mConfig;
    private final Context mContext;
    private long mDebounceFrom;
    private final Handler mHandler;
    private boolean mListening;
    private boolean mListeningProxSensors;
    private boolean mListeningTouchScreenSensors;
    private final Consumer<Boolean> mProxCallback;
    private final ProximitySensor mProximitySensor;
    private final boolean mScreenOffUdfpsEnabled;
    private final SecureSettings mSecureSettings;
    private boolean mSelectivelyRegisterProxSensors;
    private final AsyncSensorManager mSensorManager;
    protected TriggerSensor[] mSensors;
    private boolean mSettingRegistered;
    private final ContentObserver mSettingsObserver;
    private final WakeLock mWakeLock;

    /* loaded from: classes.dex */
    public interface Callback {
        void onSensorPulse(int i, float f, float f2, float[] fArr);
    }

    /* loaded from: classes.dex */
    public enum DozeSensorsUiEvent implements UiEventLogger.UiEventEnum {
        ACTION_AMBIENT_GESTURE_PICKUP(459);
        
        private final int mId;

        DozeSensorsUiEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    public DozeSensors(Context context, AsyncSensorManager asyncSensorManager, DozeParameters dozeParameters, AmbientDisplayConfiguration ambientDisplayConfiguration, WakeLock wakeLock, Callback callback, Consumer<Boolean> consumer, DozeLog dozeLog, ProximitySensor proximitySensor, SecureSettings secureSettings, AuthController authController) {
        Handler handler = new Handler();
        this.mHandler = handler;
        this.mSettingsObserver = new ContentObserver(handler) { // from class: com.android.systemui.doze.DozeSensors.1
            public void onChange(boolean z, Collection<Uri> collection, int i, int i2) {
                if (i2 == ActivityManager.getCurrentUser()) {
                    for (TriggerSensor triggerSensor : DozeSensors.this.mSensors) {
                        triggerSensor.updateListening();
                    }
                }
            }
        };
        this.mContext = context;
        this.mSensorManager = asyncSensorManager;
        this.mConfig = ambientDisplayConfiguration;
        this.mWakeLock = wakeLock;
        this.mProxCallback = consumer;
        this.mSecureSettings = secureSettings;
        this.mCallback = callback;
        this.mProximitySensor = proximitySensor;
        proximitySensor.setTag("DozeSensors");
        boolean selectivelyRegisterSensorsUsingProx = dozeParameters.getSelectivelyRegisterSensorsUsingProx();
        this.mSelectivelyRegisterProxSensors = selectivelyRegisterSensorsUsingProx;
        this.mListeningProxSensors = !selectivelyRegisterSensorsUsingProx;
        boolean screenOffUdfpsEnabled = ambientDisplayConfiguration.screenOffUdfpsEnabled(KeyguardUpdateMonitor.getCurrentUser());
        this.mScreenOffUdfpsEnabled = screenOffUdfpsEnabled;
        boolean isUdfpsEnrolled = authController.isUdfpsEnrolled(KeyguardUpdateMonitor.getCurrentUser());
        boolean alwaysOnEnabled = ambientDisplayConfiguration.alwaysOnEnabled(-2);
        TriggerSensor[] triggerSensorArr = new TriggerSensor[9];
        triggerSensorArr[0] = new TriggerSensor(this, asyncSensorManager.getDefaultSensor(17), null, dozeParameters.getPulseOnSigMotion(), 2, false, false, dozeLog);
        triggerSensorArr[1] = new TriggerSensor(asyncSensorManager.getDefaultSensor(25), "doze_pulse_on_pick_up", true, ambientDisplayConfiguration.dozePickupSensorAvailable(), 3, false, false, false, false, dozeLog);
        triggerSensorArr[2] = new TriggerSensor(this, findSensorWithType(ambientDisplayConfiguration.doubleTapSensorType()), "doze_pulse_on_double_tap", true, 4, dozeParameters.doubleTapReportsTouchCoordinates(), true, dozeLog);
        triggerSensorArr[3] = new TriggerSensor(findSensorWithType(ambientDisplayConfiguration.tapSensorType()), "doze_tap_gesture", true, true, 9, false, true, false, dozeParameters.singleTapUsesProx(), dozeLog);
        triggerSensorArr[4] = new TriggerSensor(findSensorWithType(ambientDisplayConfiguration.longPressSensorType()), "doze_pulse_on_long_press", false, true, 5, true, true, false, dozeParameters.longPressUsesProx(), dozeLog);
        triggerSensorArr[5] = new TriggerSensor(findSensorWithType(ambientDisplayConfiguration.udfpsLongPressSensorType()), "doze_pulse_on_auth", true, isUdfpsEnrolled && (alwaysOnEnabled || screenOffUdfpsEnabled), 10, true, true, false, dozeParameters.longPressUsesProx(), dozeLog);
        triggerSensorArr[6] = new PluginSensor(this, new SensorManagerPlugin.Sensor(2), "doze_wake_display_gesture", ambientDisplayConfiguration.wakeScreenGestureAvailable() && alwaysOnEnabled, 7, false, false, dozeLog);
        triggerSensorArr[7] = new PluginSensor(new SensorManagerPlugin.Sensor(1), "doze_wake_screen_gesture", ambientDisplayConfiguration.wakeScreenGestureAvailable(), 8, false, false, ambientDisplayConfiguration.getWakeLockScreenDebounce(), dozeLog);
        triggerSensorArr[8] = new TriggerSensor(this, findSensorWithType(ambientDisplayConfiguration.quickPickupSensorType()), "doze_quick_pickup_gesture", true, ambientDisplayConfiguration.quickPickupSensorEnabled(KeyguardUpdateMonitor.getCurrentUser()) && isUdfpsEnrolled, 11, false, false, dozeLog);
        this.mSensors = triggerSensorArr;
        setProxListening(false);
        proximitySensor.register(new ThresholdSensor.Listener() { // from class: com.android.systemui.doze.DozeSensors$$ExternalSyntheticLambda0
            @Override // com.android.systemui.util.sensors.ThresholdSensor.Listener
            public final void onThresholdCrossed(ThresholdSensor.ThresholdSensorEvent thresholdSensorEvent) {
                DozeSensors.$r8$lambda$N4YrYDoXTRYK88tngl6eFyEj_Pk(DozeSensors.this, thresholdSensorEvent);
            }
        });
    }

    public /* synthetic */ void lambda$new$0(ThresholdSensor.ThresholdSensorEvent thresholdSensorEvent) {
        if (thresholdSensorEvent != null) {
            this.mProxCallback.accept(Boolean.valueOf(!thresholdSensorEvent.getBelow()));
        }
    }

    public void destroy() {
        for (TriggerSensor triggerSensor : this.mSensors) {
            triggerSensor.setListening(false);
        }
        this.mProximitySensor.pause();
    }

    public void requestTemporaryDisable() {
        this.mDebounceFrom = SystemClock.uptimeMillis();
    }

    private Sensor findSensorWithType(String str) {
        return findSensorWithType(this.mSensorManager, str);
    }

    public static Sensor findSensorWithType(SensorManager sensorManager, String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        for (Sensor sensor : sensorManager.getSensorList(-1)) {
            if (str.equals(sensor.getStringType())) {
                return sensor;
            }
        }
        return null;
    }

    public void setListening(boolean z, boolean z2) {
        if (this.mListening != z || this.mListeningTouchScreenSensors != z2) {
            this.mListening = z;
            this.mListeningTouchScreenSensors = z2;
            updateListening();
        }
    }

    public void setListening(boolean z, boolean z2, boolean z3) {
        boolean z4 = !this.mSelectivelyRegisterProxSensors || z3;
        if (this.mListening != z || this.mListeningTouchScreenSensors != z2 || this.mListeningProxSensors != z4) {
            this.mListening = z;
            this.mListeningTouchScreenSensors = z2;
            this.mListeningProxSensors = z4;
            updateListening();
        }
    }

    private void updateListening() {
        TriggerSensor[] triggerSensorArr = this.mSensors;
        boolean z = false;
        for (TriggerSensor triggerSensor : triggerSensorArr) {
            boolean z2 = this.mListening && (!triggerSensor.mRequiresTouchscreen || this.mListeningTouchScreenSensors) && (!triggerSensor.mRequiresProx || this.mListeningProxSensors);
            triggerSensor.setListening(z2);
            if (z2) {
                z = true;
            }
        }
        if (!z) {
            this.mSecureSettings.unregisterContentObserver(this.mSettingsObserver);
        } else if (!this.mSettingRegistered) {
            for (TriggerSensor triggerSensor2 : this.mSensors) {
                triggerSensor2.registerSettingsObserver(this.mSettingsObserver);
            }
        }
        this.mSettingRegistered = z;
    }

    public void onUserSwitched() {
        for (TriggerSensor triggerSensor : this.mSensors) {
            triggerSensor.updateListening();
        }
    }

    public void onScreenState(int i) {
        ProximitySensor proximitySensor = this.mProximitySensor;
        boolean z = true;
        if (!(i == 3 || i == 4 || i == 1)) {
            z = false;
        }
        proximitySensor.setSecondarySafe(z);
    }

    public void setProxListening(boolean z) {
        if (this.mProximitySensor.isRegistered() && z) {
            this.mProximitySensor.alertListeners();
        } else if (z) {
            this.mProximitySensor.resume();
        } else {
            this.mProximitySensor.pause();
        }
    }

    public void ignoreTouchScreenSensorsSettingInterferingWithDocking(boolean z) {
        TriggerSensor[] triggerSensorArr = this.mSensors;
        for (TriggerSensor triggerSensor : triggerSensorArr) {
            if (triggerSensor.mRequiresTouchscreen) {
                triggerSensor.ignoreSetting(z);
            }
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("mListening=" + this.mListening);
        printWriter.println("mListeningTouchScreenSensors=" + this.mListeningTouchScreenSensors);
        printWriter.println("mSelectivelyRegisterProxSensors=" + this.mSelectivelyRegisterProxSensors);
        printWriter.println("mListeningProxSensors=" + this.mListeningProxSensors);
        printWriter.println("mScreenOffUdfpsEnabled=" + this.mScreenOffUdfpsEnabled);
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
        indentingPrintWriter.increaseIndent();
        TriggerSensor[] triggerSensorArr = this.mSensors;
        for (TriggerSensor triggerSensor : triggerSensorArr) {
            indentingPrintWriter.println("Sensor: " + triggerSensor.toString());
        }
        indentingPrintWriter.println("ProxSensor: " + this.mProximitySensor.toString());
    }

    public Boolean isProximityCurrentlyNear() {
        return this.mProximitySensor.isNear();
    }

    /* loaded from: classes.dex */
    public class TriggerSensor extends TriggerEventListener {
        final boolean mConfigured;
        protected boolean mDisabled;
        protected final DozeLog mDozeLog;
        protected boolean mIgnoresSetting;
        final int mPulseReason;
        protected boolean mRegistered;
        private final boolean mReportsTouchCoordinates;
        protected boolean mRequested;
        private final boolean mRequiresProx;
        private final boolean mRequiresTouchscreen;
        final Sensor mSensor;
        private final String mSetting;
        private final boolean mSettingDefault;

        public TriggerSensor(DozeSensors dozeSensors, Sensor sensor, String str, boolean z, int i, boolean z2, boolean z3, DozeLog dozeLog) {
            this(dozeSensors, sensor, str, true, z, i, z2, z3, dozeLog);
        }

        public TriggerSensor(DozeSensors dozeSensors, Sensor sensor, String str, boolean z, boolean z2, int i, boolean z3, boolean z4, DozeLog dozeLog) {
            this(sensor, str, z, z2, i, z3, z4, false, false, dozeLog);
        }

        private TriggerSensor(Sensor sensor, String str, boolean z, boolean z2, int i, boolean z3, boolean z4, boolean z5, boolean z6, DozeLog dozeLog) {
            DozeSensors.this = r1;
            this.mSensor = sensor;
            this.mSetting = str;
            this.mSettingDefault = z;
            this.mConfigured = z2;
            this.mPulseReason = i;
            this.mReportsTouchCoordinates = z3;
            this.mRequiresTouchscreen = z4;
            this.mIgnoresSetting = z5;
            this.mRequiresProx = z6;
            this.mDozeLog = dozeLog;
        }

        public void setListening(boolean z) {
            if (this.mRequested != z) {
                this.mRequested = z;
                updateListening();
            }
        }

        public void ignoreSetting(boolean z) {
            if (this.mIgnoresSetting != z) {
                this.mIgnoresSetting = z;
                updateListening();
            }
        }

        public void updateListening() {
            if (this.mConfigured && this.mSensor != null) {
                if (this.mRequested && !this.mDisabled && ((enabledBySetting() || this.mIgnoresSetting) && !this.mRegistered)) {
                    this.mRegistered = DozeSensors.this.mSensorManager.requestTriggerSensor(this, this.mSensor);
                    if (DozeSensors.DEBUG) {
                        Log.d("DozeSensors", "requestTriggerSensor " + this.mRegistered);
                    }
                } else if (this.mRegistered) {
                    boolean cancelTriggerSensor = DozeSensors.this.mSensorManager.cancelTriggerSensor(this, this.mSensor);
                    if (DozeSensors.DEBUG) {
                        Log.d("DozeSensors", "cancelTriggerSensor " + cancelTriggerSensor);
                    }
                    this.mRegistered = false;
                }
            }
        }

        protected boolean enabledBySetting() {
            if (!DozeSensors.this.mConfig.enabled(-2)) {
                return false;
            }
            if (!TextUtils.isEmpty(this.mSetting) && DozeSensors.this.mSecureSettings.getIntForUser(this.mSetting, this.mSettingDefault ? 1 : 0, -2) == 0) {
                return false;
            }
            return true;
        }

        @Override // java.lang.Object
        public String toString() {
            return "{mRegistered=" + this.mRegistered + ", mRequested=" + this.mRequested + ", mDisabled=" + this.mDisabled + ", mConfigured=" + this.mConfigured + ", mIgnoresSetting=" + this.mIgnoresSetting + ", mSensor=" + this.mSensor + "}";
        }

        @Override // android.hardware.TriggerEventListener
        public void onTrigger(TriggerEvent triggerEvent) {
            this.mDozeLog.traceSensor(this.mPulseReason);
            DozeSensors.this.mHandler.post(DozeSensors.this.mWakeLock.wrap(new DozeSensors$TriggerSensor$$ExternalSyntheticLambda0(this, triggerEvent)));
        }

        /* JADX WARNING: Removed duplicated region for block: B:18:0x005c  */
        /* JADX WARNING: Removed duplicated region for block: B:20:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public /* synthetic */ void lambda$onTrigger$0(android.hardware.TriggerEvent r6) {
            /*
                r5 = this;
                boolean r0 = com.android.systemui.doze.DozeSensors.access$400()
                if (r0 == 0) goto L_0x0020
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                r0.<init>()
                java.lang.String r1 = "onTrigger: "
                r0.append(r1)
                java.lang.String r1 = r5.triggerEventToString(r6)
                r0.append(r1)
                java.lang.String r0 = r0.toString()
                java.lang.String r1 = "DozeSensors"
                android.util.Log.d(r1, r0)
            L_0x0020:
                android.hardware.Sensor r0 = r5.mSensor
                if (r0 == 0) goto L_0x0035
                int r0 = r0.getType()
                r1 = 25
                if (r0 != r1) goto L_0x0035
                com.android.internal.logging.UiEventLogger r0 = com.android.systemui.doze.DozeSensors.access$1000()
                com.android.systemui.doze.DozeSensors$DozeSensorsUiEvent r1 = com.android.systemui.doze.DozeSensors.DozeSensorsUiEvent.ACTION_AMBIENT_GESTURE_PICKUP
                r0.log(r1)
            L_0x0035:
                r0 = 0
                r5.mRegistered = r0
                boolean r1 = r5.mReportsTouchCoordinates
                r2 = -1082130432(0xffffffffbf800000, float:-1.0)
                if (r1 == 0) goto L_0x004a
                float[] r1 = r6.values
                int r3 = r1.length
                r4 = 2
                if (r3 < r4) goto L_0x004a
                r2 = r1[r0]
                r0 = 1
                r0 = r1[r0]
                goto L_0x004b
            L_0x004a:
                r0 = r2
            L_0x004b:
                com.android.systemui.doze.DozeSensors r1 = com.android.systemui.doze.DozeSensors.this
                com.android.systemui.doze.DozeSensors$Callback r1 = com.android.systemui.doze.DozeSensors.access$1100(r1)
                int r3 = r5.mPulseReason
                float[] r6 = r6.values
                r1.onSensorPulse(r3, r2, r0, r6)
                boolean r6 = r5.mRegistered
                if (r6 != 0) goto L_0x005f
                r5.updateListening()
            L_0x005f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.doze.DozeSensors.TriggerSensor.lambda$onTrigger$0(android.hardware.TriggerEvent):void");
        }

        public void registerSettingsObserver(ContentObserver contentObserver) {
            if (this.mConfigured && !TextUtils.isEmpty(this.mSetting)) {
                DozeSensors.this.mSecureSettings.registerContentObserverForUser(this.mSetting, DozeSensors.this.mSettingsObserver, -1);
            }
        }

        protected String triggerEventToString(TriggerEvent triggerEvent) {
            if (triggerEvent == null) {
                return null;
            }
            StringBuilder sb = new StringBuilder("SensorEvent[");
            sb.append(triggerEvent.timestamp);
            sb.append(',');
            sb.append(triggerEvent.sensor.getName());
            if (triggerEvent.values != null) {
                for (int i = 0; i < triggerEvent.values.length; i++) {
                    sb.append(',');
                    sb.append(triggerEvent.values[i]);
                }
            }
            sb.append(']');
            return sb.toString();
        }
    }

    /* loaded from: classes.dex */
    public class PluginSensor extends TriggerSensor implements SensorManagerPlugin.SensorEventListener {
        private long mDebounce;
        final SensorManagerPlugin.Sensor mPluginSensor;

        PluginSensor(DozeSensors dozeSensors, SensorManagerPlugin.Sensor sensor, String str, boolean z, int i, boolean z2, boolean z3, DozeLog dozeLog) {
            this(sensor, str, z, i, z2, z3, 0, dozeLog);
        }

        /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
        PluginSensor(SensorManagerPlugin.Sensor sensor, String str, boolean z, int i, boolean z2, boolean z3, long j, DozeLog dozeLog) {
            super(r11, null, str, z, i, z2, z3, dozeLog);
            DozeSensors.this = r11;
            this.mPluginSensor = sensor;
            this.mDebounce = j;
        }

        @Override // com.android.systemui.doze.DozeSensors.TriggerSensor
        public void updateListening() {
            if (this.mConfigured) {
                AsyncSensorManager asyncSensorManager = DozeSensors.this.mSensorManager;
                if (this.mRequested && !this.mDisabled && ((enabledBySetting() || this.mIgnoresSetting) && !this.mRegistered)) {
                    asyncSensorManager.registerPluginListener(this.mPluginSensor, this);
                    this.mRegistered = true;
                    if (DozeSensors.DEBUG) {
                        Log.d("DozeSensors", "registerPluginListener");
                    }
                } else if (this.mRegistered) {
                    asyncSensorManager.unregisterPluginListener(this.mPluginSensor, this);
                    this.mRegistered = false;
                    if (DozeSensors.DEBUG) {
                        Log.d("DozeSensors", "unregisterPluginListener");
                    }
                }
            }
        }

        @Override // com.android.systemui.doze.DozeSensors.TriggerSensor, java.lang.Object
        public String toString() {
            return "{mRegistered=" + this.mRegistered + ", mRequested=" + this.mRequested + ", mDisabled=" + this.mDisabled + ", mConfigured=" + this.mConfigured + ", mIgnoresSetting=" + this.mIgnoresSetting + ", mSensor=" + this.mPluginSensor + "}";
        }

        private String triggerEventToString(SensorManagerPlugin.SensorEvent sensorEvent) {
            if (sensorEvent == null) {
                return null;
            }
            StringBuilder sb = new StringBuilder("PluginTriggerEvent[");
            sb.append(sensorEvent.getSensor());
            sb.append(',');
            sb.append(sensorEvent.getVendorType());
            if (sensorEvent.getValues() != null) {
                for (int i = 0; i < sensorEvent.getValues().length; i++) {
                    sb.append(',');
                    sb.append(sensorEvent.getValues()[i]);
                }
            }
            sb.append(']');
            return sb.toString();
        }

        @Override // com.android.systemui.plugins.SensorManagerPlugin.SensorEventListener
        public void onSensorChanged(SensorManagerPlugin.SensorEvent sensorEvent) {
            this.mDozeLog.traceSensor(this.mPulseReason);
            DozeSensors.this.mHandler.post(DozeSensors.this.mWakeLock.wrap(new DozeSensors$PluginSensor$$ExternalSyntheticLambda0(this, sensorEvent)));
        }

        public /* synthetic */ void lambda$onSensorChanged$0(SensorManagerPlugin.SensorEvent sensorEvent) {
            if (SystemClock.uptimeMillis() < DozeSensors.this.mDebounceFrom + this.mDebounce) {
                Log.d("DozeSensors", "onSensorEvent dropped: " + triggerEventToString(sensorEvent));
                return;
            }
            if (DozeSensors.DEBUG) {
                Log.d("DozeSensors", "onSensorEvent: " + triggerEventToString(sensorEvent));
            }
            DozeSensors.this.mCallback.onSensorPulse(this.mPulseReason, -1.0f, -1.0f, sensorEvent.getValues());
        }
    }
}
