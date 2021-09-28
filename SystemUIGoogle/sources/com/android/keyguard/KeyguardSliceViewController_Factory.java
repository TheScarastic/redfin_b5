package com.android.keyguard;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.tuner.TunerService;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardSliceViewController_Factory implements Factory<KeyguardSliceViewController> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<KeyguardSliceView> keyguardSliceViewProvider;
    private final Provider<TunerService> tunerServiceProvider;

    public KeyguardSliceViewController_Factory(Provider<KeyguardSliceView> provider, Provider<ActivityStarter> provider2, Provider<ConfigurationController> provider3, Provider<TunerService> provider4, Provider<DumpManager> provider5) {
        this.keyguardSliceViewProvider = provider;
        this.activityStarterProvider = provider2;
        this.configurationControllerProvider = provider3;
        this.tunerServiceProvider = provider4;
        this.dumpManagerProvider = provider5;
    }

    @Override // javax.inject.Provider
    public KeyguardSliceViewController get() {
        return newInstance(this.keyguardSliceViewProvider.get(), this.activityStarterProvider.get(), this.configurationControllerProvider.get(), this.tunerServiceProvider.get(), this.dumpManagerProvider.get());
    }

    public static KeyguardSliceViewController_Factory create(Provider<KeyguardSliceView> provider, Provider<ActivityStarter> provider2, Provider<ConfigurationController> provider3, Provider<TunerService> provider4, Provider<DumpManager> provider5) {
        return new KeyguardSliceViewController_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static KeyguardSliceViewController newInstance(KeyguardSliceView keyguardSliceView, ActivityStarter activityStarter, ConfigurationController configurationController, TunerService tunerService, DumpManager dumpManager) {
        return new KeyguardSliceViewController(keyguardSliceView, activityStarter, configurationController, tunerService, dumpManager);
    }
}
