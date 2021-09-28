package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkScoreManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import com.android.systemui.statusbar.policy.AccessPointControllerImpl;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AccessPointControllerImpl_WifiPickerTrackerFactory_Factory implements Factory<AccessPointControllerImpl.WifiPickerTrackerFactory> {
    private final Provider<ConnectivityManager> connectivityManagerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<NetworkScoreManager> networkScoreManagerProvider;
    private final Provider<WifiManager> wifiManagerProvider;
    private final Provider<Handler> workerHandlerProvider;

    public AccessPointControllerImpl_WifiPickerTrackerFactory_Factory(Provider<Context> provider, Provider<WifiManager> provider2, Provider<ConnectivityManager> provider3, Provider<NetworkScoreManager> provider4, Provider<Handler> provider5, Provider<Handler> provider6) {
        this.contextProvider = provider;
        this.wifiManagerProvider = provider2;
        this.connectivityManagerProvider = provider3;
        this.networkScoreManagerProvider = provider4;
        this.mainHandlerProvider = provider5;
        this.workerHandlerProvider = provider6;
    }

    @Override // javax.inject.Provider
    public AccessPointControllerImpl.WifiPickerTrackerFactory get() {
        return newInstance(this.contextProvider.get(), this.wifiManagerProvider.get(), this.connectivityManagerProvider.get(), this.networkScoreManagerProvider.get(), this.mainHandlerProvider.get(), this.workerHandlerProvider.get());
    }

    public static AccessPointControllerImpl_WifiPickerTrackerFactory_Factory create(Provider<Context> provider, Provider<WifiManager> provider2, Provider<ConnectivityManager> provider3, Provider<NetworkScoreManager> provider4, Provider<Handler> provider5, Provider<Handler> provider6) {
        return new AccessPointControllerImpl_WifiPickerTrackerFactory_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static AccessPointControllerImpl.WifiPickerTrackerFactory newInstance(Context context, WifiManager wifiManager, ConnectivityManager connectivityManager, NetworkScoreManager networkScoreManager, Handler handler, Handler handler2) {
        return new AccessPointControllerImpl.WifiPickerTrackerFactory(context, wifiManager, connectivityManager, networkScoreManager, handler, handler2);
    }
}
