package com.android.keyguard;

import android.view.ViewGroup;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardRootViewController_Factory implements Factory<KeyguardRootViewController> {
    private final Provider<ViewGroup> viewProvider;

    public KeyguardRootViewController_Factory(Provider<ViewGroup> provider) {
        this.viewProvider = provider;
    }

    @Override // javax.inject.Provider
    public KeyguardRootViewController get() {
        return newInstance(this.viewProvider.get());
    }

    public static KeyguardRootViewController_Factory create(Provider<ViewGroup> provider) {
        return new KeyguardRootViewController_Factory(provider);
    }

    public static KeyguardRootViewController newInstance(ViewGroup viewGroup) {
        return new KeyguardRootViewController(viewGroup);
    }
}
