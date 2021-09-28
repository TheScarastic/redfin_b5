package com.android.systemui.dagger;

import android.content.Context;
import android.content.om.OverlayManager;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.theme.ThemeOverlayApplier;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvideThemeOverlayManagerFactory implements Factory<ThemeOverlayApplier> {
    private final Provider<Executor> bgExecutorProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<OverlayManager> overlayManagerProvider;

    public DependencyProvider_ProvideThemeOverlayManagerFactory(Provider<Context> provider, Provider<Executor> provider2, Provider<OverlayManager> provider3, Provider<DumpManager> provider4) {
        this.contextProvider = provider;
        this.bgExecutorProvider = provider2;
        this.overlayManagerProvider = provider3;
        this.dumpManagerProvider = provider4;
    }

    @Override // javax.inject.Provider
    public ThemeOverlayApplier get() {
        return provideThemeOverlayManager(this.contextProvider.get(), this.bgExecutorProvider.get(), this.overlayManagerProvider.get(), this.dumpManagerProvider.get());
    }

    public static DependencyProvider_ProvideThemeOverlayManagerFactory create(Provider<Context> provider, Provider<Executor> provider2, Provider<OverlayManager> provider3, Provider<DumpManager> provider4) {
        return new DependencyProvider_ProvideThemeOverlayManagerFactory(provider, provider2, provider3, provider4);
    }

    public static ThemeOverlayApplier provideThemeOverlayManager(Context context, Executor executor, OverlayManager overlayManager, DumpManager dumpManager) {
        return (ThemeOverlayApplier) Preconditions.checkNotNullFromProvides(DependencyProvider.provideThemeOverlayManager(context, executor, overlayManager, dumpManager));
    }
}
