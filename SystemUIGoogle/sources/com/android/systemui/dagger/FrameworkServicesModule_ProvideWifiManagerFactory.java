package com.android.systemui.dagger;

import android.content.Context;
import android.net.wifi.WifiManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideWifiManagerFactory implements Factory<WifiManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideWifiManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public WifiManager get() {
        return provideWifiManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideWifiManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideWifiManagerFactory(provider);
    }

    public static WifiManager provideWifiManager(Context context) {
        return FrameworkServicesModule.provideWifiManager(context);
    }
}
