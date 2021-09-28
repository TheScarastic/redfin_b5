package com.android.systemui.statusbar;

import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.Handler;
import android.os.UserManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationLockscreenUserManagerImpl_Factory implements Factory<NotificationLockscreenUserManagerImpl> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<NotificationClickNotifier> clickNotifierProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DevicePolicyManager> devicePolicyManagerProvider;
    private final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    private final Provider<KeyguardManager> keyguardManagerProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<UserManager> userManagerProvider;

    public NotificationLockscreenUserManagerImpl_Factory(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<DevicePolicyManager> provider3, Provider<UserManager> provider4, Provider<NotificationClickNotifier> provider5, Provider<KeyguardManager> provider6, Provider<StatusBarStateController> provider7, Provider<Handler> provider8, Provider<DeviceProvisionedController> provider9, Provider<KeyguardStateController> provider10) {
        this.contextProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.devicePolicyManagerProvider = provider3;
        this.userManagerProvider = provider4;
        this.clickNotifierProvider = provider5;
        this.keyguardManagerProvider = provider6;
        this.statusBarStateControllerProvider = provider7;
        this.mainHandlerProvider = provider8;
        this.deviceProvisionedControllerProvider = provider9;
        this.keyguardStateControllerProvider = provider10;
    }

    @Override // javax.inject.Provider
    public NotificationLockscreenUserManagerImpl get() {
        return newInstance(this.contextProvider.get(), this.broadcastDispatcherProvider.get(), this.devicePolicyManagerProvider.get(), this.userManagerProvider.get(), this.clickNotifierProvider.get(), this.keyguardManagerProvider.get(), this.statusBarStateControllerProvider.get(), this.mainHandlerProvider.get(), this.deviceProvisionedControllerProvider.get(), this.keyguardStateControllerProvider.get());
    }

    public static NotificationLockscreenUserManagerImpl_Factory create(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<DevicePolicyManager> provider3, Provider<UserManager> provider4, Provider<NotificationClickNotifier> provider5, Provider<KeyguardManager> provider6, Provider<StatusBarStateController> provider7, Provider<Handler> provider8, Provider<DeviceProvisionedController> provider9, Provider<KeyguardStateController> provider10) {
        return new NotificationLockscreenUserManagerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static NotificationLockscreenUserManagerImpl newInstance(Context context, BroadcastDispatcher broadcastDispatcher, DevicePolicyManager devicePolicyManager, UserManager userManager, NotificationClickNotifier notificationClickNotifier, KeyguardManager keyguardManager, StatusBarStateController statusBarStateController, Handler handler, DeviceProvisionedController deviceProvisionedController, KeyguardStateController keyguardStateController) {
        return new NotificationLockscreenUserManagerImpl(context, broadcastDispatcher, devicePolicyManager, userManager, notificationClickNotifier, keyguardManager, statusBarStateController, handler, deviceProvisionedController, keyguardStateController);
    }
}
