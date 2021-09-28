package com.android.wifitrackerlib;

import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.RouteInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkScoreCache;
import android.os.Handler;
import androidx.core.util.Preconditions;
import com.android.net.module.util.NetUtils;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;
/* loaded from: classes2.dex */
public class WifiEntry implements Comparable<WifiEntry> {
    protected final Handler mCallbackHandler;
    protected ConnectCallback mConnectCallback;
    protected ConnectedInfo mConnectedInfo;
    protected DisconnectCallback mDisconnectCallback;
    final boolean mForSavedNetworksPage;
    protected boolean mIsDefaultNetwork;
    protected boolean mIsLowQuality;
    private boolean mIsValidated;
    private WifiEntryCallback mListener;
    protected NetworkCapabilities mNetworkCapabilities;
    protected NetworkInfo mNetworkInfo;
    protected WifiNetworkScoreCache mScoreCache;
    protected WifiInfo mWifiInfo;
    protected final WifiManager mWifiManager;
    protected int mLevel = -1;
    protected int mSpeed = 0;
    protected boolean mCalledConnect = false;
    protected boolean mCalledDisconnect = false;
    private Optional<?> mManageSubscriptionAction = Optional.empty();

    /* loaded from: classes2.dex */
    public interface ConnectCallback {
        void onConnectResult(int i);
    }

    /* loaded from: classes2.dex */
    public interface DisconnectCallback {
        void onDisconnectResult(int i);
    }

    /* loaded from: classes2.dex */
    public interface SignInCallback {
    }

    /* loaded from: classes2.dex */
    public interface WifiEntryCallback {
        void onUpdated();
    }

    public boolean canSetAutoJoinEnabled() {
        return false;
    }

    public boolean canSetMeteredChoice() {
        return false;
    }

    public boolean canSignIn() {
        return false;
    }

    public void connect(ConnectCallback connectCallback) {
    }

    protected boolean connectionInfoMatches(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        return false;
    }

    public String getHelpUriString() {
        return null;
    }

    public String getKey() {
        return "";
    }

