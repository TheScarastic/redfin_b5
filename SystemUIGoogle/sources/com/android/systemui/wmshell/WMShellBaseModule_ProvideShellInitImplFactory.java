package com.android.systemui.wmshell;

import com.android.wm.shell.FullscreenTaskListener;
import com.android.wm.shell.ShellInitImpl;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.apppairs.AppPairsController;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.draganddrop.DragAndDropController;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.startingsurface.StartingWindowController;
import com.android.wm.shell.transition.Transitions;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideShellInitImplFactory implements Factory<ShellInitImpl> {
    private final Provider<Optional<AppPairsController>> appPairsOptionalProvider;
    private final Provider<Optional<BubbleController>> bubblesOptionalProvider;
    private final Provider<DisplayImeController> displayImeControllerProvider;
    private final Provider<DragAndDropController> dragAndDropControllerProvider;
    private final Provider<FullscreenTaskListener> fullscreenTaskListenerProvider;
    private final Provider<Optional<LegacySplitScreenController>> legacySplitScreenOptionalProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<Optional<PipTouchHandler>> pipTouchHandlerOptionalProvider;
    private final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    private final Provider<Optional<SplitScreenController>> splitScreenOptionalProvider;
    private final Provider<StartingWindowController> startingWindowProvider;
    private final Provider<Transitions> transitionsProvider;

    public WMShellBaseModule_ProvideShellInitImplFactory(Provider<DisplayImeController> provider, Provider<DragAndDropController> provider2, Provider<ShellTaskOrganizer> provider3, Provider<Optional<BubbleController>> provider4, Provider<Optional<LegacySplitScreenController>> provider5, Provider<Optional<SplitScreenController>> provider6, Provider<Optional<AppPairsController>> provider7, Provider<Optional<PipTouchHandler>> provider8, Provider<FullscreenTaskListener> provider9, Provider<Transitions> provider10, Provider<StartingWindowController> provider11, Provider<ShellExecutor> provider12) {
        this.displayImeControllerProvider = provider;
        this.dragAndDropControllerProvider = provider2;
        this.shellTaskOrganizerProvider = provider3;
        this.bubblesOptionalProvider = provider4;
        this.legacySplitScreenOptionalProvider = provider5;
        this.splitScreenOptionalProvider = provider6;
        this.appPairsOptionalProvider = provider7;
        this.pipTouchHandlerOptionalProvider = provider8;
        this.fullscreenTaskListenerProvider = provider9;
        this.transitionsProvider = provider10;
        this.startingWindowProvider = provider11;
        this.mainExecutorProvider = provider12;
    }

    @Override // javax.inject.Provider
    public ShellInitImpl get() {
        return provideShellInitImpl(this.displayImeControllerProvider.get(), this.dragAndDropControllerProvider.get(), this.shellTaskOrganizerProvider.get(), this.bubblesOptionalProvider.get(), this.legacySplitScreenOptionalProvider.get(), this.splitScreenOptionalProvider.get(), this.appPairsOptionalProvider.get(), this.pipTouchHandlerOptionalProvider.get(), this.fullscreenTaskListenerProvider.get(), this.transitionsProvider.get(), this.startingWindowProvider.get(), this.mainExecutorProvider.get());
    }

    public static WMShellBaseModule_ProvideShellInitImplFactory create(Provider<DisplayImeController> provider, Provider<DragAndDropController> provider2, Provider<ShellTaskOrganizer> provider3, Provider<Optional<BubbleController>> provider4, Provider<Optional<LegacySplitScreenController>> provider5, Provider<Optional<SplitScreenController>> provider6, Provider<Optional<AppPairsController>> provider7, Provider<Optional<PipTouchHandler>> provider8, Provider<FullscreenTaskListener> provider9, Provider<Transitions> provider10, Provider<StartingWindowController> provider11, Provider<ShellExecutor> provider12) {
        return new WMShellBaseModule_ProvideShellInitImplFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12);
    }

    public static ShellInitImpl provideShellInitImpl(DisplayImeController displayImeController, DragAndDropController dragAndDropController, ShellTaskOrganizer shellTaskOrganizer, Optional<BubbleController> optional, Optional<LegacySplitScreenController> optional2, Optional<SplitScreenController> optional3, Optional<AppPairsController> optional4, Optional<PipTouchHandler> optional5, FullscreenTaskListener fullscreenTaskListener, Transitions transitions, StartingWindowController startingWindowController, ShellExecutor shellExecutor) {
        return (ShellInitImpl) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideShellInitImpl(displayImeController, dragAndDropController, shellTaskOrganizer, optional, optional2, optional3, optional4, optional5, fullscreenTaskListener, transitions, startingWindowController, shellExecutor));
    }
}
