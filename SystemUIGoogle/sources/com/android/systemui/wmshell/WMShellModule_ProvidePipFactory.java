package com.android.systemui.wmshell;

import android.content.Context;
import com.android.wm.shell.WindowManagerShellWrapper;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import com.android.wm.shell.pip.phone.PipAppOpsListener;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellModule_ProvidePipFactory implements Factory<Optional<Pip>> {
    private final Provider<Context> contextProvider;
    private final Provider<DisplayController> displayControllerProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<Optional<OneHandedController>> oneHandedControllerProvider;
    private final Provider<PhonePipMenuController> phonePipMenuControllerProvider;
    private final Provider<PipAppOpsListener> pipAppOpsListenerProvider;
    private final Provider<PipBoundsAlgorithm> pipBoundsAlgorithmProvider;
    private final Provider<PipBoundsState> pipBoundsStateProvider;
    private final Provider<PipMediaController> pipMediaControllerProvider;
    private final Provider<PipTaskOrganizer> pipTaskOrganizerProvider;
    private final Provider<PipTouchHandler> pipTouchHandlerProvider;
    private final Provider<PipTransitionController> pipTransitionControllerProvider;
    private final Provider<TaskStackListenerImpl> taskStackListenerProvider;
    private final Provider<WindowManagerShellWrapper> windowManagerShellWrapperProvider;

    public WMShellModule_ProvidePipFactory(Provider<Context> provider, Provider<DisplayController> provider2, Provider<PipAppOpsListener> provider3, Provider<PipBoundsAlgorithm> provider4, Provider<PipBoundsState> provider5, Provider<PipMediaController> provider6, Provider<PhonePipMenuController> provider7, Provider<PipTaskOrganizer> provider8, Provider<PipTouchHandler> provider9, Provider<PipTransitionController> provider10, Provider<WindowManagerShellWrapper> provider11, Provider<TaskStackListenerImpl> provider12, Provider<Optional<OneHandedController>> provider13, Provider<ShellExecutor> provider14) {
        this.contextProvider = provider;
        this.displayControllerProvider = provider2;
        this.pipAppOpsListenerProvider = provider3;
        this.pipBoundsAlgorithmProvider = provider4;
        this.pipBoundsStateProvider = provider5;
        this.pipMediaControllerProvider = provider6;
        this.phonePipMenuControllerProvider = provider7;
        this.pipTaskOrganizerProvider = provider8;
        this.pipTouchHandlerProvider = provider9;
        this.pipTransitionControllerProvider = provider10;
        this.windowManagerShellWrapperProvider = provider11;
        this.taskStackListenerProvider = provider12;
        this.oneHandedControllerProvider = provider13;
        this.mainExecutorProvider = provider14;
    }

    @Override // javax.inject.Provider
    public Optional<Pip> get() {
        return providePip(this.contextProvider.get(), this.displayControllerProvider.get(), this.pipAppOpsListenerProvider.get(), this.pipBoundsAlgorithmProvider.get(), this.pipBoundsStateProvider.get(), this.pipMediaControllerProvider.get(), this.phonePipMenuControllerProvider.get(), this.pipTaskOrganizerProvider.get(), this.pipTouchHandlerProvider.get(), this.pipTransitionControllerProvider.get(), this.windowManagerShellWrapperProvider.get(), this.taskStackListenerProvider.get(), this.oneHandedControllerProvider.get(), this.mainExecutorProvider.get());
    }

    public static WMShellModule_ProvidePipFactory create(Provider<Context> provider, Provider<DisplayController> provider2, Provider<PipAppOpsListener> provider3, Provider<PipBoundsAlgorithm> provider4, Provider<PipBoundsState> provider5, Provider<PipMediaController> provider6, Provider<PhonePipMenuController> provider7, Provider<PipTaskOrganizer> provider8, Provider<PipTouchHandler> provider9, Provider<PipTransitionController> provider10, Provider<WindowManagerShellWrapper> provider11, Provider<TaskStackListenerImpl> provider12, Provider<Optional<OneHandedController>> provider13, Provider<ShellExecutor> provider14) {
        return new WMShellModule_ProvidePipFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14);
    }

    public static Optional<Pip> providePip(Context context, DisplayController displayController, PipAppOpsListener pipAppOpsListener, PipBoundsAlgorithm pipBoundsAlgorithm, PipBoundsState pipBoundsState, PipMediaController pipMediaController, PhonePipMenuController phonePipMenuController, PipTaskOrganizer pipTaskOrganizer, PipTouchHandler pipTouchHandler, PipTransitionController pipTransitionController, WindowManagerShellWrapper windowManagerShellWrapper, TaskStackListenerImpl taskStackListenerImpl, Optional<OneHandedController> optional, ShellExecutor shellExecutor) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellModule.providePip(context, displayController, pipAppOpsListener, pipBoundsAlgorithm, pipBoundsState, pipMediaController, phonePipMenuController, pipTaskOrganizer, pipTouchHandler, pipTransitionController, windowManagerShellWrapper, taskStackListenerImpl, optional, shellExecutor));
    }
}
