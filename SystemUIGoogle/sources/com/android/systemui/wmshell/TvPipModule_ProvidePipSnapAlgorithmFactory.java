package com.android.systemui.wmshell;

import com.android.wm.shell.pip.PipSnapAlgorithm;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes2.dex */
public final class TvPipModule_ProvidePipSnapAlgorithmFactory implements Factory<PipSnapAlgorithm> {

    /* loaded from: classes2.dex */
    private static final class InstanceHolder {
        private static final TvPipModule_ProvidePipSnapAlgorithmFactory INSTANCE = new TvPipModule_ProvidePipSnapAlgorithmFactory();
    }

    @Override // javax.inject.Provider
    public PipSnapAlgorithm get() {
        return providePipSnapAlgorithm();
    }

    public static TvPipModule_ProvidePipSnapAlgorithmFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static PipSnapAlgorithm providePipSnapAlgorithm() {
        return (PipSnapAlgorithm) Preconditions.checkNotNullFromProvides(TvPipModule.providePipSnapAlgorithm());
    }
}
