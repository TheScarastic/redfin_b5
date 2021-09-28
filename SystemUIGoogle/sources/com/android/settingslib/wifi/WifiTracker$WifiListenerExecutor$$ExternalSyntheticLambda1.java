package com.android.settingslib.wifi;

import com.android.settingslib.wifi.WifiTracker;
/* loaded from: classes.dex */
public final /* synthetic */ class WifiTracker$WifiListenerExecutor$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ WifiTracker.WifiListener f$0;

    public /* synthetic */ WifiTracker$WifiListenerExecutor$$ExternalSyntheticLambda1(WifiTracker.WifiListener wifiListener) {
        this.f$0 = wifiListener;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.onConnectedChanged();
    }
}
