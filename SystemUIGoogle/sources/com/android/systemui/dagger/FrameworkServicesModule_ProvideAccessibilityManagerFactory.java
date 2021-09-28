package com.android.systemui.dagger;

import android.content.Context;
import android.view.accessibility.AccessibilityManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideAccessibilityManagerFactory implements Factory<AccessibilityManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideAccessibilityManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public AccessibilityManager get() {
        return provideAccessibilityManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideAccessibilityManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideAccessibilityManagerFactory(provider);
    }

    public static AccessibilityManager provideAccessibilityManager(Context context) {
        return (AccessibilityManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideAccessibilityManager(context));
    }
}
