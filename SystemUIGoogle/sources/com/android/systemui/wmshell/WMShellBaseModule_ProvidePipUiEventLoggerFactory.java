package com.android.systemui.wmshell;

import android.content.pm.PackageManager;
import com.android.internal.logging.UiEventLogger;
import com.android.wm.shell.pip.PipUiEventLogger;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvidePipUiEventLoggerFactory implements Factory<PipUiEventLogger> {
    private final Provider<PackageManager> packageManagerProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public WMShellBaseModule_ProvidePipUiEventLoggerFactory(Provider<UiEventLogger> provider, Provider<PackageManager> provider2) {
        this.uiEventLoggerProvider = provider;
        this.packageManagerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public PipUiEventLogger get() {
        return providePipUiEventLogger(this.uiEventLoggerProvider.get(), this.packageManagerProvider.get());
    }

    public static WMShellBaseModule_ProvidePipUiEventLoggerFactory create(Provider<UiEventLogger> provider, Provider<PackageManager> provider2) {
        return new WMShellBaseModule_ProvidePipUiEventLoggerFactory(provider, provider2);
    }

    public static PipUiEventLogger providePipUiEventLogger(UiEventLogger uiEventLogger, PackageManager packageManager) {
        return (PipUiEventLogger) Preconditions.checkNotNullFromProvides(WMShellBaseModule.providePipUiEventLogger(uiEventLogger, packageManager));
    }
}
