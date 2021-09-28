package com.android.systemui.tv;

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
import android.media.AudioManager;
import android.media.IAudioService;
import android.media.MediaRouter2Manager;
import android.media.session.MediaSessionManager;
import android.net.ConnectivityManager;
import android.net.NetworkScoreManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
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
import com.android.systemui.assist.AssistLogger;
import com.android.systemui.assist.AssistLogger_Factory;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.assist.AssistManager_Factory;
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
import com.android.systemui.dagger.DependencyProvider_ProvideTaskStackChangeListenersFactory;
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
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.demomode.dagger.DemoModeModule_ProvideDemoModeControllerFactory;
import com.android.systemui.dock.DockManager;
import com.android.systemui.dock.DockManagerImpl;
import com.android.systemui.dock.DockManagerImpl_Factory;
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
import com.android.systemui.media.systemsounds.HomeSoundEffectController;
import com.android.systemui.media.systemsounds.HomeSoundEffectController_Factory;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationBarOverlayController;
import com.android.systemui.navigationbar.NavigationBarOverlayController_Factory;
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
import com.android.systemui.power.EnhancedEstimatesImpl;
import com.android.systemui.power.EnhancedEstimatesImpl_Factory;
import com.android.systemui.power.PowerNotificationWarnings;
import com.android.systemui.power.PowerNotificationWarnings_Factory;
import com.android.systemui.power.PowerUI;
import com.android.systemui.power.PowerUI_Factory;
import com.android.systemui.privacy.PrivacyDialogController;
import com.android.systemui.privacy.PrivacyDialogController_Factory;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.PrivacyItemController_Factory;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.privacy.logging.PrivacyLogger_Factory;
import com.android.systemui.privacy.television.TvOngoingPrivacyChip;
import com.android.systemui.privacy.television.TvOngoingPrivacyChip_Factory;
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
import com.android.systemui.qs.dagger.QSModule_ProvideAutoTileManagerFactory;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.qs.external.CustomTileStatePersister;
import com.android.systemui.qs.external.CustomTileStatePersister_Factory;
import com.android.systemui.qs.external.CustomTile_Builder_Factory;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.logging.QSLogger_Factory;
import com.android.systemui.qs.tileimpl.QSFactoryImpl;
import com.android.systemui.qs.tileimpl.QSFactoryImpl_Factory;
import com.android.systemui.qs.tiles.AirplaneModeTile;
import com.android.systemui.qs.tiles.AirplaneModeTile_Factory;
import com.android.systemui.qs.tiles.AlarmTile;
import com.android.systemui.qs.tiles.AlarmTile_Factory;
import com.android.systemui.qs.tiles.BatterySaverTile;
import com.android.systemui.qs.tiles.BatterySaverTile_Factory;
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
import com.android.systemui.shared.system.TaskStackChangeListeners;
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
import com.android.systemui.statusbar.KeyguardIndicationController;
import com.android.systemui.statusbar.KeyguardIndicationController_Factory;
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
import com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl;
import com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl_Factory;
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
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.phone.ScrimController_Factory;
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
import com.android.systemui.statusbar.phone.dagger.StatusBarPhoneModule_ProvideStatusBarFactory;
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
import com.android.systemui.statusbar.tv.notifications.TvNotificationPanel;
import com.android.systemui.statusbar.tv.notifications.TvNotificationPanelActivity;
import com.android.systemui.statusbar.tv.notifications.TvNotificationPanelActivity_Factory;
import com.android.systemui.statusbar.tv.notifications.TvNotificationPanel_Factory;
import com.android.systemui.telephony.TelephonyCallback_Factory;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.telephony.TelephonyListenerManager_Factory;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.systemui.theme.ThemeOverlayController;
import com.android.systemui.theme.ThemeOverlayController_Factory;
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
import com.android.systemui.tv.TvGlobalRootComponent;
import com.android.systemui.tv.TvSysUIComponent;
import com.android.systemui.tv.TvWMComponent;
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
import com.android.systemui.wmshell.TvPipModule_ProvidePipAnimationControllerFactory;
import com.android.systemui.wmshell.TvPipModule_ProvidePipBoundsAlgorithmFactory;
import com.android.systemui.wmshell.TvPipModule_ProvidePipBoundsStateFactory;
import com.android.systemui.wmshell.TvPipModule_ProvidePipFactory;
import com.android.systemui.wmshell.TvPipModule_ProvidePipSnapAlgorithmFactory;
import com.android.systemui.wmshell.TvPipModule_ProvidePipTaskOrganizerFactory;
import com.android.systemui.wmshell.TvPipModule_ProvideTvPipNotificationControllerFactory;
import com.android.systemui.wmshell.TvPipModule_ProvideTvPipTransitionFactory;
import com.android.systemui.wmshell.TvPipModule_ProvidesTvPipMenuControllerFactory;
import com.android.systemui.wmshell.TvWMShellModule_ProvideDisplayImeControllerFactory;
import com.android.systemui.wmshell.TvWMShellModule_ProvideSplitScreenFactory;
import com.android.systemui.wmshell.TvWMShellModule_ProvideStartingWindowTypeAlgorithmFactory;
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
import com.android.wm.shell.pip.phone.PipTouchHandler;
import com.android.wm.shell.pip.tv.TvPipMenuController;
import com.android.wm.shell.pip.tv.TvPipNotificationController;
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
import dagger.Lazy;
import dagger.internal.DelegateFactory;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.InstanceFactory;
import dagger.internal.MapProviderFactory;
import dagger.internal.Preconditions;
import dagger.internal.SetFactory;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class DaggerTvGlobalRootComponent implements TvGlobalRootComponent {
    private static final Provider ABSENT_JDK_OPTIONAL_PROVIDER = InstanceFactory.create(Optional.empty());
    private Provider<BuildInfo> buildInfoProvider;
    private final Context context;
    private Provider<Context> contextProvider;
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

    private DaggerTvGlobalRootComponent(GlobalModule globalModule, Context context) {
        this.context = context;
        initialize(globalModule, context);
    }

    public static TvGlobalRootComponent.Builder builder() {
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
        this.provideContentResolverProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideContentResolverFactory.create(this.contextProvider));
        this.provideUserManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideUserManagerFactory.create(this.contextProvider));
        this.provideMainExecutorProvider = GlobalConcurrencyModule_ProvideMainExecutorFactory.create(this.contextProvider);
        this.provideDisplayMetricsProvider = GlobalModule_ProvideDisplayMetricsFactory.create(globalModule, this.contextProvider);
        this.providePowerManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidePowerManagerFactory.create(this.contextProvider));
        this.provideViewConfigurationProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideViewConfigurationFactory.create(this.contextProvider));
        this.provideResourcesProvider = FrameworkServicesModule_ProvideResourcesFactory.create(this.contextProvider);
        this.provideAudioManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideAudioManagerFactory.create(this.contextProvider));
        this.provideActivityTaskManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideActivityTaskManagerFactory.create());
        this.providesFingerprintManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidesFingerprintManagerFactory.create(this.contextProvider));
        this.provideFaceManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideFaceManagerFactory.create(this.contextProvider));
        this.provideExecutionProvider = DoubleCheck.provider(ExecutionImpl_Factory.create());
        this.buildInfoProvider = DoubleCheck.provider(BuildInfo_Factory.create());
        this.provideNotificationManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideNotificationManagerFactory.create(this.contextProvider));
        this.provideDevicePolicyManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideDevicePolicyManagerFactory.create(this.contextProvider));
        this.provideKeyguardManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideKeyguardManagerFactory.create(this.contextProvider));
        this.providePackageManagerWrapperProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidePackageManagerWrapperFactory.create());
        this.provideIActivityManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIActivityManagerFactory.create());
        this.providesSensorManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidesSensorManagerFactory.create(this.contextProvider));
        this.provideTrustManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideTrustManagerFactory.create(this.contextProvider));
        this.provideTelephonyManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideTelephonyManagerFactory.create(this.contextProvider));
        this.provideIActivityTaskManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIActivityTaskManagerFactory.create());
        this.provideAccessibilityManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideAccessibilityManagerFactory.create(this.contextProvider));
        this.provideCrossWindowBlurListenersProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideCrossWindowBlurListenersFactory.create());
        this.provideAlarmManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideAlarmManagerFactory.create(this.contextProvider));
        this.provideWallpaperManagerProvider = FrameworkServicesModule_ProvideWallpaperManagerFactory.create(this.contextProvider);
        this.provideMediaSessionManagerProvider = FrameworkServicesModule_ProvideMediaSessionManagerFactory.create(this.contextProvider);
        this.provideMediaRouter2ManagerProvider = FrameworkServicesModule_ProvideMediaRouter2ManagerFactory.create(this.contextProvider);
        this.provideSensorPrivacyManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideSensorPrivacyManagerFactory.create(this.contextProvider));
        this.provideIPackageManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIPackageManagerFactory.create());
        this.provideIDreamManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIDreamManagerFactory.create());
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
        this.provideIBatteryStatsProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIBatteryStatsFactory.create());
        this.provideVibratorProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideVibratorFactory.create(this.contextProvider));
        this.provideDisplayManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideDisplayManagerFactory.create(this.contextProvider));
        this.provideActivityManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideActivityManagerFactory.create(this.contextProvider));
        this.provideOverlayManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideOverlayManagerFactory.create(this.contextProvider));
        this.provideColorDisplayManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideColorDisplayManagerFactory.create(this.contextProvider));
        this.provideLatencyTrackerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideLatencyTrackerFactory.create(this.contextProvider));
        this.provideInputMethodManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideInputMethodManagerFactory.create(this.contextProvider));
        this.qSExpansionPathInterpolatorProvider = DoubleCheck.provider(QSExpansionPathInterpolator_Factory.create());
        this.providePermissionManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidePermissionManagerFactory.create(this.contextProvider));
    }

    @Override // com.android.systemui.dagger.GlobalRootComponent
    public ThreadFactory createThreadFactory() {
        return ThreadFactoryImpl_Factory.newInstance();
    }

    @Override // com.android.systemui.dagger.GlobalRootComponent
    public TvWMComponent.Builder getWMComponentBuilder() {
        return new TvWMComponentBuilder();
    }

    @Override // com.android.systemui.dagger.GlobalRootComponent
    public TvSysUIComponent.Builder getSysUIComponent() {
        return new TvSysUIComponentBuilder();
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
    private static final class Builder implements TvGlobalRootComponent.Builder {
        private Context context;

        private Builder() {
        }

        @Override // com.android.systemui.dagger.GlobalRootComponent.Builder
        public Builder context(Context context) {
            this.context = (Context) Preconditions.checkNotNull(context);
            return this;
        }

        @Override // com.android.systemui.dagger.GlobalRootComponent.Builder
        public TvGlobalRootComponent build() {
            Preconditions.checkBuilderRequirement(this.context, Context.class);
            return new DaggerTvGlobalRootComponent(new GlobalModule(), this.context);
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public final class TvWMComponentBuilder implements TvWMComponent.Builder {
        private TvWMComponentBuilder() {
        }

        @Override // com.android.systemui.dagger.WMComponent.Builder
        public TvWMComponent build() {
            return new TvWMComponentImpl();
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public final class TvWMComponentImpl implements TvWMComponent {
        private Provider<Optional<AppPairsController>> optionalOfAppPairsControllerProvider;
        private Provider<Optional<LegacySplitScreenController>> optionalOfLegacySplitScreenControllerProvider;
        private Provider<Optional<PipTouchHandler>> optionalOfPipTouchHandlerProvider;
        private Provider<Optional<AppPairs>> provideAppPairsProvider;
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
        private Provider<Optional<LegacySplitScreen>> provideLegacySplitScreenProvider;
        private Provider<Optional<OneHandedController>> provideOneHandedControllerProvider;
        private Provider<Optional<OneHanded>> provideOneHandedProvider;
        private Provider<PipAnimationController> providePipAnimationControllerProvider;
        private Provider<PipBoundsAlgorithm> providePipBoundsAlgorithmProvider;
        private Provider<PipBoundsState> providePipBoundsStateProvider;
        private Provider<PipMediaController> providePipMediaControllerProvider;
        private Provider<Optional<Pip>> providePipProvider;
        private Provider<PipSnapAlgorithm> providePipSnapAlgorithmProvider;
        private Provider<PipSurfaceTransactionHelper> providePipSurfaceTransactionHelperProvider;
        private Provider<PipTaskOrganizer> providePipTaskOrganizerProvider;
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
        private Provider<LegacySplitScreenController> provideSplitScreenProvider;
        private Provider<Optional<SplitScreen>> provideSplitScreenProvider2;
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
        private Provider<TvPipNotificationController> provideTvPipNotificationControllerProvider;
        private Provider<PipTransitionController> provideTvPipTransitionProvider;
        private Provider<WindowManagerShellWrapper> provideWindowManagerShellWrapperProvider;
        private Provider<TaskStackListenerImpl> providerTaskStackListenerImplProvider;
        private Provider<TvPipMenuController> providesTvPipMenuControllerProvider;

        @Override // com.android.systemui.dagger.WMComponent
        public /* bridge */ /* synthetic */ void init() {
            super.init();
        }

        private TvWMComponentImpl() {
            initialize();
        }

        private void initialize() {
            this.provideShellMainHandlerProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideShellMainHandlerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider));
            this.provideSysUIMainExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideSysUIMainExecutorFactory.create(DaggerTvGlobalRootComponent.this.provideMainHandlerProvider));
            this.provideShellMainExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideShellMainExecutorFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideShellMainHandlerProvider, this.provideSysUIMainExecutorProvider));
            this.provideDisplayControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDisplayControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideIWindowManagerProvider, this.provideShellMainExecutorProvider));
            this.provideTransactionPoolProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTransactionPoolFactory.create());
            this.provideDisplayImeControllerProvider = DoubleCheck.provider(TvWMShellModule_ProvideDisplayImeControllerFactory.create(DaggerTvGlobalRootComponent.this.provideIWindowManagerProvider, this.provideDisplayControllerProvider, this.provideShellMainExecutorProvider, this.provideTransactionPoolProvider));
            this.provideDragAndDropControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDragAndDropControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideDisplayControllerProvider));
            this.provideSyncTransactionQueueProvider = DoubleCheck.provider(WMShellBaseModule_ProvideSyncTransactionQueueFactory.create(this.provideTransactionPoolProvider, this.provideShellMainExecutorProvider));
            this.provideSizeCompatUIControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideSizeCompatUIControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideDisplayControllerProvider, this.provideDisplayImeControllerProvider, this.provideSyncTransactionQueueProvider));
            this.provideShellTaskOrganizerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideShellTaskOrganizerFactory.create(this.provideShellMainExecutorProvider, DaggerTvGlobalRootComponent.this.contextProvider, this.provideSizeCompatUIControllerProvider));
            this.provideFloatingContentCoordinatorProvider = DoubleCheck.provider(WMShellBaseModule_ProvideFloatingContentCoordinatorFactory.create());
            this.provideWindowManagerShellWrapperProvider = DoubleCheck.provider(WMShellBaseModule_ProvideWindowManagerShellWrapperFactory.create(this.provideShellMainExecutorProvider));
            this.providerTaskStackListenerImplProvider = DoubleCheck.provider(WMShellBaseModule_ProviderTaskStackListenerImplFactory.create(this.provideShellMainHandlerProvider));
            this.provideBubbleControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideBubbleControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideFloatingContentCoordinatorProvider, DaggerTvGlobalRootComponent.this.provideIStatusBarServiceProvider, DaggerTvGlobalRootComponent.this.provideWindowManagerProvider, this.provideWindowManagerShellWrapperProvider, DaggerTvGlobalRootComponent.this.provideLauncherAppsProvider, this.providerTaskStackListenerImplProvider, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider, this.provideShellTaskOrganizerProvider, this.provideDisplayControllerProvider, this.provideShellMainExecutorProvider, this.provideShellMainHandlerProvider));
            this.provideSystemWindowsProvider = DoubleCheck.provider(WMShellBaseModule_ProvideSystemWindowsFactory.create(this.provideDisplayControllerProvider, DaggerTvGlobalRootComponent.this.provideIWindowManagerProvider));
            this.provideShellAnimationExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory.create());
            this.provideTransitionsProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTransitionsFactory.create(this.provideShellTaskOrganizerProvider, this.provideTransactionPoolProvider, DaggerTvGlobalRootComponent.this.contextProvider, this.provideShellMainExecutorProvider, this.provideShellAnimationExecutorProvider));
            this.provideShellMainExecutorSfVsyncAnimationHandlerProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideShellMainExecutorSfVsyncAnimationHandlerFactory.create(this.provideShellMainExecutorProvider));
            Provider<LegacySplitScreenController> provider = DoubleCheck.provider(TvWMShellModule_ProvideSplitScreenFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideDisplayControllerProvider, this.provideSystemWindowsProvider, this.provideDisplayImeControllerProvider, this.provideTransactionPoolProvider, this.provideShellTaskOrganizerProvider, this.provideSyncTransactionQueueProvider, this.providerTaskStackListenerImplProvider, this.provideTransitionsProvider, this.provideShellMainExecutorProvider, this.provideShellMainExecutorSfVsyncAnimationHandlerProvider));
            this.provideSplitScreenProvider = provider;
            this.optionalOfLegacySplitScreenControllerProvider = PresentJdkOptionalInstanceProvider.of(provider);
            this.provideRootTaskDisplayAreaOrganizerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideRootTaskDisplayAreaOrganizerFactory.create(this.provideShellMainExecutorProvider, DaggerTvGlobalRootComponent.this.contextProvider));
            this.provideSplitScreenControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideSplitScreenControllerFactory.create(this.provideShellTaskOrganizerProvider, this.provideSyncTransactionQueueProvider, DaggerTvGlobalRootComponent.this.contextProvider, this.provideRootTaskDisplayAreaOrganizerProvider, this.provideShellMainExecutorProvider, this.provideDisplayImeControllerProvider, this.provideTransitionsProvider, this.provideTransactionPoolProvider));
            this.optionalOfAppPairsControllerProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.optionalOfPipTouchHandlerProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.provideFullscreenTaskListenerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideFullscreenTaskListenerFactory.create(this.provideSyncTransactionQueueProvider));
            this.provideSplashScreenExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory.create());
            this.provideStartingWindowTypeAlgorithmProvider = DoubleCheck.provider(TvWMShellModule_ProvideStartingWindowTypeAlgorithmFactory.create());
            Provider<StartingWindowController> provider2 = DoubleCheck.provider(WMShellBaseModule_ProvideStartingWindowControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideSplashScreenExecutorProvider, this.provideStartingWindowTypeAlgorithmProvider, this.provideTransactionPoolProvider));
            this.provideStartingWindowControllerProvider = provider2;
            Provider<ShellInitImpl> provider3 = DoubleCheck.provider(WMShellBaseModule_ProvideShellInitImplFactory.create(this.provideDisplayImeControllerProvider, this.provideDragAndDropControllerProvider, this.provideShellTaskOrganizerProvider, this.provideBubbleControllerProvider, this.optionalOfLegacySplitScreenControllerProvider, this.provideSplitScreenControllerProvider, this.optionalOfAppPairsControllerProvider, this.optionalOfPipTouchHandlerProvider, this.provideFullscreenTaskListenerProvider, this.provideTransitionsProvider, provider2, this.provideShellMainExecutorProvider));
            this.provideShellInitImplProvider = provider3;
            this.provideShellInitProvider = DoubleCheck.provider(WMShellBaseModule_ProvideShellInitFactory.create(provider3));
            this.providePipBoundsStateProvider = DoubleCheck.provider(TvPipModule_ProvidePipBoundsStateFactory.create(DaggerTvGlobalRootComponent.this.contextProvider));
            this.providePipSnapAlgorithmProvider = DoubleCheck.provider(TvPipModule_ProvidePipSnapAlgorithmFactory.create());
            this.providePipBoundsAlgorithmProvider = DoubleCheck.provider(TvPipModule_ProvidePipBoundsAlgorithmFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.providePipBoundsStateProvider, this.providePipSnapAlgorithmProvider));
            this.providePipMediaControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvidePipMediaControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideShellMainHandlerProvider));
            this.providesTvPipMenuControllerProvider = DoubleCheck.provider(TvPipModule_ProvidesTvPipMenuControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.providePipBoundsStateProvider, this.provideSystemWindowsProvider, this.providePipMediaControllerProvider, this.provideShellMainHandlerProvider));
            Provider<PipSurfaceTransactionHelper> provider4 = DoubleCheck.provider(WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory.create());
            this.providePipSurfaceTransactionHelperProvider = provider4;
            Provider<PipAnimationController> provider5 = DoubleCheck.provider(TvPipModule_ProvidePipAnimationControllerFactory.create(provider4));
            this.providePipAnimationControllerProvider = provider5;
            this.provideTvPipTransitionProvider = DoubleCheck.provider(TvPipModule_ProvideTvPipTransitionFactory.create(this.provideTransitionsProvider, this.provideShellTaskOrganizerProvider, provider5, this.providePipBoundsAlgorithmProvider, this.providePipBoundsStateProvider, this.providesTvPipMenuControllerProvider));
            this.providePipUiEventLoggerProvider = DoubleCheck.provider(WMShellBaseModule_ProvidePipUiEventLoggerFactory.create(DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider, DaggerTvGlobalRootComponent.this.providePackageManagerProvider));
            this.providePipTaskOrganizerProvider = DoubleCheck.provider(TvPipModule_ProvidePipTaskOrganizerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.providesTvPipMenuControllerProvider, this.provideSyncTransactionQueueProvider, this.providePipBoundsStateProvider, this.providePipBoundsAlgorithmProvider, this.providePipAnimationControllerProvider, this.provideTvPipTransitionProvider, this.providePipSurfaceTransactionHelperProvider, this.optionalOfLegacySplitScreenControllerProvider, this.provideDisplayControllerProvider, this.providePipUiEventLoggerProvider, this.provideShellTaskOrganizerProvider, this.provideShellMainExecutorProvider));
            this.provideTvPipNotificationControllerProvider = DoubleCheck.provider(TvPipModule_ProvideTvPipNotificationControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.providePipMediaControllerProvider, this.provideShellMainHandlerProvider));
            this.providePipProvider = DoubleCheck.provider(TvPipModule_ProvidePipFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.providePipBoundsStateProvider, this.providePipBoundsAlgorithmProvider, this.providePipTaskOrganizerProvider, this.providesTvPipMenuControllerProvider, this.providePipMediaControllerProvider, this.provideTvPipTransitionProvider, this.provideTvPipNotificationControllerProvider, this.providerTaskStackListenerImplProvider, this.provideWindowManagerShellWrapperProvider, this.provideShellMainExecutorProvider));
            this.provideDisplayLayoutProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDisplayLayoutFactory.create());
            this.provideOneHandedControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideOneHandedControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideWindowManagerProvider, this.provideDisplayControllerProvider, this.provideDisplayLayoutProvider, this.providerTaskStackListenerImplProvider, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider, this.provideShellMainExecutorProvider, this.provideShellMainHandlerProvider));
            Provider<Optional<HideDisplayCutoutController>> provider6 = DoubleCheck.provider(WMShellBaseModule_ProvideHideDisplayCutoutControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideDisplayControllerProvider, this.provideShellMainExecutorProvider));
            this.provideHideDisplayCutoutControllerProvider = provider6;
            Provider<ShellCommandHandlerImpl> provider7 = DoubleCheck.provider(WMShellBaseModule_ProvideShellCommandHandlerImplFactory.create(this.provideShellTaskOrganizerProvider, this.optionalOfLegacySplitScreenControllerProvider, this.provideSplitScreenControllerProvider, this.providePipProvider, this.provideOneHandedControllerProvider, provider6, this.optionalOfAppPairsControllerProvider, this.provideShellMainExecutorProvider));
            this.provideShellCommandHandlerImplProvider = provider7;
            this.provideShellCommandHandlerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideShellCommandHandlerFactory.create(provider7));
            this.provideOneHandedProvider = DoubleCheck.provider(WMShellBaseModule_ProvideOneHandedFactory.create(this.provideOneHandedControllerProvider));
            this.provideLegacySplitScreenProvider = DoubleCheck.provider(WMShellBaseModule_ProvideLegacySplitScreenFactory.create(this.optionalOfLegacySplitScreenControllerProvider));
            this.provideSplitScreenProvider2 = DoubleCheck.provider(WMShellBaseModule_ProvideSplitScreenFactory.create(this.provideSplitScreenControllerProvider));
            this.provideAppPairsProvider = DoubleCheck.provider(WMShellBaseModule_ProvideAppPairsFactory.create(this.optionalOfAppPairsControllerProvider));
            this.provideBubblesProvider = DoubleCheck.provider(WMShellBaseModule_ProvideBubblesFactory.create(this.provideBubbleControllerProvider));
            this.provideHideDisplayCutoutProvider = DoubleCheck.provider(WMShellBaseModule_ProvideHideDisplayCutoutFactory.create(this.provideHideDisplayCutoutControllerProvider));
            Provider<TaskViewFactoryController> provider8 = DoubleCheck.provider(WMShellBaseModule_ProvideTaskViewFactoryControllerFactory.create(this.provideShellTaskOrganizerProvider, this.provideShellMainExecutorProvider));
            this.provideTaskViewFactoryControllerProvider = provider8;
            this.provideTaskViewFactoryProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTaskViewFactoryFactory.create(provider8));
            this.provideRemoteTransitionsProvider = DoubleCheck.provider(WMShellBaseModule_ProvideRemoteTransitionsFactory.create(this.provideTransitionsProvider));
            this.provideStartingSurfaceProvider = DoubleCheck.provider(WMShellBaseModule_ProvideStartingSurfaceFactory.create(this.provideStartingWindowControllerProvider));
            Provider<Optional<TaskSurfaceHelperController>> provider9 = DoubleCheck.provider(WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory.create(this.provideShellTaskOrganizerProvider, this.provideShellMainExecutorProvider));
            this.provideTaskSurfaceHelperControllerProvider = provider9;
            this.provideTaskSurfaceHelperProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTaskSurfaceHelperFactory.create(provider9));
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
            return this.provideLegacySplitScreenProvider.get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public Optional<SplitScreen> getSplitScreen() {
            return this.provideSplitScreenProvider2.get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public Optional<AppPairs> getAppPairs() {
            return this.provideAppPairsProvider.get();
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
    public final class TvSysUIComponentBuilder implements TvSysUIComponent.Builder {
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

        private TvSysUIComponentBuilder() {
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public TvSysUIComponentBuilder setPip(Optional<Pip> optional) {
            this.setPip = (Optional) Preconditions.checkNotNull(optional);
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public TvSysUIComponentBuilder setLegacySplitScreen(Optional<LegacySplitScreen> optional) {
            this.setLegacySplitScreen = (Optional) Preconditions.checkNotNull(optional);
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public TvSysUIComponentBuilder setSplitScreen(Optional<SplitScreen> optional) {
            this.setSplitScreen = (Optional) Preconditions.checkNotNull(optional);
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public TvSysUIComponentBuilder setAppPairs(Optional<AppPairs> optional) {
            this.setAppPairs = (Optional) Preconditions.checkNotNull(optional);
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public TvSysUIComponentBuilder setOneHanded(Optional<OneHanded> optional) {
            this.setOneHanded = (Optional) Preconditions.checkNotNull(optional);
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public TvSysUIComponentBuilder setBubbles(Optional<Bubbles> optional) {
            this.setBubbles = (Optional) Preconditions.checkNotNull(optional);
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public TvSysUIComponentBuilder setTaskViewFactory(Optional<TaskViewFactory> optional) {
            this.setTaskViewFactory = (Optional) Preconditions.checkNotNull(optional);
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public TvSysUIComponentBuilder setHideDisplayCutout(Optional<HideDisplayCutout> optional) {
            this.setHideDisplayCutout = (Optional) Preconditions.checkNotNull(optional);
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public TvSysUIComponentBuilder setShellCommandHandler(Optional<ShellCommandHandler> optional) {
            this.setShellCommandHandler = (Optional) Preconditions.checkNotNull(optional);
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public TvSysUIComponentBuilder setTransitions(ShellTransitions shellTransitions) {
            this.setTransitions = (ShellTransitions) Preconditions.checkNotNull(shellTransitions);
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public TvSysUIComponentBuilder setStartingSurface(Optional<StartingSurface> optional) {
            this.setStartingSurface = (Optional) Preconditions.checkNotNull(optional);
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public TvSysUIComponentBuilder setTaskSurfaceHelper(Optional<TaskSurfaceHelper> optional) {
            this.setTaskSurfaceHelper = (Optional) Preconditions.checkNotNull(optional);
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public TvSysUIComponent build() {
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
            return new TvSysUIComponentImpl(new DependencyProvider(), new NightDisplayListenerModule(), new UserModule(), this.setPip, this.setLegacySplitScreen, this.setSplitScreen, this.setAppPairs, this.setOneHanded, this.setBubbles, this.setTaskViewFactory, this.setHideDisplayCutout, this.setShellCommandHandler, this.setTransitions, this.setStartingSurface, this.setTaskSurfaceHelper);
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public final class TvSysUIComponentImpl implements TvSysUIComponent {
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
        private Provider<AssistLogger> assistLoggerProvider;
        private Provider<AssistManager> assistManagerProvider;
        private Provider<AssistantFeedbackController> assistantFeedbackControllerProvider;
        private Provider<AsyncSensorManager> asyncSensorManagerProvider;
        private Provider<AuthController> authControllerProvider;
        private Provider<BatterySaverTile> batterySaverTileProvider;
        private Provider<BatteryStateNotifier> batteryStateNotifierProvider;
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
        private Provider<DelayedWakeLock.Builder> builderProvider2;
        private Provider<NotificationClicker.Builder> builderProvider3;
        private Provider<StatusBarNotificationActivityStarter.Builder> builderProvider4;
        private Provider<WakeLock.Builder> builderProvider5;
        private Provider<CustomTile.Builder> builderProvider6;
        private Provider<NightDisplayListenerModule.Builder> builderProvider7;
        private Provider<AutoAddTracker.Builder> builderProvider8;
        private Provider<BypassHeadsUpNotifier> bypassHeadsUpNotifierProvider;
        private Provider<CallbackHandler> callbackHandlerProvider;
        private Provider<CameraToggleTile> cameraToggleTileProvider;
        private Provider<CarrierConfigTracker> carrierConfigTrackerProvider;
        private Provider<CastControllerImpl> castControllerImplProvider;
        private Provider<CastTile> castTileProvider;
        private Provider<CellularTile> cellularTileProvider;
        private Provider<ChannelEditorDialogController> channelEditorDialogControllerProvider;
        private Provider<ClockManager> clockManagerProvider;
        private Provider<ColorInversionTile> colorInversionTileProvider;
        private Provider<CommandRegistry> commandRegistryProvider;
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
        private Provider<DataSaverTile> dataSaverTileProvider;
        private Provider<DateFormatUtil> dateFormatUtilProvider;
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
        private Provider distanceClassifierProvider;
        private Provider<DndTile> dndTileProvider;
        private Provider<DockManagerImpl> dockManagerImplProvider;
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
        private Provider<EnhancedEstimatesImpl> enhancedEstimatesImplProvider;
        private Provider<ExpandableNotificationRowComponent.Builder> expandableNotificationRowComponentBuilderProvider;
        private Provider<NotificationLogger.ExpansionStateLogger> expansionStateLoggerProvider;
        private Provider<ExtensionControllerImpl> extensionControllerImplProvider;
        private Provider<KeyguardBouncer.Factory> factoryProvider;
        private Provider<KeyguardMessageAreaController.Factory> factoryProvider2;
        private Provider<BrightnessSlider.Factory> factoryProvider3;
        private Provider<EdgeBackGestureHandler.Factory> factoryProvider4;
        private Provider falsingCollectorImplProvider;
        private Provider<FalsingDataProvider> falsingDataProvider;
        private Provider<FalsingManagerProxy> falsingManagerProxyProvider;
        private Provider<FeatureFlagReader> featureFlagReaderProvider;
        private Provider<FeatureFlags> featureFlagsProvider;
        private Provider<Files> filesProvider;
        private Provider<FlashlightControllerImpl> flashlightControllerImplProvider;
        private Provider<FlashlightTile> flashlightTileProvider;
        private Provider<ForegroundServiceController> foregroundServiceControllerProvider;
        private Provider<ForegroundServiceDismissalFeatureController> foregroundServiceDismissalFeatureControllerProvider;
        private Provider<ForegroundServiceNotificationListener> foregroundServiceNotificationListenerProvider;
        private Provider<ForegroundServiceSectionController> foregroundServiceSectionControllerProvider;
        private Provider<FragmentService.FragmentCreator.Factory> fragmentCreatorFactoryProvider;
        private Provider<FragmentService> fragmentServiceProvider;
        private Provider<GarbageMonitor> garbageMonitorProvider;
        private Provider<GlobalActionsComponent> globalActionsComponentProvider;
        private Provider<GlobalActionsDialogLite> globalActionsDialogLiteProvider;
        private Provider<GlobalActionsImpl> globalActionsImplProvider;
        private Provider<GlobalActionsInfoProvider> globalActionsInfoProvider;
        private Provider globalSettingsImplProvider;
        private Provider<GroupCoalescerLogger> groupCoalescerLoggerProvider;
        private Provider<GroupCoalescer> groupCoalescerProvider;
        private Provider<HeadsUpController> headsUpControllerProvider;
        private Provider<HeadsUpCoordinator> headsUpCoordinatorProvider;
        private Provider<HeadsUpViewBinder> headsUpViewBinderProvider;
        private Provider<HideNotifsForOtherUsersCoordinator> hideNotifsForOtherUsersCoordinatorProvider;
        private Provider<HighPriorityProvider> highPriorityProvider;
        private Provider<HistoryTracker> historyTrackerProvider;
        private Provider<HomeSoundEffectController> homeSoundEffectControllerProvider;
        private Provider<HotspotControllerImpl> hotspotControllerImplProvider;
        private Provider<HotspotTile> hotspotTileProvider;
        private Provider<IconBuilder> iconBuilderProvider;
        private Provider<IconManager> iconManagerProvider;
        private Provider imageExporterProvider;
        private Provider imageTileSetProvider;
        private Provider<InitController> initControllerProvider;
        private Provider<InjectionInflationController> injectionInflationControllerProvider;
        private Provider<InstantAppNotifier> instantAppNotifierProvider;
        private Provider<InternetTile> internetTileProvider;
        private Provider<Boolean> isPMLiteEnabledProvider;
        private Provider<Boolean> isReduceBrightColorsAvailableProvider;
        private Provider<KeyguardBouncerComponent.Factory> keyguardBouncerComponentFactoryProvider;
        private Provider<KeyguardBypassController> keyguardBypassControllerProvider;
        private Provider<KeyguardCoordinator> keyguardCoordinatorProvider;
        private Provider<KeyguardDismissUtil> keyguardDismissUtilProvider;
        private Provider<KeyguardDisplayManager> keyguardDisplayManagerProvider;
        private Provider<KeyguardEnvironmentImpl> keyguardEnvironmentImplProvider;
        private Provider<KeyguardIndicationController> keyguardIndicationControllerProvider;
        private Provider<KeyguardLifecyclesDispatcher> keyguardLifecyclesDispatcherProvider;
        private Provider<KeyguardMediaController> keyguardMediaControllerProvider;
        private Provider<KeyguardQsUserSwitchComponent.Factory> keyguardQsUserSwitchComponentFactoryProvider;
        private Provider<KeyguardSecurityModel> keyguardSecurityModelProvider;
        private Provider<KeyguardService> keyguardServiceProvider;
        private Provider<KeyguardStateControllerImpl> keyguardStateControllerImplProvider;
        private Provider<KeyguardStatusBarViewComponent.Factory> keyguardStatusBarViewComponentFactoryProvider;
        private Provider<KeyguardStatusViewComponent.Factory> keyguardStatusViewComponentFactoryProvider;
        private Provider<KeyguardUnlockAnimationController> keyguardUnlockAnimationControllerProvider;
        private Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
        private Provider<KeyguardUserSwitcherComponent.Factory> keyguardUserSwitcherComponentFactoryProvider;
        private Provider<LatencyTester> latencyTesterProvider;
        private Provider<LaunchConversationActivity> launchConversationActivityProvider;
        private Provider<LeakReporter> leakReporterProvider;
        private Provider<LightBarController> lightBarControllerProvider;
        private Provider<LightsOutNotifController> lightsOutNotifControllerProvider;
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
        private Provider<Set<FalsingClassifier>> namedSetOfFalsingClassifierProvider;
        private Provider<NavigationBarOverlayController> navigationBarOverlayControllerProvider;
        private Provider<NavigationModeController> navigationModeControllerProvider;
        private Provider<NetworkControllerImpl> networkControllerImplProvider;
        private Provider<KeyguardViewMediator> newKeyguardViewMediatorProvider;
        private Provider<NextAlarmControllerImpl> nextAlarmControllerImplProvider;
        private Provider<NfcTile> nfcTileProvider;
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
        private Provider<NotificationLockscreenUserManagerImpl> notificationLockscreenUserManagerImplProvider;
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
        private Provider<NotificationWakeUpCoordinator> notificationWakeUpCoordinatorProvider;
        private Provider<NotificationsControllerImpl> notificationsControllerImplProvider;
        private Provider<NotificationsControllerStub> notificationsControllerStubProvider;
        private Provider<OngoingCallLogger> ongoingCallLoggerProvider;
        private Provider<Optional<BcSmartspaceDataPlugin>> optionalOfBcSmartspaceDataPluginProvider;
        private Provider<Optional<ControlsFavoritePersistenceWrapper>> optionalOfControlsFavoritePersistenceWrapperProvider;
        private Provider<Optional<Lazy<StatusBar>>> optionalOfLazyOfStatusBarProvider;
        private Provider<Optional<Recents>> optionalOfRecentsProvider;
        private Provider<Optional<StatusBar>> optionalOfStatusBarProvider;
        private Provider<Optional<UdfpsHbmProvider>> optionalOfUdfpsHbmProvider;
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
        private Provider<PowerNotificationWarnings> powerNotificationWarningsProvider;
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
        private Provider<Boolean> provideAllowNotificationLongPressProvider;
        private Provider<AlwaysOnDisplayPolicy> provideAlwaysOnDisplayPolicyProvider;
        private Provider<AmbientDisplayConfiguration> provideAmbientDisplayConfigurationProvider;
        private Provider<AssistUtils> provideAssistUtilsProvider;
        private Provider<AutoHideController> provideAutoHideControllerProvider;
        private Provider<AutoTileManager> provideAutoTileManagerProvider;
        private Provider<DelayableExecutor> provideBackgroundDelayableExecutorProvider;
        private Provider<Executor> provideBackgroundExecutorProvider;
        private Provider<RepeatableExecutor> provideBackgroundRepeatableExecutorProvider;
        private Provider<BatteryController> provideBatteryControllerProvider;
        private Provider<Handler> provideBgHandlerProvider;
        private Provider<Looper> provideBgLooperProvider;
        private Provider<LogBuffer> provideBroadcastDispatcherLogBufferProvider;
        private Provider<Optional<BubblesManager>> provideBubblesManagerProvider;
        private Provider provideClockInfoListProvider;
        private Provider<CommandQueue> provideCommandQueueProvider;
        private Provider<CommonNotifCollection> provideCommonNotifCollectionProvider;
        private Provider<ConfigurationController> provideConfigurationControllerProvider;
        private Provider<DataSaverController> provideDataSaverControllerProvider;
        private Provider<DelayableExecutor> provideDelayableExecutorProvider;
        private Provider<DemoModeController> provideDemoModeControllerProvider;
        private Provider<DevicePolicyManagerWrapper> provideDevicePolicyManagerWrapperProvider;
        private Provider<LogBuffer> provideDozeLogBufferProvider;
        private Provider<EditUserInfoController> provideEditUserInfoControllerProvider;
        private Provider<Executor> provideExecutorProvider;
        private Provider<Optional<FaceAuthScreenBrightnessController>> provideFaceAuthScreenBrightnessControllerProvider;
        private Provider<GroupExpansionManager> provideGroupExpansionManagerProvider;
        private Provider<GroupMembershipManager> provideGroupMembershipManagerProvider;
        private Provider<Handler> provideHandlerProvider;
        private Provider<HeadsUpManagerPhone> provideHeadsUpManagerPhoneProvider;
        private Provider<INotificationManager> provideINotificationManagerProvider;
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
        private Provider<NotificationsController> provideNotificationsControllerProvider;
        private Provider<LogBuffer> provideNotificationsLogBufferProvider;
        private Provider<OnUserInteractionCallback> provideOnUserInteractionCallbackProvider;
        private Provider<OngoingCallController> provideOngoingCallControllerProvider;
        private Provider<PluginManager> providePluginManagerProvider;
        private Provider<ThresholdSensor> providePrimaryProxSensorProvider;
        private Provider<LogBuffer> providePrivacyLogBufferProvider;
        private Provider<QuickAccessWalletClient> provideQuickAccessWalletClientProvider;
        private Provider<LogBuffer> provideQuickSettingsLogBufferProvider;
        private Provider<RecentsImplementation> provideRecentsImplProvider;
        private Provider<Recents> provideRecentsProvider;
        private Provider<ReduceBrightColorsController> provideReduceBrightColorsListenerProvider;
        private Provider<ThresholdSensor> provideSecondaryProxSensorProvider;
        private Provider<SensorPrivacyController> provideSensorPrivacyControllerProvider;
        private Provider<SharedPreferences> provideSharePreferencesProvider;
        private Provider<SmartReplyController> provideSmartReplyControllerProvider;
        private Provider<SmartspaceTransitionController> provideSmartspaceTransitionControllerProvider;
        private Provider<StatusBar> provideStatusBarProvider;
        private Provider<SysUiState> provideSysUiStateProvider;
        private Provider<TaskStackChangeListeners> provideTaskStackChangeListenersProvider;
        private Provider<ThemeOverlayApplier> provideThemeOverlayManagerProvider;
        private Provider<Handler> provideTimeTickHandlerProvider;
        private Provider<LogBuffer> provideToastLogBufferProvider;
        private Provider<TvNotificationHandler> provideTvNotificationHandlerProvider;
        private Provider<Executor> provideUiBackgroundExecutorProvider;
        private Provider<UserTracker> provideUserTrackerProvider;
        private Provider<VisualStabilityManager> provideVisualStabilityManagerProvider;
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
        private Provider<ProximitySensor> proximitySensorProvider;
        private Provider<PulseExpansionHandler> pulseExpansionHandlerProvider;
        private Provider<QSDetailDisplayer> qSDetailDisplayerProvider;
        private Provider<QSFactoryImpl> qSFactoryImplProvider;
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
        private Provider<RingerModeTrackerImpl> ringerModeTrackerImplProvider;
        private Provider<RotationLockControllerImpl> rotationLockControllerImplProvider;
        private Provider<RotationLockTile> rotationLockTileProvider;
        private Provider<RowContentBindStageLogger> rowContentBindStageLoggerProvider;
        private Provider<RowContentBindStage> rowContentBindStageProvider;
        private Provider<ScreenDecorations> screenDecorationsProvider;
        private Provider<ScreenLifecycle> screenLifecycleProvider;
        private Provider<ScreenPinningRequest> screenPinningRequestProvider;
        private Provider<ScreenRecordDialog> screenRecordDialogProvider;
        private Provider<ScreenRecordTile> screenRecordTileProvider;
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
        private Provider<SensorUseStartedActivity> sensorUseStartedActivityProvider;
        private Provider<GarbageMonitor.Service> serviceProvider;
        private Provider<Optional<Bubbles>> setBubblesProvider;
        private Provider<Optional<HideDisplayCutout>> setHideDisplayCutoutProvider;
        private Provider<Optional<LegacySplitScreen>> setLegacySplitScreenProvider;
        private Provider<Optional<OneHanded>> setOneHandedProvider;
        private Provider<Optional<Pip>> setPipProvider;
        private Provider<Optional<ShellCommandHandler>> setShellCommandHandlerProvider;
        private Provider<Optional<SplitScreen>> setSplitScreenProvider;
        private Provider<Optional<StartingSurface>> setStartingSurfaceProvider;
        private Provider<Optional<TaskViewFactory>> setTaskViewFactoryProvider;
        private Provider<ShellTransitions> setTransitionsProvider;
        private Provider<ShadeControllerImpl> shadeControllerImplProvider;
        private Provider<ShadeListBuilderLogger> shadeListBuilderLoggerProvider;
        private Provider<ShadeListBuilder> shadeListBuilderProvider;
        private Provider<ShadeViewDifferLogger> shadeViewDifferLoggerProvider;
        private Provider<ShadeViewManagerFactory> shadeViewManagerFactoryProvider;
        private Provider<SharedCoordinatorLogger> sharedCoordinatorLoggerProvider;
        private Provider<ShortcutKeyDispatcher> shortcutKeyDispatcherProvider;
        private Provider<SidefpsController> sidefpsControllerProvider;
        private Provider<SingleTapClassifier> singleTapClassifierProvider;
        private Provider<SliceBroadcastRelayHandler> sliceBroadcastRelayHandlerProvider;
        private Provider<SmartActionInflaterImpl> smartActionInflaterImplProvider;
        private Provider<SmartActionsReceiver> smartActionsReceiverProvider;
        private Provider<SmartReplyConstants> smartReplyConstantsProvider;
        private Provider<SmartReplyInflaterImpl> smartReplyInflaterImplProvider;
        private Provider<SmartReplyStateInflaterImpl> smartReplyStateInflaterImplProvider;
        private Provider<SmartspaceDedupingCoordinator> smartspaceDedupingCoordinatorProvider;
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
        private Provider<SystemPropertiesHelper> systemPropertiesHelperProvider;
        private Provider systemSettingsImplProvider;
        private Provider<SystemStatusAnimationScheduler> systemStatusAnimationSchedulerProvider;
        private Provider<SystemUIAuxiliaryDumpService> systemUIAuxiliaryDumpServiceProvider;
        private Provider<SystemUIService> systemUIServiceProvider;
        private Provider<SysuiColorExtractor> sysuiColorExtractorProvider;
        private Provider<TakeScreenshotService> takeScreenshotServiceProvider;
        private Provider<TargetSdkResolver> targetSdkResolverProvider;
        private Provider<TelephonyListenerManager> telephonyListenerManagerProvider;
        private Provider<ThemeOverlayController> themeOverlayControllerProvider;
        private Provider<ToastFactory> toastFactoryProvider;
        private Provider<ToastLogger> toastLoggerProvider;
        private Provider<ToastUI> toastUIProvider;
        private Provider<TunablePadding.TunablePaddingService> tunablePaddingServiceProvider;
        private Provider<TunerActivity> tunerActivityProvider;
        private Provider<TunerServiceImpl> tunerServiceImplProvider;
        private Provider<TvNotificationPanelActivity> tvNotificationPanelActivityProvider;
        private Provider<TvNotificationPanel> tvNotificationPanelProvider;
        private Provider<TvOngoingPrivacyChip> tvOngoingPrivacyChipProvider;
        private Provider<TvStatusBar> tvStatusBarProvider;
        private Provider<TvUnblockSensorActivity> tvUnblockSensorActivityProvider;
        private Provider<TypeClassifier> typeClassifierProvider;
        private Provider<UdfpsController> udfpsControllerProvider;
        private Provider<UdfpsHapticsSimulator> udfpsHapticsSimulatorProvider;
        private Provider<UiModeNightTile> uiModeNightTileProvider;
        private Provider<UiOffloadThread> uiOffloadThreadProvider;
        private Provider<UnlockedScreenOffAnimationController> unlockedScreenOffAnimationControllerProvider;
        private Provider<UsbDebuggingActivity> usbDebuggingActivityProvider;
        private Provider<UsbDebuggingSecondaryUserActivity> usbDebuggingSecondaryUserActivityProvider;
        private Provider<UserCreator> userCreatorProvider;
        private Provider<UserSwitcherController.UserDetailAdapter> userDetailAdapterProvider;
        private Provider<UserInfoControllerImpl> userInfoControllerImplProvider;
        private Provider<UserSwitcherController> userSwitcherControllerProvider;
        private Provider<UserTile> userTileProvider;
        private Provider<VibratorHelper> vibratorHelperProvider;
        private Provider<VisualStabilityCoordinator> visualStabilityCoordinatorProvider;
        private Provider<VolumeDialogComponent> volumeDialogComponentProvider;
        private Provider<VolumeDialogControllerImpl> volumeDialogControllerImplProvider;
        private Provider<VolumeUI> volumeUIProvider;
        private Provider<WMShell> wMShellProvider;
        private Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;
        private Provider<WalletActivity> walletActivityProvider;
        private Provider<WalletControllerImpl> walletControllerImplProvider;
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

        private TvSysUIComponentImpl(DependencyProvider dependencyProvider, NightDisplayListenerModule nightDisplayListenerModule, UserModule userModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<TaskSurfaceHelper> optional11) {
            initialize(dependencyProvider, nightDisplayListenerModule, userModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11);
            initialize2(dependencyProvider, nightDisplayListenerModule, userModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11);
            initialize3(dependencyProvider, nightDisplayListenerModule, userModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11);
            initialize4(dependencyProvider, nightDisplayListenerModule, userModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11);
            initialize5(dependencyProvider, nightDisplayListenerModule, userModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11);
            initialize6(dependencyProvider, nightDisplayListenerModule, userModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11);
        }

        private NotificationSectionsFeatureManager notificationSectionsFeatureManager() {
            return new NotificationSectionsFeatureManager(this.deviceConfigProxyProvider.get(), DaggerTvGlobalRootComponent.this.context);
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
            this.provideConfigurationControllerProvider = DoubleCheck.provider(DependencyProvider_ProvideConfigurationControllerFactory.create(dependencyProvider, DaggerTvGlobalRootComponent.this.contextProvider));
            this.globalSettingsImplProvider = GlobalSettingsImpl_Factory.create(DaggerTvGlobalRootComponent.this.provideContentResolverProvider);
            this.provideDemoModeControllerProvider = DoubleCheck.provider(DemoModeModule_ProvideDemoModeControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.dumpManagerProvider, this.globalSettingsImplProvider));
            this.provideLeakDetectorProvider = DoubleCheck.provider(DependencyProvider_ProvideLeakDetectorFactory.create(dependencyProvider));
            Provider<Looper> provider2 = DoubleCheck.provider(SysUIConcurrencyModule_ProvideBgLooperFactory.create());
            this.provideBgLooperProvider = provider2;
            this.provideBgHandlerProvider = SysUIConcurrencyModule_ProvideBgHandlerFactory.create(provider2);
            this.provideUserTrackerProvider = DoubleCheck.provider(SettingsModule_ProvideUserTrackerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideUserManagerProvider, this.dumpManagerProvider, this.provideBgHandlerProvider));
            Provider<TunerServiceImpl> provider3 = DoubleCheck.provider(TunerServiceImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.provideLeakDetectorProvider, this.provideDemoModeControllerProvider, this.provideUserTrackerProvider));
            this.tunerServiceImplProvider = provider3;
            this.tunerActivityProvider = TunerActivity_Factory.create(this.provideDemoModeControllerProvider, provider3);
            this.provideBackgroundExecutorProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideBackgroundExecutorFactory.create(this.provideBgLooperProvider));
            Provider<LogcatEchoTracker> provider4 = DoubleCheck.provider(LogModule_ProvideLogcatEchoTrackerFactory.create(DaggerTvGlobalRootComponent.this.provideContentResolverProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create()));
            this.provideLogcatEchoTrackerProvider = provider4;
            Provider<LogBufferFactory> provider5 = DoubleCheck.provider(LogBufferFactory_Factory.create(this.dumpManagerProvider, provider4));
            this.logBufferFactoryProvider = provider5;
            Provider<LogBuffer> provider6 = DoubleCheck.provider(LogModule_ProvideBroadcastDispatcherLogBufferFactory.create(provider5));
            this.provideBroadcastDispatcherLogBufferProvider = provider6;
            this.broadcastDispatcherLoggerProvider = BroadcastDispatcherLogger_Factory.create(provider6);
            Provider<BroadcastDispatcher> provider7 = DoubleCheck.provider(DependencyProvider_ProvidesBroadcastDispatcherFactory.create(dependencyProvider, DaggerTvGlobalRootComponent.this.contextProvider, this.provideBgLooperProvider, this.provideBackgroundExecutorProvider, this.dumpManagerProvider, this.broadcastDispatcherLoggerProvider, this.provideUserTrackerProvider));
            this.providesBroadcastDispatcherProvider = provider7;
            this.workLockActivityProvider = WorkLockActivity_Factory.create(provider7);
            this.providePluginManagerProvider = DoubleCheck.provider(DependencyProvider_ProvidePluginManagerFactory.create(dependencyProvider, DaggerTvGlobalRootComponent.this.contextProvider));
            this.deviceConfigProxyProvider = DoubleCheck.provider(DeviceConfigProxy_Factory.create());
            this.enhancedEstimatesImplProvider = DoubleCheck.provider(EnhancedEstimatesImpl_Factory.create());
            this.provideBatteryControllerProvider = DoubleCheck.provider(TvSystemUIModule_ProvideBatteryControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.enhancedEstimatesImplProvider, DaggerTvGlobalRootComponent.this.providePowerManagerProvider, this.providesBroadcastDispatcherProvider, this.provideDemoModeControllerProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.provideBgHandlerProvider));
            this.dockManagerImplProvider = DoubleCheck.provider(DockManagerImpl_Factory.create());
            this.falsingDataProvider = DoubleCheck.provider(FalsingDataProvider_Factory.create(DaggerTvGlobalRootComponent.this.provideDisplayMetricsProvider, this.provideBatteryControllerProvider, this.dockManagerImplProvider));
            this.provideMetricsLoggerProvider = DoubleCheck.provider(DependencyProvider_ProvideMetricsLoggerFactory.create(dependencyProvider));
            DistanceClassifier_Factory create = DistanceClassifier_Factory.create(this.falsingDataProvider, this.deviceConfigProxyProvider);
            this.distanceClassifierProvider = create;
            this.proximityClassifierProvider = ProximityClassifier_Factory.create(create, this.falsingDataProvider, this.deviceConfigProxyProvider);
            this.pointerCountClassifierProvider = PointerCountClassifier_Factory.create(this.falsingDataProvider);
            this.typeClassifierProvider = TypeClassifier_Factory.create(this.falsingDataProvider);
            this.diagonalClassifierProvider = DiagonalClassifier_Factory.create(this.falsingDataProvider, this.deviceConfigProxyProvider);
            ZigZagClassifier_Factory create2 = ZigZagClassifier_Factory.create(this.falsingDataProvider, this.deviceConfigProxyProvider);
            this.zigZagClassifierProvider = create2;
            this.providesBrightLineGestureClassifiersProvider = FalsingModule_ProvidesBrightLineGestureClassifiersFactory.create(this.distanceClassifierProvider, this.proximityClassifierProvider, this.pointerCountClassifierProvider, this.typeClassifierProvider, this.diagonalClassifierProvider, create2);
            this.namedSetOfFalsingClassifierProvider = SetFactory.builder(0, 1).addCollectionProvider(this.providesBrightLineGestureClassifiersProvider).build();
            FalsingModule_ProvidesSingleTapTouchSlopFactory create3 = FalsingModule_ProvidesSingleTapTouchSlopFactory.create(DaggerTvGlobalRootComponent.this.provideViewConfigurationProvider);
            this.providesSingleTapTouchSlopProvider = create3;
            this.singleTapClassifierProvider = SingleTapClassifier_Factory.create(this.falsingDataProvider, create3);
            FalsingModule_ProvidesDoubleTapTouchSlopFactory create4 = FalsingModule_ProvidesDoubleTapTouchSlopFactory.create(DaggerTvGlobalRootComponent.this.provideResourcesProvider);
            this.providesDoubleTapTouchSlopProvider = create4;
            this.doubleTapClassifierProvider = DoubleTapClassifier_Factory.create(this.falsingDataProvider, this.singleTapClassifierProvider, create4, FalsingModule_ProvidesDoubleTapTimeoutMsFactory.create());
            Provider<SystemClock> provider8 = DoubleCheck.provider(SystemClockImpl_Factory.create());
            this.bindSystemClockProvider = provider8;
            this.historyTrackerProvider = DoubleCheck.provider(HistoryTracker_Factory.create(provider8));
            this.ringerModeTrackerImplProvider = DoubleCheck.provider(RingerModeTrackerImpl_Factory.create(DaggerTvGlobalRootComponent.this.provideAudioManagerProvider, this.providesBroadcastDispatcherProvider, this.provideBackgroundExecutorProvider));
            this.statusBarStateControllerImplProvider = DoubleCheck.provider(StatusBarStateControllerImpl_Factory.create(DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider));
            this.provideLockPatternUtilsProvider = DoubleCheck.provider(DependencyProvider_ProvideLockPatternUtilsFactory.create(dependencyProvider, DaggerTvGlobalRootComponent.this.contextProvider));
            this.protoTracerProvider = DoubleCheck.provider(ProtoTracer_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.dumpManagerProvider));
            this.commandRegistryProvider = DoubleCheck.provider(CommandRegistry_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider));
            this.provideCommandQueueProvider = DoubleCheck.provider(StatusBarDependenciesModule_ProvideCommandQueueFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.protoTracerProvider, this.commandRegistryProvider));
            this.providerLayoutInflaterProvider = DoubleCheck.provider(DependencyProvider_ProviderLayoutInflaterFactory.create(dependencyProvider, DaggerTvGlobalRootComponent.this.contextProvider));
            this.provideMainDelayableExecutorProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideMainDelayableExecutorFactory.create(GlobalConcurrencyModule_ProvideMainLooperFactory.create()));
            this.systemPropertiesHelperProvider = DoubleCheck.provider(SystemPropertiesHelper_Factory.create());
            Provider<FeatureFlagReader> provider9 = DoubleCheck.provider(FeatureFlagReader_Factory.create(DaggerTvGlobalRootComponent.this.provideResourcesProvider, DaggerTvGlobalRootComponent.this.buildInfoProvider, this.systemPropertiesHelperProvider));
            this.featureFlagReaderProvider = provider9;
            this.featureFlagsProvider = DoubleCheck.provider(FeatureFlags_Factory.create(provider9, DaggerTvGlobalRootComponent.this.contextProvider));
            this.provideNotificationListenerProvider = DoubleCheck.provider(StatusBarDependenciesModule_ProvideNotificationListenerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideNotificationManagerProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider));
            Provider<LogBuffer> provider10 = DoubleCheck.provider(LogModule_ProvideNotificationsLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNotificationsLogBufferProvider = provider10;
            this.notificationEntryManagerLoggerProvider = NotificationEntryManagerLogger_Factory.create(provider10);
            Provider<ExtensionControllerImpl> provider11 = DoubleCheck.provider(ExtensionControllerImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideLeakDetectorProvider, this.providePluginManagerProvider, this.tunerServiceImplProvider, this.provideConfigurationControllerProvider));
            this.extensionControllerImplProvider = provider11;
            this.notificationPersonExtractorPluginBoundaryProvider = DoubleCheck.provider(NotificationPersonExtractorPluginBoundary_Factory.create(provider11));
            DelegateFactory delegateFactory = new DelegateFactory();
            this.notificationGroupManagerLegacyProvider = delegateFactory;
            Provider<GroupMembershipManager> provider12 = DoubleCheck.provider(NotificationsModule_ProvideGroupMembershipManagerFactory.create(this.featureFlagsProvider, delegateFactory));
            this.provideGroupMembershipManagerProvider = provider12;
            this.peopleNotificationIdentifierImplProvider = DoubleCheck.provider(PeopleNotificationIdentifierImpl_Factory.create(this.notificationPersonExtractorPluginBoundaryProvider, provider12));
            Factory create5 = InstanceFactory.create(optional6);
            this.setBubblesProvider = create5;
            DelegateFactory.setDelegate(this.notificationGroupManagerLegacyProvider, DoubleCheck.provider(NotificationGroupManagerLegacy_Factory.create(this.statusBarStateControllerImplProvider, this.peopleNotificationIdentifierImplProvider, create5)));
            this.provideNotificationMessagingUtilProvider = DependencyProvider_ProvideNotificationMessagingUtilFactory.create(dependencyProvider, DaggerTvGlobalRootComponent.this.contextProvider);
            this.notificationClickNotifierProvider = DoubleCheck.provider(NotificationClickNotifier_Factory.create(DaggerTvGlobalRootComponent.this.provideIStatusBarServiceProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider));
            this.secureSettingsImplProvider = SecureSettingsImpl_Factory.create(DaggerTvGlobalRootComponent.this.provideContentResolverProvider);
            this.deviceProvisionedControllerImplProvider = DoubleCheck.provider(DeviceProvisionedControllerImpl_Factory.create(DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.providesBroadcastDispatcherProvider, this.globalSettingsImplProvider, this.secureSettingsImplProvider));
            this.keyguardStateControllerImplProvider = new DelegateFactory();
            this.notificationLockscreenUserManagerImplProvider = DoubleCheck.provider(NotificationLockscreenUserManagerImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.providesBroadcastDispatcherProvider, DaggerTvGlobalRootComponent.this.provideDevicePolicyManagerProvider, DaggerTvGlobalRootComponent.this.provideUserManagerProvider, this.notificationClickNotifierProvider, DaggerTvGlobalRootComponent.this.provideKeyguardManagerProvider, this.statusBarStateControllerImplProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.deviceProvisionedControllerImplProvider, this.keyguardStateControllerImplProvider));
            DelegateFactory delegateFactory2 = new DelegateFactory();
            this.provideNotificationEntryManagerProvider = delegateFactory2;
            this.provideSmartReplyControllerProvider = DoubleCheck.provider(StatusBarDependenciesModule_ProvideSmartReplyControllerFactory.create(delegateFactory2, DaggerTvGlobalRootComponent.this.provideIStatusBarServiceProvider, this.notificationClickNotifierProvider));
            this.provideStatusBarProvider = new DelegateFactory();
            this.provideHandlerProvider = DependencyProvider_ProvideHandlerFactory.create(dependencyProvider);
            this.remoteInputUriControllerProvider = DoubleCheck.provider(RemoteInputUriController_Factory.create(DaggerTvGlobalRootComponent.this.provideIStatusBarServiceProvider));
            Provider<LogBuffer> provider13 = DoubleCheck.provider(LogModule_ProvideNotifInteractionLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNotifInteractionLogBufferProvider = provider13;
            this.actionClickLoggerProvider = ActionClickLogger_Factory.create(provider13);
            this.provideNotificationRemoteInputManagerProvider = DoubleCheck.provider(StatusBarDependenciesModule_ProvideNotificationRemoteInputManagerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.notificationLockscreenUserManagerImplProvider, this.provideSmartReplyControllerProvider, this.provideNotificationEntryManagerProvider, this.provideStatusBarProvider, this.statusBarStateControllerImplProvider, this.provideHandlerProvider, this.remoteInputUriControllerProvider, this.notificationClickNotifierProvider, this.actionClickLoggerProvider));
            this.notifCollectionLoggerProvider = NotifCollectionLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.filesProvider = DoubleCheck.provider(Files_Factory.create());
            this.logBufferEulogizerProvider = DoubleCheck.provider(LogBufferEulogizer_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.dumpManagerProvider, this.bindSystemClockProvider, this.filesProvider));
            this.notifCollectionProvider = DoubleCheck.provider(NotifCollection_Factory.create(DaggerTvGlobalRootComponent.this.provideIStatusBarServiceProvider, this.bindSystemClockProvider, this.featureFlagsProvider, this.notifCollectionLoggerProvider, this.logBufferEulogizerProvider, this.dumpManagerProvider));
            this.shadeListBuilderLoggerProvider = ShadeListBuilderLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            Provider<NotificationInteractionTracker> provider14 = DoubleCheck.provider(NotificationInteractionTracker_Factory.create(this.notificationClickNotifierProvider, this.provideNotificationEntryManagerProvider));
            this.notificationInteractionTrackerProvider = provider14;
            Provider<ShadeListBuilder> provider15 = DoubleCheck.provider(ShadeListBuilder_Factory.create(this.bindSystemClockProvider, this.shadeListBuilderLoggerProvider, this.dumpManagerProvider, provider14));
            this.shadeListBuilderProvider = provider15;
            Provider<NotifPipeline> provider16 = DoubleCheck.provider(NotifPipeline_Factory.create(this.notifCollectionProvider, provider15));
            this.notifPipelineProvider = provider16;
            this.provideCommonNotifCollectionProvider = DoubleCheck.provider(NotificationsModule_ProvideCommonNotifCollectionFactory.create(this.featureFlagsProvider, provider16, this.provideNotificationEntryManagerProvider));
            NotifBindPipelineLogger_Factory create6 = NotifBindPipelineLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.notifBindPipelineLoggerProvider = create6;
            this.notifBindPipelineProvider = DoubleCheck.provider(NotifBindPipeline_Factory.create(this.provideCommonNotifCollectionProvider, create6, GlobalConcurrencyModule_ProvideMainLooperFactory.create()));
            NotifRemoteViewCacheImpl_Factory create7 = NotifRemoteViewCacheImpl_Factory.create(this.provideCommonNotifCollectionProvider);
            this.notifRemoteViewCacheImplProvider = create7;
            this.provideNotifRemoteViewCacheProvider = DoubleCheck.provider(create7);
            this.conversationNotificationManagerProvider = DoubleCheck.provider(ConversationNotificationManager_Factory.create(this.provideNotificationEntryManagerProvider, this.notificationGroupManagerLegacyProvider, DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider));
            this.conversationNotificationProcessorProvider = ConversationNotificationProcessor_Factory.create(DaggerTvGlobalRootComponent.this.provideLauncherAppsProvider, this.conversationNotificationManagerProvider);
            this.mediaFeatureFlagProvider = MediaFeatureFlag_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider);
            this.smartReplyConstantsProvider = DoubleCheck.provider(SmartReplyConstants_Factory.create(DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, DaggerTvGlobalRootComponent.this.contextProvider, this.deviceConfigProxyProvider));
            this.provideActivityManagerWrapperProvider = DoubleCheck.provider(DependencyProvider_ProvideActivityManagerWrapperFactory.create(dependencyProvider));
            this.provideDevicePolicyManagerWrapperProvider = DoubleCheck.provider(DependencyProvider_ProvideDevicePolicyManagerWrapperFactory.create(dependencyProvider));
            Provider<KeyguardDismissUtil> provider17 = DoubleCheck.provider(KeyguardDismissUtil_Factory.create());
            this.keyguardDismissUtilProvider = provider17;
            this.smartReplyInflaterImplProvider = SmartReplyInflaterImpl_Factory.create(this.smartReplyConstantsProvider, provider17, this.provideNotificationRemoteInputManagerProvider, this.provideSmartReplyControllerProvider, DaggerTvGlobalRootComponent.this.contextProvider);
            Provider<Optional<Lazy<StatusBar>>> of = PresentJdkOptionalLazyProvider.of(this.provideStatusBarProvider);
            this.optionalOfLazyOfStatusBarProvider = of;
            this.activityStarterDelegateProvider = DoubleCheck.provider(ActivityStarterDelegate_Factory.create(of));
            this.keyguardBypassControllerProvider = DoubleCheck.provider(KeyguardBypassController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.tunerServiceImplProvider, this.statusBarStateControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.keyguardStateControllerImplProvider, this.dumpManagerProvider));
            Provider<HeadsUpManagerPhone> provider18 = DoubleCheck.provider(TvSystemUIModule_ProvideHeadsUpManagerPhoneFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.statusBarStateControllerImplProvider, this.keyguardBypassControllerProvider, this.notificationGroupManagerLegacyProvider, this.provideConfigurationControllerProvider));
            this.provideHeadsUpManagerPhoneProvider = provider18;
            this.smartActionInflaterImplProvider = SmartActionInflaterImpl_Factory.create(this.smartReplyConstantsProvider, this.activityStarterDelegateProvider, this.provideSmartReplyControllerProvider, provider18);
        }

        private void initialize2(DependencyProvider dependencyProvider, NightDisplayListenerModule nightDisplayListenerModule, UserModule userModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<TaskSurfaceHelper> optional11) {
            SmartReplyStateInflaterImpl_Factory create = SmartReplyStateInflaterImpl_Factory.create(this.smartReplyConstantsProvider, this.provideActivityManagerWrapperProvider, DaggerTvGlobalRootComponent.this.providePackageManagerWrapperProvider, this.provideDevicePolicyManagerWrapperProvider, this.smartReplyInflaterImplProvider, this.smartActionInflaterImplProvider);
            this.smartReplyStateInflaterImplProvider = create;
            this.notificationContentInflaterProvider = DoubleCheck.provider(NotificationContentInflater_Factory.create(this.provideNotifRemoteViewCacheProvider, this.provideNotificationRemoteInputManagerProvider, this.conversationNotificationProcessorProvider, this.mediaFeatureFlagProvider, this.provideBackgroundExecutorProvider, create));
            this.notifInflationErrorManagerProvider = DoubleCheck.provider(NotifInflationErrorManager_Factory.create());
            RowContentBindStageLogger_Factory create2 = RowContentBindStageLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.rowContentBindStageLoggerProvider = create2;
            this.rowContentBindStageProvider = DoubleCheck.provider(RowContentBindStage_Factory.create(this.notificationContentInflaterProvider, this.notifInflationErrorManagerProvider, create2));
            this.expandableNotificationRowComponentBuilderProvider = new Provider<ExpandableNotificationRowComponent.Builder>() { // from class: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentImpl.1
                @Override // javax.inject.Provider
                public ExpandableNotificationRowComponent.Builder get() {
                    return new ExpandableNotificationRowComponentBuilder();
                }
            };
            this.iconBuilderProvider = IconBuilder_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider);
            this.iconManagerProvider = IconManager_Factory.create(this.provideCommonNotifCollectionProvider, DaggerTvGlobalRootComponent.this.provideLauncherAppsProvider, this.iconBuilderProvider);
            this.lowPriorityInflationHelperProvider = DoubleCheck.provider(LowPriorityInflationHelper_Factory.create(this.featureFlagsProvider, this.notificationGroupManagerLegacyProvider, this.rowContentBindStageProvider));
            this.notificationRowBinderImplProvider = DoubleCheck.provider(NotificationRowBinderImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideNotificationMessagingUtilProvider, this.provideNotificationRemoteInputManagerProvider, this.notificationLockscreenUserManagerImplProvider, this.notifBindPipelineProvider, this.rowContentBindStageProvider, RowInflaterTask_Factory.create(), this.expandableNotificationRowComponentBuilderProvider, this.iconManagerProvider, this.lowPriorityInflationHelperProvider));
            Provider<ForegroundServiceDismissalFeatureController> provider = DoubleCheck.provider(ForegroundServiceDismissalFeatureController_Factory.create(this.deviceConfigProxyProvider, DaggerTvGlobalRootComponent.this.contextProvider));
            this.foregroundServiceDismissalFeatureControllerProvider = provider;
            DelegateFactory.setDelegate(this.provideNotificationEntryManagerProvider, DoubleCheck.provider(NotificationsModule_ProvideNotificationEntryManagerFactory.create(this.notificationEntryManagerLoggerProvider, this.notificationGroupManagerLegacyProvider, this.featureFlagsProvider, this.notificationRowBinderImplProvider, this.provideNotificationRemoteInputManagerProvider, this.provideLeakDetectorProvider, provider, DaggerTvGlobalRootComponent.this.provideIStatusBarServiceProvider)));
            this.provideAmbientDisplayConfigurationProvider = DependencyProvider_ProvideAmbientDisplayConfigurationFactory.create(dependencyProvider, DaggerTvGlobalRootComponent.this.contextProvider);
            this.provideAlwaysOnDisplayPolicyProvider = DoubleCheck.provider(DependencyProvider_ProvideAlwaysOnDisplayPolicyFactory.create(dependencyProvider, DaggerTvGlobalRootComponent.this.contextProvider));
            this.wakefulnessLifecycleProvider = DoubleCheck.provider(WakefulnessLifecycle_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, FrameworkServicesModule_ProvideIWallPaperManagerFactory.create()));
            this.falsingManagerProxyProvider = new DelegateFactory();
            this.keyguardUpdateMonitorProvider = new DelegateFactory();
            this.asyncSensorManagerProvider = DoubleCheck.provider(AsyncSensorManager_Factory.create(DaggerTvGlobalRootComponent.this.providesSensorManagerProvider, ThreadFactoryImpl_Factory.create(), this.providePluginManagerProvider));
            this.builderProvider = ThresholdSensorImpl_Builder_Factory.create(DaggerTvGlobalRootComponent.this.provideResourcesProvider, this.asyncSensorManagerProvider, DaggerTvGlobalRootComponent.this.provideExecutionProvider);
            this.providePrimaryProxSensorProvider = SensorModule_ProvidePrimaryProxSensorFactory.create(DaggerTvGlobalRootComponent.this.providesSensorManagerProvider, this.builderProvider);
            SensorModule_ProvideSecondaryProxSensorFactory create3 = SensorModule_ProvideSecondaryProxSensorFactory.create(this.builderProvider);
            this.provideSecondaryProxSensorProvider = create3;
            ProximitySensor_Factory create4 = ProximitySensor_Factory.create(this.providePrimaryProxSensorProvider, create3, this.provideMainDelayableExecutorProvider, DaggerTvGlobalRootComponent.this.provideExecutionProvider);
            this.proximitySensorProvider = create4;
            this.falsingCollectorImplProvider = DoubleCheck.provider(FalsingCollectorImpl_Factory.create(this.falsingDataProvider, this.falsingManagerProxyProvider, this.keyguardUpdateMonitorProvider, this.historyTrackerProvider, create4, this.statusBarStateControllerImplProvider, this.keyguardStateControllerImplProvider, this.provideBatteryControllerProvider, this.dockManagerImplProvider, this.provideMainDelayableExecutorProvider, this.bindSystemClockProvider));
            DelegateFactory delegateFactory = new DelegateFactory();
            this.newKeyguardViewMediatorProvider = delegateFactory;
            this.providesViewMediatorCallbackProvider = DependencyProvider_ProvidesViewMediatorCallbackFactory.create(dependencyProvider, delegateFactory);
            this.provideUiBackgroundExecutorProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideUiBackgroundExecutorFactory.create());
            this.navigationModeControllerProvider = DoubleCheck.provider(NavigationModeController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.deviceProvisionedControllerImplProvider, this.provideConfigurationControllerProvider, this.provideUiBackgroundExecutorProvider));
            this.notificationShadeWindowControllerImplProvider = new DelegateFactory();
            this.systemSettingsImplProvider = SystemSettingsImpl_Factory.create(DaggerTvGlobalRootComponent.this.provideContentResolverProvider);
            this.provideFaceAuthScreenBrightnessControllerProvider = DoubleCheck.provider(KeyguardModule_ProvideFaceAuthScreenBrightnessControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.notificationShadeWindowControllerImplProvider, DaggerTvGlobalRootComponent.this.provideResourcesProvider, this.provideHandlerProvider, DaggerTvGlobalRootComponent.this.provideFaceManagerProvider, DaggerTvGlobalRootComponent.this.providePackageManagerProvider, this.keyguardUpdateMonitorProvider, this.globalSettingsImplProvider, this.systemSettingsImplProvider, this.dumpManagerProvider));
            this.provideNotificationMediaManagerProvider = new DelegateFactory();
            this.dismissCallbackRegistryProvider = DoubleCheck.provider(DismissCallbackRegistry_Factory.create(this.provideUiBackgroundExecutorProvider));
            this.keyguardSecurityModelProvider = DoubleCheck.provider(KeyguardSecurityModel_Factory.create(DaggerTvGlobalRootComponent.this.provideResourcesProvider, this.provideLockPatternUtilsProvider, this.keyguardUpdateMonitorProvider));
            this.keyguardBouncerComponentFactoryProvider = new Provider<KeyguardBouncerComponent.Factory>() { // from class: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentImpl.2
                @Override // javax.inject.Provider
                public KeyguardBouncerComponent.Factory get() {
                    return new KeyguardBouncerComponentFactory();
                }
            };
            this.factoryProvider = KeyguardBouncer_Factory_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.providesViewMediatorCallbackProvider, this.dismissCallbackRegistryProvider, this.falsingCollectorImplProvider, this.keyguardStateControllerImplProvider, this.keyguardUpdateMonitorProvider, this.keyguardBypassControllerProvider, this.provideHandlerProvider, this.keyguardSecurityModelProvider, this.keyguardBouncerComponentFactoryProvider);
            this.unlockedScreenOffAnimationControllerProvider = new DelegateFactory();
            this.factoryProvider2 = KeyguardMessageAreaController_Factory_Factory.create(this.keyguardUpdateMonitorProvider, this.provideConfigurationControllerProvider);
            this.statusBarKeyguardViewManagerProvider = DoubleCheck.provider(StatusBarKeyguardViewManager_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.providesViewMediatorCallbackProvider, this.provideLockPatternUtilsProvider, this.statusBarStateControllerImplProvider, this.provideConfigurationControllerProvider, this.keyguardUpdateMonitorProvider, this.navigationModeControllerProvider, this.dockManagerImplProvider, this.notificationShadeWindowControllerImplProvider, this.keyguardStateControllerImplProvider, this.provideFaceAuthScreenBrightnessControllerProvider, this.provideNotificationMediaManagerProvider, this.factoryProvider, this.wakefulnessLifecycleProvider, this.unlockedScreenOffAnimationControllerProvider, this.factoryProvider2));
            this.telephonyListenerManagerProvider = DoubleCheck.provider(TelephonyListenerManager_Factory.create(DaggerTvGlobalRootComponent.this.provideTelephonyManagerProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider, TelephonyCallback_Factory.create()));
            this.userSwitcherControllerProvider = new DelegateFactory();
            this.adapterProvider = UserDetailView_Adapter_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.userSwitcherControllerProvider, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider, this.falsingManagerProxyProvider);
            this.userDetailAdapterProvider = UserSwitcherController_UserDetailAdapter_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.adapterProvider);
            DelegateFactory.setDelegate(this.userSwitcherControllerProvider, DoubleCheck.provider(UserSwitcherController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideUserManagerProvider, this.provideUserTrackerProvider, this.keyguardStateControllerImplProvider, this.deviceProvisionedControllerImplProvider, DaggerTvGlobalRootComponent.this.provideDevicePolicyManagerProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.activityStarterDelegateProvider, this.providesBroadcastDispatcherProvider, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider, this.falsingManagerProxyProvider, this.telephonyListenerManagerProvider, DaggerTvGlobalRootComponent.this.provideIActivityTaskManagerProvider, this.userDetailAdapterProvider, this.secureSettingsImplProvider, this.provideBackgroundExecutorProvider)));
            this.provideAssistUtilsProvider = DoubleCheck.provider(AssistModule_ProvideAssistUtilsFactory.create(DaggerTvGlobalRootComponent.this.contextProvider));
            this.phoneStateMonitorProvider = DoubleCheck.provider(PhoneStateMonitor_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.providesBroadcastDispatcherProvider, this.optionalOfLazyOfStatusBarProvider, this.bootCompleteCacheImplProvider));
            this.provideNavigationBarControllerProvider = new DelegateFactory();
            this.provideSysUiStateProvider = DoubleCheck.provider(SystemUIModule_ProvideSysUiStateFactory.create());
            this.setPipProvider = InstanceFactory.create(optional);
            this.setLegacySplitScreenProvider = InstanceFactory.create(optional2);
            this.setSplitScreenProvider = InstanceFactory.create(optional3);
            this.setOneHandedProvider = InstanceFactory.create(optional5);
            this.setTransitionsProvider = InstanceFactory.create(shellTransitions);
            this.setStartingSurfaceProvider = InstanceFactory.create(optional10);
            this.provideSmartspaceTransitionControllerProvider = DoubleCheck.provider(SystemUIModule_ProvideSmartspaceTransitionControllerFactory.create());
            this.overviewProxyServiceProvider = DoubleCheck.provider(OverviewProxyService_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider, this.provideNavigationBarControllerProvider, this.navigationModeControllerProvider, this.notificationShadeWindowControllerImplProvider, this.provideSysUiStateProvider, this.setPipProvider, this.setLegacySplitScreenProvider, this.setSplitScreenProvider, this.optionalOfLazyOfStatusBarProvider, this.setOneHandedProvider, this.providesBroadcastDispatcherProvider, this.setTransitionsProvider, this.setStartingSurfaceProvider, this.provideSmartspaceTransitionControllerProvider));
            this.assistLoggerProvider = DoubleCheck.provider(AssistLogger_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider, this.provideAssistUtilsProvider, this.phoneStateMonitorProvider));
            this.defaultUiControllerProvider = DoubleCheck.provider(DefaultUiController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.assistLoggerProvider));
            this.assistManagerProvider = DoubleCheck.provider(AssistManager_Factory.create(this.deviceProvisionedControllerImplProvider, DaggerTvGlobalRootComponent.this.contextProvider, this.provideAssistUtilsProvider, this.provideCommandQueueProvider, this.phoneStateMonitorProvider, this.overviewProxyServiceProvider, this.provideConfigurationControllerProvider, this.provideSysUiStateProvider, this.defaultUiControllerProvider, this.assistLoggerProvider));
            this.accessibilityManagerWrapperProvider = DoubleCheck.provider(AccessibilityManagerWrapper_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider));
            this.accessibilityButtonModeObserverProvider = DoubleCheck.provider(AccessibilityButtonModeObserver_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider));
            this.contextComponentResolverProvider = new DelegateFactory();
            this.provideRecentsImplProvider = RecentsModule_ProvideRecentsImplFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.contextComponentResolverProvider);
            Provider<Recents> provider2 = DoubleCheck.provider(TvSystemUIModule_ProvideRecentsFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideRecentsImplProvider, this.provideCommandQueueProvider));
            this.provideRecentsProvider = provider2;
            this.optionalOfRecentsProvider = PresentJdkOptionalInstanceProvider.of(provider2);
            this.shadeControllerImplProvider = DoubleCheck.provider(ShadeControllerImpl_Factory.create(this.provideCommandQueueProvider, this.statusBarStateControllerImplProvider, this.notificationShadeWindowControllerImplProvider, this.statusBarKeyguardViewManagerProvider, DaggerTvGlobalRootComponent.this.provideWindowManagerProvider, this.provideStatusBarProvider, this.assistManagerProvider, this.setBubblesProvider));
            this.blurUtilsProvider = DoubleCheck.provider(BlurUtils_Factory.create(DaggerTvGlobalRootComponent.this.provideResourcesProvider, DaggerTvGlobalRootComponent.this.provideCrossWindowBlurListenersProvider, this.dumpManagerProvider));
            this.dozeParametersProvider = new DelegateFactory();
            Provider<LogBuffer> provider3 = DoubleCheck.provider(LogModule_ProvideDozeLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideDozeLogBufferProvider = provider3;
            DozeLogger_Factory create5 = DozeLogger_Factory.create(provider3);
            this.dozeLoggerProvider = create5;
            Provider<DozeLog> provider4 = DoubleCheck.provider(DozeLog_Factory.create(this.keyguardUpdateMonitorProvider, this.dumpManagerProvider, create5));
            this.dozeLogProvider = provider4;
            this.dozeScrimControllerProvider = DoubleCheck.provider(DozeScrimController_Factory.create(this.dozeParametersProvider, provider4));
            this.darkIconDispatcherImplProvider = DoubleCheck.provider(DarkIconDispatcherImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider));
            this.lightBarControllerProvider = DoubleCheck.provider(LightBarController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.darkIconDispatcherImplProvider, this.provideBatteryControllerProvider, this.navigationModeControllerProvider));
            this.builderProvider2 = DelayedWakeLock_Builder_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider);
            this.scrimControllerProvider = DoubleCheck.provider(ScrimController_Factory.create(this.lightBarControllerProvider, this.dozeParametersProvider, DaggerTvGlobalRootComponent.this.provideAlarmManagerProvider, this.keyguardStateControllerImplProvider, this.builderProvider2, this.provideHandlerProvider, this.keyguardUpdateMonitorProvider, this.dockManagerImplProvider, this.provideConfigurationControllerProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider, this.unlockedScreenOffAnimationControllerProvider));
            this.screenLifecycleProvider = DoubleCheck.provider(ScreenLifecycle_Factory.create());
            this.authControllerProvider = new DelegateFactory();
            this.biometricUnlockControllerProvider = DoubleCheck.provider(BiometricUnlockController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.dozeScrimControllerProvider, this.newKeyguardViewMediatorProvider, this.scrimControllerProvider, this.shadeControllerImplProvider, this.notificationShadeWindowControllerImplProvider, this.keyguardStateControllerImplProvider, this.provideHandlerProvider, this.keyguardUpdateMonitorProvider, DaggerTvGlobalRootComponent.this.provideResourcesProvider, this.keyguardBypassControllerProvider, this.dozeParametersProvider, this.provideMetricsLoggerProvider, this.dumpManagerProvider, DaggerTvGlobalRootComponent.this.providePowerManagerProvider, this.provideNotificationMediaManagerProvider, this.wakefulnessLifecycleProvider, this.screenLifecycleProvider, this.authControllerProvider));
            Provider<Choreographer> provider5 = DoubleCheck.provider(DependencyProvider_ProvidesChoreographerFactory.create(dependencyProvider));
            this.providesChoreographerProvider = provider5;
            this.notificationShadeDepthControllerProvider = DoubleCheck.provider(NotificationShadeDepthController_Factory.create(this.statusBarStateControllerImplProvider, this.blurUtilsProvider, this.biometricUnlockControllerProvider, this.keyguardStateControllerImplProvider, provider5, DaggerTvGlobalRootComponent.this.provideWallpaperManagerProvider, this.notificationShadeWindowControllerImplProvider, this.dozeParametersProvider, this.dumpManagerProvider));
            this.systemActionsProvider = DoubleCheck.provider(SystemActions_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.notificationShadeWindowControllerImplProvider, this.provideStatusBarProvider, this.provideRecentsProvider));
            this.navigationBarOverlayControllerProvider = DoubleCheck.provider(NavigationBarOverlayController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider));
            DelegateFactory.setDelegate(this.provideNavigationBarControllerProvider, DoubleCheck.provider(DependencyProvider_ProvideNavigationBarControllerFactory.create(dependencyProvider, DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideWindowManagerProvider, this.assistManagerProvider, DaggerTvGlobalRootComponent.this.provideAccessibilityManagerProvider, this.accessibilityManagerWrapperProvider, this.deviceProvisionedControllerImplProvider, this.provideMetricsLoggerProvider, this.overviewProxyServiceProvider, this.navigationModeControllerProvider, this.accessibilityButtonModeObserverProvider, this.statusBarStateControllerImplProvider, this.provideSysUiStateProvider, this.providesBroadcastDispatcherProvider, this.provideCommandQueueProvider, this.setPipProvider, this.setLegacySplitScreenProvider, this.optionalOfRecentsProvider, this.provideStatusBarProvider, this.shadeControllerImplProvider, this.provideNotificationRemoteInputManagerProvider, this.notificationShadeDepthControllerProvider, this.systemActionsProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider, this.navigationBarOverlayControllerProvider, this.provideConfigurationControllerProvider, this.provideUserTrackerProvider)));
            this.keyguardStatusViewComponentFactoryProvider = new Provider<KeyguardStatusViewComponent.Factory>() { // from class: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentImpl.3
                @Override // javax.inject.Provider
                public KeyguardStatusViewComponent.Factory get() {
                    return new KeyguardStatusViewComponentFactory();
                }
            };
            this.keyguardDisplayManagerProvider = KeyguardDisplayManager_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideNavigationBarControllerProvider, this.keyguardStatusViewComponentFactoryProvider, this.provideUiBackgroundExecutorProvider);
            this.keyguardUnlockAnimationControllerProvider = DoubleCheck.provider(KeyguardUnlockAnimationController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.keyguardStateControllerImplProvider, this.newKeyguardViewMediatorProvider, this.statusBarKeyguardViewManagerProvider, this.provideSmartspaceTransitionControllerProvider, this.featureFlagsProvider));
            DelegateFactory.setDelegate(this.newKeyguardViewMediatorProvider, DoubleCheck.provider(KeyguardModule_NewKeyguardViewMediatorFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.falsingCollectorImplProvider, this.provideLockPatternUtilsProvider, this.providesBroadcastDispatcherProvider, this.statusBarKeyguardViewManagerProvider, this.dismissCallbackRegistryProvider, this.keyguardUpdateMonitorProvider, this.dumpManagerProvider, DaggerTvGlobalRootComponent.this.providePowerManagerProvider, DaggerTvGlobalRootComponent.this.provideTrustManagerProvider, this.userSwitcherControllerProvider, this.provideUiBackgroundExecutorProvider, this.deviceConfigProxyProvider, this.navigationModeControllerProvider, this.keyguardDisplayManagerProvider, this.dozeParametersProvider, this.statusBarStateControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardUnlockAnimationControllerProvider, this.unlockedScreenOffAnimationControllerProvider, this.notificationShadeDepthControllerProvider)));
            DelegateFactory.setDelegate(this.unlockedScreenOffAnimationControllerProvider, DoubleCheck.provider(UnlockedScreenOffAnimationController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.wakefulnessLifecycleProvider, this.statusBarStateControllerImplProvider, this.newKeyguardViewMediatorProvider, this.keyguardStateControllerImplProvider, this.dozeParametersProvider)));
            DelegateFactory.setDelegate(this.dozeParametersProvider, DoubleCheck.provider(DozeParameters_Factory.create(DaggerTvGlobalRootComponent.this.provideResourcesProvider, this.provideAmbientDisplayConfigurationProvider, this.provideAlwaysOnDisplayPolicyProvider, DaggerTvGlobalRootComponent.this.providePowerManagerProvider, this.provideBatteryControllerProvider, this.tunerServiceImplProvider, this.dumpManagerProvider, this.featureFlagsProvider, this.unlockedScreenOffAnimationControllerProvider)));
            this.sysuiColorExtractorProvider = DoubleCheck.provider(SysuiColorExtractor_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideConfigurationControllerProvider));
            DelegateFactory.setDelegate(this.notificationShadeWindowControllerImplProvider, DoubleCheck.provider(NotificationShadeWindowControllerImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideWindowManagerProvider, DaggerTvGlobalRootComponent.this.provideIActivityManagerProvider, this.dozeParametersProvider, this.statusBarStateControllerImplProvider, this.provideConfigurationControllerProvider, this.newKeyguardViewMediatorProvider, this.keyguardBypassControllerProvider, this.sysuiColorExtractorProvider, this.dumpManagerProvider, this.keyguardStateControllerImplProvider, this.unlockedScreenOffAnimationControllerProvider, this.authControllerProvider)));
            this.mediaArtworkProcessorProvider = DoubleCheck.provider(MediaArtworkProcessor_Factory.create());
            MediaControllerFactory_Factory create6 = MediaControllerFactory_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider);
            this.mediaControllerFactoryProvider = create6;
            this.mediaTimeoutListenerProvider = DoubleCheck.provider(MediaTimeoutListener_Factory.create(create6, this.provideMainDelayableExecutorProvider));
            this.mediaBrowserFactoryProvider = MediaBrowserFactory_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider);
            this.resumeMediaBrowserFactoryProvider = ResumeMediaBrowserFactory_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.mediaBrowserFactoryProvider);
            this.mediaResumeListenerProvider = DoubleCheck.provider(MediaResumeListener_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.providesBroadcastDispatcherProvider, this.provideBackgroundExecutorProvider, this.tunerServiceImplProvider, this.resumeMediaBrowserFactoryProvider, this.dumpManagerProvider));
            this.mediaSessionBasedFilterProvider = MediaSessionBasedFilter_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideMediaSessionManagerProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider, this.provideBackgroundExecutorProvider);
            this.provideLocalBluetoothControllerProvider = DoubleCheck.provider(DependencyProvider_ProvideLocalBluetoothControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideBgHandlerProvider));
            this.localMediaManagerFactoryProvider = LocalMediaManagerFactory_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideLocalBluetoothControllerProvider);
        }

        private void initialize3(DependencyProvider dependencyProvider, NightDisplayListenerModule nightDisplayListenerModule, UserModule userModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<TaskSurfaceHelper> optional11) {
            this.mediaDeviceManagerProvider = MediaDeviceManager_Factory.create(this.mediaControllerFactoryProvider, this.localMediaManagerFactoryProvider, DaggerTvGlobalRootComponent.this.provideMediaRouter2ManagerProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.dumpManagerProvider);
            this.mediaDataFilterProvider = MediaDataFilter_Factory.create(this.providesBroadcastDispatcherProvider, this.mediaResumeListenerProvider, this.notificationLockscreenUserManagerImplProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider, this.bindSystemClockProvider);
            this.mediaDataManagerProvider = DoubleCheck.provider(MediaDataManager_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideBackgroundExecutorProvider, this.provideMainDelayableExecutorProvider, this.mediaControllerFactoryProvider, this.dumpManagerProvider, this.providesBroadcastDispatcherProvider, this.mediaTimeoutListenerProvider, this.mediaResumeListenerProvider, this.mediaSessionBasedFilterProvider, this.mediaDeviceManagerProvider, MediaDataCombineLatest_Factory.create(), this.mediaDataFilterProvider, this.activityStarterDelegateProvider, SmartspaceMediaDataProvider_Factory.create(), this.bindSystemClockProvider, this.tunerServiceImplProvider));
            DelegateFactory.setDelegate(this.provideNotificationMediaManagerProvider, DoubleCheck.provider(StatusBarDependenciesModule_ProvideNotificationMediaManagerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideStatusBarProvider, this.notificationShadeWindowControllerImplProvider, this.provideNotificationEntryManagerProvider, this.mediaArtworkProcessorProvider, this.keyguardBypassControllerProvider, this.notifPipelineProvider, this.notifCollectionProvider, this.featureFlagsProvider, this.provideMainDelayableExecutorProvider, this.deviceConfigProxyProvider, this.mediaDataManagerProvider)));
            this.keyguardEnvironmentImplProvider = DoubleCheck.provider(KeyguardEnvironmentImpl_Factory.create());
            this.provideIndividualSensorPrivacyControllerProvider = DoubleCheck.provider(TvSystemUIModule_ProvideIndividualSensorPrivacyControllerFactory.create(DaggerTvGlobalRootComponent.this.provideSensorPrivacyManagerProvider));
            Provider<AppOpsControllerImpl> provider = DoubleCheck.provider(AppOpsControllerImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideBgLooperProvider, this.dumpManagerProvider, DaggerTvGlobalRootComponent.this.provideAudioManagerProvider, this.provideIndividualSensorPrivacyControllerProvider, this.providesBroadcastDispatcherProvider, this.bindSystemClockProvider));
            this.appOpsControllerImplProvider = provider;
            Provider<ForegroundServiceController> provider2 = DoubleCheck.provider(ForegroundServiceController_Factory.create(provider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider));
            this.foregroundServiceControllerProvider = provider2;
            this.notificationFilterProvider = DoubleCheck.provider(NotificationFilter_Factory.create(this.statusBarStateControllerImplProvider, this.keyguardEnvironmentImplProvider, provider2, this.notificationLockscreenUserManagerImplProvider, this.mediaFeatureFlagProvider));
            this.notificationSectionsFeatureManagerProvider = NotificationSectionsFeatureManager_Factory.create(this.deviceConfigProxyProvider, DaggerTvGlobalRootComponent.this.contextProvider);
            Provider<HighPriorityProvider> provider3 = DoubleCheck.provider(HighPriorityProvider_Factory.create(this.peopleNotificationIdentifierImplProvider, this.provideGroupMembershipManagerProvider));
            this.highPriorityProvider = provider3;
            this.notificationRankingManagerProvider = NotificationRankingManager_Factory.create(this.provideNotificationMediaManagerProvider, this.notificationGroupManagerLegacyProvider, this.provideHeadsUpManagerPhoneProvider, this.notificationFilterProvider, this.notificationEntryManagerLoggerProvider, this.notificationSectionsFeatureManagerProvider, this.peopleNotificationIdentifierImplProvider, provider3, this.keyguardEnvironmentImplProvider);
            this.targetSdkResolverProvider = DoubleCheck.provider(TargetSdkResolver_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider));
            GroupCoalescerLogger_Factory create = GroupCoalescerLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.groupCoalescerLoggerProvider = create;
            this.groupCoalescerProvider = GroupCoalescer_Factory.create(this.provideMainDelayableExecutorProvider, this.bindSystemClockProvider, create);
            SharedCoordinatorLogger_Factory create2 = SharedCoordinatorLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.sharedCoordinatorLoggerProvider = create2;
            this.hideNotifsForOtherUsersCoordinatorProvider = HideNotifsForOtherUsersCoordinator_Factory.create(this.notificationLockscreenUserManagerImplProvider, create2);
            this.keyguardCoordinatorProvider = DoubleCheck.provider(KeyguardCoordinator_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideHandlerProvider, this.keyguardStateControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.providesBroadcastDispatcherProvider, this.statusBarStateControllerImplProvider, this.keyguardUpdateMonitorProvider, this.highPriorityProvider));
            AnonymousClass4 r1 = new Provider<SectionHeaderControllerSubcomponent.Builder>() { // from class: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentImpl.4
                @Override // javax.inject.Provider
                public SectionHeaderControllerSubcomponent.Builder get() {
                    return new SectionHeaderControllerSubcomponentBuilder();
                }
            };
            this.sectionHeaderControllerSubcomponentBuilderProvider = r1;
            Provider<SectionHeaderControllerSubcomponent> provider4 = DoubleCheck.provider(NotificationSectionHeadersModule_ProvidesAlertingHeaderSubcomponentFactory.create(r1));
            this.providesAlertingHeaderSubcomponentProvider = provider4;
            this.providesAlertingHeaderNodeControllerProvider = NotificationSectionHeadersModule_ProvidesAlertingHeaderNodeControllerFactory.create(provider4);
            Provider<SectionHeaderControllerSubcomponent> provider5 = DoubleCheck.provider(NotificationSectionHeadersModule_ProvidesSilentHeaderSubcomponentFactory.create(this.sectionHeaderControllerSubcomponentBuilderProvider));
            this.providesSilentHeaderSubcomponentProvider = provider5;
            NotificationSectionHeadersModule_ProvidesSilentHeaderNodeControllerFactory create3 = NotificationSectionHeadersModule_ProvidesSilentHeaderNodeControllerFactory.create(provider5);
            this.providesSilentHeaderNodeControllerProvider = create3;
            this.rankingCoordinatorProvider = DoubleCheck.provider(RankingCoordinator_Factory.create(this.statusBarStateControllerImplProvider, this.highPriorityProvider, this.providesAlertingHeaderNodeControllerProvider, create3));
            this.appOpsCoordinatorProvider = DoubleCheck.provider(AppOpsCoordinator_Factory.create(this.foregroundServiceControllerProvider, this.appOpsControllerImplProvider, this.provideMainDelayableExecutorProvider));
            this.deviceProvisionedCoordinatorProvider = DoubleCheck.provider(DeviceProvisionedCoordinator_Factory.create(this.deviceProvisionedControllerImplProvider, DaggerTvGlobalRootComponent.this.provideIPackageManagerProvider));
            this.provideINotificationManagerProvider = DoubleCheck.provider(DependencyProvider_ProvideINotificationManagerFactory.create(dependencyProvider));
            this.notificationInterruptStateProviderImplProvider = DoubleCheck.provider(NotificationInterruptStateProviderImpl_Factory.create(DaggerTvGlobalRootComponent.this.provideContentResolverProvider, DaggerTvGlobalRootComponent.this.providePowerManagerProvider, DaggerTvGlobalRootComponent.this.provideIDreamManagerProvider, this.provideAmbientDisplayConfigurationProvider, this.notificationFilterProvider, this.provideBatteryControllerProvider, this.statusBarStateControllerImplProvider, this.provideHeadsUpManagerPhoneProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider));
            this.zenModeControllerImplProvider = DoubleCheck.provider(ZenModeControllerImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.providesBroadcastDispatcherProvider));
            Provider<Optional<BubblesManager>> provider6 = DoubleCheck.provider(SystemUIModule_ProvideBubblesManagerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.setBubblesProvider, this.notificationShadeWindowControllerImplProvider, this.statusBarStateControllerImplProvider, this.shadeControllerImplProvider, this.provideConfigurationControllerProvider, DaggerTvGlobalRootComponent.this.provideIStatusBarServiceProvider, this.provideINotificationManagerProvider, this.notificationInterruptStateProviderImplProvider, this.zenModeControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.notificationGroupManagerLegacyProvider, this.provideNotificationEntryManagerProvider, this.notifPipelineProvider, this.provideSysUiStateProvider, this.featureFlagsProvider, this.dumpManagerProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider));
            this.provideBubblesManagerProvider = provider6;
            this.bubbleCoordinatorProvider = DoubleCheck.provider(BubbleCoordinator_Factory.create(provider6, this.setBubblesProvider, this.notifCollectionProvider));
            this.headsUpViewBinderProvider = DoubleCheck.provider(HeadsUpViewBinder_Factory.create(this.provideNotificationMessagingUtilProvider, this.rowContentBindStageProvider));
            Provider<SectionHeaderControllerSubcomponent> provider7 = DoubleCheck.provider(NotificationSectionHeadersModule_ProvidesIncomingHeaderSubcomponentFactory.create(this.sectionHeaderControllerSubcomponentBuilderProvider));
            this.providesIncomingHeaderSubcomponentProvider = provider7;
            NotificationSectionHeadersModule_ProvidesIncomingHeaderNodeControllerFactory create4 = NotificationSectionHeadersModule_ProvidesIncomingHeaderNodeControllerFactory.create(provider7);
            this.providesIncomingHeaderNodeControllerProvider = create4;
            this.headsUpCoordinatorProvider = DoubleCheck.provider(HeadsUpCoordinator_Factory.create(this.provideHeadsUpManagerPhoneProvider, this.headsUpViewBinderProvider, this.notificationInterruptStateProviderImplProvider, this.provideNotificationRemoteInputManagerProvider, create4));
            Provider<SectionHeaderControllerSubcomponent> provider8 = DoubleCheck.provider(NotificationSectionHeadersModule_ProvidesPeopleHeaderSubcomponentFactory.create(this.sectionHeaderControllerSubcomponentBuilderProvider));
            this.providesPeopleHeaderSubcomponentProvider = provider8;
            NotificationSectionHeadersModule_ProvidesPeopleHeaderNodeControllerFactory create5 = NotificationSectionHeadersModule_ProvidesPeopleHeaderNodeControllerFactory.create(provider8);
            this.providesPeopleHeaderNodeControllerProvider = create5;
            this.conversationCoordinatorProvider = DoubleCheck.provider(ConversationCoordinator_Factory.create(this.peopleNotificationIdentifierImplProvider, create5));
            this.preparationCoordinatorLoggerProvider = PreparationCoordinatorLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.notifInflaterImplProvider = DoubleCheck.provider(NotifInflaterImpl_Factory.create(DaggerTvGlobalRootComponent.this.provideIStatusBarServiceProvider, this.notifCollectionProvider, this.notifInflationErrorManagerProvider, this.notifPipelineProvider));
            Provider<NotifViewBarn> provider9 = DoubleCheck.provider(NotifViewBarn_Factory.create());
            this.notifViewBarnProvider = provider9;
            this.preparationCoordinatorProvider = DoubleCheck.provider(PreparationCoordinator_Factory.create(this.preparationCoordinatorLoggerProvider, this.notifInflaterImplProvider, this.notifInflationErrorManagerProvider, provider9, DaggerTvGlobalRootComponent.this.provideIStatusBarServiceProvider));
            this.mediaCoordinatorProvider = MediaCoordinator_Factory.create(this.mediaFeatureFlagProvider);
            this.optionalOfBcSmartspaceDataPluginProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            Provider<LockscreenSmartspaceController> provider10 = DoubleCheck.provider(LockscreenSmartspaceController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.featureFlagsProvider, DaggerTvGlobalRootComponent.this.provideSmartspaceManagerProvider, this.activityStarterDelegateProvider, this.falsingManagerProxyProvider, this.secureSettingsImplProvider, this.provideUserTrackerProvider, DaggerTvGlobalRootComponent.this.provideContentResolverProvider, this.provideConfigurationControllerProvider, this.statusBarStateControllerImplProvider, DaggerTvGlobalRootComponent.this.provideExecutionProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.optionalOfBcSmartspaceDataPluginProvider));
            this.lockscreenSmartspaceControllerProvider = provider10;
            this.smartspaceDedupingCoordinatorProvider = DoubleCheck.provider(SmartspaceDedupingCoordinator_Factory.create(this.statusBarStateControllerImplProvider, provider10, this.provideNotificationEntryManagerProvider, this.notificationLockscreenUserManagerImplProvider, this.notifPipelineProvider, this.provideMainDelayableExecutorProvider, this.bindSystemClockProvider));
            Provider<DelayableExecutor> provider11 = DoubleCheck.provider(SysUIConcurrencyModule_ProvideDelayableExecutorFactory.create(this.provideBgLooperProvider));
            this.provideDelayableExecutorProvider = provider11;
            Provider<VisualStabilityCoordinator> provider12 = DoubleCheck.provider(VisualStabilityCoordinator_Factory.create(this.provideHeadsUpManagerPhoneProvider, this.wakefulnessLifecycleProvider, this.statusBarStateControllerImplProvider, provider11));
            this.visualStabilityCoordinatorProvider = provider12;
            this.notifCoordinatorsProvider = DoubleCheck.provider(NotifCoordinators_Factory.create(this.dumpManagerProvider, this.featureFlagsProvider, this.hideNotifsForOtherUsersCoordinatorProvider, this.keyguardCoordinatorProvider, this.rankingCoordinatorProvider, this.appOpsCoordinatorProvider, this.deviceProvisionedCoordinatorProvider, this.bubbleCoordinatorProvider, this.headsUpCoordinatorProvider, this.conversationCoordinatorProvider, this.preparationCoordinatorProvider, this.mediaCoordinatorProvider, this.smartspaceDedupingCoordinatorProvider, provider12));
            this.shadeViewDifferLoggerProvider = ShadeViewDifferLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.notificationWakeUpCoordinatorProvider = DoubleCheck.provider(NotificationWakeUpCoordinator_Factory.create(this.provideHeadsUpManagerPhoneProvider, this.statusBarStateControllerImplProvider, this.keyguardBypassControllerProvider, this.dozeParametersProvider, this.unlockedScreenOffAnimationControllerProvider));
            AnonymousClass5 r12 = new Provider<InjectionInflationController.ViewInstanceCreator.Factory>() { // from class: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentImpl.5
                @Override // javax.inject.Provider
                public InjectionInflationController.ViewInstanceCreator.Factory get() {
                    return new ViewInstanceCreatorFactory();
                }
            };
            this.createViewInstanceCreatorFactoryProvider = r12;
            this.injectionInflationControllerProvider = DoubleCheck.provider(InjectionInflationController_Factory.create(r12));
            this.notificationShelfComponentBuilderProvider = new Provider<NotificationShelfComponent.Builder>() { // from class: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentImpl.6
                @Override // javax.inject.Provider
                public NotificationShelfComponent.Builder get() {
                    return new NotificationShelfComponentBuilder();
                }
            };
            this.superStatusBarViewFactoryProvider = DoubleCheck.provider(SuperStatusBarViewFactory_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.injectionInflationControllerProvider, this.notificationShelfComponentBuilderProvider));
            this.statusBarContentInsetsProvider = DoubleCheck.provider(StatusBarContentInsetsProvider_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideConfigurationControllerProvider, DaggerTvGlobalRootComponent.this.provideWindowManagerProvider, this.dumpManagerProvider));
            this.statusBarWindowControllerProvider = DoubleCheck.provider(StatusBarWindowController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideWindowManagerProvider, DaggerTvGlobalRootComponent.this.provideIWindowManagerProvider, this.superStatusBarViewFactoryProvider, this.statusBarContentInsetsProvider, DaggerTvGlobalRootComponent.this.provideResourcesProvider));
            this.notificationIconAreaControllerProvider = DoubleCheck.provider(NotificationIconAreaController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.statusBarStateControllerImplProvider, this.notificationWakeUpCoordinatorProvider, this.keyguardBypassControllerProvider, this.provideNotificationMediaManagerProvider, this.provideNotificationListenerProvider, this.dozeParametersProvider, this.setBubblesProvider, this.provideDemoModeControllerProvider, this.darkIconDispatcherImplProvider, this.statusBarWindowControllerProvider, this.unlockedScreenOffAnimationControllerProvider));
            ShadeViewManagerFactory_Factory create6 = ShadeViewManagerFactory_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.shadeViewDifferLoggerProvider, this.notifViewBarnProvider, this.notificationIconAreaControllerProvider);
            this.shadeViewManagerFactoryProvider = create6;
            this.notifPipelineInitializerProvider = DoubleCheck.provider(NotifPipelineInitializer_Factory.create(this.notifPipelineProvider, this.groupCoalescerProvider, this.notifCollectionProvider, this.shadeListBuilderProvider, this.notifCoordinatorsProvider, this.notifInflaterImplProvider, this.dumpManagerProvider, create6, this.featureFlagsProvider));
            this.notifBindPipelineInitializerProvider = NotifBindPipelineInitializer_Factory.create(this.notifBindPipelineProvider, this.rowContentBindStageProvider);
            this.provideNotificationGroupAlertTransferHelperProvider = DoubleCheck.provider(StatusBarPhoneDependenciesModule_ProvideNotificationGroupAlertTransferHelperFactory.create(this.rowContentBindStageProvider));
            Provider<VisualStabilityManager> provider13 = DoubleCheck.provider(NotificationsModule_ProvideVisualStabilityManagerFactory.create(this.featureFlagsProvider, this.provideNotificationEntryManagerProvider, this.provideHandlerProvider, this.statusBarStateControllerImplProvider, this.wakefulnessLifecycleProvider));
            this.provideVisualStabilityManagerProvider = provider13;
            this.headsUpControllerProvider = DoubleCheck.provider(HeadsUpController_Factory.create(this.headsUpViewBinderProvider, this.notificationInterruptStateProviderImplProvider, this.provideHeadsUpManagerPhoneProvider, this.provideNotificationRemoteInputManagerProvider, this.statusBarStateControllerImplProvider, provider13, this.provideNotificationListenerProvider));
            NotificationClickerLogger_Factory create7 = NotificationClickerLogger_Factory.create(this.provideNotifInteractionLogBufferProvider);
            this.notificationClickerLoggerProvider = create7;
            this.builderProvider3 = NotificationClicker_Builder_Factory.create(create7);
            this.animatedImageNotificationManagerProvider = DoubleCheck.provider(AnimatedImageNotificationManager_Factory.create(this.provideNotificationEntryManagerProvider, this.provideHeadsUpManagerPhoneProvider, this.statusBarStateControllerImplProvider));
            Provider<PeopleSpaceWidgetManager> provider14 = DoubleCheck.provider(PeopleSpaceWidgetManager_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideLauncherAppsProvider, this.provideNotificationEntryManagerProvider, DaggerTvGlobalRootComponent.this.providePackageManagerProvider, this.setBubblesProvider, DaggerTvGlobalRootComponent.this.provideUserManagerProvider, DaggerTvGlobalRootComponent.this.provideNotificationManagerProvider, this.providesBroadcastDispatcherProvider, this.provideBackgroundExecutorProvider));
            this.peopleSpaceWidgetManagerProvider = provider14;
            this.notificationsControllerImplProvider = DoubleCheck.provider(NotificationsControllerImpl_Factory.create(this.featureFlagsProvider, this.provideNotificationListenerProvider, this.provideNotificationEntryManagerProvider, this.notificationRankingManagerProvider, this.notifPipelineProvider, this.targetSdkResolverProvider, this.notifPipelineInitializerProvider, this.notifBindPipelineInitializerProvider, this.deviceProvisionedControllerImplProvider, this.notificationRowBinderImplProvider, this.remoteInputUriControllerProvider, this.notificationGroupManagerLegacyProvider, this.provideNotificationGroupAlertTransferHelperProvider, this.provideHeadsUpManagerPhoneProvider, this.headsUpControllerProvider, this.headsUpViewBinderProvider, this.builderProvider3, this.animatedImageNotificationManagerProvider, provider14));
            this.notificationsControllerStubProvider = NotificationsControllerStub_Factory.create(this.provideNotificationListenerProvider);
            this.provideNotificationsControllerProvider = DoubleCheck.provider(NotificationsModule_ProvideNotificationsControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.notificationsControllerImplProvider, this.notificationsControllerStubProvider));
            this.provideAutoHideControllerProvider = DoubleCheck.provider(DependencyProvider_ProvideAutoHideControllerFactory.create(dependencyProvider, DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, DaggerTvGlobalRootComponent.this.provideIWindowManagerProvider));
            this.statusBarIconControllerImplProvider = DoubleCheck.provider(StatusBarIconControllerImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider, this.provideDemoModeControllerProvider));
            this.carrierConfigTrackerProvider = DoubleCheck.provider(CarrierConfigTracker_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider));
            this.callbackHandlerProvider = CallbackHandler_Factory.create(GlobalConcurrencyModule_ProvideMainLooperFactory.create());
            this.wifiPickerTrackerFactoryProvider = DoubleCheck.provider(AccessPointControllerImpl_WifiPickerTrackerFactory_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideWifiManagerProvider, DaggerTvGlobalRootComponent.this.provideConnectivityManagagerProvider, DaggerTvGlobalRootComponent.this.provideNetworkScoreManagerProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.provideBgHandlerProvider));
            this.provideAccessPointControllerImplProvider = DoubleCheck.provider(StatusBarPolicyModule_ProvideAccessPointControllerImplFactory.create(DaggerTvGlobalRootComponent.this.provideUserManagerProvider, this.provideUserTrackerProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider, this.wifiPickerTrackerFactoryProvider));
            this.networkControllerImplProvider = DoubleCheck.provider(NetworkControllerImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideBgLooperProvider, this.provideBackgroundExecutorProvider, DaggerTvGlobalRootComponent.this.provideSubcriptionManagerProvider, this.callbackHandlerProvider, this.deviceProvisionedControllerImplProvider, this.providesBroadcastDispatcherProvider, DaggerTvGlobalRootComponent.this.provideConnectivityManagagerProvider, DaggerTvGlobalRootComponent.this.provideTelephonyManagerProvider, this.telephonyListenerManagerProvider, DaggerTvGlobalRootComponent.this.provideWifiManagerProvider, DaggerTvGlobalRootComponent.this.provideNetworkScoreManagerProvider, this.provideAccessPointControllerImplProvider, this.provideDemoModeControllerProvider, this.carrierConfigTrackerProvider, this.featureFlagsProvider, this.dumpManagerProvider));
            this.securityControllerImplProvider = DoubleCheck.provider(SecurityControllerImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideBgHandlerProvider, this.providesBroadcastDispatcherProvider, this.provideBackgroundExecutorProvider));
            this.statusBarSignalPolicyProvider = DoubleCheck.provider(StatusBarSignalPolicy_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.statusBarIconControllerImplProvider, this.carrierConfigTrackerProvider, this.networkControllerImplProvider, this.securityControllerImplProvider, this.tunerServiceImplProvider, this.featureFlagsProvider));
            this.notificationRoundnessManagerProvider = DoubleCheck.provider(NotificationRoundnessManager_Factory.create(this.keyguardBypassControllerProvider, this.notificationSectionsFeatureManagerProvider));
            this.lockscreenGestureLoggerProvider = DoubleCheck.provider(LockscreenGestureLogger_Factory.create());
            this.mediaHostStatesManagerProvider = DoubleCheck.provider(MediaHostStatesManager_Factory.create());
            this.mediaViewControllerProvider = MediaViewController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideConfigurationControllerProvider, this.mediaHostStatesManagerProvider);
            Provider<DelayableExecutor> provider15 = DoubleCheck.provider(SysUIConcurrencyModule_ProvideBackgroundDelayableExecutorFactory.create(this.provideBgLooperProvider));
            this.provideBackgroundDelayableExecutorProvider = provider15;
            Provider<RepeatableExecutor> provider16 = DoubleCheck.provider(SysUIConcurrencyModule_ProvideBackgroundRepeatableExecutorFactory.create(provider15));
            this.provideBackgroundRepeatableExecutorProvider = provider16;
            this.seekBarViewModelProvider = SeekBarViewModel_Factory.create(provider16);
            this.mediaOutputDialogFactoryProvider = MediaOutputDialogFactory_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideMediaSessionManagerProvider, this.provideLocalBluetoothControllerProvider, this.shadeControllerImplProvider, this.activityStarterDelegateProvider, this.provideNotificationEntryManagerProvider, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider);
            this.mediaCarouselControllerProvider = new DelegateFactory();
            this.mediaControlPanelProvider = MediaControlPanel_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideBackgroundExecutorProvider, this.activityStarterDelegateProvider, this.mediaViewControllerProvider, this.seekBarViewModelProvider, this.mediaDataManagerProvider, this.keyguardDismissUtilProvider, this.mediaOutputDialogFactoryProvider, this.mediaCarouselControllerProvider);
            DelegateFactory.setDelegate(this.mediaCarouselControllerProvider, DoubleCheck.provider(MediaCarouselController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.mediaControlPanelProvider, this.provideVisualStabilityManagerProvider, this.mediaHostStatesManagerProvider, this.activityStarterDelegateProvider, this.bindSystemClockProvider, this.provideMainDelayableExecutorProvider, this.mediaDataManagerProvider, this.provideConfigurationControllerProvider, this.falsingCollectorImplProvider, this.falsingManagerProxyProvider, this.dumpManagerProvider)));
            this.mediaHierarchyManagerProvider = DoubleCheck.provider(MediaHierarchyManager_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.statusBarStateControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardBypassControllerProvider, this.mediaCarouselControllerProvider, this.notificationLockscreenUserManagerImplProvider, this.provideConfigurationControllerProvider, this.wakefulnessLifecycleProvider, this.statusBarKeyguardViewManagerProvider));
            Provider<MediaHost> provider17 = DoubleCheck.provider(MediaModule_ProvidesKeyguardMediaHostFactory.create(MediaHost_MediaHostStateHolder_Factory.create(), this.mediaHierarchyManagerProvider, this.mediaDataManagerProvider, this.mediaHostStatesManagerProvider));
            this.providesKeyguardMediaHostProvider = provider17;
            this.keyguardMediaControllerProvider = DoubleCheck.provider(KeyguardMediaController_Factory.create(provider17, this.keyguardBypassControllerProvider, this.statusBarStateControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.featureFlagsProvider, DaggerTvGlobalRootComponent.this.contextProvider, this.provideConfigurationControllerProvider));
            Provider<LogBuffer> provider18 = DoubleCheck.provider(LogModule_ProvideNotificationSectionLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNotificationSectionLogBufferProvider = provider18;
            this.notificationSectionsLoggerProvider = DoubleCheck.provider(NotificationSectionsLogger_Factory.create(provider18));
            this.providesIncomingHeaderControllerProvider = NotificationSectionHeadersModule_ProvidesIncomingHeaderControllerFactory.create(this.providesIncomingHeaderSubcomponentProvider);
            this.providesPeopleHeaderControllerProvider = NotificationSectionHeadersModule_ProvidesPeopleHeaderControllerFactory.create(this.providesPeopleHeaderSubcomponentProvider);
            this.providesAlertingHeaderControllerProvider = NotificationSectionHeadersModule_ProvidesAlertingHeaderControllerFactory.create(this.providesAlertingHeaderSubcomponentProvider);
            this.providesSilentHeaderControllerProvider = NotificationSectionHeadersModule_ProvidesSilentHeaderControllerFactory.create(this.providesSilentHeaderSubcomponentProvider);
        }

        private void initialize4(DependencyProvider dependencyProvider, NightDisplayListenerModule nightDisplayListenerModule, UserModule userModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<TaskSurfaceHelper> optional11) {
            this.notificationSectionsManagerProvider = NotificationSectionsManager_Factory.create(this.statusBarStateControllerImplProvider, this.provideConfigurationControllerProvider, this.keyguardMediaControllerProvider, this.notificationSectionsFeatureManagerProvider, this.notificationSectionsLoggerProvider, this.providesIncomingHeaderControllerProvider, this.providesPeopleHeaderControllerProvider, this.providesAlertingHeaderControllerProvider, this.providesSilentHeaderControllerProvider);
            Provider<AmbientState> provider = DoubleCheck.provider(AmbientState_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.notificationSectionsManagerProvider, this.keyguardBypassControllerProvider));
            this.ambientStateProvider = provider;
            this.lockscreenShadeTransitionControllerProvider = DoubleCheck.provider(LockscreenShadeTransitionController_Factory.create(this.statusBarStateControllerImplProvider, this.lockscreenGestureLoggerProvider, this.keyguardBypassControllerProvider, this.notificationLockscreenUserManagerImplProvider, this.falsingCollectorImplProvider, provider, DaggerTvGlobalRootComponent.this.provideDisplayMetricsProvider, this.mediaHierarchyManagerProvider, this.scrimControllerProvider, this.notificationShadeDepthControllerProvider, this.featureFlagsProvider, DaggerTvGlobalRootComponent.this.contextProvider, this.provideConfigurationControllerProvider, this.falsingManagerProxyProvider));
            this.pulseExpansionHandlerProvider = DoubleCheck.provider(PulseExpansionHandler_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.notificationWakeUpCoordinatorProvider, this.keyguardBypassControllerProvider, this.provideHeadsUpManagerPhoneProvider, this.notificationRoundnessManagerProvider, this.provideConfigurationControllerProvider, this.statusBarStateControllerImplProvider, this.falsingManagerProxyProvider, this.lockscreenShadeTransitionControllerProvider, this.falsingCollectorImplProvider));
            this.dynamicPrivacyControllerProvider = DoubleCheck.provider(DynamicPrivacyController_Factory.create(this.notificationLockscreenUserManagerImplProvider, this.keyguardStateControllerImplProvider, this.statusBarStateControllerImplProvider));
            this.bypassHeadsUpNotifierProvider = DoubleCheck.provider(BypassHeadsUpNotifier_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.keyguardBypassControllerProvider, this.statusBarStateControllerImplProvider, this.provideHeadsUpManagerPhoneProvider, this.notificationLockscreenUserManagerImplProvider, this.provideNotificationMediaManagerProvider, this.provideNotificationEntryManagerProvider, this.tunerServiceImplProvider));
            this.remoteInputQuickSettingsDisablerProvider = DoubleCheck.provider(RemoteInputQuickSettingsDisabler_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideConfigurationControllerProvider, this.provideCommandQueueProvider));
            this.channelEditorDialogControllerProvider = DoubleCheck.provider(ChannelEditorDialogController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideINotificationManagerProvider, ChannelEditorDialog_Builder_Factory.create()));
            this.assistantFeedbackControllerProvider = DoubleCheck.provider(AssistantFeedbackController_Factory.create(DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, DaggerTvGlobalRootComponent.this.contextProvider, this.deviceConfigProxyProvider));
            this.provideOnUserInteractionCallbackProvider = DoubleCheck.provider(NotificationsModule_ProvideOnUserInteractionCallbackFactory.create(this.featureFlagsProvider, this.provideHeadsUpManagerPhoneProvider, this.statusBarStateControllerImplProvider, this.notifPipelineProvider, this.notifCollectionProvider, this.visualStabilityCoordinatorProvider, this.provideNotificationEntryManagerProvider, this.provideVisualStabilityManagerProvider, this.provideGroupMembershipManagerProvider));
            this.provideNotificationGutsManagerProvider = DoubleCheck.provider(NotificationsModule_ProvideNotificationGutsManagerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideStatusBarProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.provideBgHandlerProvider, DaggerTvGlobalRootComponent.this.provideAccessibilityManagerProvider, this.highPriorityProvider, this.provideINotificationManagerProvider, this.provideNotificationEntryManagerProvider, this.peopleSpaceWidgetManagerProvider, DaggerTvGlobalRootComponent.this.provideLauncherAppsProvider, DaggerTvGlobalRootComponent.this.provideShortcutManagerProvider, this.channelEditorDialogControllerProvider, this.provideUserTrackerProvider, this.assistantFeedbackControllerProvider, this.provideBubblesManagerProvider, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider, this.provideOnUserInteractionCallbackProvider, this.shadeControllerImplProvider));
            this.expansionStateLoggerProvider = NotificationLogger_ExpansionStateLogger_Factory.create(this.provideUiBackgroundExecutorProvider);
            Provider<NotificationPanelLogger> provider2 = DoubleCheck.provider(NotificationsModule_ProvideNotificationPanelLoggerFactory.create());
            this.provideNotificationPanelLoggerProvider = provider2;
            this.provideNotificationLoggerProvider = DoubleCheck.provider(NotificationsModule_ProvideNotificationLoggerFactory.create(this.provideNotificationListenerProvider, this.provideUiBackgroundExecutorProvider, this.provideNotificationEntryManagerProvider, this.statusBarStateControllerImplProvider, this.expansionStateLoggerProvider, provider2));
            this.foregroundServiceSectionControllerProvider = DoubleCheck.provider(ForegroundServiceSectionController_Factory.create(this.provideNotificationEntryManagerProvider, this.foregroundServiceDismissalFeatureControllerProvider));
            this.dynamicChildBindControllerProvider = DynamicChildBindController_Factory.create(this.rowContentBindStageProvider);
            this.provideNotificationViewHierarchyManagerProvider = DoubleCheck.provider(StatusBarDependenciesModule_ProvideNotificationViewHierarchyManagerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.notificationLockscreenUserManagerImplProvider, this.notificationGroupManagerLegacyProvider, this.provideVisualStabilityManagerProvider, this.statusBarStateControllerImplProvider, this.provideNotificationEntryManagerProvider, this.keyguardBypassControllerProvider, this.setBubblesProvider, this.dynamicPrivacyControllerProvider, this.foregroundServiceSectionControllerProvider, this.dynamicChildBindControllerProvider, this.lowPriorityInflationHelperProvider, this.assistantFeedbackControllerProvider));
            this.vibratorHelperProvider = DoubleCheck.provider(VibratorHelper_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider));
            this.accessibilityButtonTargetsObserverProvider = DoubleCheck.provider(AccessibilityButtonTargetsObserver_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider));
            this.provideAccessibilityFloatingMenuControllerProvider = DoubleCheck.provider(DependencyProvider_ProvideAccessibilityFloatingMenuControllerFactory.create(dependencyProvider, DaggerTvGlobalRootComponent.this.contextProvider, this.accessibilityButtonTargetsObserverProvider, this.accessibilityButtonModeObserverProvider, this.keyguardUpdateMonitorProvider));
            this.provideKeyguardLiftControllerProvider = DoubleCheck.provider(KeyguardModule_ProvideKeyguardLiftControllerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.statusBarStateControllerImplProvider, this.asyncSensorManagerProvider, this.keyguardUpdateMonitorProvider, this.dumpManagerProvider));
            this.lockscreenWallpaperProvider = DoubleCheck.provider(LockscreenWallpaper_Factory.create(DaggerTvGlobalRootComponent.this.provideWallpaperManagerProvider, FrameworkServicesModule_ProvideIWallPaperManagerFactory.create(), this.keyguardUpdateMonitorProvider, this.dumpManagerProvider, this.provideNotificationMediaManagerProvider, this.provideFaceAuthScreenBrightnessControllerProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider));
            this.dozeServiceHostProvider = DoubleCheck.provider(DozeServiceHost_Factory.create(this.dozeLogProvider, DaggerTvGlobalRootComponent.this.providePowerManagerProvider, this.wakefulnessLifecycleProvider, this.statusBarStateControllerImplProvider, this.deviceProvisionedControllerImplProvider, this.provideHeadsUpManagerPhoneProvider, this.provideBatteryControllerProvider, this.scrimControllerProvider, this.biometricUnlockControllerProvider, this.newKeyguardViewMediatorProvider, this.assistManagerProvider, this.dozeScrimControllerProvider, this.keyguardUpdateMonitorProvider, this.pulseExpansionHandlerProvider, this.notificationShadeWindowControllerImplProvider, this.notificationWakeUpCoordinatorProvider, this.authControllerProvider, this.notificationIconAreaControllerProvider));
            this.screenPinningRequestProvider = ScreenPinningRequest_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.optionalOfLazyOfStatusBarProvider);
            this.volumeDialogControllerImplProvider = DoubleCheck.provider(VolumeDialogControllerImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.providesBroadcastDispatcherProvider, this.ringerModeTrackerImplProvider, ThreadFactoryImpl_Factory.create(), DaggerTvGlobalRootComponent.this.provideAudioManagerProvider, DaggerTvGlobalRootComponent.this.provideNotificationManagerProvider, DaggerTvGlobalRootComponent.this.provideOptionalVibratorProvider, DaggerTvGlobalRootComponent.this.provideIAudioServiceProvider, DaggerTvGlobalRootComponent.this.provideAccessibilityManagerProvider, DaggerTvGlobalRootComponent.this.providePackageManagerProvider, this.wakefulnessLifecycleProvider));
            this.volumeDialogComponentProvider = DoubleCheck.provider(VolumeDialogComponent_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.newKeyguardViewMediatorProvider, this.volumeDialogControllerImplProvider, this.provideDemoModeControllerProvider));
            this.statusBarComponentBuilderProvider = new Provider<StatusBarComponent.Builder>() { // from class: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentImpl.7
                @Override // javax.inject.Provider
                public StatusBarComponent.Builder get() {
                    return new StatusBarComponentBuilder();
                }
            };
            this.lightsOutNotifControllerProvider = DoubleCheck.provider(LightsOutNotifController_Factory.create(DaggerTvGlobalRootComponent.this.provideWindowManagerProvider, this.provideNotificationEntryManagerProvider, this.provideCommandQueueProvider));
            this.provideGroupExpansionManagerProvider = DoubleCheck.provider(NotificationsModule_ProvideGroupExpansionManagerFactory.create(this.featureFlagsProvider, this.provideGroupMembershipManagerProvider, this.notificationGroupManagerLegacyProvider));
            this.statusBarRemoteInputCallbackProvider = DoubleCheck.provider(StatusBarRemoteInputCallback_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideGroupExpansionManagerProvider, this.notificationLockscreenUserManagerImplProvider, this.keyguardStateControllerImplProvider, this.statusBarStateControllerImplProvider, this.statusBarKeyguardViewManagerProvider, this.activityStarterDelegateProvider, this.shadeControllerImplProvider, this.provideCommandQueueProvider, this.actionClickLoggerProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider));
            this.activityIntentHelperProvider = DoubleCheck.provider(ActivityIntentHelper_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider));
            this.statusBarNotificationActivityStarterLoggerProvider = StatusBarNotificationActivityStarterLogger_Factory.create(this.provideNotifInteractionLogBufferProvider);
            this.builderProvider4 = DoubleCheck.provider(StatusBarNotificationActivityStarter_Builder_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.provideUiBackgroundExecutorProvider, this.provideNotificationEntryManagerProvider, this.notifPipelineProvider, this.provideHeadsUpManagerPhoneProvider, this.activityStarterDelegateProvider, this.notificationClickNotifierProvider, this.statusBarStateControllerImplProvider, this.statusBarKeyguardViewManagerProvider, DaggerTvGlobalRootComponent.this.provideKeyguardManagerProvider, DaggerTvGlobalRootComponent.this.provideIDreamManagerProvider, this.provideBubblesManagerProvider, this.assistManagerProvider, this.provideNotificationRemoteInputManagerProvider, this.provideGroupMembershipManagerProvider, this.notificationLockscreenUserManagerImplProvider, this.shadeControllerImplProvider, this.keyguardStateControllerImplProvider, this.notificationInterruptStateProviderImplProvider, this.provideLockPatternUtilsProvider, this.statusBarRemoteInputCallbackProvider, this.activityIntentHelperProvider, this.featureFlagsProvider, this.provideMetricsLoggerProvider, this.statusBarNotificationActivityStarterLoggerProvider, this.provideOnUserInteractionCallbackProvider));
            this.initControllerProvider = DoubleCheck.provider(InitController_Factory.create());
            this.provideTimeTickHandlerProvider = DoubleCheck.provider(DependencyProvider_ProvideTimeTickHandlerFactory.create(dependencyProvider));
            this.pluginDependencyProvider = DoubleCheck.provider(PluginDependencyProvider_Factory.create(this.providePluginManagerProvider));
            this.userInfoControllerImplProvider = DoubleCheck.provider(UserInfoControllerImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider));
            this.castControllerImplProvider = DoubleCheck.provider(CastControllerImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.dumpManagerProvider));
            this.hotspotControllerImplProvider = DoubleCheck.provider(HotspotControllerImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.provideBgHandlerProvider));
            this.bluetoothControllerImplProvider = DoubleCheck.provider(BluetoothControllerImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.dumpManagerProvider, this.provideBgLooperProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.provideLocalBluetoothControllerProvider));
            this.nextAlarmControllerImplProvider = DoubleCheck.provider(NextAlarmControllerImpl_Factory.create(DaggerTvGlobalRootComponent.this.provideAlarmManagerProvider, this.providesBroadcastDispatcherProvider, this.dumpManagerProvider));
            this.rotationLockControllerImplProvider = DoubleCheck.provider(RotationLockControllerImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider));
            this.provideDataSaverControllerProvider = DoubleCheck.provider(DependencyProvider_ProvideDataSaverControllerFactory.create(dependencyProvider, this.networkControllerImplProvider));
            this.locationControllerImplProvider = DoubleCheck.provider(LocationControllerImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.appOpsControllerImplProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.provideBgHandlerProvider, this.providesBroadcastDispatcherProvider, this.bootCompleteCacheImplProvider, this.provideUserTrackerProvider));
            this.provideSensorPrivacyControllerProvider = DoubleCheck.provider(TvSystemUIModule_ProvideSensorPrivacyControllerFactory.create(DaggerTvGlobalRootComponent.this.provideSensorPrivacyManagerProvider));
            this.recordingControllerProvider = DoubleCheck.provider(RecordingController_Factory.create(this.providesBroadcastDispatcherProvider));
            this.provideSharePreferencesProvider = DependencyProvider_ProvideSharePreferencesFactory.create(dependencyProvider, DaggerTvGlobalRootComponent.this.contextProvider);
            this.dateFormatUtilProvider = DateFormatUtil_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider);
            Provider<LogBuffer> provider3 = DoubleCheck.provider(LogModule_ProvidePrivacyLogBufferFactory.create(this.logBufferFactoryProvider));
            this.providePrivacyLogBufferProvider = provider3;
            PrivacyLogger_Factory create = PrivacyLogger_Factory.create(provider3);
            this.privacyLoggerProvider = create;
            this.privacyItemControllerProvider = DoubleCheck.provider(PrivacyItemController_Factory.create(this.appOpsControllerImplProvider, this.provideMainDelayableExecutorProvider, this.provideBackgroundDelayableExecutorProvider, this.deviceConfigProxyProvider, this.provideUserTrackerProvider, create, this.bindSystemClockProvider, this.dumpManagerProvider));
            this.phoneStatusBarPolicyProvider = PhoneStatusBarPolicy_Factory.create(this.statusBarIconControllerImplProvider, this.provideCommandQueueProvider, this.providesBroadcastDispatcherProvider, this.provideUiBackgroundExecutorProvider, DaggerTvGlobalRootComponent.this.provideResourcesProvider, this.castControllerImplProvider, this.hotspotControllerImplProvider, this.bluetoothControllerImplProvider, this.nextAlarmControllerImplProvider, this.userInfoControllerImplProvider, this.rotationLockControllerImplProvider, this.provideDataSaverControllerProvider, this.zenModeControllerImplProvider, this.deviceProvisionedControllerImplProvider, this.keyguardStateControllerImplProvider, this.locationControllerImplProvider, this.provideSensorPrivacyControllerProvider, DaggerTvGlobalRootComponent.this.provideIActivityManagerProvider, DaggerTvGlobalRootComponent.this.provideAlarmManagerProvider, DaggerTvGlobalRootComponent.this.provideUserManagerProvider, this.recordingControllerProvider, DaggerTvGlobalRootComponent.this.provideTelecomManagerProvider, DaggerTvGlobalRootComponent.this.provideDisplayIdProvider, this.provideSharePreferencesProvider, this.dateFormatUtilProvider, this.ringerModeTrackerImplProvider, this.privacyItemControllerProvider, this.privacyLoggerProvider);
            this.builderProvider5 = WakeLock_Builder_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider);
            this.keyguardIndicationControllerProvider = DoubleCheck.provider(KeyguardIndicationController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.builderProvider5, this.keyguardStateControllerImplProvider, this.statusBarStateControllerImplProvider, this.keyguardUpdateMonitorProvider, this.dockManagerImplProvider, this.providesBroadcastDispatcherProvider, DaggerTvGlobalRootComponent.this.provideDevicePolicyManagerProvider, DaggerTvGlobalRootComponent.this.provideIBatteryStatsProvider, DaggerTvGlobalRootComponent.this.provideUserManagerProvider, this.provideMainDelayableExecutorProvider, this.falsingManagerProxyProvider, this.provideLockPatternUtilsProvider, DaggerTvGlobalRootComponent.this.provideIActivityManagerProvider, this.keyguardBypassControllerProvider));
            this.statusBarTouchableRegionManagerProvider = DoubleCheck.provider(StatusBarTouchableRegionManager_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.notificationShadeWindowControllerImplProvider, this.provideConfigurationControllerProvider, this.provideHeadsUpManagerPhoneProvider));
            this.factoryProvider3 = new DelegateFactory();
            this.wiredChargingRippleControllerProvider = DoubleCheck.provider(WiredChargingRippleController_Factory.create(this.commandRegistryProvider, this.provideBatteryControllerProvider, this.provideConfigurationControllerProvider, this.featureFlagsProvider, DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideWindowManagerProvider, this.bindSystemClockProvider, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider));
            this.ongoingCallLoggerProvider = DoubleCheck.provider(OngoingCallLogger_Factory.create(DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider));
            this.provideOngoingCallControllerProvider = DoubleCheck.provider(StatusBarDependenciesModule_ProvideOngoingCallControllerFactory.create(this.provideCommonNotifCollectionProvider, this.featureFlagsProvider, this.bindSystemClockProvider, this.activityStarterDelegateProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider, DaggerTvGlobalRootComponent.this.provideIActivityManagerProvider, this.ongoingCallLoggerProvider));
            this.systemEventCoordinatorProvider = DoubleCheck.provider(SystemEventCoordinator_Factory.create(this.bindSystemClockProvider, this.provideBatteryControllerProvider, this.privacyItemControllerProvider, DaggerTvGlobalRootComponent.this.contextProvider));
            this.statusBarLocationPublisherProvider = DoubleCheck.provider(StatusBarLocationPublisher_Factory.create());
            SystemEventChipAnimationController_Factory create2 = SystemEventChipAnimationController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.superStatusBarViewFactoryProvider, this.statusBarWindowControllerProvider, this.statusBarLocationPublisherProvider);
            this.systemEventChipAnimationControllerProvider = create2;
            this.systemStatusAnimationSchedulerProvider = DoubleCheck.provider(SystemStatusAnimationScheduler_Factory.create(this.systemEventCoordinatorProvider, create2, this.statusBarWindowControllerProvider, this.dumpManagerProvider, this.bindSystemClockProvider, this.provideMainDelayableExecutorProvider));
            DelegateFactory.setDelegate(this.provideStatusBarProvider, DoubleCheck.provider(StatusBarPhoneModule_ProvideStatusBarFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideNotificationsControllerProvider, this.lightBarControllerProvider, this.provideAutoHideControllerProvider, this.keyguardUpdateMonitorProvider, this.statusBarSignalPolicyProvider, this.pulseExpansionHandlerProvider, this.notificationWakeUpCoordinatorProvider, this.keyguardBypassControllerProvider, this.keyguardStateControllerImplProvider, this.provideHeadsUpManagerPhoneProvider, this.dynamicPrivacyControllerProvider, this.bypassHeadsUpNotifierProvider, this.falsingManagerProxyProvider, this.falsingCollectorImplProvider, this.providesBroadcastDispatcherProvider, this.remoteInputQuickSettingsDisablerProvider, this.provideNotificationGutsManagerProvider, this.provideNotificationLoggerProvider, this.notificationInterruptStateProviderImplProvider, this.provideNotificationViewHierarchyManagerProvider, this.newKeyguardViewMediatorProvider, DaggerTvGlobalRootComponent.this.provideDisplayMetricsProvider, this.provideMetricsLoggerProvider, this.provideUiBackgroundExecutorProvider, this.provideNotificationMediaManagerProvider, this.notificationLockscreenUserManagerImplProvider, this.provideNotificationRemoteInputManagerProvider, this.userSwitcherControllerProvider, this.networkControllerImplProvider, this.provideBatteryControllerProvider, this.sysuiColorExtractorProvider, this.screenLifecycleProvider, this.wakefulnessLifecycleProvider, this.statusBarStateControllerImplProvider, this.vibratorHelperProvider, this.provideBubblesManagerProvider, this.setBubblesProvider, this.provideVisualStabilityManagerProvider, this.deviceProvisionedControllerImplProvider, this.provideNavigationBarControllerProvider, this.provideAccessibilityFloatingMenuControllerProvider, this.assistManagerProvider, this.provideConfigurationControllerProvider, this.notificationShadeWindowControllerImplProvider, this.dozeParametersProvider, this.scrimControllerProvider, this.provideKeyguardLiftControllerProvider, this.lockscreenWallpaperProvider, this.biometricUnlockControllerProvider, this.dozeServiceHostProvider, DaggerTvGlobalRootComponent.this.providePowerManagerProvider, this.screenPinningRequestProvider, this.dozeScrimControllerProvider, this.volumeDialogComponentProvider, this.provideCommandQueueProvider, this.statusBarComponentBuilderProvider, this.providePluginManagerProvider, this.setLegacySplitScreenProvider, this.lightsOutNotifControllerProvider, this.builderProvider4, this.shadeControllerImplProvider, this.superStatusBarViewFactoryProvider, this.statusBarKeyguardViewManagerProvider, this.providesViewMediatorCallbackProvider, this.initControllerProvider, this.provideTimeTickHandlerProvider, this.pluginDependencyProvider, this.keyguardDismissUtilProvider, this.extensionControllerImplProvider, this.userInfoControllerImplProvider, this.phoneStatusBarPolicyProvider, this.keyguardIndicationControllerProvider, this.provideDemoModeControllerProvider, this.notificationShadeDepthControllerProvider, this.dismissCallbackRegistryProvider, this.statusBarTouchableRegionManagerProvider, this.notificationIconAreaControllerProvider, this.factoryProvider3, this.wiredChargingRippleControllerProvider, this.provideOngoingCallControllerProvider, this.systemStatusAnimationSchedulerProvider, this.statusBarLocationPublisherProvider, this.statusBarIconControllerImplProvider, this.lockscreenShadeTransitionControllerProvider, this.featureFlagsProvider, this.keyguardUnlockAnimationControllerProvider, this.unlockedScreenOffAnimationControllerProvider, this.setStartingSurfaceProvider)));
            this.udfpsHapticsSimulatorProvider = DoubleCheck.provider(UdfpsHapticsSimulator_Factory.create(this.commandRegistryProvider, DaggerTvGlobalRootComponent.this.provideVibratorProvider, this.keyguardUpdateMonitorProvider));
            this.optionalOfUdfpsHbmProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.udfpsControllerProvider = DoubleCheck.provider(UdfpsController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideExecutionProvider, this.providerLayoutInflaterProvider, DaggerTvGlobalRootComponent.this.providesFingerprintManagerProvider, DaggerTvGlobalRootComponent.this.provideWindowManagerProvider, this.statusBarStateControllerImplProvider, this.provideMainDelayableExecutorProvider, this.provideStatusBarProvider, this.statusBarKeyguardViewManagerProvider, this.dumpManagerProvider, this.keyguardUpdateMonitorProvider, this.newKeyguardViewMediatorProvider, this.falsingManagerProxyProvider, DaggerTvGlobalRootComponent.this.providePowerManagerProvider, DaggerTvGlobalRootComponent.this.provideAccessibilityManagerProvider, this.lockscreenShadeTransitionControllerProvider, this.screenLifecycleProvider, DaggerTvGlobalRootComponent.this.provideVibratorProvider, this.udfpsHapticsSimulatorProvider, this.optionalOfUdfpsHbmProvider, this.keyguardStateControllerImplProvider, this.keyguardBypassControllerProvider, DaggerTvGlobalRootComponent.this.provideDisplayManagerProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.provideConfigurationControllerProvider));
            this.sidefpsControllerProvider = DoubleCheck.provider(SidefpsController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.providerLayoutInflaterProvider, DaggerTvGlobalRootComponent.this.providesFingerprintManagerProvider, DaggerTvGlobalRootComponent.this.provideWindowManagerProvider, this.provideMainDelayableExecutorProvider, DaggerTvGlobalRootComponent.this.provideDisplayManagerProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider));
            DelegateFactory.setDelegate(this.authControllerProvider, DoubleCheck.provider(AuthController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider, DaggerTvGlobalRootComponent.this.provideActivityTaskManagerProvider, DaggerTvGlobalRootComponent.this.provideWindowManagerProvider, DaggerTvGlobalRootComponent.this.providesFingerprintManagerProvider, DaggerTvGlobalRootComponent.this.provideFaceManagerProvider, this.udfpsControllerProvider, this.sidefpsControllerProvider, DaggerTvGlobalRootComponent.this.provideDisplayManagerProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider)));
            DelegateFactory.setDelegate(this.keyguardUpdateMonitorProvider, DoubleCheck.provider(KeyguardUpdateMonitor_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.providesBroadcastDispatcherProvider, this.dumpManagerProvider, this.ringerModeTrackerImplProvider, this.provideBackgroundExecutorProvider, this.statusBarStateControllerImplProvider, this.provideLockPatternUtilsProvider, this.authControllerProvider, this.telephonyListenerManagerProvider, this.featureFlagsProvider, DaggerTvGlobalRootComponent.this.provideVibratorProvider)));
            DelegateFactory.setDelegate(this.keyguardStateControllerImplProvider, DoubleCheck.provider(KeyguardStateControllerImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.keyguardUpdateMonitorProvider, this.provideLockPatternUtilsProvider, this.provideSmartspaceTransitionControllerProvider)));
            this.brightLineFalsingManagerProvider = BrightLineFalsingManager_Factory.create(this.falsingDataProvider, this.provideMetricsLoggerProvider, this.namedSetOfFalsingClassifierProvider, this.singleTapClassifierProvider, this.doubleTapClassifierProvider, this.historyTrackerProvider, this.keyguardStateControllerImplProvider, DaggerTvGlobalRootComponent.this.provideAccessibilityManagerProvider, GlobalModule_ProvideIsTestHarnessFactory.create());
            DelegateFactory.setDelegate(this.falsingManagerProxyProvider, DoubleCheck.provider(FalsingManagerProxy_Factory.create(this.providePluginManagerProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider, this.deviceConfigProxyProvider, this.dumpManagerProvider, this.brightLineFalsingManagerProvider)));
            DelegateFactory.setDelegate(this.factoryProvider3, BrightnessSlider_Factory_Factory.create(this.falsingManagerProxyProvider));
            this.brightnessDialogProvider = BrightnessDialog_Factory.create(this.providesBroadcastDispatcherProvider, this.factoryProvider3);
            this.screenRecordDialogProvider = ScreenRecordDialog_Factory.create(this.recordingControllerProvider, this.provideUserTrackerProvider);
            this.usbDebuggingActivityProvider = UsbDebuggingActivity_Factory.create(this.providesBroadcastDispatcherProvider);
            this.usbDebuggingSecondaryUserActivityProvider = UsbDebuggingSecondaryUserActivity_Factory.create(this.providesBroadcastDispatcherProvider);
            this.userCreatorProvider = UserCreator_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideUserManagerProvider);
            UserModule_ProvideEditUserInfoControllerFactory create3 = UserModule_ProvideEditUserInfoControllerFactory.create(userModule);
            this.provideEditUserInfoControllerProvider = create3;
            this.createUserActivityProvider = CreateUserActivity_Factory.create(this.userCreatorProvider, create3, DaggerTvGlobalRootComponent.this.provideIActivityManagerProvider);
            Provider<TvNotificationHandler> provider4 = DoubleCheck.provider(TvSystemUIModule_ProvideTvNotificationHandlerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideNotificationListenerProvider));
            this.provideTvNotificationHandlerProvider = provider4;
            this.tvNotificationPanelActivityProvider = TvNotificationPanelActivity_Factory.create(provider4);
            this.peopleSpaceActivityProvider = PeopleSpaceActivity_Factory.create(this.peopleSpaceWidgetManagerProvider);
            this.imageExporterProvider = ImageExporter_Factory.create(DaggerTvGlobalRootComponent.this.provideContentResolverProvider);
            this.longScreenshotDataProvider = DoubleCheck.provider(LongScreenshotData_Factory.create());
            this.longScreenshotActivityProvider = LongScreenshotActivity_Factory.create(DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider, this.imageExporterProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.longScreenshotDataProvider);
            this.launchConversationActivityProvider = LaunchConversationActivity_Factory.create(this.provideNotificationEntryManagerProvider, this.provideBubblesManagerProvider, DaggerTvGlobalRootComponent.this.provideUserManagerProvider, this.provideCommandQueueProvider);
            this.sensorUseStartedActivityProvider = SensorUseStartedActivity_Factory.create(this.provideIndividualSensorPrivacyControllerProvider, this.keyguardStateControllerImplProvider, this.keyguardDismissUtilProvider, this.provideBgHandlerProvider);
            this.tvUnblockSensorActivityProvider = TvUnblockSensorActivity_Factory.create(this.provideIndividualSensorPrivacyControllerProvider);
            this.provideExecutorProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideExecutorFactory.create(this.provideBgLooperProvider));
            this.controlsListingControllerImplProvider = DoubleCheck.provider(ControlsListingControllerImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideExecutorProvider, this.provideUserTrackerProvider));
            this.controlsControllerImplProvider = new DelegateFactory();
            this.globalActionsComponentProvider = new DelegateFactory();
            this.provideQuickAccessWalletClientProvider = DoubleCheck.provider(WalletModule_ProvideQuickAccessWalletClientFactory.create(DaggerTvGlobalRootComponent.this.contextProvider));
            this.globalActionsInfoProvider = GlobalActionsInfoProvider_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideQuickAccessWalletClientProvider, this.controlsControllerImplProvider, this.activityStarterDelegateProvider);
            this.globalActionsDialogLiteProvider = GlobalActionsDialogLite_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.globalActionsComponentProvider, DaggerTvGlobalRootComponent.this.provideAudioManagerProvider, DaggerTvGlobalRootComponent.this.provideIDreamManagerProvider, DaggerTvGlobalRootComponent.this.provideDevicePolicyManagerProvider, this.provideLockPatternUtilsProvider, this.providesBroadcastDispatcherProvider, this.telephonyListenerManagerProvider, this.globalSettingsImplProvider, this.secureSettingsImplProvider, DaggerTvGlobalRootComponent.this.provideVibratorProvider, DaggerTvGlobalRootComponent.this.provideResourcesProvider, this.provideConfigurationControllerProvider, this.keyguardStateControllerImplProvider, DaggerTvGlobalRootComponent.this.provideUserManagerProvider, DaggerTvGlobalRootComponent.this.provideTrustManagerProvider, DaggerTvGlobalRootComponent.this.provideIActivityManagerProvider, DaggerTvGlobalRootComponent.this.provideTelecomManagerProvider, this.provideMetricsLoggerProvider, this.sysuiColorExtractorProvider, DaggerTvGlobalRootComponent.this.provideIStatusBarServiceProvider, this.notificationShadeWindowControllerImplProvider, DaggerTvGlobalRootComponent.this.provideIWindowManagerProvider, this.provideBackgroundExecutorProvider, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider, this.globalActionsInfoProvider, this.ringerModeTrackerImplProvider, this.provideSysUiStateProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, DaggerTvGlobalRootComponent.this.providePackageManagerProvider, this.provideStatusBarProvider);
            this.globalActionsImplProvider = GlobalActionsImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider, this.globalActionsDialogLiteProvider, this.blurUtilsProvider, this.keyguardStateControllerImplProvider, this.deviceProvisionedControllerImplProvider);
            DelegateFactory.setDelegate(this.globalActionsComponentProvider, DoubleCheck.provider(GlobalActionsComponent_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider, this.extensionControllerImplProvider, this.globalActionsImplProvider, this.statusBarKeyguardViewManagerProvider)));
            this.setTaskViewFactoryProvider = InstanceFactory.create(optional7);
        }

        private void initialize5(DependencyProvider dependencyProvider, NightDisplayListenerModule nightDisplayListenerModule, UserModule userModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<TaskSurfaceHelper> optional11) {
            this.controlsUiControllerImplProvider = new DelegateFactory();
            this.controlsMetricsLoggerImplProvider = DoubleCheck.provider(ControlsMetricsLoggerImpl_Factory.create());
            this.controlActionCoordinatorImplProvider = DoubleCheck.provider(ControlActionCoordinatorImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideDelayableExecutorProvider, this.provideMainDelayableExecutorProvider, this.activityStarterDelegateProvider, this.keyguardStateControllerImplProvider, this.globalActionsComponentProvider, this.setTaskViewFactoryProvider, this.providesBroadcastDispatcherProvider, this.controlsUiControllerImplProvider, this.controlsMetricsLoggerImplProvider));
            this.customIconCacheProvider = DoubleCheck.provider(CustomIconCache_Factory.create());
            DelegateFactory.setDelegate(this.controlsUiControllerImplProvider, DoubleCheck.provider(ControlsUiControllerImpl_Factory.create(this.controlsControllerImplProvider, DaggerTvGlobalRootComponent.this.contextProvider, this.provideMainDelayableExecutorProvider, this.provideBackgroundDelayableExecutorProvider, this.controlsListingControllerImplProvider, this.provideSharePreferencesProvider, this.controlActionCoordinatorImplProvider, this.activityStarterDelegateProvider, this.shadeControllerImplProvider, this.customIconCacheProvider, this.controlsMetricsLoggerImplProvider, this.keyguardStateControllerImplProvider)));
            this.controlsBindingControllerImplProvider = DoubleCheck.provider(ControlsBindingControllerImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideBackgroundDelayableExecutorProvider, this.controlsControllerImplProvider, this.provideUserTrackerProvider));
            this.optionalOfControlsFavoritePersistenceWrapperProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            DelegateFactory.setDelegate(this.controlsControllerImplProvider, DoubleCheck.provider(ControlsControllerImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideBackgroundDelayableExecutorProvider, this.controlsUiControllerImplProvider, this.controlsBindingControllerImplProvider, this.controlsListingControllerImplProvider, this.providesBroadcastDispatcherProvider, this.optionalOfControlsFavoritePersistenceWrapperProvider, this.dumpManagerProvider, this.provideUserTrackerProvider)));
            this.controlsProviderSelectorActivityProvider = ControlsProviderSelectorActivity_Factory.create(DaggerTvGlobalRootComponent.this.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.controlsListingControllerImplProvider, this.controlsControllerImplProvider, this.providesBroadcastDispatcherProvider, this.controlsUiControllerImplProvider);
            this.controlsFavoritingActivityProvider = ControlsFavoritingActivity_Factory.create(DaggerTvGlobalRootComponent.this.provideMainExecutorProvider, this.controlsControllerImplProvider, this.controlsListingControllerImplProvider, this.providesBroadcastDispatcherProvider, this.controlsUiControllerImplProvider);
            this.controlsEditingActivityProvider = ControlsEditingActivity_Factory.create(this.controlsControllerImplProvider, this.providesBroadcastDispatcherProvider, this.customIconCacheProvider, this.controlsUiControllerImplProvider);
            this.controlsRequestDialogProvider = ControlsRequestDialog_Factory.create(this.controlsControllerImplProvider, this.providesBroadcastDispatcherProvider, this.controlsListingControllerImplProvider);
            this.controlsActivityProvider = ControlsActivity_Factory.create(this.controlsUiControllerImplProvider);
            this.walletActivityProvider = WalletActivity_Factory.create(this.keyguardStateControllerImplProvider, this.keyguardDismissUtilProvider, this.activityStarterDelegateProvider, this.provideBackgroundExecutorProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.falsingCollectorImplProvider, this.provideUserTrackerProvider, this.keyguardUpdateMonitorProvider, this.statusBarKeyguardViewManagerProvider, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider);
            this.mapOfClassOfAndProviderOfActivityProvider = MapProviderFactory.builder(20).put((MapProviderFactory.Builder) TunerActivity.class, (Provider) this.tunerActivityProvider).put((MapProviderFactory.Builder) ForegroundServicesDialog.class, (Provider) ForegroundServicesDialog_Factory.create()).put((MapProviderFactory.Builder) WorkLockActivity.class, (Provider) this.workLockActivityProvider).put((MapProviderFactory.Builder) BrightnessDialog.class, (Provider) this.brightnessDialogProvider).put((MapProviderFactory.Builder) ScreenRecordDialog.class, (Provider) this.screenRecordDialogProvider).put((MapProviderFactory.Builder) UsbDebuggingActivity.class, (Provider) this.usbDebuggingActivityProvider).put((MapProviderFactory.Builder) UsbDebuggingSecondaryUserActivity.class, (Provider) this.usbDebuggingSecondaryUserActivityProvider).put((MapProviderFactory.Builder) CreateUserActivity.class, (Provider) this.createUserActivityProvider).put((MapProviderFactory.Builder) TvNotificationPanelActivity.class, (Provider) this.tvNotificationPanelActivityProvider).put((MapProviderFactory.Builder) PeopleSpaceActivity.class, (Provider) this.peopleSpaceActivityProvider).put((MapProviderFactory.Builder) LongScreenshotActivity.class, (Provider) this.longScreenshotActivityProvider).put((MapProviderFactory.Builder) LaunchConversationActivity.class, (Provider) this.launchConversationActivityProvider).put((MapProviderFactory.Builder) SensorUseStartedActivity.class, (Provider) this.sensorUseStartedActivityProvider).put((MapProviderFactory.Builder) TvUnblockSensorActivity.class, (Provider) this.tvUnblockSensorActivityProvider).put((MapProviderFactory.Builder) ControlsProviderSelectorActivity.class, (Provider) this.controlsProviderSelectorActivityProvider).put((MapProviderFactory.Builder) ControlsFavoritingActivity.class, (Provider) this.controlsFavoritingActivityProvider).put((MapProviderFactory.Builder) ControlsEditingActivity.class, (Provider) this.controlsEditingActivityProvider).put((MapProviderFactory.Builder) ControlsRequestDialog.class, (Provider) this.controlsRequestDialogProvider).put((MapProviderFactory.Builder) ControlsActivity.class, (Provider) this.controlsActivityProvider).put((MapProviderFactory.Builder) WalletActivity.class, (Provider) this.walletActivityProvider).build();
            AnonymousClass8 r2 = new Provider<DozeComponent.Builder>() { // from class: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentImpl.8
                @Override // javax.inject.Provider
                public DozeComponent.Builder get() {
                    return new DozeComponentFactory();
                }
            };
            this.dozeComponentBuilderProvider = r2;
            this.dozeServiceProvider = DozeService_Factory.create(r2, this.providePluginManagerProvider);
            Provider<KeyguardLifecyclesDispatcher> provider = DoubleCheck.provider(KeyguardLifecyclesDispatcher_Factory.create(this.screenLifecycleProvider, this.wakefulnessLifecycleProvider));
            this.keyguardLifecyclesDispatcherProvider = provider;
            this.keyguardServiceProvider = KeyguardService_Factory.create(this.newKeyguardViewMediatorProvider, provider);
            this.dumpHandlerProvider = DumpHandler_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.dumpManagerProvider, this.logBufferEulogizerProvider);
            this.logBufferFreezerProvider = LogBufferFreezer_Factory.create(this.dumpManagerProvider, this.provideMainDelayableExecutorProvider);
            this.batteryStateNotifierProvider = BatteryStateNotifier_Factory.create(this.provideBatteryControllerProvider, DaggerTvGlobalRootComponent.this.provideNotificationManagerProvider, this.provideDelayableExecutorProvider, DaggerTvGlobalRootComponent.this.contextProvider);
            this.systemUIServiceProvider = SystemUIService_Factory.create(DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.dumpHandlerProvider, this.providesBroadcastDispatcherProvider, this.logBufferFreezerProvider, this.batteryStateNotifierProvider);
            this.systemUIAuxiliaryDumpServiceProvider = SystemUIAuxiliaryDumpService_Factory.create(this.dumpHandlerProvider);
            Provider<Looper> provider2 = DoubleCheck.provider(SysUIConcurrencyModule_ProvideLongRunningLooperFactory.create());
            this.provideLongRunningLooperProvider = provider2;
            Provider<Executor> provider3 = DoubleCheck.provider(SysUIConcurrencyModule_ProvideLongRunningExecutorFactory.create(provider2));
            this.provideLongRunningExecutorProvider = provider3;
            this.recordingServiceProvider = RecordingService_Factory.create(this.recordingControllerProvider, provider3, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider, DaggerTvGlobalRootComponent.this.provideNotificationManagerProvider, this.provideUserTrackerProvider, this.keyguardDismissUtilProvider);
            this.screenshotSmartActionsProvider = DoubleCheck.provider(ScreenshotSmartActions_Factory.create());
            this.screenshotNotificationsControllerProvider = ScreenshotNotificationsController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideWindowManagerProvider);
            this.scrollCaptureClientProvider = ScrollCaptureClient_Factory.create(DaggerTvGlobalRootComponent.this.provideIWindowManagerProvider, this.provideBackgroundExecutorProvider, DaggerTvGlobalRootComponent.this.contextProvider);
            this.imageTileSetProvider = ImageTileSet_Factory.create(this.provideHandlerProvider);
            this.scrollCaptureControllerProvider = ScrollCaptureController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideBackgroundExecutorProvider, this.scrollCaptureClientProvider, this.imageTileSetProvider);
            ScreenshotController_Factory create = ScreenshotController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.screenshotSmartActionsProvider, this.screenshotNotificationsControllerProvider, this.scrollCaptureClientProvider, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider, this.imageExporterProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider, this.scrollCaptureControllerProvider, this.longScreenshotDataProvider, DaggerTvGlobalRootComponent.this.provideActivityManagerProvider);
            this.screenshotControllerProvider = create;
            this.takeScreenshotServiceProvider = TakeScreenshotService_Factory.create(create, DaggerTvGlobalRootComponent.this.provideUserManagerProvider, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider, this.screenshotNotificationsControllerProvider);
            this.mapOfClassOfAndProviderOfServiceProvider = MapProviderFactory.builder(7).put((MapProviderFactory.Builder) DozeService.class, (Provider) this.dozeServiceProvider).put((MapProviderFactory.Builder) ImageWallpaper.class, (Provider) ImageWallpaper_Factory.create()).put((MapProviderFactory.Builder) KeyguardService.class, (Provider) this.keyguardServiceProvider).put((MapProviderFactory.Builder) SystemUIService.class, (Provider) this.systemUIServiceProvider).put((MapProviderFactory.Builder) SystemUIAuxiliaryDumpService.class, (Provider) this.systemUIAuxiliaryDumpServiceProvider).put((MapProviderFactory.Builder) RecordingService.class, (Provider) this.recordingServiceProvider).put((MapProviderFactory.Builder) TakeScreenshotService.class, (Provider) this.takeScreenshotServiceProvider).build();
            this.provideLeakReportEmailProvider = DoubleCheck.provider(TvSystemUIModule_ProvideLeakReportEmailFactory.create());
            this.leakReporterProvider = DoubleCheck.provider(LeakReporter_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideLeakDetectorProvider, this.provideLeakReportEmailProvider));
            this.garbageMonitorProvider = DoubleCheck.provider(GarbageMonitor_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideBgLooperProvider, this.provideLeakDetectorProvider, this.leakReporterProvider));
            this.serviceProvider = DoubleCheck.provider(GarbageMonitor_Service_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.garbageMonitorProvider));
            this.instantAppNotifierProvider = DoubleCheck.provider(InstantAppNotifier_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider, this.provideUiBackgroundExecutorProvider, this.setLegacySplitScreenProvider));
            this.latencyTesterProvider = DoubleCheck.provider(LatencyTester_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.biometricUnlockControllerProvider, DaggerTvGlobalRootComponent.this.providePowerManagerProvider, this.providesBroadcastDispatcherProvider));
            this.powerUIProvider = DoubleCheck.provider(PowerUI_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.providesBroadcastDispatcherProvider, this.provideCommandQueueProvider, this.provideStatusBarProvider));
            this.privacyDotViewControllerProvider = DoubleCheck.provider(PrivacyDotViewController_Factory.create(DaggerTvGlobalRootComponent.this.provideMainExecutorProvider, this.statusBarStateControllerImplProvider, this.provideConfigurationControllerProvider, this.statusBarContentInsetsProvider, this.systemStatusAnimationSchedulerProvider));
            this.screenDecorationsProvider = DoubleCheck.provider(ScreenDecorations_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider, this.secureSettingsImplProvider, this.providesBroadcastDispatcherProvider, this.tunerServiceImplProvider, this.provideUserTrackerProvider, this.privacyDotViewControllerProvider, ThreadFactoryImpl_Factory.create()));
            this.shortcutKeyDispatcherProvider = DoubleCheck.provider(ShortcutKeyDispatcher_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.setLegacySplitScreenProvider));
            this.sliceBroadcastRelayHandlerProvider = DoubleCheck.provider(SliceBroadcastRelayHandler_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.providesBroadcastDispatcherProvider));
            this.provideThemeOverlayManagerProvider = DoubleCheck.provider(DependencyProvider_ProvideThemeOverlayManagerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideBackgroundExecutorProvider, DaggerTvGlobalRootComponent.this.provideOverlayManagerProvider, this.dumpManagerProvider));
            this.themeOverlayControllerProvider = DoubleCheck.provider(ThemeOverlayController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.providesBroadcastDispatcherProvider, this.provideBgHandlerProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.provideThemeOverlayManagerProvider, this.secureSettingsImplProvider, DaggerTvGlobalRootComponent.this.provideWallpaperManagerProvider, DaggerTvGlobalRootComponent.this.provideUserManagerProvider, this.deviceProvisionedControllerImplProvider, this.provideUserTrackerProvider, this.dumpManagerProvider, this.featureFlagsProvider, this.wakefulnessLifecycleProvider));
            this.toastFactoryProvider = DoubleCheck.provider(ToastFactory_Factory.create(this.providerLayoutInflaterProvider, this.providePluginManagerProvider, this.dumpManagerProvider));
            Provider<LogBuffer> provider4 = DoubleCheck.provider(LogModule_ProvideToastLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideToastLogBufferProvider = provider4;
            this.toastLoggerProvider = ToastLogger_Factory.create(provider4);
            this.toastUIProvider = DoubleCheck.provider(ToastUI_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider, this.toastFactoryProvider, this.toastLoggerProvider));
            this.tvStatusBarProvider = DoubleCheck.provider(TvStatusBar_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider, this.assistManagerProvider));
            this.tvNotificationPanelProvider = DoubleCheck.provider(TvNotificationPanel_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider));
            this.tvOngoingPrivacyChipProvider = DoubleCheck.provider(TvOngoingPrivacyChip_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.privacyItemControllerProvider));
            this.volumeUIProvider = DoubleCheck.provider(VolumeUI_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.volumeDialogComponentProvider));
            this.providesModeSwitchesControllerProvider = DoubleCheck.provider(DependencyProvider_ProvidesModeSwitchesControllerFactory.create(dependencyProvider, DaggerTvGlobalRootComponent.this.contextProvider));
            this.windowMagnificationProvider = DoubleCheck.provider(WindowMagnification_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.provideCommandQueueProvider, this.providesModeSwitchesControllerProvider, this.provideSysUiStateProvider, this.overviewProxyServiceProvider));
            this.setHideDisplayCutoutProvider = InstanceFactory.create(optional8);
            this.setShellCommandHandlerProvider = InstanceFactory.create(optional9);
            this.wMShellProvider = DoubleCheck.provider(WMShell_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.setPipProvider, this.setLegacySplitScreenProvider, this.setOneHandedProvider, this.setHideDisplayCutoutProvider, this.setShellCommandHandlerProvider, this.provideCommandQueueProvider, this.provideConfigurationControllerProvider, this.keyguardUpdateMonitorProvider, this.navigationModeControllerProvider, this.screenLifecycleProvider, this.provideSysUiStateProvider, this.protoTracerProvider, this.wakefulnessLifecycleProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider));
            this.provideTaskStackChangeListenersProvider = DoubleCheck.provider(DependencyProvider_ProvideTaskStackChangeListenersFactory.create(dependencyProvider));
            this.homeSoundEffectControllerProvider = DoubleCheck.provider(HomeSoundEffectController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideAudioManagerProvider, this.provideTaskStackChangeListenersProvider, this.provideActivityManagerWrapperProvider, DaggerTvGlobalRootComponent.this.providePackageManagerProvider));
            this.mapOfClassOfAndProviderOfSystemUIProvider = MapProviderFactory.builder(23).put((MapProviderFactory.Builder) AuthController.class, (Provider) this.authControllerProvider).put((MapProviderFactory.Builder) GarbageMonitor.Service.class, (Provider) this.serviceProvider).put((MapProviderFactory.Builder) GlobalActionsComponent.class, (Provider) this.globalActionsComponentProvider).put((MapProviderFactory.Builder) InstantAppNotifier.class, (Provider) this.instantAppNotifierProvider).put((MapProviderFactory.Builder) KeyguardViewMediator.class, (Provider) this.newKeyguardViewMediatorProvider).put((MapProviderFactory.Builder) LatencyTester.class, (Provider) this.latencyTesterProvider).put((MapProviderFactory.Builder) PowerUI.class, (Provider) this.powerUIProvider).put((MapProviderFactory.Builder) Recents.class, (Provider) this.provideRecentsProvider).put((MapProviderFactory.Builder) ScreenDecorations.class, (Provider) this.screenDecorationsProvider).put((MapProviderFactory.Builder) ShortcutKeyDispatcher.class, (Provider) this.shortcutKeyDispatcherProvider).put((MapProviderFactory.Builder) SliceBroadcastRelayHandler.class, (Provider) this.sliceBroadcastRelayHandlerProvider).put((MapProviderFactory.Builder) StatusBar.class, (Provider) this.provideStatusBarProvider).put((MapProviderFactory.Builder) SystemActions.class, (Provider) this.systemActionsProvider).put((MapProviderFactory.Builder) ThemeOverlayController.class, (Provider) this.themeOverlayControllerProvider).put((MapProviderFactory.Builder) ToastUI.class, (Provider) this.toastUIProvider).put((MapProviderFactory.Builder) TvStatusBar.class, (Provider) this.tvStatusBarProvider).put((MapProviderFactory.Builder) TvNotificationPanel.class, (Provider) this.tvNotificationPanelProvider).put((MapProviderFactory.Builder) TvOngoingPrivacyChip.class, (Provider) this.tvOngoingPrivacyChipProvider).put((MapProviderFactory.Builder) VolumeUI.class, (Provider) this.volumeUIProvider).put((MapProviderFactory.Builder) WindowMagnification.class, (Provider) this.windowMagnificationProvider).put((MapProviderFactory.Builder) WMShell.class, (Provider) this.wMShellProvider).put((MapProviderFactory.Builder) HomeSoundEffectController.class, (Provider) this.homeSoundEffectControllerProvider).put((MapProviderFactory.Builder) TvNotificationHandler.class, (Provider) this.provideTvNotificationHandlerProvider).build();
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
            this.flashlightControllerImplProvider = DoubleCheck.provider(FlashlightControllerImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider));
            this.provideNightDisplayListenerProvider = NightDisplayListenerModule_ProvideNightDisplayListenerFactory.create(nightDisplayListenerModule, DaggerTvGlobalRootComponent.this.contextProvider, this.provideBgHandlerProvider);
            this.provideReduceBrightColorsListenerProvider = DoubleCheck.provider(DependencyProvider_ProvideReduceBrightColorsListenerFactory.create(dependencyProvider, this.provideBgHandlerProvider, this.provideUserTrackerProvider, DaggerTvGlobalRootComponent.this.provideColorDisplayManagerProvider, this.secureSettingsImplProvider));
            this.managedProfileControllerImplProvider = DoubleCheck.provider(ManagedProfileControllerImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.providesBroadcastDispatcherProvider));
            this.accessibilityControllerProvider = DoubleCheck.provider(AccessibilityController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider));
            AnonymousClass9 r1 = new Provider<FragmentService.FragmentCreator.Factory>() { // from class: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentImpl.9
                @Override // javax.inject.Provider
                public FragmentService.FragmentCreator.Factory get() {
                    return new FragmentCreatorFactory();
                }
            };
            this.fragmentCreatorFactoryProvider = r1;
            this.fragmentServiceProvider = DoubleCheck.provider(FragmentService_Factory.create(r1, this.provideConfigurationControllerProvider));
            this.tunablePaddingServiceProvider = DoubleCheck.provider(TunablePadding_TunablePaddingService_Factory.create(this.tunerServiceImplProvider));
            this.uiOffloadThreadProvider = DoubleCheck.provider(UiOffloadThread_Factory.create());
            this.powerNotificationWarningsProvider = DoubleCheck.provider(PowerNotificationWarnings_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.activityStarterDelegateProvider));
            this.foregroundServiceNotificationListenerProvider = DoubleCheck.provider(ForegroundServiceNotificationListener_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.foregroundServiceControllerProvider, this.provideNotificationEntryManagerProvider, this.notifPipelineProvider, this.bindSystemClockProvider));
            this.clockManagerProvider = DoubleCheck.provider(ClockManager_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.injectionInflationControllerProvider, this.providePluginManagerProvider, this.sysuiColorExtractorProvider, this.dockManagerImplProvider, this.providesBroadcastDispatcherProvider));
            this.factoryProvider4 = EdgeBackGestureHandler_Factory_Factory.create(this.overviewProxyServiceProvider, this.provideSysUiStateProvider, this.providePluginManagerProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider, this.providesBroadcastDispatcherProvider, this.protoTracerProvider, this.navigationModeControllerProvider, DaggerTvGlobalRootComponent.this.provideViewConfigurationProvider, DaggerTvGlobalRootComponent.this.provideWindowManagerProvider, DaggerTvGlobalRootComponent.this.provideIWindowManagerProvider, this.falsingManagerProxyProvider);
            this.dependencyProvider2 = DoubleCheck.provider(Dependency_Factory.create(this.dumpManagerProvider, this.activityStarterDelegateProvider, this.providesBroadcastDispatcherProvider, this.asyncSensorManagerProvider, this.bluetoothControllerImplProvider, this.locationControllerImplProvider, this.rotationLockControllerImplProvider, this.networkControllerImplProvider, this.zenModeControllerImplProvider, this.hotspotControllerImplProvider, this.castControllerImplProvider, this.flashlightControllerImplProvider, this.userSwitcherControllerProvider, this.userInfoControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardUpdateMonitorProvider, this.provideBatteryControllerProvider, this.provideNightDisplayListenerProvider, this.provideReduceBrightColorsListenerProvider, this.managedProfileControllerImplProvider, this.nextAlarmControllerImplProvider, this.provideDataSaverControllerProvider, this.accessibilityControllerProvider, this.deviceProvisionedControllerImplProvider, this.providePluginManagerProvider, this.assistManagerProvider, this.securityControllerImplProvider, this.provideLeakDetectorProvider, this.leakReporterProvider, this.garbageMonitorProvider, this.tunerServiceImplProvider, this.notificationShadeWindowControllerImplProvider, this.statusBarWindowControllerProvider, this.darkIconDispatcherImplProvider, this.provideConfigurationControllerProvider, this.statusBarIconControllerImplProvider, this.screenLifecycleProvider, this.wakefulnessLifecycleProvider, this.fragmentServiceProvider, this.extensionControllerImplProvider, this.pluginDependencyProvider, this.provideLocalBluetoothControllerProvider, this.volumeDialogControllerImplProvider, this.provideMetricsLoggerProvider, this.accessibilityManagerWrapperProvider, this.sysuiColorExtractorProvider, this.tunablePaddingServiceProvider, this.foregroundServiceControllerProvider, this.uiOffloadThreadProvider, this.powerNotificationWarningsProvider, this.lightBarControllerProvider, DaggerTvGlobalRootComponent.this.provideIWindowManagerProvider, this.overviewProxyServiceProvider, this.navigationModeControllerProvider, this.accessibilityButtonModeObserverProvider, this.accessibilityButtonTargetsObserverProvider, this.enhancedEstimatesImplProvider, this.vibratorHelperProvider, DaggerTvGlobalRootComponent.this.provideIStatusBarServiceProvider, DaggerTvGlobalRootComponent.this.provideDisplayMetricsProvider, this.lockscreenGestureLoggerProvider, this.keyguardEnvironmentImplProvider, this.shadeControllerImplProvider, this.statusBarRemoteInputCallbackProvider, this.appOpsControllerImplProvider, this.provideNavigationBarControllerProvider, this.provideAccessibilityFloatingMenuControllerProvider, this.statusBarStateControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.provideNotificationGroupAlertTransferHelperProvider, this.notificationGroupManagerLegacyProvider, this.provideVisualStabilityManagerProvider, this.provideNotificationGutsManagerProvider, this.provideNotificationMediaManagerProvider, this.provideNotificationRemoteInputManagerProvider, this.smartReplyConstantsProvider, this.provideNotificationListenerProvider, this.provideNotificationLoggerProvider, this.provideNotificationViewHierarchyManagerProvider, this.notificationFilterProvider, this.keyguardDismissUtilProvider, this.provideSmartReplyControllerProvider, this.remoteInputQuickSettingsDisablerProvider, this.provideNotificationEntryManagerProvider, DaggerTvGlobalRootComponent.this.provideSensorPrivacyManagerProvider, this.provideAutoHideControllerProvider, this.foregroundServiceNotificationListenerProvider, this.privacyItemControllerProvider, this.provideBgLooperProvider, this.provideBgHandlerProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.provideTimeTickHandlerProvider, this.provideLeakReportEmailProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.clockManagerProvider, this.provideActivityManagerWrapperProvider, this.provideDevicePolicyManagerWrapperProvider, DaggerTvGlobalRootComponent.this.providePackageManagerWrapperProvider, this.provideSensorPrivacyControllerProvider, this.dockManagerImplProvider, this.provideINotificationManagerProvider, this.provideSysUiStateProvider, DaggerTvGlobalRootComponent.this.provideAlarmManagerProvider, this.keyguardSecurityModelProvider, this.dozeParametersProvider, FrameworkServicesModule_ProvideIWallPaperManagerFactory.create(), this.provideCommandQueueProvider, this.provideRecentsProvider, this.provideStatusBarProvider, this.recordingControllerProvider, this.protoTracerProvider, this.mediaOutputDialogFactoryProvider, this.deviceConfigProxyProvider, this.navigationBarOverlayControllerProvider, this.telephonyListenerManagerProvider, this.systemStatusAnimationSchedulerProvider, this.privacyDotViewControllerProvider, this.factoryProvider4, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider, this.featureFlagsProvider, this.statusBarContentInsetsProvider));
            this.provideClockInfoListProvider = ClockModule_ProvideClockInfoListFactory.create(this.clockManagerProvider);
            this.provideAllowNotificationLongPressProvider = DoubleCheck.provider(TvSystemUIModule_ProvideAllowNotificationLongPressFactory.create());
            this.keyguardQsUserSwitchComponentFactoryProvider = new Provider<KeyguardQsUserSwitchComponent.Factory>() { // from class: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentImpl.10
                @Override // javax.inject.Provider
                public KeyguardQsUserSwitchComponent.Factory get() {
                    return new KeyguardQsUserSwitchComponentFactory();
                }
            };
            this.keyguardUserSwitcherComponentFactoryProvider = new Provider<KeyguardUserSwitcherComponent.Factory>() { // from class: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentImpl.11
                @Override // javax.inject.Provider
                public KeyguardUserSwitcherComponent.Factory get() {
                    return new KeyguardUserSwitcherComponentFactory();
                }
            };
            this.keyguardStatusBarViewComponentFactoryProvider = new Provider<KeyguardStatusBarViewComponent.Factory>() { // from class: com.android.systemui.tv.DaggerTvGlobalRootComponent.TvSysUIComponentImpl.12
                @Override // javax.inject.Provider
                public KeyguardStatusBarViewComponent.Factory get() {
                    return new KeyguardStatusBarViewComponentFactory();
                }
            };
            this.qSDetailDisplayerProvider = DoubleCheck.provider(QSDetailDisplayer_Factory.create());
            this.quickAccessWalletControllerProvider = DoubleCheck.provider(QuickAccessWalletController_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider, this.secureSettingsImplProvider, this.provideQuickAccessWalletClientProvider, this.bindSystemClockProvider));
            Provider<Boolean> provider5 = DoubleCheck.provider(ControlsModule_ProvidesControlsFeatureEnabledFactory.create(DaggerTvGlobalRootComponent.this.providePackageManagerProvider));
            this.providesControlsFeatureEnabledProvider = provider5;
            this.controlsComponentProvider = DoubleCheck.provider(ControlsComponent_Factory.create(provider5, DaggerTvGlobalRootComponent.this.contextProvider, this.controlsControllerImplProvider, this.controlsUiControllerImplProvider, this.controlsListingControllerImplProvider, this.provideLockPatternUtilsProvider, this.keyguardStateControllerImplProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider));
            this.qSTileHostProvider = new DelegateFactory();
            this.provideQuickSettingsLogBufferProvider = DoubleCheck.provider(LogModule_ProvideQuickSettingsLogBufferFactory.create(this.logBufferFactoryProvider));
        }

        private void initialize6(DependencyProvider dependencyProvider, NightDisplayListenerModule nightDisplayListenerModule, UserModule userModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<TaskSurfaceHelper> optional11) {
            this.qSLoggerProvider = QSLogger_Factory.create(this.provideQuickSettingsLogBufferProvider);
            this.customTileStatePersisterProvider = CustomTileStatePersister_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider);
            this.builderProvider6 = CustomTile_Builder_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.customTileStatePersisterProvider);
            this.wifiTileProvider = WifiTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.networkControllerImplProvider, this.provideAccessPointControllerImplProvider);
            this.internetTileProvider = InternetTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.networkControllerImplProvider);
            this.bluetoothTileProvider = BluetoothTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.bluetoothControllerImplProvider);
            this.cellularTileProvider = CellularTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.networkControllerImplProvider);
            this.dndTileProvider = DndTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.zenModeControllerImplProvider, this.provideSharePreferencesProvider);
            this.colorInversionTileProvider = ColorInversionTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider);
            this.airplaneModeTileProvider = AirplaneModeTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.providesBroadcastDispatcherProvider, DaggerTvGlobalRootComponent.this.provideConnectivityManagagerProvider);
            this.workModeTileProvider = WorkModeTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.managedProfileControllerImplProvider);
            this.rotationLockTileProvider = RotationLockTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.rotationLockControllerImplProvider);
            this.flashlightTileProvider = FlashlightTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.flashlightControllerImplProvider);
            this.locationTileProvider = LocationTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.locationControllerImplProvider, this.keyguardStateControllerImplProvider);
            this.castTileProvider = CastTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.castControllerImplProvider, this.keyguardStateControllerImplProvider, this.networkControllerImplProvider, this.hotspotControllerImplProvider);
            this.hotspotTileProvider = HotspotTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.hotspotControllerImplProvider, this.provideDataSaverControllerProvider);
            this.userTileProvider = UserTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.userSwitcherControllerProvider, this.userInfoControllerImplProvider);
            this.batterySaverTileProvider = BatterySaverTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.provideBatteryControllerProvider, this.secureSettingsImplProvider);
            this.dataSaverTileProvider = DataSaverTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.provideDataSaverControllerProvider);
            this.builderProvider7 = NightDisplayListenerModule_Builder_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideBgHandlerProvider);
            this.nightDisplayTileProvider = NightDisplayTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.locationControllerImplProvider, DaggerTvGlobalRootComponent.this.provideColorDisplayManagerProvider, this.builderProvider7);
            this.nfcTileProvider = NfcTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.providesBroadcastDispatcherProvider);
            this.memoryTileProvider = GarbageMonitor_MemoryTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.garbageMonitorProvider);
            this.uiModeNightTileProvider = UiModeNightTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.provideConfigurationControllerProvider, this.provideBatteryControllerProvider, this.locationControllerImplProvider);
            this.screenRecordTileProvider = ScreenRecordTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.recordingControllerProvider, this.keyguardDismissUtilProvider);
            Provider<Boolean> provider = DoubleCheck.provider(QSFlagsModule_IsReduceBrightColorsAvailableFactory.create(DaggerTvGlobalRootComponent.this.contextProvider));
            this.isReduceBrightColorsAvailableProvider = provider;
            this.reduceBrightColorsTileProvider = ReduceBrightColorsTile_Factory.create(provider, this.provideReduceBrightColorsListenerProvider, this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider);
            this.cameraToggleTileProvider = CameraToggleTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.provideMetricsLoggerProvider, this.falsingManagerProxyProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.provideIndividualSensorPrivacyControllerProvider, this.keyguardStateControllerImplProvider);
            this.microphoneToggleTileProvider = MicrophoneToggleTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.provideMetricsLoggerProvider, this.falsingManagerProxyProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.provideIndividualSensorPrivacyControllerProvider, this.keyguardStateControllerImplProvider);
            this.deviceControlsTileProvider = DeviceControlsTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.controlsComponentProvider, this.keyguardStateControllerImplProvider);
            this.alarmTileProvider = AlarmTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.provideUserTrackerProvider, this.nextAlarmControllerImplProvider);
            QuickAccessWalletTile_Factory create = QuickAccessWalletTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.activityStarterDelegateProvider, this.qSLoggerProvider, this.keyguardStateControllerImplProvider, DaggerTvGlobalRootComponent.this.providePackageManagerProvider, this.secureSettingsImplProvider, this.quickAccessWalletControllerProvider);
            this.quickAccessWalletTileProvider = create;
            this.qSFactoryImplProvider = DoubleCheck.provider(QSFactoryImpl_Factory.create(this.qSTileHostProvider, this.builderProvider6, this.wifiTileProvider, this.internetTileProvider, this.bluetoothTileProvider, this.cellularTileProvider, this.dndTileProvider, this.colorInversionTileProvider, this.airplaneModeTileProvider, this.workModeTileProvider, this.rotationLockTileProvider, this.flashlightTileProvider, this.locationTileProvider, this.castTileProvider, this.hotspotTileProvider, this.userTileProvider, this.batterySaverTileProvider, this.dataSaverTileProvider, this.nightDisplayTileProvider, this.nfcTileProvider, this.memoryTileProvider, this.uiModeNightTileProvider, this.screenRecordTileProvider, this.reduceBrightColorsTileProvider, this.cameraToggleTileProvider, this.microphoneToggleTileProvider, this.deviceControlsTileProvider, this.alarmTileProvider, create));
            this.builderProvider8 = AutoAddTracker_Builder_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider);
            this.deviceControlsControllerImplProvider = DoubleCheck.provider(DeviceControlsControllerImpl_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.controlsComponentProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider));
            this.walletControllerImplProvider = DoubleCheck.provider(WalletControllerImpl_Factory.create(this.provideQuickAccessWalletClientProvider));
            this.provideAutoTileManagerProvider = QSModule_ProvideAutoTileManagerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.builderProvider8, this.qSTileHostProvider, this.provideBgHandlerProvider, this.secureSettingsImplProvider, this.hotspotControllerImplProvider, this.provideDataSaverControllerProvider, this.managedProfileControllerImplProvider, this.provideNightDisplayListenerProvider, this.castControllerImplProvider, this.provideReduceBrightColorsListenerProvider, this.deviceControlsControllerImplProvider, this.walletControllerImplProvider, this.isReduceBrightColorsAvailableProvider);
            DelegateFactory.setDelegate(this.qSTileHostProvider, DoubleCheck.provider(QSTileHost_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.statusBarIconControllerImplProvider, this.qSFactoryImplProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, this.provideBgLooperProvider, this.providePluginManagerProvider, this.tunerServiceImplProvider, this.provideAutoTileManagerProvider, this.dumpManagerProvider, this.providesBroadcastDispatcherProvider, this.optionalOfStatusBarProvider, this.qSLoggerProvider, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider, this.customTileStatePersisterProvider)));
            this.providesQSMediaHostProvider = DoubleCheck.provider(MediaModule_ProvidesQSMediaHostFactory.create(MediaHost_MediaHostStateHolder_Factory.create(), this.mediaHierarchyManagerProvider, this.mediaDataManagerProvider, this.mediaHostStatesManagerProvider));
            this.providesQuickQSMediaHostProvider = DoubleCheck.provider(MediaModule_ProvidesQuickQSMediaHostFactory.create(MediaHost_MediaHostStateHolder_Factory.create(), this.mediaHierarchyManagerProvider, this.mediaDataManagerProvider, this.mediaHostStatesManagerProvider));
            this.subscriptionManagerSlotIndexResolverProvider = DoubleCheck.provider(QSCarrierGroupController_SubscriptionManagerSlotIndexResolver_Factory.create());
            this.privacyDialogControllerProvider = DoubleCheck.provider(PrivacyDialogController_Factory.create(DaggerTvGlobalRootComponent.this.providePermissionManagerProvider, DaggerTvGlobalRootComponent.this.providePackageManagerProvider, this.privacyItemControllerProvider, this.provideUserTrackerProvider, this.activityStarterDelegateProvider, this.provideBackgroundExecutorProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider, this.privacyLoggerProvider, this.keyguardStateControllerImplProvider, this.appOpsControllerImplProvider));
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

        private SystemUIAppComponentFactory injectSystemUIAppComponentFactory(SystemUIAppComponentFactory systemUIAppComponentFactory) {
            SystemUIAppComponentFactory_MembersInjector.injectMComponentHelper(systemUIAppComponentFactory, this.contextComponentResolverProvider.get());
            return systemUIAppComponentFactory;
        }

        private KeyguardSliceProvider injectKeyguardSliceProvider(KeyguardSliceProvider keyguardSliceProvider) {
            KeyguardSliceProvider_MembersInjector.injectMDozeParameters(keyguardSliceProvider, this.dozeParametersProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMZenModeController(keyguardSliceProvider, this.zenModeControllerImplProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMNextAlarmController(keyguardSliceProvider, this.nextAlarmControllerImplProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMAlarmManager(keyguardSliceProvider, (AlarmManager) DaggerTvGlobalRootComponent.this.provideAlarmManagerProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMContentResolver(keyguardSliceProvider, (ContentResolver) DaggerTvGlobalRootComponent.this.provideContentResolverProvider.get());
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
                this.factoryProvider = NotificationTapHelper_Factory_Factory.create(TvSysUIComponentImpl.this.falsingManagerProxyProvider, TvSysUIComponentImpl.this.provideMainDelayableExecutorProvider);
                ExpandableViewController_Factory create = ExpandableViewController_Factory.create(this.expandableNotificationRowProvider);
                this.expandableViewControllerProvider = create;
                ExpandableOutlineViewController_Factory create2 = ExpandableOutlineViewController_Factory.create(this.expandableNotificationRowProvider, create);
                this.expandableOutlineViewControllerProvider = create2;
                this.activatableNotificationViewControllerProvider = ActivatableNotificationViewController_Factory.create(this.expandableNotificationRowProvider, this.factoryProvider, create2, DaggerTvGlobalRootComponent.this.provideAccessibilityManagerProvider, TvSysUIComponentImpl.this.falsingManagerProxyProvider, TvSysUIComponentImpl.this.falsingCollectorImplProvider);
                Factory create3 = InstanceFactory.create(notificationEntry);
                this.notificationEntryProvider = create3;
                this.provideStatusBarNotificationProvider = ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory.create(create3);
                this.provideAppNameProvider = ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideAppNameFactory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.provideStatusBarNotificationProvider);
                this.provideNotificationKeyProvider = ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideNotificationKeyFactory.create(this.provideStatusBarNotificationProvider);
                this.onExpandClickListenerProvider = InstanceFactory.create(onExpandClickListener);
                this.expandableNotificationRowControllerProvider = DoubleCheck.provider(ExpandableNotificationRowController_Factory.create(this.expandableNotificationRowProvider, this.listContainerProvider, this.activatableNotificationViewControllerProvider, TvSysUIComponentImpl.this.provideNotificationMediaManagerProvider, TvSysUIComponentImpl.this.providePluginManagerProvider, TvSysUIComponentImpl.this.bindSystemClockProvider, this.provideAppNameProvider, this.provideNotificationKeyProvider, TvSysUIComponentImpl.this.keyguardBypassControllerProvider, TvSysUIComponentImpl.this.provideGroupMembershipManagerProvider, TvSysUIComponentImpl.this.provideGroupExpansionManagerProvider, TvSysUIComponentImpl.this.rowContentBindStageProvider, TvSysUIComponentImpl.this.provideNotificationLoggerProvider, TvSysUIComponentImpl.this.provideHeadsUpManagerPhoneProvider, this.onExpandClickListenerProvider, TvSysUIComponentImpl.this.statusBarStateControllerImplProvider, TvSysUIComponentImpl.this.provideNotificationGutsManagerProvider, TvSysUIComponentImpl.this.provideAllowNotificationLongPressProvider, TvSysUIComponentImpl.this.provideOnUserInteractionCallbackProvider, TvSysUIComponentImpl.this.falsingManagerProxyProvider, TvSysUIComponentImpl.this.falsingCollectorImplProvider, TvSysUIComponentImpl.this.peopleNotificationIdentifierImplProvider, TvSysUIComponentImpl.this.provideBubblesManagerProvider));
            }

            @Override // com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent
            public ExpandableNotificationRowController getExpandableNotificationRowController() {
                return this.expandableNotificationRowControllerProvider.get();
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
                Provider<ViewGroup> provider = DoubleCheck.provider(KeyguardBouncerModule_ProvidesRootViewFactory.create(TvSysUIComponentImpl.this.providerLayoutInflaterProvider));
                this.providesRootViewProvider = provider;
                this.keyguardRootViewControllerProvider = DoubleCheck.provider(KeyguardRootViewController_Factory.create(provider));
                Provider<KeyguardHostView> provider2 = DoubleCheck.provider(KeyguardBouncerModule_ProvidesKeyguardHostViewFactory.create(this.providesRootViewProvider));
                this.providesKeyguardHostViewProvider = provider2;
                this.providesKeyguardSecurityContainerProvider = DoubleCheck.provider(KeyguardBouncerModule_ProvidesKeyguardSecurityContainerFactory.create(provider2));
                this.factoryProvider = DoubleCheck.provider(AdminSecondaryLockScreenController_Factory_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.providesKeyguardSecurityContainerProvider, TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider));
                this.providesKeyguardSecurityViewFlipperProvider = DoubleCheck.provider(KeyguardBouncerModule_ProvidesKeyguardSecurityViewFlipperFactory.create(this.providesKeyguardSecurityContainerProvider));
                this.liftToActivateListenerProvider = LiftToActivateListener_Factory.create(DaggerTvGlobalRootComponent.this.provideAccessibilityManagerProvider);
                this.factoryProvider2 = EmergencyButtonController_Factory_Factory.create(TvSysUIComponentImpl.this.provideConfigurationControllerProvider, TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider, DaggerTvGlobalRootComponent.this.provideTelephonyManagerProvider, DaggerTvGlobalRootComponent.this.providePowerManagerProvider, DaggerTvGlobalRootComponent.this.provideActivityTaskManagerProvider, TvSysUIComponentImpl.this.shadeControllerImplProvider, DaggerTvGlobalRootComponent.this.provideTelecomManagerProvider, TvSysUIComponentImpl.this.provideMetricsLoggerProvider);
                this.factoryProvider3 = KeyguardInputViewController_Factory_Factory.create(TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider, TvSysUIComponentImpl.this.provideLockPatternUtilsProvider, DaggerTvGlobalRootComponent.this.provideLatencyTrackerProvider, TvSysUIComponentImpl.this.factoryProvider2, DaggerTvGlobalRootComponent.this.provideInputMethodManagerProvider, TvSysUIComponentImpl.this.provideMainDelayableExecutorProvider, DaggerTvGlobalRootComponent.this.provideResourcesProvider, this.liftToActivateListenerProvider, DaggerTvGlobalRootComponent.this.provideTelephonyManagerProvider, TvSysUIComponentImpl.this.falsingCollectorImplProvider, this.factoryProvider2);
                this.keyguardSecurityViewFlipperControllerProvider = DoubleCheck.provider(KeyguardSecurityViewFlipperController_Factory.create(this.providesKeyguardSecurityViewFlipperProvider, TvSysUIComponentImpl.this.providerLayoutInflaterProvider, this.factoryProvider3, this.factoryProvider2));
                this.factoryProvider4 = KeyguardSecurityContainerController_Factory_Factory.create(this.providesKeyguardSecurityContainerProvider, this.factoryProvider, TvSysUIComponentImpl.this.provideLockPatternUtilsProvider, TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider, TvSysUIComponentImpl.this.keyguardSecurityModelProvider, TvSysUIComponentImpl.this.provideMetricsLoggerProvider, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider, TvSysUIComponentImpl.this.keyguardStateControllerImplProvider, this.keyguardSecurityViewFlipperControllerProvider, TvSysUIComponentImpl.this.provideConfigurationControllerProvider);
                this.keyguardHostViewControllerProvider = DoubleCheck.provider(KeyguardHostViewController_Factory.create(this.providesKeyguardHostViewProvider, TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider, DaggerTvGlobalRootComponent.this.provideAudioManagerProvider, DaggerTvGlobalRootComponent.this.provideTelephonyManagerProvider, TvSysUIComponentImpl.this.providesViewMediatorCallbackProvider, this.factoryProvider4));
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
            private Provider<KeyguardClockSwitch> getKeyguardClockSwitchProvider;
            private Provider<KeyguardSliceView> getKeyguardSliceViewProvider;
            private Provider<KeyguardSliceViewController> keyguardSliceViewControllerProvider;
            private final KeyguardStatusView presentation;
            private Provider<KeyguardStatusView> presentationProvider;

            private KeyguardStatusViewComponentImpl(KeyguardStatusView keyguardStatusView) {
                this.presentation = keyguardStatusView;
                initialize(keyguardStatusView);
            }

            private KeyguardClockSwitch keyguardClockSwitch() {
                return KeyguardStatusViewModule_GetKeyguardClockSwitchFactory.getKeyguardClockSwitch(this.presentation);
            }

            private void initialize(KeyguardStatusView keyguardStatusView) {
                Factory create = InstanceFactory.create(keyguardStatusView);
                this.presentationProvider = create;
                KeyguardStatusViewModule_GetKeyguardClockSwitchFactory create2 = KeyguardStatusViewModule_GetKeyguardClockSwitchFactory.create(create);
                this.getKeyguardClockSwitchProvider = create2;
                KeyguardStatusViewModule_GetKeyguardSliceViewFactory create3 = KeyguardStatusViewModule_GetKeyguardSliceViewFactory.create(create2);
                this.getKeyguardSliceViewProvider = create3;
                this.keyguardSliceViewControllerProvider = DoubleCheck.provider(KeyguardSliceViewController_Factory.create(create3, TvSysUIComponentImpl.this.activityStarterDelegateProvider, TvSysUIComponentImpl.this.provideConfigurationControllerProvider, TvSysUIComponentImpl.this.tunerServiceImplProvider, TvSysUIComponentImpl.this.dumpManagerProvider));
            }

            @Override // com.android.keyguard.dagger.KeyguardStatusViewComponent
            public KeyguardClockSwitchController getKeyguardClockSwitchController() {
                return new KeyguardClockSwitchController(keyguardClockSwitch(), (StatusBarStateController) TvSysUIComponentImpl.this.statusBarStateControllerImplProvider.get(), (SysuiColorExtractor) TvSysUIComponentImpl.this.sysuiColorExtractorProvider.get(), (ClockManager) TvSysUIComponentImpl.this.clockManagerProvider.get(), this.keyguardSliceViewControllerProvider.get(), (NotificationIconAreaController) TvSysUIComponentImpl.this.notificationIconAreaControllerProvider.get(), (BroadcastDispatcher) TvSysUIComponentImpl.this.providesBroadcastDispatcherProvider.get(), (BatteryController) TvSysUIComponentImpl.this.provideBatteryControllerProvider.get(), (KeyguardUpdateMonitor) TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider.get(), (KeyguardBypassController) TvSysUIComponentImpl.this.keyguardBypassControllerProvider.get(), (LockscreenSmartspaceController) TvSysUIComponentImpl.this.lockscreenSmartspaceControllerProvider.get(), (KeyguardUnlockAnimationController) TvSysUIComponentImpl.this.keyguardUnlockAnimationControllerProvider.get(), (SmartspaceTransitionController) TvSysUIComponentImpl.this.provideSmartspaceTransitionControllerProvider.get());
            }

            @Override // com.android.keyguard.dagger.KeyguardStatusViewComponent
            public KeyguardStatusViewController getKeyguardStatusViewController() {
                return new KeyguardStatusViewController(this.presentation, this.keyguardSliceViewControllerProvider.get(), getKeyguardClockSwitchController(), (KeyguardStateController) TvSysUIComponentImpl.this.keyguardStateControllerImplProvider.get(), (KeyguardUpdateMonitor) TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider.get(), (ConfigurationController) TvSysUIComponentImpl.this.provideConfigurationControllerProvider.get(), (DozeParameters) TvSysUIComponentImpl.this.dozeParametersProvider.get(), (KeyguardUnlockAnimationController) TvSysUIComponentImpl.this.keyguardUnlockAnimationControllerProvider.get(), (SmartspaceTransitionController) TvSysUIComponentImpl.this.provideSmartspaceTransitionControllerProvider.get(), (UnlockedScreenOffAnimationController) TvSysUIComponentImpl.this.unlockedScreenOffAnimationControllerProvider.get());
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
                this.sectionHeaderNodeControllerImplProvider = DoubleCheck.provider(SectionHeaderNodeControllerImpl_Factory.create(this.nodeLabelProvider, TvSysUIComponentImpl.this.providerLayoutInflaterProvider, this.headerTextProvider, TvSysUIComponentImpl.this.activityStarterDelegateProvider, this.clickIntentActionProvider));
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
            private final AttributeSet attributeSet;
            private final Context context;

            private ViewInstanceCreatorImpl(Context context, AttributeSet attributeSet) {
                this.context = context;
                this.attributeSet = attributeSet;
            }

            @Override // com.android.systemui.util.InjectionInflationController.ViewInstanceCreator
            public NotificationStackScrollLayout createNotificationStackScrollLayout() {
                return new NotificationStackScrollLayout(this.context, this.attributeSet, TvSysUIComponentImpl.this.notificationSectionsManager(), (GroupMembershipManager) TvSysUIComponentImpl.this.provideGroupMembershipManagerProvider.get(), (GroupExpansionManager) TvSysUIComponentImpl.this.provideGroupExpansionManagerProvider.get(), (AmbientState) TvSysUIComponentImpl.this.ambientStateProvider.get(), (FeatureFlags) TvSysUIComponentImpl.this.featureFlagsProvider.get(), (UnlockedScreenOffAnimationController) TvSysUIComponentImpl.this.unlockedScreenOffAnimationControllerProvider.get());
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
                this.factoryProvider = NotificationTapHelper_Factory_Factory.create(TvSysUIComponentImpl.this.falsingManagerProxyProvider, TvSysUIComponentImpl.this.provideMainDelayableExecutorProvider);
                ExpandableViewController_Factory create = ExpandableViewController_Factory.create(this.notificationShelfProvider);
                this.expandableViewControllerProvider = create;
                ExpandableOutlineViewController_Factory create2 = ExpandableOutlineViewController_Factory.create(this.notificationShelfProvider, create);
                this.expandableOutlineViewControllerProvider = create2;
                ActivatableNotificationViewController_Factory create3 = ActivatableNotificationViewController_Factory.create(this.notificationShelfProvider, this.factoryProvider, create2, DaggerTvGlobalRootComponent.this.provideAccessibilityManagerProvider, TvSysUIComponentImpl.this.falsingManagerProxyProvider, TvSysUIComponentImpl.this.falsingCollectorImplProvider);
                this.activatableNotificationViewControllerProvider = create3;
                this.notificationShelfControllerProvider = DoubleCheck.provider(NotificationShelfController_Factory.create(this.notificationShelfProvider, create3, TvSysUIComponentImpl.this.keyguardBypassControllerProvider, TvSysUIComponentImpl.this.statusBarStateControllerImplProvider));
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
                this.builderProvider = FlingAnimationUtils_Builder_Factory.create(DaggerTvGlobalRootComponent.this.provideDisplayMetricsProvider);
                this.builderProvider2 = NotificationSwipeHelper_Builder_Factory.create(DaggerTvGlobalRootComponent.this.provideResourcesProvider, DaggerTvGlobalRootComponent.this.provideViewConfigurationProvider, TvSysUIComponentImpl.this.falsingManagerProxyProvider);
                this.notificationStackScrollLayoutControllerProvider = DoubleCheck.provider(NotificationStackScrollLayoutController_Factory.create(TvSysUIComponentImpl.this.provideAllowNotificationLongPressProvider, TvSysUIComponentImpl.this.provideNotificationGutsManagerProvider, TvSysUIComponentImpl.this.provideHeadsUpManagerPhoneProvider, TvSysUIComponentImpl.this.notificationRoundnessManagerProvider, TvSysUIComponentImpl.this.tunerServiceImplProvider, TvSysUIComponentImpl.this.dynamicPrivacyControllerProvider, TvSysUIComponentImpl.this.provideConfigurationControllerProvider, TvSysUIComponentImpl.this.statusBarStateControllerImplProvider, TvSysUIComponentImpl.this.keyguardMediaControllerProvider, TvSysUIComponentImpl.this.keyguardBypassControllerProvider, TvSysUIComponentImpl.this.zenModeControllerImplProvider, TvSysUIComponentImpl.this.sysuiColorExtractorProvider, TvSysUIComponentImpl.this.notificationLockscreenUserManagerImplProvider, TvSysUIComponentImpl.this.provideMetricsLoggerProvider, TvSysUIComponentImpl.this.falsingCollectorImplProvider, TvSysUIComponentImpl.this.falsingManagerProxyProvider, DaggerTvGlobalRootComponent.this.provideResourcesProvider, this.builderProvider2, TvSysUIComponentImpl.this.provideStatusBarProvider, TvSysUIComponentImpl.this.scrimControllerProvider, TvSysUIComponentImpl.this.notificationGroupManagerLegacyProvider, TvSysUIComponentImpl.this.provideGroupExpansionManagerProvider, TvSysUIComponentImpl.this.providesSilentHeaderControllerProvider, TvSysUIComponentImpl.this.featureFlagsProvider, TvSysUIComponentImpl.this.notifPipelineProvider, TvSysUIComponentImpl.this.notifCollectionProvider, TvSysUIComponentImpl.this.provideNotificationEntryManagerProvider, TvSysUIComponentImpl.this.lockscreenShadeTransitionControllerProvider, DaggerTvGlobalRootComponent.this.provideIStatusBarServiceProvider, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider, TvSysUIComponentImpl.this.foregroundServiceDismissalFeatureControllerProvider, TvSysUIComponentImpl.this.foregroundServiceSectionControllerProvider, TvSysUIComponentImpl.this.providerLayoutInflaterProvider, TvSysUIComponentImpl.this.provideNotificationRemoteInputManagerProvider, TvSysUIComponentImpl.this.provideVisualStabilityManagerProvider, TvSysUIComponentImpl.this.shadeControllerImplProvider));
                Provider<LockIconView> provider = DoubleCheck.provider(StatusBarViewModule_GetLockIconViewFactory.create(this.statusBarWindowViewProvider));
                this.getLockIconViewProvider = provider;
                this.lockIconViewControllerProvider = DoubleCheck.provider(LockIconViewController_Factory.create(provider, TvSysUIComponentImpl.this.statusBarStateControllerImplProvider, TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider, TvSysUIComponentImpl.this.statusBarKeyguardViewManagerProvider, TvSysUIComponentImpl.this.keyguardStateControllerImplProvider, TvSysUIComponentImpl.this.falsingManagerProxyProvider, TvSysUIComponentImpl.this.authControllerProvider, TvSysUIComponentImpl.this.dumpManagerProvider, DaggerTvGlobalRootComponent.this.provideAccessibilityManagerProvider, TvSysUIComponentImpl.this.provideConfigurationControllerProvider, TvSysUIComponentImpl.this.provideMainDelayableExecutorProvider, DaggerTvGlobalRootComponent.this.provideVibratorProvider));
                Provider<TapAgainView> provider2 = DoubleCheck.provider(StatusBarViewModule_GetTapAgainViewFactory.create(this.getNotificationPanelViewProvider));
                this.getTapAgainViewProvider = provider2;
                this.tapAgainViewControllerProvider = DoubleCheck.provider(TapAgainViewController_Factory.create(provider2, TvSysUIComponentImpl.this.provideMainDelayableExecutorProvider, TvSysUIComponentImpl.this.provideConfigurationControllerProvider, FalsingModule_ProvidesDoubleTapTimeoutMsFactory.create()));
                this.notificationPanelViewControllerProvider = DoubleCheck.provider(NotificationPanelViewController_Factory.create(this.getNotificationPanelViewProvider, DaggerTvGlobalRootComponent.this.provideResourcesProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, TvSysUIComponentImpl.this.providerLayoutInflaterProvider, TvSysUIComponentImpl.this.notificationWakeUpCoordinatorProvider, TvSysUIComponentImpl.this.pulseExpansionHandlerProvider, TvSysUIComponentImpl.this.dynamicPrivacyControllerProvider, TvSysUIComponentImpl.this.keyguardBypassControllerProvider, TvSysUIComponentImpl.this.falsingManagerProxyProvider, TvSysUIComponentImpl.this.falsingCollectorImplProvider, TvSysUIComponentImpl.this.notificationLockscreenUserManagerImplProvider, TvSysUIComponentImpl.this.provideNotificationEntryManagerProvider, TvSysUIComponentImpl.this.keyguardStateControllerImplProvider, TvSysUIComponentImpl.this.statusBarStateControllerImplProvider, TvSysUIComponentImpl.this.dozeLogProvider, TvSysUIComponentImpl.this.dozeParametersProvider, TvSysUIComponentImpl.this.provideCommandQueueProvider, TvSysUIComponentImpl.this.vibratorHelperProvider, DaggerTvGlobalRootComponent.this.provideLatencyTrackerProvider, DaggerTvGlobalRootComponent.this.providePowerManagerProvider, DaggerTvGlobalRootComponent.this.provideAccessibilityManagerProvider, DaggerTvGlobalRootComponent.this.provideDisplayIdProvider, TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider, TvSysUIComponentImpl.this.provideMetricsLoggerProvider, DaggerTvGlobalRootComponent.this.provideActivityManagerProvider, TvSysUIComponentImpl.this.provideConfigurationControllerProvider, this.builderProvider, TvSysUIComponentImpl.this.statusBarTouchableRegionManagerProvider, TvSysUIComponentImpl.this.conversationNotificationManagerProvider, TvSysUIComponentImpl.this.mediaHierarchyManagerProvider, TvSysUIComponentImpl.this.biometricUnlockControllerProvider, TvSysUIComponentImpl.this.statusBarKeyguardViewManagerProvider, this.notificationStackScrollLayoutControllerProvider, TvSysUIComponentImpl.this.keyguardStatusViewComponentFactoryProvider, TvSysUIComponentImpl.this.keyguardQsUserSwitchComponentFactoryProvider, TvSysUIComponentImpl.this.keyguardUserSwitcherComponentFactoryProvider, TvSysUIComponentImpl.this.keyguardStatusBarViewComponentFactoryProvider, TvSysUIComponentImpl.this.lockscreenShadeTransitionControllerProvider, TvSysUIComponentImpl.this.qSDetailDisplayerProvider, TvSysUIComponentImpl.this.notificationGroupManagerLegacyProvider, TvSysUIComponentImpl.this.notificationIconAreaControllerProvider, TvSysUIComponentImpl.this.authControllerProvider, TvSysUIComponentImpl.this.scrimControllerProvider, DaggerTvGlobalRootComponent.this.provideUserManagerProvider, TvSysUIComponentImpl.this.mediaDataManagerProvider, TvSysUIComponentImpl.this.notificationShadeDepthControllerProvider, TvSysUIComponentImpl.this.ambientStateProvider, this.lockIconViewControllerProvider, TvSysUIComponentImpl.this.featureFlagsProvider, TvSysUIComponentImpl.this.keyguardMediaControllerProvider, TvSysUIComponentImpl.this.privacyDotViewControllerProvider, this.tapAgainViewControllerProvider, TvSysUIComponentImpl.this.navigationModeControllerProvider, TvSysUIComponentImpl.this.fragmentServiceProvider, DaggerTvGlobalRootComponent.this.provideContentResolverProvider, TvSysUIComponentImpl.this.quickAccessWalletControllerProvider, TvSysUIComponentImpl.this.recordingControllerProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider, TvSysUIComponentImpl.this.secureSettingsImplProvider, TvSysUIComponentImpl.this.unlockedScreenOffAnimationControllerProvider, TvSysUIComponentImpl.this.provideNotificationRemoteInputManagerProvider, TvSysUIComponentImpl.this.controlsComponentProvider));
                this.getAuthRippleViewProvider = DoubleCheck.provider(StatusBarViewModule_GetAuthRippleViewFactory.create(this.statusBarWindowViewProvider));
                this.authRippleControllerProvider = DoubleCheck.provider(AuthRippleController_Factory.create(TvSysUIComponentImpl.this.provideStatusBarProvider, DaggerTvGlobalRootComponent.this.contextProvider, TvSysUIComponentImpl.this.authControllerProvider, TvSysUIComponentImpl.this.provideConfigurationControllerProvider, TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider, TvSysUIComponentImpl.this.commandRegistryProvider, TvSysUIComponentImpl.this.notificationShadeWindowControllerImplProvider, TvSysUIComponentImpl.this.keyguardBypassControllerProvider, TvSysUIComponentImpl.this.biometricUnlockControllerProvider, this.getAuthRippleViewProvider));
            }

            @Override // com.android.systemui.statusbar.phone.dagger.StatusBarComponent
            public NotificationShadeWindowViewController getNotificationShadeWindowViewController() {
                return new NotificationShadeWindowViewController((InjectionInflationController) TvSysUIComponentImpl.this.injectionInflationControllerProvider.get(), (NotificationWakeUpCoordinator) TvSysUIComponentImpl.this.notificationWakeUpCoordinatorProvider.get(), (PulseExpansionHandler) TvSysUIComponentImpl.this.pulseExpansionHandlerProvider.get(), (DynamicPrivacyController) TvSysUIComponentImpl.this.dynamicPrivacyControllerProvider.get(), (KeyguardBypassController) TvSysUIComponentImpl.this.keyguardBypassControllerProvider.get(), (LockscreenShadeTransitionController) TvSysUIComponentImpl.this.lockscreenShadeTransitionControllerProvider.get(), (FalsingCollector) TvSysUIComponentImpl.this.falsingCollectorImplProvider.get(), (PluginManager) TvSysUIComponentImpl.this.providePluginManagerProvider.get(), (TunerService) TvSysUIComponentImpl.this.tunerServiceImplProvider.get(), (NotificationLockscreenUserManager) TvSysUIComponentImpl.this.notificationLockscreenUserManagerImplProvider.get(), (NotificationEntryManager) TvSysUIComponentImpl.this.provideNotificationEntryManagerProvider.get(), (KeyguardStateController) TvSysUIComponentImpl.this.keyguardStateControllerImplProvider.get(), (SysuiStatusBarStateController) TvSysUIComponentImpl.this.statusBarStateControllerImplProvider.get(), (DozeLog) TvSysUIComponentImpl.this.dozeLogProvider.get(), (DozeParameters) TvSysUIComponentImpl.this.dozeParametersProvider.get(), (CommandQueue) TvSysUIComponentImpl.this.provideCommandQueueProvider.get(), (ShadeController) TvSysUIComponentImpl.this.shadeControllerImplProvider.get(), (DockManager) TvSysUIComponentImpl.this.dockManagerImplProvider.get(), (NotificationShadeDepthController) TvSysUIComponentImpl.this.notificationShadeDepthControllerProvider.get(), this.statusBarWindowView, this.notificationPanelViewControllerProvider.get(), (SuperStatusBarViewFactory) TvSysUIComponentImpl.this.superStatusBarViewFactoryProvider.get(), this.notificationStackScrollLayoutControllerProvider.get(), (StatusBarKeyguardViewManager) TvSysUIComponentImpl.this.statusBarKeyguardViewManagerProvider.get());
            }

            @Override // com.android.systemui.statusbar.phone.dagger.StatusBarComponent
            public StatusBarWindowController getStatusBarWindowController() {
                return (StatusBarWindowController) TvSysUIComponentImpl.this.statusBarWindowControllerProvider.get();
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
            private Provider<DozeAuthRemover> dozeAuthRemoverProvider;
            private Provider<DozeDockHandler> dozeDockHandlerProvider;
            private Provider<DozeFalsingManagerAdapter> dozeFalsingManagerAdapterProvider;
            private Provider<DozeMachine> dozeMachineProvider;
            private Provider<DozeMachine.Service> dozeMachineServiceProvider;
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
                this.dozeMachineServiceProvider = create;
                this.providesWrappedServiceProvider = DoubleCheck.provider(DozeModule_ProvidesWrappedServiceFactory.create(create, TvSysUIComponentImpl.this.dozeServiceHostProvider, TvSysUIComponentImpl.this.dozeParametersProvider));
                this.providesDozeWakeLockProvider = DoubleCheck.provider(DozeModule_ProvidesDozeWakeLockFactory.create(TvSysUIComponentImpl.this.builderProvider2, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider));
                this.dozePauserProvider = DoubleCheck.provider(DozePauser_Factory.create(DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, DaggerTvGlobalRootComponent.this.provideAlarmManagerProvider, TvSysUIComponentImpl.this.provideAlwaysOnDisplayPolicyProvider));
                this.dozeFalsingManagerAdapterProvider = DoubleCheck.provider(DozeFalsingManagerAdapter_Factory.create(TvSysUIComponentImpl.this.falsingCollectorImplProvider));
                this.proximityCheckProvider = ProximitySensor_ProximityCheck_Factory.create(TvSysUIComponentImpl.this.proximitySensorProvider, TvSysUIComponentImpl.this.provideMainDelayableExecutorProvider);
                this.dozeTriggersProvider = DoubleCheck.provider(DozeTriggers_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, TvSysUIComponentImpl.this.dozeServiceHostProvider, TvSysUIComponentImpl.this.provideAmbientDisplayConfigurationProvider, TvSysUIComponentImpl.this.dozeParametersProvider, TvSysUIComponentImpl.this.asyncSensorManagerProvider, this.providesDozeWakeLockProvider, TvSysUIComponentImpl.this.dockManagerImplProvider, TvSysUIComponentImpl.this.proximitySensorProvider, this.proximityCheckProvider, TvSysUIComponentImpl.this.dozeLogProvider, TvSysUIComponentImpl.this.providesBroadcastDispatcherProvider, TvSysUIComponentImpl.this.secureSettingsImplProvider, TvSysUIComponentImpl.this.authControllerProvider, TvSysUIComponentImpl.this.provideMainDelayableExecutorProvider, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider));
                this.dozeUiProvider = DoubleCheck.provider(DozeUi_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideAlarmManagerProvider, this.providesDozeWakeLockProvider, TvSysUIComponentImpl.this.dozeServiceHostProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, TvSysUIComponentImpl.this.dozeParametersProvider, TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider, TvSysUIComponentImpl.this.dozeLogProvider, TvSysUIComponentImpl.this.tunerServiceImplProvider, TvSysUIComponentImpl.this.statusBarStateControllerImplProvider, TvSysUIComponentImpl.this.provideConfigurationControllerProvider));
                this.dozeScreenStateProvider = DoubleCheck.provider(DozeScreenState_Factory.create(this.providesWrappedServiceProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, TvSysUIComponentImpl.this.dozeServiceHostProvider, TvSysUIComponentImpl.this.dozeParametersProvider, this.providesDozeWakeLockProvider));
                this.providesBrightnessSensorProvider = DozeModule_ProvidesBrightnessSensorFactory.create(TvSysUIComponentImpl.this.asyncSensorManagerProvider, DaggerTvGlobalRootComponent.this.contextProvider);
                this.dozeScreenBrightnessProvider = DoubleCheck.provider(DozeScreenBrightness_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.providesWrappedServiceProvider, TvSysUIComponentImpl.this.asyncSensorManagerProvider, this.providesBrightnessSensorProvider, TvSysUIComponentImpl.this.dozeServiceHostProvider, TvSysUIComponentImpl.this.provideHandlerProvider, TvSysUIComponentImpl.this.provideAlwaysOnDisplayPolicyProvider, TvSysUIComponentImpl.this.wakefulnessLifecycleProvider, TvSysUIComponentImpl.this.dozeParametersProvider));
                this.dozeWallpaperStateProvider = DoubleCheck.provider(DozeWallpaperState_Factory.create(FrameworkServicesModule_ProvideIWallPaperManagerFactory.create(), TvSysUIComponentImpl.this.biometricUnlockControllerProvider, TvSysUIComponentImpl.this.dozeParametersProvider));
                this.dozeDockHandlerProvider = DoubleCheck.provider(DozeDockHandler_Factory.create(TvSysUIComponentImpl.this.provideAmbientDisplayConfigurationProvider, TvSysUIComponentImpl.this.dockManagerImplProvider));
                Provider<DozeAuthRemover> provider = DoubleCheck.provider(DozeAuthRemover_Factory.create(TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider));
                this.dozeAuthRemoverProvider = provider;
                this.providesDozeMachinePartesProvider = DozeModule_ProvidesDozeMachinePartesFactory.create(this.dozePauserProvider, this.dozeFalsingManagerAdapterProvider, this.dozeTriggersProvider, this.dozeUiProvider, this.dozeScreenStateProvider, this.dozeScreenBrightnessProvider, this.dozeWallpaperStateProvider, this.dozeDockHandlerProvider, provider);
                this.dozeMachineProvider = DoubleCheck.provider(DozeMachine_Factory.create(this.providesWrappedServiceProvider, TvSysUIComponentImpl.this.provideAmbientDisplayConfigurationProvider, this.providesDozeWakeLockProvider, TvSysUIComponentImpl.this.wakefulnessLifecycleProvider, TvSysUIComponentImpl.this.provideBatteryControllerProvider, TvSysUIComponentImpl.this.dozeLogProvider, TvSysUIComponentImpl.this.dockManagerImplProvider, TvSysUIComponentImpl.this.dozeServiceHostProvider, this.providesDozeMachinePartesProvider));
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
                return new QSFragment((RemoteInputQuickSettingsDisabler) TvSysUIComponentImpl.this.remoteInputQuickSettingsDisablerProvider.get(), (InjectionInflationController) TvSysUIComponentImpl.this.injectionInflationControllerProvider.get(), (QSTileHost) TvSysUIComponentImpl.this.qSTileHostProvider.get(), (StatusBarStateController) TvSysUIComponentImpl.this.statusBarStateControllerImplProvider.get(), (CommandQueue) TvSysUIComponentImpl.this.provideCommandQueueProvider.get(), (QSDetailDisplayer) TvSysUIComponentImpl.this.qSDetailDisplayerProvider.get(), (MediaHost) TvSysUIComponentImpl.this.providesQSMediaHostProvider.get(), (MediaHost) TvSysUIComponentImpl.this.providesQuickQSMediaHostProvider.get(), (KeyguardBypassController) TvSysUIComponentImpl.this.keyguardBypassControllerProvider.get(), new QSFragmentComponentFactory(), (FeatureFlags) TvSysUIComponentImpl.this.featureFlagsProvider.get(), (FalsingManager) TvSysUIComponentImpl.this.falsingManagerProxyProvider.get(), (DumpManager) TvSysUIComponentImpl.this.dumpManagerProvider.get());
            }

            @Override // com.android.systemui.fragments.FragmentService.FragmentCreator
            public CollapsedStatusBarFragment createCollapsedStatusBarFragment() {
                return new CollapsedStatusBarFragment((OngoingCallController) TvSysUIComponentImpl.this.provideOngoingCallControllerProvider.get(), (SystemStatusAnimationScheduler) TvSysUIComponentImpl.this.systemStatusAnimationSchedulerProvider.get(), (StatusBarLocationPublisher) TvSysUIComponentImpl.this.statusBarLocationPublisherProvider.get(), (NotificationIconAreaController) TvSysUIComponentImpl.this.notificationIconAreaControllerProvider.get(), (FeatureFlags) TvSysUIComponentImpl.this.featureFlagsProvider.get(), (StatusBarIconController) TvSysUIComponentImpl.this.statusBarIconControllerImplProvider.get(), (KeyguardStateController) TvSysUIComponentImpl.this.keyguardStateControllerImplProvider.get(), (NetworkController) TvSysUIComponentImpl.this.networkControllerImplProvider.get(), (StatusBarStateController) TvSysUIComponentImpl.this.statusBarStateControllerImplProvider.get(), (StatusBar) TvSysUIComponentImpl.this.provideStatusBarProvider.get(), (CommandQueue) TvSysUIComponentImpl.this.provideCommandQueueProvider.get());
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
            private Provider<KeyguardQsUserSwitchController> keyguardQsUserSwitchControllerProvider;
            private Provider<UserAvatarView> userAvatarViewProvider;

            private KeyguardQsUserSwitchComponentImpl(UserAvatarView userAvatarView) {
                initialize(userAvatarView);
            }

            private void initialize(UserAvatarView userAvatarView) {
                Factory create = InstanceFactory.create(userAvatarView);
                this.userAvatarViewProvider = create;
                this.keyguardQsUserSwitchControllerProvider = DoubleCheck.provider(KeyguardQsUserSwitchController_Factory.create(create, DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideResourcesProvider, TvSysUIComponentImpl.this.screenLifecycleProvider, TvSysUIComponentImpl.this.userSwitcherControllerProvider, TvSysUIComponentImpl.this.keyguardStateControllerImplProvider, TvSysUIComponentImpl.this.falsingManagerProxyProvider, TvSysUIComponentImpl.this.provideConfigurationControllerProvider, TvSysUIComponentImpl.this.statusBarStateControllerImplProvider, TvSysUIComponentImpl.this.dozeParametersProvider, TvSysUIComponentImpl.this.adapterProvider, TvSysUIComponentImpl.this.unlockedScreenOffAnimationControllerProvider));
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
            private Provider<KeyguardUserSwitcherController> keyguardUserSwitcherControllerProvider;
            private Provider<KeyguardUserSwitcherView> keyguardUserSwitcherViewProvider;

            private KeyguardUserSwitcherComponentImpl(KeyguardUserSwitcherView keyguardUserSwitcherView) {
                initialize(keyguardUserSwitcherView);
            }

            private void initialize(KeyguardUserSwitcherView keyguardUserSwitcherView) {
                Factory create = InstanceFactory.create(keyguardUserSwitcherView);
                this.keyguardUserSwitcherViewProvider = create;
                this.keyguardUserSwitcherControllerProvider = DoubleCheck.provider(KeyguardUserSwitcherController_Factory.create(create, DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideResourcesProvider, TvSysUIComponentImpl.this.providerLayoutInflaterProvider, TvSysUIComponentImpl.this.screenLifecycleProvider, TvSysUIComponentImpl.this.userSwitcherControllerProvider, TvSysUIComponentImpl.this.keyguardStateControllerImplProvider, TvSysUIComponentImpl.this.statusBarStateControllerImplProvider, TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider, TvSysUIComponentImpl.this.dozeParametersProvider, TvSysUIComponentImpl.this.unlockedScreenOffAnimationControllerProvider));
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
            private Provider<CarrierText> getCarrierTextProvider;
            private final KeyguardStatusBarView view;
            private Provider<KeyguardStatusBarView> viewProvider;

            private KeyguardStatusBarViewComponentImpl(KeyguardStatusBarView keyguardStatusBarView) {
                this.view = keyguardStatusBarView;
                initialize(keyguardStatusBarView);
            }

            private CarrierTextManager.Builder carrierTextManagerBuilder() {
                return new CarrierTextManager.Builder(DaggerTvGlobalRootComponent.this.context, DaggerTvGlobalRootComponent.this.mainResources(), (WifiManager) DaggerTvGlobalRootComponent.this.provideWifiManagerProvider.get(), (TelephonyManager) DaggerTvGlobalRootComponent.this.provideTelephonyManagerProvider.get(), (TelephonyListenerManager) TvSysUIComponentImpl.this.telephonyListenerManagerProvider.get(), (WakefulnessLifecycle) TvSysUIComponentImpl.this.wakefulnessLifecycleProvider.get(), DaggerTvGlobalRootComponent.this.mainExecutor(), (Executor) TvSysUIComponentImpl.this.provideBackgroundExecutorProvider.get(), (KeyguardUpdateMonitor) TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider.get());
            }

            private CarrierTextController carrierTextController() {
                return new CarrierTextController(this.getCarrierTextProvider.get(), carrierTextManagerBuilder(), (KeyguardUpdateMonitor) TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider.get());
            }

            private void initialize(KeyguardStatusBarView keyguardStatusBarView) {
                Factory create = InstanceFactory.create(keyguardStatusBarView);
                this.viewProvider = create;
                this.getCarrierTextProvider = DoubleCheck.provider(KeyguardStatusBarViewModule_GetCarrierTextFactory.create(create));
            }

            @Override // com.android.keyguard.dagger.KeyguardStatusBarViewComponent
            public KeyguardStatusBarViewController getKeyguardStatusBarViewController() {
                return new KeyguardStatusBarViewController(this.view, carrierTextController());
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
            private Provider<QSFragment> qsFragmentProvider;
            private Provider<QuickQSPanelController> quickQSPanelControllerProvider;
            private Provider quickStatusBarHeaderControllerProvider;
            private Provider<TileAdapter> tileAdapterProvider;
            private Provider<TileQueryHelper> tileQueryHelperProvider;

            private QSFragmentComponentImpl(QSFragment qSFragment) {
                initialize(qSFragment);
            }

            private void initialize(QSFragment qSFragment) {
                Factory create = InstanceFactory.create(qSFragment);
                this.qsFragmentProvider = create;
                QSFragmentModule_ProvideRootViewFactory create2 = QSFragmentModule_ProvideRootViewFactory.create(create);
                this.provideRootViewProvider = create2;
                this.provideQSPanelProvider = QSFragmentModule_ProvideQSPanelFactory.create(create2);
                QSFragmentModule_ProvideThemedContextFactory create3 = QSFragmentModule_ProvideThemedContextFactory.create(this.provideRootViewProvider);
                this.provideThemedContextProvider = create3;
                QSFragmentModule_ProvideThemedLayoutInflaterFactory create4 = QSFragmentModule_ProvideThemedLayoutInflaterFactory.create(create3);
                this.provideThemedLayoutInflaterProvider = create4;
                Provider<View> provider = DoubleCheck.provider(QSFragmentModule_ProvidesQSSecurityFooterViewFactory.create(create4, this.provideQSPanelProvider));
                this.providesQSSecurityFooterViewProvider = provider;
                this.qSSecurityFooterProvider = DoubleCheck.provider(QSSecurityFooter_Factory.create(provider, TvSysUIComponentImpl.this.provideUserTrackerProvider, DaggerTvGlobalRootComponent.this.provideMainHandlerProvider, TvSysUIComponentImpl.this.activityStarterDelegateProvider, TvSysUIComponentImpl.this.securityControllerImplProvider, TvSysUIComponentImpl.this.provideBgLooperProvider));
                this.providesQSCutomizerProvider = DoubleCheck.provider(QSFragmentModule_ProvidesQSCutomizerFactory.create(this.provideRootViewProvider));
                this.tileQueryHelperProvider = DoubleCheck.provider(TileQueryHelper_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, TvSysUIComponentImpl.this.provideUserTrackerProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider, TvSysUIComponentImpl.this.provideBackgroundExecutorProvider));
                this.tileAdapterProvider = DoubleCheck.provider(TileAdapter_Factory.create(this.provideThemedContextProvider, TvSysUIComponentImpl.this.qSTileHostProvider, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider));
                this.qSCustomizerControllerProvider = DoubleCheck.provider(QSCustomizerController_Factory.create(this.providesQSCutomizerProvider, this.tileQueryHelperProvider, TvSysUIComponentImpl.this.qSTileHostProvider, this.tileAdapterProvider, TvSysUIComponentImpl.this.screenLifecycleProvider, TvSysUIComponentImpl.this.keyguardStateControllerImplProvider, TvSysUIComponentImpl.this.lightBarControllerProvider, TvSysUIComponentImpl.this.provideConfigurationControllerProvider, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider));
                this.providesQSUsingMediaPlayerProvider = QSFragmentModule_ProvidesQSUsingMediaPlayerFactory.create(DaggerTvGlobalRootComponent.this.contextProvider);
                this.factoryProvider = DoubleCheck.provider(QSTileRevealController_Factory_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, this.qSCustomizerControllerProvider));
                this.factoryProvider2 = BrightnessController_Factory_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, TvSysUIComponentImpl.this.providesBroadcastDispatcherProvider);
                this.qSPanelControllerProvider = DoubleCheck.provider(QSPanelController_Factory.create(this.provideQSPanelProvider, this.qSSecurityFooterProvider, TvSysUIComponentImpl.this.tunerServiceImplProvider, TvSysUIComponentImpl.this.qSTileHostProvider, this.qSCustomizerControllerProvider, this.providesQSUsingMediaPlayerProvider, TvSysUIComponentImpl.this.providesQSMediaHostProvider, this.factoryProvider, TvSysUIComponentImpl.this.dumpManagerProvider, TvSysUIComponentImpl.this.provideMetricsLoggerProvider, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider, TvSysUIComponentImpl.this.qSLoggerProvider, this.factoryProvider2, TvSysUIComponentImpl.this.factoryProvider3, TvSysUIComponentImpl.this.falsingManagerProxyProvider, TvSysUIComponentImpl.this.featureFlagsProvider));
                QSFragmentModule_ProvidesQuickStatusBarHeaderFactory create5 = QSFragmentModule_ProvidesQuickStatusBarHeaderFactory.create(this.provideRootViewProvider);
                this.providesQuickStatusBarHeaderProvider = create5;
                QSFragmentModule_ProvidesQuickQSPanelFactory create6 = QSFragmentModule_ProvidesQuickQSPanelFactory.create(create5);
                this.providesQuickQSPanelProvider = create6;
                Provider<QuickQSPanelController> provider2 = DoubleCheck.provider(QuickQSPanelController_Factory.create(create6, TvSysUIComponentImpl.this.qSTileHostProvider, this.qSCustomizerControllerProvider, this.providesQSUsingMediaPlayerProvider, TvSysUIComponentImpl.this.providesQuickQSMediaHostProvider, TvSysUIComponentImpl.this.provideMetricsLoggerProvider, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider, TvSysUIComponentImpl.this.qSLoggerProvider, TvSysUIComponentImpl.this.dumpManagerProvider, TvSysUIComponentImpl.this.featureFlagsProvider));
                this.quickQSPanelControllerProvider = provider2;
                this.qSAnimatorProvider = DoubleCheck.provider(QSAnimator_Factory.create(this.qsFragmentProvider, this.providesQuickQSPanelProvider, this.providesQuickStatusBarHeaderProvider, this.qSPanelControllerProvider, provider2, TvSysUIComponentImpl.this.qSTileHostProvider, this.qSSecurityFooterProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider, TvSysUIComponentImpl.this.tunerServiceImplProvider, DaggerTvGlobalRootComponent.this.qSExpansionPathInterpolatorProvider));
                this.providesQSContainerImplProvider = QSFragmentModule_ProvidesQSContainerImplFactory.create(this.provideRootViewProvider);
                this.builderProvider = CarrierTextManager_Builder_Factory.create(DaggerTvGlobalRootComponent.this.contextProvider, DaggerTvGlobalRootComponent.this.provideResourcesProvider, DaggerTvGlobalRootComponent.this.provideWifiManagerProvider, DaggerTvGlobalRootComponent.this.provideTelephonyManagerProvider, TvSysUIComponentImpl.this.telephonyListenerManagerProvider, TvSysUIComponentImpl.this.wakefulnessLifecycleProvider, DaggerTvGlobalRootComponent.this.provideMainExecutorProvider, TvSysUIComponentImpl.this.provideBackgroundExecutorProvider, TvSysUIComponentImpl.this.keyguardUpdateMonitorProvider);
                this.builderProvider2 = QSCarrierGroupController_Builder_Factory.create(TvSysUIComponentImpl.this.activityStarterDelegateProvider, TvSysUIComponentImpl.this.provideBgHandlerProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), TvSysUIComponentImpl.this.networkControllerImplProvider, this.builderProvider, DaggerTvGlobalRootComponent.this.contextProvider, TvSysUIComponentImpl.this.carrierConfigTrackerProvider, TvSysUIComponentImpl.this.featureFlagsProvider, TvSysUIComponentImpl.this.subscriptionManagerSlotIndexResolverProvider);
                Provider provider3 = DoubleCheck.provider(QuickStatusBarHeaderController_Factory.create(this.providesQuickStatusBarHeaderProvider, TvSysUIComponentImpl.this.privacyItemControllerProvider, TvSysUIComponentImpl.this.activityStarterDelegateProvider, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider, TvSysUIComponentImpl.this.statusBarIconControllerImplProvider, TvSysUIComponentImpl.this.provideDemoModeControllerProvider, this.quickQSPanelControllerProvider, this.builderProvider2, TvSysUIComponentImpl.this.privacyLoggerProvider, TvSysUIComponentImpl.this.sysuiColorExtractorProvider, TvSysUIComponentImpl.this.privacyDialogControllerProvider, DaggerTvGlobalRootComponent.this.qSExpansionPathInterpolatorProvider, TvSysUIComponentImpl.this.featureFlagsProvider));
                this.quickStatusBarHeaderControllerProvider = provider3;
                this.qSContainerImplControllerProvider = DoubleCheck.provider(QSContainerImplController_Factory.create(this.providesQSContainerImplProvider, this.qSPanelControllerProvider, provider3, TvSysUIComponentImpl.this.provideConfigurationControllerProvider));
                QSFragmentModule_ProvidesQSFooterViewFactory create7 = QSFragmentModule_ProvidesQSFooterViewFactory.create(this.provideRootViewProvider);
                this.providesQSFooterViewProvider = create7;
                QSFragmentModule_ProvidesMultiUserSWitchFactory create8 = QSFragmentModule_ProvidesMultiUserSWitchFactory.create(create7);
                this.providesMultiUserSWitchProvider = create8;
                this.multiUserSwitchControllerProvider = DoubleCheck.provider(MultiUserSwitchController_Factory.create(create8, DaggerTvGlobalRootComponent.this.provideUserManagerProvider, TvSysUIComponentImpl.this.userSwitcherControllerProvider, TvSysUIComponentImpl.this.qSDetailDisplayerProvider, TvSysUIComponentImpl.this.falsingManagerProxyProvider));
                Provider<QSFooterViewController> provider4 = DoubleCheck.provider(QSFooterViewController_Factory.create(this.providesQSFooterViewProvider, DaggerTvGlobalRootComponent.this.provideUserManagerProvider, TvSysUIComponentImpl.this.userInfoControllerImplProvider, TvSysUIComponentImpl.this.activityStarterDelegateProvider, TvSysUIComponentImpl.this.deviceProvisionedControllerImplProvider, TvSysUIComponentImpl.this.provideUserTrackerProvider, this.qSPanelControllerProvider, this.multiUserSwitchControllerProvider, this.quickQSPanelControllerProvider, TvSysUIComponentImpl.this.tunerServiceImplProvider, TvSysUIComponentImpl.this.provideMetricsLoggerProvider, TvSysUIComponentImpl.this.falsingManagerProxyProvider, TvSysUIComponentImpl.this.isPMLiteEnabledProvider, TvSysUIComponentImpl.this.globalActionsDialogLiteProvider, DaggerTvGlobalRootComponent.this.provideUiEventLoggerProvider));
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
