package com.android.systemui.wmshell;

import android.content.Context;
import android.content.pm.LauncherApps;
import android.os.Handler;
import android.view.WindowManager;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.WindowManagerShellWrapper;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.FloatingContentCoordinator;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TaskStackListenerImpl;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideBubbleControllerFactory implements Factory<Optional<BubbleController>> {
    private final Provider<Context> contextProvider;
    private final Provider<DisplayController> displayControllerProvider;
    private final Provider<FloatingContentCoordinator> floatingContentCoordinatorProvider;
    private final Provider<LauncherApps> launcherAppsProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<ShellTaskOrganizer> organizerProvider;
    private final Provider<IStatusBarService> statusBarServiceProvider;
    private final Provider<TaskStackListenerImpl> taskStackListenerProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<WindowManager> windowManagerProvider;
    private final Provider<WindowManagerShellWrapper> windowManagerShellWrapperProvider;

    public WMShellBaseModule_ProvideBubbleControllerFactory(Provider<Context> provider, Provider<FloatingContentCoordinator> provider2, Provider<IStatusBarService> provider3, Provider<WindowManager> provider4, Provider<WindowManagerShellWrapper> provider5, Provider<LauncherApps> provider6, Provider<TaskStackListenerImpl> provider7, Provider<UiEventLogger> provider8, Provider<ShellTaskOrganizer> provider9, Provider<DisplayController> provider10, Provider<ShellExecutor> provider11, Provider<Handler> provider12) {
        this.contextProvider = provider;
        this.floatingContentCoordinatorProvider = provider2;
        this.statusBarServiceProvider = provider3;
        this.windowManagerProvider = provider4;
        this.windowManagerShellWrapperProvider = provider5;
        this.launcherAppsProvider = provider6;
        this.taskStackListenerProvider = provider7;
        this.uiEventLoggerProvider = provider8;
        this.organizerProvider = provider9;
        this.displayControllerProvider = provider10;
        this.mainExecutorProvider = provider11;
        this.mainHandlerProvider = provider12;
    }

    @Override // javax.inject.Provider
    public Optional<BubbleController> get() {
        return provideBubbleController(this.contextProvider.get(), this.floatingContentCoordinatorProvider.get(), this.statusBarServiceProvider.get(), this.windowManagerProvider.get(), this.windowManagerShellWrapperProvider.get(), this.launcherAppsProvider.get(), this.taskStackListenerProvider.get(), this.uiEventLoggerProvider.get(), this.organizerProvider.get(), this.displayControllerProvider.get(), this.mainExecutorProvider.get(), this.mainHandlerProvider.get());
    }

    public static WMShellBaseModule_ProvideBubbleControllerFactory create(Provider<Context> provider, Provider<FloatingContentCoordinator> provider2, Provider<IStatusBarService> provider3, Provider<WindowManager> provider4, Provider<WindowManagerShellWrapper> provider5, Provider<LauncherApps> provider6, Provider<TaskStackListenerImpl> provider7, Provider<UiEventLogger> provider8, Provider<ShellTaskOrganizer> provider9, Provider<DisplayController> provider10, Provider<ShellExecutor> provider11, Provider<Handler> provider12) {
        return new WMShellBaseModule_ProvideBubbleControllerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12);
    }

    public static Optional<BubbleController> provideBubbleController(Context context, FloatingContentCoordinator floatingContentCoordinator, IStatusBarService iStatusBarService, WindowManager windowManager, WindowManagerShellWrapper windowManagerShellWrapper, LauncherApps launcherApps, TaskStackListenerImpl taskStackListenerImpl, UiEventLogger uiEventLogger, ShellTaskOrganizer shellTaskOrganizer, DisplayController displayController, ShellExecutor shellExecutor, Handler handler) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideBubbleController(context, floatingContentCoordinator, iStatusBarService, windowManager, windowManagerShellWrapper, launcherApps, taskStackListenerImpl, uiEventLogger, shellTaskOrganizer, displayController, shellExecutor, handler));
    }
}
