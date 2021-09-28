package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.NotificationClicker;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationClicker_Builder_Factory implements Factory<NotificationClicker.Builder> {
    private final Provider<NotificationClickerLogger> loggerProvider;

    public NotificationClicker_Builder_Factory(Provider<NotificationClickerLogger> provider) {
        this.loggerProvider = provider;
    }

    @Override // javax.inject.Provider
    public NotificationClicker.Builder get() {
        return newInstance(this.loggerProvider.get());
    }

    public static NotificationClicker_Builder_Factory create(Provider<NotificationClickerLogger> provider) {
        return new NotificationClicker_Builder_Factory(provider);
    }

    public static NotificationClicker.Builder newInstance(NotificationClickerLogger notificationClickerLogger) {
        return new NotificationClicker.Builder(notificationClickerLogger);
    }
}
