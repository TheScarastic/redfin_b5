package com.android.systemui.statusbar.phone;

import android.os.PowerManager;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.doze.DozeLog;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.PulseExpansionHandler;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeServiceHost_Factory implements Factory<DozeServiceHost> {
    private final Provider<AssistManager> assistManagerLazyProvider;
    private final Provider<AuthController> authControllerProvider;
    private final Provider<BatteryController> batteryControllerProvider;
    private final Provider<BiometricUnlockController> biometricUnlockControllerLazyProvider;
    private final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    private final Provider<DozeLog> dozeLogProvider;
    private final Provider<DozeScrimController> dozeScrimControllerProvider;
    private final Provider<HeadsUpManagerPhone> headsUpManagerPhoneProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<KeyguardViewMediator> keyguardViewMediatorProvider;
    private final Provider<NotificationIconAreaController> notificationIconAreaControllerProvider;
    private final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    private final Provider<NotificationWakeUpCoordinator> notificationWakeUpCoordinatorProvider;
    private final Provider<PowerManager> powerManagerProvider;
    private final Provider<PulseExpansionHandler> pulseExpansionHandlerProvider;
    private final Provider<ScrimController> scrimControllerProvider;
    private final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;
    private final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public DozeServiceHost_Factory(Provider<DozeLog> provider, Provider<PowerManager> provider2, Provider<WakefulnessLifecycle> provider3, Provider<SysuiStatusBarStateController> provider4, Provider<DeviceProvisionedController> provider5, Provider<HeadsUpManagerPhone> provider6, Provider<BatteryController> provider7, Provider<ScrimController> provider8, Provider<BiometricUnlockController> provider9, Provider<KeyguardViewMediator> provider10, Provider<AssistManager> provider11, Provider<DozeScrimController> provider12, Provider<KeyguardUpdateMonitor> provider13, Provider<PulseExpansionHandler> provider14, Provider<NotificationShadeWindowController> provider15, Provider<NotificationWakeUpCoordinator> provider16, Provider<AuthController> provider17, Provider<NotificationIconAreaController> provider18) {
        this.dozeLogProvider = provider;
        this.powerManagerProvider = provider2;
        this.wakefulnessLifecycleProvider = provider3;
        this.statusBarStateControllerProvider = provider4;
        this.deviceProvisionedControllerProvider = provider5;
        this.headsUpManagerPhoneProvider = provider6;
        this.batteryControllerProvider = provider7;
        this.scrimControllerProvider = provider8;
        this.biometricUnlockControllerLazyProvider = provider9;
        this.keyguardViewMediatorProvider = provider10;
        this.assistManagerLazyProvider = provider11;
        this.dozeScrimControllerProvider = provider12;
        this.keyguardUpdateMonitorProvider = provider13;
        this.pulseExpansionHandlerProvider = provider14;
        this.notificationShadeWindowControllerProvider = provider15;
        this.notificationWakeUpCoordinatorProvider = provider16;
        this.authControllerProvider = provider17;
        this.notificationIconAreaControllerProvider = provider18;
    }

    @Override // javax.inject.Provider
    public DozeServiceHost get() {
        return newInstance(this.dozeLogProvider.get(), this.powerManagerProvider.get(), this.wakefulnessLifecycleProvider.get(), this.statusBarStateControllerProvider.get(), this.deviceProvisionedControllerProvider.get(), this.headsUpManagerPhoneProvider.get(), this.batteryControllerProvider.get(), this.scrimControllerProvider.get(), DoubleCheck.lazy(this.biometricUnlockControllerLazyProvider), this.keyguardViewMediatorProvider.get(), DoubleCheck.lazy(this.assistManagerLazyProvider), this.dozeScrimControllerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.pulseExpansionHandlerProvider.get(), this.notificationShadeWindowControllerProvider.get(), this.notificationWakeUpCoordinatorProvider.get(), this.authControllerProvider.get(), this.notificationIconAreaControllerProvider.get());
    }

    public static DozeServiceHost_Factory create(Provider<DozeLog> provider, Provider<PowerManager> provider2, Provider<WakefulnessLifecycle> provider3, Provider<SysuiStatusBarStateController> provider4, Provider<DeviceProvisionedController> provider5, Provider<HeadsUpManagerPhone> provider6, Provider<BatteryController> provider7, Provider<ScrimController> provider8, Provider<BiometricUnlockController> provider9, Provider<KeyguardViewMediator> provider10, Provider<AssistManager> provider11, Provider<DozeScrimController> provider12, Provider<KeyguardUpdateMonitor> provider13, Provider<PulseExpansionHandler> provider14, Provider<NotificationShadeWindowController> provider15, Provider<NotificationWakeUpCoordinator> provider16, Provider<AuthController> provider17, Provider<NotificationIconAreaController> provider18) {
        return new DozeServiceHost_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18);
    }

    public static DozeServiceHost newInstance(DozeLog dozeLog, PowerManager powerManager, WakefulnessLifecycle wakefulnessLifecycle, SysuiStatusBarStateController sysuiStatusBarStateController, DeviceProvisionedController deviceProvisionedController, HeadsUpManagerPhone headsUpManagerPhone, BatteryController batteryController, ScrimController scrimController, Lazy<BiometricUnlockController> lazy, KeyguardViewMediator keyguardViewMediator, Lazy<AssistManager> lazy2, DozeScrimController dozeScrimController, KeyguardUpdateMonitor keyguardUpdateMonitor, PulseExpansionHandler pulseExpansionHandler, NotificationShadeWindowController notificationShadeWindowController, NotificationWakeUpCoordinator notificationWakeUpCoordinator, AuthController authController, NotificationIconAreaController notificationIconAreaController) {
        return new DozeServiceHost(dozeLog, powerManager, wakefulnessLifecycle, sysuiStatusBarStateController, deviceProvisionedController, headsUpManagerPhone, batteryController, scrimController, lazy, keyguardViewMediator, lazy2, dozeScrimController, keyguardUpdateMonitor, pulseExpansionHandler, notificationShadeWindowController, notificationWakeUpCoordinator, authController, notificationIconAreaController);
    }
}
