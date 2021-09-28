package com.android.systemui.wmshell;

import android.content.Context;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideHideDisplayCutoutControllerFactory implements Factory<Optional<HideDisplayCutoutController>> {
    private final Provider<Context> contextProvider;
    private final Provider<DisplayController> displayControllerProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;

    public WMShellBaseModule_ProvideHideDisplayCutoutControllerFactory(Provider<Context> provider, Provider<DisplayController> provider2, Provider<ShellExecutor> provider3) {
        this.contextProvider = provider;
        this.displayControllerProvider = provider2;
        this.mainExecutorProvider = provider3;
    }

    @Override // javax.inject.Provider
    public Optional<HideDisplayCutoutController> get() {
        return provideHideDisplayCutoutController(this.contextProvider.get(), this.displayControllerProvider.get(), this.mainExecutorProvider.get());
    }

    public static WMShellBaseModule_ProvideHideDisplayCutoutControllerFactory create(Provider<Context> provider, Provider<DisplayController> provider2, Provider<ShellExecutor> provider3) {
        return new WMShellBaseModule_ProvideHideDisplayCutoutControllerFactory(provider, provider2, provider3);
    }

    public static Optional<HideDisplayCutoutController> provideHideDisplayCutoutController(Context context, DisplayController displayController, ShellExecutor shellExecutor) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideHideDisplayCutoutController(context, displayController, shellExecutor));
    }
}
