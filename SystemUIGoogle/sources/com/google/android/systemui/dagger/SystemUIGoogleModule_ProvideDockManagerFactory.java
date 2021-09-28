package com.google.android.systemui.dagger;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dock.DockManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SystemUIGoogleModule_ProvideDockManagerFactory implements Factory<DockManager> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DelayableExecutor> mainExecutorProvider;
    private final Provider<NotificationInterruptStateProvider> notificationInterruptionStateProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public SystemUIGoogleModule_ProvideDockManagerFactory(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<StatusBarStateController> provider3, Provider<NotificationInterruptStateProvider> provider4, Provider<ConfigurationController> provider5, Provider<DelayableExecutor> provider6) {
        this.contextProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.statusBarStateControllerProvider = provider3;
        this.notificationInterruptionStateProvider = provider4;
        this.configurationControllerProvider = provider5;
        this.mainExecutorProvider = provider6;
    }

    @Override // javax.inject.Provider
    public DockManager get() {
        return provideDockManager(this.contextProvider.get(), this.broadcastDispatcherProvider.get(), this.statusBarStateControllerProvider.get(), this.notificationInterruptionStateProvider.get(), this.configurationControllerProvider.get(), this.mainExecutorProvider.get());
    }

    public static SystemUIGoogleModule_ProvideDockManagerFactory create(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<StatusBarStateController> provider3, Provider<NotificationInterruptStateProvider> provider4, Provider<ConfigurationController> provider5, Provider<DelayableExecutor> provider6) {
        return new SystemUIGoogleModule_ProvideDockManagerFactory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static DockManager provideDockManager(Context context, BroadcastDispatcher broadcastDispatcher, StatusBarStateController statusBarStateController, NotificationInterruptStateProvider notificationInterruptStateProvider, ConfigurationController configurationController, DelayableExecutor delayableExecutor) {
        return (DockManager) Preconditions.checkNotNullFromProvides(SystemUIGoogleModule.provideDockManager(context, broadcastDispatcher, statusBarStateController, notificationInterruptStateProvider, configurationController, delayableExecutor));
    }
}
