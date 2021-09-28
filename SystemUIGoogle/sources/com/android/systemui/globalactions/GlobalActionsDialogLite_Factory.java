package com.android.systemui.globalactions;

import android.app.IActivityManager;
import android.app.admin.DevicePolicyManager;
import android.app.trust.TrustManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Handler;
import android.os.UserManager;
import android.os.Vibrator;
import android.service.dreams.IDreamManager;
import android.telecom.TelecomManager;
import android.view.IWindowManager;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.model.SysUiState;
import com.android.systemui.plugins.GlobalActions;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.util.RingerModeTracker;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class GlobalActionsDialogLite_Factory implements Factory<GlobalActionsDialogLite> {
    private final Provider<AudioManager> audioManagerProvider;
    private final Provider<Executor> backgroundExecutorProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<SysuiColorExtractor> colorExtractorProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DevicePolicyManager> devicePolicyManagerProvider;
    private final Provider<GlobalSettings> globalSettingsProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<IActivityManager> iActivityManagerProvider;
    private final Provider<IDreamManager> iDreamManagerProvider;
    private final Provider<IWindowManager> iWindowManagerProvider;
    private final Provider<GlobalActionsInfoProvider> infoProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<LockPatternUtils> lockPatternUtilsProvider;
    private final Provider<MetricsLogger> metricsLoggerProvider;
    private final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    private final Provider<PackageManager> packageManagerProvider;
    private final Provider<Resources> resourcesProvider;
    private final Provider<RingerModeTracker> ringerModeTrackerProvider;
    private final Provider<SecureSettings> secureSettingsProvider;
    private final Provider<StatusBar> statusBarProvider;
    private final Provider<IStatusBarService> statusBarServiceProvider;
    private final Provider<SysUiState> sysUiStateProvider;
    private final Provider<TelecomManager> telecomManagerProvider;
    private final Provider<TelephonyListenerManager> telephonyListenerManagerProvider;
    private final Provider<TrustManager> trustManagerProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<UserManager> userManagerProvider;
    private final Provider<Vibrator> vibratorProvider;
    private final Provider<GlobalActions.GlobalActionsManager> windowManagerFuncsProvider;

    public GlobalActionsDialogLite_Factory(Provider<Context> provider, Provider<GlobalActions.GlobalActionsManager> provider2, Provider<AudioManager> provider3, Provider<IDreamManager> provider4, Provider<DevicePolicyManager> provider5, Provider<LockPatternUtils> provider6, Provider<BroadcastDispatcher> provider7, Provider<TelephonyListenerManager> provider8, Provider<GlobalSettings> provider9, Provider<SecureSettings> provider10, Provider<Vibrator> provider11, Provider<Resources> provider12, Provider<ConfigurationController> provider13, Provider<KeyguardStateController> provider14, Provider<UserManager> provider15, Provider<TrustManager> provider16, Provider<IActivityManager> provider17, Provider<TelecomManager> provider18, Provider<MetricsLogger> provider19, Provider<SysuiColorExtractor> provider20, Provider<IStatusBarService> provider21, Provider<NotificationShadeWindowController> provider22, Provider<IWindowManager> provider23, Provider<Executor> provider24, Provider<UiEventLogger> provider25, Provider<GlobalActionsInfoProvider> provider26, Provider<RingerModeTracker> provider27, Provider<SysUiState> provider28, Provider<Handler> provider29, Provider<PackageManager> provider30, Provider<StatusBar> provider31) {
        this.contextProvider = provider;
        this.windowManagerFuncsProvider = provider2;
        this.audioManagerProvider = provider3;
        this.iDreamManagerProvider = provider4;
        this.devicePolicyManagerProvider = provider5;
        this.lockPatternUtilsProvider = provider6;
        this.broadcastDispatcherProvider = provider7;
        this.telephonyListenerManagerProvider = provider8;
        this.globalSettingsProvider = provider9;
        this.secureSettingsProvider = provider10;
        this.vibratorProvider = provider11;
        this.resourcesProvider = provider12;
        this.configurationControllerProvider = provider13;
        this.keyguardStateControllerProvider = provider14;
        this.userManagerProvider = provider15;
        this.trustManagerProvider = provider16;
        this.iActivityManagerProvider = provider17;
        this.telecomManagerProvider = provider18;
        this.metricsLoggerProvider = provider19;
        this.colorExtractorProvider = provider20;
        this.statusBarServiceProvider = provider21;
        this.notificationShadeWindowControllerProvider = provider22;
        this.iWindowManagerProvider = provider23;
        this.backgroundExecutorProvider = provider24;
        this.uiEventLoggerProvider = provider25;
        this.infoProvider = provider26;
        this.ringerModeTrackerProvider = provider27;
        this.sysUiStateProvider = provider28;
        this.handlerProvider = provider29;
        this.packageManagerProvider = provider30;
        this.statusBarProvider = provider31;
    }

    @Override // javax.inject.Provider
    public GlobalActionsDialogLite get() {
        return newInstance(this.contextProvider.get(), this.windowManagerFuncsProvider.get(), this.audioManagerProvider.get(), this.iDreamManagerProvider.get(), this.devicePolicyManagerProvider.get(), this.lockPatternUtilsProvider.get(), this.broadcastDispatcherProvider.get(), this.telephonyListenerManagerProvider.get(), this.globalSettingsProvider.get(), this.secureSettingsProvider.get(), this.vibratorProvider.get(), this.resourcesProvider.get(), this.configurationControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.userManagerProvider.get(), this.trustManagerProvider.get(), this.iActivityManagerProvider.get(), this.telecomManagerProvider.get(), this.metricsLoggerProvider.get(), this.colorExtractorProvider.get(), this.statusBarServiceProvider.get(), this.notificationShadeWindowControllerProvider.get(), this.iWindowManagerProvider.get(), this.backgroundExecutorProvider.get(), this.uiEventLoggerProvider.get(), this.infoProvider.get(), this.ringerModeTrackerProvider.get(), this.sysUiStateProvider.get(), this.handlerProvider.get(), this.packageManagerProvider.get(), this.statusBarProvider.get());
    }

    public static GlobalActionsDialogLite_Factory create(Provider<Context> provider, Provider<GlobalActions.GlobalActionsManager> provider2, Provider<AudioManager> provider3, Provider<IDreamManager> provider4, Provider<DevicePolicyManager> provider5, Provider<LockPatternUtils> provider6, Provider<BroadcastDispatcher> provider7, Provider<TelephonyListenerManager> provider8, Provider<GlobalSettings> provider9, Provider<SecureSettings> provider10, Provider<Vibrator> provider11, Provider<Resources> provider12, Provider<ConfigurationController> provider13, Provider<KeyguardStateController> provider14, Provider<UserManager> provider15, Provider<TrustManager> provider16, Provider<IActivityManager> provider17, Provider<TelecomManager> provider18, Provider<MetricsLogger> provider19, Provider<SysuiColorExtractor> provider20, Provider<IStatusBarService> provider21, Provider<NotificationShadeWindowController> provider22, Provider<IWindowManager> provider23, Provider<Executor> provider24, Provider<UiEventLogger> provider25, Provider<GlobalActionsInfoProvider> provider26, Provider<RingerModeTracker> provider27, Provider<SysUiState> provider28, Provider<Handler> provider29, Provider<PackageManager> provider30, Provider<StatusBar> provider31) {
        return new GlobalActionsDialogLite_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27, provider28, provider29, provider30, provider31);
    }

    public static GlobalActionsDialogLite newInstance(Context context, GlobalActions.GlobalActionsManager globalActionsManager, AudioManager audioManager, IDreamManager iDreamManager, DevicePolicyManager devicePolicyManager, LockPatternUtils lockPatternUtils, BroadcastDispatcher broadcastDispatcher, TelephonyListenerManager telephonyListenerManager, GlobalSettings globalSettings, SecureSettings secureSettings, Vibrator vibrator, Resources resources, ConfigurationController configurationController, KeyguardStateController keyguardStateController, UserManager userManager, TrustManager trustManager, IActivityManager iActivityManager, TelecomManager telecomManager, MetricsLogger metricsLogger, SysuiColorExtractor sysuiColorExtractor, IStatusBarService iStatusBarService, NotificationShadeWindowController notificationShadeWindowController, IWindowManager iWindowManager, Executor executor, UiEventLogger uiEventLogger, GlobalActionsInfoProvider globalActionsInfoProvider, RingerModeTracker ringerModeTracker, SysUiState sysUiState, Handler handler, PackageManager packageManager, StatusBar statusBar) {
        return new GlobalActionsDialogLite(context, globalActionsManager, audioManager, iDreamManager, devicePolicyManager, lockPatternUtils, broadcastDispatcher, telephonyListenerManager, globalSettings, secureSettings, vibrator, resources, configurationController, keyguardStateController, userManager, trustManager, iActivityManager, telecomManager, metricsLogger, sysuiColorExtractor, iStatusBarService, notificationShadeWindowController, iWindowManager, executor, uiEventLogger, globalActionsInfoProvider, ringerModeTracker, sysUiState, handler, packageManager, statusBar);
    }
}
