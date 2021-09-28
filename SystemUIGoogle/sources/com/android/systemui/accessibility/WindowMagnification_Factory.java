package com.android.systemui.accessibility;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.model.SysUiState;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.CommandQueue;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WindowMagnification_Factory implements Factory<WindowMagnification> {
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<Context> contextProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<ModeSwitchesController> modeSwitchesControllerProvider;
    private final Provider<OverviewProxyService> overviewProxyServiceProvider;
    private final Provider<SysUiState> sysUiStateProvider;

    public WindowMagnification_Factory(Provider<Context> provider, Provider<Handler> provider2, Provider<CommandQueue> provider3, Provider<ModeSwitchesController> provider4, Provider<SysUiState> provider5, Provider<OverviewProxyService> provider6) {
        this.contextProvider = provider;
        this.mainHandlerProvider = provider2;
        this.commandQueueProvider = provider3;
        this.modeSwitchesControllerProvider = provider4;
        this.sysUiStateProvider = provider5;
        this.overviewProxyServiceProvider = provider6;
    }

    @Override // javax.inject.Provider
    public WindowMagnification get() {
        return newInstance(this.contextProvider.get(), this.mainHandlerProvider.get(), this.commandQueueProvider.get(), this.modeSwitchesControllerProvider.get(), this.sysUiStateProvider.get(), this.overviewProxyServiceProvider.get());
    }

    public static WindowMagnification_Factory create(Provider<Context> provider, Provider<Handler> provider2, Provider<CommandQueue> provider3, Provider<ModeSwitchesController> provider4, Provider<SysUiState> provider5, Provider<OverviewProxyService> provider6) {
        return new WindowMagnification_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static WindowMagnification newInstance(Context context, Handler handler, CommandQueue commandQueue, ModeSwitchesController modeSwitchesController, SysUiState sysUiState, OverviewProxyService overviewProxyService) {
        return new WindowMagnification(context, handler, commandQueue, modeSwitchesController, sysUiState, overviewProxyService);
    }
}
