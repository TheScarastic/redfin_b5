package com.google.android.systemui.assist;

import com.android.systemui.statusbar.phone.StatusBar;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class OpaEnabledDispatcher_Factory implements Factory<OpaEnabledDispatcher> {
    private final Provider<StatusBar> statusBarLazyProvider;

    public OpaEnabledDispatcher_Factory(Provider<StatusBar> provider) {
        this.statusBarLazyProvider = provider;
    }

    @Override // javax.inject.Provider
    public OpaEnabledDispatcher get() {
        return newInstance(DoubleCheck.lazy(this.statusBarLazyProvider));
    }

    public static OpaEnabledDispatcher_Factory create(Provider<StatusBar> provider) {
        return new OpaEnabledDispatcher_Factory(provider);
    }

    public static OpaEnabledDispatcher newInstance(Lazy<StatusBar> lazy) {
        return new OpaEnabledDispatcher(lazy);
    }
}
