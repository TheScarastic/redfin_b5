package com.android.systemui.navigationbar;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NavigationBarOverlayController_Factory implements Factory<NavigationBarOverlayController> {
    private final Provider<Context> contextProvider;

    public NavigationBarOverlayController_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public NavigationBarOverlayController get() {
        return newInstance(this.contextProvider.get());
    }

    public static NavigationBarOverlayController_Factory create(Provider<Context> provider) {
        return new NavigationBarOverlayController_Factory(provider);
    }

    public static NavigationBarOverlayController newInstance(Context context) {
        return new NavigationBarOverlayController(context);
    }
}
