package com.android.systemui.dagger;

import android.app.WallpaperManager;
import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideWallpaperManagerFactory implements Factory<WallpaperManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideWallpaperManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public WallpaperManager get() {
        return provideWallpaperManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideWallpaperManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideWallpaperManagerFactory(provider);
    }

    public static WallpaperManager provideWallpaperManager(Context context) {
        return (WallpaperManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideWallpaperManager(context));
    }
}
