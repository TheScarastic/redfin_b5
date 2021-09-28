package com.android.wifitrackerlib;

import android.net.wifi.ScanResult;
import java.util.function.Predicate;
/* loaded from: classes2.dex */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda18 implements Predicate {
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda18 INSTANCE = new WifiPickerTracker$$ExternalSyntheticLambda18();

    private /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda18() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return WifiPickerTracker.lambda$updateStandardWifiEntryScans$9((ScanResult) obj);
    }
}
