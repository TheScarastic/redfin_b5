package com.android.systemui.wmshell;

import android.animation.AnimationHandler;
import com.android.wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellConcurrencyModule_ProvideShellMainExecutorSfVsyncAnimationHandlerFactory implements Factory<AnimationHandler> {
    private final Provider<ShellExecutor> mainExecutorProvider;

    public WMShellConcurrencyModule_ProvideShellMainExecutorSfVsyncAnimationHandlerFactory(Provider<ShellExecutor> provider) {
        this.mainExecutorProvider = provider;
    }

    @Override // javax.inject.Provider
    public AnimationHandler get() {
        return provideShellMainExecutorSfVsyncAnimationHandler(this.mainExecutorProvider.get());
    }

    public static WMShellConcurrencyModule_ProvideShellMainExecutorSfVsyncAnimationHandlerFactory create(Provider<ShellExecutor> provider) {
        return new WMShellConcurrencyModule_ProvideShellMainExecutorSfVsyncAnimationHandlerFactory(provider);
    }

    public static AnimationHandler provideShellMainExecutorSfVsyncAnimationHandler(ShellExecutor shellExecutor) {
        return (AnimationHandler) Preconditions.checkNotNullFromProvides(WMShellConcurrencyModule.provideShellMainExecutorSfVsyncAnimationHandler(shellExecutor));
    }
}
