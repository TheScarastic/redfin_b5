package com.android.settingslib.wifi;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.NetworkKey;
import android.net.ScoredNetwork;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkScoreCache;
import android.net.wifi.hotspot2.OsuProvider;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.net.wifi.hotspot2.ProvisioningCallback;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.CollectionUtils;
import com.android.settingslib.R$string;
import com.android.settingslib.utils.ThreadUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
@Deprecated
/* loaded from: classes.dex */
public class AccessPoint implements Comparable<AccessPoint> {
    static final AtomicInteger sLastId = new AtomicInteger(0);
    private String bssid;
    AccessPointListener mAccessPointListener;
    private WifiConfiguration mConfig;
    private WifiManager.ActionListener mConnectListener;
    private final Context mContext;
    private int mEapType;
    private final ArraySet<ScanResult> mExtraScanResults;
    private String mFqdn;
    private WifiInfo mInfo;
    private boolean mIsOweTransitionMode;
    private boolean mIsPskSaeTransitionMode;
    private boolean mIsRoaming;
    private boolean mIsScoredNetworkMetered;
    private String mKey;
    private final Object mLock;
    private NetworkInfo mNetworkInfo;
    private String mOsuFailure;
    private OsuProvider mOsuProvider;
    private boolean mOsuProvisioningComplete;
    private String mOsuStatus;
    private int mPasspointConfigurationVersion;
    private String mPasspointUniqueId;
    private String mProviderFriendlyName;
    private int mRssi;
    private final ArraySet<ScanResult> mScanResults;
    private final Map<String, TimestampedScoredNetwork> mScoredNetworkCache;
    private int mSpeed;
    private long mSubscriptionExpirationTimeInMillis;
    private Object mTag;
    private WifiManager mWifiManager;
    private int networkId;
    private int pskType;
    private int security;
    private String ssid;

    /* loaded from: classes.dex */
    public interface AccessPointListener {
        void onAccessPointChanged(AccessPoint accessPoint);

        void onLevelChanged(AccessPoint accessPoint);
    }

    private static int roundToClosestSpeedEnum(int i) {
        if (i < 5) {
            return 0;
        }
        if (i < 7) {
            return 5;
        }
        if (i < 15) {
            return 10;
        }
        return i < 25 ? 20 : 30;
    }

    public static String securityToString(int i, int i2) {
        return i == 1 ? "WEP" : i == 2 ? i2 == 1 ? "WPA" : i2 == 2 ? "WPA2" : i2 == 3 ? "WPA_WPA2" : "PSK" : i == 3 ? "EAP" : i == 5 ? "SAE" : i == 6 ? "SUITE_B" : i == 4 ? "OWE" : "NONE";
    }

    public AccessPoint(Context context, Bundle bundle) {
        this.mLock = new Object();
        ArraySet<ScanResult> arraySet = new ArraySet<>();
        this.mScanResults = arraySet;
        this.mExtraScanResults = new ArraySet<>();
        this.mScoredNetworkCache = new HashMap();
        this.networkId = -1;
        this.pskType = 0;
        this.mEapType = 0;
        this.mRssi = Integer.MIN_VALUE;
        this.mSpeed = 0;
        this.mIsScoredNetworkMetered = false;
        this.mIsRoaming = false;
        this.mPasspointConfigurationVersion = 0;
        this.mOsuProvisioningComplete = false;
        this.mIsPskSaeTransitionMode = false;
        this.mIsOweTransitionMode = false;
        this.mContext = context;
        if (bundle.containsKey("key_config")) {
            this.mConfig = (WifiConfiguration) bundle.getParcelable("key_config");
        }
        WifiConfiguration wifiConfiguration = this.mConfig;
        if (wifiConfiguration != null) {
            loadConfig(wifiConfiguration);
        }
        if (bundle.containsKey("key_ssid")) {
            this.ssid = bundle.getString("key_ssid");
        }
        if (bundle.containsKey("key_security")) {
            this.security = bundle.getInt("key_security");
        }
        if (bundle.containsKey("key_speed")) {
            this.mSpeed = bundle.getInt("key_speed");
        }
        if (bundle.containsKey("key_psktype")) {
            this.pskType = bundle.getInt("key_psktype");
        }
        if (bundle.containsKey("eap_psktype")) {
            this.mEapType = bundle.getInt("eap_psktype");
        }
        this.mInfo = (WifiInfo) bundle.getParcelable("key_wifiinfo");
        if (bundle.containsKey("key_networkinfo")) {
            this.mNetworkInfo = (NetworkInfo) bundle.getParcelable("key_networkinfo");
        }
        if (bundle.containsKey("key_scanresults")) {
            Parcelable[] parcelableArray = bundle.getParcelableArray("key_scanresults");
            arraySet.clear();
            for (Parcelable parcelable : parcelableArray) {
                this.mScanResults.add((ScanResult) parcelable);
            }
        }
        if (bundle.containsKey("key_scorednetworkcache")) {
            Iterator it = bundle.getParcelableArrayList("key_scorednetworkcache").iterator();
            while (it.hasNext()) {
                TimestampedScoredNetwork timestampedScoredNetwork = (TimestampedScoredNetwork) it.next();
                this.mScoredNetworkCache.put(timestampedScoredNetwork.getScore().networkKey.wifiKey.bssid, timestampedScoredNetwork);
            }
        }
        if (bundle.containsKey("key_passpoint_unique_id")) {
            this.mPasspointUniqueId = bundle.getString("key_passpoint_unique_id");
        }
        if (bundle.containsKey("key_fqdn")) {
            this.mFqdn = bundle.getString("key_fqdn");
        }
        if (bundle.containsKey("key_provider_friendly_name")) {
            this.mProviderFriendlyName = bundle.getString("key_provider_friendly_name");
        }
        if (bundle.containsKey("key_subscription_expiration_time_in_millis")) {
            this.mSubscriptionExpirationTimeInMillis = bundle.getLong("key_subscription_expiration_time_in_millis");
        }
        if (bundle.containsKey("key_passpoint_configuration_version")) {
            this.mPasspointConfigurationVersion = bundle.getInt("key_passpoint_configuration_version");
        }
        if (bundle.containsKey("key_is_psk_sae_transition_mode")) {
            this.mIsPskSaeTransitionMode = bundle.getBoolean("key_is_psk_sae_transition_mode");
        }
        if (bundle.containsKey("key_is_owe_transition_mode")) {
            this.mIsOweTransitionMode = bundle.getBoolean("key_is_owe_transition_mode");
        }
        update(this.mConfig, this.mInfo, this.mNetworkInfo);
        updateKey();
        updateBestRssiInfo();
    }

