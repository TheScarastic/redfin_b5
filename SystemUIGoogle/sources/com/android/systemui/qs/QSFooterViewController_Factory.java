package com.android.systemui.qs;

import android.os.UserManager;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.MultiUserSwitchController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.tuner.TunerService;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSFooterViewController_Factory implements Factory<QSFooterViewController> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<GlobalActionsDialogLite> globalActionsDialogProvider;
    private final Provider<MetricsLogger> metricsLoggerProvider;
    private final Provider<MultiUserSwitchController> multiUserSwitchControllerProvider;
    private final Provider<QSPanelController> qsPanelControllerProvider;
    private final Provider<QuickQSPanelController> quickQSPanelControllerProvider;
    private final Provider<Boolean> showPMLiteButtonProvider;
    private final Provider<TunerService> tunerServiceProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<UserInfoController> userInfoControllerProvider;
    private final Provider<UserManager> userManagerProvider;
    private final Provider<UserTracker> userTrackerProvider;
    private final Provider<QSFooterView> viewProvider;

    public QSFooterViewController_Factory(Provider<QSFooterView> provider, Provider<UserManager> provider2, Provider<UserInfoController> provider3, Provider<ActivityStarter> provider4, Provider<DeviceProvisionedController> provider5, Provider<UserTracker> provider6, Provider<QSPanelController> provider7, Provider<MultiUserSwitchController> provider8, Provider<QuickQSPanelController> provider9, Provider<TunerService> provider10, Provider<MetricsLogger> provider11, Provider<FalsingManager> provider12, Provider<Boolean> provider13, Provider<GlobalActionsDialogLite> provider14, Provider<UiEventLogger> provider15) {
        this.viewProvider = provider;
        this.userManagerProvider = provider2;
        this.userInfoControllerProvider = provider3;
        this.activityStarterProvider = provider4;
        this.deviceProvisionedControllerProvider = provider5;
        this.userTrackerProvider = provider6;
        this.qsPanelControllerProvider = provider7;
        this.multiUserSwitchControllerProvider = provider8;
        this.quickQSPanelControllerProvider = provider9;
        this.tunerServiceProvider = provider10;
        this.metricsLoggerProvider = provider11;
        this.falsingManagerProvider = provider12;
        this.showPMLiteButtonProvider = provider13;
        this.globalActionsDialogProvider = provider14;
        this.uiEventLoggerProvider = provider15;
    }

    @Override // javax.inject.Provider
    public QSFooterViewController get() {
        return newInstance(this.viewProvider.get(), this.userManagerProvider.get(), this.userInfoControllerProvider.get(), this.activityStarterProvider.get(), this.deviceProvisionedControllerProvider.get(), this.userTrackerProvider.get(), this.qsPanelControllerProvider.get(), this.multiUserSwitchControllerProvider.get(), this.quickQSPanelControllerProvider.get(), this.tunerServiceProvider.get(), this.metricsLoggerProvider.get(), this.falsingManagerProvider.get(), this.showPMLiteButtonProvider.get().booleanValue(), this.globalActionsDialogProvider.get(), this.uiEventLoggerProvider.get());
    }

    public static QSFooterViewController_Factory create(Provider<QSFooterView> provider, Provider<UserManager> provider2, Provider<UserInfoController> provider3, Provider<ActivityStarter> provider4, Provider<DeviceProvisionedController> provider5, Provider<UserTracker> provider6, Provider<QSPanelController> provider7, Provider<MultiUserSwitchController> provider8, Provider<QuickQSPanelController> provider9, Provider<TunerService> provider10, Provider<MetricsLogger> provider11, Provider<FalsingManager> provider12, Provider<Boolean> provider13, Provider<GlobalActionsDialogLite> provider14, Provider<UiEventLogger> provider15) {
        return new QSFooterViewController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15);
    }

    public static QSFooterViewController newInstance(QSFooterView qSFooterView, UserManager userManager, UserInfoController userInfoController, ActivityStarter activityStarter, DeviceProvisionedController deviceProvisionedController, UserTracker userTracker, QSPanelController qSPanelController, MultiUserSwitchController multiUserSwitchController, QuickQSPanelController quickQSPanelController, TunerService tunerService, MetricsLogger metricsLogger, FalsingManager falsingManager, boolean z, GlobalActionsDialogLite globalActionsDialogLite, UiEventLogger uiEventLogger) {
        return new QSFooterViewController(qSFooterView, userManager, userInfoController, activityStarter, deviceProvisionedController, userTracker, qSPanelController, multiUserSwitchController, quickQSPanelController, tunerService, metricsLogger, falsingManager, z, globalActionsDialogLite, uiEventLogger);
    }
}
