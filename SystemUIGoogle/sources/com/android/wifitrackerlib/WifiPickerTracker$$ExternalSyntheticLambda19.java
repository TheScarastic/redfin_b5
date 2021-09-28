package com.android.wifitrackerlib;

import android.net.wifi.ScanResult;
import java.util.function.Predicate;
/* loaded from: classes2.dex */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda19 implements Predicate {
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda19 INSTANCE = new WifiPickerTracker$$ExternalSyntheticLambda19();

    private /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda19() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return WifiPickerTracker.lambda$updateSuggestedWifiEntryScans$12((ScanResult) obj);
    }
}
