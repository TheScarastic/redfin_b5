package com.android.systemui.recents;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.shared.system.smartspace.SmartspaceTransitionController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.onehanded.OneHanded;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.splitscreen.SplitScreen;
import com.android.wm.shell.startingsurface.StartingSurface;
import com.android.wm.shell.transition.ShellTransitions;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class OverviewProxyService_Factory implements Factory<OverviewProxyService> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<Context> contextProvider;
    private final Provider<Optional<LegacySplitScreen>> legacySplitScreenOptionalProvider;
    private final Provider<NavigationBarController> navBarControllerLazyProvider;
    private final Provider<NavigationModeController> navModeControllerProvider;
    private final Provider<Optional<OneHanded>> oneHandedOptionalProvider;
    private final Provider<Optional<Pip>> pipOptionalProvider;
    private final Provider<ShellTransitions> shellTransitionsProvider;
    private final Provider<SmartspaceTransitionController> smartspaceTransitionControllerProvider;
    private final Provider<Optional<SplitScreen>> splitScreenOptionalProvider;
    private final Provider<Optional<StartingSurface>> startingSurfaceProvider;
    private final Provider<Optional<Lazy<StatusBar>>> statusBarOptionalLazyProvider;
    private final Provider<NotificationShadeWindowController> statusBarWinControllerProvider;
    private final Provider<SysUiState> sysUiStateProvider;

    public OverviewProxyService_Factory(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<NavigationBarController> provider3, Provider<NavigationModeController> provider4, Provider<NotificationShadeWindowController> provider5, Provider<SysUiState> provider6, Provider<Optional<Pip>> provider7, Provider<Optional<LegacySplitScreen>> provider8, Provider<Optional<SplitScreen>> provider9, Provider<Optional<Lazy<StatusBar>>> provider10, Provider<Optional<OneHanded>> provider11, Provider<BroadcastDispatcher> provider12, Provider<ShellTransitions> provider13, Provider<Optional<StartingSurface>> provider14, Provider<SmartspaceTransitionController> provider15) {
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
        this.navBarControllerLazyProvider = provider3;
        this.navModeControllerProvider = provider4;
        this.statusBarWinControllerProvider = provider5;
        this.sysUiStateProvider = provider6;
        this.pipOptionalProvider = provider7;
        this.legacySplitScreenOptionalProvider = provider8;
        this.splitScreenOptionalProvider = provider9;
        this.statusBarOptionalLazyProvider = provider10;
        this.oneHandedOptionalProvider = provider11;
        this.broadcastDispatcherProvider = provider12;
        this.shellTransitionsProvider = provider13;
        this.startingSurfaceProvider = provider14;
        this.smartspaceTransitionControllerProvider = provider15;
    }

    @Override // javax.inject.Provider
    public OverviewProxyService get() {
        return newInstance(this.contextProvider.get(), this.commandQueueProvider.get(), DoubleCheck.lazy(this.navBarControllerLazyProvider), this.navModeControllerProvider.get(), this.statusBarWinControllerProvider.get(), this.sysUiStateProvider.get(), this.pipOptionalProvider.get(), this.legacySplitScreenOptionalProvider.get(), this.splitScreenOptionalProvider.get(), this.statusBarOptionalLazyProvider.get(), this.oneHandedOptionalProvider.get(), this.broadcastDispatcherProvider.get(), this.shellTransitionsProvider.get(), this.startingSurfaceProvider.get(), this.smartspaceTransitionControllerProvider.get());
    }

    public static OverviewProxyService_Factory create(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<NavigationBarController> provider3, Provider<NavigationModeController> provider4, Provider<NotificationShadeWindowController> provider5, Provider<SysUiState> provider6, Provider<Optional<Pip>> provider7, Provider<Optional<LegacySplitScreen>> provider8, Provider<Optional<SplitScreen>> provider9, Provider<Optional<Lazy<StatusBar>>> provider10, Provider<Optional<OneHanded>> provider11, Provider<BroadcastDispatcher> provider12, Provider<ShellTransitions> provider13, Provider<Optional<StartingSurface>> provider14, Provider<SmartspaceTransitionController> provider15) {
        return new OverviewProxyService_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15);
    }

    public static OverviewProxyService newInstance(Context context, CommandQueue commandQueue, Lazy<NavigationBarController> lazy, NavigationModeController navigationModeController, NotificationShadeWindowController notificationShadeWindowController, SysUiState sysUiState, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<Lazy<StatusBar>> optional4, Optional<OneHanded> optional5, BroadcastDispatcher broadcastDispatcher, ShellTransitions shellTransitions, Optional<StartingSurface> optional6, SmartspaceTransitionController smartspaceTransitionController) {
        return new OverviewProxyService(context, commandQueue, lazy, navigationModeController, notificationShadeWindowController, sysUiState, optional, optional2, optional3, optional4, optional5, broadcastDispatcher, shellTransitions, optional6, smartspaceTransitionController);
    }
}
