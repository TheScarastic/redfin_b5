package com.android.wifitrackerlib;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkScoreManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.hotspot2.OsuProvider;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.os.Handler;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import androidx.core.util.Preconditions;
import androidx.lifecycle.Lifecycle;
import com.android.wifitrackerlib.BaseWifiTracker;
import com.android.wifitrackerlib.StandardWifiEntry;
import com.android.wifitrackerlib.WifiPickerTracker;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
/* loaded from: classes2.dex */
public class WifiPickerTracker extends BaseWifiTracker {
    private WifiEntry mConnectedWifiEntry;
    private NetworkInfo mCurrentNetworkInfo;
    private final WifiPickerTrackerCallback mListener;
    private MergedCarrierEntry mMergedCarrierEntry;
    private NetworkRequestEntry mNetworkRequestEntry;
    private int mNumSavedNetworks;
    private final Object mLock = new Object();
    private final List<WifiEntry> mWifiEntries = new ArrayList();
    private final Map<StandardWifiEntry.StandardWifiEntryKey, List<WifiConfiguration>> mStandardWifiConfigCache = new ArrayMap();
    private final Map<StandardWifiEntry.StandardWifiEntryKey, List<WifiConfiguration>> mSuggestedConfigCache = new ArrayMap();
    private final ArrayMap<StandardWifiEntry.StandardWifiEntryKey, List<WifiConfiguration>> mNetworkRequestConfigCache = new ArrayMap<>();
    private final List<StandardWifiEntry> mStandardWifiEntryCache = new ArrayList();
    private final List<StandardWifiEntry> mSuggestedWifiEntryCache = new ArrayList();
    private final Map<String, PasspointConfiguration> mPasspointConfigCache = new ArrayMap();
    private final SparseArray<WifiConfiguration> mPasspointWifiConfigCache = new SparseArray<>();
    private final Map<String, PasspointWifiEntry> mPasspointWifiEntryCache = new ArrayMap();
    private final Map<String, OsuWifiEntry> mOsuWifiEntryCache = new ArrayMap();

    /* loaded from: classes2.dex */
    public interface WifiPickerTrackerCallback extends BaseWifiTracker.BaseWifiTrackerCallback {
        void onNumSavedNetworksChanged();

        void onNumSavedSubscriptionsChanged();

        void onWifiEntriesChanged();
    }

    protected void updateContextualWifiEntryScans(List<ScanResult> list) {
    }

    public WifiPickerTracker(Lifecycle lifecycle, Context context, WifiManager wifiManager, ConnectivityManager connectivityManager, NetworkScoreManager networkScoreManager, Handler handler, Handler handler2, Clock clock, long j, long j2, WifiPickerTrackerCallback wifiPickerTrackerCallback) {
        super(lifecycle, context, wifiManager, connectivityManager, networkScoreManager, handler, handler2, clock, j, j2, wifiPickerTrackerCallback, "WifiPickerTracker");
        this.mListener = wifiPickerTrackerCallback;
    }

    public WifiEntry getConnectedWifiEntry() {
        return this.mConnectedWifiEntry;
    }

