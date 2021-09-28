package com.android.systemui.dagger;

import android.content.Context;
import android.media.MediaRouter2Manager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideMediaRouter2ManagerFactory implements Factory<MediaRouter2Manager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideMediaRouter2ManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public MediaRouter2Manager get() {
        return provideMediaRouter2Manager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideMediaRouter2ManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideMediaRouter2ManagerFactory(provider);
    }

    public static MediaRouter2Manager provideMediaRouter2Manager(Context context) {
        return (MediaRouter2Manager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideMediaRouter2Manager(context));
    }
}
