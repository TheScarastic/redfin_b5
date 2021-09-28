package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class VisualStabilityCoordinator_Factory implements Factory<VisualStabilityCoordinator> {
    private final Provider<DelayableExecutor> delayableExecutorProvider;
    private final Provider<HeadsUpManager> headsUpManagerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public VisualStabilityCoordinator_Factory(Provider<HeadsUpManager> provider, Provider<WakefulnessLifecycle> provider2, Provider<StatusBarStateController> provider3, Provider<DelayableExecutor> provider4) {
        this.headsUpManagerProvider = provider;
        this.wakefulnessLifecycleProvider = provider2;
        this.statusBarStateControllerProvider = provider3;
        this.delayableExecutorProvider = provider4;
    }

    @Override // javax.inject.Provider
    public VisualStabilityCoordinator get() {
        return newInstance(this.headsUpManagerProvider.get(), this.wakefulnessLifecycleProvider.get(), this.statusBarStateControllerProvider.get(), this.delayableExecutorProvider.get());
    }

    public static VisualStabilityCoordinator_Factory create(Provider<HeadsUpManager> provider, Provider<WakefulnessLifecycle> provider2, Provider<StatusBarStateController> provider3, Provider<DelayableExecutor> provider4) {
        return new VisualStabilityCoordinator_Factory(provider, provider2, provider3, provider4);
    }

    public static VisualStabilityCoordinator newInstance(HeadsUpManager headsUpManager, WakefulnessLifecycle wakefulnessLifecycle, StatusBarStateController statusBarStateController, DelayableExecutor delayableExecutor) {
        return new VisualStabilityCoordinator(headsUpManager, wakefulnessLifecycle, statusBarStateController, delayableExecutor);
    }
}
