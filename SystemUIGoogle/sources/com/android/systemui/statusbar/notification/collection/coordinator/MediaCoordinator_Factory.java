package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.media.MediaFeatureFlag;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaCoordinator_Factory implements Factory<MediaCoordinator> {
    private final Provider<MediaFeatureFlag> featureFlagProvider;

    public MediaCoordinator_Factory(Provider<MediaFeatureFlag> provider) {
        this.featureFlagProvider = provider;
    }

    @Override // javax.inject.Provider
    public MediaCoordinator get() {
        return newInstance(this.featureFlagProvider.get());
    }

    public static MediaCoordinator_Factory create(Provider<MediaFeatureFlag> provider) {
        return new MediaCoordinator_Factory(provider);
    }

    public static MediaCoordinator newInstance(MediaFeatureFlag mediaFeatureFlag) {
        return new MediaCoordinator(mediaFeatureFlag);
    }
}
