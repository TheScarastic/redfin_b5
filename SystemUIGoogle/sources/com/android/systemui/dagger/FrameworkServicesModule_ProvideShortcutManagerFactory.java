package com.android.systemui.dagger;

import android.content.Context;
import android.content.pm.ShortcutManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideShortcutManagerFactory implements Factory<ShortcutManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideShortcutManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public ShortcutManager get() {
        return provideShortcutManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideShortcutManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideShortcutManagerFactory(provider);
    }

    public static ShortcutManager provideShortcutManager(Context context) {
        return (ShortcutManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideShortcutManager(context));
    }
}