    public int getMeteredChoice() {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public String getNetworkSelectionDescription() {
        return "";
    }

    /* access modifiers changed from: protected */
    public String getScanResultDescription() {
        return "";
    }

    public String getSsid() {
        return null;
    }

    public String getSummary(boolean z) {
        return "";
    }

    public String getTitle() {
        return "";
    }

    public WifiConfiguration getWifiConfiguration() {
        return null;
    }

    public boolean isAutoJoinEnabled() {
        return false;
    }

    public boolean isMetered() {
        return false;
    }

    public boolean isSaved() {
        return false;
    }

    public boolean isSubscription() {
        return false;
    }

    public boolean isSuggestion() {
        return false;
    }

    protected void updateSecurityTypes() {
    }

    public WifiEntry(Handler handler, WifiManager wifiManager, WifiNetworkScoreCache wifiNetworkScoreCache, boolean z) throws IllegalArgumentException {
        Preconditions.checkNotNull(handler, "Cannot construct with null handler!");
        Preconditions.checkNotNull(wifiManager, "Cannot construct with null WifiManager!");
        this.mCallbackHandler = handler;
        this.mForSavedNetworksPage = z;
        this.mWifiManager = wifiManager;
        this.mScoreCache = wifiNetworkScoreCache;
    }

    public synchronized int getConnectedState() {
        NetworkInfo networkInfo = this.mNetworkInfo;
        if (networkInfo == null) {
            return 0;
        }
        switch (AnonymousClass1.$SwitchMap$android$net$NetworkInfo$DetailedState[networkInfo.getDetailedState().ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return 1;
            case 7:
                return 2;
            default:
                return 0;
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: com.android.wifitrackerlib.WifiEntry$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$net$NetworkInfo$DetailedState;

        static {
            int[] iArr = new int[NetworkInfo.DetailedState.values().length];
            $SwitchMap$android$net$NetworkInfo$DetailedState = iArr;
            try {
                iArr[NetworkInfo.DetailedState.SCANNING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$android$net$NetworkInfo$DetailedState[NetworkInfo.DetailedState.CONNECTING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$android$net$NetworkInfo$DetailedState[NetworkInfo.DetailedState.AUTHENTICATING.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$android$net$NetworkInfo$DetailedState[NetworkInfo.DetailedState.OBTAINING_IPADDR.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$android$net$NetworkInfo$DetailedState[NetworkInfo.DetailedState.VERIFYING_POOR_LINK.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$android$net$NetworkInfo$DetailedState[NetworkInfo.DetailedState.CAPTIVE_PORTAL_CHECK.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$android$net$NetworkInfo$DetailedState[NetworkInfo.DetailedState.CONNECTED.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    public String getSummary() {
        return getSummary(true);
    }

    public int getLevel() {
        return this.mLevel;
    }

    public boolean shouldShowXLevelIcon() {
        return getConnectedState() != 0 && (!this.mIsValidated || !this.mIsDefaultNetwork) && !canSignIn();
    }

    public int getSpeed() {
        return this.mSpeed;
    }

    public int getSecurity() {
        switch (Utils.getSingleSecurityTypeFromMultipleSecurityTypes(getSecurityTypes())) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 5;
            case 5:
                return 6;
            case 6:
                return 4;
            case 7:
            case 8:
            case 10:
            default:
                return 0;
            case 9:
                return 7;
            case 11:
            case 12:
                return 3;
        }
    }

    public List<Integer> getSecurityTypes() {
        return Collections.emptyList();
    }

    public synchronized ConnectedInfo getConnectedInfo() {
        if (getConnectedState() != 2) {
            return null;
        }
        return new ConnectedInfo(this.mConnectedInfo);
    }

    /* loaded from: classes2.dex */
    public static class ConnectedInfo {
        public List<String> dnsServers;
        public int frequencyMhz;
        public String gateway;
        public String ipAddress;
        public List<String> ipv6Addresses;
        public int linkSpeedMbps;
        public String subnetMask;
        public int wifiStandard;

        public ConnectedInfo() {
            this.dnsServers = new ArrayList();
            this.ipv6Addresses = new ArrayList();
            this.wifiStandard = 0;
        }

        public ConnectedInfo(ConnectedInfo connectedInfo) {
            this.dnsServers = new ArrayList();
            this.ipv6Addresses = new ArrayList();
            this.wifiStandard = 0;
            this.frequencyMhz = connectedInfo.frequencyMhz;
            this.dnsServers = new ArrayList(this.dnsServers);
            this.linkSpeedMbps = connectedInfo.linkSpeedMbps;
            this.ipAddress = connectedInfo.ipAddress;
            this.ipv6Addresses = new ArrayList(connectedInfo.ipv6Addresses);
            this.gateway = connectedInfo.gateway;
            this.subnetMask = connectedInfo.subnetMask;
            this.wifiStandard = connectedInfo.wifiStandard;
        }
    }

    /* access modifiers changed from: package-private */
    public String getNetworkCapabilityDescription() {
        StringBuilder sb = new StringBuilder();
        if (getConnectedState() == 2) {
            sb.append("isValidated:");
            sb.append(this.mIsValidated);
            sb.append(", isDefaultNetwork:");
            sb.append(this.mIsDefaultNetwork);
            sb.append(", isLowQuality:");
            sb.append(this.mIsLowQuality);
        }
        return sb.toString();
    }

    public synchronized void setListener(WifiEntryCallback wifiEntryCallback) {
        this.mListener = wifiEntryCallback;
    }

    /* access modifiers changed from: protected */
    public void notifyOnUpdated() {
        if (this.mListener != null) {
            this.mCallbackHandler.post(new Runnable() { // from class: com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    WifiEntry.this.lambda$notifyOnUpdated$0();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyOnUpdated$0() {
        WifiEntryCallback wifiEntryCallback = this.mListener;
        if (wifiEntryCallback != null) {
            wifiEntryCallback.onUpdated();
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void updateConnectionInfo(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        if (!(wifiInfo == null || networkInfo == null)) {
            if (connectionInfoMatches(wifiInfo, networkInfo)) {
                this.mWifiInfo = wifiInfo;
                this.mNetworkInfo = networkInfo;
                int rssi = wifiInfo.getRssi();
                if (rssi != -127) {
                    this.mLevel = this.mWifiManager.calculateSignalLevel(rssi);
                    this.mSpeed = Utils.getSpeedFromWifiInfo(this.mScoreCache, wifiInfo);
                }
                if (getConnectedState() == 2) {
                    if (this.mCalledConnect) {
                        this.mCalledConnect = false;
                        this.mCallbackHandler.post(new Runnable() { // from class: com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                WifiEntry.this.lambda$updateConnectionInfo$1();
                            }
                        });
                    }
                    if (this.mConnectedInfo == null) {
                        this.mConnectedInfo = new ConnectedInfo();
                    }
                    this.mConnectedInfo.frequencyMhz = wifiInfo.getFrequency();
                    this.mConnectedInfo.linkSpeedMbps = wifiInfo.getLinkSpeed();
                    this.mConnectedInfo.wifiStandard = wifiInfo.getWifiStandard();
                }
                updateSecurityTypes();
                notifyOnUpdated();
            }
        }
        this.mWifiInfo = null;
        this.mNetworkInfo = null;
        this.mNetworkCapabilities = null;
        this.mConnectedInfo = null;
        this.mIsValidated = false;
        this.mIsDefaultNetwork = false;
        this.mIsLowQuality = false;
        if (this.mCalledDisconnect) {
            this.mCalledDisconnect = false;
            this.mCallbackHandler.post(new Runnable() { // from class: com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    WifiEntry.this.lambda$updateConnectionInfo$2();
                }
            });
        }
        updateSecurityTypes();
        notifyOnUpdated();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateConnectionInfo$1() {
        ConnectCallback connectCallback = this.mConnectCallback;
        if (connectCallback != null) {
            connectCallback.onConnectResult(0);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateConnectionInfo$2() {
        DisconnectCallback disconnectCallback = this.mDisconnectCallback;
        if (disconnectCallback != null) {
            disconnectCallback.onDisconnectResult(0);
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void updateLinkProperties(LinkProperties linkProperties) {
        if (linkProperties != null) {
            if (getConnectedState() == 2) {
                if (this.mConnectedInfo == null) {
                    this.mConnectedInfo = new ConnectedInfo();
                }
                ArrayList arrayList = new ArrayList();
                for (LinkAddress linkAddress : linkProperties.getLinkAddresses()) {
                    if (linkAddress.getAddress() instanceof Inet4Address) {
                        this.mConnectedInfo.ipAddress = linkAddress.getAddress().getHostAddress();
                        try {
                            this.mConnectedInfo.subnetMask = NetUtils.getNetworkPart(InetAddress.getByAddress(new byte[]{-1, -1, -1, -1}), linkAddress.getPrefixLength()).getHostAddress();
                        } catch (UnknownHostException unused) {
                        }
                    } else if (linkAddress.getAddress() instanceof Inet6Address) {
                        arrayList.add(linkAddress.getAddress().getHostAddress());
                    }
                }
                this.mConnectedInfo.ipv6Addresses = arrayList;
                Iterator<RouteInfo> it = linkProperties.getRoutes().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    RouteInfo next = it.next();
                    if (next.isDefaultRoute() && (next.getDestination().getAddress() instanceof Inet4Address) && next.hasGateway()) {
                        this.mConnectedInfo.gateway = next.getGateway().getHostAddress();
                        break;
                    }
                }
                this.mConnectedInfo.dnsServers = (List) linkProperties.getDnsServers().stream().map(WifiEntry$$ExternalSyntheticLambda3.INSTANCE).collect(Collectors.toList());
                notifyOnUpdated();
                return;
            }
        }
        this.mConnectedInfo = null;
        notifyOnUpdated();
    }

    /* access modifiers changed from: package-private */
    public synchronized void setIsDefaultNetwork(boolean z) {
        this.mIsDefaultNetwork = z;
        notifyOnUpdated();
    }

    /* access modifiers changed from: package-private */
    public synchronized void setIsLowQuality(boolean z) {
        this.mIsLowQuality = z;
    }

    /* access modifiers changed from: package-private */
    public synchronized void updateNetworkCapabilities(NetworkCapabilities networkCapabilities) {
        this.mNetworkCapabilities = networkCapabilities;
        if (this.mConnectedInfo != null) {
            this.mIsValidated = networkCapabilities != null && networkCapabilities.hasCapability(16);
            notifyOnUpdated();
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized String getWifiInfoDescription() {
        StringJoiner stringJoiner;
        stringJoiner = new StringJoiner(" ");
        if (getConnectedState() == 2 && this.mWifiInfo != null) {
            stringJoiner.add("f = " + this.mWifiInfo.getFrequency());
            String bssid = this.mWifiInfo.getBSSID();
            if (bssid != null) {
                stringJoiner.add(bssid);
            }
            stringJoiner.add("standard = " + this.mWifiInfo.getWifiStandard());
            stringJoiner.add("rssi = " + this.mWifiInfo.getRssi());
            stringJoiner.add("score = " + this.mWifiInfo.getScore());
            stringJoiner.add(String.format(" tx=%.1f,", Double.valueOf(this.mWifiInfo.getSuccessfulTxPacketsPerSecond())));
            stringJoiner.add(String.format("%.1f,", Double.valueOf(this.mWifiInfo.getRetriedTxPacketsPerSecond())));
            stringJoiner.add(String.format("%.1f ", Double.valueOf(this.mWifiInfo.getLostTxPacketsPerSecond())));
            stringJoiner.add(String.format("rx=%.1f", Double.valueOf(this.mWifiInfo.getSuccessfulRxPacketsPerSecond())));
        }
        return stringJoiner.toString();
    }

    /* loaded from: classes2.dex */
    protected class ConnectActionListener implements WifiManager.ActionListener {
        /* access modifiers changed from: protected */
        public ConnectActionListener() {
        }

        public void onSuccess() {
            WifiEntry wifiEntry;
            synchronized (WifiEntry.this) {
                wifiEntry = WifiEntry.this;
                wifiEntry.mCalledConnect = true;
            }
            wifiEntry.mCallbackHandler.postDelayed(new WifiEntry$ConnectActionListener$$ExternalSyntheticLambda0(this), 10000);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onSuccess$0() {
            WifiEntry wifiEntry = WifiEntry.this;
            ConnectCallback connectCallback = wifiEntry.mConnectCallback;
            if (connectCallback != null && wifiEntry.mCalledConnect && wifiEntry.getConnectedState() == 0) {
                connectCallback.onConnectResult(2);
                WifiEntry.this.mCalledConnect = false;
            }
        }

        public void onFailure(int i) {
            WifiEntry.this.mCallbackHandler.post(new WifiEntry$ConnectActionListener$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onFailure$1() {
            ConnectCallback connectCallback = WifiEntry.this.mConnectCallback;
            if (connectCallback != null) {
                connectCallback.onConnectResult(2);
            }
        }
    }

    public int compareTo(WifiEntry wifiEntry) {
        if (getLevel() != -1 && wifiEntry.getLevel() == -1) {
            return -1;
        }
        if (getLevel() == -1 && wifiEntry.getLevel() != -1) {
            return 1;
        }
        if (isSubscription() && !wifiEntry.isSubscription()) {
            return -1;
        }
        if (!isSubscription() && wifiEntry.isSubscription()) {
            return 1;
        }
        if (isSaved() && !wifiEntry.isSaved()) {
            return -1;
        }
        if (!isSaved() && wifiEntry.isSaved()) {
            return 1;
        }
        if (isSuggestion() && !wifiEntry.isSuggestion()) {
            return -1;
        }
        if (!isSuggestion() && wifiEntry.isSuggestion()) {
            return 1;
        }
        if (getLevel() > wifiEntry.getLevel()) {
            return -1;
        }
        if (getLevel() < wifiEntry.getLevel()) {
            return 1;
        }
        return getTitle().compareTo(wifiEntry.getTitle());
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        if (!(obj instanceof WifiEntry)) {
            return false;
        }
        return getKey().equals(((WifiEntry) obj).getKey());
    }

    @Override // java.lang.Object
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getKey());
        sb.append(",title:");
        sb.append(getTitle());
        sb.append(",summary:");
        sb.append(getSummary());
        sb.append(",isSaved:");
        sb.append(isSaved());
        sb.append(",isSubscription:");
        sb.append(isSubscription());
        sb.append(",isSuggestion:");
        sb.append(isSuggestion());
        sb.append(",level:");
        sb.append(getLevel());
        sb.append(shouldShowXLevelIcon() ? "X" : "");
        sb.append(",security:");
        sb.append(getSecurityTypes());
        sb.append(",connected:");
        sb.append(getConnectedState() == 2 ? "true" : "false");
        sb.append(",connectedInfo:");
        sb.append(getConnectedInfo());
        sb.append(",isValidated:");
        sb.append(this.mIsValidated);
        sb.append(",isDefaultNetwork:");
        sb.append(this.mIsDefaultNetwork);
        return sb.toString();
    }
}
