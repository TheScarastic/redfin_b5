package com.android.systemui.classifier;

import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.dock.DockManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FalsingCollectorImpl_Factory implements Factory<FalsingCollectorImpl> {
    private final Provider<BatteryController> batteryControllerProvider;
    private final Provider<DockManager> dockManagerProvider;
    private final Provider<FalsingDataProvider> falsingDataProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<HistoryTracker> historyTrackerProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<DelayableExecutor> mainExecutorProvider;
    private final Provider<ProximitySensor> proximitySensorProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<SystemClock> systemClockProvider;

    public FalsingCollectorImpl_Factory(Provider<FalsingDataProvider> provider, Provider<FalsingManager> provider2, Provider<KeyguardUpdateMonitor> provider3, Provider<HistoryTracker> provider4, Provider<ProximitySensor> provider5, Provider<StatusBarStateController> provider6, Provider<KeyguardStateController> provider7, Provider<BatteryController> provider8, Provider<DockManager> provider9, Provider<DelayableExecutor> provider10, Provider<SystemClock> provider11) {
        this.falsingDataProvider = provider;
        this.falsingManagerProvider = provider2;
        this.keyguardUpdateMonitorProvider = provider3;
        this.historyTrackerProvider = provider4;
        this.proximitySensorProvider = provider5;
        this.statusBarStateControllerProvider = provider6;
        this.keyguardStateControllerProvider = provider7;
        this.batteryControllerProvider = provider8;
        this.dockManagerProvider = provider9;
        this.mainExecutorProvider = provider10;
        this.systemClockProvider = provider11;
    }

    @Override // javax.inject.Provider
    public FalsingCollectorImpl get() {
        return newInstance(this.falsingDataProvider.get(), this.falsingManagerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.historyTrackerProvider.get(), this.proximitySensorProvider.get(), this.statusBarStateControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.batteryControllerProvider.get(), this.dockManagerProvider.get(), this.mainExecutorProvider.get(), this.systemClockProvider.get());
    }

    public static FalsingCollectorImpl_Factory create(Provider<FalsingDataProvider> provider, Provider<FalsingManager> provider2, Provider<KeyguardUpdateMonitor> provider3, Provider<HistoryTracker> provider4, Provider<ProximitySensor> provider5, Provider<StatusBarStateController> provider6, Provider<KeyguardStateController> provider7, Provider<BatteryController> provider8, Provider<DockManager> provider9, Provider<DelayableExecutor> provider10, Provider<SystemClock> provider11) {
        return new FalsingCollectorImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    public static FalsingCollectorImpl newInstance(FalsingDataProvider falsingDataProvider, FalsingManager falsingManager, KeyguardUpdateMonitor keyguardUpdateMonitor, HistoryTracker historyTracker, ProximitySensor proximitySensor, StatusBarStateController statusBarStateController, KeyguardStateController keyguardStateController, BatteryController batteryController, DockManager dockManager, DelayableExecutor delayableExecutor, SystemClock systemClock) {
        return new FalsingCollectorImpl(falsingDataProvider, falsingManager, keyguardUpdateMonitor, historyTracker, proximitySensor, statusBarStateController, keyguardStateController, batteryController, dockManager, delayableExecutor, systemClock);
    }
}
