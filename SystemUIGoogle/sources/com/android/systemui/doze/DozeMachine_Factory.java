package com.android.systemui.doze;

import android.hardware.display.AmbientDisplayConfiguration;
import com.android.systemui.dock.DockManager;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.wakelock.WakeLock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeMachine_Factory implements Factory<DozeMachine> {
    private final Provider<BatteryController> batteryControllerProvider;
    private final Provider<AmbientDisplayConfiguration> configProvider;
    private final Provider<DockManager> dockManagerProvider;
    private final Provider<DozeHost> dozeHostProvider;
    private final Provider<DozeLog> dozeLogProvider;
    private final Provider<DozeMachine.Part[]> partsProvider;
    private final Provider<DozeMachine.Service> serviceProvider;
    private final Provider<WakeLock> wakeLockProvider;
    private final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public DozeMachine_Factory(Provider<DozeMachine.Service> provider, Provider<AmbientDisplayConfiguration> provider2, Provider<WakeLock> provider3, Provider<WakefulnessLifecycle> provider4, Provider<BatteryController> provider5, Provider<DozeLog> provider6, Provider<DockManager> provider7, Provider<DozeHost> provider8, Provider<DozeMachine.Part[]> provider9) {
        this.serviceProvider = provider;
        this.configProvider = provider2;
        this.wakeLockProvider = provider3;
        this.wakefulnessLifecycleProvider = provider4;
        this.batteryControllerProvider = provider5;
        this.dozeLogProvider = provider6;
        this.dockManagerProvider = provider7;
        this.dozeHostProvider = provider8;
        this.partsProvider = provider9;
    }

    @Override // javax.inject.Provider
    public DozeMachine get() {
        return newInstance(this.serviceProvider.get(), this.configProvider.get(), this.wakeLockProvider.get(), this.wakefulnessLifecycleProvider.get(), this.batteryControllerProvider.get(), this.dozeLogProvider.get(), this.dockManagerProvider.get(), this.dozeHostProvider.get(), this.partsProvider.get());
    }

    public static DozeMachine_Factory create(Provider<DozeMachine.Service> provider, Provider<AmbientDisplayConfiguration> provider2, Provider<WakeLock> provider3, Provider<WakefulnessLifecycle> provider4, Provider<BatteryController> provider5, Provider<DozeLog> provider6, Provider<DockManager> provider7, Provider<DozeHost> provider8, Provider<DozeMachine.Part[]> provider9) {
        return new DozeMachine_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static DozeMachine newInstance(DozeMachine.Service service, AmbientDisplayConfiguration ambientDisplayConfiguration, WakeLock wakeLock, WakefulnessLifecycle wakefulnessLifecycle, BatteryController batteryController, DozeLog dozeLog, DockManager dockManager, DozeHost dozeHost, DozeMachine.Part[] partArr) {
        return new DozeMachine(service, ambientDisplayConfiguration, wakeLock, wakefulnessLifecycle, batteryController, dozeLog, dockManager, dozeHost, partArr);
    }
}
