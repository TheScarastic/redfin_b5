package com.android.systemui.statusbar;

import android.content.Context;
import com.android.systemui.flags.FeatureFlagReader;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FeatureFlags_Factory implements Factory<FeatureFlags> {
    private final Provider<Context> contextProvider;
    private final Provider<FeatureFlagReader> flagReaderProvider;

    public FeatureFlags_Factory(Provider<FeatureFlagReader> provider, Provider<Context> provider2) {
        this.flagReaderProvider = provider;
        this.contextProvider = provider2;
    }

    @Override // javax.inject.Provider
    public FeatureFlags get() {
        return newInstance(this.flagReaderProvider.get(), this.contextProvider.get());
    }

    public static FeatureFlags_Factory create(Provider<FeatureFlagReader> provider, Provider<Context> provider2) {
        return new FeatureFlags_Factory(provider, provider2);
    }

    public static FeatureFlags newInstance(FeatureFlagReader featureFlagReader, Context context) {
        return new FeatureFlags(featureFlagReader, context);
    }
}
