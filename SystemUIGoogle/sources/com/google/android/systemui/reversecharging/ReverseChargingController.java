package com.google.android.systemui.reversecharging;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.IAppCallback;
import android.nfc.INfcAdapter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import androidx.constraintlayout.widget.R$styleable;
import com.android.systemui.BootCompleteCache;
import com.android.systemui.R$array;
import com.android.systemui.R$bool;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.CallbackController;
import com.google.android.systemui.reversecharging.ReverseWirelessCharger;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
/* loaded from: classes2.dex */
public class ReverseChargingController extends BroadcastReceiver implements CallbackController<ReverseChargingChangeCallback> {
    private static final boolean DEBUG = Log.isLoggable("ReverseChargingControl", 3);
    private static final long DURATION_TO_ADVANCED_ACCESSORY_DEVICE_RECONNECTED_TIME_OUT;
    private static final long DURATION_TO_ADVANCED_PHONE_RECONNECTED_TIME_OUT;
    private static final long DURATION_TO_ADVANCED_PLUS_ACCESSORY_DEVICE_RECONNECTED_TIME_OUT;
    private static final long DURATION_TO_REVERSE_AC_TIME_OUT;
    private static final long DURATION_TO_REVERSE_RX_REMOVAL_TIME_OUT;
    private static final long DURATION_TO_REVERSE_TIME_OUT;
    private static final long DURATION_WAIT_NFC_SERVICE;
    private final AlarmManager mAlarmManager;
    private final Executor mBgExecutor;
    private final BootCompleteCache mBootCompleteCache;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private boolean mCacheIsReverseSupported;
    private final Context mContext;
    final boolean mDoesNfcConflictWithUsbAudio;
    private final boolean mDoesNfcConflictWithWlc;
    private boolean mIsReverseSupported;
    private int mLevel;
    private final Executor mMainExecutor;
    private String mName;
    final int[] mNfcUsbProductIds;
    final int[] mNfcUsbVendorIds;
    private boolean mPluggedAc;
    private boolean mPowerSave;
    boolean mRestoreUsbNfcPollingMode;
    private boolean mRestoreWlcNfcPollingMode;
    boolean mReverse;
    private final Optional<ReverseWirelessCharger> mRtxChargerManagerOptional;
    private int mRtxLevel;
    private boolean mStartReconnected;
    private boolean mStopReverseAtAcUnplug;
    private final Optional<UsbManager> mUsbManagerOptional;
    private boolean mUseRxRemovalTimeOut;
    private boolean mWirelessCharging;
    private final IBinder mNfcInterfaceToken = new Binder();
    private final ArrayList<ReverseChargingChangeCallback> mChangeCallbacks = new ArrayList<>();
    int mCurrentRtxMode = 0;
    boolean mIsUsbPlugIn = false;
    private int mCurrentRtxReceiverType = 0;
    private boolean mProvidingBattery = false;
    private long mReverseStartTime = 0;
    private long mReverseStopTime = 0;
    private final BootCompleteCache.BootCompleteListener mBootCompleteListener = new BootCompleteCache.BootCompleteListener() { // from class: com.google.android.systemui.reversecharging.ReverseChargingController$$ExternalSyntheticLambda5
        @Override // com.android.systemui.BootCompleteCache.BootCompleteListener
        public final void onBootComplete() {
            ReverseChargingController.this.lambda$new$0();
        }
    };
    private final AlarmManager.OnAlarmListener mRtxFinishAlarmAction = new AlarmManager.OnAlarmListener() { // from class: com.google.android.systemui.reversecharging.ReverseChargingController$$ExternalSyntheticLambda0
        @Override // android.app.AlarmManager.OnAlarmListener
        public final void onAlarm() {
            ReverseChargingController.this.lambda$new$1();
        }
    };
    private final AlarmManager.OnAlarmListener mRtxFinishRxFullAlarmAction = new AlarmManager.OnAlarmListener() { // from class: com.google.android.systemui.reversecharging.ReverseChargingController$$ExternalSyntheticLambda4
        @Override // android.app.AlarmManager.OnAlarmListener
        public final void onAlarm() {
            ReverseChargingController.this.lambda$new$2();
        }
    };
    private final AlarmManager.OnAlarmListener mCheckNfcConflictWithUsbAudioAlarmAction = new AlarmManager.OnAlarmListener() { // from class: com.google.android.systemui.reversecharging.ReverseChargingController$$ExternalSyntheticLambda2
        @Override // android.app.AlarmManager.OnAlarmListener
        public final void onAlarm() {
            ReverseChargingController.this.lambda$new$3();
        }
    };
    private final AlarmManager.OnAlarmListener mReconnectedTimeoutAlarmAction = new AlarmManager.OnAlarmListener() { // from class: com.google.android.systemui.reversecharging.ReverseChargingController$$ExternalSyntheticLambda1
        @Override // android.app.AlarmManager.OnAlarmListener
        public final void onAlarm() {
            ReverseChargingController.this.lambda$new$4();
        }
    };
    private final AlarmManager.OnAlarmListener mAccessoryDeviceRemovedTimeoutAlarmAction = new AlarmManager.OnAlarmListener() { // from class: com.google.android.systemui.reversecharging.ReverseChargingController$$ExternalSyntheticLambda3
        @Override // android.app.AlarmManager.OnAlarmListener
        public final void onAlarm() {
            ReverseChargingController.this.lambda$new$5();
        }
    };
    final BatteryController.BatteryStateChangeCallback mBatteryStateChangeCallback = new BatteryController.BatteryStateChangeCallback() { // from class: com.google.android.systemui.reversecharging.ReverseChargingController.1
        @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
        public void onPowerSaveChanged(boolean z) {
            ReverseChargingController.this.mPowerSave = z;
        }

        @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
        public void onWirelessChargingChanged(boolean z) {
            ReverseChargingController.this.mWirelessCharging = z;
        }
    };

