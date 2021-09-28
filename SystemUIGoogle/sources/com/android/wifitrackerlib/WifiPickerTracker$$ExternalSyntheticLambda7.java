package com.android.wifitrackerlib;

import android.net.wifi.WifiConfiguration;
import java.util.function.Function;
/* loaded from: classes2.dex */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda7 implements Function {
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda7 INSTANCE = new WifiPickerTracker$$ExternalSyntheticLambda7();

    private /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda7() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return WifiPickerTracker.lambda$conditionallyCreateConnectedStandardWifiEntry$25((WifiConfiguration) obj);
    }
}
