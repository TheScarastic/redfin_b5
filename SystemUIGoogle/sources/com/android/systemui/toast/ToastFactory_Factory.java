package com.android.systemui.toast;

import android.view.LayoutInflater;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.shared.plugins.PluginManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ToastFactory_Factory implements Factory<ToastFactory> {
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<LayoutInflater> layoutInflaterProvider;
    private final Provider<PluginManager> pluginManagerProvider;

    public ToastFactory_Factory(Provider<LayoutInflater> provider, Provider<PluginManager> provider2, Provider<DumpManager> provider3) {
        this.layoutInflaterProvider = provider;
        this.pluginManagerProvider = provider2;
        this.dumpManagerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public ToastFactory get() {
        return newInstance(this.layoutInflaterProvider.get(), this.pluginManagerProvider.get(), this.dumpManagerProvider.get());
    }

    public static ToastFactory_Factory create(Provider<LayoutInflater> provider, Provider<PluginManager> provider2, Provider<DumpManager> provider3) {
        return new ToastFactory_Factory(provider, provider2, provider3);
    }

    public static ToastFactory newInstance(LayoutInflater layoutInflater, PluginManager pluginManager, DumpManager dumpManager) {
        return new ToastFactory(layoutInflater, pluginManager, dumpManager);
    }
}
