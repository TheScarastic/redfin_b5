package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.SystemClock;
import android.util.Log;
import android.util.MathUtils;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.util.LatencyTracker;
import com.android.systemui.DejankUtils;
import com.android.systemui.R$bool;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.doze.DozeLog;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import com.android.systemui.statusbar.phone.PanelView;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.wm.shell.animation.FlingAnimationUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
/* loaded from: classes.dex */
public abstract class PanelViewController {
    public static final String TAG = PanelView.class.getSimpleName();
    protected final AmbientState mAmbientState;
    private boolean mAnimateAfterExpanding;
    private boolean mAnimatingOnDown;
    PanelBar mBar;
    private boolean mClosing;
    private boolean mCollapsedAndHeadsUpOnDown;
    protected long mDownTime;
    private final DozeLog mDozeLog;
    private boolean mExpandLatencyTracking;
    protected boolean mExpanding;
    private final FalsingManager mFalsingManager;
    private FlingAnimationUtils mFlingAnimationUtils;
    private FlingAnimationUtils mFlingAnimationUtilsClosing;
    private FlingAnimationUtils mFlingAnimationUtilsDismissing;
    private boolean mGestureWaitForTouchSlop;
    private boolean mHandlingPointerUp;
    private boolean mHasLayoutedSinceDown;
    protected HeadsUpManagerPhone mHeadsUpManager;
    private ValueAnimator mHeightAnimator;
    protected boolean mHintAnimationRunning;
    private float mHintDistance;
    private boolean mIgnoreXTouchSlop;
    private float mInitialOffsetOnTouch;
    private float mInitialTouchX;
    private float mInitialTouchY;
    private boolean mInstantExpanding;
    private boolean mIsFlinging;
    protected boolean mIsLaunchAnimationRunning;
    private boolean mIsSpringBackAnimation;
    protected KeyguardBottomAreaView mKeyguardBottomArea;
    protected final KeyguardStateController mKeyguardStateController;
    private final LatencyTracker mLatencyTracker;
    private float mMinExpandHeight;
    private boolean mMotionAborted;
    private boolean mNotificationsDragEnabled;
    protected float mOverExpansion;
    private boolean mPanelClosedOnDown;
    private float mPanelFlingOvershootAmount;
    private boolean mPanelUpdateWhenAnimatorEnds;
    protected final Resources mResources;
    private float mSlopMultiplier;
    protected StatusBar mStatusBar;
    private final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    protected final SysuiStatusBarStateController mStatusBarStateController;
    protected final StatusBarTouchableRegionManager mStatusBarTouchableRegionManager;
    private boolean mTouchAboveFalsingThreshold;
    private boolean mTouchDisabled;
    private int mTouchSlop;
    private boolean mTouchSlopExceeded;
    protected boolean mTouchSlopExceededBeforeDown;
    private boolean mTouchStartedInEmptyArea;
    protected boolean mTracking;
    private int mTrackingPointer;
    private int mUnlockFalsingThreshold;
    private boolean mUpdateFlingOnLayout;
    private float mUpdateFlingVelocity;
    private boolean mUpwardsWhenThresholdReached;
    private boolean mVibrateOnOpening;
    private final VibratorHelper mVibratorHelper;
    private final PanelView mView;
    private String mViewName;
    private LockscreenGestureLogger mLockscreenGestureLogger = new LockscreenGestureLogger();
    private int mFixedDuration = -1;
    protected ArrayList<PanelExpansionListener> mExpansionListeners = new ArrayList<>();
    private float mLastGesturedOverExpansion = -1.0f;
    private float mExpandedFraction = 0.0f;
    protected float mExpandedHeight = 0.0f;
    private final VelocityTracker mVelocityTracker = VelocityTracker.obtain();
    private float mNextCollapseSpeedUpFactor = 1.0f;
    private final Runnable mFlingCollapseRunnable = new Runnable() { // from class: com.android.systemui.statusbar.phone.PanelViewController.4
        @Override // java.lang.Runnable
        public void run() {
            PanelViewController panelViewController = PanelViewController.this;
            panelViewController.fling(0.0f, false, panelViewController.mNextCollapseSpeedUpFactor, false);
        }
    };
    protected final Runnable mPostCollapseRunnable = new Runnable() { // from class: com.android.systemui.statusbar.phone.PanelViewController.8
        @Override // java.lang.Runnable
        public void run() {
            PanelViewController.this.collapse(false, 1.0f);
        }
    };
    private Interpolator mBounceInterpolator = new BounceInterpolator();

    protected abstract boolean canCollapsePanelOnTouch();

    public abstract OnLayoutChangeListener createLayoutChangeListener();

    protected abstract OnConfigurationChangedListener createOnConfigurationChangedListener();

    protected abstract TouchHandler createTouchHandler();

    protected abstract int getMaxPanelHeight();

    protected abstract boolean isInContentBounds(float f, float f2);

    protected abstract boolean isPanelVisibleBecauseOfHeadsUp();

    protected abstract boolean isTrackingBlocked();

    /* access modifiers changed from: protected */
    public void onExpandingStarted() {
    }

    protected abstract void onHeightUpdated(float f);

    protected abstract boolean onMiddleClicked();

    public abstract void resetViews(boolean z);

    public abstract void setIsShadeOpening(boolean z);

    protected abstract boolean shouldGestureIgnoreXTouchSlop(float f, float f2);

    protected abstract boolean shouldGestureWaitForTouchSlop();

    protected abstract boolean shouldUseDismissingAnimation();

    /* access modifiers changed from: protected */
    public void onExpandingFinished() {
        this.mBar.onExpandingFinished();
    }

    /* access modifiers changed from: protected */
    public void notifyExpandingStarted() {
        if (!this.mExpanding) {
            this.mExpanding = true;
            onExpandingStarted();
        }
    }

    /* access modifiers changed from: protected */
    public final void notifyExpandingFinished() {
        endClosing();
        if (this.mExpanding) {
            this.mExpanding = false;
            onExpandingFinished();
        }
    }

