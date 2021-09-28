package com.android.systemui.demomode.dagger;

import android.content.Context;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.util.settings.GlobalSettings;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DemoModeModule_ProvideDemoModeControllerFactory implements Factory<DemoModeController> {
    private final Provider<Context> contextProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<GlobalSettings> globalSettingsProvider;

    public DemoModeModule_ProvideDemoModeControllerFactory(Provider<Context> provider, Provider<DumpManager> provider2, Provider<GlobalSettings> provider3) {
        this.contextProvider = provider;
        this.dumpManagerProvider = provider2;
        this.globalSettingsProvider = provider3;
    }

    @Override // javax.inject.Provider
    public DemoModeController get() {
        return provideDemoModeController(this.contextProvider.get(), this.dumpManagerProvider.get(), this.globalSettingsProvider.get());
    }

    public static DemoModeModule_ProvideDemoModeControllerFactory create(Provider<Context> provider, Provider<DumpManager> provider2, Provider<GlobalSettings> provider3) {
        return new DemoModeModule_ProvideDemoModeControllerFactory(provider, provider2, provider3);
    }

    public static DemoModeController provideDemoModeController(Context context, DumpManager dumpManager, GlobalSettings globalSettings) {
        return (DemoModeController) Preconditions.checkNotNullFromProvides(DemoModeModule.provideDemoModeController(context, dumpManager, globalSettings));
    }
}
