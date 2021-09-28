package com.google.android.systemui.assist.uihints;

import android.os.Handler;
import com.android.systemui.navigationbar.NavigationBarController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class NavBarFader_Factory implements Factory<NavBarFader> {
    private final Provider<Handler> handlerProvider;
    private final Provider<NavigationBarController> navigationBarControllerProvider;

    public NavBarFader_Factory(Provider<NavigationBarController> provider, Provider<Handler> provider2) {
        this.navigationBarControllerProvider = provider;
        this.handlerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public NavBarFader get() {
        return newInstance(DoubleCheck.lazy(this.navigationBarControllerProvider), this.handlerProvider.get());
    }

    public static NavBarFader_Factory create(Provider<NavigationBarController> provider, Provider<Handler> provider2) {
        return new NavBarFader_Factory(provider, provider2);
    }

    public static NavBarFader newInstance(Lazy<NavigationBarController> lazy, Handler handler) {
        return new NavBarFader(lazy, handler);
    }
}
