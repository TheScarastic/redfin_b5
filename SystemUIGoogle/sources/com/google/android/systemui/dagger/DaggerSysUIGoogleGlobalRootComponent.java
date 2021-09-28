package com.google.android.systemui.dagger;

import android.animation.AnimationHandler;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.AlarmManager;
import android.app.IActivityManager;
import android.app.IActivityTaskManager;
import android.app.INotificationManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.Service;
import android.app.StatsManager;
import android.app.WallpaperManager;
import android.app.admin.DevicePolicyManager;
import android.app.smartspace.SmartspaceManager;
import android.app.trust.TrustManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.om.OverlayManager;
import android.content.pm.IPackageManager;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutManager;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorPrivacyManager;
import android.hardware.display.AmbientDisplayConfiguration;
import android.hardware.display.ColorDisplayManager;
import android.hardware.display.DisplayManager;
import android.hardware.display.NightDisplayListener;
import android.hardware.face.FaceManager;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.usb.UsbManager;
import android.media.AudioManager;
import android.media.IAudioService;
import android.media.MediaRouter2Manager;
import android.media.session.MediaSessionManager;
import android.net.ConnectivityManager;
import android.net.NetworkScoreManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IThermalService;
import android.os.Looper;
import android.os.PowerManager;
import android.os.UserManager;
import android.os.Vibrator;
import android.permission.PermissionManager;
import android.service.dreams.IDreamManager;
import android.service.notification.StatusBarNotification;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Choreographer;
import android.view.CrossWindowBlurListeners;
import android.view.IWindowManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.app.AssistUtils;
import com.android.internal.app.IBatteryStats;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.util.LatencyTracker;
import com.android.internal.util.NotificationMessagingUtil;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.AdminSecondaryLockScreenController;
import com.android.keyguard.AdminSecondaryLockScreenController_Factory_Factory;
import com.android.keyguard.CarrierText;
import com.android.keyguard.CarrierTextController;
import com.android.keyguard.CarrierTextManager;
import com.android.keyguard.CarrierTextManager_Builder_Factory;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.EmergencyButtonController_Factory_Factory;
import com.android.keyguard.KeyguardClockSwitch;
import com.android.keyguard.KeyguardClockSwitchController;
import com.android.keyguard.KeyguardDisplayManager;
import com.android.keyguard.KeyguardDisplayManager_Factory;
import com.android.keyguard.KeyguardHostView;
import com.android.keyguard.KeyguardHostViewController;
import com.android.keyguard.KeyguardHostViewController_Factory;
import com.android.keyguard.KeyguardInputViewController;
import com.android.keyguard.KeyguardInputViewController_Factory_Factory;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardMessageAreaController_Factory_Factory;
import com.android.keyguard.KeyguardRootViewController;
import com.android.keyguard.KeyguardRootViewController_Factory;
import com.android.keyguard.KeyguardSecurityContainer;
import com.android.keyguard.KeyguardSecurityContainerController_Factory_Factory;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.keyguard.KeyguardSecurityModel_Factory;
import com.android.keyguard.KeyguardSecurityViewFlipper;
import com.android.keyguard.KeyguardSecurityViewFlipperController;
import com.android.keyguard.KeyguardSecurityViewFlipperController_Factory;
import com.android.keyguard.KeyguardSliceView;
import com.android.keyguard.KeyguardSliceViewController;
import com.android.keyguard.KeyguardSliceViewController_Factory;
import com.android.keyguard.KeyguardStatusView;
import com.android.keyguard.KeyguardStatusViewController;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitor_Factory;
import com.android.keyguard.LiftToActivateListener_Factory;
import com.android.keyguard.LockIconView;
import com.android.keyguard.LockIconViewController;
import com.android.keyguard.LockIconViewController_Factory;
import com.android.keyguard.ViewMediatorCallback;
import com.android.keyguard.clock.ClockManager;
import com.android.keyguard.clock.ClockManager_Factory;
import com.android.keyguard.clock.ClockModule_ProvideClockInfoListFactory;
import com.android.keyguard.clock.ClockOptionsProvider;
import com.android.keyguard.clock.ClockOptionsProvider_MembersInjector;
import com.android.keyguard.dagger.KeyguardBouncerComponent;
import com.android.keyguard.dagger.KeyguardBouncerModule_ProvidesKeyguardHostViewFactory;
import com.android.keyguard.dagger.KeyguardBouncerModule_ProvidesKeyguardSecurityContainerFactory;
import com.android.keyguard.dagger.KeyguardBouncerModule_ProvidesKeyguardSecurityViewFlipperFactory;
import com.android.keyguard.dagger.KeyguardBouncerModule_ProvidesRootViewFactory;
import com.android.keyguard.dagger.KeyguardQsUserSwitchComponent;
import com.android.keyguard.dagger.KeyguardStatusBarViewComponent;
import com.android.keyguard.dagger.KeyguardStatusBarViewModule_GetCarrierTextFactory;
import com.android.keyguard.dagger.KeyguardStatusViewComponent;
import com.android.keyguard.dagger.KeyguardStatusViewModule_GetKeyguardClockSwitchFactory;
import com.android.keyguard.dagger.KeyguardStatusViewModule_GetKeyguardSliceViewFactory;
import com.android.keyguard.dagger.KeyguardUserSwitcherComponent;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.users.EditUserInfoController;
import com.android.systemui.ActivityIntentHelper;
import com.android.systemui.ActivityIntentHelper_Factory;
import com.android.systemui.ActivityStarterDelegate;
import com.android.systemui.ActivityStarterDelegate_Factory;
import com.android.systemui.BootCompleteCacheImpl;
import com.android.systemui.BootCompleteCacheImpl_Factory;
import com.android.systemui.Dependency;
import com.android.systemui.Dependency_Factory;
import com.android.systemui.ForegroundServiceController;
import com.android.systemui.ForegroundServiceController_Factory;
import com.android.systemui.ForegroundServiceNotificationListener;
import com.android.systemui.ForegroundServiceNotificationListener_Factory;
import com.android.systemui.ForegroundServicesDialog;
import com.android.systemui.ForegroundServicesDialog_Factory;
import com.android.systemui.ImageWallpaper;
import com.android.systemui.ImageWallpaper_Factory;
import com.android.systemui.InitController;
import com.android.systemui.InitController_Factory;
import com.android.systemui.LatencyTester;
import com.android.systemui.LatencyTester_Factory;
import com.android.systemui.ScreenDecorations;
import com.android.systemui.ScreenDecorations_Factory;
import com.android.systemui.SliceBroadcastRelayHandler;
import com.android.systemui.SliceBroadcastRelayHandler_Factory;
import com.android.systemui.SystemUI;
import com.android.systemui.SystemUIAppComponentFactory;
import com.android.systemui.SystemUIAppComponentFactory_MembersInjector;
import com.android.systemui.SystemUIService;
import com.android.systemui.SystemUIService_Factory;
import com.android.systemui.UiOffloadThread;
import com.android.systemui.UiOffloadThread_Factory;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver_Factory;
import com.android.systemui.accessibility.AccessibilityButtonTargetsObserver;
import com.android.systemui.accessibility.AccessibilityButtonTargetsObserver_Factory;
import com.android.systemui.accessibility.ModeSwitchesController;
import com.android.systemui.accessibility.SystemActions;
import com.android.systemui.accessibility.SystemActions_Factory;
import com.android.systemui.accessibility.WindowMagnification;
import com.android.systemui.accessibility.WindowMagnification_Factory;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuController;
import com.android.systemui.appops.AppOpsControllerImpl;
import com.android.systemui.appops.AppOpsControllerImpl_Factory;
import com.android.systemui.assist.AssistModule_ProvideAssistUtilsFactory;
import com.android.systemui.assist.PhoneStateMonitor;
import com.android.systemui.assist.PhoneStateMonitor_Factory;
import com.android.systemui.assist.ui.DefaultUiController;
import com.android.systemui.assist.ui.DefaultUiController_Factory;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.AuthController_Factory;
import com.android.systemui.biometrics.AuthRippleController;
import com.android.systemui.biometrics.AuthRippleController_Factory;
import com.android.systemui.biometrics.AuthRippleView;
import com.android.systemui.biometrics.SidefpsController;
import com.android.systemui.biometrics.SidefpsController_Factory;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.biometrics.UdfpsController_Factory;
import com.android.systemui.biometrics.UdfpsHapticsSimulator;
import com.android.systemui.biometrics.UdfpsHapticsSimulator_Factory;
import com.android.systemui.biometrics.UdfpsHbmProvider;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger_Factory;
import com.android.systemui.classifier.BrightLineFalsingManager;
import com.android.systemui.classifier.BrightLineFalsingManager_Factory;
import com.android.systemui.classifier.DiagonalClassifier_Factory;
import com.android.systemui.classifier.DistanceClassifier_Factory;
import com.android.systemui.classifier.DoubleTapClassifier;
import com.android.systemui.classifier.DoubleTapClassifier_Factory;
import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.classifier.FalsingCollectorImpl_Factory;
import com.android.systemui.classifier.FalsingDataProvider;
import com.android.systemui.classifier.FalsingDataProvider_Factory;
import com.android.systemui.classifier.FalsingManagerProxy;
import com.android.systemui.classifier.FalsingManagerProxy_Factory;
import com.android.systemui.classifier.FalsingModule_ProvidesBrightLineGestureClassifiersFactory;
import com.android.systemui.classifier.FalsingModule_ProvidesDoubleTapTimeoutMsFactory;
import com.android.systemui.classifier.FalsingModule_ProvidesDoubleTapTouchSlopFactory;
import com.android.systemui.classifier.FalsingModule_ProvidesSingleTapTouchSlopFactory;
import com.android.systemui.classifier.HistoryTracker;
import com.android.systemui.classifier.HistoryTracker_Factory;
import com.android.systemui.classifier.PointerCountClassifier_Factory;
import com.android.systemui.classifier.ProximityClassifier_Factory;
import com.android.systemui.classifier.SingleTapClassifier;
import com.android.systemui.classifier.SingleTapClassifier_Factory;
import com.android.systemui.classifier.TypeClassifier;
import com.android.systemui.classifier.TypeClassifier_Factory;
import com.android.systemui.classifier.ZigZagClassifier_Factory;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.colorextraction.SysuiColorExtractor_Factory;
import com.android.systemui.controls.ControlsMetricsLoggerImpl;
import com.android.systemui.controls.ControlsMetricsLoggerImpl_Factory;
import com.android.systemui.controls.CustomIconCache;
import com.android.systemui.controls.CustomIconCache_Factory;
import com.android.systemui.controls.controller.ControlsBindingControllerImpl;
import com.android.systemui.controls.controller.ControlsBindingControllerImpl_Factory;
import com.android.systemui.controls.controller.ControlsControllerImpl;
import com.android.systemui.controls.controller.ControlsControllerImpl_Factory;
import com.android.systemui.controls.controller.ControlsFavoritePersistenceWrapper;
import com.android.systemui.controls.dagger.ControlsComponent;
import com.android.systemui.controls.dagger.ControlsComponent_Factory;
import com.android.systemui.controls.dagger.ControlsModule_ProvidesControlsFeatureEnabledFactory;
import com.android.systemui.controls.management.ControlsEditingActivity;
import com.android.systemui.controls.management.ControlsEditingActivity_Factory;
import com.android.systemui.controls.management.ControlsFavoritingActivity;
import com.android.systemui.controls.management.ControlsFavoritingActivity_Factory;
import com.android.systemui.controls.management.ControlsListingControllerImpl;
import com.android.systemui.controls.management.ControlsListingControllerImpl_Factory;
import com.android.systemui.controls.management.ControlsProviderSelectorActivity;
import com.android.systemui.controls.management.ControlsProviderSelectorActivity_Factory;
import com.android.systemui.controls.management.ControlsRequestDialog;
import com.android.systemui.controls.management.ControlsRequestDialog_Factory;
import com.android.systemui.controls.ui.ControlActionCoordinatorImpl;
import com.android.systemui.controls.ui.ControlActionCoordinatorImpl_Factory;
import com.android.systemui.controls.ui.ControlsActivity;
import com.android.systemui.controls.ui.ControlsActivity_Factory;
import com.android.systemui.controls.ui.ControlsUiControllerImpl;
import com.android.systemui.controls.ui.ControlsUiControllerImpl_Factory;
import com.android.systemui.dagger.ContextComponentHelper;
import com.android.systemui.dagger.ContextComponentResolver;
import com.android.systemui.dagger.ContextComponentResolver_Factory;
import com.android.systemui.dagger.DependencyProvider;
import com.android.systemui.dagger.DependencyProvider_ProvideAccessibilityFloatingMenuControllerFactory;
import com.android.systemui.dagger.DependencyProvider_ProvideActivityManagerWrapperFactory;
import com.android.systemui.dagger.DependencyProvider_ProvideAlwaysOnDisplayPolicyFactory;
import com.android.systemui.dagger.DependencyProvider_ProvideAmbientDisplayConfigurationFactory;
import com.android.systemui.dagger.DependencyProvider_ProvideAutoHideControllerFactory;
import com.android.systemui.dagger.DependencyProvider_ProvideConfigurationControllerFactory;
import com.android.systemui.dagger.DependencyProvider_ProvideDataSaverControllerFactory;
import com.android.systemui.dagger.DependencyProvider_ProvideDevicePolicyManagerWrapperFactory;
import com.android.systemui.dagger.DependencyProvider_ProvideHandlerFactory;
import com.android.systemui.dagger.DependencyProvider_ProvideINotificationManagerFactory;
import com.android.systemui.dagger.DependencyProvider_ProvideLeakDetectorFactory;
import com.android.systemui.dagger.DependencyProvider_ProvideLocalBluetoothControllerFactory;
import com.android.systemui.dagger.DependencyProvider_ProvideLockPatternUtilsFactory;
import com.android.systemui.dagger.DependencyProvider_ProvideMetricsLoggerFactory;
import com.android.systemui.dagger.DependencyProvider_ProvideNavigationBarControllerFactory;
import com.android.systemui.dagger.DependencyProvider_ProvideNotificationMessagingUtilFactory;
import com.android.systemui.dagger.DependencyProvider_ProvidePluginManagerFactory;
import com.android.systemui.dagger.DependencyProvider_ProvideReduceBrightColorsListenerFactory;
import com.android.systemui.dagger.DependencyProvider_ProvideSharePreferencesFactory;
import com.android.systemui.dagger.DependencyProvider_ProvideThemeOverlayManagerFactory;
import com.android.systemui.dagger.DependencyProvider_ProvideTimeTickHandlerFactory;
import com.android.systemui.dagger.DependencyProvider_ProviderLayoutInflaterFactory;
import com.android.systemui.dagger.DependencyProvider_ProvidesBroadcastDispatcherFactory;
import com.android.systemui.dagger.DependencyProvider_ProvidesChoreographerFactory;
import com.android.systemui.dagger.DependencyProvider_ProvidesModeSwitchesControllerFactory;
import com.android.systemui.dagger.DependencyProvider_ProvidesViewMediatorCallbackFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideAccessibilityManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideActivityManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideActivityTaskManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideAlarmManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideAudioManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideColorDisplayManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideConnectivityManagagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideContentResolverFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideCrossWindowBlurListenersFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideDevicePolicyManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideDisplayIdFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideDisplayManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideFaceManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIActivityManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIActivityTaskManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIAudioServiceFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIBatteryStatsFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIDreamManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIPackageManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIStatusBarServiceFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIWallPaperManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIWindowManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideInputMethodManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideKeyguardManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideLatencyTrackerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideLauncherAppsFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideMediaRouter2ManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideMediaSessionManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideNetworkScoreManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideNotificationManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideOptionalVibratorFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideOverlayManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvidePackageManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvidePackageManagerWrapperFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvidePermissionManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvidePowerManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideResourcesFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideSensorPrivacyManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideShortcutManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideSmartspaceManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideStatsManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideSubcriptionManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideTelecomManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideTelephonyManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideTrustManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideUserManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideVibratorFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideViewConfigurationFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideWallpaperManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideWifiManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideWindowManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvidesFingerprintManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvidesSensorManagerFactory;
import com.android.systemui.dagger.GlobalModule;
import com.android.systemui.dagger.GlobalModule_ProvideDisplayMetricsFactory;
import com.android.systemui.dagger.GlobalModule_ProvideIsTestHarnessFactory;
import com.android.systemui.dagger.GlobalModule_ProvideUiEventLoggerFactory;
import com.android.systemui.dagger.NightDisplayListenerModule;
import com.android.systemui.dagger.NightDisplayListenerModule_Builder_Factory;
import com.android.systemui.dagger.NightDisplayListenerModule_ProvideNightDisplayListenerFactory;
import com.android.systemui.dagger.SystemUIModule_ProvideBubblesManagerFactory;
import com.android.systemui.dagger.SystemUIModule_ProvideSmartspaceTransitionControllerFactory;
import com.android.systemui.dagger.SystemUIModule_ProvideSysUiStateFactory;
import com.android.systemui.dagger.WMComponent;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.demomode.dagger.DemoModeModule_ProvideDemoModeControllerFactory;
import com.android.systemui.dock.DockManager;
import com.android.systemui.doze.AlwaysOnDisplayPolicy;
import com.android.systemui.doze.DozeAuthRemover;
import com.android.systemui.doze.DozeAuthRemover_Factory;
import com.android.systemui.doze.DozeDockHandler;
import com.android.systemui.doze.DozeDockHandler_Factory;
import com.android.systemui.doze.DozeFalsingManagerAdapter;
import com.android.systemui.doze.DozeFalsingManagerAdapter_Factory;
import com.android.systemui.doze.DozeLog;
import com.android.systemui.doze.DozeLog_Factory;
import com.android.systemui.doze.DozeLogger;
import com.android.systemui.doze.DozeLogger_Factory;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.doze.DozeMachine_Factory;
import com.android.systemui.doze.DozePauser;
import com.android.systemui.doze.DozePauser_Factory;
import com.android.systemui.doze.DozeScreenBrightness;
import com.android.systemui.doze.DozeScreenBrightness_Factory;
import com.android.systemui.doze.DozeScreenState;
import com.android.systemui.doze.DozeScreenState_Factory;
import com.android.systemui.doze.DozeService;
import com.android.systemui.doze.DozeService_Factory;
import com.android.systemui.doze.DozeTriggers;
import com.android.systemui.doze.DozeTriggers_Factory;
import com.android.systemui.doze.DozeUi;
import com.android.systemui.doze.DozeUi_Factory;
import com.android.systemui.doze.DozeWallpaperState;
import com.android.systemui.doze.DozeWallpaperState_Factory;
import com.android.systemui.doze.dagger.DozeComponent;
import com.android.systemui.doze.dagger.DozeModule_ProvidesBrightnessSensorFactory;
import com.android.systemui.doze.dagger.DozeModule_ProvidesDozeMachinePartesFactory;
import com.android.systemui.doze.dagger.DozeModule_ProvidesDozeWakeLockFactory;
import com.android.systemui.doze.dagger.DozeModule_ProvidesWrappedServiceFactory;
import com.android.systemui.dump.DumpHandler;
import com.android.systemui.dump.DumpHandler_Factory;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.dump.DumpManager_Factory;
import com.android.systemui.dump.LogBufferEulogizer;
import com.android.systemui.dump.LogBufferEulogizer_Factory;
import com.android.systemui.dump.LogBufferFreezer;
import com.android.systemui.dump.LogBufferFreezer_Factory;
import com.android.systemui.dump.SystemUIAuxiliaryDumpService;
import com.android.systemui.dump.SystemUIAuxiliaryDumpService_Factory;
import com.android.systemui.flags.FeatureFlagReader;
import com.android.systemui.flags.FeatureFlagReader_Factory;
import com.android.systemui.flags.SystemPropertiesHelper;
import com.android.systemui.flags.SystemPropertiesHelper_Factory;
import com.android.systemui.fragments.FragmentService;
import com.android.systemui.fragments.FragmentService_Factory;
import com.android.systemui.globalactions.GlobalActionsComponent;
import com.android.systemui.globalactions.GlobalActionsComponent_Factory;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import com.android.systemui.globalactions.GlobalActionsDialogLite_Factory;
import com.android.systemui.globalactions.GlobalActionsImpl;
import com.android.systemui.globalactions.GlobalActionsImpl_Factory;
import com.android.systemui.globalactions.GlobalActionsInfoProvider;
import com.android.systemui.globalactions.GlobalActionsInfoProvider_Factory;
import com.android.systemui.keyguard.DismissCallbackRegistry;
import com.android.systemui.keyguard.DismissCallbackRegistry_Factory;
import com.android.systemui.keyguard.FaceAuthScreenBrightnessController;
import com.android.systemui.keyguard.KeyguardLifecyclesDispatcher;
import com.android.systemui.keyguard.KeyguardLifecyclesDispatcher_Factory;
import com.android.systemui.keyguard.KeyguardService;
import com.android.systemui.keyguard.KeyguardService_Factory;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.keyguard.KeyguardSliceProvider_MembersInjector;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController_Factory;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.keyguard.ScreenLifecycle_Factory;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.keyguard.WakefulnessLifecycle_Factory;
import com.android.systemui.keyguard.WorkLockActivity;
import com.android.systemui.keyguard.WorkLockActivity_Factory;
import com.android.systemui.keyguard.dagger.KeyguardModule_NewKeyguardViewMediatorFactory;
import com.android.systemui.keyguard.dagger.KeyguardModule_ProvideFaceAuthScreenBrightnessControllerFactory;
import com.android.systemui.keyguard.dagger.KeyguardModule_ProvideKeyguardLiftControllerFactory;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import com.android.systemui.log.LogBufferFactory_Factory;
import com.android.systemui.log.LogcatEchoTracker;
import com.android.systemui.log.dagger.LogModule_ProvideBroadcastDispatcherLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideDozeLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideLogcatEchoTrackerFactory;
import com.android.systemui.log.dagger.LogModule_ProvideNotifInteractionLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideNotificationSectionLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideNotificationsLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvidePrivacyLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideQuickSettingsLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideToastLogBufferFactory;
import com.android.systemui.media.KeyguardMediaController;
import com.android.systemui.media.KeyguardMediaController_Factory;
import com.android.systemui.media.LocalMediaManagerFactory;
import com.android.systemui.media.LocalMediaManagerFactory_Factory;
import com.android.systemui.media.MediaBrowserFactory;
import com.android.systemui.media.MediaBrowserFactory_Factory;
import com.android.systemui.media.MediaCarouselController;
import com.android.systemui.media.MediaCarouselController_Factory;
import com.android.systemui.media.MediaControlPanel;
import com.android.systemui.media.MediaControlPanel_Factory;
import com.android.systemui.media.MediaControllerFactory;
import com.android.systemui.media.MediaControllerFactory_Factory;
import com.android.systemui.media.MediaDataCombineLatest_Factory;
import com.android.systemui.media.MediaDataFilter;
import com.android.systemui.media.MediaDataFilter_Factory;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaDataManager_Factory;
import com.android.systemui.media.MediaDeviceManager;
import com.android.systemui.media.MediaDeviceManager_Factory;
import com.android.systemui.media.MediaFeatureFlag;
import com.android.systemui.media.MediaFeatureFlag_Factory;
import com.android.systemui.media.MediaHierarchyManager;
import com.android.systemui.media.MediaHierarchyManager_Factory;
import com.android.systemui.media.MediaHost;
import com.android.systemui.media.MediaHostStatesManager;
import com.android.systemui.media.MediaHostStatesManager_Factory;
import com.android.systemui.media.MediaHost_MediaHostStateHolder_Factory;
import com.android.systemui.media.MediaResumeListener;
import com.android.systemui.media.MediaResumeListener_Factory;
import com.android.systemui.media.MediaSessionBasedFilter;
import com.android.systemui.media.MediaSessionBasedFilter_Factory;
import com.android.systemui.media.MediaTimeoutListener;
import com.android.systemui.media.MediaTimeoutListener_Factory;
import com.android.systemui.media.MediaViewController;
import com.android.systemui.media.MediaViewController_Factory;
import com.android.systemui.media.ResumeMediaBrowserFactory;
import com.android.systemui.media.ResumeMediaBrowserFactory_Factory;
import com.android.systemui.media.SeekBarViewModel;
import com.android.systemui.media.SeekBarViewModel_Factory;
import com.android.systemui.media.SmartspaceMediaDataProvider_Factory;
import com.android.systemui.media.dagger.MediaModule_ProvidesKeyguardMediaHostFactory;
import com.android.systemui.media.dagger.MediaModule_ProvidesQSMediaHostFactory;
import com.android.systemui.media.dagger.MediaModule_ProvidesQuickQSMediaHostFactory;
import com.android.systemui.media.dialog.MediaOutputDialogFactory;
import com.android.systemui.media.dialog.MediaOutputDialogFactory_Factory;
import com.android.systemui.media.dialog.MediaOutputDialogReceiver;
import com.android.systemui.media.dialog.MediaOutputDialogReceiver_Factory;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.navigationbar.NavigationModeController_Factory;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler_Factory_Factory;
import com.android.systemui.people.PeopleProvider;
import com.android.systemui.people.PeopleProvider_MembersInjector;
import com.android.systemui.people.PeopleSpaceActivity;
import com.android.systemui.people.PeopleSpaceActivity_Factory;
import com.android.systemui.people.widget.LaunchConversationActivity;
import com.android.systemui.people.widget.LaunchConversationActivity_Factory;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager_Factory;
import com.android.systemui.people.widget.PeopleSpaceWidgetPinnedReceiver;
import com.android.systemui.people.widget.PeopleSpaceWidgetPinnedReceiver_Factory;
import com.android.systemui.people.widget.PeopleSpaceWidgetProvider;
import com.android.systemui.people.widget.PeopleSpaceWidgetProvider_Factory;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.PluginDependencyProvider;
import com.android.systemui.plugins.PluginDependencyProvider_Factory;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.power.PowerUI;
import com.android.systemui.power.PowerUI_Factory;
import com.android.systemui.privacy.PrivacyDialogController;
import com.android.systemui.privacy.PrivacyDialogController_Factory;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.PrivacyItemController_Factory;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.privacy.logging.PrivacyLogger_Factory;
import com.android.systemui.qs.AutoAddTracker;
import com.android.systemui.qs.AutoAddTracker_Builder_Factory;
import com.android.systemui.qs.QSAnimator;
import com.android.systemui.qs.QSAnimator_Factory;
import com.android.systemui.qs.QSContainerImpl;
import com.android.systemui.qs.QSContainerImplController;
import com.android.systemui.qs.QSContainerImplController_Factory;
import com.android.systemui.qs.QSDetailDisplayer;
import com.android.systemui.qs.QSDetailDisplayer_Factory;
import com.android.systemui.qs.QSExpansionPathInterpolator;
import com.android.systemui.qs.QSExpansionPathInterpolator_Factory;
import com.android.systemui.qs.QSFooter;
import com.android.systemui.qs.QSFooterView;
import com.android.systemui.qs.QSFooterViewController;
import com.android.systemui.qs.QSFooterViewController_Factory;
import com.android.systemui.qs.QSFragment;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.QSPanelController;
import com.android.systemui.qs.QSPanelController_Factory;
import com.android.systemui.qs.QSSecurityFooter_Factory;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.qs.QSTileHost_Factory;
import com.android.systemui.qs.QSTileRevealController_Factory_Factory;
import com.android.systemui.qs.QuickQSPanel;
import com.android.systemui.qs.QuickQSPanelController;
import com.android.systemui.qs.QuickQSPanelController_Factory;
import com.android.systemui.qs.QuickStatusBarHeader;
import com.android.systemui.qs.QuickStatusBarHeaderController_Factory;
import com.android.systemui.qs.ReduceBrightColorsController;
import com.android.systemui.qs.carrier.QSCarrierGroupController;
import com.android.systemui.qs.carrier.QSCarrierGroupController_Builder_Factory;
import com.android.systemui.qs.carrier.QSCarrierGroupController_SubscriptionManagerSlotIndexResolver_Factory;
import com.android.systemui.qs.customize.QSCustomizer;
import com.android.systemui.qs.customize.QSCustomizerController;
import com.android.systemui.qs.customize.QSCustomizerController_Factory;
import com.android.systemui.qs.customize.TileAdapter;
import com.android.systemui.qs.customize.TileAdapter_Factory;
import com.android.systemui.qs.customize.TileQueryHelper;
import com.android.systemui.qs.customize.TileQueryHelper_Factory;
import com.android.systemui.qs.dagger.QSFlagsModule_IsPMLiteEnabledFactory;
import com.android.systemui.qs.dagger.QSFlagsModule_IsReduceBrightColorsAvailableFactory;
import com.android.systemui.qs.dagger.QSFragmentComponent;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvideQSPanelFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvideRootViewFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvideThemedContextFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvideThemedLayoutInflaterFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesMultiUserSWitchFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQSContainerImplFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQSCutomizerFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQSFooterFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQSFooterViewFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQSSecurityFooterViewFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQSUsingMediaPlayerFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQuickQSPanelFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQuickStatusBarHeaderFactory;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.qs.external.CustomTileStatePersister;
import com.android.systemui.qs.external.CustomTileStatePersister_Factory;
import com.android.systemui.qs.external.CustomTile_Builder_Factory;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.logging.QSLogger_Factory;
import com.android.systemui.qs.tiles.AirplaneModeTile;
import com.android.systemui.qs.tiles.AirplaneModeTile_Factory;
import com.android.systemui.qs.tiles.AlarmTile;
import com.android.systemui.qs.tiles.AlarmTile_Factory;
import com.android.systemui.qs.tiles.BluetoothTile;
import com.android.systemui.qs.tiles.BluetoothTile_Factory;
import com.android.systemui.qs.tiles.CameraToggleTile;
import com.android.systemui.qs.tiles.CameraToggleTile_Factory;
import com.android.systemui.qs.tiles.CastTile;
import com.android.systemui.qs.tiles.CastTile_Factory;
import com.android.systemui.qs.tiles.CellularTile;
import com.android.systemui.qs.tiles.CellularTile_Factory;
import com.android.systemui.qs.tiles.ColorInversionTile;
import com.android.systemui.qs.tiles.ColorInversionTile_Factory;
import com.android.systemui.qs.tiles.DataSaverTile;
import com.android.systemui.qs.tiles.DataSaverTile_Factory;
import com.android.systemui.qs.tiles.DeviceControlsTile;
import com.android.systemui.qs.tiles.DeviceControlsTile_Factory;
import com.android.systemui.qs.tiles.DndTile;
import com.android.systemui.qs.tiles.DndTile_Factory;
import com.android.systemui.qs.tiles.FlashlightTile;
import com.android.systemui.qs.tiles.FlashlightTile_Factory;
import com.android.systemui.qs.tiles.HotspotTile;
import com.android.systemui.qs.tiles.HotspotTile_Factory;
import com.android.systemui.qs.tiles.InternetTile;
import com.android.systemui.qs.tiles.InternetTile_Factory;
import com.android.systemui.qs.tiles.LocationTile;
import com.android.systemui.qs.tiles.LocationTile_Factory;
import com.android.systemui.qs.tiles.MicrophoneToggleTile;
import com.android.systemui.qs.tiles.MicrophoneToggleTile_Factory;
import com.android.systemui.qs.tiles.NfcTile;
import com.android.systemui.qs.tiles.NfcTile_Factory;
import com.android.systemui.qs.tiles.NightDisplayTile;
import com.android.systemui.qs.tiles.NightDisplayTile_Factory;
import com.android.systemui.qs.tiles.QuickAccessWalletTile;
import com.android.systemui.qs.tiles.QuickAccessWalletTile_Factory;
import com.android.systemui.qs.tiles.ReduceBrightColorsTile;
import com.android.systemui.qs.tiles.ReduceBrightColorsTile_Factory;
import com.android.systemui.qs.tiles.RotationLockTile;
import com.android.systemui.qs.tiles.RotationLockTile_Factory;
import com.android.systemui.qs.tiles.ScreenRecordTile;
import com.android.systemui.qs.tiles.ScreenRecordTile_Factory;
import com.android.systemui.qs.tiles.UiModeNightTile;
import com.android.systemui.qs.tiles.UiModeNightTile_Factory;
import com.android.systemui.qs.tiles.UserDetailView;
import com.android.systemui.qs.tiles.UserDetailView_Adapter_Factory;
import com.android.systemui.qs.tiles.UserTile;
import com.android.systemui.qs.tiles.UserTile_Factory;
import com.android.systemui.qs.tiles.WifiTile;
import com.android.systemui.qs.tiles.WifiTile_Factory;
import com.android.systemui.qs.tiles.WorkModeTile;
import com.android.systemui.qs.tiles.WorkModeTile_Factory;
import com.android.systemui.recents.OverviewProxyRecentsImpl;
import com.android.systemui.recents.OverviewProxyRecentsImpl_Factory;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.recents.OverviewProxyService_Factory;
import com.android.systemui.recents.Recents;
import com.android.systemui.recents.RecentsImplementation;
import com.android.systemui.recents.RecentsModule_ProvideRecentsImplFactory;
import com.android.systemui.recents.ScreenPinningRequest;
import com.android.systemui.recents.ScreenPinningRequest_Factory;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.screenrecord.RecordingController_Factory;
import com.android.systemui.screenrecord.RecordingService;
import com.android.systemui.screenrecord.RecordingService_Factory;
import com.android.systemui.screenrecord.ScreenRecordDialog;
import com.android.systemui.screenrecord.ScreenRecordDialog_Factory;
import com.android.systemui.screenshot.ActionProxyReceiver;
import com.android.systemui.screenshot.ActionProxyReceiver_Factory;
import com.android.systemui.screenshot.DeleteScreenshotReceiver;
import com.android.systemui.screenshot.DeleteScreenshotReceiver_Factory;
import com.android.systemui.screenshot.ImageExporter_Factory;
import com.android.systemui.screenshot.ImageTileSet_Factory;
import com.android.systemui.screenshot.LongScreenshotActivity;
import com.android.systemui.screenshot.LongScreenshotActivity_Factory;
import com.android.systemui.screenshot.LongScreenshotData;
import com.android.systemui.screenshot.LongScreenshotData_Factory;
import com.android.systemui.screenshot.ScreenshotController;
import com.android.systemui.screenshot.ScreenshotController_Factory;
import com.android.systemui.screenshot.ScreenshotNotificationsController;
import com.android.systemui.screenshot.ScreenshotNotificationsController_Factory;
import com.android.systemui.screenshot.ScreenshotSmartActions;
import com.android.systemui.screenshot.ScreenshotSmartActions_Factory;
import com.android.systemui.screenshot.ScrollCaptureClient;
import com.android.systemui.screenshot.ScrollCaptureClient_Factory;
import com.android.systemui.screenshot.ScrollCaptureController;
import com.android.systemui.screenshot.ScrollCaptureController_Factory;
import com.android.systemui.screenshot.SmartActionsReceiver;
import com.android.systemui.screenshot.SmartActionsReceiver_Factory;
import com.android.systemui.screenshot.TakeScreenshotService;
import com.android.systemui.screenshot.TakeScreenshotService_Factory;
import com.android.systemui.sensorprivacy.SensorUseStartedActivity;
import com.android.systemui.sensorprivacy.SensorUseStartedActivity_Factory;
import com.android.systemui.sensorprivacy.television.TvUnblockSensorActivity;
import com.android.systemui.sensorprivacy.television.TvUnblockSensorActivity_Factory;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.settings.brightness.BrightnessController;
import com.android.systemui.settings.brightness.BrightnessController_Factory_Factory;
import com.android.systemui.settings.brightness.BrightnessDialog;
import com.android.systemui.settings.brightness.BrightnessDialog_Factory;
import com.android.systemui.settings.brightness.BrightnessSlider;
import com.android.systemui.settings.brightness.BrightnessSlider_Factory_Factory;
import com.android.systemui.settings.dagger.SettingsModule_ProvideUserTrackerFactory;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.DevicePolicyManagerWrapper;
import com.android.systemui.shared.system.PackageManagerWrapper;
import com.android.systemui.shared.system.smartspace.SmartspaceTransitionController;
import com.android.systemui.shortcut.ShortcutKeyDispatcher;
import com.android.systemui.shortcut.ShortcutKeyDispatcher_Factory;
import com.android.systemui.statusbar.ActionClickLogger;
import com.android.systemui.statusbar.ActionClickLogger_Factory;
import com.android.systemui.statusbar.BlurUtils;
import com.android.systemui.statusbar.BlurUtils_Factory;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.FeatureFlags_Factory;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController_Factory;
import com.android.systemui.statusbar.MediaArtworkProcessor;
import com.android.systemui.statusbar.MediaArtworkProcessor_Factory;
import com.android.systemui.statusbar.NotificationClickNotifier;
import com.android.systemui.statusbar.NotificationClickNotifier_Factory;
import com.android.systemui.statusbar.NotificationInteractionTracker;
import com.android.systemui.statusbar.NotificationInteractionTracker_Factory;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.NotificationShadeDepthController_Factory;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.NotificationShelfController;
import com.android.systemui.statusbar.NotificationShelfController_Factory;
import com.android.systemui.statusbar.NotificationViewHierarchyManager;
import com.android.systemui.statusbar.PulseExpansionHandler;
import com.android.systemui.statusbar.PulseExpansionHandler_Factory;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.StatusBarStateControllerImpl;
import com.android.systemui.statusbar.StatusBarStateControllerImpl_Factory;
import com.android.systemui.statusbar.SuperStatusBarViewFactory;
import com.android.systemui.statusbar.SuperStatusBarViewFactory_Factory;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.VibratorHelper_Factory;
import com.android.systemui.statusbar.charging.WiredChargingRippleController;
import com.android.systemui.statusbar.charging.WiredChargingRippleController_Factory;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.commandline.CommandRegistry_Factory;
import com.android.systemui.statusbar.dagger.StatusBarDependenciesModule_ProvideCommandQueueFactory;
import com.android.systemui.statusbar.dagger.StatusBarDependenciesModule_ProvideNotificationListenerFactory;
import com.android.systemui.statusbar.dagger.StatusBarDependenciesModule_ProvideNotificationMediaManagerFactory;
import com.android.systemui.statusbar.dagger.StatusBarDependenciesModule_ProvideNotificationRemoteInputManagerFactory;
import com.android.systemui.statusbar.dagger.StatusBarDependenciesModule_ProvideNotificationViewHierarchyManagerFactory;
import com.android.systemui.statusbar.dagger.StatusBarDependenciesModule_ProvideOngoingCallControllerFactory;
import com.android.systemui.statusbar.dagger.StatusBarDependenciesModule_ProvideSmartReplyControllerFactory;
import com.android.systemui.statusbar.events.PrivacyDotViewController;
import com.android.systemui.statusbar.events.PrivacyDotViewController_Factory;
import com.android.systemui.statusbar.events.SystemEventChipAnimationController;
import com.android.systemui.statusbar.events.SystemEventChipAnimationController_Factory;
import com.android.systemui.statusbar.events.SystemEventCoordinator;
import com.android.systemui.statusbar.events.SystemEventCoordinator_Factory;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler_Factory;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController_Factory;
import com.android.systemui.statusbar.notification.AnimatedImageNotificationManager;
import com.android.systemui.statusbar.notification.AnimatedImageNotificationManager_Factory;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.AssistantFeedbackController_Factory;
import com.android.systemui.statusbar.notification.ConversationNotificationManager;
import com.android.systemui.statusbar.notification.ConversationNotificationManager_Factory;
import com.android.systemui.statusbar.notification.ConversationNotificationProcessor;
import com.android.systemui.statusbar.notification.ConversationNotificationProcessor_Factory;
import com.android.systemui.statusbar.notification.DynamicChildBindController;
import com.android.systemui.statusbar.notification.DynamicChildBindController_Factory;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.DynamicPrivacyController_Factory;
import com.android.systemui.statusbar.notification.ForegroundServiceDismissalFeatureController;
import com.android.systemui.statusbar.notification.ForegroundServiceDismissalFeatureController_Factory;
import com.android.systemui.statusbar.notification.InstantAppNotifier;
import com.android.systemui.statusbar.notification.InstantAppNotifier_Factory;
import com.android.systemui.statusbar.notification.NotificationClicker;
import com.android.systemui.statusbar.notification.NotificationClickerLogger;
import com.android.systemui.statusbar.notification.NotificationClickerLogger_Factory;
import com.android.systemui.statusbar.notification.NotificationClicker_Builder_Factory;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationEntryManagerLogger;
import com.android.systemui.statusbar.notification.NotificationEntryManagerLogger_Factory;
import com.android.systemui.statusbar.notification.NotificationFilter;
import com.android.systemui.statusbar.notification.NotificationFilter_Factory;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager_Factory;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifCollection_Factory;
import com.android.systemui.statusbar.notification.collection.NotifInflaterImpl;
import com.android.systemui.statusbar.notification.collection.NotifInflaterImpl_Factory;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotifPipeline_Factory;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.NotificationRankingManager;
import com.android.systemui.statusbar.notification.collection.NotificationRankingManager_Factory;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder_Factory;
import com.android.systemui.statusbar.notification.collection.TargetSdkResolver;
import com.android.systemui.statusbar.notification.collection.TargetSdkResolver_Factory;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescer;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescerLogger;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescerLogger_Factory;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescer_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.AppOpsCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.AppOpsCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.BubbleCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.BubbleCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.ConversationCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.ConversationCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.DeviceProvisionedCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.DeviceProvisionedCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.HideNotifsForOtherUsersCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.HideNotifsForOtherUsersCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.KeyguardCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.KeyguardCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.MediaCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.MediaCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.NotifCoordinators;
import com.android.systemui.statusbar.notification.collection.coordinator.NotifCoordinators_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinatorLogger;
import com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinatorLogger_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.SharedCoordinatorLogger;
import com.android.systemui.statusbar.notification.collection.coordinator.SharedCoordinatorLogger_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.inflation.LowPriorityInflationHelper;
import com.android.systemui.statusbar.notification.collection.inflation.LowPriorityInflationHelper_Factory;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl_Factory;
import com.android.systemui.statusbar.notification.collection.init.NotifPipelineInitializer;
import com.android.systemui.statusbar.notification.collection.init.NotifPipelineInitializer_Factory;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy_Factory;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.listbuilder.ShadeListBuilderLogger;
import com.android.systemui.statusbar.notification.collection.listbuilder.ShadeListBuilderLogger_Factory;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionLogger;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionLogger_Factory;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider_Factory;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.collection.render.NotifViewBarn;
import com.android.systemui.statusbar.notification.collection.render.NotifViewBarn_Factory;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderNodeControllerImpl;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderNodeControllerImpl_Factory;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewDifferLogger;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewDifferLogger_Factory;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewManagerFactory;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewManagerFactory_Factory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesAlertingHeaderControllerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesAlertingHeaderNodeControllerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesAlertingHeaderSubcomponentFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesIncomingHeaderControllerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesIncomingHeaderNodeControllerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesIncomingHeaderSubcomponentFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesPeopleHeaderControllerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesPeopleHeaderNodeControllerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesPeopleHeaderSubcomponentFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesSilentHeaderControllerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesSilentHeaderNodeControllerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesSilentHeaderSubcomponentFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideCommonNotifCollectionFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideGroupExpansionManagerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideGroupMembershipManagerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationEntryManagerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationGutsManagerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationLoggerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationPanelLoggerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationsControllerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideOnUserInteractionCallbackFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideVisualStabilityManagerFactory;
import com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent;
import com.android.systemui.statusbar.notification.icon.IconBuilder;
import com.android.systemui.statusbar.notification.icon.IconBuilder_Factory;
import com.android.systemui.statusbar.notification.icon.IconManager;
import com.android.systemui.statusbar.notification.icon.IconManager_Factory;
import com.android.systemui.statusbar.notification.init.NotificationsController;
import com.android.systemui.statusbar.notification.init.NotificationsControllerImpl;
import com.android.systemui.statusbar.notification.init.NotificationsControllerImpl_Factory;
import com.android.systemui.statusbar.notification.init.NotificationsControllerStub;
import com.android.systemui.statusbar.notification.init.NotificationsControllerStub_Factory;
import com.android.systemui.statusbar.notification.interruption.BypassHeadsUpNotifier;
import com.android.systemui.statusbar.notification.interruption.BypassHeadsUpNotifier_Factory;
import com.android.systemui.statusbar.notification.interruption.HeadsUpController;
import com.android.systemui.statusbar.notification.interruption.HeadsUpController_Factory;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinder;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinder_Factory;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProviderImpl;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProviderImpl_Factory;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.logging.NotificationLogger_ExpansionStateLogger_Factory;
import com.android.systemui.statusbar.notification.logging.NotificationPanelLogger;
import com.android.systemui.statusbar.notification.people.NotificationPersonExtractorPluginBoundary;
import com.android.systemui.statusbar.notification.people.NotificationPersonExtractorPluginBoundary_Factory;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifierImpl;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifierImpl_Factory;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationViewController;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationViewController_Factory;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialogController;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialogController_Factory;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialog_Builder_Factory;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController_Factory;
import com.android.systemui.statusbar.notification.row.ExpandableOutlineViewController;
import com.android.systemui.statusbar.notification.row.ExpandableOutlineViewController_Factory;
import com.android.systemui.statusbar.notification.row.ExpandableViewController;
import com.android.systemui.statusbar.notification.row.ExpandableViewController_Factory;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import com.android.systemui.statusbar.notification.row.NotifBindPipelineInitializer;
import com.android.systemui.statusbar.notification.row.NotifBindPipelineInitializer_Factory;
import com.android.systemui.statusbar.notification.row.NotifBindPipelineLogger;
import com.android.systemui.statusbar.notification.row.NotifBindPipelineLogger_Factory;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline_Factory;
import com.android.systemui.statusbar.notification.row.NotifInflationErrorManager;
import com.android.systemui.statusbar.notification.row.NotifInflationErrorManager_Factory;
import com.android.systemui.statusbar.notification.row.NotifRemoteViewCache;
import com.android.systemui.statusbar.notification.row.NotifRemoteViewCacheImpl;
import com.android.systemui.statusbar.notification.row.NotifRemoteViewCacheImpl_Factory;
import com.android.systemui.statusbar.notification.row.NotificationContentInflater;
import com.android.systemui.statusbar.notification.row.NotificationContentInflater_Factory;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.notification.row.OnUserInteractionCallback;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import com.android.systemui.statusbar.notification.row.RowContentBindStageLogger;
import com.android.systemui.statusbar.notification.row.RowContentBindStageLogger_Factory;
import com.android.systemui.statusbar.notification.row.RowContentBindStage_Factory;
import com.android.systemui.statusbar.notification.row.RowInflaterTask_Factory;
import com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent;
import com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideAppNameFactory;
import com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideNotificationKeyFactory;
import com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory;
import com.android.systemui.statusbar.notification.row.dagger.NotificationShelfComponent;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.AmbientState_Factory;
import com.android.systemui.statusbar.notification.stack.ForegroundServiceSectionController;
import com.android.systemui.statusbar.notification.stack.ForegroundServiceSectionController_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.notification.stack.NotificationRoundnessManager;
import com.android.systemui.statusbar.notification.stack.NotificationRoundnessManager_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsManager;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsManager_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper_Builder_Factory;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.BiometricUnlockController_Factory;
import com.android.systemui.statusbar.phone.CollapsedStatusBarFragment;
import com.android.systemui.statusbar.phone.DarkIconDispatcherImpl;
import com.android.systemui.statusbar.phone.DarkIconDispatcherImpl_Factory;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.DozeParameters_Factory;
import com.android.systemui.statusbar.phone.DozeScrimController;
import com.android.systemui.statusbar.phone.DozeScrimController_Factory;
import com.android.systemui.statusbar.phone.DozeServiceHost;
import com.android.systemui.statusbar.phone.DozeServiceHost_Factory;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.KeyguardBouncer;
import com.android.systemui.statusbar.phone.KeyguardBouncer_Factory_Factory;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.KeyguardBypassController_Factory;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil_Factory;
import com.android.systemui.statusbar.phone.KeyguardEnvironmentImpl;
import com.android.systemui.statusbar.phone.KeyguardEnvironmentImpl_Factory;
import com.android.systemui.statusbar.phone.KeyguardLiftController;
import com.android.systemui.statusbar.phone.KeyguardStatusBarView;
import com.android.systemui.statusbar.phone.KeyguardStatusBarViewController;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.LightBarController_Factory;
import com.android.systemui.statusbar.phone.LightsOutNotifController;
import com.android.systemui.statusbar.phone.LightsOutNotifController_Factory;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger_Factory;
import com.android.systemui.statusbar.phone.LockscreenWallpaper;
import com.android.systemui.statusbar.phone.LockscreenWallpaper_Factory;
import com.android.systemui.statusbar.phone.ManagedProfileControllerImpl;
import com.android.systemui.statusbar.phone.ManagedProfileControllerImpl_Factory;
import com.android.systemui.statusbar.phone.MultiUserSwitch;
import com.android.systemui.statusbar.phone.MultiUserSwitchController;
import com.android.systemui.statusbar.phone.MultiUserSwitchController_Factory;
import com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import com.android.systemui.statusbar.phone.NotificationIconAreaController_Factory;
import com.android.systemui.statusbar.phone.NotificationPanelView;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController_Factory;
import com.android.systemui.statusbar.phone.NotificationShadeWindowControllerImpl;
import com.android.systemui.statusbar.phone.NotificationShadeWindowControllerImpl_Factory;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.android.systemui.statusbar.phone.NotificationShadeWindowViewController;
import com.android.systemui.statusbar.phone.NotificationTapHelper;
import com.android.systemui.statusbar.phone.NotificationTapHelper_Factory_Factory;
import com.android.systemui.statusbar.phone.PhoneStatusBarPolicy;
import com.android.systemui.statusbar.phone.PhoneStatusBarPolicy_Factory;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.ShadeControllerImpl;
import com.android.systemui.statusbar.phone.ShadeControllerImpl_Factory;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider_Factory;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusBarIconControllerImpl;
import com.android.systemui.statusbar.phone.StatusBarIconControllerImpl_Factory;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager_Factory;
import com.android.systemui.statusbar.phone.StatusBarLocationPublisher;
import com.android.systemui.statusbar.phone.StatusBarLocationPublisher_Factory;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarterLogger;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarterLogger_Factory;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter_Builder_Factory;
import com.android.systemui.statusbar.phone.StatusBarRemoteInputCallback;
import com.android.systemui.statusbar.phone.StatusBarRemoteInputCallback_Factory;
import com.android.systemui.statusbar.phone.StatusBarSignalPolicy;
import com.android.systemui.statusbar.phone.StatusBarSignalPolicy_Factory;
import com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager;
import com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager_Factory;
import com.android.systemui.statusbar.phone.StatusBarWindowController;
import com.android.systemui.statusbar.phone.StatusBarWindowController_Factory;
import com.android.systemui.statusbar.phone.TapAgainView;
import com.android.systemui.statusbar.phone.TapAgainViewController;
import com.android.systemui.statusbar.phone.TapAgainViewController_Factory;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController_Factory;
import com.android.systemui.statusbar.phone.UserAvatarView;
import com.android.systemui.statusbar.phone.dagger.StatusBarComponent;
import com.android.systemui.statusbar.phone.dagger.StatusBarPhoneDependenciesModule_ProvideNotificationGroupAlertTransferHelperFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_GetAuthRippleViewFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_GetLockIconViewFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_GetNotificationPanelViewFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_GetTapAgainViewFactory;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallLogger;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallLogger_Factory;
import com.android.systemui.statusbar.policy.AccessPointControllerImpl;
import com.android.systemui.statusbar.policy.AccessPointControllerImpl_WifiPickerTrackerFactory_Factory;
import com.android.systemui.statusbar.policy.AccessibilityController;
import com.android.systemui.statusbar.policy.AccessibilityController_Factory;
import com.android.systemui.statusbar.policy.AccessibilityManagerWrapper;
import com.android.systemui.statusbar.policy.AccessibilityManagerWrapper_Factory;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.BatteryStateNotifier;
import com.android.systemui.statusbar.policy.BatteryStateNotifier_Factory;
import com.android.systemui.statusbar.policy.BluetoothControllerImpl;
import com.android.systemui.statusbar.policy.BluetoothControllerImpl_Factory;
import com.android.systemui.statusbar.policy.CallbackHandler;
import com.android.systemui.statusbar.policy.CallbackHandler_Factory;
import com.android.systemui.statusbar.policy.CastControllerImpl;
import com.android.systemui.statusbar.policy.CastControllerImpl_Factory;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DeviceControlsControllerImpl;
import com.android.systemui.statusbar.policy.DeviceControlsControllerImpl_Factory;
import com.android.systemui.statusbar.policy.DeviceProvisionedControllerImpl;
import com.android.systemui.statusbar.policy.DeviceProvisionedControllerImpl_Factory;
import com.android.systemui.statusbar.policy.ExtensionControllerImpl;
import com.android.systemui.statusbar.policy.ExtensionControllerImpl_Factory;
import com.android.systemui.statusbar.policy.FlashlightControllerImpl;
import com.android.systemui.statusbar.policy.FlashlightControllerImpl_Factory;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.HotspotControllerImpl;
import com.android.systemui.statusbar.policy.HotspotControllerImpl_Factory;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.statusbar.policy.KeyguardQsUserSwitchController;
import com.android.systemui.statusbar.policy.KeyguardQsUserSwitchController_Factory;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.KeyguardStateControllerImpl;
import com.android.systemui.statusbar.policy.KeyguardStateControllerImpl_Factory;
import com.android.systemui.statusbar.policy.KeyguardUserSwitcherController;
import com.android.systemui.statusbar.policy.KeyguardUserSwitcherController_Factory;
import com.android.systemui.statusbar.policy.KeyguardUserSwitcherView;
import com.android.systemui.statusbar.policy.LocationControllerImpl;
import com.android.systemui.statusbar.policy.LocationControllerImpl_Factory;
import com.android.systemui.statusbar.policy.NetworkController;
import com.android.systemui.statusbar.policy.NetworkControllerImpl;
import com.android.systemui.statusbar.policy.NetworkControllerImpl_Factory;
import com.android.systemui.statusbar.policy.NextAlarmControllerImpl;
import com.android.systemui.statusbar.policy.NextAlarmControllerImpl_Factory;
import com.android.systemui.statusbar.policy.RemoteInputQuickSettingsDisabler;
import com.android.systemui.statusbar.policy.RemoteInputQuickSettingsDisabler_Factory;
import com.android.systemui.statusbar.policy.RemoteInputUriController;
import com.android.systemui.statusbar.policy.RemoteInputUriController_Factory;
import com.android.systemui.statusbar.policy.RotationLockControllerImpl;
import com.android.systemui.statusbar.policy.RotationLockControllerImpl_Factory;
import com.android.systemui.statusbar.policy.SecurityControllerImpl;
import com.android.systemui.statusbar.policy.SecurityControllerImpl_Factory;
import com.android.systemui.statusbar.policy.SensorPrivacyController;
import com.android.systemui.statusbar.policy.SmartActionInflaterImpl;
import com.android.systemui.statusbar.policy.SmartActionInflaterImpl_Factory;
import com.android.systemui.statusbar.policy.SmartReplyConstants;
import com.android.systemui.statusbar.policy.SmartReplyConstants_Factory;
import com.android.systemui.statusbar.policy.SmartReplyInflaterImpl;
import com.android.systemui.statusbar.policy.SmartReplyInflaterImpl_Factory;
import com.android.systemui.statusbar.policy.SmartReplyStateInflaterImpl;
import com.android.systemui.statusbar.policy.SmartReplyStateInflaterImpl_Factory;
import com.android.systemui.statusbar.policy.UserInfoControllerImpl;
import com.android.systemui.statusbar.policy.UserInfoControllerImpl_Factory;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.statusbar.policy.UserSwitcherController_Factory;
import com.android.systemui.statusbar.policy.UserSwitcherController_UserDetailAdapter_Factory;
import com.android.systemui.statusbar.policy.WalletControllerImpl;
import com.android.systemui.statusbar.policy.WalletControllerImpl_Factory;
import com.android.systemui.statusbar.policy.ZenModeControllerImpl;
import com.android.systemui.statusbar.policy.ZenModeControllerImpl_Factory;
import com.android.systemui.statusbar.policy.dagger.StatusBarPolicyModule_ProvideAccessPointControllerImplFactory;
import com.android.systemui.statusbar.tv.TvStatusBar;
import com.android.systemui.statusbar.tv.TvStatusBar_Factory;
import com.android.systemui.statusbar.tv.notifications.TvNotificationHandler;
import com.android.systemui.statusbar.tv.notifications.TvNotificationHandler_Factory;
import com.android.systemui.statusbar.tv.notifications.TvNotificationPanelActivity;
import com.android.systemui.statusbar.tv.notifications.TvNotificationPanelActivity_Factory;
import com.android.systemui.telephony.TelephonyCallback_Factory;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.telephony.TelephonyListenerManager_Factory;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.systemui.theme.ThemeOverlayController;
import com.android.systemui.toast.ToastFactory;
import com.android.systemui.toast.ToastFactory_Factory;
import com.android.systemui.toast.ToastLogger;
import com.android.systemui.toast.ToastLogger_Factory;
import com.android.systemui.toast.ToastUI;
import com.android.systemui.toast.ToastUI_Factory;
import com.android.systemui.tracing.ProtoTracer;
import com.android.systemui.tracing.ProtoTracer_Factory;
import com.android.systemui.tuner.TunablePadding;
import com.android.systemui.tuner.TunablePadding_TunablePaddingService_Factory;
import com.android.systemui.tuner.TunerActivity;
import com.android.systemui.tuner.TunerActivity_Factory;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.tuner.TunerServiceImpl;
import com.android.systemui.tuner.TunerServiceImpl_Factory;
import com.android.systemui.usb.UsbDebuggingActivity;
import com.android.systemui.usb.UsbDebuggingActivity_Factory;
import com.android.systemui.usb.UsbDebuggingSecondaryUserActivity;
import com.android.systemui.usb.UsbDebuggingSecondaryUserActivity_Factory;
import com.android.systemui.user.CreateUserActivity;
import com.android.systemui.user.CreateUserActivity_Factory;
import com.android.systemui.user.UserCreator;
import com.android.systemui.user.UserCreator_Factory;
import com.android.systemui.user.UserModule;
import com.android.systemui.user.UserModule_ProvideEditUserInfoControllerFactory;
import com.android.systemui.util.CarrierConfigTracker;
import com.android.systemui.util.CarrierConfigTracker_Factory;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.DeviceConfigProxy_Factory;
import com.android.systemui.util.InjectionInflationController;
import com.android.systemui.util.InjectionInflationController_Factory;
import com.android.systemui.util.RingerModeTrackerImpl;
import com.android.systemui.util.RingerModeTrackerImpl_Factory;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.concurrency.ExecutionImpl_Factory;
import com.android.systemui.util.concurrency.GlobalConcurrencyModule_ProvideMainExecutorFactory;
import com.android.systemui.util.concurrency.GlobalConcurrencyModule_ProvideMainHandlerFactory;
import com.android.systemui.util.concurrency.GlobalConcurrencyModule_ProvideMainLooperFactory;
import com.android.systemui.util.concurrency.RepeatableExecutor;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideBackgroundDelayableExecutorFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideBackgroundExecutorFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideBackgroundRepeatableExecutorFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideBgHandlerFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideBgLooperFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideDelayableExecutorFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideExecutorFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideLongRunningExecutorFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideLongRunningLooperFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideMainDelayableExecutorFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideUiBackgroundExecutorFactory;
import com.android.systemui.util.concurrency.ThreadFactory;
import com.android.systemui.util.concurrency.ThreadFactoryImpl_Factory;
import com.android.systemui.util.io.Files;
import com.android.systemui.util.io.Files_Factory;
import com.android.systemui.util.leak.GarbageMonitor;
import com.android.systemui.util.leak.GarbageMonitor_Factory;
import com.android.systemui.util.leak.GarbageMonitor_MemoryTile_Factory;
import com.android.systemui.util.leak.GarbageMonitor_Service_Factory;
import com.android.systemui.util.leak.LeakDetector;
import com.android.systemui.util.leak.LeakReporter;
import com.android.systemui.util.leak.LeakReporter_Factory;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.android.systemui.util.sensors.AsyncSensorManager_Factory;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.sensors.ProximitySensor_Factory;
import com.android.systemui.util.sensors.ProximitySensor_ProximityCheck_Factory;
import com.android.systemui.util.sensors.SensorModule_ProvidePrimaryProxSensorFactory;
import com.android.systemui.util.sensors.SensorModule_ProvideSecondaryProxSensorFactory;
import com.android.systemui.util.sensors.ThresholdSensor;
import com.android.systemui.util.sensors.ThresholdSensorImpl_Builder_Factory;
import com.android.systemui.util.settings.GlobalSettingsImpl_Factory;
import com.android.systemui.util.settings.SecureSettingsImpl_Factory;
import com.android.systemui.util.settings.SystemSettingsImpl_Factory;
import com.android.systemui.util.time.DateFormatUtil;
import com.android.systemui.util.time.DateFormatUtil_Factory;
import com.android.systemui.util.time.SystemClock;
import com.android.systemui.util.time.SystemClockImpl_Factory;
import com.android.systemui.util.wakelock.DelayedWakeLock;
import com.android.systemui.util.wakelock.DelayedWakeLock_Builder_Factory;
import com.android.systemui.util.wakelock.WakeLock;
import com.android.systemui.util.wakelock.WakeLock_Builder_Factory;
import com.android.systemui.util.wrapper.BuildInfo;
import com.android.systemui.util.wrapper.BuildInfo_Factory;
import com.android.systemui.volume.VolumeDialogComponent;
import com.android.systemui.volume.VolumeDialogComponent_Factory;
import com.android.systemui.volume.VolumeDialogControllerImpl;
import com.android.systemui.volume.VolumeDialogControllerImpl_Factory;
import com.android.systemui.volume.VolumeUI;
import com.android.systemui.volume.VolumeUI_Factory;
import com.android.systemui.wallet.controller.QuickAccessWalletController;
import com.android.systemui.wallet.controller.QuickAccessWalletController_Factory;
import com.android.systemui.wallet.dagger.WalletModule_ProvideQuickAccessWalletClientFactory;
import com.android.systemui.wallet.ui.WalletActivity;
import com.android.systemui.wallet.ui.WalletActivity_Factory;
import com.android.systemui.wmshell.BubblesManager;
import com.android.systemui.wmshell.WMShell;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideAppPairsFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideBubbleControllerFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideBubblesFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideDisplayControllerFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideDisplayLayoutFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideDragAndDropControllerFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideFloatingContentCoordinatorFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideFullscreenTaskListenerFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideHideDisplayCutoutControllerFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideHideDisplayCutoutFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideLegacySplitScreenFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideOneHandedControllerFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideOneHandedFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvidePipAppOpsListenerFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvidePipMediaControllerFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvidePipUiEventLoggerFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideRemoteTransitionsFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideRootTaskDisplayAreaOrganizerFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideShellCommandHandlerFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideShellCommandHandlerImplFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideShellInitFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideShellInitImplFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideShellTaskOrganizerFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideSizeCompatUIControllerFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideSplitScreenControllerFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideSplitScreenFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideStartingSurfaceFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideStartingWindowControllerFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideSyncTransactionQueueFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideSystemWindowsFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideTaskSurfaceHelperFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideTaskViewFactoryControllerFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideTaskViewFactoryFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideTransactionPoolFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideTransitionsFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProvideWindowManagerShellWrapperFactory;
import com.android.systemui.wmshell.WMShellBaseModule_ProviderTaskStackListenerImplFactory;
import com.android.systemui.wmshell.WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory;
import com.android.systemui.wmshell.WMShellConcurrencyModule_ProvideShellMainExecutorFactory;
import com.android.systemui.wmshell.WMShellConcurrencyModule_ProvideShellMainExecutorSfVsyncAnimationHandlerFactory;
import com.android.systemui.wmshell.WMShellConcurrencyModule_ProvideShellMainHandlerFactory;
import com.android.systemui.wmshell.WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory;
import com.android.systemui.wmshell.WMShellConcurrencyModule_ProvideSysUIMainExecutorFactory;
import com.android.systemui.wmshell.WMShellModule_ProvideAppPairsFactory;
import com.android.systemui.wmshell.WMShellModule_ProvideDisplayImeControllerFactory;
import com.android.systemui.wmshell.WMShellModule_ProvideLegacySplitScreenFactory;
import com.android.systemui.wmshell.WMShellModule_ProvidePipAnimationControllerFactory;
import com.android.systemui.wmshell.WMShellModule_ProvidePipBoundsStateFactory;
import com.android.systemui.wmshell.WMShellModule_ProvidePipFactory;
import com.android.systemui.wmshell.WMShellModule_ProvidePipMotionHelperFactory;
import com.android.systemui.wmshell.WMShellModule_ProvidePipSnapAlgorithmFactory;
import com.android.systemui.wmshell.WMShellModule_ProvidePipTaskOrganizerFactory;
import com.android.systemui.wmshell.WMShellModule_ProvidePipTouchHandlerFactory;
import com.android.systemui.wmshell.WMShellModule_ProvidePipTransitionControllerFactory;
import com.android.systemui.wmshell.WMShellModule_ProvideStartingWindowTypeAlgorithmFactory;
import com.android.systemui.wmshell.WMShellModule_ProvidesPipBoundsAlgorithmFactory;
import com.android.systemui.wmshell.WMShellModule_ProvidesPipPhoneMenuControllerFactory;
import com.android.systemui.wmshell.WMShell_Factory;
import com.android.wm.shell.FullscreenTaskListener;
import com.android.wm.shell.RootTaskDisplayAreaOrganizer;
import com.android.wm.shell.ShellCommandHandler;
import com.android.wm.shell.ShellCommandHandlerImpl;
import com.android.wm.shell.ShellInit;
import com.android.wm.shell.ShellInitImpl;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.TaskViewFactory;
import com.android.wm.shell.TaskViewFactoryController;
import com.android.wm.shell.WindowManagerShellWrapper;
import com.android.wm.shell.animation.FlingAnimationUtils;
import com.android.wm.shell.animation.FlingAnimationUtils_Builder_Factory;
import com.android.wm.shell.apppairs.AppPairs;
import com.android.wm.shell.apppairs.AppPairsController;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.FloatingContentCoordinator;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.SystemWindows;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.draganddrop.DragAndDropController;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutout;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutController;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.onehanded.OneHanded;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipSnapAlgorithm;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import com.android.wm.shell.pip.phone.PipAppOpsListener;
import com.android.wm.shell.pip.phone.PipMotionHelper;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import com.android.wm.shell.sizecompatui.SizeCompatUIController;
import com.android.wm.shell.splitscreen.SplitScreen;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.startingsurface.StartingSurface;
import com.android.wm.shell.startingsurface.StartingWindowController;
import com.android.wm.shell.startingsurface.StartingWindowTypeAlgorithm;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelperController;
import com.android.wm.shell.transition.ShellTransitions;
import com.android.wm.shell.transition.Transitions;
import com.google.android.systemui.GoogleServices;
import com.google.android.systemui.GoogleServices_Factory;
import com.google.android.systemui.LiveWallpaperScrimController;
import com.google.android.systemui.LiveWallpaperScrimController_Factory;
import com.google.android.systemui.NotificationLockscreenUserManagerGoogle;
import com.google.android.systemui.NotificationLockscreenUserManagerGoogle_Factory;
import com.google.android.systemui.assist.AssistManagerGoogle;
import com.google.android.systemui.assist.AssistManagerGoogle_Factory;
import com.google.android.systemui.assist.GoogleAssistLogger;
import com.google.android.systemui.assist.GoogleAssistLogger_Factory;
import com.google.android.systemui.assist.OpaEnabledDispatcher;
import com.google.android.systemui.assist.OpaEnabledDispatcher_Factory;
import com.google.android.systemui.assist.OpaEnabledReceiver;
import com.google.android.systemui.assist.OpaEnabledReceiver_Factory;
import com.google.android.systemui.assist.OpaEnabledSettings;
import com.google.android.systemui.assist.OpaEnabledSettings_Factory;
import com.google.android.systemui.assist.uihints.AssistantPresenceHandler;
import com.google.android.systemui.assist.uihints.AssistantPresenceHandler_Factory;
import com.google.android.systemui.assist.uihints.AssistantUIHintsModule_BindEdgeLightsInfoListenersFactory;
import com.google.android.systemui.assist.uihints.AssistantUIHintsModule_ProvideActivityStarterFactory;
import com.google.android.systemui.assist.uihints.AssistantUIHintsModule_ProvideAudioInfoListenersFactory;
import com.google.android.systemui.assist.uihints.AssistantUIHintsModule_ProvideCardInfoListenersFactory;
import com.google.android.systemui.assist.uihints.AssistantUIHintsModule_ProvideConfigInfoListenersFactory;
import com.google.android.systemui.assist.uihints.AssistantUIHintsModule_ProvideParentViewGroupFactory;
import com.google.android.systemui.assist.uihints.AssistantWarmer;
import com.google.android.systemui.assist.uihints.AssistantWarmer_Factory;
import com.google.android.systemui.assist.uihints.ColorChangeHandler;
import com.google.android.systemui.assist.uihints.ColorChangeHandler_Factory;
import com.google.android.systemui.assist.uihints.ConfigurationHandler;
import com.google.android.systemui.assist.uihints.ConfigurationHandler_Factory;
import com.google.android.systemui.assist.uihints.FlingVelocityWrapper_Factory;
import com.google.android.systemui.assist.uihints.GlowController;
import com.google.android.systemui.assist.uihints.GlowController_Factory;
import com.google.android.systemui.assist.uihints.GoBackHandler_Factory;
import com.google.android.systemui.assist.uihints.GoogleDefaultUiController;
import com.google.android.systemui.assist.uihints.GoogleDefaultUiController_Factory;
import com.google.android.systemui.assist.uihints.IconController;
import com.google.android.systemui.assist.uihints.IconController_Factory;
import com.google.android.systemui.assist.uihints.KeyboardMonitor_Factory;
import com.google.android.systemui.assist.uihints.LightnessProvider_Factory;
import com.google.android.systemui.assist.uihints.NavBarFader;
import com.google.android.systemui.assist.uihints.NavBarFader_Factory;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import com.google.android.systemui.assist.uihints.NgaMessageHandler_Factory;
import com.google.android.systemui.assist.uihints.NgaUiController;
import com.google.android.systemui.assist.uihints.NgaUiController_Factory;
import com.google.android.systemui.assist.uihints.OverlappedElementController_Factory;
import com.google.android.systemui.assist.uihints.OverlayUiHost_Factory;
import com.google.android.systemui.assist.uihints.ScrimController;
import com.google.android.systemui.assist.uihints.ScrimController_Factory;
import com.google.android.systemui.assist.uihints.TakeScreenshotHandler_Factory;
import com.google.android.systemui.assist.uihints.TaskStackNotifier_Factory;
import com.google.android.systemui.assist.uihints.TimeoutManager_Factory;
import com.google.android.systemui.assist.uihints.TouchInsideHandler;
import com.google.android.systemui.assist.uihints.TouchInsideHandler_Factory;
import com.google.android.systemui.assist.uihints.TouchOutsideHandler_Factory;
import com.google.android.systemui.assist.uihints.TranscriptionController;
import com.google.android.systemui.assist.uihints.TranscriptionController_Factory;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsController;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsController_Factory;
import com.google.android.systemui.assist.uihints.input.InputModule_ProvideTouchActionRegionsFactory;
import com.google.android.systemui.assist.uihints.input.InputModule_ProvideTouchInsideRegionsFactory;
import com.google.android.systemui.assist.uihints.input.NgaInputHandler;
import com.google.android.systemui.assist.uihints.input.NgaInputHandler_Factory;
import com.google.android.systemui.assist.uihints.input.TouchActionRegion;
import com.google.android.systemui.assist.uihints.input.TouchInsideRegion;
import com.google.android.systemui.autorotate.AutorotateDataService;
import com.google.android.systemui.autorotate.AutorotateDataService_Factory;
import com.google.android.systemui.autorotate.DataLogger_Factory;
import com.google.android.systemui.columbus.ColumbusContentObserver;
import com.google.android.systemui.columbus.ColumbusContentObserver_Factory_Factory;
import com.google.android.systemui.columbus.ColumbusModule_ProvideBlockingSystemKeysFactory;
import com.google.android.systemui.columbus.ColumbusModule_ProvideColumbusActionsFactory;
import com.google.android.systemui.columbus.ColumbusModule_ProvideColumbusEffectsFactory;
import com.google.android.systemui.columbus.ColumbusModule_ProvideColumbusGatesFactory;
import com.google.android.systemui.columbus.ColumbusModule_ProvideFullscreenActionsFactory;
import com.google.android.systemui.columbus.ColumbusModule_ProvideGestureAdjustmentsFactory;
import com.google.android.systemui.columbus.ColumbusModule_ProvideGestureSensorFactory;
import com.google.android.systemui.columbus.ColumbusModule_ProvideTransientGateDurationFactory;
import com.google.android.systemui.columbus.ColumbusModule_ProvideUserSelectedActionsFactory;
import com.google.android.systemui.columbus.ColumbusService;
import com.google.android.systemui.columbus.ColumbusServiceWrapper;
import com.google.android.systemui.columbus.ColumbusServiceWrapper_Factory;
import com.google.android.systemui.columbus.ColumbusService_Factory;
import com.google.android.systemui.columbus.ColumbusSettings;
import com.google.android.systemui.columbus.ColumbusSettings_Factory;
import com.google.android.systemui.columbus.ContentResolverWrapper;
import com.google.android.systemui.columbus.ContentResolverWrapper_Factory;
import com.google.android.systemui.columbus.PowerManagerWrapper;
import com.google.android.systemui.columbus.PowerManagerWrapper_Factory;
import com.google.android.systemui.columbus.actions.Action;
import com.google.android.systemui.columbus.actions.DismissTimer;
import com.google.android.systemui.columbus.actions.DismissTimer_Factory;
import com.google.android.systemui.columbus.actions.LaunchApp;
import com.google.android.systemui.columbus.actions.LaunchApp_Factory;
import com.google.android.systemui.columbus.actions.LaunchOpa_Factory;
import com.google.android.systemui.columbus.actions.LaunchOverview;
import com.google.android.systemui.columbus.actions.LaunchOverview_Factory;
import com.google.android.systemui.columbus.actions.ManageMedia;
import com.google.android.systemui.columbus.actions.ManageMedia_Factory;
import com.google.android.systemui.columbus.actions.OpenNotificationShade;
import com.google.android.systemui.columbus.actions.OpenNotificationShade_Factory;
import com.google.android.systemui.columbus.actions.SettingsAction_Factory;
import com.google.android.systemui.columbus.actions.SnoozeAlarm;
import com.google.android.systemui.columbus.actions.SnoozeAlarm_Factory;
import com.google.android.systemui.columbus.actions.TakeScreenshot;
import com.google.android.systemui.columbus.actions.TakeScreenshot_Factory;
import com.google.android.systemui.columbus.actions.UserAction;
import com.google.android.systemui.columbus.actions.UserSelectedAction;
import com.google.android.systemui.columbus.actions.UserSelectedAction_Factory;
import com.google.android.systemui.columbus.feedback.FeedbackEffect;
import com.google.android.systemui.columbus.feedback.HapticClick;
import com.google.android.systemui.columbus.feedback.HapticClick_Factory;
import com.google.android.systemui.columbus.feedback.UserActivity;
import com.google.android.systemui.columbus.feedback.UserActivity_Factory;
import com.google.android.systemui.columbus.gates.CameraVisibility;
import com.google.android.systemui.columbus.gates.CameraVisibility_Factory;
import com.google.android.systemui.columbus.gates.ChargingState;
import com.google.android.systemui.columbus.gates.ChargingState_Factory;
import com.google.android.systemui.columbus.gates.FlagEnabled;
import com.google.android.systemui.columbus.gates.FlagEnabled_Factory;
import com.google.android.systemui.columbus.gates.Gate;
import com.google.android.systemui.columbus.gates.KeyguardProximity;
import com.google.android.systemui.columbus.gates.KeyguardProximity_Factory;
import com.google.android.systemui.columbus.gates.KeyguardVisibility;
import com.google.android.systemui.columbus.gates.KeyguardVisibility_Factory;
import com.google.android.systemui.columbus.gates.PowerSaveState;
import com.google.android.systemui.columbus.gates.PowerSaveState_Factory;
import com.google.android.systemui.columbus.gates.PowerState;
import com.google.android.systemui.columbus.gates.PowerState_Factory;
import com.google.android.systemui.columbus.gates.Proximity;
import com.google.android.systemui.columbus.gates.Proximity_Factory;
import com.google.android.systemui.columbus.gates.ScreenTouch;
import com.google.android.systemui.columbus.gates.ScreenTouch_Factory;
import com.google.android.systemui.columbus.gates.SetupWizard;
import com.google.android.systemui.columbus.gates.SetupWizard_Factory;
import com.google.android.systemui.columbus.gates.SilenceAlertsDisabled;
import com.google.android.systemui.columbus.gates.SilenceAlertsDisabled_Factory;
import com.google.android.systemui.columbus.gates.SystemKeyPress;
import com.google.android.systemui.columbus.gates.SystemKeyPress_Factory;
import com.google.android.systemui.columbus.gates.UsbState;
import com.google.android.systemui.columbus.gates.UsbState_Factory;
import com.google.android.systemui.columbus.gates.VrMode;
import com.google.android.systemui.columbus.gates.VrMode_Factory;
import com.google.android.systemui.columbus.sensors.CHREGestureSensor;
import com.google.android.systemui.columbus.sensors.CHREGestureSensor_Factory;
import com.google.android.systemui.columbus.sensors.GestureController;
import com.google.android.systemui.columbus.sensors.GestureController_Factory;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import com.google.android.systemui.columbus.sensors.GestureSensorImpl;
import com.google.android.systemui.columbus.sensors.GestureSensorImpl_Factory;
import com.google.android.systemui.columbus.sensors.config.Adjustment;
import com.google.android.systemui.columbus.sensors.config.GestureConfiguration;
import com.google.android.systemui.columbus.sensors.config.GestureConfiguration_Factory;
import com.google.android.systemui.columbus.sensors.config.LowSensitivitySettingAdjustment;
import com.google.android.systemui.columbus.sensors.config.LowSensitivitySettingAdjustment_Factory;
import com.google.android.systemui.columbus.sensors.config.SensorConfiguration;
import com.google.android.systemui.columbus.sensors.config.SensorConfiguration_Factory;
import com.google.android.systemui.dagger.SysUIGoogleGlobalRootComponent;
import com.google.android.systemui.dagger.SysUIGoogleSysUIComponent;
import com.google.android.systemui.elmyra.ServiceConfigurationGoogle;
import com.google.android.systemui.elmyra.ServiceConfigurationGoogle_Factory;
import com.google.android.systemui.elmyra.actions.CameraAction;
import com.google.android.systemui.elmyra.actions.CameraAction_Builder_Factory;
import com.google.android.systemui.elmyra.actions.LaunchOpa;
import com.google.android.systemui.elmyra.actions.LaunchOpa_Builder_Factory;
import com.google.android.systemui.elmyra.actions.SettingsAction;
import com.google.android.systemui.elmyra.actions.SettingsAction_Builder_Factory;
import com.google.android.systemui.elmyra.actions.SetupWizardAction;
import com.google.android.systemui.elmyra.actions.SetupWizardAction_Builder_Factory;
import com.google.android.systemui.elmyra.actions.SilenceCall;
import com.google.android.systemui.elmyra.actions.SilenceCall_Factory;
import com.google.android.systemui.elmyra.actions.UnpinNotifications;
import com.google.android.systemui.elmyra.actions.UnpinNotifications_Factory;
import com.google.android.systemui.elmyra.feedback.AssistInvocationEffect;
import com.google.android.systemui.elmyra.feedback.AssistInvocationEffect_Factory;
import com.google.android.systemui.elmyra.feedback.OpaHomeButton;
import com.google.android.systemui.elmyra.feedback.OpaHomeButton_Factory;
import com.google.android.systemui.elmyra.feedback.OpaLockscreen;
import com.google.android.systemui.elmyra.feedback.OpaLockscreen_Factory;
import com.google.android.systemui.elmyra.feedback.SquishyNavigationButtons;
import com.google.android.systemui.elmyra.feedback.SquishyNavigationButtons_Factory;
import com.google.android.systemui.elmyra.gates.TelephonyActivity;
import com.google.android.systemui.elmyra.gates.TelephonyActivity_Factory;
import com.google.android.systemui.fingerprint.UdfpsGhbmProvider_Factory;
import com.google.android.systemui.fingerprint.UdfpsLhbmProvider_Factory;
import com.google.android.systemui.gamedashboard.EntryPointController;
import com.google.android.systemui.gamedashboard.EntryPointController_Factory;
import com.google.android.systemui.gamedashboard.FpsController;
import com.google.android.systemui.gamedashboard.FpsController_Factory;
import com.google.android.systemui.gamedashboard.GameDashboardUiEventLogger;
import com.google.android.systemui.gamedashboard.GameDashboardUiEventLogger_Factory;
import com.google.android.systemui.gamedashboard.GameMenuActivity;
import com.google.android.systemui.gamedashboard.GameMenuActivity_Factory;
import com.google.android.systemui.gamedashboard.GameModeDndController;
import com.google.android.systemui.gamedashboard.GameModeDndController_Factory;
import com.google.android.systemui.gamedashboard.ScreenRecordController;
import com.google.android.systemui.gamedashboard.ScreenRecordController_Factory;
import com.google.android.systemui.gamedashboard.ShortcutBarController;
import com.google.android.systemui.gamedashboard.ShortcutBarController_Factory;
import com.google.android.systemui.gamedashboard.ToastController;
import com.google.android.systemui.gamedashboard.ToastController_Factory;
import com.google.android.systemui.keyguard.KeyguardSliceProviderGoogle;
import com.google.android.systemui.keyguard.KeyguardSliceProviderGoogle_MembersInjector;
import com.google.android.systemui.power.EnhancedEstimatesGoogleImpl;
import com.google.android.systemui.power.EnhancedEstimatesGoogleImpl_Factory;
import com.google.android.systemui.power.PowerModuleGoogle_ProvideWarningsUiFactory;
import com.google.android.systemui.qs.dagger.QSModuleGoogle_ProvideAutoTileManagerFactory;
import com.google.android.systemui.qs.tileimpl.QSFactoryImplGoogle;
import com.google.android.systemui.qs.tileimpl.QSFactoryImplGoogle_Factory;
import com.google.android.systemui.qs.tiles.BatterySaverTileGoogle;
import com.google.android.systemui.qs.tiles.BatterySaverTileGoogle_Factory;
import com.google.android.systemui.qs.tiles.OverlayToggleTile;
import com.google.android.systemui.qs.tiles.OverlayToggleTile_Factory;
import com.google.android.systemui.qs.tiles.ReverseChargingTile;
import com.google.android.systemui.qs.tiles.ReverseChargingTile_Factory;
import com.google.android.systemui.reversecharging.ReverseChargingController;
import com.google.android.systemui.reversecharging.ReverseChargingController_Factory;
import com.google.android.systemui.reversecharging.ReverseChargingViewController;
import com.google.android.systemui.reversecharging.ReverseChargingViewController_Factory;
import com.google.android.systemui.reversecharging.ReverseWirelessCharger;
import com.google.android.systemui.smartspace.KeyguardMediaViewController;
import com.google.android.systemui.smartspace.KeyguardMediaViewController_Factory;
import com.google.android.systemui.smartspace.KeyguardSmartspaceController;
import com.google.android.systemui.smartspace.KeyguardSmartspaceController_Factory;
import com.google.android.systemui.smartspace.KeyguardZenAlarmViewController;
import com.google.android.systemui.smartspace.KeyguardZenAlarmViewController_Factory;
import com.google.android.systemui.smartspace.SmartSpaceController;
import com.google.android.systemui.smartspace.SmartSpaceController_Factory;
import com.google.android.systemui.statusbar.KeyguardIndicationControllerGoogle;
import com.google.android.systemui.statusbar.KeyguardIndicationControllerGoogle_Factory;
import com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService;
import com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService_Factory;
import com.google.android.systemui.statusbar.notification.voicereplies.DebugNotificationVoiceReplyClient;
import com.google.android.systemui.statusbar.notification.voicereplies.DebugNotificationVoiceReplyClient_Factory;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyClient;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController_Factory;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger_Factory;
import com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyModule_ProvideNotificationVoiceReplyClientFactory;
import com.google.android.systemui.statusbar.phone.StatusBarGoogle;
import com.google.android.systemui.statusbar.phone.StatusBarGoogleModule_ProvideReverseChargingViewControllerOptionalFactory;
import com.google.android.systemui.statusbar.phone.StatusBarGoogleModule_ProvideStatusBarFactory;
import com.google.android.systemui.statusbar.phone.WallpaperNotifier;
import com.google.android.systemui.statusbar.phone.WallpaperNotifier_Factory;
import com.google.android.systemui.theme.ThemeOverlayControllerGoogle;
import com.google.android.systemui.theme.ThemeOverlayControllerGoogle_Factory;
import dagger.Lazy;
import dagger.internal.DelegateFactory;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.InstanceFactory;
import dagger.internal.MapProviderFactory;
import dagger.internal.Preconditions;
import dagger.internal.SetFactory;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class DaggerSysUIGoogleGlobalRootComponent implements SysUIGoogleGlobalRootComponent {
    private static final Provider ABSENT_JDK_OPTIONAL_PROVIDER = InstanceFactory.create(Optional.empty());
    private Provider<BuildInfo> buildInfoProvider;
    private final Context context;
    private Provider<Context> contextProvider;
    private Provider<OpaEnabledSettings> opaEnabledSettingsProvider;
    private Provider<AccessibilityManager> provideAccessibilityManagerProvider;
    private Provider<ActivityManager> provideActivityManagerProvider;
    private Provider<ActivityTaskManager> provideActivityTaskManagerProvider;
    private Provider<AlarmManager> provideAlarmManagerProvider;
    private Provider<AudioManager> provideAudioManagerProvider;
    private Provider<ColorDisplayManager> provideColorDisplayManagerProvider;
    private Provider<ConnectivityManager> provideConnectivityManagagerProvider;
    private Provider<ContentResolver> provideContentResolverProvider;
    private Provider<CrossWindowBlurListeners> provideCrossWindowBlurListenersProvider;
    private Provider<DevicePolicyManager> provideDevicePolicyManagerProvider;
    private Provider<Integer> provideDisplayIdProvider;
    private Provider<DisplayManager> provideDisplayManagerProvider;
    private Provider<DisplayMetrics> provideDisplayMetricsProvider;
    private Provider<Execution> provideExecutionProvider;
    private Provider<FaceManager> provideFaceManagerProvider;
    private Provider<IActivityManager> provideIActivityManagerProvider;
    private Provider<IActivityTaskManager> provideIActivityTaskManagerProvider;
    private Provider<IAudioService> provideIAudioServiceProvider;
    private Provider<IBatteryStats> provideIBatteryStatsProvider;
    private Provider<IDreamManager> provideIDreamManagerProvider;
    private Provider<IPackageManager> provideIPackageManagerProvider;
    private Provider<IStatusBarService> provideIStatusBarServiceProvider;
    private Provider<IWindowManager> provideIWindowManagerProvider;
    private Provider<InputMethodManager> provideInputMethodManagerProvider;
    private Provider<KeyguardManager> provideKeyguardManagerProvider;
    private Provider<LatencyTracker> provideLatencyTrackerProvider;
    private Provider<LauncherApps> provideLauncherAppsProvider;
    private Provider<Executor> provideMainExecutorProvider;
    private Provider<Handler> provideMainHandlerProvider;
    private Provider<MediaRouter2Manager> provideMediaRouter2ManagerProvider;
    private Provider<MediaSessionManager> provideMediaSessionManagerProvider;
    private Provider<NetworkScoreManager> provideNetworkScoreManagerProvider;
    private Provider<NotificationManager> provideNotificationManagerProvider;
    private Provider<Optional<Vibrator>> provideOptionalVibratorProvider;
    private Provider<OverlayManager> provideOverlayManagerProvider;
    private Provider<PackageManager> providePackageManagerProvider;
    private Provider<PackageManagerWrapper> providePackageManagerWrapperProvider;
    private Provider<PermissionManager> providePermissionManagerProvider;
    private Provider<PowerManager> providePowerManagerProvider;
    private Provider<Resources> provideResourcesProvider;
    private Provider<SensorPrivacyManager> provideSensorPrivacyManagerProvider;
    private Provider<ShortcutManager> provideShortcutManagerProvider;
    private Provider<SmartspaceManager> provideSmartspaceManagerProvider;
    private Provider<StatsManager> provideStatsManagerProvider;
    private Provider<SubscriptionManager> provideSubcriptionManagerProvider;
    private Provider<TelecomManager> provideTelecomManagerProvider;
    private Provider<TelephonyManager> provideTelephonyManagerProvider;
    private Provider<TrustManager> provideTrustManagerProvider;
    private Provider<UiEventLogger> provideUiEventLoggerProvider;
    private Provider<UserManager> provideUserManagerProvider;
    private Provider<Vibrator> provideVibratorProvider;
    private Provider<ViewConfiguration> provideViewConfigurationProvider;
    private Provider<WallpaperManager> provideWallpaperManagerProvider;
    private Provider<WifiManager> provideWifiManagerProvider;
    private Provider<WindowManager> provideWindowManagerProvider;
    private Provider<FingerprintManager> providesFingerprintManagerProvider;
    private Provider<SensorManager> providesSensorManagerProvider;
    private Provider<QSExpansionPathInterpolator> qSExpansionPathInterpolatorProvider;

    private DaggerSysUIGoogleGlobalRootComponent(GlobalModule globalModule, Context context) {
        this.context = context;
        initialize(globalModule, context);
    }

    public static SysUIGoogleGlobalRootComponent.Builder builder() {
        return new Builder();
    }

    /* access modifiers changed from: private */
    public Resources mainResources() {
        return FrameworkServicesModule_ProvideResourcesFactory.provideResources(this.context);
    }

    /* access modifiers changed from: private */
    public Executor mainExecutor() {
        return GlobalConcurrencyModule_ProvideMainExecutorFactory.provideMainExecutor(this.context);
    }

    private void initialize(GlobalModule globalModule, Context context) {
        this.provideIWindowManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIWindowManagerFactory.create());
        this.contextProvider = InstanceFactory.create(context);
        this.provideMainHandlerProvider = GlobalConcurrencyModule_ProvideMainHandlerFactory.create(GlobalConcurrencyModule_ProvideMainLooperFactory.create());
        this.provideIStatusBarServiceProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIStatusBarServiceFactory.create());
        this.provideWindowManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideWindowManagerFactory.create(this.contextProvider));
        this.provideLauncherAppsProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideLauncherAppsFactory.create(this.contextProvider));
        this.provideUiEventLoggerProvider = DoubleCheck.provider(GlobalModule_ProvideUiEventLoggerFactory.create());
        this.providePackageManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidePackageManagerFactory.create(this.contextProvider));
        this.provideMainExecutorProvider = GlobalConcurrencyModule_ProvideMainExecutorFactory.create(this.contextProvider);
        this.provideUserManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideUserManagerFactory.create(this.contextProvider));
        this.provideContentResolverProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideContentResolverFactory.create(this.contextProvider));
        this.provideAudioManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideAudioManagerFactory.create(this.contextProvider));
        this.provideActivityTaskManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideActivityTaskManagerFactory.create());
        this.providesFingerprintManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidesFingerprintManagerFactory.create(this.contextProvider));
        this.provideFaceManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideFaceManagerFactory.create(this.contextProvider));
        this.provideExecutionProvider = DoubleCheck.provider(ExecutionImpl_Factory.create());
        this.provideDisplayMetricsProvider = GlobalModule_ProvideDisplayMetricsFactory.create(globalModule, this.contextProvider);
        this.providePowerManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidePowerManagerFactory.create(this.contextProvider));
        this.provideAlarmManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideAlarmManagerFactory.create(this.contextProvider));
        this.provideIDreamManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIDreamManagerFactory.create());
        this.provideSensorPrivacyManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideSensorPrivacyManagerFactory.create(this.contextProvider));
        this.provideDevicePolicyManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideDevicePolicyManagerFactory.create(this.contextProvider));
        this.provideKeyguardManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideKeyguardManagerFactory.create(this.contextProvider));
        this.provideResourcesProvider = FrameworkServicesModule_ProvideResourcesFactory.create(this.contextProvider);
        this.buildInfoProvider = DoubleCheck.provider(BuildInfo_Factory.create());
        this.provideViewConfigurationProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideViewConfigurationFactory.create(this.contextProvider));
        this.provideAccessibilityManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideAccessibilityManagerFactory.create(this.contextProvider));
        this.providesSensorManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidesSensorManagerFactory.create(this.contextProvider));
        this.provideTrustManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideTrustManagerFactory.create(this.contextProvider));
        this.provideTelephonyManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideTelephonyManagerFactory.create(this.contextProvider));
        this.provideIActivityTaskManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIActivityTaskManagerFactory.create());
        this.opaEnabledSettingsProvider = DoubleCheck.provider(OpaEnabledSettings_Factory.create(this.contextProvider));
        this.provideIActivityManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIActivityManagerFactory.create());
        this.providePackageManagerWrapperProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidePackageManagerWrapperFactory.create());
        this.provideCrossWindowBlurListenersProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideCrossWindowBlurListenersFactory.create());
        this.provideWallpaperManagerProvider = FrameworkServicesModule_ProvideWallpaperManagerFactory.create(this.contextProvider);
        this.provideMediaSessionManagerProvider = FrameworkServicesModule_ProvideMediaSessionManagerFactory.create(this.contextProvider);
        this.provideMediaRouter2ManagerProvider = FrameworkServicesModule_ProvideMediaRouter2ManagerFactory.create(this.contextProvider);
        this.provideNotificationManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideNotificationManagerFactory.create(this.contextProvider));
        this.provideVibratorProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideVibratorFactory.create(this.contextProvider));
        this.provideDisplayManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideDisplayManagerFactory.create(this.contextProvider));
        this.provideIBatteryStatsProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIBatteryStatsFactory.create());
        this.provideIPackageManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIPackageManagerFactory.create());
        this.provideSmartspaceManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideSmartspaceManagerFactory.create(this.contextProvider));
        this.provideSubcriptionManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideSubcriptionManagerFactory.create(this.contextProvider));
        this.provideConnectivityManagagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideConnectivityManagagerFactory.create(this.contextProvider));
        this.provideWifiManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideWifiManagerFactory.create(this.contextProvider));
        this.provideNetworkScoreManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideNetworkScoreManagerFactory.create(this.contextProvider));
        this.provideShortcutManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideShortcutManagerFactory.create(this.contextProvider));
        this.provideOptionalVibratorProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideOptionalVibratorFactory.create(this.contextProvider));
        this.provideIAudioServiceProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIAudioServiceFactory.create());
        this.provideTelecomManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideTelecomManagerFactory.create(this.contextProvider));
        this.provideDisplayIdProvider = FrameworkServicesModule_ProvideDisplayIdFactory.create(this.contextProvider);
        this.provideActivityManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideActivityManagerFactory.create(this.contextProvider));
        this.provideStatsManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideStatsManagerFactory.create(this.contextProvider));
        this.provideOverlayManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideOverlayManagerFactory.create(this.contextProvider));
        this.provideColorDisplayManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideColorDisplayManagerFactory.create(this.contextProvider));
        this.provideLatencyTrackerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideLatencyTrackerFactory.create(this.contextProvider));
        this.provideInputMethodManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideInputMethodManagerFactory.create(this.contextProvider));
        this.qSExpansionPathInterpolatorProvider = DoubleCheck.provider(QSExpansionPathInterpolator_Factory.create());
        this.providePermissionManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidePermissionManagerFactory.create(this.contextProvider));
    }

    @Override // com.android.systemui.dagger.GlobalRootComponent
    public WMComponent.Builder getWMComponentBuilder() {
        return new WMComponentBuilder();
    }

    @Override // com.android.systemui.dagger.GlobalRootComponent
    public ThreadFactory createThreadFactory() {
        return ThreadFactoryImpl_Factory.newInstance();
    }

    @Override // com.android.systemui.dagger.GlobalRootComponent
    public SysUIGoogleSysUIComponent.Builder getSysUIComponent() {
        return new SysUIGoogleSysUIComponentBuilder();
    }

    /* access modifiers changed from: private */
    public static <T> Provider<Optional<T>> absentJdkOptionalProvider() {
        return ABSENT_JDK_OPTIONAL_PROVIDER;
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class PresentJdkOptionalInstanceProvider<T> implements Provider<Optional<T>> {
        private final Provider<T> delegate;

        private PresentJdkOptionalInstanceProvider(Provider<T> provider) {
            this.delegate = (Provider) Preconditions.checkNotNull(provider);
        }

        @Override // javax.inject.Provider
        public Optional<T> get() {
            return Optional.of(this.delegate.get());
        }

        /* access modifiers changed from: private */
        public static <T> Provider<Optional<T>> of(Provider<T> provider) {
            return new PresentJdkOptionalInstanceProvider(provider);
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class PresentJdkOptionalLazyProvider<T> implements Provider<Optional<Lazy<T>>> {
        private final Provider<T> delegate;

        private PresentJdkOptionalLazyProvider(Provider<T> provider) {
            this.delegate = (Provider) Preconditions.checkNotNull(provider);
        }

        @Override // javax.inject.Provider
        public Optional<Lazy<T>> get() {
            return Optional.of(DoubleCheck.lazy(this.delegate));
        }

        /* access modifiers changed from: private */
        public static <T> Provider<Optional<Lazy<T>>> of(Provider<T> provider) {
            return new PresentJdkOptionalLazyProvider(provider);
        }
    }

    /* loaded from: classes2.dex */
    private static final class Builder implements SysUIGoogleGlobalRootComponent.Builder {
        private Context context;

        private Builder() {
        }

        @Override // com.android.systemui.dagger.GlobalRootComponent.Builder
        public Builder context(Context context) {
            this.context = (Context) Preconditions.checkNotNull(context);
            return this;
        }

        @Override // com.android.systemui.dagger.GlobalRootComponent.Builder
        public SysUIGoogleGlobalRootComponent build() {
            Preconditions.checkBuilderRequirement(this.context, Context.class);
            return new DaggerSysUIGoogleGlobalRootComponent(new GlobalModule(), this.context);
        }
    }

    /* loaded from: classes2.dex */
    private final class WMComponentBuilder implements WMComponent.Builder {
        private WMComponentBuilder() {
        }

        @Override // com.android.systemui.dagger.WMComponent.Builder
        public WMComponent build() {
            return new WMComponentImpl();
        }
    }

    /* loaded from: classes2.dex */
    private final class WMComponentImpl implements WMComponent {
        private Provider<Optional<AppPairsController>> optionalOfAppPairsControllerProvider;
        private Provider<Optional<LegacySplitScreenController>> optionalOfLegacySplitScreenControllerProvider;
        private Provider<Optional<PipTouchHandler>> optionalOfPipTouchHandlerProvider;
        private Provider<AppPairsController> provideAppPairsProvider;
        private Provider<Optional<AppPairs>> provideAppPairsProvider2;
        private Provider<Optional<BubbleController>> provideBubbleControllerProvider;
        private Provider<Optional<Bubbles>> provideBubblesProvider;
        private Provider<DisplayController> provideDisplayControllerProvider;
        private Provider<DisplayImeController> provideDisplayImeControllerProvider;
        private Provider<DisplayLayout> provideDisplayLayoutProvider;
        private Provider<DragAndDropController> provideDragAndDropControllerProvider;
        private Provider<FloatingContentCoordinator> provideFloatingContentCoordinatorProvider;
        private Provider<FullscreenTaskListener> provideFullscreenTaskListenerProvider;
        private Provider<Optional<HideDisplayCutoutController>> provideHideDisplayCutoutControllerProvider;
        private Provider<Optional<HideDisplayCutout>> provideHideDisplayCutoutProvider;
        private Provider<LegacySplitScreenController> provideLegacySplitScreenProvider;
        private Provider<Optional<LegacySplitScreen>> provideLegacySplitScreenProvider2;
        private Provider<Optional<OneHandedController>> provideOneHandedControllerProvider;
        private Provider<Optional<OneHanded>> provideOneHandedProvider;
        private Provider<PipAnimationController> providePipAnimationControllerProvider;
        private Provider<PipAppOpsListener> providePipAppOpsListenerProvider;
        private Provider<PipBoundsState> providePipBoundsStateProvider;
        private Provider<PipMediaController> providePipMediaControllerProvider;
        private Provider<PipMotionHelper> providePipMotionHelperProvider;
        private Provider<Optional<Pip>> providePipProvider;
        private Provider<PipSnapAlgorithm> providePipSnapAlgorithmProvider;
        private Provider<PipSurfaceTransactionHelper> providePipSurfaceTransactionHelperProvider;
        private Provider<PipTaskOrganizer> providePipTaskOrganizerProvider;
        private Provider<PipTouchHandler> providePipTouchHandlerProvider;
        private Provider<PipTransitionController> providePipTransitionControllerProvider;
        private Provider<PipUiEventLogger> providePipUiEventLoggerProvider;
        private Provider<ShellTransitions> provideRemoteTransitionsProvider;
        private Provider<RootTaskDisplayAreaOrganizer> provideRootTaskDisplayAreaOrganizerProvider;
        private Provider<ShellExecutor> provideShellAnimationExecutorProvider;
        private Provider<ShellCommandHandlerImpl> provideShellCommandHandlerImplProvider;
        private Provider<Optional<ShellCommandHandler>> provideShellCommandHandlerProvider;
        private Provider<ShellInitImpl> provideShellInitImplProvider;
        private Provider<ShellInit> provideShellInitProvider;
        private Provider<ShellExecutor> provideShellMainExecutorProvider;
        private Provider<AnimationHandler> provideShellMainExecutorSfVsyncAnimationHandlerProvider;
        private Provider<Handler> provideShellMainHandlerProvider;
        private Provider<ShellTaskOrganizer> provideShellTaskOrganizerProvider;
        private Provider<SizeCompatUIController> provideSizeCompatUIControllerProvider;
        private Provider<ShellExecutor> provideSplashScreenExecutorProvider;
        private Provider<Optional<SplitScreenController>> provideSplitScreenControllerProvider;
        private Provider<Optional<SplitScreen>> provideSplitScreenProvider;
        private Provider<Optional<StartingSurface>> provideStartingSurfaceProvider;
        private Provider<StartingWindowController> provideStartingWindowControllerProvider;
        private Provider<StartingWindowTypeAlgorithm> provideStartingWindowTypeAlgorithmProvider;
        private Provider<SyncTransactionQueue> provideSyncTransactionQueueProvider;
        private Provider<ShellExecutor> provideSysUIMainExecutorProvider;
        private Provider<SystemWindows> provideSystemWindowsProvider;
        private Provider<Optional<TaskSurfaceHelperController>> provideTaskSurfaceHelperControllerProvider;
        private Provider<Optional<TaskSurfaceHelper>> provideTaskSurfaceHelperProvider;
        private Provider<TaskViewFactoryController> provideTaskViewFactoryControllerProvider;
        private Provider<Optional<TaskViewFactory>> provideTaskViewFactoryProvider;
        private Provider<TransactionPool> provideTransactionPoolProvider;
        private Provider<Transitions> provideTransitionsProvider;
        private Provider<WindowManagerShellWrapper> provideWindowManagerShellWrapperProvider;
        private Provider<TaskStackListenerImpl> providerTaskStackListenerImplProvider;
        private Provider<PipBoundsAlgorithm> providesPipBoundsAlgorithmProvider;
        private Provider<PhonePipMenuController> providesPipPhoneMenuControllerProvider;

        private WMComponentImpl() {
            initialize();
        }

        private void initialize() {
            this.provideShellMainHandlerProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideShellMainHandlerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider));
            this.provideSysUIMainExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideSysUIMainExecutorFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider));
            this.provideShellMainExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideShellMainExecutorFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideShellMainHandlerProvider, this.provideSysUIMainExecutorProvider));
            this.provideDisplayControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDisplayControllerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIWindowManagerProvider, this.provideShellMainExecutorProvider));
            this.provideTransactionPoolProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTransactionPoolFactory.create());
            this.provideDisplayImeControllerProvider = DoubleCheck.provider(WMShellModule_ProvideDisplayImeControllerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideIWindowManagerProvider, this.provideDisplayControllerProvider, this.provideShellMainExecutorProvider, this.provideTransactionPoolProvider));
            this.provideDragAndDropControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDragAndDropControllerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideDisplayControllerProvider));
            this.provideSyncTransactionQueueProvider = DoubleCheck.provider(WMShellBaseModule_ProvideSyncTransactionQueueFactory.create(this.provideTransactionPoolProvider, this.provideShellMainExecutorProvider));
            this.provideSizeCompatUIControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideSizeCompatUIControllerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideDisplayControllerProvider, this.provideDisplayImeControllerProvider, this.provideSyncTransactionQueueProvider));
            this.provideShellTaskOrganizerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideShellTaskOrganizerFactory.create(this.provideShellMainExecutorProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideSizeCompatUIControllerProvider));
            this.provideFloatingContentCoordinatorProvider = DoubleCheck.provider(WMShellBaseModule_ProvideFloatingContentCoordinatorFactory.create());
            this.provideWindowManagerShellWrapperProvider = DoubleCheck.provider(WMShellBaseModule_ProvideWindowManagerShellWrapperFactory.create(this.provideShellMainExecutorProvider));
            this.providerTaskStackListenerImplProvider = DoubleCheck.provider(WMShellBaseModule_ProviderTaskStackListenerImplFactory.create(this.provideShellMainHandlerProvider));
            this.provideBubbleControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideBubbleControllerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideFloatingContentCoordinatorProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIStatusBarServiceProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideWindowManagerProvider, this.provideWindowManagerShellWrapperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideLauncherAppsProvider, this.providerTaskStackListenerImplProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider, this.provideShellTaskOrganizerProvider, this.provideDisplayControllerProvider, this.provideShellMainExecutorProvider, this.provideShellMainHandlerProvider));
            this.provideSystemWindowsProvider = DoubleCheck.provider(WMShellBaseModule_ProvideSystemWindowsFactory.create(this.provideDisplayControllerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIWindowManagerProvider));
            this.provideShellAnimationExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory.create());
            this.provideTransitionsProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTransitionsFactory.create(this.provideShellTaskOrganizerProvider, this.provideTransactionPoolProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideShellMainExecutorProvider, this.provideShellAnimationExecutorProvider));
            this.provideShellMainExecutorSfVsyncAnimationHandlerProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideShellMainExecutorSfVsyncAnimationHandlerFactory.create(this.provideShellMainExecutorProvider));
            Provider<LegacySplitScreenController> provider = DoubleCheck.provider(WMShellModule_ProvideLegacySplitScreenFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideDisplayControllerProvider, this.provideSystemWindowsProvider, this.provideDisplayImeControllerProvider, this.provideTransactionPoolProvider, this.provideShellTaskOrganizerProvider, this.provideSyncTransactionQueueProvider, this.providerTaskStackListenerImplProvider, this.provideTransitionsProvider, this.provideShellMainExecutorProvider, this.provideShellMainExecutorSfVsyncAnimationHandlerProvider));
            this.provideLegacySplitScreenProvider = provider;
            this.optionalOfLegacySplitScreenControllerProvider = PresentJdkOptionalInstanceProvider.of(provider);
            this.provideRootTaskDisplayAreaOrganizerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideRootTaskDisplayAreaOrganizerFactory.create(this.provideShellMainExecutorProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.provideSplitScreenControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideSplitScreenControllerFactory.create(this.provideShellTaskOrganizerProvider, this.provideSyncTransactionQueueProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideRootTaskDisplayAreaOrganizerProvider, this.provideShellMainExecutorProvider, this.provideDisplayImeControllerProvider, this.provideTransitionsProvider, this.provideTransactionPoolProvider));
            Provider<AppPairsController> provider2 = DoubleCheck.provider(WMShellModule_ProvideAppPairsFactory.create(this.provideShellTaskOrganizerProvider, this.provideSyncTransactionQueueProvider, this.provideDisplayControllerProvider, this.provideShellMainExecutorProvider, this.provideDisplayImeControllerProvider));
            this.provideAppPairsProvider = provider2;
            this.optionalOfAppPairsControllerProvider = PresentJdkOptionalInstanceProvider.of(provider2);
            this.providePipBoundsStateProvider = DoubleCheck.provider(WMShellModule_ProvidePipBoundsStateFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.providePipMediaControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvidePipMediaControllerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideShellMainHandlerProvider));
            this.providesPipPhoneMenuControllerProvider = DoubleCheck.provider(WMShellModule_ProvidesPipPhoneMenuControllerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.providePipBoundsStateProvider, this.providePipMediaControllerProvider, this.provideSystemWindowsProvider, this.provideShellMainExecutorProvider, this.provideShellMainHandlerProvider));
            this.providePipSnapAlgorithmProvider = DoubleCheck.provider(WMShellModule_ProvidePipSnapAlgorithmFactory.create());
            this.providesPipBoundsAlgorithmProvider = DoubleCheck.provider(WMShellModule_ProvidesPipBoundsAlgorithmFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.providePipBoundsStateProvider, this.providePipSnapAlgorithmProvider));
            Provider<PipSurfaceTransactionHelper> provider3 = DoubleCheck.provider(WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory.create());
            this.providePipSurfaceTransactionHelperProvider = provider3;
            this.providePipAnimationControllerProvider = DoubleCheck.provider(WMShellModule_ProvidePipAnimationControllerFactory.create(provider3));
            this.providePipTransitionControllerProvider = DoubleCheck.provider(WMShellModule_ProvidePipTransitionControllerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideTransitionsProvider, this.provideShellTaskOrganizerProvider, this.providePipAnimationControllerProvider, this.providesPipBoundsAlgorithmProvider, this.providePipBoundsStateProvider, this.providesPipPhoneMenuControllerProvider));
            this.providePipUiEventLoggerProvider = DoubleCheck.provider(WMShellBaseModule_ProvidePipUiEventLoggerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider, DaggerSysUIGoogleGlobalRootComponent.this.providePackageManagerProvider));
            this.providePipTaskOrganizerProvider = DoubleCheck.provider(WMShellModule_ProvidePipTaskOrganizerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideSyncTransactionQueueProvider, this.providePipBoundsStateProvider, this.providesPipBoundsAlgorithmProvider, this.providesPipPhoneMenuControllerProvider, this.providePipAnimationControllerProvider, this.providePipSurfaceTransactionHelperProvider, this.providePipTransitionControllerProvider, this.optionalOfLegacySplitScreenControllerProvider, this.provideDisplayControllerProvider, this.providePipUiEventLoggerProvider, this.provideShellTaskOrganizerProvider, this.provideShellMainExecutorProvider));
            this.providePipMotionHelperProvider = DoubleCheck.provider(WMShellModule_ProvidePipMotionHelperFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.providePipBoundsStateProvider, this.providePipTaskOrganizerProvider, this.providesPipPhoneMenuControllerProvider, this.providePipSnapAlgorithmProvider, this.providePipTransitionControllerProvider, this.provideFloatingContentCoordinatorProvider));
            Provider<PipTouchHandler> provider4 = DoubleCheck.provider(WMShellModule_ProvidePipTouchHandlerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.providesPipPhoneMenuControllerProvider, this.providesPipBoundsAlgorithmProvider, this.providePipBoundsStateProvider, this.providePipTaskOrganizerProvider, this.providePipMotionHelperProvider, this.provideFloatingContentCoordinatorProvider, this.providePipUiEventLoggerProvider, this.provideShellMainExecutorProvider));
            this.providePipTouchHandlerProvider = provider4;
            this.optionalOfPipTouchHandlerProvider = PresentJdkOptionalInstanceProvider.of(provider4);
            this.provideFullscreenTaskListenerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideFullscreenTaskListenerFactory.create(this.provideSyncTransactionQueueProvider));
            this.provideSplashScreenExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory.create());
            this.provideStartingWindowTypeAlgorithmProvider = DoubleCheck.provider(WMShellModule_ProvideStartingWindowTypeAlgorithmFactory.create());
            Provider<StartingWindowController> provider5 = DoubleCheck.provider(WMShellBaseModule_ProvideStartingWindowControllerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideSplashScreenExecutorProvider, this.provideStartingWindowTypeAlgorithmProvider, this.provideTransactionPoolProvider));
            this.provideStartingWindowControllerProvider = provider5;
            Provider<ShellInitImpl> provider6 = DoubleCheck.provider(WMShellBaseModule_ProvideShellInitImplFactory.create(this.provideDisplayImeControllerProvider, this.provideDragAndDropControllerProvider, this.provideShellTaskOrganizerProvider, this.provideBubbleControllerProvider, this.optionalOfLegacySplitScreenControllerProvider, this.provideSplitScreenControllerProvider, this.optionalOfAppPairsControllerProvider, this.optionalOfPipTouchHandlerProvider, this.provideFullscreenTaskListenerProvider, this.provideTransitionsProvider, provider5, this.provideShellMainExecutorProvider));
            this.provideShellInitImplProvider = provider6;
            this.provideShellInitProvider = DoubleCheck.provider(WMShellBaseModule_ProvideShellInitFactory.create(provider6));
            this.providePipAppOpsListenerProvider = DoubleCheck.provider(WMShellBaseModule_ProvidePipAppOpsListenerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.providePipTouchHandlerProvider, this.provideShellMainExecutorProvider));
            this.provideDisplayLayoutProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDisplayLayoutFactory.create());
            this.provideOneHandedControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideOneHandedControllerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideWindowManagerProvider, this.provideDisplayControllerProvider, this.provideDisplayLayoutProvider, this.providerTaskStackListenerImplProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider, this.provideShellMainExecutorProvider, this.provideShellMainHandlerProvider));
            this.providePipProvider = DoubleCheck.provider(WMShellModule_ProvidePipFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideDisplayControllerProvider, this.providePipAppOpsListenerProvider, this.providesPipBoundsAlgorithmProvider, this.providePipBoundsStateProvider, this.providePipMediaControllerProvider, this.providesPipPhoneMenuControllerProvider, this.providePipTaskOrganizerProvider, this.providePipTouchHandlerProvider, this.providePipTransitionControllerProvider, this.provideWindowManagerShellWrapperProvider, this.providerTaskStackListenerImplProvider, this.provideOneHandedControllerProvider, this.provideShellMainExecutorProvider));
            Provider<Optional<HideDisplayCutoutController>> provider7 = DoubleCheck.provider(WMShellBaseModule_ProvideHideDisplayCutoutControllerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideDisplayControllerProvider, this.provideShellMainExecutorProvider));
            this.provideHideDisplayCutoutControllerProvider = provider7;
            Provider<ShellCommandHandlerImpl> provider8 = DoubleCheck.provider(WMShellBaseModule_ProvideShellCommandHandlerImplFactory.create(this.provideShellTaskOrganizerProvider, this.optionalOfLegacySplitScreenControllerProvider, this.provideSplitScreenControllerProvider, this.providePipProvider, this.provideOneHandedControllerProvider, provider7, this.optionalOfAppPairsControllerProvider, this.provideShellMainExecutorProvider));
            this.provideShellCommandHandlerImplProvider = provider8;
            this.provideShellCommandHandlerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideShellCommandHandlerFactory.create(provider8));
            this.provideOneHandedProvider = DoubleCheck.provider(WMShellBaseModule_ProvideOneHandedFactory.create(this.provideOneHandedControllerProvider));
            this.provideLegacySplitScreenProvider2 = DoubleCheck.provider(WMShellBaseModule_ProvideLegacySplitScreenFactory.create(this.optionalOfLegacySplitScreenControllerProvider));
            this.provideSplitScreenProvider = DoubleCheck.provider(WMShellBaseModule_ProvideSplitScreenFactory.create(this.provideSplitScreenControllerProvider));
            this.provideAppPairsProvider2 = DoubleCheck.provider(WMShellBaseModule_ProvideAppPairsFactory.create(this.optionalOfAppPairsControllerProvider));
            this.provideBubblesProvider = DoubleCheck.provider(WMShellBaseModule_ProvideBubblesFactory.create(this.provideBubbleControllerProvider));
            this.provideHideDisplayCutoutProvider = DoubleCheck.provider(WMShellBaseModule_ProvideHideDisplayCutoutFactory.create(this.provideHideDisplayCutoutControllerProvider));
            Provider<TaskViewFactoryController> provider9 = DoubleCheck.provider(WMShellBaseModule_ProvideTaskViewFactoryControllerFactory.create(this.provideShellTaskOrganizerProvider, this.provideShellMainExecutorProvider));
            this.provideTaskViewFactoryControllerProvider = provider9;
            this.provideTaskViewFactoryProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTaskViewFactoryFactory.create(provider9));
            this.provideRemoteTransitionsProvider = DoubleCheck.provider(WMShellBaseModule_ProvideRemoteTransitionsFactory.create(this.provideTransitionsProvider));
            this.provideStartingSurfaceProvider = DoubleCheck.provider(WMShellBaseModule_ProvideStartingSurfaceFactory.create(this.provideStartingWindowControllerProvider));
            Provider<Optional<TaskSurfaceHelperController>> provider10 = DoubleCheck.provider(WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory.create(this.provideShellTaskOrganizerProvider, this.provideShellMainExecutorProvider));
            this.provideTaskSurfaceHelperControllerProvider = provider10;
            this.provideTaskSurfaceHelperProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTaskSurfaceHelperFactory.create(provider10));
        }

        @Override // com.android.systemui.dagger.WMComponent
        public ShellInit getShellInit() {
            return this.provideShellInitProvider.get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public Optional<ShellCommandHandler> getShellCommandHandler() {
            return this.provideShellCommandHandlerProvider.get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public Optional<OneHanded> getOneHanded() {
            return this.provideOneHandedProvider.get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public Optional<Pip> getPip() {
            return this.providePipProvider.get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public Optional<LegacySplitScreen> getLegacySplitScreen() {
            return this.provideLegacySplitScreenProvider2.get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public Optional<SplitScreen> getSplitScreen() {
            return this.provideSplitScreenProvider.get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public Optional<AppPairs> getAppPairs() {
            return this.provideAppPairsProvider2.get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public Optional<Bubbles> getBubbles() {
            return this.provideBubblesProvider.get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public Optional<HideDisplayCutout> getHideDisplayCutout() {
            return this.provideHideDisplayCutoutProvider.get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public Optional<TaskViewFactory> getTaskViewFactory() {
            return this.provideTaskViewFactoryProvider.get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public ShellTransitions getTransitions() {
            return this.provideRemoteTransitionsProvider.get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public Optional<StartingSurface> getStartingSurface() {
            return this.provideStartingSurfaceProvider.get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public Optional<TaskSurfaceHelper> getTaskSurfaceHelper() {
            return this.provideTaskSurfaceHelperProvider.get();
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public final class SysUIGoogleSysUIComponentBuilder implements SysUIGoogleSysUIComponent.Builder {
        private Optional<AppPairs> setAppPairs;
        private Optional<Bubbles> setBubbles;
        private Optional<HideDisplayCutout> setHideDisplayCutout;
        private Optional<LegacySplitScreen> setLegacySplitScreen;
        private Optional<OneHanded> setOneHanded;
        private Optional<Pip> setPip;
        private Optional<ShellCommandHandler> setShellCommandHandler;
        private Optional<SplitScreen> setSplitScreen;
        private Optional<StartingSurface> setStartingSurface;
        private Optional<TaskSurfaceHelper> setTaskSurfaceHelper;
        private Optional<TaskViewFactory> setTaskViewFactory;
        private ShellTransitions setTransitions;

        private SysUIGoogleSysUIComponentBuilder() {
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public SysUIGoogleSysUIComponentBuilder setPip(Optional<Pip> optional) {
            this.setPip = (Optional) Preconditions.checkNotNull(optional);
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public SysUIGoogleSysUIComponentBuilder setLegacySplitScreen(Optional<LegacySplitScreen> optional) {
            this.setLegacySplitScreen = (Optional) Preconditions.checkNotNull(optional);
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public SysUIGoogleSysUIComponentBuilder setSplitScreen(Optional<SplitScreen> optional) {
            this.setSplitScreen = (Optional) Preconditions.checkNotNull(optional);
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public SysUIGoogleSysUIComponentBuilder setAppPairs(Optional<AppPairs> optional) {
            this.setAppPairs = (Optional) Preconditions.checkNotNull(optional);
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public SysUIGoogleSysUIComponentBuilder setOneHanded(Optional<OneHanded> optional) {
            this.setOneHanded = (Optional) Preconditions.checkNotNull(optional);
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public SysUIGoogleSysUIComponentBuilder setBubbles(Optional<Bubbles> optional) {
            this.setBubbles = (Optional) Preconditions.checkNotNull(optional);
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public SysUIGoogleSysUIComponentBuilder setTaskViewFactory(Optional<TaskViewFactory> optional) {
            this.setTaskViewFactory = (Optional) Preconditions.checkNotNull(optional);
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public SysUIGoogleSysUIComponentBuilder setHideDisplayCutout(Optional<HideDisplayCutout> optional) {
            this.setHideDisplayCutout = (Optional) Preconditions.checkNotNull(optional);
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public SysUIGoogleSysUIComponentBuilder setShellCommandHandler(Optional<ShellCommandHandler> optional) {
            this.setShellCommandHandler = (Optional) Preconditions.checkNotNull(optional);
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public SysUIGoogleSysUIComponentBuilder setTransitions(ShellTransitions shellTransitions) {
            this.setTransitions = (ShellTransitions) Preconditions.checkNotNull(shellTransitions);
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public SysUIGoogleSysUIComponentBuilder setStartingSurface(Optional<StartingSurface> optional) {
            this.setStartingSurface = (Optional) Preconditions.checkNotNull(optional);
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public SysUIGoogleSysUIComponentBuilder setTaskSurfaceHelper(Optional<TaskSurfaceHelper> optional) {
            this.setTaskSurfaceHelper = (Optional) Preconditions.checkNotNull(optional);
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public SysUIGoogleSysUIComponent build() {
            Preconditions.checkBuilderRequirement(this.setPip, Optional.class);
            Preconditions.checkBuilderRequirement(this.setLegacySplitScreen, Optional.class);
            Preconditions.checkBuilderRequirement(this.setSplitScreen, Optional.class);
            Preconditions.checkBuilderRequirement(this.setAppPairs, Optional.class);
            Preconditions.checkBuilderRequirement(this.setOneHanded, Optional.class);
            Preconditions.checkBuilderRequirement(this.setBubbles, Optional.class);
            Preconditions.checkBuilderRequirement(this.setTaskViewFactory, Optional.class);
            Preconditions.checkBuilderRequirement(this.setHideDisplayCutout, Optional.class);
            Preconditions.checkBuilderRequirement(this.setShellCommandHandler, Optional.class);
            Preconditions.checkBuilderRequirement(this.setTransitions, ShellTransitions.class);
            Preconditions.checkBuilderRequirement(this.setStartingSurface, Optional.class);
            Preconditions.checkBuilderRequirement(this.setTaskSurfaceHelper, Optional.class);
            return new SysUIGoogleSysUIComponentImpl(new DependencyProvider(), new NightDisplayListenerModule(), new UserModule(), this.setPip, this.setLegacySplitScreen, this.setSplitScreen, this.setAppPairs, this.setOneHanded, this.setBubbles, this.setTaskViewFactory, this.setHideDisplayCutout, this.setShellCommandHandler, this.setTransitions, this.setStartingSurface, this.setTaskSurfaceHelper);
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public final class SysUIGoogleSysUIComponentImpl implements SysUIGoogleSysUIComponent {
        private Provider<AccessibilityButtonModeObserver> accessibilityButtonModeObserverProvider;
        private Provider<AccessibilityButtonTargetsObserver> accessibilityButtonTargetsObserverProvider;
        private Provider<AccessibilityController> accessibilityControllerProvider;
        private Provider<AccessibilityManagerWrapper> accessibilityManagerWrapperProvider;
        private Provider<ActionClickLogger> actionClickLoggerProvider;
        private Provider<ActionProxyReceiver> actionProxyReceiverProvider;
        private Provider<ActivityIntentHelper> activityIntentHelperProvider;
        private Provider<ActivityStarterDelegate> activityStarterDelegateProvider;
        private Provider<UserDetailView.Adapter> adapterProvider;
        private Provider<AirplaneModeTile> airplaneModeTileProvider;
        private Provider<AlarmTile> alarmTileProvider;
        private Provider<AmbientState> ambientStateProvider;
        private Provider<AnimatedImageNotificationManager> animatedImageNotificationManagerProvider;
        private Provider<AppOpsControllerImpl> appOpsControllerImplProvider;
        private Provider<AppOpsCoordinator> appOpsCoordinatorProvider;
        private Provider<AssistInvocationEffect> assistInvocationEffectProvider;
        private Provider<com.google.android.systemui.columbus.feedback.AssistInvocationEffect> assistInvocationEffectProvider2;
        private Provider<AssistManagerGoogle> assistManagerGoogleProvider;
        private Provider<AssistantFeedbackController> assistantFeedbackControllerProvider;
        private Provider<AssistantPresenceHandler> assistantPresenceHandlerProvider;
        private Provider<AssistantWarmer> assistantWarmerProvider;
        private Provider<AsyncSensorManager> asyncSensorManagerProvider;
        private Provider<AuthController> authControllerProvider;
        private Provider<AutorotateDataService> autorotateDataServiceProvider;
        private Provider<BatterySaverTileGoogle> batterySaverTileGoogleProvider;
        private Provider<BatteryStateNotifier> batteryStateNotifierProvider;
        private Provider<Set<NgaMessageHandler.EdgeLightsInfoListener>> bindEdgeLightsInfoListenersProvider;
        private Provider<SystemClock> bindSystemClockProvider;
        private Provider<BiometricUnlockController> biometricUnlockControllerProvider;
        private Provider<BluetoothControllerImpl> bluetoothControllerImplProvider;
        private Provider<BluetoothTile> bluetoothTileProvider;
        private Provider<BlurUtils> blurUtilsProvider;
        private Provider<BootCompleteCacheImpl> bootCompleteCacheImplProvider;
        private Provider<BrightLineFalsingManager> brightLineFalsingManagerProvider;
        private Provider<BrightnessDialog> brightnessDialogProvider;
        private Provider<BroadcastDispatcherLogger> broadcastDispatcherLoggerProvider;
        private Provider<BubbleCoordinator> bubbleCoordinatorProvider;
        private Provider builderProvider;
        private Provider<CustomTile.Builder> builderProvider10;
        private Provider<NightDisplayListenerModule.Builder> builderProvider11;
        private Provider<AutoAddTracker.Builder> builderProvider12;
        private Provider<DelayedWakeLock.Builder> builderProvider2;
        private Provider<WakeLock.Builder> builderProvider3;
        private Provider<NotificationClicker.Builder> builderProvider4;
        private Provider<StatusBarNotificationActivityStarter.Builder> builderProvider5;
        private Provider<LaunchOpa.Builder> builderProvider6;
        private Provider<SettingsAction.Builder> builderProvider7;
        private Provider<CameraAction.Builder> builderProvider8;
        private Provider<SetupWizardAction.Builder> builderProvider9;
        private Provider<BypassHeadsUpNotifier> bypassHeadsUpNotifierProvider;
        private Provider<CHREGestureSensor> cHREGestureSensorProvider;
        private Provider<CallbackHandler> callbackHandlerProvider;
        private Provider<CameraToggleTile> cameraToggleTileProvider;
        private Provider<CameraVisibility> cameraVisibilityProvider;
        private Provider<CarrierConfigTracker> carrierConfigTrackerProvider;
        private Provider<CastControllerImpl> castControllerImplProvider;
        private Provider<CastTile> castTileProvider;
        private Provider<CellularTile> cellularTileProvider;
        private Provider<ChannelEditorDialogController> channelEditorDialogControllerProvider;
        private Provider<ChargingState> chargingStateProvider;
        private Provider<ClockManager> clockManagerProvider;
        private Provider<ColorChangeHandler> colorChangeHandlerProvider;
        private Provider<ColorInversionTile> colorInversionTileProvider;
        private Provider<ColumbusService> columbusServiceProvider;
        private Provider<ColumbusServiceWrapper> columbusServiceWrapperProvider;
        private Provider<ColumbusSettings> columbusSettingsProvider;
        private Provider<CommandRegistry> commandRegistryProvider;
        private Provider<ConfigurationHandler> configurationHandlerProvider;
        private Provider<ContentResolverWrapper> contentResolverWrapperProvider;
        private Provider<ContextComponentResolver> contextComponentResolverProvider;
        private Provider<ControlActionCoordinatorImpl> controlActionCoordinatorImplProvider;
        private Provider<ControlsActivity> controlsActivityProvider;
        private Provider<ControlsBindingControllerImpl> controlsBindingControllerImplProvider;
        private Provider<ControlsComponent> controlsComponentProvider;
        private Provider<ControlsControllerImpl> controlsControllerImplProvider;
        private Provider<ControlsEditingActivity> controlsEditingActivityProvider;
        private Provider<ControlsFavoritingActivity> controlsFavoritingActivityProvider;
        private Provider<ControlsListingControllerImpl> controlsListingControllerImplProvider;
        private Provider<ControlsMetricsLoggerImpl> controlsMetricsLoggerImplProvider;
        private Provider<ControlsProviderSelectorActivity> controlsProviderSelectorActivityProvider;
        private Provider<ControlsRequestDialog> controlsRequestDialogProvider;
        private Provider<ControlsUiControllerImpl> controlsUiControllerImplProvider;
        private Provider<ConversationCoordinator> conversationCoordinatorProvider;
        private Provider<ConversationNotificationManager> conversationNotificationManagerProvider;
        private Provider<ConversationNotificationProcessor> conversationNotificationProcessorProvider;
        private Provider<CreateUserActivity> createUserActivityProvider;
        private Provider<InjectionInflationController.ViewInstanceCreator.Factory> createViewInstanceCreatorFactoryProvider;
        private Provider<CustomIconCache> customIconCacheProvider;
        private Provider<CustomTileStatePersister> customTileStatePersisterProvider;
        private Provider<DarkIconDispatcherImpl> darkIconDispatcherImplProvider;
        private Provider dataLoggerProvider;
        private Provider<DataSaverTile> dataSaverTileProvider;
        private Provider<DateFormatUtil> dateFormatUtilProvider;
        private Provider<DebugNotificationVoiceReplyClient> debugNotificationVoiceReplyClientProvider;
        private Provider<DefaultUiController> defaultUiControllerProvider;
        private Provider<DeleteScreenshotReceiver> deleteScreenshotReceiverProvider;
        private Provider<Dependency> dependencyProvider2;
        private Provider<DeviceConfigProxy> deviceConfigProxyProvider;
        private Provider<DeviceControlsControllerImpl> deviceControlsControllerImplProvider;
        private Provider<DeviceControlsTile> deviceControlsTileProvider;
        private Provider<DeviceProvisionedControllerImpl> deviceProvisionedControllerImplProvider;
        private Provider<DeviceProvisionedCoordinator> deviceProvisionedCoordinatorProvider;
        private Provider diagonalClassifierProvider;
        private Provider<DismissCallbackRegistry> dismissCallbackRegistryProvider;
        private Provider<DismissTimer> dismissTimerProvider;
        private Provider distanceClassifierProvider;
        private Provider<DndTile> dndTileProvider;
        private Provider<DoubleTapClassifier> doubleTapClassifierProvider;
        private Provider<DozeComponent.Builder> dozeComponentBuilderProvider;
        private Provider<DozeLog> dozeLogProvider;
        private Provider<DozeLogger> dozeLoggerProvider;
        private Provider<DozeParameters> dozeParametersProvider;
        private Provider<DozeScrimController> dozeScrimControllerProvider;
        private Provider<DozeServiceHost> dozeServiceHostProvider;
        private Provider<DozeService> dozeServiceProvider;
        private Provider<DumpHandler> dumpHandlerProvider;
        private Provider<DumpManager> dumpManagerProvider;
        private Provider<DynamicChildBindController> dynamicChildBindControllerProvider;
        private Provider<DynamicPrivacyController> dynamicPrivacyControllerProvider;
        private Provider<EdgeLightsController> edgeLightsControllerProvider;
        private Provider<EnhancedEstimatesGoogleImpl> enhancedEstimatesGoogleImplProvider;
        private Provider<EntryPointController> entryPointControllerProvider;
        private Provider<ExpandableNotificationRowComponent.Builder> expandableNotificationRowComponentBuilderProvider;
        private Provider<NotificationLogger.ExpansionStateLogger> expansionStateLoggerProvider;
        private Provider<ExtensionControllerImpl> extensionControllerImplProvider;
        private Provider<KeyguardBouncer.Factory> factoryProvider;
        private Provider<KeyguardMessageAreaController.Factory> factoryProvider2;
        private Provider<BrightnessSlider.Factory> factoryProvider3;
        private Provider<ColumbusContentObserver.Factory> factoryProvider4;
        private Provider<EdgeBackGestureHandler.Factory> factoryProvider5;
        private Provider falsingCollectorImplProvider;
        private Provider<FalsingDataProvider> falsingDataProvider;
        private Provider<FalsingManagerProxy> falsingManagerProxyProvider;
        private Provider<FeatureFlagReader> featureFlagReaderProvider;
        private Provider<FeatureFlags> featureFlagsProvider;
        private Provider<Files> filesProvider;
        private Provider<FlagEnabled> flagEnabledProvider;
        private Provider<FlashlightControllerImpl> flashlightControllerImplProvider;
        private Provider<FlashlightTile> flashlightTileProvider;
        private Provider flingVelocityWrapperProvider;
        private Provider<ForegroundServiceController> foregroundServiceControllerProvider;
        private Provider<ForegroundServiceDismissalFeatureController> foregroundServiceDismissalFeatureControllerProvider;
        private Provider<ForegroundServiceNotificationListener> foregroundServiceNotificationListenerProvider;
        private Provider<ForegroundServiceSectionController> foregroundServiceSectionControllerProvider;
        private Provider<FpsController> fpsControllerProvider;
        private Provider<FragmentService.FragmentCreator.Factory> fragmentCreatorFactoryProvider;
        private Provider<FragmentService> fragmentServiceProvider;
        private Provider<GameDashboardUiEventLogger> gameDashboardUiEventLoggerProvider;
        private Provider<GameMenuActivity> gameMenuActivityProvider;
        private Provider<GameModeDndController> gameModeDndControllerProvider;
        private Provider<GarbageMonitor> garbageMonitorProvider;
        private Provider<GestureConfiguration> gestureConfigurationProvider;
        private Provider<GestureController> gestureControllerProvider;
        private Provider<GestureSensorImpl> gestureSensorImplProvider;
        private Provider<GlobalActionsComponent> globalActionsComponentProvider;
        private Provider<GlobalActionsDialogLite> globalActionsDialogLiteProvider;
        private Provider<GlobalActionsImpl> globalActionsImplProvider;
        private Provider<GlobalActionsInfoProvider> globalActionsInfoProvider;
        private Provider globalSettingsImplProvider;
        private Provider<GlowController> glowControllerProvider;
        private Provider goBackHandlerProvider;
        private Provider<GoogleAssistLogger> googleAssistLoggerProvider;
        private Provider<GoogleDefaultUiController> googleDefaultUiControllerProvider;
        private Provider<GoogleServices> googleServicesProvider;
        private Provider<GroupCoalescerLogger> groupCoalescerLoggerProvider;
        private Provider<GroupCoalescer> groupCoalescerProvider;
        private Provider<HapticClick> hapticClickProvider;
        private Provider<HeadsUpController> headsUpControllerProvider;
        private Provider<HeadsUpCoordinator> headsUpCoordinatorProvider;
        private Provider<HeadsUpViewBinder> headsUpViewBinderProvider;
        private Provider<HideNotifsForOtherUsersCoordinator> hideNotifsForOtherUsersCoordinatorProvider;
        private Provider<HighPriorityProvider> highPriorityProvider;
        private Provider<HistoryTracker> historyTrackerProvider;
        private Provider<HotspotControllerImpl> hotspotControllerImplProvider;
        private Provider<HotspotTile> hotspotTileProvider;
        private Provider<IconBuilder> iconBuilderProvider;
        private Provider<IconController> iconControllerProvider;
        private Provider<IconManager> iconManagerProvider;
        private Provider imageExporterProvider;
        private Provider imageTileSetProvider;
        private Provider<InitController> initControllerProvider;
        private Provider<InjectionInflationController> injectionInflationControllerProvider;
        private Provider<InstantAppNotifier> instantAppNotifierProvider;
        private Provider<InternetTile> internetTileProvider;
        private Provider<Boolean> isPMLiteEnabledProvider;
        private Provider<Boolean> isReduceBrightColorsAvailableProvider;
        private Provider keyboardMonitorProvider;
        private Provider<KeyguardBouncerComponent.Factory> keyguardBouncerComponentFactoryProvider;
        private Provider<KeyguardBypassController> keyguardBypassControllerProvider;
        private Provider<KeyguardCoordinator> keyguardCoordinatorProvider;
        private Provider<KeyguardDismissUtil> keyguardDismissUtilProvider;
        private Provider<KeyguardDisplayManager> keyguardDisplayManagerProvider;
        private Provider<KeyguardEnvironmentImpl> keyguardEnvironmentImplProvider;
        private Provider<KeyguardIndicationControllerGoogle> keyguardIndicationControllerGoogleProvider;
        private Provider<KeyguardLifecyclesDispatcher> keyguardLifecyclesDispatcherProvider;
        private Provider<KeyguardMediaController> keyguardMediaControllerProvider;
        private Provider<KeyguardMediaViewController> keyguardMediaViewControllerProvider;
        private Provider<KeyguardProximity> keyguardProximityProvider;
        private Provider<KeyguardQsUserSwitchComponent.Factory> keyguardQsUserSwitchComponentFactoryProvider;
        private Provider<KeyguardSecurityModel> keyguardSecurityModelProvider;
        private Provider<KeyguardService> keyguardServiceProvider;
        private Provider<KeyguardSmartspaceController> keyguardSmartspaceControllerProvider;
        private Provider<KeyguardStateControllerImpl> keyguardStateControllerImplProvider;
        private Provider<KeyguardStatusBarViewComponent.Factory> keyguardStatusBarViewComponentFactoryProvider;
        private Provider<KeyguardStatusViewComponent.Factory> keyguardStatusViewComponentFactoryProvider;
        private Provider<KeyguardUnlockAnimationController> keyguardUnlockAnimationControllerProvider;
        private Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
        private Provider<KeyguardUserSwitcherComponent.Factory> keyguardUserSwitcherComponentFactoryProvider;
        private Provider<KeyguardVisibility> keyguardVisibilityProvider;
        private Provider<KeyguardZenAlarmViewController> keyguardZenAlarmViewControllerProvider;
        private Provider<LatencyTester> latencyTesterProvider;
        private Provider<LaunchApp> launchAppProvider;
        private Provider<LaunchConversationActivity> launchConversationActivityProvider;
        private Provider<com.google.android.systemui.columbus.actions.LaunchOpa> launchOpaProvider;
        private Provider<LaunchOverview> launchOverviewProvider;
        private Provider<LeakReporter> leakReporterProvider;
        private Provider<LightBarController> lightBarControllerProvider;
        private Provider lightnessProvider;
        private Provider<LightsOutNotifController> lightsOutNotifControllerProvider;
        private Provider<LiveWallpaperScrimController> liveWallpaperScrimControllerProvider;
        private Provider<LocalMediaManagerFactory> localMediaManagerFactoryProvider;
        private Provider<LocationControllerImpl> locationControllerImplProvider;
        private Provider<LocationTile> locationTileProvider;
        private Provider<LockscreenGestureLogger> lockscreenGestureLoggerProvider;
        private Provider<LockscreenShadeTransitionController> lockscreenShadeTransitionControllerProvider;
        private Provider<LockscreenSmartspaceController> lockscreenSmartspaceControllerProvider;
        private Provider<LockscreenWallpaper> lockscreenWallpaperProvider;
        private Provider<LogBufferEulogizer> logBufferEulogizerProvider;
        private Provider<LogBufferFactory> logBufferFactoryProvider;
        private Provider<LogBufferFreezer> logBufferFreezerProvider;
        private Provider<LongScreenshotActivity> longScreenshotActivityProvider;
        private Provider<LongScreenshotData> longScreenshotDataProvider;
        private Provider<LowPriorityInflationHelper> lowPriorityInflationHelperProvider;
        private Provider<LowSensitivitySettingAdjustment> lowSensitivitySettingAdjustmentProvider;
        private Provider<ManageMedia> manageMediaProvider;
        private Provider<ManagedProfileControllerImpl> managedProfileControllerImplProvider;
        private Provider<Map<Class<?>, Provider<Activity>>> mapOfClassOfAndProviderOfActivityProvider;
        private Provider<Map<Class<?>, Provider<BroadcastReceiver>>> mapOfClassOfAndProviderOfBroadcastReceiverProvider;
        private Provider<Map<Class<?>, Provider<RecentsImplementation>>> mapOfClassOfAndProviderOfRecentsImplementationProvider;
        private Provider<Map<Class<?>, Provider<Service>>> mapOfClassOfAndProviderOfServiceProvider;
        private Provider<Map<Class<?>, Provider<SystemUI>>> mapOfClassOfAndProviderOfSystemUIProvider;
        private Provider<MediaArtworkProcessor> mediaArtworkProcessorProvider;
        private Provider<MediaBrowserFactory> mediaBrowserFactoryProvider;
        private Provider<MediaCarouselController> mediaCarouselControllerProvider;
        private Provider<MediaControlPanel> mediaControlPanelProvider;
        private Provider<MediaControllerFactory> mediaControllerFactoryProvider;
        private Provider<MediaCoordinator> mediaCoordinatorProvider;
        private Provider<MediaDataFilter> mediaDataFilterProvider;
        private Provider<MediaDataManager> mediaDataManagerProvider;
        private Provider<MediaDeviceManager> mediaDeviceManagerProvider;
        private Provider<MediaFeatureFlag> mediaFeatureFlagProvider;
        private Provider<MediaHierarchyManager> mediaHierarchyManagerProvider;
        private Provider<MediaHostStatesManager> mediaHostStatesManagerProvider;
        private Provider<MediaOutputDialogFactory> mediaOutputDialogFactoryProvider;
        private Provider<MediaOutputDialogReceiver> mediaOutputDialogReceiverProvider;
        private Provider<MediaResumeListener> mediaResumeListenerProvider;
        private Provider<MediaSessionBasedFilter> mediaSessionBasedFilterProvider;
        private Provider<MediaTimeoutListener> mediaTimeoutListenerProvider;
        private Provider<MediaViewController> mediaViewControllerProvider;
        private Provider<GarbageMonitor.MemoryTile> memoryTileProvider;
        private Provider<MicrophoneToggleTile> microphoneToggleTileProvider;
        private Provider<Set<Action>> namedSetOfActionProvider;
        private Provider<Set<FalsingClassifier>> namedSetOfFalsingClassifierProvider;
        private Provider<Set<FeedbackEffect>> namedSetOfFeedbackEffectProvider;
        private Provider<Set<FeedbackEffect>> namedSetOfFeedbackEffectProvider2;
        private Provider<Set<Gate>> namedSetOfGateProvider;
        private Provider<Set<Integer>> namedSetOfIntegerProvider;
        private Provider<NavBarFader> navBarFaderProvider;
        private Provider<NavigationModeController> navigationModeControllerProvider;
        private Provider<NetworkControllerImpl> networkControllerImplProvider;
        private Provider<KeyguardViewMediator> newKeyguardViewMediatorProvider;
        private Provider<NextAlarmControllerImpl> nextAlarmControllerImplProvider;
        private Provider<NfcTile> nfcTileProvider;
        private Provider<NgaInputHandler> ngaInputHandlerProvider;
        private Provider<NgaMessageHandler> ngaMessageHandlerProvider;
        private Provider<NgaUiController> ngaUiControllerProvider;
        private Provider<NightDisplayTile> nightDisplayTileProvider;
        private Provider<NotifBindPipelineInitializer> notifBindPipelineInitializerProvider;
        private Provider<NotifBindPipelineLogger> notifBindPipelineLoggerProvider;
        private Provider<NotifBindPipeline> notifBindPipelineProvider;
        private Provider<NotifCollectionLogger> notifCollectionLoggerProvider;
        private Provider<NotifCollection> notifCollectionProvider;
        private Provider<NotifCoordinators> notifCoordinatorsProvider;
        private Provider<NotifInflaterImpl> notifInflaterImplProvider;
        private Provider<NotifInflationErrorManager> notifInflationErrorManagerProvider;
        private Provider<NotifPipelineInitializer> notifPipelineInitializerProvider;
        private Provider<NotifPipeline> notifPipelineProvider;
        private Provider<NotifRemoteViewCacheImpl> notifRemoteViewCacheImplProvider;
        private Provider<NotifViewBarn> notifViewBarnProvider;
        private Provider<NotificationClickNotifier> notificationClickNotifierProvider;
        private Provider<NotificationClickerLogger> notificationClickerLoggerProvider;
        private Provider<NotificationContentInflater> notificationContentInflaterProvider;
        private Provider<NotificationEntryManagerLogger> notificationEntryManagerLoggerProvider;
        private Provider<NotificationFilter> notificationFilterProvider;
        private Provider<NotificationGroupManagerLegacy> notificationGroupManagerLegacyProvider;
        private Provider<NotificationIconAreaController> notificationIconAreaControllerProvider;
        private Provider<NotificationInteractionTracker> notificationInteractionTrackerProvider;
        private Provider<NotificationInterruptStateProviderImpl> notificationInterruptStateProviderImplProvider;
        private Provider<NotificationLockscreenUserManagerGoogle> notificationLockscreenUserManagerGoogleProvider;
        private Provider<NotificationPersonExtractorPluginBoundary> notificationPersonExtractorPluginBoundaryProvider;
        private Provider<NotificationRankingManager> notificationRankingManagerProvider;
        private Provider<NotificationRoundnessManager> notificationRoundnessManagerProvider;
        private Provider<NotificationRowBinderImpl> notificationRowBinderImplProvider;
        private Provider<NotificationSectionsFeatureManager> notificationSectionsFeatureManagerProvider;
        private Provider<NotificationSectionsLogger> notificationSectionsLoggerProvider;
        private Provider<NotificationSectionsManager> notificationSectionsManagerProvider;
        private Provider<NotificationShadeDepthController> notificationShadeDepthControllerProvider;
        private Provider<NotificationShadeWindowControllerImpl> notificationShadeWindowControllerImplProvider;
        private Provider<NotificationShelfComponent.Builder> notificationShelfComponentBuilderProvider;
        private Provider<NotificationVoiceReplyController> notificationVoiceReplyControllerProvider;
        private Provider<NotificationVoiceReplyLogger> notificationVoiceReplyLoggerProvider;
        private Provider<NotificationVoiceReplyManagerService> notificationVoiceReplyManagerServiceProvider;
        private Provider<NotificationWakeUpCoordinator> notificationWakeUpCoordinatorProvider;
        private Provider<NotificationsControllerImpl> notificationsControllerImplProvider;
        private Provider<NotificationsControllerStub> notificationsControllerStubProvider;
        private Provider<OngoingCallLogger> ongoingCallLoggerProvider;
        private Provider<OpaEnabledDispatcher> opaEnabledDispatcherProvider;
        private Provider<OpaEnabledReceiver> opaEnabledReceiverProvider;
        private Provider<OpaHomeButton> opaHomeButtonProvider;
        private Provider<OpaLockscreen> opaLockscreenProvider;
        private Provider<OpenNotificationShade> openNotificationShadeProvider;
        private Provider<Optional<BcSmartspaceDataPlugin>> optionalOfBcSmartspaceDataPluginProvider;
        private Provider<Optional<CommandQueue>> optionalOfCommandQueueProvider;
        private Provider<Optional<ControlsFavoritePersistenceWrapper>> optionalOfControlsFavoritePersistenceWrapperProvider;
        private Provider<Optional<HeadsUpManager>> optionalOfHeadsUpManagerProvider;
        private Provider<Optional<Lazy<StatusBar>>> optionalOfLazyOfStatusBarProvider;
        private Provider<Optional<Recents>> optionalOfRecentsProvider;
        private Provider<Optional<StatusBar>> optionalOfStatusBarProvider;
        private Provider<Optional<UdfpsHbmProvider>> optionalOfUdfpsHbmProvider;
        private Provider overlappedElementControllerProvider;
        private Provider<OverlayToggleTile> overlayToggleTileProvider;
        private Provider overlayUiHostProvider;
        private Provider<OverviewProxyRecentsImpl> overviewProxyRecentsImplProvider;
        private Provider<OverviewProxyService> overviewProxyServiceProvider;
        private Provider<PeopleNotificationIdentifierImpl> peopleNotificationIdentifierImplProvider;
        private Provider<PeopleSpaceActivity> peopleSpaceActivityProvider;
        private Provider<PeopleSpaceWidgetManager> peopleSpaceWidgetManagerProvider;
        private Provider<PeopleSpaceWidgetPinnedReceiver> peopleSpaceWidgetPinnedReceiverProvider;
        private Provider<PeopleSpaceWidgetProvider> peopleSpaceWidgetProvider;
        private Provider<PhoneStateMonitor> phoneStateMonitorProvider;
        private Provider<PhoneStatusBarPolicy> phoneStatusBarPolicyProvider;
        private Provider<PluginDependencyProvider> pluginDependencyProvider;
        private Provider pointerCountClassifierProvider;
        private Provider<PowerManagerWrapper> powerManagerWrapperProvider;
        private Provider<PowerSaveState> powerSaveStateProvider;
        private Provider<PowerState> powerStateProvider;
        private Provider<PowerUI> powerUIProvider;
        private Provider<PreparationCoordinatorLogger> preparationCoordinatorLoggerProvider;
        private Provider<PreparationCoordinator> preparationCoordinatorProvider;
        private Provider<PrivacyDialogController> privacyDialogControllerProvider;
        private Provider<PrivacyDotViewController> privacyDotViewControllerProvider;
        private Provider<PrivacyItemController> privacyItemControllerProvider;
        private Provider<PrivacyLogger> privacyLoggerProvider;
        private Provider<ProtoTracer> protoTracerProvider;
        private Provider<AccessPointControllerImpl> provideAccessPointControllerImplProvider;
        private Provider<AccessibilityFloatingMenuController> provideAccessibilityFloatingMenuControllerProvider;
        private Provider<ActivityManagerWrapper> provideActivityManagerWrapperProvider;
        private Provider<NgaMessageHandler.StartActivityInfoListener> provideActivityStarterProvider;
        private Provider<Boolean> provideAllowNotificationLongPressProvider;
        private Provider<AlwaysOnDisplayPolicy> provideAlwaysOnDisplayPolicyProvider;
        private Provider<AmbientDisplayConfiguration> provideAmbientDisplayConfigurationProvider;
        private Provider<AssistUtils> provideAssistUtilsProvider;
        private Provider<Set<NgaMessageHandler.AudioInfoListener>> provideAudioInfoListenersProvider;
        private Provider<AutoHideController> provideAutoHideControllerProvider;
        private Provider<AutoTileManager> provideAutoTileManagerProvider;
        private Provider<DelayableExecutor> provideBackgroundDelayableExecutorProvider;
        private Provider<Executor> provideBackgroundExecutorProvider;
        private Provider<RepeatableExecutor> provideBackgroundRepeatableExecutorProvider;
        private Provider<BatteryController> provideBatteryControllerProvider;
        private Provider<BcSmartspaceDataPlugin> provideBcSmartspaceDataPluginProvider;
        private Provider<Handler> provideBgHandlerProvider;
        private Provider<Looper> provideBgLooperProvider;
        private Provider<LogBuffer> provideBroadcastDispatcherLogBufferProvider;
        private Provider<Optional<BubblesManager>> provideBubblesManagerProvider;
        private Provider<Set<NgaMessageHandler.CardInfoListener>> provideCardInfoListenersProvider;
        private Provider provideClockInfoListProvider;
        private Provider<List<Action>> provideColumbusActionsProvider;
        private Provider<Set<FeedbackEffect>> provideColumbusEffectsProvider;
        private Provider<Set<Gate>> provideColumbusGatesProvider;
        private Provider<CommandQueue> provideCommandQueueProvider;
        private Provider<CommonNotifCollection> provideCommonNotifCollectionProvider;
        private Provider<Set<NgaMessageHandler.ConfigInfoListener>> provideConfigInfoListenersProvider;
        private Provider<ConfigurationController> provideConfigurationControllerProvider;
        private Provider<DataSaverController> provideDataSaverControllerProvider;
        private Provider<DelayableExecutor> provideDelayableExecutorProvider;
        private Provider<DemoModeController> provideDemoModeControllerProvider;
        private Provider<DevicePolicyManagerWrapper> provideDevicePolicyManagerWrapperProvider;
        private Provider<DockManager> provideDockManagerProvider;
        private Provider<LogBuffer> provideDozeLogBufferProvider;
        private Provider<EditUserInfoController> provideEditUserInfoControllerProvider;
        private Provider<Executor> provideExecutorProvider;
        private Provider<Optional<FaceAuthScreenBrightnessController>> provideFaceAuthScreenBrightnessControllerProvider;
        private Provider<List<Action>> provideFullscreenActionsProvider;
        private Provider<List<Adjustment>> provideGestureAdjustmentsProvider;
        private Provider<GestureSensor> provideGestureSensorProvider;
        private Provider<GroupExpansionManager> provideGroupExpansionManagerProvider;
        private Provider<GroupMembershipManager> provideGroupMembershipManagerProvider;
        private Provider<Handler> provideHandlerProvider;
        private Provider<HeadsUpManagerPhone> provideHeadsUpManagerPhoneProvider;
        private Provider<INotificationManager> provideINotificationManagerProvider;
        private Provider<IThermalService> provideIThermalServiceProvider;
        private Provider<IndividualSensorPrivacyController> provideIndividualSensorPrivacyControllerProvider;
        private Provider<KeyguardLiftController> provideKeyguardLiftControllerProvider;
        private Provider<LeakDetector> provideLeakDetectorProvider;
        private Provider<String> provideLeakReportEmailProvider;
        private Provider<LocalBluetoothManager> provideLocalBluetoothControllerProvider;
        private Provider<LockPatternUtils> provideLockPatternUtilsProvider;
        private Provider<LogcatEchoTracker> provideLogcatEchoTrackerProvider;
        private Provider<Executor> provideLongRunningExecutorProvider;
        private Provider<Looper> provideLongRunningLooperProvider;
        private Provider<DelayableExecutor> provideMainDelayableExecutorProvider;
        private Provider<MetricsLogger> provideMetricsLoggerProvider;
        private Provider<NavigationBarController> provideNavigationBarControllerProvider;
        private Provider<NightDisplayListener> provideNightDisplayListenerProvider;
        private Provider<LogBuffer> provideNotifInteractionLogBufferProvider;
        private Provider<NotifRemoteViewCache> provideNotifRemoteViewCacheProvider;
        private Provider<LogBuffer> provideNotifVoiceReplyLogBufferProvider;
        private Provider<NotificationEntryManager> provideNotificationEntryManagerProvider;
        private Provider<NotificationGroupAlertTransferHelper> provideNotificationGroupAlertTransferHelperProvider;
        private Provider<NotificationGutsManager> provideNotificationGutsManagerProvider;
        private Provider<NotificationListener> provideNotificationListenerProvider;
        private Provider<NotificationLogger> provideNotificationLoggerProvider;
        private Provider<NotificationMediaManager> provideNotificationMediaManagerProvider;
        private Provider<NotificationMessagingUtil> provideNotificationMessagingUtilProvider;
        private Provider<NotificationPanelLogger> provideNotificationPanelLoggerProvider;
        private Provider<NotificationRemoteInputManager> provideNotificationRemoteInputManagerProvider;
        private Provider<LogBuffer> provideNotificationSectionLogBufferProvider;
        private Provider<NotificationViewHierarchyManager> provideNotificationViewHierarchyManagerProvider;
        private Provider<Optional<NotificationVoiceReplyClient>> provideNotificationVoiceReplyClientProvider;
        private Provider<NotificationsController> provideNotificationsControllerProvider;
        private Provider<LogBuffer> provideNotificationsLogBufferProvider;
        private Provider<OnUserInteractionCallback> provideOnUserInteractionCallbackProvider;
        private Provider<OngoingCallController> provideOngoingCallControllerProvider;
        private Provider<ViewGroup> provideParentViewGroupProvider;
        private Provider<PluginManager> providePluginManagerProvider;
        private Provider<ThresholdSensor> providePrimaryProxSensorProvider;
        private Provider<LogBuffer> providePrivacyLogBufferProvider;
        private Provider<QuickAccessWalletClient> provideQuickAccessWalletClientProvider;
        private Provider<LogBuffer> provideQuickSettingsLogBufferProvider;
        private Provider<RecentsImplementation> provideRecentsImplProvider;
        private Provider<Recents> provideRecentsProvider;
        private Provider<ReduceBrightColorsController> provideReduceBrightColorsListenerProvider;
        private Provider<Optional<ReverseChargingViewController>> provideReverseChargingViewControllerOptionalProvider;
        private Provider<Optional<ReverseWirelessCharger>> provideReverseWirelessChargerProvider;
        private Provider<ThresholdSensor> provideSecondaryProxSensorProvider;
        private Provider<SensorPrivacyController> provideSensorPrivacyControllerProvider;
        private Provider<SharedPreferences> provideSharePreferencesProvider;
        private Provider<SmartReplyController> provideSmartReplyControllerProvider;
        private Provider<SmartspaceTransitionController> provideSmartspaceTransitionControllerProvider;
        private Provider<StatusBarGoogle> provideStatusBarProvider;
        private Provider<SysUiState> provideSysUiStateProvider;
        private Provider<ThemeOverlayApplier> provideThemeOverlayManagerProvider;
        private Provider<Handler> provideTimeTickHandlerProvider;
        private Provider<LogBuffer> provideToastLogBufferProvider;
        private Provider<Set<TouchActionRegion>> provideTouchActionRegionsProvider;
        private Provider<Set<TouchInsideRegion>> provideTouchInsideRegionsProvider;
        private Provider<UdfpsHbmProvider> provideUdfpsHbmProvider;
        private Provider<Executor> provideUiBackgroundExecutorProvider;
        private Provider<Optional<UsbManager>> provideUsbManagerProvider;
        private Provider<Map<String, UserAction>> provideUserSelectedActionsProvider;
        private Provider<UserTracker> provideUserTrackerProvider;
        private Provider<VisualStabilityManager> provideVisualStabilityManagerProvider;
        private Provider<PowerUI.WarningsUI> provideWarningsUiProvider;
        private Provider<LayoutInflater> providerLayoutInflaterProvider;
        private Provider<SectionHeaderController> providesAlertingHeaderControllerProvider;
        private Provider<NodeController> providesAlertingHeaderNodeControllerProvider;
        private Provider<SectionHeaderControllerSubcomponent> providesAlertingHeaderSubcomponentProvider;
        private Provider<Set<FalsingClassifier>> providesBrightLineGestureClassifiersProvider;
        private Provider<BroadcastDispatcher> providesBroadcastDispatcherProvider;
        private Provider<Choreographer> providesChoreographerProvider;
        private Provider<Boolean> providesControlsFeatureEnabledProvider;
        private Provider<Float> providesDoubleTapTouchSlopProvider;
        private Provider<SectionHeaderController> providesIncomingHeaderControllerProvider;
        private Provider<NodeController> providesIncomingHeaderNodeControllerProvider;
        private Provider<SectionHeaderControllerSubcomponent> providesIncomingHeaderSubcomponentProvider;
        private Provider<MediaHost> providesKeyguardMediaHostProvider;
        private Provider<ModeSwitchesController> providesModeSwitchesControllerProvider;
        private Provider<SectionHeaderController> providesPeopleHeaderControllerProvider;
        private Provider<NodeController> providesPeopleHeaderNodeControllerProvider;
        private Provider<SectionHeaderControllerSubcomponent> providesPeopleHeaderSubcomponentProvider;
        private Provider<MediaHost> providesQSMediaHostProvider;
        private Provider<MediaHost> providesQuickQSMediaHostProvider;
        private Provider<SectionHeaderController> providesSilentHeaderControllerProvider;
        private Provider<NodeController> providesSilentHeaderNodeControllerProvider;
        private Provider<SectionHeaderControllerSubcomponent> providesSilentHeaderSubcomponentProvider;
        private Provider<Float> providesSingleTapTouchSlopProvider;
        private Provider<ViewMediatorCallback> providesViewMediatorCallbackProvider;
        private Provider proximityClassifierProvider;
        private Provider<Proximity> proximityProvider;
        private Provider<ProximitySensor> proximitySensorProvider;
        private Provider<PulseExpansionHandler> pulseExpansionHandlerProvider;
        private Provider<QSDetailDisplayer> qSDetailDisplayerProvider;
        private Provider<QSFactoryImplGoogle> qSFactoryImplGoogleProvider;
        private Provider<QSLogger> qSLoggerProvider;
        private Provider<QSTileHost> qSTileHostProvider;
        private Provider<QuickAccessWalletController> quickAccessWalletControllerProvider;
        private Provider<QuickAccessWalletTile> quickAccessWalletTileProvider;
        private Provider<RankingCoordinator> rankingCoordinatorProvider;
        private Provider<RecordingController> recordingControllerProvider;
        private Provider<RecordingService> recordingServiceProvider;
        private Provider<ReduceBrightColorsTile> reduceBrightColorsTileProvider;
        private Provider<RemoteInputQuickSettingsDisabler> remoteInputQuickSettingsDisablerProvider;
        private Provider<RemoteInputUriController> remoteInputUriControllerProvider;
        private Provider<ResumeMediaBrowserFactory> resumeMediaBrowserFactoryProvider;
        private Provider<ReverseChargingController> reverseChargingControllerProvider;
        private Provider<ReverseChargingTile> reverseChargingTileProvider;
        private Provider<ReverseChargingViewController> reverseChargingViewControllerProvider;
        private Provider<RingerModeTrackerImpl> ringerModeTrackerImplProvider;
        private Provider<RotationLockControllerImpl> rotationLockControllerImplProvider;
        private Provider<RotationLockTile> rotationLockTileProvider;
        private Provider<RowContentBindStageLogger> rowContentBindStageLoggerProvider;
        private Provider<RowContentBindStage> rowContentBindStageProvider;
        private Provider<ScreenDecorations> screenDecorationsProvider;
        private Provider<ScreenLifecycle> screenLifecycleProvider;
        private Provider<ScreenPinningRequest> screenPinningRequestProvider;
        private Provider<ScreenRecordController> screenRecordControllerProvider;
        private Provider<ScreenRecordDialog> screenRecordDialogProvider;
        private Provider<ScreenRecordTile> screenRecordTileProvider;
        private Provider<ScreenTouch> screenTouchProvider;
        private Provider<ScreenshotController> screenshotControllerProvider;
        private Provider<ScreenshotNotificationsController> screenshotNotificationsControllerProvider;
        private Provider<ScreenshotSmartActions> screenshotSmartActionsProvider;
        private Provider<ScrimController> scrimControllerProvider;
        private Provider<ScrollCaptureClient> scrollCaptureClientProvider;
        private Provider<ScrollCaptureController> scrollCaptureControllerProvider;
        private Provider<SectionHeaderControllerSubcomponent.Builder> sectionHeaderControllerSubcomponentBuilderProvider;
        private Provider secureSettingsImplProvider;
        private Provider<SecurityControllerImpl> securityControllerImplProvider;
        private Provider<SeekBarViewModel> seekBarViewModelProvider;
        private Provider<SensorConfiguration> sensorConfigurationProvider;
        private Provider<SensorUseStartedActivity> sensorUseStartedActivityProvider;
        private Provider<ServiceConfigurationGoogle> serviceConfigurationGoogleProvider;
        private Provider<GarbageMonitor.Service> serviceProvider;
        private Provider<Optional<Bubbles>> setBubblesProvider;
        private Provider<Optional<HideDisplayCutout>> setHideDisplayCutoutProvider;
        private Provider<Optional<LegacySplitScreen>> setLegacySplitScreenProvider;
        private Provider<Set<NgaMessageHandler.AudioInfoListener>> setOfAudioInfoListenerProvider;
        private Provider<Set<NgaMessageHandler.CardInfoListener>> setOfCardInfoListenerProvider;
        private Provider<Set<NgaMessageHandler.ChipsInfoListener>> setOfChipsInfoListenerProvider;
        private Provider<Set<NgaMessageHandler.ClearListener>> setOfClearListenerProvider;
        private Provider<Set<NgaMessageHandler.ConfigInfoListener>> setOfConfigInfoListenerProvider;
        private Provider<Set<NgaMessageHandler.EdgeLightsInfoListener>> setOfEdgeLightsInfoListenerProvider;
        private Provider<Set<NgaMessageHandler.GoBackListener>> setOfGoBackListenerProvider;
        private Provider<Set<NgaMessageHandler.GreetingInfoListener>> setOfGreetingInfoListenerProvider;
        private Provider<Set<NgaMessageHandler.KeepAliveListener>> setOfKeepAliveListenerProvider;
        private Provider<Set<NgaMessageHandler.KeyboardInfoListener>> setOfKeyboardInfoListenerProvider;
        private Provider<Set<NgaMessageHandler.NavBarVisibilityListener>> setOfNavBarVisibilityListenerProvider;
        private Provider<Set<NgaMessageHandler.StartActivityInfoListener>> setOfStartActivityInfoListenerProvider;
        private Provider<Set<NgaMessageHandler.TakeScreenshotListener>> setOfTakeScreenshotListenerProvider;
        private Provider<Set<TouchActionRegion>> setOfTouchActionRegionProvider;
        private Provider<Set<TouchInsideRegion>> setOfTouchInsideRegionProvider;
        private Provider<Set<NgaMessageHandler.TranscriptionInfoListener>> setOfTranscriptionInfoListenerProvider;
        private Provider<Set<NgaMessageHandler.WarmingListener>> setOfWarmingListenerProvider;
        private Provider<Set<NgaMessageHandler.ZerostateInfoListener>> setOfZerostateInfoListenerProvider;
        private Provider<Optional<OneHanded>> setOneHandedProvider;
        private Provider<Optional<Pip>> setPipProvider;
        private Provider<Optional<ShellCommandHandler>> setShellCommandHandlerProvider;
        private Provider<Optional<SplitScreen>> setSplitScreenProvider;
        private Provider<Optional<StartingSurface>> setStartingSurfaceProvider;
        private Provider<Optional<TaskSurfaceHelper>> setTaskSurfaceHelperProvider;
        private Provider<Optional<TaskViewFactory>> setTaskViewFactoryProvider;
        private Provider<ShellTransitions> setTransitionsProvider;
        private Provider<com.google.android.systemui.columbus.actions.SettingsAction> settingsActionProvider;
        private Provider<SetupWizard> setupWizardProvider;
        private Provider<ShadeControllerImpl> shadeControllerImplProvider;
        private Provider<ShadeListBuilderLogger> shadeListBuilderLoggerProvider;
        private Provider<ShadeListBuilder> shadeListBuilderProvider;
        private Provider<ShadeViewDifferLogger> shadeViewDifferLoggerProvider;
        private Provider<ShadeViewManagerFactory> shadeViewManagerFactoryProvider;
        private Provider<SharedCoordinatorLogger> sharedCoordinatorLoggerProvider;
        private Provider<ShortcutBarController> shortcutBarControllerProvider;
        private Provider<ShortcutKeyDispatcher> shortcutKeyDispatcherProvider;
        private Provider<SidefpsController> sidefpsControllerProvider;
        private Provider<SilenceAlertsDisabled> silenceAlertsDisabledProvider;
        private Provider<SilenceCall> silenceCallProvider;
        private Provider<com.google.android.systemui.columbus.actions.SilenceCall> silenceCallProvider2;
        private Provider<SingleTapClassifier> singleTapClassifierProvider;
        private Provider<SliceBroadcastRelayHandler> sliceBroadcastRelayHandlerProvider;
        private Provider<SmartActionInflaterImpl> smartActionInflaterImplProvider;
        private Provider<SmartActionsReceiver> smartActionsReceiverProvider;
        private Provider<SmartReplyConstants> smartReplyConstantsProvider;
        private Provider<SmartReplyInflaterImpl> smartReplyInflaterImplProvider;
        private Provider<SmartReplyStateInflaterImpl> smartReplyStateInflaterImplProvider;
        private Provider<SmartSpaceController> smartSpaceControllerProvider;
        private Provider<SmartspaceDedupingCoordinator> smartspaceDedupingCoordinatorProvider;
        private Provider<SnoozeAlarm> snoozeAlarmProvider;
        private Provider<SquishyNavigationButtons> squishyNavigationButtonsProvider;
        private Provider<StatusBarComponent.Builder> statusBarComponentBuilderProvider;
        private Provider<StatusBarContentInsetsProvider> statusBarContentInsetsProvider;
        private Provider<StatusBarIconControllerImpl> statusBarIconControllerImplProvider;
        private Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
        private Provider<StatusBarLocationPublisher> statusBarLocationPublisherProvider;
        private Provider<StatusBarNotificationActivityStarterLogger> statusBarNotificationActivityStarterLoggerProvider;
        private Provider<StatusBarRemoteInputCallback> statusBarRemoteInputCallbackProvider;
        private Provider<StatusBarSignalPolicy> statusBarSignalPolicyProvider;
        private Provider<StatusBarStateControllerImpl> statusBarStateControllerImplProvider;
        private Provider<StatusBarTouchableRegionManager> statusBarTouchableRegionManagerProvider;
        private Provider<StatusBarWindowController> statusBarWindowControllerProvider;
        private Provider<QSCarrierGroupController.SubscriptionManagerSlotIndexResolver> subscriptionManagerSlotIndexResolverProvider;
        private Provider<SuperStatusBarViewFactory> superStatusBarViewFactoryProvider;
        private Provider<SystemActions> systemActionsProvider;
        private Provider<SystemEventChipAnimationController> systemEventChipAnimationControllerProvider;
        private Provider<SystemEventCoordinator> systemEventCoordinatorProvider;
        private Provider<SystemKeyPress> systemKeyPressProvider;
        private Provider<SystemPropertiesHelper> systemPropertiesHelperProvider;
        private Provider systemSettingsImplProvider;
        private Provider<SystemStatusAnimationScheduler> systemStatusAnimationSchedulerProvider;
        private Provider<SystemUIAuxiliaryDumpService> systemUIAuxiliaryDumpServiceProvider;
        private Provider<SystemUIService> systemUIServiceProvider;
        private Provider<SysuiColorExtractor> sysuiColorExtractorProvider;
        private Provider takeScreenshotHandlerProvider;
        private Provider<TakeScreenshot> takeScreenshotProvider;
        private Provider<TakeScreenshotService> takeScreenshotServiceProvider;
        private Provider<TargetSdkResolver> targetSdkResolverProvider;
        private Provider taskStackNotifierProvider;
        private Provider<TelephonyActivity> telephonyActivityProvider;
        private Provider<com.google.android.systemui.columbus.gates.TelephonyActivity> telephonyActivityProvider2;
        private Provider<TelephonyListenerManager> telephonyListenerManagerProvider;
        private Provider<ThemeOverlayControllerGoogle> themeOverlayControllerGoogleProvider;
        private Provider timeoutManagerProvider;
        private Provider<ToastController> toastControllerProvider;
        private Provider<ToastFactory> toastFactoryProvider;
        private Provider<ToastLogger> toastLoggerProvider;
        private Provider<ToastUI> toastUIProvider;
        private Provider<TouchInsideHandler> touchInsideHandlerProvider;
        private Provider touchOutsideHandlerProvider;
        private Provider<TranscriptionController> transcriptionControllerProvider;
        private Provider<TunablePadding.TunablePaddingService> tunablePaddingServiceProvider;
        private Provider<TunerActivity> tunerActivityProvider;
        private Provider<TunerServiceImpl> tunerServiceImplProvider;
        private Provider<TvNotificationHandler> tvNotificationHandlerProvider;
        private Provider<TvNotificationPanelActivity> tvNotificationPanelActivityProvider;
        private Provider<TvStatusBar> tvStatusBarProvider;
        private Provider<TvUnblockSensorActivity> tvUnblockSensorActivityProvider;
        private Provider<TypeClassifier> typeClassifierProvider;
        private Provider<UdfpsController> udfpsControllerProvider;
        private Provider<UdfpsHapticsSimulator> udfpsHapticsSimulatorProvider;
        private Provider<UiModeNightTile> uiModeNightTileProvider;
        private Provider<UiOffloadThread> uiOffloadThreadProvider;
        private Provider<UnlockedScreenOffAnimationController> unlockedScreenOffAnimationControllerProvider;
        private Provider<UnpinNotifications> unpinNotificationsProvider;
        private Provider<com.google.android.systemui.columbus.actions.UnpinNotifications> unpinNotificationsProvider2;
        private Provider<UsbDebuggingActivity> usbDebuggingActivityProvider;
        private Provider<UsbDebuggingSecondaryUserActivity> usbDebuggingSecondaryUserActivityProvider;
        private Provider<UsbState> usbStateProvider;
        private Provider<UserActivity> userActivityProvider;
        private Provider<UserCreator> userCreatorProvider;
        private Provider<UserSwitcherController.UserDetailAdapter> userDetailAdapterProvider;
        private Provider<UserInfoControllerImpl> userInfoControllerImplProvider;
        private Provider<UserSelectedAction> userSelectedActionProvider;
        private Provider<UserSwitcherController> userSwitcherControllerProvider;
        private Provider<UserTile> userTileProvider;
        private Provider<VibratorHelper> vibratorHelperProvider;
        private Provider<VisualStabilityCoordinator> visualStabilityCoordinatorProvider;
        private Provider<VolumeDialogComponent> volumeDialogComponentProvider;
        private Provider<VolumeDialogControllerImpl> volumeDialogControllerImplProvider;
        private Provider<VolumeUI> volumeUIProvider;
        private Provider<VrMode> vrModeProvider;
        private Provider<WMShell> wMShellProvider;
        private Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;
        private Provider<WalletActivity> walletActivityProvider;
        private Provider<WalletControllerImpl> walletControllerImplProvider;
        private Provider<WallpaperNotifier> wallpaperNotifierProvider;
        private Provider<AccessPointControllerImpl.WifiPickerTrackerFactory> wifiPickerTrackerFactoryProvider;
        private Provider<WifiTile> wifiTileProvider;
        private Provider<WindowMagnification> windowMagnificationProvider;
        private Provider<WiredChargingRippleController> wiredChargingRippleControllerProvider;
        private Provider<WorkLockActivity> workLockActivityProvider;
        private Provider<WorkModeTile> workModeTileProvider;
        private Provider<ZenModeControllerImpl> zenModeControllerImplProvider;
        private Provider zigZagClassifierProvider;

        @Override // com.android.systemui.dagger.SysUIComponent
        public /* bridge */ /* synthetic */ void init() {
            super.init();
        }

        private SysUIGoogleSysUIComponentImpl(DependencyProvider dependencyProvider, NightDisplayListenerModule nightDisplayListenerModule, UserModule userModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<TaskSurfaceHelper> optional11) {
            initialize(dependencyProvider, nightDisplayListenerModule, userModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11);
            initialize2(dependencyProvider, nightDisplayListenerModule, userModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11);
            initialize3(dependencyProvider, nightDisplayListenerModule, userModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11);
            initialize4(dependencyProvider, nightDisplayListenerModule, userModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11);
            initialize5(dependencyProvider, nightDisplayListenerModule, userModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11);
            initialize6(dependencyProvider, nightDisplayListenerModule, userModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11);
            initialize7(dependencyProvider, nightDisplayListenerModule, userModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11);
        }

        private NotificationSectionsFeatureManager notificationSectionsFeatureManager() {
            return new NotificationSectionsFeatureManager(this.deviceConfigProxyProvider.get(), DaggerSysUIGoogleGlobalRootComponent.this.context);
        }

        private SectionHeaderController incomingHeaderSectionHeaderController() {
            return NotificationSectionHeadersModule_ProvidesIncomingHeaderControllerFactory.providesIncomingHeaderController(this.providesIncomingHeaderSubcomponentProvider.get());
        }

        private SectionHeaderController peopleHeaderSectionHeaderController() {
            return NotificationSectionHeadersModule_ProvidesPeopleHeaderControllerFactory.providesPeopleHeaderController(this.providesPeopleHeaderSubcomponentProvider.get());
        }

        private SectionHeaderController alertingHeaderSectionHeaderController() {
            return NotificationSectionHeadersModule_ProvidesAlertingHeaderControllerFactory.providesAlertingHeaderController(this.providesAlertingHeaderSubcomponentProvider.get());
        }

        private SectionHeaderController silentHeaderSectionHeaderController() {
            return NotificationSectionHeadersModule_ProvidesSilentHeaderControllerFactory.providesSilentHeaderController(this.providesSilentHeaderSubcomponentProvider.get());
        }

        /* access modifiers changed from: private */
        public NotificationSectionsManager notificationSectionsManager() {
            return new NotificationSectionsManager(this.statusBarStateControllerImplProvider.get(), this.provideConfigurationControllerProvider.get(), this.keyguardMediaControllerProvider.get(), notificationSectionsFeatureManager(), this.notificationSectionsLoggerProvider.get(), incomingHeaderSectionHeaderController(), peopleHeaderSectionHeaderController(), alertingHeaderSectionHeaderController(), silentHeaderSectionHeaderController());
        }

        private void initialize(DependencyProvider dependencyProvider, NightDisplayListenerModule nightDisplayListenerModule, UserModule userModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<TaskSurfaceHelper> optional11) {
            Provider<DumpManager> provider = DoubleCheck.provider(DumpManager_Factory.create());
            this.dumpManagerProvider = provider;
            this.bootCompleteCacheImplProvider = DoubleCheck.provider(BootCompleteCacheImpl_Factory.create(provider));
            this.provideConfigurationControllerProvider = DoubleCheck.provider(DependencyProvider_ProvideConfigurationControllerFactory.create(dependencyProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            Provider<Looper> provider2 = DoubleCheck.provider(SysUIConcurrencyModule_ProvideBgLooperFactory.create());
            this.provideBgLooperProvider = provider2;
            this.provideBackgroundExecutorProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideBackgroundExecutorFactory.create(provider2));
            this.provideExecutorProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideExecutorFactory.create(this.provideBgLooperProvider));
            this.provideBgHandlerProvider = SysUIConcurrencyModule_ProvideBgHandlerFactory.create(this.provideBgLooperProvider);
            this.provideUserTrackerProvider = DoubleCheck.provider(SettingsModule_ProvideUserTrackerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUserManagerProvider, this.dumpManagerProvider, this.provideBgHandlerProvider));
            this.controlsListingControllerImplProvider = DoubleCheck.provider(ControlsListingControllerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideExecutorProvider, this.provideUserTrackerProvider));
            this.provideBackgroundDelayableExecutorProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideBackgroundDelayableExecutorFactory.create(this.provideBgLooperProvider));
            this.controlsControllerImplProvider = new DelegateFactory();
            this.provideMainDelayableExecutorProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideMainDelayableExecutorFactory.create(GlobalConcurrencyModule_ProvideMainLooperFactory.create()));
            this.provideSharePreferencesProvider = DependencyProvider_ProvideSharePreferencesFactory.create(dependencyProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider);
            this.provideDelayableExecutorProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideDelayableExecutorFactory.create(this.provideBgLooperProvider));
            Provider<LogcatEchoTracker> provider3 = DoubleCheck.provider(LogModule_ProvideLogcatEchoTrackerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideContentResolverProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create()));
            this.provideLogcatEchoTrackerProvider = provider3;
            Provider<LogBufferFactory> provider4 = DoubleCheck.provider(LogBufferFactory_Factory.create(this.dumpManagerProvider, provider3));
            this.logBufferFactoryProvider = provider4;
            Provider<LogBuffer> provider5 = DoubleCheck.provider(LogModule_ProvideBroadcastDispatcherLogBufferFactory.create(provider4));
            this.provideBroadcastDispatcherLogBufferProvider = provider5;
            this.broadcastDispatcherLoggerProvider = BroadcastDispatcherLogger_Factory.create(provider5);
            this.providesBroadcastDispatcherProvider = DoubleCheck.provider(DependencyProvider_ProvidesBroadcastDispatcherFactory.create(dependencyProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideBgLooperProvider, this.provideBackgroundExecutorProvider, this.dumpManagerProvider, this.broadcastDispatcherLoggerProvider, this.provideUserTrackerProvider));
            this.ringerModeTrackerImplProvider = DoubleCheck.provider(RingerModeTrackerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideAudioManagerProvider, this.providesBroadcastDispatcherProvider, this.provideBackgroundExecutorProvider));
            this.statusBarStateControllerImplProvider = DoubleCheck.provider(StatusBarStateControllerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider));
            this.provideLockPatternUtilsProvider = DoubleCheck.provider(DependencyProvider_ProvideLockPatternUtilsFactory.create(dependencyProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.protoTracerProvider = DoubleCheck.provider(ProtoTracer_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.dumpManagerProvider));
            this.commandRegistryProvider = DoubleCheck.provider(CommandRegistry_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider));
            this.provideCommandQueueProvider = DoubleCheck.provider(StatusBarDependenciesModule_ProvideCommandQueueFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.protoTracerProvider, this.commandRegistryProvider));
            this.providerLayoutInflaterProvider = DoubleCheck.provider(DependencyProvider_ProviderLayoutInflaterFactory.create(dependencyProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.provideStatusBarProvider = new DelegateFactory();
            this.enhancedEstimatesGoogleImplProvider = DoubleCheck.provider(EnhancedEstimatesGoogleImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.globalSettingsImplProvider = GlobalSettingsImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideContentResolverProvider);
            this.provideDemoModeControllerProvider = DoubleCheck.provider(DemoModeModule_ProvideDemoModeControllerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.dumpManagerProvider, this.globalSettingsImplProvider));
            this.provideReverseWirelessChargerProvider = DoubleCheck.provider(SystemUIGoogleModule_ProvideReverseWirelessChargerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.provideUsbManagerProvider = DoubleCheck.provider(SystemUIGoogleModule_ProvideUsbManagerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.reverseChargingControllerProvider = DoubleCheck.provider(ReverseChargingController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.providesBroadcastDispatcherProvider, this.provideReverseWirelessChargerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideAlarmManagerProvider, this.provideUsbManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.bootCompleteCacheImplProvider));
            this.provideBatteryControllerProvider = DoubleCheck.provider(SystemUIGoogleModule_ProvideBatteryControllerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.enhancedEstimatesGoogleImplProvider, DaggerSysUIGoogleGlobalRootComponent.this.providePowerManagerProvider, this.providesBroadcastDispatcherProvider, this.provideDemoModeControllerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.provideBgHandlerProvider, this.provideUserTrackerProvider, this.reverseChargingControllerProvider));
            this.provideAmbientDisplayConfigurationProvider = DependencyProvider_ProvideAmbientDisplayConfigurationFactory.create(dependencyProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider);
            this.keyguardEnvironmentImplProvider = DoubleCheck.provider(KeyguardEnvironmentImpl_Factory.create());
            this.provideIndividualSensorPrivacyControllerProvider = DoubleCheck.provider(SystemUIGoogleModule_ProvideIndividualSensorPrivacyControllerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideSensorPrivacyManagerProvider));
            this.bindSystemClockProvider = DoubleCheck.provider(SystemClockImpl_Factory.create());
            Provider<AppOpsControllerImpl> provider6 = DoubleCheck.provider(AppOpsControllerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideBgLooperProvider, this.dumpManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideAudioManagerProvider, this.provideIndividualSensorPrivacyControllerProvider, this.providesBroadcastDispatcherProvider, this.bindSystemClockProvider));
            this.appOpsControllerImplProvider = provider6;
            this.foregroundServiceControllerProvider = DoubleCheck.provider(ForegroundServiceController_Factory.create(provider6, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider));
            this.notificationClickNotifierProvider = DoubleCheck.provider(NotificationClickNotifier_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideIStatusBarServiceProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider));
            this.secureSettingsImplProvider = SecureSettingsImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideContentResolverProvider);
            this.deviceProvisionedControllerImplProvider = DoubleCheck.provider(DeviceProvisionedControllerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.providesBroadcastDispatcherProvider, this.globalSettingsImplProvider, this.secureSettingsImplProvider));
            this.keyguardUpdateMonitorProvider = new DelegateFactory();
            this.provideSmartspaceTransitionControllerProvider = DoubleCheck.provider(SystemUIModule_ProvideSmartspaceTransitionControllerFactory.create());
            this.keyguardStateControllerImplProvider = DoubleCheck.provider(KeyguardStateControllerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.keyguardUpdateMonitorProvider, this.provideLockPatternUtilsProvider, this.provideSmartspaceTransitionControllerProvider));
            this.provideLeakDetectorProvider = DoubleCheck.provider(DependencyProvider_ProvideLeakDetectorFactory.create(dependencyProvider));
            this.tunerServiceImplProvider = DoubleCheck.provider(TunerServiceImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.provideLeakDetectorProvider, this.provideDemoModeControllerProvider, this.provideUserTrackerProvider));
            this.notificationLockscreenUserManagerGoogleProvider = new DelegateFactory();
            this.keyguardBypassControllerProvider = DoubleCheck.provider(KeyguardBypassController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.tunerServiceImplProvider, this.statusBarStateControllerImplProvider, this.notificationLockscreenUserManagerGoogleProvider, this.keyguardStateControllerImplProvider, this.dumpManagerProvider));
            this.smartSpaceControllerProvider = new DelegateFactory();
            DelegateFactory.setDelegate(this.notificationLockscreenUserManagerGoogleProvider, DoubleCheck.provider(NotificationLockscreenUserManagerGoogle_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.providesBroadcastDispatcherProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideDevicePolicyManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUserManagerProvider, this.notificationClickNotifierProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideKeyguardManagerProvider, this.statusBarStateControllerImplProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.deviceProvisionedControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardBypassControllerProvider, this.smartSpaceControllerProvider)));
            MediaFeatureFlag_Factory create = MediaFeatureFlag_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider);
            this.mediaFeatureFlagProvider = create;
            this.notificationFilterProvider = DoubleCheck.provider(NotificationFilter_Factory.create(this.statusBarStateControllerImplProvider, this.keyguardEnvironmentImplProvider, this.foregroundServiceControllerProvider, this.notificationLockscreenUserManagerGoogleProvider, create));
            this.systemPropertiesHelperProvider = DoubleCheck.provider(SystemPropertiesHelper_Factory.create());
            Provider<FeatureFlagReader> provider7 = DoubleCheck.provider(FeatureFlagReader_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideResourcesProvider, DaggerSysUIGoogleGlobalRootComponent.this.buildInfoProvider, this.systemPropertiesHelperProvider));
            this.featureFlagReaderProvider = provider7;
            this.featureFlagsProvider = DoubleCheck.provider(FeatureFlags_Factory.create(provider7, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.providePluginManagerProvider = DoubleCheck.provider(DependencyProvider_ProvidePluginManagerFactory.create(dependencyProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            Provider<ExtensionControllerImpl> provider8 = DoubleCheck.provider(ExtensionControllerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideLeakDetectorProvider, this.providePluginManagerProvider, this.tunerServiceImplProvider, this.provideConfigurationControllerProvider));
            this.extensionControllerImplProvider = provider8;
            this.notificationPersonExtractorPluginBoundaryProvider = DoubleCheck.provider(NotificationPersonExtractorPluginBoundary_Factory.create(provider8));
            DelegateFactory delegateFactory = new DelegateFactory();
            this.provideGroupMembershipManagerProvider = delegateFactory;
            this.peopleNotificationIdentifierImplProvider = DoubleCheck.provider(PeopleNotificationIdentifierImpl_Factory.create(this.notificationPersonExtractorPluginBoundaryProvider, delegateFactory));
            Factory create2 = InstanceFactory.create(optional6);
            this.setBubblesProvider = create2;
            Provider<NotificationGroupManagerLegacy> provider9 = DoubleCheck.provider(NotificationGroupManagerLegacy_Factory.create(this.statusBarStateControllerImplProvider, this.peopleNotificationIdentifierImplProvider, create2));
            this.notificationGroupManagerLegacyProvider = provider9;
            DelegateFactory.setDelegate(this.provideGroupMembershipManagerProvider, DoubleCheck.provider(NotificationsModule_ProvideGroupMembershipManagerFactory.create(this.featureFlagsProvider, provider9)));
            this.provideHeadsUpManagerPhoneProvider = DoubleCheck.provider(SystemUIGoogleModule_ProvideHeadsUpManagerPhoneFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.statusBarStateControllerImplProvider, this.keyguardBypassControllerProvider, this.provideGroupMembershipManagerProvider, this.provideConfigurationControllerProvider));
            this.notificationInterruptStateProviderImplProvider = DoubleCheck.provider(NotificationInterruptStateProviderImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideContentResolverProvider, DaggerSysUIGoogleGlobalRootComponent.this.providePowerManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIDreamManagerProvider, this.provideAmbientDisplayConfigurationProvider, this.notificationFilterProvider, this.provideBatteryControllerProvider, this.statusBarStateControllerImplProvider, this.provideHeadsUpManagerPhoneProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider));
            this.provideDockManagerProvider = DoubleCheck.provider(SystemUIGoogleModule_ProvideDockManagerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.providesBroadcastDispatcherProvider, this.statusBarStateControllerImplProvider, this.notificationInterruptStateProviderImplProvider, this.provideConfigurationControllerProvider, this.provideMainDelayableExecutorProvider));
            this.falsingDataProvider = DoubleCheck.provider(FalsingDataProvider_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideDisplayMetricsProvider, this.provideBatteryControllerProvider, this.provideDockManagerProvider));
            this.deviceConfigProxyProvider = DoubleCheck.provider(DeviceConfigProxy_Factory.create());
            this.provideMetricsLoggerProvider = DoubleCheck.provider(DependencyProvider_ProvideMetricsLoggerFactory.create(dependencyProvider));
            DistanceClassifier_Factory create3 = DistanceClassifier_Factory.create(this.falsingDataProvider, this.deviceConfigProxyProvider);
            this.distanceClassifierProvider = create3;
            this.proximityClassifierProvider = ProximityClassifier_Factory.create(create3, this.falsingDataProvider, this.deviceConfigProxyProvider);
            this.pointerCountClassifierProvider = PointerCountClassifier_Factory.create(this.falsingDataProvider);
            this.typeClassifierProvider = TypeClassifier_Factory.create(this.falsingDataProvider);
            this.diagonalClassifierProvider = DiagonalClassifier_Factory.create(this.falsingDataProvider, this.deviceConfigProxyProvider);
            ZigZagClassifier_Factory create4 = ZigZagClassifier_Factory.create(this.falsingDataProvider, this.deviceConfigProxyProvider);
            this.zigZagClassifierProvider = create4;
            this.providesBrightLineGestureClassifiersProvider = FalsingModule_ProvidesBrightLineGestureClassifiersFactory.create(this.distanceClassifierProvider, this.proximityClassifierProvider, this.pointerCountClassifierProvider, this.typeClassifierProvider, this.diagonalClassifierProvider, create4);
            this.namedSetOfFalsingClassifierProvider = SetFactory.builder(0, 1).addCollectionProvider(this.providesBrightLineGestureClassifiersProvider).build();
            FalsingModule_ProvidesSingleTapTouchSlopFactory create5 = FalsingModule_ProvidesSingleTapTouchSlopFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideViewConfigurationProvider);
            this.providesSingleTapTouchSlopProvider = create5;
            this.singleTapClassifierProvider = SingleTapClassifier_Factory.create(this.falsingDataProvider, create5);
            FalsingModule_ProvidesDoubleTapTouchSlopFactory create6 = FalsingModule_ProvidesDoubleTapTouchSlopFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideResourcesProvider);
            this.providesDoubleTapTouchSlopProvider = create6;
            this.doubleTapClassifierProvider = DoubleTapClassifier_Factory.create(this.falsingDataProvider, this.singleTapClassifierProvider, create6, FalsingModule_ProvidesDoubleTapTimeoutMsFactory.create());
            Provider<HistoryTracker> provider10 = DoubleCheck.provider(HistoryTracker_Factory.create(this.bindSystemClockProvider));
            this.historyTrackerProvider = provider10;
            this.brightLineFalsingManagerProvider = BrightLineFalsingManager_Factory.create(this.falsingDataProvider, this.provideMetricsLoggerProvider, this.namedSetOfFalsingClassifierProvider, this.singleTapClassifierProvider, this.doubleTapClassifierProvider, provider10, this.keyguardStateControllerImplProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideAccessibilityManagerProvider, GlobalModule_ProvideIsTestHarnessFactory.create());
            this.falsingManagerProxyProvider = DoubleCheck.provider(FalsingManagerProxy_Factory.create(this.providePluginManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, this.deviceConfigProxyProvider, this.dumpManagerProvider, this.brightLineFalsingManagerProvider));
            this.asyncSensorManagerProvider = DoubleCheck.provider(AsyncSensorManager_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.providesSensorManagerProvider, ThreadFactoryImpl_Factory.create(), this.providePluginManagerProvider));
            this.builderProvider = ThresholdSensorImpl_Builder_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideResourcesProvider, this.asyncSensorManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideExecutionProvider);
            this.providePrimaryProxSensorProvider = SensorModule_ProvidePrimaryProxSensorFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.providesSensorManagerProvider, this.builderProvider);
            SensorModule_ProvideSecondaryProxSensorFactory create7 = SensorModule_ProvideSecondaryProxSensorFactory.create(this.builderProvider);
            this.provideSecondaryProxSensorProvider = create7;
            ProximitySensor_Factory create8 = ProximitySensor_Factory.create(this.providePrimaryProxSensorProvider, create7, this.provideMainDelayableExecutorProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideExecutionProvider);
            this.proximitySensorProvider = create8;
            this.falsingCollectorImplProvider = DoubleCheck.provider(FalsingCollectorImpl_Factory.create(this.falsingDataProvider, this.falsingManagerProxyProvider, this.keyguardUpdateMonitorProvider, this.historyTrackerProvider, create8, this.statusBarStateControllerImplProvider, this.keyguardStateControllerImplProvider, this.provideBatteryControllerProvider, this.provideDockManagerProvider, this.provideMainDelayableExecutorProvider, this.bindSystemClockProvider));
            this.statusBarKeyguardViewManagerProvider = new DelegateFactory();
            Provider<Executor> provider11 = DoubleCheck.provider(SysUIConcurrencyModule_ProvideUiBackgroundExecutorFactory.create());
            this.provideUiBackgroundExecutorProvider = provider11;
            this.dismissCallbackRegistryProvider = DoubleCheck.provider(DismissCallbackRegistry_Factory.create(provider11));
            this.activityStarterDelegateProvider = new DelegateFactory();
            this.telephonyListenerManagerProvider = DoubleCheck.provider(TelephonyListenerManager_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideTelephonyManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, TelephonyCallback_Factory.create()));
            this.userSwitcherControllerProvider = new DelegateFactory();
            this.adapterProvider = UserDetailView_Adapter_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.userSwitcherControllerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider, this.falsingManagerProxyProvider);
            this.userDetailAdapterProvider = UserSwitcherController_UserDetailAdapter_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.adapterProvider);
        }

        private void initialize2(DependencyProvider dependencyProvider, NightDisplayListenerModule nightDisplayListenerModule, UserModule userModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<TaskSurfaceHelper> optional11) {
            DelegateFactory.setDelegate(this.userSwitcherControllerProvider, DoubleCheck.provider(UserSwitcherController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUserManagerProvider, this.provideUserTrackerProvider, this.keyguardStateControllerImplProvider, this.deviceProvisionedControllerImplProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideDevicePolicyManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.activityStarterDelegateProvider, this.providesBroadcastDispatcherProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider, this.falsingManagerProxyProvider, this.telephonyListenerManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIActivityTaskManagerProvider, this.userDetailAdapterProvider, this.secureSettingsImplProvider, this.provideBackgroundExecutorProvider)));
            this.navigationModeControllerProvider = DoubleCheck.provider(NavigationModeController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.deviceProvisionedControllerImplProvider, this.provideConfigurationControllerProvider, this.provideUiBackgroundExecutorProvider));
            this.provideAssistUtilsProvider = DoubleCheck.provider(AssistModule_ProvideAssistUtilsFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            DelegateFactory delegateFactory = new DelegateFactory();
            this.assistManagerGoogleProvider = delegateFactory;
            this.timeoutManagerProvider = DoubleCheck.provider(TimeoutManager_Factory.create(delegateFactory));
            this.assistantPresenceHandlerProvider = DoubleCheck.provider(AssistantPresenceHandler_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideAssistUtilsProvider));
            this.optionalOfLazyOfStatusBarProvider = new DelegateFactory();
            this.phoneStateMonitorProvider = DoubleCheck.provider(PhoneStateMonitor_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.providesBroadcastDispatcherProvider, this.optionalOfLazyOfStatusBarProvider, this.bootCompleteCacheImplProvider));
            Provider<GoogleAssistLogger> provider = DoubleCheck.provider(GoogleAssistLogger_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider, this.provideAssistUtilsProvider, this.phoneStateMonitorProvider, this.assistantPresenceHandlerProvider));
            this.googleAssistLoggerProvider = provider;
            this.touchInsideHandlerProvider = DoubleCheck.provider(TouchInsideHandler_Factory.create(this.assistManagerGoogleProvider, this.navigationModeControllerProvider, provider));
            this.colorChangeHandlerProvider = DoubleCheck.provider(ColorChangeHandler_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.touchOutsideHandlerProvider = DoubleCheck.provider(TouchOutsideHandler_Factory.create());
            Provider provider2 = DoubleCheck.provider(OverlayUiHost_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.touchOutsideHandlerProvider));
            this.overlayUiHostProvider = provider2;
            this.provideParentViewGroupProvider = DoubleCheck.provider(AssistantUIHintsModule_ProvideParentViewGroupFactory.create(provider2));
            this.edgeLightsControllerProvider = DoubleCheck.provider(EdgeLightsController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideParentViewGroupProvider, this.googleAssistLoggerProvider));
            this.glowControllerProvider = DoubleCheck.provider(GlowController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideParentViewGroupProvider, this.touchInsideHandlerProvider));
            this.overlappedElementControllerProvider = DoubleCheck.provider(OverlappedElementController_Factory.create(this.provideStatusBarProvider));
            Provider provider3 = DoubleCheck.provider(LightnessProvider_Factory.create());
            this.lightnessProvider = provider3;
            this.scrimControllerProvider = DoubleCheck.provider(ScrimController_Factory.create(this.provideParentViewGroupProvider, this.overlappedElementControllerProvider, provider3, this.touchInsideHandlerProvider));
            Provider provider4 = DoubleCheck.provider(FlingVelocityWrapper_Factory.create());
            this.flingVelocityWrapperProvider = provider4;
            this.transcriptionControllerProvider = DoubleCheck.provider(TranscriptionController_Factory.create(this.provideParentViewGroupProvider, this.touchInsideHandlerProvider, provider4, this.provideConfigurationControllerProvider));
            this.iconControllerProvider = DoubleCheck.provider(IconController_Factory.create(this.providerLayoutInflaterProvider, this.provideParentViewGroupProvider, this.provideConfigurationControllerProvider));
            this.assistantWarmerProvider = DoubleCheck.provider(AssistantWarmer_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            DelegateFactory delegateFactory2 = new DelegateFactory();
            this.provideNavigationBarControllerProvider = delegateFactory2;
            this.navBarFaderProvider = DoubleCheck.provider(NavBarFader_Factory.create(delegateFactory2, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider));
            this.ngaUiControllerProvider = DoubleCheck.provider(NgaUiController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.timeoutManagerProvider, this.assistantPresenceHandlerProvider, this.touchInsideHandlerProvider, this.colorChangeHandlerProvider, this.overlayUiHostProvider, this.edgeLightsControllerProvider, this.glowControllerProvider, this.scrimControllerProvider, this.transcriptionControllerProvider, this.iconControllerProvider, this.lightnessProvider, this.statusBarStateControllerImplProvider, this.assistManagerGoogleProvider, this.flingVelocityWrapperProvider, this.assistantWarmerProvider, this.navBarFaderProvider, this.googleAssistLoggerProvider));
            this.opaEnabledReceiverProvider = DoubleCheck.provider(OpaEnabledReceiver_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.providesBroadcastDispatcherProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, DaggerSysUIGoogleGlobalRootComponent.this.opaEnabledSettingsProvider));
            this.provideAlwaysOnDisplayPolicyProvider = DoubleCheck.provider(DependencyProvider_ProvideAlwaysOnDisplayPolicyFactory.create(dependencyProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.wakefulnessLifecycleProvider = DoubleCheck.provider(WakefulnessLifecycle_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, FrameworkServicesModule_ProvideIWallPaperManagerFactory.create()));
            this.newKeyguardViewMediatorProvider = new DelegateFactory();
            this.dozeParametersProvider = new DelegateFactory();
            this.unlockedScreenOffAnimationControllerProvider = DoubleCheck.provider(UnlockedScreenOffAnimationController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.wakefulnessLifecycleProvider, this.statusBarStateControllerImplProvider, this.newKeyguardViewMediatorProvider, this.keyguardStateControllerImplProvider, this.dozeParametersProvider));
            DelegateFactory.setDelegate(this.dozeParametersProvider, DoubleCheck.provider(DozeParameters_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideResourcesProvider, this.provideAmbientDisplayConfigurationProvider, this.provideAlwaysOnDisplayPolicyProvider, DaggerSysUIGoogleGlobalRootComponent.this.providePowerManagerProvider, this.provideBatteryControllerProvider, this.tunerServiceImplProvider, this.dumpManagerProvider, this.featureFlagsProvider, this.unlockedScreenOffAnimationControllerProvider)));
            this.sysuiColorExtractorProvider = DoubleCheck.provider(SysuiColorExtractor_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideConfigurationControllerProvider));
            this.authControllerProvider = new DelegateFactory();
            this.notificationShadeWindowControllerImplProvider = DoubleCheck.provider(NotificationShadeWindowControllerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideWindowManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIActivityManagerProvider, this.dozeParametersProvider, this.statusBarStateControllerImplProvider, this.provideConfigurationControllerProvider, this.newKeyguardViewMediatorProvider, this.keyguardBypassControllerProvider, this.sysuiColorExtractorProvider, this.dumpManagerProvider, this.keyguardStateControllerImplProvider, this.unlockedScreenOffAnimationControllerProvider, this.authControllerProvider));
            this.provideSysUiStateProvider = DoubleCheck.provider(SystemUIModule_ProvideSysUiStateFactory.create());
            this.setPipProvider = InstanceFactory.create(optional);
            this.setLegacySplitScreenProvider = InstanceFactory.create(optional2);
            this.setSplitScreenProvider = InstanceFactory.create(optional3);
            this.setOneHandedProvider = InstanceFactory.create(optional5);
            this.setTransitionsProvider = InstanceFactory.create(shellTransitions);
            this.setStartingSurfaceProvider = InstanceFactory.create(optional10);
            this.overviewProxyServiceProvider = DoubleCheck.provider(OverviewProxyService_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider, this.provideNavigationBarControllerProvider, this.navigationModeControllerProvider, this.notificationShadeWindowControllerImplProvider, this.provideSysUiStateProvider, this.setPipProvider, this.setLegacySplitScreenProvider, this.setSplitScreenProvider, this.optionalOfLazyOfStatusBarProvider, this.setOneHandedProvider, this.providesBroadcastDispatcherProvider, this.setTransitionsProvider, this.setStartingSurfaceProvider, this.provideSmartspaceTransitionControllerProvider));
            this.opaEnabledDispatcherProvider = OpaEnabledDispatcher_Factory.create(this.provideStatusBarProvider);
            this.setOfKeepAliveListenerProvider = SetFactory.builder(1, 0).addProvider(this.timeoutManagerProvider).build();
            this.provideAudioInfoListenersProvider = AssistantUIHintsModule_ProvideAudioInfoListenersFactory.create(this.edgeLightsControllerProvider, this.glowControllerProvider);
            this.setOfAudioInfoListenerProvider = SetFactory.builder(0, 1).addCollectionProvider(this.provideAudioInfoListenersProvider).build();
            this.provideCardInfoListenersProvider = AssistantUIHintsModule_ProvideCardInfoListenersFactory.create(this.glowControllerProvider, this.scrimControllerProvider, this.transcriptionControllerProvider, this.lightnessProvider);
            this.setOfCardInfoListenerProvider = SetFactory.builder(0, 1).addCollectionProvider(this.provideCardInfoListenersProvider).build();
            this.taskStackNotifierProvider = DoubleCheck.provider(TaskStackNotifier_Factory.create());
            this.optionalOfCommandQueueProvider = PresentJdkOptionalInstanceProvider.of(this.provideCommandQueueProvider);
            this.keyboardMonitorProvider = DoubleCheck.provider(KeyboardMonitor_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.optionalOfCommandQueueProvider));
            Provider<ConfigurationHandler> provider5 = DoubleCheck.provider(ConfigurationHandler_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.configurationHandlerProvider = provider5;
            this.provideConfigInfoListenersProvider = AssistantUIHintsModule_ProvideConfigInfoListenersFactory.create(this.assistantPresenceHandlerProvider, this.touchInsideHandlerProvider, this.touchOutsideHandlerProvider, this.taskStackNotifierProvider, this.keyboardMonitorProvider, this.colorChangeHandlerProvider, provider5);
            this.setOfConfigInfoListenerProvider = SetFactory.builder(0, 1).addCollectionProvider(this.provideConfigInfoListenersProvider).build();
            this.provideTouchActionRegionsProvider = InputModule_ProvideTouchActionRegionsFactory.create(this.iconControllerProvider, this.transcriptionControllerProvider);
            this.setOfTouchActionRegionProvider = SetFactory.builder(0, 1).addCollectionProvider(this.provideTouchActionRegionsProvider).build();
            this.provideTouchInsideRegionsProvider = InputModule_ProvideTouchInsideRegionsFactory.create(this.glowControllerProvider, this.scrimControllerProvider, this.transcriptionControllerProvider);
            SetFactory build = SetFactory.builder(0, 1).addCollectionProvider(this.provideTouchInsideRegionsProvider).build();
            this.setOfTouchInsideRegionProvider = build;
            Provider<NgaInputHandler> provider6 = DoubleCheck.provider(NgaInputHandler_Factory.create(this.touchInsideHandlerProvider, this.setOfTouchActionRegionProvider, build));
            this.ngaInputHandlerProvider = provider6;
            this.bindEdgeLightsInfoListenersProvider = AssistantUIHintsModule_BindEdgeLightsInfoListenersFactory.create(this.edgeLightsControllerProvider, provider6);
            this.setOfEdgeLightsInfoListenerProvider = SetFactory.builder(0, 1).addCollectionProvider(this.bindEdgeLightsInfoListenersProvider).build();
            this.setOfTranscriptionInfoListenerProvider = SetFactory.builder(1, 0).addProvider(this.transcriptionControllerProvider).build();
            this.setOfGreetingInfoListenerProvider = SetFactory.builder(1, 0).addProvider(this.transcriptionControllerProvider).build();
            this.setOfChipsInfoListenerProvider = SetFactory.builder(1, 0).addProvider(this.transcriptionControllerProvider).build();
            this.setOfClearListenerProvider = SetFactory.builder(1, 0).addProvider(this.transcriptionControllerProvider).build();
            this.provideActivityStarterProvider = AssistantUIHintsModule_ProvideActivityStarterFactory.create(this.provideStatusBarProvider);
            this.setOfStartActivityInfoListenerProvider = SetFactory.builder(1, 0).addProvider(this.provideActivityStarterProvider).build();
            this.setOfKeyboardInfoListenerProvider = SetFactory.builder(1, 0).addProvider(this.iconControllerProvider).build();
            this.setOfZerostateInfoListenerProvider = SetFactory.builder(1, 0).addProvider(this.iconControllerProvider).build();
            this.goBackHandlerProvider = DoubleCheck.provider(GoBackHandler_Factory.create());
            this.setOfGoBackListenerProvider = SetFactory.builder(1, 0).addProvider(this.goBackHandlerProvider).build();
            this.takeScreenshotHandlerProvider = DoubleCheck.provider(TakeScreenshotHandler_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.setOfTakeScreenshotListenerProvider = SetFactory.builder(1, 0).addProvider(this.takeScreenshotHandlerProvider).build();
            this.setOfWarmingListenerProvider = SetFactory.builder(1, 0).addProvider(this.assistantWarmerProvider).build();
            SetFactory build2 = SetFactory.builder(1, 0).addProvider(this.navBarFaderProvider).build();
            this.setOfNavBarVisibilityListenerProvider = build2;
            this.ngaMessageHandlerProvider = DoubleCheck.provider(NgaMessageHandler_Factory.create(this.ngaUiControllerProvider, this.assistantPresenceHandlerProvider, this.navigationModeControllerProvider, this.setOfKeepAliveListenerProvider, this.setOfAudioInfoListenerProvider, this.setOfCardInfoListenerProvider, this.setOfConfigInfoListenerProvider, this.setOfEdgeLightsInfoListenerProvider, this.setOfTranscriptionInfoListenerProvider, this.setOfGreetingInfoListenerProvider, this.setOfChipsInfoListenerProvider, this.setOfClearListenerProvider, this.setOfStartActivityInfoListenerProvider, this.setOfKeyboardInfoListenerProvider, this.setOfZerostateInfoListenerProvider, this.setOfGoBackListenerProvider, this.setOfTakeScreenshotListenerProvider, this.setOfWarmingListenerProvider, build2, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider));
            this.defaultUiControllerProvider = DoubleCheck.provider(DefaultUiController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.googleAssistLoggerProvider));
            this.googleDefaultUiControllerProvider = DoubleCheck.provider(GoogleDefaultUiController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.googleAssistLoggerProvider));
            DelegateFactory.setDelegate(this.assistManagerGoogleProvider, DoubleCheck.provider(AssistManagerGoogle_Factory.create(this.deviceProvisionedControllerImplProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideAssistUtilsProvider, this.ngaUiControllerProvider, this.provideCommandQueueProvider, this.opaEnabledReceiverProvider, this.phoneStateMonitorProvider, this.overviewProxyServiceProvider, this.opaEnabledDispatcherProvider, this.keyguardUpdateMonitorProvider, this.navigationModeControllerProvider, this.provideConfigurationControllerProvider, this.assistantPresenceHandlerProvider, this.ngaMessageHandlerProvider, this.provideSysUiStateProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.defaultUiControllerProvider, this.googleDefaultUiControllerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIWindowManagerProvider, this.googleAssistLoggerProvider)));
            this.accessibilityManagerWrapperProvider = DoubleCheck.provider(AccessibilityManagerWrapper_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.accessibilityButtonModeObserverProvider = DoubleCheck.provider(AccessibilityButtonModeObserver_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.contextComponentResolverProvider = new DelegateFactory();
            this.provideRecentsImplProvider = RecentsModule_ProvideRecentsImplFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.contextComponentResolverProvider);
            Provider<Recents> provider7 = DoubleCheck.provider(SystemUIGoogleModule_ProvideRecentsFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideRecentsImplProvider, this.provideCommandQueueProvider));
            this.provideRecentsProvider = provider7;
            this.optionalOfRecentsProvider = PresentJdkOptionalInstanceProvider.of(provider7);
            this.shadeControllerImplProvider = DoubleCheck.provider(ShadeControllerImpl_Factory.create(this.provideCommandQueueProvider, this.statusBarStateControllerImplProvider, this.notificationShadeWindowControllerImplProvider, this.statusBarKeyguardViewManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideWindowManagerProvider, this.provideStatusBarProvider, this.assistManagerGoogleProvider, this.setBubblesProvider));
            Provider<LogBuffer> provider8 = DoubleCheck.provider(LogModule_ProvideNotificationsLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNotificationsLogBufferProvider = provider8;
            this.notificationEntryManagerLoggerProvider = NotificationEntryManagerLogger_Factory.create(provider8);
            this.provideNotificationMessagingUtilProvider = DependencyProvider_ProvideNotificationMessagingUtilFactory.create(dependencyProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider);
            this.provideNotificationRemoteInputManagerProvider = new DelegateFactory();
            this.notifCollectionLoggerProvider = NotifCollectionLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.filesProvider = DoubleCheck.provider(Files_Factory.create());
            this.logBufferEulogizerProvider = DoubleCheck.provider(LogBufferEulogizer_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.dumpManagerProvider, this.bindSystemClockProvider, this.filesProvider));
            this.notifCollectionProvider = DoubleCheck.provider(NotifCollection_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideIStatusBarServiceProvider, this.bindSystemClockProvider, this.featureFlagsProvider, this.notifCollectionLoggerProvider, this.logBufferEulogizerProvider, this.dumpManagerProvider));
            this.shadeListBuilderLoggerProvider = ShadeListBuilderLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            DelegateFactory delegateFactory3 = new DelegateFactory();
            this.provideNotificationEntryManagerProvider = delegateFactory3;
            Provider<NotificationInteractionTracker> provider9 = DoubleCheck.provider(NotificationInteractionTracker_Factory.create(this.notificationClickNotifierProvider, delegateFactory3));
            this.notificationInteractionTrackerProvider = provider9;
            this.shadeListBuilderProvider = DoubleCheck.provider(ShadeListBuilder_Factory.create(this.bindSystemClockProvider, this.shadeListBuilderLoggerProvider, this.dumpManagerProvider, provider9));
        }

        private void initialize3(DependencyProvider dependencyProvider, NightDisplayListenerModule nightDisplayListenerModule, UserModule userModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<TaskSurfaceHelper> optional11) {
            Provider<NotifPipeline> provider = DoubleCheck.provider(NotifPipeline_Factory.create(this.notifCollectionProvider, this.shadeListBuilderProvider));
            this.notifPipelineProvider = provider;
            this.provideCommonNotifCollectionProvider = DoubleCheck.provider(NotificationsModule_ProvideCommonNotifCollectionFactory.create(this.featureFlagsProvider, provider, this.provideNotificationEntryManagerProvider));
            NotifBindPipelineLogger_Factory create = NotifBindPipelineLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.notifBindPipelineLoggerProvider = create;
            this.notifBindPipelineProvider = DoubleCheck.provider(NotifBindPipeline_Factory.create(this.provideCommonNotifCollectionProvider, create, GlobalConcurrencyModule_ProvideMainLooperFactory.create()));
            NotifRemoteViewCacheImpl_Factory create2 = NotifRemoteViewCacheImpl_Factory.create(this.provideCommonNotifCollectionProvider);
            this.notifRemoteViewCacheImplProvider = create2;
            this.provideNotifRemoteViewCacheProvider = DoubleCheck.provider(create2);
            this.conversationNotificationManagerProvider = DoubleCheck.provider(ConversationNotificationManager_Factory.create(this.provideNotificationEntryManagerProvider, this.notificationGroupManagerLegacyProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider));
            this.conversationNotificationProcessorProvider = ConversationNotificationProcessor_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideLauncherAppsProvider, this.conversationNotificationManagerProvider);
            this.smartReplyConstantsProvider = DoubleCheck.provider(SmartReplyConstants_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.deviceConfigProxyProvider));
            this.provideActivityManagerWrapperProvider = DoubleCheck.provider(DependencyProvider_ProvideActivityManagerWrapperFactory.create(dependencyProvider));
            this.provideDevicePolicyManagerWrapperProvider = DoubleCheck.provider(DependencyProvider_ProvideDevicePolicyManagerWrapperFactory.create(dependencyProvider));
            this.keyguardDismissUtilProvider = DoubleCheck.provider(KeyguardDismissUtil_Factory.create());
            DelegateFactory delegateFactory = new DelegateFactory();
            this.provideSmartReplyControllerProvider = delegateFactory;
            this.smartReplyInflaterImplProvider = SmartReplyInflaterImpl_Factory.create(this.smartReplyConstantsProvider, this.keyguardDismissUtilProvider, this.provideNotificationRemoteInputManagerProvider, delegateFactory, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider);
            this.smartActionInflaterImplProvider = SmartActionInflaterImpl_Factory.create(this.smartReplyConstantsProvider, this.activityStarterDelegateProvider, this.provideSmartReplyControllerProvider, this.provideHeadsUpManagerPhoneProvider);
            SmartReplyStateInflaterImpl_Factory create3 = SmartReplyStateInflaterImpl_Factory.create(this.smartReplyConstantsProvider, this.provideActivityManagerWrapperProvider, DaggerSysUIGoogleGlobalRootComponent.this.providePackageManagerWrapperProvider, this.provideDevicePolicyManagerWrapperProvider, this.smartReplyInflaterImplProvider, this.smartActionInflaterImplProvider);
            this.smartReplyStateInflaterImplProvider = create3;
            this.notificationContentInflaterProvider = DoubleCheck.provider(NotificationContentInflater_Factory.create(this.provideNotifRemoteViewCacheProvider, this.provideNotificationRemoteInputManagerProvider, this.conversationNotificationProcessorProvider, this.mediaFeatureFlagProvider, this.provideBackgroundExecutorProvider, create3));
            this.notifInflationErrorManagerProvider = DoubleCheck.provider(NotifInflationErrorManager_Factory.create());
            RowContentBindStageLogger_Factory create4 = RowContentBindStageLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.rowContentBindStageLoggerProvider = create4;
            this.rowContentBindStageProvider = DoubleCheck.provider(RowContentBindStage_Factory.create(this.notificationContentInflaterProvider, this.notifInflationErrorManagerProvider, create4));
            this.expandableNotificationRowComponentBuilderProvider = new Provider<ExpandableNotificationRowComponent.Builder>() { // from class: com.google.android.systemui.dagger.DaggerSysUIGoogleGlobalRootComponent.SysUIGoogleSysUIComponentImpl.1
                @Override // javax.inject.Provider
                public ExpandableNotificationRowComponent.Builder get() {
                    return new ExpandableNotificationRowComponentBuilder();
                }
            };
            this.iconBuilderProvider = IconBuilder_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider);
            this.iconManagerProvider = IconManager_Factory.create(this.provideCommonNotifCollectionProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideLauncherAppsProvider, this.iconBuilderProvider);
            this.lowPriorityInflationHelperProvider = DoubleCheck.provider(LowPriorityInflationHelper_Factory.create(this.featureFlagsProvider, this.notificationGroupManagerLegacyProvider, this.rowContentBindStageProvider));
            this.notificationRowBinderImplProvider = DoubleCheck.provider(NotificationRowBinderImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideNotificationMessagingUtilProvider, this.provideNotificationRemoteInputManagerProvider, this.notificationLockscreenUserManagerGoogleProvider, this.notifBindPipelineProvider, this.rowContentBindStageProvider, RowInflaterTask_Factory.create(), this.expandableNotificationRowComponentBuilderProvider, this.iconManagerProvider, this.lowPriorityInflationHelperProvider));
            Provider<ForegroundServiceDismissalFeatureController> provider2 = DoubleCheck.provider(ForegroundServiceDismissalFeatureController_Factory.create(this.deviceConfigProxyProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.foregroundServiceDismissalFeatureControllerProvider = provider2;
            DelegateFactory.setDelegate(this.provideNotificationEntryManagerProvider, DoubleCheck.provider(NotificationsModule_ProvideNotificationEntryManagerFactory.create(this.notificationEntryManagerLoggerProvider, this.notificationGroupManagerLegacyProvider, this.featureFlagsProvider, this.notificationRowBinderImplProvider, this.provideNotificationRemoteInputManagerProvider, this.provideLeakDetectorProvider, provider2, DaggerSysUIGoogleGlobalRootComponent.this.provideIStatusBarServiceProvider)));
            DelegateFactory.setDelegate(this.provideSmartReplyControllerProvider, DoubleCheck.provider(StatusBarDependenciesModule_ProvideSmartReplyControllerFactory.create(this.provideNotificationEntryManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIStatusBarServiceProvider, this.notificationClickNotifierProvider)));
            this.provideHandlerProvider = DependencyProvider_ProvideHandlerFactory.create(dependencyProvider);
            this.remoteInputUriControllerProvider = DoubleCheck.provider(RemoteInputUriController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideIStatusBarServiceProvider));
            Provider<LogBuffer> provider3 = DoubleCheck.provider(LogModule_ProvideNotifInteractionLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNotifInteractionLogBufferProvider = provider3;
            this.actionClickLoggerProvider = ActionClickLogger_Factory.create(provider3);
            DelegateFactory.setDelegate(this.provideNotificationRemoteInputManagerProvider, DoubleCheck.provider(StatusBarDependenciesModule_ProvideNotificationRemoteInputManagerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.notificationLockscreenUserManagerGoogleProvider, this.provideSmartReplyControllerProvider, this.provideNotificationEntryManagerProvider, this.provideStatusBarProvider, this.statusBarStateControllerImplProvider, this.provideHandlerProvider, this.remoteInputUriControllerProvider, this.notificationClickNotifierProvider, this.actionClickLoggerProvider)));
            this.blurUtilsProvider = DoubleCheck.provider(BlurUtils_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideResourcesProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideCrossWindowBlurListenersProvider, this.dumpManagerProvider));
            Provider<LogBuffer> provider4 = DoubleCheck.provider(LogModule_ProvideDozeLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideDozeLogBufferProvider = provider4;
            DozeLogger_Factory create5 = DozeLogger_Factory.create(provider4);
            this.dozeLoggerProvider = create5;
            Provider<DozeLog> provider5 = DoubleCheck.provider(DozeLog_Factory.create(this.keyguardUpdateMonitorProvider, this.dumpManagerProvider, create5));
            this.dozeLogProvider = provider5;
            this.dozeScrimControllerProvider = DoubleCheck.provider(DozeScrimController_Factory.create(this.dozeParametersProvider, provider5));
            this.darkIconDispatcherImplProvider = DoubleCheck.provider(DarkIconDispatcherImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider));
            this.lightBarControllerProvider = DoubleCheck.provider(LightBarController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.darkIconDispatcherImplProvider, this.provideBatteryControllerProvider, this.navigationModeControllerProvider));
            this.builderProvider2 = DelayedWakeLock_Builder_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider);
            this.mediaArtworkProcessorProvider = DoubleCheck.provider(MediaArtworkProcessor_Factory.create());
            MediaControllerFactory_Factory create6 = MediaControllerFactory_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider);
            this.mediaControllerFactoryProvider = create6;
            this.mediaTimeoutListenerProvider = DoubleCheck.provider(MediaTimeoutListener_Factory.create(create6, this.provideMainDelayableExecutorProvider));
            this.mediaBrowserFactoryProvider = MediaBrowserFactory_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider);
            this.resumeMediaBrowserFactoryProvider = ResumeMediaBrowserFactory_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.mediaBrowserFactoryProvider);
            this.mediaResumeListenerProvider = DoubleCheck.provider(MediaResumeListener_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.providesBroadcastDispatcherProvider, this.provideBackgroundExecutorProvider, this.tunerServiceImplProvider, this.resumeMediaBrowserFactoryProvider, this.dumpManagerProvider));
            this.mediaSessionBasedFilterProvider = MediaSessionBasedFilter_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMediaSessionManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, this.provideBackgroundExecutorProvider);
            this.provideLocalBluetoothControllerProvider = DoubleCheck.provider(DependencyProvider_ProvideLocalBluetoothControllerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideBgHandlerProvider));
            LocalMediaManagerFactory_Factory create7 = LocalMediaManagerFactory_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideLocalBluetoothControllerProvider);
            this.localMediaManagerFactoryProvider = create7;
            this.mediaDeviceManagerProvider = MediaDeviceManager_Factory.create(this.mediaControllerFactoryProvider, create7, DaggerSysUIGoogleGlobalRootComponent.this.provideMediaRouter2ManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.dumpManagerProvider);
            this.mediaDataFilterProvider = MediaDataFilter_Factory.create(this.providesBroadcastDispatcherProvider, this.mediaResumeListenerProvider, this.notificationLockscreenUserManagerGoogleProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, this.bindSystemClockProvider);
            this.mediaDataManagerProvider = DoubleCheck.provider(MediaDataManager_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideBackgroundExecutorProvider, this.provideMainDelayableExecutorProvider, this.mediaControllerFactoryProvider, this.dumpManagerProvider, this.providesBroadcastDispatcherProvider, this.mediaTimeoutListenerProvider, this.mediaResumeListenerProvider, this.mediaSessionBasedFilterProvider, this.mediaDeviceManagerProvider, MediaDataCombineLatest_Factory.create(), this.mediaDataFilterProvider, this.activityStarterDelegateProvider, SmartspaceMediaDataProvider_Factory.create(), this.bindSystemClockProvider, this.tunerServiceImplProvider));
            this.provideNotificationMediaManagerProvider = DoubleCheck.provider(StatusBarDependenciesModule_ProvideNotificationMediaManagerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideStatusBarProvider, this.notificationShadeWindowControllerImplProvider, this.provideNotificationEntryManagerProvider, this.mediaArtworkProcessorProvider, this.keyguardBypassControllerProvider, this.notifPipelineProvider, this.notifCollectionProvider, this.featureFlagsProvider, this.provideMainDelayableExecutorProvider, this.deviceConfigProxyProvider, this.mediaDataManagerProvider));
            this.systemSettingsImplProvider = SystemSettingsImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideContentResolverProvider);
            this.provideFaceAuthScreenBrightnessControllerProvider = DoubleCheck.provider(KeyguardModule_ProvideFaceAuthScreenBrightnessControllerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.notificationShadeWindowControllerImplProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideResourcesProvider, this.provideHandlerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideFaceManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.providePackageManagerProvider, this.keyguardUpdateMonitorProvider, this.globalSettingsImplProvider, this.systemSettingsImplProvider, this.dumpManagerProvider));
            this.lockscreenWallpaperProvider = DoubleCheck.provider(LockscreenWallpaper_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideWallpaperManagerProvider, FrameworkServicesModule_ProvideIWallPaperManagerFactory.create(), this.keyguardUpdateMonitorProvider, this.dumpManagerProvider, this.provideNotificationMediaManagerProvider, this.provideFaceAuthScreenBrightnessControllerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider));
            this.liveWallpaperScrimControllerProvider = DoubleCheck.provider(LiveWallpaperScrimController_Factory.create(this.lightBarControllerProvider, this.dozeParametersProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideAlarmManagerProvider, this.keyguardStateControllerImplProvider, this.builderProvider2, this.provideHandlerProvider, FrameworkServicesModule_ProvideIWallPaperManagerFactory.create(), this.lockscreenWallpaperProvider, this.keyguardUpdateMonitorProvider, this.provideConfigurationControllerProvider, this.provideDockManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, this.unlockedScreenOffAnimationControllerProvider));
            this.screenLifecycleProvider = DoubleCheck.provider(ScreenLifecycle_Factory.create());
            this.biometricUnlockControllerProvider = DoubleCheck.provider(BiometricUnlockController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.dozeScrimControllerProvider, this.newKeyguardViewMediatorProvider, this.liveWallpaperScrimControllerProvider, this.shadeControllerImplProvider, this.notificationShadeWindowControllerImplProvider, this.keyguardStateControllerImplProvider, this.provideHandlerProvider, this.keyguardUpdateMonitorProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideResourcesProvider, this.keyguardBypassControllerProvider, this.dozeParametersProvider, this.provideMetricsLoggerProvider, this.dumpManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.providePowerManagerProvider, this.provideNotificationMediaManagerProvider, this.wakefulnessLifecycleProvider, this.screenLifecycleProvider, this.authControllerProvider));
            Provider<Choreographer> provider6 = DoubleCheck.provider(DependencyProvider_ProvidesChoreographerFactory.create(dependencyProvider));
            this.providesChoreographerProvider = provider6;
            this.notificationShadeDepthControllerProvider = DoubleCheck.provider(NotificationShadeDepthController_Factory.create(this.statusBarStateControllerImplProvider, this.blurUtilsProvider, this.biometricUnlockControllerProvider, this.keyguardStateControllerImplProvider, provider6, DaggerSysUIGoogleGlobalRootComponent.this.provideWallpaperManagerProvider, this.notificationShadeWindowControllerImplProvider, this.dozeParametersProvider, this.dumpManagerProvider));
            this.systemActionsProvider = DoubleCheck.provider(SystemActions_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.notificationShadeWindowControllerImplProvider, this.provideStatusBarProvider, this.provideRecentsProvider));
            this.gameModeDndControllerProvider = DoubleCheck.provider(GameModeDndController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideNotificationManagerProvider, this.providesBroadcastDispatcherProvider));
            this.fpsControllerProvider = DoubleCheck.provider(FpsController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider));
            this.recordingControllerProvider = DoubleCheck.provider(RecordingController_Factory.create(this.providesBroadcastDispatcherProvider));
            Provider<ToastController> provider7 = DoubleCheck.provider(ToastController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideConfigurationControllerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideWindowManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider, this.navigationModeControllerProvider));
            this.toastControllerProvider = provider7;
            this.screenRecordControllerProvider = DoubleCheck.provider(ScreenRecordController_Factory.create(this.recordingControllerProvider, this.provideHandlerProvider, this.keyguardDismissUtilProvider, this.provideUserTrackerProvider, provider7));
            this.setTaskSurfaceHelperProvider = InstanceFactory.create(optional11);
            this.gameDashboardUiEventLoggerProvider = GameDashboardUiEventLogger_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider);
            this.shortcutBarControllerProvider = DoubleCheck.provider(ShortcutBarController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideWindowManagerProvider, this.fpsControllerProvider, this.provideConfigurationControllerProvider, this.provideHandlerProvider, this.screenRecordControllerProvider, this.setTaskSurfaceHelperProvider, this.gameDashboardUiEventLoggerProvider, this.toastControllerProvider));
            this.entryPointControllerProvider = DoubleCheck.provider(EntryPointController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideAccessibilityManagerProvider, this.providesBroadcastDispatcherProvider, this.provideCommandQueueProvider, this.gameModeDndControllerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.navigationModeControllerProvider, this.overviewProxyServiceProvider, DaggerSysUIGoogleGlobalRootComponent.this.providePackageManagerProvider, this.shortcutBarControllerProvider, this.toastControllerProvider, this.gameDashboardUiEventLoggerProvider, this.setTaskSurfaceHelperProvider));
            DelegateFactory.setDelegate(this.provideNavigationBarControllerProvider, DoubleCheck.provider(DependencyProvider_ProvideNavigationBarControllerFactory.create(dependencyProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideWindowManagerProvider, this.assistManagerGoogleProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideAccessibilityManagerProvider, this.accessibilityManagerWrapperProvider, this.deviceProvisionedControllerImplProvider, this.provideMetricsLoggerProvider, this.overviewProxyServiceProvider, this.navigationModeControllerProvider, this.accessibilityButtonModeObserverProvider, this.statusBarStateControllerImplProvider, this.provideSysUiStateProvider, this.providesBroadcastDispatcherProvider, this.provideCommandQueueProvider, this.setPipProvider, this.setLegacySplitScreenProvider, this.optionalOfRecentsProvider, this.provideStatusBarProvider, this.shadeControllerImplProvider, this.provideNotificationRemoteInputManagerProvider, this.notificationShadeDepthControllerProvider, this.systemActionsProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider, this.entryPointControllerProvider, this.provideConfigurationControllerProvider, this.provideUserTrackerProvider)));
            this.keyguardStatusViewComponentFactoryProvider = new Provider<KeyguardStatusViewComponent.Factory>() { // from class: com.google.android.systemui.dagger.DaggerSysUIGoogleGlobalRootComponent.SysUIGoogleSysUIComponentImpl.2
                @Override // javax.inject.Provider
                public KeyguardStatusViewComponent.Factory get() {
                    return new KeyguardStatusViewComponentFactory();
                }
            };
            this.keyguardDisplayManagerProvider = KeyguardDisplayManager_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideNavigationBarControllerProvider, this.keyguardStatusViewComponentFactoryProvider, this.provideUiBackgroundExecutorProvider);
            this.keyguardUnlockAnimationControllerProvider = DoubleCheck.provider(KeyguardUnlockAnimationController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.keyguardStateControllerImplProvider, this.newKeyguardViewMediatorProvider, this.statusBarKeyguardViewManagerProvider, this.provideSmartspaceTransitionControllerProvider, this.featureFlagsProvider));
            DelegateFactory.setDelegate(this.newKeyguardViewMediatorProvider, DoubleCheck.provider(KeyguardModule_NewKeyguardViewMediatorFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.falsingCollectorImplProvider, this.provideLockPatternUtilsProvider, this.providesBroadcastDispatcherProvider, this.statusBarKeyguardViewManagerProvider, this.dismissCallbackRegistryProvider, this.keyguardUpdateMonitorProvider, this.dumpManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.providePowerManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideTrustManagerProvider, this.userSwitcherControllerProvider, this.provideUiBackgroundExecutorProvider, this.deviceConfigProxyProvider, this.navigationModeControllerProvider, this.keyguardDisplayManagerProvider, this.dozeParametersProvider, this.statusBarStateControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardUnlockAnimationControllerProvider, this.unlockedScreenOffAnimationControllerProvider, this.notificationShadeDepthControllerProvider)));
            this.providesViewMediatorCallbackProvider = DependencyProvider_ProvidesViewMediatorCallbackFactory.create(dependencyProvider, this.newKeyguardViewMediatorProvider);
            this.keyguardSecurityModelProvider = DoubleCheck.provider(KeyguardSecurityModel_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideResourcesProvider, this.provideLockPatternUtilsProvider, this.keyguardUpdateMonitorProvider));
            this.keyguardBouncerComponentFactoryProvider = new Provider<KeyguardBouncerComponent.Factory>() { // from class: com.google.android.systemui.dagger.DaggerSysUIGoogleGlobalRootComponent.SysUIGoogleSysUIComponentImpl.3
                @Override // javax.inject.Provider
                public KeyguardBouncerComponent.Factory get() {
                    return new KeyguardBouncerComponentFactory();
                }
            };
            this.factoryProvider = KeyguardBouncer_Factory_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.providesViewMediatorCallbackProvider, this.dismissCallbackRegistryProvider, this.falsingCollectorImplProvider, this.keyguardStateControllerImplProvider, this.keyguardUpdateMonitorProvider, this.keyguardBypassControllerProvider, this.provideHandlerProvider, this.keyguardSecurityModelProvider, this.keyguardBouncerComponentFactoryProvider);
            this.factoryProvider2 = KeyguardMessageAreaController_Factory_Factory.create(this.keyguardUpdateMonitorProvider, this.provideConfigurationControllerProvider);
            DelegateFactory.setDelegate(this.statusBarKeyguardViewManagerProvider, DoubleCheck.provider(StatusBarKeyguardViewManager_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.providesViewMediatorCallbackProvider, this.provideLockPatternUtilsProvider, this.statusBarStateControllerImplProvider, this.provideConfigurationControllerProvider, this.keyguardUpdateMonitorProvider, this.navigationModeControllerProvider, this.provideDockManagerProvider, this.notificationShadeWindowControllerImplProvider, this.keyguardStateControllerImplProvider, this.provideFaceAuthScreenBrightnessControllerProvider, this.provideNotificationMediaManagerProvider, this.factoryProvider, this.wakefulnessLifecycleProvider, this.unlockedScreenOffAnimationControllerProvider, this.factoryProvider2)));
            this.lockscreenGestureLoggerProvider = DoubleCheck.provider(LockscreenGestureLogger_Factory.create());
            this.mediaHostStatesManagerProvider = DoubleCheck.provider(MediaHostStatesManager_Factory.create());
            this.mediaViewControllerProvider = MediaViewController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideConfigurationControllerProvider, this.mediaHostStatesManagerProvider);
            Provider<RepeatableExecutor> provider8 = DoubleCheck.provider(SysUIConcurrencyModule_ProvideBackgroundRepeatableExecutorFactory.create(this.provideBackgroundDelayableExecutorProvider));
            this.provideBackgroundRepeatableExecutorProvider = provider8;
            this.seekBarViewModelProvider = SeekBarViewModel_Factory.create(provider8);
            this.mediaOutputDialogFactoryProvider = MediaOutputDialogFactory_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMediaSessionManagerProvider, this.provideLocalBluetoothControllerProvider, this.shadeControllerImplProvider, this.activityStarterDelegateProvider, this.provideNotificationEntryManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider);
            this.mediaCarouselControllerProvider = new DelegateFactory();
            this.mediaControlPanelProvider = MediaControlPanel_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideBackgroundExecutorProvider, this.activityStarterDelegateProvider, this.mediaViewControllerProvider, this.seekBarViewModelProvider, this.mediaDataManagerProvider, this.keyguardDismissUtilProvider, this.mediaOutputDialogFactoryProvider, this.mediaCarouselControllerProvider);
            this.provideVisualStabilityManagerProvider = DoubleCheck.provider(NotificationsModule_ProvideVisualStabilityManagerFactory.create(this.featureFlagsProvider, this.provideNotificationEntryManagerProvider, this.provideHandlerProvider, this.statusBarStateControllerImplProvider, this.wakefulnessLifecycleProvider));
            DelegateFactory.setDelegate(this.mediaCarouselControllerProvider, DoubleCheck.provider(MediaCarouselController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.mediaControlPanelProvider, this.provideVisualStabilityManagerProvider, this.mediaHostStatesManagerProvider, this.activityStarterDelegateProvider, this.bindSystemClockProvider, this.provideMainDelayableExecutorProvider, this.mediaDataManagerProvider, this.provideConfigurationControllerProvider, this.falsingCollectorImplProvider, this.falsingManagerProxyProvider, this.dumpManagerProvider)));
            this.mediaHierarchyManagerProvider = DoubleCheck.provider(MediaHierarchyManager_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.statusBarStateControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardBypassControllerProvider, this.mediaCarouselControllerProvider, this.notificationLockscreenUserManagerGoogleProvider, this.provideConfigurationControllerProvider, this.wakefulnessLifecycleProvider, this.statusBarKeyguardViewManagerProvider));
            Provider<MediaHost> provider9 = DoubleCheck.provider(MediaModule_ProvidesKeyguardMediaHostFactory.create(MediaHost_MediaHostStateHolder_Factory.create(), this.mediaHierarchyManagerProvider, this.mediaDataManagerProvider, this.mediaHostStatesManagerProvider));
            this.providesKeyguardMediaHostProvider = provider9;
            this.keyguardMediaControllerProvider = DoubleCheck.provider(KeyguardMediaController_Factory.create(provider9, this.keyguardBypassControllerProvider, this.statusBarStateControllerImplProvider, this.notificationLockscreenUserManagerGoogleProvider, this.featureFlagsProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideConfigurationControllerProvider));
            this.notificationSectionsFeatureManagerProvider = NotificationSectionsFeatureManager_Factory.create(this.deviceConfigProxyProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider);
            Provider<LogBuffer> provider10 = DoubleCheck.provider(LogModule_ProvideNotificationSectionLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNotificationSectionLogBufferProvider = provider10;
            this.notificationSectionsLoggerProvider = DoubleCheck.provider(NotificationSectionsLogger_Factory.create(provider10));
            this.sectionHeaderControllerSubcomponentBuilderProvider = new Provider<SectionHeaderControllerSubcomponent.Builder>() { // from class: com.google.android.systemui.dagger.DaggerSysUIGoogleGlobalRootComponent.SysUIGoogleSysUIComponentImpl.4
                @Override // javax.inject.Provider
                public SectionHeaderControllerSubcomponent.Builder get() {
                    return new SectionHeaderControllerSubcomponentBuilder();
                }
            };
        }

        private void initialize4(DependencyProvider dependencyProvider, NightDisplayListenerModule nightDisplayListenerModule, UserModule userModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<TaskSurfaceHelper> optional11) {
            Provider<SectionHeaderControllerSubcomponent> provider = DoubleCheck.provider(NotificationSectionHeadersModule_ProvidesIncomingHeaderSubcomponentFactory.create(this.sectionHeaderControllerSubcomponentBuilderProvider));
            this.providesIncomingHeaderSubcomponentProvider = provider;
            this.providesIncomingHeaderControllerProvider = NotificationSectionHeadersModule_ProvidesIncomingHeaderControllerFactory.create(provider);
            Provider<SectionHeaderControllerSubcomponent> provider2 = DoubleCheck.provider(NotificationSectionHeadersModule_ProvidesPeopleHeaderSubcomponentFactory.create(this.sectionHeaderControllerSubcomponentBuilderProvider));
            this.providesPeopleHeaderSubcomponentProvider = provider2;
            this.providesPeopleHeaderControllerProvider = NotificationSectionHeadersModule_ProvidesPeopleHeaderControllerFactory.create(provider2);
            Provider<SectionHeaderControllerSubcomponent> provider3 = DoubleCheck.provider(NotificationSectionHeadersModule_ProvidesAlertingHeaderSubcomponentFactory.create(this.sectionHeaderControllerSubcomponentBuilderProvider));
            this.providesAlertingHeaderSubcomponentProvider = provider3;
            this.providesAlertingHeaderControllerProvider = NotificationSectionHeadersModule_ProvidesAlertingHeaderControllerFactory.create(provider3);
            Provider<SectionHeaderControllerSubcomponent> provider4 = DoubleCheck.provider(NotificationSectionHeadersModule_ProvidesSilentHeaderSubcomponentFactory.create(this.sectionHeaderControllerSubcomponentBuilderProvider));
            this.providesSilentHeaderSubcomponentProvider = provider4;
            NotificationSectionHeadersModule_ProvidesSilentHeaderControllerFactory create = NotificationSectionHeadersModule_ProvidesSilentHeaderControllerFactory.create(provider4);
            this.providesSilentHeaderControllerProvider = create;
            this.notificationSectionsManagerProvider = NotificationSectionsManager_Factory.create(this.statusBarStateControllerImplProvider, this.provideConfigurationControllerProvider, this.keyguardMediaControllerProvider, this.notificationSectionsFeatureManagerProvider, this.notificationSectionsLoggerProvider, this.providesIncomingHeaderControllerProvider, this.providesPeopleHeaderControllerProvider, this.providesAlertingHeaderControllerProvider, create);
            Provider<AmbientState> provider5 = DoubleCheck.provider(AmbientState_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.notificationSectionsManagerProvider, this.keyguardBypassControllerProvider));
            this.ambientStateProvider = provider5;
            this.lockscreenShadeTransitionControllerProvider = DoubleCheck.provider(LockscreenShadeTransitionController_Factory.create(this.statusBarStateControllerImplProvider, this.lockscreenGestureLoggerProvider, this.keyguardBypassControllerProvider, this.notificationLockscreenUserManagerGoogleProvider, this.falsingCollectorImplProvider, provider5, DaggerSysUIGoogleGlobalRootComponent.this.provideDisplayMetricsProvider, this.mediaHierarchyManagerProvider, this.liveWallpaperScrimControllerProvider, this.notificationShadeDepthControllerProvider, this.featureFlagsProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideConfigurationControllerProvider, this.falsingManagerProxyProvider));
            this.udfpsHapticsSimulatorProvider = DoubleCheck.provider(UdfpsHapticsSimulator_Factory.create(this.commandRegistryProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideVibratorProvider, this.keyguardUpdateMonitorProvider));
            Provider<UdfpsHbmProvider> provider6 = DoubleCheck.provider(SystemUIGoogleModule_ProvideUdfpsHbmProviderFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.provideUiBackgroundExecutorProvider, UdfpsGhbmProvider_Factory.create(), UdfpsLhbmProvider_Factory.create(), this.authControllerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideDisplayManagerProvider));
            this.provideUdfpsHbmProvider = provider6;
            this.optionalOfUdfpsHbmProvider = PresentJdkOptionalInstanceProvider.of(provider6);
            this.udfpsControllerProvider = DoubleCheck.provider(UdfpsController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideExecutionProvider, this.providerLayoutInflaterProvider, DaggerSysUIGoogleGlobalRootComponent.this.providesFingerprintManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideWindowManagerProvider, this.statusBarStateControllerImplProvider, this.provideMainDelayableExecutorProvider, this.provideStatusBarProvider, this.statusBarKeyguardViewManagerProvider, this.dumpManagerProvider, this.keyguardUpdateMonitorProvider, this.newKeyguardViewMediatorProvider, this.falsingManagerProxyProvider, DaggerSysUIGoogleGlobalRootComponent.this.providePowerManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideAccessibilityManagerProvider, this.lockscreenShadeTransitionControllerProvider, this.screenLifecycleProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideVibratorProvider, this.udfpsHapticsSimulatorProvider, this.optionalOfUdfpsHbmProvider, this.keyguardStateControllerImplProvider, this.keyguardBypassControllerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideDisplayManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.provideConfigurationControllerProvider));
            this.sidefpsControllerProvider = DoubleCheck.provider(SidefpsController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.providerLayoutInflaterProvider, DaggerSysUIGoogleGlobalRootComponent.this.providesFingerprintManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideWindowManagerProvider, this.provideMainDelayableExecutorProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideDisplayManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider));
            DelegateFactory.setDelegate(this.authControllerProvider, DoubleCheck.provider(AuthController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideActivityTaskManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideWindowManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.providesFingerprintManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideFaceManagerProvider, this.udfpsControllerProvider, this.sidefpsControllerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideDisplayManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider)));
            DelegateFactory.setDelegate(this.keyguardUpdateMonitorProvider, DoubleCheck.provider(KeyguardUpdateMonitor_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.providesBroadcastDispatcherProvider, this.dumpManagerProvider, this.ringerModeTrackerImplProvider, this.provideBackgroundExecutorProvider, this.statusBarStateControllerImplProvider, this.provideLockPatternUtilsProvider, this.authControllerProvider, this.telephonyListenerManagerProvider, this.featureFlagsProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideVibratorProvider)));
            DelegateFactory.setDelegate(this.smartSpaceControllerProvider, DoubleCheck.provider(SmartSpaceController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.keyguardUpdateMonitorProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideAlarmManagerProvider, this.dumpManagerProvider)));
            this.wallpaperNotifierProvider = WallpaperNotifier_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideNotificationEntryManagerProvider, this.providesBroadcastDispatcherProvider);
            this.statusBarIconControllerImplProvider = DoubleCheck.provider(StatusBarIconControllerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider, this.provideDemoModeControllerProvider));
            this.builderProvider3 = WakeLock_Builder_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider);
            this.keyguardIndicationControllerGoogleProvider = DoubleCheck.provider(KeyguardIndicationControllerGoogle_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.builderProvider3, this.keyguardStateControllerImplProvider, this.statusBarStateControllerImplProvider, this.keyguardUpdateMonitorProvider, this.provideDockManagerProvider, this.providesBroadcastDispatcherProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideDevicePolicyManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIBatteryStatsProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUserManagerProvider, this.tunerServiceImplProvider, this.deviceConfigProxyProvider, this.provideMainDelayableExecutorProvider, this.falsingManagerProxyProvider, this.provideLockPatternUtilsProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIActivityManagerProvider, this.keyguardBypassControllerProvider));
            Provider<ReverseChargingViewController> provider7 = DoubleCheck.provider(ReverseChargingViewController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideBatteryControllerProvider, this.provideStatusBarProvider, this.statusBarIconControllerImplProvider, this.providesBroadcastDispatcherProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, this.keyguardIndicationControllerGoogleProvider));
            this.reverseChargingViewControllerProvider = provider7;
            this.provideReverseChargingViewControllerOptionalProvider = DoubleCheck.provider(StatusBarGoogleModule_ProvideReverseChargingViewControllerOptionalFactory.create(this.provideBatteryControllerProvider, provider7));
            this.provideNotificationListenerProvider = DoubleCheck.provider(StatusBarDependenciesModule_ProvideNotificationListenerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideNotificationManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider));
            Provider<HighPriorityProvider> provider8 = DoubleCheck.provider(HighPriorityProvider_Factory.create(this.peopleNotificationIdentifierImplProvider, this.provideGroupMembershipManagerProvider));
            this.highPriorityProvider = provider8;
            this.notificationRankingManagerProvider = NotificationRankingManager_Factory.create(this.provideNotificationMediaManagerProvider, this.notificationGroupManagerLegacyProvider, this.provideHeadsUpManagerPhoneProvider, this.notificationFilterProvider, this.notificationEntryManagerLoggerProvider, this.notificationSectionsFeatureManagerProvider, this.peopleNotificationIdentifierImplProvider, provider8, this.keyguardEnvironmentImplProvider);
            this.targetSdkResolverProvider = DoubleCheck.provider(TargetSdkResolver_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            GroupCoalescerLogger_Factory create2 = GroupCoalescerLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.groupCoalescerLoggerProvider = create2;
            this.groupCoalescerProvider = GroupCoalescer_Factory.create(this.provideMainDelayableExecutorProvider, this.bindSystemClockProvider, create2);
            SharedCoordinatorLogger_Factory create3 = SharedCoordinatorLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.sharedCoordinatorLoggerProvider = create3;
            this.hideNotifsForOtherUsersCoordinatorProvider = HideNotifsForOtherUsersCoordinator_Factory.create(this.notificationLockscreenUserManagerGoogleProvider, create3);
            this.keyguardCoordinatorProvider = DoubleCheck.provider(KeyguardCoordinator_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideHandlerProvider, this.keyguardStateControllerImplProvider, this.notificationLockscreenUserManagerGoogleProvider, this.providesBroadcastDispatcherProvider, this.statusBarStateControllerImplProvider, this.keyguardUpdateMonitorProvider, this.highPriorityProvider));
            this.providesAlertingHeaderNodeControllerProvider = NotificationSectionHeadersModule_ProvidesAlertingHeaderNodeControllerFactory.create(this.providesAlertingHeaderSubcomponentProvider);
            NotificationSectionHeadersModule_ProvidesSilentHeaderNodeControllerFactory create4 = NotificationSectionHeadersModule_ProvidesSilentHeaderNodeControllerFactory.create(this.providesSilentHeaderSubcomponentProvider);
            this.providesSilentHeaderNodeControllerProvider = create4;
            this.rankingCoordinatorProvider = DoubleCheck.provider(RankingCoordinator_Factory.create(this.statusBarStateControllerImplProvider, this.highPriorityProvider, this.providesAlertingHeaderNodeControllerProvider, create4));
            this.appOpsCoordinatorProvider = DoubleCheck.provider(AppOpsCoordinator_Factory.create(this.foregroundServiceControllerProvider, this.appOpsControllerImplProvider, this.provideMainDelayableExecutorProvider));
            this.deviceProvisionedCoordinatorProvider = DoubleCheck.provider(DeviceProvisionedCoordinator_Factory.create(this.deviceProvisionedControllerImplProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIPackageManagerProvider));
            this.provideINotificationManagerProvider = DoubleCheck.provider(DependencyProvider_ProvideINotificationManagerFactory.create(dependencyProvider));
            this.zenModeControllerImplProvider = DoubleCheck.provider(ZenModeControllerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.providesBroadcastDispatcherProvider));
            Provider<Optional<BubblesManager>> provider9 = DoubleCheck.provider(SystemUIModule_ProvideBubblesManagerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.setBubblesProvider, this.notificationShadeWindowControllerImplProvider, this.statusBarStateControllerImplProvider, this.shadeControllerImplProvider, this.provideConfigurationControllerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIStatusBarServiceProvider, this.provideINotificationManagerProvider, this.notificationInterruptStateProviderImplProvider, this.zenModeControllerImplProvider, this.notificationLockscreenUserManagerGoogleProvider, this.notificationGroupManagerLegacyProvider, this.provideNotificationEntryManagerProvider, this.notifPipelineProvider, this.provideSysUiStateProvider, this.featureFlagsProvider, this.dumpManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider));
            this.provideBubblesManagerProvider = provider9;
            this.bubbleCoordinatorProvider = DoubleCheck.provider(BubbleCoordinator_Factory.create(provider9, this.setBubblesProvider, this.notifCollectionProvider));
            this.headsUpViewBinderProvider = DoubleCheck.provider(HeadsUpViewBinder_Factory.create(this.provideNotificationMessagingUtilProvider, this.rowContentBindStageProvider));
            NotificationSectionHeadersModule_ProvidesIncomingHeaderNodeControllerFactory create5 = NotificationSectionHeadersModule_ProvidesIncomingHeaderNodeControllerFactory.create(this.providesIncomingHeaderSubcomponentProvider);
            this.providesIncomingHeaderNodeControllerProvider = create5;
            this.headsUpCoordinatorProvider = DoubleCheck.provider(HeadsUpCoordinator_Factory.create(this.provideHeadsUpManagerPhoneProvider, this.headsUpViewBinderProvider, this.notificationInterruptStateProviderImplProvider, this.provideNotificationRemoteInputManagerProvider, create5));
            NotificationSectionHeadersModule_ProvidesPeopleHeaderNodeControllerFactory create6 = NotificationSectionHeadersModule_ProvidesPeopleHeaderNodeControllerFactory.create(this.providesPeopleHeaderSubcomponentProvider);
            this.providesPeopleHeaderNodeControllerProvider = create6;
            this.conversationCoordinatorProvider = DoubleCheck.provider(ConversationCoordinator_Factory.create(this.peopleNotificationIdentifierImplProvider, create6));
            this.preparationCoordinatorLoggerProvider = PreparationCoordinatorLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.notifInflaterImplProvider = DoubleCheck.provider(NotifInflaterImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideIStatusBarServiceProvider, this.notifCollectionProvider, this.notifInflationErrorManagerProvider, this.notifPipelineProvider));
            Provider<NotifViewBarn> provider10 = DoubleCheck.provider(NotifViewBarn_Factory.create());
            this.notifViewBarnProvider = provider10;
            this.preparationCoordinatorProvider = DoubleCheck.provider(PreparationCoordinator_Factory.create(this.preparationCoordinatorLoggerProvider, this.notifInflaterImplProvider, this.notifInflationErrorManagerProvider, provider10, DaggerSysUIGoogleGlobalRootComponent.this.provideIStatusBarServiceProvider));
            this.mediaCoordinatorProvider = MediaCoordinator_Factory.create(this.mediaFeatureFlagProvider);
            Provider<BcSmartspaceDataPlugin> provider11 = DoubleCheck.provider(SystemUIGoogleModule_ProvideBcSmartspaceDataPluginFactory.create());
            this.provideBcSmartspaceDataPluginProvider = provider11;
            this.optionalOfBcSmartspaceDataPluginProvider = PresentJdkOptionalInstanceProvider.of(provider11);
            Provider<LockscreenSmartspaceController> provider12 = DoubleCheck.provider(LockscreenSmartspaceController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.featureFlagsProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideSmartspaceManagerProvider, this.activityStarterDelegateProvider, this.falsingManagerProxyProvider, this.secureSettingsImplProvider, this.provideUserTrackerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideContentResolverProvider, this.provideConfigurationControllerProvider, this.statusBarStateControllerImplProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideExecutionProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.optionalOfBcSmartspaceDataPluginProvider));
            this.lockscreenSmartspaceControllerProvider = provider12;
            this.smartspaceDedupingCoordinatorProvider = DoubleCheck.provider(SmartspaceDedupingCoordinator_Factory.create(this.statusBarStateControllerImplProvider, provider12, this.provideNotificationEntryManagerProvider, this.notificationLockscreenUserManagerGoogleProvider, this.notifPipelineProvider, this.provideMainDelayableExecutorProvider, this.bindSystemClockProvider));
            Provider<VisualStabilityCoordinator> provider13 = DoubleCheck.provider(VisualStabilityCoordinator_Factory.create(this.provideHeadsUpManagerPhoneProvider, this.wakefulnessLifecycleProvider, this.statusBarStateControllerImplProvider, this.provideDelayableExecutorProvider));
            this.visualStabilityCoordinatorProvider = provider13;
            this.notifCoordinatorsProvider = DoubleCheck.provider(NotifCoordinators_Factory.create(this.dumpManagerProvider, this.featureFlagsProvider, this.hideNotifsForOtherUsersCoordinatorProvider, this.keyguardCoordinatorProvider, this.rankingCoordinatorProvider, this.appOpsCoordinatorProvider, this.deviceProvisionedCoordinatorProvider, this.bubbleCoordinatorProvider, this.headsUpCoordinatorProvider, this.conversationCoordinatorProvider, this.preparationCoordinatorProvider, this.mediaCoordinatorProvider, this.smartspaceDedupingCoordinatorProvider, provider13));
            this.shadeViewDifferLoggerProvider = ShadeViewDifferLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.notificationWakeUpCoordinatorProvider = DoubleCheck.provider(NotificationWakeUpCoordinator_Factory.create(this.provideHeadsUpManagerPhoneProvider, this.statusBarStateControllerImplProvider, this.keyguardBypassControllerProvider, this.dozeParametersProvider, this.unlockedScreenOffAnimationControllerProvider));
            AnonymousClass5 r1 = new Provider<InjectionInflationController.ViewInstanceCreator.Factory>() { // from class: com.google.android.systemui.dagger.DaggerSysUIGoogleGlobalRootComponent.SysUIGoogleSysUIComponentImpl.5
                @Override // javax.inject.Provider
                public InjectionInflationController.ViewInstanceCreator.Factory get() {
                    return new ViewInstanceCreatorFactory();
                }
            };
            this.createViewInstanceCreatorFactoryProvider = r1;
            this.injectionInflationControllerProvider = DoubleCheck.provider(InjectionInflationController_Factory.create(r1));
            this.notificationShelfComponentBuilderProvider = new Provider<NotificationShelfComponent.Builder>() { // from class: com.google.android.systemui.dagger.DaggerSysUIGoogleGlobalRootComponent.SysUIGoogleSysUIComponentImpl.6
                @Override // javax.inject.Provider
                public NotificationShelfComponent.Builder get() {
                    return new NotificationShelfComponentBuilder();
                }
            };
            this.superStatusBarViewFactoryProvider = DoubleCheck.provider(SuperStatusBarViewFactory_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.injectionInflationControllerProvider, this.notificationShelfComponentBuilderProvider));
            this.statusBarContentInsetsProvider = DoubleCheck.provider(StatusBarContentInsetsProvider_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideConfigurationControllerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideWindowManagerProvider, this.dumpManagerProvider));
            this.statusBarWindowControllerProvider = DoubleCheck.provider(StatusBarWindowController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideWindowManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIWindowManagerProvider, this.superStatusBarViewFactoryProvider, this.statusBarContentInsetsProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideResourcesProvider));
            this.notificationIconAreaControllerProvider = DoubleCheck.provider(NotificationIconAreaController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.statusBarStateControllerImplProvider, this.notificationWakeUpCoordinatorProvider, this.keyguardBypassControllerProvider, this.provideNotificationMediaManagerProvider, this.provideNotificationListenerProvider, this.dozeParametersProvider, this.setBubblesProvider, this.provideDemoModeControllerProvider, this.darkIconDispatcherImplProvider, this.statusBarWindowControllerProvider, this.unlockedScreenOffAnimationControllerProvider));
            ShadeViewManagerFactory_Factory create7 = ShadeViewManagerFactory_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.shadeViewDifferLoggerProvider, this.notifViewBarnProvider, this.notificationIconAreaControllerProvider);
            this.shadeViewManagerFactoryProvider = create7;
            this.notifPipelineInitializerProvider = DoubleCheck.provider(NotifPipelineInitializer_Factory.create(this.notifPipelineProvider, this.groupCoalescerProvider, this.notifCollectionProvider, this.shadeListBuilderProvider, this.notifCoordinatorsProvider, this.notifInflaterImplProvider, this.dumpManagerProvider, create7, this.featureFlagsProvider));
            this.notifBindPipelineInitializerProvider = NotifBindPipelineInitializer_Factory.create(this.notifBindPipelineProvider, this.rowContentBindStageProvider);
            this.provideNotificationGroupAlertTransferHelperProvider = DoubleCheck.provider(StatusBarPhoneDependenciesModule_ProvideNotificationGroupAlertTransferHelperFactory.create(this.rowContentBindStageProvider));
            this.headsUpControllerProvider = DoubleCheck.provider(HeadsUpController_Factory.create(this.headsUpViewBinderProvider, this.notificationInterruptStateProviderImplProvider, this.provideHeadsUpManagerPhoneProvider, this.provideNotificationRemoteInputManagerProvider, this.statusBarStateControllerImplProvider, this.provideVisualStabilityManagerProvider, this.provideNotificationListenerProvider));
            NotificationClickerLogger_Factory create8 = NotificationClickerLogger_Factory.create(this.provideNotifInteractionLogBufferProvider);
            this.notificationClickerLoggerProvider = create8;
            this.builderProvider4 = NotificationClicker_Builder_Factory.create(create8);
            this.animatedImageNotificationManagerProvider = DoubleCheck.provider(AnimatedImageNotificationManager_Factory.create(this.provideNotificationEntryManagerProvider, this.provideHeadsUpManagerPhoneProvider, this.statusBarStateControllerImplProvider));
            Provider<PeopleSpaceWidgetManager> provider14 = DoubleCheck.provider(PeopleSpaceWidgetManager_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideLauncherAppsProvider, this.provideNotificationEntryManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.providePackageManagerProvider, this.setBubblesProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUserManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideNotificationManagerProvider, this.providesBroadcastDispatcherProvider, this.provideBackgroundExecutorProvider));
            this.peopleSpaceWidgetManagerProvider = provider14;
            this.notificationsControllerImplProvider = DoubleCheck.provider(NotificationsControllerImpl_Factory.create(this.featureFlagsProvider, this.provideNotificationListenerProvider, this.provideNotificationEntryManagerProvider, this.notificationRankingManagerProvider, this.notifPipelineProvider, this.targetSdkResolverProvider, this.notifPipelineInitializerProvider, this.notifBindPipelineInitializerProvider, this.deviceProvisionedControllerImplProvider, this.notificationRowBinderImplProvider, this.remoteInputUriControllerProvider, this.notificationGroupManagerLegacyProvider, this.provideNotificationGroupAlertTransferHelperProvider, this.provideHeadsUpManagerPhoneProvider, this.headsUpControllerProvider, this.headsUpViewBinderProvider, this.builderProvider4, this.animatedImageNotificationManagerProvider, provider14));
            this.notificationsControllerStubProvider = NotificationsControllerStub_Factory.create(this.provideNotificationListenerProvider);
            this.provideNotificationsControllerProvider = DoubleCheck.provider(NotificationsModule_ProvideNotificationsControllerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.notificationsControllerImplProvider, this.notificationsControllerStubProvider));
            this.provideAutoHideControllerProvider = DoubleCheck.provider(DependencyProvider_ProvideAutoHideControllerFactory.create(dependencyProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIWindowManagerProvider));
            this.carrierConfigTrackerProvider = DoubleCheck.provider(CarrierConfigTracker_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.callbackHandlerProvider = CallbackHandler_Factory.create(GlobalConcurrencyModule_ProvideMainLooperFactory.create());
            this.wifiPickerTrackerFactoryProvider = DoubleCheck.provider(AccessPointControllerImpl_WifiPickerTrackerFactory_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideWifiManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideConnectivityManagagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideNetworkScoreManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.provideBgHandlerProvider));
            this.provideAccessPointControllerImplProvider = DoubleCheck.provider(StatusBarPolicyModule_ProvideAccessPointControllerImplFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideUserManagerProvider, this.provideUserTrackerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, this.wifiPickerTrackerFactoryProvider));
            this.networkControllerImplProvider = DoubleCheck.provider(NetworkControllerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideBgLooperProvider, this.provideBackgroundExecutorProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideSubcriptionManagerProvider, this.callbackHandlerProvider, this.deviceProvisionedControllerImplProvider, this.providesBroadcastDispatcherProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideConnectivityManagagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideTelephonyManagerProvider, this.telephonyListenerManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideWifiManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideNetworkScoreManagerProvider, this.provideAccessPointControllerImplProvider, this.provideDemoModeControllerProvider, this.carrierConfigTrackerProvider, this.featureFlagsProvider, this.dumpManagerProvider));
            this.securityControllerImplProvider = DoubleCheck.provider(SecurityControllerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideBgHandlerProvider, this.providesBroadcastDispatcherProvider, this.provideBackgroundExecutorProvider));
            this.statusBarSignalPolicyProvider = DoubleCheck.provider(StatusBarSignalPolicy_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.statusBarIconControllerImplProvider, this.carrierConfigTrackerProvider, this.networkControllerImplProvider, this.securityControllerImplProvider, this.tunerServiceImplProvider, this.featureFlagsProvider));
            this.notificationRoundnessManagerProvider = DoubleCheck.provider(NotificationRoundnessManager_Factory.create(this.keyguardBypassControllerProvider, this.notificationSectionsFeatureManagerProvider));
            this.pulseExpansionHandlerProvider = DoubleCheck.provider(PulseExpansionHandler_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.notificationWakeUpCoordinatorProvider, this.keyguardBypassControllerProvider, this.provideHeadsUpManagerPhoneProvider, this.notificationRoundnessManagerProvider, this.provideConfigurationControllerProvider, this.statusBarStateControllerImplProvider, this.falsingManagerProxyProvider, this.lockscreenShadeTransitionControllerProvider, this.falsingCollectorImplProvider));
            this.dynamicPrivacyControllerProvider = DoubleCheck.provider(DynamicPrivacyController_Factory.create(this.notificationLockscreenUserManagerGoogleProvider, this.keyguardStateControllerImplProvider, this.statusBarStateControllerImplProvider));
            this.bypassHeadsUpNotifierProvider = DoubleCheck.provider(BypassHeadsUpNotifier_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.keyguardBypassControllerProvider, this.statusBarStateControllerImplProvider, this.provideHeadsUpManagerPhoneProvider, this.notificationLockscreenUserManagerGoogleProvider, this.provideNotificationMediaManagerProvider, this.provideNotificationEntryManagerProvider, this.tunerServiceImplProvider));
            this.remoteInputQuickSettingsDisablerProvider = DoubleCheck.provider(RemoteInputQuickSettingsDisabler_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideConfigurationControllerProvider, this.provideCommandQueueProvider));
            this.channelEditorDialogControllerProvider = DoubleCheck.provider(ChannelEditorDialogController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideINotificationManagerProvider, ChannelEditorDialog_Builder_Factory.create()));
            this.assistantFeedbackControllerProvider = DoubleCheck.provider(AssistantFeedbackController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.deviceConfigProxyProvider));
            this.provideOnUserInteractionCallbackProvider = DoubleCheck.provider(NotificationsModule_ProvideOnUserInteractionCallbackFactory.create(this.featureFlagsProvider, this.provideHeadsUpManagerPhoneProvider, this.statusBarStateControllerImplProvider, this.notifPipelineProvider, this.notifCollectionProvider, this.visualStabilityCoordinatorProvider, this.provideNotificationEntryManagerProvider, this.provideVisualStabilityManagerProvider, this.provideGroupMembershipManagerProvider));
            this.provideNotificationGutsManagerProvider = DoubleCheck.provider(NotificationsModule_ProvideNotificationGutsManagerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideStatusBarProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.provideBgHandlerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideAccessibilityManagerProvider, this.highPriorityProvider, this.provideINotificationManagerProvider, this.provideNotificationEntryManagerProvider, this.peopleSpaceWidgetManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideLauncherAppsProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideShortcutManagerProvider, this.channelEditorDialogControllerProvider, this.provideUserTrackerProvider, this.assistantFeedbackControllerProvider, this.provideBubblesManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider, this.provideOnUserInteractionCallbackProvider, this.shadeControllerImplProvider));
            this.expansionStateLoggerProvider = NotificationLogger_ExpansionStateLogger_Factory.create(this.provideUiBackgroundExecutorProvider);
            Provider<NotificationPanelLogger> provider15 = DoubleCheck.provider(NotificationsModule_ProvideNotificationPanelLoggerFactory.create());
            this.provideNotificationPanelLoggerProvider = provider15;
            this.provideNotificationLoggerProvider = DoubleCheck.provider(NotificationsModule_ProvideNotificationLoggerFactory.create(this.provideNotificationListenerProvider, this.provideUiBackgroundExecutorProvider, this.provideNotificationEntryManagerProvider, this.statusBarStateControllerImplProvider, this.expansionStateLoggerProvider, provider15));
        }

        private void initialize5(DependencyProvider dependencyProvider, NightDisplayListenerModule nightDisplayListenerModule, UserModule userModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<TaskSurfaceHelper> optional11) {
            this.foregroundServiceSectionControllerProvider = DoubleCheck.provider(ForegroundServiceSectionController_Factory.create(this.provideNotificationEntryManagerProvider, this.foregroundServiceDismissalFeatureControllerProvider));
            this.dynamicChildBindControllerProvider = DynamicChildBindController_Factory.create(this.rowContentBindStageProvider);
            this.provideNotificationViewHierarchyManagerProvider = DoubleCheck.provider(StatusBarDependenciesModule_ProvideNotificationViewHierarchyManagerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.notificationLockscreenUserManagerGoogleProvider, this.notificationGroupManagerLegacyProvider, this.provideVisualStabilityManagerProvider, this.statusBarStateControllerImplProvider, this.provideNotificationEntryManagerProvider, this.keyguardBypassControllerProvider, this.setBubblesProvider, this.dynamicPrivacyControllerProvider, this.foregroundServiceSectionControllerProvider, this.dynamicChildBindControllerProvider, this.lowPriorityInflationHelperProvider, this.assistantFeedbackControllerProvider));
            this.vibratorHelperProvider = DoubleCheck.provider(VibratorHelper_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.accessibilityButtonTargetsObserverProvider = DoubleCheck.provider(AccessibilityButtonTargetsObserver_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.provideAccessibilityFloatingMenuControllerProvider = DoubleCheck.provider(DependencyProvider_ProvideAccessibilityFloatingMenuControllerFactory.create(dependencyProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.accessibilityButtonTargetsObserverProvider, this.accessibilityButtonModeObserverProvider, this.keyguardUpdateMonitorProvider));
            this.provideKeyguardLiftControllerProvider = DoubleCheck.provider(KeyguardModule_ProvideKeyguardLiftControllerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.statusBarStateControllerImplProvider, this.asyncSensorManagerProvider, this.keyguardUpdateMonitorProvider, this.dumpManagerProvider));
            this.dozeServiceHostProvider = DoubleCheck.provider(DozeServiceHost_Factory.create(this.dozeLogProvider, DaggerSysUIGoogleGlobalRootComponent.this.providePowerManagerProvider, this.wakefulnessLifecycleProvider, this.statusBarStateControllerImplProvider, this.deviceProvisionedControllerImplProvider, this.provideHeadsUpManagerPhoneProvider, this.provideBatteryControllerProvider, this.liveWallpaperScrimControllerProvider, this.biometricUnlockControllerProvider, this.newKeyguardViewMediatorProvider, this.assistManagerGoogleProvider, this.dozeScrimControllerProvider, this.keyguardUpdateMonitorProvider, this.pulseExpansionHandlerProvider, this.notificationShadeWindowControllerImplProvider, this.notificationWakeUpCoordinatorProvider, this.authControllerProvider, this.notificationIconAreaControllerProvider));
            this.screenPinningRequestProvider = ScreenPinningRequest_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.optionalOfLazyOfStatusBarProvider);
            this.volumeDialogControllerImplProvider = DoubleCheck.provider(VolumeDialogControllerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.providesBroadcastDispatcherProvider, this.ringerModeTrackerImplProvider, ThreadFactoryImpl_Factory.create(), DaggerSysUIGoogleGlobalRootComponent.this.provideAudioManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideNotificationManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideOptionalVibratorProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIAudioServiceProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideAccessibilityManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.providePackageManagerProvider, this.wakefulnessLifecycleProvider));
            this.volumeDialogComponentProvider = DoubleCheck.provider(VolumeDialogComponent_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.newKeyguardViewMediatorProvider, this.volumeDialogControllerImplProvider, this.provideDemoModeControllerProvider));
            this.statusBarComponentBuilderProvider = new Provider<StatusBarComponent.Builder>() { // from class: com.google.android.systemui.dagger.DaggerSysUIGoogleGlobalRootComponent.SysUIGoogleSysUIComponentImpl.7
                @Override // javax.inject.Provider
                public StatusBarComponent.Builder get() {
                    return new StatusBarComponentBuilder();
                }
            };
            this.lightsOutNotifControllerProvider = DoubleCheck.provider(LightsOutNotifController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideWindowManagerProvider, this.provideNotificationEntryManagerProvider, this.provideCommandQueueProvider));
            this.provideGroupExpansionManagerProvider = DoubleCheck.provider(NotificationsModule_ProvideGroupExpansionManagerFactory.create(this.featureFlagsProvider, this.provideGroupMembershipManagerProvider, this.notificationGroupManagerLegacyProvider));
            this.statusBarRemoteInputCallbackProvider = DoubleCheck.provider(StatusBarRemoteInputCallback_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideGroupExpansionManagerProvider, this.notificationLockscreenUserManagerGoogleProvider, this.keyguardStateControllerImplProvider, this.statusBarStateControllerImplProvider, this.statusBarKeyguardViewManagerProvider, this.activityStarterDelegateProvider, this.shadeControllerImplProvider, this.provideCommandQueueProvider, this.actionClickLoggerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider));
            this.activityIntentHelperProvider = DoubleCheck.provider(ActivityIntentHelper_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.statusBarNotificationActivityStarterLoggerProvider = StatusBarNotificationActivityStarterLogger_Factory.create(this.provideNotifInteractionLogBufferProvider);
            this.builderProvider5 = DoubleCheck.provider(StatusBarNotificationActivityStarter_Builder_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.provideUiBackgroundExecutorProvider, this.provideNotificationEntryManagerProvider, this.notifPipelineProvider, this.provideHeadsUpManagerPhoneProvider, this.activityStarterDelegateProvider, this.notificationClickNotifierProvider, this.statusBarStateControllerImplProvider, this.statusBarKeyguardViewManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideKeyguardManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIDreamManagerProvider, this.provideBubblesManagerProvider, this.assistManagerGoogleProvider, this.provideNotificationRemoteInputManagerProvider, this.provideGroupMembershipManagerProvider, this.notificationLockscreenUserManagerGoogleProvider, this.shadeControllerImplProvider, this.keyguardStateControllerImplProvider, this.notificationInterruptStateProviderImplProvider, this.provideLockPatternUtilsProvider, this.statusBarRemoteInputCallbackProvider, this.activityIntentHelperProvider, this.featureFlagsProvider, this.provideMetricsLoggerProvider, this.statusBarNotificationActivityStarterLoggerProvider, this.provideOnUserInteractionCallbackProvider));
            this.initControllerProvider = DoubleCheck.provider(InitController_Factory.create());
            this.provideTimeTickHandlerProvider = DoubleCheck.provider(DependencyProvider_ProvideTimeTickHandlerFactory.create(dependencyProvider));
            this.pluginDependencyProvider = DoubleCheck.provider(PluginDependencyProvider_Factory.create(this.providePluginManagerProvider));
            this.userInfoControllerImplProvider = DoubleCheck.provider(UserInfoControllerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.castControllerImplProvider = DoubleCheck.provider(CastControllerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.dumpManagerProvider));
            this.hotspotControllerImplProvider = DoubleCheck.provider(HotspotControllerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.provideBgHandlerProvider));
            this.bluetoothControllerImplProvider = DoubleCheck.provider(BluetoothControllerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.dumpManagerProvider, this.provideBgLooperProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.provideLocalBluetoothControllerProvider));
            this.nextAlarmControllerImplProvider = DoubleCheck.provider(NextAlarmControllerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideAlarmManagerProvider, this.providesBroadcastDispatcherProvider, this.dumpManagerProvider));
            this.rotationLockControllerImplProvider = DoubleCheck.provider(RotationLockControllerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.provideDataSaverControllerProvider = DoubleCheck.provider(DependencyProvider_ProvideDataSaverControllerFactory.create(dependencyProvider, this.networkControllerImplProvider));
            this.locationControllerImplProvider = DoubleCheck.provider(LocationControllerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.appOpsControllerImplProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.provideBgHandlerProvider, this.providesBroadcastDispatcherProvider, this.bootCompleteCacheImplProvider, this.provideUserTrackerProvider));
            this.provideSensorPrivacyControllerProvider = DoubleCheck.provider(SystemUIGoogleModule_ProvideSensorPrivacyControllerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideSensorPrivacyManagerProvider));
            this.dateFormatUtilProvider = DateFormatUtil_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider);
            Provider<LogBuffer> provider = DoubleCheck.provider(LogModule_ProvidePrivacyLogBufferFactory.create(this.logBufferFactoryProvider));
            this.providePrivacyLogBufferProvider = provider;
            PrivacyLogger_Factory create = PrivacyLogger_Factory.create(provider);
            this.privacyLoggerProvider = create;
            this.privacyItemControllerProvider = DoubleCheck.provider(PrivacyItemController_Factory.create(this.appOpsControllerImplProvider, this.provideMainDelayableExecutorProvider, this.provideBackgroundDelayableExecutorProvider, this.deviceConfigProxyProvider, this.provideUserTrackerProvider, create, this.bindSystemClockProvider, this.dumpManagerProvider));
            this.phoneStatusBarPolicyProvider = PhoneStatusBarPolicy_Factory.create(this.statusBarIconControllerImplProvider, this.provideCommandQueueProvider, this.providesBroadcastDispatcherProvider, this.provideUiBackgroundExecutorProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideResourcesProvider, this.castControllerImplProvider, this.hotspotControllerImplProvider, this.bluetoothControllerImplProvider, this.nextAlarmControllerImplProvider, this.userInfoControllerImplProvider, this.rotationLockControllerImplProvider, this.provideDataSaverControllerProvider, this.zenModeControllerImplProvider, this.deviceProvisionedControllerImplProvider, this.keyguardStateControllerImplProvider, this.locationControllerImplProvider, this.provideSensorPrivacyControllerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIActivityManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideAlarmManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUserManagerProvider, this.recordingControllerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideTelecomManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideDisplayIdProvider, this.provideSharePreferencesProvider, this.dateFormatUtilProvider, this.ringerModeTrackerImplProvider, this.privacyItemControllerProvider, this.privacyLoggerProvider);
            this.statusBarTouchableRegionManagerProvider = DoubleCheck.provider(StatusBarTouchableRegionManager_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.notificationShadeWindowControllerImplProvider, this.provideConfigurationControllerProvider, this.provideHeadsUpManagerPhoneProvider));
            this.factoryProvider3 = BrightnessSlider_Factory_Factory.create(this.falsingManagerProxyProvider);
            this.wiredChargingRippleControllerProvider = DoubleCheck.provider(WiredChargingRippleController_Factory.create(this.commandRegistryProvider, this.provideBatteryControllerProvider, this.provideConfigurationControllerProvider, this.featureFlagsProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideWindowManagerProvider, this.bindSystemClockProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider));
            this.ongoingCallLoggerProvider = DoubleCheck.provider(OngoingCallLogger_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider));
            this.provideOngoingCallControllerProvider = DoubleCheck.provider(StatusBarDependenciesModule_ProvideOngoingCallControllerFactory.create(this.provideCommonNotifCollectionProvider, this.featureFlagsProvider, this.bindSystemClockProvider, this.activityStarterDelegateProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIActivityManagerProvider, this.ongoingCallLoggerProvider));
            this.systemEventCoordinatorProvider = DoubleCheck.provider(SystemEventCoordinator_Factory.create(this.bindSystemClockProvider, this.provideBatteryControllerProvider, this.privacyItemControllerProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.statusBarLocationPublisherProvider = DoubleCheck.provider(StatusBarLocationPublisher_Factory.create());
            SystemEventChipAnimationController_Factory create2 = SystemEventChipAnimationController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.superStatusBarViewFactoryProvider, this.statusBarWindowControllerProvider, this.statusBarLocationPublisherProvider);
            this.systemEventChipAnimationControllerProvider = create2;
            this.systemStatusAnimationSchedulerProvider = DoubleCheck.provider(SystemStatusAnimationScheduler_Factory.create(this.systemEventCoordinatorProvider, create2, this.statusBarWindowControllerProvider, this.dumpManagerProvider, this.bindSystemClockProvider, this.provideMainDelayableExecutorProvider));
            Provider<LogBuffer> provider2 = DoubleCheck.provider(SystemUIGoogleModule_ProvideNotifVoiceReplyLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNotifVoiceReplyLogBufferProvider = provider2;
            this.notificationVoiceReplyLoggerProvider = DoubleCheck.provider(NotificationVoiceReplyLogger_Factory.create(provider2, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider));
            Provider<NotificationVoiceReplyController> provider3 = DoubleCheck.provider(NotificationVoiceReplyController_Factory.create(this.provideNotificationEntryManagerProvider, this.notificationLockscreenUserManagerGoogleProvider, this.provideNotificationRemoteInputManagerProvider, SystemUIGoogleModule_ProvideVoiceReplyCtaLayoutFactory.create(), SystemUIGoogleModule_ProvideVoiceReplyCtaContainerIdFactory.create(), SystemUIGoogleModule_ProvideVoiceReplyCtaTextIdFactory.create(), SystemUIGoogleModule_ProvideVoiceReplyCtaIconIdFactory.create(), this.lockscreenShadeTransitionControllerProvider, this.notificationShadeWindowControllerImplProvider, this.statusBarKeyguardViewManagerProvider, this.provideStatusBarProvider, this.statusBarStateControllerImplProvider, this.provideHeadsUpManagerPhoneProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.notificationVoiceReplyLoggerProvider));
            this.notificationVoiceReplyControllerProvider = provider3;
            Provider<DebugNotificationVoiceReplyClient> provider4 = DoubleCheck.provider(DebugNotificationVoiceReplyClient_Factory.create(this.providesBroadcastDispatcherProvider, this.notificationLockscreenUserManagerGoogleProvider, provider3));
            this.debugNotificationVoiceReplyClientProvider = provider4;
            this.provideNotificationVoiceReplyClientProvider = DoubleCheck.provider(VoiceReplyModule_ProvideNotificationVoiceReplyClientFactory.create(provider4));
            DelegateFactory.setDelegate(this.provideStatusBarProvider, DoubleCheck.provider(StatusBarGoogleModule_ProvideStatusBarFactory.create(this.smartSpaceControllerProvider, this.wallpaperNotifierProvider, this.provideReverseChargingViewControllerOptionalProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideNotificationsControllerProvider, this.lightBarControllerProvider, this.provideAutoHideControllerProvider, this.keyguardUpdateMonitorProvider, this.statusBarSignalPolicyProvider, this.pulseExpansionHandlerProvider, this.notificationWakeUpCoordinatorProvider, this.keyguardBypassControllerProvider, this.keyguardStateControllerImplProvider, this.provideHeadsUpManagerPhoneProvider, this.dynamicPrivacyControllerProvider, this.bypassHeadsUpNotifierProvider, this.falsingManagerProxyProvider, this.falsingCollectorImplProvider, this.providesBroadcastDispatcherProvider, this.remoteInputQuickSettingsDisablerProvider, this.provideNotificationGutsManagerProvider, this.provideNotificationLoggerProvider, this.notificationInterruptStateProviderImplProvider, this.provideNotificationViewHierarchyManagerProvider, this.newKeyguardViewMediatorProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideDisplayMetricsProvider, this.provideMetricsLoggerProvider, this.provideUiBackgroundExecutorProvider, this.provideNotificationMediaManagerProvider, this.notificationLockscreenUserManagerGoogleProvider, this.provideNotificationRemoteInputManagerProvider, this.userSwitcherControllerProvider, this.networkControllerImplProvider, this.provideBatteryControllerProvider, this.sysuiColorExtractorProvider, this.screenLifecycleProvider, this.wakefulnessLifecycleProvider, this.statusBarStateControllerImplProvider, this.vibratorHelperProvider, this.provideBubblesManagerProvider, this.setBubblesProvider, this.provideVisualStabilityManagerProvider, this.deviceProvisionedControllerImplProvider, this.provideNavigationBarControllerProvider, this.provideAccessibilityFloatingMenuControllerProvider, this.assistManagerGoogleProvider, this.provideConfigurationControllerProvider, this.notificationShadeWindowControllerImplProvider, this.dozeParametersProvider, this.liveWallpaperScrimControllerProvider, this.provideKeyguardLiftControllerProvider, this.lockscreenWallpaperProvider, this.biometricUnlockControllerProvider, this.notificationShadeDepthControllerProvider, this.dozeServiceHostProvider, DaggerSysUIGoogleGlobalRootComponent.this.providePowerManagerProvider, this.screenPinningRequestProvider, this.dozeScrimControllerProvider, this.volumeDialogComponentProvider, this.provideCommandQueueProvider, this.statusBarComponentBuilderProvider, this.providePluginManagerProvider, this.setLegacySplitScreenProvider, this.lightsOutNotifControllerProvider, this.builderProvider5, this.shadeControllerImplProvider, this.superStatusBarViewFactoryProvider, this.statusBarKeyguardViewManagerProvider, this.providesViewMediatorCallbackProvider, this.initControllerProvider, this.provideTimeTickHandlerProvider, this.pluginDependencyProvider, this.keyguardDismissUtilProvider, this.extensionControllerImplProvider, this.userInfoControllerImplProvider, this.phoneStatusBarPolicyProvider, this.keyguardIndicationControllerGoogleProvider, this.dismissCallbackRegistryProvider, this.provideDemoModeControllerProvider, this.statusBarTouchableRegionManagerProvider, this.notificationIconAreaControllerProvider, this.factoryProvider3, this.wiredChargingRippleControllerProvider, this.provideOngoingCallControllerProvider, this.systemStatusAnimationSchedulerProvider, this.statusBarLocationPublisherProvider, this.statusBarIconControllerImplProvider, this.lockscreenShadeTransitionControllerProvider, this.featureFlagsProvider, this.provideNotificationVoiceReplyClientProvider, this.keyguardUnlockAnimationControllerProvider, this.unlockedScreenOffAnimationControllerProvider, this.setStartingSurfaceProvider)));
            DelegateFactory.setDelegate(this.optionalOfLazyOfStatusBarProvider, PresentJdkOptionalLazyProvider.of(this.provideStatusBarProvider));
            DelegateFactory.setDelegate(this.activityStarterDelegateProvider, DoubleCheck.provider(ActivityStarterDelegate_Factory.create(this.optionalOfLazyOfStatusBarProvider)));
            this.globalActionsComponentProvider = new DelegateFactory();
            this.provideQuickAccessWalletClientProvider = DoubleCheck.provider(WalletModule_ProvideQuickAccessWalletClientFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.globalActionsInfoProvider = GlobalActionsInfoProvider_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideQuickAccessWalletClientProvider, this.controlsControllerImplProvider, this.activityStarterDelegateProvider);
            this.globalActionsDialogLiteProvider = GlobalActionsDialogLite_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.globalActionsComponentProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideAudioManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIDreamManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideDevicePolicyManagerProvider, this.provideLockPatternUtilsProvider, this.providesBroadcastDispatcherProvider, this.telephonyListenerManagerProvider, this.globalSettingsImplProvider, this.secureSettingsImplProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideVibratorProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideResourcesProvider, this.provideConfigurationControllerProvider, this.keyguardStateControllerImplProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUserManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideTrustManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIActivityManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideTelecomManagerProvider, this.provideMetricsLoggerProvider, this.sysuiColorExtractorProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIStatusBarServiceProvider, this.notificationShadeWindowControllerImplProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIWindowManagerProvider, this.provideBackgroundExecutorProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider, this.globalActionsInfoProvider, this.ringerModeTrackerImplProvider, this.provideSysUiStateProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, DaggerSysUIGoogleGlobalRootComponent.this.providePackageManagerProvider, this.provideStatusBarProvider);
            this.globalActionsImplProvider = GlobalActionsImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider, this.globalActionsDialogLiteProvider, this.blurUtilsProvider, this.keyguardStateControllerImplProvider, this.deviceProvisionedControllerImplProvider);
            DelegateFactory.setDelegate(this.globalActionsComponentProvider, DoubleCheck.provider(GlobalActionsComponent_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider, this.extensionControllerImplProvider, this.globalActionsImplProvider, this.statusBarKeyguardViewManagerProvider)));
            this.setTaskViewFactoryProvider = InstanceFactory.create(optional7);
            this.controlsUiControllerImplProvider = new DelegateFactory();
            this.controlsMetricsLoggerImplProvider = DoubleCheck.provider(ControlsMetricsLoggerImpl_Factory.create());
            this.controlActionCoordinatorImplProvider = DoubleCheck.provider(ControlActionCoordinatorImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideDelayableExecutorProvider, this.provideMainDelayableExecutorProvider, this.activityStarterDelegateProvider, this.keyguardStateControllerImplProvider, this.globalActionsComponentProvider, this.setTaskViewFactoryProvider, this.providesBroadcastDispatcherProvider, this.controlsUiControllerImplProvider, this.controlsMetricsLoggerImplProvider));
            this.customIconCacheProvider = DoubleCheck.provider(CustomIconCache_Factory.create());
            DelegateFactory.setDelegate(this.controlsUiControllerImplProvider, DoubleCheck.provider(ControlsUiControllerImpl_Factory.create(this.controlsControllerImplProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideMainDelayableExecutorProvider, this.provideBackgroundDelayableExecutorProvider, this.controlsListingControllerImplProvider, this.provideSharePreferencesProvider, this.controlActionCoordinatorImplProvider, this.activityStarterDelegateProvider, this.shadeControllerImplProvider, this.customIconCacheProvider, this.controlsMetricsLoggerImplProvider, this.keyguardStateControllerImplProvider)));
            this.controlsBindingControllerImplProvider = DoubleCheck.provider(ControlsBindingControllerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideBackgroundDelayableExecutorProvider, this.controlsControllerImplProvider, this.provideUserTrackerProvider));
            this.optionalOfControlsFavoritePersistenceWrapperProvider = DaggerSysUIGoogleGlobalRootComponent.absentJdkOptionalProvider();
            DelegateFactory.setDelegate(this.controlsControllerImplProvider, DoubleCheck.provider(ControlsControllerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideBackgroundDelayableExecutorProvider, this.controlsUiControllerImplProvider, this.controlsBindingControllerImplProvider, this.controlsListingControllerImplProvider, this.providesBroadcastDispatcherProvider, this.optionalOfControlsFavoritePersistenceWrapperProvider, this.dumpManagerProvider, this.provideUserTrackerProvider)));
            this.controlsProviderSelectorActivityProvider = ControlsProviderSelectorActivity_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.controlsListingControllerImplProvider, this.controlsControllerImplProvider, this.providesBroadcastDispatcherProvider, this.controlsUiControllerImplProvider);
            this.controlsFavoritingActivityProvider = ControlsFavoritingActivity_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, this.controlsControllerImplProvider, this.controlsListingControllerImplProvider, this.providesBroadcastDispatcherProvider, this.controlsUiControllerImplProvider);
            this.controlsEditingActivityProvider = ControlsEditingActivity_Factory.create(this.controlsControllerImplProvider, this.providesBroadcastDispatcherProvider, this.customIconCacheProvider, this.controlsUiControllerImplProvider);
            this.controlsRequestDialogProvider = ControlsRequestDialog_Factory.create(this.controlsControllerImplProvider, this.providesBroadcastDispatcherProvider, this.controlsListingControllerImplProvider);
            this.controlsActivityProvider = ControlsActivity_Factory.create(this.controlsUiControllerImplProvider);
            this.walletActivityProvider = WalletActivity_Factory.create(this.keyguardStateControllerImplProvider, this.keyguardDismissUtilProvider, this.activityStarterDelegateProvider, this.provideBackgroundExecutorProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.falsingCollectorImplProvider, this.provideUserTrackerProvider, this.keyguardUpdateMonitorProvider, this.statusBarKeyguardViewManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider);
            this.tunerActivityProvider = TunerActivity_Factory.create(this.provideDemoModeControllerProvider, this.tunerServiceImplProvider);
            this.workLockActivityProvider = WorkLockActivity_Factory.create(this.providesBroadcastDispatcherProvider);
            this.brightnessDialogProvider = BrightnessDialog_Factory.create(this.providesBroadcastDispatcherProvider, this.factoryProvider3);
            this.screenRecordDialogProvider = ScreenRecordDialog_Factory.create(this.recordingControllerProvider, this.provideUserTrackerProvider);
            this.usbDebuggingActivityProvider = UsbDebuggingActivity_Factory.create(this.providesBroadcastDispatcherProvider);
            this.usbDebuggingSecondaryUserActivityProvider = UsbDebuggingSecondaryUserActivity_Factory.create(this.providesBroadcastDispatcherProvider);
            this.userCreatorProvider = UserCreator_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUserManagerProvider);
            UserModule_ProvideEditUserInfoControllerFactory create3 = UserModule_ProvideEditUserInfoControllerFactory.create(userModule);
            this.provideEditUserInfoControllerProvider = create3;
            this.createUserActivityProvider = CreateUserActivity_Factory.create(this.userCreatorProvider, create3, DaggerSysUIGoogleGlobalRootComponent.this.provideIActivityManagerProvider);
            TvNotificationHandler_Factory create4 = TvNotificationHandler_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideNotificationListenerProvider);
            this.tvNotificationHandlerProvider = create4;
            this.tvNotificationPanelActivityProvider = TvNotificationPanelActivity_Factory.create(create4);
            this.peopleSpaceActivityProvider = PeopleSpaceActivity_Factory.create(this.peopleSpaceWidgetManagerProvider);
            this.imageExporterProvider = ImageExporter_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideContentResolverProvider);
            this.longScreenshotDataProvider = DoubleCheck.provider(LongScreenshotData_Factory.create());
            this.longScreenshotActivityProvider = LongScreenshotActivity_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider, this.imageExporterProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.longScreenshotDataProvider);
            this.launchConversationActivityProvider = LaunchConversationActivity_Factory.create(this.provideNotificationEntryManagerProvider, this.provideBubblesManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUserManagerProvider, this.provideCommandQueueProvider);
            this.sensorUseStartedActivityProvider = SensorUseStartedActivity_Factory.create(this.provideIndividualSensorPrivacyControllerProvider, this.keyguardStateControllerImplProvider, this.keyguardDismissUtilProvider, this.provideBgHandlerProvider);
            this.tvUnblockSensorActivityProvider = TvUnblockSensorActivity_Factory.create(this.provideIndividualSensorPrivacyControllerProvider);
            this.gameMenuActivityProvider = GameMenuActivity_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.entryPointControllerProvider, this.activityStarterDelegateProvider, this.shortcutBarControllerProvider, this.gameModeDndControllerProvider, this.providerLayoutInflaterProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.gameDashboardUiEventLoggerProvider);
            this.mapOfClassOfAndProviderOfActivityProvider = MapProviderFactory.builder(21).put((MapProviderFactory.Builder) ControlsProviderSelectorActivity.class, (Provider) this.controlsProviderSelectorActivityProvider).put((MapProviderFactory.Builder) ControlsFavoritingActivity.class, (Provider) this.controlsFavoritingActivityProvider).put((MapProviderFactory.Builder) ControlsEditingActivity.class, (Provider) this.controlsEditingActivityProvider).put((MapProviderFactory.Builder) ControlsRequestDialog.class, (Provider) this.controlsRequestDialogProvider).put((MapProviderFactory.Builder) ControlsActivity.class, (Provider) this.controlsActivityProvider).put((MapProviderFactory.Builder) WalletActivity.class, (Provider) this.walletActivityProvider).put((MapProviderFactory.Builder) TunerActivity.class, (Provider) this.tunerActivityProvider).put((MapProviderFactory.Builder) ForegroundServicesDialog.class, (Provider) ForegroundServicesDialog_Factory.create()).put((MapProviderFactory.Builder) WorkLockActivity.class, (Provider) this.workLockActivityProvider).put((MapProviderFactory.Builder) BrightnessDialog.class, (Provider) this.brightnessDialogProvider).put((MapProviderFactory.Builder) ScreenRecordDialog.class, (Provider) this.screenRecordDialogProvider).put((MapProviderFactory.Builder) UsbDebuggingActivity.class, (Provider) this.usbDebuggingActivityProvider).put((MapProviderFactory.Builder) UsbDebuggingSecondaryUserActivity.class, (Provider) this.usbDebuggingSecondaryUserActivityProvider).put((MapProviderFactory.Builder) CreateUserActivity.class, (Provider) this.createUserActivityProvider).put((MapProviderFactory.Builder) TvNotificationPanelActivity.class, (Provider) this.tvNotificationPanelActivityProvider).put((MapProviderFactory.Builder) PeopleSpaceActivity.class, (Provider) this.peopleSpaceActivityProvider).put((MapProviderFactory.Builder) LongScreenshotActivity.class, (Provider) this.longScreenshotActivityProvider).put((MapProviderFactory.Builder) LaunchConversationActivity.class, (Provider) this.launchConversationActivityProvider).put((MapProviderFactory.Builder) SensorUseStartedActivity.class, (Provider) this.sensorUseStartedActivityProvider).put((MapProviderFactory.Builder) TvUnblockSensorActivity.class, (Provider) this.tvUnblockSensorActivityProvider).put((MapProviderFactory.Builder) GameMenuActivity.class, (Provider) this.gameMenuActivityProvider).build();
            this.screenshotSmartActionsProvider = DoubleCheck.provider(ScreenshotSmartActions_Factory.create());
            this.screenshotNotificationsControllerProvider = ScreenshotNotificationsController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideWindowManagerProvider);
            this.scrollCaptureClientProvider = ScrollCaptureClient_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideIWindowManagerProvider, this.provideBackgroundExecutorProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider);
            this.imageTileSetProvider = ImageTileSet_Factory.create(this.provideHandlerProvider);
            this.scrollCaptureControllerProvider = ScrollCaptureController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideBackgroundExecutorProvider, this.scrollCaptureClientProvider, this.imageTileSetProvider);
            ScreenshotController_Factory create5 = ScreenshotController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.screenshotSmartActionsProvider, this.screenshotNotificationsControllerProvider, this.scrollCaptureClientProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider, this.imageExporterProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, this.scrollCaptureControllerProvider, this.longScreenshotDataProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideActivityManagerProvider);
            this.screenshotControllerProvider = create5;
            this.takeScreenshotServiceProvider = TakeScreenshotService_Factory.create(create5, DaggerSysUIGoogleGlobalRootComponent.this.provideUserManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider, this.screenshotNotificationsControllerProvider);
        }

        private void initialize6(DependencyProvider dependencyProvider, NightDisplayListenerModule nightDisplayListenerModule, UserModule userModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<TaskSurfaceHelper> optional11) {
            AnonymousClass8 r1 = new Provider<DozeComponent.Builder>() { // from class: com.google.android.systemui.dagger.DaggerSysUIGoogleGlobalRootComponent.SysUIGoogleSysUIComponentImpl.8
                @Override // javax.inject.Provider
                public DozeComponent.Builder get() {
                    return new DozeComponentFactory();
                }
            };
            this.dozeComponentBuilderProvider = r1;
            this.dozeServiceProvider = DozeService_Factory.create(r1, this.providePluginManagerProvider);
            Provider<KeyguardLifecyclesDispatcher> provider = DoubleCheck.provider(KeyguardLifecyclesDispatcher_Factory.create(this.screenLifecycleProvider, this.wakefulnessLifecycleProvider));
            this.keyguardLifecyclesDispatcherProvider = provider;
            this.keyguardServiceProvider = KeyguardService_Factory.create(this.newKeyguardViewMediatorProvider, provider);
            this.dumpHandlerProvider = DumpHandler_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.dumpManagerProvider, this.logBufferEulogizerProvider);
            this.logBufferFreezerProvider = LogBufferFreezer_Factory.create(this.dumpManagerProvider, this.provideMainDelayableExecutorProvider);
            this.batteryStateNotifierProvider = BatteryStateNotifier_Factory.create(this.provideBatteryControllerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideNotificationManagerProvider, this.provideDelayableExecutorProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider);
            this.systemUIServiceProvider = SystemUIService_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.dumpHandlerProvider, this.providesBroadcastDispatcherProvider, this.logBufferFreezerProvider, this.batteryStateNotifierProvider);
            this.systemUIAuxiliaryDumpServiceProvider = SystemUIAuxiliaryDumpService_Factory.create(this.dumpHandlerProvider);
            Provider<Looper> provider2 = DoubleCheck.provider(SysUIConcurrencyModule_ProvideLongRunningLooperFactory.create());
            this.provideLongRunningLooperProvider = provider2;
            Provider<Executor> provider3 = DoubleCheck.provider(SysUIConcurrencyModule_ProvideLongRunningExecutorFactory.create(provider2));
            this.provideLongRunningExecutorProvider = provider3;
            this.recordingServiceProvider = RecordingService_Factory.create(this.recordingControllerProvider, provider3, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideNotificationManagerProvider, this.provideUserTrackerProvider, this.keyguardDismissUtilProvider);
            this.notificationVoiceReplyManagerServiceProvider = NotificationVoiceReplyManagerService_Factory.create(this.notificationVoiceReplyControllerProvider, this.notificationVoiceReplyLoggerProvider);
            this.mapOfClassOfAndProviderOfServiceProvider = MapProviderFactory.builder(8).put((MapProviderFactory.Builder) TakeScreenshotService.class, (Provider) this.takeScreenshotServiceProvider).put((MapProviderFactory.Builder) DozeService.class, (Provider) this.dozeServiceProvider).put((MapProviderFactory.Builder) ImageWallpaper.class, (Provider) ImageWallpaper_Factory.create()).put((MapProviderFactory.Builder) KeyguardService.class, (Provider) this.keyguardServiceProvider).put((MapProviderFactory.Builder) SystemUIService.class, (Provider) this.systemUIServiceProvider).put((MapProviderFactory.Builder) SystemUIAuxiliaryDumpService.class, (Provider) this.systemUIAuxiliaryDumpServiceProvider).put((MapProviderFactory.Builder) RecordingService.class, (Provider) this.recordingServiceProvider).put((MapProviderFactory.Builder) NotificationVoiceReplyManagerService.class, (Provider) this.notificationVoiceReplyManagerServiceProvider).build();
            this.provideLeakReportEmailProvider = DoubleCheck.provider(SystemUIGoogleModule_ProvideLeakReportEmailFactory.create());
            this.leakReporterProvider = DoubleCheck.provider(LeakReporter_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideLeakDetectorProvider, this.provideLeakReportEmailProvider));
            this.garbageMonitorProvider = DoubleCheck.provider(GarbageMonitor_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideBgLooperProvider, this.provideLeakDetectorProvider, this.leakReporterProvider));
            this.serviceProvider = DoubleCheck.provider(GarbageMonitor_Service_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.garbageMonitorProvider));
            this.opaHomeButtonProvider = OpaHomeButton_Factory.create(this.newKeyguardViewMediatorProvider, this.provideStatusBarProvider, this.navigationModeControllerProvider);
            OpaLockscreen_Factory create = OpaLockscreen_Factory.create(this.provideStatusBarProvider, this.keyguardStateControllerImplProvider);
            this.opaLockscreenProvider = create;
            this.assistInvocationEffectProvider = AssistInvocationEffect_Factory.create(this.assistManagerGoogleProvider, this.opaHomeButtonProvider, create);
            this.builderProvider6 = LaunchOpa_Builder_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideStatusBarProvider);
            this.builderProvider7 = SettingsAction_Builder_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideStatusBarProvider);
            this.builderProvider8 = CameraAction_Builder_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideStatusBarProvider);
            this.builderProvider9 = SetupWizardAction_Builder_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideStatusBarProvider);
            this.squishyNavigationButtonsProvider = SquishyNavigationButtons_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.newKeyguardViewMediatorProvider, this.provideStatusBarProvider, this.navigationModeControllerProvider);
            this.optionalOfHeadsUpManagerProvider = PresentJdkOptionalInstanceProvider.of(this.provideHeadsUpManagerPhoneProvider);
            this.unpinNotificationsProvider = UnpinNotifications_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.optionalOfHeadsUpManagerProvider);
            this.silenceCallProvider = SilenceCall_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.telephonyListenerManagerProvider);
            this.telephonyActivityProvider = TelephonyActivity_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.telephonyListenerManagerProvider);
            this.serviceConfigurationGoogleProvider = ServiceConfigurationGoogle_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.assistInvocationEffectProvider, this.builderProvider6, this.builderProvider7, this.builderProvider8, this.builderProvider9, this.squishyNavigationButtonsProvider, this.unpinNotificationsProvider, this.silenceCallProvider, this.telephonyActivityProvider);
            Provider<ContentResolverWrapper> provider4 = DoubleCheck.provider(ContentResolverWrapper_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.contentResolverWrapperProvider = provider4;
            this.factoryProvider4 = DoubleCheck.provider(ColumbusContentObserver_Factory_Factory.create(provider4, DaggerSysUIGoogleGlobalRootComponent.this.provideIActivityManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider));
            this.columbusSettingsProvider = DoubleCheck.provider(ColumbusSettings_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.factoryProvider4));
            this.silenceAlertsDisabledProvider = DoubleCheck.provider(SilenceAlertsDisabled_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.columbusSettingsProvider));
            this.dismissTimerProvider = DoubleCheck.provider(DismissTimer_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.silenceAlertsDisabledProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIActivityManagerProvider));
            this.snoozeAlarmProvider = DoubleCheck.provider(SnoozeAlarm_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.silenceAlertsDisabledProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIActivityManagerProvider));
            this.silenceCallProvider2 = DoubleCheck.provider(com.google.android.systemui.columbus.actions.SilenceCall_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.silenceAlertsDisabledProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideTelecomManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideTelephonyManagerProvider, this.telephonyListenerManagerProvider));
            Provider<com.google.android.systemui.columbus.actions.SettingsAction> provider5 = DoubleCheck.provider(SettingsAction_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideStatusBarProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider));
            this.settingsActionProvider = provider5;
            this.provideFullscreenActionsProvider = DoubleCheck.provider(ColumbusModule_ProvideFullscreenActionsFactory.create(this.dismissTimerProvider, this.snoozeAlarmProvider, this.silenceCallProvider2, provider5));
            this.unpinNotificationsProvider2 = DoubleCheck.provider(com.google.android.systemui.columbus.actions.UnpinNotifications_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.silenceAlertsDisabledProvider, this.optionalOfHeadsUpManagerProvider));
            this.assistInvocationEffectProvider2 = DoubleCheck.provider(com.google.android.systemui.columbus.feedback.AssistInvocationEffect_Factory.create(this.assistManagerGoogleProvider));
            this.namedSetOfFeedbackEffectProvider = SetFactory.builder(1, 0).addProvider(this.assistInvocationEffectProvider2).build();
            this.launchOpaProvider = DoubleCheck.provider(LaunchOpa_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideStatusBarProvider, this.namedSetOfFeedbackEffectProvider, this.assistManagerGoogleProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideKeyguardManagerProvider, this.tunerServiceImplProvider, this.factoryProvider4, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider));
            this.manageMediaProvider = DoubleCheck.provider(ManageMedia_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideAudioManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider));
            this.takeScreenshotProvider = DoubleCheck.provider(TakeScreenshot_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider));
            this.launchOverviewProvider = DoubleCheck.provider(LaunchOverview_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideRecentsProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider));
            this.openNotificationShadeProvider = DoubleCheck.provider(OpenNotificationShade_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.notificationShadeWindowControllerImplProvider, this.provideStatusBarProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider));
            this.keyguardVisibilityProvider = DoubleCheck.provider(KeyguardVisibility_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.keyguardStateControllerImplProvider));
            Provider<LaunchApp> provider6 = DoubleCheck.provider(LaunchApp_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideLauncherAppsProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIActivityManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUserManagerProvider, this.columbusSettingsProvider, this.keyguardVisibilityProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.provideBgHandlerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider));
            this.launchAppProvider = provider6;
            this.provideUserSelectedActionsProvider = DoubleCheck.provider(ColumbusModule_ProvideUserSelectedActionsFactory.create(this.launchOpaProvider, this.manageMediaProvider, this.takeScreenshotProvider, this.launchOverviewProvider, this.openNotificationShadeProvider, provider6));
            this.powerManagerWrapperProvider = DoubleCheck.provider(PowerManagerWrapper_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            Provider<UserSelectedAction> provider7 = DoubleCheck.provider(UserSelectedAction_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.columbusSettingsProvider, this.provideUserSelectedActionsProvider, this.takeScreenshotProvider, this.keyguardStateControllerImplProvider, this.powerManagerWrapperProvider, this.wakefulnessLifecycleProvider));
            this.userSelectedActionProvider = provider7;
            this.provideColumbusActionsProvider = DoubleCheck.provider(ColumbusModule_ProvideColumbusActionsFactory.create(this.provideFullscreenActionsProvider, this.unpinNotificationsProvider2, provider7));
            this.hapticClickProvider = DoubleCheck.provider(HapticClick_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideVibratorProvider));
            Provider<UserActivity> provider8 = DoubleCheck.provider(UserActivity_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.providePowerManagerProvider));
            this.userActivityProvider = provider8;
            this.provideColumbusEffectsProvider = ColumbusModule_ProvideColumbusEffectsFactory.create(this.hapticClickProvider, provider8);
            this.namedSetOfFeedbackEffectProvider2 = SetFactory.builder(0, 1).addCollectionProvider(this.provideColumbusEffectsProvider).build();
            this.flagEnabledProvider = DoubleCheck.provider(FlagEnabled_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.columbusSettingsProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider));
            this.chargingStateProvider = DoubleCheck.provider(ChargingState_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, ColumbusModule_ProvideTransientGateDurationFactory.create()));
            this.usbStateProvider = DoubleCheck.provider(UsbState_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, ColumbusModule_ProvideTransientGateDurationFactory.create()));
            this.proximityProvider = DoubleCheck.provider(Proximity_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.proximitySensorProvider));
            this.keyguardProximityProvider = DoubleCheck.provider(KeyguardProximity_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.keyguardVisibilityProvider, this.proximityProvider));
            this.namedSetOfActionProvider = SetFactory.builder(1, 0).addProvider(this.settingsActionProvider).build();
            this.setupWizardProvider = DoubleCheck.provider(SetupWizard_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.namedSetOfActionProvider, this.deviceProvisionedControllerImplProvider));
            this.namedSetOfIntegerProvider = SetFactory.builder(0, 1).addCollectionProvider(ColumbusModule_ProvideBlockingSystemKeysFactory.create()).build();
            this.systemKeyPressProvider = DoubleCheck.provider(SystemKeyPress_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.provideCommandQueueProvider, ColumbusModule_ProvideTransientGateDurationFactory.create(), this.namedSetOfIntegerProvider));
            this.telephonyActivityProvider2 = DoubleCheck.provider(com.google.android.systemui.columbus.gates.TelephonyActivity_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideTelephonyManagerProvider, this.telephonyListenerManagerProvider));
            this.vrModeProvider = DoubleCheck.provider(VrMode_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.powerStateProvider = DoubleCheck.provider(PowerState_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.wakefulnessLifecycleProvider));
            this.cameraVisibilityProvider = DoubleCheck.provider(CameraVisibility_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideFullscreenActionsProvider, this.keyguardVisibilityProvider, this.powerStateProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIActivityManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider));
            this.powerSaveStateProvider = DoubleCheck.provider(PowerSaveState_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            Provider<ScreenTouch> provider9 = DoubleCheck.provider(ScreenTouch_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.powerStateProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider));
            this.screenTouchProvider = provider9;
            this.provideColumbusGatesProvider = ColumbusModule_ProvideColumbusGatesFactory.create(this.flagEnabledProvider, this.chargingStateProvider, this.usbStateProvider, this.keyguardProximityProvider, this.setupWizardProvider, this.systemKeyPressProvider, this.telephonyActivityProvider2, this.vrModeProvider, this.cameraVisibilityProvider, this.powerSaveStateProvider, this.powerStateProvider, provider9);
            this.namedSetOfGateProvider = SetFactory.builder(0, 1).addCollectionProvider(this.provideColumbusGatesProvider).build();
            this.sensorConfigurationProvider = DoubleCheck.provider(SensorConfiguration_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            Provider<LowSensitivitySettingAdjustment> provider10 = DoubleCheck.provider(LowSensitivitySettingAdjustment_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.columbusSettingsProvider, this.sensorConfigurationProvider));
            this.lowSensitivitySettingAdjustmentProvider = provider10;
            Provider<List<Adjustment>> provider11 = DoubleCheck.provider(ColumbusModule_ProvideGestureAdjustmentsFactory.create(provider10));
            this.provideGestureAdjustmentsProvider = provider11;
            this.gestureConfigurationProvider = DoubleCheck.provider(GestureConfiguration_Factory.create(provider11, this.sensorConfigurationProvider));
            this.cHREGestureSensorProvider = DoubleCheck.provider(CHREGestureSensor_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider, this.gestureConfigurationProvider, this.statusBarStateControllerImplProvider, this.wakefulnessLifecycleProvider, this.provideBgHandlerProvider));
            this.gestureSensorImplProvider = DoubleCheck.provider(GestureSensorImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider));
            Provider<GestureSensor> provider12 = DoubleCheck.provider(ColumbusModule_ProvideGestureSensorFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.columbusSettingsProvider, this.cHREGestureSensorProvider, this.gestureSensorImplProvider));
            this.provideGestureSensorProvider = provider12;
            Provider<GestureController> provider13 = DoubleCheck.provider(GestureController_Factory.create(provider12, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider));
            this.gestureControllerProvider = provider13;
            Provider<ColumbusService> provider14 = DoubleCheck.provider(ColumbusService_Factory.create(this.provideColumbusActionsProvider, this.namedSetOfFeedbackEffectProvider2, this.namedSetOfGateProvider, provider13, this.powerManagerWrapperProvider));
            this.columbusServiceProvider = provider14;
            this.columbusServiceWrapperProvider = DoubleCheck.provider(ColumbusServiceWrapper_Factory.create(this.columbusSettingsProvider, provider14, this.settingsActionProvider));
            this.dataLoggerProvider = DataLogger_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideStatsManagerProvider);
            this.autorotateDataServiceProvider = AutorotateDataService_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.providesSensorManagerProvider, this.dataLoggerProvider, this.providesBroadcastDispatcherProvider, this.deviceConfigProxyProvider, this.provideMainDelayableExecutorProvider);
            this.googleServicesProvider = DoubleCheck.provider(GoogleServices_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.serviceConfigurationGoogleProvider, this.provideStatusBarProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider, this.columbusServiceWrapperProvider, this.featureFlagsProvider, this.keyguardIndicationControllerGoogleProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideAlarmManagerProvider, this.autorotateDataServiceProvider));
            this.instantAppNotifierProvider = DoubleCheck.provider(InstantAppNotifier_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider, this.provideUiBackgroundExecutorProvider, this.setLegacySplitScreenProvider));
            this.latencyTesterProvider = DoubleCheck.provider(LatencyTester_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.biometricUnlockControllerProvider, DaggerSysUIGoogleGlobalRootComponent.this.providePowerManagerProvider, this.providesBroadcastDispatcherProvider));
            this.powerUIProvider = DoubleCheck.provider(PowerUI_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.providesBroadcastDispatcherProvider, this.provideCommandQueueProvider, this.provideStatusBarProvider));
            this.privacyDotViewControllerProvider = DoubleCheck.provider(PrivacyDotViewController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, this.statusBarStateControllerImplProvider, this.provideConfigurationControllerProvider, this.statusBarContentInsetsProvider, this.systemStatusAnimationSchedulerProvider));
            this.screenDecorationsProvider = DoubleCheck.provider(ScreenDecorations_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, this.secureSettingsImplProvider, this.providesBroadcastDispatcherProvider, this.tunerServiceImplProvider, this.provideUserTrackerProvider, this.privacyDotViewControllerProvider, ThreadFactoryImpl_Factory.create()));
            this.shortcutKeyDispatcherProvider = DoubleCheck.provider(ShortcutKeyDispatcher_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.setLegacySplitScreenProvider));
            this.sliceBroadcastRelayHandlerProvider = DoubleCheck.provider(SliceBroadcastRelayHandler_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.providesBroadcastDispatcherProvider));
            this.provideThemeOverlayManagerProvider = DoubleCheck.provider(DependencyProvider_ProvideThemeOverlayManagerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideBackgroundExecutorProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideOverlayManagerProvider, this.dumpManagerProvider));
            this.themeOverlayControllerGoogleProvider = DoubleCheck.provider(ThemeOverlayControllerGoogle_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.providesBroadcastDispatcherProvider, this.provideBgHandlerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.provideThemeOverlayManagerProvider, this.secureSettingsImplProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideWallpaperManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUserManagerProvider, this.dumpManagerProvider, this.deviceProvisionedControllerImplProvider, this.provideUserTrackerProvider, this.featureFlagsProvider, this.wakefulnessLifecycleProvider));
            this.toastFactoryProvider = DoubleCheck.provider(ToastFactory_Factory.create(this.providerLayoutInflaterProvider, this.providePluginManagerProvider, this.dumpManagerProvider));
            Provider<LogBuffer> provider15 = DoubleCheck.provider(LogModule_ProvideToastLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideToastLogBufferProvider = provider15;
            this.toastLoggerProvider = ToastLogger_Factory.create(provider15);
        }

        private void initialize7(DependencyProvider dependencyProvider, NightDisplayListenerModule nightDisplayListenerModule, UserModule userModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<TaskSurfaceHelper> optional11) {
            this.toastUIProvider = DoubleCheck.provider(ToastUI_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider, this.toastFactoryProvider, this.toastLoggerProvider));
            this.tvStatusBarProvider = DoubleCheck.provider(TvStatusBar_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider, this.assistManagerGoogleProvider));
            this.volumeUIProvider = DoubleCheck.provider(VolumeUI_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.volumeDialogComponentProvider));
            this.providesModeSwitchesControllerProvider = DoubleCheck.provider(DependencyProvider_ProvidesModeSwitchesControllerFactory.create(dependencyProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.windowMagnificationProvider = DoubleCheck.provider(WindowMagnification_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.provideCommandQueueProvider, this.providesModeSwitchesControllerProvider, this.provideSysUiStateProvider, this.overviewProxyServiceProvider));
            this.setHideDisplayCutoutProvider = InstanceFactory.create(optional8);
            this.setShellCommandHandlerProvider = InstanceFactory.create(optional9);
            this.wMShellProvider = DoubleCheck.provider(WMShell_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.setPipProvider, this.setLegacySplitScreenProvider, this.setOneHandedProvider, this.setHideDisplayCutoutProvider, this.setShellCommandHandlerProvider, this.provideCommandQueueProvider, this.provideConfigurationControllerProvider, this.keyguardUpdateMonitorProvider, this.navigationModeControllerProvider, this.screenLifecycleProvider, this.provideSysUiStateProvider, this.protoTracerProvider, this.wakefulnessLifecycleProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider));
            this.mapOfClassOfAndProviderOfSystemUIProvider = MapProviderFactory.builder(21).put((MapProviderFactory.Builder) AuthController.class, (Provider) this.authControllerProvider).put((MapProviderFactory.Builder) GarbageMonitor.Service.class, (Provider) this.serviceProvider).put((MapProviderFactory.Builder) GlobalActionsComponent.class, (Provider) this.globalActionsComponentProvider).put((MapProviderFactory.Builder) GoogleServices.class, (Provider) this.googleServicesProvider).put((MapProviderFactory.Builder) InstantAppNotifier.class, (Provider) this.instantAppNotifierProvider).put((MapProviderFactory.Builder) KeyguardViewMediator.class, (Provider) this.newKeyguardViewMediatorProvider).put((MapProviderFactory.Builder) LatencyTester.class, (Provider) this.latencyTesterProvider).put((MapProviderFactory.Builder) PowerUI.class, (Provider) this.powerUIProvider).put((MapProviderFactory.Builder) Recents.class, (Provider) this.provideRecentsProvider).put((MapProviderFactory.Builder) ScreenDecorations.class, (Provider) this.screenDecorationsProvider).put((MapProviderFactory.Builder) ShortcutKeyDispatcher.class, (Provider) this.shortcutKeyDispatcherProvider).put((MapProviderFactory.Builder) SliceBroadcastRelayHandler.class, (Provider) this.sliceBroadcastRelayHandlerProvider).put((MapProviderFactory.Builder) StatusBar.class, (Provider) this.provideStatusBarProvider).put((MapProviderFactory.Builder) SystemActions.class, (Provider) this.systemActionsProvider).put((MapProviderFactory.Builder) StatusBarGoogle.class, (Provider) this.provideStatusBarProvider).put((MapProviderFactory.Builder) ThemeOverlayController.class, (Provider) this.themeOverlayControllerGoogleProvider).put((MapProviderFactory.Builder) ToastUI.class, (Provider) this.toastUIProvider).put((MapProviderFactory.Builder) TvStatusBar.class, (Provider) this.tvStatusBarProvider).put((MapProviderFactory.Builder) VolumeUI.class, (Provider) this.volumeUIProvider).put((MapProviderFactory.Builder) WindowMagnification.class, (Provider) this.windowMagnificationProvider).put((MapProviderFactory.Builder) WMShell.class, (Provider) this.wMShellProvider).build();
            this.overviewProxyRecentsImplProvider = DoubleCheck.provider(OverviewProxyRecentsImpl_Factory.create(this.optionalOfLazyOfStatusBarProvider));
            this.mapOfClassOfAndProviderOfRecentsImplementationProvider = MapProviderFactory.builder(1).put((MapProviderFactory.Builder) OverviewProxyRecentsImpl.class, (Provider) this.overviewProxyRecentsImplProvider).build();
            Provider<Optional<StatusBar>> of = PresentJdkOptionalInstanceProvider.of(this.provideStatusBarProvider);
            this.optionalOfStatusBarProvider = of;
            this.actionProxyReceiverProvider = ActionProxyReceiver_Factory.create(of, this.provideActivityManagerWrapperProvider, this.screenshotSmartActionsProvider);
            this.deleteScreenshotReceiverProvider = DeleteScreenshotReceiver_Factory.create(this.screenshotSmartActionsProvider, this.provideBackgroundExecutorProvider);
            this.smartActionsReceiverProvider = SmartActionsReceiver_Factory.create(this.screenshotSmartActionsProvider);
            this.mediaOutputDialogReceiverProvider = MediaOutputDialogReceiver_Factory.create(this.mediaOutputDialogFactoryProvider);
            this.peopleSpaceWidgetPinnedReceiverProvider = PeopleSpaceWidgetPinnedReceiver_Factory.create(this.peopleSpaceWidgetManagerProvider);
            this.peopleSpaceWidgetProvider = PeopleSpaceWidgetProvider_Factory.create(this.peopleSpaceWidgetManagerProvider);
            MapProviderFactory build = MapProviderFactory.builder(6).put((MapProviderFactory.Builder) ActionProxyReceiver.class, (Provider) this.actionProxyReceiverProvider).put((MapProviderFactory.Builder) DeleteScreenshotReceiver.class, (Provider) this.deleteScreenshotReceiverProvider).put((MapProviderFactory.Builder) SmartActionsReceiver.class, (Provider) this.smartActionsReceiverProvider).put((MapProviderFactory.Builder) MediaOutputDialogReceiver.class, (Provider) this.mediaOutputDialogReceiverProvider).put((MapProviderFactory.Builder) PeopleSpaceWidgetPinnedReceiver.class, (Provider) this.peopleSpaceWidgetPinnedReceiverProvider).put((MapProviderFactory.Builder) PeopleSpaceWidgetProvider.class, (Provider) this.peopleSpaceWidgetProvider).build();
            this.mapOfClassOfAndProviderOfBroadcastReceiverProvider = build;
            DelegateFactory.setDelegate(this.contextComponentResolverProvider, DoubleCheck.provider(ContextComponentResolver_Factory.create(this.mapOfClassOfAndProviderOfActivityProvider, this.mapOfClassOfAndProviderOfServiceProvider, this.mapOfClassOfAndProviderOfSystemUIProvider, this.mapOfClassOfAndProviderOfRecentsImplementationProvider, build)));
            this.flashlightControllerImplProvider = DoubleCheck.provider(FlashlightControllerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.provideNightDisplayListenerProvider = NightDisplayListenerModule_ProvideNightDisplayListenerFactory.create(nightDisplayListenerModule, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideBgHandlerProvider);
            this.provideReduceBrightColorsListenerProvider = DoubleCheck.provider(DependencyProvider_ProvideReduceBrightColorsListenerFactory.create(dependencyProvider, this.provideBgHandlerProvider, this.provideUserTrackerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideColorDisplayManagerProvider, this.secureSettingsImplProvider));
            this.managedProfileControllerImplProvider = DoubleCheck.provider(ManagedProfileControllerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.providesBroadcastDispatcherProvider));
            this.accessibilityControllerProvider = DoubleCheck.provider(AccessibilityController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            AnonymousClass9 r1 = new Provider<FragmentService.FragmentCreator.Factory>() { // from class: com.google.android.systemui.dagger.DaggerSysUIGoogleGlobalRootComponent.SysUIGoogleSysUIComponentImpl.9
                @Override // javax.inject.Provider
                public FragmentService.FragmentCreator.Factory get() {
                    return new FragmentCreatorFactory();
                }
            };
            this.fragmentCreatorFactoryProvider = r1;
            this.fragmentServiceProvider = DoubleCheck.provider(FragmentService_Factory.create(r1, this.provideConfigurationControllerProvider));
            this.tunablePaddingServiceProvider = DoubleCheck.provider(TunablePadding_TunablePaddingService_Factory.create(this.tunerServiceImplProvider));
            this.uiOffloadThreadProvider = DoubleCheck.provider(UiOffloadThread_Factory.create());
            this.provideWarningsUiProvider = PowerModuleGoogle_ProvideWarningsUiFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.activityStarterDelegateProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider);
            this.foregroundServiceNotificationListenerProvider = DoubleCheck.provider(ForegroundServiceNotificationListener_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.foregroundServiceControllerProvider, this.provideNotificationEntryManagerProvider, this.notifPipelineProvider, this.bindSystemClockProvider));
            this.clockManagerProvider = DoubleCheck.provider(ClockManager_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.injectionInflationControllerProvider, this.providePluginManagerProvider, this.sysuiColorExtractorProvider, this.provideDockManagerProvider, this.providesBroadcastDispatcherProvider));
            this.factoryProvider5 = EdgeBackGestureHandler_Factory_Factory.create(this.overviewProxyServiceProvider, this.provideSysUiStateProvider, this.providePluginManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, this.providesBroadcastDispatcherProvider, this.protoTracerProvider, this.navigationModeControllerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideViewConfigurationProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideWindowManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIWindowManagerProvider, this.falsingManagerProxyProvider);
            this.dependencyProvider2 = DoubleCheck.provider(Dependency_Factory.create(this.dumpManagerProvider, this.activityStarterDelegateProvider, this.providesBroadcastDispatcherProvider, this.asyncSensorManagerProvider, this.bluetoothControllerImplProvider, this.locationControllerImplProvider, this.rotationLockControllerImplProvider, this.networkControllerImplProvider, this.zenModeControllerImplProvider, this.hotspotControllerImplProvider, this.castControllerImplProvider, this.flashlightControllerImplProvider, this.userSwitcherControllerProvider, this.userInfoControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardUpdateMonitorProvider, this.provideBatteryControllerProvider, this.provideNightDisplayListenerProvider, this.provideReduceBrightColorsListenerProvider, this.managedProfileControllerImplProvider, this.nextAlarmControllerImplProvider, this.provideDataSaverControllerProvider, this.accessibilityControllerProvider, this.deviceProvisionedControllerImplProvider, this.providePluginManagerProvider, this.assistManagerGoogleProvider, this.securityControllerImplProvider, this.provideLeakDetectorProvider, this.leakReporterProvider, this.garbageMonitorProvider, this.tunerServiceImplProvider, this.notificationShadeWindowControllerImplProvider, this.statusBarWindowControllerProvider, this.darkIconDispatcherImplProvider, this.provideConfigurationControllerProvider, this.statusBarIconControllerImplProvider, this.screenLifecycleProvider, this.wakefulnessLifecycleProvider, this.fragmentServiceProvider, this.extensionControllerImplProvider, this.pluginDependencyProvider, this.provideLocalBluetoothControllerProvider, this.volumeDialogControllerImplProvider, this.provideMetricsLoggerProvider, this.accessibilityManagerWrapperProvider, this.sysuiColorExtractorProvider, this.tunablePaddingServiceProvider, this.foregroundServiceControllerProvider, this.uiOffloadThreadProvider, this.provideWarningsUiProvider, this.lightBarControllerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIWindowManagerProvider, this.overviewProxyServiceProvider, this.navigationModeControllerProvider, this.accessibilityButtonModeObserverProvider, this.accessibilityButtonTargetsObserverProvider, this.enhancedEstimatesGoogleImplProvider, this.vibratorHelperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIStatusBarServiceProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideDisplayMetricsProvider, this.lockscreenGestureLoggerProvider, this.keyguardEnvironmentImplProvider, this.shadeControllerImplProvider, this.statusBarRemoteInputCallbackProvider, this.appOpsControllerImplProvider, this.provideNavigationBarControllerProvider, this.provideAccessibilityFloatingMenuControllerProvider, this.statusBarStateControllerImplProvider, this.notificationLockscreenUserManagerGoogleProvider, this.provideNotificationGroupAlertTransferHelperProvider, this.notificationGroupManagerLegacyProvider, this.provideVisualStabilityManagerProvider, this.provideNotificationGutsManagerProvider, this.provideNotificationMediaManagerProvider, this.provideNotificationRemoteInputManagerProvider, this.smartReplyConstantsProvider, this.provideNotificationListenerProvider, this.provideNotificationLoggerProvider, this.provideNotificationViewHierarchyManagerProvider, this.notificationFilterProvider, this.keyguardDismissUtilProvider, this.provideSmartReplyControllerProvider, this.remoteInputQuickSettingsDisablerProvider, this.provideNotificationEntryManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideSensorPrivacyManagerProvider, this.provideAutoHideControllerProvider, this.foregroundServiceNotificationListenerProvider, this.privacyItemControllerProvider, this.provideBgLooperProvider, this.provideBgHandlerProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.provideTimeTickHandlerProvider, this.provideLeakReportEmailProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.clockManagerProvider, this.provideActivityManagerWrapperProvider, this.provideDevicePolicyManagerWrapperProvider, DaggerSysUIGoogleGlobalRootComponent.this.providePackageManagerWrapperProvider, this.provideSensorPrivacyControllerProvider, this.provideDockManagerProvider, this.provideINotificationManagerProvider, this.provideSysUiStateProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideAlarmManagerProvider, this.keyguardSecurityModelProvider, this.dozeParametersProvider, FrameworkServicesModule_ProvideIWallPaperManagerFactory.create(), this.provideCommandQueueProvider, this.provideRecentsProvider, this.provideStatusBarProvider, this.recordingControllerProvider, this.protoTracerProvider, this.mediaOutputDialogFactoryProvider, this.deviceConfigProxyProvider, this.entryPointControllerProvider, this.telephonyListenerManagerProvider, this.systemStatusAnimationSchedulerProvider, this.privacyDotViewControllerProvider, this.factoryProvider5, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider, this.featureFlagsProvider, this.statusBarContentInsetsProvider));
            this.provideClockInfoListProvider = ClockModule_ProvideClockInfoListFactory.create(this.clockManagerProvider);
            this.keyguardZenAlarmViewControllerProvider = KeyguardZenAlarmViewController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideBcSmartspaceDataPluginProvider, this.zenModeControllerImplProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideAlarmManagerProvider, this.nextAlarmControllerImplProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider);
            this.keyguardMediaViewControllerProvider = KeyguardMediaViewController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideBcSmartspaceDataPluginProvider, this.provideMainDelayableExecutorProvider, this.provideNotificationMediaManagerProvider, this.providesBroadcastDispatcherProvider);
            this.keyguardSmartspaceControllerProvider = DoubleCheck.provider(KeyguardSmartspaceController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.featureFlagsProvider, this.keyguardZenAlarmViewControllerProvider, this.keyguardMediaViewControllerProvider));
            this.provideAllowNotificationLongPressProvider = DoubleCheck.provider(SystemUIGoogleModule_ProvideAllowNotificationLongPressFactory.create());
            this.keyguardQsUserSwitchComponentFactoryProvider = new Provider<KeyguardQsUserSwitchComponent.Factory>() { // from class: com.google.android.systemui.dagger.DaggerSysUIGoogleGlobalRootComponent.SysUIGoogleSysUIComponentImpl.10
                @Override // javax.inject.Provider
                public KeyguardQsUserSwitchComponent.Factory get() {
                    return new KeyguardQsUserSwitchComponentFactory();
                }
            };
            this.keyguardUserSwitcherComponentFactoryProvider = new Provider<KeyguardUserSwitcherComponent.Factory>() { // from class: com.google.android.systemui.dagger.DaggerSysUIGoogleGlobalRootComponent.SysUIGoogleSysUIComponentImpl.11
                @Override // javax.inject.Provider
                public KeyguardUserSwitcherComponent.Factory get() {
                    return new KeyguardUserSwitcherComponentFactory();
                }
            };
            this.keyguardStatusBarViewComponentFactoryProvider = new Provider<KeyguardStatusBarViewComponent.Factory>() { // from class: com.google.android.systemui.dagger.DaggerSysUIGoogleGlobalRootComponent.SysUIGoogleSysUIComponentImpl.12
                @Override // javax.inject.Provider
                public KeyguardStatusBarViewComponent.Factory get() {
                    return new KeyguardStatusBarViewComponentFactory();
                }
            };
            this.qSDetailDisplayerProvider = DoubleCheck.provider(QSDetailDisplayer_Factory.create());
            this.quickAccessWalletControllerProvider = DoubleCheck.provider(QuickAccessWalletController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, this.secureSettingsImplProvider, this.provideQuickAccessWalletClientProvider, this.bindSystemClockProvider));
            Provider<Boolean> provider = DoubleCheck.provider(ControlsModule_ProvidesControlsFeatureEnabledFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.providePackageManagerProvider));
            this.providesControlsFeatureEnabledProvider = provider;
            this.controlsComponentProvider = DoubleCheck.provider(ControlsComponent_Factory.create(provider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.controlsControllerImplProvider, this.controlsUiControllerImplProvider, this.controlsListingControllerImplProvider, this.provideLockPatternUtilsProvider, this.keyguardStateControllerImplProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider));
            this.qSTileHostProvider = new DelegateFactory();
            Provider<LogBuffer> provider2 = DoubleCheck.provider(LogModule_ProvideQuickSettingsLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideQuickSettingsLogBufferProvider = provider2;
            this.qSLoggerProvider = QSLogger_Factory.create(provider2);
            this.customTileStatePersisterProvider = CustomTileStatePersister_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider);
            this.builderProvider10 = CustomTile_Builder_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.customTileStatePersisterProvider);
            this.wifiTileProvider = WifiTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.networkControllerImplProvider, this.provideAccessPointControllerImplProvider);
            this.internetTileProvider = InternetTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.networkControllerImplProvider);
            this.bluetoothTileProvider = BluetoothTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.bluetoothControllerImplProvider);
            this.cellularTileProvider = CellularTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.networkControllerImplProvider);
            this.dndTileProvider = DndTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.zenModeControllerImplProvider, this.provideSharePreferencesProvider);
            this.colorInversionTileProvider = ColorInversionTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider);
            this.airplaneModeTileProvider = AirplaneModeTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.providesBroadcastDispatcherProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideConnectivityManagagerProvider);
            this.workModeTileProvider = WorkModeTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.managedProfileControllerImplProvider);
            this.rotationLockTileProvider = RotationLockTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.rotationLockControllerImplProvider);
            this.flashlightTileProvider = FlashlightTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.flashlightControllerImplProvider);
            this.locationTileProvider = LocationTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.locationControllerImplProvider, this.keyguardStateControllerImplProvider);
            this.castTileProvider = CastTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.castControllerImplProvider, this.keyguardStateControllerImplProvider, this.networkControllerImplProvider, this.hotspotControllerImplProvider);
            this.hotspotTileProvider = HotspotTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.hotspotControllerImplProvider, this.provideDataSaverControllerProvider);
            this.userTileProvider = UserTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.userSwitcherControllerProvider, this.userInfoControllerImplProvider);
            this.batterySaverTileGoogleProvider = BatterySaverTileGoogle_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.provideBatteryControllerProvider, this.secureSettingsImplProvider);
            this.dataSaverTileProvider = DataSaverTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.provideDataSaverControllerProvider);
            this.builderProvider11 = NightDisplayListenerModule_Builder_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideBgHandlerProvider);
            this.nightDisplayTileProvider = NightDisplayTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.locationControllerImplProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideColorDisplayManagerProvider, this.builderProvider11);
            this.nfcTileProvider = NfcTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.providesBroadcastDispatcherProvider);
            this.memoryTileProvider = GarbageMonitor_MemoryTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.garbageMonitorProvider);
            this.uiModeNightTileProvider = UiModeNightTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.provideConfigurationControllerProvider, this.provideBatteryControllerProvider, this.locationControllerImplProvider);
            this.screenRecordTileProvider = ScreenRecordTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.recordingControllerProvider, this.keyguardDismissUtilProvider);
            this.provideIThermalServiceProvider = DoubleCheck.provider(SystemUIGoogleModule_ProvideIThermalServiceFactory.create());
            this.reverseChargingTileProvider = ReverseChargingTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.provideBatteryControllerProvider, this.provideIThermalServiceProvider);
            Provider<Boolean> provider3 = DoubleCheck.provider(QSFlagsModule_IsReduceBrightColorsAvailableFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider));
            this.isReduceBrightColorsAvailableProvider = provider3;
            this.reduceBrightColorsTileProvider = ReduceBrightColorsTile_Factory.create(provider3, this.provideReduceBrightColorsListenerProvider, this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider);
            this.cameraToggleTileProvider = CameraToggleTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.provideMetricsLoggerProvider, this.falsingManagerProxyProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.provideIndividualSensorPrivacyControllerProvider, this.keyguardStateControllerImplProvider);
            this.microphoneToggleTileProvider = MicrophoneToggleTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.provideMetricsLoggerProvider, this.falsingManagerProxyProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.provideIndividualSensorPrivacyControllerProvider, this.keyguardStateControllerImplProvider);
            this.deviceControlsTileProvider = DeviceControlsTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.controlsComponentProvider, this.keyguardStateControllerImplProvider);
            this.alarmTileProvider = AlarmTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.provideUserTrackerProvider, this.nextAlarmControllerImplProvider);
            this.overlayToggleTileProvider = OverlayToggleTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideOverlayManagerProvider);
            QuickAccessWalletTile_Factory create = QuickAccessWalletTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.keyguardStateControllerImplProvider, DaggerSysUIGoogleGlobalRootComponent.this.providePackageManagerProvider, this.secureSettingsImplProvider, this.quickAccessWalletControllerProvider);
            this.quickAccessWalletTileProvider = create;
            this.qSFactoryImplGoogleProvider = DoubleCheck.provider(QSFactoryImplGoogle_Factory.create(this.qSTileHostProvider, this.builderProvider10, this.wifiTileProvider, this.internetTileProvider, this.bluetoothTileProvider, this.cellularTileProvider, this.dndTileProvider, this.colorInversionTileProvider, this.airplaneModeTileProvider, this.workModeTileProvider, this.rotationLockTileProvider, this.flashlightTileProvider, this.locationTileProvider, this.castTileProvider, this.hotspotTileProvider, this.userTileProvider, this.batterySaverTileGoogleProvider, this.dataSaverTileProvider, this.nightDisplayTileProvider, this.nfcTileProvider, this.memoryTileProvider, this.uiModeNightTileProvider, this.screenRecordTileProvider, this.reverseChargingTileProvider, this.reduceBrightColorsTileProvider, this.cameraToggleTileProvider, this.microphoneToggleTileProvider, this.deviceControlsTileProvider, this.alarmTileProvider, this.overlayToggleTileProvider, create));
            this.builderProvider12 = AutoAddTracker_Builder_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider);
            this.deviceControlsControllerImplProvider = DoubleCheck.provider(DeviceControlsControllerImpl_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.controlsComponentProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider));
            this.walletControllerImplProvider = DoubleCheck.provider(WalletControllerImpl_Factory.create(this.provideQuickAccessWalletClientProvider));
            this.provideAutoTileManagerProvider = QSModuleGoogle_ProvideAutoTileManagerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.builderProvider12, this.qSTileHostProvider, this.provideBgHandlerProvider, this.secureSettingsImplProvider, this.hotspotControllerImplProvider, this.provideDataSaverControllerProvider, this.managedProfileControllerImplProvider, this.provideNightDisplayListenerProvider, this.castControllerImplProvider, this.provideBatteryControllerProvider, this.provideReduceBrightColorsListenerProvider, this.deviceControlsControllerImplProvider, this.walletControllerImplProvider, this.isReduceBrightColorsAvailableProvider);
            DelegateFactory.setDelegate(this.qSTileHostProvider, DoubleCheck.provider(QSTileHost_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.statusBarIconControllerImplProvider, this.qSFactoryImplGoogleProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, this.provideBgLooperProvider, this.providePluginManagerProvider, this.tunerServiceImplProvider, this.provideAutoTileManagerProvider, this.dumpManagerProvider, this.providesBroadcastDispatcherProvider, this.optionalOfStatusBarProvider, this.qSLoggerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider, this.customTileStatePersisterProvider)));
            this.providesQSMediaHostProvider = DoubleCheck.provider(MediaModule_ProvidesQSMediaHostFactory.create(MediaHost_MediaHostStateHolder_Factory.create(), this.mediaHierarchyManagerProvider, this.mediaDataManagerProvider, this.mediaHostStatesManagerProvider));
            this.providesQuickQSMediaHostProvider = DoubleCheck.provider(MediaModule_ProvidesQuickQSMediaHostFactory.create(MediaHost_MediaHostStateHolder_Factory.create(), this.mediaHierarchyManagerProvider, this.mediaDataManagerProvider, this.mediaHostStatesManagerProvider));
            this.subscriptionManagerSlotIndexResolverProvider = DoubleCheck.provider(QSCarrierGroupController_SubscriptionManagerSlotIndexResolver_Factory.create());
            this.privacyDialogControllerProvider = DoubleCheck.provider(PrivacyDialogController_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.providePermissionManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.providePackageManagerProvider, this.privacyItemControllerProvider, this.provideUserTrackerProvider, this.activityStarterDelegateProvider, this.provideBackgroundExecutorProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, this.privacyLoggerProvider, this.keyguardStateControllerImplProvider, this.appOpsControllerImplProvider));
            this.isPMLiteEnabledProvider = DoubleCheck.provider(QSFlagsModule_IsPMLiteEnabledFactory.create(this.featureFlagsProvider, this.globalSettingsImplProvider));
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public BootCompleteCacheImpl provideBootCacheImpl() {
            return this.bootCompleteCacheImplProvider.get();
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public ConfigurationController getConfigurationController() {
            return this.provideConfigurationControllerProvider.get();
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public ContextComponentHelper getContextComponentHelper() {
            return this.contextComponentResolverProvider.get();
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public Dependency createDependency() {
            return this.dependencyProvider2.get();
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public DumpManager createDumpManager() {
            return this.dumpManagerProvider.get();
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public InitController getInitController() {
            return this.initControllerProvider.get();
        }

        public InjectionInflationController.ViewInstanceCreator.Factory createViewInstanceCreatorFactory() {
            return new ViewInstanceCreatorFactory();
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public void inject(SystemUIAppComponentFactory systemUIAppComponentFactory) {
            injectSystemUIAppComponentFactory(systemUIAppComponentFactory);
        }

        public void inject(KeyguardSliceProvider keyguardSliceProvider) {
            injectKeyguardSliceProvider(keyguardSliceProvider);
        }

        public void inject(ClockOptionsProvider clockOptionsProvider) {
            injectClockOptionsProvider(clockOptionsProvider);
        }

        public void inject(PeopleProvider peopleProvider) {
            injectPeopleProvider(peopleProvider);
        }

        public void inject(KeyguardSliceProviderGoogle keyguardSliceProviderGoogle) {
            injectKeyguardSliceProviderGoogle(keyguardSliceProviderGoogle);
        }

        @Override // com.google.android.systemui.dagger.SysUIGoogleSysUIComponent
        public KeyguardSmartspaceController createKeyguardSmartspaceController() {
            return this.keyguardSmartspaceControllerProvider.get();
        }

        private SystemUIAppComponentFactory injectSystemUIAppComponentFactory(SystemUIAppComponentFactory systemUIAppComponentFactory) {
            SystemUIAppComponentFactory_MembersInjector.injectMComponentHelper(systemUIAppComponentFactory, this.contextComponentResolverProvider.get());
            return systemUIAppComponentFactory;
        }

        private KeyguardSliceProvider injectKeyguardSliceProvider(KeyguardSliceProvider keyguardSliceProvider) {
            KeyguardSliceProvider_MembersInjector.injectMDozeParameters(keyguardSliceProvider, this.dozeParametersProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMZenModeController(keyguardSliceProvider, this.zenModeControllerImplProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMNextAlarmController(keyguardSliceProvider, this.nextAlarmControllerImplProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMAlarmManager(keyguardSliceProvider, (AlarmManager) DaggerSysUIGoogleGlobalRootComponent.this.provideAlarmManagerProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMContentResolver(keyguardSliceProvider, (ContentResolver) DaggerSysUIGoogleGlobalRootComponent.this.provideContentResolverProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMMediaManager(keyguardSliceProvider, this.provideNotificationMediaManagerProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMStatusBarStateController(keyguardSliceProvider, this.statusBarStateControllerImplProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMKeyguardBypassController(keyguardSliceProvider, this.keyguardBypassControllerProvider.get());
            return keyguardSliceProvider;
        }

        private ClockOptionsProvider injectClockOptionsProvider(ClockOptionsProvider clockOptionsProvider) {
            ClockOptionsProvider_MembersInjector.injectMClockInfosProvider(clockOptionsProvider, this.provideClockInfoListProvider);
            return clockOptionsProvider;
        }

        private PeopleProvider injectPeopleProvider(PeopleProvider peopleProvider) {
            PeopleProvider_MembersInjector.injectMPeopleSpaceWidgetManager(peopleProvider, this.peopleSpaceWidgetManagerProvider.get());
            return peopleProvider;
        }

        private KeyguardSliceProviderGoogle injectKeyguardSliceProviderGoogle(KeyguardSliceProviderGoogle keyguardSliceProviderGoogle) {
            KeyguardSliceProvider_MembersInjector.injectMDozeParameters(keyguardSliceProviderGoogle, this.dozeParametersProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMZenModeController(keyguardSliceProviderGoogle, this.zenModeControllerImplProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMNextAlarmController(keyguardSliceProviderGoogle, this.nextAlarmControllerImplProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMAlarmManager(keyguardSliceProviderGoogle, (AlarmManager) DaggerSysUIGoogleGlobalRootComponent.this.provideAlarmManagerProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMContentResolver(keyguardSliceProviderGoogle, (ContentResolver) DaggerSysUIGoogleGlobalRootComponent.this.provideContentResolverProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMMediaManager(keyguardSliceProviderGoogle, this.provideNotificationMediaManagerProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMStatusBarStateController(keyguardSliceProviderGoogle, this.statusBarStateControllerImplProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMKeyguardBypassController(keyguardSliceProviderGoogle, this.keyguardBypassControllerProvider.get());
            KeyguardSliceProviderGoogle_MembersInjector.injectMSmartSpaceController(keyguardSliceProviderGoogle, this.smartSpaceControllerProvider.get());
            return keyguardSliceProviderGoogle;
        }

        /* access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public final class ExpandableNotificationRowComponentBuilder implements ExpandableNotificationRowComponent.Builder {
            private ExpandableNotificationRow expandableNotificationRow;
            private NotificationListContainer listContainer;
            private NotificationEntry notificationEntry;
            private ExpandableNotificationRow.OnExpandClickListener onExpandClickListener;

            private ExpandableNotificationRowComponentBuilder() {
            }

            @Override // com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent.Builder
            public ExpandableNotificationRowComponentBuilder expandableNotificationRow(ExpandableNotificationRow expandableNotificationRow) {
                this.expandableNotificationRow = (ExpandableNotificationRow) Preconditions.checkNotNull(expandableNotificationRow);
                return this;
            }

            @Override // com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent.Builder
            public ExpandableNotificationRowComponentBuilder notificationEntry(NotificationEntry notificationEntry) {
                this.notificationEntry = (NotificationEntry) Preconditions.checkNotNull(notificationEntry);
                return this;
            }

            @Override // com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent.Builder
            public ExpandableNotificationRowComponentBuilder onExpandClickListener(ExpandableNotificationRow.OnExpandClickListener onExpandClickListener) {
                this.onExpandClickListener = (ExpandableNotificationRow.OnExpandClickListener) Preconditions.checkNotNull(onExpandClickListener);
                return this;
            }

            @Override // com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent.Builder
            public ExpandableNotificationRowComponentBuilder listContainer(NotificationListContainer notificationListContainer) {
                this.listContainer = (NotificationListContainer) Preconditions.checkNotNull(notificationListContainer);
                return this;
            }

            @Override // com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent.Builder
            public ExpandableNotificationRowComponent build() {
                Preconditions.checkBuilderRequirement(this.expandableNotificationRow, ExpandableNotificationRow.class);
                Preconditions.checkBuilderRequirement(this.notificationEntry, NotificationEntry.class);
                Preconditions.checkBuilderRequirement(this.onExpandClickListener, ExpandableNotificationRow.OnExpandClickListener.class);
                Preconditions.checkBuilderRequirement(this.listContainer, NotificationListContainer.class);
                return new ExpandableNotificationRowComponentImpl(this.expandableNotificationRow, this.notificationEntry, this.onExpandClickListener, this.listContainer);
            }
        }

        /* loaded from: classes2.dex */
        private final class ExpandableNotificationRowComponentImpl implements ExpandableNotificationRowComponent {
            private Provider<ActivatableNotificationViewController> activatableNotificationViewControllerProvider;
            private Provider<ExpandableNotificationRowController> expandableNotificationRowControllerProvider;
            private Provider<ExpandableNotificationRow> expandableNotificationRowProvider;
            private Provider<ExpandableOutlineViewController> expandableOutlineViewControllerProvider;
            private Provider<ExpandableViewController> expandableViewControllerProvider;
            private Provider<NotificationTapHelper.Factory> factoryProvider;
            private Provider<NotificationListContainer> listContainerProvider;
            private Provider<NotificationEntry> notificationEntryProvider;
            private Provider<ExpandableNotificationRow.OnExpandClickListener> onExpandClickListenerProvider;
            private Provider<String> provideAppNameProvider;
            private Provider<String> provideNotificationKeyProvider;
            private Provider<StatusBarNotification> provideStatusBarNotificationProvider;

            private ExpandableNotificationRowComponentImpl(ExpandableNotificationRow expandableNotificationRow, NotificationEntry notificationEntry, ExpandableNotificationRow.OnExpandClickListener onExpandClickListener, NotificationListContainer notificationListContainer) {
                initialize(expandableNotificationRow, notificationEntry, onExpandClickListener, notificationListContainer);
            }

            private void initialize(ExpandableNotificationRow expandableNotificationRow, NotificationEntry notificationEntry, ExpandableNotificationRow.OnExpandClickListener onExpandClickListener, NotificationListContainer notificationListContainer) {
                this.expandableNotificationRowProvider = InstanceFactory.create(expandableNotificationRow);
                this.listContainerProvider = InstanceFactory.create(notificationListContainer);
                this.factoryProvider = NotificationTapHelper_Factory_Factory.create(SysUIGoogleSysUIComponentImpl.this.falsingManagerProxyProvider, SysUIGoogleSysUIComponentImpl.this.provideMainDelayableExecutorProvider);
                ExpandableViewController_Factory create = ExpandableViewController_Factory.create(this.expandableNotificationRowProvider);
                this.expandableViewControllerProvider = create;
                ExpandableOutlineViewController_Factory create2 = ExpandableOutlineViewController_Factory.create(this.expandableNotificationRowProvider, create);
                this.expandableOutlineViewControllerProvider = create2;
                this.activatableNotificationViewControllerProvider = ActivatableNotificationViewController_Factory.create(this.expandableNotificationRowProvider, this.factoryProvider, create2, DaggerSysUIGoogleGlobalRootComponent.this.provideAccessibilityManagerProvider, SysUIGoogleSysUIComponentImpl.this.falsingManagerProxyProvider, SysUIGoogleSysUIComponentImpl.this.falsingCollectorImplProvider);
                Factory create3 = InstanceFactory.create(notificationEntry);
                this.notificationEntryProvider = create3;
                this.provideStatusBarNotificationProvider = ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory.create(create3);
                this.provideAppNameProvider = ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideAppNameFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.provideStatusBarNotificationProvider);
                this.provideNotificationKeyProvider = ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideNotificationKeyFactory.create(this.provideStatusBarNotificationProvider);
                this.onExpandClickListenerProvider = InstanceFactory.create(onExpandClickListener);
                this.expandableNotificationRowControllerProvider = DoubleCheck.provider(ExpandableNotificationRowController_Factory.create(this.expandableNotificationRowProvider, this.listContainerProvider, this.activatableNotificationViewControllerProvider, SysUIGoogleSysUIComponentImpl.this.provideNotificationMediaManagerProvider, SysUIGoogleSysUIComponentImpl.this.providePluginManagerProvider, SysUIGoogleSysUIComponentImpl.this.bindSystemClockProvider, this.provideAppNameProvider, this.provideNotificationKeyProvider, SysUIGoogleSysUIComponentImpl.this.keyguardBypassControllerProvider, SysUIGoogleSysUIComponentImpl.this.provideGroupMembershipManagerProvider, SysUIGoogleSysUIComponentImpl.this.provideGroupExpansionManagerProvider, SysUIGoogleSysUIComponentImpl.this.rowContentBindStageProvider, SysUIGoogleSysUIComponentImpl.this.provideNotificationLoggerProvider, SysUIGoogleSysUIComponentImpl.this.provideHeadsUpManagerPhoneProvider, this.onExpandClickListenerProvider, SysUIGoogleSysUIComponentImpl.this.statusBarStateControllerImplProvider, SysUIGoogleSysUIComponentImpl.this.provideNotificationGutsManagerProvider, SysUIGoogleSysUIComponentImpl.this.provideAllowNotificationLongPressProvider, SysUIGoogleSysUIComponentImpl.this.provideOnUserInteractionCallbackProvider, SysUIGoogleSysUIComponentImpl.this.falsingManagerProxyProvider, SysUIGoogleSysUIComponentImpl.this.falsingCollectorImplProvider, SysUIGoogleSysUIComponentImpl.this.peopleNotificationIdentifierImplProvider, SysUIGoogleSysUIComponentImpl.this.provideBubblesManagerProvider));
            }

            @Override // com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent
            public ExpandableNotificationRowController getExpandableNotificationRowController() {
                return this.expandableNotificationRowControllerProvider.get();
            }
        }

        /* access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public final class KeyguardStatusViewComponentFactory implements KeyguardStatusViewComponent.Factory {
            private KeyguardStatusViewComponentFactory() {
            }

            @Override // com.android.keyguard.dagger.KeyguardStatusViewComponent.Factory
            public KeyguardStatusViewComponent build(KeyguardStatusView keyguardStatusView) {
                Preconditions.checkNotNull(keyguardStatusView);
                return new KeyguardStatusViewComponentImpl(keyguardStatusView);
            }
        }

        /* loaded from: classes2.dex */
        private final class KeyguardStatusViewComponentImpl implements KeyguardStatusViewComponent {
            private final KeyguardStatusView arg0;
            private Provider<KeyguardStatusView> arg0Provider;
            private Provider<KeyguardClockSwitch> getKeyguardClockSwitchProvider;
            private Provider<KeyguardSliceView> getKeyguardSliceViewProvider;
            private Provider<KeyguardSliceViewController> keyguardSliceViewControllerProvider;

            private KeyguardStatusViewComponentImpl(KeyguardStatusView keyguardStatusView) {
                this.arg0 = keyguardStatusView;
                initialize(keyguardStatusView);
            }

            private KeyguardClockSwitch keyguardClockSwitch() {
                return KeyguardStatusViewModule_GetKeyguardClockSwitchFactory.getKeyguardClockSwitch(this.arg0);
            }

            private void initialize(KeyguardStatusView keyguardStatusView) {
                Factory create = InstanceFactory.create(keyguardStatusView);
                this.arg0Provider = create;
                KeyguardStatusViewModule_GetKeyguardClockSwitchFactory create2 = KeyguardStatusViewModule_GetKeyguardClockSwitchFactory.create(create);
                this.getKeyguardClockSwitchProvider = create2;
                KeyguardStatusViewModule_GetKeyguardSliceViewFactory create3 = KeyguardStatusViewModule_GetKeyguardSliceViewFactory.create(create2);
                this.getKeyguardSliceViewProvider = create3;
                this.keyguardSliceViewControllerProvider = DoubleCheck.provider(KeyguardSliceViewController_Factory.create(create3, SysUIGoogleSysUIComponentImpl.this.activityStarterDelegateProvider, SysUIGoogleSysUIComponentImpl.this.provideConfigurationControllerProvider, SysUIGoogleSysUIComponentImpl.this.tunerServiceImplProvider, SysUIGoogleSysUIComponentImpl.this.dumpManagerProvider));
            }

            @Override // com.android.keyguard.dagger.KeyguardStatusViewComponent
            public KeyguardClockSwitchController getKeyguardClockSwitchController() {
                return new KeyguardClockSwitchController(keyguardClockSwitch(), (StatusBarStateController) SysUIGoogleSysUIComponentImpl.this.statusBarStateControllerImplProvider.get(), (SysuiColorExtractor) SysUIGoogleSysUIComponentImpl.this.sysuiColorExtractorProvider.get(), (ClockManager) SysUIGoogleSysUIComponentImpl.this.clockManagerProvider.get(), this.keyguardSliceViewControllerProvider.get(), (NotificationIconAreaController) SysUIGoogleSysUIComponentImpl.this.notificationIconAreaControllerProvider.get(), (BroadcastDispatcher) SysUIGoogleSysUIComponentImpl.this.providesBroadcastDispatcherProvider.get(), (BatteryController) SysUIGoogleSysUIComponentImpl.this.provideBatteryControllerProvider.get(), (KeyguardUpdateMonitor) SysUIGoogleSysUIComponentImpl.this.keyguardUpdateMonitorProvider.get(), (KeyguardBypassController) SysUIGoogleSysUIComponentImpl.this.keyguardBypassControllerProvider.get(), (LockscreenSmartspaceController) SysUIGoogleSysUIComponentImpl.this.lockscreenSmartspaceControllerProvider.get(), (KeyguardUnlockAnimationController) SysUIGoogleSysUIComponentImpl.this.keyguardUnlockAnimationControllerProvider.get(), (SmartspaceTransitionController) SysUIGoogleSysUIComponentImpl.this.provideSmartspaceTransitionControllerProvider.get());
            }

            @Override // com.android.keyguard.dagger.KeyguardStatusViewComponent
            public KeyguardStatusViewController getKeyguardStatusViewController() {
                return new KeyguardStatusViewController(this.arg0, this.keyguardSliceViewControllerProvider.get(), getKeyguardClockSwitchController(), (KeyguardStateController) SysUIGoogleSysUIComponentImpl.this.keyguardStateControllerImplProvider.get(), (KeyguardUpdateMonitor) SysUIGoogleSysUIComponentImpl.this.keyguardUpdateMonitorProvider.get(), (ConfigurationController) SysUIGoogleSysUIComponentImpl.this.provideConfigurationControllerProvider.get(), (DozeParameters) SysUIGoogleSysUIComponentImpl.this.dozeParametersProvider.get(), (KeyguardUnlockAnimationController) SysUIGoogleSysUIComponentImpl.this.keyguardUnlockAnimationControllerProvider.get(), (SmartspaceTransitionController) SysUIGoogleSysUIComponentImpl.this.provideSmartspaceTransitionControllerProvider.get(), (UnlockedScreenOffAnimationController) SysUIGoogleSysUIComponentImpl.this.unlockedScreenOffAnimationControllerProvider.get());
            }
        }

        /* access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public final class KeyguardBouncerComponentFactory implements KeyguardBouncerComponent.Factory {
            private KeyguardBouncerComponentFactory() {
            }

            @Override // com.android.keyguard.dagger.KeyguardBouncerComponent.Factory
            public KeyguardBouncerComponent create() {
                return new KeyguardBouncerComponentImpl();
            }
        }

        /* loaded from: classes2.dex */
        private final class KeyguardBouncerComponentImpl implements KeyguardBouncerComponent {
            private Provider<AdminSecondaryLockScreenController.Factory> factoryProvider;
            private Provider<EmergencyButtonController.Factory> factoryProvider2;
            private Provider<KeyguardInputViewController.Factory> factoryProvider3;
            private Provider factoryProvider4;
            private Provider<KeyguardHostViewController> keyguardHostViewControllerProvider;
            private Provider<KeyguardRootViewController> keyguardRootViewControllerProvider;
            private Provider<KeyguardSecurityViewFlipperController> keyguardSecurityViewFlipperControllerProvider;
            private Provider liftToActivateListenerProvider;
            private Provider<KeyguardHostView> providesKeyguardHostViewProvider;
            private Provider<KeyguardSecurityContainer> providesKeyguardSecurityContainerProvider;
            private Provider<KeyguardSecurityViewFlipper> providesKeyguardSecurityViewFlipperProvider;
            private Provider<ViewGroup> providesRootViewProvider;

            private KeyguardBouncerComponentImpl() {
                initialize();
            }

            private void initialize() {
                Provider<ViewGroup> provider = DoubleCheck.provider(KeyguardBouncerModule_ProvidesRootViewFactory.create(SysUIGoogleSysUIComponentImpl.this.providerLayoutInflaterProvider));
                this.providesRootViewProvider = provider;
                this.keyguardRootViewControllerProvider = DoubleCheck.provider(KeyguardRootViewController_Factory.create(provider));
                Provider<KeyguardHostView> provider2 = DoubleCheck.provider(KeyguardBouncerModule_ProvidesKeyguardHostViewFactory.create(this.providesRootViewProvider));
                this.providesKeyguardHostViewProvider = provider2;
                this.providesKeyguardSecurityContainerProvider = DoubleCheck.provider(KeyguardBouncerModule_ProvidesKeyguardSecurityContainerFactory.create(provider2));
                this.factoryProvider = DoubleCheck.provider(AdminSecondaryLockScreenController_Factory_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.providesKeyguardSecurityContainerProvider, SysUIGoogleSysUIComponentImpl.this.keyguardUpdateMonitorProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider));
                this.providesKeyguardSecurityViewFlipperProvider = DoubleCheck.provider(KeyguardBouncerModule_ProvidesKeyguardSecurityViewFlipperFactory.create(this.providesKeyguardSecurityContainerProvider));
                this.liftToActivateListenerProvider = LiftToActivateListener_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideAccessibilityManagerProvider);
                this.factoryProvider2 = EmergencyButtonController_Factory_Factory.create(SysUIGoogleSysUIComponentImpl.this.provideConfigurationControllerProvider, SysUIGoogleSysUIComponentImpl.this.keyguardUpdateMonitorProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideTelephonyManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.providePowerManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideActivityTaskManagerProvider, SysUIGoogleSysUIComponentImpl.this.shadeControllerImplProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideTelecomManagerProvider, SysUIGoogleSysUIComponentImpl.this.provideMetricsLoggerProvider);
                this.factoryProvider3 = KeyguardInputViewController_Factory_Factory.create(SysUIGoogleSysUIComponentImpl.this.keyguardUpdateMonitorProvider, SysUIGoogleSysUIComponentImpl.this.provideLockPatternUtilsProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideLatencyTrackerProvider, SysUIGoogleSysUIComponentImpl.this.factoryProvider2, DaggerSysUIGoogleGlobalRootComponent.this.provideInputMethodManagerProvider, SysUIGoogleSysUIComponentImpl.this.provideMainDelayableExecutorProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideResourcesProvider, this.liftToActivateListenerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideTelephonyManagerProvider, SysUIGoogleSysUIComponentImpl.this.falsingCollectorImplProvider, this.factoryProvider2);
                this.keyguardSecurityViewFlipperControllerProvider = DoubleCheck.provider(KeyguardSecurityViewFlipperController_Factory.create(this.providesKeyguardSecurityViewFlipperProvider, SysUIGoogleSysUIComponentImpl.this.providerLayoutInflaterProvider, this.factoryProvider3, this.factoryProvider2));
                this.factoryProvider4 = KeyguardSecurityContainerController_Factory_Factory.create(this.providesKeyguardSecurityContainerProvider, this.factoryProvider, SysUIGoogleSysUIComponentImpl.this.provideLockPatternUtilsProvider, SysUIGoogleSysUIComponentImpl.this.keyguardUpdateMonitorProvider, SysUIGoogleSysUIComponentImpl.this.keyguardSecurityModelProvider, SysUIGoogleSysUIComponentImpl.this.provideMetricsLoggerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider, SysUIGoogleSysUIComponentImpl.this.keyguardStateControllerImplProvider, this.keyguardSecurityViewFlipperControllerProvider, SysUIGoogleSysUIComponentImpl.this.provideConfigurationControllerProvider);
                this.keyguardHostViewControllerProvider = DoubleCheck.provider(KeyguardHostViewController_Factory.create(this.providesKeyguardHostViewProvider, SysUIGoogleSysUIComponentImpl.this.keyguardUpdateMonitorProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideAudioManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideTelephonyManagerProvider, SysUIGoogleSysUIComponentImpl.this.providesViewMediatorCallbackProvider, this.factoryProvider4));
            }

            @Override // com.android.keyguard.dagger.KeyguardBouncerComponent
            public KeyguardRootViewController getKeyguardRootViewController() {
                return this.keyguardRootViewControllerProvider.get();
            }

            @Override // com.android.keyguard.dagger.KeyguardBouncerComponent
            public KeyguardHostViewController getKeyguardHostViewController() {
                return this.keyguardHostViewControllerProvider.get();
            }
        }

        /* access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public final class SectionHeaderControllerSubcomponentBuilder implements SectionHeaderControllerSubcomponent.Builder {
            private String clickIntentAction;
            private Integer headerText;
            private String nodeLabel;

            private SectionHeaderControllerSubcomponentBuilder() {
            }

            @Override // com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent.Builder
            public SectionHeaderControllerSubcomponentBuilder nodeLabel(String str) {
                this.nodeLabel = (String) Preconditions.checkNotNull(str);
                return this;
            }

            @Override // com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent.Builder
            public SectionHeaderControllerSubcomponentBuilder headerText(int i) {
                this.headerText = (Integer) Preconditions.checkNotNull(Integer.valueOf(i));
                return this;
            }

            @Override // com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent.Builder
            public SectionHeaderControllerSubcomponentBuilder clickIntentAction(String str) {
                this.clickIntentAction = (String) Preconditions.checkNotNull(str);
                return this;
            }

            @Override // com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent.Builder
            public SectionHeaderControllerSubcomponent build() {
                Preconditions.checkBuilderRequirement(this.nodeLabel, String.class);
                Preconditions.checkBuilderRequirement(this.headerText, Integer.class);
                Preconditions.checkBuilderRequirement(this.clickIntentAction, String.class);
                return new SectionHeaderControllerSubcomponentImpl(this.nodeLabel, this.headerText, this.clickIntentAction);
            }
        }

        /* loaded from: classes2.dex */
        private final class SectionHeaderControllerSubcomponentImpl implements SectionHeaderControllerSubcomponent {
            private Provider<String> clickIntentActionProvider;
            private Provider<Integer> headerTextProvider;
            private Provider<String> nodeLabelProvider;
            private Provider<SectionHeaderNodeControllerImpl> sectionHeaderNodeControllerImplProvider;

            private SectionHeaderControllerSubcomponentImpl(String str, Integer num, String str2) {
                initialize(str, num, str2);
            }

            private void initialize(String str, Integer num, String str2) {
                this.nodeLabelProvider = InstanceFactory.create(str);
                this.headerTextProvider = InstanceFactory.create(num);
                this.clickIntentActionProvider = InstanceFactory.create(str2);
                this.sectionHeaderNodeControllerImplProvider = DoubleCheck.provider(SectionHeaderNodeControllerImpl_Factory.create(this.nodeLabelProvider, SysUIGoogleSysUIComponentImpl.this.providerLayoutInflaterProvider, this.headerTextProvider, SysUIGoogleSysUIComponentImpl.this.activityStarterDelegateProvider, this.clickIntentActionProvider));
            }

            @Override // com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent
            public NodeController getNodeController() {
                return this.sectionHeaderNodeControllerImplProvider.get();
            }

            @Override // com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent
            public SectionHeaderController getHeaderController() {
                return this.sectionHeaderNodeControllerImplProvider.get();
            }
        }

        /* access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public final class ViewInstanceCreatorFactory implements InjectionInflationController.ViewInstanceCreator.Factory {
            private ViewInstanceCreatorFactory() {
            }

            @Override // com.android.systemui.util.InjectionInflationController.ViewInstanceCreator.Factory
            public InjectionInflationController.ViewInstanceCreator build(Context context, AttributeSet attributeSet) {
                Preconditions.checkNotNull(context);
                Preconditions.checkNotNull(attributeSet);
                return new ViewInstanceCreatorImpl(context, attributeSet);
            }
        }

        /* loaded from: classes2.dex */
        private final class ViewInstanceCreatorImpl implements InjectionInflationController.ViewInstanceCreator {
            private final Context arg0;
            private final AttributeSet arg1;

            private ViewInstanceCreatorImpl(Context context, AttributeSet attributeSet) {
                this.arg0 = context;
                this.arg1 = attributeSet;
            }

            @Override // com.android.systemui.util.InjectionInflationController.ViewInstanceCreator
            public NotificationStackScrollLayout createNotificationStackScrollLayout() {
                return new NotificationStackScrollLayout(this.arg0, this.arg1, SysUIGoogleSysUIComponentImpl.this.notificationSectionsManager(), (GroupMembershipManager) SysUIGoogleSysUIComponentImpl.this.provideGroupMembershipManagerProvider.get(), (GroupExpansionManager) SysUIGoogleSysUIComponentImpl.this.provideGroupExpansionManagerProvider.get(), (AmbientState) SysUIGoogleSysUIComponentImpl.this.ambientStateProvider.get(), (FeatureFlags) SysUIGoogleSysUIComponentImpl.this.featureFlagsProvider.get(), (UnlockedScreenOffAnimationController) SysUIGoogleSysUIComponentImpl.this.unlockedScreenOffAnimationControllerProvider.get());
            }
        }

        /* access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public final class NotificationShelfComponentBuilder implements NotificationShelfComponent.Builder {
            private NotificationShelf notificationShelf;

            private NotificationShelfComponentBuilder() {
            }

            @Override // com.android.systemui.statusbar.notification.row.dagger.NotificationShelfComponent.Builder
            public NotificationShelfComponentBuilder notificationShelf(NotificationShelf notificationShelf) {
                this.notificationShelf = (NotificationShelf) Preconditions.checkNotNull(notificationShelf);
                return this;
            }

            @Override // com.android.systemui.statusbar.notification.row.dagger.NotificationShelfComponent.Builder
            public NotificationShelfComponent build() {
                Preconditions.checkBuilderRequirement(this.notificationShelf, NotificationShelf.class);
                return new NotificationShelfComponentImpl(this.notificationShelf);
            }
        }

        /* loaded from: classes2.dex */
        private final class NotificationShelfComponentImpl implements NotificationShelfComponent {
            private Provider<ActivatableNotificationViewController> activatableNotificationViewControllerProvider;
            private Provider<ExpandableOutlineViewController> expandableOutlineViewControllerProvider;
            private Provider<ExpandableViewController> expandableViewControllerProvider;
            private Provider<NotificationTapHelper.Factory> factoryProvider;
            private Provider<NotificationShelfController> notificationShelfControllerProvider;
            private Provider<NotificationShelf> notificationShelfProvider;

            private NotificationShelfComponentImpl(NotificationShelf notificationShelf) {
                initialize(notificationShelf);
            }

            private void initialize(NotificationShelf notificationShelf) {
                this.notificationShelfProvider = InstanceFactory.create(notificationShelf);
                this.factoryProvider = NotificationTapHelper_Factory_Factory.create(SysUIGoogleSysUIComponentImpl.this.falsingManagerProxyProvider, SysUIGoogleSysUIComponentImpl.this.provideMainDelayableExecutorProvider);
                ExpandableViewController_Factory create = ExpandableViewController_Factory.create(this.notificationShelfProvider);
                this.expandableViewControllerProvider = create;
                ExpandableOutlineViewController_Factory create2 = ExpandableOutlineViewController_Factory.create(this.notificationShelfProvider, create);
                this.expandableOutlineViewControllerProvider = create2;
                ActivatableNotificationViewController_Factory create3 = ActivatableNotificationViewController_Factory.create(this.notificationShelfProvider, this.factoryProvider, create2, DaggerSysUIGoogleGlobalRootComponent.this.provideAccessibilityManagerProvider, SysUIGoogleSysUIComponentImpl.this.falsingManagerProxyProvider, SysUIGoogleSysUIComponentImpl.this.falsingCollectorImplProvider);
                this.activatableNotificationViewControllerProvider = create3;
                this.notificationShelfControllerProvider = DoubleCheck.provider(NotificationShelfController_Factory.create(this.notificationShelfProvider, create3, SysUIGoogleSysUIComponentImpl.this.keyguardBypassControllerProvider, SysUIGoogleSysUIComponentImpl.this.statusBarStateControllerImplProvider));
            }

            @Override // com.android.systemui.statusbar.notification.row.dagger.NotificationShelfComponent
            public NotificationShelfController getNotificationShelfController() {
                return this.notificationShelfControllerProvider.get();
            }
        }

        /* access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public final class StatusBarComponentBuilder implements StatusBarComponent.Builder {
            private NotificationShadeWindowView statusBarWindowView;

            private StatusBarComponentBuilder() {
            }

            @Override // com.android.systemui.statusbar.phone.dagger.StatusBarComponent.Builder
            public StatusBarComponentBuilder statusBarWindowView(NotificationShadeWindowView notificationShadeWindowView) {
                this.statusBarWindowView = (NotificationShadeWindowView) Preconditions.checkNotNull(notificationShadeWindowView);
                return this;
            }

            @Override // com.android.systemui.statusbar.phone.dagger.StatusBarComponent.Builder
            public StatusBarComponent build() {
                Preconditions.checkBuilderRequirement(this.statusBarWindowView, NotificationShadeWindowView.class);
                return new StatusBarComponentImpl(this.statusBarWindowView);
            }
        }

        /* loaded from: classes2.dex */
        private final class StatusBarComponentImpl implements StatusBarComponent {
            private Provider<AuthRippleController> authRippleControllerProvider;
            private Provider<FlingAnimationUtils.Builder> builderProvider;
            private Provider builderProvider2;
            private Provider<AuthRippleView> getAuthRippleViewProvider;
            private Provider<LockIconView> getLockIconViewProvider;
            private Provider<NotificationPanelView> getNotificationPanelViewProvider;
            private Provider<TapAgainView> getTapAgainViewProvider;
            private Provider<LockIconViewController> lockIconViewControllerProvider;
            private Provider<NotificationPanelViewController> notificationPanelViewControllerProvider;
            private Provider<NotificationStackScrollLayoutController> notificationStackScrollLayoutControllerProvider;
            private final NotificationShadeWindowView statusBarWindowView;
            private Provider<NotificationShadeWindowView> statusBarWindowViewProvider;
            private Provider<TapAgainViewController> tapAgainViewControllerProvider;

            private StatusBarComponentImpl(NotificationShadeWindowView notificationShadeWindowView) {
                this.statusBarWindowView = notificationShadeWindowView;
                initialize(notificationShadeWindowView);
            }

            private void initialize(NotificationShadeWindowView notificationShadeWindowView) {
                Factory create = InstanceFactory.create(notificationShadeWindowView);
                this.statusBarWindowViewProvider = create;
                this.getNotificationPanelViewProvider = DoubleCheck.provider(StatusBarViewModule_GetNotificationPanelViewFactory.create(create));
                this.builderProvider = FlingAnimationUtils_Builder_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideDisplayMetricsProvider);
                this.builderProvider2 = NotificationSwipeHelper_Builder_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideResourcesProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideViewConfigurationProvider, SysUIGoogleSysUIComponentImpl.this.falsingManagerProxyProvider);
                this.notificationStackScrollLayoutControllerProvider = DoubleCheck.provider(NotificationStackScrollLayoutController_Factory.create(SysUIGoogleSysUIComponentImpl.this.provideAllowNotificationLongPressProvider, SysUIGoogleSysUIComponentImpl.this.provideNotificationGutsManagerProvider, SysUIGoogleSysUIComponentImpl.this.provideHeadsUpManagerPhoneProvider, SysUIGoogleSysUIComponentImpl.this.notificationRoundnessManagerProvider, SysUIGoogleSysUIComponentImpl.this.tunerServiceImplProvider, SysUIGoogleSysUIComponentImpl.this.dynamicPrivacyControllerProvider, SysUIGoogleSysUIComponentImpl.this.provideConfigurationControllerProvider, SysUIGoogleSysUIComponentImpl.this.statusBarStateControllerImplProvider, SysUIGoogleSysUIComponentImpl.this.keyguardMediaControllerProvider, SysUIGoogleSysUIComponentImpl.this.keyguardBypassControllerProvider, SysUIGoogleSysUIComponentImpl.this.zenModeControllerImplProvider, SysUIGoogleSysUIComponentImpl.this.sysuiColorExtractorProvider, SysUIGoogleSysUIComponentImpl.this.notificationLockscreenUserManagerGoogleProvider, SysUIGoogleSysUIComponentImpl.this.provideMetricsLoggerProvider, SysUIGoogleSysUIComponentImpl.this.falsingCollectorImplProvider, SysUIGoogleSysUIComponentImpl.this.falsingManagerProxyProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideResourcesProvider, this.builderProvider2, SysUIGoogleSysUIComponentImpl.this.provideStatusBarProvider, SysUIGoogleSysUIComponentImpl.this.liveWallpaperScrimControllerProvider, SysUIGoogleSysUIComponentImpl.this.notificationGroupManagerLegacyProvider, SysUIGoogleSysUIComponentImpl.this.provideGroupExpansionManagerProvider, SysUIGoogleSysUIComponentImpl.this.providesSilentHeaderControllerProvider, SysUIGoogleSysUIComponentImpl.this.featureFlagsProvider, SysUIGoogleSysUIComponentImpl.this.notifPipelineProvider, SysUIGoogleSysUIComponentImpl.this.notifCollectionProvider, SysUIGoogleSysUIComponentImpl.this.provideNotificationEntryManagerProvider, SysUIGoogleSysUIComponentImpl.this.lockscreenShadeTransitionControllerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideIStatusBarServiceProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider, SysUIGoogleSysUIComponentImpl.this.foregroundServiceDismissalFeatureControllerProvider, SysUIGoogleSysUIComponentImpl.this.foregroundServiceSectionControllerProvider, SysUIGoogleSysUIComponentImpl.this.providerLayoutInflaterProvider, SysUIGoogleSysUIComponentImpl.this.provideNotificationRemoteInputManagerProvider, SysUIGoogleSysUIComponentImpl.this.provideVisualStabilityManagerProvider, SysUIGoogleSysUIComponentImpl.this.shadeControllerImplProvider));
                Provider<LockIconView> provider = DoubleCheck.provider(StatusBarViewModule_GetLockIconViewFactory.create(this.statusBarWindowViewProvider));
                this.getLockIconViewProvider = provider;
                this.lockIconViewControllerProvider = DoubleCheck.provider(LockIconViewController_Factory.create(provider, SysUIGoogleSysUIComponentImpl.this.statusBarStateControllerImplProvider, SysUIGoogleSysUIComponentImpl.this.keyguardUpdateMonitorProvider, SysUIGoogleSysUIComponentImpl.this.statusBarKeyguardViewManagerProvider, SysUIGoogleSysUIComponentImpl.this.keyguardStateControllerImplProvider, SysUIGoogleSysUIComponentImpl.this.falsingManagerProxyProvider, SysUIGoogleSysUIComponentImpl.this.authControllerProvider, SysUIGoogleSysUIComponentImpl.this.dumpManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideAccessibilityManagerProvider, SysUIGoogleSysUIComponentImpl.this.provideConfigurationControllerProvider, SysUIGoogleSysUIComponentImpl.this.provideMainDelayableExecutorProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideVibratorProvider));
                Provider<TapAgainView> provider2 = DoubleCheck.provider(StatusBarViewModule_GetTapAgainViewFactory.create(this.getNotificationPanelViewProvider));
                this.getTapAgainViewProvider = provider2;
                this.tapAgainViewControllerProvider = DoubleCheck.provider(TapAgainViewController_Factory.create(provider2, SysUIGoogleSysUIComponentImpl.this.provideMainDelayableExecutorProvider, SysUIGoogleSysUIComponentImpl.this.provideConfigurationControllerProvider, FalsingModule_ProvidesDoubleTapTimeoutMsFactory.create()));
                this.notificationPanelViewControllerProvider = DoubleCheck.provider(NotificationPanelViewController_Factory.create(this.getNotificationPanelViewProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideResourcesProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, SysUIGoogleSysUIComponentImpl.this.providerLayoutInflaterProvider, SysUIGoogleSysUIComponentImpl.this.notificationWakeUpCoordinatorProvider, SysUIGoogleSysUIComponentImpl.this.pulseExpansionHandlerProvider, SysUIGoogleSysUIComponentImpl.this.dynamicPrivacyControllerProvider, SysUIGoogleSysUIComponentImpl.this.keyguardBypassControllerProvider, SysUIGoogleSysUIComponentImpl.this.falsingManagerProxyProvider, SysUIGoogleSysUIComponentImpl.this.falsingCollectorImplProvider, SysUIGoogleSysUIComponentImpl.this.notificationLockscreenUserManagerGoogleProvider, SysUIGoogleSysUIComponentImpl.this.provideNotificationEntryManagerProvider, SysUIGoogleSysUIComponentImpl.this.keyguardStateControllerImplProvider, SysUIGoogleSysUIComponentImpl.this.statusBarStateControllerImplProvider, SysUIGoogleSysUIComponentImpl.this.dozeLogProvider, SysUIGoogleSysUIComponentImpl.this.dozeParametersProvider, SysUIGoogleSysUIComponentImpl.this.provideCommandQueueProvider, SysUIGoogleSysUIComponentImpl.this.vibratorHelperProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideLatencyTrackerProvider, DaggerSysUIGoogleGlobalRootComponent.this.providePowerManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideAccessibilityManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideDisplayIdProvider, SysUIGoogleSysUIComponentImpl.this.keyguardUpdateMonitorProvider, SysUIGoogleSysUIComponentImpl.this.provideMetricsLoggerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideActivityManagerProvider, SysUIGoogleSysUIComponentImpl.this.provideConfigurationControllerProvider, this.builderProvider, SysUIGoogleSysUIComponentImpl.this.statusBarTouchableRegionManagerProvider, SysUIGoogleSysUIComponentImpl.this.conversationNotificationManagerProvider, SysUIGoogleSysUIComponentImpl.this.mediaHierarchyManagerProvider, SysUIGoogleSysUIComponentImpl.this.biometricUnlockControllerProvider, SysUIGoogleSysUIComponentImpl.this.statusBarKeyguardViewManagerProvider, this.notificationStackScrollLayoutControllerProvider, SysUIGoogleSysUIComponentImpl.this.keyguardStatusViewComponentFactoryProvider, SysUIGoogleSysUIComponentImpl.this.keyguardQsUserSwitchComponentFactoryProvider, SysUIGoogleSysUIComponentImpl.this.keyguardUserSwitcherComponentFactoryProvider, SysUIGoogleSysUIComponentImpl.this.keyguardStatusBarViewComponentFactoryProvider, SysUIGoogleSysUIComponentImpl.this.lockscreenShadeTransitionControllerProvider, SysUIGoogleSysUIComponentImpl.this.qSDetailDisplayerProvider, SysUIGoogleSysUIComponentImpl.this.notificationGroupManagerLegacyProvider, SysUIGoogleSysUIComponentImpl.this.notificationIconAreaControllerProvider, SysUIGoogleSysUIComponentImpl.this.authControllerProvider, SysUIGoogleSysUIComponentImpl.this.liveWallpaperScrimControllerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUserManagerProvider, SysUIGoogleSysUIComponentImpl.this.mediaDataManagerProvider, SysUIGoogleSysUIComponentImpl.this.notificationShadeDepthControllerProvider, SysUIGoogleSysUIComponentImpl.this.ambientStateProvider, this.lockIconViewControllerProvider, SysUIGoogleSysUIComponentImpl.this.featureFlagsProvider, SysUIGoogleSysUIComponentImpl.this.keyguardMediaControllerProvider, SysUIGoogleSysUIComponentImpl.this.privacyDotViewControllerProvider, this.tapAgainViewControllerProvider, SysUIGoogleSysUIComponentImpl.this.navigationModeControllerProvider, SysUIGoogleSysUIComponentImpl.this.fragmentServiceProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideContentResolverProvider, SysUIGoogleSysUIComponentImpl.this.quickAccessWalletControllerProvider, SysUIGoogleSysUIComponentImpl.this.recordingControllerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, SysUIGoogleSysUIComponentImpl.this.secureSettingsImplProvider, SysUIGoogleSysUIComponentImpl.this.unlockedScreenOffAnimationControllerProvider, SysUIGoogleSysUIComponentImpl.this.provideNotificationRemoteInputManagerProvider, SysUIGoogleSysUIComponentImpl.this.controlsComponentProvider));
                this.getAuthRippleViewProvider = DoubleCheck.provider(StatusBarViewModule_GetAuthRippleViewFactory.create(this.statusBarWindowViewProvider));
                this.authRippleControllerProvider = DoubleCheck.provider(AuthRippleController_Factory.create(SysUIGoogleSysUIComponentImpl.this.provideStatusBarProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, SysUIGoogleSysUIComponentImpl.this.authControllerProvider, SysUIGoogleSysUIComponentImpl.this.provideConfigurationControllerProvider, SysUIGoogleSysUIComponentImpl.this.keyguardUpdateMonitorProvider, SysUIGoogleSysUIComponentImpl.this.commandRegistryProvider, SysUIGoogleSysUIComponentImpl.this.notificationShadeWindowControllerImplProvider, SysUIGoogleSysUIComponentImpl.this.keyguardBypassControllerProvider, SysUIGoogleSysUIComponentImpl.this.biometricUnlockControllerProvider, this.getAuthRippleViewProvider));
            }

            @Override // com.android.systemui.statusbar.phone.dagger.StatusBarComponent
            public NotificationShadeWindowViewController getNotificationShadeWindowViewController() {
                return new NotificationShadeWindowViewController((InjectionInflationController) SysUIGoogleSysUIComponentImpl.this.injectionInflationControllerProvider.get(), (NotificationWakeUpCoordinator) SysUIGoogleSysUIComponentImpl.this.notificationWakeUpCoordinatorProvider.get(), (PulseExpansionHandler) SysUIGoogleSysUIComponentImpl.this.pulseExpansionHandlerProvider.get(), (DynamicPrivacyController) SysUIGoogleSysUIComponentImpl.this.dynamicPrivacyControllerProvider.get(), (KeyguardBypassController) SysUIGoogleSysUIComponentImpl.this.keyguardBypassControllerProvider.get(), (LockscreenShadeTransitionController) SysUIGoogleSysUIComponentImpl.this.lockscreenShadeTransitionControllerProvider.get(), (FalsingCollector) SysUIGoogleSysUIComponentImpl.this.falsingCollectorImplProvider.get(), (PluginManager) SysUIGoogleSysUIComponentImpl.this.providePluginManagerProvider.get(), (TunerService) SysUIGoogleSysUIComponentImpl.this.tunerServiceImplProvider.get(), (NotificationLockscreenUserManager) SysUIGoogleSysUIComponentImpl.this.notificationLockscreenUserManagerGoogleProvider.get(), (NotificationEntryManager) SysUIGoogleSysUIComponentImpl.this.provideNotificationEntryManagerProvider.get(), (KeyguardStateController) SysUIGoogleSysUIComponentImpl.this.keyguardStateControllerImplProvider.get(), (SysuiStatusBarStateController) SysUIGoogleSysUIComponentImpl.this.statusBarStateControllerImplProvider.get(), (DozeLog) SysUIGoogleSysUIComponentImpl.this.dozeLogProvider.get(), (DozeParameters) SysUIGoogleSysUIComponentImpl.this.dozeParametersProvider.get(), (CommandQueue) SysUIGoogleSysUIComponentImpl.this.provideCommandQueueProvider.get(), (ShadeController) SysUIGoogleSysUIComponentImpl.this.shadeControllerImplProvider.get(), (DockManager) SysUIGoogleSysUIComponentImpl.this.provideDockManagerProvider.get(), (NotificationShadeDepthController) SysUIGoogleSysUIComponentImpl.this.notificationShadeDepthControllerProvider.get(), this.statusBarWindowView, this.notificationPanelViewControllerProvider.get(), (SuperStatusBarViewFactory) SysUIGoogleSysUIComponentImpl.this.superStatusBarViewFactoryProvider.get(), this.notificationStackScrollLayoutControllerProvider.get(), (StatusBarKeyguardViewManager) SysUIGoogleSysUIComponentImpl.this.statusBarKeyguardViewManagerProvider.get());
            }

            @Override // com.android.systemui.statusbar.phone.dagger.StatusBarComponent
            public StatusBarWindowController getStatusBarWindowController() {
                return (StatusBarWindowController) SysUIGoogleSysUIComponentImpl.this.statusBarWindowControllerProvider.get();
            }

            @Override // com.android.systemui.statusbar.phone.dagger.StatusBarComponent
            public NotificationPanelViewController getNotificationPanelViewController() {
                return this.notificationPanelViewControllerProvider.get();
            }

            @Override // com.android.systemui.statusbar.phone.dagger.StatusBarComponent
            public LockIconViewController getLockIconViewController() {
                return this.lockIconViewControllerProvider.get();
            }

            @Override // com.android.systemui.statusbar.phone.dagger.StatusBarComponent
            public AuthRippleController getAuthRippleController() {
                return this.authRippleControllerProvider.get();
            }
        }

        /* access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public final class DozeComponentFactory implements DozeComponent.Builder {
            private DozeComponentFactory() {
            }

            @Override // com.android.systemui.doze.dagger.DozeComponent.Builder
            public DozeComponent build(DozeMachine.Service service) {
                Preconditions.checkNotNull(service);
                return new DozeComponentImpl(service);
            }
        }

        /* loaded from: classes2.dex */
        private final class DozeComponentImpl implements DozeComponent {
            private Provider<DozeMachine.Service> arg0Provider;
            private Provider<DozeAuthRemover> dozeAuthRemoverProvider;
            private Provider<DozeDockHandler> dozeDockHandlerProvider;
            private Provider<DozeFalsingManagerAdapter> dozeFalsingManagerAdapterProvider;
            private Provider<DozeMachine> dozeMachineProvider;
            private Provider<DozePauser> dozePauserProvider;
            private Provider<DozeScreenBrightness> dozeScreenBrightnessProvider;
            private Provider<DozeScreenState> dozeScreenStateProvider;
            private Provider<DozeTriggers> dozeTriggersProvider;
            private Provider<DozeUi> dozeUiProvider;
            private Provider<DozeWallpaperState> dozeWallpaperStateProvider;
            private Provider<Optional<Sensor>> providesBrightnessSensorProvider;
            private Provider<DozeMachine.Part[]> providesDozeMachinePartesProvider;
            private Provider<WakeLock> providesDozeWakeLockProvider;
            private Provider<DozeMachine.Service> providesWrappedServiceProvider;
            private Provider<ProximitySensor.ProximityCheck> proximityCheckProvider;

            private DozeComponentImpl(DozeMachine.Service service) {
                initialize(service);
            }

            private void initialize(DozeMachine.Service service) {
                Factory create = InstanceFactory.create(service);
                this.arg0Provider = create;
                this.providesWrappedServiceProvider = DoubleCheck.provider(DozeModule_ProvidesWrappedServiceFactory.create(create, SysUIGoogleSysUIComponentImpl.this.dozeServiceHostProvider, SysUIGoogleSysUIComponentImpl.this.dozeParametersProvider));
                this.providesDozeWakeLockProvider = DoubleCheck.provider(DozeModule_ProvidesDozeWakeLockFactory.create(SysUIGoogleSysUIComponentImpl.this.builderProvider2, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider));
                this.dozePauserProvider = DoubleCheck.provider(DozePauser_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideAlarmManagerProvider, SysUIGoogleSysUIComponentImpl.this.provideAlwaysOnDisplayPolicyProvider));
                this.dozeFalsingManagerAdapterProvider = DoubleCheck.provider(DozeFalsingManagerAdapter_Factory.create(SysUIGoogleSysUIComponentImpl.this.falsingCollectorImplProvider));
                this.proximityCheckProvider = ProximitySensor_ProximityCheck_Factory.create(SysUIGoogleSysUIComponentImpl.this.proximitySensorProvider, SysUIGoogleSysUIComponentImpl.this.provideMainDelayableExecutorProvider);
                this.dozeTriggersProvider = DoubleCheck.provider(DozeTriggers_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, SysUIGoogleSysUIComponentImpl.this.dozeServiceHostProvider, SysUIGoogleSysUIComponentImpl.this.provideAmbientDisplayConfigurationProvider, SysUIGoogleSysUIComponentImpl.this.dozeParametersProvider, SysUIGoogleSysUIComponentImpl.this.asyncSensorManagerProvider, this.providesDozeWakeLockProvider, SysUIGoogleSysUIComponentImpl.this.provideDockManagerProvider, SysUIGoogleSysUIComponentImpl.this.proximitySensorProvider, this.proximityCheckProvider, SysUIGoogleSysUIComponentImpl.this.dozeLogProvider, SysUIGoogleSysUIComponentImpl.this.providesBroadcastDispatcherProvider, SysUIGoogleSysUIComponentImpl.this.secureSettingsImplProvider, SysUIGoogleSysUIComponentImpl.this.authControllerProvider, SysUIGoogleSysUIComponentImpl.this.provideMainDelayableExecutorProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider));
                this.dozeUiProvider = DoubleCheck.provider(DozeUi_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideAlarmManagerProvider, this.providesDozeWakeLockProvider, SysUIGoogleSysUIComponentImpl.this.dozeServiceHostProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, SysUIGoogleSysUIComponentImpl.this.dozeParametersProvider, SysUIGoogleSysUIComponentImpl.this.keyguardUpdateMonitorProvider, SysUIGoogleSysUIComponentImpl.this.dozeLogProvider, SysUIGoogleSysUIComponentImpl.this.tunerServiceImplProvider, SysUIGoogleSysUIComponentImpl.this.statusBarStateControllerImplProvider, SysUIGoogleSysUIComponentImpl.this.provideConfigurationControllerProvider));
                this.dozeScreenStateProvider = DoubleCheck.provider(DozeScreenState_Factory.create(this.providesWrappedServiceProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, SysUIGoogleSysUIComponentImpl.this.dozeServiceHostProvider, SysUIGoogleSysUIComponentImpl.this.dozeParametersProvider, this.providesDozeWakeLockProvider));
                this.providesBrightnessSensorProvider = DozeModule_ProvidesBrightnessSensorFactory.create(SysUIGoogleSysUIComponentImpl.this.asyncSensorManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider);
                this.dozeScreenBrightnessProvider = DoubleCheck.provider(DozeScreenBrightness_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.providesWrappedServiceProvider, SysUIGoogleSysUIComponentImpl.this.asyncSensorManagerProvider, this.providesBrightnessSensorProvider, SysUIGoogleSysUIComponentImpl.this.dozeServiceHostProvider, SysUIGoogleSysUIComponentImpl.this.provideHandlerProvider, SysUIGoogleSysUIComponentImpl.this.provideAlwaysOnDisplayPolicyProvider, SysUIGoogleSysUIComponentImpl.this.wakefulnessLifecycleProvider, SysUIGoogleSysUIComponentImpl.this.dozeParametersProvider));
                this.dozeWallpaperStateProvider = DoubleCheck.provider(DozeWallpaperState_Factory.create(FrameworkServicesModule_ProvideIWallPaperManagerFactory.create(), SysUIGoogleSysUIComponentImpl.this.biometricUnlockControllerProvider, SysUIGoogleSysUIComponentImpl.this.dozeParametersProvider));
                this.dozeDockHandlerProvider = DoubleCheck.provider(DozeDockHandler_Factory.create(SysUIGoogleSysUIComponentImpl.this.provideAmbientDisplayConfigurationProvider, SysUIGoogleSysUIComponentImpl.this.provideDockManagerProvider));
                Provider<DozeAuthRemover> provider = DoubleCheck.provider(DozeAuthRemover_Factory.create(SysUIGoogleSysUIComponentImpl.this.keyguardUpdateMonitorProvider));
                this.dozeAuthRemoverProvider = provider;
                this.providesDozeMachinePartesProvider = DozeModule_ProvidesDozeMachinePartesFactory.create(this.dozePauserProvider, this.dozeFalsingManagerAdapterProvider, this.dozeTriggersProvider, this.dozeUiProvider, this.dozeScreenStateProvider, this.dozeScreenBrightnessProvider, this.dozeWallpaperStateProvider, this.dozeDockHandlerProvider, provider);
                this.dozeMachineProvider = DoubleCheck.provider(DozeMachine_Factory.create(this.providesWrappedServiceProvider, SysUIGoogleSysUIComponentImpl.this.provideAmbientDisplayConfigurationProvider, this.providesDozeWakeLockProvider, SysUIGoogleSysUIComponentImpl.this.wakefulnessLifecycleProvider, SysUIGoogleSysUIComponentImpl.this.provideBatteryControllerProvider, SysUIGoogleSysUIComponentImpl.this.dozeLogProvider, SysUIGoogleSysUIComponentImpl.this.provideDockManagerProvider, SysUIGoogleSysUIComponentImpl.this.dozeServiceHostProvider, this.providesDozeMachinePartesProvider));
            }

            @Override // com.android.systemui.doze.dagger.DozeComponent
            public DozeMachine getDozeMachine() {
                return this.dozeMachineProvider.get();
            }
        }

        /* access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public final class FragmentCreatorFactory implements FragmentService.FragmentCreator.Factory {
            private FragmentCreatorFactory() {
            }

            @Override // com.android.systemui.fragments.FragmentService.FragmentCreator.Factory
            public FragmentService.FragmentCreator build() {
                return new FragmentCreatorImpl();
            }
        }

        /* loaded from: classes2.dex */
        private final class FragmentCreatorImpl implements FragmentService.FragmentCreator {
            private FragmentCreatorImpl() {
            }

            @Override // com.android.systemui.fragments.FragmentService.FragmentCreator
            public QSFragment createQSFragment() {
                return new QSFragment((RemoteInputQuickSettingsDisabler) SysUIGoogleSysUIComponentImpl.this.remoteInputQuickSettingsDisablerProvider.get(), (InjectionInflationController) SysUIGoogleSysUIComponentImpl.this.injectionInflationControllerProvider.get(), (QSTileHost) SysUIGoogleSysUIComponentImpl.this.qSTileHostProvider.get(), (StatusBarStateController) SysUIGoogleSysUIComponentImpl.this.statusBarStateControllerImplProvider.get(), (CommandQueue) SysUIGoogleSysUIComponentImpl.this.provideCommandQueueProvider.get(), (QSDetailDisplayer) SysUIGoogleSysUIComponentImpl.this.qSDetailDisplayerProvider.get(), (MediaHost) SysUIGoogleSysUIComponentImpl.this.providesQSMediaHostProvider.get(), (MediaHost) SysUIGoogleSysUIComponentImpl.this.providesQuickQSMediaHostProvider.get(), (KeyguardBypassController) SysUIGoogleSysUIComponentImpl.this.keyguardBypassControllerProvider.get(), new QSFragmentComponentFactory(), (FeatureFlags) SysUIGoogleSysUIComponentImpl.this.featureFlagsProvider.get(), (FalsingManager) SysUIGoogleSysUIComponentImpl.this.falsingManagerProxyProvider.get(), (DumpManager) SysUIGoogleSysUIComponentImpl.this.dumpManagerProvider.get());
            }

            @Override // com.android.systemui.fragments.FragmentService.FragmentCreator
            public CollapsedStatusBarFragment createCollapsedStatusBarFragment() {
                return new CollapsedStatusBarFragment((OngoingCallController) SysUIGoogleSysUIComponentImpl.this.provideOngoingCallControllerProvider.get(), (SystemStatusAnimationScheduler) SysUIGoogleSysUIComponentImpl.this.systemStatusAnimationSchedulerProvider.get(), (StatusBarLocationPublisher) SysUIGoogleSysUIComponentImpl.this.statusBarLocationPublisherProvider.get(), (NotificationIconAreaController) SysUIGoogleSysUIComponentImpl.this.notificationIconAreaControllerProvider.get(), (FeatureFlags) SysUIGoogleSysUIComponentImpl.this.featureFlagsProvider.get(), (StatusBarIconController) SysUIGoogleSysUIComponentImpl.this.statusBarIconControllerImplProvider.get(), (KeyguardStateController) SysUIGoogleSysUIComponentImpl.this.keyguardStateControllerImplProvider.get(), (NetworkController) SysUIGoogleSysUIComponentImpl.this.networkControllerImplProvider.get(), (StatusBarStateController) SysUIGoogleSysUIComponentImpl.this.statusBarStateControllerImplProvider.get(), (StatusBar) SysUIGoogleSysUIComponentImpl.this.provideStatusBarProvider.get(), (CommandQueue) SysUIGoogleSysUIComponentImpl.this.provideCommandQueueProvider.get());
            }
        }

        /* access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public final class KeyguardQsUserSwitchComponentFactory implements KeyguardQsUserSwitchComponent.Factory {
            private KeyguardQsUserSwitchComponentFactory() {
            }

            @Override // com.android.keyguard.dagger.KeyguardQsUserSwitchComponent.Factory
            public KeyguardQsUserSwitchComponent build(UserAvatarView userAvatarView) {
                Preconditions.checkNotNull(userAvatarView);
                return new KeyguardQsUserSwitchComponentImpl(userAvatarView);
            }
        }

        /* loaded from: classes2.dex */
        private final class KeyguardQsUserSwitchComponentImpl implements KeyguardQsUserSwitchComponent {
            private Provider<UserAvatarView> arg0Provider;
            private Provider<KeyguardQsUserSwitchController> keyguardQsUserSwitchControllerProvider;

            private KeyguardQsUserSwitchComponentImpl(UserAvatarView userAvatarView) {
                initialize(userAvatarView);
            }

            private void initialize(UserAvatarView userAvatarView) {
                Factory create = InstanceFactory.create(userAvatarView);
                this.arg0Provider = create;
                this.keyguardQsUserSwitchControllerProvider = DoubleCheck.provider(KeyguardQsUserSwitchController_Factory.create(create, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideResourcesProvider, SysUIGoogleSysUIComponentImpl.this.screenLifecycleProvider, SysUIGoogleSysUIComponentImpl.this.userSwitcherControllerProvider, SysUIGoogleSysUIComponentImpl.this.keyguardStateControllerImplProvider, SysUIGoogleSysUIComponentImpl.this.falsingManagerProxyProvider, SysUIGoogleSysUIComponentImpl.this.provideConfigurationControllerProvider, SysUIGoogleSysUIComponentImpl.this.statusBarStateControllerImplProvider, SysUIGoogleSysUIComponentImpl.this.dozeParametersProvider, SysUIGoogleSysUIComponentImpl.this.adapterProvider, SysUIGoogleSysUIComponentImpl.this.unlockedScreenOffAnimationControllerProvider));
            }

            @Override // com.android.keyguard.dagger.KeyguardQsUserSwitchComponent
            public KeyguardQsUserSwitchController getKeyguardQsUserSwitchController() {
                return this.keyguardQsUserSwitchControllerProvider.get();
            }
        }

        /* access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public final class KeyguardUserSwitcherComponentFactory implements KeyguardUserSwitcherComponent.Factory {
            private KeyguardUserSwitcherComponentFactory() {
            }

            @Override // com.android.keyguard.dagger.KeyguardUserSwitcherComponent.Factory
            public KeyguardUserSwitcherComponent build(KeyguardUserSwitcherView keyguardUserSwitcherView) {
                Preconditions.checkNotNull(keyguardUserSwitcherView);
                return new KeyguardUserSwitcherComponentImpl(keyguardUserSwitcherView);
            }
        }

        /* loaded from: classes2.dex */
        private final class KeyguardUserSwitcherComponentImpl implements KeyguardUserSwitcherComponent {
            private Provider<KeyguardUserSwitcherView> arg0Provider;
            private Provider<KeyguardUserSwitcherController> keyguardUserSwitcherControllerProvider;

            private KeyguardUserSwitcherComponentImpl(KeyguardUserSwitcherView keyguardUserSwitcherView) {
                initialize(keyguardUserSwitcherView);
            }

            private void initialize(KeyguardUserSwitcherView keyguardUserSwitcherView) {
                Factory create = InstanceFactory.create(keyguardUserSwitcherView);
                this.arg0Provider = create;
                this.keyguardUserSwitcherControllerProvider = DoubleCheck.provider(KeyguardUserSwitcherController_Factory.create(create, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideResourcesProvider, SysUIGoogleSysUIComponentImpl.this.providerLayoutInflaterProvider, SysUIGoogleSysUIComponentImpl.this.screenLifecycleProvider, SysUIGoogleSysUIComponentImpl.this.userSwitcherControllerProvider, SysUIGoogleSysUIComponentImpl.this.keyguardStateControllerImplProvider, SysUIGoogleSysUIComponentImpl.this.statusBarStateControllerImplProvider, SysUIGoogleSysUIComponentImpl.this.keyguardUpdateMonitorProvider, SysUIGoogleSysUIComponentImpl.this.dozeParametersProvider, SysUIGoogleSysUIComponentImpl.this.unlockedScreenOffAnimationControllerProvider));
            }

            @Override // com.android.keyguard.dagger.KeyguardUserSwitcherComponent
            public KeyguardUserSwitcherController getKeyguardUserSwitcherController() {
                return this.keyguardUserSwitcherControllerProvider.get();
            }
        }

        /* access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public final class KeyguardStatusBarViewComponentFactory implements KeyguardStatusBarViewComponent.Factory {
            private KeyguardStatusBarViewComponentFactory() {
            }

            @Override // com.android.keyguard.dagger.KeyguardStatusBarViewComponent.Factory
            public KeyguardStatusBarViewComponent build(KeyguardStatusBarView keyguardStatusBarView) {
                Preconditions.checkNotNull(keyguardStatusBarView);
                return new KeyguardStatusBarViewComponentImpl(keyguardStatusBarView);
            }
        }

        /* loaded from: classes2.dex */
        private final class KeyguardStatusBarViewComponentImpl implements KeyguardStatusBarViewComponent {
            private final KeyguardStatusBarView arg0;
            private Provider<KeyguardStatusBarView> arg0Provider;
            private Provider<CarrierText> getCarrierTextProvider;

            private KeyguardStatusBarViewComponentImpl(KeyguardStatusBarView keyguardStatusBarView) {
                this.arg0 = keyguardStatusBarView;
                initialize(keyguardStatusBarView);
            }

            private CarrierTextManager.Builder carrierTextManagerBuilder() {
                return new CarrierTextManager.Builder(DaggerSysUIGoogleGlobalRootComponent.this.context, DaggerSysUIGoogleGlobalRootComponent.this.mainResources(), (WifiManager) DaggerSysUIGoogleGlobalRootComponent.this.provideWifiManagerProvider.get(), (TelephonyManager) DaggerSysUIGoogleGlobalRootComponent.this.provideTelephonyManagerProvider.get(), (TelephonyListenerManager) SysUIGoogleSysUIComponentImpl.this.telephonyListenerManagerProvider.get(), (WakefulnessLifecycle) SysUIGoogleSysUIComponentImpl.this.wakefulnessLifecycleProvider.get(), DaggerSysUIGoogleGlobalRootComponent.this.mainExecutor(), (Executor) SysUIGoogleSysUIComponentImpl.this.provideBackgroundExecutorProvider.get(), (KeyguardUpdateMonitor) SysUIGoogleSysUIComponentImpl.this.keyguardUpdateMonitorProvider.get());
            }

            private CarrierTextController carrierTextController() {
                return new CarrierTextController(this.getCarrierTextProvider.get(), carrierTextManagerBuilder(), (KeyguardUpdateMonitor) SysUIGoogleSysUIComponentImpl.this.keyguardUpdateMonitorProvider.get());
            }

            private void initialize(KeyguardStatusBarView keyguardStatusBarView) {
                Factory create = InstanceFactory.create(keyguardStatusBarView);
                this.arg0Provider = create;
                this.getCarrierTextProvider = DoubleCheck.provider(KeyguardStatusBarViewModule_GetCarrierTextFactory.create(create));
            }

            @Override // com.android.keyguard.dagger.KeyguardStatusBarViewComponent
            public KeyguardStatusBarViewController getKeyguardStatusBarViewController() {
                return new KeyguardStatusBarViewController(this.arg0, carrierTextController());
            }
        }

        /* loaded from: classes2.dex */
        private final class QSFragmentComponentFactory implements QSFragmentComponent.Factory {
            private QSFragmentComponentFactory() {
            }

            @Override // com.android.systemui.qs.dagger.QSFragmentComponent.Factory
            public QSFragmentComponent create(QSFragment qSFragment) {
                Preconditions.checkNotNull(qSFragment);
                return new QSFragmentComponentImpl(qSFragment);
            }
        }

        /* loaded from: classes2.dex */
        private final class QSFragmentComponentImpl implements QSFragmentComponent {
            private Provider<QSFragment> arg0Provider;
            private Provider<CarrierTextManager.Builder> builderProvider;
            private Provider<QSCarrierGroupController.Builder> builderProvider2;
            private Provider factoryProvider;
            private Provider<BrightnessController.Factory> factoryProvider2;
            private Provider<MultiUserSwitchController> multiUserSwitchControllerProvider;
            private Provider<QSPanel> provideQSPanelProvider;
            private Provider<View> provideRootViewProvider;
            private Provider<Context> provideThemedContextProvider;
            private Provider<LayoutInflater> provideThemedLayoutInflaterProvider;
            private Provider<MultiUserSwitch> providesMultiUserSWitchProvider;
            private Provider<QSContainerImpl> providesQSContainerImplProvider;
            private Provider<QSCustomizer> providesQSCutomizerProvider;
            private Provider<QSFooter> providesQSFooterProvider;
            private Provider<QSFooterView> providesQSFooterViewProvider;
            private Provider<View> providesQSSecurityFooterViewProvider;
            private Provider<Boolean> providesQSUsingMediaPlayerProvider;
            private Provider<QuickQSPanel> providesQuickQSPanelProvider;
            private Provider<QuickStatusBarHeader> providesQuickStatusBarHeaderProvider;
            private Provider<QSAnimator> qSAnimatorProvider;
            private Provider<QSContainerImplController> qSContainerImplControllerProvider;
            private Provider<QSCustomizerController> qSCustomizerControllerProvider;
            private Provider<QSFooterViewController> qSFooterViewControllerProvider;
            private Provider<QSPanelController> qSPanelControllerProvider;
            private Provider qSSecurityFooterProvider;
            private Provider<QuickQSPanelController> quickQSPanelControllerProvider;
            private Provider quickStatusBarHeaderControllerProvider;
            private Provider<TileAdapter> tileAdapterProvider;
            private Provider<TileQueryHelper> tileQueryHelperProvider;

            private QSFragmentComponentImpl(QSFragment qSFragment) {
                initialize(qSFragment);
            }

            private void initialize(QSFragment qSFragment) {
                Factory create = InstanceFactory.create(qSFragment);
                this.arg0Provider = create;
                QSFragmentModule_ProvideRootViewFactory create2 = QSFragmentModule_ProvideRootViewFactory.create(create);
                this.provideRootViewProvider = create2;
                this.provideQSPanelProvider = QSFragmentModule_ProvideQSPanelFactory.create(create2);
                QSFragmentModule_ProvideThemedContextFactory create3 = QSFragmentModule_ProvideThemedContextFactory.create(this.provideRootViewProvider);
                this.provideThemedContextProvider = create3;
                QSFragmentModule_ProvideThemedLayoutInflaterFactory create4 = QSFragmentModule_ProvideThemedLayoutInflaterFactory.create(create3);
                this.provideThemedLayoutInflaterProvider = create4;
                Provider<View> provider = DoubleCheck.provider(QSFragmentModule_ProvidesQSSecurityFooterViewFactory.create(create4, this.provideQSPanelProvider));
                this.providesQSSecurityFooterViewProvider = provider;
                this.qSSecurityFooterProvider = DoubleCheck.provider(QSSecurityFooter_Factory.create(provider, SysUIGoogleSysUIComponentImpl.this.provideUserTrackerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainHandlerProvider, SysUIGoogleSysUIComponentImpl.this.activityStarterDelegateProvider, SysUIGoogleSysUIComponentImpl.this.securityControllerImplProvider, SysUIGoogleSysUIComponentImpl.this.provideBgLooperProvider));
                this.providesQSCutomizerProvider = DoubleCheck.provider(QSFragmentModule_ProvidesQSCutomizerFactory.create(this.provideRootViewProvider));
                this.tileQueryHelperProvider = DoubleCheck.provider(TileQueryHelper_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, SysUIGoogleSysUIComponentImpl.this.provideUserTrackerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, SysUIGoogleSysUIComponentImpl.this.provideBackgroundExecutorProvider));
                this.tileAdapterProvider = DoubleCheck.provider(TileAdapter_Factory.create(this.provideThemedContextProvider, SysUIGoogleSysUIComponentImpl.this.qSTileHostProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider));
                this.qSCustomizerControllerProvider = DoubleCheck.provider(QSCustomizerController_Factory.create(this.providesQSCutomizerProvider, this.tileQueryHelperProvider, SysUIGoogleSysUIComponentImpl.this.qSTileHostProvider, this.tileAdapterProvider, SysUIGoogleSysUIComponentImpl.this.screenLifecycleProvider, SysUIGoogleSysUIComponentImpl.this.keyguardStateControllerImplProvider, SysUIGoogleSysUIComponentImpl.this.lightBarControllerProvider, SysUIGoogleSysUIComponentImpl.this.provideConfigurationControllerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider));
                this.providesQSUsingMediaPlayerProvider = QSFragmentModule_ProvidesQSUsingMediaPlayerFactory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider);
                this.factoryProvider = DoubleCheck.provider(QSTileRevealController_Factory_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, this.qSCustomizerControllerProvider));
                this.factoryProvider2 = BrightnessController_Factory_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, SysUIGoogleSysUIComponentImpl.this.providesBroadcastDispatcherProvider);
                this.qSPanelControllerProvider = DoubleCheck.provider(QSPanelController_Factory.create(this.provideQSPanelProvider, this.qSSecurityFooterProvider, SysUIGoogleSysUIComponentImpl.this.tunerServiceImplProvider, SysUIGoogleSysUIComponentImpl.this.qSTileHostProvider, this.qSCustomizerControllerProvider, this.providesQSUsingMediaPlayerProvider, SysUIGoogleSysUIComponentImpl.this.providesQSMediaHostProvider, this.factoryProvider, SysUIGoogleSysUIComponentImpl.this.dumpManagerProvider, SysUIGoogleSysUIComponentImpl.this.provideMetricsLoggerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider, SysUIGoogleSysUIComponentImpl.this.qSLoggerProvider, this.factoryProvider2, SysUIGoogleSysUIComponentImpl.this.factoryProvider3, SysUIGoogleSysUIComponentImpl.this.falsingManagerProxyProvider, SysUIGoogleSysUIComponentImpl.this.featureFlagsProvider));
                QSFragmentModule_ProvidesQuickStatusBarHeaderFactory create5 = QSFragmentModule_ProvidesQuickStatusBarHeaderFactory.create(this.provideRootViewProvider);
                this.providesQuickStatusBarHeaderProvider = create5;
                QSFragmentModule_ProvidesQuickQSPanelFactory create6 = QSFragmentModule_ProvidesQuickQSPanelFactory.create(create5);
                this.providesQuickQSPanelProvider = create6;
                Provider<QuickQSPanelController> provider2 = DoubleCheck.provider(QuickQSPanelController_Factory.create(create6, SysUIGoogleSysUIComponentImpl.this.qSTileHostProvider, this.qSCustomizerControllerProvider, this.providesQSUsingMediaPlayerProvider, SysUIGoogleSysUIComponentImpl.this.providesQuickQSMediaHostProvider, SysUIGoogleSysUIComponentImpl.this.provideMetricsLoggerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider, SysUIGoogleSysUIComponentImpl.this.qSLoggerProvider, SysUIGoogleSysUIComponentImpl.this.dumpManagerProvider, SysUIGoogleSysUIComponentImpl.this.featureFlagsProvider));
                this.quickQSPanelControllerProvider = provider2;
                this.qSAnimatorProvider = DoubleCheck.provider(QSAnimator_Factory.create(this.arg0Provider, this.providesQuickQSPanelProvider, this.providesQuickStatusBarHeaderProvider, this.qSPanelControllerProvider, provider2, SysUIGoogleSysUIComponentImpl.this.qSTileHostProvider, this.qSSecurityFooterProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, SysUIGoogleSysUIComponentImpl.this.tunerServiceImplProvider, DaggerSysUIGoogleGlobalRootComponent.this.qSExpansionPathInterpolatorProvider));
                this.providesQSContainerImplProvider = QSFragmentModule_ProvidesQSContainerImplFactory.create(this.provideRootViewProvider);
                this.builderProvider = CarrierTextManager_Builder_Factory.create(DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideResourcesProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideWifiManagerProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideTelephonyManagerProvider, SysUIGoogleSysUIComponentImpl.this.telephonyListenerManagerProvider, SysUIGoogleSysUIComponentImpl.this.wakefulnessLifecycleProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideMainExecutorProvider, SysUIGoogleSysUIComponentImpl.this.provideBackgroundExecutorProvider, SysUIGoogleSysUIComponentImpl.this.keyguardUpdateMonitorProvider);
                this.builderProvider2 = QSCarrierGroupController_Builder_Factory.create(SysUIGoogleSysUIComponentImpl.this.activityStarterDelegateProvider, SysUIGoogleSysUIComponentImpl.this.provideBgHandlerProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), SysUIGoogleSysUIComponentImpl.this.networkControllerImplProvider, this.builderProvider, DaggerSysUIGoogleGlobalRootComponent.this.contextProvider, SysUIGoogleSysUIComponentImpl.this.carrierConfigTrackerProvider, SysUIGoogleSysUIComponentImpl.this.featureFlagsProvider, SysUIGoogleSysUIComponentImpl.this.subscriptionManagerSlotIndexResolverProvider);
                Provider provider3 = DoubleCheck.provider(QuickStatusBarHeaderController_Factory.create(this.providesQuickStatusBarHeaderProvider, SysUIGoogleSysUIComponentImpl.this.privacyItemControllerProvider, SysUIGoogleSysUIComponentImpl.this.activityStarterDelegateProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider, SysUIGoogleSysUIComponentImpl.this.statusBarIconControllerImplProvider, SysUIGoogleSysUIComponentImpl.this.provideDemoModeControllerProvider, this.quickQSPanelControllerProvider, this.builderProvider2, SysUIGoogleSysUIComponentImpl.this.privacyLoggerProvider, SysUIGoogleSysUIComponentImpl.this.sysuiColorExtractorProvider, SysUIGoogleSysUIComponentImpl.this.privacyDialogControllerProvider, DaggerSysUIGoogleGlobalRootComponent.this.qSExpansionPathInterpolatorProvider, SysUIGoogleSysUIComponentImpl.this.featureFlagsProvider));
                this.quickStatusBarHeaderControllerProvider = provider3;
                this.qSContainerImplControllerProvider = DoubleCheck.provider(QSContainerImplController_Factory.create(this.providesQSContainerImplProvider, this.qSPanelControllerProvider, provider3, SysUIGoogleSysUIComponentImpl.this.provideConfigurationControllerProvider));
                QSFragmentModule_ProvidesQSFooterViewFactory create7 = QSFragmentModule_ProvidesQSFooterViewFactory.create(this.provideRootViewProvider);
                this.providesQSFooterViewProvider = create7;
                QSFragmentModule_ProvidesMultiUserSWitchFactory create8 = QSFragmentModule_ProvidesMultiUserSWitchFactory.create(create7);
                this.providesMultiUserSWitchProvider = create8;
                this.multiUserSwitchControllerProvider = DoubleCheck.provider(MultiUserSwitchController_Factory.create(create8, DaggerSysUIGoogleGlobalRootComponent.this.provideUserManagerProvider, SysUIGoogleSysUIComponentImpl.this.userSwitcherControllerProvider, SysUIGoogleSysUIComponentImpl.this.qSDetailDisplayerProvider, SysUIGoogleSysUIComponentImpl.this.falsingManagerProxyProvider));
                Provider<QSFooterViewController> provider4 = DoubleCheck.provider(QSFooterViewController_Factory.create(this.providesQSFooterViewProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUserManagerProvider, SysUIGoogleSysUIComponentImpl.this.userInfoControllerImplProvider, SysUIGoogleSysUIComponentImpl.this.activityStarterDelegateProvider, SysUIGoogleSysUIComponentImpl.this.deviceProvisionedControllerImplProvider, SysUIGoogleSysUIComponentImpl.this.provideUserTrackerProvider, this.qSPanelControllerProvider, this.multiUserSwitchControllerProvider, this.quickQSPanelControllerProvider, SysUIGoogleSysUIComponentImpl.this.tunerServiceImplProvider, SysUIGoogleSysUIComponentImpl.this.provideMetricsLoggerProvider, SysUIGoogleSysUIComponentImpl.this.falsingManagerProxyProvider, SysUIGoogleSysUIComponentImpl.this.isPMLiteEnabledProvider, SysUIGoogleSysUIComponentImpl.this.globalActionsDialogLiteProvider, DaggerSysUIGoogleGlobalRootComponent.this.provideUiEventLoggerProvider));
                this.qSFooterViewControllerProvider = provider4;
                this.providesQSFooterProvider = DoubleCheck.provider(QSFragmentModule_ProvidesQSFooterFactory.create(provider4));
            }

            @Override // com.android.systemui.qs.dagger.QSFragmentComponent
            public QSPanelController getQSPanelController() {
                return this.qSPanelControllerProvider.get();
            }

            @Override // com.android.systemui.qs.dagger.QSFragmentComponent
            public QuickQSPanelController getQuickQSPanelController() {
                return this.quickQSPanelControllerProvider.get();
            }

            @Override // com.android.systemui.qs.dagger.QSFragmentComponent
            public QSAnimator getQSAnimator() {
                return this.qSAnimatorProvider.get();
            }

            @Override // com.android.systemui.qs.dagger.QSFragmentComponent
            public QSContainerImplController getQSContainerImplController() {
                return this.qSContainerImplControllerProvider.get();
            }

            @Override // com.android.systemui.qs.dagger.QSFragmentComponent
            public QSFooter getQSFooter() {
                return this.providesQSFooterProvider.get();
            }

            @Override // com.android.systemui.qs.dagger.QSFragmentComponent
            public QSCustomizerController getQSCustomizerController() {
                return this.qSCustomizerControllerProvider.get();
            }
        }
    }
}
