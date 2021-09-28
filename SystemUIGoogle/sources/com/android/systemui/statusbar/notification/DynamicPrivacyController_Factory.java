package com.android.systemui.statusbar.notification;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DynamicPrivacyController_Factory implements Factory<DynamicPrivacyController> {
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<NotificationLockscreenUserManager> notificationLockscreenUserManagerProvider;
    private final Provider<StatusBarStateController> stateControllerProvider;

    public DynamicPrivacyController_Factory(Provider<NotificationLockscreenUserManager> provider, Provider<KeyguardStateController> provider2, Provider<StatusBarStateController> provider3) {
        this.notificationLockscreenUserManagerProvider = provider;
        this.keyguardStateControllerProvider = provider2;
        this.stateControllerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public DynamicPrivacyController get() {
        return newInstance(this.notificationLockscreenUserManagerProvider.get(), this.keyguardStateControllerProvider.get(), this.stateControllerProvider.get());
    }

    public static DynamicPrivacyController_Factory create(Provider<NotificationLockscreenUserManager> provider, Provider<KeyguardStateController> provider2, Provider<StatusBarStateController> provider3) {
        return new DynamicPrivacyController_Factory(provider, provider2, provider3);
    }

    public static DynamicPrivacyController newInstance(NotificationLockscreenUserManager notificationLockscreenUserManager, KeyguardStateController keyguardStateController, StatusBarStateController statusBarStateController) {
        return new DynamicPrivacyController(notificationLockscreenUserManager, keyguardStateController, statusBarStateController);
    }
}
