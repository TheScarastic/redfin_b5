package com.android.systemui;

import android.app.AlarmManager;
import android.app.INotificationManager;
import android.app.IWallpaperManager;
import android.hardware.SensorPrivacyManager;
import android.hardware.display.NightDisplayListener;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.IWindowManager;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.clock.ClockManager;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.accessibility.AccessibilityButtonTargetsObserver;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuController;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.dock.DockManager;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.fragments.FragmentService;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.media.dialog.MediaOutputDialogFactory;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationBarOverlayController;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.plugins.PluginDependencyProvider;
import com.android.systemui.plugins.VolumeDialogController;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.power.EnhancedEstimates;
import com.android.systemui.power.PowerUI;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.qs.ReduceBrightColorsController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.recents.Recents;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.DevicePolicyManagerWrapper;
import com.android.systemui.shared.system.PackageManagerWrapper;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.NotificationViewHierarchyManager;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.events.PrivacyDotViewController;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationFilter;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import com.android.systemui.statusbar.phone.ManagedProfileController;
import com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusBarWindowController;
import com.android.systemui.statusbar.policy.AccessibilityController;
import com.android.systemui.statusbar.policy.AccessibilityManagerWrapper;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.BluetoothController;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.ExtensionController;
import com.android.systemui.statusbar.policy.FlashlightController;
import com.android.systemui.statusbar.policy.HotspotController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.LocationController;
import com.android.systemui.statusbar.policy.NetworkController;
import com.android.systemui.statusbar.policy.NextAlarmController;
import com.android.systemui.statusbar.policy.RemoteInputQuickSettingsDisabler;
import com.android.systemui.statusbar.policy.RotationLockController;
import com.android.systemui.statusbar.policy.SecurityController;
import com.android.systemui.statusbar.policy.SensorPrivacyController;
import com.android.systemui.statusbar.policy.SmartReplyConstants;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.tracing.ProtoTracer;
import com.android.systemui.tuner.TunablePadding;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.leak.GarbageMonitor;
import com.android.systemui.util.leak.LeakDetector;
import com.android.systemui.util.leak.LeakReporter;
import com.android.systemui.util.sensors.AsyncSensorManager;
import dagger.Lazy;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class Dependency_MembersInjector {
    public static void injectMDumpManager(Dependency dependency, DumpManager dumpManager) {
        dependency.mDumpManager = dumpManager;
    }

    public static void injectMActivityStarter(Dependency dependency, Lazy<ActivityStarter> lazy) {
        dependency.mActivityStarter = lazy;
    }

    public static void injectMBroadcastDispatcher(Dependency dependency, Lazy<BroadcastDispatcher> lazy) {
        dependency.mBroadcastDispatcher = lazy;
    }

    public static void injectMAsyncSensorManager(Dependency dependency, Lazy<AsyncSensorManager> lazy) {
        dependency.mAsyncSensorManager = lazy;
    }

    public static void injectMBluetoothController(Dependency dependency, Lazy<BluetoothController> lazy) {
        dependency.mBluetoothController = lazy;
    }

    public static void injectMLocationController(Dependency dependency, Lazy<LocationController> lazy) {
        dependency.mLocationController = lazy;
    }

    public static void injectMRotationLockController(Dependency dependency, Lazy<RotationLockController> lazy) {
        dependency.mRotationLockController = lazy;
    }

    public static void injectMNetworkController(Dependency dependency, Lazy<NetworkController> lazy) {
        dependency.mNetworkController = lazy;
    }

    public static void injectMZenModeController(Dependency dependency, Lazy<ZenModeController> lazy) {
        dependency.mZenModeController = lazy;
    }

    public static void injectMHotspotController(Dependency dependency, Lazy<HotspotController> lazy) {
        dependency.mHotspotController = lazy;
    }

    public static void injectMCastController(Dependency dependency, Lazy<CastController> lazy) {
        dependency.mCastController = lazy;
    }

    public static void injectMFlashlightController(Dependency dependency, Lazy<FlashlightController> lazy) {
        dependency.mFlashlightController = lazy;
    }

    public static void injectMUserSwitcherController(Dependency dependency, Lazy<UserSwitcherController> lazy) {
        dependency.mUserSwitcherController = lazy;
    }

    public static void injectMUserInfoController(Dependency dependency, Lazy<UserInfoController> lazy) {
        dependency.mUserInfoController = lazy;
    }

    public static void injectMKeyguardMonitor(Dependency dependency, Lazy<KeyguardStateController> lazy) {
        dependency.mKeyguardMonitor = lazy;
    }

    public static void injectMKeyguardUpdateMonitor(Dependency dependency, Lazy<KeyguardUpdateMonitor> lazy) {
        dependency.mKeyguardUpdateMonitor = lazy;
    }

    public static void injectMBatteryController(Dependency dependency, Lazy<BatteryController> lazy) {
        dependency.mBatteryController = lazy;
    }

    public static void injectMNightDisplayListener(Dependency dependency, Lazy<NightDisplayListener> lazy) {
        dependency.mNightDisplayListener = lazy;
    }

    public static void injectMReduceBrightColorsController(Dependency dependency, Lazy<ReduceBrightColorsController> lazy) {
        dependency.mReduceBrightColorsController = lazy;
    }

    public static void injectMManagedProfileController(Dependency dependency, Lazy<ManagedProfileController> lazy) {
        dependency.mManagedProfileController = lazy;
    }

    public static void injectMNextAlarmController(Dependency dependency, Lazy<NextAlarmController> lazy) {
        dependency.mNextAlarmController = lazy;
    }

    public static void injectMDataSaverController(Dependency dependency, Lazy<DataSaverController> lazy) {
        dependency.mDataSaverController = lazy;
    }

    public static void injectMAccessibilityController(Dependency dependency, Lazy<AccessibilityController> lazy) {
        dependency.mAccessibilityController = lazy;
    }

    public static void injectMDeviceProvisionedController(Dependency dependency, Lazy<DeviceProvisionedController> lazy) {
        dependency.mDeviceProvisionedController = lazy;
    }

    public static void injectMPluginManager(Dependency dependency, Lazy<PluginManager> lazy) {
        dependency.mPluginManager = lazy;
    }

    public static void injectMAssistManager(Dependency dependency, Lazy<AssistManager> lazy) {
        dependency.mAssistManager = lazy;
    }

    public static void injectMSecurityController(Dependency dependency, Lazy<SecurityController> lazy) {
        dependency.mSecurityController = lazy;
    }

    public static void injectMLeakDetector(Dependency dependency, Lazy<LeakDetector> lazy) {
        dependency.mLeakDetector = lazy;
    }

    public static void injectMLeakReporter(Dependency dependency, Lazy<LeakReporter> lazy) {
        dependency.mLeakReporter = lazy;
    }

    public static void injectMGarbageMonitor(Dependency dependency, Lazy<GarbageMonitor> lazy) {
        dependency.mGarbageMonitor = lazy;
    }

    public static void injectMTunerService(Dependency dependency, Lazy<TunerService> lazy) {
        dependency.mTunerService = lazy;
    }

    public static void injectMNotificationShadeWindowController(Dependency dependency, Lazy<NotificationShadeWindowController> lazy) {
        dependency.mNotificationShadeWindowController = lazy;
    }

    public static void injectMTempStatusBarWindowController(Dependency dependency, Lazy<StatusBarWindowController> lazy) {
        dependency.mTempStatusBarWindowController = lazy;
    }

    public static void injectMDarkIconDispatcher(Dependency dependency, Lazy<DarkIconDispatcher> lazy) {
        dependency.mDarkIconDispatcher = lazy;
    }

    public static void injectMConfigurationController(Dependency dependency, Lazy<ConfigurationController> lazy) {
        dependency.mConfigurationController = lazy;
    }

    public static void injectMStatusBarIconController(Dependency dependency, Lazy<StatusBarIconController> lazy) {
        dependency.mStatusBarIconController = lazy;
    }

    public static void injectMScreenLifecycle(Dependency dependency, Lazy<ScreenLifecycle> lazy) {
        dependency.mScreenLifecycle = lazy;
    }

    public static void injectMWakefulnessLifecycle(Dependency dependency, Lazy<WakefulnessLifecycle> lazy) {
        dependency.mWakefulnessLifecycle = lazy;
    }

    public static void injectMFragmentService(Dependency dependency, Lazy<FragmentService> lazy) {
        dependency.mFragmentService = lazy;
    }

    public static void injectMExtensionController(Dependency dependency, Lazy<ExtensionController> lazy) {
        dependency.mExtensionController = lazy;
    }

    public static void injectMPluginDependencyProvider(Dependency dependency, Lazy<PluginDependencyProvider> lazy) {
        dependency.mPluginDependencyProvider = lazy;
    }

    public static void injectMLocalBluetoothManager(Dependency dependency, Lazy<LocalBluetoothManager> lazy) {
        dependency.mLocalBluetoothManager = lazy;
    }

    public static void injectMVolumeDialogController(Dependency dependency, Lazy<VolumeDialogController> lazy) {
        dependency.mVolumeDialogController = lazy;
    }

    public static void injectMMetricsLogger(Dependency dependency, Lazy<MetricsLogger> lazy) {
        dependency.mMetricsLogger = lazy;
    }

    public static void injectMAccessibilityManagerWrapper(Dependency dependency, Lazy<AccessibilityManagerWrapper> lazy) {
        dependency.mAccessibilityManagerWrapper = lazy;
    }

    public static void injectMSysuiColorExtractor(Dependency dependency, Lazy<SysuiColorExtractor> lazy) {
        dependency.mSysuiColorExtractor = lazy;
    }

    public static void injectMTunablePaddingService(Dependency dependency, Lazy<TunablePadding.TunablePaddingService> lazy) {
        dependency.mTunablePaddingService = lazy;
    }

    public static void injectMForegroundServiceController(Dependency dependency, Lazy<ForegroundServiceController> lazy) {
        dependency.mForegroundServiceController = lazy;
    }

    public static void injectMUiOffloadThread(Dependency dependency, Lazy<UiOffloadThread> lazy) {
        dependency.mUiOffloadThread = lazy;
    }

    public static void injectMWarningsUI(Dependency dependency, Lazy<PowerUI.WarningsUI> lazy) {
        dependency.mWarningsUI = lazy;
    }

    public static void injectMLightBarController(Dependency dependency, Lazy<LightBarController> lazy) {
        dependency.mLightBarController = lazy;
    }

    public static void injectMIWindowManager(Dependency dependency, Lazy<IWindowManager> lazy) {
        dependency.mIWindowManager = lazy;
    }

    public static void injectMOverviewProxyService(Dependency dependency, Lazy<OverviewProxyService> lazy) {
        dependency.mOverviewProxyService = lazy;
    }

    public static void injectMNavBarModeController(Dependency dependency, Lazy<NavigationModeController> lazy) {
        dependency.mNavBarModeController = lazy;
    }

    public static void injectMAccessibilityButtonModeObserver(Dependency dependency, Lazy<AccessibilityButtonModeObserver> lazy) {
        dependency.mAccessibilityButtonModeObserver = lazy;
    }

    public static void injectMAccessibilityButtonListController(Dependency dependency, Lazy<AccessibilityButtonTargetsObserver> lazy) {
        dependency.mAccessibilityButtonListController = lazy;
    }

    public static void injectMEnhancedEstimates(Dependency dependency, Lazy<EnhancedEstimates> lazy) {
        dependency.mEnhancedEstimates = lazy;
    }

    public static void injectMVibratorHelper(Dependency dependency, Lazy<VibratorHelper> lazy) {
        dependency.mVibratorHelper = lazy;
    }

    public static void injectMIStatusBarService(Dependency dependency, Lazy<IStatusBarService> lazy) {
        dependency.mIStatusBarService = lazy;
    }

    public static void injectMDisplayMetrics(Dependency dependency, Lazy<DisplayMetrics> lazy) {
        dependency.mDisplayMetrics = lazy;
    }

    public static void injectMLockscreenGestureLogger(Dependency dependency, Lazy<LockscreenGestureLogger> lazy) {
        dependency.mLockscreenGestureLogger = lazy;
    }

    public static void injectMKeyguardEnvironment(Dependency dependency, Lazy<NotificationEntryManager.KeyguardEnvironment> lazy) {
        dependency.mKeyguardEnvironment = lazy;
    }

    public static void injectMShadeController(Dependency dependency, Lazy<ShadeController> lazy) {
        dependency.mShadeController = lazy;
    }

    public static void injectMNotificationRemoteInputManagerCallback(Dependency dependency, Lazy<NotificationRemoteInputManager.Callback> lazy) {
        dependency.mNotificationRemoteInputManagerCallback = lazy;
    }

    public static void injectMAppOpsController(Dependency dependency, Lazy<AppOpsController> lazy) {
        dependency.mAppOpsController = lazy;
    }

    public static void injectMNavigationBarController(Dependency dependency, Lazy<NavigationBarController> lazy) {
        dependency.mNavigationBarController = lazy;
    }

    public static void injectMAccessibilityFloatingMenuController(Dependency dependency, Lazy<AccessibilityFloatingMenuController> lazy) {
        dependency.mAccessibilityFloatingMenuController = lazy;
    }

    public static void injectMStatusBarStateController(Dependency dependency, Lazy<StatusBarStateController> lazy) {
        dependency.mStatusBarStateController = lazy;
    }

    public static void injectMNotificationLockscreenUserManager(Dependency dependency, Lazy<NotificationLockscreenUserManager> lazy) {
        dependency.mNotificationLockscreenUserManager = lazy;
    }

    public static void injectMNotificationGroupAlertTransferHelper(Dependency dependency, Lazy<NotificationGroupAlertTransferHelper> lazy) {
        dependency.mNotificationGroupAlertTransferHelper = lazy;
    }

    public static void injectMNotificationGroupManager(Dependency dependency, Lazy<NotificationGroupManagerLegacy> lazy) {
        dependency.mNotificationGroupManager = lazy;
    }

    public static void injectMVisualStabilityManager(Dependency dependency, Lazy<VisualStabilityManager> lazy) {
        dependency.mVisualStabilityManager = lazy;
    }

    public static void injectMNotificationGutsManager(Dependency dependency, Lazy<NotificationGutsManager> lazy) {
        dependency.mNotificationGutsManager = lazy;
    }

    public static void injectMNotificationMediaManager(Dependency dependency, Lazy<NotificationMediaManager> lazy) {
        dependency.mNotificationMediaManager = lazy;
    }

    public static void injectMNotificationRemoteInputManager(Dependency dependency, Lazy<NotificationRemoteInputManager> lazy) {
        dependency.mNotificationRemoteInputManager = lazy;
    }

    public static void injectMSmartReplyConstants(Dependency dependency, Lazy<SmartReplyConstants> lazy) {
        dependency.mSmartReplyConstants = lazy;
    }

    public static void injectMNotificationListener(Dependency dependency, Lazy<NotificationListener> lazy) {
        dependency.mNotificationListener = lazy;
    }

    public static void injectMNotificationLogger(Dependency dependency, Lazy<NotificationLogger> lazy) {
        dependency.mNotificationLogger = lazy;
    }

    public static void injectMNotificationViewHierarchyManager(Dependency dependency, Lazy<NotificationViewHierarchyManager> lazy) {
        dependency.mNotificationViewHierarchyManager = lazy;
    }

    public static void injectMNotificationFilter(Dependency dependency, Lazy<NotificationFilter> lazy) {
        dependency.mNotificationFilter = lazy;
    }

    public static void injectMKeyguardDismissUtil(Dependency dependency, Lazy<KeyguardDismissUtil> lazy) {
        dependency.mKeyguardDismissUtil = lazy;
    }

    public static void injectMSmartReplyController(Dependency dependency, Lazy<SmartReplyController> lazy) {
        dependency.mSmartReplyController = lazy;
    }

    public static void injectMRemoteInputQuickSettingsDisabler(Dependency dependency, Lazy<RemoteInputQuickSettingsDisabler> lazy) {
        dependency.mRemoteInputQuickSettingsDisabler = lazy;
    }

    public static void injectMNotificationEntryManager(Dependency dependency, Lazy<NotificationEntryManager> lazy) {
        dependency.mNotificationEntryManager = lazy;
    }

    public static void injectMSensorPrivacyManager(Dependency dependency, Lazy<SensorPrivacyManager> lazy) {
        dependency.mSensorPrivacyManager = lazy;
    }

    public static void injectMAutoHideController(Dependency dependency, Lazy<AutoHideController> lazy) {
        dependency.mAutoHideController = lazy;
    }

    public static void injectMForegroundServiceNotificationListener(Dependency dependency, Lazy<ForegroundServiceNotificationListener> lazy) {
        dependency.mForegroundServiceNotificationListener = lazy;
    }

    public static void injectMPrivacyItemController(Dependency dependency, Lazy<PrivacyItemController> lazy) {
        dependency.mPrivacyItemController = lazy;
    }

    public static void injectMBgLooper(Dependency dependency, Lazy<Looper> lazy) {
        dependency.mBgLooper = lazy;
    }

    public static void injectMBgHandler(Dependency dependency, Lazy<Handler> lazy) {
        dependency.mBgHandler = lazy;
    }

    public static void injectMMainLooper(Dependency dependency, Lazy<Looper> lazy) {
        dependency.mMainLooper = lazy;
    }

    public static void injectMMainHandler(Dependency dependency, Lazy<Handler> lazy) {
        dependency.mMainHandler = lazy;
    }

    public static void injectMTimeTickHandler(Dependency dependency, Lazy<Handler> lazy) {
        dependency.mTimeTickHandler = lazy;
    }

    public static void injectMLeakReportEmail(Dependency dependency, Lazy<String> lazy) {
        dependency.mLeakReportEmail = lazy;
    }

    public static void injectMMainExecutor(Dependency dependency, Lazy<Executor> lazy) {
        dependency.mMainExecutor = lazy;
    }

    public static void injectMBackgroundExecutor(Dependency dependency, Lazy<Executor> lazy) {
        dependency.mBackgroundExecutor = lazy;
    }

    public static void injectMClockManager(Dependency dependency, Lazy<ClockManager> lazy) {
        dependency.mClockManager = lazy;
    }

    public static void injectMActivityManagerWrapper(Dependency dependency, Lazy<ActivityManagerWrapper> lazy) {
        dependency.mActivityManagerWrapper = lazy;
    }

    public static void injectMDevicePolicyManagerWrapper(Dependency dependency, Lazy<DevicePolicyManagerWrapper> lazy) {
        dependency.mDevicePolicyManagerWrapper = lazy;
    }

    public static void injectMPackageManagerWrapper(Dependency dependency, Lazy<PackageManagerWrapper> lazy) {
        dependency.mPackageManagerWrapper = lazy;
    }

    public static void injectMSensorPrivacyController(Dependency dependency, Lazy<SensorPrivacyController> lazy) {
        dependency.mSensorPrivacyController = lazy;
    }

    public static void injectMDockManager(Dependency dependency, Lazy<DockManager> lazy) {
        dependency.mDockManager = lazy;
    }

    public static void injectMINotificationManager(Dependency dependency, Lazy<INotificationManager> lazy) {
        dependency.mINotificationManager = lazy;
    }

    public static void injectMSysUiStateFlagsContainer(Dependency dependency, Lazy<SysUiState> lazy) {
        dependency.mSysUiStateFlagsContainer = lazy;
    }

    public static void injectMAlarmManager(Dependency dependency, Lazy<AlarmManager> lazy) {
        dependency.mAlarmManager = lazy;
    }

    public static void injectMKeyguardSecurityModel(Dependency dependency, Lazy<KeyguardSecurityModel> lazy) {
        dependency.mKeyguardSecurityModel = lazy;
    }

    public static void injectMDozeParameters(Dependency dependency, Lazy<DozeParameters> lazy) {
        dependency.mDozeParameters = lazy;
    }

    public static void injectMWallpaperManager(Dependency dependency, Lazy<IWallpaperManager> lazy) {
        dependency.mWallpaperManager = lazy;
    }

    public static void injectMCommandQueue(Dependency dependency, Lazy<CommandQueue> lazy) {
        dependency.mCommandQueue = lazy;
    }

    public static void injectMRecents(Dependency dependency, Lazy<Recents> lazy) {
        dependency.mRecents = lazy;
    }

    public static void injectMStatusBar(Dependency dependency, Lazy<StatusBar> lazy) {
        dependency.mStatusBar = lazy;
    }

    public static void injectMRecordingController(Dependency dependency, Lazy<RecordingController> lazy) {
        dependency.mRecordingController = lazy;
    }

    public static void injectMProtoTracer(Dependency dependency, Lazy<ProtoTracer> lazy) {
        dependency.mProtoTracer = lazy;
    }

    public static void injectMMediaOutputDialogFactory(Dependency dependency, Lazy<MediaOutputDialogFactory> lazy) {
        dependency.mMediaOutputDialogFactory = lazy;
    }

    public static void injectMDeviceConfigProxy(Dependency dependency, Lazy<DeviceConfigProxy> lazy) {
        dependency.mDeviceConfigProxy = lazy;
    }

    public static void injectMNavbarButtonsControllerLazy(Dependency dependency, Lazy<NavigationBarOverlayController> lazy) {
        dependency.mNavbarButtonsControllerLazy = lazy;
    }

    public static void injectMTelephonyListenerManager(Dependency dependency, Lazy<TelephonyListenerManager> lazy) {
        dependency.mTelephonyListenerManager = lazy;
    }

    public static void injectMSystemStatusAnimationSchedulerLazy(Dependency dependency, Lazy<SystemStatusAnimationScheduler> lazy) {
        dependency.mSystemStatusAnimationSchedulerLazy = lazy;
    }

    public static void injectMPrivacyDotViewControllerLazy(Dependency dependency, Lazy<PrivacyDotViewController> lazy) {
        dependency.mPrivacyDotViewControllerLazy = lazy;
    }

    public static void injectMEdgeBackGestureHandlerFactoryLazy(Dependency dependency, Lazy<EdgeBackGestureHandler.Factory> lazy) {
        dependency.mEdgeBackGestureHandlerFactoryLazy = lazy;
    }

    public static void injectMUiEventLogger(Dependency dependency, Lazy<UiEventLogger> lazy) {
        dependency.mUiEventLogger = lazy;
    }

    public static void injectMFeatureFlagsLazy(Dependency dependency, Lazy<FeatureFlags> lazy) {
        dependency.mFeatureFlagsLazy = lazy;
    }

    public static void injectMContentInsetsProviderLazy(Dependency dependency, Lazy<StatusBarContentInsetsProvider> lazy) {
        dependency.mContentInsetsProviderLazy = lazy;
    }
}
