package com.android.systemui.wmshell;

import android.content.Context;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.transition.Transitions;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideTransitionsFactory implements Factory<Transitions> {
    private final Provider<ShellExecutor> animExecutorProvider;
    private final Provider<Context> contextProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<ShellTaskOrganizer> organizerProvider;
    private final Provider<TransactionPool> poolProvider;

    public WMShellBaseModule_ProvideTransitionsFactory(Provider<ShellTaskOrganizer> provider, Provider<TransactionPool> provider2, Provider<Context> provider3, Provider<ShellExecutor> provider4, Provider<ShellExecutor> provider5) {
        this.organizerProvider = provider;
        this.poolProvider = provider2;
        this.contextProvider = provider3;
        this.mainExecutorProvider = provider4;
        this.animExecutorProvider = provider5;
    }

    @Override // javax.inject.Provider
    public Transitions get() {
        return provideTransitions(this.organizerProvider.get(), this.poolProvider.get(), this.contextProvider.get(), this.mainExecutorProvider.get(), this.animExecutorProvider.get());
    }

    public static WMShellBaseModule_ProvideTransitionsFactory create(Provider<ShellTaskOrganizer> provider, Provider<TransactionPool> provider2, Provider<Context> provider3, Provider<ShellExecutor> provider4, Provider<ShellExecutor> provider5) {
        return new WMShellBaseModule_ProvideTransitionsFactory(provider, provider2, provider3, provider4, provider5);
    }

    public static Transitions provideTransitions(ShellTaskOrganizer shellTaskOrganizer, TransactionPool transactionPool, Context context, ShellExecutor shellExecutor, ShellExecutor shellExecutor2) {
        return (Transitions) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideTransitions(shellTaskOrganizer, transactionPool, context, shellExecutor, shellExecutor2));
    }
}
