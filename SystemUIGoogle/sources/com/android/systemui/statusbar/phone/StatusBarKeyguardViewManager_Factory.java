package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.ViewMediatorCallback;
import com.android.systemui.dock.DockManager;
import com.android.systemui.keyguard.FaceAuthScreenBrightnessController;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardBouncer;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarKeyguardViewManager_Factory implements Factory<StatusBarKeyguardViewManager> {
    private final Provider<ViewMediatorCallback> callbackProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DockManager> dockManagerProvider;
    private final Provider<Optional<FaceAuthScreenBrightnessController>> faceAuthScreenBrightnessControllerProvider;
    private final Provider<KeyguardBouncer.Factory> keyguardBouncerFactoryProvider;
    private final Provider<KeyguardMessageAreaController.Factory> keyguardMessageAreaFactoryProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<LockPatternUtils> lockPatternUtilsProvider;
    private final Provider<NavigationModeController> navigationModeControllerProvider;
    private final Provider<NotificationMediaManager> notificationMediaManagerProvider;
    private final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    private final Provider<SysuiStatusBarStateController> sysuiStatusBarStateControllerProvider;
    private final Provider<UnlockedScreenOffAnimationController> unlockedScreenOffAnimationControllerProvider;
    private final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public StatusBarKeyguardViewManager_Factory(Provider<Context> provider, Provider<ViewMediatorCallback> provider2, Provider<LockPatternUtils> provider3, Provider<SysuiStatusBarStateController> provider4, Provider<ConfigurationController> provider5, Provider<KeyguardUpdateMonitor> provider6, Provider<NavigationModeController> provider7, Provider<DockManager> provider8, Provider<NotificationShadeWindowController> provider9, Provider<KeyguardStateController> provider10, Provider<Optional<FaceAuthScreenBrightnessController>> provider11, Provider<NotificationMediaManager> provider12, Provider<KeyguardBouncer.Factory> provider13, Provider<WakefulnessLifecycle> provider14, Provider<UnlockedScreenOffAnimationController> provider15, Provider<KeyguardMessageAreaController.Factory> provider16) {
        this.contextProvider = provider;
        this.callbackProvider = provider2;
        this.lockPatternUtilsProvider = provider3;
        this.sysuiStatusBarStateControllerProvider = provider4;
        this.configurationControllerProvider = provider5;
        this.keyguardUpdateMonitorProvider = provider6;
        this.navigationModeControllerProvider = provider7;
        this.dockManagerProvider = provider8;
        this.notificationShadeWindowControllerProvider = provider9;
        this.keyguardStateControllerProvider = provider10;
        this.faceAuthScreenBrightnessControllerProvider = provider11;
        this.notificationMediaManagerProvider = provider12;
        this.keyguardBouncerFactoryProvider = provider13;
        this.wakefulnessLifecycleProvider = provider14;
        this.unlockedScreenOffAnimationControllerProvider = provider15;
        this.keyguardMessageAreaFactoryProvider = provider16;
    }

    @Override // javax.inject.Provider
    public StatusBarKeyguardViewManager get() {
        return newInstance(this.contextProvider.get(), this.callbackProvider.get(), this.lockPatternUtilsProvider.get(), this.sysuiStatusBarStateControllerProvider.get(), this.configurationControllerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.navigationModeControllerProvider.get(), this.dockManagerProvider.get(), this.notificationShadeWindowControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.faceAuthScreenBrightnessControllerProvider.get(), this.notificationMediaManagerProvider.get(), this.keyguardBouncerFactoryProvider.get(), this.wakefulnessLifecycleProvider.get(), this.unlockedScreenOffAnimationControllerProvider.get(), this.keyguardMessageAreaFactoryProvider.get());
    }

    public static StatusBarKeyguardViewManager_Factory create(Provider<Context> provider, Provider<ViewMediatorCallback> provider2, Provider<LockPatternUtils> provider3, Provider<SysuiStatusBarStateController> provider4, Provider<ConfigurationController> provider5, Provider<KeyguardUpdateMonitor> provider6, Provider<NavigationModeController> provider7, Provider<DockManager> provider8, Provider<NotificationShadeWindowController> provider9, Provider<KeyguardStateController> provider10, Provider<Optional<FaceAuthScreenBrightnessController>> provider11, Provider<NotificationMediaManager> provider12, Provider<KeyguardBouncer.Factory> provider13, Provider<WakefulnessLifecycle> provider14, Provider<UnlockedScreenOffAnimationController> provider15, Provider<KeyguardMessageAreaController.Factory> provider16) {
        return new StatusBarKeyguardViewManager_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16);
    }

    public static StatusBarKeyguardViewManager newInstance(Context context, ViewMediatorCallback viewMediatorCallback, LockPatternUtils lockPatternUtils, SysuiStatusBarStateController sysuiStatusBarStateController, ConfigurationController configurationController, KeyguardUpdateMonitor keyguardUpdateMonitor, NavigationModeController navigationModeController, DockManager dockManager, NotificationShadeWindowController notificationShadeWindowController, KeyguardStateController keyguardStateController, Optional<FaceAuthScreenBrightnessController> optional, NotificationMediaManager notificationMediaManager, KeyguardBouncer.Factory factory, WakefulnessLifecycle wakefulnessLifecycle, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, KeyguardMessageAreaController.Factory factory2) {
        return new StatusBarKeyguardViewManager(context, viewMediatorCallback, lockPatternUtils, sysuiStatusBarStateController, configurationController, keyguardUpdateMonitor, navigationModeController, dockManager, notificationShadeWindowController, keyguardStateController, optional, notificationMediaManager, factory, wakefulnessLifecycle, unlockedScreenOffAnimationController, factory2);
    }
}
