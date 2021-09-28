package com.android.systemui.wmshell;

import com.android.wm.shell.startingsurface.StartingWindowTypeAlgorithm;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes2.dex */
public final class WMShellModule_ProvideStartingWindowTypeAlgorithmFactory implements Factory<StartingWindowTypeAlgorithm> {

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final WMShellModule_ProvideStartingWindowTypeAlgorithmFactory INSTANCE = new WMShellModule_ProvideStartingWindowTypeAlgorithmFactory();
    }

    @Override // javax.inject.Provider
    public StartingWindowTypeAlgorithm get() {
        return provideStartingWindowTypeAlgorithm();
    }

    public static WMShellModule_ProvideStartingWindowTypeAlgorithmFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static StartingWindowTypeAlgorithm provideStartingWindowTypeAlgorithm() {
        return (StartingWindowTypeAlgorithm) Preconditions.checkNotNullFromProvides(WMShellModule.provideStartingWindowTypeAlgorithm());
    }
}
