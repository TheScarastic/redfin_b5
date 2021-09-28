package com.android.settingslib.wifi;

import androidx.annotation.Keep;
/* loaded from: classes.dex */
public class WifiTrackerFactory {
    private static WifiTracker sTestingWifiTracker;

    @Keep
    public static void setTestingWifiTracker(WifiTracker wifiTracker) {
        sTestingWifiTracker = wifiTracker;
    }
}
