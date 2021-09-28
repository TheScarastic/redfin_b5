package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkScoreManager;
import android.net.wifi.WifiManager;
import android.text.Html;
import android.text.TextUtils;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.SignalIcon$IconGroup;
import com.android.settingslib.SignalIcon$MobileIconGroup;
import com.android.settingslib.SignalIcon$State;
import com.android.settingslib.graph.SignalDrawable;
import com.android.settingslib.mobile.TelephonyIcons;
import com.android.settingslib.wifi.WifiStatusTracker;
import com.android.systemui.R$bool;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.policy.NetworkController;
import java.io.PrintWriter;
import java.util.Objects;
/* loaded from: classes.dex */
public class WifiSignalController extends SignalController<WifiState, SignalIcon$IconGroup> {
    private final SignalIcon$MobileIconGroup mCarrierMergedWifiIconGroup = TelephonyIcons.CARRIER_MERGED_WIFI;
    private final boolean mHasMobileDataFeature;
    private final boolean mProviderModelSetting;
    private final SignalIcon$IconGroup mUnmergedWifiIconGroup;
    private final WifiManager mWifiManager;
    private final WifiStatusTracker mWifiTracker;

    public WifiSignalController(Context context, boolean z, CallbackHandler callbackHandler, NetworkControllerImpl networkControllerImpl, WifiManager wifiManager, ConnectivityManager connectivityManager, NetworkScoreManager networkScoreManager, FeatureFlags featureFlags) {
        super("WifiSignalController", context, 1, callbackHandler, networkControllerImpl);
        SignalIcon$IconGroup signalIcon$IconGroup = WifiIcons.UNMERGED_WIFI;
        this.mUnmergedWifiIconGroup = signalIcon$IconGroup;
        this.mWifiManager = wifiManager;
        WifiStatusTracker wifiStatusTracker = new WifiStatusTracker(this.mContext, wifiManager, networkScoreManager, connectivityManager, new Runnable() { // from class: com.android.systemui.statusbar.policy.WifiSignalController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                WifiSignalController.m380$r8$lambda$Z4p5zzA9Fv3s3CeygZbi0Jxwk0(WifiSignalController.this);
            }
        });
        this.mWifiTracker = wifiStatusTracker;
        wifiStatusTracker.setListening(true);
        this.mHasMobileDataFeature = z;
        if (wifiManager != null) {
            wifiManager.registerTrafficStateCallback(context.getMainExecutor(), new WifiTrafficStateCallback());
        }
        ((WifiState) this.mLastState).iconGroup = signalIcon$IconGroup;
        ((WifiState) this.mCurrentState).iconGroup = signalIcon$IconGroup;
        this.mProviderModelSetting = featureFlags.isProviderModelSettingEnabled();
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.policy.SignalController
    public WifiState cleanState() {
        return new WifiState();
    }

    /* access modifiers changed from: package-private */
    public void refreshLocale() {
        this.mWifiTracker.refreshLocale();
    }

    @Override // com.android.systemui.statusbar.policy.SignalController
    public void notifyListeners(NetworkController.SignalCallback signalCallback) {
        T t = this.mCurrentState;
        if (!((WifiState) t).isCarrierMerged) {
            notifyListenersForNonCarrierWifi(signalCallback);
        } else if (((WifiState) t).isDefault) {
            notifyListenersForCarrierWifi(signalCallback);
        }
    }