    public AccessPoint(Context context, WifiConfiguration wifiConfiguration) {
        this.mLock = new Object();
        this.mScanResults = new ArraySet<>();
        this.mExtraScanResults = new ArraySet<>();
        this.mScoredNetworkCache = new HashMap();
        this.networkId = -1;
        this.pskType = 0;
        this.mEapType = 0;
        this.mRssi = Integer.MIN_VALUE;
        this.mSpeed = 0;
        this.mIsScoredNetworkMetered = false;
        this.mIsRoaming = false;
        this.mPasspointConfigurationVersion = 0;
        this.mOsuProvisioningComplete = false;
        this.mIsPskSaeTransitionMode = false;
        this.mIsOweTransitionMode = false;
        this.mContext = context;
        loadConfig(wifiConfiguration);
        updateKey();
    }

    public AccessPoint(Context context, WifiConfiguration wifiConfiguration, Collection<ScanResult> collection, Collection<ScanResult> collection2) {
        this.mLock = new Object();
        this.mScanResults = new ArraySet<>();
        this.mExtraScanResults = new ArraySet<>();
        this.mScoredNetworkCache = new HashMap();
        this.networkId = -1;
        this.pskType = 0;
        this.mEapType = 0;
        this.mRssi = Integer.MIN_VALUE;
        this.mSpeed = 0;
        this.mIsScoredNetworkMetered = false;
        this.mIsRoaming = false;
        this.mPasspointConfigurationVersion = 0;
        this.mOsuProvisioningComplete = false;
        this.mIsPskSaeTransitionMode = false;
        this.mIsOweTransitionMode = false;
        this.mContext = context;
        this.networkId = wifiConfiguration.networkId;
        this.mConfig = wifiConfiguration;
        this.mPasspointUniqueId = wifiConfiguration.getKey();
        this.mFqdn = wifiConfiguration.FQDN;
        setScanResultsPasspoint(collection, collection2);
        updateKey();
    }

    public AccessPoint(Context context, OsuProvider osuProvider, Collection<ScanResult> collection) {
        this.mLock = new Object();
        this.mScanResults = new ArraySet<>();
        this.mExtraScanResults = new ArraySet<>();
        this.mScoredNetworkCache = new HashMap();
        this.networkId = -1;
        this.pskType = 0;
        this.mEapType = 0;
        this.mRssi = Integer.MIN_VALUE;
        this.mSpeed = 0;
        this.mIsScoredNetworkMetered = false;
        this.mIsRoaming = false;
        this.mPasspointConfigurationVersion = 0;
        this.mOsuProvisioningComplete = false;
        this.mIsPskSaeTransitionMode = false;
        this.mIsOweTransitionMode = false;
        this.mContext = context;
        this.mOsuProvider = osuProvider;
        setScanResults(collection);
        updateKey();
    }

    public AccessPoint(Context context, Collection<ScanResult> collection) {
        this.mLock = new Object();
        this.mScanResults = new ArraySet<>();
        this.mExtraScanResults = new ArraySet<>();
        this.mScoredNetworkCache = new HashMap();
        this.networkId = -1;
        this.pskType = 0;
        this.mEapType = 0;
        this.mRssi = Integer.MIN_VALUE;
        this.mSpeed = 0;
        this.mIsScoredNetworkMetered = false;
        this.mIsRoaming = false;
        this.mPasspointConfigurationVersion = 0;
        this.mOsuProvisioningComplete = false;
        this.mIsPskSaeTransitionMode = false;
        this.mIsOweTransitionMode = false;
        this.mContext = context;
        setScanResults(collection);
        updateKey();
    }

    @VisibleForTesting
    void loadConfig(WifiConfiguration wifiConfiguration) {
        String str = wifiConfiguration.SSID;
        this.ssid = str == null ? "" : removeDoubleQuotes(str);
        this.bssid = wifiConfiguration.BSSID;
        this.security = getSecurity(wifiConfiguration);
        this.networkId = wifiConfiguration.networkId;
        this.mConfig = wifiConfiguration;
    }

    private void updateKey() {
        if (isPasspoint()) {
            this.mKey = getKey(this.mConfig);
        } else if (isPasspointConfig()) {
            this.mKey = getKey(this.mPasspointUniqueId);
        } else if (isOsuProvider()) {
            this.mKey = getKey(this.mOsuProvider);
        } else {
            this.mKey = getKey(getSsidStr(), getBssid(), getSecurity());
        }
    }

