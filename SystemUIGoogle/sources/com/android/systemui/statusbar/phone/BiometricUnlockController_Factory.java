package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.PowerManager;
import com.android.internal.logging.MetricsLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class BiometricUnlockController_Factory implements Factory<BiometricUnlockController> {
    private final Provider<AuthController> authControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DozeParameters> dozeParametersProvider;
    private final Provider<DozeScrimController> dozeScrimControllerProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<KeyguardBypassController> keyguardBypassControllerProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<KeyguardViewMediator> keyguardViewMediatorProvider;
    private final Provider<MetricsLogger> metricsLoggerProvider;
    private final Provider<NotificationMediaManager> notificationMediaManagerProvider;
    private final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    private final Provider<PowerManager> powerManagerProvider;
    private final Provider<Resources> resourcesProvider;
    private final Provider<ScreenLifecycle> screenLifecycleProvider;
    private final Provider<ScrimController> scrimControllerProvider;
    private final Provider<ShadeController> shadeControllerProvider;
    private final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public BiometricUnlockController_Factory(Provider<Context> provider, Provider<DozeScrimController> provider2, Provider<KeyguardViewMediator> provider3, Provider<ScrimController> provider4, Provider<ShadeController> provider5, Provider<NotificationShadeWindowController> provider6, Provider<KeyguardStateController> provider7, Provider<Handler> provider8, Provider<KeyguardUpdateMonitor> provider9, Provider<Resources> provider10, Provider<KeyguardBypassController> provider11, Provider<DozeParameters> provider12, Provider<MetricsLogger> provider13, Provider<DumpManager> provider14, Provider<PowerManager> provider15, Provider<NotificationMediaManager> provider16, Provider<WakefulnessLifecycle> provider17, Provider<ScreenLifecycle> provider18, Provider<AuthController> provider19) {
        this.contextProvider = provider;
        this.dozeScrimControllerProvider = provider2;
        this.keyguardViewMediatorProvider = provider3;
        this.scrimControllerProvider = provider4;
        this.shadeControllerProvider = provider5;
        this.notificationShadeWindowControllerProvider = provider6;
        this.keyguardStateControllerProvider = provider7;
        this.handlerProvider = provider8;
        this.keyguardUpdateMonitorProvider = provider9;
        this.resourcesProvider = provider10;
        this.keyguardBypassControllerProvider = provider11;
        this.dozeParametersProvider = provider12;
        this.metricsLoggerProvider = provider13;
        this.dumpManagerProvider = provider14;
        this.powerManagerProvider = provider15;
        this.notificationMediaManagerProvider = provider16;
        this.wakefulnessLifecycleProvider = provider17;
        this.screenLifecycleProvider = provider18;
        this.authControllerProvider = provider19;
    }

    @Override // javax.inject.Provider
    public BiometricUnlockController get() {
        return newInstance(this.contextProvider.get(), this.dozeScrimControllerProvider.get(), this.keyguardViewMediatorProvider.get(), this.scrimControllerProvider.get(), this.shadeControllerProvider.get(), this.notificationShadeWindowControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.handlerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.resourcesProvider.get(), this.keyguardBypassControllerProvider.get(), this.dozeParametersProvider.get(), this.metricsLoggerProvider.get(), this.dumpManagerProvider.get(), this.powerManagerProvider.get(), this.notificationMediaManagerProvider.get(), this.wakefulnessLifecycleProvider.get(), this.screenLifecycleProvider.get(), this.authControllerProvider.get());
    }

    public static BiometricUnlockController_Factory create(Provider<Context> provider, Provider<DozeScrimController> provider2, Provider<KeyguardViewMediator> provider3, Provider<ScrimController> provider4, Provider<ShadeController> provider5, Provider<NotificationShadeWindowController> provider6, Provider<KeyguardStateController> provider7, Provider<Handler> provider8, Provider<KeyguardUpdateMonitor> provider9, Provider<Resources> provider10, Provider<KeyguardBypassController> provider11, Provider<DozeParameters> provider12, Provider<MetricsLogger> provider13, Provider<DumpManager> provider14, Provider<PowerManager> provider15, Provider<NotificationMediaManager> provider16, Provider<WakefulnessLifecycle> provider17, Provider<ScreenLifecycle> provider18, Provider<AuthController> provider19) {
        return new BiometricUnlockController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19);
    }

    public static BiometricUnlockController newInstance(Context context, DozeScrimController dozeScrimController, KeyguardViewMediator keyguardViewMediator, ScrimController scrimController, ShadeController shadeController, NotificationShadeWindowController notificationShadeWindowController, KeyguardStateController keyguardStateController, Handler handler, KeyguardUpdateMonitor keyguardUpdateMonitor, Resources resources, KeyguardBypassController keyguardBypassController, DozeParameters dozeParameters, MetricsLogger metricsLogger, DumpManager dumpManager, PowerManager powerManager, NotificationMediaManager notificationMediaManager, WakefulnessLifecycle wakefulnessLifecycle, ScreenLifecycle screenLifecycle, AuthController authController) {
        return new BiometricUnlockController(context, dozeScrimController, keyguardViewMediator, scrimController, shadeController, notificationShadeWindowController, keyguardStateController, handler, keyguardUpdateMonitor, resources, keyguardBypassController, dozeParameters, metricsLogger, dumpManager, powerManager, notificationMediaManager, wakefulnessLifecycle, screenLifecycle, authController);
    }
}
