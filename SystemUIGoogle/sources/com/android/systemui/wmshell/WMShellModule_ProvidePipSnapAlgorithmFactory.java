package com.android.systemui.wmshell;

import com.android.wm.shell.pip.PipSnapAlgorithm;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes2.dex */
public final class WMShellModule_ProvidePipSnapAlgorithmFactory implements Factory<PipSnapAlgorithm> {

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final WMShellModule_ProvidePipSnapAlgorithmFactory INSTANCE = new WMShellModule_ProvidePipSnapAlgorithmFactory();
    }

    @Override // javax.inject.Provider
    public PipSnapAlgorithm get() {
        return providePipSnapAlgorithm();
    }

    public static WMShellModule_ProvidePipSnapAlgorithmFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static PipSnapAlgorithm providePipSnapAlgorithm() {
        return (PipSnapAlgorithm) Preconditions.checkNotNullFromProvides(WMShellModule.providePipSnapAlgorithm());
    }
}
