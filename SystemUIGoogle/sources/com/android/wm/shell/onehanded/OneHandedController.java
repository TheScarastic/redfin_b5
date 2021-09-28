package com.android.wm.shell.onehanded;

import android.content.ComponentName;
import android.content.Context;
import android.content.om.IOverlayManager;
import android.content.om.OverlayInfo;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.graphics.Rect;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.Slog;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.window.WindowContainerTransaction;
import com.android.internal.logging.UiEventLogger;
import com.android.wm.shell.R;
import com.android.wm.shell.common.DisplayChangeController;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.ExecutorUtils;
import com.android.wm.shell.common.RemoteCallable;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TaskStackListenerCallback;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.onehanded.IOneHanded;
import com.android.wm.shell.onehanded.OneHandedTimeoutHandler;
import com.android.wm.shell.onehanded.OneHandedTouchHandler;
import java.io.PrintWriter;
/* loaded from: classes2.dex */
public class OneHandedController implements RemoteCallable<OneHandedController> {
    private final AccessibilityManager mAccessibilityManager;
    private OneHandedBackgroundPanelOrganizer mBackgroundPanelOrganizer;
    private Context mContext;
    private OneHandedDisplayAreaOrganizer mDisplayAreaOrganizer;
    private final DisplayController mDisplayController;
    private final DisplayController.OnDisplaysChangedListener mDisplaysChangedListener;
    private OneHandedEventCallback mEventCallback;
    private volatile boolean mIsOneHandedEnabled;
    private boolean mIsShortcutEnabled;
    private volatile boolean mIsSwipeToNotificationEnabled;
    private boolean mKeyguardShowing;
    private boolean mLockedDisabled;
    private final ShellExecutor mMainExecutor;
    private final Handler mMainHandler;
    private float mOffSetFraction;
    private final OneHandedAccessibilityUtil mOneHandedAccessibilityUtil;
    private final OneHandedSettingsUtil mOneHandedSettingsUtil;
    private OneHandedUiEventLogger mOneHandedUiEventLogger;
    private final IOverlayManager mOverlayManager;
    private final DisplayChangeController.OnDisplayChangingListener mRotationController;
    private final OneHandedState mState;
    private boolean mTaskChangeToExit;
    private final TaskStackListenerImpl mTaskStackListener;
    private final OneHandedTimeoutHandler mTimeoutHandler;
    private final OneHandedTouchHandler mTouchHandler;
    private final OneHandedTutorialHandler mTutorialHandler;
    private final OneHandedImpl mImpl = new OneHandedImpl();
    private AccessibilityManager.AccessibilityStateChangeListener mAccessibilityStateChangeListener = new AccessibilityManager.AccessibilityStateChangeListener() { // from class: com.android.wm.shell.onehanded.OneHandedController.2
        @Override // android.view.accessibility.AccessibilityManager.AccessibilityStateChangeListener
        public void onAccessibilityStateChanged(boolean z) {
            if (OneHandedController.this.isInitialized()) {
                if (z) {
                    OneHandedController.this.mTimeoutHandler.setTimeout(OneHandedController.this.mAccessibilityManager.getRecommendedTimeoutMillis(OneHandedController.this.mOneHandedSettingsUtil.getSettingsOneHandedModeTimeout(OneHandedController.this.mContext.getContentResolver(), OneHandedController.this.mUserId) * 1000, 4) / 1000);
                    return;
                }
                OneHandedController.this.mTimeoutHandler.setTimeout(OneHandedController.this.mOneHandedSettingsUtil.getSettingsOneHandedModeTimeout(OneHandedController.this.mContext.getContentResolver(), OneHandedController.this.mUserId));
            }
        }
    };
    private final OneHandedTransitionCallback mTransitionCallBack = new OneHandedTransitionCallback() { // from class: com.android.wm.shell.onehanded.OneHandedController.3
        @Override // com.android.wm.shell.onehanded.OneHandedTransitionCallback
        public void onStartFinished(Rect rect) {
            OneHandedController.this.mState.setState(2);
            OneHandedController.this.notifyShortcutStateChanged(2);
        }

        @Override // com.android.wm.shell.onehanded.OneHandedTransitionCallback
        public void onStopFinished(Rect rect) {
            OneHandedController.this.mState.setState(0);
            OneHandedController.this.notifyShortcutStateChanged(0);
            OneHandedController.this.mBackgroundPanelOrganizer.onStopFinished();
        }
    };
    private final TaskStackListenerCallback mTaskStackListenerCallback = new TaskStackListenerCallback() { // from class: com.android.wm.shell.onehanded.OneHandedController.4
        @Override // com.android.wm.shell.common.TaskStackListenerCallback
        public void onTaskCreated(int i, ComponentName componentName) {
            OneHandedController.this.stopOneHanded(5);
        }

        @Override // com.android.wm.shell.common.TaskStackListenerCallback
        public void onTaskMovedToFront(int i) {
            OneHandedController.this.stopOneHanded(5);
        }
    };
    private int mUserId = UserHandle.myUserId();
    private final ContentObserver mActivatedObserver = getObserver(new Runnable() { // from class: com.android.wm.shell.onehanded.OneHandedController$$ExternalSyntheticLambda3
        @Override // java.lang.Runnable
        public final void run() {
            OneHandedController.this.onActivatedActionChanged();
        }
    });
    private final ContentObserver mEnabledObserver = getObserver(new Runnable() { // from class: com.android.wm.shell.onehanded.OneHandedController$$ExternalSyntheticLambda4
        @Override // java.lang.Runnable
        public final void run() {
            OneHandedController.this.onEnabledSettingChanged();
        }
    });
    private final ContentObserver mSwipeToNotificationEnabledObserver = getObserver(new Runnable() { // from class: com.android.wm.shell.onehanded.OneHandedController$$ExternalSyntheticLambda6
        @Override // java.lang.Runnable
        public final void run() {
            OneHandedController.this.onSwipeToNotificationEnabledChanged();
        }
    });
    private final ContentObserver mShortcutEnabledObserver = getObserver(new Runnable() { // from class: com.android.wm.shell.onehanded.OneHandedController$$ExternalSyntheticLambda5
        @Override // java.lang.Runnable
        public final void run() {
            OneHandedController.this.onShortcutEnabledChanged();
        }
    });

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction) {
        if (isInitialized()) {
            this.mDisplayAreaOrganizer.onRotateDisplay(this.mContext, i3, windowContainerTransaction);
            this.mOneHandedUiEventLogger.writeEvent(4);
        }
    }

    /* access modifiers changed from: private */
    public boolean isInitialized() {
        if (this.mDisplayAreaOrganizer != null && this.mDisplayController != null && this.mOneHandedSettingsUtil != null) {
            return true;
        }
        Slog.w("OneHandedController", "Components may not initialized yet!");
        return false;
    }

    public static OneHandedController create(Context context, WindowManager windowManager, DisplayController displayController, DisplayLayout displayLayout, TaskStackListenerImpl taskStackListenerImpl, UiEventLogger uiEventLogger, ShellExecutor shellExecutor, Handler handler) {
        if (!SystemProperties.getBoolean("ro.support_one_handed_mode", false)) {
            Slog.w("OneHandedController", "Device doesn't support OneHanded feature");
            return null;
        }
        OneHandedSettingsUtil oneHandedSettingsUtil = new OneHandedSettingsUtil();
        OneHandedAccessibilityUtil oneHandedAccessibilityUtil = new OneHandedAccessibilityUtil(context);
        OneHandedTimeoutHandler oneHandedTimeoutHandler = new OneHandedTimeoutHandler(shellExecutor);
        OneHandedState oneHandedState = new OneHandedState();
        OneHandedTutorialHandler oneHandedTutorialHandler = new OneHandedTutorialHandler(context, oneHandedSettingsUtil, windowManager);
        OneHandedAnimationController oneHandedAnimationController = new OneHandedAnimationController(context);
        OneHandedTouchHandler oneHandedTouchHandler = new OneHandedTouchHandler(oneHandedTimeoutHandler, shellExecutor);
        OneHandedBackgroundPanelOrganizer oneHandedBackgroundPanelOrganizer = new OneHandedBackgroundPanelOrganizer(context, displayLayout, oneHandedSettingsUtil, shellExecutor);
        return new OneHandedController(context, displayController, oneHandedBackgroundPanelOrganizer, new OneHandedDisplayAreaOrganizer(context, displayLayout, oneHandedSettingsUtil, oneHandedAnimationController, oneHandedTutorialHandler, oneHandedBackgroundPanelOrganizer, shellExecutor), oneHandedTouchHandler, oneHandedTutorialHandler, oneHandedSettingsUtil, oneHandedAccessibilityUtil, oneHandedTimeoutHandler, oneHandedState, new OneHandedUiEventLogger(uiEventLogger), IOverlayManager.Stub.asInterface(ServiceManager.getService("overlay")), taskStackListenerImpl, shellExecutor, handler);
    }

    OneHandedController(Context context, DisplayController displayController, OneHandedBackgroundPanelOrganizer oneHandedBackgroundPanelOrganizer, OneHandedDisplayAreaOrganizer oneHandedDisplayAreaOrganizer, OneHandedTouchHandler oneHandedTouchHandler, OneHandedTutorialHandler oneHandedTutorialHandler, OneHandedSettingsUtil oneHandedSettingsUtil, OneHandedAccessibilityUtil oneHandedAccessibilityUtil, OneHandedTimeoutHandler oneHandedTimeoutHandler, OneHandedState oneHandedState, OneHandedUiEventLogger oneHandedUiEventLogger, IOverlayManager iOverlayManager, TaskStackListenerImpl taskStackListenerImpl, ShellExecutor shellExecutor, Handler handler) {
        OneHandedController$$ExternalSyntheticLambda0 oneHandedController$$ExternalSyntheticLambda0 = new DisplayChangeController.OnDisplayChangingListener() { // from class: com.android.wm.shell.onehanded.OneHandedController$$ExternalSyntheticLambda0
            @Override // com.android.wm.shell.common.DisplayChangeController.OnDisplayChangingListener
            public final void onRotateDisplay(int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction) {
                OneHandedController.this.lambda$new$0(i, i2, i3, windowContainerTransaction);
            }
        };
        this.mRotationController = oneHandedController$$ExternalSyntheticLambda0;
        AnonymousClass1 r6 = new DisplayController.OnDisplaysChangedListener() { // from class: com.android.wm.shell.onehanded.OneHandedController.1
            @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
            public void onDisplayConfigurationChanged(int i, Configuration configuration) {
                if (i == 0 && OneHandedController.this.isInitialized()) {
                    OneHandedController.this.updateDisplayLayout(i);
                }
            }

            @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
            public void onDisplayAdded(int i) {
                if (i == 0 && OneHandedController.this.isInitialized()) {
                    OneHandedController.this.updateDisplayLayout(i);
                }
            }
        };
        this.mDisplaysChangedListener = r6;
        this.mContext = context;
        this.mOneHandedSettingsUtil = oneHandedSettingsUtil;
        this.mOneHandedAccessibilityUtil = oneHandedAccessibilityUtil;
        this.mBackgroundPanelOrganizer = oneHandedBackgroundPanelOrganizer;
        this.mDisplayAreaOrganizer = oneHandedDisplayAreaOrganizer;
        this.mDisplayController = displayController;
        this.mTouchHandler = oneHandedTouchHandler;
        this.mState = oneHandedState;
        this.mTutorialHandler = oneHandedTutorialHandler;
        this.mOverlayManager = iOverlayManager;
        this.mMainExecutor = shellExecutor;
        this.mMainHandler = handler;
        this.mOneHandedUiEventLogger = oneHandedUiEventLogger;
        this.mTaskStackListener = taskStackListenerImpl;
        displayController.addDisplayWindowListener(r6);
        int i = SystemProperties.getInt("persist.debug.one_handed_offset_percentage", Math.round(context.getResources().getFraction(R.fraction.config_one_handed_offset, 1, 1) * 100.0f));
        this.mOffSetFraction = ((float) i) / 100.0f;
        this.mIsOneHandedEnabled = oneHandedSettingsUtil.getSettingsOneHandedModeEnabled(context.getContentResolver(), this.mUserId);
        this.mIsSwipeToNotificationEnabled = oneHandedSettingsUtil.getSettingsSwipeToNotificationEnabled(context.getContentResolver(), this.mUserId);
        this.mTimeoutHandler = oneHandedTimeoutHandler;
        displayController.addDisplayChangingController(oneHandedController$$ExternalSyntheticLambda0);
        setupCallback();
        registerSettingObservers(this.mUserId);
        setupTimeoutListener();
        setupGesturalOverlay();
        updateSettings();
        AccessibilityManager instance = AccessibilityManager.getInstance(context);
        this.mAccessibilityManager = instance;
        instance.addAccessibilityStateChangeListener(this.mAccessibilityStateChangeListener);
        oneHandedState.addSListeners(this.mBackgroundPanelOrganizer);
        oneHandedState.addSListeners(oneHandedTutorialHandler);
    }

    public OneHanded asOneHanded() {
        return this.mImpl;
    }

    @Override // com.android.wm.shell.common.RemoteCallable
    public Context getContext() {
        return this.mContext;
    }

    @Override // com.android.wm.shell.common.RemoteCallable
    public ShellExecutor getRemoteCallExecutor() {
        return this.mMainExecutor;
    }

    void setOneHandedEnabled(boolean z) {
        this.mIsOneHandedEnabled = z;
        updateOneHandedEnabled();
    }

    void setTaskChangeToExit(boolean z) {
        if (z) {
            this.mTaskStackListener.addListener(this.mTaskStackListenerCallback);
        } else {
            this.mTaskStackListener.removeListener(this.mTaskStackListenerCallback);
        }
        this.mTaskChangeToExit = z;
    }

    void setSwipeToNotificationEnabled(boolean z) {
        this.mIsSwipeToNotificationEnabled = z;
    }

    void notifyShortcutStateChanged(int i) {
        if (isShortcutEnabled()) {
            this.mOneHandedSettingsUtil.setOneHandedModeActivated(this.mContext.getContentResolver(), i == 2 ? 1 : 0, this.mUserId);
        }
    }

    /* access modifiers changed from: package-private */
    public void startOneHanded() {
        if (isLockedDisabled() || this.mKeyguardShowing) {
            Slog.d("OneHandedController", "Temporary lock disabled");
        } else if (!this.mDisplayAreaOrganizer.isReady()) {
            this.mMainExecutor.executeDelayed(new Runnable() { // from class: com.android.wm.shell.onehanded.OneHandedController$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    OneHandedController.this.startOneHanded();
                }
            }, 10);
        } else if (!this.mState.isTransitioning() && !this.mState.isInOneHanded()) {
            int rotation = this.mDisplayAreaOrganizer.getDisplayLayout().rotation();
            if (rotation == 0 || rotation == 2) {
                this.mState.setState(1);
                int round = Math.round(((float) this.mDisplayAreaOrganizer.getDisplayLayout().height()) * this.mOffSetFraction);
                OneHandedAccessibilityUtil oneHandedAccessibilityUtil = this.mOneHandedAccessibilityUtil;
                oneHandedAccessibilityUtil.announcementForScreenReader(oneHandedAccessibilityUtil.getOneHandedStartDescription());
                this.mBackgroundPanelOrganizer.onStart();
                this.mDisplayAreaOrganizer.scheduleOffset(0, round);
                this.mTimeoutHandler.resetTimer();
                this.mOneHandedUiEventLogger.writeEvent(0);
                return;
            }
            Slog.w("OneHandedController", "One handed mode only support portrait mode");
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: stopOneHanded */
    public void lambda$updateOneHandedEnabled$4() {
        stopOneHanded(1);
    }

    /* access modifiers changed from: private */
    public void stopOneHanded(int i) {
        if (!this.mState.isTransitioning() && this.mState.getState() != 0) {
            this.mState.setState(3);
            OneHandedAccessibilityUtil oneHandedAccessibilityUtil = this.mOneHandedAccessibilityUtil;
            oneHandedAccessibilityUtil.announcementForScreenReader(oneHandedAccessibilityUtil.getOneHandedStopDescription());
            this.mDisplayAreaOrganizer.scheduleOffset(0, 0);
            this.mTimeoutHandler.removeTimer();
            this.mOneHandedUiEventLogger.writeEvent(i);
        }
    }

    void registerEventCallback(OneHandedEventCallback oneHandedEventCallback) {
        this.mEventCallback = oneHandedEventCallback;
    }

    void registerTransitionCallback(OneHandedTransitionCallback oneHandedTransitionCallback) {
        this.mDisplayAreaOrganizer.registerTransitionCallback(oneHandedTransitionCallback);
    }

    private void setupCallback() {
        this.mTouchHandler.registerTouchEventListener(new OneHandedTouchHandler.OneHandedTouchEventCallback() { // from class: com.android.wm.shell.onehanded.OneHandedController$$ExternalSyntheticLambda2
            @Override // com.android.wm.shell.onehanded.OneHandedTouchHandler.OneHandedTouchEventCallback
            public final void onStop() {
                OneHandedController.this.lambda$setupCallback$1();
            }
        });
        this.mDisplayAreaOrganizer.registerTransitionCallback(this.mTouchHandler);
        this.mDisplayAreaOrganizer.registerTransitionCallback(this.mTutorialHandler);
        this.mDisplayAreaOrganizer.registerTransitionCallback(this.mTransitionCallBack);
        if (this.mTaskChangeToExit) {
            this.mTaskStackListener.addListener(this.mTaskStackListenerCallback);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setupCallback$1() {
        stopOneHanded(2);
    }

    private void registerSettingObservers(int i) {
        this.mOneHandedSettingsUtil.registerSettingsKeyObserver("one_handed_mode_activated", this.mContext.getContentResolver(), this.mActivatedObserver, i);
        this.mOneHandedSettingsUtil.registerSettingsKeyObserver("one_handed_mode_enabled", this.mContext.getContentResolver(), this.mEnabledObserver, i);
        this.mOneHandedSettingsUtil.registerSettingsKeyObserver("swipe_bottom_to_notification_enabled", this.mContext.getContentResolver(), this.mSwipeToNotificationEnabledObserver, i);
        this.mOneHandedSettingsUtil.registerSettingsKeyObserver("accessibility_button_targets", this.mContext.getContentResolver(), this.mShortcutEnabledObserver, i);
    }

    private void unregisterSettingObservers() {
        this.mOneHandedSettingsUtil.unregisterSettingsKeyObserver(this.mContext.getContentResolver(), this.mEnabledObserver);
        this.mOneHandedSettingsUtil.unregisterSettingsKeyObserver(this.mContext.getContentResolver(), this.mSwipeToNotificationEnabledObserver);
        this.mOneHandedSettingsUtil.unregisterSettingsKeyObserver(this.mContext.getContentResolver(), this.mShortcutEnabledObserver);
    }

    private void updateSettings() {
        setOneHandedEnabled(this.mOneHandedSettingsUtil.getSettingsOneHandedModeEnabled(this.mContext.getContentResolver(), this.mUserId));
        this.mTimeoutHandler.setTimeout(this.mOneHandedSettingsUtil.getSettingsOneHandedModeTimeout(this.mContext.getContentResolver(), this.mUserId));
        setTaskChangeToExit(this.mOneHandedSettingsUtil.getSettingsTapsAppToExit(this.mContext.getContentResolver(), this.mUserId));
        setSwipeToNotificationEnabled(this.mOneHandedSettingsUtil.getSettingsSwipeToNotificationEnabled(this.mContext.getContentResolver(), this.mUserId));
        onShortcutEnabledChanged();
    }

    /* access modifiers changed from: private */
    public void updateDisplayLayout(int i) {
        DisplayLayout displayLayout = this.mDisplayController.getDisplayLayout(i);
        this.mDisplayAreaOrganizer.setDisplayLayout(displayLayout);
        this.mTutorialHandler.onDisplayChanged(displayLayout);
        this.mBackgroundPanelOrganizer.onDisplayChanged(displayLayout);
    }

    private ContentObserver getObserver(final Runnable runnable) {
        return new ContentObserver(this.mMainHandler) { // from class: com.android.wm.shell.onehanded.OneHandedController.5
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                runnable.run();
            }
        };
    }

    void notifyExpandNotification() {
        if (this.mEventCallback != null) {
            this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.onehanded.OneHandedController$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    OneHandedController.this.lambda$notifyExpandNotification$2();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyExpandNotification$2() {
        this.mEventCallback.notifyExpandNotification();
    }

    /* access modifiers changed from: package-private */
    public void onActivatedActionChanged() {
        if (!isShortcutEnabled()) {
            Slog.w("OneHandedController", "Shortcut not enabled, skip onActivatedActionChanged()");
            return;
        }
        boolean z = true;
        if (!isOneHandedEnabled()) {
            boolean oneHandedModeEnabled = this.mOneHandedSettingsUtil.setOneHandedModeEnabled(this.mContext.getContentResolver(), 1, this.mUserId);
            Slog.d("OneHandedController", "Auto enabled One-handed mode by shortcut trigger, success=" + oneHandedModeEnabled);
        }
        if (isSwipeToNotificationEnabled()) {
            notifyExpandNotification();
            return;
        }
        if (this.mState.getState() != 2) {
            z = false;
        }
        boolean oneHandedModeActivated = this.mOneHandedSettingsUtil.getOneHandedModeActivated(this.mContext.getContentResolver(), this.mUserId);
        if (!(z ^ oneHandedModeActivated)) {
            return;
        }
        if (oneHandedModeActivated) {
            startOneHanded();
        } else {
            lambda$updateOneHandedEnabled$4();
        }
    }

    /* access modifiers changed from: package-private */
    public void onEnabledSettingChanged() {
        boolean settingsOneHandedModeEnabled = this.mOneHandedSettingsUtil.getSettingsOneHandedModeEnabled(this.mContext.getContentResolver(), this.mUserId);
        this.mOneHandedUiEventLogger.writeEvent(settingsOneHandedModeEnabled ? 8 : 9);
        setOneHandedEnabled(settingsOneHandedModeEnabled);
        setEnabledGesturalOverlay(settingsOneHandedModeEnabled || this.mOneHandedSettingsUtil.getSettingsSwipeToNotificationEnabled(this.mContext.getContentResolver(), this.mUserId), true);
    }

    /* access modifiers changed from: package-private */
    public void onSwipeToNotificationEnabledChanged() {
        boolean settingsSwipeToNotificationEnabled = this.mOneHandedSettingsUtil.getSettingsSwipeToNotificationEnabled(this.mContext.getContentResolver(), this.mUserId);
        setSwipeToNotificationEnabled(settingsSwipeToNotificationEnabled);
        this.mOneHandedUiEventLogger.writeEvent(settingsSwipeToNotificationEnabled ? 18 : 19);
    }

    /* access modifiers changed from: package-private */
    public void onShortcutEnabledChanged() {
        boolean shortcutEnabled = this.mOneHandedSettingsUtil.getShortcutEnabled(this.mContext.getContentResolver(), this.mUserId);
        this.mIsShortcutEnabled = shortcutEnabled;
        this.mOneHandedUiEventLogger.writeEvent(shortcutEnabled ? 20 : 21);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setupTimeoutListener$3(int i) {
        stopOneHanded(6);
    }

    private void setupTimeoutListener() {
        this.mTimeoutHandler.registerTimeoutListener(new OneHandedTimeoutHandler.TimeoutListener() { // from class: com.android.wm.shell.onehanded.OneHandedController$$ExternalSyntheticLambda1
            @Override // com.android.wm.shell.onehanded.OneHandedTimeoutHandler.TimeoutListener
            public final void onTimeout(int i) {
                OneHandedController.this.lambda$setupTimeoutListener$3(i);
            }
        });
    }

    boolean isLockedDisabled() {
        return this.mLockedDisabled;
    }

    boolean isOneHandedEnabled() {
        return this.mIsOneHandedEnabled;
    }

    boolean isShortcutEnabled() {
        return this.mIsShortcutEnabled;
    }

    boolean isSwipeToNotificationEnabled() {
        return this.mIsSwipeToNotificationEnabled;
    }

    private void updateOneHandedEnabled() {
        if (this.mState.getState() == 1 || this.mState.getState() == 2) {
            this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.onehanded.OneHandedController$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    OneHandedController.this.lambda$updateOneHandedEnabled$4();
                }
            });
        }
        if (isOneHandedEnabled() && !isSwipeToNotificationEnabled()) {
            notifyShortcutStateChanged(this.mState.getState());
        }
        this.mTouchHandler.onOneHandedEnabled(this.mIsOneHandedEnabled);
        if (!this.mIsOneHandedEnabled) {
            this.mDisplayAreaOrganizer.unregisterOrganizer();
            this.mBackgroundPanelOrganizer.unregisterOrganizer();
            return;
        }
        if (this.mDisplayAreaOrganizer.getDisplayAreaTokenMap().isEmpty()) {
            this.mDisplayAreaOrganizer.registerOrganizer(3);
        }
        if (!this.mBackgroundPanelOrganizer.isRegistered()) {
            this.mBackgroundPanelOrganizer.registerOrganizer(8);
        }
    }

    private void setupGesturalOverlay() {
        if (this.mOneHandedSettingsUtil.getSettingsOneHandedModeEnabled(this.mContext.getContentResolver(), this.mUserId)) {
            OverlayInfo overlayInfo = null;
            try {
                this.mOverlayManager.setHighestPriority("com.android.internal.systemui.onehanded.gestural", -2);
                overlayInfo = this.mOverlayManager.getOverlayInfo("com.android.internal.systemui.onehanded.gestural", -2);
            } catch (RemoteException unused) {
            }
            if (overlayInfo != null && !overlayInfo.isEnabled()) {
                setEnabledGesturalOverlay(true, false);
            }
        }
    }

    private void setEnabledGesturalOverlay(boolean z, boolean z2) {
        if (this.mState.isTransitioning() || z2) {
            this.mMainExecutor.executeDelayed(new Runnable(z) { // from class: com.android.wm.shell.onehanded.OneHandedController$$ExternalSyntheticLambda10
                public final /* synthetic */ boolean f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    OneHandedController.this.lambda$setEnabledGesturalOverlay$5(this.f$1);
                }
            }, 250);
            return;
        }
        try {
            this.mOverlayManager.setEnabled("com.android.internal.systemui.onehanded.gestural", z, -2);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setEnabledGesturalOverlay$5(boolean z) {
        setEnabledGesturalOverlay(z, false);
    }

    void setLockedDisabled(boolean z, boolean z2) {
        boolean z3 = false;
        if (z2 != (this.mIsOneHandedEnabled || this.mIsSwipeToNotificationEnabled)) {
            if (z && !z2) {
                z3 = true;
            }
            this.mLockedDisabled = z3;
        }
    }

    /* access modifiers changed from: private */
    public void onConfigChanged(Configuration configuration) {
        if (this.mTutorialHandler != null && this.mBackgroundPanelOrganizer != null && this.mIsOneHandedEnabled && configuration.orientation != 2) {
            this.mBackgroundPanelOrganizer.onConfigurationChanged();
            this.mTutorialHandler.onConfigurationChanged();
        }
    }

    /* access modifiers changed from: private */
    public void onKeyguardVisibilityChanged(boolean z) {
        this.mKeyguardShowing = z;
    }

    /* access modifiers changed from: private */
    public void onUserSwitch(int i) {
        unregisterSettingObservers();
        this.mUserId = i;
        registerSettingObservers(i);
        updateSettings();
        updateOneHandedEnabled();
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println();
        printWriter.println("OneHandedController");
        printWriter.print("  mOffSetFraction=");
        printWriter.println(this.mOffSetFraction);
        printWriter.print("  mLockedDisabled=");
        printWriter.println(this.mLockedDisabled);
        printWriter.print("  mUserId=");
        printWriter.println(this.mUserId);
        printWriter.print("  isShortcutEnabled=");
        printWriter.println(isShortcutEnabled());
        OneHandedBackgroundPanelOrganizer oneHandedBackgroundPanelOrganizer = this.mBackgroundPanelOrganizer;
        if (oneHandedBackgroundPanelOrganizer != null) {
            oneHandedBackgroundPanelOrganizer.dump(printWriter);
        }
        OneHandedDisplayAreaOrganizer oneHandedDisplayAreaOrganizer = this.mDisplayAreaOrganizer;
        if (oneHandedDisplayAreaOrganizer != null) {
            oneHandedDisplayAreaOrganizer.dump(printWriter);
        }
        OneHandedTouchHandler oneHandedTouchHandler = this.mTouchHandler;
        if (oneHandedTouchHandler != null) {
            oneHandedTouchHandler.dump(printWriter);
        }
        OneHandedTimeoutHandler oneHandedTimeoutHandler = this.mTimeoutHandler;
        if (oneHandedTimeoutHandler != null) {
            oneHandedTimeoutHandler.dump(printWriter);
        }
        OneHandedState oneHandedState = this.mState;
        if (oneHandedState != null) {
            oneHandedState.dump(printWriter);
        }
        OneHandedTutorialHandler oneHandedTutorialHandler = this.mTutorialHandler;
        if (oneHandedTutorialHandler != null) {
            oneHandedTutorialHandler.dump(printWriter);
        }
        OneHandedAccessibilityUtil oneHandedAccessibilityUtil = this.mOneHandedAccessibilityUtil;
        if (oneHandedAccessibilityUtil != null) {
            oneHandedAccessibilityUtil.dump(printWriter);
        }
        this.mOneHandedSettingsUtil.dump(printWriter, "  ", this.mContext.getContentResolver(), this.mUserId);
        IOverlayManager iOverlayManager = this.mOverlayManager;
        if (iOverlayManager != null) {
            OverlayInfo overlayInfo = null;
            try {
                overlayInfo = iOverlayManager.getOverlayInfo("com.android.internal.systemui.onehanded.gestural", -2);
            } catch (RemoteException unused) {
            }
            if (overlayInfo != null && !overlayInfo.isEnabled()) {
                printWriter.print("  OverlayInfo=");
                printWriter.println(overlayInfo);
            }
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class OneHandedImpl implements OneHanded {
        private IOneHandedImpl mIOneHanded;

        private OneHandedImpl() {
        }

        @Override // com.android.wm.shell.onehanded.OneHanded
        public IOneHanded createExternalInterface() {
            IOneHandedImpl iOneHandedImpl = this.mIOneHanded;
            if (iOneHandedImpl != null) {
                iOneHandedImpl.invalidate();
            }
            IOneHandedImpl iOneHandedImpl2 = new IOneHandedImpl(OneHandedController.this);
            this.mIOneHanded = iOneHandedImpl2;
            return iOneHandedImpl2;
        }

        @Override // com.android.wm.shell.onehanded.OneHanded
        public void stopOneHanded() {
            OneHandedController.this.mMainExecutor.execute(new OneHandedController$OneHandedImpl$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$stopOneHanded$1() {
            OneHandedController.this.lambda$updateOneHandedEnabled$4();
        }

        @Override // com.android.wm.shell.onehanded.OneHanded
        public void stopOneHanded(int i) {
            OneHandedController.this.mMainExecutor.execute(new OneHandedController$OneHandedImpl$$ExternalSyntheticLambda1(this, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$stopOneHanded$2(int i) {
            OneHandedController.this.stopOneHanded(i);
        }

        @Override // com.android.wm.shell.onehanded.OneHanded
        public void setLockedDisabled(boolean z, boolean z2) {
            OneHandedController.this.mMainExecutor.execute(new OneHandedController$OneHandedImpl$$ExternalSyntheticLambda7(this, z, z2));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setLockedDisabled$3(boolean z, boolean z2) {
            OneHandedController.this.setLockedDisabled(z, z2);
        }

        @Override // com.android.wm.shell.onehanded.OneHanded
        public void registerEventCallback(OneHandedEventCallback oneHandedEventCallback) {
            OneHandedController.this.mMainExecutor.execute(new OneHandedController$OneHandedImpl$$ExternalSyntheticLambda4(this, oneHandedEventCallback));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$registerEventCallback$4(OneHandedEventCallback oneHandedEventCallback) {
            OneHandedController.this.registerEventCallback(oneHandedEventCallback);
        }

        @Override // com.android.wm.shell.onehanded.OneHanded
        public void registerTransitionCallback(OneHandedTransitionCallback oneHandedTransitionCallback) {
            OneHandedController.this.mMainExecutor.execute(new OneHandedController$OneHandedImpl$$ExternalSyntheticLambda5(this, oneHandedTransitionCallback));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$registerTransitionCallback$5(OneHandedTransitionCallback oneHandedTransitionCallback) {
            OneHandedController.this.registerTransitionCallback(oneHandedTransitionCallback);
        }

        @Override // com.android.wm.shell.onehanded.OneHanded
        public void onConfigChanged(Configuration configuration) {
            OneHandedController.this.mMainExecutor.execute(new OneHandedController$OneHandedImpl$$ExternalSyntheticLambda3(this, configuration));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onConfigChanged$6(Configuration configuration) {
            OneHandedController.this.onConfigChanged(configuration);
        }

        @Override // com.android.wm.shell.onehanded.OneHanded
        public void onUserSwitch(int i) {
            OneHandedController.this.mMainExecutor.execute(new OneHandedController$OneHandedImpl$$ExternalSyntheticLambda2(this, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserSwitch$7(int i) {
            OneHandedController.this.onUserSwitch(i);
        }

        @Override // com.android.wm.shell.onehanded.OneHanded
        public void onKeyguardVisibilityChanged(boolean z) {
            OneHandedController.this.mMainExecutor.execute(new OneHandedController$OneHandedImpl$$ExternalSyntheticLambda6(this, z));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onKeyguardVisibilityChanged$8(boolean z) {
            OneHandedController.this.onKeyguardVisibilityChanged(z);
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class IOneHandedImpl extends IOneHanded.Stub {
        private OneHandedController mController;

        IOneHandedImpl(OneHandedController oneHandedController) {
            this.mController = oneHandedController;
        }

        void invalidate() {
            this.mController = null;
        }

        @Override // com.android.wm.shell.onehanded.IOneHanded
        public void startOneHanded() {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startOneHanded", OneHandedController$IOneHandedImpl$$ExternalSyntheticLambda1.INSTANCE);
        }

        @Override // com.android.wm.shell.onehanded.IOneHanded
        public void stopOneHanded() {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "stopOneHanded", OneHandedController$IOneHandedImpl$$ExternalSyntheticLambda0.INSTANCE);
        }
    }
}
