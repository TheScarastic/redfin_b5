package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.AlarmManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Trace;
import android.util.Log;
import android.util.MathUtils;
import android.util.Pair;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.internal.graphics.ColorUtils;
import com.android.internal.util.function.TriConsumer;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.settingslib.Utils;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dumpable;
import com.android.systemui.R$id;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.dock.DockManager;
import com.android.systemui.scrim.ScrimView;
import com.android.systemui.statusbar.notification.stack.ViewState;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.AlarmTimeout;
import com.android.systemui.util.wakelock.DelayedWakeLock;
import com.android.systemui.util.wakelock.WakeLock;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class ScrimController implements ViewTreeObserver.OnPreDrawListener, Dumpable {
    private boolean mAnimateChange;
    private long mAnimationDelay;
    private Animator.AnimatorListener mAnimatorListener;
    private int mBehindTint;
    private boolean mBlankScreen;
    private Runnable mBlankingTransitionRunnable;
    private int mBubbleTint;
    private Callback mCallback;
    private boolean mClipsQsScrim;
    private boolean mDarkenWhileDragging;
    private final DockManager mDockManager;
    private final DozeParameters mDozeParameters;
    private final Handler mHandler;
    private int mInFrontTint;
    private boolean mKeyguardOccluded;
    private final KeyguardStateController mKeyguardStateController;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final Executor mMainExecutor;
    private boolean mNeedsDrawableColorUpdate;
    private ScrimView mNotificationsScrim;
    private int mNotificationsTint;
    private Runnable mPendingFrameCallback;
    private boolean mQsBottomVisible;
    private float mQsExpansion;
    private boolean mScreenBlankingCallbackCalled;
    private boolean mScreenOn;
    private ScrimView mScrimBehind;
    private Runnable mScrimBehindChangeRunnable;
    private ScrimView mScrimForBubble;
    private ScrimView mScrimInFront;
    private final TriConsumer<ScrimState, Float, ColorExtractor.GradientColors> mScrimStateListener;
    private Consumer<Integer> mScrimVisibleListener;
    private int mScrimsVisibility;
    private final AlarmTimeout mTimeTicker;
    private boolean mTracking;
    private float mTransitionToFullShadeProgress;
    private boolean mTransitioningToFullShade;
    private UnlockedScreenOffAnimationController mUnlockedScreenOffAnimationController;
    private boolean mUpdatePending;
    private final WakeLock mWakeLock;
    private boolean mWakeLockHeld;
    private boolean mWallpaperSupportsAmbientMode;
    private boolean mWallpaperVisibilityTimedOut;
    private static final boolean DEBUG = Log.isLoggable("ScrimController", 3);
    static final int TAG_KEY_ANIM = R$id.scrim;
    private static final int TAG_START_ALPHA = R$id.scrim_alpha_start;
    private static final int TAG_END_ALPHA = R$id.scrim_alpha_end;
    private ScrimState mState = ScrimState.UNINITIALIZED;
    private float mScrimBehindAlphaKeyguard = 0.2f;
    private float mPanelExpansion = 1.0f;
    private boolean mExpansionAffectsAlpha = true;
    private long mAnimationDuration = -1;
    private final Interpolator mInterpolator = new DecelerateInterpolator();
    private float mInFrontAlpha = -1.0f;
    private float mBehindAlpha = -1.0f;
    private float mNotificationsAlpha = -1.0f;
    private float mBubbleAlpha = -1.0f;
    private final float mDefaultScrimAlpha = 1.0f;
    private final KeyguardVisibilityCallback mKeyguardVisibilityCallback = new KeyguardVisibilityCallback();
    private ColorExtractor.GradientColors mColors = new ColorExtractor.GradientColors();

    /* loaded from: classes.dex */
    public interface Callback {
        default void onCancelled() {
        }

        default void onDisplayBlanked() {
        }

        default void onFinished() {
        }

        default void onStart() {
        }
    }

    public void setCurrentUser(int i) {
    }

    public ScrimController(LightBarController lightBarController, DozeParameters dozeParameters, AlarmManager alarmManager, final KeyguardStateController keyguardStateController, DelayedWakeLock.Builder builder, Handler handler, KeyguardUpdateMonitor keyguardUpdateMonitor, DockManager dockManager, ConfigurationController configurationController, Executor executor, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController) {
        Objects.requireNonNull(lightBarController);
        this.mScrimStateListener = new TriConsumer() { // from class: com.android.systemui.statusbar.phone.ScrimController$$ExternalSyntheticLambda2
            public final void accept(Object obj, Object obj2, Object obj3) {
                LightBarController.this.setScrimState((ScrimState) obj, ((Float) obj2).floatValue(), (ColorExtractor.GradientColors) obj3);
            }
        };
        ScrimState.BUBBLE_EXPANDED.setBubbleAlpha(0.6f);
        this.mKeyguardStateController = keyguardStateController;
        this.mDarkenWhileDragging = !keyguardStateController.canDismissLockScreen();
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mHandler = handler;
        this.mMainExecutor = executor;
        this.mUnlockedScreenOffAnimationController = unlockedScreenOffAnimationController;
        this.mTimeTicker = new AlarmTimeout(alarmManager, new AlarmManager.OnAlarmListener() { // from class: com.android.systemui.statusbar.phone.ScrimController$$ExternalSyntheticLambda1
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                ScrimController.this.onHideWallpaperTimeout();
            }
        }, "hide_aod_wallpaper", handler);
        this.mWakeLock = builder.setHandler(handler).setTag("Scrims").build();
        this.mDozeParameters = dozeParameters;
        this.mDockManager = dockManager;
        keyguardStateController.addCallback(new KeyguardStateController.Callback() { // from class: com.android.systemui.statusbar.phone.ScrimController.1
            @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
            public void onKeyguardFadingAwayChanged() {
                ScrimController.this.setKeyguardFadingAway(keyguardStateController.isKeyguardFadingAway(), keyguardStateController.getKeyguardFadingAwayDuration());
            }
        });
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.statusbar.phone.ScrimController.2
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onThemeChanged() {
                ScrimController.this.onThemeChanged();
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onOverlayChanged() {
                ScrimController.this.onThemeChanged();
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onUiModeChanged() {
                ScrimController.this.onThemeChanged();
            }
        });
    }

    public void attachViews(ScrimView scrimView, ScrimView scrimView2, ScrimView scrimView3, ScrimView scrimView4) {
        this.mNotificationsScrim = scrimView2;
        this.mScrimBehind = scrimView;
        this.mScrimInFront = scrimView3;
        this.mScrimForBubble = scrimView4;
        updateThemeColors();
        scrimView.enableBottomEdgeConcave(this.mClipsQsScrim);
        this.mNotificationsScrim.enableRoundedCorners(true);
        Runnable runnable = this.mScrimBehindChangeRunnable;
        if (runnable != null) {
            this.mScrimBehind.setChangeRunnable(runnable, this.mMainExecutor);
            this.mScrimBehindChangeRunnable = null;
        }
        ScrimState[] values = ScrimState.values();
        for (int i = 0; i < values.length; i++) {
            values[i].init(this.mScrimInFront, this.mScrimBehind, this.mScrimForBubble, this.mDozeParameters, this.mDockManager);
            values[i].setScrimBehindAlphaKeyguard(this.mScrimBehindAlphaKeyguard);
            values[i].setDefaultScrimAlpha(this.mDefaultScrimAlpha);
        }
        this.mScrimBehind.setDefaultFocusHighlightEnabled(false);
        this.mNotificationsScrim.setDefaultFocusHighlightEnabled(false);
        this.mScrimInFront.setDefaultFocusHighlightEnabled(false);
        ScrimView scrimView5 = this.mScrimForBubble;
        if (scrimView5 != null) {
            scrimView5.setDefaultFocusHighlightEnabled(false);
        }
        updateScrims();
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardVisibilityCallback);
    }

    public void setScrimCornerRadius(int i) {
        ScrimView scrimView = this.mScrimBehind;
        if (scrimView != null && this.mNotificationsScrim != null) {
            scrimView.setCornerRadius(i);
            this.mNotificationsScrim.setCornerRadius(i);
        }
    }

    /* access modifiers changed from: package-private */
    public void setScrimVisibleListener(Consumer<Integer> consumer) {
        this.mScrimVisibleListener = consumer;
    }

    public void transitionTo(ScrimState scrimState) {
        transitionTo(scrimState, null);
    }

    public void transitionTo(ScrimState scrimState, Callback callback) {
        if (scrimState != this.mState) {
            if (DEBUG) {
                Log.d("ScrimController", "State changed to: " + scrimState);
            }
            if (scrimState != ScrimState.UNINITIALIZED) {
                ScrimState scrimState2 = this.mState;
                this.mState = scrimState;
                Trace.traceCounter(4096, "scrim_state", scrimState.ordinal());
                Callback callback2 = this.mCallback;
                if (callback2 != null) {
                    callback2.onCancelled();
                }
                this.mCallback = callback;
                scrimState.prepare(scrimState2);
                this.mScreenBlankingCallbackCalled = false;
                this.mAnimationDelay = 0;
                this.mBlankScreen = scrimState.getBlanksScreen();
                this.mAnimateChange = scrimState.getAnimateChange();
                this.mAnimationDuration = scrimState.getAnimationDuration();
                this.mInFrontTint = scrimState.getFrontTint();
                this.mBehindTint = scrimState.getBehindTint();
                this.mNotificationsTint = scrimState.getNotifTint();
                this.mBubbleTint = scrimState.getBubbleTint();
                this.mInFrontAlpha = scrimState.getFrontAlpha();
                this.mBehindAlpha = scrimState.getBehindAlpha();
                this.mBubbleAlpha = scrimState.getBubbleAlpha();
                this.mNotificationsAlpha = scrimState.getNotifAlpha();
                if (Float.isNaN(this.mBehindAlpha) || Float.isNaN(this.mInFrontAlpha) || Float.isNaN(this.mNotificationsAlpha)) {
                    throw new IllegalStateException("Scrim opacity is NaN for state: " + scrimState + ", front: " + this.mInFrontAlpha + ", back: " + this.mBehindAlpha + ", notif: " + this.mNotificationsAlpha);
                }
                applyStateToAlpha();
                boolean z = true;
                this.mScrimInFront.setFocusable(!scrimState.isLowPowerState());
                this.mScrimBehind.setFocusable(!scrimState.isLowPowerState());
                this.mNotificationsScrim.setFocusable(!scrimState.isLowPowerState());
                Runnable runnable = this.mPendingFrameCallback;
                if (runnable != null) {
                    this.mScrimBehind.removeCallbacks(runnable);
                    this.mPendingFrameCallback = null;
                }
                if (this.mHandler.hasCallbacks(this.mBlankingTransitionRunnable)) {
                    this.mHandler.removeCallbacks(this.mBlankingTransitionRunnable);
                    this.mBlankingTransitionRunnable = null;
                }
                if (scrimState == ScrimState.BRIGHTNESS_MIRROR) {
                    z = false;
                }
                this.mNeedsDrawableColorUpdate = z;
                if (this.mState.isLowPowerState()) {
                    holdWakeLock();
                }
                this.mWallpaperVisibilityTimedOut = false;
                if (shouldFadeAwayWallpaper()) {
                    DejankUtils.postAfterTraversal(new Runnable() { // from class: com.android.systemui.statusbar.phone.ScrimController$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            ScrimController.this.lambda$transitionTo$0();
                        }
                    });
                } else {
                    AlarmTimeout alarmTimeout = this.mTimeTicker;
                    Objects.requireNonNull(alarmTimeout);
                    DejankUtils.postAfterTraversal(new Runnable() { // from class: com.android.systemui.statusbar.phone.ScrimController$$ExternalSyntheticLambda7
                        @Override // java.lang.Runnable
                        public final void run() {
                            AlarmTimeout.this.cancel();
                        }
                    });
                }
                if (!this.mKeyguardUpdateMonitor.needsSlowUnlockTransition() || this.mState != ScrimState.UNLOCKED) {
                    ScrimState scrimState3 = ScrimState.AOD;
                    if ((scrimState2 != scrimState3 || (this.mDozeParameters.getAlwaysOn() && this.mState != ScrimState.UNLOCKED)) && (this.mState != scrimState3 || this.mDozeParameters.getDisplayNeedsBlanking())) {
                        scheduleUpdate();
                    } else {
                        onPreDraw();
                    }
                } else {
                    this.mAnimationDelay = 100;
                    scheduleUpdate();
                }
                dispatchBackScrimState(this.mScrimBehind.getViewAlpha());
                return;
            }
            throw new IllegalArgumentException("Cannot change to UNINITIALIZED.");
        } else if (callback != null && this.mCallback != callback) {
            callback.onFinished();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$transitionTo$0() {
        this.mTimeTicker.schedule(this.mDozeParameters.getWallpaperAodDuration(), 1);
    }

    private boolean shouldFadeAwayWallpaper() {
        if (this.mWallpaperSupportsAmbientMode && this.mState == ScrimState.AOD && (this.mDozeParameters.getAlwaysOn() || this.mDockManager.isDocked())) {
            return true;
        }
        return false;
    }

    public ScrimState getState() {
        return this.mState;
    }

    /* access modifiers changed from: protected */
    public void setScrimBehindValues(float f) {
        this.mScrimBehindAlphaKeyguard = f;
        for (ScrimState scrimState : ScrimState.values()) {
            scrimState.setScrimBehindAlphaKeyguard(f);
        }
        scheduleUpdate();
    }

    public void onTrackingStarted() {
        this.mTracking = true;
        this.mDarkenWhileDragging = true ^ this.mKeyguardStateController.canDismissLockScreen();
    }

    public void onExpandingFinished() {
        this.mTracking = false;
    }

    /* access modifiers changed from: protected */
    @VisibleForTesting
    public void onHideWallpaperTimeout() {
        ScrimState scrimState = this.mState;
        if (scrimState == ScrimState.AOD || scrimState == ScrimState.PULSING) {
            holdWakeLock();
            this.mWallpaperVisibilityTimedOut = true;
            this.mAnimateChange = true;
            this.mAnimationDuration = this.mDozeParameters.getWallpaperFadeOutDuration();
            scheduleUpdate();
        }
    }

    private void holdWakeLock() {
        if (!this.mWakeLockHeld) {
            WakeLock wakeLock = this.mWakeLock;
            if (wakeLock != null) {
                this.mWakeLockHeld = true;
                wakeLock.acquire("ScrimController");
                return;
            }
            Log.w("ScrimController", "Cannot hold wake lock, it has not been set yet");
        }
    }

    public void setPanelExpansion(float f) {
        if (Float.isNaN(f)) {
            throw new IllegalArgumentException("Fraction should not be NaN");
        } else if (this.mPanelExpansion != f) {
            this.mPanelExpansion = f;
            ScrimState scrimState = this.mState;
            if ((scrimState == ScrimState.UNLOCKED || scrimState == ScrimState.KEYGUARD || scrimState == ScrimState.SHADE_LOCKED || scrimState == ScrimState.PULSING || scrimState == ScrimState.BUBBLE_EXPANDED) && this.mExpansionAffectsAlpha) {
                applyAndDispatchState();
            }
        }
    }

    public void setTransitionToFullShadeProgress(float f) {
        if (f != this.mTransitionToFullShadeProgress) {
            this.mTransitionToFullShadeProgress = f;
            setTransitionToFullShade(f > 0.0f);
            applyAndDispatchState();
        }
    }

    private void setTransitionToFullShade(boolean z) {
        if (z != this.mTransitioningToFullShade) {
            this.mTransitioningToFullShade = z;
            if (z) {
                ScrimState.SHADE_LOCKED.prepare(this.mState);
            }
        }
    }

    public void setNotificationsBounds(float f, float f2, float f3, float f4) {
        if (this.mClipsQsScrim) {
            this.mNotificationsScrim.setDrawableBounds(f - 1.0f, f2, f3 + 1.0f, f4);
            this.mScrimBehind.setBottomEdgePosition((int) f2);
            return;
        }
        this.mNotificationsScrim.setDrawableBounds(f, f2, f3, f4);
    }

    public void setQsPosition(float f, int i) {
        if (!Float.isNaN(f)) {
            boolean z = true;
            boolean z2 = i > 0;
            if (this.mQsExpansion != f || this.mQsBottomVisible != z2) {
                this.mQsExpansion = f;
                this.mQsBottomVisible = z2;
                ScrimState scrimState = this.mState;
                if (!(scrimState == ScrimState.SHADE_LOCKED || scrimState == ScrimState.KEYGUARD || scrimState == ScrimState.PULSING || scrimState == ScrimState.BUBBLE_EXPANDED)) {
                    z = false;
                }
                if (z && this.mExpansionAffectsAlpha) {
                    applyAndDispatchState();
                }
            }
        }
    }

    public void setClipsQsScrim(boolean z) {
        if (z != this.mClipsQsScrim) {
            this.mClipsQsScrim = z;
            for (ScrimState scrimState : ScrimState.values()) {
                scrimState.setClipQsScrim(this.mClipsQsScrim);
            }
            ScrimView scrimView = this.mScrimBehind;
            if (scrimView != null) {
                scrimView.enableBottomEdgeConcave(this.mClipsQsScrim);
            }
            ScrimState scrimState2 = this.mState;
            if (scrimState2 != ScrimState.UNINITIALIZED) {
                scrimState2.prepare(scrimState2);
                applyAndDispatchState();
            }
        }
    }

    @VisibleForTesting
    public boolean getClipQsScrim() {
        return this.mClipsQsScrim;
    }

    private void setOrAdaptCurrentAnimation(View view) {
        if (view != null) {
            float currentScrimAlpha = getCurrentScrimAlpha(view);
            boolean z = view == this.mScrimBehind && this.mQsBottomVisible;
            if (!isAnimating(view) || z) {
                updateScrimColor(view, currentScrimAlpha, getCurrentScrimTint(view));
                return;
            }
            ValueAnimator valueAnimator = (ValueAnimator) view.getTag(TAG_KEY_ANIM);
            int i = TAG_END_ALPHA;
            float floatValue = ((Float) view.getTag(i)).floatValue();
            int i2 = TAG_START_ALPHA;
            view.setTag(i2, Float.valueOf(((Float) view.getTag(i2)).floatValue() + (currentScrimAlpha - floatValue)));
            view.setTag(i, Float.valueOf(currentScrimAlpha));
            valueAnimator.setCurrentPlayTime(valueAnimator.getCurrentPlayTime());
        }
    }

    private void applyStateToAlpha() {
        if (this.mExpansionAffectsAlpha) {
            ScrimState scrimState = this.mState;
            if (scrimState != ScrimState.UNLOCKED && scrimState != ScrimState.BUBBLE_EXPANDED) {
                ScrimState scrimState2 = ScrimState.KEYGUARD;
                if (scrimState == scrimState2 || scrimState == ScrimState.SHADE_LOCKED || scrimState == ScrimState.PULSING) {
                    Pair<Integer, Float> calculateBackStateForState = calculateBackStateForState(scrimState);
                    int intValue = ((Integer) calculateBackStateForState.first).intValue();
                    float floatValue = ((Float) calculateBackStateForState.second).floatValue();
                    if (this.mTransitionToFullShadeProgress > 0.0f) {
                        Pair<Integer, Float> calculateBackStateForState2 = calculateBackStateForState(ScrimState.SHADE_LOCKED);
                        floatValue = MathUtils.lerp(floatValue, ((Float) calculateBackStateForState2.second).floatValue(), this.mTransitionToFullShadeProgress);
                        intValue = ColorUtils.blendARGB(intValue, ((Integer) calculateBackStateForState2.first).intValue(), this.mTransitionToFullShadeProgress);
                    }
                    this.mInFrontAlpha = this.mState.getFrontAlpha();
                    if (this.mClipsQsScrim) {
                        this.mNotificationsAlpha = floatValue;
                        this.mNotificationsTint = intValue;
                        this.mBehindAlpha = 1.0f;
                        this.mBehindTint = -16777216;
                    } else {
                        this.mBehindAlpha = floatValue;
                        if (this.mState == ScrimState.SHADE_LOCKED) {
                            this.mNotificationsAlpha = getInterpolatedFraction();
                        } else {
                            this.mNotificationsAlpha = Math.max(1.0f - getInterpolatedFraction(), this.mQsExpansion);
                        }
                        if (this.mState == scrimState2 && this.mTransitionToFullShadeProgress > 0.0f) {
                            this.mNotificationsAlpha = MathUtils.lerp(this.mNotificationsAlpha, getInterpolatedFraction(), this.mTransitionToFullShadeProgress);
                        }
                        this.mNotificationsTint = this.mState.getNotifTint();
                        this.mBehindTint = intValue;
                    }
                }
            } else if (!this.mUnlockedScreenOffAnimationController.isScreenOffAnimationPlaying()) {
                float pow = (float) Math.pow((double) getInterpolatedFraction(), 0.800000011920929d);
                if (this.mClipsQsScrim) {
                    this.mBehindAlpha = 1.0f;
                    this.mNotificationsAlpha = pow * this.mDefaultScrimAlpha;
                } else {
                    float f = pow * this.mDefaultScrimAlpha;
                    this.mBehindAlpha = f;
                    this.mNotificationsAlpha = f;
                }
                this.mInFrontAlpha = 0.0f;
            }
            if (Float.isNaN(this.mBehindAlpha) || Float.isNaN(this.mInFrontAlpha) || Float.isNaN(this.mNotificationsAlpha)) {
                throw new IllegalStateException("Scrim opacity is NaN for state: " + this.mState + ", front: " + this.mInFrontAlpha + ", back: " + this.mBehindAlpha + ", notif: " + this.mNotificationsAlpha);
            }
        }
    }

    private Pair<Integer, Float> calculateBackStateForState(ScrimState scrimState) {
        float f;
        int i;
        int i2;
        float interpolatedFraction = getInterpolatedFraction();
        float notifAlpha = this.mClipsQsScrim ? scrimState.getNotifAlpha() : scrimState.getBehindAlpha();
        if (this.mDarkenWhileDragging) {
            f = MathUtils.lerp(this.mDefaultScrimAlpha, notifAlpha, interpolatedFraction);
        } else {
            f = MathUtils.lerp(0.0f, notifAlpha, interpolatedFraction);
        }
        if (this.mClipsQsScrim) {
            i = ColorUtils.blendARGB(ScrimState.BOUNCER.getNotifTint(), scrimState.getNotifTint(), interpolatedFraction);
        } else {
            i = ColorUtils.blendARGB(ScrimState.BOUNCER.getBehindTint(), scrimState.getBehindTint(), interpolatedFraction);
        }
        float f2 = this.mQsExpansion;
        if (f2 > 0.0f) {
            f = MathUtils.lerp(f, this.mDefaultScrimAlpha, f2);
            if (this.mClipsQsScrim) {
                i2 = ScrimState.SHADE_LOCKED.getNotifTint();
            } else {
                i2 = ScrimState.SHADE_LOCKED.getBehindTint();
            }
            i = ColorUtils.blendARGB(i, i2, this.mQsExpansion);
        }
        return new Pair<>(Integer.valueOf(i), Float.valueOf(f));
    }

    private void applyAndDispatchState() {
        applyStateToAlpha();
        if (!this.mUpdatePending) {
            setOrAdaptCurrentAnimation(this.mScrimBehind);
            setOrAdaptCurrentAnimation(this.mNotificationsScrim);
            setOrAdaptCurrentAnimation(this.mScrimInFront);
            setOrAdaptCurrentAnimation(this.mScrimForBubble);
            dispatchBackScrimState(this.mScrimBehind.getViewAlpha());
            if (this.mWallpaperVisibilityTimedOut) {
                this.mWallpaperVisibilityTimedOut = false;
                DejankUtils.postAfterTraversal(new Runnable() { // from class: com.android.systemui.statusbar.phone.ScrimController$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        ScrimController.this.lambda$applyAndDispatchState$1();
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$applyAndDispatchState$1() {
        this.mTimeTicker.schedule(this.mDozeParameters.getWallpaperAodDuration(), 1);
    }

    public void setAodFrontScrimAlpha(float f) {
        if (this.mInFrontAlpha != f && shouldUpdateFrontScrimAlpha()) {
            this.mInFrontAlpha = f;
            updateScrims();
        }
        ScrimState.AOD.setAodFrontScrimAlpha(f);
        ScrimState.PULSING.setAodFrontScrimAlpha(f);
    }

    private boolean shouldUpdateFrontScrimAlpha() {
        if ((this.mState != ScrimState.AOD || (!this.mDozeParameters.getAlwaysOn() && !this.mDockManager.isDocked())) && this.mState != ScrimState.PULSING) {
            return false;
        }
        return true;
    }

    public void setWakeLockScreenSensorActive(boolean z) {
        for (ScrimState scrimState : ScrimState.values()) {
            scrimState.setWakeLockScreenSensorActive(z);
        }
        ScrimState scrimState2 = this.mState;
        if (scrimState2 == ScrimState.PULSING) {
            float behindAlpha = scrimState2.getBehindAlpha();
            if (this.mBehindAlpha != behindAlpha) {
                this.mBehindAlpha = behindAlpha;
                if (!Float.isNaN(behindAlpha)) {
                    updateScrims();
                    return;
                }
                throw new IllegalStateException("Scrim opacity is NaN for state: " + this.mState + ", back: " + this.mBehindAlpha);
            }
        }
    }

    protected void scheduleUpdate() {
        ScrimView scrimView;
        if (!this.mUpdatePending && (scrimView = this.mScrimBehind) != null) {
            scrimView.invalidate();
            this.mScrimBehind.getViewTreeObserver().addOnPreDrawListener(this);
            this.mUpdatePending = true;
        }
    }

    protected void updateScrims() {
        boolean z = true;
        if (this.mNeedsDrawableColorUpdate) {
            this.mNeedsDrawableColorUpdate = false;
            boolean z2 = this.mScrimInFront.getViewAlpha() != 0.0f && !this.mBlankScreen;
            boolean z3 = this.mScrimBehind.getViewAlpha() != 0.0f && !this.mBlankScreen;
            boolean z4 = this.mNotificationsScrim.getViewAlpha() != 0.0f && !this.mBlankScreen;
            this.mScrimInFront.setColors(this.mColors, z2);
            this.mScrimBehind.setColors(this.mColors, z3);
            this.mNotificationsScrim.setColors(this.mColors, z4);
            dispatchBackScrimState(this.mScrimBehind.getViewAlpha());
        }
        ScrimState scrimState = this.mState;
        ScrimState scrimState2 = ScrimState.AOD;
        boolean z5 = (scrimState == scrimState2 || scrimState == ScrimState.PULSING) && this.mWallpaperVisibilityTimedOut;
        boolean z6 = (scrimState == ScrimState.PULSING || scrimState == scrimState2) && this.mKeyguardOccluded;
        if (z5 || z6) {
            this.mBehindAlpha = 1.0f;
        }
        setScrimAlpha(this.mScrimInFront, this.mInFrontAlpha);
        setScrimAlpha(this.mScrimBehind, this.mBehindAlpha);
        setScrimAlpha(this.mNotificationsScrim, this.mNotificationsAlpha);
        ScrimView scrimView = this.mScrimForBubble;
        if (scrimView != null) {
            if (scrimView.getViewAlpha() == 0.0f || this.mBlankScreen) {
                z = false;
            }
            this.mScrimForBubble.setColors(this.mColors, z);
            setScrimAlpha(this.mScrimForBubble, this.mBubbleAlpha);
        }
        onFinished(this.mState);
        dispatchScrimsVisible();
    }

    private void dispatchBackScrimState(float f) {
        if (this.mClipsQsScrim && this.mQsBottomVisible) {
            f = this.mNotificationsAlpha;
        }
        this.mScrimStateListener.accept(this.mState, Float.valueOf(f), this.mScrimInFront.getColors());
    }

    /* access modifiers changed from: private */
    public void dispatchScrimsVisible() {
        ScrimView scrimView = this.mClipsQsScrim ? this.mNotificationsScrim : this.mScrimBehind;
        int i = (this.mScrimInFront.getViewAlpha() == 1.0f || scrimView.getViewAlpha() == 1.0f) ? 2 : (this.mScrimInFront.getViewAlpha() == 0.0f && scrimView.getViewAlpha() == 0.0f) ? 0 : 1;
        if (this.mScrimsVisibility != i) {
            this.mScrimsVisibility = i;
            this.mScrimVisibleListener.accept(Integer.valueOf(i));
        }
    }

    private float getInterpolatedFraction() {
        return Interpolators.getNotificationScrimAlpha(this.mPanelExpansion, false);
    }

    private void setScrimAlpha(ScrimView scrimView, float f) {
        boolean z = false;
        if (f == 0.0f) {
            scrimView.setClickable(false);
        } else {
            if (this.mState != ScrimState.AOD) {
                z = true;
            }
            scrimView.setClickable(z);
        }
        updateScrim(scrimView, f);
    }

    private String getScrimName(ScrimView scrimView) {
        if (scrimView == this.mScrimInFront) {
            return "front_scrim";
        }
        if (scrimView == this.mScrimBehind) {
            return "behind_scrim";
        }
        if (scrimView == this.mNotificationsScrim) {
            return "notifications_scrim";
        }
        return scrimView == this.mScrimForBubble ? "bubble_scrim" : "unknown_scrim";
    }

    private void updateScrimColor(View view, float f, int i) {
        float max = Math.max(0.0f, Math.min(1.0f, f));
        if (view instanceof ScrimView) {
            ScrimView scrimView = (ScrimView) view;
            Trace.traceCounter(4096, getScrimName(scrimView) + "_alpha", (int) (255.0f * max));
            Trace.traceCounter(4096, getScrimName(scrimView) + "_tint", Color.alpha(i));
            scrimView.setTint(i);
            scrimView.setViewAlpha(max);
        } else {
            view.setAlpha(max);
        }
        dispatchScrimsVisible();
    }

    private void startScrimAnimation(final View view, float f) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        Animator.AnimatorListener animatorListener = this.mAnimatorListener;
        if (animatorListener != null) {
            ofFloat.addListener(animatorListener);
        }
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(view, view instanceof ScrimView ? ((ScrimView) view).getTint() : 0) { // from class: com.android.systemui.statusbar.phone.ScrimController$$ExternalSyntheticLambda0
            public final /* synthetic */ View f$1;
            public final /* synthetic */ int f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScrimController.this.lambda$startScrimAnimation$2(this.f$1, this.f$2, valueAnimator);
            }
        });
        ofFloat.setInterpolator(this.mInterpolator);
        ofFloat.setStartDelay(this.mAnimationDelay);
        ofFloat.setDuration(this.mAnimationDuration);
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.ScrimController.3
            private final Callback mLastCallback;
            private final ScrimState mLastState;

            {
                this.mLastState = ScrimController.this.mState;
                this.mLastCallback = ScrimController.this.mCallback;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                view.setTag(ScrimController.TAG_KEY_ANIM, null);
                ScrimController.this.onFinished(this.mLastCallback, this.mLastState);
                ScrimController.this.dispatchScrimsVisible();
            }
        });
        view.setTag(TAG_START_ALPHA, Float.valueOf(f));
        view.setTag(TAG_END_ALPHA, Float.valueOf(getCurrentScrimAlpha(view)));
        view.setTag(TAG_KEY_ANIM, ofFloat);
        ofFloat.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startScrimAnimation$2(View view, int i, ValueAnimator valueAnimator) {
        float floatValue = ((Float) view.getTag(TAG_START_ALPHA)).floatValue();
        float floatValue2 = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        updateScrimColor(view, MathUtils.constrain(MathUtils.lerp(floatValue, getCurrentScrimAlpha(view), floatValue2), 0.0f, 1.0f), ColorUtils.blendARGB(i, getCurrentScrimTint(view), floatValue2));
        dispatchScrimsVisible();
    }

    private float getCurrentScrimAlpha(View view) {
        if (view == this.mScrimInFront) {
            return this.mInFrontAlpha;
        }
        if (view == this.mScrimBehind) {
            return this.mBehindAlpha;
        }
        if (view == this.mNotificationsScrim) {
            return this.mNotificationsAlpha;
        }
        if (view == this.mScrimForBubble) {
            return this.mBubbleAlpha;
        }
        throw new IllegalArgumentException("Unknown scrim view");
    }

    private int getCurrentScrimTint(View view) {
        if (view == this.mScrimInFront) {
            return this.mInFrontTint;
        }
        if (view == this.mScrimBehind) {
            return this.mBehindTint;
        }
        if (view == this.mNotificationsScrim) {
            return this.mNotificationsTint;
        }
        if (view == this.mScrimForBubble) {
            return this.mBubbleTint;
        }
        throw new IllegalArgumentException("Unknown scrim view");
    }

    @Override // android.view.ViewTreeObserver.OnPreDrawListener
    public boolean onPreDraw() {
        this.mScrimBehind.getViewTreeObserver().removeOnPreDrawListener(this);
        this.mUpdatePending = false;
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onStart();
        }
        updateScrims();
        return true;
    }

    private void onFinished(ScrimState scrimState) {
        onFinished(this.mCallback, scrimState);
    }

    /* access modifiers changed from: private */
    public void onFinished(Callback callback, ScrimState scrimState) {
        if (this.mPendingFrameCallback == null) {
            if (!isAnimating(this.mScrimBehind) && !isAnimating(this.mNotificationsScrim) && !isAnimating(this.mScrimInFront) && !isAnimating(this.mScrimForBubble)) {
                if (this.mWakeLockHeld) {
                    this.mWakeLock.release("ScrimController");
                    this.mWakeLockHeld = false;
                }
                if (callback != null) {
                    callback.onFinished();
                    if (callback == this.mCallback) {
                        this.mCallback = null;
                    }
                }
                if (scrimState == ScrimState.UNLOCKED) {
                    this.mInFrontTint = 0;
                    this.mBehindTint = this.mState.getBehindTint();
                    this.mNotificationsTint = this.mState.getNotifTint();
                    this.mBubbleTint = 0;
                    updateScrimColor(this.mScrimInFront, this.mInFrontAlpha, this.mInFrontTint);
                    updateScrimColor(this.mScrimBehind, this.mBehindAlpha, this.mBehindTint);
                    updateScrimColor(this.mNotificationsScrim, this.mNotificationsAlpha, this.mNotificationsTint);
                    ScrimView scrimView = this.mScrimForBubble;
                    if (scrimView != null) {
                        updateScrimColor(scrimView, this.mBubbleAlpha, this.mBubbleTint);
                    }
                }
            } else if (callback != null && callback != this.mCallback) {
                callback.onFinished();
            }
        }
    }

    private boolean isAnimating(View view) {
        return (view == null || view.getTag(TAG_KEY_ANIM) == null) ? false : true;
    }

    @VisibleForTesting
    void setAnimatorListener(Animator.AnimatorListener animatorListener) {
        this.mAnimatorListener = animatorListener;
    }

    private void updateScrim(ScrimView scrimView, float f) {
        Callback callback;
        float viewAlpha = scrimView.getViewAlpha();
        ValueAnimator valueAnimator = (ValueAnimator) ViewState.getChildTag(scrimView, TAG_KEY_ANIM);
        if (valueAnimator != null) {
            cancelAnimator(valueAnimator);
        }
        if (this.mPendingFrameCallback == null) {
            if (this.mBlankScreen) {
                blankDisplay();
                return;
            }
            boolean z = true;
            if (!this.mScreenBlankingCallbackCalled && (callback = this.mCallback) != null) {
                callback.onDisplayBlanked();
                this.mScreenBlankingCallbackCalled = true;
            }
            if (scrimView == this.mScrimBehind) {
                dispatchBackScrimState(f);
            }
            boolean z2 = f != viewAlpha;
            if (scrimView.getTint() == getCurrentScrimTint(scrimView)) {
                z = false;
            }
            if (!z2 && !z) {
                return;
            }
            if (this.mAnimateChange) {
                startScrimAnimation(scrimView, viewAlpha);
            } else {
                updateScrimColor(scrimView, f, getCurrentScrimTint(scrimView));
            }
        }
    }

    private void cancelAnimator(ValueAnimator valueAnimator) {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    private void blankDisplay() {
        updateScrimColor(this.mScrimInFront, 1.0f, -16777216);
        ScrimController$$ExternalSyntheticLambda3 scrimController$$ExternalSyntheticLambda3 = new Runnable() { // from class: com.android.systemui.statusbar.phone.ScrimController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                ScrimController.this.lambda$blankDisplay$4();
            }
        };
        this.mPendingFrameCallback = scrimController$$ExternalSyntheticLambda3;
        doOnTheNextFrame(scrimController$$ExternalSyntheticLambda3);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$blankDisplay$4() {
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onDisplayBlanked();
            this.mScreenBlankingCallbackCalled = true;
        }
        this.mBlankingTransitionRunnable = new Runnable() { // from class: com.android.systemui.statusbar.phone.ScrimController$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                ScrimController.this.lambda$blankDisplay$3();
            }
        };
        int i = this.mScreenOn ? 32 : 500;
        if (DEBUG) {
            Log.d("ScrimController", "Fading out scrims with delay: " + i);
        }
        this.mHandler.postDelayed(this.mBlankingTransitionRunnable, (long) i);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$blankDisplay$3() {
        this.mBlankingTransitionRunnable = null;
        this.mPendingFrameCallback = null;
        this.mBlankScreen = false;
        updateScrims();
    }

    @VisibleForTesting
    protected void doOnTheNextFrame(Runnable runnable) {
        this.mScrimBehind.postOnAnimationDelayed(runnable, 32);
    }

    public void setScrimBehindChangeRunnable(Runnable runnable) {
        ScrimView scrimView = this.mScrimBehind;
        if (scrimView == null) {
            this.mScrimBehindChangeRunnable = runnable;
        } else {
            scrimView.setChangeRunnable(runnable, this.mMainExecutor);
        }
    }

    private void updateThemeColors() {
        ScrimView scrimView = this.mScrimBehind;
        if (scrimView != null) {
            int defaultColor = Utils.getColorAttr(scrimView.getContext(), 16844002).getDefaultColor();
            int defaultColor2 = Utils.getColorAccent(this.mScrimBehind.getContext()).getDefaultColor();
            this.mColors.setMainColor(defaultColor);
            this.mColors.setSecondaryColor(defaultColor2);
            ColorExtractor.GradientColors gradientColors = this.mColors;
            gradientColors.setSupportsDarkText(ColorUtils.calculateContrast(gradientColors.getMainColor(), -1) > 4.5d);
            this.mNeedsDrawableColorUpdate = true;
        }
    }

    /* access modifiers changed from: private */
    public void onThemeChanged() {
        updateThemeColors();
        scheduleUpdate();
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println(" ScrimController: ");
        printWriter.print("  state: ");
        printWriter.println(this.mState);
        printWriter.print("  frontScrim:");
        printWriter.print(" viewAlpha=");
        printWriter.print(this.mScrimInFront.getViewAlpha());
        printWriter.print(" alpha=");
        printWriter.print(this.mInFrontAlpha);
        printWriter.print(" tint=0x");
        printWriter.println(Integer.toHexString(this.mScrimInFront.getTint()));
        printWriter.print("  behindScrim:");
        printWriter.print(" viewAlpha=");
        printWriter.print(this.mScrimBehind.getViewAlpha());
        printWriter.print(" alpha=");
        printWriter.print(this.mBehindAlpha);
        printWriter.print(" tint=0x");
        printWriter.println(Integer.toHexString(this.mScrimBehind.getTint()));
        printWriter.print("  notificationsScrim:");
        printWriter.print(" viewAlpha=");
        printWriter.print(this.mNotificationsScrim.getViewAlpha());
        printWriter.print(" alpha=");
        printWriter.print(this.mNotificationsAlpha);
        printWriter.print(" tint=0x");
        printWriter.println(Integer.toHexString(this.mNotificationsScrim.getTint()));
        printWriter.print("  bubbleScrim:");
        printWriter.print(" viewAlpha=");
        printWriter.print(this.mScrimForBubble.getViewAlpha());
        printWriter.print(" alpha=");
        printWriter.print(this.mBubbleAlpha);
        printWriter.print(" tint=0x");
        printWriter.println(Integer.toHexString(this.mScrimForBubble.getTint()));
        printWriter.print("  mTracking=");
        printWriter.println(this.mTracking);
        printWriter.print("  mDefaultScrimAlpha=");
        printWriter.println(this.mDefaultScrimAlpha);
        printWriter.print("  mExpansionFraction=");
        printWriter.println(this.mPanelExpansion);
        printWriter.print("  mState.getMaxLightRevealScrimAlpha=");
        printWriter.println(this.mState.getMaxLightRevealScrimAlpha());
    }

    public void setWallpaperSupportsAmbientMode(boolean z) {
        this.mWallpaperSupportsAmbientMode = z;
        for (ScrimState scrimState : ScrimState.values()) {
            scrimState.setWallpaperSupportsAmbientMode(z);
        }
    }

    public void onScreenTurnedOn() {
        this.mScreenOn = true;
        if (this.mHandler.hasCallbacks(this.mBlankingTransitionRunnable)) {
            if (DEBUG) {
                Log.d("ScrimController", "Shorter blanking because screen turned on. All good.");
            }
            this.mHandler.removeCallbacks(this.mBlankingTransitionRunnable);
            this.mBlankingTransitionRunnable.run();
        }
    }

    public void onScreenTurnedOff() {
        this.mScreenOn = false;
    }

    public void setExpansionAffectsAlpha(boolean z) {
        this.mExpansionAffectsAlpha = z;
        if (z) {
            applyAndDispatchState();
        }
    }

    public void setKeyguardOccluded(boolean z) {
        this.mKeyguardOccluded = z;
        updateScrims();
    }

    public void setHasBackdrop(boolean z) {
        for (ScrimState scrimState : ScrimState.values()) {
            scrimState.setHasBackdrop(z);
        }
        ScrimState scrimState2 = this.mState;
        if (scrimState2 == ScrimState.AOD || scrimState2 == ScrimState.PULSING) {
            float behindAlpha = scrimState2.getBehindAlpha();
            if (Float.isNaN(behindAlpha)) {
                throw new IllegalStateException("Scrim opacity is NaN for state: " + this.mState + ", back: " + this.mBehindAlpha);
            } else if (this.mBehindAlpha != behindAlpha) {
                this.mBehindAlpha = behindAlpha;
                updateScrims();
            }
        }
    }

    /* access modifiers changed from: private */
    public void setKeyguardFadingAway(boolean z, long j) {
        for (ScrimState scrimState : ScrimState.values()) {
            scrimState.setKeyguardFadingAway(z, j);
        }
    }

    public void setLaunchingAffordanceWithPreview(boolean z) {
        for (ScrimState scrimState : ScrimState.values()) {
            scrimState.setLaunchingAffordanceWithPreview(z);
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class KeyguardVisibilityCallback extends KeyguardUpdateMonitorCallback {
        private KeyguardVisibilityCallback() {
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onKeyguardVisibilityChanged(boolean z) {
            ScrimController.this.mNeedsDrawableColorUpdate = true;
            ScrimController.this.scheduleUpdate();
        }
    }
}
