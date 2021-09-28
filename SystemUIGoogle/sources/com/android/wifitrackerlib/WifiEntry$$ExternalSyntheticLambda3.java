package com.android.wifitrackerlib;

import java.net.InetAddress;
import java.util.function.Function;
/* loaded from: classes2.dex */
public final /* synthetic */ class WifiEntry$$ExternalSyntheticLambda3 implements Function {
    public static final /* synthetic */ WifiEntry$$ExternalSyntheticLambda3 INSTANCE = new WifiEntry$$ExternalSyntheticLambda3();

    private /* synthetic */ WifiEntry$$ExternalSyntheticLambda3() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return ((InetAddress) obj).getHostAddress();
    }
}
