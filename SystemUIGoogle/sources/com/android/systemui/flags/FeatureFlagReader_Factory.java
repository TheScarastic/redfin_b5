package com.android.systemui.flags;

import android.content.res.Resources;
import com.android.systemui.util.wrapper.BuildInfo;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FeatureFlagReader_Factory implements Factory<FeatureFlagReader> {
    private final Provider<BuildInfo> buildProvider;
    private final Provider<Resources> resourcesProvider;
    private final Provider<SystemPropertiesHelper> systemPropertiesHelperProvider;

    public FeatureFlagReader_Factory(Provider<Resources> provider, Provider<BuildInfo> provider2, Provider<SystemPropertiesHelper> provider3) {
        this.resourcesProvider = provider;
        this.buildProvider = provider2;
        this.systemPropertiesHelperProvider = provider3;
    }

    @Override // javax.inject.Provider
    public FeatureFlagReader get() {
        return newInstance(this.resourcesProvider.get(), this.buildProvider.get(), this.systemPropertiesHelperProvider.get());
    }

    public static FeatureFlagReader_Factory create(Provider<Resources> provider, Provider<BuildInfo> provider2, Provider<SystemPropertiesHelper> provider3) {
        return new FeatureFlagReader_Factory(provider, provider2, provider3);
    }

    public static FeatureFlagReader newInstance(Resources resources, BuildInfo buildInfo, SystemPropertiesHelper systemPropertiesHelper) {
        return new FeatureFlagReader(resources, buildInfo, systemPropertiesHelper);
    }
}
