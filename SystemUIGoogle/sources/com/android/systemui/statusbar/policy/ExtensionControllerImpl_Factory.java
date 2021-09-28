package com.android.systemui.statusbar.policy;

import android.content.Context;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.leak.LeakDetector;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ExtensionControllerImpl_Factory implements Factory<ExtensionControllerImpl> {
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<LeakDetector> leakDetectorProvider;
    private final Provider<PluginManager> pluginManagerProvider;
    private final Provider<TunerService> tunerServiceProvider;

    public ExtensionControllerImpl_Factory(Provider<Context> provider, Provider<LeakDetector> provider2, Provider<PluginManager> provider3, Provider<TunerService> provider4, Provider<ConfigurationController> provider5) {
        this.contextProvider = provider;
        this.leakDetectorProvider = provider2;
        this.pluginManagerProvider = provider3;
        this.tunerServiceProvider = provider4;
        this.configurationControllerProvider = provider5;
    }

    @Override // javax.inject.Provider
    public ExtensionControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.leakDetectorProvider.get(), this.pluginManagerProvider.get(), this.tunerServiceProvider.get(), this.configurationControllerProvider.get());
    }

    public static ExtensionControllerImpl_Factory create(Provider<Context> provider, Provider<LeakDetector> provider2, Provider<PluginManager> provider3, Provider<TunerService> provider4, Provider<ConfigurationController> provider5) {
        return new ExtensionControllerImpl_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static ExtensionControllerImpl newInstance(Context context, LeakDetector leakDetector, PluginManager pluginManager, TunerService tunerService, ConfigurationController configurationController) {
        return new ExtensionControllerImpl(context, leakDetector, pluginManager, tunerService, configurationController);
    }
}
