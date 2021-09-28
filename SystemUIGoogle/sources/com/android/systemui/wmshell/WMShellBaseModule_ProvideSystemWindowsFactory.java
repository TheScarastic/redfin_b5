package com.android.systemui.wmshell;

import android.view.IWindowManager;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.SystemWindows;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideSystemWindowsFactory implements Factory<SystemWindows> {
    private final Provider<DisplayController> displayControllerProvider;
    private final Provider<IWindowManager> wmServiceProvider;

    public WMShellBaseModule_ProvideSystemWindowsFactory(Provider<DisplayController> provider, Provider<IWindowManager> provider2) {
        this.displayControllerProvider = provider;
        this.wmServiceProvider = provider2;
    }

    @Override // javax.inject.Provider
    public SystemWindows get() {
        return provideSystemWindows(this.displayControllerProvider.get(), this.wmServiceProvider.get());
    }

    public static WMShellBaseModule_ProvideSystemWindowsFactory create(Provider<DisplayController> provider, Provider<IWindowManager> provider2) {
        return new WMShellBaseModule_ProvideSystemWindowsFactory(provider, provider2);
    }

    public static SystemWindows provideSystemWindows(DisplayController displayController, IWindowManager iWindowManager) {
        return (SystemWindows) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideSystemWindows(displayController, iWindowManager));
    }
}
