package com.android.systemui.statusbar.notification.collection.coordinator;

import android.content.pm.IPackageManager;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DeviceProvisionedCoordinator_Factory implements Factory<DeviceProvisionedCoordinator> {
    private final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    private final Provider<IPackageManager> packageManagerProvider;

    public DeviceProvisionedCoordinator_Factory(Provider<DeviceProvisionedController> provider, Provider<IPackageManager> provider2) {
        this.deviceProvisionedControllerProvider = provider;
        this.packageManagerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public DeviceProvisionedCoordinator get() {
        return newInstance(this.deviceProvisionedControllerProvider.get(), this.packageManagerProvider.get());
    }

    public static DeviceProvisionedCoordinator_Factory create(Provider<DeviceProvisionedController> provider, Provider<IPackageManager> provider2) {
        return new DeviceProvisionedCoordinator_Factory(provider, provider2);
    }

    public static DeviceProvisionedCoordinator newInstance(DeviceProvisionedController deviceProvisionedController, IPackageManager iPackageManager) {
        return new DeviceProvisionedCoordinator(deviceProvisionedController, iPackageManager);
    }
}
