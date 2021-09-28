package com.android.systemui.doze;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.SystemProperties;
import android.os.Trace;
import android.provider.Settings;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.util.sensors.AsyncSensorManager;
import java.io.PrintWriter;
import java.util.Optional;
/* loaded from: classes.dex */
public class DozeScreenBrightness extends BroadcastReceiver implements DozeMachine.Part, SensorEventListener {
    private static final boolean DEBUG_AOD_BRIGHTNESS = SystemProperties.getBoolean("debug.aod_brightness", false);
    private final Context mContext;
    private int mDefaultDozeBrightness;
    private final DozeHost mDozeHost;
    private final DozeParameters mDozeParameters;
    private final DozeMachine.Service mDozeService;
    private final Handler mHandler;
    private final Optional<Sensor> mLightSensorOptional;
    private boolean mRegistered;
    private final int mScreenBrightnessDim;
    private final SensorManager mSensorManager;
    private final int[] mSensorToBrightness;
    private final int[] mSensorToScrimOpacity;
    private final WakefulnessLifecycle mWakefulnessLifecycle;
    private boolean mPaused = false;
    private boolean mScreenOff = false;
    private int mLastSensorValue = -1;
    private int mDebugBrightnessBucket = -1;

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public DozeScreenBrightness(Context context, DozeMachine.Service service, AsyncSensorManager asyncSensorManager, Optional<Sensor> optional, DozeHost dozeHost, Handler handler, AlwaysOnDisplayPolicy alwaysOnDisplayPolicy, WakefulnessLifecycle wakefulnessLifecycle, DozeParameters dozeParameters) {
        this.mContext = context;
        this.mDozeService = service;
        this.mSensorManager = asyncSensorManager;
        this.mLightSensorOptional = optional;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        this.mDozeParameters = dozeParameters;
        this.mDozeHost = dozeHost;
        this.mHandler = handler;
        this.mDefaultDozeBrightness = alwaysOnDisplayPolicy.defaultDozeBrightness;
        this.mScreenBrightnessDim = alwaysOnDisplayPolicy.dimBrightness;
        this.mSensorToBrightness = alwaysOnDisplayPolicy.screenBrightnessArray;
        this.mSensorToScrimOpacity = alwaysOnDisplayPolicy.dimmingScrimArray;
    }

    /* renamed from: com.android.systemui.doze.DozeScreenBrightness$1  reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$systemui$doze$DozeMachine$State;

        static {
            int[] iArr = new int[DozeMachine.State.values().length];
            $SwitchMap$com$android$systemui$doze$DozeMachine$State = iArr;
            try {
                iArr[DozeMachine.State.INITIALIZED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.FINISH.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        int i = AnonymousClass1.$SwitchMap$com$android$systemui$doze$DozeMachine$State[state2.ordinal()];
        boolean z = true;
        if (i == 1 || i == 2) {
            resetBrightnessToDefault();
        } else if (i == 3) {
            onDestroy();
        }
        if (state2 != DozeMachine.State.FINISH) {
            setScreenOff(state2 == DozeMachine.State.DOZE);
            if (state2 != DozeMachine.State.DOZE_AOD_PAUSED) {
                z = false;
            }
            setPaused(z);
        }
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public void onScreenState(int i) {
        if (i == 3 || i == 4) {
            setLightSensorEnabled(true);
        } else {
            setLightSensorEnabled(false);
        }
    }

    private void onDestroy() {
        setLightSensorEnabled(false);
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        Trace.beginSection("DozeScreenBrightness.onSensorChanged" + sensorEvent.values[0]);
        try {
            if (this.mRegistered) {
                this.mLastSensorValue = (int) sensorEvent.values[0];
                updateBrightnessAndReady(false);
            }
        } finally {
            Trace.endSection();
        }
    }

    private void updateBrightnessAndReady(boolean z) {
        int i = -1;
        if (z || this.mRegistered || this.mDebugBrightnessBucket != -1) {
            int i2 = this.mDebugBrightnessBucket;
            if (i2 == -1) {
                i2 = this.mLastSensorValue;
            }
            int computeBrightness = computeBrightness(i2);
            boolean z2 = computeBrightness > 0;
            if (z2) {
                this.mDozeService.setDozeScreenBrightness(clampToUserSetting(computeBrightness));
            }
            if (!this.mLightSensorOptional.isPresent()) {
                i = 0;
            } else if (z2) {
                i = computeScrimOpacity(i2);
            }
            if (i >= 0) {
                this.mDozeHost.setAodDimmingScrim(((float) i) / 255.0f);
            }
        }
    }

    private int computeScrimOpacity(int i) {
        if (i < 0) {
            return -1;
        }
        int[] iArr = this.mSensorToScrimOpacity;
        if (i >= iArr.length) {
            return -1;
        }
        return iArr[i];
    }

    private int computeBrightness(int i) {
        if (i < 0) {
            return -1;
        }
        int[] iArr = this.mSensorToBrightness;
        if (i >= iArr.length) {
            return -1;
        }
        return iArr[i];
    }

    private void resetBrightnessToDefault() {
        this.mDozeService.setDozeScreenBrightness(clampToDimBrightnessForScreenOff(clampToUserSetting(this.mDefaultDozeBrightness)));
        this.mDozeHost.setAodDimmingScrim(0.0f);
    }

    private int clampToUserSetting(int i) {
        return Math.min(i, Settings.System.getIntForUser(this.mContext.getContentResolver(), "screen_brightness", Integer.MAX_VALUE, -2));
    }

    private int clampToDimBrightnessForScreenOff(int i) {
        return (!this.mDozeParameters.shouldControlUnlockedScreenOff() || this.mWakefulnessLifecycle.getLastSleepReason() != 2) ? i : Math.min(this.mScreenBrightnessDim, i);
    }

    private void setLightSensorEnabled(boolean z) {
        if (z && !this.mRegistered && this.mLightSensorOptional.isPresent()) {
            this.mRegistered = this.mSensorManager.registerListener(this, this.mLightSensorOptional.get(), 3, this.mHandler);
            this.mLastSensorValue = -1;
        } else if (!z && this.mRegistered) {
            this.mSensorManager.unregisterListener(this);
            this.mRegistered = false;
            this.mLastSensorValue = -1;
        }
    }

    private void setPaused(boolean z) {
        if (this.mPaused != z) {
            this.mPaused = z;
            updateBrightnessAndReady(false);
        }
    }

    private void setScreenOff(boolean z) {
        if (this.mScreenOff != z) {
            this.mScreenOff = z;
            updateBrightnessAndReady(true);
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        this.mDebugBrightnessBucket = intent.getIntExtra("brightness_bucket", -1);
        updateBrightnessAndReady(false);
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public void dump(PrintWriter printWriter) {
        printWriter.println("DozeScreenBrightnessSensorRegistered=" + this.mRegistered);
    }
}
