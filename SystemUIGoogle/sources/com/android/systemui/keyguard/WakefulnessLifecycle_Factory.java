package com.android.systemui.keyguard;

import android.app.IWallpaperManager;
import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WakefulnessLifecycle_Factory implements Factory<WakefulnessLifecycle> {
    private final Provider<Context> contextProvider;
    private final Provider<IWallpaperManager> wallpaperManagerServiceProvider;

    public WakefulnessLifecycle_Factory(Provider<Context> provider, Provider<IWallpaperManager> provider2) {
        this.contextProvider = provider;
        this.wallpaperManagerServiceProvider = provider2;
    }

    @Override // javax.inject.Provider
    public WakefulnessLifecycle get() {
        return newInstance(this.contextProvider.get(), this.wallpaperManagerServiceProvider.get());
    }

    public static WakefulnessLifecycle_Factory create(Provider<Context> provider, Provider<IWallpaperManager> provider2) {
        return new WakefulnessLifecycle_Factory(provider, provider2);
    }

    public static WakefulnessLifecycle newInstance(Context context, IWallpaperManager iWallpaperManager) {
        return new WakefulnessLifecycle(context, iWallpaperManager);
    }
}
