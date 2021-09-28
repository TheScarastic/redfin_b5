package com.android.systemui.dagger;

import android.app.NotificationManager;
import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideNotificationManagerFactory implements Factory<NotificationManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideNotificationManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public NotificationManager get() {
        return provideNotificationManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideNotificationManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideNotificationManagerFactory(provider);
    }

    public static NotificationManager provideNotificationManager(Context context) {
        return (NotificationManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideNotificationManager(context));
    }
}
