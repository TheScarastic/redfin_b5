package com.android.wifitrackerlib;

import java.util.function.Predicate;
/* loaded from: classes2.dex */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda23 implements Predicate {
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda23 INSTANCE = new WifiPickerTracker$$ExternalSyntheticLambda23();

    private /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda23() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return WifiPickerTracker.lambda$updateWifiEntries$6((PasspointWifiEntry) obj);
    }
}
