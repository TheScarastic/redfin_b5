package com.android.wifitrackerlib;

import android.net.wifi.WifiConfiguration;
import com.android.wifitrackerlib.StandardWifiEntry;
import java.util.function.Function;
/* loaded from: classes2.dex */
public final /* synthetic */ class SavedNetworkTracker$$ExternalSyntheticLambda5 implements Function {
    public static final /* synthetic */ SavedNetworkTracker$$ExternalSyntheticLambda5 INSTANCE = new SavedNetworkTracker$$ExternalSyntheticLambda5();

    private /* synthetic */ SavedNetworkTracker$$ExternalSyntheticLambda5() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return new StandardWifiEntry.StandardWifiEntryKey((WifiConfiguration) obj);
    }
}
