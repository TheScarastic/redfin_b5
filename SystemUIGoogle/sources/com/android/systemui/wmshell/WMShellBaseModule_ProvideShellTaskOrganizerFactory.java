package com.android.systemui.wmshell;

import android.content.Context;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.sizecompatui.SizeCompatUIController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideShellTaskOrganizerFactory implements Factory<ShellTaskOrganizer> {
    private final Provider<Context> contextProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<SizeCompatUIController> sizeCompatUIProvider;

    public WMShellBaseModule_ProvideShellTaskOrganizerFactory(Provider<ShellExecutor> provider, Provider<Context> provider2, Provider<SizeCompatUIController> provider3) {
        this.mainExecutorProvider = provider;
        this.contextProvider = provider2;
        this.sizeCompatUIProvider = provider3;
    }

    @Override // javax.inject.Provider
    public ShellTaskOrganizer get() {
        return provideShellTaskOrganizer(this.mainExecutorProvider.get(), this.contextProvider.get(), this.sizeCompatUIProvider.get());
    }

    public static WMShellBaseModule_ProvideShellTaskOrganizerFactory create(Provider<ShellExecutor> provider, Provider<Context> provider2, Provider<SizeCompatUIController> provider3) {
        return new WMShellBaseModule_ProvideShellTaskOrganizerFactory(provider, provider2, provider3);
    }

    public static ShellTaskOrganizer provideShellTaskOrganizer(ShellExecutor shellExecutor, Context context, SizeCompatUIController sizeCompatUIController) {
        return (ShellTaskOrganizer) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideShellTaskOrganizer(shellExecutor, context, sizeCompatUIController));
    }
}
