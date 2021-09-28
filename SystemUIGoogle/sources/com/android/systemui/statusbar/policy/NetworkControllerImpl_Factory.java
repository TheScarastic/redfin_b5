package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkScoreManager;
import android.net.wifi.WifiManager;
import android.os.Looper;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.util.CarrierConfigTracker;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NetworkControllerImpl_Factory implements Factory<NetworkControllerImpl> {
    private final Provider<AccessPointControllerImpl> accessPointControllerProvider;
    private final Provider<Executor> bgExecutorProvider;
    private final Provider<Looper> bgLooperProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<CallbackHandler> callbackHandlerProvider;
    private final Provider<CarrierConfigTracker> carrierConfigTrackerProvider;
    private final Provider<ConnectivityManager> connectivityManagerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DemoModeController> demoModeControllerProvider;
    private final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<NetworkScoreManager> networkScoreManagerProvider;
    private final Provider<SubscriptionManager> subscriptionManagerProvider;
    private final Provider<TelephonyListenerManager> telephonyListenerManagerProvider;
    private final Provider<TelephonyManager> telephonyManagerProvider;
    private final Provider<WifiManager> wifiManagerProvider;

    public NetworkControllerImpl_Factory(Provider<Context> provider, Provider<Looper> provider2, Provider<Executor> provider3, Provider<SubscriptionManager> provider4, Provider<CallbackHandler> provider5, Provider<DeviceProvisionedController> provider6, Provider<BroadcastDispatcher> provider7, Provider<ConnectivityManager> provider8, Provider<TelephonyManager> provider9, Provider<TelephonyListenerManager> provider10, Provider<WifiManager> provider11, Provider<NetworkScoreManager> provider12, Provider<AccessPointControllerImpl> provider13, Provider<DemoModeController> provider14, Provider<CarrierConfigTracker> provider15, Provider<FeatureFlags> provider16, Provider<DumpManager> provider17) {
        this.contextProvider = provider;
        this.bgLooperProvider = provider2;
        this.bgExecutorProvider = provider3;
        this.subscriptionManagerProvider = provider4;
        this.callbackHandlerProvider = provider5;
        this.deviceProvisionedControllerProvider = provider6;
        this.broadcastDispatcherProvider = provider7;
        this.connectivityManagerProvider = provider8;
        this.telephonyManagerProvider = provider9;
        this.telephonyListenerManagerProvider = provider10;
        this.wifiManagerProvider = provider11;
        this.networkScoreManagerProvider = provider12;
        this.accessPointControllerProvider = provider13;
        this.demoModeControllerProvider = provider14;
        this.carrierConfigTrackerProvider = provider15;
        this.featureFlagsProvider = provider16;
        this.dumpManagerProvider = provider17;
    }

    @Override // javax.inject.Provider
    public NetworkControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.bgLooperProvider.get(), this.bgExecutorProvider.get(), this.subscriptionManagerProvider.get(), this.callbackHandlerProvider.get(), this.deviceProvisionedControllerProvider.get(), this.broadcastDispatcherProvider.get(), this.connectivityManagerProvider.get(), this.telephonyManagerProvider.get(), this.telephonyListenerManagerProvider.get(), this.wifiManagerProvider.get(), this.networkScoreManagerProvider.get(), this.accessPointControllerProvider.get(), this.demoModeControllerProvider.get(), this.carrierConfigTrackerProvider.get(), this.featureFlagsProvider.get(), this.dumpManagerProvider.get());
    }

    public static NetworkControllerImpl_Factory create(Provider<Context> provider, Provider<Looper> provider2, Provider<Executor> provider3, Provider<SubscriptionManager> provider4, Provider<CallbackHandler> provider5, Provider<DeviceProvisionedController> provider6, Provider<BroadcastDispatcher> provider7, Provider<ConnectivityManager> provider8, Provider<TelephonyManager> provider9, Provider<TelephonyListenerManager> provider10, Provider<WifiManager> provider11, Provider<NetworkScoreManager> provider12, Provider<AccessPointControllerImpl> provider13, Provider<DemoModeController> provider14, Provider<CarrierConfigTracker> provider15, Provider<FeatureFlags> provider16, Provider<DumpManager> provider17) {
        return new NetworkControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17);
    }

    public static NetworkControllerImpl newInstance(Context context, Looper looper, Executor executor, SubscriptionManager subscriptionManager, CallbackHandler callbackHandler, DeviceProvisionedController deviceProvisionedController, BroadcastDispatcher broadcastDispatcher, ConnectivityManager connectivityManager, TelephonyManager telephonyManager, TelephonyListenerManager telephonyListenerManager, WifiManager wifiManager, NetworkScoreManager networkScoreManager, AccessPointControllerImpl accessPointControllerImpl, DemoModeController demoModeController, CarrierConfigTracker carrierConfigTracker, FeatureFlags featureFlags, DumpManager dumpManager) {
        return new NetworkControllerImpl(context, looper, executor, subscriptionManager, callbackHandler, deviceProvisionedController, broadcastDispatcher, connectivityManager, telephonyManager, telephonyListenerManager, wifiManager, networkScoreManager, accessPointControllerImpl, demoModeController, carrierConfigTracker, featureFlags, dumpManager);
    }
}
