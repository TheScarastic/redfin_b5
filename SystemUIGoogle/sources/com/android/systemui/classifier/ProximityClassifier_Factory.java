package com.android.systemui.classifier;

import com.android.systemui.util.DeviceConfigProxy;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ProximityClassifier_Factory implements Factory<ProximityClassifier> {
    private final Provider<FalsingDataProvider> dataProvider;
    private final Provider<DeviceConfigProxy> deviceConfigProxyProvider;
    private final Provider<DistanceClassifier> distanceClassifierProvider;

    public ProximityClassifier_Factory(Provider<DistanceClassifier> provider, Provider<FalsingDataProvider> provider2, Provider<DeviceConfigProxy> provider3) {
        this.distanceClassifierProvider = provider;
        this.dataProvider = provider2;
        this.deviceConfigProxyProvider = provider3;
    }

    @Override // javax.inject.Provider
    public ProximityClassifier get() {
        return newInstance(this.distanceClassifierProvider.get(), this.dataProvider.get(), this.deviceConfigProxyProvider.get());
    }

    public static ProximityClassifier_Factory create(Provider<DistanceClassifier> provider, Provider<FalsingDataProvider> provider2, Provider<DeviceConfigProxy> provider3) {
        return new ProximityClassifier_Factory(provider, provider2, provider3);
    }

    public static ProximityClassifier newInstance(Object obj, FalsingDataProvider falsingDataProvider, DeviceConfigProxy deviceConfigProxy) {
        return new ProximityClassifier((DistanceClassifier) obj, falsingDataProvider, deviceConfigProxy);
    }
}
