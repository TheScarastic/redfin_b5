package com.android.systemui.recents;

import com.android.systemui.statusbar.phone.StatusBar;
import dagger.Lazy;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class OverviewProxyRecentsImpl_Factory implements Factory<OverviewProxyRecentsImpl> {
    private final Provider<Optional<Lazy<StatusBar>>> statusBarLazyProvider;

    public OverviewProxyRecentsImpl_Factory(Provider<Optional<Lazy<StatusBar>>> provider) {
        this.statusBarLazyProvider = provider;
    }

    @Override // javax.inject.Provider
    public OverviewProxyRecentsImpl get() {
        return newInstance(this.statusBarLazyProvider.get());
    }

    public static OverviewProxyRecentsImpl_Factory create(Provider<Optional<Lazy<StatusBar>>> provider) {
        return new OverviewProxyRecentsImpl_Factory(provider);
    }

    public static OverviewProxyRecentsImpl newInstance(Optional<Lazy<StatusBar>> optional) {
        return new OverviewProxyRecentsImpl(optional);
    }
}
