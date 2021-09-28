package com.android.systemui.dagger;

import android.content.Context;
import android.view.WindowManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideWindowManagerFactory implements Factory<WindowManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideWindowManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public WindowManager get() {
        return provideWindowManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideWindowManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideWindowManagerFactory(provider);
    }

    public static WindowManager provideWindowManager(Context context) {
        return (WindowManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideWindowManager(context));
    }
}
