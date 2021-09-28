package com.android.systemui.statusbar.notification.collection.render;

import android.content.Context;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ShadeViewManagerFactory_Factory implements Factory<ShadeViewManagerFactory> {
    private final Provider<Context> contextProvider;
    private final Provider<ShadeViewDifferLogger> loggerProvider;
    private final Provider<NotificationIconAreaController> notificationIconAreaControllerProvider;
    private final Provider<NotifViewBarn> viewBarnProvider;

    public ShadeViewManagerFactory_Factory(Provider<Context> provider, Provider<ShadeViewDifferLogger> provider2, Provider<NotifViewBarn> provider3, Provider<NotificationIconAreaController> provider4) {
        this.contextProvider = provider;
        this.loggerProvider = provider2;
        this.viewBarnProvider = provider3;
        this.notificationIconAreaControllerProvider = provider4;
    }

    @Override // javax.inject.Provider
    public ShadeViewManagerFactory get() {
        return newInstance(this.contextProvider.get(), this.loggerProvider.get(), this.viewBarnProvider.get(), this.notificationIconAreaControllerProvider.get());
    }

    public static ShadeViewManagerFactory_Factory create(Provider<Context> provider, Provider<ShadeViewDifferLogger> provider2, Provider<NotifViewBarn> provider3, Provider<NotificationIconAreaController> provider4) {
        return new ShadeViewManagerFactory_Factory(provider, provider2, provider3, provider4);
    }

    public static ShadeViewManagerFactory newInstance(Context context, ShadeViewDifferLogger shadeViewDifferLogger, NotifViewBarn notifViewBarn, NotificationIconAreaController notificationIconAreaController) {
        return new ShadeViewManagerFactory(context, shadeViewDifferLogger, notifViewBarn, notificationIconAreaController);
    }
}
