package com.android.systemui.wmshell;

import android.content.Context;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.tracing.ProtoTracer;
import com.android.wm.shell.ShellCommandHandler;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutout;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.onehanded.OneHanded;
import com.android.wm.shell.pip.Pip;
import dagger.internal.Factory;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShell_Factory implements Factory<WMShell> {
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<Optional<HideDisplayCutout>> hideDisplayCutoutOptionalProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<NavigationModeController> navigationModeControllerProvider;
    private final Provider<Optional<OneHanded>> oneHandedOptionalProvider;
    private final Provider<Optional<Pip>> pipOptionalProvider;
    private final Provider<ProtoTracer> protoTracerProvider;
    private final Provider<ScreenLifecycle> screenLifecycleProvider;
    private final Provider<Optional<ShellCommandHandler>> shellCommandHandlerProvider;
    private final Provider<Optional<LegacySplitScreen>> splitScreenOptionalProvider;
    private final Provider<Executor> sysUiMainExecutorProvider;
    private final Provider<SysUiState> sysUiStateProvider;
    private final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public WMShell_Factory(Provider<Context> provider, Provider<Optional<Pip>> provider2, Provider<Optional<LegacySplitScreen>> provider3, Provider<Optional<OneHanded>> provider4, Provider<Optional<HideDisplayCutout>> provider5, Provider<Optional<ShellCommandHandler>> provider6, Provider<CommandQueue> provider7, Provider<ConfigurationController> provider8, Provider<KeyguardUpdateMonitor> provider9, Provider<NavigationModeController> provider10, Provider<ScreenLifecycle> provider11, Provider<SysUiState> provider12, Provider<ProtoTracer> provider13, Provider<WakefulnessLifecycle> provider14, Provider<Executor> provider15) {
        this.contextProvider = provider;
        this.pipOptionalProvider = provider2;
        this.splitScreenOptionalProvider = provider3;
        this.oneHandedOptionalProvider = provider4;
        this.hideDisplayCutoutOptionalProvider = provider5;
        this.shellCommandHandlerProvider = provider6;
        this.commandQueueProvider = provider7;
        this.configurationControllerProvider = provider8;
        this.keyguardUpdateMonitorProvider = provider9;
        this.navigationModeControllerProvider = provider10;
        this.screenLifecycleProvider = provider11;
        this.sysUiStateProvider = provider12;
        this.protoTracerProvider = provider13;
        this.wakefulnessLifecycleProvider = provider14;
        this.sysUiMainExecutorProvider = provider15;
    }

    @Override // javax.inject.Provider
    public WMShell get() {
        return newInstance(this.contextProvider.get(), this.pipOptionalProvider.get(), this.splitScreenOptionalProvider.get(), this.oneHandedOptionalProvider.get(), this.hideDisplayCutoutOptionalProvider.get(), this.shellCommandHandlerProvider.get(), this.commandQueueProvider.get(), this.configurationControllerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.navigationModeControllerProvider.get(), this.screenLifecycleProvider.get(), this.sysUiStateProvider.get(), this.protoTracerProvider.get(), this.wakefulnessLifecycleProvider.get(), this.sysUiMainExecutorProvider.get());
    }

    public static WMShell_Factory create(Provider<Context> provider, Provider<Optional<Pip>> provider2, Provider<Optional<LegacySplitScreen>> provider3, Provider<Optional<OneHanded>> provider4, Provider<Optional<HideDisplayCutout>> provider5, Provider<Optional<ShellCommandHandler>> provider6, Provider<CommandQueue> provider7, Provider<ConfigurationController> provider8, Provider<KeyguardUpdateMonitor> provider9, Provider<NavigationModeController> provider10, Provider<ScreenLifecycle> provider11, Provider<SysUiState> provider12, Provider<ProtoTracer> provider13, Provider<WakefulnessLifecycle> provider14, Provider<Executor> provider15) {
        return new WMShell_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15);
    }

    public static WMShell newInstance(Context context, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<OneHanded> optional3, Optional<HideDisplayCutout> optional4, Optional<ShellCommandHandler> optional5, CommandQueue commandQueue, ConfigurationController configurationController, KeyguardUpdateMonitor keyguardUpdateMonitor, NavigationModeController navigationModeController, ScreenLifecycle screenLifecycle, SysUiState sysUiState, ProtoTracer protoTracer, WakefulnessLifecycle wakefulnessLifecycle, Executor executor) {
        return new WMShell(context, optional, optional2, optional3, optional4, optional5, commandQueue, configurationController, keyguardUpdateMonitor, navigationModeController, screenLifecycle, sysUiState, protoTracer, wakefulnessLifecycle, executor);
    }
}
