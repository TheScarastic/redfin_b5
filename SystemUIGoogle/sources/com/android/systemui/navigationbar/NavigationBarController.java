package com.android.systemui.navigationbar;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.RegisterStatusBarResult;
import com.android.settingslib.applications.InterestingConfigChanges;
import com.android.systemui.Dumpable;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.accessibility.SystemActions;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.recents.Recents;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.AccessibilityManagerWrapper;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.pip.Pip;
import dagger.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Optional;
/* loaded from: classes.dex */
public class NavigationBarController implements CommandQueue.Callbacks, ConfigurationController.ConfigurationListener, NavigationModeController.ModeChangedListener, Dumpable {
    private static final String TAG = "NavigationBarController";
    private final AccessibilityButtonModeObserver mAccessibilityButtonModeObserver;
    private final AccessibilityManager mAccessibilityManager;
    private final AccessibilityManagerWrapper mAccessibilityManagerWrapper;
    private final Lazy<AssistManager> mAssistManagerLazy;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final CommandQueue mCommandQueue;
    private final InterestingConfigChanges mConfigChanges;
    private final Context mContext;
    private final DeviceProvisionedController mDeviceProvisionedController;
    private final DisplayManager mDisplayManager;
    private final Handler mHandler;
    private boolean mIsTablet;
    private final MetricsLogger mMetricsLogger;
    private final NavigationBarOverlayController mNavBarOverlayController;
    private int mNavMode;
    @VisibleForTesting
    SparseArray<NavigationBar> mNavigationBars = new SparseArray<>();
    private final NavigationModeController mNavigationModeController;
    private final NotificationRemoteInputManager mNotificationRemoteInputManager;
    private final NotificationShadeDepthController mNotificationShadeDepthController;
    private final OverviewProxyService mOverviewProxyService;
    private final Optional<Pip> mPipOptional;
    private final Optional<Recents> mRecentsOptional;
    private final ShadeController mShadeController;
    private final Optional<LegacySplitScreen> mSplitScreenOptional;
    private final Lazy<StatusBar> mStatusBarLazy;
    private final StatusBarStateController mStatusBarStateController;
    private final SysUiState mSysUiFlagsContainer;
    private final SystemActions mSystemActions;
    private final TaskbarDelegate mTaskbarDelegate;
    private final UiEventLogger mUiEventLogger;
    private final UserTracker mUserTracker;
    private final WindowManager mWindowManager;