    public int compareTo(AccessPoint accessPoint) {
        if (isActive() && !accessPoint.isActive()) {
            return -1;
        }
        if (!isActive() && accessPoint.isActive()) {
            return 1;
        }
        if (isReachable() && !accessPoint.isReachable()) {
            return -1;
        }
        if (!isReachable() && accessPoint.isReachable()) {
            return 1;
        }
        if (isSaved() && !accessPoint.isSaved()) {
            return -1;
        }
        if (!isSaved() && accessPoint.isSaved()) {
            return 1;
        }
        if (getSpeed() != accessPoint.getSpeed()) {
            return accessPoint.getSpeed() - getSpeed();
        }
        WifiManager wifiManager = getWifiManager();
        int calculateSignalLevel = wifiManager.calculateSignalLevel(accessPoint.mRssi) - wifiManager.calculateSignalLevel(this.mRssi);
        if (calculateSignalLevel != 0) {
            return calculateSignalLevel;
        }
        int compareToIgnoreCase = getTitle().compareToIgnoreCase(accessPoint.getTitle());
        if (compareToIgnoreCase != 0) {
            return compareToIgnoreCase;
        }
        return getSsidStr().compareTo(accessPoint.getSsidStr());
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        if ((obj instanceof AccessPoint) && compareTo((AccessPoint) obj) == 0) {
            return true;
        }
        return false;
    }

    @Override // java.lang.Object
    public int hashCode() {
        WifiInfo wifiInfo = this.mInfo;
        int i = 0;
        if (wifiInfo != null) {
            i = 0 + (wifiInfo.hashCode() * 13);
        }
        return i + (this.mRssi * 19) + (this.networkId * 23) + (this.ssid.hashCode() * 29);
    }

    @Override // java.lang.Object
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AccessPoint(");
        sb.append(this.ssid);
        if (this.bssid != null) {
            sb.append(":");
            sb.append(this.bssid);
        }
        if (isSaved()) {
            sb.append(',');
            sb.append("saved");
        }
        if (isActive()) {
            sb.append(',');
            sb.append("active");
        }
        if (isEphemeral()) {
            sb.append(',');
            sb.append("ephemeral");
        }
        if (isConnectable()) {
            sb.append(',');
            sb.append("connectable");
        }
        int i = this.security;
        if (!(i == 0 || i == 4)) {
            sb.append(',');
            sb.append(securityToString(this.security, this.pskType));
        }
        sb.append(",level=");
        sb.append(getLevel());
        if (this.mSpeed != 0) {
            sb.append(",speed=");
            sb.append(this.mSpeed);
        }
        sb.append(",metered=");
        sb.append(isMetered());
        if (isVerboseLoggingEnabled()) {
            sb.append(",rssi=");
            sb.append(this.mRssi);
            synchronized (this.mLock) {
                sb.append(",scan cache size=");
                sb.append(this.mScanResults.size() + this.mExtraScanResults.size());
            }
        }
        sb.append(')');
        return sb.toString();
    }

    public boolean update(WifiNetworkScoreCache wifiNetworkScoreCache, boolean z, long j) {
        boolean updateScores = z ? updateScores(wifiNetworkScoreCache, j) : false;
        if (updateMetered(wifiNetworkScoreCache) || updateScores) {
            return true;
        }
        return false;
    }

