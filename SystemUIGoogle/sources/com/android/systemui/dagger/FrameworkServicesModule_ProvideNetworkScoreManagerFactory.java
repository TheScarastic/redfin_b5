package com.android.systemui.dagger;

import android.content.Context;
import android.net.NetworkScoreManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideNetworkScoreManagerFactory implements Factory<NetworkScoreManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideNetworkScoreManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public NetworkScoreManager get() {
        return provideNetworkScoreManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideNetworkScoreManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideNetworkScoreManagerFactory(provider);
    }

    public static NetworkScoreManager provideNetworkScoreManager(Context context) {
        return (NetworkScoreManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideNetworkScoreManager(context));
    }
}