    public List<WifiEntry> getWifiEntries() {
        ArrayList arrayList;
        synchronized (this.mLock) {
            arrayList = new ArrayList(this.mWifiEntries);
        }
        return arrayList;
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    protected void handleOnStart() {
        updateWifiConfigurations(this.mWifiManager.getPrivilegedConfiguredNetworks());
        updatePasspointConfigurations(this.mWifiManager.getPasspointConfigurations());
        this.mScanResultUpdater.update(this.mWifiManager.getScanResults());
        conditionallyUpdateScanResults(true);
        WifiInfo connectionInfo = this.mWifiManager.getConnectionInfo();
        Network currentNetwork = this.mWifiManager.getCurrentNetwork();
        NetworkInfo networkInfo = this.mConnectivityManager.getNetworkInfo(currentNetwork);
        this.mCurrentNetworkInfo = networkInfo;
        updateConnectionInfo(connectionInfo, networkInfo);
        notifyOnNumSavedNetworksChanged();
        notifyOnNumSavedSubscriptionsChanged();
        handleDefaultSubscriptionChanged(SubscriptionManager.getDefaultDataSubscriptionId());
        updateWifiEntries();
        handleNetworkCapabilitiesChanged(this.mConnectivityManager.getNetworkCapabilities(currentNetwork));
        handleLinkPropertiesChanged(this.mConnectivityManager.getLinkProperties(currentNetwork));
        handleDefaultRouteChanged();
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    protected void handleWifiStateChangedAction() {
        conditionallyUpdateScanResults(true);
        if (this.mWifiManager.getWifiState() != 3) {
            updateConnectionInfo(null, null);
        }
        updateWifiEntries();
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    protected void handleScanResultsAvailableAction(Intent intent) {
        Preconditions.checkNotNull(intent, "Intent cannot be null!");
        conditionallyUpdateScanResults(intent.getBooleanExtra("resultsUpdated", true));
        updateWifiEntries();
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    protected void handleConfiguredNetworksChangedAction(Intent intent) {
        Preconditions.checkNotNull(intent, "Intent cannot be null!");
        processConfiguredNetworksChanged();
    }

    protected void processConfiguredNetworksChanged() {
        updateWifiConfigurations(this.mWifiManager.getPrivilegedConfiguredNetworks());
        updatePasspointConfigurations(this.mWifiManager.getPasspointConfigurations());
        List<ScanResult> scanResults = this.mScanResultUpdater.getScanResults();
        updateStandardWifiEntryScans(scanResults);
        updateNetworkRequestEntryScans(scanResults);
        updatePasspointWifiEntryScans(scanResults);
        updateOsuWifiEntryScans(scanResults);
        notifyOnNumSavedNetworksChanged();
        notifyOnNumSavedSubscriptionsChanged();
        updateWifiEntries();
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    protected void handleNetworkStateChangedAction(Intent intent) {
        Preconditions.checkNotNull(intent, "Intent cannot be null!");
        this.mCurrentNetworkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
        updateConnectionInfo(this.mWifiManager.getConnectionInfo(), this.mCurrentNetworkInfo);
        updateWifiEntries();
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    protected void handleRssiChangedAction() {
        WifiInfo connectionInfo = this.mWifiManager.getConnectionInfo();
        WifiEntry wifiEntry = this.mConnectedWifiEntry;
        if (wifiEntry != null) {
            wifiEntry.updateConnectionInfo(connectionInfo, this.mCurrentNetworkInfo);
        }
        MergedCarrierEntry mergedCarrierEntry = this.mMergedCarrierEntry;
        if (mergedCarrierEntry != null) {
            mergedCarrierEntry.updateConnectionInfo(connectionInfo, this.mCurrentNetworkInfo);
        }
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    protected void handleLinkPropertiesChanged(LinkProperties linkProperties) {
        WifiEntry wifiEntry = this.mConnectedWifiEntry;
        if (wifiEntry != null && wifiEntry.getConnectedState() == 2) {
            this.mConnectedWifiEntry.updateLinkProperties(linkProperties);
        }
        MergedCarrierEntry mergedCarrierEntry = this.mMergedCarrierEntry;
        if (mergedCarrierEntry != null) {
            mergedCarrierEntry.updateLinkProperties(linkProperties);
        }
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    protected void handleNetworkCapabilitiesChanged(NetworkCapabilities networkCapabilities) {
        WifiEntry wifiEntry = this.mConnectedWifiEntry;
        if (wifiEntry != null && wifiEntry.getConnectedState() == 2) {
            this.mConnectedWifiEntry.updateNetworkCapabilities(networkCapabilities);
            this.mConnectedWifiEntry.setIsLowQuality(this.mIsWifiValidated && this.mIsCellDefaultRoute);
        }
        MergedCarrierEntry mergedCarrierEntry = this.mMergedCarrierEntry;
        if (mergedCarrierEntry != null) {
            mergedCarrierEntry.updateNetworkCapabilities(networkCapabilities);
        }
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    protected void handleDefaultRouteChanged() {
        WifiEntry wifiEntry = this.mConnectedWifiEntry;
        if (wifiEntry != null) {
            wifiEntry.setIsDefaultNetwork(this.mIsWifiDefaultRoute);
            this.mConnectedWifiEntry.setIsLowQuality(this.mIsWifiValidated && this.mIsCellDefaultRoute);
        }
        MergedCarrierEntry mergedCarrierEntry = this.mMergedCarrierEntry;
        if (mergedCarrierEntry != null) {
            if (mergedCarrierEntry.getConnectedState() == 2) {
                this.mMergedCarrierEntry.setIsDefaultNetwork(this.mIsWifiDefaultRoute);
            }
            this.mMergedCarrierEntry.updateIsCellDefaultRoute(this.mIsCellDefaultRoute);
        }
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    protected void handleNetworkScoreCacheUpdated() {
        for (StandardWifiEntry standardWifiEntry : this.mStandardWifiEntryCache) {
            standardWifiEntry.onScoreCacheUpdated();
        }
        for (StandardWifiEntry standardWifiEntry2 : this.mSuggestedWifiEntryCache) {
            standardWifiEntry2.onScoreCacheUpdated();
        }
        for (PasspointWifiEntry passpointWifiEntry : this.mPasspointWifiEntryCache.values()) {
            passpointWifiEntry.onScoreCacheUpdated();
        }
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    protected void handleDefaultSubscriptionChanged(int i) {
        updateMergedCarrierEntry(i);
    }

    protected void updateWifiEntries() {
        NetworkRequestEntry networkRequestEntry;
        synchronized (this.mLock) {
            StandardWifiEntry orElse = this.mStandardWifiEntryCache.stream().filter(WifiPickerTracker$$ExternalSyntheticLambda25.INSTANCE).findAny().orElse(null);
            this.mConnectedWifiEntry = orElse;
            if (orElse == null) {
                this.mConnectedWifiEntry = this.mSuggestedWifiEntryCache.stream().filter(WifiPickerTracker$$ExternalSyntheticLambda26.INSTANCE).findAny().orElse(null);
            }
            if (this.mConnectedWifiEntry == null) {
                this.mConnectedWifiEntry = this.mPasspointWifiEntryCache.values().stream().filter(WifiPickerTracker$$ExternalSyntheticLambda22.INSTANCE).findAny().orElse(null);
            }
            if (!(this.mConnectedWifiEntry != null || (networkRequestEntry = this.mNetworkRequestEntry) == null || networkRequestEntry.getConnectedState() == 0)) {
                this.mConnectedWifiEntry = this.mNetworkRequestEntry;
            }
            this.mWifiEntries.clear();
            Set set = (Set) this.mSuggestedWifiEntryCache.stream().filter(new Predicate() { // from class: com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda14
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return WifiPickerTracker.this.lambda$updateWifiEntries$3((StandardWifiEntry) obj);
                }
            }).map(WifiPickerTracker$$ExternalSyntheticLambda10.INSTANCE).collect(Collectors.toSet());
            for (StandardWifiEntry standardWifiEntry : this.mStandardWifiEntryCache) {
                if (standardWifiEntry != this.mConnectedWifiEntry && (standardWifiEntry.isSaved() || !set.contains(standardWifiEntry.getStandardWifiEntryKey().getScanResultKey()))) {
                    this.mWifiEntries.add(standardWifiEntry);
                }
            }
            this.mWifiEntries.addAll((Collection) this.mSuggestedWifiEntryCache.stream().filter(WifiPickerTracker$$ExternalSyntheticLambda28.INSTANCE).collect(Collectors.toList()));
            this.mWifiEntries.addAll((Collection) this.mPasspointWifiEntryCache.values().stream().filter(WifiPickerTracker$$ExternalSyntheticLambda23.INSTANCE).collect(Collectors.toList()));
            this.mWifiEntries.addAll((Collection) this.mOsuWifiEntryCache.values().stream().filter(WifiPickerTracker$$ExternalSyntheticLambda21.INSTANCE).collect(Collectors.toList()));
            this.mWifiEntries.addAll((Collection) getContextualWifiEntries().stream().filter(WifiPickerTracker$$ExternalSyntheticLambda29.INSTANCE).collect(Collectors.toList()));
            Collections.sort(this.mWifiEntries);
            if (BaseWifiTracker.isVerboseLoggingEnabled()) {
                Log.v("WifiPickerTracker", "Connected WifiEntry: " + this.mConnectedWifiEntry);
                Log.v("WifiPickerTracker", "Updated WifiEntries: " + Arrays.toString(this.mWifiEntries.toArray()));
            }
        }
        notifyOnWifiEntriesChanged();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateWifiEntries$0(StandardWifiEntry standardWifiEntry) {
        int connectedState = standardWifiEntry.getConnectedState();
        return connectedState == 2 || connectedState == 1;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateWifiEntries$1(StandardWifiEntry standardWifiEntry) {
        int connectedState = standardWifiEntry.getConnectedState();
        return connectedState == 2 || connectedState == 1;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateWifiEntries$2(PasspointWifiEntry passpointWifiEntry) {
        int connectedState = passpointWifiEntry.getConnectedState();
        return connectedState == 2 || connectedState == 1;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateWifiEntries$3(StandardWifiEntry standardWifiEntry) {
        return standardWifiEntry.isUserShareable() || standardWifiEntry == this.mConnectedWifiEntry;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ StandardWifiEntry.ScanResultKey lambda$updateWifiEntries$4(StandardWifiEntry standardWifiEntry) {
        return standardWifiEntry.getStandardWifiEntryKey().getScanResultKey();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateWifiEntries$5(StandardWifiEntry standardWifiEntry) {
        return standardWifiEntry.getConnectedState() == 0 && standardWifiEntry.isUserShareable();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateWifiEntries$6(PasspointWifiEntry passpointWifiEntry) {
        return passpointWifiEntry.getConnectedState() == 0;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateWifiEntries$7(OsuWifiEntry osuWifiEntry) {
        return osuWifiEntry.getConnectedState() == 0 && !osuWifiEntry.isAlreadyProvisioned();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateWifiEntries$8(WifiEntry wifiEntry) {
        return wifiEntry.getConnectedState() == 0;
    }

    private void updateMergedCarrierEntry(int i) {
        if (i != -1) {
            MergedCarrierEntry mergedCarrierEntry = this.mMergedCarrierEntry;
            if (mergedCarrierEntry == null || i != mergedCarrierEntry.getSubscriptionId()) {
                MergedCarrierEntry mergedCarrierEntry2 = new MergedCarrierEntry(this.mWorkerHandler, this.mWifiManager, this.mWifiNetworkScoreCache, false, this.mContext, i);
                this.mMergedCarrierEntry = mergedCarrierEntry2;
                mergedCarrierEntry2.updateConnectionInfo(this.mWifiManager.getConnectionInfo(), this.mCurrentNetworkInfo);
            } else {
                return;
            }
        } else if (this.mMergedCarrierEntry != null) {
            this.mMergedCarrierEntry = null;
        } else {
            return;
        }
        notifyOnWifiEntriesChanged();
    }

    protected List<WifiEntry> getContextualWifiEntries() {
        return Collections.emptyList();
    }

    private void updateStandardWifiEntryScans(List<ScanResult> list) {
        Preconditions.checkNotNull(list, "Scan Result list should not be null!");
        Map map = (Map) list.stream().filter(WifiPickerTracker$$ExternalSyntheticLambda18.INSTANCE).collect(Collectors.groupingBy(SavedNetworkTracker$$ExternalSyntheticLambda4.INSTANCE));
        ArraySet<StandardWifiEntry.ScanResultKey> arraySet = new ArraySet(map.keySet());
        this.mStandardWifiEntryCache.forEach(new Consumer(arraySet, map) { // from class: com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda5
            public final /* synthetic */ Set f$0;
            public final /* synthetic */ Map f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                WifiPickerTracker.lambda$updateStandardWifiEntryScans$10(this.f$0, this.f$1, (StandardWifiEntry) obj);
            }
        });
        for (StandardWifiEntry.ScanResultKey scanResultKey : arraySet) {
            StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey = new StandardWifiEntry.StandardWifiEntryKey(scanResultKey, true);
            this.mStandardWifiEntryCache.add(new StandardWifiEntry(this.mContext, this.mMainHandler, standardWifiEntryKey, this.mStandardWifiConfigCache.get(standardWifiEntryKey), (List) map.get(scanResultKey), this.mWifiManager, this.mWifiNetworkScoreCache, false));
        }
        this.mStandardWifiEntryCache.removeIf(WifiPickerTracker$$ExternalSyntheticLambda27.INSTANCE);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateStandardWifiEntryScans$9(ScanResult scanResult) {
        return !TextUtils.isEmpty(scanResult.SSID);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateStandardWifiEntryScans$10(Set set, Map map, StandardWifiEntry standardWifiEntry) {
        StandardWifiEntry.ScanResultKey scanResultKey = standardWifiEntry.getStandardWifiEntryKey().getScanResultKey();
        set.remove(scanResultKey);
        standardWifiEntry.updateScanResultInfo((List) map.get(scanResultKey));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateStandardWifiEntryScans$11(StandardWifiEntry standardWifiEntry) {
        return standardWifiEntry.getLevel() == -1;
    }

    private void updateSuggestedWifiEntryScans(List<ScanResult> list) {
        Preconditions.checkNotNull(list, "Scan Result list should not be null!");
        Map map = (Map) list.stream().filter(WifiPickerTracker$$ExternalSyntheticLambda19.INSTANCE).collect(Collectors.groupingBy(SavedNetworkTracker$$ExternalSyntheticLambda4.INSTANCE));
        ArraySet arraySet = new ArraySet();
        this.mSuggestedWifiEntryCache.forEach(new Consumer(arraySet, map) { // from class: com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda6
            public final /* synthetic */ Set f$0;
            public final /* synthetic */ Map f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                WifiPickerTracker.lambda$updateSuggestedWifiEntryScans$13(this.f$0, this.f$1, (StandardWifiEntry) obj);
            }
        });
        Set set = (Set) this.mWifiManager.getWifiConfigForMatchedNetworkSuggestionsSharedWithUser(list).stream().map(SavedNetworkTracker$$ExternalSyntheticLambda5.INSTANCE).collect(Collectors.toSet());
        for (StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey : this.mSuggestedConfigCache.keySet()) {
            StandardWifiEntry.ScanResultKey scanResultKey = standardWifiEntryKey.getScanResultKey();
            if (!arraySet.contains(standardWifiEntryKey) && map.containsKey(scanResultKey)) {
                StandardWifiEntry standardWifiEntry = new StandardWifiEntry(this.mContext, this.mMainHandler, standardWifiEntryKey, this.mSuggestedConfigCache.get(standardWifiEntryKey), (List) map.get(scanResultKey), this.mWifiManager, this.mWifiNetworkScoreCache, false);
                standardWifiEntry.setUserShareable(set.contains(standardWifiEntryKey));
                this.mSuggestedWifiEntryCache.add(standardWifiEntry);
            }
        }
        this.mSuggestedWifiEntryCache.removeIf(WifiPickerTracker$$ExternalSyntheticLambda24.INSTANCE);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateSuggestedWifiEntryScans$12(ScanResult scanResult) {
        return !TextUtils.isEmpty(scanResult.SSID);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateSuggestedWifiEntryScans$13(Set set, Map map, StandardWifiEntry standardWifiEntry) {
        StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey = standardWifiEntry.getStandardWifiEntryKey();
        set.add(standardWifiEntryKey);
        standardWifiEntry.updateScanResultInfo((List) map.get(standardWifiEntryKey.getScanResultKey()));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateSuggestedWifiEntryScans$14(StandardWifiEntry standardWifiEntry) {
        return standardWifiEntry.getLevel() == -1;
    }

    private void updatePasspointWifiEntryScans(List<ScanResult> list) {
        Preconditions.checkNotNull(list, "Scan Result list should not be null!");
        TreeSet treeSet = new TreeSet();
        for (Pair pair : this.mWifiManager.getAllMatchingWifiConfigs(list)) {
            WifiConfiguration wifiConfiguration = (WifiConfiguration) pair.first;
            List<ScanResult> list2 = (List) ((Map) pair.second).get(0);
            List<ScanResult> list3 = (List) ((Map) pair.second).get(1);
            String uniqueIdToPasspointWifiEntryKey = PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(wifiConfiguration.getKey());
            treeSet.add(uniqueIdToPasspointWifiEntryKey);
            if (!this.mPasspointWifiEntryCache.containsKey(uniqueIdToPasspointWifiEntryKey)) {
                if (wifiConfiguration.fromWifiNetworkSuggestion) {
                    this.mPasspointWifiEntryCache.put(uniqueIdToPasspointWifiEntryKey, new PasspointWifiEntry(this.mContext, this.mMainHandler, wifiConfiguration, this.mWifiManager, this.mWifiNetworkScoreCache, false));
                } else if (this.mPasspointConfigCache.containsKey(uniqueIdToPasspointWifiEntryKey)) {
                    this.mPasspointWifiEntryCache.put(uniqueIdToPasspointWifiEntryKey, new PasspointWifiEntry(this.mContext, this.mMainHandler, this.mPasspointConfigCache.get(uniqueIdToPasspointWifiEntryKey), this.mWifiManager, this.mWifiNetworkScoreCache, false));
                }
            }
            this.mPasspointWifiEntryCache.get(uniqueIdToPasspointWifiEntryKey).updateScanResultInfo(wifiConfiguration, list2, list3);
        }
        this.mPasspointWifiEntryCache.entrySet().removeIf(new Predicate(treeSet) { // from class: com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda17
            public final /* synthetic */ Set f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return WifiPickerTracker.lambda$updatePasspointWifiEntryScans$15(this.f$0, (Map.Entry) obj);
            }
        });
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updatePasspointWifiEntryScans$15(Set set, Map.Entry entry) {
        return ((PasspointWifiEntry) entry.getValue()).getLevel() == -1 || (!set.contains(entry.getKey()) && ((PasspointWifiEntry) entry.getValue()).getConnectedState() == 0);
    }

    private void updateOsuWifiEntryScans(List<ScanResult> list) {
        Preconditions.checkNotNull(list, "Scan Result list should not be null!");
        Map matchingOsuProviders = this.mWifiManager.getMatchingOsuProviders(list);
        Map matchingPasspointConfigsForOsuProviders = this.mWifiManager.getMatchingPasspointConfigsForOsuProviders(matchingOsuProviders.keySet());
        for (OsuWifiEntry osuWifiEntry : this.mOsuWifiEntryCache.values()) {
            osuWifiEntry.updateScanResultInfo((List) matchingOsuProviders.remove(osuWifiEntry.getOsuProvider()));
        }
        for (OsuProvider osuProvider : matchingOsuProviders.keySet()) {
            OsuWifiEntry osuWifiEntry2 = new OsuWifiEntry(this.mContext, this.mMainHandler, osuProvider, this.mWifiManager, this.mWifiNetworkScoreCache, false);
            osuWifiEntry2.updateScanResultInfo((List) matchingOsuProviders.get(osuProvider));
            this.mOsuWifiEntryCache.put(OsuWifiEntry.osuProviderToOsuWifiEntryKey(osuProvider), osuWifiEntry2);
        }
        this.mOsuWifiEntryCache.values().forEach(new Consumer(matchingPasspointConfigsForOsuProviders) { // from class: com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda4
            public final /* synthetic */ Map f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                WifiPickerTracker.this.lambda$updateOsuWifiEntryScans$16(this.f$1, (OsuWifiEntry) obj);
            }
        });
        this.mOsuWifiEntryCache.entrySet().removeIf(WifiPickerTracker$$ExternalSyntheticLambda30.INSTANCE);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateOsuWifiEntryScans$16(Map map, OsuWifiEntry osuWifiEntry) {
        PasspointConfiguration passpointConfiguration = (PasspointConfiguration) map.get(osuWifiEntry.getOsuProvider());
        if (passpointConfiguration == null) {
            osuWifiEntry.setAlreadyProvisioned(false);
            return;
        }
        osuWifiEntry.setAlreadyProvisioned(true);
        PasspointWifiEntry passpointWifiEntry = this.mPasspointWifiEntryCache.get(PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(passpointConfiguration.getUniqueId()));
        if (passpointWifiEntry != null) {
            passpointWifiEntry.setOsuWifiEntry(osuWifiEntry);
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateOsuWifiEntryScans$17(Map.Entry entry) {
        return ((OsuWifiEntry) entry.getValue()).getLevel() == -1;
    }

    private void updateNetworkRequestEntryScans(List<ScanResult> list) {
        Preconditions.checkNotNull(list, "Scan Result list should not be null!");
        NetworkRequestEntry networkRequestEntry = this.mNetworkRequestEntry;
        if (networkRequestEntry != null) {
            StandardWifiEntry.ScanResultKey scanResultKey = networkRequestEntry.getStandardWifiEntryKey().getScanResultKey();
            this.mNetworkRequestEntry.updateScanResultInfo((List) list.stream().filter(new Predicate() { // from class: com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda13
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return WifiPickerTracker.lambda$updateNetworkRequestEntryScans$18(StandardWifiEntry.ScanResultKey.this, (ScanResult) obj);
                }
            }).collect(Collectors.toList()));
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateNetworkRequestEntryScans$18(StandardWifiEntry.ScanResultKey scanResultKey, ScanResult scanResult) {
        return scanResultKey.equals(new StandardWifiEntry.ScanResultKey(scanResult));
    }

    private void conditionallyUpdateScanResults(boolean z) {
        if (this.mWifiManager.getWifiState() == 1) {
            updateStandardWifiEntryScans(Collections.emptyList());
            updateSuggestedWifiEntryScans(Collections.emptyList());
            updatePasspointWifiEntryScans(Collections.emptyList());
            updateOsuWifiEntryScans(Collections.emptyList());
            updateNetworkRequestEntryScans(Collections.emptyList());
            updateContextualWifiEntryScans(Collections.emptyList());
            return;
        }
        long j = this.mMaxScanAgeMillis;
        if (z) {
            this.mScanResultUpdater.update(this.mWifiManager.getScanResults());
        } else {
            j += this.mScanIntervalMillis;
        }
        List<ScanResult> scanResults = this.mScanResultUpdater.getScanResults(j);
        updateStandardWifiEntryScans(scanResults);
        updateSuggestedWifiEntryScans(scanResults);
        updatePasspointWifiEntryScans(scanResults);
        updateOsuWifiEntryScans(scanResults);
        updateNetworkRequestEntryScans(scanResults);
        updateContextualWifiEntryScans(scanResults);
    }

    private void updateWifiConfigurations(List<WifiConfiguration> list) {
        Preconditions.checkNotNull(list, "Config list should not be null!");
        this.mStandardWifiConfigCache.clear();
        this.mSuggestedConfigCache.clear();
        this.mNetworkRequestConfigCache.clear();
        new ArrayList();
        for (WifiConfiguration wifiConfiguration : list) {
            if (!wifiConfiguration.carrierMerged) {
                StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey = new StandardWifiEntry.StandardWifiEntryKey(wifiConfiguration, true);
                if (wifiConfiguration.isPasspoint()) {
                    this.mPasspointWifiConfigCache.put(wifiConfiguration.networkId, wifiConfiguration);
                } else if (wifiConfiguration.fromWifiNetworkSuggestion) {
                    if (!this.mSuggestedConfigCache.containsKey(standardWifiEntryKey)) {
                        this.mSuggestedConfigCache.put(standardWifiEntryKey, new ArrayList());
                    }
                    this.mSuggestedConfigCache.get(standardWifiEntryKey).add(wifiConfiguration);
                } else if (wifiConfiguration.fromWifiNetworkSpecifier) {
                    if (!this.mNetworkRequestConfigCache.containsKey(standardWifiEntryKey)) {
                        this.mNetworkRequestConfigCache.put(standardWifiEntryKey, new ArrayList());
                    }
                    this.mNetworkRequestConfigCache.get(standardWifiEntryKey).add(wifiConfiguration);
                } else {
                    if (!this.mStandardWifiConfigCache.containsKey(standardWifiEntryKey)) {
                        this.mStandardWifiConfigCache.put(standardWifiEntryKey, new ArrayList());
                    }
                    this.mStandardWifiConfigCache.get(standardWifiEntryKey).add(wifiConfiguration);
                }
            }
        }
        this.mNumSavedNetworks = (int) this.mStandardWifiConfigCache.values().stream().flatMap(WifiPickerTracker$$ExternalSyntheticLambda11.INSTANCE).filter(WifiPickerTracker$$ExternalSyntheticLambda20.INSTANCE).map(WifiPickerTracker$$ExternalSyntheticLambda8.INSTANCE).distinct().count();
        this.mStandardWifiEntryCache.forEach(new Consumer() { // from class: com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                WifiPickerTracker.this.lambda$updateWifiConfigurations$21((StandardWifiEntry) obj);
            }
        });
        this.mSuggestedWifiEntryCache.removeIf(new Predicate() { // from class: com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda15
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return WifiPickerTracker.this.lambda$updateWifiConfigurations$22((StandardWifiEntry) obj);
            }
        });
        NetworkRequestEntry networkRequestEntry = this.mNetworkRequestEntry;
        if (networkRequestEntry != null) {
            networkRequestEntry.updateConfig(this.mNetworkRequestConfigCache.get(networkRequestEntry.getStandardWifiEntryKey()));
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateWifiConfigurations$19(WifiConfiguration wifiConfiguration) {
        return !wifiConfiguration.isEphemeral();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$updateWifiConfigurations$20(WifiConfiguration wifiConfiguration) {
        return Integer.valueOf(wifiConfiguration.networkId);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateWifiConfigurations$21(StandardWifiEntry standardWifiEntry) {
        standardWifiEntry.updateConfig(this.mStandardWifiConfigCache.get(standardWifiEntry.getStandardWifiEntryKey()));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateWifiConfigurations$22(StandardWifiEntry standardWifiEntry) {
        standardWifiEntry.updateConfig(this.mSuggestedConfigCache.get(standardWifiEntry.getStandardWifiEntryKey()));
        return !standardWifiEntry.isSuggestion();
    }

    private void updatePasspointConfigurations(List<PasspointConfiguration> list) {
        Preconditions.checkNotNull(list, "Config list should not be null!");
        this.mPasspointConfigCache.clear();
        this.mPasspointConfigCache.putAll((Map) list.stream().collect(Collectors.toMap(WifiPickerTracker$$ExternalSyntheticLambda9.INSTANCE, Function.identity())));
        this.mPasspointWifiEntryCache.entrySet().removeIf(new Predicate() { // from class: com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda16
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return WifiPickerTracker.this.lambda$updatePasspointConfigurations$24((Map.Entry) obj);
            }
        });
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ String lambda$updatePasspointConfigurations$23(PasspointConfiguration passpointConfiguration) {
        return PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(passpointConfiguration.getUniqueId());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updatePasspointConfigurations$24(Map.Entry entry) {
        PasspointWifiEntry passpointWifiEntry = (PasspointWifiEntry) entry.getValue();
        passpointWifiEntry.updatePasspointConfig(this.mPasspointConfigCache.get(passpointWifiEntry.getKey()));
        return !passpointWifiEntry.isSubscription() && !passpointWifiEntry.isSuggestion();
    }

    private void updateConnectionInfo(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        for (StandardWifiEntry standardWifiEntry : this.mStandardWifiEntryCache) {
            standardWifiEntry.updateConnectionInfo(wifiInfo, networkInfo);
        }
        for (StandardWifiEntry standardWifiEntry2 : this.mSuggestedWifiEntryCache) {
            standardWifiEntry2.updateConnectionInfo(wifiInfo, networkInfo);
        }
        for (PasspointWifiEntry passpointWifiEntry : this.mPasspointWifiEntryCache.values()) {
            passpointWifiEntry.updateConnectionInfo(wifiInfo, networkInfo);
        }
        for (OsuWifiEntry osuWifiEntry : this.mOsuWifiEntryCache.values()) {
            osuWifiEntry.updateConnectionInfo(wifiInfo, networkInfo);
        }
        NetworkRequestEntry networkRequestEntry = this.mNetworkRequestEntry;
        if (networkRequestEntry != null) {
            networkRequestEntry.updateConnectionInfo(wifiInfo, networkInfo);
        }
        updateNetworkRequestEntryConnectionInfo(wifiInfo, networkInfo);
        MergedCarrierEntry mergedCarrierEntry = this.mMergedCarrierEntry;
        if (mergedCarrierEntry != null) {
            mergedCarrierEntry.updateConnectionInfo(wifiInfo, networkInfo);
        }
        conditionallyCreateConnectedStandardWifiEntry(wifiInfo, networkInfo);
        conditionallyCreateConnectedSuggestedWifiEntry(wifiInfo, networkInfo);
        conditionallyCreateConnectedPasspointWifiEntry(wifiInfo, networkInfo);
    }

    private void updateNetworkRequestEntryConnectionInfo(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        ArrayList arrayList = new ArrayList();
        if (wifiInfo != null) {
            int i = 0;
            while (true) {
                if (i >= this.mNetworkRequestConfigCache.size()) {
                    break;
                }
                List<WifiConfiguration> valueAt = this.mNetworkRequestConfigCache.valueAt(i);
                if (!valueAt.isEmpty() && valueAt.get(0).networkId == wifiInfo.getNetworkId()) {
                    arrayList.addAll(valueAt);
                    break;
                }
                i++;
            }
        }
        if (arrayList.isEmpty()) {
            this.mNetworkRequestEntry = null;
            return;
        }
        StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey = new StandardWifiEntry.StandardWifiEntryKey((WifiConfiguration) arrayList.get(0));
        NetworkRequestEntry networkRequestEntry = this.mNetworkRequestEntry;
        if (networkRequestEntry == null || !networkRequestEntry.getStandardWifiEntryKey().equals(standardWifiEntryKey)) {
            NetworkRequestEntry networkRequestEntry2 = new NetworkRequestEntry(this.mContext, this.mMainHandler, standardWifiEntryKey, this.mWifiManager, this.mWifiNetworkScoreCache, false);
            this.mNetworkRequestEntry = networkRequestEntry2;
            networkRequestEntry2.updateConfig(arrayList);
        }
        this.mNetworkRequestEntry.updateConnectionInfo(wifiInfo, networkInfo);
    }

    private void conditionallyCreateConnectedStandardWifiEntry(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        if (!(wifiInfo == null || wifiInfo.isPasspointAp() || wifiInfo.isOsuAp())) {
            int networkId = wifiInfo.getNetworkId();
            for (List<WifiConfiguration> list : this.mStandardWifiConfigCache.values()) {
                if (list.stream().map(WifiPickerTracker$$ExternalSyntheticLambda7.INSTANCE).filter(new Predicate(networkId) { // from class: com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda12
                    public final /* synthetic */ int f$0;

                    {
                        this.f$0 = r1;
                    }

                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return WifiPickerTracker.lambda$conditionallyCreateConnectedStandardWifiEntry$26(this.f$0, (Integer) obj);
                    }
                }).count() != 0) {
                    StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey = new StandardWifiEntry.StandardWifiEntryKey(list.get(0), true);
                    for (StandardWifiEntry standardWifiEntry : this.mStandardWifiEntryCache) {
                        if (standardWifiEntryKey.equals(standardWifiEntry.getStandardWifiEntryKey())) {
                            return;
                        }
                    }
                    StandardWifiEntry standardWifiEntry2 = new StandardWifiEntry(this.mContext, this.mMainHandler, standardWifiEntryKey, list, null, this.mWifiManager, this.mWifiNetworkScoreCache, false);
                    standardWifiEntry2.updateConnectionInfo(wifiInfo, networkInfo);
                    this.mStandardWifiEntryCache.add(standardWifiEntry2);
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$conditionallyCreateConnectedStandardWifiEntry$25(WifiConfiguration wifiConfiguration) {
        return Integer.valueOf(wifiConfiguration.networkId);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$conditionallyCreateConnectedStandardWifiEntry$26(int i, Integer num) {
        return num.intValue() == i;
    }

    private void conditionallyCreateConnectedSuggestedWifiEntry(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        if (!(wifiInfo == null || wifiInfo.isPasspointAp() || wifiInfo.isOsuAp())) {
            int networkId = wifiInfo.getNetworkId();
            for (List<WifiConfiguration> list : this.mSuggestedConfigCache.values()) {
                if (!list.isEmpty() && list.get(0).networkId == networkId) {
                    StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey = new StandardWifiEntry.StandardWifiEntryKey(list.get(0), true);
                    for (StandardWifiEntry standardWifiEntry : this.mSuggestedWifiEntryCache) {
                        if (standardWifiEntryKey.equals(standardWifiEntry.getStandardWifiEntryKey())) {
                            return;
                        }
                    }
                    StandardWifiEntry standardWifiEntry2 = new StandardWifiEntry(this.mContext, this.mMainHandler, standardWifiEntryKey, list, null, this.mWifiManager, this.mWifiNetworkScoreCache, false);
                    standardWifiEntry2.updateConnectionInfo(wifiInfo, networkInfo);
                    this.mSuggestedWifiEntryCache.add(standardWifiEntry2);
                    return;
                }
            }
        }
    }

    private void conditionallyCreateConnectedPasspointWifiEntry(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        WifiConfiguration wifiConfiguration;
        PasspointWifiEntry passpointWifiEntry;
        if (wifiInfo != null && wifiInfo.isPasspointAp() && (wifiConfiguration = this.mPasspointWifiConfigCache.get(wifiInfo.getNetworkId())) != null) {
            if (!this.mPasspointWifiEntryCache.containsKey(PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(wifiConfiguration.getKey()))) {
                PasspointConfiguration passpointConfiguration = this.mPasspointConfigCache.get(PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(wifiConfiguration.getKey()));
                if (passpointConfiguration != null) {
                    passpointWifiEntry = new PasspointWifiEntry(this.mContext, this.mMainHandler, passpointConfiguration, this.mWifiManager, this.mWifiNetworkScoreCache, false);
                } else {
                    passpointWifiEntry = new PasspointWifiEntry(this.mContext, this.mMainHandler, wifiConfiguration, this.mWifiManager, this.mWifiNetworkScoreCache, false);
                }
                passpointWifiEntry.updateConnectionInfo(wifiInfo, networkInfo);
                this.mPasspointWifiEntryCache.put(passpointWifiEntry.getKey(), passpointWifiEntry);
            }
        }
    }

    private void notifyOnWifiEntriesChanged() {
        WifiPickerTrackerCallback wifiPickerTrackerCallback = this.mListener;
        if (wifiPickerTrackerCallback != null) {
            Handler handler = this.mMainHandler;
            Objects.requireNonNull(wifiPickerTrackerCallback);
            handler.post(new Runnable() { // from class: com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    WifiPickerTracker.WifiPickerTrackerCallback.this.onWifiEntriesChanged();
                }
            });
        }
    }

    private void notifyOnNumSavedNetworksChanged() {
        WifiPickerTrackerCallback wifiPickerTrackerCallback = this.mListener;
        if (wifiPickerTrackerCallback != null) {
            Handler handler = this.mMainHandler;
            Objects.requireNonNull(wifiPickerTrackerCallback);
            handler.post(new Runnable() { // from class: com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    WifiPickerTracker.WifiPickerTrackerCallback.this.onNumSavedNetworksChanged();
                }
            });
        }
    }

    private void notifyOnNumSavedSubscriptionsChanged() {
        WifiPickerTrackerCallback wifiPickerTrackerCallback = this.mListener;
        if (wifiPickerTrackerCallback != null) {
            Handler handler = this.mMainHandler;
            Objects.requireNonNull(wifiPickerTrackerCallback);
            handler.post(new Runnable() { // from class: com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    WifiPickerTracker.WifiPickerTrackerCallback.this.onNumSavedSubscriptionsChanged();
                }
            });
        }
    }
}
