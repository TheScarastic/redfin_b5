package com.android.systemui.dagger;

import android.content.Context;
import com.android.internal.util.NotificationMessagingUtil;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvideNotificationMessagingUtilFactory implements Factory<NotificationMessagingUtil> {
    private final Provider<Context> contextProvider;
    private final DependencyProvider module;

    public DependencyProvider_ProvideNotificationMessagingUtilFactory(DependencyProvider dependencyProvider, Provider<Context> provider) {
        this.module = dependencyProvider;
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public NotificationMessagingUtil get() {
        return provideNotificationMessagingUtil(this.module, this.contextProvider.get());
    }

    public static DependencyProvider_ProvideNotificationMessagingUtilFactory create(DependencyProvider dependencyProvider, Provider<Context> provider) {
        return new DependencyProvider_ProvideNotificationMessagingUtilFactory(dependencyProvider, provider);
    }

    public static NotificationMessagingUtil provideNotificationMessagingUtil(DependencyProvider dependencyProvider, Context context) {
        return (NotificationMessagingUtil) Preconditions.checkNotNullFromProvides(dependencyProvider.provideNotificationMessagingUtil(context));
    }
}
