package com.google.android.systemui;

import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.Handler;
import android.os.UserManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationClickNotifier;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.google.android.systemui.smartspace.SmartSpaceController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class NotificationLockscreenUserManagerGoogle_Factory implements Factory<NotificationLockscreenUserManagerGoogle> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<NotificationClickNotifier> clickNotifierProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DevicePolicyManager> devicePolicyManagerProvider;
    private final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    private final Provider<KeyguardBypassController> keyguardBypassControllerProvider;
    private final Provider<KeyguardManager> keyguardManagerProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<SmartSpaceController> smartSpaceControllerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<UserManager> userManagerProvider;

    public NotificationLockscreenUserManagerGoogle_Factory(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<DevicePolicyManager> provider3, Provider<UserManager> provider4, Provider<NotificationClickNotifier> provider5, Provider<KeyguardManager> provider6, Provider<StatusBarStateController> provider7, Provider<Handler> provider8, Provider<DeviceProvisionedController> provider9, Provider<KeyguardStateController> provider10, Provider<KeyguardBypassController> provider11, Provider<SmartSpaceController> provider12) {
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
        this.keyguardBypassControllerProvider = provider11;
        this.smartSpaceControllerProvider = provider12;
    }

    @Override // javax.inject.Provider
    public NotificationLockscreenUserManagerGoogle get() {
        return newInstance(this.contextProvider.get(), this.broadcastDispatcherProvider.get(), this.devicePolicyManagerProvider.get(), this.userManagerProvider.get(), this.clickNotifierProvider.get(), this.keyguardManagerProvider.get(), this.statusBarStateControllerProvider.get(), this.mainHandlerProvider.get(), this.deviceProvisionedControllerProvider.get(), this.keyguardStateControllerProvider.get(), DoubleCheck.lazy(this.keyguardBypassControllerProvider), this.smartSpaceControllerProvider.get());
    }

    public static NotificationLockscreenUserManagerGoogle_Factory create(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<DevicePolicyManager> provider3, Provider<UserManager> provider4, Provider<NotificationClickNotifier> provider5, Provider<KeyguardManager> provider6, Provider<StatusBarStateController> provider7, Provider<Handler> provider8, Provider<DeviceProvisionedController> provider9, Provider<KeyguardStateController> provider10, Provider<KeyguardBypassController> provider11, Provider<SmartSpaceController> provider12) {
        return new NotificationLockscreenUserManagerGoogle_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12);
    }

    public static NotificationLockscreenUserManagerGoogle newInstance(Context context, BroadcastDispatcher broadcastDispatcher, DevicePolicyManager devicePolicyManager, UserManager userManager, NotificationClickNotifier notificationClickNotifier, KeyguardManager keyguardManager, StatusBarStateController statusBarStateController, Handler handler, DeviceProvisionedController deviceProvisionedController, KeyguardStateController keyguardStateController, Lazy<KeyguardBypassController> lazy, SmartSpaceController smartSpaceController) {
        return new NotificationLockscreenUserManagerGoogle(context, broadcastDispatcher, devicePolicyManager, userManager, notificationClickNotifier, keyguardManager, statusBarStateController, handler, deviceProvisionedController, keyguardStateController, lazy, smartSpaceController);
    }
}
