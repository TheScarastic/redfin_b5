package com.android.systemui.dagger;

import android.content.Context;
import android.hardware.display.DisplayManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideDisplayManagerFactory implements Factory<DisplayManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideDisplayManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public DisplayManager get() {
        return provideDisplayManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideDisplayManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideDisplayManagerFactory(provider);
    }

    public static DisplayManager provideDisplayManager(Context context) {
        return (DisplayManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideDisplayManager(context));
    }
}
