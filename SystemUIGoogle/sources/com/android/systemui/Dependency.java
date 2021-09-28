package com.android.systemui;

import android.app.AlarmManager;
import android.app.INotificationManager;
import android.app.IWallpaperManager;
import android.hardware.SensorPrivacyManager;
import android.hardware.display.NightDisplayListener;
import android.os.Handler;
import android.os.Looper;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.IWindowManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.util.Preconditions;
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
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class Dependency {
    private static Dependency sDependency;
    Lazy<AccessibilityButtonTargetsObserver> mAccessibilityButtonListController;
    Lazy<AccessibilityButtonModeObserver> mAccessibilityButtonModeObserver;
    Lazy<AccessibilityController> mAccessibilityController;
    Lazy<AccessibilityFloatingMenuController> mAccessibilityFloatingMenuController;
    Lazy<AccessibilityManagerWrapper> mAccessibilityManagerWrapper;
    Lazy<ActivityManagerWrapper> mActivityManagerWrapper;
    Lazy<ActivityStarter> mActivityStarter;
    Lazy<AlarmManager> mAlarmManager;
    Lazy<AppOpsController> mAppOpsController;
    Lazy<AssistManager> mAssistManager;
    Lazy<AsyncSensorManager> mAsyncSensorManager;
    Lazy<AutoHideController> mAutoHideController;
    Lazy<Executor> mBackgroundExecutor;
    Lazy<BatteryController> mBatteryController;
    Lazy<Handler> mBgHandler;
    Lazy<Looper> mBgLooper;
    Lazy<BluetoothController> mBluetoothController;
    Lazy<BroadcastDispatcher> mBroadcastDispatcher;
    Lazy<CastController> mCastController;
    Lazy<ClockManager> mClockManager;
    Lazy<CommandQueue> mCommandQueue;
    Lazy<ConfigurationController> mConfigurationController;
    Lazy<StatusBarContentInsetsProvider> mContentInsetsProviderLazy;
    Lazy<DarkIconDispatcher> mDarkIconDispatcher;
    Lazy<DataSaverController> mDataSaverController;
    Lazy<DeviceConfigProxy> mDeviceConfigProxy;
    Lazy<DevicePolicyManagerWrapper> mDevicePolicyManagerWrapper;
    Lazy<DeviceProvisionedController> mDeviceProvisionedController;
    Lazy<DisplayMetrics> mDisplayMetrics;
    Lazy<DockManager> mDockManager;
    Lazy<DozeParameters> mDozeParameters;
    DumpManager mDumpManager;
    Lazy<EdgeBackGestureHandler.Factory> mEdgeBackGestureHandlerFactoryLazy;
    Lazy<EnhancedEstimates> mEnhancedEstimates;
    Lazy<ExtensionController> mExtensionController;
    Lazy<FeatureFlags> mFeatureFlagsLazy;
    Lazy<FlashlightController> mFlashlightController;
    Lazy<ForegroundServiceController> mForegroundServiceController;
    Lazy<ForegroundServiceNotificationListener> mForegroundServiceNotificationListener;
    Lazy<FragmentService> mFragmentService;
    Lazy<GarbageMonitor> mGarbageMonitor;
    Lazy<HotspotController> mHotspotController;
    Lazy<INotificationManager> mINotificationManager;
    Lazy<IStatusBarService> mIStatusBarService;
    Lazy<IWindowManager> mIWindowManager;
    Lazy<KeyguardDismissUtil> mKeyguardDismissUtil;
    Lazy<NotificationEntryManager.KeyguardEnvironment> mKeyguardEnvironment;
    Lazy<KeyguardStateController> mKeyguardMonitor;
    Lazy<KeyguardSecurityModel> mKeyguardSecurityModel;
    Lazy<KeyguardUpdateMonitor> mKeyguardUpdateMonitor;
    Lazy<LeakDetector> mLeakDetector;
    Lazy<String> mLeakReportEmail;
    Lazy<LeakReporter> mLeakReporter;
    Lazy<LightBarController> mLightBarController;
    Lazy<LocalBluetoothManager> mLocalBluetoothManager;
    Lazy<LocationController> mLocationController;
    Lazy<LockscreenGestureLogger> mLockscreenGestureLogger;
    Lazy<Executor> mMainExecutor;
    Lazy<Handler> mMainHandler;
    Lazy<Looper> mMainLooper;
    Lazy<ManagedProfileController> mManagedProfileController;
    Lazy<MediaOutputDialogFactory> mMediaOutputDialogFactory;
    Lazy<MetricsLogger> mMetricsLogger;
    Lazy<NavigationModeController> mNavBarModeController;
    Lazy<NavigationBarOverlayController> mNavbarButtonsControllerLazy;
    Lazy<NavigationBarController> mNavigationBarController;
    Lazy<NetworkController> mNetworkController;
    Lazy<NextAlarmController> mNextAlarmController;
    Lazy<NightDisplayListener> mNightDisplayListener;
    Lazy<NotificationEntryManager> mNotificationEntryManager;
    Lazy<NotificationFilter> mNotificationFilter;
    Lazy<NotificationGroupAlertTransferHelper> mNotificationGroupAlertTransferHelper;
    Lazy<NotificationGroupManagerLegacy> mNotificationGroupManager;
    Lazy<NotificationGutsManager> mNotificationGutsManager;
    Lazy<NotificationListener> mNotificationListener;
    Lazy<NotificationLockscreenUserManager> mNotificationLockscreenUserManager;
    Lazy<NotificationLogger> mNotificationLogger;
    Lazy<NotificationMediaManager> mNotificationMediaManager;
    Lazy<NotificationRemoteInputManager> mNotificationRemoteInputManager;
    Lazy<NotificationRemoteInputManager.Callback> mNotificationRemoteInputManagerCallback;
    Lazy<NotificationShadeWindowController> mNotificationShadeWindowController;
    Lazy<NotificationViewHierarchyManager> mNotificationViewHierarchyManager;
    Lazy<OverviewProxyService> mOverviewProxyService;
    Lazy<PackageManagerWrapper> mPackageManagerWrapper;
    Lazy<PluginDependencyProvider> mPluginDependencyProvider;
    Lazy<PluginManager> mPluginManager;
    Lazy<PrivacyDotViewController> mPrivacyDotViewControllerLazy;
    Lazy<PrivacyItemController> mPrivacyItemController;
    Lazy<ProtoTracer> mProtoTracer;
    Lazy<Recents> mRecents;
    Lazy<RecordingController> mRecordingController;
    Lazy<ReduceBrightColorsController> mReduceBrightColorsController;
    Lazy<RemoteInputQuickSettingsDisabler> mRemoteInputQuickSettingsDisabler;
    Lazy<RotationLockController> mRotationLockController;
    Lazy<ScreenLifecycle> mScreenLifecycle;
    Lazy<SecurityController> mSecurityController;
    Lazy<SensorPrivacyController> mSensorPrivacyController;
    Lazy<SensorPrivacyManager> mSensorPrivacyManager;
    Lazy<ShadeController> mShadeController;
    Lazy<SmartReplyConstants> mSmartReplyConstants;
    Lazy<SmartReplyController> mSmartReplyController;
    Lazy<StatusBar> mStatusBar;
    Lazy<StatusBarIconController> mStatusBarIconController;
    Lazy<StatusBarStateController> mStatusBarStateController;
    Lazy<SysUiState> mSysUiStateFlagsContainer;
    Lazy<SystemStatusAnimationScheduler> mSystemStatusAnimationSchedulerLazy;
    Lazy<SysuiColorExtractor> mSysuiColorExtractor;
    Lazy<TelephonyListenerManager> mTelephonyListenerManager;
    Lazy<StatusBarWindowController> mTempStatusBarWindowController;
    Lazy<Handler> mTimeTickHandler;
    Lazy<TunablePadding.TunablePaddingService> mTunablePaddingService;
    Lazy<TunerService> mTunerService;
    Lazy<UiEventLogger> mUiEventLogger;
    Lazy<UiOffloadThread> mUiOffloadThread;
    Lazy<UserInfoController> mUserInfoController;
    Lazy<UserSwitcherController> mUserSwitcherController;
    Lazy<VibratorHelper> mVibratorHelper;
    Lazy<VisualStabilityManager> mVisualStabilityManager;
    Lazy<VolumeDialogController> mVolumeDialogController;
    Lazy<WakefulnessLifecycle> mWakefulnessLifecycle;
    Lazy<IWallpaperManager> mWallpaperManager;
    Lazy<PowerUI.WarningsUI> mWarningsUI;
    Lazy<ZenModeController> mZenModeController;
    public static final DependencyKey<Looper> BG_LOOPER = new DependencyKey<>("background_looper");
    public static final DependencyKey<Looper> MAIN_LOOPER = new DependencyKey<>("main_looper");
    public static final DependencyKey<Handler> TIME_TICK_HANDLER = new DependencyKey<>("time_tick_handler");
    public static final DependencyKey<Handler> MAIN_HANDLER = new DependencyKey<>("main_handler");
    public static final DependencyKey<Executor> MAIN_EXECUTOR = new DependencyKey<>("main_executor");
    public static final DependencyKey<Executor> BACKGROUND_EXECUTOR = new DependencyKey<>("background_executor");
    public static final DependencyKey<String> LEAK_REPORT_EMAIL = new DependencyKey<>("leak_report_email");
    private final ArrayMap<Object, Object> mDependencies = new ArrayMap<>();
    private final ArrayMap<Object, LazyDependencyCreator> mProviders = new ArrayMap<>();

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface LazyDependencyCreator<T> {
        T createDependency();
    }

    @VisibleForTesting
    protected boolean autoRegisterModulesForDump() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void start() {
        ArrayMap<Object, LazyDependencyCreator> arrayMap = this.mProviders;
        DependencyKey<Handler> dependencyKey = TIME_TICK_HANDLER;
        Lazy<Handler> lazy = this.mTimeTickHandler;
        Objects.requireNonNull(lazy);
        arrayMap.put(dependencyKey, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap2 = this.mProviders;
        DependencyKey<Looper> dependencyKey2 = BG_LOOPER;
        Lazy<Looper> lazy2 = this.mBgLooper;
        Objects.requireNonNull(lazy2);
        arrayMap2.put(dependencyKey2, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap3 = this.mProviders;
        DependencyKey<Looper> dependencyKey3 = MAIN_LOOPER;
        Lazy<Looper> lazy3 = this.mMainLooper;
        Objects.requireNonNull(lazy3);
        arrayMap3.put(dependencyKey3, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap4 = this.mProviders;
        DependencyKey<Handler> dependencyKey4 = MAIN_HANDLER;
        Lazy<Handler> lazy4 = this.mMainHandler;
        Objects.requireNonNull(lazy4);
        arrayMap4.put(dependencyKey4, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap5 = this.mProviders;
        DependencyKey<Executor> dependencyKey5 = MAIN_EXECUTOR;
        Lazy<Executor> lazy5 = this.mMainExecutor;
        Objects.requireNonNull(lazy5);
        arrayMap5.put(dependencyKey5, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap6 = this.mProviders;
        DependencyKey<Executor> dependencyKey6 = BACKGROUND_EXECUTOR;
        Lazy<Executor> lazy6 = this.mBackgroundExecutor;
        Objects.requireNonNull(lazy6);
        arrayMap6.put(dependencyKey6, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap7 = this.mProviders;
        Lazy<ActivityStarter> lazy7 = this.mActivityStarter;
        Objects.requireNonNull(lazy7);
        arrayMap7.put(ActivityStarter.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap8 = this.mProviders;
        Lazy<BroadcastDispatcher> lazy8 = this.mBroadcastDispatcher;
        Objects.requireNonNull(lazy8);
        arrayMap8.put(BroadcastDispatcher.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap9 = this.mProviders;
        Lazy<AsyncSensorManager> lazy9 = this.mAsyncSensorManager;
        Objects.requireNonNull(lazy9);
        arrayMap9.put(AsyncSensorManager.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap10 = this.mProviders;
        Lazy<BluetoothController> lazy10 = this.mBluetoothController;
        Objects.requireNonNull(lazy10);
        arrayMap10.put(BluetoothController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap11 = this.mProviders;
        Lazy<SensorPrivacyManager> lazy11 = this.mSensorPrivacyManager;
        Objects.requireNonNull(lazy11);
        arrayMap11.put(SensorPrivacyManager.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap12 = this.mProviders;
        Lazy<LocationController> lazy12 = this.mLocationController;
        Objects.requireNonNull(lazy12);
        arrayMap12.put(LocationController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap13 = this.mProviders;
        Lazy<RotationLockController> lazy13 = this.mRotationLockController;
        Objects.requireNonNull(lazy13);
        arrayMap13.put(RotationLockController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap14 = this.mProviders;
        Lazy<NetworkController> lazy14 = this.mNetworkController;
        Objects.requireNonNull(lazy14);
        arrayMap14.put(NetworkController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap15 = this.mProviders;
        Lazy<ZenModeController> lazy15 = this.mZenModeController;
        Objects.requireNonNull(lazy15);
        arrayMap15.put(ZenModeController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap16 = this.mProviders;
        Lazy<HotspotController> lazy16 = this.mHotspotController;
        Objects.requireNonNull(lazy16);
        arrayMap16.put(HotspotController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap17 = this.mProviders;
        Lazy<CastController> lazy17 = this.mCastController;
        Objects.requireNonNull(lazy17);
        arrayMap17.put(CastController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap18 = this.mProviders;
        Lazy<FlashlightController> lazy18 = this.mFlashlightController;
        Objects.requireNonNull(lazy18);
        arrayMap18.put(FlashlightController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap19 = this.mProviders;
        Lazy<KeyguardStateController> lazy19 = this.mKeyguardMonitor;
        Objects.requireNonNull(lazy19);
        arrayMap19.put(KeyguardStateController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap20 = this.mProviders;
        Lazy<KeyguardUpdateMonitor> lazy20 = this.mKeyguardUpdateMonitor;
        Objects.requireNonNull(lazy20);
        arrayMap20.put(KeyguardUpdateMonitor.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap21 = this.mProviders;
        Lazy<UserSwitcherController> lazy21 = this.mUserSwitcherController;
        Objects.requireNonNull(lazy21);
        arrayMap21.put(UserSwitcherController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap22 = this.mProviders;
        Lazy<UserInfoController> lazy22 = this.mUserInfoController;
        Objects.requireNonNull(lazy22);
        arrayMap22.put(UserInfoController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap23 = this.mProviders;
        Lazy<BatteryController> lazy23 = this.mBatteryController;
        Objects.requireNonNull(lazy23);
        arrayMap23.put(BatteryController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap24 = this.mProviders;
        Lazy<NightDisplayListener> lazy24 = this.mNightDisplayListener;
        Objects.requireNonNull(lazy24);
        arrayMap24.put(NightDisplayListener.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap25 = this.mProviders;
        Lazy<ReduceBrightColorsController> lazy25 = this.mReduceBrightColorsController;
        Objects.requireNonNull(lazy25);
        arrayMap25.put(ReduceBrightColorsController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap26 = this.mProviders;
        Lazy<ManagedProfileController> lazy26 = this.mManagedProfileController;
        Objects.requireNonNull(lazy26);
        arrayMap26.put(ManagedProfileController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap27 = this.mProviders;
        Lazy<NextAlarmController> lazy27 = this.mNextAlarmController;
        Objects.requireNonNull(lazy27);
        arrayMap27.put(NextAlarmController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap28 = this.mProviders;
        Lazy<DataSaverController> lazy28 = this.mDataSaverController;
        Objects.requireNonNull(lazy28);
        arrayMap28.put(DataSaverController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap29 = this.mProviders;
        Lazy<AccessibilityController> lazy29 = this.mAccessibilityController;
        Objects.requireNonNull(lazy29);
        arrayMap29.put(AccessibilityController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap30 = this.mProviders;
        Lazy<DeviceProvisionedController> lazy30 = this.mDeviceProvisionedController;
        Objects.requireNonNull(lazy30);
        arrayMap30.put(DeviceProvisionedController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap31 = this.mProviders;
        Lazy<PluginManager> lazy31 = this.mPluginManager;
        Objects.requireNonNull(lazy31);
        arrayMap31.put(PluginManager.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap32 = this.mProviders;
        Lazy<AssistManager> lazy32 = this.mAssistManager;
        Objects.requireNonNull(lazy32);
        arrayMap32.put(AssistManager.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap33 = this.mProviders;
        Lazy<SecurityController> lazy33 = this.mSecurityController;
        Objects.requireNonNull(lazy33);
        arrayMap33.put(SecurityController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap34 = this.mProviders;
        Lazy<LeakDetector> lazy34 = this.mLeakDetector;
        Objects.requireNonNull(lazy34);
        arrayMap34.put(LeakDetector.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap35 = this.mProviders;
        DependencyKey<String> dependencyKey7 = LEAK_REPORT_EMAIL;
        Lazy<String> lazy35 = this.mLeakReportEmail;
        Objects.requireNonNull(lazy35);
        arrayMap35.put(dependencyKey7, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap36 = this.mProviders;
        Lazy<LeakReporter> lazy36 = this.mLeakReporter;
        Objects.requireNonNull(lazy36);
        arrayMap36.put(LeakReporter.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap37 = this.mProviders;
        Lazy<GarbageMonitor> lazy37 = this.mGarbageMonitor;
        Objects.requireNonNull(lazy37);
        arrayMap37.put(GarbageMonitor.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap38 = this.mProviders;
        Lazy<TunerService> lazy38 = this.mTunerService;
        Objects.requireNonNull(lazy38);
        arrayMap38.put(TunerService.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap39 = this.mProviders;
        Lazy<NotificationShadeWindowController> lazy39 = this.mNotificationShadeWindowController;
        Objects.requireNonNull(lazy39);
        arrayMap39.put(NotificationShadeWindowController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap40 = this.mProviders;
        Lazy<StatusBarWindowController> lazy40 = this.mTempStatusBarWindowController;
        Objects.requireNonNull(lazy40);
        arrayMap40.put(StatusBarWindowController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap41 = this.mProviders;
        Lazy<DarkIconDispatcher> lazy41 = this.mDarkIconDispatcher;
        Objects.requireNonNull(lazy41);
        arrayMap41.put(DarkIconDispatcher.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap42 = this.mProviders;
        Lazy<ConfigurationController> lazy42 = this.mConfigurationController;
        Objects.requireNonNull(lazy42);
        arrayMap42.put(ConfigurationController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap43 = this.mProviders;
        Lazy<StatusBarIconController> lazy43 = this.mStatusBarIconController;
        Objects.requireNonNull(lazy43);
        arrayMap43.put(StatusBarIconController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap44 = this.mProviders;
        Lazy<ScreenLifecycle> lazy44 = this.mScreenLifecycle;
        Objects.requireNonNull(lazy44);
        arrayMap44.put(ScreenLifecycle.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap45 = this.mProviders;
        Lazy<WakefulnessLifecycle> lazy45 = this.mWakefulnessLifecycle;
        Objects.requireNonNull(lazy45);
        arrayMap45.put(WakefulnessLifecycle.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap46 = this.mProviders;
        Lazy<FragmentService> lazy46 = this.mFragmentService;
        Objects.requireNonNull(lazy46);
        arrayMap46.put(FragmentService.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap47 = this.mProviders;
        Lazy<ExtensionController> lazy47 = this.mExtensionController;
        Objects.requireNonNull(lazy47);
        arrayMap47.put(ExtensionController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap48 = this.mProviders;
        Lazy<PluginDependencyProvider> lazy48 = this.mPluginDependencyProvider;
        Objects.requireNonNull(lazy48);
        arrayMap48.put(PluginDependencyProvider.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap49 = this.mProviders;
        Lazy<LocalBluetoothManager> lazy49 = this.mLocalBluetoothManager;
        Objects.requireNonNull(lazy49);
        arrayMap49.put(LocalBluetoothManager.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap50 = this.mProviders;
        Lazy<VolumeDialogController> lazy50 = this.mVolumeDialogController;
        Objects.requireNonNull(lazy50);
        arrayMap50.put(VolumeDialogController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap51 = this.mProviders;
        Lazy<MetricsLogger> lazy51 = this.mMetricsLogger;
        Objects.requireNonNull(lazy51);
        arrayMap51.put(MetricsLogger.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap52 = this.mProviders;
        Lazy<AccessibilityManagerWrapper> lazy52 = this.mAccessibilityManagerWrapper;
        Objects.requireNonNull(lazy52);
        arrayMap52.put(AccessibilityManagerWrapper.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap53 = this.mProviders;
        Lazy<SysuiColorExtractor> lazy53 = this.mSysuiColorExtractor;
        Objects.requireNonNull(lazy53);
        arrayMap53.put(SysuiColorExtractor.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap54 = this.mProviders;
        Lazy<TunablePadding.TunablePaddingService> lazy54 = this.mTunablePaddingService;
        Objects.requireNonNull(lazy54);
        arrayMap54.put(TunablePadding.TunablePaddingService.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap55 = this.mProviders;
        Lazy<ForegroundServiceController> lazy55 = this.mForegroundServiceController;
        Objects.requireNonNull(lazy55);
        arrayMap55.put(ForegroundServiceController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap56 = this.mProviders;
        Lazy<UiOffloadThread> lazy56 = this.mUiOffloadThread;
        Objects.requireNonNull(lazy56);
        arrayMap56.put(UiOffloadThread.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap57 = this.mProviders;
        Lazy<PowerUI.WarningsUI> lazy57 = this.mWarningsUI;
        Objects.requireNonNull(lazy57);
        arrayMap57.put(PowerUI.WarningsUI.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap58 = this.mProviders;
        Lazy<LightBarController> lazy58 = this.mLightBarController;
        Objects.requireNonNull(lazy58);
        arrayMap58.put(LightBarController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap59 = this.mProviders;
        Lazy<IWindowManager> lazy59 = this.mIWindowManager;
        Objects.requireNonNull(lazy59);
        arrayMap59.put(IWindowManager.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap60 = this.mProviders;
        Lazy<OverviewProxyService> lazy60 = this.mOverviewProxyService;
        Objects.requireNonNull(lazy60);
        arrayMap60.put(OverviewProxyService.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap61 = this.mProviders;
        Lazy<NavigationModeController> lazy61 = this.mNavBarModeController;
        Objects.requireNonNull(lazy61);
        arrayMap61.put(NavigationModeController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap62 = this.mProviders;
        Lazy<AccessibilityButtonModeObserver> lazy62 = this.mAccessibilityButtonModeObserver;
        Objects.requireNonNull(lazy62);
        arrayMap62.put(AccessibilityButtonModeObserver.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap63 = this.mProviders;
        Lazy<AccessibilityButtonTargetsObserver> lazy63 = this.mAccessibilityButtonListController;
        Objects.requireNonNull(lazy63);
        arrayMap63.put(AccessibilityButtonTargetsObserver.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap64 = this.mProviders;
        Lazy<EnhancedEstimates> lazy64 = this.mEnhancedEstimates;
        Objects.requireNonNull(lazy64);
        arrayMap64.put(EnhancedEstimates.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap65 = this.mProviders;
        Lazy<VibratorHelper> lazy65 = this.mVibratorHelper;
        Objects.requireNonNull(lazy65);
        arrayMap65.put(VibratorHelper.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap66 = this.mProviders;
        Lazy<IStatusBarService> lazy66 = this.mIStatusBarService;
        Objects.requireNonNull(lazy66);
        arrayMap66.put(IStatusBarService.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap67 = this.mProviders;
        Lazy<DisplayMetrics> lazy67 = this.mDisplayMetrics;
        Objects.requireNonNull(lazy67);
        arrayMap67.put(DisplayMetrics.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap68 = this.mProviders;
        Lazy<LockscreenGestureLogger> lazy68 = this.mLockscreenGestureLogger;
        Objects.requireNonNull(lazy68);
        arrayMap68.put(LockscreenGestureLogger.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap69 = this.mProviders;
        Lazy<NotificationEntryManager.KeyguardEnvironment> lazy69 = this.mKeyguardEnvironment;
        Objects.requireNonNull(lazy69);
        arrayMap69.put(NotificationEntryManager.KeyguardEnvironment.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap70 = this.mProviders;
        Lazy<ShadeController> lazy70 = this.mShadeController;
        Objects.requireNonNull(lazy70);
        arrayMap70.put(ShadeController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap71 = this.mProviders;
        Lazy<NotificationRemoteInputManager.Callback> lazy71 = this.mNotificationRemoteInputManagerCallback;
        Objects.requireNonNull(lazy71);
        arrayMap71.put(NotificationRemoteInputManager.Callback.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap72 = this.mProviders;
        Lazy<AppOpsController> lazy72 = this.mAppOpsController;
        Objects.requireNonNull(lazy72);
        arrayMap72.put(AppOpsController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap73 = this.mProviders;
        Lazy<NavigationBarController> lazy73 = this.mNavigationBarController;
        Objects.requireNonNull(lazy73);
        arrayMap73.put(NavigationBarController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap74 = this.mProviders;
        Lazy<AccessibilityFloatingMenuController> lazy74 = this.mAccessibilityFloatingMenuController;
        Objects.requireNonNull(lazy74);
        arrayMap74.put(AccessibilityFloatingMenuController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap75 = this.mProviders;
        Lazy<StatusBarStateController> lazy75 = this.mStatusBarStateController;
        Objects.requireNonNull(lazy75);
        arrayMap75.put(StatusBarStateController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap76 = this.mProviders;
        Lazy<NotificationLockscreenUserManager> lazy76 = this.mNotificationLockscreenUserManager;
        Objects.requireNonNull(lazy76);
        arrayMap76.put(NotificationLockscreenUserManager.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap77 = this.mProviders;
        Lazy<VisualStabilityManager> lazy77 = this.mVisualStabilityManager;
        Objects.requireNonNull(lazy77);
        arrayMap77.put(VisualStabilityManager.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap78 = this.mProviders;
        Lazy<NotificationGroupManagerLegacy> lazy78 = this.mNotificationGroupManager;
        Objects.requireNonNull(lazy78);
        arrayMap78.put(NotificationGroupManagerLegacy.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap79 = this.mProviders;
        Lazy<NotificationGroupAlertTransferHelper> lazy79 = this.mNotificationGroupAlertTransferHelper;
        Objects.requireNonNull(lazy79);
        arrayMap79.put(NotificationGroupAlertTransferHelper.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap80 = this.mProviders;
        Lazy<NotificationMediaManager> lazy80 = this.mNotificationMediaManager;
        Objects.requireNonNull(lazy80);
        arrayMap80.put(NotificationMediaManager.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap81 = this.mProviders;
        Lazy<NotificationGutsManager> lazy81 = this.mNotificationGutsManager;
        Objects.requireNonNull(lazy81);
        arrayMap81.put(NotificationGutsManager.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap82 = this.mProviders;
        Lazy<NotificationRemoteInputManager> lazy82 = this.mNotificationRemoteInputManager;
        Objects.requireNonNull(lazy82);
        arrayMap82.put(NotificationRemoteInputManager.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap83 = this.mProviders;
        Lazy<SmartReplyConstants> lazy83 = this.mSmartReplyConstants;
        Objects.requireNonNull(lazy83);
        arrayMap83.put(SmartReplyConstants.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap84 = this.mProviders;
        Lazy<NotificationListener> lazy84 = this.mNotificationListener;
        Objects.requireNonNull(lazy84);
        arrayMap84.put(NotificationListener.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap85 = this.mProviders;
        Lazy<NotificationLogger> lazy85 = this.mNotificationLogger;
        Objects.requireNonNull(lazy85);
        arrayMap85.put(NotificationLogger.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap86 = this.mProviders;
        Lazy<NotificationViewHierarchyManager> lazy86 = this.mNotificationViewHierarchyManager;
        Objects.requireNonNull(lazy86);
        arrayMap86.put(NotificationViewHierarchyManager.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap87 = this.mProviders;
        Lazy<NotificationFilter> lazy87 = this.mNotificationFilter;
        Objects.requireNonNull(lazy87);
        arrayMap87.put(NotificationFilter.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap88 = this.mProviders;
        Lazy<KeyguardDismissUtil> lazy88 = this.mKeyguardDismissUtil;
        Objects.requireNonNull(lazy88);
        arrayMap88.put(KeyguardDismissUtil.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap89 = this.mProviders;
        Lazy<SmartReplyController> lazy89 = this.mSmartReplyController;
        Objects.requireNonNull(lazy89);
        arrayMap89.put(SmartReplyController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap90 = this.mProviders;
        Lazy<RemoteInputQuickSettingsDisabler> lazy90 = this.mRemoteInputQuickSettingsDisabler;
        Objects.requireNonNull(lazy90);
        arrayMap90.put(RemoteInputQuickSettingsDisabler.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap91 = this.mProviders;
        Lazy<NotificationEntryManager> lazy91 = this.mNotificationEntryManager;
        Objects.requireNonNull(lazy91);
        arrayMap91.put(NotificationEntryManager.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap92 = this.mProviders;
        Lazy<ForegroundServiceNotificationListener> lazy92 = this.mForegroundServiceNotificationListener;
        Objects.requireNonNull(lazy92);
        arrayMap92.put(ForegroundServiceNotificationListener.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap93 = this.mProviders;
        Lazy<ClockManager> lazy93 = this.mClockManager;
        Objects.requireNonNull(lazy93);
        arrayMap93.put(ClockManager.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap94 = this.mProviders;
        Lazy<PrivacyItemController> lazy94 = this.mPrivacyItemController;
        Objects.requireNonNull(lazy94);
        arrayMap94.put(PrivacyItemController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap95 = this.mProviders;
        Lazy<ActivityManagerWrapper> lazy95 = this.mActivityManagerWrapper;
        Objects.requireNonNull(lazy95);
        arrayMap95.put(ActivityManagerWrapper.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap96 = this.mProviders;
        Lazy<DevicePolicyManagerWrapper> lazy96 = this.mDevicePolicyManagerWrapper;
        Objects.requireNonNull(lazy96);
        arrayMap96.put(DevicePolicyManagerWrapper.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap97 = this.mProviders;
        Lazy<PackageManagerWrapper> lazy97 = this.mPackageManagerWrapper;
        Objects.requireNonNull(lazy97);
        arrayMap97.put(PackageManagerWrapper.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap98 = this.mProviders;
        Lazy<SensorPrivacyController> lazy98 = this.mSensorPrivacyController;
        Objects.requireNonNull(lazy98);
        arrayMap98.put(SensorPrivacyController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap99 = this.mProviders;
        Lazy<DockManager> lazy99 = this.mDockManager;
        Objects.requireNonNull(lazy99);
        arrayMap99.put(DockManager.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap100 = this.mProviders;
        Lazy<INotificationManager> lazy100 = this.mINotificationManager;
        Objects.requireNonNull(lazy100);
        arrayMap100.put(INotificationManager.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap101 = this.mProviders;
        Lazy<SysUiState> lazy101 = this.mSysUiStateFlagsContainer;
        Objects.requireNonNull(lazy101);
        arrayMap101.put(SysUiState.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap102 = this.mProviders;
        Lazy<AlarmManager> lazy102 = this.mAlarmManager;
        Objects.requireNonNull(lazy102);
        arrayMap102.put(AlarmManager.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap103 = this.mProviders;
        Lazy<KeyguardSecurityModel> lazy103 = this.mKeyguardSecurityModel;
        Objects.requireNonNull(lazy103);
        arrayMap103.put(KeyguardSecurityModel.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap104 = this.mProviders;
        Lazy<DozeParameters> lazy104 = this.mDozeParameters;
        Objects.requireNonNull(lazy104);
        arrayMap104.put(DozeParameters.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap105 = this.mProviders;
        Lazy<IWallpaperManager> lazy105 = this.mWallpaperManager;
        Objects.requireNonNull(lazy105);
        arrayMap105.put(IWallpaperManager.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap106 = this.mProviders;
        Lazy<CommandQueue> lazy106 = this.mCommandQueue;
        Objects.requireNonNull(lazy106);
        arrayMap106.put(CommandQueue.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap107 = this.mProviders;
        Lazy<Recents> lazy107 = this.mRecents;
        Objects.requireNonNull(lazy107);
        arrayMap107.put(Recents.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap108 = this.mProviders;
        Lazy<StatusBar> lazy108 = this.mStatusBar;
        Objects.requireNonNull(lazy108);
        arrayMap108.put(StatusBar.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap109 = this.mProviders;
        Lazy<ProtoTracer> lazy109 = this.mProtoTracer;
        Objects.requireNonNull(lazy109);
        arrayMap109.put(ProtoTracer.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap110 = this.mProviders;
        Lazy<DeviceConfigProxy> lazy110 = this.mDeviceConfigProxy;
        Objects.requireNonNull(lazy110);
        arrayMap110.put(DeviceConfigProxy.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap111 = this.mProviders;
        Lazy<TelephonyListenerManager> lazy111 = this.mTelephonyListenerManager;
        Objects.requireNonNull(lazy111);
        arrayMap111.put(TelephonyListenerManager.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap112 = this.mProviders;
        Lazy<AutoHideController> lazy112 = this.mAutoHideController;
        Objects.requireNonNull(lazy112);
        arrayMap112.put(AutoHideController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap113 = this.mProviders;
        Lazy<RecordingController> lazy113 = this.mRecordingController;
        Objects.requireNonNull(lazy113);
        arrayMap113.put(RecordingController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap114 = this.mProviders;
        Lazy<MediaOutputDialogFactory> lazy114 = this.mMediaOutputDialogFactory;
        Objects.requireNonNull(lazy114);
        arrayMap114.put(MediaOutputDialogFactory.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap115 = this.mProviders;
        Lazy<NavigationBarOverlayController> lazy115 = this.mNavbarButtonsControllerLazy;
        Objects.requireNonNull(lazy115);
        arrayMap115.put(NavigationBarOverlayController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap116 = this.mProviders;
        Lazy<SystemStatusAnimationScheduler> lazy116 = this.mSystemStatusAnimationSchedulerLazy;
        Objects.requireNonNull(lazy116);
        arrayMap116.put(SystemStatusAnimationScheduler.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap117 = this.mProviders;
        Lazy<PrivacyDotViewController> lazy117 = this.mPrivacyDotViewControllerLazy;
        Objects.requireNonNull(lazy117);
        arrayMap117.put(PrivacyDotViewController.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap118 = this.mProviders;
        Lazy<EdgeBackGestureHandler.Factory> lazy118 = this.mEdgeBackGestureHandlerFactoryLazy;
        Objects.requireNonNull(lazy118);
        arrayMap118.put(EdgeBackGestureHandler.Factory.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap119 = this.mProviders;
        Lazy<UiEventLogger> lazy119 = this.mUiEventLogger;
        Objects.requireNonNull(lazy119);
        arrayMap119.put(UiEventLogger.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap120 = this.mProviders;
        Lazy<FeatureFlags> lazy120 = this.mFeatureFlagsLazy;
        Objects.requireNonNull(lazy120);
        arrayMap120.put(FeatureFlags.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        ArrayMap<Object, LazyDependencyCreator> arrayMap121 = this.mProviders;
        Lazy<StatusBarContentInsetsProvider> lazy121 = this.mContentInsetsProviderLazy;
        Objects.requireNonNull(lazy121);
        arrayMap121.put(StatusBarContentInsetsProvider.class, new LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                return Lazy.this.get();
            }
        });
        setInstance(this);
    }

    @VisibleForTesting
    public static void setInstance(Dependency dependency) {
        sDependency = dependency;
    }

    protected final <T> T getDependency(Class<T> cls) {
        return (T) getDependencyInner(cls);
    }

    protected final <T> T getDependency(DependencyKey<T> dependencyKey) {
        return (T) getDependencyInner(dependencyKey);
    }

    private synchronized <T> T getDependencyInner(Object obj) {
        T t;
        t = (T) this.mDependencies.get(obj);
        if (t == null) {
            t = (T) createDependency(obj);
            this.mDependencies.put(obj, t);
            if (autoRegisterModulesForDump() && (t instanceof Dumpable)) {
                this.mDumpManager.registerDumpable(t.getClass().getName(), (Dumpable) t);
            }
        }
        return t;
    }

    @VisibleForTesting
    public <T> T createDependency(Object obj) {
        Preconditions.checkArgument((obj instanceof DependencyKey) || (obj instanceof Class));
        LazyDependencyCreator lazyDependencyCreator = this.mProviders.get(obj);
        if (lazyDependencyCreator != null) {
            return (T) lazyDependencyCreator.createDependency();
        }
        throw new IllegalArgumentException("Unsupported dependency " + obj + ". " + this.mProviders.size() + " providers known.");
    }

    /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: java.util.function.Consumer<T> */
    /* JADX WARN: Multi-variable type inference failed */
    private <T> void destroyDependency(Class<T> cls, Consumer<T> consumer) {
        Object remove = this.mDependencies.remove(cls);
        if (remove instanceof Dumpable) {
            this.mDumpManager.unregisterDumpable(remove.getClass().getName());
        }
        if (remove != null && consumer != 0) {
            consumer.accept(remove);
        }
    }

    public static <T> void destroy(Class<T> cls, Consumer<T> consumer) {
        sDependency.destroyDependency(cls, consumer);
    }

    @Deprecated
    public static <T> T get(Class<T> cls) {
        return (T) sDependency.getDependency(cls);
    }

    @Deprecated
    public static <T> T get(DependencyKey<T> dependencyKey) {
        return (T) sDependency.getDependency(dependencyKey);
    }

    /* loaded from: classes.dex */
    public static final class DependencyKey<V> {
        private final String mDisplayName;

        public DependencyKey(String str) {
            this.mDisplayName = str;
        }

        public String toString() {
            return this.mDisplayName;
        }
    }
}
