package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SmartspaceDedupingCoordinator_Factory implements Factory<SmartspaceDedupingCoordinator> {
    private final Provider<SystemClock> clockProvider;
    private final Provider<DelayableExecutor> executorProvider;
    private final Provider<NotifPipeline> notifPipelineProvider;
    private final Provider<NotificationEntryManager> notificationEntryManagerProvider;
    private final Provider<NotificationLockscreenUserManager> notificationLockscreenUserManagerProvider;
    private final Provider<LockscreenSmartspaceController> smartspaceControllerProvider;
    private final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;

    public SmartspaceDedupingCoordinator_Factory(Provider<SysuiStatusBarStateController> provider, Provider<LockscreenSmartspaceController> provider2, Provider<NotificationEntryManager> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<NotifPipeline> provider5, Provider<DelayableExecutor> provider6, Provider<SystemClock> provider7) {
        this.statusBarStateControllerProvider = provider;
        this.smartspaceControllerProvider = provider2;
        this.notificationEntryManagerProvider = provider3;
        this.notificationLockscreenUserManagerProvider = provider4;
        this.notifPipelineProvider = provider5;
        this.executorProvider = provider6;
        this.clockProvider = provider7;
    }

    @Override // javax.inject.Provider
    public SmartspaceDedupingCoordinator get() {
        return newInstance(this.statusBarStateControllerProvider.get(), this.smartspaceControllerProvider.get(), this.notificationEntryManagerProvider.get(), this.notificationLockscreenUserManagerProvider.get(), this.notifPipelineProvider.get(), this.executorProvider.get(), this.clockProvider.get());
    }

    public static SmartspaceDedupingCoordinator_Factory create(Provider<SysuiStatusBarStateController> provider, Provider<LockscreenSmartspaceController> provider2, Provider<NotificationEntryManager> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<NotifPipeline> provider5, Provider<DelayableExecutor> provider6, Provider<SystemClock> provider7) {
        return new SmartspaceDedupingCoordinator_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static SmartspaceDedupingCoordinator newInstance(SysuiStatusBarStateController sysuiStatusBarStateController, LockscreenSmartspaceController lockscreenSmartspaceController, NotificationEntryManager notificationEntryManager, NotificationLockscreenUserManager notificationLockscreenUserManager, NotifPipeline notifPipeline, DelayableExecutor delayableExecutor, SystemClock systemClock) {
        return new SmartspaceDedupingCoordinator(sysuiStatusBarStateController, lockscreenSmartspaceController, notificationEntryManager, notificationLockscreenUserManager, notifPipeline, delayableExecutor, systemClock);
    }
}