    public PanelViewController(PanelView panelView, FalsingManager falsingManager, DozeLog dozeLog, KeyguardStateController keyguardStateController, SysuiStatusBarStateController sysuiStatusBarStateController, VibratorHelper vibratorHelper, StatusBarKeyguardViewManager statusBarKeyguardViewManager, LatencyTracker latencyTracker, FlingAnimationUtils.Builder builder, StatusBarTouchableRegionManager statusBarTouchableRegionManager, AmbientState ambientState) {
        this.mAmbientState = ambientState;
        this.mView = panelView;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        panelView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.android.systemui.statusbar.phone.PanelViewController.1
            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View view) {
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View view) {
                PanelViewController panelViewController = PanelViewController.this;
                panelViewController.mViewName = panelViewController.mResources.getResourceName(panelViewController.mView.getId());
            }
        });
        panelView.addOnLayoutChangeListener(createLayoutChangeListener());
        panelView.setOnTouchListener(createTouchHandler());
        panelView.setOnConfigurationChangedListener(createOnConfigurationChangedListener());
        Resources resources = panelView.getResources();
        this.mResources = resources;
        this.mKeyguardStateController = keyguardStateController;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mFlingAnimationUtils = builder.reset().setMaxLengthSeconds(0.6f).setSpeedUpFactor(0.6f).build();
        this.mFlingAnimationUtilsClosing = builder.reset().setMaxLengthSeconds(0.5f).setSpeedUpFactor(0.6f).build();
        this.mFlingAnimationUtilsDismissing = builder.reset().setMaxLengthSeconds(0.5f).setSpeedUpFactor(0.6f).setX2(0.6f).setY2(0.84f).build();
        this.mLatencyTracker = latencyTracker;
        this.mFalsingManager = falsingManager;
        this.mDozeLog = dozeLog;
        this.mNotificationsDragEnabled = resources.getBoolean(R$bool.config_enableNotificationShadeDrag);
        this.mVibratorHelper = vibratorHelper;
        this.mVibrateOnOpening = resources.getBoolean(R$bool.config_vibrateOnIconAnimation);
        this.mStatusBarTouchableRegionManager = statusBarTouchableRegionManager;
    }

    /* access modifiers changed from: protected */
    public void loadDimens() {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(this.mView.getContext());
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mSlopMultiplier = viewConfiguration.getScaledAmbiguousGestureMultiplier();
        this.mHintDistance = this.mResources.getDimension(R$dimen.hint_move_distance);
        this.mPanelFlingOvershootAmount = this.mResources.getDimension(R$dimen.panel_overshoot_amount);
        this.mUnlockFalsingThreshold = this.mResources.getDimensionPixelSize(R$dimen.unlock_falsing_threshold);
    }

    /* access modifiers changed from: protected */
    public float getTouchSlop(MotionEvent motionEvent) {
        if (motionEvent.getClassification() == 1) {
            return ((float) this.mTouchSlop) * this.mSlopMultiplier;
        }
        return (float) this.mTouchSlop;
    }

    /* access modifiers changed from: private */
    public void addMovement(MotionEvent motionEvent) {
        float rawX = motionEvent.getRawX() - motionEvent.getX();
        float rawY = motionEvent.getRawY() - motionEvent.getY();
        motionEvent.offsetLocation(rawX, rawY);
        this.mVelocityTracker.addMovement(motionEvent);
        motionEvent.offsetLocation(-rawX, -rawY);
    }

    public void setTouchAndAnimationDisabled(boolean z) {
        this.mTouchDisabled = z;
        if (z) {
            cancelHeightAnimator();
            if (this.mTracking) {
                onTrackingStopped(true);
            }
            notifyExpandingFinished();
        }
    }

    public void startExpandLatencyTracking() {
        if (this.mLatencyTracker.isEnabled()) {
            this.mLatencyTracker.onActionStart(0);
            this.mExpandLatencyTracking = true;
        }
    }

    /* access modifiers changed from: private */
    public void startOpening(MotionEvent motionEvent) {
        notifyBarPanelExpansionChanged();
        maybeVibrateOnOpening();
        float displayWidth = this.mStatusBar.getDisplayWidth();
        float displayHeight = this.mStatusBar.getDisplayHeight();
        this.mLockscreenGestureLogger.writeAtFractionalPosition(1328, (int) ((motionEvent.getX() / displayWidth) * 100.0f), (int) ((motionEvent.getY() / displayHeight) * 100.0f), this.mStatusBar.getRotation());
        this.mLockscreenGestureLogger.log(LockscreenGestureLogger.LockscreenUiEvent.LOCKSCREEN_UNLOCKED_NOTIFICATION_PANEL_EXPAND);
    }

    /* access modifiers changed from: protected */
    public void maybeVibrateOnOpening() {
        if (this.mVibrateOnOpening) {
            this.mVibratorHelper.vibrate(2);
        }
    }

    /* access modifiers changed from: private */
    public boolean isDirectionUpwards(float f, float f2) {
        float f3 = f - this.mInitialTouchX;
        float f4 = f2 - this.mInitialTouchY;
        if (f4 < 0.0f && Math.abs(f4) >= Math.abs(f3)) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void startExpandMotion(float f, float f2, boolean z, float f3) {
        if (!this.mHandlingPointerUp) {
            beginJankMonitoring(0);
        }
        this.mInitialOffsetOnTouch = f3;
        this.mInitialTouchY = f2;
        this.mInitialTouchX = f;
        if (z) {
            this.mTouchSlopExceeded = true;
            setExpandedHeight(f3);
            onTrackingStarted();
        }
    }

    /* access modifiers changed from: private */
    public void endMotionEvent(MotionEvent motionEvent, float f, float f2, boolean z) {
        boolean z2;
        int i;
        this.mTrackingPointer = -1;
        if ((this.mTracking && this.mTouchSlopExceeded) || Math.abs(f - this.mInitialTouchX) > ((float) this.mTouchSlop) || Math.abs(f2 - this.mInitialTouchY) > ((float) this.mTouchSlop) || motionEvent.getActionMasked() == 3 || z) {
            this.mVelocityTracker.computeCurrentVelocity(1000);
            float yVelocity = this.mVelocityTracker.getYVelocity();
            float hypot = (float) Math.hypot((double) this.mVelocityTracker.getXVelocity(), (double) this.mVelocityTracker.getYVelocity());
            boolean z3 = false;
            boolean z4 = this.mStatusBarStateController.getState() == 1;
            if (motionEvent.getActionMasked() == 3 || z) {
                z2 = z4 ? true : !this.mPanelClosedOnDown;
            } else {
                z2 = flingExpands(yVelocity, hypot, f, f2);
            }
            this.mDozeLog.traceFling(z2, this.mTouchAboveFalsingThreshold, this.mStatusBar.isFalsingThresholdNeeded(), this.mStatusBar.isWakeUpComingFromTouch());
            if (!z2 && z4) {
                float displayDensity = this.mStatusBar.getDisplayDensity();
                this.mLockscreenGestureLogger.write(186, (int) Math.abs((f2 - this.mInitialTouchY) / displayDensity), (int) Math.abs(yVelocity / displayDensity));
                this.mLockscreenGestureLogger.log(LockscreenGestureLogger.LockscreenUiEvent.LOCKSCREEN_UNLOCK);
            }
            int i2 = (yVelocity > 0.0f ? 1 : (yVelocity == 0.0f ? 0 : -1));
            if (i2 == 0) {
                i = 7;
            } else {
                i = i2 > 0 ? 0 : this.mKeyguardStateController.canDismissLockScreen() ? 4 : 8;
            }
            fling(yVelocity, z2, isFalseTouch(f, f2, i));
            onTrackingStopped(z2);
            if (z2 && this.mPanelClosedOnDown && !this.mHasLayoutedSinceDown) {
                z3 = true;
            }
            this.mUpdateFlingOnLayout = z3;
            if (z3) {
                this.mUpdateFlingVelocity = yVelocity;
            }
        } else if (!this.mStatusBar.isBouncerShowing() && !this.mStatusBarKeyguardViewManager.isShowingAlternateAuthOrAnimating()) {
            onTrackingStopped(onEmptySpaceClick(this.mInitialTouchX));
        }
        this.mVelocityTracker.clear();
    }

    /* access modifiers changed from: protected */
    public float getCurrentExpandVelocity() {
        this.mVelocityTracker.computeCurrentVelocity(1000);
        return this.mVelocityTracker.getYVelocity();
    }

    /* access modifiers changed from: private */
    public int getFalsingThreshold() {
        return (int) (((float) this.mUnlockFalsingThreshold) * (this.mStatusBar.isWakeUpComingFromTouch() ? 1.5f : 1.0f));
    }

    /* access modifiers changed from: protected */
    public void onTrackingStopped(boolean z) {
        this.mTracking = false;
        this.mBar.onTrackingStopped(z);
        notifyBarPanelExpansionChanged();
    }

    /* access modifiers changed from: protected */
    public void onTrackingStarted() {
        endClosing();
        this.mTracking = true;
        this.mBar.onTrackingStarted();
        notifyExpandingStarted();
        notifyBarPanelExpansionChanged();
    }

    /* access modifiers changed from: protected */
    public void cancelHeightAnimator() {
        ValueAnimator valueAnimator = this.mHeightAnimator;
        if (valueAnimator != null) {
            if (valueAnimator.isRunning()) {
                this.mPanelUpdateWhenAnimatorEnds = false;
            }
            this.mHeightAnimator.cancel();
        }
        endClosing();
    }

    private void endClosing() {
        if (this.mClosing) {
            this.mClosing = false;
            onClosingFinished();
        }
    }

    /* access modifiers changed from: protected */
    public boolean flingExpands(float f, float f2, float f3, float f4) {
        int i;
        if (this.mFalsingManager.isUnlockingDisabled()) {
            return true;
        }
        int i2 = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
        if (i2 > 0) {
            i = 0;
        } else {
            i = this.mKeyguardStateController.canDismissLockScreen() ? 4 : 8;
        }
        if (isFalseTouch(f3, f4, i)) {
            return true;
        }
        if (Math.abs(f2) < this.mFlingAnimationUtils.getMinVelocityPxPerSecond()) {
            return shouldExpandWhenNotFlinging();
        }
        return i2 > 0;
    }

    /* access modifiers changed from: protected */
    public boolean shouldExpandWhenNotFlinging() {
        return getExpandedFraction() > 0.5f;
    }

    private boolean isFalseTouch(float f, float f2, int i) {
        if (!this.mStatusBar.isFalsingThresholdNeeded()) {
            return false;
        }
        if (this.mFalsingManager.isClassifierEnabled()) {
            return this.mFalsingManager.isFalseTouch(i);
        }
        if (!this.mTouchAboveFalsingThreshold) {
            return true;
        }
        if (this.mUpwardsWhenThresholdReached) {
            return false;
        }
        return !isDirectionUpwards(f, f2);
    }

    /* access modifiers changed from: protected */
    public void fling(float f, boolean z) {
        fling(f, z, 1.0f, false);
    }

    protected void fling(float f, boolean z, boolean z2) {
        fling(f, z, 1.0f, z2);
    }

    protected void fling(float f, boolean z, float f2, boolean z2) {
        float maxPanelHeight = z ? (float) getMaxPanelHeight() : 0.0f;
        if (!z) {
            this.mClosing = true;
        }
        flingToHeight(f, z, maxPanelHeight, f2, z2);
    }

    /* access modifiers changed from: protected */
    public void flingToHeight(float f, boolean z, float f2, float f3, boolean z2) {
        final boolean z3 = false;
        if (f2 == this.mExpandedHeight && this.mOverExpansion == 0.0f) {
            endJankMonitoring(0);
            this.mKeyguardStateController.notifyPanelFlingEnd();
            notifyExpandingFinished();
            return;
        }
        this.mIsFlinging = true;
        boolean z4 = z && this.mStatusBarStateController.getState() != 1 && this.mOverExpansion == 0.0f && f >= 0.0f;
        if (z4 || (this.mOverExpansion != 0.0f && z)) {
            z3 = true;
        }
        float lerp = z4 ? MathUtils.lerp(0.2f, 1.0f, MathUtils.saturate(f / (this.mFlingAnimationUtils.getHighVelocityPxPerSecond() * 0.5f))) + (this.mOverExpansion / this.mPanelFlingOvershootAmount) : 0.0f;
        ValueAnimator createHeightAnimator = createHeightAnimator(f2, lerp);
        if (z) {
            if (z2 && f < 0.0f) {
                f = 0.0f;
            }
            this.mFlingAnimationUtils.apply(createHeightAnimator, this.mExpandedHeight, f2 + (lerp * this.mPanelFlingOvershootAmount), f, (float) this.mView.getHeight());
            if (f == 0.0f) {
                createHeightAnimator.setDuration(350L);
            }
        } else {
            if (!shouldUseDismissingAnimation()) {
                this.mFlingAnimationUtilsClosing.apply(createHeightAnimator, this.mExpandedHeight, f2, f, (float) this.mView.getHeight());
            } else if (f == 0.0f) {
                createHeightAnimator.setInterpolator(Interpolators.PANEL_CLOSE_ACCELERATED);
                createHeightAnimator.setDuration((long) (((this.mExpandedHeight / ((float) this.mView.getHeight())) * 100.0f) + 200.0f));
            } else {
                this.mFlingAnimationUtilsDismissing.apply(createHeightAnimator, this.mExpandedHeight, f2, f, (float) this.mView.getHeight());
            }
            if (f == 0.0f) {
                createHeightAnimator.setDuration((long) (((float) createHeightAnimator.getDuration()) / f3));
            }
            int i = this.mFixedDuration;
            if (i != -1) {
                createHeightAnimator.setDuration((long) i);
            }
        }
        createHeightAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.PanelViewController.2
            private boolean mCancelled;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                PanelViewController.this.beginJankMonitoring(0);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                this.mCancelled = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (!z3 || this.mCancelled) {
                    PanelViewController.this.onFlingEnd(this.mCancelled);
                } else {
                    PanelViewController.this.springBack();
                }
            }
        });
        setAnimator(createHeightAnimator);
        createHeightAnimator.start();
    }

    /* access modifiers changed from: private */
    public void springBack() {
        float f = this.mOverExpansion;
        if (f == 0.0f) {
            onFlingEnd(false);
            return;
        }
        this.mIsSpringBackAnimation = true;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(f, 0.0f);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.phone.PanelViewController$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                PanelViewController.this.lambda$springBack$0(valueAnimator);
            }
        });
        ofFloat.setDuration(400L);
        ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.PanelViewController.3
            private boolean mCancelled;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                this.mCancelled = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                PanelViewController.this.mIsSpringBackAnimation = false;
                PanelViewController.this.onFlingEnd(this.mCancelled);
            }
        });
        setAnimator(ofFloat);
        ofFloat.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$springBack$0(ValueAnimator valueAnimator) {
        setOverExpansionInternal(((Float) valueAnimator.getAnimatedValue()).floatValue(), false);
    }

    /* access modifiers changed from: private */
    public void onFlingEnd(boolean z) {
        this.mIsFlinging = false;
        setOverExpansionInternal(0.0f, false);
        setAnimator(null);
        this.mKeyguardStateController.notifyPanelFlingEnd();
        if (!z) {
            endJankMonitoring(0);
            notifyExpandingFinished();
        } else {
            cancelJankMonitoring(0);
        }
        notifyBarPanelExpansionChanged();
    }

    public void setExpandedHeight(float f) {
        setExpandedHeightInternal(f);
    }

    /* access modifiers changed from: protected */
    public void requestPanelHeightUpdate() {
        float maxPanelHeight = (float) getMaxPanelHeight();
        if (isFullyCollapsed() || maxPanelHeight == this.mExpandedHeight) {
            return;
        }
        if (this.mTracking && !isTrackingBlocked()) {
            return;
        }
        if (this.mHeightAnimator == null || this.mIsSpringBackAnimation) {
            setExpandedHeight(maxPanelHeight);
        } else {
            this.mPanelUpdateWhenAnimatorEnds = true;
        }
    }

    public void setExpandedHeightInternal(float f) {
        if (Float.isNaN(f)) {
            Log.wtf(TAG, "ExpandedHeight set to NaN");
        }
        float f2 = 0.0f;
        if (this.mExpandLatencyTracking && f != 0.0f) {
            DejankUtils.postAfterTraversal(new Runnable() { // from class: com.android.systemui.statusbar.phone.PanelViewController$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    PanelViewController.this.lambda$setExpandedHeightInternal$1();
                }
            });
            this.mExpandLatencyTracking = false;
        }
        float maxPanelHeight = (float) getMaxPanelHeight();
        if (this.mHeightAnimator == null) {
            if (this.mTracking) {
                setOverExpansionInternal(Math.max(0.0f, f - maxPanelHeight), true);
            }
            this.mExpandedHeight = Math.min(f, maxPanelHeight);
        } else {
            this.mExpandedHeight = f;
        }
        float f3 = this.mExpandedHeight;
        if (f3 < 1.0f && f3 != 0.0f && this.mClosing) {
            this.mExpandedHeight = 0.0f;
            ValueAnimator valueAnimator = this.mHeightAnimator;
            if (valueAnimator != null) {
                valueAnimator.end();
            }
        }
        if (maxPanelHeight != 0.0f) {
            f2 = this.mExpandedHeight / maxPanelHeight;
        }
        this.mExpandedFraction = Math.min(1.0f, f2);
        onHeightUpdated(this.mExpandedHeight);
        notifyBarPanelExpansionChanged();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setExpandedHeightInternal$1() {
        this.mLatencyTracker.onActionEnd(0);
    }

    /* access modifiers changed from: protected */
    public void setOverExpansion(float f) {
        this.mOverExpansion = f;
    }

    private void setOverExpansionInternal(float f, boolean z) {
        if (!z) {
            this.mLastGesturedOverExpansion = -1.0f;
            setOverExpansion(f);
        } else if (this.mLastGesturedOverExpansion != f) {
            this.mLastGesturedOverExpansion = f;
            setOverExpansion(Interpolators.getOvershootInterpolation(MathUtils.saturate(f / (((float) this.mView.getHeight()) / 3.0f))) * this.mPanelFlingOvershootAmount * 2.0f);
        }
    }

    public void setExpandedFraction(float f) {
        setExpandedHeight(((float) getMaxPanelHeight()) * f);
    }

    public float getExpandedHeight() {
        return this.mExpandedHeight;
    }

    public float getExpandedFraction() {
        return this.mExpandedFraction;
    }

    public boolean isFullyExpanded() {
        return this.mExpandedHeight >= ((float) getMaxPanelHeight());
    }

    public boolean isFullyCollapsed() {
        return this.mExpandedFraction <= 0.0f;
    }

    public boolean isCollapsing() {
        return this.mClosing || this.mIsLaunchAnimationRunning;
    }

    public boolean isTracking() {
        return this.mTracking;
    }

    public void setBar(PanelBar panelBar) {
        this.mBar = panelBar;
    }

    public void collapse(boolean z, float f) {
        if (canPanelBeCollapsed()) {
            cancelHeightAnimator();
            notifyExpandingStarted();
            this.mClosing = true;
            if (z) {
                this.mNextCollapseSpeedUpFactor = f;
                this.mView.postDelayed(this.mFlingCollapseRunnable, 120);
                return;
            }
            fling(0.0f, false, f, false);
        }
    }

    public boolean canPanelBeCollapsed() {
        return !isFullyCollapsed() && !this.mTracking && !this.mClosing;
    }

    public void expand(boolean z) {
        if (isFullyCollapsed() || isCollapsing()) {
            this.mInstantExpanding = true;
            this.mAnimateAfterExpanding = z;
            this.mUpdateFlingOnLayout = false;
            abortAnimations();
            if (this.mTracking) {
                onTrackingStopped(true);
            }
            if (this.mExpanding) {
                notifyExpandingFinished();
            }
            notifyBarPanelExpansionChanged();
            this.mView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.android.systemui.statusbar.phone.PanelViewController.5
                @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                public void onGlobalLayout() {
                    if (!PanelViewController.this.mInstantExpanding) {
                        PanelViewController.this.mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else if (PanelViewController.this.mStatusBar.getNotificationShadeWindowView().isVisibleToUser()) {
                        PanelViewController.this.mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        if (PanelViewController.this.mAnimateAfterExpanding) {
                            PanelViewController.this.notifyExpandingStarted();
                            PanelViewController.this.beginJankMonitoring(0);
                            PanelViewController.this.fling(0.0f, true);
                        } else {
                            PanelViewController.this.setExpandedFraction(1.0f);
                        }
                        PanelViewController.this.mInstantExpanding = false;
                    }
                }
            });
            this.mView.requestLayout();
        }
    }

    public void instantCollapse() {
        abortAnimations();
        setExpandedFraction(0.0f);
        if (this.mExpanding) {
            notifyExpandingFinished();
        }
        if (this.mInstantExpanding) {
            this.mInstantExpanding = false;
            notifyBarPanelExpansionChanged();
        }
    }

    /* access modifiers changed from: private */
    public void abortAnimations() {
        cancelHeightAnimator();
        this.mView.removeCallbacks(this.mPostCollapseRunnable);
        this.mView.removeCallbacks(this.mFlingCollapseRunnable);
    }

    /* access modifiers changed from: protected */
    public void onClosingFinished() {
        this.mBar.onClosingFinished();
    }

    /* access modifiers changed from: protected */
    public void startUnlockHintAnimation() {
        if (this.mHeightAnimator == null && !this.mTracking) {
            notifyExpandingStarted();
            startUnlockHintAnimationPhase1(new Runnable() { // from class: com.android.systemui.statusbar.phone.PanelViewController$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    PanelViewController.this.lambda$startUnlockHintAnimation$2();
                }
            });
            onUnlockHintStarted();
            this.mHintAnimationRunning = true;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startUnlockHintAnimation$2() {
        notifyExpandingFinished();
        onUnlockHintFinished();
        this.mHintAnimationRunning = false;
    }

    /* access modifiers changed from: protected */
    public void onUnlockHintFinished() {
        this.mStatusBar.onHintFinished();
    }

    /* access modifiers changed from: protected */
    public void onUnlockHintStarted() {
        this.mStatusBar.onUnlockHintStarted();
    }

    public boolean isUnlockHintRunning() {
        return this.mHintAnimationRunning;
    }

    private void startUnlockHintAnimationPhase1(final Runnable runnable) {
        ValueAnimator createHeightAnimator = createHeightAnimator(Math.max(0.0f, ((float) getMaxPanelHeight()) - this.mHintDistance));
        createHeightAnimator.setDuration(250L);
        createHeightAnimator.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        createHeightAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.PanelViewController.6
            private boolean mCancelled;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                this.mCancelled = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (this.mCancelled) {
                    PanelViewController.this.setAnimator(null);
                    runnable.run();
                    return;
                }
                PanelViewController.this.startUnlockHintAnimationPhase2(runnable);
            }
        });
        createHeightAnimator.start();
        setAnimator(createHeightAnimator);
        View[] viewArr = {this.mKeyguardBottomArea.getIndicationArea(), this.mStatusBar.getAmbientIndicationContainer()};
        for (int i = 0; i < 2; i++) {
            View view = viewArr[i];
            if (view != null) {
                view.animate().translationY(-this.mHintDistance).setDuration(250).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).withEndAction(new Runnable(view) { // from class: com.android.systemui.statusbar.phone.PanelViewController$$ExternalSyntheticLambda4
                    public final /* synthetic */ View f$1;

                    {
                        this.f$1 = r2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        PanelViewController.this.lambda$startUnlockHintAnimationPhase1$3(this.f$1);
                    }
                }).start();
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startUnlockHintAnimationPhase1$3(View view) {
        view.animate().translationY(0.0f).setDuration(450).setInterpolator(this.mBounceInterpolator).start();
    }

    /* access modifiers changed from: private */
    public void setAnimator(ValueAnimator valueAnimator) {
        this.mHeightAnimator = valueAnimator;
        if (valueAnimator == null && this.mPanelUpdateWhenAnimatorEnds) {
            this.mPanelUpdateWhenAnimatorEnds = false;
            requestPanelHeightUpdate();
        }
    }

    /* access modifiers changed from: private */
    public void startUnlockHintAnimationPhase2(final Runnable runnable) {
        ValueAnimator createHeightAnimator = createHeightAnimator((float) getMaxPanelHeight());
        createHeightAnimator.setDuration(450L);
        createHeightAnimator.setInterpolator(this.mBounceInterpolator);
        createHeightAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.PanelViewController.7
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                PanelViewController.this.setAnimator(null);
                runnable.run();
                PanelViewController.this.notifyBarPanelExpansionChanged();
            }
        });
        createHeightAnimator.start();
        setAnimator(createHeightAnimator);
    }

    private ValueAnimator createHeightAnimator(float f) {
        return createHeightAnimator(f, 0.0f);
    }

    private ValueAnimator createHeightAnimator(float f, float f2) {
        float f3 = this.mOverExpansion;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.mExpandedHeight, f);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(f2, f, f3, ofFloat) { // from class: com.android.systemui.statusbar.phone.PanelViewController$$ExternalSyntheticLambda1
            public final /* synthetic */ float f$1;
            public final /* synthetic */ float f$2;
            public final /* synthetic */ float f$3;
            public final /* synthetic */ ValueAnimator f$4;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                PanelViewController.this.lambda$createHeightAnimator$4(this.f$1, this.f$2, this.f$3, this.f$4, valueAnimator);
            }
        });
        return ofFloat;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createHeightAnimator$4(float f, float f2, float f3, ValueAnimator valueAnimator, ValueAnimator valueAnimator2) {
        if (f > 0.0f || (f2 == 0.0f && f3 != 0.0f)) {
            setOverExpansionInternal(MathUtils.lerp(f3, this.mPanelFlingOvershootAmount * f, Interpolators.FAST_OUT_SLOW_IN.getInterpolation(valueAnimator.getAnimatedFraction())), false);
        }
        setExpandedHeightInternal(((Float) valueAnimator2.getAnimatedValue()).floatValue());
    }

    /* access modifiers changed from: protected */
    public void notifyBarPanelExpansionChanged() {
        PanelBar panelBar = this.mBar;
        if (panelBar != null) {
            float f = this.mExpandedFraction;
            panelBar.panelExpansionChanged(f, f > 0.0f || this.mInstantExpanding || isPanelVisibleBecauseOfHeadsUp() || this.mTracking || (this.mHeightAnimator != null && !this.mIsSpringBackAnimation));
        }
        for (int i = 0; i < this.mExpansionListeners.size(); i++) {
            this.mExpansionListeners.get(i).onPanelExpansionChanged(this.mExpandedFraction, this.mTracking);
        }
    }

    public void addExpansionListener(PanelExpansionListener panelExpansionListener) {
        this.mExpansionListeners.add(panelExpansionListener);
    }

    /* access modifiers changed from: protected */
    public boolean onEmptySpaceClick(float f) {
        if (this.mHintAnimationRunning) {
            return true;
        }
        return onMiddleClicked();
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Object[] objArr = new Object[8];
        objArr[0] = getClass().getSimpleName();
        objArr[1] = Float.valueOf(getExpandedHeight());
        objArr[2] = Integer.valueOf(getMaxPanelHeight());
        String str = "T";
        objArr[3] = this.mClosing ? str : "f";
        objArr[4] = this.mTracking ? str : "f";
        ValueAnimator valueAnimator = this.mHeightAnimator;
        objArr[5] = valueAnimator;
        objArr[6] = (valueAnimator == null || !valueAnimator.isStarted()) ? "" : " (started)";
        if (!this.mTouchDisabled) {
            str = "f";
        }
        objArr[7] = str;
        printWriter.println(String.format("[PanelView(%s): expandedHeight=%f maxPanelHeight=%d closing=%s tracking=%s timeAnim=%s%s touchDisabled=%s]", objArr));
    }

    public void setHeadsUpManager(HeadsUpManagerPhone headsUpManagerPhone) {
        this.mHeadsUpManager = headsUpManagerPhone;
    }

    public void setIsLaunchAnimationRunning(boolean z) {
        this.mIsLaunchAnimationRunning = z;
    }

    public void collapseWithDuration(int i) {
        this.mFixedDuration = i;
        collapse(false, 1.0f);
        this.mFixedDuration = -1;
    }

    public ViewGroup getView() {
        return this.mView;
    }

    public boolean isEnabled() {
        return this.mView.isEnabled();
    }

    /* loaded from: classes.dex */
    public class TouchHandler implements View.OnTouchListener {
        public TouchHandler() {
        }

        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            int pointerId;
            if (PanelViewController.this.mInstantExpanding || !PanelViewController.this.mNotificationsDragEnabled || PanelViewController.this.mTouchDisabled) {
                return false;
            }
            if (PanelViewController.this.mMotionAborted && motionEvent.getActionMasked() != 0) {
                return false;
            }
            int findPointerIndex = motionEvent.findPointerIndex(PanelViewController.this.mTrackingPointer);
            if (findPointerIndex < 0) {
                PanelViewController.this.mTrackingPointer = motionEvent.getPointerId(0);
                findPointerIndex = 0;
            }
            float x = motionEvent.getX(findPointerIndex);
            float y = motionEvent.getY(findPointerIndex);
            boolean canCollapsePanelOnTouch = PanelViewController.this.canCollapsePanelOnTouch();
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked != 0) {
                if (actionMasked != 1) {
                    if (actionMasked == 2) {
                        float f = y - PanelViewController.this.mInitialTouchY;
                        PanelViewController.this.addMovement(motionEvent);
                        if (canCollapsePanelOnTouch || PanelViewController.this.mTouchStartedInEmptyArea || PanelViewController.this.mAnimatingOnDown) {
                            float abs = Math.abs(f);
                            float touchSlop = PanelViewController.this.getTouchSlop(motionEvent);
                            if ((f < (-touchSlop) || (PanelViewController.this.mAnimatingOnDown && abs > touchSlop)) && abs > Math.abs(x - PanelViewController.this.mInitialTouchX)) {
                                PanelViewController.this.cancelHeightAnimator();
                                PanelViewController panelViewController = PanelViewController.this;
                                panelViewController.startExpandMotion(x, y, true, panelViewController.mExpandedHeight);
                                return true;
                            }
                        }
                    } else if (actionMasked != 3) {
                        if (actionMasked != 5) {
                            if (actionMasked == 6 && PanelViewController.this.mTrackingPointer == (pointerId = motionEvent.getPointerId(motionEvent.getActionIndex()))) {
                                int i = motionEvent.getPointerId(0) != pointerId ? 0 : 1;
                                PanelViewController.this.mTrackingPointer = motionEvent.getPointerId(i);
                                PanelViewController.this.mInitialTouchX = motionEvent.getX(i);
                                PanelViewController.this.mInitialTouchY = motionEvent.getY(i);
                            }
                        } else if (PanelViewController.this.mStatusBarStateController.getState() == 1) {
                            PanelViewController.this.mMotionAborted = true;
                            PanelViewController.this.mVelocityTracker.clear();
                        }
                    }
                }
                PanelViewController.this.mVelocityTracker.clear();
            } else {
                PanelViewController.this.mStatusBar.userActivity();
                PanelViewController panelViewController2 = PanelViewController.this;
                panelViewController2.mAnimatingOnDown = panelViewController2.mHeightAnimator != null && !PanelViewController.this.mIsSpringBackAnimation;
                PanelViewController.this.mMinExpandHeight = 0.0f;
                PanelViewController.this.mDownTime = SystemClock.uptimeMillis();
                if (PanelViewController.this.mAnimatingOnDown && PanelViewController.this.mClosing) {
                    PanelViewController panelViewController3 = PanelViewController.this;
                    if (!panelViewController3.mHintAnimationRunning) {
                        panelViewController3.cancelHeightAnimator();
                        PanelViewController.this.mTouchSlopExceeded = true;
                        return true;
                    }
                }
                PanelViewController.this.mInitialTouchY = y;
                PanelViewController.this.mInitialTouchX = x;
                PanelViewController panelViewController4 = PanelViewController.this;
                panelViewController4.mTouchStartedInEmptyArea = !panelViewController4.isInContentBounds(x, y);
                PanelViewController panelViewController5 = PanelViewController.this;
                panelViewController5.mTouchSlopExceeded = panelViewController5.mTouchSlopExceededBeforeDown;
                PanelViewController.this.mMotionAborted = false;
                PanelViewController panelViewController6 = PanelViewController.this;
                panelViewController6.mPanelClosedOnDown = panelViewController6.isFullyCollapsed();
                PanelViewController.this.mCollapsedAndHeadsUpOnDown = false;
                PanelViewController.this.mHasLayoutedSinceDown = false;
                PanelViewController.this.mUpdateFlingOnLayout = false;
                PanelViewController.this.mTouchAboveFalsingThreshold = false;
                PanelViewController.this.addMovement(motionEvent);
            }
            if (PanelViewController.this.mView.getVisibility() != 0) {
                return true;
            }
            return false;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            boolean z;
            int pointerId;
            if (PanelViewController.this.mInstantExpanding) {
                return false;
            }
            if (PanelViewController.this.mTouchDisabled && motionEvent.getActionMasked() != 3) {
                return false;
            }
            if (PanelViewController.this.mMotionAborted && motionEvent.getActionMasked() != 0) {
                return false;
            }
            if (!PanelViewController.this.mNotificationsDragEnabled) {
                PanelViewController panelViewController = PanelViewController.this;
                if (panelViewController.mTracking) {
                    panelViewController.onTrackingStopped(true);
                }
                return false;
            } else if (!PanelViewController.this.isFullyCollapsed() || !motionEvent.isFromSource(8194)) {
                int findPointerIndex = motionEvent.findPointerIndex(PanelViewController.this.mTrackingPointer);
                if (findPointerIndex < 0) {
                    PanelViewController.this.mTrackingPointer = motionEvent.getPointerId(0);
                    findPointerIndex = 0;
                }
                float x = motionEvent.getX(findPointerIndex);
                float y = motionEvent.getY(findPointerIndex);
                if (motionEvent.getActionMasked() == 0) {
                    PanelViewController panelViewController2 = PanelViewController.this;
                    panelViewController2.mGestureWaitForTouchSlop = panelViewController2.shouldGestureWaitForTouchSlop();
                    PanelViewController panelViewController3 = PanelViewController.this;
                    panelViewController3.mIgnoreXTouchSlop = panelViewController3.isFullyCollapsed() || PanelViewController.this.shouldGestureIgnoreXTouchSlop(x, y);
                }
                int actionMasked = motionEvent.getActionMasked();
                if (actionMasked != 0) {
                    if (actionMasked != 1) {
                        if (actionMasked == 2) {
                            PanelViewController.this.addMovement(motionEvent);
                            float f = y - PanelViewController.this.mInitialTouchY;
                            if (Math.abs(f) > PanelViewController.this.getTouchSlop(motionEvent) && (Math.abs(f) > Math.abs(x - PanelViewController.this.mInitialTouchX) || PanelViewController.this.mIgnoreXTouchSlop)) {
                                PanelViewController.this.mTouchSlopExceeded = true;
                                if (PanelViewController.this.mGestureWaitForTouchSlop) {
                                    PanelViewController panelViewController4 = PanelViewController.this;
                                    if (!panelViewController4.mTracking && !panelViewController4.mCollapsedAndHeadsUpOnDown) {
                                        if (PanelViewController.this.mInitialOffsetOnTouch != 0.0f) {
                                            PanelViewController panelViewController5 = PanelViewController.this;
                                            panelViewController5.startExpandMotion(x, y, false, panelViewController5.mExpandedHeight);
                                            f = 0.0f;
                                        }
                                        PanelViewController.this.cancelHeightAnimator();
                                        PanelViewController.this.onTrackingStarted();
                                    }
                                }
                            }
                            float max = Math.max(Math.max(0.0f, PanelViewController.this.mInitialOffsetOnTouch + f), PanelViewController.this.mMinExpandHeight);
                            if ((-f) >= ((float) PanelViewController.this.getFalsingThreshold())) {
                                PanelViewController.this.mTouchAboveFalsingThreshold = true;
                                PanelViewController panelViewController6 = PanelViewController.this;
                                panelViewController6.mUpwardsWhenThresholdReached = panelViewController6.isDirectionUpwards(x, y);
                            }
                            if ((!PanelViewController.this.mGestureWaitForTouchSlop || PanelViewController.this.mTracking) && !PanelViewController.this.isTrackingBlocked()) {
                                PanelViewController.this.setExpandedHeightInternal(max);
                            }
                        } else if (actionMasked != 3) {
                            if (actionMasked != 5) {
                                if (actionMasked == 6 && PanelViewController.this.mTrackingPointer == (pointerId = motionEvent.getPointerId(motionEvent.getActionIndex()))) {
                                    int i = motionEvent.getPointerId(0) != pointerId ? 0 : 1;
                                    float y2 = motionEvent.getY(i);
                                    float x2 = motionEvent.getX(i);
                                    PanelViewController.this.mTrackingPointer = motionEvent.getPointerId(i);
                                    PanelViewController.this.mHandlingPointerUp = true;
                                    PanelViewController panelViewController7 = PanelViewController.this;
                                    panelViewController7.startExpandMotion(x2, y2, true, panelViewController7.mExpandedHeight);
                                    PanelViewController.this.mHandlingPointerUp = false;
                                }
                            } else if (PanelViewController.this.mStatusBarStateController.getState() == 1) {
                                PanelViewController.this.mMotionAborted = true;
                                PanelViewController.this.endMotionEvent(motionEvent, x, y, true);
                                return false;
                            }
                        }
                    }
                    PanelViewController.this.addMovement(motionEvent);
                    PanelViewController.this.endMotionEvent(motionEvent, x, y, false);
                    if (PanelViewController.this.mHeightAnimator == null) {
                        if (motionEvent.getActionMasked() == 1) {
                            PanelViewController.this.endJankMonitoring(0);
                        } else {
                            PanelViewController.this.cancelJankMonitoring(0);
                        }
                    }
                } else {
                    PanelViewController panelViewController8 = PanelViewController.this;
                    panelViewController8.startExpandMotion(x, y, false, panelViewController8.mExpandedHeight);
                    PanelViewController.this.mMinExpandHeight = 0.0f;
                    PanelViewController panelViewController9 = PanelViewController.this;
                    panelViewController9.mPanelClosedOnDown = panelViewController9.isFullyCollapsed();
                    PanelViewController.this.mHasLayoutedSinceDown = false;
                    PanelViewController.this.mUpdateFlingOnLayout = false;
                    PanelViewController.this.mMotionAborted = false;
                    PanelViewController.this.mDownTime = SystemClock.uptimeMillis();
                    PanelViewController.this.mTouchAboveFalsingThreshold = false;
                    PanelViewController panelViewController10 = PanelViewController.this;
                    panelViewController10.mCollapsedAndHeadsUpOnDown = panelViewController10.isFullyCollapsed() && PanelViewController.this.mHeadsUpManager.hasPinnedHeadsUp();
                    PanelViewController.this.addMovement(motionEvent);
                    if (PanelViewController.this.mHeightAnimator != null) {
                        PanelViewController panelViewController11 = PanelViewController.this;
                        if (!panelViewController11.mHintAnimationRunning && !panelViewController11.mIsSpringBackAnimation) {
                            z = true;
                            if (PanelViewController.this.mGestureWaitForTouchSlop || z) {
                                PanelViewController panelViewController12 = PanelViewController.this;
                                panelViewController12.mTouchSlopExceeded = !z || panelViewController12.mTouchSlopExceededBeforeDown;
                                PanelViewController.this.cancelHeightAnimator();
                                PanelViewController.this.onTrackingStarted();
                            }
                            if (PanelViewController.this.isFullyCollapsed() && !PanelViewController.this.mHeadsUpManager.hasPinnedHeadsUp() && !PanelViewController.this.mStatusBar.isBouncerShowing()) {
                                PanelViewController.this.startOpening(motionEvent);
                            }
                        }
                    }
                    z = false;
                    if (PanelViewController.this.mGestureWaitForTouchSlop) {
                    }
                    PanelViewController panelViewController12 = PanelViewController.this;
                    panelViewController12.mTouchSlopExceeded = !z || panelViewController12.mTouchSlopExceededBeforeDown;
                    PanelViewController.this.cancelHeightAnimator();
                    PanelViewController.this.onTrackingStarted();
                    if (PanelViewController.this.isFullyCollapsed()) {
                        PanelViewController.this.startOpening(motionEvent);
                    }
                }
                if (!PanelViewController.this.mGestureWaitForTouchSlop || PanelViewController.this.mTracking) {
                    return true;
                }
                return false;
            } else {
                if (motionEvent.getAction() == 1) {
                    PanelViewController.this.expand(true);
                }
                return true;
            }
        }
    }

    /* loaded from: classes.dex */
    public class OnLayoutChangeListener implements View.OnLayoutChangeListener {
        public OnLayoutChangeListener() {
        }

        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            PanelViewController.this.requestPanelHeightUpdate();
            PanelViewController.this.mHasLayoutedSinceDown = true;
            if (PanelViewController.this.mUpdateFlingOnLayout) {
                PanelViewController.this.abortAnimations();
                PanelViewController panelViewController = PanelViewController.this;
                panelViewController.fling(panelViewController.mUpdateFlingVelocity, true);
                PanelViewController.this.mUpdateFlingOnLayout = false;
            }
        }
    }

    /* loaded from: classes.dex */
    public class OnConfigurationChangedListener implements PanelView.OnConfigurationChangedListener {
        public OnConfigurationChangedListener() {
        }

        @Override // com.android.systemui.statusbar.phone.PanelView.OnConfigurationChangedListener
        public void onConfigurationChanged(Configuration configuration) {
            PanelViewController.this.loadDimens();
        }
    }

    /* access modifiers changed from: private */
    public void beginJankMonitoring(int i) {
        InteractionJankMonitor.getInstance().begin(new InteractionJankMonitor.Configuration.Builder(i).setView(this.mView).setTag(isFullyCollapsed() ? "Expand" : "Collapse"));
    }

    /* access modifiers changed from: private */
    public void endJankMonitoring(int i) {
        InteractionJankMonitor.getInstance().end(i);
    }

    /* access modifiers changed from: private */
    public void cancelJankMonitoring(int i) {
        InteractionJankMonitor.getInstance().cancel(i);
    }
}
