package com.google.android.systemui.columbus.actions;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.StatusBar;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class OpenNotificationShade_Factory implements Factory<OpenNotificationShade> {
    private final Provider<Context> contextProvider;
    private final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    private final Provider<StatusBar> statusBarProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public OpenNotificationShade_Factory(Provider<Context> provider, Provider<NotificationShadeWindowController> provider2, Provider<StatusBar> provider3, Provider<UiEventLogger> provider4) {
        this.contextProvider = provider;
        this.notificationShadeWindowControllerProvider = provider2;
        this.statusBarProvider = provider3;
        this.uiEventLoggerProvider = provider4;
    }

    @Override // javax.inject.Provider
    public OpenNotificationShade get() {
        return newInstance(this.contextProvider.get(), DoubleCheck.lazy(this.notificationShadeWindowControllerProvider), DoubleCheck.lazy(this.statusBarProvider), this.uiEventLoggerProvider.get());
    }

    public static OpenNotificationShade_Factory create(Provider<Context> provider, Provider<NotificationShadeWindowController> provider2, Provider<StatusBar> provider3, Provider<UiEventLogger> provider4) {
        return new OpenNotificationShade_Factory(provider, provider2, provider3, provider4);
    }

    public static OpenNotificationShade newInstance(Context context, Lazy<NotificationShadeWindowController> lazy, Lazy<StatusBar> lazy2, UiEventLogger uiEventLogger) {
        return new OpenNotificationShade(context, lazy, lazy2, uiEventLogger);
    }
}
