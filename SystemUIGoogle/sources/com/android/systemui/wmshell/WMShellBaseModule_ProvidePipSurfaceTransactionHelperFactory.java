package com.android.systemui.wmshell;

import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory implements Factory<PipSurfaceTransactionHelper> {

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory INSTANCE = new WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory();
    }

    @Override // javax.inject.Provider
    public PipSurfaceTransactionHelper get() {
        return providePipSurfaceTransactionHelper();
    }

    public static WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static PipSurfaceTransactionHelper providePipSurfaceTransactionHelper() {
        return (PipSurfaceTransactionHelper) Preconditions.checkNotNullFromProvides(WMShellBaseModule.providePipSurfaceTransactionHelper());
    }
}
