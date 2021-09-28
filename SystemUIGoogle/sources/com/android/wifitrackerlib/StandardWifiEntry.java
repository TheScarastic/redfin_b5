package com.android.wifitrackerlib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkScoreManager;
import android.net.NetworkScorerAppData;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkScoreCache;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wifitrackerlib.WifiEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
@VisibleForTesting
/* loaded from: classes2.dex */
public class StandardWifiEntry extends WifiEntry {
    private final Context mContext;
    private final boolean mIsEnhancedOpenSupported;
    private boolean mIsUserShareable;
    private final boolean mIsWpa3SaeSupported;
    private final boolean mIsWpa3SuiteBSupported;
    private final StandardWifiEntryKey mKey;
    private final Map<Integer, List<ScanResult>> mMatchingScanResults;
    private final Map<Integer, WifiConfiguration> mMatchingWifiConfigs;
    private String mRecommendationServiceLabel;
    private boolean mShouldAutoOpenCaptivePortal;
    private final List<ScanResult> mTargetScanResults;
    private List<Integer> mTargetSecurityTypes;
    private WifiConfiguration mTargetWifiConfig;

    public StandardWifiEntry(Context context, Handler handler, StandardWifiEntryKey standardWifiEntryKey, WifiManager wifiManager, WifiNetworkScoreCache wifiNetworkScoreCache, boolean z) {
        super(handler, wifiManager, wifiNetworkScoreCache, z);
        this.mMatchingScanResults = new HashMap();
        this.mMatchingWifiConfigs = new HashMap();
        this.mTargetScanResults = new ArrayList();
        this.mTargetSecurityTypes = new ArrayList();
        this.mIsUserShareable = false;
        this.mShouldAutoOpenCaptivePortal = false;
        this.mContext = context;
        this.mKey = standardWifiEntryKey;
        this.mIsWpa3SaeSupported = wifiManager.isWpa3SaeSupported();
        this.mIsWpa3SuiteBSupported = wifiManager.isWpa3SuiteBSupported();
        this.mIsEnhancedOpenSupported = wifiManager.isEnhancedOpenSupported();
        updateRecommendationServiceLabel();
    }

