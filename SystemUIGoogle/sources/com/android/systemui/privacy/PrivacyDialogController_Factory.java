package com.android.systemui.privacy;

import android.content.pm.PackageManager;
import android.permission.PermissionManager;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PrivacyDialogController_Factory implements Factory<PrivacyDialogController> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<AppOpsController> appOpsControllerProvider;
    private final Provider<Executor> backgroundExecutorProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<PackageManager> packageManagerProvider;
    private final Provider<PermissionManager> permissionManagerProvider;
    private final Provider<PrivacyItemController> privacyItemControllerProvider;
    private final Provider<PrivacyLogger> privacyLoggerProvider;
    private final Provider<Executor> uiExecutorProvider;
    private final Provider<UserTracker> userTrackerProvider;

    public PrivacyDialogController_Factory(Provider<PermissionManager> provider, Provider<PackageManager> provider2, Provider<PrivacyItemController> provider3, Provider<UserTracker> provider4, Provider<ActivityStarter> provider5, Provider<Executor> provider6, Provider<Executor> provider7, Provider<PrivacyLogger> provider8, Provider<KeyguardStateController> provider9, Provider<AppOpsController> provider10) {
        this.permissionManagerProvider = provider;
        this.packageManagerProvider = provider2;
        this.privacyItemControllerProvider = provider3;
        this.userTrackerProvider = provider4;
        this.activityStarterProvider = provider5;
        this.backgroundExecutorProvider = provider6;
        this.uiExecutorProvider = provider7;
        this.privacyLoggerProvider = provider8;
        this.keyguardStateControllerProvider = provider9;
        this.appOpsControllerProvider = provider10;
    }

    @Override // javax.inject.Provider
    public PrivacyDialogController get() {
        return newInstance(this.permissionManagerProvider.get(), this.packageManagerProvider.get(), this.privacyItemControllerProvider.get(), this.userTrackerProvider.get(), this.activityStarterProvider.get(), this.backgroundExecutorProvider.get(), this.uiExecutorProvider.get(), this.privacyLoggerProvider.get(), this.keyguardStateControllerProvider.get(), this.appOpsControllerProvider.get());
    }

    public static PrivacyDialogController_Factory create(Provider<PermissionManager> provider, Provider<PackageManager> provider2, Provider<PrivacyItemController> provider3, Provider<UserTracker> provider4, Provider<ActivityStarter> provider5, Provider<Executor> provider6, Provider<Executor> provider7, Provider<PrivacyLogger> provider8, Provider<KeyguardStateController> provider9, Provider<AppOpsController> provider10) {
        return new PrivacyDialogController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static PrivacyDialogController newInstance(PermissionManager permissionManager, PackageManager packageManager, PrivacyItemController privacyItemController, UserTracker userTracker, ActivityStarter activityStarter, Executor executor, Executor executor2, PrivacyLogger privacyLogger, KeyguardStateController keyguardStateController, AppOpsController appOpsController) {
        return new PrivacyDialogController(permissionManager, packageManager, privacyItemController, userTracker, activityStarter, executor, executor2, privacyLogger, keyguardStateController, appOpsController);
    }
}
