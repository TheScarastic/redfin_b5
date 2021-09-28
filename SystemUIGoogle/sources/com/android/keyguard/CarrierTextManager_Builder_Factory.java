package com.android.keyguard;

import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import com.android.keyguard.CarrierTextManager;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.telephony.TelephonyListenerManager;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class CarrierTextManager_Builder_Factory implements Factory<CarrierTextManager.Builder> {
    private final Provider<Executor> bgExecutorProvider;
    private final Provider<Context> contextProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<Executor> mainExecutorProvider;
    private final Provider<Resources> resourcesProvider;
    private final Provider<TelephonyListenerManager> telephonyListenerManagerProvider;
    private final Provider<TelephonyManager> telephonyManagerProvider;
    private final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;
    private final Provider<WifiManager> wifiManagerProvider;

    public CarrierTextManager_Builder_Factory(Provider<Context> provider, Provider<Resources> provider2, Provider<WifiManager> provider3, Provider<TelephonyManager> provider4, Provider<TelephonyListenerManager> provider5, Provider<WakefulnessLifecycle> provider6, Provider<Executor> provider7, Provider<Executor> provider8, Provider<KeyguardUpdateMonitor> provider9) {
        this.contextProvider = provider;
        this.resourcesProvider = provider2;
        this.wifiManagerProvider = provider3;
        this.telephonyManagerProvider = provider4;
        this.telephonyListenerManagerProvider = provider5;
        this.wakefulnessLifecycleProvider = provider6;
        this.mainExecutorProvider = provider7;
        this.bgExecutorProvider = provider8;
        this.keyguardUpdateMonitorProvider = provider9;
    }

    @Override // javax.inject.Provider
    public CarrierTextManager.Builder get() {
        return newInstance(this.contextProvider.get(), this.resourcesProvider.get(), this.wifiManagerProvider.get(), this.telephonyManagerProvider.get(), this.telephonyListenerManagerProvider.get(), this.wakefulnessLifecycleProvider.get(), this.mainExecutorProvider.get(), this.bgExecutorProvider.get(), this.keyguardUpdateMonitorProvider.get());
    }

    public static CarrierTextManager_Builder_Factory create(Provider<Context> provider, Provider<Resources> provider2, Provider<WifiManager> provider3, Provider<TelephonyManager> provider4, Provider<TelephonyListenerManager> provider5, Provider<WakefulnessLifecycle> provider6, Provider<Executor> provider7, Provider<Executor> provider8, Provider<KeyguardUpdateMonitor> provider9) {
        return new CarrierTextManager_Builder_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static CarrierTextManager.Builder newInstance(Context context, Resources resources, WifiManager wifiManager, TelephonyManager telephonyManager, TelephonyListenerManager telephonyListenerManager, WakefulnessLifecycle wakefulnessLifecycle, Executor executor, Executor executor2, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        return new CarrierTextManager.Builder(context, resources, wifiManager, telephonyManager, telephonyListenerManager, wakefulnessLifecycle, executor, executor2, keyguardUpdateMonitor);
    }
}
