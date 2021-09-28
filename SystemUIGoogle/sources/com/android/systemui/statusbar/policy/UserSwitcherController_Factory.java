package com.android.systemui.statusbar.policy;

import android.app.IActivityTaskManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.Handler;
import android.os.UserManager;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class UserSwitcherController_Factory implements Factory<UserSwitcherController> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<IActivityTaskManager> activityTaskManagerProvider;
    private final Provider<Executor> bgExecutorProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DevicePolicyManager> devicePolicyManagerProvider;
    private final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<SecureSettings> secureSettingsProvider;
    private final Provider<TelephonyListenerManager> telephonyListenerManagerProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<UserSwitcherController.UserDetailAdapter> userDetailAdapterProvider;
    private final Provider<UserManager> userManagerProvider;
    private final Provider<UserTracker> userTrackerProvider;

    public UserSwitcherController_Factory(Provider<Context> provider, Provider<UserManager> provider2, Provider<UserTracker> provider3, Provider<KeyguardStateController> provider4, Provider<DeviceProvisionedController> provider5, Provider<DevicePolicyManager> provider6, Provider<Handler> provider7, Provider<ActivityStarter> provider8, Provider<BroadcastDispatcher> provider9, Provider<UiEventLogger> provider10, Provider<FalsingManager> provider11, Provider<TelephonyListenerManager> provider12, Provider<IActivityTaskManager> provider13, Provider<UserSwitcherController.UserDetailAdapter> provider14, Provider<SecureSettings> provider15, Provider<Executor> provider16) {
        this.contextProvider = provider;
        this.userManagerProvider = provider2;
        this.userTrackerProvider = provider3;
        this.keyguardStateControllerProvider = provider4;
        this.deviceProvisionedControllerProvider = provider5;
        this.devicePolicyManagerProvider = provider6;
        this.handlerProvider = provider7;
        this.activityStarterProvider = provider8;
        this.broadcastDispatcherProvider = provider9;
        this.uiEventLoggerProvider = provider10;
        this.falsingManagerProvider = provider11;
        this.telephonyListenerManagerProvider = provider12;
        this.activityTaskManagerProvider = provider13;
        this.userDetailAdapterProvider = provider14;
        this.secureSettingsProvider = provider15;
        this.bgExecutorProvider = provider16;
    }

    @Override // javax.inject.Provider
    public UserSwitcherController get() {
        return newInstance(this.contextProvider.get(), this.userManagerProvider.get(), this.userTrackerProvider.get(), this.keyguardStateControllerProvider.get(), this.deviceProvisionedControllerProvider.get(), this.devicePolicyManagerProvider.get(), this.handlerProvider.get(), this.activityStarterProvider.get(), this.broadcastDispatcherProvider.get(), this.uiEventLoggerProvider.get(), this.falsingManagerProvider.get(), this.telephonyListenerManagerProvider.get(), this.activityTaskManagerProvider.get(), this.userDetailAdapterProvider.get(), this.secureSettingsProvider.get(), this.bgExecutorProvider.get());
    }

    public static UserSwitcherController_Factory create(Provider<Context> provider, Provider<UserManager> provider2, Provider<UserTracker> provider3, Provider<KeyguardStateController> provider4, Provider<DeviceProvisionedController> provider5, Provider<DevicePolicyManager> provider6, Provider<Handler> provider7, Provider<ActivityStarter> provider8, Provider<BroadcastDispatcher> provider9, Provider<UiEventLogger> provider10, Provider<FalsingManager> provider11, Provider<TelephonyListenerManager> provider12, Provider<IActivityTaskManager> provider13, Provider<UserSwitcherController.UserDetailAdapter> provider14, Provider<SecureSettings> provider15, Provider<Executor> provider16) {
        return new UserSwitcherController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16);
    }

    public static UserSwitcherController newInstance(Context context, UserManager userManager, UserTracker userTracker, KeyguardStateController keyguardStateController, DeviceProvisionedController deviceProvisionedController, DevicePolicyManager devicePolicyManager, Handler handler, ActivityStarter activityStarter, BroadcastDispatcher broadcastDispatcher, UiEventLogger uiEventLogger, FalsingManager falsingManager, TelephonyListenerManager telephonyListenerManager, IActivityTaskManager iActivityTaskManager, UserSwitcherController.UserDetailAdapter userDetailAdapter, SecureSettings secureSettings, Executor executor) {
        return new UserSwitcherController(context, userManager, userTracker, keyguardStateController, deviceProvisionedController, devicePolicyManager, handler, activityStarter, broadcastDispatcher, uiEventLogger, falsingManager, telephonyListenerManager, iActivityTaskManager, userDetailAdapter, secureSettings, executor);
    }
}
