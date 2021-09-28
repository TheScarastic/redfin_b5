package com.android.wifitrackerlib;

import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import androidx.core.util.Preconditions;
import com.android.wifitrackerlib.BaseWifiTracker;
import com.android.wifitrackerlib.SavedNetworkTracker;
import com.android.wifitrackerlib.StandardWifiEntry;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
/* loaded from: classes2.dex */
public class SavedNetworkTracker extends BaseWifiTracker {
    private final SavedNetworkTrackerCallback mListener;
    private final Object mLock;
    private final Map<String, PasspointWifiEntry> mPasspointWifiEntryCache;
    private final List<WifiEntry> mSavedWifiEntries;
    private final List<StandardWifiEntry> mStandardWifiEntryCache;
    private final List<WifiEntry> mSubscriptionWifiEntries;

    /* loaded from: classes2.dex */
    public interface SavedNetworkTrackerCallback extends BaseWifiTracker.BaseWifiTrackerCallback {
        void onSavedWifiEntriesChanged();

        void onSubscriptionWifiEntriesChanged();
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    protected void handleOnStart() {
        updateStandardWifiEntryConfigs(this.mWifiManager.getConfiguredNetworks());
        updatePasspointWifiEntryConfigs(this.mWifiManager.getPasspointConfigurations());
        conditionallyUpdateScanResults(true);
        updateSavedWifiEntries();
        updateSubscriptionWifiEntries();
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    protected void handleWifiStateChangedAction() {
        conditionallyUpdateScanResults(true);
        updateSavedWifiEntries();
        updateSubscriptionWifiEntries();
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    protected void handleScanResultsAvailableAction(Intent intent) {
        Preconditions.checkNotNull(intent, "Intent cannot be null!");
        conditionallyUpdateScanResults(intent.getBooleanExtra("resultsUpdated", true));
        updateSavedWifiEntries();
        updateSubscriptionWifiEntries();
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    protected void handleConfiguredNetworksChangedAction(Intent intent) {
        Preconditions.checkNotNull(intent, "Intent cannot be null!");
        updateStandardWifiEntryConfigs(this.mWifiManager.getConfiguredNetworks());
        updatePasspointWifiEntryConfigs(this.mWifiManager.getPasspointConfigurations());
        updateSavedWifiEntries();
        updateSubscriptionWifiEntries();
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker
    protected void handleNetworkScoreCacheUpdated() {
        for (StandardWifiEntry standardWifiEntry : this.mStandardWifiEntryCache) {
            standardWifiEntry.onScoreCacheUpdated();
        }
        for (PasspointWifiEntry passpointWifiEntry : this.mPasspointWifiEntryCache.values()) {
            passpointWifiEntry.onScoreCacheUpdated();
        }
    }

    private void updateSavedWifiEntries() {
        synchronized (this.mLock) {
            this.mSavedWifiEntries.clear();
            this.mSavedWifiEntries.addAll(this.mStandardWifiEntryCache);
            Collections.sort(this.mSavedWifiEntries);
            if (BaseWifiTracker.isVerboseLoggingEnabled()) {
                Log.v("SavedNetworkTracker", "Updated SavedWifiEntries: " + Arrays.toString(this.mSavedWifiEntries.toArray()));
            }
        }
        notifyOnSavedWifiEntriesChanged();
    }

    private void updateSubscriptionWifiEntries() {
        synchronized (this.mLock) {
            this.mSubscriptionWifiEntries.clear();
            this.mSubscriptionWifiEntries.addAll(this.mPasspointWifiEntryCache.values());
            Collections.sort(this.mSubscriptionWifiEntries);
            if (BaseWifiTracker.isVerboseLoggingEnabled()) {
                Log.v("SavedNetworkTracker", "Updated SubscriptionWifiEntries: " + Arrays.toString(this.mSubscriptionWifiEntries.toArray()));
            }
        }
        notifyOnSubscriptionWifiEntriesChanged();
    }

    private void updateStandardWifiEntryScans(List<ScanResult> list) {
        Preconditions.checkNotNull(list, "Scan Result list should not be null!");
        this.mStandardWifiEntryCache.forEach(new Consumer((Map) list.stream().collect(Collectors.groupingBy(SavedNetworkTracker$$ExternalSyntheticLambda4.INSTANCE))) { // from class: com.android.wifitrackerlib.SavedNetworkTracker$$ExternalSyntheticLambda2
            public final /* synthetic */ Map f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                SavedNetworkTracker.lambda$updateStandardWifiEntryScans$0(this.f$0, (StandardWifiEntry) obj);
            }
        });
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateStandardWifiEntryScans$0(Map map, StandardWifiEntry standardWifiEntry) {
        standardWifiEntry.updateScanResultInfo((List) map.get(standardWifiEntry.getStandardWifiEntryKey().getScanResultKey()));
    }

    private void updatePasspointWifiEntryScans(List<ScanResult> list) {
        Preconditions.checkNotNull(list, "Scan Result list should not be null!");
        TreeSet treeSet = new TreeSet();
        for (Pair pair : this.mWifiManager.getAllMatchingWifiConfigs(list)) {
            WifiConfiguration wifiConfiguration = (WifiConfiguration) pair.first;
            String uniqueIdToPasspointWifiEntryKey = PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(wifiConfiguration.getKey());
            treeSet.add(uniqueIdToPasspointWifiEntryKey);
            if (this.mPasspointWifiEntryCache.containsKey(uniqueIdToPasspointWifiEntryKey)) {
                this.mPasspointWifiEntryCache.get(uniqueIdToPasspointWifiEntryKey).updateScanResultInfo(wifiConfiguration, (List) ((Map) pair.second).get(0), (List) ((Map) pair.second).get(1));
            }
        }
        for (PasspointWifiEntry passpointWifiEntry : this.mPasspointWifiEntryCache.values()) {
            if (!treeSet.contains(passpointWifiEntry.getKey())) {
                passpointWifiEntry.updateScanResultInfo(null, null, null);
            }
        }
    }

    private void conditionallyUpdateScanResults(boolean z) {
        if (this.mWifiManager.getWifiState() == 1) {
            updateStandardWifiEntryScans(Collections.emptyList());
            updatePasspointWifiEntryScans(Collections.emptyList());
            return;
        }
        long j = this.mMaxScanAgeMillis;
        if (z) {
            this.mScanResultUpdater.update(this.mWifiManager.getScanResults());
        } else {
            j += this.mScanIntervalMillis;
        }
        updateStandardWifiEntryScans(this.mScanResultUpdater.getScanResults(j));
        updatePasspointWifiEntryScans(this.mScanResultUpdater.getScanResults(j));
    }

    private void updateStandardWifiEntryConfigs(List<WifiConfiguration> list) {
        Preconditions.checkNotNull(list, "Config list should not be null!");
        Map map = (Map) list.stream().filter(SavedNetworkTracker$$ExternalSyntheticLambda8.INSTANCE).collect(Collectors.groupingBy(SavedNetworkTracker$$ExternalSyntheticLambda5.INSTANCE));
        this.mStandardWifiEntryCache.removeIf(new Predicate(map) { // from class: com.android.wifitrackerlib.SavedNetworkTracker$$ExternalSyntheticLambda6
            public final /* synthetic */ Map f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return SavedNetworkTracker.lambda$updateStandardWifiEntryConfigs$2(this.f$0, (StandardWifiEntry) obj);
            }
        });
        for (StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey : map.keySet()) {
            this.mStandardWifiEntryCache.add(new StandardWifiEntry(this.mContext, this.mMainHandler, standardWifiEntryKey, (List) map.get(standardWifiEntryKey), null, this.mWifiManager, this.mWifiNetworkScoreCache, true));
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateStandardWifiEntryConfigs$1(WifiConfiguration wifiConfiguration) {
        return !wifiConfiguration.carrierMerged;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateStandardWifiEntryConfigs$2(Map map, StandardWifiEntry standardWifiEntry) {
        standardWifiEntry.updateConfig((List) map.remove(standardWifiEntry.getStandardWifiEntryKey()));
        return !standardWifiEntry.isSaved();
    }

    private void updatePasspointWifiEntryConfigs(List<PasspointConfiguration> list) {
        Preconditions.checkNotNull(list, "Config list should not be null!");
        Map map = (Map) list.stream().collect(Collectors.toMap(SavedNetworkTracker$$ExternalSyntheticLambda3.INSTANCE, Function.identity()));
        this.mPasspointWifiEntryCache.entrySet().removeIf(new Predicate(map) { // from class: com.android.wifitrackerlib.SavedNetworkTracker$$ExternalSyntheticLambda7
            public final /* synthetic */ Map f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return SavedNetworkTracker.lambda$updatePasspointWifiEntryConfigs$4(this.f$0, (Map.Entry) obj);
            }
        });
        for (String str : map.keySet()) {
            this.mPasspointWifiEntryCache.put(str, new PasspointWifiEntry(this.mContext, this.mMainHandler, (PasspointConfiguration) map.get(str), this.mWifiManager, this.mWifiNetworkScoreCache, true));
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ String lambda$updatePasspointWifiEntryConfigs$3(PasspointConfiguration passpointConfiguration) {
        return PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(passpointConfiguration.getUniqueId());
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updatePasspointWifiEntryConfigs$4(Map map, Map.Entry entry) {
        PasspointWifiEntry passpointWifiEntry = (PasspointWifiEntry) entry.getValue();
        PasspointConfiguration passpointConfiguration = (PasspointConfiguration) map.remove(passpointWifiEntry.getKey());
        if (passpointConfiguration == null) {
            return true;
        }
        passpointWifiEntry.updatePasspointConfig(passpointConfiguration);
        return false;
    }

    private void notifyOnSavedWifiEntriesChanged() {
        SavedNetworkTrackerCallback savedNetworkTrackerCallback = this.mListener;
        if (savedNetworkTrackerCallback != null) {
            Handler handler = this.mMainHandler;
            Objects.requireNonNull(savedNetworkTrackerCallback);
            handler.post(new Runnable() { // from class: com.android.wifitrackerlib.SavedNetworkTracker$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SavedNetworkTracker.SavedNetworkTrackerCallback.this.onSavedWifiEntriesChanged();
                }
            });
        }
    }

    private void notifyOnSubscriptionWifiEntriesChanged() {
        SavedNetworkTrackerCallback savedNetworkTrackerCallback = this.mListener;
        if (savedNetworkTrackerCallback != null) {
            Handler handler = this.mMainHandler;
            Objects.requireNonNull(savedNetworkTrackerCallback);
            handler.post(new Runnable() { // from class: com.android.wifitrackerlib.SavedNetworkTracker$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    SavedNetworkTracker.SavedNetworkTrackerCallback.this.onSubscriptionWifiEntriesChanged();
                }
            });
        }
    }
}