    private boolean updateScores(WifiNetworkScoreCache wifiNetworkScoreCache, long j) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        synchronized (this.mLock) {
            Iterator<ScanResult> it = this.mScanResults.iterator();
            while (it.hasNext()) {
                ScanResult next = it.next();
                ScoredNetwork scoredNetwork = wifiNetworkScoreCache.getScoredNetwork(next);
                if (scoredNetwork != null) {
                    TimestampedScoredNetwork timestampedScoredNetwork = this.mScoredNetworkCache.get(next.BSSID);
                    if (timestampedScoredNetwork == null) {
                        this.mScoredNetworkCache.put(next.BSSID, new TimestampedScoredNetwork(scoredNetwork, elapsedRealtime));
                    } else {
                        timestampedScoredNetwork.update(scoredNetwork, elapsedRealtime);
                    }
                }
            }
        }
        Iterator<TimestampedScoredNetwork> it2 = this.mScoredNetworkCache.values().iterator();
        it2.forEachRemaining(new Consumer(elapsedRealtime - j, it2) { // from class: com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda5
            public final /* synthetic */ long f$0;
            public final /* synthetic */ Iterator f$1;

            {
                this.f$0 = r1;
                this.f$1 = r3;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                AccessPoint.$r8$lambda$eeEFeIMR6UCrf_qCtv1iNqRPOo8(this.f$0, this.f$1, (TimestampedScoredNetwork) obj);
            }
        });
        return updateSpeed();
    }

    public static /* synthetic */ void lambda$updateScores$0(long j, Iterator it, TimestampedScoredNetwork timestampedScoredNetwork) {
        if (timestampedScoredNetwork.getUpdatedTimestampMillis() < j) {
            it.remove();
        }
    }

    private boolean updateSpeed() {
        int i = this.mSpeed;
        int generateAverageSpeedForSsid = generateAverageSpeedForSsid();
        this.mSpeed = generateAverageSpeedForSsid;
        boolean z = i != generateAverageSpeedForSsid;
        if (isVerboseLoggingEnabled() && z) {
            Log.i("SettingsLib.AccessPoint", String.format("%s: Set speed to %d", this.ssid, Integer.valueOf(this.mSpeed)));
        }
        return z;
    }

    private int generateAverageSpeedForSsid() {
        int i;
        if (this.mScoredNetworkCache.isEmpty()) {
            return 0;
        }
        if (Log.isLoggable("SettingsLib.AccessPoint", 3)) {
            Log.d("SettingsLib.AccessPoint", String.format("Generating fallbackspeed for %s using cache: %s", getSsidStr(), this.mScoredNetworkCache));
        }
        int i2 = 0;
        int i3 = 0;
        for (TimestampedScoredNetwork timestampedScoredNetwork : this.mScoredNetworkCache.values()) {
            int calculateBadge = timestampedScoredNetwork.getScore().calculateBadge(this.mRssi);
            if (calculateBadge != 0) {
                i2++;
                i3 += calculateBadge;
            }
        }
        if (i2 == 0) {
            i = 0;
        } else {
            i = i3 / i2;
        }
        if (isVerboseLoggingEnabled()) {
            Log.i("SettingsLib.AccessPoint", String.format("%s generated fallback speed is: %d", getSsidStr(), Integer.valueOf(i)));
        }
        return roundToClosestSpeedEnum(i);
    }

    private boolean updateMetered(WifiNetworkScoreCache wifiNetworkScoreCache) {
        WifiInfo wifiInfo;
        boolean z = this.mIsScoredNetworkMetered;
        this.mIsScoredNetworkMetered = false;
        if (!isActive() || (wifiInfo = this.mInfo) == null) {
            synchronized (this.mLock) {
                Iterator<ScanResult> it = this.mScanResults.iterator();
                while (it.hasNext()) {
                    ScoredNetwork scoredNetwork = wifiNetworkScoreCache.getScoredNetwork(it.next());
                    if (scoredNetwork != null) {
                        this.mIsScoredNetworkMetered = scoredNetwork.meteredHint | this.mIsScoredNetworkMetered;
                    }
                }
            }
        } else {
            ScoredNetwork scoredNetwork2 = wifiNetworkScoreCache.getScoredNetwork(NetworkKey.createFromWifiInfo(wifiInfo));
            if (scoredNetwork2 != null) {
                this.mIsScoredNetworkMetered = scoredNetwork2.meteredHint | this.mIsScoredNetworkMetered;
            }
        }
        if (z != this.mIsScoredNetworkMetered) {
            return true;
        }
        return false;
    }

    public static String getKey(Context context, ScanResult scanResult) {
        return getKey(scanResult.SSID, scanResult.BSSID, getSecurity(context, scanResult));
    }

    public static String getKey(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration.isPasspoint()) {
            return getKey(wifiConfiguration.getKey());
        }
        return getKey(removeDoubleQuotes(wifiConfiguration.SSID), wifiConfiguration.BSSID, getSecurity(wifiConfiguration));
    }

    public static String getKey(String str) {
        return "PASSPOINT:" + str;
    }

    public static String getKey(OsuProvider osuProvider) {
        return "OSU:" + osuProvider.getFriendlyName() + ',' + osuProvider.getServerUri();
    }

    private static String getKey(String str, String str2, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("AP:");
        if (TextUtils.isEmpty(str)) {
            sb.append(str2);
        } else {
            sb.append(str);
        }
        sb.append(',');
        sb.append(i);
        return sb.toString();
    }

    public String getKey() {
        return this.mKey;
    }

    public boolean matches(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration.isPasspoint()) {
            return isPasspoint() && wifiConfiguration.getKey().equals(this.mConfig.getKey());
        }
        if (!this.ssid.equals(removeDoubleQuotes(wifiConfiguration.SSID))) {
            return false;
        }
        WifiConfiguration wifiConfiguration2 = this.mConfig;
        if (wifiConfiguration2 != null && wifiConfiguration2.shared != wifiConfiguration.shared) {
            return false;
        }
        int security = getSecurity(wifiConfiguration);
        if (!this.mIsPskSaeTransitionMode || ((security != 5 || !getWifiManager().isWpa3SaeSupported()) && security != 2)) {
            return (this.mIsOweTransitionMode && ((security == 4 && getWifiManager().isEnhancedOpenSupported()) || security == 0)) || this.security == getSecurity(wifiConfiguration);
        }
        return true;
    }

    private boolean matches(WifiConfiguration wifiConfiguration, WifiInfo wifiInfo) {
        if (wifiConfiguration == null || wifiInfo == null) {
            return false;
        }
        if (wifiConfiguration.isPasspoint() || isSameSsidOrBssid(wifiInfo)) {
            return matches(wifiConfiguration);
        }
        return false;
    }

    @VisibleForTesting
    boolean matches(ScanResult scanResult) {
        if (scanResult == null) {
            return false;
        }
        if (isPasspoint() || isOsuProvider()) {
            throw new IllegalStateException("Should not matches a Passpoint by ScanResult");
        } else if (!isSameSsidOrBssid(scanResult)) {
            return false;
        } else {
            if (!this.mIsPskSaeTransitionMode) {
                int i = this.security;
                if ((i == 5 || i == 2) && isPskSaeTransitionMode(scanResult)) {
                    return true;
                }
            } else if ((scanResult.capabilities.contains("SAE") && getWifiManager().isWpa3SaeSupported()) || scanResult.capabilities.contains("PSK")) {
                return true;
            }
            if (this.mIsOweTransitionMode) {
                int security = getSecurity(this.mContext, scanResult);
                if ((security == 4 && getWifiManager().isEnhancedOpenSupported()) || security == 0) {
                    return true;
                }
            } else {
                int i2 = this.security;
                if ((i2 == 4 || i2 == 0) && isOweTransitionMode(scanResult)) {
                    return true;
                }
            }
            if (this.security == getSecurity(this.mContext, scanResult)) {
                return true;
            }
            return false;
        }
    }

    public WifiConfiguration getConfig() {
        return this.mConfig;
    }

    public int getLevel() {
        return getWifiManager().calculateSignalLevel(this.mRssi);
    }

    private void updateBestRssiInfo() {
        int i;
        int i2;
        if (!isActive()) {
            ScanResult scanResult = null;
            synchronized (this.mLock) {
                Iterator<ScanResult> it = this.mScanResults.iterator();
                i = Integer.MIN_VALUE;
                while (it.hasNext()) {
                    ScanResult next = it.next();
                    int i3 = next.level;
                    if (i3 > i) {
                        scanResult = next;
                        i = i3;
                    }
                }
            }
            if (i == Integer.MIN_VALUE || (i2 = this.mRssi) == Integer.MIN_VALUE) {
                this.mRssi = i;
            } else {
                this.mRssi = (i2 + i) / 2;
            }
            if (scanResult != null) {
                this.ssid = scanResult.SSID;
                this.bssid = scanResult.BSSID;
                int security = getSecurity(this.mContext, scanResult);
                this.security = security;
                if (security == 2 || security == 5) {
                    this.pskType = getPskType(scanResult);
                }
                if (this.security == 3) {
                    this.mEapType = getEapType(scanResult);
                }
                this.mIsPskSaeTransitionMode = isPskSaeTransitionMode(scanResult);
                this.mIsOweTransitionMode = isOweTransitionMode(scanResult);
            }
            if (isPasspoint()) {
                this.mConfig.SSID = convertToQuotedString(this.ssid);
            }
        }
    }

    public boolean isMetered() {
        return this.mIsScoredNetworkMetered || WifiConfiguration.isMetered(this.mConfig, this.mInfo);
    }

    public int getSecurity() {
        return this.security;
    }

    public String getSsidStr() {
        return this.ssid;
    }

    public String getBssid() {
        return this.bssid;
    }

    public NetworkInfo.DetailedState getDetailedState() {
        NetworkInfo networkInfo = this.mNetworkInfo;
        if (networkInfo != null) {
            return networkInfo.getDetailedState();
        }
        Log.w("SettingsLib.AccessPoint", "NetworkInfo is null, cannot return detailed state");
        return null;
    }

    public String getTitle() {
        if (isPasspoint() && !TextUtils.isEmpty(this.mConfig.providerFriendlyName)) {
            return this.mConfig.providerFriendlyName;
        }
        if (isPasspointConfig() && !TextUtils.isEmpty(this.mProviderFriendlyName)) {
            return this.mProviderFriendlyName;
        }
        if (!isOsuProvider() || TextUtils.isEmpty(this.mOsuProvider.getFriendlyName())) {
            return !TextUtils.isEmpty(getSsidStr()) ? getSsidStr() : "";
        }
        return this.mOsuProvider.getFriendlyName();
    }

    public boolean isActive() {
        NetworkInfo networkInfo = this.mNetworkInfo;
        return (networkInfo == null || (this.networkId == -1 && networkInfo.getState() == NetworkInfo.State.DISCONNECTED)) ? false : true;
    }

    public boolean isConnectable() {
        return getLevel() != -1 && getDetailedState() == null;
    }

    public boolean isEphemeral() {
        NetworkInfo networkInfo;
        WifiInfo wifiInfo = this.mInfo;
        return (wifiInfo == null || !wifiInfo.isEphemeral() || (networkInfo = this.mNetworkInfo) == null || networkInfo.getState() == NetworkInfo.State.DISCONNECTED) ? false : true;
    }

    public boolean isPasspoint() {
        WifiConfiguration wifiConfiguration = this.mConfig;
        return wifiConfiguration != null && wifiConfiguration.isPasspoint();
    }

    public boolean isPasspointConfig() {
        return this.mPasspointUniqueId != null && this.mConfig == null;
    }

    public boolean isOsuProvider() {
        return this.mOsuProvider != null;
    }

    private boolean isInfoForThisAccessPoint(WifiConfiguration wifiConfiguration, WifiInfo wifiInfo) {
        if (wifiInfo.isOsuAp() || this.mOsuStatus != null) {
            return wifiInfo.isOsuAp() && this.mOsuStatus != null;
        }
        if (wifiInfo.isPasspointAp() || isPasspoint()) {
            return wifiInfo.isPasspointAp() && isPasspoint() && TextUtils.equals(wifiInfo.getPasspointFqdn(), this.mConfig.FQDN) && TextUtils.equals(wifiInfo.getPasspointProviderFriendlyName(), this.mConfig.providerFriendlyName);
        }
        int i = this.networkId;
        if (i != -1) {
            return i == wifiInfo.getNetworkId();
        }
        if (wifiConfiguration != null) {
            return matches(wifiConfiguration, wifiInfo);
        }
        return TextUtils.equals(removeDoubleQuotes(wifiInfo.getSSID()), this.ssid);
    }

    public boolean isSaved() {
        return this.mConfig != null;
    }

    public void setTag(Object obj) {
        this.mTag = obj;
    }

    public void setScanResults(Collection<ScanResult> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            Log.d("SettingsLib.AccessPoint", "Cannot set scan results to empty list");
            return;
        }
        if (this.mKey != null && !isPasspoint() && !isOsuProvider()) {
            for (ScanResult scanResult : collection) {
                if (!matches(scanResult)) {
                    Log.d("SettingsLib.AccessPoint", String.format("ScanResult %s\nkey of %s did not match current AP key %s", scanResult, getKey(this.mContext, scanResult), this.mKey));
                    return;
                }
            }
        }
        int level = getLevel();
        synchronized (this.mLock) {
            this.mScanResults.clear();
            this.mScanResults.addAll(collection);
        }
        updateBestRssiInfo();
        int level2 = getLevel();
        if (level2 > 0 && level2 != level) {
            updateSpeed();
            ThreadUtils.postOnMainThread(new Runnable() { // from class: com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AccessPoint.$r8$lambda$1MyryaP_FPxIoOWXMURmr9gSCko(AccessPoint.this);
                }
            });
        }
        ThreadUtils.postOnMainThread(new Runnable() { // from class: com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                AccessPoint.m34$r8$lambda$l7HvoRkL0mn502GEmI049YA2Xk(AccessPoint.this);
            }
        });
    }

    public /* synthetic */ void lambda$setScanResults$1() {
        AccessPointListener accessPointListener = this.mAccessPointListener;
        if (accessPointListener != null) {
            accessPointListener.onLevelChanged(this);
        }
    }

    public /* synthetic */ void lambda$setScanResults$2() {
        AccessPointListener accessPointListener = this.mAccessPointListener;
        if (accessPointListener != null) {
            accessPointListener.onAccessPointChanged(this);
        }
    }

    public void setScanResultsPasspoint(Collection<ScanResult> collection, Collection<ScanResult> collection2) {
        synchronized (this.mLock) {
            this.mExtraScanResults.clear();
            if (!CollectionUtils.isEmpty(collection)) {
                this.mIsRoaming = false;
                if (!CollectionUtils.isEmpty(collection2)) {
                    this.mExtraScanResults.addAll(collection2);
                }
                setScanResults(collection);
            } else if (!CollectionUtils.isEmpty(collection2)) {
                this.mIsRoaming = true;
                setScanResults(collection2);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0046, code lost:
        if (r5.getDetailedState() != r7.getDetailedState()) goto L_0x0036;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean update(android.net.wifi.WifiConfiguration r5, android.net.wifi.WifiInfo r6, android.net.NetworkInfo r7) {
        /*
            r4 = this;
            int r0 = r4.getLevel()
            r1 = 0
            r2 = 1
            if (r6 == 0) goto L_0x004e
            boolean r3 = r4.isInfoForThisAccessPoint(r5, r6)
            if (r3 == 0) goto L_0x004e
            android.net.wifi.WifiInfo r3 = r4.mInfo
            if (r3 != 0) goto L_0x0013
            r1 = r2
        L_0x0013:
            boolean r3 = r4.isPasspoint()
            if (r3 != 0) goto L_0x0020
            android.net.wifi.WifiConfiguration r3 = r4.mConfig
            if (r3 == r5) goto L_0x0020
            r4.update(r5)
        L_0x0020:
            int r5 = r4.mRssi
            int r3 = r6.getRssi()
            if (r5 == r3) goto L_0x0038
            int r5 = r6.getRssi()
            r3 = -127(0xffffffffffffff81, float:NaN)
            if (r5 == r3) goto L_0x0038
            int r5 = r6.getRssi()
            r4.mRssi = r5
        L_0x0036:
            r1 = r2
            goto L_0x0049
        L_0x0038:
            android.net.NetworkInfo r5 = r4.mNetworkInfo
            if (r5 == 0) goto L_0x0049
            if (r7 == 0) goto L_0x0049
            android.net.NetworkInfo$DetailedState r5 = r5.getDetailedState()
            android.net.NetworkInfo$DetailedState r3 = r7.getDetailedState()
            if (r5 == r3) goto L_0x0049
            goto L_0x0036
        L_0x0049:
            r4.mInfo = r6
            r4.mNetworkInfo = r7
            goto L_0x0058
        L_0x004e:
            android.net.wifi.WifiInfo r5 = r4.mInfo
            if (r5 == 0) goto L_0x0058
            r5 = 0
            r4.mInfo = r5
            r4.mNetworkInfo = r5
            r1 = r2
        L_0x0058:
            if (r1 == 0) goto L_0x0074
            com.android.settingslib.wifi.AccessPoint$AccessPointListener r5 = r4.mAccessPointListener
            if (r5 == 0) goto L_0x0074
            com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda3 r5 = new com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda3
            r5.<init>()
            com.android.settingslib.utils.ThreadUtils.postOnMainThread(r5)
            int r5 = r4.getLevel()
            if (r0 == r5) goto L_0x0074
            com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda2 r5 = new com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda2
            r5.<init>()
            com.android.settingslib.utils.ThreadUtils.postOnMainThread(r5)
        L_0x0074:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.wifi.AccessPoint.update(android.net.wifi.WifiConfiguration, android.net.wifi.WifiInfo, android.net.NetworkInfo):boolean");
    }

    public /* synthetic */ void lambda$update$3() {
        AccessPointListener accessPointListener = this.mAccessPointListener;
        if (accessPointListener != null) {
            accessPointListener.onAccessPointChanged(this);
        }
    }

    public /* synthetic */ void lambda$update$4() {
        AccessPointListener accessPointListener = this.mAccessPointListener;
        if (accessPointListener != null) {
            accessPointListener.onLevelChanged(this);
        }
    }

    public void update(WifiConfiguration wifiConfiguration) {
        this.mConfig = wifiConfiguration;
        if (wifiConfiguration != null && !isPasspoint()) {
            this.ssid = removeDoubleQuotes(this.mConfig.SSID);
        }
        this.networkId = wifiConfiguration != null ? wifiConfiguration.networkId : -1;
        ThreadUtils.postOnMainThread(new Runnable() { // from class: com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                AccessPoint.$r8$lambda$9sqJQrSDLPPBbVafxevqWDKYWqc(AccessPoint.this);
            }
        });
    }

    public /* synthetic */ void lambda$update$5() {
        AccessPointListener accessPointListener = this.mAccessPointListener;
        if (accessPointListener != null) {
            accessPointListener.onAccessPointChanged(this);
        }
    }

    @VisibleForTesting
    public void setRssi(int i) {
        this.mRssi = i;
    }

    int getSpeed() {
        return this.mSpeed;
    }

    private static String getSpeedLabel(Context context, int i) {
        if (i == 5) {
            return context.getString(R$string.speed_label_slow);
        }
        if (i == 10) {
            return context.getString(R$string.speed_label_okay);
        }
        if (i == 20) {
            return context.getString(R$string.speed_label_fast);
        }
        if (i != 30) {
            return null;
        }
        return context.getString(R$string.speed_label_very_fast);
    }

    public static String getSpeedLabel(Context context, ScoredNetwork scoredNetwork, int i) {
        return getSpeedLabel(context, roundToClosestSpeedEnum(scoredNetwork.calculateBadge(i)));
    }

    public boolean isReachable() {
        return this.mRssi != Integer.MIN_VALUE;
    }

    public static String convertToQuotedString(String str) {
        return "\"" + str + "\"";
    }

    private static int getPskType(ScanResult scanResult) {
        boolean contains = scanResult.capabilities.contains("WPA-PSK");
        boolean contains2 = scanResult.capabilities.contains("RSN-PSK");
        boolean contains3 = scanResult.capabilities.contains("RSN-SAE");
        if (contains2 && contains) {
            return 3;
        }
        if (contains2) {
            return 2;
        }
        if (contains) {
            return 1;
        }
        if (contains3) {
            return 0;
        }
        Log.w("SettingsLib.AccessPoint", "Received abnormal flag string: " + scanResult.capabilities);
        return 0;
    }

    private static int getEapType(ScanResult scanResult) {
        if (scanResult.capabilities.contains("RSN-EAP")) {
            return 2;
        }
        return scanResult.capabilities.contains("WPA-EAP") ? 1 : 0;
    }

    private static int getSecurity(Context context, ScanResult scanResult) {
        boolean contains = scanResult.capabilities.contains("WEP");
        boolean contains2 = scanResult.capabilities.contains("SAE");
        boolean contains3 = scanResult.capabilities.contains("PSK");
        boolean contains4 = scanResult.capabilities.contains("EAP_SUITE_B_192");
        boolean contains5 = scanResult.capabilities.contains("EAP");
        boolean contains6 = scanResult.capabilities.contains("OWE");
        boolean contains7 = scanResult.capabilities.contains("OWE_TRANSITION");
        if (contains2 && contains3) {
            return ((WifiManager) context.getSystemService("wifi")).isWpa3SaeSupported() ? 5 : 2;
        }
        if (contains7) {
            return ((WifiManager) context.getSystemService("wifi")).isEnhancedOpenSupported() ? 4 : 0;
        }
        if (contains) {
            return 1;
        }
        if (contains2) {
            return 5;
        }
        if (contains3) {
            return 2;
        }
        if (contains4) {
            return 6;
        }
        if (contains5) {
            return 3;
        }
        return contains6 ? 4 : 0;
    }

    public static int getSecurity(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration.allowedKeyManagement.get(8)) {
            return 5;
        }
        if (wifiConfiguration.allowedKeyManagement.get(1)) {
            return 2;
        }
        if (wifiConfiguration.allowedKeyManagement.get(10)) {
            return 6;
        }
        if (wifiConfiguration.allowedKeyManagement.get(2) || wifiConfiguration.allowedKeyManagement.get(3)) {
            return 3;
        }
        if (wifiConfiguration.allowedKeyManagement.get(9)) {
            return 4;
        }
        int i = wifiConfiguration.wepTxKeyIndex;
        if (i >= 0) {
            String[] strArr = wifiConfiguration.wepKeys;
            if (i < strArr.length && strArr[i] != null) {
                return 1;
            }
        }
        return 0;
    }

    static String removeDoubleQuotes(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        int length = str.length();
        if (length <= 1 || str.charAt(0) != '\"') {
            return str;
        }
        int i = length - 1;
        return str.charAt(i) == '\"' ? str.substring(1, i) : str;
    }

    public WifiManager getWifiManager() {
        if (this.mWifiManager == null) {
            this.mWifiManager = (WifiManager) this.mContext.getSystemService("wifi");
        }
        return this.mWifiManager;
    }

    private static boolean isVerboseLoggingEnabled() {
        return WifiTracker.sVerboseLogging || Log.isLoggable("SettingsLib.AccessPoint", 2);
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public class AccessPointProvisioningCallback extends ProvisioningCallback {
        final /* synthetic */ AccessPoint this$0;

        public void onProvisioningFailure(int i) {
            if (TextUtils.equals(this.this$0.mOsuStatus, this.this$0.mContext.getString(R$string.osu_completing_sign_up))) {
                AccessPoint accessPoint = this.this$0;
                accessPoint.mOsuFailure = accessPoint.mContext.getString(R$string.osu_sign_up_failed);
            } else {
                AccessPoint accessPoint2 = this.this$0;
                accessPoint2.mOsuFailure = accessPoint2.mContext.getString(R$string.osu_connect_failed);
            }
            this.this$0.mOsuStatus = null;
            this.this$0.mOsuProvisioningComplete = false;
            ThreadUtils.postOnMainThread(new AccessPoint$AccessPointProvisioningCallback$$ExternalSyntheticLambda1(this));
        }

        public /* synthetic */ void lambda$onProvisioningFailure$0() {
            AccessPoint accessPoint = this.this$0;
            AccessPointListener accessPointListener = accessPoint.mAccessPointListener;
            if (accessPointListener != null) {
                accessPointListener.onAccessPointChanged(accessPoint);
            }
        }

        public void onProvisioningStatus(int i) {
            String str;
            switch (i) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    str = String.format(this.this$0.mContext.getString(R$string.osu_opening_provider), this.this$0.mOsuProvider.getFriendlyName());
                    break;
                case 8:
                case 9:
                case 10:
                case 11:
                    str = this.this$0.mContext.getString(R$string.osu_completing_sign_up);
                    break;
                default:
                    str = null;
                    break;
            }
            boolean equals = true ^ TextUtils.equals(this.this$0.mOsuStatus, str);
            this.this$0.mOsuStatus = str;
            this.this$0.mOsuFailure = null;
            this.this$0.mOsuProvisioningComplete = false;
            if (equals) {
                ThreadUtils.postOnMainThread(new AccessPoint$AccessPointProvisioningCallback$$ExternalSyntheticLambda2(this));
            }
        }

        public /* synthetic */ void lambda$onProvisioningStatus$1() {
            AccessPoint accessPoint = this.this$0;
            AccessPointListener accessPointListener = accessPoint.mAccessPointListener;
            if (accessPointListener != null) {
                accessPointListener.onAccessPointChanged(accessPoint);
            }
        }

        public void onProvisioningComplete() {
            this.this$0.mOsuProvisioningComplete = true;
            this.this$0.mOsuFailure = null;
            this.this$0.mOsuStatus = null;
            ThreadUtils.postOnMainThread(new AccessPoint$AccessPointProvisioningCallback$$ExternalSyntheticLambda0(this));
            WifiManager wifiManager = this.this$0.getWifiManager();
            PasspointConfiguration passpointConfiguration = (PasspointConfiguration) wifiManager.getMatchingPasspointConfigsForOsuProviders(Collections.singleton(this.this$0.mOsuProvider)).get(this.this$0.mOsuProvider);
            if (passpointConfiguration == null) {
                Log.e("SettingsLib.AccessPoint", "Missing PasspointConfiguration for newly provisioned network!");
                if (this.this$0.mConnectListener != null) {
                    this.this$0.mConnectListener.onFailure(0);
                    return;
                }
                return;
            }
            String uniqueId = passpointConfiguration.getUniqueId();
            for (Pair pair : wifiManager.getAllMatchingWifiConfigs(wifiManager.getScanResults())) {
                WifiConfiguration wifiConfiguration = (WifiConfiguration) pair.first;
                if (TextUtils.equals(wifiConfiguration.getKey(), uniqueId)) {
                    wifiManager.connect(new AccessPoint(this.this$0.mContext, wifiConfiguration, (List) ((Map) pair.second).get(0), (List) ((Map) pair.second).get(1)).getConfig(), this.this$0.mConnectListener);
                    return;
                }
            }
            if (this.this$0.mConnectListener != null) {
                this.this$0.mConnectListener.onFailure(0);
            }
        }

        public /* synthetic */ void lambda$onProvisioningComplete$2() {
            AccessPoint accessPoint = this.this$0;
            AccessPointListener accessPointListener = accessPoint.mAccessPointListener;
            if (accessPointListener != null) {
                accessPointListener.onAccessPointChanged(accessPoint);
            }
        }
    }

    private static boolean isPskSaeTransitionMode(ScanResult scanResult) {
        return scanResult.capabilities.contains("PSK") && scanResult.capabilities.contains("SAE");
    }

    private static boolean isOweTransitionMode(ScanResult scanResult) {
        return scanResult.capabilities.contains("OWE_TRANSITION");
    }

    private boolean isSameSsidOrBssid(ScanResult scanResult) {
        if (scanResult == null) {
            return false;
        }
        if (TextUtils.equals(this.ssid, scanResult.SSID)) {
            return true;
        }
        String str = scanResult.BSSID;
        return str != null && TextUtils.equals(this.bssid, str);
    }

    private boolean isSameSsidOrBssid(WifiInfo wifiInfo) {
        if (wifiInfo == null) {
            return false;
        }
        if (TextUtils.equals(this.ssid, removeDoubleQuotes(wifiInfo.getSSID()))) {
            return true;
        }
        return wifiInfo.getBSSID() != null && TextUtils.equals(this.bssid, wifiInfo.getBSSID());
    }
}
