package com.android.systemui.wmshell;

import com.android.wm.shell.common.TransactionPool;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideTransactionPoolFactory implements Factory<TransactionPool> {

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final WMShellBaseModule_ProvideTransactionPoolFactory INSTANCE = new WMShellBaseModule_ProvideTransactionPoolFactory();
    }

    @Override // javax.inject.Provider
    public TransactionPool get() {
        return provideTransactionPool();
    }

    public static WMShellBaseModule_ProvideTransactionPoolFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static TransactionPool provideTransactionPool() {
        return (TransactionPool) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideTransactionPool());
    }
}
