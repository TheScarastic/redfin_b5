package com.android.systemui.statusbar;

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
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.wakelock.WakeLock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardIndicationController_Factory implements Factory<KeyguardIndicationController> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;
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
    private final Provider<UserManager> userManagerProvider;
    private final Provider<WakeLock.Builder> wakeLockBuilderProvider;

    public KeyguardIndicationController_Factory(Provider<Context> provider, Provider<WakeLock.Builder> provider2, Provider<KeyguardStateController> provider3, Provider<StatusBarStateController> provider4, Provider<KeyguardUpdateMonitor> provider5, Provider<DockManager> provider6, Provider<BroadcastDispatcher> provider7, Provider<DevicePolicyManager> provider8, Provider<IBatteryStats> provider9, Provider<UserManager> provider10, Provider<DelayableExecutor> provider11, Provider<FalsingManager> provider12, Provider<LockPatternUtils> provider13, Provider<IActivityManager> provider14, Provider<KeyguardBypassController> provider15) {
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
        this.executorProvider = provider11;
        this.falsingManagerProvider = provider12;
        this.lockPatternUtilsProvider = provider13;
        this.iActivityManagerProvider = provider14;
        this.keyguardBypassControllerProvider = provider15;
    }

    @Override // javax.inject.Provider
    public KeyguardIndicationController get() {
        return newInstance(this.contextProvider.get(), this.wakeLockBuilderProvider.get(), this.keyguardStateControllerProvider.get(), this.statusBarStateControllerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.dockManagerProvider.get(), this.broadcastDispatcherProvider.get(), this.devicePolicyManagerProvider.get(), this.iBatteryStatsProvider.get(), this.userManagerProvider.get(), this.executorProvider.get(), this.falsingManagerProvider.get(), this.lockPatternUtilsProvider.get(), this.iActivityManagerProvider.get(), this.keyguardBypassControllerProvider.get());
    }

    public static KeyguardIndicationController_Factory create(Provider<Context> provider, Provider<WakeLock.Builder> provider2, Provider<KeyguardStateController> provider3, Provider<StatusBarStateController> provider4, Provider<KeyguardUpdateMonitor> provider5, Provider<DockManager> provider6, Provider<BroadcastDispatcher> provider7, Provider<DevicePolicyManager> provider8, Provider<IBatteryStats> provider9, Provider<UserManager> provider10, Provider<DelayableExecutor> provider11, Provider<FalsingManager> provider12, Provider<LockPatternUtils> provider13, Provider<IActivityManager> provider14, Provider<KeyguardBypassController> provider15) {
        return new KeyguardIndicationController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15);
    }

    public static KeyguardIndicationController newInstance(Context context, WakeLock.Builder builder, KeyguardStateController keyguardStateController, StatusBarStateController statusBarStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, DockManager dockManager, BroadcastDispatcher broadcastDispatcher, DevicePolicyManager devicePolicyManager, IBatteryStats iBatteryStats, UserManager userManager, DelayableExecutor delayableExecutor, FalsingManager falsingManager, LockPatternUtils lockPatternUtils, IActivityManager iActivityManager, KeyguardBypassController keyguardBypassController) {
        return new KeyguardIndicationController(context, builder, keyguardStateController, statusBarStateController, keyguardUpdateMonitor, dockManager, broadcastDispatcher, devicePolicyManager, iBatteryStats, userManager, delayableExecutor, falsingManager, lockPatternUtils, iActivityManager, keyguardBypassController);
    }
}
