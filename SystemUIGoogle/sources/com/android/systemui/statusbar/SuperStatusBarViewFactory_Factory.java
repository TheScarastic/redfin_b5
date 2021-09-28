package com.android.systemui.statusbar;

import android.content.Context;
import com.android.systemui.statusbar.notification.row.dagger.NotificationShelfComponent;
import com.android.systemui.util.InjectionInflationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SuperStatusBarViewFactory_Factory implements Factory<SuperStatusBarViewFactory> {
    private final Provider<Context> contextProvider;
    private final Provider<InjectionInflationController> injectionInflationControllerProvider;
    private final Provider<NotificationShelfComponent.Builder> notificationShelfComponentBuilderProvider;

    public SuperStatusBarViewFactory_Factory(Provider<Context> provider, Provider<InjectionInflationController> provider2, Provider<NotificationShelfComponent.Builder> provider3) {
        this.contextProvider = provider;
        this.injectionInflationControllerProvider = provider2;
        this.notificationShelfComponentBuilderProvider = provider3;
    }

    @Override // javax.inject.Provider
    public SuperStatusBarViewFactory get() {
        return newInstance(this.contextProvider.get(), this.injectionInflationControllerProvider.get(), this.notificationShelfComponentBuilderProvider.get());
    }

    public static SuperStatusBarViewFactory_Factory create(Provider<Context> provider, Provider<InjectionInflationController> provider2, Provider<NotificationShelfComponent.Builder> provider3) {
        return new SuperStatusBarViewFactory_Factory(provider, provider2, provider3);
    }

    public static SuperStatusBarViewFactory newInstance(Context context, InjectionInflationController injectionInflationController, NotificationShelfComponent.Builder builder) {
        return new SuperStatusBarViewFactory(context, injectionInflationController, builder);
    }
}
