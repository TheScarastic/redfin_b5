package com.android.systemui.wmshell;

import android.content.Context;
import android.os.Handler;
import android.view.WindowManager;
import com.android.internal.logging.UiEventLogger;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.onehanded.OneHandedController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideOneHandedControllerFactory implements Factory<Optional<OneHandedController>> {
    private final Provider<Context> contextProvider;
    private final Provider<DisplayController> displayControllerProvider;
    private final Provider<DisplayLayout> displayLayoutProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<TaskStackListenerImpl> taskStackListenerProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<WindowManager> windowManagerProvider;

    public WMShellBaseModule_ProvideOneHandedControllerFactory(Provider<Context> provider, Provider<WindowManager> provider2, Provider<DisplayController> provider3, Provider<DisplayLayout> provider4, Provider<TaskStackListenerImpl> provider5, Provider<UiEventLogger> provider6, Provider<ShellExecutor> provider7, Provider<Handler> provider8) {
        this.contextProvider = provider;
        this.windowManagerProvider = provider2;
        this.displayControllerProvider = provider3;
        this.displayLayoutProvider = provider4;
        this.taskStackListenerProvider = provider5;
        this.uiEventLoggerProvider = provider6;
        this.mainExecutorProvider = provider7;
        this.mainHandlerProvider = provider8;
    }

    @Override // javax.inject.Provider
    public Optional<OneHandedController> get() {
        return provideOneHandedController(this.contextProvider.get(), this.windowManagerProvider.get(), this.displayControllerProvider.get(), this.displayLayoutProvider.get(), this.taskStackListenerProvider.get(), this.uiEventLoggerProvider.get(), this.mainExecutorProvider.get(), this.mainHandlerProvider.get());
    }

    public static WMShellBaseModule_ProvideOneHandedControllerFactory create(Provider<Context> provider, Provider<WindowManager> provider2, Provider<DisplayController> provider3, Provider<DisplayLayout> provider4, Provider<TaskStackListenerImpl> provider5, Provider<UiEventLogger> provider6, Provider<ShellExecutor> provider7, Provider<Handler> provider8) {
        return new WMShellBaseModule_ProvideOneHandedControllerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    public static Optional<OneHandedController> provideOneHandedController(Context context, WindowManager windowManager, DisplayController displayController, DisplayLayout displayLayout, TaskStackListenerImpl taskStackListenerImpl, UiEventLogger uiEventLogger, ShellExecutor shellExecutor, Handler handler) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideOneHandedController(context, windowManager, displayController, displayLayout, taskStackListenerImpl, uiEventLogger, shellExecutor, handler));
    }
}
