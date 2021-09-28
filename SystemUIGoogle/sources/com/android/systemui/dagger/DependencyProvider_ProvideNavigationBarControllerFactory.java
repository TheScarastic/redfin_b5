package com.android.systemui.dagger;

import android.content.Context;
import android.os.Handler;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.accessibility.SystemActions;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationBarOverlayController;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.recents.Recents;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.AccessibilityManagerWrapper;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.pip.Pip;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvideNavigationBarControllerFactory implements Factory<NavigationBarController> {
    private final Provider<AccessibilityButtonModeObserver> accessibilityButtonModeObserverProvider;
    private final Provider<AccessibilityManager> accessibilityManagerProvider;
    private final Provider<AccessibilityManagerWrapper> accessibilityManagerWrapperProvider;
    private final Provider<AssistManager> assistManagerLazyProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<MetricsLogger> metricsLoggerProvider;
    private final DependencyProvider module;
    private final Provider<NavigationBarOverlayController> navBarOverlayControllerProvider;
    private final Provider<NavigationModeController> navigationModeControllerProvider;
    private final Provider<NotificationRemoteInputManager> notificationRemoteInputManagerProvider;
    private final Provider<NotificationShadeDepthController> notificationShadeDepthControllerProvider;
    private final Provider<OverviewProxyService> overviewProxyServiceProvider;
    private final Provider<Optional<Pip>> pipOptionalProvider;
    private final Provider<Optional<Recents>> recentsOptionalProvider;
    private final Provider<ShadeController> shadeControllerProvider;
    private final Provider<Optional<LegacySplitScreen>> splitScreenOptionalProvider;
    private final Provider<StatusBar> statusBarLazyProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<SysUiState> sysUiFlagsContainerProvider;
    private final Provider<SystemActions> systemActionsProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<UserTracker> userTrackerProvider;
    private final Provider<WindowManager> windowManagerProvider;

    public DependencyProvider_ProvideNavigationBarControllerFactory(DependencyProvider dependencyProvider, Provider<Context> provider, Provider<WindowManager> provider2, Provider<AssistManager> provider3, Provider<AccessibilityManager> provider4, Provider<AccessibilityManagerWrapper> provider5, Provider<DeviceProvisionedController> provider6, Provider<MetricsLogger> provider7, Provider<OverviewProxyService> provider8, Provider<NavigationModeController> provider9, Provider<AccessibilityButtonModeObserver> provider10, Provider<StatusBarStateController> provider11, Provider<SysUiState> provider12, Provider<BroadcastDispatcher> provider13, Provider<CommandQueue> provider14, Provider<Optional<Pip>> provider15, Provider<Optional<LegacySplitScreen>> provider16, Provider<Optional<Recents>> provider17, Provider<StatusBar> provider18, Provider<ShadeController> provider19, Provider<NotificationRemoteInputManager> provider20, Provider<NotificationShadeDepthController> provider21, Provider<SystemActions> provider22, Provider<Handler> provider23, Provider<UiEventLogger> provider24, Provider<NavigationBarOverlayController> provider25, Provider<ConfigurationController> provider26, Provider<UserTracker> provider27) {
        this.module = dependencyProvider;
        this.contextProvider = provider;
        this.windowManagerProvider = provider2;
        this.assistManagerLazyProvider = provider3;
        this.accessibilityManagerProvider = provider4;
        this.accessibilityManagerWrapperProvider = provider5;
        this.deviceProvisionedControllerProvider = provider6;
        this.metricsLoggerProvider = provider7;
        this.overviewProxyServiceProvider = provider8;
        this.navigationModeControllerProvider = provider9;
        this.accessibilityButtonModeObserverProvider = provider10;
        this.statusBarStateControllerProvider = provider11;
        this.sysUiFlagsContainerProvider = provider12;
        this.broadcastDispatcherProvider = provider13;
        this.commandQueueProvider = provider14;
        this.pipOptionalProvider = provider15;
        this.splitScreenOptionalProvider = provider16;
        this.recentsOptionalProvider = provider17;
        this.statusBarLazyProvider = provider18;
        this.shadeControllerProvider = provider19;
        this.notificationRemoteInputManagerProvider = provider20;
        this.notificationShadeDepthControllerProvider = provider21;
        this.systemActionsProvider = provider22;
        this.mainHandlerProvider = provider23;
        this.uiEventLoggerProvider = provider24;
        this.navBarOverlayControllerProvider = provider25;
        this.configurationControllerProvider = provider26;
        this.userTrackerProvider = provider27;
    }

    @Override // javax.inject.Provider
    public NavigationBarController get() {
        return provideNavigationBarController(this.module, this.contextProvider.get(), this.windowManagerProvider.get(), DoubleCheck.lazy(this.assistManagerLazyProvider), this.accessibilityManagerProvider.get(), this.accessibilityManagerWrapperProvider.get(), this.deviceProvisionedControllerProvider.get(), this.metricsLoggerProvider.get(), this.overviewProxyServiceProvider.get(), this.navigationModeControllerProvider.get(), this.accessibilityButtonModeObserverProvider.get(), this.statusBarStateControllerProvider.get(), this.sysUiFlagsContainerProvider.get(), this.broadcastDispatcherProvider.get(), this.commandQueueProvider.get(), this.pipOptionalProvider.get(), this.splitScreenOptionalProvider.get(), this.recentsOptionalProvider.get(), DoubleCheck.lazy(this.statusBarLazyProvider), this.shadeControllerProvider.get(), this.notificationRemoteInputManagerProvider.get(), this.notificationShadeDepthControllerProvider.get(), this.systemActionsProvider.get(), this.mainHandlerProvider.get(), this.uiEventLoggerProvider.get(), this.navBarOverlayControllerProvider.get(), this.configurationControllerProvider.get(), this.userTrackerProvider.get());
    }

    public static DependencyProvider_ProvideNavigationBarControllerFactory create(DependencyProvider dependencyProvider, Provider<Context> provider, Provider<WindowManager> provider2, Provider<AssistManager> provider3, Provider<AccessibilityManager> provider4, Provider<AccessibilityManagerWrapper> provider5, Provider<DeviceProvisionedController> provider6, Provider<MetricsLogger> provider7, Provider<OverviewProxyService> provider8, Provider<NavigationModeController> provider9, Provider<AccessibilityButtonModeObserver> provider10, Provider<StatusBarStateController> provider11, Provider<SysUiState> provider12, Provider<BroadcastDispatcher> provider13, Provider<CommandQueue> provider14, Provider<Optional<Pip>> provider15, Provider<Optional<LegacySplitScreen>> provider16, Provider<Optional<Recents>> provider17, Provider<StatusBar> provider18, Provider<ShadeController> provider19, Provider<NotificationRemoteInputManager> provider20, Provider<NotificationShadeDepthController> provider21, Provider<SystemActions> provider22, Provider<Handler> provider23, Provider<UiEventLogger> provider24, Provider<NavigationBarOverlayController> provider25, Provider<ConfigurationController> provider26, Provider<UserTracker> provider27) {
        return new DependencyProvider_ProvideNavigationBarControllerFactory(dependencyProvider, provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27);
    }

    public static NavigationBarController provideNavigationBarController(DependencyProvider dependencyProvider, Context context, WindowManager windowManager, Lazy<AssistManager> lazy, AccessibilityManager accessibilityManager, AccessibilityManagerWrapper accessibilityManagerWrapper, DeviceProvisionedController deviceProvisionedController, MetricsLogger metricsLogger, OverviewProxyService overviewProxyService, NavigationModeController navigationModeController, AccessibilityButtonModeObserver accessibilityButtonModeObserver, StatusBarStateController statusBarStateController, SysUiState sysUiState, BroadcastDispatcher broadcastDispatcher, CommandQueue commandQueue, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<Recents> optional3, Lazy<StatusBar> lazy2, ShadeController shadeController, NotificationRemoteInputManager notificationRemoteInputManager, NotificationShadeDepthController notificationShadeDepthController, SystemActions systemActions, Handler handler, UiEventLogger uiEventLogger, NavigationBarOverlayController navigationBarOverlayController, ConfigurationController configurationController, UserTracker userTracker) {
        return (NavigationBarController) Preconditions.checkNotNullFromProvides(dependencyProvider.provideNavigationBarController(context, windowManager, lazy, accessibilityManager, accessibilityManagerWrapper, deviceProvisionedController, metricsLogger, overviewProxyService, navigationModeController, accessibilityButtonModeObserver, statusBarStateController, sysUiState, broadcastDispatcher, commandQueue, optional, optional2, optional3, lazy2, shadeController, notificationRemoteInputManager, notificationShadeDepthController, systemActions, handler, uiEventLogger, navigationBarOverlayController, configurationController, userTracker));
    }
}
