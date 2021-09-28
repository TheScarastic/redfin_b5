package com.android.wifitrackerlib;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkScoreCache;
import android.os.Handler;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wifitrackerlib.StandardWifiEntry;
import com.android.wifitrackerlib.WifiEntry;
@VisibleForTesting
/* loaded from: classes2.dex */
public class NetworkRequestEntry extends StandardWifiEntry {
    @Override // com.android.wifitrackerlib.StandardWifiEntry, com.android.wifitrackerlib.WifiEntry
    public boolean canSetAutoJoinEnabled() {
        return false;
    }

    @Override // com.android.wifitrackerlib.StandardWifiEntry, com.android.wifitrackerlib.WifiEntry
    public boolean canSetMeteredChoice() {
        return false;
    }

    @Override // com.android.wifitrackerlib.StandardWifiEntry, com.android.wifitrackerlib.WifiEntry
    public void connect(WifiEntry.ConnectCallback connectCallback) {
    }

    @Override // com.android.wifitrackerlib.StandardWifiEntry, com.android.wifitrackerlib.WifiEntry
    public int getMeteredChoice() {
        return 0;
    }

    @Override // com.android.wifitrackerlib.StandardWifiEntry, com.android.wifitrackerlib.WifiEntry
    public WifiConfiguration getWifiConfiguration() {
        return null;
    }

    @Override // com.android.wifitrackerlib.StandardWifiEntry, com.android.wifitrackerlib.WifiEntry
    public boolean isAutoJoinEnabled() {
        return true;
    }

    @Override // com.android.wifitrackerlib.StandardWifiEntry, com.android.wifitrackerlib.WifiEntry
    public boolean isMetered() {
        return false;
    }

    @Override // com.android.wifitrackerlib.StandardWifiEntry, com.android.wifitrackerlib.WifiEntry
    public boolean isSaved() {
        return false;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public boolean isSubscription() {
        return false;
    }

    @Override // com.android.wifitrackerlib.StandardWifiEntry, com.android.wifitrackerlib.WifiEntry
    public boolean isSuggestion() {
        return false;
    }

    /* access modifiers changed from: package-private */
    public NetworkRequestEntry(Context context, Handler handler, StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey, WifiManager wifiManager, WifiNetworkScoreCache wifiNetworkScoreCache, boolean z) throws IllegalArgumentException {
        super(context, handler, standardWifiEntryKey, wifiManager, wifiNetworkScoreCache, z);
    }
}
