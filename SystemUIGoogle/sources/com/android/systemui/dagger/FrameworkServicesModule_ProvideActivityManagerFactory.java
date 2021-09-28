package com.android.systemui.dagger;

import android.app.ActivityManager;
import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideActivityManagerFactory implements Factory<ActivityManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideActivityManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public ActivityManager get() {
        return provideActivityManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideActivityManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideActivityManagerFactory(provider);
    }

    public static ActivityManager provideActivityManager(Context context) {
        return (ActivityManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideActivityManager(context));
    }
}
