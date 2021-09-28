package com.android.wifitrackerlib;

import android.net.wifi.hotspot2.PasspointConfiguration;
import java.util.function.Function;
/* loaded from: classes2.dex */
public final /* synthetic */ class SavedNetworkTracker$$ExternalSyntheticLambda3 implements Function {
    public static final /* synthetic */ SavedNetworkTracker$$ExternalSyntheticLambda3 INSTANCE = new SavedNetworkTracker$$ExternalSyntheticLambda3();

    private /* synthetic */ SavedNetworkTracker$$ExternalSyntheticLambda3() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return SavedNetworkTracker.lambda$updatePasspointWifiEntryConfigs$3((PasspointConfiguration) obj);
    }
}
