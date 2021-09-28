package com.android.systemui.navigationbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.IRotationWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManagerGlobal;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.systemui.Dependency;
import com.android.systemui.R$drawable;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.navigationbar.buttons.KeyButtonDrawable;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.TaskStackChangeListener;
import com.android.systemui.shared.system.TaskStackChangeListeners;
import com.android.systemui.statusbar.policy.AccessibilityManagerWrapper;
import com.android.systemui.statusbar.policy.RotationLockController;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class RotationButtonController {
    private final Context mContext;
    private int mDarkIconColor;
    private boolean mHomeRotationEnabled;
    private boolean mHoveringRotationSuggestion;
    private boolean mIsRecentsAnimationRunning;
    private int mLastRotationSuggestion;
    private int mLightIconColor;
    private boolean mPendingRotationSuggestion;
    private Consumer<Integer> mRotWatcherListener;
    private Animator mRotateHideAnimator;
    private RotationButton mRotationButton;
    private boolean mSkipOverrideUserLockPrefsOnce;
    private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());
    private final UiEventLogger mUiEventLogger = new UiEventLoggerImpl();
    private final ViewRippler mViewRippler = new ViewRippler();
    private boolean mListenersRegistered = false;
    private int mBehavior = 1;
    private int mIconResId = R$drawable.ic_sysbar_rotate_button_ccw_start_90;
    private final Runnable mRemoveRotationProposal = new Runnable() { // from class: com.android.systemui.navigationbar.RotationButtonController$$ExternalSyntheticLambda3
        @Override // java.lang.Runnable
        public final void run() {
            RotationButtonController.this.lambda$new$0();
        }
    };
    private final Runnable mCancelPendingRotationProposal = new Runnable() { // from class: com.android.systemui.navigationbar.RotationButtonController$$ExternalSyntheticLambda2
        @Override // java.lang.Runnable
        public final void run() {
            RotationButtonController.this.lambda$new$1();
        }
    };
    private final IRotationWatcher.Stub mRotationWatcher = new IRotationWatcher.Stub() { // from class: com.android.systemui.navigationbar.RotationButtonController.1
        public void onRotationChanged(int i) throws RemoteException {
            RotationButtonController.this.mMainThreadHandler.postAtFrontOfQueue(new RotationButtonController$1$$ExternalSyntheticLambda0(this, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onRotationChanged$0(int i) {
            if (RotationButtonController.this.mRotationLockController.isRotationLocked()) {
                if (RotationButtonController.this.shouldOverrideUserLockPrefs(i)) {
                    RotationButtonController.this.setRotationLockedAtAngle(i);
                }
                RotationButtonController.this.setRotateSuggestionButtonState(false, true);
            }
            if (RotationButtonController.this.mRotWatcherListener != null) {
                RotationButtonController.this.mRotWatcherListener.accept(Integer.valueOf(i));
            }
        }
    };
    private boolean mIsNavigationBarShowing = true;
    private RotationLockController mRotationLockController = (RotationLockController) Dependency.get(RotationLockController.class);
    private AccessibilityManagerWrapper mAccessibilityManagerWrapper = (AccessibilityManagerWrapper) Dependency.get(AccessibilityManagerWrapper.class);
    private TaskStackListenerImpl mTaskStackListener = new TaskStackListenerImpl();

    /* access modifiers changed from: package-private */
    public static boolean hasDisable2RotateSuggestionFlag(int i) {
        return (i & 16) != 0;
    }

    private boolean isRotationAnimationCCW(int i, int i2) {
        if (i == 0 && i2 == 1) {
            return false;
        }
        if (i == 0 && i2 == 2) {
            return true;
        }
        if (i == 0 && i2 == 3) {
            return true;
        }
        if (i == 1 && i2 == 0) {
            return true;
        }
        if (i == 1 && i2 == 2) {
            return false;
        }
        if (i == 1 && i2 == 3) {
            return true;
        }
        if (i == 2 && i2 == 0) {
            return true;
        }
        if (i == 2 && i2 == 1) {
            return true;
        }
        if (i == 2 && i2 == 3) {
            return false;
        }
        if (i == 3 && i2 == 0) {
            return false;
        }
        if (i == 3 && i2 == 1) {
            return true;
        }
        return i == 3 && i2 == 2;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        setRotateSuggestionButtonState(false);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        this.mPendingRotationSuggestion = false;
    }

    /* access modifiers changed from: package-private */
    public RotationButtonController(Context context, int i, int i2) {
        this.mContext = context;
        this.mLightIconColor = i;
        this.mDarkIconColor = i2;
    }

    /* access modifiers changed from: package-private */
    public void setRotationButton(RotationButton rotationButton, Consumer<Boolean> consumer) {
        this.mRotationButton = rotationButton;
        rotationButton.setRotationButtonController(this);
        this.mRotationButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.navigationbar.RotationButtonController$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                RotationButtonController.this.onRotateSuggestionClick(view);
            }
        });
        this.mRotationButton.setOnHoverListener(new View.OnHoverListener() { // from class: com.android.systemui.navigationbar.RotationButtonController$$ExternalSyntheticLambda1
            @Override // android.view.View.OnHoverListener
            public final boolean onHover(View view, MotionEvent motionEvent) {
                return RotationButtonController.this.onRotateSuggestionHover(view, motionEvent);
            }
        });
        this.mRotationButton.setVisibilityChangedCallback(consumer);
    }

    /* access modifiers changed from: package-private */
    public void registerListeners() {
        if (!this.mListenersRegistered) {
            this.mListenersRegistered = true;
            try {
                WindowManagerGlobal.getWindowManagerService().watchRotation(this.mRotationWatcher, this.mContext.getDisplay().getDisplayId());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (IllegalArgumentException unused) {
                this.mListenersRegistered = false;
                Log.w("StatusBar/RotationButtonController", "RegisterListeners for the display failed");
            }
            TaskStackChangeListeners.getInstance().registerTaskStackListener(this.mTaskStackListener);
        }
    }

    /* access modifiers changed from: package-private */
    public void unregisterListeners() {
        if (this.mListenersRegistered) {
            this.mListenersRegistered = false;
            try {
                WindowManagerGlobal.getWindowManagerService().removeRotationWatcher(this.mRotationWatcher);
                TaskStackChangeListeners.getInstance().unregisterTaskStackListener(this.mTaskStackListener);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void addRotationCallback(Consumer<Integer> consumer) {
        this.mRotWatcherListener = consumer;
    }

    /* access modifiers changed from: package-private */
    public void setRotationLockedAtAngle(int i) {
        this.mRotationLockController.setRotationLockedAtAngle(true, i);
    }

    public boolean isRotationLocked() {
        return this.mRotationLockController.isRotationLocked();
    }

    void setRotateSuggestionButtonState(boolean z) {
        setRotateSuggestionButtonState(z, false);
    }

    void setRotateSuggestionButtonState(boolean z, boolean z2) {
        View currentView;
        KeyButtonDrawable imageDrawable;
        if ((z || this.mRotationButton.isVisible()) && (currentView = this.mRotationButton.getCurrentView()) != null && (imageDrawable = this.mRotationButton.getImageDrawable()) != null) {
            this.mPendingRotationSuggestion = false;
            this.mMainThreadHandler.removeCallbacks(this.mCancelPendingRotationProposal);
            if (z) {
                Animator animator = this.mRotateHideAnimator;
                if (animator != null && animator.isRunning()) {
                    this.mRotateHideAnimator.cancel();
                }
                this.mRotateHideAnimator = null;
                currentView.setAlpha(1.0f);
                if (imageDrawable.canAnimate()) {
                    imageDrawable.resetAnimation();
                    imageDrawable.startAnimation();
                }
                if (!isRotateSuggestionIntroduced()) {
                    this.mViewRippler.start(currentView);
                }
                this.mRotationButton.show();
                return;
            }
            this.mViewRippler.stop();
            if (z2) {
                Animator animator2 = this.mRotateHideAnimator;
                if (animator2 != null && animator2.isRunning()) {
                    this.mRotateHideAnimator.pause();
                }
                this.mRotationButton.hide();
                return;
            }
            Animator animator3 = this.mRotateHideAnimator;
            if (animator3 == null || !animator3.isRunning()) {
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(currentView, "alpha", 0.0f);
                ofFloat.setDuration(100L);
                ofFloat.setInterpolator(Interpolators.LINEAR);
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.navigationbar.RotationButtonController.2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator4) {
                        RotationButtonController.this.mRotationButton.hide();
                    }
                });
                this.mRotateHideAnimator = ofFloat;
                ofFloat.start();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setRecentsAnimationRunning(boolean z) {
        this.mIsRecentsAnimationRunning = z;
        updateRotationButtonStateInOverview();
    }

    /* access modifiers changed from: package-private */
    public void setHomeRotationEnabled(boolean z) {
        this.mHomeRotationEnabled = z;
        updateRotationButtonStateInOverview();
    }

    private void updateRotationButtonStateInOverview() {
        if (this.mIsRecentsAnimationRunning && !this.mHomeRotationEnabled) {
            setRotateSuggestionButtonState(false, true);
        }
    }

    /* access modifiers changed from: package-private */
    public void setDarkIntensity(float f) {
        this.mRotationButton.setDarkIntensity(f);
    }

    /* access modifiers changed from: package-private */
    public void onRotationProposal(int i, int i2, boolean z) {
        int i3;
        int i4;
        if (!this.mRotationButton.acceptRotationProposal()) {
            return;
        }
        if (!this.mHomeRotationEnabled && this.mIsRecentsAnimationRunning) {
            return;
        }
        if (!z) {
            setRotateSuggestionButtonState(false);
        } else if (i == i2) {
            this.mMainThreadHandler.removeCallbacks(this.mRemoveRotationProposal);
            setRotateSuggestionButtonState(false);
        } else {
            this.mLastRotationSuggestion = i;
            boolean isRotationAnimationCCW = isRotationAnimationCCW(i2, i);
            if (i2 == 0 || i2 == 2) {
                if (isRotationAnimationCCW) {
                    i3 = R$drawable.ic_sysbar_rotate_button_ccw_start_90;
                } else {
                    i3 = R$drawable.ic_sysbar_rotate_button_cw_start_90;
                }
                this.mIconResId = i3;
            } else {
                if (isRotationAnimationCCW) {
                    i4 = R$drawable.ic_sysbar_rotate_button_ccw_start_0;
                } else {
                    i4 = R$drawable.ic_sysbar_rotate_button_ccw_start_0;
                }
                this.mIconResId = i4;
            }
            this.mRotationButton.updateIcon(this.mLightIconColor, this.mDarkIconColor);
            if (canShowRotationButton()) {
                showAndLogRotationSuggestion();
                return;
            }
            this.mPendingRotationSuggestion = true;
            this.mMainThreadHandler.removeCallbacks(this.mCancelPendingRotationProposal);
            this.mMainThreadHandler.postDelayed(this.mCancelPendingRotationProposal, 20000);
        }
    }

    /* access modifiers changed from: package-private */
    public void onDisable2FlagChanged(int i) {
        if (hasDisable2RotateSuggestionFlag(i)) {
            onRotationSuggestionsDisabled();
        }
    }

    /* access modifiers changed from: package-private */
    public void onNavigationBarWindowVisibilityChange(boolean z) {
        if (this.mIsNavigationBarShowing != z) {
            this.mIsNavigationBarShowing = z;
            showPendingRotationButtonIfNeeded();
        }
    }

    /* access modifiers changed from: package-private */
    public void onBehaviorChanged(int i) {
        if (this.mBehavior != i) {
            this.mBehavior = i;
            showPendingRotationButtonIfNeeded();
        }
    }

    private void showPendingRotationButtonIfNeeded() {
        if (canShowRotationButton() && this.mPendingRotationSuggestion) {
            showAndLogRotationSuggestion();
        }
    }

    private boolean canShowRotationButton() {
        return this.mIsNavigationBarShowing || this.mBehavior == 1;
    }

    public Context getContext() {
        return this.mContext;
    }

    /* access modifiers changed from: package-private */
    public RotationButton getRotationButton() {
        return this.mRotationButton;
    }

    public int getIconResId() {
        return this.mIconResId;
    }

    public int getLightIconColor() {
        return this.mLightIconColor;
    }

    public int getDarkIconColor() {
        return this.mDarkIconColor;
    }

    /* access modifiers changed from: private */
    public void onRotateSuggestionClick(View view) {
        this.mUiEventLogger.log(RotationButtonEvent.ROTATION_SUGGESTION_ACCEPTED);
        incrementNumAcceptedRotationSuggestionsIfNeeded();
        setRotationLockedAtAngle(this.mLastRotationSuggestion);
    }

    /* access modifiers changed from: private */
    public boolean onRotateSuggestionHover(View view, MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        this.mHoveringRotationSuggestion = actionMasked == 9 || actionMasked == 7;
        rescheduleRotationTimeout(true);
        return false;
    }

    private void onRotationSuggestionsDisabled() {
        setRotateSuggestionButtonState(false, true);
        this.mMainThreadHandler.removeCallbacks(this.mRemoveRotationProposal);
    }

    private void showAndLogRotationSuggestion() {
        setRotateSuggestionButtonState(true);
        rescheduleRotationTimeout(false);
        this.mUiEventLogger.log(RotationButtonEvent.ROTATION_SUGGESTION_SHOWN);
    }

    /* access modifiers changed from: package-private */
    public void setSkipOverrideUserLockPrefsOnce() {
        this.mSkipOverrideUserLockPrefsOnce = true;
    }

    /* access modifiers changed from: private */
    public boolean shouldOverrideUserLockPrefs(int i) {
        if (this.mSkipOverrideUserLockPrefsOnce) {
            this.mSkipOverrideUserLockPrefsOnce = false;
            return false;
        } else if (i == 0) {
            return true;
        } else {
            return false;
        }
    }

    private void rescheduleRotationTimeout(boolean z) {
        Animator animator;
        if (!z || (((animator = this.mRotateHideAnimator) == null || !animator.isRunning()) && this.mRotationButton.isVisible())) {
            this.mMainThreadHandler.removeCallbacks(this.mRemoveRotationProposal);
            this.mMainThreadHandler.postDelayed(this.mRemoveRotationProposal, (long) computeRotationProposalTimeout());
        }
    }

    private int computeRotationProposalTimeout() {
        return this.mAccessibilityManagerWrapper.getRecommendedTimeoutMillis(this.mHoveringRotationSuggestion ? 16000 : 5000, 4);
    }

    private boolean isRotateSuggestionIntroduced() {
        if (Settings.Secure.getInt(this.mContext.getContentResolver(), "num_rotation_suggestions_accepted", 0) >= 3) {
            return true;
        }
        return false;
    }

    private void incrementNumAcceptedRotationSuggestionsIfNeeded() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        int i = Settings.Secure.getInt(contentResolver, "num_rotation_suggestions_accepted", 0);
        if (i < 3) {
            Settings.Secure.putInt(contentResolver, "num_rotation_suggestions_accepted", i + 1);
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class TaskStackListenerImpl extends TaskStackChangeListener {
        private TaskStackListenerImpl() {
        }

        @Override // com.android.systemui.shared.system.TaskStackChangeListener
        public void onTaskStackChanged() {
            RotationButtonController.this.setRotateSuggestionButtonState(false);
        }

        @Override // com.android.systemui.shared.system.TaskStackChangeListener
        public void onTaskRemoved(int i) {
            RotationButtonController.this.setRotateSuggestionButtonState(false);
        }

        @Override // com.android.systemui.shared.system.TaskStackChangeListener
        public void onTaskMovedToFront(int i) {
            RotationButtonController.this.setRotateSuggestionButtonState(false);
        }

        @Override // com.android.systemui.shared.system.TaskStackChangeListener
        public void onActivityRequestedOrientationChanged(int i, int i2) {
            Optional.ofNullable(ActivityManagerWrapper.getInstance()).map(RotationButtonController$TaskStackListenerImpl$$ExternalSyntheticLambda1.INSTANCE).ifPresent(new RotationButtonController$TaskStackListenerImpl$$ExternalSyntheticLambda0(this, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onActivityRequestedOrientationChanged$0(int i, ActivityManager.RunningTaskInfo runningTaskInfo) {
            if (runningTaskInfo.id == i) {
                RotationButtonController.this.setRotateSuggestionButtonState(false);
            }
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ViewRippler {
        private final Runnable mRipple;
        private View mRoot;

        private ViewRippler() {
            this.mRipple = new Runnable() { // from class: com.android.systemui.navigationbar.RotationButtonController.ViewRippler.1
                @Override // java.lang.Runnable
                public void run() {
                    if (ViewRippler.this.mRoot.isAttachedToWindow()) {
                        ViewRippler.this.mRoot.setPressed(true);
                        ViewRippler.this.mRoot.setPressed(false);
                    }
                }
            };
        }

        public void start(View view) {
            stop();
            this.mRoot = view;
            view.postOnAnimationDelayed(this.mRipple, 50);
            this.mRoot.postOnAnimationDelayed(this.mRipple, 2000);
            this.mRoot.postOnAnimationDelayed(this.mRipple, 4000);
            this.mRoot.postOnAnimationDelayed(this.mRipple, 6000);
            this.mRoot.postOnAnimationDelayed(this.mRipple, 8000);
        }

        public void stop() {
            View view = this.mRoot;
            if (view != null) {
                view.removeCallbacks(this.mRipple);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum RotationButtonEvent implements UiEventLogger.UiEventEnum {
        ROTATION_SUGGESTION_SHOWN(206),
        ROTATION_SUGGESTION_ACCEPTED(207);
        
        private final int mId;

        RotationButtonEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }
}
