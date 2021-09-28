package com.android.systemui.wmshell;

import com.android.wm.shell.startingsurface.StartingWindowTypeAlgorithm;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes2.dex */
public final class TvWMShellModule_ProvideStartingWindowTypeAlgorithmFactory implements Factory<StartingWindowTypeAlgorithm> {

    /* loaded from: classes2.dex */
    private static final class InstanceHolder {
        private static final TvWMShellModule_ProvideStartingWindowTypeAlgorithmFactory INSTANCE = new TvWMShellModule_ProvideStartingWindowTypeAlgorithmFactory();
    }

    @Override // javax.inject.Provider
    public StartingWindowTypeAlgorithm get() {
        return provideStartingWindowTypeAlgorithm();
    }

    public static TvWMShellModule_ProvideStartingWindowTypeAlgorithmFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static StartingWindowTypeAlgorithm provideStartingWindowTypeAlgorithm() {
        return (StartingWindowTypeAlgorithm) Preconditions.checkNotNullFromProvides(TvWMShellModule.provideStartingWindowTypeAlgorithm());
    }
}
