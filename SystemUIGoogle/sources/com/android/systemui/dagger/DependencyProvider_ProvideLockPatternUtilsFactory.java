package com.android.systemui.dagger;

import android.content.Context;
import com.android.internal.widget.LockPatternUtils;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvideLockPatternUtilsFactory implements Factory<LockPatternUtils> {
    private final Provider<Context> contextProvider;
    private final DependencyProvider module;

    public DependencyProvider_ProvideLockPatternUtilsFactory(DependencyProvider dependencyProvider, Provider<Context> provider) {
        this.module = dependencyProvider;
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public LockPatternUtils get() {
        return provideLockPatternUtils(this.module, this.contextProvider.get());
    }

    public static DependencyProvider_ProvideLockPatternUtilsFactory create(DependencyProvider dependencyProvider, Provider<Context> provider) {
        return new DependencyProvider_ProvideLockPatternUtilsFactory(dependencyProvider, provider);
    }

    public static LockPatternUtils provideLockPatternUtils(DependencyProvider dependencyProvider, Context context) {
        return (LockPatternUtils) Preconditions.checkNotNullFromProvides(dependencyProvider.provideLockPatternUtils(context));
    }
}
