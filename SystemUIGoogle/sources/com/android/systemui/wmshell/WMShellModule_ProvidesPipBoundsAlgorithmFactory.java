package com.android.systemui.wmshell;

import android.content.Context;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipSnapAlgorithm;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellModule_ProvidesPipBoundsAlgorithmFactory implements Factory<PipBoundsAlgorithm> {
    private final Provider<Context> contextProvider;
    private final Provider<PipBoundsState> pipBoundsStateProvider;
    private final Provider<PipSnapAlgorithm> pipSnapAlgorithmProvider;

    public WMShellModule_ProvidesPipBoundsAlgorithmFactory(Provider<Context> provider, Provider<PipBoundsState> provider2, Provider<PipSnapAlgorithm> provider3) {
        this.contextProvider = provider;
        this.pipBoundsStateProvider = provider2;
        this.pipSnapAlgorithmProvider = provider3;
    }

    @Override // javax.inject.Provider
    public PipBoundsAlgorithm get() {
        return providesPipBoundsAlgorithm(this.contextProvider.get(), this.pipBoundsStateProvider.get(), this.pipSnapAlgorithmProvider.get());
    }

    public static WMShellModule_ProvidesPipBoundsAlgorithmFactory create(Provider<Context> provider, Provider<PipBoundsState> provider2, Provider<PipSnapAlgorithm> provider3) {
        return new WMShellModule_ProvidesPipBoundsAlgorithmFactory(provider, provider2, provider3);
    }

    public static PipBoundsAlgorithm providesPipBoundsAlgorithm(Context context, PipBoundsState pipBoundsState, PipSnapAlgorithm pipSnapAlgorithm) {
        return (PipBoundsAlgorithm) Preconditions.checkNotNullFromProvides(WMShellModule.providesPipBoundsAlgorithm(context, pipBoundsState, pipSnapAlgorithm));
    }
}
