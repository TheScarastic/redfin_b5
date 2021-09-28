package com.google.android.systemui.statusbar.phone;

import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.internal.logging.MetricsLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.ViewMediatorCallback;
import com.android.systemui.Dependency;
import com.android.systemui.InitController;
import com.android.systemui.R$id;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuController;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.dock.DockManager;
import com.android.systemui.keyguard.DismissCallbackRegistry;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.PluginDependencyProvider;
import com.android.systemui.recents.ScreenPinningRequest;
import com.android.systemui.settings.brightness.BrightnessSlider;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.NotificationViewHierarchyManager;
import com.android.systemui.statusbar.PulseExpansionHandler;
import com.android.systemui.statusbar.SuperStatusBarViewFactory;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.charging.WiredChargingRippleController;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.init.NotificationsController;
import com.android.systemui.statusbar.notification.interruption.BypassHeadsUpNotifier;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.DozeScrimController;
import com.android.systemui.statusbar.phone.DozeServiceHost;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.phone.KeyguardLiftController;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.LightsOutNotifController;
import com.android.systemui.statusbar.phone.LockscreenWallpaper;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import com.android.systemui.statusbar.phone.PhoneStatusBarPolicy;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.phone.StatusBarLocationPublisher;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter;
import com.android.systemui.statusbar.phone.StatusBarSignalPolicy;
import com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.phone.dagger.StatusBarComponent;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.ExtensionController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.NetworkController;
import com.android.systemui.statusbar.policy.RemoteInputQuickSettingsDisabler;
import com.android.systemui.statusbar.policy.UserInfoControllerImpl;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.volume.VolumeComponent;
import com.android.systemui.wmshell.BubblesManager;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.startingsurface.StartingSurface;
import com.google.android.systemui.LiveWallpaperScrimController;
import com.google.android.systemui.NotificationLockscreenUserManagerGoogle;
import com.google.android.systemui.dreamliner.DockIndicationController;
import com.google.android.systemui.dreamliner.DockObserver;
import com.google.android.systemui.reversecharging.ReverseChargingViewController;
import com.google.android.systemui.smartspace.SmartSpaceController;
import com.google.android.systemui.statusbar.KeyguardIndicationControllerGoogle;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyClient;
import dagger.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public class StatusBarGoogle extends StatusBar {
    public static final boolean DEBUG = Log.isLoggable("StatusBarGoogle", 3);
    private long mAnimStartTime;
    private boolean mChargingAnimShown;
    private final KeyguardIndicationControllerGoogle mKeyguardIndicationController;
    private NotificationIconCenteringController mNotifIconCenteringController;
    private int mReceivingBatteryLevel;
    private boolean mReverseChargingAnimShown;
    private Optional<ReverseChargingViewController> mReverseChargingViewControllerOptional;
    private final SmartSpaceController mSmartSpaceController;
    private final Lazy<Optional<NotificationVoiceReplyClient>> mVoiceReplyClient;
    private WallpaperNotifier mWallpaperNotifier;

    public StatusBarGoogle(SmartSpaceController smartSpaceController, WallpaperNotifier wallpaperNotifier, Optional<ReverseChargingViewController> optional, Context context, NotificationsController notificationsController, LightBarController lightBarController, AutoHideController autoHideController, KeyguardUpdateMonitor keyguardUpdateMonitor, StatusBarSignalPolicy statusBarSignalPolicy, PulseExpansionHandler pulseExpansionHandler, NotificationWakeUpCoordinator notificationWakeUpCoordinator, KeyguardBypassController keyguardBypassController, KeyguardStateController keyguardStateController, HeadsUpManagerPhone headsUpManagerPhone, DynamicPrivacyController dynamicPrivacyController, BypassHeadsUpNotifier bypassHeadsUpNotifier, FalsingManager falsingManager, FalsingCollector falsingCollector, BroadcastDispatcher broadcastDispatcher, RemoteInputQuickSettingsDisabler remoteInputQuickSettingsDisabler, NotificationGutsManager notificationGutsManager, NotificationLogger notificationLogger, NotificationInterruptStateProvider notificationInterruptStateProvider, NotificationViewHierarchyManager notificationViewHierarchyManager, KeyguardViewMediator keyguardViewMediator, DisplayMetrics displayMetrics, MetricsLogger metricsLogger, Executor executor, NotificationMediaManager notificationMediaManager, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationRemoteInputManager notificationRemoteInputManager, UserSwitcherController userSwitcherController, NetworkController networkController, BatteryController batteryController, SysuiColorExtractor sysuiColorExtractor, ScreenLifecycle screenLifecycle, WakefulnessLifecycle wakefulnessLifecycle, SysuiStatusBarStateController sysuiStatusBarStateController, VibratorHelper vibratorHelper, Optional<BubblesManager> optional2, Optional<Bubbles> optional3, VisualStabilityManager visualStabilityManager, DeviceProvisionedController deviceProvisionedController, NavigationBarController navigationBarController, AccessibilityFloatingMenuController accessibilityFloatingMenuController, Lazy<AssistManager> lazy, ConfigurationController configurationController, NotificationShadeWindowController notificationShadeWindowController, DozeParameters dozeParameters, LiveWallpaperScrimController liveWallpaperScrimController, KeyguardLiftController keyguardLiftController, Lazy<LockscreenWallpaper> lazy2, Lazy<BiometricUnlockController> lazy3, Lazy<NotificationShadeDepthController> lazy4, DozeServiceHost dozeServiceHost, PowerManager powerManager, ScreenPinningRequest screenPinningRequest, DozeScrimController dozeScrimController, VolumeComponent volumeComponent, CommandQueue commandQueue, Provider<StatusBarComponent.Builder> provider, PluginManager pluginManager, Optional<LegacySplitScreen> optional4, LightsOutNotifController lightsOutNotifController, StatusBarNotificationActivityStarter.Builder builder, ShadeController shadeController, SuperStatusBarViewFactory superStatusBarViewFactory, StatusBarKeyguardViewManager statusBarKeyguardViewManager, ViewMediatorCallback viewMediatorCallback, InitController initController, Handler handler, PluginDependencyProvider pluginDependencyProvider, KeyguardDismissUtil keyguardDismissUtil, ExtensionController extensionController, UserInfoControllerImpl userInfoControllerImpl, PhoneStatusBarPolicy phoneStatusBarPolicy, KeyguardIndicationControllerGoogle keyguardIndicationControllerGoogle, DismissCallbackRegistry dismissCallbackRegistry, DemoModeController demoModeController, StatusBarTouchableRegionManager statusBarTouchableRegionManager, NotificationIconAreaController notificationIconAreaController, BrightnessSlider.Factory factory, WiredChargingRippleController wiredChargingRippleController, OngoingCallController ongoingCallController, SystemStatusAnimationScheduler systemStatusAnimationScheduler, StatusBarLocationPublisher statusBarLocationPublisher, StatusBarIconController statusBarIconController, LockscreenShadeTransitionController lockscreenShadeTransitionController, FeatureFlags featureFlags, Lazy<Optional<NotificationVoiceReplyClient>> lazy5, KeyguardUnlockAnimationController keyguardUnlockAnimationController, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, Optional<StartingSurface> optional5) {
        super(context, notificationsController, lightBarController, autoHideController, keyguardUpdateMonitor, statusBarSignalPolicy, pulseExpansionHandler, notificationWakeUpCoordinator, keyguardBypassController, keyguardStateController, headsUpManagerPhone, dynamicPrivacyController, bypassHeadsUpNotifier, falsingManager, falsingCollector, broadcastDispatcher, remoteInputQuickSettingsDisabler, notificationGutsManager, notificationLogger, notificationInterruptStateProvider, notificationViewHierarchyManager, keyguardViewMediator, displayMetrics, metricsLogger, executor, notificationMediaManager, notificationLockscreenUserManager, notificationRemoteInputManager, userSwitcherController, networkController, batteryController, sysuiColorExtractor, screenLifecycle, wakefulnessLifecycle, sysuiStatusBarStateController, vibratorHelper, optional2, optional3, visualStabilityManager, deviceProvisionedController, navigationBarController, accessibilityFloatingMenuController, lazy, configurationController, notificationShadeWindowController, dozeParameters, liveWallpaperScrimController, keyguardLiftController, lazy2, lazy3, dozeServiceHost, powerManager, screenPinningRequest, dozeScrimController, volumeComponent, commandQueue, provider, pluginManager, optional4, lightsOutNotifController, builder, shadeController, superStatusBarViewFactory, statusBarKeyguardViewManager, viewMediatorCallback, initController, handler, pluginDependencyProvider, keyguardDismissUtil, extensionController, userInfoControllerImpl, phoneStatusBarPolicy, keyguardIndicationControllerGoogle, dismissCallbackRegistry, demoModeController, lazy4, statusBarTouchableRegionManager, notificationIconAreaController, factory, wiredChargingRippleController, ongoingCallController, systemStatusAnimationScheduler, statusBarLocationPublisher, statusBarIconController, lockscreenShadeTransitionController, featureFlags, keyguardUnlockAnimationController, unlockedScreenOffAnimationController, optional5);
        this.mSmartSpaceController = smartSpaceController;
        this.mWallpaperNotifier = wallpaperNotifier;
        this.mReverseChargingViewControllerOptional = optional;
        this.mVoiceReplyClient = lazy5;
        this.mKeyguardIndicationController = keyguardIndicationControllerGoogle;
        keyguardIndicationControllerGoogle.setStatusBar(this);
    }

    @Override // com.android.systemui.statusbar.phone.StatusBar, com.android.systemui.SystemUI
    public void start() {
        super.start();
        ((NotificationLockscreenUserManagerGoogle) Dependency.get(NotificationLockscreenUserManager.class)).updateSmartSpaceVisibilitySettings();
        DockObserver dockObserver = (DockObserver) Dependency.get(DockManager.class);
        dockObserver.setDreamlinerGear((ImageView) this.mNotificationShadeWindowView.findViewById(R$id.dreamliner_gear));
        dockObserver.setPhotoPreview((FrameLayout) this.mNotificationShadeWindowView.findViewById(R$id.photo_preview));
        dockObserver.setIndicationController(new DockIndicationController(this.mContext, this.mKeyguardIndicationController, this));
        dockObserver.registerDockAlignInfo();
        if (this.mReverseChargingViewControllerOptional.isPresent()) {
            this.mReverseChargingViewControllerOptional.get().initialize();
        }
        this.mWallpaperNotifier.attach();
        this.mVoiceReplyClient.get().ifPresent(StatusBarGoogle$$ExternalSyntheticLambda0.INSTANCE);
    }

    @Override // com.android.systemui.statusbar.phone.StatusBar
    public void setLockscreenUser(int i) {
        super.setLockscreenUser(i);
        this.mSmartSpaceController.reloadData();
    }

    @Override // com.android.systemui.statusbar.phone.StatusBar, com.android.systemui.SystemUI, com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(fileDescriptor, printWriter, strArr);
        this.mSmartSpaceController.dump(fileDescriptor, printWriter, strArr);
        NotificationIconCenteringController notificationIconCenteringController = this.mNotifIconCenteringController;
        if (notificationIconCenteringController != null) {
            notificationIconCenteringController.dump(fileDescriptor, printWriter, strArr);
        }
    }

    @Override // com.android.systemui.statusbar.phone.StatusBar, com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
        super.onBatteryLevelChanged(i, z, z2);
        this.mReceivingBatteryLevel = i;
        if (!this.mBatteryController.isWirelessCharging()) {
            if (SystemClock.uptimeMillis() - this.mAnimStartTime > 1500) {
                this.mChargingAnimShown = false;
            }
            this.mReverseChargingAnimShown = false;
        }
        if (DEBUG) {
            Log.d("StatusBarGoogle", "onBatteryLevelChanged(): level=" + i + ",wlc=" + (this.mBatteryController.isWirelessCharging() ? 1 : 0) + ",wlcs=" + this.mChargingAnimShown + ",rtxs=" + this.mReverseChargingAnimShown + ",this=" + this);
        }
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public void onReverseChanged(boolean z, int i, String str) {
        super.onReverseChanged(z, i, str);
        if (!z && i >= 0 && !TextUtils.isEmpty(str) && this.mBatteryController.isWirelessCharging() && this.mChargingAnimShown && !this.mReverseChargingAnimShown) {
            this.mReverseChargingAnimShown = true;
            long uptimeMillis = SystemClock.uptimeMillis() - this.mAnimStartTime;
            showChargingAnimation(this.mReceivingBatteryLevel, i, uptimeMillis > 1500 ? 0 : 1500 - uptimeMillis);
        }
        if (DEBUG) {
            Log.d("StatusBarGoogle", "onReverseChanged(): rtx=" + (z ? 1 : 0) + ",rxlevel=" + this.mReceivingBatteryLevel + ",level=" + i + ",name=" + str + ",wlc=" + (this.mBatteryController.isWirelessCharging() ? 1 : 0) + ",wlcs=" + this.mChargingAnimShown + ",rtxs=" + this.mReverseChargingAnimShown + ",this=" + this);
        }
    }

    @Override // com.android.systemui.statusbar.phone.StatusBar, com.android.systemui.statusbar.CommandQueue.Callbacks
    public void showWirelessChargingAnimation(int i) {
        if (DEBUG) {
            Log.d("StatusBarGoogle", "showWirelessChargingAnimation()");
        }
        this.mChargingAnimShown = true;
        super.showWirelessChargingAnimation(i);
        this.mAnimStartTime = SystemClock.uptimeMillis();
    }
}
