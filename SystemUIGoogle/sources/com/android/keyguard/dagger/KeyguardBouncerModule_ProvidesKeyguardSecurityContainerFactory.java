package com.android.keyguard.dagger;

import com.android.keyguard.KeyguardHostView;
import com.android.keyguard.KeyguardSecurityContainer;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardBouncerModule_ProvidesKeyguardSecurityContainerFactory implements Factory<KeyguardSecurityContainer> {
    private final Provider<KeyguardHostView> hostViewProvider;

    public KeyguardBouncerModule_ProvidesKeyguardSecurityContainerFactory(Provider<KeyguardHostView> provider) {
        this.hostViewProvider = provider;
    }

    @Override // javax.inject.Provider
    public KeyguardSecurityContainer get() {
        return providesKeyguardSecurityContainer(this.hostViewProvider.get());
    }

    public static KeyguardBouncerModule_ProvidesKeyguardSecurityContainerFactory create(Provider<KeyguardHostView> provider) {
        return new KeyguardBouncerModule_ProvidesKeyguardSecurityContainerFactory(provider);
    }

    public static KeyguardSecurityContainer providesKeyguardSecurityContainer(KeyguardHostView keyguardHostView) {
        return (KeyguardSecurityContainer) Preconditions.checkNotNullFromProvides(KeyguardBouncerModule.providesKeyguardSecurityContainer(keyguardHostView));
    }
}
