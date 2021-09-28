package com.android.systemui.dagger;

import android.app.KeyguardManager;
import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideKeyguardManagerFactory implements Factory<KeyguardManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideKeyguardManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public KeyguardManager get() {
        return provideKeyguardManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideKeyguardManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideKeyguardManagerFactory(provider);
    }

    public static KeyguardManager provideKeyguardManager(Context context) {
        return (KeyguardManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideKeyguardManager(context));
    }
}
