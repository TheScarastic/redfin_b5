package com.android.keyguard;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.keyguard.CarrierTextManager;
import com.android.settingslib.WirelessUtils;
import com.android.systemui.R$string;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.telephony.TelephonyListenerManager;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes.dex */
public class CarrierTextManager {
    private final Executor mBgExecutor;
    protected final KeyguardUpdateMonitorCallback mCallback;
    private CarrierTextCallback mCarrierTextCallback;
    private final Context mContext;
    private final boolean mIsEmergencyCallCapable;
    protected KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final Executor mMainExecutor;
    private final AtomicBoolean mNetworkSupported;
    private final TelephonyCallback.ActiveDataSubscriptionIdListener mPhoneStateListener;
    private final CharSequence mSeparator;
    private final boolean mShowAirplaneMode;
    private final boolean mShowMissingSim;
    private final boolean[] mSimErrorState;
    private final int mSimSlotsNumber;
    private boolean mTelephonyCapable;
    private final TelephonyListenerManager mTelephonyListenerManager;
    private final TelephonyManager mTelephonyManager;
    private final WakefulnessLifecycle mWakefulnessLifecycle;
    private final WakefulnessLifecycle.Observer mWakefulnessObserver;
    private final WifiManager mWifiManager;

    /* loaded from: classes.dex */
    public interface CarrierTextCallback {
        default void finishedWakingUp() {
        }

        default void startedGoingToSleep() {
        }

        default void updateCarrierInfo(CarrierTextCallbackInfo carrierTextCallbackInfo) {
        }
    }

    /* loaded from: classes.dex */
    public enum StatusMode {
        Normal,
        NetworkLocked,
        SimMissing,
        SimMissingLocked,
        SimPukLocked,
        SimLocked,
        SimPermDisabled,
        SimNotReady,
        SimIoError,
        SimUnknown
    }