    private void notifyListenersForNonCarrierWifi(NetworkController.SignalCallback signalCallback) {
        int i;
        int i2;
        boolean z = this.mContext.getResources().getBoolean(R$bool.config_showWifiIndicatorWhenEnabled);
        T t = this.mCurrentState;
        boolean z2 = ((WifiState) t).enabled && ((((WifiState) t).connected && ((WifiState) t).inetCondition == 1) || !this.mHasMobileDataFeature || ((WifiState) t).isDefault || z);
        NetworkController.IconState iconState = null;
        String str = ((WifiState) t).connected ? ((WifiState) t).ssid : null;
        boolean z3 = z2 && ((WifiState) t).ssid != null;
        String charSequence = getTextIfExists(getContentDescription()).toString();
        if (((WifiState) this.mCurrentState).inetCondition == 0) {
            charSequence = charSequence + "," + this.mContext.getString(R$string.data_connection_no_internet);
        }
        if (this.mProviderModelSetting) {
            NetworkController.IconState iconState2 = new NetworkController.IconState(z2, getCurrentIconId(), charSequence);
            if (((WifiState) this.mCurrentState).isDefault || (!this.mNetworkController.isRadioOn() && !this.mNetworkController.isEthernetDefault())) {
                boolean z4 = ((WifiState) this.mCurrentState).connected;
                if (this.mWifiTracker.isCaptivePortal) {
                    i2 = R$drawable.ic_qs_wifi_disconnected;
                } else {
                    i2 = getQsCurrentIconId();
                }
                iconState = new NetworkController.IconState(z4, i2, charSequence);
            }
            T t2 = this.mCurrentState;
            signalCallback.setWifiIndicators(new NetworkController.WifiIndicators(((WifiState) t2).enabled, iconState2, iconState, z3 && ((WifiState) t2).activityIn, z3 && ((WifiState) t2).activityOut, str, ((WifiState) t2).isTransient, ((WifiState) t2).statusLabel));
            return;
        }
        NetworkController.IconState iconState3 = new NetworkController.IconState(z2, getCurrentIconId(), charSequence);
        boolean z5 = ((WifiState) this.mCurrentState).connected;
        if (this.mWifiTracker.isCaptivePortal) {
            i = R$drawable.ic_qs_wifi_disconnected;
        } else {
            i = getQsCurrentIconId();
        }
        NetworkController.IconState iconState4 = new NetworkController.IconState(z5, i, charSequence);
        T t3 = this.mCurrentState;
        signalCallback.setWifiIndicators(new NetworkController.WifiIndicators(((WifiState) t3).enabled, iconState3, iconState4, z3 && ((WifiState) t3).activityIn, z3 && ((WifiState) t3).activityOut, str, ((WifiState) t3).isTransient, ((WifiState) t3).statusLabel));
    }

    private void notifyListenersForCarrierWifi(NetworkController.SignalCallback signalCallback) {
        int i;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup = this.mCarrierMergedWifiIconGroup;
        String charSequence = getTextIfExists(getContentDescription()).toString();
        CharSequence textIfExists = getTextIfExists(signalIcon$MobileIconGroup.dataContentDescription);
        String obj = Html.fromHtml(textIfExists.toString(), 0).toString();
        if (((WifiState) this.mCurrentState).inetCondition == 0) {
            obj = this.mContext.getString(R$string.data_connection_no_internet);
        }
        T t = this.mCurrentState;
        boolean z = ((WifiState) t).enabled && ((WifiState) t).connected && ((WifiState) t).isDefault;
        NetworkController.IconState iconState = new NetworkController.IconState(z, getCurrentIconIdForCarrierWifi(), charSequence);
        int i2 = z ? signalIcon$MobileIconGroup.dataType : 0;
        NetworkController.IconState iconState2 = null;
        if (z) {
            int i3 = signalIcon$MobileIconGroup.qsDataType;
            iconState2 = new NetworkController.IconState(((WifiState) this.mCurrentState).connected, getQsCurrentIconIdForCarrierWifi(), charSequence);
            i = i3;
        } else {
            i = 0;
        }
        String networkNameForCarrierWiFi = this.mNetworkController.getNetworkNameForCarrierWiFi(((WifiState) this.mCurrentState).subId);
        T t2 = this.mCurrentState;
        signalCallback.setMobileDataIndicators(new NetworkController.MobileDataIndicators(iconState, iconState2, i2, i, ((WifiState) t2).activityIn, ((WifiState) t2).activityOut, obj, textIfExists, networkNameForCarrierWiFi, signalIcon$MobileIconGroup.isWide, ((WifiState) t2).subId, false, true));
    }

    private int getCurrentIconIdForCarrierWifi() {
        int i = ((WifiState) this.mCurrentState).level;
        boolean z = true;
        int maxSignalLevel = this.mWifiManager.getMaxSignalLevel() + 1;
        T t = this.mCurrentState;
        if (((WifiState) t).inetCondition != 0) {
            z = false;
        }
        if (((WifiState) t).connected) {
            return SignalDrawable.getState(i, maxSignalLevel, z);
        }
        if (((WifiState) t).enabled) {
            return SignalDrawable.getEmptyState(maxSignalLevel);
        }
        return 0;
    }

    private int getQsCurrentIconIdForCarrierWifi() {
        return getCurrentIconIdForCarrierWifi();
    }

    public void fetchInitialState() {
        this.mWifiTracker.fetchInitialState();
        copyWifiStates();
        notifyListenersIfNecessary();
    }

    public void handleBroadcast(Intent intent) {
        this.mWifiTracker.handleBroadcast(intent);
        copyWifiStates();
        notifyListenersIfNecessary();
    }

    /* access modifiers changed from: private */
    public void handleStatusUpdated() {
        copyWifiStates();
        notifyListenersIfNecessary();
    }

