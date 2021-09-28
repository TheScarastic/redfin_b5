package com.android.systemui.accessibility;

import android.content.Context;
import com.android.systemui.recents.Recents;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.StatusBar;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SystemActions_Factory implements Factory<SystemActions> {
    private final Provider<Context> contextProvider;
    private final Provider<NotificationShadeWindowController> notificationShadeControllerProvider;
    private final Provider<Recents> recentsProvider;
    private final Provider<StatusBar> statusBarProvider;

    public SystemActions_Factory(Provider<Context> provider, Provider<NotificationShadeWindowController> provider2, Provider<StatusBar> provider3, Provider<Recents> provider4) {
        this.contextProvider = provider;
        this.notificationShadeControllerProvider = provider2;
        this.statusBarProvider = provider3;
        this.recentsProvider = provider4;
    }

    @Override // javax.inject.Provider
    public SystemActions get() {
        return newInstance(this.contextProvider.get(), this.notificationShadeControllerProvider.get(), DoubleCheck.lazy(this.statusBarProvider), this.recentsProvider.get());
    }

    public static SystemActions_Factory create(Provider<Context> provider, Provider<NotificationShadeWindowController> provider2, Provider<StatusBar> provider3, Provider<Recents> provider4) {
        return new SystemActions_Factory(provider, provider2, provider3, provider4);
    }

    public static SystemActions newInstance(Context context, NotificationShadeWindowController notificationShadeWindowController, Lazy<StatusBar> lazy, Recents recents) {
        return new SystemActions(context, notificationShadeWindowController, lazy, recents);
    }
}
