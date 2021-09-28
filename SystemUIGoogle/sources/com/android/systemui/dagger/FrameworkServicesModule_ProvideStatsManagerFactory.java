package com.android.systemui.dagger;

import android.app.StatsManager;
import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideStatsManagerFactory implements Factory<StatsManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideStatsManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public StatsManager get() {
        return provideStatsManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideStatsManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideStatsManagerFactory(provider);
    }

    public static StatsManager provideStatsManager(Context context) {
        return (StatsManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideStatsManager(context));
    }
}