    private CarrierTextManager(Context context, CharSequence charSequence, boolean z, boolean z2, WifiManager wifiManager, TelephonyManager telephonyManager, TelephonyListenerManager telephonyListenerManager, WakefulnessLifecycle wakefulnessLifecycle, Executor executor, Executor executor2, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        this.mNetworkSupported = new AtomicBoolean();
        this.mWakefulnessObserver = new WakefulnessLifecycle.Observer() { // from class: com.android.keyguard.CarrierTextManager.1
            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public void onFinishedWakingUp() {
                CarrierTextCallback carrierTextCallback = CarrierTextManager.this.mCarrierTextCallback;
                if (carrierTextCallback != null) {
                    carrierTextCallback.finishedWakingUp();
                }
            }

            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public void onStartedGoingToSleep() {
                CarrierTextCallback carrierTextCallback = CarrierTextManager.this.mCarrierTextCallback;
                if (carrierTextCallback != null) {
                    carrierTextCallback.startedGoingToSleep();
                }
            }
        };
        this.mCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.keyguard.CarrierTextManager.2
            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onRefreshCarrierInfo() {
                CarrierTextManager.this.updateCarrierText();
            }

            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onTelephonyCapable(boolean z3) {
                CarrierTextManager.this.mTelephonyCapable = z3;
                CarrierTextManager.this.updateCarrierText();
            }

            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onSimStateChanged(int i, int i2, int i3) {
                if (i2 < 0 || i2 >= CarrierTextManager.this.mSimSlotsNumber) {
                    Log.d("CarrierTextController", "onSimStateChanged() - slotId invalid: " + i2 + " mTelephonyCapable: " + Boolean.toString(CarrierTextManager.this.mTelephonyCapable));
                } else if (CarrierTextManager.this.getStatusForIccState(i3) == StatusMode.SimIoError) {
                    CarrierTextManager.this.mSimErrorState[i2] = true;
                    CarrierTextManager.this.updateCarrierText();
                } else if (CarrierTextManager.this.mSimErrorState[i2]) {
                    CarrierTextManager.this.mSimErrorState[i2] = false;
                    CarrierTextManager.this.updateCarrierText();
                }
            }
        };
        this.mPhoneStateListener = new TelephonyCallback.ActiveDataSubscriptionIdListener() { // from class: com.android.keyguard.CarrierTextManager.3
            public void onActiveDataSubscriptionIdChanged(int i) {
                if (CarrierTextManager.this.mNetworkSupported.get() && CarrierTextManager.this.mCarrierTextCallback != null) {
                    CarrierTextManager.this.updateCarrierText();
                }
            }
        };
        this.mContext = context;
        this.mIsEmergencyCallCapable = telephonyManager.isVoiceCapable();
        this.mShowAirplaneMode = z;
        this.mShowMissingSim = z2;
        this.mWifiManager = wifiManager;
        this.mTelephonyManager = telephonyManager;
        this.mSeparator = charSequence;
        this.mTelephonyListenerManager = telephonyListenerManager;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        int supportedModemCount = getTelephonyManager().getSupportedModemCount();
        this.mSimSlotsNumber = supportedModemCount;
        this.mSimErrorState = new boolean[supportedModemCount];
        this.mMainExecutor = executor;
        this.mBgExecutor = executor2;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        executor2.execute(new Runnable() { // from class: com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                CarrierTextManager.$r8$lambda$L9d1mgw5vNjn8Mg05dhUwzOrxVU(CarrierTextManager.this);
            }
        });
    }

    public /* synthetic */ void lambda$new$0() {
        boolean hasSystemFeature = this.mContext.getPackageManager().hasSystemFeature("android.hardware.telephony");
        if (hasSystemFeature && this.mNetworkSupported.compareAndSet(false, hasSystemFeature)) {
            lambda$setListening$4(this.mCarrierTextCallback);
        }
    }

    private TelephonyManager getTelephonyManager() {
        return this.mTelephonyManager;
    }

    private CharSequence updateCarrierTextWithSimIoError(CharSequence charSequence, CharSequence[] charSequenceArr, int[] iArr, boolean z) {
        CharSequence carrierTextForSimState = getCarrierTextForSimState(8, "");
        for (int i = 0; i < getTelephonyManager().getActiveModemCount(); i++) {
            if (this.mSimErrorState[i]) {
                if (z) {
                    return concatenate(carrierTextForSimState, getContext().getText(17040137), this.mSeparator);
                }
                if (iArr[i] != -1) {
                    int i2 = iArr[i];
                    charSequenceArr[i2] = concatenate(carrierTextForSimState, charSequenceArr[i2], this.mSeparator);
                } else {
                    charSequence = concatenate(charSequence, carrierTextForSimState, this.mSeparator);
                }
            }
        }
        return charSequence;
    }

    /* renamed from: handleSetListening */
    public void lambda$setListening$4(CarrierTextCallback carrierTextCallback) {
        if (carrierTextCallback != null) {
            this.mCarrierTextCallback = carrierTextCallback;
            if (this.mNetworkSupported.get()) {
                this.mMainExecutor.execute(new Runnable() { // from class: com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        CarrierTextManager.$r8$lambda$yPhTveG85QRLdQUPcWaqi_CIZI0(CarrierTextManager.this);
                    }
                });
                this.mWakefulnessLifecycle.addObserver(this.mWakefulnessObserver);
                this.mTelephonyListenerManager.addActiveDataSubscriptionIdListener(this.mPhoneStateListener);
                return;
            }
            this.mMainExecutor.execute(new Runnable() { // from class: com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    CarrierTextManager.m5$r8$lambda$63achlzikhs62sf08YycVzB9Zk(CarrierTextManager.CarrierTextCallback.this);
                }
            });
            return;
        }
        this.mCarrierTextCallback = null;
        this.mMainExecutor.execute(new Runnable() { // from class: com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                CarrierTextManager.$r8$lambda$b5bD2vFXFy_TMV_aPSbFpUnQzPE(CarrierTextManager.this);
            }
        });
        this.mWakefulnessLifecycle.removeObserver(this.mWakefulnessObserver);
        this.mTelephonyListenerManager.removeActiveDataSubscriptionIdListener(this.mPhoneStateListener);
    }

    public /* synthetic */ void lambda$handleSetListening$1() {
        this.mKeyguardUpdateMonitor.registerCallback(this.mCallback);
    }

    public static /* synthetic */ void lambda$handleSetListening$2(CarrierTextCallback carrierTextCallback) {
        carrierTextCallback.updateCarrierInfo(new CarrierTextCallbackInfo("", null, false, null));
    }

    public /* synthetic */ void lambda$handleSetListening$3() {
        this.mKeyguardUpdateMonitor.removeCallback(this.mCallback);
    }

    public void setListening(CarrierTextCallback carrierTextCallback) {
        this.mBgExecutor.execute(new Runnable(carrierTextCallback) { // from class: com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda5
            public final /* synthetic */ CarrierTextManager.CarrierTextCallback f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                CarrierTextManager.this.lambda$setListening$4(this.f$1);
            }
        });
    }

    protected List<SubscriptionInfo> getSubscriptionInfo() {
        return this.mKeyguardUpdateMonitor.getFilteredSubscriptionInfo(false);
    }

    protected void updateCarrierText() {
        String str;
        boolean z;
        CharSequence charSequence;
        ServiceState serviceState;
        WifiManager wifiManager;
        List<SubscriptionInfo> subscriptionInfo = getSubscriptionInfo();
        int size = subscriptionInfo.size();
        int[] iArr = new int[size];
        int[] iArr2 = new int[this.mSimSlotsNumber];
        for (int i = 0; i < this.mSimSlotsNumber; i++) {
            iArr2[i] = -1;
        }
        CharSequence[] charSequenceArr = new CharSequence[size];
        int i2 = 0;
        boolean z2 = false;
        boolean z3 = true;
        while (true) {
            str = "";
            if (i2 >= size) {
                break;
            }
            int subscriptionId = subscriptionInfo.get(i2).getSubscriptionId();
            charSequenceArr[i2] = str;
            iArr[i2] = subscriptionId;
            iArr2[subscriptionInfo.get(i2).getSimSlotIndex()] = i2;
            int simState = this.mKeyguardUpdateMonitor.getSimState(subscriptionId);
            CharSequence carrierTextForSimState = getCarrierTextForSimState(simState, subscriptionInfo.get(i2).getCarrierName());
            if (carrierTextForSimState != null) {
                charSequenceArr[i2] = carrierTextForSimState;
                z3 = false;
            }
            if (simState == 5 && (serviceState = this.mKeyguardUpdateMonitor.mServiceStates.get(Integer.valueOf(subscriptionId))) != null && serviceState.getDataRegistrationState() == 0 && !(serviceState.getRilDataRadioTechnology() == 18 && ((wifiManager = this.mWifiManager) == null || !wifiManager.isWifiEnabled() || this.mWifiManager.getConnectionInfo() == null || this.mWifiManager.getConnectionInfo().getBSSID() == null))) {
                z2 = true;
            }
            i2++;
        }
        CharSequence charSequence2 = null;
        if (z3 && !z2) {
            if (size != 0) {
                charSequence2 = makeCarrierStringOnEmergencyCapable(getMissingSimMessage(), subscriptionInfo.get(0).getCarrierName());
            } else {
                CharSequence text = getContext().getText(17040137);
                Intent registerReceiver = getContext().registerReceiver(null, new IntentFilter("android.telephony.action.SERVICE_PROVIDERS_UPDATED"));
                if (registerReceiver != null) {
                    String stringExtra = registerReceiver.getBooleanExtra("android.telephony.extra.SHOW_SPN", false) ? registerReceiver.getStringExtra("android.telephony.extra.SPN") : str;
                    if (registerReceiver.getBooleanExtra("android.telephony.extra.SHOW_PLMN", false)) {
                        str = registerReceiver.getStringExtra("android.telephony.extra.PLMN");
                    }
                    if (Objects.equals(str, stringExtra)) {
                        text = str;
                    } else {
                        text = concatenate(str, stringExtra, this.mSeparator);
                    }
                }
                charSequence2 = makeCarrierStringOnEmergencyCapable(getMissingSimMessage(), text);
            }
        }
        if (TextUtils.isEmpty(charSequence2)) {
            charSequence2 = joinNotEmpty(this.mSeparator, charSequenceArr);
        }
        CharSequence updateCarrierTextWithSimIoError = updateCarrierTextWithSimIoError(charSequence2, charSequenceArr, iArr2, z3);
        if (z2 || !WirelessUtils.isAirplaneModeOn(this.mContext)) {
            z = false;
            charSequence = updateCarrierTextWithSimIoError;
        } else {
            charSequence = getAirplaneModeMessage();
            z = true;
        }
        postToCallback(new CarrierTextCallbackInfo(charSequence, charSequenceArr, true ^ z3, iArr, z));
    }

    protected void postToCallback(CarrierTextCallbackInfo carrierTextCallbackInfo) {
        CarrierTextCallback carrierTextCallback = this.mCarrierTextCallback;
        if (carrierTextCallback != null) {
            this.mMainExecutor.execute(new Runnable(carrierTextCallbackInfo) { // from class: com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda1
                public final /* synthetic */ CarrierTextManager.CarrierTextCallbackInfo f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    CarrierTextManager.$r8$lambda$1PcBlFCWZvW0k91my1MlV7sgoXc(CarrierTextManager.CarrierTextCallback.this, this.f$1);
                }
            });
        }
    }

    private Context getContext() {
        return this.mContext;
    }

    private String getMissingSimMessage() {
        return (!this.mShowMissingSim || !this.mTelephonyCapable) ? "" : getContext().getString(R$string.keyguard_missing_sim_message_short);
    }

    private String getAirplaneModeMessage() {
        return this.mShowAirplaneMode ? getContext().getString(R$string.airplane_mode) : "";
    }

    /* renamed from: com.android.keyguard.CarrierTextManager$4 */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode;

        static {
            int[] iArr = new int[StatusMode.values().length];
            $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode = iArr;
            try {
                iArr[StatusMode.Normal.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode[StatusMode.SimNotReady.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode[StatusMode.NetworkLocked.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode[StatusMode.SimMissing.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode[StatusMode.SimPermDisabled.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode[StatusMode.SimMissingLocked.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode[StatusMode.SimLocked.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode[StatusMode.SimPukLocked.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode[StatusMode.SimIoError.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode[StatusMode.SimUnknown.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
        }
    }

    private CharSequence getCarrierTextForSimState(int i, CharSequence charSequence) {
        switch (AnonymousClass4.$SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode[getStatusForIccState(i).ordinal()]) {
            case 1:
                return charSequence;
            case 2:
                return "";
            case 3:
                return makeCarrierStringOnEmergencyCapable(this.mContext.getText(R$string.keyguard_network_locked_message), charSequence);
            case 4:
            case 6:
            case 10:
            default:
                return null;
            case 5:
                return makeCarrierStringOnEmergencyCapable(getContext().getText(R$string.keyguard_permanent_disabled_sim_message_short), charSequence);
            case 7:
                return makeCarrierStringOnLocked(getContext().getText(R$string.keyguard_sim_locked_message), charSequence);
            case 8:
                return makeCarrierStringOnLocked(getContext().getText(R$string.keyguard_sim_puk_locked_message), charSequence);
            case 9:
                return makeCarrierStringOnEmergencyCapable(getContext().getText(R$string.keyguard_sim_error_message_short), charSequence);
        }
    }

    private CharSequence makeCarrierStringOnEmergencyCapable(CharSequence charSequence, CharSequence charSequence2) {
        return this.mIsEmergencyCallCapable ? concatenate(charSequence, charSequence2, this.mSeparator) : charSequence;
    }

    private CharSequence makeCarrierStringOnLocked(CharSequence charSequence, CharSequence charSequence2) {
        boolean z = !TextUtils.isEmpty(charSequence);
        boolean z2 = !TextUtils.isEmpty(charSequence2);
        if (z && z2) {
            return this.mContext.getString(R$string.keyguard_carrier_name_with_sim_locked_template, charSequence2, charSequence);
        }
        if (z) {
            return charSequence;
        }
        return z2 ? charSequence2 : "";
    }

    public StatusMode getStatusForIccState(int i) {
        boolean z = true;
        if (this.mKeyguardUpdateMonitor.isDeviceProvisioned() || !(i == 1 || i == 7)) {
            z = false;
        }
        if (z) {
            i = 4;
        }
        switch (i) {
            case 0:
                return StatusMode.SimUnknown;
            case 1:
                return StatusMode.SimMissing;
            case 2:
                return StatusMode.SimLocked;
            case 3:
                return StatusMode.SimPukLocked;
            case 4:
                return StatusMode.SimMissingLocked;
            case 5:
                return StatusMode.Normal;
            case 6:
                return StatusMode.SimNotReady;
            case 7:
                return StatusMode.SimPermDisabled;
            case 8:
                return StatusMode.SimIoError;
            default:
                return StatusMode.SimUnknown;
        }
    }

    /* JADX DEBUG: TODO: convert one arg to string using `String.valueOf()`, args: [(r2v0 java.lang.CharSequence), (r4v0 java.lang.CharSequence), (r3v0 java.lang.CharSequence)] */
    private static CharSequence concatenate(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3) {
        boolean z = !TextUtils.isEmpty(charSequence);
        boolean z2 = !TextUtils.isEmpty(charSequence2);
        if (z && z2) {
            StringBuilder sb = new StringBuilder();
            sb.append(charSequence);
            sb.append(charSequence3);
            sb.append(charSequence2);
            return sb.toString();
        } else if (z) {
            return charSequence;
        } else {
            return z2 ? charSequence2 : "";
        }
    }

    private static CharSequence joinNotEmpty(CharSequence charSequence, CharSequence[] charSequenceArr) {
        int length = charSequenceArr.length;
        if (length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (!TextUtils.isEmpty(charSequenceArr[i])) {
                if (!TextUtils.isEmpty(sb)) {
                    sb.append(charSequence);
                }
                sb.append(charSequenceArr[i]);
            }
        }
        return sb.toString();
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private final Executor mBgExecutor;
        private final Context mContext;
        private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
        private final Executor mMainExecutor;
        private final String mSeparator;
        private boolean mShowAirplaneMode;
        private boolean mShowMissingSim;
        private final TelephonyListenerManager mTelephonyListenerManager;
        private final TelephonyManager mTelephonyManager;
        private final WakefulnessLifecycle mWakefulnessLifecycle;
        private final WifiManager mWifiManager;

        public Builder(Context context, Resources resources, WifiManager wifiManager, TelephonyManager telephonyManager, TelephonyListenerManager telephonyListenerManager, WakefulnessLifecycle wakefulnessLifecycle, Executor executor, Executor executor2, KeyguardUpdateMonitor keyguardUpdateMonitor) {
            this.mContext = context;
            this.mSeparator = resources.getString(17040488);
            this.mWifiManager = wifiManager;
            this.mTelephonyManager = telephonyManager;
            this.mTelephonyListenerManager = telephonyListenerManager;
            this.mWakefulnessLifecycle = wakefulnessLifecycle;
            this.mMainExecutor = executor;
            this.mBgExecutor = executor2;
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        }

        public Builder setShowAirplaneMode(boolean z) {
            this.mShowAirplaneMode = z;
            return this;
        }

        public Builder setShowMissingSim(boolean z) {
            this.mShowMissingSim = z;
            return this;
        }

        public CarrierTextManager build() {
            return new CarrierTextManager(this.mContext, this.mSeparator, this.mShowAirplaneMode, this.mShowMissingSim, this.mWifiManager, this.mTelephonyManager, this.mTelephonyListenerManager, this.mWakefulnessLifecycle, this.mMainExecutor, this.mBgExecutor, this.mKeyguardUpdateMonitor);
        }
    }

    /* loaded from: classes.dex */
    public static final class CarrierTextCallbackInfo {
        public boolean airplaneMode;
        public final boolean anySimReady;
        public final CharSequence carrierText;
        public final CharSequence[] listOfCarriers;
        public final int[] subscriptionIds;

        public CarrierTextCallbackInfo(CharSequence charSequence, CharSequence[] charSequenceArr, boolean z, int[] iArr) {
            this(charSequence, charSequenceArr, z, iArr, false);
        }

        public CarrierTextCallbackInfo(CharSequence charSequence, CharSequence[] charSequenceArr, boolean z, int[] iArr, boolean z2) {
            this.carrierText = charSequence;
            this.listOfCarriers = charSequenceArr;
            this.anySimReady = z;
            this.subscriptionIds = iArr;
            this.airplaneMode = z2;
        }
    }
}