    /* loaded from: classes2.dex */
    public interface ReverseChargingChangeCallback {
        default void onReverseChargingChanged(boolean z, int i, String str) {
        }
    }

    private boolean shouldEnableAccessoryReconnect(int i) {
        return i == 16 || i == 90 || i == 114;
    }

    static {
        TimeUnit timeUnit = TimeUnit.MINUTES;
        DURATION_TO_REVERSE_TIME_OUT = timeUnit.toMillis(1);
        DURATION_TO_REVERSE_AC_TIME_OUT = timeUnit.toMillis(1);
        TimeUnit timeUnit2 = TimeUnit.SECONDS;
        DURATION_TO_REVERSE_RX_REMOVAL_TIME_OUT = timeUnit2.toMillis(30);
        DURATION_TO_ADVANCED_ACCESSORY_DEVICE_RECONNECTED_TIME_OUT = timeUnit2.toMillis(120);
        DURATION_TO_ADVANCED_PHONE_RECONNECTED_TIME_OUT = timeUnit2.toMillis(120);
        DURATION_TO_ADVANCED_PLUS_ACCESSORY_DEVICE_RECONNECTED_TIME_OUT = timeUnit2.toMillis(120);
        DURATION_WAIT_NFC_SERVICE = timeUnit2.toMillis(10);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (DEBUG) {
            Log.d("ReverseChargingControl", "onBootComplete(): ACTION_BOOT_COMPLETED, setRtxTimer, REVERSE_FINISH");
        }
        setRtxTimer(0, 0);
        setRtxTimer(2, DURATION_WAIT_NFC_SERVICE);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        onAlarmRtxFinish(5);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        onAlarmRtxFinish(R$styleable.Constraint_layout_goneMarginTop);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4() {
        if (DEBUG) {
            Log.w("ReverseChargingControl", "mReConnectedTimeoutAlarmAction() timeout");
        }
        this.mStartReconnected = false;
        onAlarmRtxFinish(6);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5() {
        if (DEBUG) {
            Log.w("ReverseChargingControl", "mAccessoryDeviceRemovedTimeoutAlarmAction() timeout");
        }
        onAlarmRtxFinish(6);
    }

    public ReverseChargingController(Context context, BroadcastDispatcher broadcastDispatcher, Optional<ReverseWirelessCharger> optional, AlarmManager alarmManager, Optional<UsbManager> optional2, Executor executor, Executor executor2, BootCompleteCache bootCompleteCache) {
        this.mContext = context;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mRtxChargerManagerOptional = optional;
        this.mAlarmManager = alarmManager;
        this.mDoesNfcConflictWithWlc = context.getResources().getBoolean(R$bool.config_nfc_conflict_with_wlc);
        this.mUsbManagerOptional = optional2;
        this.mMainExecutor = executor;
        this.mBgExecutor = executor2;
        this.mBootCompleteCache = bootCompleteCache;
        int[] intArray = context.getResources().getIntArray(R$array.config_nfc_conflict_with_usb_audio_vendorid);
        this.mNfcUsbVendorIds = intArray;
        int[] intArray2 = context.getResources().getIntArray(R$array.config_nfc_conflict_with_usb_audio_productid);
        this.mNfcUsbProductIds = intArray2;
        if (intArray.length == intArray2.length) {
            this.mDoesNfcConflictWithUsbAudio = context.getResources().getBoolean(R$bool.config_nfc_conflict_with_usb_audio);
            return;
        }
        throw new IllegalStateException("VendorIds and ProductIds must be the same length");
    }

    /* access modifiers changed from: private */
    /* renamed from: checkNfcConflictWithUsbAudioBootTask */
    public void lambda$new$3() {
        if (this.mUsbManagerOptional.isPresent()) {
            for (UsbDevice usbDevice : this.mUsbManagerOptional.get().getDeviceList().values()) {
                checkAndChangeNfcPollingAgainstUsbAudioDevice(false, usbDevice);
            }
        }
    }

    private void checkAndChangeNfcPollingAgainstUsbAudioDevice(boolean z, UsbDevice usbDevice) {
        boolean z2 = false;
        for (int i = 0; i < this.mNfcUsbVendorIds.length; i++) {
            if (usbDevice.getVendorId() == this.mNfcUsbVendorIds[i] && usbDevice.getProductId() == this.mNfcUsbProductIds[i]) {
                this.mRestoreUsbNfcPollingMode = !z;
                if (!this.mRestoreWlcNfcPollingMode && z) {
                    z2 = true;
                }
                enableNfcPollingMode(z2);
                return;
            }
        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        this.mBroadcastDispatcher.registerReceiver(this, intentFilter);
    }

    public void init(BatteryController batteryController) {
        batteryController.addCallback(this.mBatteryStateChangeCallback);
        this.mCacheIsReverseSupported = false;
        resetReverseInfo();
        registerReceiver();
        this.mBootCompleteCache.addListener(this.mBootCompleteListener);
        if (this.mRtxChargerManagerOptional.isPresent()) {
            this.mRtxChargerManagerOptional.get().addIsDockPresentChangeListener(new ReverseWirelessCharger.IsDockPresentChangeListener() { // from class: com.google.android.systemui.reversecharging.ReverseChargingController$$ExternalSyntheticLambda6
                @Override // com.google.android.systemui.reversecharging.ReverseWirelessCharger.IsDockPresentChangeListener
                public final void onDockPresentChanged(Bundle bundle) {
                    ReverseChargingController.this.onDockPresentChanged(bundle);
                }
            });
            this.mRtxChargerManagerOptional.get().addReverseChargingInformationChangeListener(new ReverseWirelessCharger.ReverseChargingInformationChangeListener() { // from class: com.google.android.systemui.reversecharging.ReverseChargingController$$ExternalSyntheticLambda8
                @Override // com.google.android.systemui.reversecharging.ReverseWirelessCharger.ReverseChargingInformationChangeListener
                public final void onReverseInformationChanged(Bundle bundle) {
                    ReverseChargingController.this.onReverseInformationChanged(bundle);
                }
            });
            this.mRtxChargerManagerOptional.get().addReverseChargingChangeListener(new ReverseWirelessCharger.ReverseChargingChangeListener() { // from class: com.google.android.systemui.reversecharging.ReverseChargingController$$ExternalSyntheticLambda7
                @Override // com.google.android.systemui.reversecharging.ReverseWirelessCharger.ReverseChargingChangeListener
                public final void onReverseStatusChanged(Bundle bundle) {
                    ReverseChargingController.this.onReverseStateChanged(bundle);
                }
            });
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        handleIntentForReverseCharging(intent);
    }

    public void handleIntentForReverseCharging(Intent intent) {
        UsbDevice usbDevice;
        Object[] objArr;
        Object[] objArr2;
        if (isReverseSupported()) {
            String action = intent.getAction();
            boolean z = true;
            if (action.equals("android.intent.action.BATTERY_CHANGED")) {
                boolean z2 = this.mPluggedAc;
                this.mLevel = (int) ((((float) intent.getIntExtra("level", 0)) * 100.0f) / ((float) intent.getIntExtra("scale", 100)));
                int intExtra = intent.getIntExtra("plugged", 0);
                this.mPluggedAc = intExtra == 1;
                Log.i("ReverseChargingControl", "handleIntentForReverseCharging(): rtx=" + (this.mReverse ? 1 : 0) + " wlc=" + (this.mWirelessCharging ? 1 : 0) + " plgac=" + (z2 ? 1 : 0) + " ac=" + (this.mPluggedAc ? 1 : 0) + " acrtx=" + (this.mStopReverseAtAcUnplug ? 1 : 0) + " extra=" + intExtra + " this=" + this);
                boolean z3 = this.mReverse;
                if (z3 && this.mWirelessCharging) {
                    if (DEBUG) {
                        Log.d("ReverseChargingControl", "handleIntentForReverseCharging(): wireless charging, stop");
                    }
                    setReverseStateInternal(false, R$styleable.Constraint_layout_goneMarginStart);
                } else if (z3 && z2 && !this.mPluggedAc && this.mStopReverseAtAcUnplug) {
                    if (DEBUG) {
                        Log.d("ReverseChargingControl", "handleIntentForReverseCharging(): wired charging, stop");
                    }
                    this.mStopReverseAtAcUnplug = false;
                    setReverseStateInternal(false, 106);
                } else if (!z3 && !z2 && this.mPluggedAc) {
                    if (DEBUG) {
                        Log.d("ReverseChargingControl", "handleIntentForReverseCharging(): wired charging, start");
                    }
                    this.mStopReverseAtAcUnplug = true;
                    setReverseStateInternal(true, 3);
                } else if (z3 && isLowBattery()) {
                    if (DEBUG) {
                        Log.d("ReverseChargingControl", "handleIntentForReverseCharging(): lower then battery threshold, stop");
                    }
                    setReverseStateInternal(false, 4);
                }
            } else if (action.equals("android.os.action.POWER_SAVE_MODE_CHANGED")) {
                if (this.mReverse && this.mPowerSave) {
                    Log.i("ReverseChargingControl", "handleIntentForReverseCharging(): power save, stop");
                    setReverseStateInternal(false, R$styleable.Constraint_pathMotionArc);
                }
            } else if (TextUtils.equals(action, "android.hardware.usb.action.USB_DEVICE_ATTACHED")) {
                UsbDevice usbDevice2 = (UsbDevice) intent.getParcelableExtra("device");
                if (usbDevice2 == null) {
                    Log.w("ReverseChargingControl", "handleIntentForReverseCharging() UsbDevice is null!");
                    this.mIsUsbPlugIn = false;
                    return;
                }
                if (this.mDoesNfcConflictWithUsbAudio) {
                    checkAndChangeNfcPollingAgainstUsbAudioDevice(false, usbDevice2);
                }
                int i = 0;
                while (true) {
                    if (i >= usbDevice2.getInterfaceCount()) {
                        objArr = null;
                        break;
                    } else if (usbDevice2.getInterface(i).getInterfaceClass() == 1) {
                        objArr = 1;
                        break;
                    } else {
                        i++;
                    }
                }
                int i2 = 0;
                while (true) {
                    if (i2 >= usbDevice2.getConfigurationCount()) {
                        objArr2 = null;
                        break;
                    } else if (usbDevice2.getConfiguration(i2).getMaxPower() < 100) {
                        objArr2 = 1;
                        break;
                    } else {
                        i2++;
                    }
                }
                if (!(objArr == null || objArr2 == null)) {
                    z = false;
                }
                this.mIsUsbPlugIn = z;
                if (this.mReverse && z) {
                    setReverseStateInternal(false, R$styleable.Constraint_transitionEasing);
                    Log.d("ReverseChargingControl", "handleIntentForReverseCharging(): stop reverse charging because USB-C plugin!");
                }
            } else if (TextUtils.equals(action, "android.hardware.usb.action.USB_DEVICE_DETACHED")) {
                if (this.mDoesNfcConflictWithUsbAudio && (usbDevice = (UsbDevice) intent.getParcelableExtra("device")) != null) {
                    checkAndChangeNfcPollingAgainstUsbAudioDevice(true, usbDevice);
                }
                this.mIsUsbPlugIn = false;
            }
        }
    }

    private boolean isLowBattery() {
        int i = Settings.Global.getInt(this.mContext.getContentResolver(), "advanced_battery_usage_amount", 2) * 5;
        if (this.mLevel > i) {
            return false;
        }
        Log.w("ReverseChargingControl", "The battery is lower than threshold turn off reverse charging ! level : " + this.mLevel + ", threshold : " + i);
        return true;
    }

    public boolean isReverseSupported() {
        if (this.mCacheIsReverseSupported) {
            return this.mIsReverseSupported;
        }
        if (this.mRtxChargerManagerOptional.isPresent()) {
            boolean isRtxSupported = this.mRtxChargerManagerOptional.get().isRtxSupported();
            this.mIsReverseSupported = isRtxSupported;
            this.mCacheIsReverseSupported = true;
            return isRtxSupported;
        } else if (!DEBUG) {
            return false;
        } else {
            Log.d("ReverseChargingControl", "isReverseSupported(): mRtxChargerManagerOptional is not present!");
            return false;
        }
    }

    public boolean isReverseOn() {
        return this.mReverse;
    }

    public void setReverseState(boolean z) {
        if (isReverseSupported()) {
            if (DEBUG) {
                Log.d("ReverseChargingControl", "setReverseState(): rtx=" + (z ? 1 : 0));
            }
            this.mStopReverseAtAcUnplug = false;
            setReverseStateInternal(z, 2);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: requestReverseInformation */
    public void lambda$onDockPresentChanged$6() {
        boolean z = DEBUG;
        if (z) {
            Log.d("ReverseChargingControl", "requestReverseInformation()");
        }
        if (this.mRtxChargerManagerOptional.isPresent()) {
            this.mRtxChargerManagerOptional.get().getRtxInformation();
        } else if (z) {
            Log.d("ReverseChargingControl", "requestReverseInformation(): mRtxChargerManagerOptional is not present!");
        }
    }

    private void setReverseStateInternal(boolean z, int i) {
        if (isReverseSupported()) {
            Log.i("ReverseChargingControl", "setReverseStateInternal(): rtx=" + (z ? 1 : 0) + ",reason=" + i);
            if (!z || isReverseOn()) {
                logReverseStopEvent(i);
            } else {
                logReverseStartEvent(i);
                if (this.mPowerSave) {
                    logReverseStopEvent(R$styleable.Constraint_motionStagger);
                    return;
                } else if (isLowBattery()) {
                    logReverseStopEvent(100);
                    return;
                } else if (this.mIsUsbPlugIn) {
                    logReverseStopEvent(R$styleable.Constraint_progress);
                    return;
                }
            }
            if (z != isReverseOn()) {
                if (z && this.mDoesNfcConflictWithWlc && !this.mRestoreWlcNfcPollingMode) {
                    enableNfcPollingMode(false);
                    this.mRestoreWlcNfcPollingMode = true;
                }
                this.mReverse = z;
                setRtxMode(z);
            }
        }
    }

    private void enableNfcPollingMode(boolean z) {
        int i = z ? 0 : 4096;
        if (DEBUG) {
            Log.d("ReverseChargingControl", "Change NFC reader mode to flags: " + i);
        }
        try {
            INfcAdapter.Stub.asInterface(ServiceManager.getService("nfc")).setReaderMode(this.mNfcInterfaceToken, (IAppCallback) null, i, (Bundle) null);
        } catch (Exception e) {
            Log.e("ReverseChargingControl", "Could not change NFC reader mode, exception: " + e);
        }
    }

    private long getRtxTimeOut(int i) {
        long j;
        if (this.mStartReconnected) {
            j = getAccessoryReconnectDuration(i);
        } else if (this.mStopReverseAtAcUnplug) {
            j = DURATION_TO_REVERSE_AC_TIME_OUT;
        } else if (this.mUseRxRemovalTimeOut) {
            j = DURATION_TO_REVERSE_RX_REMOVAL_TIME_OUT;
        } else {
            j = DURATION_TO_REVERSE_TIME_OUT;
        }
        String str = SystemProperties.get(this.mStopReverseAtAcUnplug ? "rtx.ac.timeout" : "rtx.timeout");
        if (TextUtils.isEmpty(str)) {
            return j;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            Log.w("ReverseChargingControl", "getRtxTimeOut(): invalid timeout, " + e);
            return j;
        }
    }

    private long getAccessoryReconnectDuration(int i) {
        if (i == 16) {
            return DURATION_TO_ADVANCED_ACCESSORY_DEVICE_RECONNECTED_TIME_OUT;
        }
        if (i == 114) {
            return DURATION_TO_ADVANCED_PHONE_RECONNECTED_TIME_OUT;
        }
        return DURATION_TO_ADVANCED_PLUS_ACCESSORY_DEVICE_RECONNECTED_TIME_OUT;
    }

    /* access modifiers changed from: private */
    public void onDockPresentChanged(Bundle bundle) {
        if (DEBUG) {
            Log.d("ReverseChargingControl", "onDockPresentChanged(): rtx =" + (this.mReverse ? 1 : 0) + " type=" + ((int) bundle.getByte("key_dock_present_type")) + " bundle=" + bundle.toString() + " this=" + this);
        }
        if (bundle.getByte("key_dock_present_type") == 4) {
            this.mBgExecutor.execute(new Runnable() { // from class: com.google.android.systemui.reversecharging.ReverseChargingController$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    ReverseChargingController.this.lambda$onDockPresentChanged$6();
                }
            });
            this.mMainExecutor.execute(new Runnable(bundle) { // from class: com.google.android.systemui.reversecharging.ReverseChargingController$$ExternalSyntheticLambda10
                public final /* synthetic */ Bundle f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    ReverseChargingController.this.lambda$onDockPresentChanged$7(this.f$1);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onDockPresentChangedOnMainThread */
    public void lambda$onDockPresentChanged$7(Bundle bundle) {
        if (DEBUG) {
            Log.d("ReverseChargingControl", "onDockPresentChangedOnMainThread(): rtx =" + (this.mReverse ? 1 : 0) + " type=" + ((int) bundle.getByte("key_dock_present_type")) + " bundle=" + bundle.toString() + " this=" + this);
        }
        this.mName = bundle.getByte("key_dock_present_type") == 4 ? this.mContext.getString(R$string.reverse_charging_device_name_text) : null;
    }

    /* access modifiers changed from: private */
    public void onReverseInformationChanged(Bundle bundle) {
        if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("onReverseInformationChanged(): rtx=");
            int i = 1;
            if (bundle.getInt("key_rtx_mode") != 1) {
                i = 0;
            }
            sb.append(i);
            sb.append(" wlc=");
            sb.append(this.mWirelessCharging ? 1 : 0);
            sb.append(" mName=");
            sb.append(this.mName);
            sb.append(" bundle=");
            sb.append(bundle.toString());
            sb.append(" this=");
            sb.append(this);
            Log.d("ReverseChargingControl", sb.toString());
        }
        if (bundle.getInt("key_rtx_level") > 0) {
            this.mMainExecutor.execute(new Runnable(bundle) { // from class: com.google.android.systemui.reversecharging.ReverseChargingController$$ExternalSyntheticLambda11
                public final /* synthetic */ Bundle f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    ReverseChargingController.this.lambda$onReverseInformationChanged$8(this.f$1);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onReverseInformationChangedOnMainThread */
    public void lambda$onReverseInformationChanged$8(Bundle bundle) {
        boolean z = false;
        if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("onReverseInformationChangedOnMainThread(): rtx=");
            sb.append(bundle.getInt("key_rtx_mode") == 1 ? 1 : 0);
            sb.append(" wlc=");
            sb.append(this.mWirelessCharging ? 1 : 0);
            sb.append(" mName=");
            sb.append(this.mName);
            sb.append(" bundle=");
            sb.append(bundle.toString());
            sb.append(" this=");
            sb.append(this);
            Log.d("ReverseChargingControl", sb.toString());
        }
        if (this.mWirelessCharging && this.mName != null) {
            if (bundle.getInt("key_rtx_mode") == 1) {
                z = true;
            }
            this.mReverse = z;
            this.mRtxLevel = bundle.getInt("key_rtx_level");
            fireReverseChanged();
        }
    }

    /* access modifiers changed from: package-private */
    public void onReverseStateChanged(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        sb.append("onReverseStateChanged(): rtx=");
        int i = 1;
        if (bundle.getInt("key_rtx_mode") != 1) {
            i = 0;
        }
        sb.append(i);
        sb.append(" bundle=");
        sb.append(bundle.toString());
        sb.append(" this=");
        sb.append(this);
        Log.i("ReverseChargingControl", sb.toString());
        this.mMainExecutor.execute(new Runnable(bundle) { // from class: com.google.android.systemui.reversecharging.ReverseChargingController$$ExternalSyntheticLambda12
            public final /* synthetic */ Bundle f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                ReverseChargingController.this.lambda$onReverseStateChanged$9(this.f$1);
            }
        });
    }

    /* access modifiers changed from: private */
    /* renamed from: onReverseStateChangedOnMainThread */
    public void lambda$onReverseStateChanged$9(Bundle bundle) {
        boolean z = DEBUG;
        int i = 0;
        if (z) {
            StringBuilder sb = new StringBuilder();
            sb.append("onReverseStateChangedOnMainThread(): rtx=");
            sb.append(bundle.getInt("key_rtx_mode") == 1 ? 1 : 0);
            sb.append(" bundle=");
            sb.append(bundle.toString());
            sb.append(" this=");
            sb.append(this);
            Log.d("ReverseChargingControl", sb.toString());
        }
        int i2 = bundle.getInt("key_rtx_mode");
        int i3 = bundle.getInt("key_reason_type");
        boolean z2 = bundle.getBoolean("key_rtx_connection");
        int i4 = bundle.getInt("key_accessory_type");
        int i5 = bundle.getInt("key_rtx_level");
        if (!this.mReverse && this.mWirelessCharging && i2 == 0 && i5 > 0) {
            this.mRtxLevel = i5;
            if (TextUtils.isEmpty(this.mName)) {
                this.mName = this.mContext.getString(R$string.reverse_charging_device_name_text);
            }
            fireReverseChanged();
        } else if (!isReverseSupported()) {
            resetReverseInfo();
            fireReverseChanged();
        } else {
            logReverseStateChanged(i2, i3);
            if (this.mCurrentRtxMode != 1 && i2 == 1 && !this.mReverse && this.mDoesNfcConflictWithWlc && !this.mRestoreWlcNfcPollingMode) {
                enableNfcPollingMode(false);
                this.mRestoreWlcNfcPollingMode = true;
            }
            this.mCurrentRtxMode = i2;
            resetReverseInfo();
            if (i2 == 1) {
                playSoundIfNecessary(z2, i4);
                this.mProvidingBattery = z2;
                this.mReverse = true;
                if (!z2) {
                    if (z) {
                        Log.d("ReverseChargingControl", "receiver is not available");
                    }
                    this.mRtxLevel = -1;
                    this.mCurrentRtxReceiverType = 0;
                } else {
                    this.mStopReverseAtAcUnplug = false;
                    this.mRtxLevel = i5;
                    this.mUseRxRemovalTimeOut = true;
                    if (this.mCurrentRtxReceiverType != i4) {
                        if (z) {
                            Log.d("ReverseChargingControl", "receiver type updated: " + this.mCurrentRtxReceiverType + " " + i4);
                        }
                        logReverseAccessoryType(i4);
                        this.mCurrentRtxReceiverType = i4;
                    }
                }
            } else {
                this.mStopReverseAtAcUnplug = false;
                this.mProvidingBattery = false;
                this.mUseRxRemovalTimeOut = false;
                this.mStartReconnected = false;
                if (this.mDoesNfcConflictWithWlc && this.mRestoreWlcNfcPollingMode) {
                    this.mRestoreWlcNfcPollingMode = false;
                    enableNfcPollingMode(!this.mRestoreUsbNfcPollingMode);
                }
            }
            fireReverseChanged();
            cancelRtxTimer(0);
            cancelRtxTimer(1);
            cancelRtxTimer(4);
            if (!this.mStartReconnected) {
                cancelRtxTimer(3);
            }
            boolean z3 = this.mReverse;
            if (z3 && this.mRtxLevel == -1) {
                long rtxTimeOut = getRtxTimeOut(i4);
                if (z) {
                    Log.d("ReverseChargingControl", "onReverseStateChangedOnMainThread(): time out, setRtxTimer, duration=" + rtxTimeOut);
                }
                if (this.mStartReconnected) {
                    i = 3;
                } else if (this.mUseRxRemovalTimeOut && !this.mStopReverseAtAcUnplug) {
                    i = 4;
                }
                setRtxTimer(i, rtxTimeOut);
            } else if (z3 && this.mRtxLevel >= 100) {
                if (z) {
                    Log.d("ReverseChargingControl", "onReverseStateChangedOnMainThread(): rtx=" + (this.mReverse ? 1 : 0) + ", Rx fully charged, setRtxTimer, REVERSE_FINISH_RX_FULL");
                }
                setRtxTimer(1, 0);
            }
        }
    }

    private void setRtxMode(boolean z) {
        if (this.mRtxChargerManagerOptional.isPresent()) {
            if (DEBUG) {
                Log.d("ReverseChargingControl", "setRtxMode(): rtx=" + (z ? 1 : 0));
            }
            this.mRtxChargerManagerOptional.get().setRtxMode(z);
        }
    }

    private void resetReverseInfo() {
        this.mReverse = false;
        this.mRtxLevel = -1;
        this.mName = null;
    }

    private void fireReverseChanged() {
        synchronized (this.mChangeCallbacks) {
            ArrayList arrayList = new ArrayList(this.mChangeCallbacks);
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                ((ReverseChargingChangeCallback) arrayList.get(i)).onReverseChargingChanged(this.mReverse, this.mRtxLevel, this.mName);
            }
        }
    }

    private void setRtxTimer(int i, long j) {
        if (i == 0) {
            this.mAlarmManager.setExact(2, SystemClock.elapsedRealtime() + j, "ReverseChargingControl", this.mRtxFinishAlarmAction, null);
        } else if (i == 1) {
            this.mAlarmManager.setExact(2, SystemClock.elapsedRealtime() + j, "ReverseChargingControl", this.mRtxFinishRxFullAlarmAction, null);
        } else if (i == 2) {
            this.mAlarmManager.setExact(2, SystemClock.elapsedRealtime() + j, "ReverseChargingControl", this.mCheckNfcConflictWithUsbAudioAlarmAction, null);
        } else if (i == 3) {
            this.mAlarmManager.setExact(2, SystemClock.elapsedRealtime() + j, "ReverseChargingControl", this.mReconnectedTimeoutAlarmAction, null);
        } else if (i == 4) {
            this.mAlarmManager.setExact(2, SystemClock.elapsedRealtime() + j, "ReverseChargingControl", this.mAccessoryDeviceRemovedTimeoutAlarmAction, null);
        }
    }

    private void onAlarmRtxFinish(int i) {
        Log.i("ReverseChargingControl", "onAlarmRtxFinish(): rtx=0, reason: " + i);
        setReverseStateInternal(false, i);
    }

    private void cancelRtxTimer(int i) {
        if (i == 0) {
            this.mAlarmManager.cancel(this.mRtxFinishAlarmAction);
        } else if (i == 1) {
            this.mAlarmManager.cancel(this.mRtxFinishRxFullAlarmAction);
        } else if (i == 3) {
            this.mAlarmManager.cancel(this.mReconnectedTimeoutAlarmAction);
        } else if (i == 4) {
            this.mAlarmManager.cancel(this.mAccessoryDeviceRemovedTimeoutAlarmAction);
        }
    }

    private void logReverseStartEvent(int i) {
        if (DEBUG) {
            Log.d("ReverseChargingControl", "logReverseStartEvent: " + i);
        }
        this.mReverseStartTime = SystemClock.uptimeMillis();
        ReverseChargingMetrics.logStartEvent(i, this.mLevel);
    }

    private void logReverseStopEvent(int i) {
        if (DEBUG) {
            Log.d("ReverseChargingControl", "logReverseStopEvent: " + i);
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        this.mReverseStopTime = uptimeMillis;
        ReverseChargingMetrics.logStopEvent(i, this.mLevel, (uptimeMillis - this.mReverseStartTime) / 1000);
    }

    private void logReverseAccessoryType(int i) {
        if (DEBUG) {
            Log.d("ReverseChargingControl", "logReverseAccessoryType: " + i);
        }
        if (i != 0) {
            ReverseChargingMetrics.logReceiverType(i);
        }
    }

    private void logReverseStateChanged(int i, int i2) {
        int i3 = this.mCurrentRtxMode;
        if (i3 == 1 && i != 1 && this.mReverse) {
            if (i2 != 0) {
                if (i2 == 1) {
                    logReverseStopEvent(4);
                } else if (i2 == 2) {
                    logReverseStopEvent(3);
                } else if (i2 == 3) {
                    logReverseStopEvent(R$styleable.Constraint_layout_goneMarginStart);
                } else if (i2 == 4) {
                    logReverseStopEvent(R$styleable.Constraint_visibilityMode);
                } else if (i2 == 15) {
                    logReverseStopEvent(8);
                }
            } else if (i != 2 || this.mCurrentRtxReceiverType == 0) {
                logReverseStopEvent(1);
            } else {
                logReverseStopEvent(8);
            }
            Log.w("ReverseChargingControl", "Reverse charging error happened : " + i2);
        } else if (i3 != 1 && i == 1 && !this.mReverse) {
            logReverseStartEvent(1);
        }
    }

    private void playSoundIfNecessary(boolean z, int i) {
        boolean z2 = this.mProvidingBattery;
        String str = null;
        if (!z2 && z) {
            if (DEBUG) {
                Log.d("ReverseChargingControl", "playSoundIfNecessary() play start charging sound: " + z + ", accType : " + i + ", mStartReconnected : " + this.mStartReconnected);
            }
            if (!this.mStartReconnected || !shouldEnableAccessoryReconnect(i)) {
                str = this.mContext.getString(R$string.reverse_charging_started_sound);
            }
            this.mStartReconnected = false;
        } else if (z2 && !z) {
            boolean z3 = DEBUG;
            if (z3) {
                Log.d("ReverseChargingControl", "playSoundIfNecessary() play end charging sound: " + z + ", accType : " + i + ", mStartReConnected : " + this.mStartReconnected);
            }
            if (!this.mStartReconnected && shouldEnableAccessoryReconnect(i)) {
                this.mStartReconnected = true;
                if (z3) {
                    Log.w("ReverseChargingControl", "playSoundIfNecessary() start reconnected");
                }
            }
        }
        if (!TextUtils.isEmpty(str)) {
            playSound(RingtoneManager.getRingtone(this.mContext, new Uri.Builder().scheme("file").appendPath(str).build()));
        }
    }

    void playSound(Ringtone ringtone) {
        if (ringtone != null) {
            ringtone.setStreamType(1);
            ringtone.play();
        }
    }

    public void addCallback(ReverseChargingChangeCallback reverseChargingChangeCallback) {
        synchronized (this.mChangeCallbacks) {
            this.mChangeCallbacks.add(reverseChargingChangeCallback);
        }
        reverseChargingChangeCallback.onReverseChargingChanged(this.mReverse, this.mRtxLevel, this.mName);
    }

    public void removeCallback(ReverseChargingChangeCallback reverseChargingChangeCallback) {
        synchronized (this.mChangeCallbacks) {
            this.mChangeCallbacks.remove(reverseChargingChangeCallback);
        }
    }
}
