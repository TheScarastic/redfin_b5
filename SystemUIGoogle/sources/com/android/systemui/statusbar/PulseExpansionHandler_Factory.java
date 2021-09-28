package com.android.systemui.statusbar;

import android.content.Context;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.stack.NotificationRoundnessManager;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PulseExpansionHandler_Factory implements Factory<PulseExpansionHandler> {
    private final Provider<KeyguardBypassController> bypassControllerProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<FalsingCollector> falsingCollectorProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<HeadsUpManagerPhone> headsUpManagerProvider;
    private final Provider<LockscreenShadeTransitionController> lockscreenShadeTransitionControllerProvider;
    private final Provider<NotificationRoundnessManager> roundnessManagerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<NotificationWakeUpCoordinator> wakeUpCoordinatorProvider;

    public PulseExpansionHandler_Factory(Provider<Context> provider, Provider<NotificationWakeUpCoordinator> provider2, Provider<KeyguardBypassController> provider3, Provider<HeadsUpManagerPhone> provider4, Provider<NotificationRoundnessManager> provider5, Provider<ConfigurationController> provider6, Provider<StatusBarStateController> provider7, Provider<FalsingManager> provider8, Provider<LockscreenShadeTransitionController> provider9, Provider<FalsingCollector> provider10) {
        this.contextProvider = provider;
        this.wakeUpCoordinatorProvider = provider2;
        this.bypassControllerProvider = provider3;
        this.headsUpManagerProvider = provider4;
        this.roundnessManagerProvider = provider5;
        this.configurationControllerProvider = provider6;
        this.statusBarStateControllerProvider = provider7;
        this.falsingManagerProvider = provider8;
        this.lockscreenShadeTransitionControllerProvider = provider9;
        this.falsingCollectorProvider = provider10;
    }

    @Override // javax.inject.Provider
    public PulseExpansionHandler get() {
        return newInstance(this.contextProvider.get(), this.wakeUpCoordinatorProvider.get(), this.bypassControllerProvider.get(), this.headsUpManagerProvider.get(), this.roundnessManagerProvider.get(), this.configurationControllerProvider.get(), this.statusBarStateControllerProvider.get(), this.falsingManagerProvider.get(), this.lockscreenShadeTransitionControllerProvider.get(), this.falsingCollectorProvider.get());
    }

    public static PulseExpansionHandler_Factory create(Provider<Context> provider, Provider<NotificationWakeUpCoordinator> provider2, Provider<KeyguardBypassController> provider3, Provider<HeadsUpManagerPhone> provider4, Provider<NotificationRoundnessManager> provider5, Provider<ConfigurationController> provider6, Provider<StatusBarStateController> provider7, Provider<FalsingManager> provider8, Provider<LockscreenShadeTransitionController> provider9, Provider<FalsingCollector> provider10) {
        return new PulseExpansionHandler_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static PulseExpansionHandler newInstance(Context context, NotificationWakeUpCoordinator notificationWakeUpCoordinator, KeyguardBypassController keyguardBypassController, HeadsUpManagerPhone headsUpManagerPhone, NotificationRoundnessManager notificationRoundnessManager, ConfigurationController configurationController, StatusBarStateController statusBarStateController, FalsingManager falsingManager, LockscreenShadeTransitionController lockscreenShadeTransitionController, FalsingCollector falsingCollector) {
        return new PulseExpansionHandler(context, notificationWakeUpCoordinator, keyguardBypassController, headsUpManagerPhone, notificationRoundnessManager, configurationController, statusBarStateController, falsingManager, lockscreenShadeTransitionController, falsingCollector);
    }
}
