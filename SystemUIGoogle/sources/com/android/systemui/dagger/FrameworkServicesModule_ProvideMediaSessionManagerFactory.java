package com.android.systemui.dagger;

import android.content.Context;
import android.media.session.MediaSessionManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideMediaSessionManagerFactory implements Factory<MediaSessionManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideMediaSessionManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public MediaSessionManager get() {
        return provideMediaSessionManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideMediaSessionManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideMediaSessionManagerFactory(provider);
    }

    public static MediaSessionManager provideMediaSessionManager(Context context) {
        return (MediaSessionManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideMediaSessionManager(context));
    }
}
