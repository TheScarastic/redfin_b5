package com.android.systemui.wmshell;

import com.android.wm.shell.ShellCommandHandlerImpl;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.apppairs.AppPairsController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutController;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.splitscreen.SplitScreenController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideShellCommandHandlerImplFactory implements Factory<ShellCommandHandlerImpl> {
    private final Provider<Optional<AppPairsController>> appPairsOptionalProvider;
    private final Provider<Optional<HideDisplayCutoutController>> hideDisplayCutoutProvider;
    private final Provider<Optional<LegacySplitScreenController>> legacySplitScreenOptionalProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<Optional<OneHandedController>> oneHandedOptionalProvider;
    private final Provider<Optional<Pip>> pipOptionalProvider;
    private final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    private final Provider<Optional<SplitScreenController>> splitScreenOptionalProvider;

    public WMShellBaseModule_ProvideShellCommandHandlerImplFactory(Provider<ShellTaskOrganizer> provider, Provider<Optional<LegacySplitScreenController>> provider2, Provider<Optional<SplitScreenController>> provider3, Provider<Optional<Pip>> provider4, Provider<Optional<OneHandedController>> provider5, Provider<Optional<HideDisplayCutoutController>> provider6, Provider<Optional<AppPairsController>> provider7, Provider<ShellExecutor> provider8) {
        this.shellTaskOrganizerProvider = provider;
        this.legacySplitScreenOptionalProvider = provider2;
        this.splitScreenOptionalProvider = provider3;
        this.pipOptionalProvider = provider4;
        this.oneHandedOptionalProvider = provider5;
        this.hideDisplayCutoutProvider = provider6;
        this.appPairsOptionalProvider = provider7;
        this.mainExecutorProvider = provider8;
    }

    @Override // javax.inject.Provider
    public ShellCommandHandlerImpl get() {
        return provideShellCommandHandlerImpl(this.shellTaskOrganizerProvider.get(), this.legacySplitScreenOptionalProvider.get(), this.splitScreenOptionalProvider.get(), this.pipOptionalProvider.get(), this.oneHandedOptionalProvider.get(), this.hideDisplayCutoutProvider.get(), this.appPairsOptionalProvider.get(), this.mainExecutorProvider.get());
    }

    public static WMShellBaseModule_ProvideShellCommandHandlerImplFactory create(Provider<ShellTaskOrganizer> provider, Provider<Optional<LegacySplitScreenController>> provider2, Provider<Optional<SplitScreenController>> provider3, Provider<Optional<Pip>> provider4, Provider<Optional<OneHandedController>> provider5, Provider<Optional<HideDisplayCutoutController>> provider6, Provider<Optional<AppPairsController>> provider7, Provider<ShellExecutor> provider8) {
        return new WMShellBaseModule_ProvideShellCommandHandlerImplFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    public static ShellCommandHandlerImpl provideShellCommandHandlerImpl(ShellTaskOrganizer shellTaskOrganizer, Optional<LegacySplitScreenController> optional, Optional<SplitScreenController> optional2, Optional<Pip> optional3, Optional<OneHandedController> optional4, Optional<HideDisplayCutoutController> optional5, Optional<AppPairsController> optional6, ShellExecutor shellExecutor) {
        return (ShellCommandHandlerImpl) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideShellCommandHandlerImpl(shellTaskOrganizer, optional, optional2, optional3, optional4, optional5, optional6, shellExecutor));
    }
}