    private void copyWifiStates() {
        SignalIcon$IconGroup signalIcon$IconGroup;
        T t = this.mCurrentState;
        WifiStatusTracker wifiStatusTracker = this.mWifiTracker;
        ((WifiState) t).enabled = wifiStatusTracker.enabled;
        ((WifiState) t).isDefault = wifiStatusTracker.isDefaultNetwork;
        ((WifiState) t).connected = wifiStatusTracker.connected;
        ((WifiState) t).ssid = wifiStatusTracker.ssid;
        ((WifiState) t).rssi = wifiStatusTracker.rssi;
        notifyWifiLevelChangeIfNecessary(wifiStatusTracker.level);
        T t2 = this.mCurrentState;
        WifiStatusTracker wifiStatusTracker2 = this.mWifiTracker;
        ((WifiState) t2).level = wifiStatusTracker2.level;
        ((WifiState) t2).statusLabel = wifiStatusTracker2.statusLabel;
        ((WifiState) t2).isCarrierMerged = wifiStatusTracker2.isCarrierMerged;
        ((WifiState) t2).subId = wifiStatusTracker2.subId;
        WifiState wifiState = (WifiState) t2;
        if (((WifiState) t2).isCarrierMerged) {
            signalIcon$IconGroup = this.mCarrierMergedWifiIconGroup;
        } else {
            signalIcon$IconGroup = this.mUnmergedWifiIconGroup;
        }
        wifiState.iconGroup = signalIcon$IconGroup;
    }

    void notifyWifiLevelChangeIfNecessary(int i) {
        if (i != ((WifiState) this.mCurrentState).level) {
            this.mNetworkController.notifyWifiLevelChange(i);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isCarrierMergedWifi(int i) {
        T t = this.mCurrentState;
        return ((WifiState) t).isDefault && ((WifiState) t).isCarrierMerged && ((WifiState) t).subId == i;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void setActivity(int i) {
        T t = this.mCurrentState;
        boolean z = false;
        ((WifiState) t).activityIn = i == 3 || i == 1;
        WifiState wifiState = (WifiState) t;
        if (i == 3 || i == 2) {
            z = true;
        }
        wifiState.activityOut = z;
        notifyListenersIfNecessary();
    }

    @Override // com.android.systemui.statusbar.policy.SignalController
    public void dump(PrintWriter printWriter) {
        super.dump(printWriter);
        this.mWifiTracker.dump(printWriter);
    }

    /* loaded from: classes.dex */
    private class WifiTrafficStateCallback implements WifiManager.TrafficStateCallback {
        private WifiTrafficStateCallback() {
        }

        public void onStateChanged(int i) {
            WifiSignalController.this.setActivity(i);
        }
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class WifiState extends SignalIcon$State {
        public boolean isCarrierMerged;
        public boolean isDefault;
        public boolean isTransient;
        public String ssid;
        public String statusLabel;
        public int subId;

        WifiState() {
        }

        @Override // com.android.settingslib.SignalIcon$State
        public void copyFrom(SignalIcon$State signalIcon$State) {
            super.copyFrom(signalIcon$State);
            WifiState wifiState = (WifiState) signalIcon$State;
            this.ssid = wifiState.ssid;
            this.isTransient = wifiState.isTransient;
            this.isDefault = wifiState.isDefault;
            this.statusLabel = wifiState.statusLabel;
            this.isCarrierMerged = wifiState.isCarrierMerged;
            this.subId = wifiState.subId;
        }

        /* access modifiers changed from: protected */
        @Override // com.android.settingslib.SignalIcon$State
        public void toString(StringBuilder sb) {
            super.toString(sb);
            sb.append(",ssid=");
            sb.append(this.ssid);
            sb.append(",isTransient=");
            sb.append(this.isTransient);
            sb.append(",isDefault=");
            sb.append(this.isDefault);
            sb.append(",statusLabel=");
            sb.append(this.statusLabel);
            sb.append(",isCarrierMerged=");
            sb.append(this.isCarrierMerged);
            sb.append(",subId=");
            sb.append(this.subId);
        }

        @Override // com.android.settingslib.SignalIcon$State
        public boolean equals(Object obj) {
            if (!super.equals(obj)) {
                return false;
            }
            WifiState wifiState = (WifiState) obj;
            if (Objects.equals(wifiState.ssid, this.ssid) && wifiState.isTransient == this.isTransient && wifiState.isDefault == this.isDefault && TextUtils.equals(wifiState.statusLabel, this.statusLabel) && wifiState.isCarrierMerged == this.isCarrierMerged && wifiState.subId == this.subId) {
                return true;
            }
            return false;
        }
    }
}
