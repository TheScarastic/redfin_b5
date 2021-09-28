package com.android.wifitrackerlib;

import java.util.function.Predicate;
/* loaded from: classes2.dex */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda22 implements Predicate {
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda22 INSTANCE = new WifiPickerTracker$$ExternalSyntheticLambda22();

    private /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda22() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return WifiPickerTracker.lambda$updateWifiEntries$2((PasspointWifiEntry) obj);
    }
}
