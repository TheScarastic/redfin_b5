package com.android.settingslib.wifi;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkKey;
import android.net.NetworkRequest;
import android.net.NetworkScoreManager;
import android.net.ScoredNetwork;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkScoreCache;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import com.android.settingslib.R$string;
import com.android.settingslib.Utils;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public class WifiStatusTracker {
    private static final SimpleDateFormat SSDF = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
    public boolean connected;
    public boolean enabled;
    public boolean isCaptivePortal;
    public boolean isCarrierMerged;
    public boolean isDefaultNetwork;
    public int level;
    private final WifiNetworkScoreCache.CacheListener mCacheListener;
    private final Runnable mCallback;
    private final ConnectivityManager mConnectivityManager;
    private final Context mContext;
    private final Handler mHandler;
    private int mHistoryIndex;
    private final NetworkScoreManager mNetworkScoreManager;
    private WifiInfo mWifiInfo;
    private final WifiManager mWifiManager;
    private final WifiNetworkScoreCache mWifiNetworkScoreCache;
    public int rssi;
    public String ssid;
    public int state;
    public String statusLabel;
    public int subId;
    private final Set<Integer> mNetworks = new HashSet();
    private final String[] mHistory = new String[32];
    private final NetworkRequest mNetworkRequest = new NetworkRequest.Builder().clearCapabilities().addCapability(15).addTransportType(1).addTransportType(0).build();
    private final ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback(1) { // from class: com.android.settingslib.wifi.WifiStatusTracker.2
        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            WifiInfo wifiInfo;
            boolean z = false;
            boolean z2 = true;
            if (networkCapabilities.hasTransport(0)) {
                wifiInfo = Utils.tryGetWifiInfoForVcn(networkCapabilities);
                if (wifiInfo == null) {
                    z2 = false;
                }
                z2 = false;
                z = z2;
            } else if (networkCapabilities.hasTransport(1)) {
                wifiInfo = (WifiInfo) networkCapabilities.getTransportInfo();
            } else {
                wifiInfo = null;
                z2 = false;
            }
            if (z || z2) {
                WifiStatusTracker.this.recordLastWifiNetwork(WifiStatusTracker.SSDF.format(Long.valueOf(System.currentTimeMillis())) + ",onCapabilitiesChanged: network=" + network + ",networkCapabilities=" + networkCapabilities);
            }
            if (wifiInfo != null && wifiInfo.isPrimary()) {
                if (!WifiStatusTracker.this.mNetworks.contains(Integer.valueOf(network.getNetId()))) {
                    WifiStatusTracker.this.mNetworks.add(Integer.valueOf(network.getNetId()));
                }
                WifiStatusTracker.this.updateWifiInfo(wifiInfo);
                WifiStatusTracker.this.updateStatusLabel();
                WifiStatusTracker.this.mCallback.run();
            } else if (WifiStatusTracker.this.mNetworks.contains(Integer.valueOf(network.getNetId()))) {
                WifiStatusTracker.this.mNetworks.remove(Integer.valueOf(network.getNetId()));
            }
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            WifiStatusTracker.this.recordLastWifiNetwork(WifiStatusTracker.SSDF.format(Long.valueOf(System.currentTimeMillis())) + ",onLost: network=" + network);
            if (WifiStatusTracker.this.mNetworks.contains(Integer.valueOf(network.getNetId()))) {
                WifiStatusTracker.this.mNetworks.remove(Integer.valueOf(network.getNetId()));
                WifiStatusTracker.this.updateWifiInfo(null);
                WifiStatusTracker.this.updateStatusLabel();
                WifiStatusTracker.this.mCallback.run();
            }
        }
    };
    private final ConnectivityManager.NetworkCallback mDefaultNetworkCallback = new ConnectivityManager.NetworkCallback(1) { // from class: com.android.settingslib.wifi.WifiStatusTracker.3
        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            WifiStatusTracker.this.mDefaultNetwork = network;
            WifiStatusTracker.this.mDefaultNetworkCapabilities = networkCapabilities;
            WifiStatusTracker.this.updateStatusLabel();
            WifiStatusTracker.this.mCallback.run();
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            WifiStatusTracker.this.mDefaultNetwork = null;
            WifiStatusTracker.this.mDefaultNetworkCapabilities = null;
            WifiStatusTracker.this.updateStatusLabel();
            WifiStatusTracker.this.mCallback.run();
        }
    };
    private Network mDefaultNetwork = null;
    private NetworkCapabilities mDefaultNetworkCapabilities = null;
    private final boolean mSupportMergedUi = false;

    public WifiStatusTracker(Context context, WifiManager wifiManager, NetworkScoreManager networkScoreManager, ConnectivityManager connectivityManager, Runnable runnable) {
        Handler handler = new Handler(Looper.getMainLooper());
        this.mHandler = handler;
        this.mCacheListener = new WifiNetworkScoreCache.CacheListener(handler) { // from class: com.android.settingslib.wifi.WifiStatusTracker.1
            public void networkCacheUpdated(List<ScoredNetwork> list) {
                WifiStatusTracker.this.updateStatusLabel();
                WifiStatusTracker.this.mCallback.run();
            }
        };
        this.mContext = context;
        this.mWifiManager = wifiManager;
        this.mWifiNetworkScoreCache = new WifiNetworkScoreCache(context);
        this.mNetworkScoreManager = networkScoreManager;
        this.mConnectivityManager = connectivityManager;
        this.mCallback = runnable;
    }

    public void setListening(boolean z) {
        if (z) {
            this.mNetworkScoreManager.registerNetworkScoreCache(1, this.mWifiNetworkScoreCache, 1);
            this.mWifiNetworkScoreCache.registerListener(this.mCacheListener);
            this.mConnectivityManager.registerNetworkCallback(this.mNetworkRequest, this.mNetworkCallback, this.mHandler);
            this.mConnectivityManager.registerDefaultNetworkCallback(this.mDefaultNetworkCallback, this.mHandler);
            return;
        }
        this.mNetworkScoreManager.unregisterNetworkScoreCache(1, this.mWifiNetworkScoreCache);
        this.mWifiNetworkScoreCache.unregisterListener();
        this.mConnectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
        this.mConnectivityManager.unregisterNetworkCallback(this.mDefaultNetworkCallback);
    }

    public void fetchInitialState() {
        if (this.mWifiManager != null) {
            updateWifiState();
            boolean z = true;
            NetworkInfo networkInfo = this.mConnectivityManager.getNetworkInfo(1);
            if (networkInfo == null || !networkInfo.isConnected()) {
                z = false;
            }
            this.connected = z;
            this.mWifiInfo = null;
            this.ssid = null;
            if (z) {
                WifiInfo connectionInfo = this.mWifiManager.getConnectionInfo();
                this.mWifiInfo = connectionInfo;
                if (connectionInfo != null) {
                    if (connectionInfo.isPasspointAp() || this.mWifiInfo.isOsuAp()) {
                        this.ssid = this.mWifiInfo.getPasspointProviderFriendlyName();
                    } else {
                        this.ssid = getValidSsid(this.mWifiInfo);
                    }
                    if (this.mSupportMergedUi) {
                        this.isCarrierMerged = this.mWifiInfo.isCarrierMerged();
                        this.subId = this.mWifiInfo.getSubscriptionId();
                    }
                    updateRssi(this.mWifiInfo.getRssi());
                    maybeRequestNetworkScore();
                }
            }
            updateStatusLabel();
        }
    }

    public void handleBroadcast(Intent intent) {
        if (this.mWifiManager != null && intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED")) {
            updateWifiState();
        }
    }

    /* access modifiers changed from: private */
    public void updateWifiInfo(WifiInfo wifiInfo) {
        updateWifiState();
        this.connected = wifiInfo != null;
        this.mWifiInfo = wifiInfo;
        this.ssid = null;
        if (wifiInfo != null) {
            if (wifiInfo.isPasspointAp() || this.mWifiInfo.isOsuAp()) {
                this.ssid = this.mWifiInfo.getPasspointProviderFriendlyName();
            } else {
                this.ssid = getValidSsid(this.mWifiInfo);
            }
            if (this.mSupportMergedUi) {
                this.isCarrierMerged = this.mWifiInfo.isCarrierMerged();
                this.subId = this.mWifiInfo.getSubscriptionId();
            }
            updateRssi(this.mWifiInfo.getRssi());
            maybeRequestNetworkScore();
        }
    }

    private void updateWifiState() {
        int wifiState = this.mWifiManager.getWifiState();
        this.state = wifiState;
        this.enabled = wifiState == 3;
        this.isCarrierMerged = false;
        this.subId = 0;
    }

    private void updateRssi(int i) {
        this.rssi = i;
        this.level = this.mWifiManager.calculateSignalLevel(i);
    }

    private void maybeRequestNetworkScore() {
        NetworkKey createFromWifiInfo = NetworkKey.createFromWifiInfo(this.mWifiInfo);
        if (this.mWifiNetworkScoreCache.getScoredNetwork(createFromWifiInfo) == null) {
            this.mNetworkScoreManager.requestScores(new NetworkKey[]{createFromWifiInfo});
        }
    }

    /* access modifiers changed from: private */
    public void updateStatusLabel() {
        NetworkCapabilities networkCapabilities;
        String str;
        NetworkCapabilities networkCapabilities2;
        if (this.mWifiManager != null) {
            this.isDefaultNetwork = false;
            NetworkCapabilities networkCapabilities3 = this.mDefaultNetworkCapabilities;
            if (networkCapabilities3 != null) {
                boolean hasTransport = networkCapabilities3.hasTransport(1);
                boolean z = this.mDefaultNetworkCapabilities.hasTransport(0) && Utils.tryGetWifiInfoForVcn(this.mDefaultNetworkCapabilities) != null;
                if (hasTransport || z) {
                    this.isDefaultNetwork = true;
                }
            }
            if (this.isDefaultNetwork) {
                networkCapabilities = this.mDefaultNetworkCapabilities;
            } else {
                networkCapabilities = this.mConnectivityManager.getNetworkCapabilities(this.mWifiManager.getCurrentNetwork());
            }
            this.isCaptivePortal = false;
            if (networkCapabilities != null) {
                if (networkCapabilities.hasCapability(17)) {
                    this.statusLabel = this.mContext.getString(R$string.wifi_status_sign_in_required);
                    this.isCaptivePortal = true;
                    return;
                } else if (networkCapabilities.hasCapability(24)) {
                    this.statusLabel = this.mContext.getString(R$string.wifi_limited_connection);
                    return;
                } else if (!networkCapabilities.hasCapability(16)) {
                    Settings.Global.getString(this.mContext.getContentResolver(), "private_dns_mode");
                    if (networkCapabilities.isPrivateDnsBroken()) {
                        this.statusLabel = this.mContext.getString(R$string.private_dns_broken);
                        return;
                    } else {
                        this.statusLabel = this.mContext.getString(R$string.wifi_status_no_internet);
                        return;
                    }
                } else if (!this.isDefaultNetwork && (networkCapabilities2 = this.mDefaultNetworkCapabilities) != null && networkCapabilities2.hasTransport(0)) {
                    this.statusLabel = this.mContext.getString(R$string.wifi_connected_low_quality);
                    return;
                }
            }
            ScoredNetwork scoredNetwork = this.mWifiNetworkScoreCache.getScoredNetwork(NetworkKey.createFromWifiInfo(this.mWifiInfo));
            if (scoredNetwork == null) {
                str = null;
            } else {
                str = AccessPoint.getSpeedLabel(this.mContext, scoredNetwork, this.rssi);
            }
            this.statusLabel = str;
        }
    }

    public void refreshLocale() {
        updateStatusLabel();
        this.mCallback.run();
    }

    private String getValidSsid(WifiInfo wifiInfo) {
        String ssid = wifiInfo.getSSID();
        if (ssid == null || "<unknown ssid>".equals(ssid)) {
            return null;
        }
        return ssid;
    }

    /* access modifiers changed from: private */
    public void recordLastWifiNetwork(String str) {
        String[] strArr = this.mHistory;
        int i = this.mHistoryIndex;
        strArr[i] = str;
        this.mHistoryIndex = (i + 1) % 32;
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("  - WiFi Network History ------");
        int i = 0;
        for (int i2 = 0; i2 < 32; i2++) {
            if (this.mHistory[i2] != null) {
                i++;
            }
        }
        int i3 = this.mHistoryIndex + 32;
        while (true) {
            i3--;
            if (i3 >= (this.mHistoryIndex + 32) - i) {
                printWriter.println("  Previous WiFiNetwork(" + ((this.mHistoryIndex + 32) - i3) + "): " + this.mHistory[i3 & 31]);
            } else {
                return;
            }
        }
    }
}
