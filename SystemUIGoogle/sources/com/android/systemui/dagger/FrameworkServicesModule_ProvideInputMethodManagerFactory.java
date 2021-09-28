package com.android.systemui.dagger;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideInputMethodManagerFactory implements Factory<InputMethodManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideInputMethodManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public InputMethodManager get() {
        return provideInputMethodManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideInputMethodManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideInputMethodManagerFactory(provider);
    }

    public static InputMethodManager provideInputMethodManager(Context context) {
        return (InputMethodManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideInputMethodManager(context));
    }
}
