package com.android.systemui.privacy;

import com.android.systemui.appops.AppOpsController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PrivacyItemController_Factory implements Factory<PrivacyItemController> {
    private final Provider<AppOpsController> appOpsControllerProvider;
    private final Provider<DelayableExecutor> bgExecutorProvider;
    private final Provider<DeviceConfigProxy> deviceConfigProxyProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<PrivacyLogger> loggerProvider;
    private final Provider<SystemClock> systemClockProvider;
    private final Provider<DelayableExecutor> uiExecutorProvider;
    private final Provider<UserTracker> userTrackerProvider;

    public PrivacyItemController_Factory(Provider<AppOpsController> provider, Provider<DelayableExecutor> provider2, Provider<DelayableExecutor> provider3, Provider<DeviceConfigProxy> provider4, Provider<UserTracker> provider5, Provider<PrivacyLogger> provider6, Provider<SystemClock> provider7, Provider<DumpManager> provider8) {
        this.appOpsControllerProvider = provider;
        this.uiExecutorProvider = provider2;
        this.bgExecutorProvider = provider3;
        this.deviceConfigProxyProvider = provider4;
        this.userTrackerProvider = provider5;
        this.loggerProvider = provider6;
        this.systemClockProvider = provider7;
        this.dumpManagerProvider = provider8;
    }

    @Override // javax.inject.Provider
    public PrivacyItemController get() {
        return newInstance(this.appOpsControllerProvider.get(), this.uiExecutorProvider.get(), this.bgExecutorProvider.get(), this.deviceConfigProxyProvider.get(), this.userTrackerProvider.get(), this.loggerProvider.get(), this.systemClockProvider.get(), this.dumpManagerProvider.get());
    }

    public static PrivacyItemController_Factory create(Provider<AppOpsController> provider, Provider<DelayableExecutor> provider2, Provider<DelayableExecutor> provider3, Provider<DeviceConfigProxy> provider4, Provider<UserTracker> provider5, Provider<PrivacyLogger> provider6, Provider<SystemClock> provider7, Provider<DumpManager> provider8) {
        return new PrivacyItemController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    public static PrivacyItemController newInstance(AppOpsController appOpsController, DelayableExecutor delayableExecutor, DelayableExecutor delayableExecutor2, DeviceConfigProxy deviceConfigProxy, UserTracker userTracker, PrivacyLogger privacyLogger, SystemClock systemClock, DumpManager dumpManager) {
        return new PrivacyItemController(appOpsController, delayableExecutor, delayableExecutor2, deviceConfigProxy, userTracker, privacyLogger, systemClock, dumpManager);
    }
}
