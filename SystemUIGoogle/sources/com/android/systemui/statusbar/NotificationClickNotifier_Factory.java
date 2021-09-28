package com.android.systemui.statusbar;

import com.android.internal.statusbar.IStatusBarService;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationClickNotifier_Factory implements Factory<NotificationClickNotifier> {
    private final Provider<IStatusBarService> barServiceProvider;
    private final Provider<Executor> mainExecutorProvider;

    public NotificationClickNotifier_Factory(Provider<IStatusBarService> provider, Provider<Executor> provider2) {
        this.barServiceProvider = provider;
        this.mainExecutorProvider = provider2;
    }

    @Override // javax.inject.Provider
    public NotificationClickNotifier get() {
        return newInstance(this.barServiceProvider.get(), this.mainExecutorProvider.get());
    }

    public static NotificationClickNotifier_Factory create(Provider<IStatusBarService> provider, Provider<Executor> provider2) {
        return new NotificationClickNotifier_Factory(provider, provider2);
    }

    public static NotificationClickNotifier newInstance(IStatusBarService iStatusBarService, Executor executor) {
        return new NotificationClickNotifier(iStatusBarService, executor);
    }
}