    public NavigationBarController(Context context, WindowManager windowManager, Lazy<AssistManager> lazy, AccessibilityManager accessibilityManager, AccessibilityManagerWrapper accessibilityManagerWrapper, DeviceProvisionedController deviceProvisionedController, MetricsLogger metricsLogger, OverviewProxyService overviewProxyService, NavigationModeController navigationModeController, AccessibilityButtonModeObserver accessibilityButtonModeObserver, StatusBarStateController statusBarStateController, SysUiState sysUiState, BroadcastDispatcher broadcastDispatcher, CommandQueue commandQueue, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<Recents> optional3, Lazy<StatusBar> lazy2, ShadeController shadeController, NotificationRemoteInputManager notificationRemoteInputManager, NotificationShadeDepthController notificationShadeDepthController, SystemActions systemActions, Handler handler, UiEventLogger uiEventLogger, NavigationBarOverlayController navigationBarOverlayController, ConfigurationController configurationController, UserTracker userTracker) {
        InterestingConfigChanges interestingConfigChanges = new InterestingConfigChanges(1073742592);
        this.mConfigChanges = interestingConfigChanges;
        this.mContext = context;
        this.mWindowManager = windowManager;
        this.mAssistManagerLazy = lazy;
        this.mAccessibilityManager = accessibilityManager;
        this.mAccessibilityManagerWrapper = accessibilityManagerWrapper;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mMetricsLogger = metricsLogger;
        this.mOverviewProxyService = overviewProxyService;
        this.mNavigationModeController = navigationModeController;
        this.mAccessibilityButtonModeObserver = accessibilityButtonModeObserver;
        this.mStatusBarStateController = statusBarStateController;
        this.mSysUiFlagsContainer = sysUiState;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mCommandQueue = commandQueue;
        this.mPipOptional = optional;
        this.mSplitScreenOptional = optional2;
        this.mRecentsOptional = optional3;
        this.mStatusBarLazy = lazy2;
        this.mShadeController = shadeController;
        this.mNotificationRemoteInputManager = notificationRemoteInputManager;
        this.mNotificationShadeDepthController = notificationShadeDepthController;
        this.mSystemActions = systemActions;
        this.mUiEventLogger = uiEventLogger;
        this.mHandler = handler;
        this.mDisplayManager = (DisplayManager) context.getSystemService(DisplayManager.class);
        commandQueue.addCallback((CommandQueue.Callbacks) this);
        configurationController.addCallback(this);
        interestingConfigChanges.applyNewConfig(context.getResources());
        this.mNavBarOverlayController = navigationBarOverlayController;
        this.mNavMode = navigationModeController.addListener(this);
        navigationModeController.addListener(this);
        this.mTaskbarDelegate = new TaskbarDelegate(overviewProxyService);
        this.mIsTablet = isTablet(context.getResources().getConfiguration());
        this.mUserTracker = userTracker;
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onConfigChanged(Configuration configuration) {
        boolean z = this.mIsTablet;
        boolean isTablet = isTablet(configuration);
        this.mIsTablet = isTablet;
        int i = 0;
        if ((isTablet != z) && updateNavbarForTaskbar()) {
            return;
        }
        if (this.mConfigChanges.applyNewConfig(this.mContext.getResources())) {
            while (i < this.mNavigationBars.size()) {
                recreateNavigationBar(this.mNavigationBars.keyAt(i));
                i++;
            }
            return;
        }
        while (i < this.mNavigationBars.size()) {
            this.mNavigationBars.valueAt(i).onConfigurationChanged(configuration);
            i++;
        }
    }

    @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
    public void onNavigationModeChanged(int i) {
        int i2 = this.mNavMode;
        if (i2 != i) {
            this.mNavMode = i;
            this.mHandler.post(new Runnable(i2) { // from class: com.android.systemui.navigationbar.NavigationBarController$$ExternalSyntheticLambda0
                public final /* synthetic */ int f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    NavigationBarController.this.lambda$onNavigationModeChanged$0(this.f$1);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onNavigationModeChanged$0(int i) {
        if (i != this.mNavMode) {
            updateNavbarForTaskbar();
        }
        for (int i2 = 0; i2 < this.mNavigationBars.size(); i2++) {
            NavigationBar valueAt = this.mNavigationBars.valueAt(i2);
            if (valueAt != null) {
                valueAt.getView().updateStates();
            }
        }
    }

    public boolean updateNavbarForTaskbar() {
        if (!isThreeButtonTaskbarFlagEnabled()) {
            return false;
        }
        if (this.mIsTablet && this.mNavMode == 0) {
            removeNavigationBar(this.mContext.getDisplayId());
            this.mCommandQueue.addCallback((CommandQueue.Callbacks) this.mTaskbarDelegate);
            return true;
        } else if (this.mNavigationBars.get(this.mContext.getDisplayId()) != null) {
            return true;
        } else {
            createNavigationBar(this.mContext.getDisplay(), null, null);
            this.mCommandQueue.removeCallback((CommandQueue.Callbacks) this.mTaskbarDelegate);
            return true;
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void onDisplayRemoved(int i) {
        removeNavigationBar(i);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void onDisplayReady(int i) {
        Display display = this.mDisplayManager.getDisplay(i);
        this.mIsTablet = isTablet(this.mContext.getResources().getConfiguration());
        createNavigationBar(display, null, null);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void setNavigationBarLumaSamplingEnabled(int i, boolean z) {
        NavigationBarView navigationBarView = getNavigationBarView(i);
        if (navigationBarView != null) {
            navigationBarView.setNavigationBarLumaSamplingEnabled(z);
        }
    }

    private void recreateNavigationBar(int i) {
        Bundle bundle = new Bundle();
        NavigationBar navigationBar = this.mNavigationBars.get(i);
        if (navigationBar != null) {
            navigationBar.onSaveInstanceState(bundle);
        }
        removeNavigationBar(i);
        createNavigationBar(this.mDisplayManager.getDisplay(i), bundle, null);
    }

    public void createNavigationBars(boolean z, RegisterStatusBarResult registerStatusBarResult) {
        if (!updateNavbarForTaskbar()) {
            Display[] displays = this.mDisplayManager.getDisplays();
            for (Display display : displays) {
                if (z || display.getDisplayId() != 0) {
                    createNavigationBar(display, null, registerStatusBarResult);
                }
            }
        }
    }

    @VisibleForTesting
    void createNavigationBar(final Display display, Bundle bundle, final RegisterStatusBarResult registerStatusBarResult) {
        Context context;
        if (display != null && !isThreeButtonTaskbarEnabled()) {
            int displayId = display.getDisplayId();
            boolean z = displayId == 0;
            try {
                if (WindowManagerGlobal.getWindowManagerService().hasNavigationBar(displayId)) {
                    if (z) {
                        context = this.mContext;
                    } else {
                        context = this.mContext.createDisplayContext(display);
                    }
                    final NavigationBar navigationBar = new NavigationBar(context, this.mWindowManager, this.mAssistManagerLazy, this.mAccessibilityManager, this.mAccessibilityManagerWrapper, this.mDeviceProvisionedController, this.mMetricsLogger, this.mOverviewProxyService, this.mNavigationModeController, this.mAccessibilityButtonModeObserver, this.mStatusBarStateController, this.mSysUiFlagsContainer, this.mBroadcastDispatcher, this.mCommandQueue, this.mPipOptional, this.mSplitScreenOptional, this.mRecentsOptional, this.mStatusBarLazy, this.mShadeController, this.mNotificationRemoteInputManager, this.mNotificationShadeDepthController, this.mSystemActions, this.mHandler, this.mNavBarOverlayController, this.mUiEventLogger, this.mUserTracker);
                    this.mNavigationBars.put(displayId, navigationBar);
                    navigationBar.createView(bundle).addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.android.systemui.navigationbar.NavigationBarController.1
                        @Override // android.view.View.OnAttachStateChangeListener
                        public void onViewAttachedToWindow(View view) {
                            if (registerStatusBarResult != null) {
                                NavigationBar navigationBar2 = navigationBar;
                                int displayId2 = display.getDisplayId();
                                RegisterStatusBarResult registerStatusBarResult2 = registerStatusBarResult;
                                navigationBar2.setImeWindowStatus(displayId2, registerStatusBarResult2.mImeToken, registerStatusBarResult2.mImeWindowVis, registerStatusBarResult2.mImeBackDisposition, registerStatusBarResult2.mShowImeSwitcher);
                            }
                        }

                        @Override // android.view.View.OnAttachStateChangeListener
                        public void onViewDetachedFromWindow(View view) {
                            view.removeOnAttachStateChangeListener(this);
                        }
                    });
                }
            } catch (RemoteException unused) {
                Log.w(TAG, "Cannot get WindowManager.");
            }
        }
    }

    void removeNavigationBar(int i) {
        NavigationBar navigationBar = this.mNavigationBars.get(i);
        if (navigationBar != null) {
            navigationBar.setAutoHideController(null);
            navigationBar.destroyView();
            this.mNavigationBars.remove(i);
        }
    }

    public void checkNavBarModes(int i) {
        NavigationBar navigationBar = this.mNavigationBars.get(i);
        if (navigationBar != null) {
            navigationBar.checkNavBarModes();
        }
    }

    public void finishBarAnimations(int i) {
        NavigationBar navigationBar = this.mNavigationBars.get(i);
        if (navigationBar != null) {
            navigationBar.finishBarAnimations();
        }
    }

    public void touchAutoDim(int i) {
        NavigationBar navigationBar = this.mNavigationBars.get(i);
        if (navigationBar != null) {
            navigationBar.touchAutoDim();
        }
    }

    public void transitionTo(int i, int i2, boolean z) {
        NavigationBar navigationBar = this.mNavigationBars.get(i);
        if (navigationBar != null) {
            navigationBar.transitionTo(i2, z);
        }
    }

    public void disableAnimationsDuringHide(int i, long j) {
        NavigationBar navigationBar = this.mNavigationBars.get(i);
        if (navigationBar != null) {
            navigationBar.disableAnimationsDuringHide(j);
        }
    }

    public NavigationBarView getDefaultNavigationBarView() {
        return getNavigationBarView(0);
    }

    public NavigationBarView getNavigationBarView(int i) {
        NavigationBar navigationBar = this.mNavigationBars.get(i);
        if (navigationBar == null) {
            return null;
        }
        return navigationBar.getView();
    }

    public NavigationBar getDefaultNavigationBar() {
        return this.mNavigationBars.get(0);
    }

    private boolean isThreeButtonTaskbarEnabled() {
        return this.mIsTablet && this.mNavMode == 0 && isThreeButtonTaskbarFlagEnabled();
    }

    private boolean isThreeButtonTaskbarFlagEnabled() {
        return SystemProperties.getBoolean("persist.debug.taskbar_three_button", false);
    }

    private boolean isTablet(Configuration configuration) {
        float f = Resources.getSystem().getDisplayMetrics().density;
        return ((float) Math.min((int) (((float) configuration.screenWidthDp) * f), (int) (f * ((float) configuration.screenHeightDp)))) / (((float) this.mContext.getResources().getDisplayMetrics().densityDpi) / 160.0f) >= 600.0f;
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        for (int i = 0; i < this.mNavigationBars.size(); i++) {
            if (i > 0) {
                printWriter.println();
            }
            this.mNavigationBars.valueAt(i).dump(printWriter);
        }
    }
}
