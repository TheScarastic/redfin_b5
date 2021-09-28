package com.google.android.systemui.assist;

import android.content.Context;
import android.os.Handler;
import android.view.IWindowManager;
import com.android.internal.app.AssistUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.assist.AssistLogger;
import com.android.systemui.assist.PhoneStateMonitor;
import com.android.systemui.assist.ui.DefaultUiController;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.google.android.systemui.assist.uihints.AssistantPresenceHandler;
import com.google.android.systemui.assist.uihints.GoogleDefaultUiController;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import com.google.android.systemui.assist.uihints.NgaUiController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class AssistManagerGoogle_Factory implements Factory<AssistManagerGoogle> {
    private final Provider<AssistLogger> assistLoggerProvider;
    private final Provider<AssistUtils> assistUtilsProvider;
    private final Provider<AssistantPresenceHandler> assistantPresenceHandlerProvider;
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DeviceProvisionedController> controllerProvider;
    private final Provider<DefaultUiController> defaultUiControllerProvider;
    private final Provider<GoogleDefaultUiController> googleDefaultUiControllerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<NavigationModeController> navigationModeControllerProvider;
    private final Provider<NgaMessageHandler> ngaMessageHandlerProvider;
    private final Provider<NgaUiController> ngaUiControllerProvider;
    private final Provider<OpaEnabledDispatcher> opaEnabledDispatcherProvider;
    private final Provider<OpaEnabledReceiver> opaEnabledReceiverProvider;
    private final Provider<OverviewProxyService> overviewProxyServiceProvider;
    private final Provider<PhoneStateMonitor> phoneStateMonitorProvider;
    private final Provider<SysUiState> sysUiStateProvider;
    private final Provider<Handler> uiHandlerProvider;
    private final Provider<IWindowManager> windowManagerServiceProvider;

    public AssistManagerGoogle_Factory(Provider<DeviceProvisionedController> provider, Provider<Context> provider2, Provider<AssistUtils> provider3, Provider<NgaUiController> provider4, Provider<CommandQueue> provider5, Provider<OpaEnabledReceiver> provider6, Provider<PhoneStateMonitor> provider7, Provider<OverviewProxyService> provider8, Provider<OpaEnabledDispatcher> provider9, Provider<KeyguardUpdateMonitor> provider10, Provider<NavigationModeController> provider11, Provider<ConfigurationController> provider12, Provider<AssistantPresenceHandler> provider13, Provider<NgaMessageHandler> provider14, Provider<SysUiState> provider15, Provider<Handler> provider16, Provider<DefaultUiController> provider17, Provider<GoogleDefaultUiController> provider18, Provider<IWindowManager> provider19, Provider<AssistLogger> provider20) {
        this.controllerProvider = provider;
        this.contextProvider = provider2;
        this.assistUtilsProvider = provider3;
        this.ngaUiControllerProvider = provider4;
        this.commandQueueProvider = provider5;
        this.opaEnabledReceiverProvider = provider6;
        this.phoneStateMonitorProvider = provider7;
        this.overviewProxyServiceProvider = provider8;
        this.opaEnabledDispatcherProvider = provider9;
        this.keyguardUpdateMonitorProvider = provider10;
        this.navigationModeControllerProvider = provider11;
        this.configurationControllerProvider = provider12;
        this.assistantPresenceHandlerProvider = provider13;
        this.ngaMessageHandlerProvider = provider14;
        this.sysUiStateProvider = provider15;
        this.uiHandlerProvider = provider16;
        this.defaultUiControllerProvider = provider17;
        this.googleDefaultUiControllerProvider = provider18;
        this.windowManagerServiceProvider = provider19;
        this.assistLoggerProvider = provider20;
    }

    @Override // javax.inject.Provider
    public AssistManagerGoogle get() {
        return newInstance(this.controllerProvider.get(), this.contextProvider.get(), this.assistUtilsProvider.get(), this.ngaUiControllerProvider.get(), this.commandQueueProvider.get(), this.opaEnabledReceiverProvider.get(), this.phoneStateMonitorProvider.get(), this.overviewProxyServiceProvider.get(), this.opaEnabledDispatcherProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.navigationModeControllerProvider.get(), this.configurationControllerProvider.get(), this.assistantPresenceHandlerProvider.get(), this.ngaMessageHandlerProvider.get(), DoubleCheck.lazy(this.sysUiStateProvider), this.uiHandlerProvider.get(), this.defaultUiControllerProvider.get(), this.googleDefaultUiControllerProvider.get(), this.windowManagerServiceProvider.get(), this.assistLoggerProvider.get());
    }

    public static AssistManagerGoogle_Factory create(Provider<DeviceProvisionedController> provider, Provider<Context> provider2, Provider<AssistUtils> provider3, Provider<NgaUiController> provider4, Provider<CommandQueue> provider5, Provider<OpaEnabledReceiver> provider6, Provider<PhoneStateMonitor> provider7, Provider<OverviewProxyService> provider8, Provider<OpaEnabledDispatcher> provider9, Provider<KeyguardUpdateMonitor> provider10, Provider<NavigationModeController> provider11, Provider<ConfigurationController> provider12, Provider<AssistantPresenceHandler> provider13, Provider<NgaMessageHandler> provider14, Provider<SysUiState> provider15, Provider<Handler> provider16, Provider<DefaultUiController> provider17, Provider<GoogleDefaultUiController> provider18, Provider<IWindowManager> provider19, Provider<AssistLogger> provider20) {
        return new AssistManagerGoogle_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20);
    }

    public static AssistManagerGoogle newInstance(DeviceProvisionedController deviceProvisionedController, Context context, AssistUtils assistUtils, NgaUiController ngaUiController, CommandQueue commandQueue, OpaEnabledReceiver opaEnabledReceiver, PhoneStateMonitor phoneStateMonitor, OverviewProxyService overviewProxyService, OpaEnabledDispatcher opaEnabledDispatcher, KeyguardUpdateMonitor keyguardUpdateMonitor, NavigationModeController navigationModeController, ConfigurationController configurationController, AssistantPresenceHandler assistantPresenceHandler, NgaMessageHandler ngaMessageHandler, Lazy<SysUiState> lazy, Handler handler, DefaultUiController defaultUiController, GoogleDefaultUiController googleDefaultUiController, IWindowManager iWindowManager, AssistLogger assistLogger) {
        return new AssistManagerGoogle(deviceProvisionedController, context, assistUtils, ngaUiController, commandQueue, opaEnabledReceiver, phoneStateMonitor, overviewProxyService, opaEnabledDispatcher, keyguardUpdateMonitor, navigationModeController, configurationController, assistantPresenceHandler, ngaMessageHandler, lazy, handler, defaultUiController, googleDefaultUiController, iWindowManager, assistLogger);
    }
}
