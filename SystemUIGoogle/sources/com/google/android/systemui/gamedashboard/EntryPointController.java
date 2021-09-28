package com.google.android.systemui.gamedashboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.GameManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.view.AppearanceRegion;
import com.android.systemui.R$anim;
import com.android.systemui.R$integer;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.navigationbar.NavigationBarOverlayController;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.screenrecord.ScreenRecordDialog;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.shared.system.TaskStackChangeListener;
import com.android.systemui.shared.system.TaskStackChangeListeners;
import com.android.systemui.statusbar.CommandQueue;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import com.google.android.systemui.gamedashboard.GameDashboardUiEventLogger;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class EntryPointController extends NavigationBarOverlayController implements NavigationModeController.ModeChangedListener {
    private final AccessibilityManager mAccessibilityManager;
    private boolean mAlwaysOn;
    private final ContentObserver mAlwaysOnObserver;
    private int mCurrentUserId;
    private final FloatingEntryButton mEntryPoint;
    private final GameModeDndController mGameModeDndController;
    private String mGamePackageName;
    private ActivityManager.RunningTaskInfo mGameTaskInfo;
    private final boolean mHasGameOverlay;
    private Animator mHideAnimator;
    private boolean mIsImmersive;
    private int mNavBarMode;
    public final OverviewProxyService.OverviewProxyListener mOverviewProxyListener;
    private boolean mRecentsAnimationRunning;
    private final ShortcutBarController mShortcutBarController;
    private boolean mShouldShow;
    private final TaskStackListenerImpl mTaskStackListener;
    private final Optional<TaskSurfaceHelper> mTaskSurfaceHelper;
    private final ToastController mToast;
    private final int mTranslateDownAnimationDuration;
    private final int mTranslateUpAnimationDuration;
    private final GameDashboardUiEventLogger mUiEventLogger;
    private final CurrentUserTracker mUserTracker;
    private boolean mListenersRegistered = false;
    private final GameManager mGameManager = (GameManager) this.mContext.getSystemService(GameManager.class);

    public EntryPointController(Context context, AccessibilityManager accessibilityManager, BroadcastDispatcher broadcastDispatcher, CommandQueue commandQueue, GameModeDndController gameModeDndController, Handler handler, NavigationModeController navigationModeController, OverviewProxyService overviewProxyService, PackageManager packageManager, ShortcutBarController shortcutBarController, ToastController toastController, GameDashboardUiEventLogger gameDashboardUiEventLogger, Optional<TaskSurfaceHelper> optional) {
        super(context);
        AnonymousClass1 r4 = new OverviewProxyService.OverviewProxyListener() { // from class: com.google.android.systemui.gamedashboard.EntryPointController.1
            @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
            public void onSwipeUpGestureStarted() {
                EntryPointController.this.mShortcutBarController.hide();
                EntryPointController.this.mGameTaskInfo = null;
            }
        };
        this.mOverviewProxyListener = r4;
        this.mAccessibilityManager = accessibilityManager;
        this.mGameModeDndController = gameModeDndController;
        this.mNavBarMode = navigationModeController.addListener(this);
        this.mHasGameOverlay = packageManager.hasSystemFeature("com.google.android.feature.GAME_OVERLAY") || Build.IS_DEBUGGABLE;
        FloatingEntryButton floatingEntryButton = new FloatingEntryButton(this.mContext);
        this.mEntryPoint = floatingEntryButton;
        floatingEntryButton.setEntryPointController(this);
        floatingEntryButton.setEntryPointOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.gamedashboard.EntryPointController$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                EntryPointController.$r8$lambda$VcVcWE2qGRM_B0Sxv8eSSgrgnCU(EntryPointController.this, view);
            }
        });
        Resources resources = context.getResources();
        this.mTranslateUpAnimationDuration = resources.getInteger(R$integer.game_toast_translate_up_animation_duration);
        this.mTranslateDownAnimationDuration = resources.getInteger(R$integer.game_toast_translate_down_animation_duration);
        this.mTaskStackListener = new TaskStackListenerImpl();
        this.mShouldShow = false;
        this.mShortcutBarController = shortcutBarController;
        shortcutBarController.setEntryPointController(this);
        overviewProxyService.addCallback((OverviewProxyService.OverviewProxyListener) r4);
        this.mUiEventLogger = gameDashboardUiEventLogger;
        gameDashboardUiEventLogger.setEntryPointController(this);
        this.mTaskSurfaceHelper = optional;
        this.mToast = toastController;
        AnonymousClass2 r2 = new CurrentUserTracker(broadcastDispatcher) { // from class: com.google.android.systemui.gamedashboard.EntryPointController.2
            @Override // com.android.systemui.settings.CurrentUserTracker
            public void onUserSwitched(int i) {
                ((NavigationBarOverlayController) EntryPointController.this).mContext.getContentResolver().unregisterContentObserver(EntryPointController.this.mAlwaysOnObserver);
                EntryPointController.this.mCurrentUserId = i;
                ((NavigationBarOverlayController) EntryPointController.this).mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("game_dashboard_always_on"), false, EntryPointController.this.mAlwaysOnObserver, EntryPointController.this.mCurrentUserId);
                EntryPointController.this.checkAlwaysOn();
            }
        };
        this.mUserTracker = r2;
        r2.startTracking();
        this.mCurrentUserId = r2.getCurrentUserId();
        checkAlwaysOn();
        AnonymousClass3 r22 = new ContentObserver(handler) { // from class: com.google.android.systemui.gamedashboard.EntryPointController.3
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                super.onChange(z);
                EntryPointController.this.checkAlwaysOn();
            }
        };
        this.mAlwaysOnObserver = r22;
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("game_dashboard_always_on"), false, r22, this.mCurrentUserId);
        shortcutBarController.setEntryPointOnTouchListener(new View.OnClickListener() { // from class: com.google.android.systemui.gamedashboard.EntryPointController$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                EntryPointController.$r8$lambda$VcVcWE2qGRM_B0Sxv8eSSgrgnCU(EntryPointController.this, view);
            }
        });
        commandQueue.addCallback((CommandQueue.Callbacks) new CommandQueue.Callbacks() { // from class: com.google.android.systemui.gamedashboard.EntryPointController.4
            @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
            public void onRecentsAnimationStateChanged(boolean z) {
                EntryPointController.this.mRecentsAnimationRunning = z;
                EntryPointController.this.onRunningTaskChange();
            }

            @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
            public void onSystemBarAttributesChanged(int i, int i2, AppearanceRegion[] appearanceRegionArr, boolean z, int i3, boolean z2) {
                EntryPointController.this.mIsImmersive = z2 && i3 == 2;
                if (!EntryPointController.this.mAlwaysOn) {
                    EntryPointController.this.mShortcutBarController.updateVisibility(EntryPointController.this.mIsImmersive);
                }
            }
        });
        onRunningTaskChange();
    }

    @Override // com.android.systemui.navigationbar.NavigationBarOverlayController
    public boolean isNavigationBarOverlayEnabled() {
        return this.mHasGameOverlay;
    }

    @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
    public void onNavigationModeChanged(int i) {
        this.mNavBarMode = i;
    }

    @Override // com.android.systemui.navigationbar.NavigationBarOverlayController
    public void init(Consumer<Boolean> consumer, Consumer<Rect> consumer2) {
        this.mEntryPoint.setVisibilityChangedCallback(consumer);
        this.mShortcutBarController.setExcludeBackRegionCallback(consumer2);
    }

    @Override // com.android.systemui.navigationbar.NavigationBarOverlayController
    public void setCanShow(boolean z) {
        this.mEntryPoint.setCanShow(z);
    }

    @Override // com.android.systemui.navigationbar.NavigationBarOverlayController
    public void setButtonState(boolean z, boolean z2) {
        View currentView;
        this.mShortcutBarController.setNavBarButtonState(z);
        if ((!z && !this.mEntryPoint.isVisible()) || !this.mShouldShow || this.mAlwaysOn || (currentView = this.mEntryPoint.getCurrentView()) == null) {
            return;
        }
        if (z) {
            currentView.setTranslationY(200.0f);
            currentView.animate().translationY(0.0f).setDuration((long) this.mTranslateUpAnimationDuration);
            this.mEntryPoint.show(QuickStepContract.isGesturalMode(this.mNavBarMode));
            return;
        }
        currentView.animate().cancel();
        if (z2) {
            Animator animator = this.mHideAnimator;
            if (animator != null && animator.isRunning()) {
                this.mHideAnimator.pause();
            }
            this.mEntryPoint.hide();
            return;
        }
        Animator animator2 = this.mHideAnimator;
        if (animator2 == null || !animator2.isRunning()) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(currentView, "translationY", 200.0f);
            if (this.mAccessibilityManager.isEnabled()) {
                ofFloat.setStartDelay((long) this.mAccessibilityManager.getRecommendedTimeoutMillis(this.mTranslateDownAnimationDuration, 1));
            }
            ofFloat.setDuration((long) this.mTranslateDownAnimationDuration);
            ofFloat.setInterpolator(Interpolators.LINEAR);
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.gamedashboard.EntryPointController.5
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator3) {
                    EntryPointController.this.mEntryPoint.hide();
                }
            });
            this.mHideAnimator = ofFloat;
            ofFloat.start();
        }
    }

    @Override // com.android.systemui.navigationbar.NavigationBarOverlayController
    public void registerListeners() {
        if (!this.mListenersRegistered) {
            this.mListenersRegistered = true;
            TaskStackChangeListeners.getInstance().registerTaskStackListener(this.mTaskStackListener);
        }
    }

    @Override // com.android.systemui.navigationbar.NavigationBarOverlayController
    public void unregisterListeners() {
        if (this.mListenersRegistered) {
            this.mListenersRegistered = false;
            TaskStackChangeListeners.getInstance().unregisterTaskStackListener(this.mTaskStackListener);
        }
    }

    @Override // com.android.systemui.navigationbar.NavigationBarOverlayController
    public View getCurrentView() {
        return this.mEntryPoint.getCurrentView();
    }

    @Override // com.android.systemui.navigationbar.NavigationBarOverlayController
    public boolean isVisible() {
        return this.mEntryPoint.getCurrentView() != null && this.mEntryPoint.isVisible();
    }

    public String getGamePackageName() {
        return this.mGamePackageName;
    }

    public ActivityManager.RunningTaskInfo getGameTaskInfo() {
        return this.mGameTaskInfo;
    }

    public int getGameTaskId() {
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mGameTaskInfo;
        if (runningTaskInfo == null) {
            return -1;
        }
        return runningTaskInfo.taskId;
    }

    public void checkAlwaysOn() {
        boolean z = false;
        if (Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "game_dashboard_always_on", 0, this.mCurrentUserId) == 1) {
            z = true;
        }
        this.mAlwaysOn = z;
        this.mShortcutBarController.setEntryPointVisibility(z, this.mShouldShow);
    }

    public void onRunningTaskChange() {
        if (!this.mRecentsAnimationRunning) {
            this.mToast.remove();
            ActivityManager.RunningTaskInfo runningTask = ActivityManagerWrapper.getInstance().getRunningTask();
            if (runningTask != null && !ScreenRecordDialog.class.getName().equals(runningTask.baseActivity.getClassName())) {
                String packageName = runningTask.baseActivity.getPackageName();
                this.mGamePackageName = packageName;
                boolean z = false;
                boolean z2 = runningTask.topActivityInfo.applicationInfo.category == 0 && !packageName.equals("com.google.android.play.games") && !runningTask.topActivity.getPackageName().equals("com.google.android.play.games");
                this.mShouldShow = z2;
                if ((!z2 && this.mEntryPoint.isVisible()) || this.mAlwaysOn) {
                    this.mEntryPoint.hide();
                }
                boolean equals = runningTask.topActivity.getClassName().equals(GameMenuActivity.class.getName());
                this.mGameModeDndController.setGameModeDndRuleActive((this.mShouldShow || equals) && this.mHasGameOverlay);
                if (this.mShouldShow) {
                    int i = runningTask.taskId;
                    int gameMode = this.mGameManager.getGameMode(this.mGamePackageName);
                    boolean isGameModeDndOn = this.mGameModeDndController.isGameModeDndOn();
                    this.mTaskSurfaceHelper.ifPresent(new Consumer(i, gameMode) { // from class: com.google.android.systemui.gamedashboard.EntryPointController$$ExternalSyntheticLambda1
                        public final /* synthetic */ int f$0;
                        public final /* synthetic */ int f$1;

                        {
                            this.f$0 = r1;
                            this.f$1 = r2;
                        }

                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            EntryPointController.m647$r8$lambda$7p3Q0CD199K_DFpydbawO34zwY(this.f$0, this.f$1, (TaskSurfaceHelper) obj);
                        }
                    });
                    this.mToast.showLaunchText(i, gameMode, isGameModeDndOn);
                    ShortcutBarController shortcutBarController = this.mShortcutBarController;
                    if (this.mIsImmersive || this.mAlwaysOn) {
                        z = true;
                    }
                    shortcutBarController.updateVisibility(z);
                    if (this.mShortcutBarController.isFpsVisible()) {
                        this.mShortcutBarController.registerFps(runningTask.taskId);
                    }
                } else if (equals) {
                    this.mShortcutBarController.updateVisibility(true);
                } else {
                    this.mShortcutBarController.hide();
                }
                this.mGameTaskInfo = runningTask;
            }
        }
    }

    public void onEntryPointClicked(View view) {
        this.mEntryPoint.hide();
        this.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_LAUNCH);
        Intent createIntentForStart = GameMenuActivity.createIntentForStart(this.mContext);
        ActivityOptions makeCustomTaskAnimation = ActivityOptions.makeCustomTaskAnimation(this.mContext, R$anim.game_dashboard_fade_in, R$anim.game_dashboard_fade_out, null, null, null);
        makeCustomTaskAnimation.setLaunchTaskId(ActivityManagerWrapper.getInstance().getRunningTask().taskId);
        this.mContext.startActivity(createIntentForStart, makeCustomTaskAnimation.toBundle());
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class TaskStackListenerImpl extends TaskStackChangeListener {
        private TaskStackListenerImpl() {
            EntryPointController.this = r1;
        }

        @Override // com.android.systemui.shared.system.TaskStackChangeListener
        public void onTaskStackChanged() {
            EntryPointController.this.onRunningTaskChange();
        }
    }
}
