package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.ForegroundServiceController;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AppOpsCoordinator_Factory implements Factory<AppOpsCoordinator> {
    private final Provider<AppOpsController> appOpsControllerProvider;
    private final Provider<ForegroundServiceController> foregroundServiceControllerProvider;
    private final Provider<DelayableExecutor> mainExecutorProvider;

    public AppOpsCoordinator_Factory(Provider<ForegroundServiceController> provider, Provider<AppOpsController> provider2, Provider<DelayableExecutor> provider3) {
        this.foregroundServiceControllerProvider = provider;
        this.appOpsControllerProvider = provider2;
        this.mainExecutorProvider = provider3;
    }

    @Override // javax.inject.Provider
    public AppOpsCoordinator get() {
        return newInstance(this.foregroundServiceControllerProvider.get(), this.appOpsControllerProvider.get(), this.mainExecutorProvider.get());
    }

    public static AppOpsCoordinator_Factory create(Provider<ForegroundServiceController> provider, Provider<AppOpsController> provider2, Provider<DelayableExecutor> provider3) {
        return new AppOpsCoordinator_Factory(provider, provider2, provider3);
    }

    public static AppOpsCoordinator newInstance(ForegroundServiceController foregroundServiceController, AppOpsController appOpsController, DelayableExecutor delayableExecutor) {
        return new AppOpsCoordinator(foregroundServiceController, appOpsController, delayableExecutor);
    }
}
