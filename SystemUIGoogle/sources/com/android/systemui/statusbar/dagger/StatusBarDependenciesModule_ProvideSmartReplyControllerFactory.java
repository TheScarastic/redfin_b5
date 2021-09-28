package com.android.systemui.statusbar.dagger;

import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.statusbar.NotificationClickNotifier;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarDependenciesModule_ProvideSmartReplyControllerFactory implements Factory<SmartReplyController> {
    private final Provider<NotificationClickNotifier> clickNotifierProvider;
    private final Provider<NotificationEntryManager> entryManagerProvider;
    private final Provider<IStatusBarService> statusBarServiceProvider;

    public StatusBarDependenciesModule_ProvideSmartReplyControllerFactory(Provider<NotificationEntryManager> provider, Provider<IStatusBarService> provider2, Provider<NotificationClickNotifier> provider3) {
        this.entryManagerProvider = provider;
        this.statusBarServiceProvider = provider2;
        this.clickNotifierProvider = provider3;
    }

    @Override // javax.inject.Provider
    public SmartReplyController get() {
        return provideSmartReplyController(this.entryManagerProvider.get(), this.statusBarServiceProvider.get(), this.clickNotifierProvider.get());
    }

    public static StatusBarDependenciesModule_ProvideSmartReplyControllerFactory create(Provider<NotificationEntryManager> provider, Provider<IStatusBarService> provider2, Provider<NotificationClickNotifier> provider3) {
        return new StatusBarDependenciesModule_ProvideSmartReplyControllerFactory(provider, provider2, provider3);
    }

    public static SmartReplyController provideSmartReplyController(NotificationEntryManager notificationEntryManager, IStatusBarService iStatusBarService, NotificationClickNotifier notificationClickNotifier) {
        return (SmartReplyController) Preconditions.checkNotNullFromProvides(StatusBarDependenciesModule.provideSmartReplyController(notificationEntryManager, iStatusBarService, notificationClickNotifier));
    }
}
