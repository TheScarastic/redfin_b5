package com.android.systemui.statusbar.notification;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationWakeUpCoordinator_Factory implements Factory<NotificationWakeUpCoordinator> {
    private final Provider<KeyguardBypassController> bypassControllerProvider;
    private final Provider<DozeParameters> dozeParametersProvider;
    private final Provider<HeadsUpManager> mHeadsUpManagerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<UnlockedScreenOffAnimationController> unlockedScreenOffAnimationControllerProvider;

    public NotificationWakeUpCoordinator_Factory(Provider<HeadsUpManager> provider, Provider<StatusBarStateController> provider2, Provider<KeyguardBypassController> provider3, Provider<DozeParameters> provider4, Provider<UnlockedScreenOffAnimationController> provider5) {
        this.mHeadsUpManagerProvider = provider;
        this.statusBarStateControllerProvider = provider2;
        this.bypassControllerProvider = provider3;
        this.dozeParametersProvider = provider4;
        this.unlockedScreenOffAnimationControllerProvider = provider5;
    }

    @Override // javax.inject.Provider
    public NotificationWakeUpCoordinator get() {
        return newInstance(this.mHeadsUpManagerProvider.get(), this.statusBarStateControllerProvider.get(), this.bypassControllerProvider.get(), this.dozeParametersProvider.get(), this.unlockedScreenOffAnimationControllerProvider.get());
    }

    public static NotificationWakeUpCoordinator_Factory create(Provider<HeadsUpManager> provider, Provider<StatusBarStateController> provider2, Provider<KeyguardBypassController> provider3, Provider<DozeParameters> provider4, Provider<UnlockedScreenOffAnimationController> provider5) {
        return new NotificationWakeUpCoordinator_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static NotificationWakeUpCoordinator newInstance(HeadsUpManager headsUpManager, StatusBarStateController statusBarStateController, KeyguardBypassController keyguardBypassController, DozeParameters dozeParameters, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController) {
        return new NotificationWakeUpCoordinator(headsUpManager, statusBarStateController, keyguardBypassController, dozeParameters, unlockedScreenOffAnimationController);
    }
}
