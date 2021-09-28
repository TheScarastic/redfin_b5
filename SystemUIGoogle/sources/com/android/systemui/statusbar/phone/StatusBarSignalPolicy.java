package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.os.Handler;
import android.telephony.SubscriptionInfo;
import android.util.ArraySet;
import android.util.Log;
import com.android.settingslib.mobile.TelephonyIcons;
import com.android.systemui.R$bool;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.policy.NetworkController;
import com.android.systemui.statusbar.policy.SecurityController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.CarrierConfigTracker;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class StatusBarSignalPolicy implements NetworkController.SignalCallback, SecurityController.SecurityControllerCallback, TunerService.Tunable {
    private static final boolean DEBUG = Log.isLoggable("StatusBarSignalPolicy", 3);
    private boolean mActivityEnabled;
    private final CarrierConfigTracker mCarrierConfigTracker;
    private final Context mContext;
    private final FeatureFlags mFeatureFlags;
    private boolean mForceHideWifi;
    private boolean mHideAirplane;
    private boolean mHideEthernet;
    private boolean mHideMobile;
    private boolean mHideWifi;
    private final StatusBarIconController mIconController;
    private final NetworkController mNetworkController;
    private final SecurityController mSecurityController;
    private final String mSlotAirplane;
    private final String mSlotCallStrength;
    private final String mSlotEthernet;
    private final String mSlotMobile;
    private final String mSlotNoCalling;
    private final String mSlotVpn;
    private final String mSlotWifi;
    private final TunerService mTunerService;
    private final Handler mHandler = Handler.getMain();
    private boolean mIsAirplaneMode = false;
    private boolean mIsWifiEnabled = false;
    private boolean mWifiVisible = false;
    private ArrayList<MobileIconState> mMobileStates = new ArrayList<>();
    private ArrayList<CallIndicatorIconState> mCallIndicatorStates = new ArrayList<>();
    private WifiIconState mWifiIconState = new WifiIconState();

    @Override // com.android.systemui.statusbar.policy.NetworkController.SignalCallback
    public void setMobileDataEnabled(boolean z) {
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.SignalCallback
    public void setNoSims(boolean z, boolean z2) {
    }

    public StatusBarSignalPolicy(Context context, StatusBarIconController statusBarIconController, CarrierConfigTracker carrierConfigTracker, NetworkController networkController, SecurityController securityController, TunerService tunerService, FeatureFlags featureFlags) {
        this.mContext = context;
        this.mIconController = statusBarIconController;
        this.mCarrierConfigTracker = carrierConfigTracker;
        this.mNetworkController = networkController;
        this.mSecurityController = securityController;
        this.mTunerService = tunerService;
        this.mFeatureFlags = featureFlags;
        this.mSlotAirplane = context.getString(17041426);
        this.mSlotMobile = context.getString(17041444);
        this.mSlotWifi = context.getString(17041460);
        this.mSlotEthernet = context.getString(17041437);
        this.mSlotVpn = context.getString(17041459);
        this.mSlotNoCalling = context.getString(17041447);
        this.mSlotCallStrength = context.getString(17041430);
        this.mActivityEnabled = context.getResources().getBoolean(R$bool.config_showActivity);
        tunerService.addTunable(this, "icon_blacklist");
        networkController.addCallback(this);
        securityController.addCallback(this);
    }

    /* access modifiers changed from: private */
    public void updateVpn() {
        boolean isVpnEnabled = this.mSecurityController.isVpnEnabled();
        this.mIconController.setIcon(this.mSlotVpn, currentVpnIconId(this.mSecurityController.isVpnBranded()), this.mContext.getResources().getString(R$string.accessibility_vpn_on));
        this.mIconController.setIconVisibility(this.mSlotVpn, isVpnEnabled);
    }

    private int currentVpnIconId(boolean z) {
        return z ? R$drawable.stat_sys_branded_vpn : R$drawable.stat_sys_vpn_ic;
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController.SecurityControllerCallback
    public void onStateChanged() {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarSignalPolicy$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                StatusBarSignalPolicy.this.updateVpn();
            }
        });
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public void onTuningChanged(String str, String str2) {
        if ("icon_blacklist".equals(str)) {
            ArraySet<String> iconHideList = StatusBarIconController.getIconHideList(this.mContext, str2);
            boolean contains = iconHideList.contains(this.mSlotAirplane);
            boolean contains2 = iconHideList.contains(this.mSlotMobile);
            boolean contains3 = iconHideList.contains(this.mSlotWifi);
            boolean contains4 = iconHideList.contains(this.mSlotEthernet);
            if (contains != this.mHideAirplane || contains2 != this.mHideMobile || contains4 != this.mHideEthernet || contains3 != this.mHideWifi) {
                this.mHideAirplane = contains;
                this.mHideMobile = contains2;
                this.mHideEthernet = contains4;
                this.mHideWifi = contains3 || this.mForceHideWifi;
                this.mNetworkController.removeCallback(this);
                this.mNetworkController.addCallback(this);
            }
        }
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.SignalCallback
    public void setWifiIndicators(NetworkController.WifiIndicators wifiIndicators) {
        boolean z;
        if (DEBUG) {
            Log.d("StatusBarSignalPolicy", "setWifiIndicators: " + wifiIndicators);
        }
        boolean z2 = false;
        boolean z3 = wifiIndicators.statusIcon.visible && !this.mHideWifi;
        boolean z4 = wifiIndicators.activityIn && this.mActivityEnabled && z3;
        boolean z5 = wifiIndicators.activityOut && this.mActivityEnabled && z3;
        this.mIsWifiEnabled = wifiIndicators.enabled;
        WifiIconState copy = this.mWifiIconState.copy();
        WifiIconState wifiIconState = this.mWifiIconState;
        boolean z6 = wifiIconState.noDefaultNetwork;
        if (z6 && wifiIconState.noNetworksAvailable && !this.mIsAirplaneMode) {
            copy.visible = true;
            copy.resId = R$drawable.ic_qs_no_internet_unavailable;
        } else if (!z6 || wifiIconState.noNetworksAvailable || ((z = this.mIsAirplaneMode) && (!z || !this.mIsWifiEnabled))) {
            copy.visible = z3;
            NetworkController.IconState iconState = wifiIndicators.statusIcon;
            copy.resId = iconState.icon;
            copy.activityIn = z4;
            copy.activityOut = z5;
            copy.contentDescription = iconState.contentDescription;
            MobileIconState firstMobileState = getFirstMobileState();
            if (!(firstMobileState == null || firstMobileState.typeId == 0)) {
                z2 = true;
            }
            copy.signalSpacerVisible = z2;
        } else {
            copy.visible = true;
            copy.resId = R$drawable.ic_qs_no_internet_available;
        }
        copy.slot = this.mSlotWifi;
        copy.airplaneSpacerVisible = this.mIsAirplaneMode;
        updateWifiIconWithState(copy);
        this.mWifiIconState = copy;
    }

    private void updateShowWifiSignalSpacer(WifiIconState wifiIconState) {
        MobileIconState firstMobileState = getFirstMobileState();
        wifiIconState.signalSpacerVisible = (firstMobileState == null || firstMobileState.typeId == 0) ? false : true;
    }

    private void updateWifiIconWithState(WifiIconState wifiIconState) {
        if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("WifiIconState: ");
            sb.append(wifiIconState);
            Log.d("StatusBarSignalPolicy", sb.toString() == null ? "" : wifiIconState.toString());
        }
        if (!wifiIconState.visible || wifiIconState.resId <= 0) {
            this.mIconController.setIconVisibility(this.mSlotWifi, false);
            return;
        }
        this.mIconController.setSignalIcon(this.mSlotWifi, wifiIconState);
        this.mIconController.setIconVisibility(this.mSlotWifi, true);
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.SignalCallback
    public void setCallIndicator(NetworkController.IconState iconState, int i) {
        if (DEBUG) {
            Log.d("StatusBarSignalPolicy", "setCallIndicator: statusIcon = " + iconState + ",subId = " + i);
        }
        CallIndicatorIconState noCallingState = getNoCallingState(i);
        if (noCallingState != null) {
            int i2 = iconState.icon;
            if (i2 == R$drawable.ic_qs_no_calling_sms) {
                noCallingState.isNoCalling = iconState.visible;
                noCallingState.noCallingDescription = iconState.contentDescription;
            } else {
                noCallingState.callStrengthResId = i2;
                noCallingState.callStrengthDescription = iconState.contentDescription;
            }
            if (this.mCarrierConfigTracker.getCallStrengthConfig(i)) {
                this.mIconController.setCallStrengthIcons(this.mSlotCallStrength, CallIndicatorIconState.copyStates(this.mCallIndicatorStates));
            } else {
                this.mIconController.removeIcon(this.mSlotCallStrength, i);
            }
            this.mIconController.setNoCallingIcons(this.mSlotNoCalling, CallIndicatorIconState.copyStates(this.mCallIndicatorStates));
        }
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.SignalCallback
    public void setMobileDataIndicators(NetworkController.MobileDataIndicators mobileDataIndicators) {
        boolean z = DEBUG;
        if (z) {
            Log.d("StatusBarSignalPolicy", "setMobileDataIndicators: " + mobileDataIndicators);
        }
        MobileIconState state = getState(mobileDataIndicators.subId);
        if (state != null) {
            int i = mobileDataIndicators.statusType;
            int i2 = state.typeId;
            boolean z2 = true;
            boolean z3 = i != i2 && (i == 0 || i2 == 0);
            NetworkController.IconState iconState = mobileDataIndicators.statusIcon;
            state.visible = iconState.visible && !this.mHideMobile;
            state.strengthId = iconState.icon;
            state.typeId = i;
            state.contentDescription = iconState.contentDescription;
            state.typeContentDescription = mobileDataIndicators.typeContentDescription;
            state.showTriangle = mobileDataIndicators.showTriangle;
            state.roaming = mobileDataIndicators.roaming;
            state.activityIn = mobileDataIndicators.activityIn && this.mActivityEnabled;
            if (!mobileDataIndicators.activityOut || !this.mActivityEnabled) {
                z2 = false;
            }
            state.activityOut = z2;
            if (z) {
                StringBuilder sb = new StringBuilder();
                sb.append("MobileIconStates: ");
                ArrayList<MobileIconState> arrayList = this.mMobileStates;
                sb.append(arrayList == null ? "" : arrayList.toString());
                Log.d("StatusBarSignalPolicy", sb.toString());
            }
            this.mIconController.setMobileIcons(this.mSlotMobile, MobileIconState.copyStates(this.mMobileStates));
            if (z3) {
                WifiIconState copy = this.mWifiIconState.copy();
                updateShowWifiSignalSpacer(copy);
                if (!Objects.equals(copy, this.mWifiIconState)) {
                    updateWifiIconWithState(copy);
                    this.mWifiIconState = copy;
                }
            }
        }
    }

    private CallIndicatorIconState getNoCallingState(int i) {
        Iterator<CallIndicatorIconState> it = this.mCallIndicatorStates.iterator();
        while (it.hasNext()) {
            CallIndicatorIconState next = it.next();
            if (next.subId == i) {
                return next;
            }
        }
        Log.e("StatusBarSignalPolicy", "Unexpected subscription " + i);
        return null;
    }

    private MobileIconState getState(int i) {
        Iterator<MobileIconState> it = this.mMobileStates.iterator();
        while (it.hasNext()) {
            MobileIconState next = it.next();
            if (next.subId == i) {
                return next;
            }
        }
        Log.e("StatusBarSignalPolicy", "Unexpected subscription " + i);
        return null;
    }

    private MobileIconState getFirstMobileState() {
        if (this.mMobileStates.size() > 0) {
            return this.mMobileStates.get(0);
        }
        return null;
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.SignalCallback
    public void setSubs(List<SubscriptionInfo> list) {
        boolean z;
        if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("setSubs: ");
            sb.append(list == null ? "" : list.toString());
            Log.d("StatusBarSignalPolicy", sb.toString());
        }
        if (!hasCorrectSubs(list)) {
            this.mIconController.removeAllIconsForSlot(this.mSlotMobile);
            this.mIconController.removeAllIconsForSlot(this.mSlotNoCalling);
            this.mIconController.removeAllIconsForSlot(this.mSlotCallStrength);
            this.mMobileStates.clear();
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(this.mCallIndicatorStates);
            this.mCallIndicatorStates.clear();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                this.mMobileStates.add(new MobileIconState(list.get(i).getSubscriptionId()));
                Iterator it = arrayList.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        z = true;
                        break;
                    }
                    CallIndicatorIconState callIndicatorIconState = (CallIndicatorIconState) it.next();
                    if (callIndicatorIconState.subId == list.get(i).getSubscriptionId()) {
                        this.mCallIndicatorStates.add(callIndicatorIconState);
                        z = false;
                        break;
                    }
                }
                if (z) {
                    this.mCallIndicatorStates.add(new CallIndicatorIconState(list.get(i).getSubscriptionId()));
                }
            }
        }
    }

    private boolean hasCorrectSubs(List<SubscriptionInfo> list) {
        int size = list.size();
        if (size != this.mMobileStates.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (this.mMobileStates.get(i).subId != list.get(i).getSubscriptionId()) {
                return false;
            }
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.SignalCallback
    public void setConnectivityStatus(boolean z, boolean z2, boolean z3) {
        if (this.mFeatureFlags.isCombinedStatusBarSignalIconsEnabled()) {
            if (DEBUG) {
                Log.d("StatusBarSignalPolicy", "setConnectivityStatus: noDefaultNetwork = " + z + ",noValidatedNetwork = " + z2 + ",noNetworksAvailable = " + z3);
            }
            WifiIconState copy = this.mWifiIconState.copy();
            copy.noDefaultNetwork = z;
            copy.noValidatedNetwork = z2;
            copy.noNetworksAvailable = z3;
            copy.slot = this.mSlotWifi;
            boolean z4 = this.mIsAirplaneMode;
            copy.airplaneSpacerVisible = z4;
            if (z && z3 && !z4) {
                copy.visible = true;
                copy.resId = R$drawable.ic_qs_no_internet_unavailable;
            } else if (!z || z3 || (z4 && (!z4 || !this.mIsWifiEnabled))) {
                copy.visible = false;
                copy.resId = 0;
            } else {
                copy.visible = true;
                copy.resId = R$drawable.ic_qs_no_internet_available;
            }
            updateWifiIconWithState(copy);
            this.mWifiIconState = copy;
        }
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.SignalCallback
    public void setEthernetIndicators(NetworkController.IconState iconState) {
        if (iconState.visible) {
            boolean z = this.mHideEthernet;
        }
        int i = iconState.icon;
        String str = iconState.contentDescription;
        if (i > 0) {
            this.mIconController.setIcon(this.mSlotEthernet, i, str);
            this.mIconController.setIconVisibility(this.mSlotEthernet, true);
            return;
        }
        this.mIconController.setIconVisibility(this.mSlotEthernet, false);
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.SignalCallback
    public void setIsAirplaneMode(NetworkController.IconState iconState) {
        String str;
        if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("setIsAirplaneMode: icon = ");
            if (iconState == null) {
                str = "";
            } else {
                str = iconState.toString();
            }
            sb.append(str);
            Log.d("StatusBarSignalPolicy", sb.toString());
        }
        boolean z = iconState.visible && !this.mHideAirplane;
        this.mIsAirplaneMode = z;
        int i = iconState.icon;
        String str2 = iconState.contentDescription;
        if (!z || i <= 0) {
            this.mIconController.setIconVisibility(this.mSlotAirplane, false);
            return;
        }
        this.mIconController.setIcon(this.mSlotAirplane, i, str2);
        this.mIconController.setIconVisibility(this.mSlotAirplane, true);
    }

    /* loaded from: classes.dex */
    public static class CallIndicatorIconState {
        public String callStrengthDescription;
        public int callStrengthResId;
        public boolean isNoCalling;
        public String noCallingDescription;
        public int noCallingResId;
        public int subId;

        private CallIndicatorIconState(int i) {
            this.subId = i;
            this.noCallingResId = R$drawable.ic_qs_no_calling_sms;
            this.callStrengthResId = TelephonyIcons.MOBILE_CALL_STRENGTH_ICONS[0];
        }

        public boolean equals(Object obj) {
            if (obj == null || CallIndicatorIconState.class != obj.getClass()) {
                return false;
            }
            CallIndicatorIconState callIndicatorIconState = (CallIndicatorIconState) obj;
            if (this.isNoCalling == callIndicatorIconState.isNoCalling && this.noCallingResId == callIndicatorIconState.noCallingResId && this.callStrengthResId == callIndicatorIconState.callStrengthResId && this.subId == callIndicatorIconState.subId && this.noCallingDescription == callIndicatorIconState.noCallingDescription && this.callStrengthDescription == callIndicatorIconState.callStrengthDescription) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(Boolean.valueOf(this.isNoCalling), Integer.valueOf(this.noCallingResId), Integer.valueOf(this.callStrengthResId), Integer.valueOf(this.subId), this.noCallingDescription, this.callStrengthDescription);
        }

        private void copyTo(CallIndicatorIconState callIndicatorIconState) {
            callIndicatorIconState.isNoCalling = this.isNoCalling;
            callIndicatorIconState.noCallingResId = this.noCallingResId;
            callIndicatorIconState.callStrengthResId = this.callStrengthResId;
            callIndicatorIconState.subId = this.subId;
            callIndicatorIconState.noCallingDescription = this.noCallingDescription;
            callIndicatorIconState.callStrengthDescription = this.callStrengthDescription;
        }

        /* access modifiers changed from: private */
        public static List<CallIndicatorIconState> copyStates(List<CallIndicatorIconState> list) {
            ArrayList arrayList = new ArrayList();
            for (CallIndicatorIconState callIndicatorIconState : list) {
                CallIndicatorIconState callIndicatorIconState2 = new CallIndicatorIconState(callIndicatorIconState.subId);
                callIndicatorIconState.copyTo(callIndicatorIconState2);
                arrayList.add(callIndicatorIconState2);
            }
            return arrayList;
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static abstract class SignalIconState {
        public boolean activityIn;
        public boolean activityOut;
        public String contentDescription;
        public String slot;
        public boolean visible;

        private SignalIconState() {
        }

        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            SignalIconState signalIconState = (SignalIconState) obj;
            if (this.visible == signalIconState.visible && this.activityOut == signalIconState.activityOut && this.activityIn == signalIconState.activityIn && Objects.equals(this.contentDescription, signalIconState.contentDescription) && Objects.equals(this.slot, signalIconState.slot)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(Boolean.valueOf(this.visible), Boolean.valueOf(this.activityOut), this.slot);
        }

        protected void copyTo(SignalIconState signalIconState) {
            signalIconState.visible = this.visible;
            signalIconState.activityIn = this.activityIn;
            signalIconState.activityOut = this.activityOut;
            signalIconState.slot = this.slot;
            signalIconState.contentDescription = this.contentDescription;
        }
    }

    /* loaded from: classes.dex */
    public static class WifiIconState extends SignalIconState {
        public boolean airplaneSpacerVisible;
        public boolean noDefaultNetwork;
        public boolean noNetworksAvailable;
        public boolean noValidatedNetwork;
        public int resId;
        public boolean signalSpacerVisible;

        public WifiIconState() {
            super();
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarSignalPolicy.SignalIconState
        public boolean equals(Object obj) {
            if (obj == null || WifiIconState.class != obj.getClass() || !super.equals(obj)) {
                return false;
            }
            WifiIconState wifiIconState = (WifiIconState) obj;
            if (this.resId == wifiIconState.resId && this.airplaneSpacerVisible == wifiIconState.airplaneSpacerVisible && this.signalSpacerVisible == wifiIconState.signalSpacerVisible && this.noDefaultNetwork == wifiIconState.noDefaultNetwork && this.noValidatedNetwork == wifiIconState.noValidatedNetwork && this.noNetworksAvailable == wifiIconState.noNetworksAvailable) {
                return true;
            }
            return false;
        }

        public void copyTo(WifiIconState wifiIconState) {
            super.copyTo((SignalIconState) wifiIconState);
            wifiIconState.resId = this.resId;
            wifiIconState.airplaneSpacerVisible = this.airplaneSpacerVisible;
            wifiIconState.signalSpacerVisible = this.signalSpacerVisible;
            wifiIconState.noDefaultNetwork = this.noDefaultNetwork;
            wifiIconState.noValidatedNetwork = this.noValidatedNetwork;
            wifiIconState.noNetworksAvailable = this.noNetworksAvailable;
        }

        public WifiIconState copy() {
            WifiIconState wifiIconState = new WifiIconState();
            copyTo(wifiIconState);
            return wifiIconState;
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarSignalPolicy.SignalIconState
        public int hashCode() {
            return Objects.hash(Integer.valueOf(super.hashCode()), Integer.valueOf(this.resId), Boolean.valueOf(this.airplaneSpacerVisible), Boolean.valueOf(this.signalSpacerVisible), Boolean.valueOf(this.noDefaultNetwork), Boolean.valueOf(this.noValidatedNetwork), Boolean.valueOf(this.noNetworksAvailable));
        }

        public String toString() {
            return "WifiIconState(resId=" + this.resId + ", visible=" + this.visible + ")";
        }
    }

    /* loaded from: classes.dex */
    public static class MobileIconState extends SignalIconState {
        public boolean needsLeadingPadding;
        public boolean roaming;
        public boolean showTriangle;
        public int strengthId;
        public int subId;
        public CharSequence typeContentDescription;
        public int typeId;

        private MobileIconState(int i) {
            super();
            this.subId = i;
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarSignalPolicy.SignalIconState
        public boolean equals(Object obj) {
            if (obj == null || MobileIconState.class != obj.getClass() || !super.equals(obj)) {
                return false;
            }
            MobileIconState mobileIconState = (MobileIconState) obj;
            if (this.subId == mobileIconState.subId && this.strengthId == mobileIconState.strengthId && this.typeId == mobileIconState.typeId && this.showTriangle == mobileIconState.showTriangle && this.roaming == mobileIconState.roaming && this.needsLeadingPadding == mobileIconState.needsLeadingPadding && Objects.equals(this.typeContentDescription, mobileIconState.typeContentDescription)) {
                return true;
            }
            return false;
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarSignalPolicy.SignalIconState
        public int hashCode() {
            return Objects.hash(Integer.valueOf(super.hashCode()), Integer.valueOf(this.subId), Integer.valueOf(this.strengthId), Integer.valueOf(this.typeId), Boolean.valueOf(this.showTriangle), Boolean.valueOf(this.roaming), Boolean.valueOf(this.needsLeadingPadding), this.typeContentDescription);
        }

        public MobileIconState copy() {
            MobileIconState mobileIconState = new MobileIconState(this.subId);
            copyTo(mobileIconState);
            return mobileIconState;
        }

        public void copyTo(MobileIconState mobileIconState) {
            super.copyTo((SignalIconState) mobileIconState);
            mobileIconState.subId = this.subId;
            mobileIconState.strengthId = this.strengthId;
            mobileIconState.typeId = this.typeId;
            mobileIconState.showTriangle = this.showTriangle;
            mobileIconState.roaming = this.roaming;
            mobileIconState.needsLeadingPadding = this.needsLeadingPadding;
            mobileIconState.typeContentDescription = this.typeContentDescription;
        }

        /* access modifiers changed from: private */
        public static List<MobileIconState> copyStates(List<MobileIconState> list) {
            ArrayList arrayList = new ArrayList();
            for (MobileIconState mobileIconState : list) {
                MobileIconState mobileIconState2 = new MobileIconState(mobileIconState.subId);
                mobileIconState.copyTo(mobileIconState2);
                arrayList.add(mobileIconState2);
            }
            return arrayList;
        }

        public String toString() {
            return "MobileIconState(subId=" + this.subId + ", strengthId=" + this.strengthId + ", showTriangle=" + this.showTriangle + ", roaming=" + this.roaming + ", typeId=" + this.typeId + ", visible=" + this.visible + ")";
        }
    }
}
