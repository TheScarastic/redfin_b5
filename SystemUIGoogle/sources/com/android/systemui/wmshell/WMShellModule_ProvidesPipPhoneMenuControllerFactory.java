package com.android.systemui.wmshell;

import android.content.Context;
import android.os.Handler;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SystemWindows;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellModule_ProvidesPipPhoneMenuControllerFactory implements Factory<PhonePipMenuController> {
    private final Provider<Context> contextProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<PipBoundsState> pipBoundsStateProvider;
    private final Provider<PipMediaController> pipMediaControllerProvider;
    private final Provider<SystemWindows> systemWindowsProvider;

    public WMShellModule_ProvidesPipPhoneMenuControllerFactory(Provider<Context> provider, Provider<PipBoundsState> provider2, Provider<PipMediaController> provider3, Provider<SystemWindows> provider4, Provider<ShellExecutor> provider5, Provider<Handler> provider6) {
        this.contextProvider = provider;
        this.pipBoundsStateProvider = provider2;
        this.pipMediaControllerProvider = provider3;
        this.systemWindowsProvider = provider4;
        this.mainExecutorProvider = provider5;
        this.mainHandlerProvider = provider6;
    }

    @Override // javax.inject.Provider
    public PhonePipMenuController get() {
        return providesPipPhoneMenuController(this.contextProvider.get(), this.pipBoundsStateProvider.get(), this.pipMediaControllerProvider.get(), this.systemWindowsProvider.get(), this.mainExecutorProvider.get(), this.mainHandlerProvider.get());
    }

    public static WMShellModule_ProvidesPipPhoneMenuControllerFactory create(Provider<Context> provider, Provider<PipBoundsState> provider2, Provider<PipMediaController> provider3, Provider<SystemWindows> provider4, Provider<ShellExecutor> provider5, Provider<Handler> provider6) {
        return new WMShellModule_ProvidesPipPhoneMenuControllerFactory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static PhonePipMenuController providesPipPhoneMenuController(Context context, PipBoundsState pipBoundsState, PipMediaController pipMediaController, SystemWindows systemWindows, ShellExecutor shellExecutor, Handler handler) {
        return (PhonePipMenuController) Preconditions.checkNotNullFromProvides(WMShellModule.providesPipPhoneMenuController(context, pipBoundsState, pipMediaController, systemWindows, shellExecutor, handler));
    }
}
