package com.android.systemui.statusbar.notification.collection.coordinator;

import android.content.Context;
import android.os.Handler;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardCoordinator_Factory implements Factory<KeyguardCoordinator> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;
    private final Provider<HighPriorityProvider> highPriorityProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<NotificationLockscreenUserManager> lockscreenUserManagerProvider;
    private final Provider<Handler> mainThreadHandlerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public KeyguardCoordinator_Factory(Provider<Context> provider, Provider<Handler> provider2, Provider<KeyguardStateController> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<BroadcastDispatcher> provider5, Provider<StatusBarStateController> provider6, Provider<KeyguardUpdateMonitor> provider7, Provider<HighPriorityProvider> provider8) {
        this.contextProvider = provider;
        this.mainThreadHandlerProvider = provider2;
        this.keyguardStateControllerProvider = provider3;
        this.lockscreenUserManagerProvider = provider4;
        this.broadcastDispatcherProvider = provider5;
        this.statusBarStateControllerProvider = provider6;
        this.keyguardUpdateMonitorProvider = provider7;
        this.highPriorityProvider = provider8;
    }

    @Override // javax.inject.Provider
    public KeyguardCoordinator get() {
        return newInstance(this.contextProvider.get(), this.mainThreadHandlerProvider.get(), this.keyguardStateControllerProvider.get(), this.lockscreenUserManagerProvider.get(), this.broadcastDispatcherProvider.get(), this.statusBarStateControllerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.highPriorityProvider.get());
    }

    public static KeyguardCoordinator_Factory create(Provider<Context> provider, Provider<Handler> provider2, Provider<KeyguardStateController> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<BroadcastDispatcher> provider5, Provider<StatusBarStateController> provider6, Provider<KeyguardUpdateMonitor> provider7, Provider<HighPriorityProvider> provider8) {
        return new KeyguardCoordinator_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    public static KeyguardCoordinator newInstance(Context context, Handler handler, KeyguardStateController keyguardStateController, NotificationLockscreenUserManager notificationLockscreenUserManager, BroadcastDispatcher broadcastDispatcher, StatusBarStateController statusBarStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, HighPriorityProvider highPriorityProvider) {
        return new KeyguardCoordinator(context, handler, keyguardStateController, notificationLockscreenUserManager, broadcastDispatcher, statusBarStateController, keyguardUpdateMonitor, highPriorityProvider);
    }
}
