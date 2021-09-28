package com.android.systemui.dagger;

import android.app.IWallpaperManager;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideIWallPaperManagerFactory implements Factory<IWallpaperManager> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final FrameworkServicesModule_ProvideIWallPaperManagerFactory INSTANCE = new FrameworkServicesModule_ProvideIWallPaperManagerFactory();
    }

    @Override // javax.inject.Provider
    public IWallpaperManager get() {
        return provideIWallPaperManager();
    }

    public static FrameworkServicesModule_ProvideIWallPaperManagerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static IWallpaperManager provideIWallPaperManager() {
        return FrameworkServicesModule.provideIWallPaperManager();
    }
}
