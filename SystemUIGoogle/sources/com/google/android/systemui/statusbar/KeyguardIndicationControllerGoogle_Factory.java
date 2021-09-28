package com.google.android.systemui.statusbar;

import android.app.IActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.UserManager;
import com.android.internal.app.IBatteryStats;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dock.DockManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.wakelock.WakeLock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class KeyguardIndicationControllerGoogle_Factory implements Factory<KeyguardIndicationControllerGoogle> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DeviceConfigProxy> deviceConfigProvider;
    private final Provider<DevicePolicyManager> devicePolicyManagerProvider;
    private final Provider<DockManager> dockManagerProvider;
    private final Provider<DelayableExecutor> executorProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<IActivityManager> iActivityManagerProvider;
    private final Provider<IBatteryStats> iBatteryStatsProvider;
    private final Provider<KeyguardBypassController> keyguardBypassControllerProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<LockPatternUtils> lockPatternUtilsProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<TunerService> tunerServiceProvider;
    private final Provider<UserManager> userManagerProvider;
    private final Provider<WakeLock.Builder> wakeLockBuilderProvider;

    public KeyguardIndicationControllerGoogle_Factory(Provider<Context> provider, Provider<WakeLock.Builder> provider2, Provider<KeyguardStateController> provider3, Provider<StatusBarStateController> provider4, Provider<KeyguardUpdateMonitor> provider5, Provider<DockManager> provider6, Provider<BroadcastDispatcher> provider7, Provider<DevicePolicyManager> provider8, Provider<IBatteryStats> provider9, Provider<UserManager> provider10, Provider<TunerService> provider11, Provider<DeviceConfigProxy> provider12, Provider<DelayableExecutor> provider13, Provider<FalsingManager> provider14, Provider<LockPatternUtils> provider15, Provider<IActivityManager> provider16, Provider<KeyguardBypassController> provider17) {
        this.contextProvider = provider;
        this.wakeLockBuilderProvider = provider2;
        this.keyguardStateControllerProvider = provider3;
        this.statusBarStateControllerProvider = provider4;
        this.keyguardUpdateMonitorProvider = provider5;
        this.dockManagerProvider = provider6;
        this.broadcastDispatcherProvider = provider7;
        this.devicePolicyManagerProvider = provider8;
        this.iBatteryStatsProvider = provider9;
        this.userManagerProvider = provider10;
        this.tunerServiceProvider = provider11;
        this.deviceConfigProvider = provider12;
        this.executorProvider = provider13;
        this.falsingManagerProvider = provider14;
        this.lockPatternUtilsProvider = provider15;
        this.iActivityManagerProvider = provider16;
        this.keyguardBypassControllerProvider = provider17;
    }

    @Override // javax.inject.Provider
    public KeyguardIndicationControllerGoogle get() {
        return newInstance(this.contextProvider.get(), this.wakeLockBuilderProvider.get(), this.keyguardStateControllerProvider.get(), this.statusBarStateControllerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.dockManagerProvider.get(), this.broadcastDispatcherProvider.get(), this.devicePolicyManagerProvider.get(), this.iBatteryStatsProvider.get(), this.userManagerProvider.get(), this.tunerServiceProvider.get(), this.deviceConfigProvider.get(), this.executorProvider.get(), this.falsingManagerProvider.get(), this.lockPatternUtilsProvider.get(), this.iActivityManagerProvider.get(), this.keyguardBypassControllerProvider.get());
    }

    public static KeyguardIndicationControllerGoogle_Factory create(Provider<Context> provider, Provider<WakeLock.Builder> provider2, Provider<KeyguardStateController> provider3, Provider<StatusBarStateController> provider4, Provider<KeyguardUpdateMonitor> provider5, Provider<DockManager> provider6, Provider<BroadcastDispatcher> provider7, Provider<DevicePolicyManager> provider8, Provider<IBatteryStats> provider9, Provider<UserManager> provider10, Provider<TunerService> provider11, Provider<DeviceConfigProxy> provider12, Provider<DelayableExecutor> provider13, Provider<FalsingManager> provider14, Provider<LockPatternUtils> provider15, Provider<IActivityManager> provider16, Provider<KeyguardBypassController> provider17) {
        return new KeyguardIndicationControllerGoogle_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17);
    }

    public static KeyguardIndicationControllerGoogle newInstance(Context context, WakeLock.Builder builder, KeyguardStateController keyguardStateController, StatusBarStateController statusBarStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, DockManager dockManager, BroadcastDispatcher broadcastDispatcher, DevicePolicyManager devicePolicyManager, IBatteryStats iBatteryStats, UserManager userManager, TunerService tunerService, DeviceConfigProxy deviceConfigProxy, DelayableExecutor delayableExecutor, FalsingManager falsingManager, LockPatternUtils lockPatternUtils, IActivityManager iActivityManager, KeyguardBypassController keyguardBypassController) {
        return new KeyguardIndicationControllerGoogle(context, builder, keyguardStateController, statusBarStateController, keyguardUpdateMonitor, dockManager, broadcastDispatcher, devicePolicyManager, iBatteryStats, userManager, tunerService, deviceConfigProxy, delayableExecutor, falsingManager, lockPatternUtils, iActivityManager, keyguardBypassController);
    }
}