    public StandardWifiEntry(Context context, Handler handler, StandardWifiEntryKey standardWifiEntryKey, List<WifiConfiguration> list, List<ScanResult> list2, WifiManager wifiManager, WifiNetworkScoreCache wifiNetworkScoreCache, boolean z) throws IllegalArgumentException {
        this(context, handler, standardWifiEntryKey, wifiManager, wifiNetworkScoreCache, z);
        if (list != null && !list.isEmpty()) {
            updateConfig(list);
        }
        if (list2 != null && !list2.isEmpty()) {
            updateScanResultInfo(list2);
        }
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public String getKey() {
        return this.mKey.toString();
    }

    public StandardWifiEntryKey getStandardWifiEntryKey() {
        return this.mKey;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public String getTitle() {
        return this.mKey.getScanResultKey().getSsid();
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public synchronized String getSummary(boolean z) {
        StringJoiner stringJoiner;
        String str;
        stringJoiner = new StringJoiner(this.mContext.getString(R$string.wifitrackerlib_summary_separator));
        int connectedState = getConnectedState();
        if (connectedState == 0) {
            str = Utils.getDisconnectedDescription(this.mContext, this.mTargetWifiConfig, this.mForSavedNetworksPage, z);
        } else if (connectedState == 1) {
            str = Utils.getConnectingDescription(this.mContext, this.mNetworkInfo);
        } else if (connectedState != 2) {
            Log.e("StandardWifiEntry", "getConnectedState() returned unknown state: " + connectedState);
            str = null;
        } else {
            str = Utils.getConnectedDescription(this.mContext, this.mTargetWifiConfig, this.mNetworkCapabilities, this.mRecommendationServiceLabel, this.mIsDefaultNetwork, this.mIsLowQuality);
        }
        if (!TextUtils.isEmpty(str)) {
            stringJoiner.add(str);
        }
        String speedDescription = Utils.getSpeedDescription(this.mContext, this);
        if (!TextUtils.isEmpty(speedDescription)) {
            stringJoiner.add(speedDescription);
        }
        String autoConnectDescription = Utils.getAutoConnectDescription(this.mContext, this);
        if (!TextUtils.isEmpty(autoConnectDescription)) {
            stringJoiner.add(autoConnectDescription);
        }
        String meteredDescription = Utils.getMeteredDescription(this.mContext, this);
        if (!TextUtils.isEmpty(meteredDescription)) {
            stringJoiner.add(meteredDescription);
        }
        if (!z) {
            String verboseLoggingDescription = Utils.getVerboseLoggingDescription(this);
            if (!TextUtils.isEmpty(verboseLoggingDescription)) {
                stringJoiner.add(verboseLoggingDescription);
            }
        }
        return stringJoiner.toString();
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public String getSsid() {
        return this.mKey.getScanResultKey().getSsid();
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public synchronized List<Integer> getSecurityTypes() {
        return new ArrayList(this.mTargetSecurityTypes);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x000e, code lost:
        if (r0.meteredHint != false) goto L_0x0012;
     */
    @Override // com.android.wifitrackerlib.WifiEntry
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean isMetered() {
        /*
            r2 = this;
            monitor-enter(r2)
            int r0 = r2.getMeteredChoice()     // Catch: all -> 0x0014
            r1 = 1
            if (r0 == r1) goto L_0x0012
            android.net.wifi.WifiConfiguration r0 = r2.mTargetWifiConfig     // Catch: all -> 0x0014
            if (r0 == 0) goto L_0x0011
            boolean r0 = r0.meteredHint     // Catch: all -> 0x0014
            if (r0 == 0) goto L_0x0011
            goto L_0x0012
        L_0x0011:
            r1 = 0
        L_0x0012:
            monitor-exit(r2)
            return r1
        L_0x0014:
            r0 = move-exception
            monitor-exit(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.isMetered():boolean");
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public synchronized boolean isSaved() {
        boolean z;
        WifiConfiguration wifiConfiguration = this.mTargetWifiConfig;
        if (wifiConfiguration != null && !wifiConfiguration.fromWifiNetworkSuggestion) {
            if (!wifiConfiguration.isEphemeral()) {
                z = true;
            }
        }
        z = false;
        return z;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public synchronized boolean isSuggestion() {
        boolean z;
        WifiConfiguration wifiConfiguration = this.mTargetWifiConfig;
        if (wifiConfiguration != null) {
            if (wifiConfiguration.fromWifiNetworkSuggestion) {
                z = true;
            }
        }
        z = false;
        return z;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public synchronized WifiConfiguration getWifiConfiguration() {
        if (!isSaved()) {
            return null;
        }
        return this.mTargetWifiConfig;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public synchronized void connect(WifiEntry.ConnectCallback connectCallback) {
        this.mConnectCallback = connectCallback;
        this.mShouldAutoOpenCaptivePortal = true;
        this.mWifiManager.stopRestrictingAutoJoinToSubscriptionId();
        if (!isSaved() && !isSuggestion()) {
            if (this.mTargetSecurityTypes.contains(6)) {
                WifiConfiguration wifiConfiguration = new WifiConfiguration();
                wifiConfiguration.SSID = "\"" + this.mKey.getScanResultKey().getSsid() + "\"";
                wifiConfiguration.setSecurityParams(6);
                this.mWifiManager.connect(wifiConfiguration, new WifiEntry.ConnectActionListener());
                if (this.mTargetSecurityTypes.contains(0)) {
                    WifiConfiguration wifiConfiguration2 = new WifiConfiguration();
                    wifiConfiguration2.SSID = "\"" + this.mKey.getScanResultKey().getSsid() + "\"";
                    wifiConfiguration2.setSecurityParams(0);
                    this.mWifiManager.save(wifiConfiguration2, null);
                }
            } else if (this.mTargetSecurityTypes.contains(0)) {
                WifiConfiguration wifiConfiguration3 = new WifiConfiguration();
                wifiConfiguration3.SSID = "\"" + this.mKey.getScanResultKey().getSsid() + "\"";
                wifiConfiguration3.setSecurityParams(0);
                this.mWifiManager.connect(wifiConfiguration3, new WifiEntry.ConnectActionListener());
            } else if (connectCallback != null) {
                this.mCallbackHandler.post(new Runnable() { // from class: com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        StandardWifiEntry.$r8$lambda$Oj8LAlv3oYIR1ZuYJaMh673O0GM(WifiEntry.ConnectCallback.this);
                    }
                });
            }
            return;
        }
        if (!Utils.isSimCredential(this.mTargetWifiConfig) || Utils.isSimPresent(this.mContext, this.mTargetWifiConfig.carrierId)) {
            this.mWifiManager.connect(this.mTargetWifiConfig.networkId, new WifiEntry.ConnectActionListener());
            return;
        }
        if (connectCallback != null) {
            this.mCallbackHandler.post(new Runnable() { // from class: com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    StandardWifiEntry.$r8$lambda$BNOv0DaeSYrbD4uxWuNPGPtrXFk(WifiEntry.ConnectCallback.this);
                }
            });
        }
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public synchronized boolean canSignIn() {
        boolean z;
        NetworkCapabilities networkCapabilities = this.mNetworkCapabilities;
        if (networkCapabilities != null) {
            if (networkCapabilities.hasCapability(17)) {
                z = true;
            }
        }
        z = false;
        return z;
    }

    public void signIn(WifiEntry.SignInCallback signInCallback) {
        if (canSignIn()) {
            ((ConnectivityManager) this.mContext.getSystemService("connectivity")).startCaptivePortalApp(this.mWifiManager.getCurrentNetwork());
        }
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public synchronized int getMeteredChoice() {
        WifiConfiguration wifiConfiguration;
        if (!isSuggestion() && (wifiConfiguration = this.mTargetWifiConfig) != null) {
            int i = wifiConfiguration.meteredOverride;
            if (i == 1) {
                return 1;
            }
            if (i == 2) {
                return 2;
            }
        }
        return 0;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public boolean canSetMeteredChoice() {
        return getWifiConfiguration() != null;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public synchronized boolean isAutoJoinEnabled() {
        WifiConfiguration wifiConfiguration = this.mTargetWifiConfig;
        if (wifiConfiguration == null) {
            return false;
        }
        return wifiConfiguration.allowAutojoin;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public boolean canSetAutoJoinEnabled() {
        return isSaved() || isSuggestion();
    }

    public synchronized void updateScanResultInfo(List<ScanResult> list) throws IllegalArgumentException {
        if (list == null) {
            list = new ArrayList<>();
        }
        String ssid = this.mKey.getScanResultKey().getSsid();
        for (ScanResult scanResult : list) {
            if (!TextUtils.equals(scanResult.SSID, ssid)) {
                throw new IllegalArgumentException("Attempted to update with wrong SSID! Expected: " + ssid + ", Actual: " + scanResult.SSID + ", ScanResult: " + scanResult);
            }
        }
        this.mMatchingScanResults.clear();
        Set<Integer> securityTypes = this.mKey.getScanResultKey().getSecurityTypes();
        for (ScanResult scanResult2 : list) {
            for (Integer num : Utils.getSecurityTypesFromScanResult(scanResult2)) {
                int intValue = num.intValue();
                if (securityTypes.contains(Integer.valueOf(intValue)) && isSecurityTypeSupported(intValue)) {
                    if (!this.mMatchingScanResults.containsKey(Integer.valueOf(intValue))) {
                        this.mMatchingScanResults.put(Integer.valueOf(intValue), new ArrayList());
                    }
                    this.mMatchingScanResults.get(Integer.valueOf(intValue)).add(scanResult2);
                }
            }
        }
        updateSecurityTypes();
        updateTargetScanResultInfo();
        notifyOnUpdated();
    }

    private synchronized void updateTargetScanResultInfo() {
        ScanResult bestScanResultByLevel = Utils.getBestScanResultByLevel(this.mTargetScanResults);
        if (getConnectedState() == 0) {
            this.mLevel = bestScanResultByLevel != null ? this.mWifiManager.calculateSignalLevel(bestScanResultByLevel.level) : -1;
            this.mSpeed = Utils.getAverageSpeedFromScanResults(this.mScoreCache, this.mTargetScanResults);
        }
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public synchronized void updateNetworkCapabilities(NetworkCapabilities networkCapabilities) {
        super.updateNetworkCapabilities(networkCapabilities);
        if (canSignIn() && this.mShouldAutoOpenCaptivePortal) {
            this.mShouldAutoOpenCaptivePortal = false;
            signIn(null);
        }
    }

    public synchronized void onScoreCacheUpdated() {
        WifiInfo wifiInfo = this.mWifiInfo;
        if (wifiInfo != null) {
            this.mSpeed = Utils.getSpeedFromWifiInfo(this.mScoreCache, wifiInfo);
        } else {
            this.mSpeed = Utils.getAverageSpeedFromScanResults(this.mScoreCache, this.mTargetScanResults);
        }
        notifyOnUpdated();
    }

    public synchronized void updateConfig(List<WifiConfiguration> list) throws IllegalArgumentException {
        if (list == null) {
            list = Collections.emptyList();
        }
        ScanResultKey scanResultKey = this.mKey.getScanResultKey();
        String ssid = scanResultKey.getSsid();
        Set<Integer> securityTypes = scanResultKey.getSecurityTypes();
        this.mMatchingWifiConfigs.clear();
        for (WifiConfiguration wifiConfiguration : list) {
            if (TextUtils.equals(ssid, WifiInfo.sanitizeSsid(wifiConfiguration.SSID))) {
                for (Integer num : Utils.getSecurityTypesFromWifiConfiguration(wifiConfiguration)) {
                    int intValue = num.intValue();
                    if (!securityTypes.contains(Integer.valueOf(intValue))) {
                        throw new IllegalArgumentException("Attempted to update with wrong security! Expected one of: " + securityTypes + ", Actual: " + intValue + ", Config: " + wifiConfiguration);
                    } else if (isSecurityTypeSupported(intValue)) {
                        this.mMatchingWifiConfigs.put(Integer.valueOf(intValue), wifiConfiguration);
                    }
                }
            } else {
                throw new IllegalArgumentException("Attempted to update with wrong SSID! Expected: " + ssid + ", Actual: " + WifiInfo.sanitizeSsid(wifiConfiguration.SSID) + ", Config: " + wifiConfiguration);
            }
        }
        updateSecurityTypes();
        updateTargetScanResultInfo();
        notifyOnUpdated();
    }

    private boolean isSecurityTypeSupported(int i) {
        if (i == 4) {
            return this.mIsWpa3SaeSupported;
        }
        if (i == 5) {
            return this.mIsWpa3SuiteBSupported;
        }
        if (i != 6) {
            return true;
        }
        return this.mIsEnhancedOpenSupported;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    protected synchronized void updateSecurityTypes() {
        this.mTargetSecurityTypes.clear();
        WifiInfo wifiInfo = this.mWifiInfo;
        if (!(wifiInfo == null || wifiInfo.getCurrentSecurityType() == -1)) {
            this.mTargetSecurityTypes.add(Integer.valueOf(this.mWifiInfo.getCurrentSecurityType()));
        }
        Set<Integer> keySet = this.mMatchingWifiConfigs.keySet();
        if (this.mTargetSecurityTypes.isEmpty() && this.mKey.isTargetingNewNetworks()) {
            boolean z = false;
            Set<Integer> keySet2 = this.mMatchingScanResults.keySet();
            Iterator<Integer> it = keySet.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (keySet2.contains(Integer.valueOf(it.next().intValue()))) {
                        z = true;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (!z) {
                this.mTargetSecurityTypes.addAll(keySet2);
            }
        }
        if (this.mTargetSecurityTypes.isEmpty()) {
            this.mTargetSecurityTypes.addAll(keySet);
        }
        if (this.mTargetSecurityTypes.isEmpty()) {
            this.mTargetSecurityTypes.addAll(this.mKey.getScanResultKey().getSecurityTypes());
        }
        this.mTargetWifiConfig = this.mMatchingWifiConfigs.get(Integer.valueOf(Utils.getSingleSecurityTypeFromMultipleSecurityTypes(this.mTargetSecurityTypes)));
        ArraySet arraySet = new ArraySet();
        for (Integer num : this.mTargetSecurityTypes) {
            int intValue = num.intValue();
            if (this.mMatchingScanResults.containsKey(Integer.valueOf(intValue))) {
                arraySet.addAll(this.mMatchingScanResults.get(Integer.valueOf(intValue)));
            }
        }
        this.mTargetScanResults.clear();
        this.mTargetScanResults.addAll(arraySet);
    }

    public synchronized void setUserShareable(boolean z) {
        this.mIsUserShareable = z;
    }

    public synchronized boolean isUserShareable() {
        return this.mIsUserShareable;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    protected synchronized boolean connectionInfoMatches(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        if (!wifiInfo.isPasspointAp() && !wifiInfo.isOsuAp()) {
            for (WifiConfiguration wifiConfiguration : this.mMatchingWifiConfigs.values()) {
                if (wifiConfiguration.networkId == wifiInfo.getNetworkId()) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private synchronized void updateRecommendationServiceLabel() {
        NetworkScorerAppData activeScorer = ((NetworkScoreManager) this.mContext.getSystemService("network_score")).getActiveScorer();
        if (activeScorer != null) {
            this.mRecommendationServiceLabel = activeScorer.getRecommendationServiceLabel();
        }
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public synchronized String getScanResultDescription() {
        if (this.mTargetScanResults.size() == 0) {
            return "";
        }
        return "[" + getScanResultDescription(2400, 2500) + ";" + getScanResultDescription(4900, 5900) + ";" + getScanResultDescription(5925, 7125) + ";" + getScanResultDescription(58320, 70200) + "]";
    }

    private synchronized String getScanResultDescription(int i, int i2) {
        List list = (List) this.mTargetScanResults.stream().filter(new Predicate(i, i2) { // from class: com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda3
            public final /* synthetic */ int f$0;
            public final /* synthetic */ int f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return StandardWifiEntry.$r8$lambda$rcjRehuinsPpZzXKNaCQkf1mF90(this.f$0, this.f$1, (ScanResult) obj);
            }
        }).sorted(Comparator.comparingInt(StandardWifiEntry$$ExternalSyntheticLambda5.INSTANCE)).collect(Collectors.toList());
        int size = list.size();
        if (size == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(size);
        sb.append(")");
        if (size > 4) {
            int asInt = list.stream().mapToInt(StandardWifiEntry$$ExternalSyntheticLambda4.INSTANCE).max().getAsInt();
            sb.append("max=");
            sb.append(asInt);
            sb.append(",");
        }
        list.forEach(new Consumer(sb, SystemClock.elapsedRealtime()) { // from class: com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda2
            public final /* synthetic */ StringBuilder f$1;
            public final /* synthetic */ long f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                StandardWifiEntry.m439$r8$lambda$ds6gYPm53zteeVIyrL6DFyEE54(StandardWifiEntry.this, this.f$1, this.f$2, (ScanResult) obj);
            }
        });
        return sb.toString();
    }

    public static /* synthetic */ boolean lambda$getScanResultDescription$3(int i, int i2, ScanResult scanResult) {
        int i3 = scanResult.frequency;
        return i3 >= i && i3 <= i2;
    }

    public static /* synthetic */ int lambda$getScanResultDescription$4(ScanResult scanResult) {
        return scanResult.level * -1;
    }

    public /* synthetic */ void lambda$getScanResultDescription$6(StringBuilder sb, long j, ScanResult scanResult) {
        sb.append(getScanResultDescription(scanResult, j));
    }

    private synchronized String getScanResultDescription(ScanResult scanResult, long j) {
        StringBuilder sb;
        sb = new StringBuilder();
        sb.append(" \n{");
        sb.append(scanResult.BSSID);
        WifiInfo wifiInfo = this.mWifiInfo;
        if (wifiInfo != null && scanResult.BSSID.equals(wifiInfo.getBSSID())) {
            sb.append("*");
        }
        sb.append("=");
        sb.append(scanResult.frequency);
        sb.append(",");
        sb.append(scanResult.level);
        sb.append(",");
        sb.append(((int) (j - (scanResult.timestamp / 1000))) / 1000);
        sb.append("s");
        sb.append("}");
        return sb.toString();
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public String getNetworkSelectionDescription() {
        return Utils.getNetworkSelectionDescription(getWifiConfiguration());
    }

    /* loaded from: classes2.dex */
    public static class StandardWifiEntryKey {
        private boolean mIsNetworkRequest;
        private boolean mIsTargetingNewNetworks;
        private ScanResultKey mScanResultKey;
        private String mSuggestionProfileKey;

        public StandardWifiEntryKey(ScanResultKey scanResultKey, boolean z) {
            this.mIsTargetingNewNetworks = false;
            this.mScanResultKey = scanResultKey;
            this.mIsTargetingNewNetworks = z;
        }

        public StandardWifiEntryKey(WifiConfiguration wifiConfiguration) {
            this(wifiConfiguration, false);
        }

        public StandardWifiEntryKey(WifiConfiguration wifiConfiguration, boolean z) {
            this.mIsTargetingNewNetworks = false;
            this.mScanResultKey = new ScanResultKey(wifiConfiguration);
            if (wifiConfiguration.fromWifiNetworkSuggestion) {
                this.mSuggestionProfileKey = new StringJoiner(",").add(wifiConfiguration.creatorName).add(String.valueOf(wifiConfiguration.carrierId)).add(String.valueOf(wifiConfiguration.subscriptionId)).toString();
            } else if (wifiConfiguration.fromWifiNetworkSpecifier) {
                this.mIsNetworkRequest = true;
            }
            this.mIsTargetingNewNetworks = z;
        }

        public String toString() {
            JSONObject jSONObject = new JSONObject();
            try {
                ScanResultKey scanResultKey = this.mScanResultKey;
                if (scanResultKey != null) {
                    jSONObject.put("SCAN_RESULT_KEY", scanResultKey.toString());
                }
                String str = this.mSuggestionProfileKey;
                if (str != null) {
                    jSONObject.put("SUGGESTION_PROFILE_KEY", str);
                }
                boolean z = this.mIsNetworkRequest;
                if (z) {
                    jSONObject.put("IS_NETWORK_REQUEST", z);
                }
                boolean z2 = this.mIsTargetingNewNetworks;
                if (z2) {
                    jSONObject.put("IS_TARGETING_NEW_NETWORKS", z2);
                }
            } catch (JSONException e) {
                Log.wtf("StandardWifiEntry", "JSONException while converting StandardWifiEntryKey to string: " + e);
            }
            return "StandardWifiEntry:" + jSONObject.toString();
        }

        public ScanResultKey getScanResultKey() {
            return this.mScanResultKey;
        }

        public boolean isTargetingNewNetworks() {
            return this.mIsTargetingNewNetworks;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || StandardWifiEntryKey.class != obj.getClass()) {
                return false;
            }
            StandardWifiEntryKey standardWifiEntryKey = (StandardWifiEntryKey) obj;
            return Objects.equals(this.mScanResultKey, standardWifiEntryKey.mScanResultKey) && TextUtils.equals(this.mSuggestionProfileKey, standardWifiEntryKey.mSuggestionProfileKey) && this.mIsNetworkRequest == standardWifiEntryKey.mIsNetworkRequest;
        }

        public int hashCode() {
            return Objects.hash(this.mScanResultKey, this.mSuggestionProfileKey, Boolean.valueOf(this.mIsNetworkRequest));
        }
    }

    /* loaded from: classes2.dex */
    public static class ScanResultKey {
        private Set<Integer> mSecurityTypes;
        private String mSsid;

        ScanResultKey() {
            this.mSecurityTypes = new ArraySet();
        }

        ScanResultKey(String str, List<Integer> list) {
            this.mSecurityTypes = new ArraySet();
            this.mSsid = str;
            for (Integer num : list) {
                int intValue = num.intValue();
                this.mSecurityTypes.add(Integer.valueOf(intValue));
                if (intValue == 0) {
                    this.mSecurityTypes.add(6);
                } else if (intValue == 6) {
                    this.mSecurityTypes.add(0);
                } else if (intValue == 9) {
                    this.mSecurityTypes.add(3);
                } else if (intValue == 2) {
                    this.mSecurityTypes.add(4);
                } else if (intValue == 3) {
                    this.mSecurityTypes.add(9);
                } else if (intValue == 4) {
                    this.mSecurityTypes.add(2);
                }
            }
        }

        public ScanResultKey(ScanResult scanResult) {
            this(scanResult.SSID, Utils.getSecurityTypesFromScanResult(scanResult));
        }

        ScanResultKey(WifiConfiguration wifiConfiguration) {
            this(WifiInfo.sanitizeSsid(wifiConfiguration.SSID), Utils.getSecurityTypesFromWifiConfiguration(wifiConfiguration));
        }

        public String toString() {
            JSONObject jSONObject = new JSONObject();
            try {
                String str = this.mSsid;
                if (str != null) {
                    jSONObject.put("SSID", str);
                }
                if (!this.mSecurityTypes.isEmpty()) {
                    JSONArray jSONArray = new JSONArray();
                    for (Integer num : this.mSecurityTypes) {
                        jSONArray.put(num.intValue());
                    }
                    jSONObject.put("SECURITY_TYPES", jSONArray);
                }
            } catch (JSONException e) {
                Log.e("StandardWifiEntry", "JSONException while converting ScanResultKey to string: " + e);
            }
            return jSONObject.toString();
        }

        String getSsid() {
            return this.mSsid;
        }

        Set<Integer> getSecurityTypes() {
            return this.mSecurityTypes;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || ScanResultKey.class != obj.getClass()) {
                return false;
            }
            ScanResultKey scanResultKey = (ScanResultKey) obj;
            return TextUtils.equals(this.mSsid, scanResultKey.mSsid) && this.mSecurityTypes.equals(scanResultKey.mSecurityTypes);
        }

        public int hashCode() {
            return Objects.hash(this.mSsid, this.mSecurityTypes);
        }
    }
}
