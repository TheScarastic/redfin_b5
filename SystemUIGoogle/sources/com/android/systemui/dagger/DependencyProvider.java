package com.android.systemui.dagger;

import android.annotation.SuppressLint;
import android.app.INotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.om.OverlayManager;
import android.hardware.display.AmbientDisplayConfiguration;
import android.hardware.display.ColorDisplayManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.view.Choreographer;
import android.view.IWindowManager;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.util.NotificationMessagingUtil;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.ViewMediatorCallback;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.systemui.Prefs;
import com.android.systemui.R$string;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.accessibility.AccessibilityButtonTargetsObserver;
import com.android.systemui.accessibility.ModeSwitchesController;
import com.android.systemui.accessibility.SystemActions;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuController;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import com.android.systemui.doze.AlwaysOnDisplayPolicy;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationBarOverlayController;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.plugins.PluginInitializerImpl;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.ReduceBrightColorsController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.recents.Recents;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.shared.plugins.PluginManagerImpl;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.DevicePolicyManagerWrapper;
import com.android.systemui.shared.system.TaskStackChangeListeners;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.ConfigurationControllerImpl;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.AccessibilityManagerWrapper;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.NetworkController;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.systemui.util.leak.LeakDetector;
import com.android.systemui.util.settings.SecureSettings;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.pip.Pip;
import dagger.Lazy;
import java.util.Optional;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class DependencyProvider {
    public Handler provideTimeTickHandler() {
        HandlerThread handlerThread = new HandlerThread("TimeTick");
        handlerThread.start();
        return new Handler(handlerThread.getLooper());
    }

    public SharedPreferences provideSharePreferences(Context context) {
        return Prefs.get(context);
    }

    public AmbientDisplayConfiguration provideAmbientDisplayConfiguration(Context context) {
        return new AmbientDisplayConfiguration(context);
    }

    public Handler provideHandler() {
        return new Handler();
    }

    public DataSaverController provideDataSaverController(NetworkController networkController) {
        return networkController.getDataSaverController();
    }

    public INotificationManager provideINotificationManager() {
        return INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
    }

    public LayoutInflater providerLayoutInflater(Context context) {
        return LayoutInflater.from(context);
    }

    public LeakDetector provideLeakDetector() {
        return LeakDetector.create();
    }

    /* access modifiers changed from: package-private */
    @SuppressLint({"MissingPermission"})
    public static LocalBluetoothManager provideLocalBluetoothController(Context context, Handler handler) {
        return LocalBluetoothManager.create(context, handler, UserHandle.ALL);
    }

    public MetricsLogger provideMetricsLogger() {
        return new MetricsLogger();
    }

    public PluginManager providePluginManager(Context context) {
        return new PluginManagerImpl(context, new PluginInitializerImpl());
    }

    /* access modifiers changed from: package-private */
    public static ThemeOverlayApplier provideThemeOverlayManager(Context context, Executor executor, OverlayManager overlayManager, DumpManager dumpManager) {
        return new ThemeOverlayApplier(overlayManager, executor, context.getString(R$string.launcher_overlayable_package), context.getString(R$string.themepicker_overlayable_package), dumpManager);
    }

    public NavigationBarController provideNavigationBarController(Context context, WindowManager windowManager, Lazy<AssistManager> lazy, AccessibilityManager accessibilityManager, AccessibilityManagerWrapper accessibilityManagerWrapper, DeviceProvisionedController deviceProvisionedController, MetricsLogger metricsLogger, OverviewProxyService overviewProxyService, NavigationModeController navigationModeController, AccessibilityButtonModeObserver accessibilityButtonModeObserver, StatusBarStateController statusBarStateController, SysUiState sysUiState, BroadcastDispatcher broadcastDispatcher, CommandQueue commandQueue, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<Recents> optional3, Lazy<StatusBar> lazy2, ShadeController shadeController, NotificationRemoteInputManager notificationRemoteInputManager, NotificationShadeDepthController notificationShadeDepthController, SystemActions systemActions, Handler handler, UiEventLogger uiEventLogger, NavigationBarOverlayController navigationBarOverlayController, ConfigurationController configurationController, UserTracker userTracker) {
        return new NavigationBarController(context, windowManager, lazy, accessibilityManager, accessibilityManagerWrapper, deviceProvisionedController, metricsLogger, overviewProxyService, navigationModeController, accessibilityButtonModeObserver, statusBarStateController, sysUiState, broadcastDispatcher, commandQueue, optional, optional2, optional3, lazy2, shadeController, notificationRemoteInputManager, notificationShadeDepthController, systemActions, handler, uiEventLogger, navigationBarOverlayController, configurationController, userTracker);
    }

    public AccessibilityFloatingMenuController provideAccessibilityFloatingMenuController(Context context, AccessibilityButtonTargetsObserver accessibilityButtonTargetsObserver, AccessibilityButtonModeObserver accessibilityButtonModeObserver, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        return new AccessibilityFloatingMenuController(context, accessibilityButtonTargetsObserver, accessibilityButtonModeObserver, keyguardUpdateMonitor);
    }

    public ConfigurationController provideConfigurationController(Context context) {
        return new ConfigurationControllerImpl(context);
    }

    public AutoHideController provideAutoHideController(Context context, Handler handler, IWindowManager iWindowManager) {
        return new AutoHideController(context, handler, iWindowManager);
    }

    public ReduceBrightColorsController provideReduceBrightColorsListener(Handler handler, UserTracker userTracker, ColorDisplayManager colorDisplayManager, SecureSettings secureSettings) {
        return new ReduceBrightColorsController(userTracker, handler, colorDisplayManager, secureSettings);
    }

    public ActivityManagerWrapper provideActivityManagerWrapper() {
        return ActivityManagerWrapper.getInstance();
    }

    public TaskStackChangeListeners provideTaskStackChangeListeners() {
        return TaskStackChangeListeners.getInstance();
    }

    public BroadcastDispatcher providesBroadcastDispatcher(Context context, Looper looper, Executor executor, DumpManager dumpManager, BroadcastDispatcherLogger broadcastDispatcherLogger, UserTracker userTracker) {
        BroadcastDispatcher broadcastDispatcher = new BroadcastDispatcher(context, looper, executor, dumpManager, broadcastDispatcherLogger, userTracker);
        broadcastDispatcher.initialize();
        return broadcastDispatcher;
    }

    public DevicePolicyManagerWrapper provideDevicePolicyManagerWrapper() {
        return DevicePolicyManagerWrapper.getInstance();
    }

    public LockPatternUtils provideLockPatternUtils(Context context) {
        return new LockPatternUtils(context);
    }

    public AlwaysOnDisplayPolicy provideAlwaysOnDisplayPolicy(Context context) {
        return new AlwaysOnDisplayPolicy(context);
    }

    public NotificationMessagingUtil provideNotificationMessagingUtil(Context context) {
        return new NotificationMessagingUtil(context);
    }

    public ViewMediatorCallback providesViewMediatorCallback(KeyguardViewMediator keyguardViewMediator) {
        return keyguardViewMediator.getViewMediatorCallback();
    }

    public Choreographer providesChoreographer() {
        return Choreographer.getInstance();
    }

    public ModeSwitchesController providesModeSwitchesController(Context context) {
        return new ModeSwitchesController(context);
    }
}
