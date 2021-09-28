package com.google.android.systemui.gamedashboard;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.accessibility.AccessibilityManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.CommandQueue;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class EntryPointController_Factory implements Factory<EntryPointController> {
    private final Provider<AccessibilityManager> accessibilityManagerProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<Context> contextProvider;
    private final Provider<GameModeDndController> gameModeDndControllerProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<NavigationModeController> navigationModeControllerProvider;
    private final Provider<OverviewProxyService> overviewProxyServiceProvider;
    private final Provider<PackageManager> packageManagerProvider;
    private final Provider<ShortcutBarController> shortcutBarControllerProvider;
    private final Provider<Optional<TaskSurfaceHelper>> taskSurfaceHelperProvider;
    private final Provider<ToastController> toastProvider;
    private final Provider<GameDashboardUiEventLogger> uiEventLoggerProvider;

    public EntryPointController_Factory(Provider<Context> provider, Provider<AccessibilityManager> provider2, Provider<BroadcastDispatcher> provider3, Provider<CommandQueue> provider4, Provider<GameModeDndController> provider5, Provider<Handler> provider6, Provider<NavigationModeController> provider7, Provider<OverviewProxyService> provider8, Provider<PackageManager> provider9, Provider<ShortcutBarController> provider10, Provider<ToastController> provider11, Provider<GameDashboardUiEventLogger> provider12, Provider<Optional<TaskSurfaceHelper>> provider13) {
        this.contextProvider = provider;
        this.accessibilityManagerProvider = provider2;
        this.broadcastDispatcherProvider = provider3;
        this.commandQueueProvider = provider4;
        this.gameModeDndControllerProvider = provider5;
        this.mainHandlerProvider = provider6;
        this.navigationModeControllerProvider = provider7;
        this.overviewProxyServiceProvider = provider8;
        this.packageManagerProvider = provider9;
        this.shortcutBarControllerProvider = provider10;
        this.toastProvider = provider11;
        this.uiEventLoggerProvider = provider12;
        this.taskSurfaceHelperProvider = provider13;
    }

    @Override // javax.inject.Provider
    public EntryPointController get() {
        return newInstance(this.contextProvider.get(), this.accessibilityManagerProvider.get(), this.broadcastDispatcherProvider.get(), this.commandQueueProvider.get(), this.gameModeDndControllerProvider.get(), this.mainHandlerProvider.get(), this.navigationModeControllerProvider.get(), this.overviewProxyServiceProvider.get(), this.packageManagerProvider.get(), this.shortcutBarControllerProvider.get(), this.toastProvider.get(), this.uiEventLoggerProvider.get(), this.taskSurfaceHelperProvider.get());
    }

    public static EntryPointController_Factory create(Provider<Context> provider, Provider<AccessibilityManager> provider2, Provider<BroadcastDispatcher> provider3, Provider<CommandQueue> provider4, Provider<GameModeDndController> provider5, Provider<Handler> provider6, Provider<NavigationModeController> provider7, Provider<OverviewProxyService> provider8, Provider<PackageManager> provider9, Provider<ShortcutBarController> provider10, Provider<ToastController> provider11, Provider<GameDashboardUiEventLogger> provider12, Provider<Optional<TaskSurfaceHelper>> provider13) {
        return new EntryPointController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13);
    }

    public static EntryPointController newInstance(Context context, AccessibilityManager accessibilityManager, BroadcastDispatcher broadcastDispatcher, CommandQueue commandQueue, GameModeDndController gameModeDndController, Handler handler, NavigationModeController navigationModeController, OverviewProxyService overviewProxyService, PackageManager packageManager, ShortcutBarController shortcutBarController, ToastController toastController, GameDashboardUiEventLogger gameDashboardUiEventLogger, Optional<TaskSurfaceHelper> optional) {
        return new EntryPointController(context, accessibilityManager, broadcastDispatcher, commandQueue, gameModeDndController, handler, navigationModeController, overviewProxyService, packageManager, shortcutBarController, toastController, gameDashboardUiEventLogger, optional);
    }
}
