package com.android.settingslib.mobile;

import android.os.Handler;
import android.os.Looper;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.mediarouter.media.MediaRoute2Provider$$ExternalSyntheticLambda0;
import java.util.Objects;
/* loaded from: classes.dex */
public class MobileStatusTracker {
    private final Callback mCallback;
    private final SubscriptionDefaults mDefaults;
    private final TelephonyManager mPhone;
    private final Handler mReceiverHandler;
    private final SubscriptionInfo mSubscriptionInfo;
    private final MobileTelephonyCallback mTelephonyCallback = new MobileTelephonyCallback();
    private final MobileStatus mMobileStatus = new MobileStatus();

    /* loaded from: classes.dex */
    public interface Callback {
        void onMobileStatusChanged(boolean z, MobileStatus mobileStatus);
    }

    public MobileStatusTracker(TelephonyManager telephonyManager, Looper looper, SubscriptionInfo subscriptionInfo, SubscriptionDefaults subscriptionDefaults, Callback callback) {
        this.mPhone = telephonyManager;
        Handler handler = new Handler(looper);
        this.mReceiverHandler = handler;
        this.mSubscriptionInfo = subscriptionInfo;
        this.mDefaults = subscriptionDefaults;
        this.mCallback = callback;
        updateDataSim();
        handler.post(new Runnable() { // from class: com.android.settingslib.mobile.MobileStatusTracker$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MobileStatusTracker.$r8$lambda$hp3_06HtFGHEpyHOUdie5xxysLU(MobileStatusTracker.this);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mCallback.onMobileStatusChanged(false, new MobileStatus(this.mMobileStatus));
    }

    public void setListening(boolean z) {
        if (z) {
            TelephonyManager telephonyManager = this.mPhone;
            Handler handler = this.mReceiverHandler;
            Objects.requireNonNull(handler);
            telephonyManager.registerTelephonyCallback(new MediaRoute2Provider$$ExternalSyntheticLambda0(handler), this.mTelephonyCallback);
            return;
        }
        this.mPhone.unregisterTelephonyCallback(this.mTelephonyCallback);
    }

    /* access modifiers changed from: private */
    public void updateDataSim() {
        int activeDataSubId = this.mDefaults.getActiveDataSubId();
        boolean z = true;
        if (SubscriptionManager.isValidSubscriptionId(activeDataSubId)) {
            MobileStatus mobileStatus = this.mMobileStatus;
            if (activeDataSubId != this.mSubscriptionInfo.getSubscriptionId()) {
                z = false;
            }
            mobileStatus.dataSim = z;
            return;
        }
        this.mMobileStatus.dataSim = true;
    }

    /* access modifiers changed from: private */
    public void setActivity(int i) {
        MobileStatus mobileStatus = this.mMobileStatus;
        boolean z = false;
        mobileStatus.activityIn = i == 3 || i == 1;
        if (i == 3 || i == 2) {
            z = true;
        }
        mobileStatus.activityOut = z;
    }

    /* loaded from: classes.dex */
    public class MobileTelephonyCallback extends TelephonyCallback implements TelephonyCallback.ServiceStateListener, TelephonyCallback.SignalStrengthsListener, TelephonyCallback.DataConnectionStateListener, TelephonyCallback.DataActivityListener, TelephonyCallback.CarrierNetworkListener, TelephonyCallback.ActiveDataSubscriptionIdListener, TelephonyCallback.DisplayInfoListener {
        public MobileTelephonyCallback() {
        }

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            String str;
            if (Log.isLoggable("MobileStatusTracker", 3)) {
                StringBuilder sb = new StringBuilder();
                sb.append("onSignalStrengthsChanged signalStrength=");
                sb.append(signalStrength);
                if (signalStrength == null) {
                    str = "";
                } else {
                    str = " level=" + signalStrength.getLevel();
                }
                sb.append(str);
                Log.d("MobileStatusTracker", sb.toString());
            }
            MobileStatusTracker.this.mMobileStatus.signalStrength = signalStrength;
            MobileStatusTracker.this.mCallback.onMobileStatusChanged(true, new MobileStatus(MobileStatusTracker.this.mMobileStatus));
        }

        public void onServiceStateChanged(ServiceState serviceState) {
            Object obj;
            if (Log.isLoggable("MobileStatusTracker", 3)) {
                StringBuilder sb = new StringBuilder();
                sb.append("onServiceStateChanged voiceState=");
                Object obj2 = "";
                if (serviceState == null) {
                    obj = obj2;
                } else {
                    obj = Integer.valueOf(serviceState.getState());
                }
                sb.append(obj);
                sb.append(" dataState=");
                if (serviceState != null) {
                    obj2 = Integer.valueOf(serviceState.getDataRegistrationState());
                }
                sb.append(obj2);
                Log.d("MobileStatusTracker", sb.toString());
            }
            MobileStatusTracker.this.mMobileStatus.serviceState = serviceState;
            MobileStatusTracker.this.mCallback.onMobileStatusChanged(true, new MobileStatus(MobileStatusTracker.this.mMobileStatus));
        }

        public void onDataConnectionStateChanged(int i, int i2) {
            if (Log.isLoggable("MobileStatusTracker", 3)) {
                Log.d("MobileStatusTracker", "onDataConnectionStateChanged: state=" + i + " type=" + i2);
            }
            MobileStatusTracker.this.mMobileStatus.dataState = i;
            MobileStatusTracker.this.mCallback.onMobileStatusChanged(true, new MobileStatus(MobileStatusTracker.this.mMobileStatus));
        }

        public void onDataActivity(int i) {
            if (Log.isLoggable("MobileStatusTracker", 3)) {
                Log.d("MobileStatusTracker", "onDataActivity: direction=" + i);
            }
            MobileStatusTracker.this.setActivity(i);
            MobileStatusTracker.this.mCallback.onMobileStatusChanged(false, new MobileStatus(MobileStatusTracker.this.mMobileStatus));
        }

        public void onCarrierNetworkChange(boolean z) {
            if (Log.isLoggable("MobileStatusTracker", 3)) {
                Log.d("MobileStatusTracker", "onCarrierNetworkChange: active=" + z);
            }
            MobileStatusTracker.this.mMobileStatus.carrierNetworkChangeMode = z;
            MobileStatusTracker.this.mCallback.onMobileStatusChanged(true, new MobileStatus(MobileStatusTracker.this.mMobileStatus));
        }

        public void onActiveDataSubscriptionIdChanged(int i) {
            if (Log.isLoggable("MobileStatusTracker", 3)) {
                Log.d("MobileStatusTracker", "onActiveDataSubscriptionIdChanged: subId=" + i);
            }
            MobileStatusTracker.this.updateDataSim();
            MobileStatusTracker.this.mCallback.onMobileStatusChanged(true, new MobileStatus(MobileStatusTracker.this.mMobileStatus));
        }

        public void onDisplayInfoChanged(TelephonyDisplayInfo telephonyDisplayInfo) {
            if (Log.isLoggable("MobileStatusTracker", 3)) {
                Log.d("MobileStatusTracker", "onDisplayInfoChanged: telephonyDisplayInfo=" + telephonyDisplayInfo);
            }
            MobileStatusTracker.this.mMobileStatus.telephonyDisplayInfo = telephonyDisplayInfo;
            MobileStatusTracker.this.mCallback.onMobileStatusChanged(true, new MobileStatus(MobileStatusTracker.this.mMobileStatus));
        }
    }

    /* loaded from: classes.dex */
    public static class SubscriptionDefaults {
        public int getDefaultVoiceSubId() {
            return SubscriptionManager.getDefaultVoiceSubscriptionId();
        }

        public int getDefaultDataSubId() {
            return SubscriptionManager.getDefaultDataSubscriptionId();
        }

        public int getActiveDataSubId() {
            return SubscriptionManager.getActiveDataSubscriptionId();
        }
    }

    /* loaded from: classes.dex */
    public static class MobileStatus {
        public boolean activityIn;
        public boolean activityOut;
        public boolean carrierNetworkChangeMode;
        public boolean dataSim;
        public ServiceState serviceState;
        public SignalStrength signalStrength;
        public int dataState = 0;
        public TelephonyDisplayInfo telephonyDisplayInfo = new TelephonyDisplayInfo(0, 0);

        public MobileStatus() {
        }

        public MobileStatus(MobileStatus mobileStatus) {
            copyFrom(mobileStatus);
        }

        protected void copyFrom(MobileStatus mobileStatus) {
            this.activityIn = mobileStatus.activityIn;
            this.activityOut = mobileStatus.activityOut;
            this.dataSim = mobileStatus.dataSim;
            this.carrierNetworkChangeMode = mobileStatus.carrierNetworkChangeMode;
            this.dataState = mobileStatus.dataState;
            this.serviceState = mobileStatus.serviceState;
            this.signalStrength = mobileStatus.signalStrength;
            this.telephonyDisplayInfo = mobileStatus.telephonyDisplayInfo;
        }

        public String toString() {
            String str;
            Object obj;
            StringBuilder sb = new StringBuilder();
            sb.append("[activityIn=");
            sb.append(this.activityIn);
            sb.append(',');
            sb.append("activityOut=");
            sb.append(this.activityOut);
            sb.append(',');
            sb.append("dataSim=");
            sb.append(this.dataSim);
            sb.append(',');
            sb.append("carrierNetworkChangeMode=");
            sb.append(this.carrierNetworkChangeMode);
            sb.append(',');
            sb.append("dataState=");
            sb.append(this.dataState);
            sb.append(',');
            sb.append("serviceState=");
            String str2 = "";
            if (this.serviceState == null) {
                str = str2;
            } else {
                str = "mVoiceRegState=" + this.serviceState.getState() + "(" + ServiceState.rilServiceStateToString(this.serviceState.getState()) + "), mDataRegState=" + this.serviceState.getDataRegState() + "(" + ServiceState.rilServiceStateToString(this.serviceState.getDataRegState()) + ")";
            }
            sb.append(str);
            sb.append(',');
            sb.append("signalStrength=");
            SignalStrength signalStrength = this.signalStrength;
            if (signalStrength == null) {
                obj = str2;
            } else {
                obj = Integer.valueOf(signalStrength.getLevel());
            }
            sb.append(obj);
            sb.append(',');
            sb.append("telephonyDisplayInfo=");
            TelephonyDisplayInfo telephonyDisplayInfo = this.telephonyDisplayInfo;
            if (telephonyDisplayInfo != null) {
                str2 = telephonyDisplayInfo.toString();
            }
            sb.append(str2);
            sb.append(']');
            return sb.toString();
        }
    }
}
