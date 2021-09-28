package com.android.keyguard;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.biometrics.BiometricSourceType;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.media.AudioAttributes;
import android.os.Process;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.systemui.Dumpable;
import com.android.systemui.R$anim;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.StatusBarState;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
/* loaded from: classes.dex */
public class LockIconViewController extends ViewController<LockIconView> implements Dumpable {
    private static final AudioAttributes VIBRATION_SONIFICATION_ATTRIBUTES = new AudioAttributes.Builder().setContentType(4).setUsage(13).build();
    private static final float sDefaultDensity;
    private static final float sDistAboveKgBottomAreaPx;
    private static final int sLockIconRadiusPx;
    private final AccessibilityManager mAccessibilityManager;
    private final AuthController mAuthController;
    private int mBottomPadding;
    private boolean mCanDismissLockScreen;
    private Runnable mCancelDelayedUpdateVisibilityRunnable;
    private final ConfigurationController mConfigurationController;
    private boolean mDetectedLongPress;
    private boolean mDownDetected;
    private final DelayableExecutor mExecutor;
    private final FalsingManager mFalsingManager;
    private float mHeightPixels;
    private boolean mIsBouncerShowing;
    private boolean mIsDozing;
    private boolean mIsKeyguardShowing;
    private final KeyguardStateController mKeyguardStateController;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final KeyguardViewController mKeyguardViewController;
    private final Drawable mLockIcon;
    private final AnimatedVectorDrawable mLockToUnlockIcon;
    private CharSequence mLockedLabel;
    private boolean mQsExpanded;
    private boolean mRunningFPS;
    private boolean mShowLockIcon;
    private boolean mShowUnlockIcon;
    private int mStatusBarState;
    private final StatusBarStateController mStatusBarStateController;
    private boolean mUdfpsEnrolled;
    private boolean mUdfpsSupported;
    private CharSequence mUnlockedLabel;
    private boolean mUserUnlockedWithBiometric;
    private final Vibrator mVibrator;
    private float mWidthPixels;
    private final Rect mSensorTouchLocation = new Rect();
    private final View.AccessibilityDelegate mAccessibilityDelegate = new View.AccessibilityDelegate() { // from class: com.android.keyguard.LockIconViewController.1
        private final AccessibilityNodeInfo.AccessibilityAction mAccessibilityAuthenticateHint;
        private final AccessibilityNodeInfo.AccessibilityAction mAccessibilityEnterHint;

        {
            this.mAccessibilityAuthenticateHint = new AccessibilityNodeInfo.AccessibilityAction(16, LockIconViewController.this.getResources().getString(R$string.accessibility_authenticate_hint));
            this.mAccessibilityEnterHint = new AccessibilityNodeInfo.AccessibilityAction(16, LockIconViewController.this.getResources().getString(R$string.accessibility_enter_hint));
        }

        @Override // android.view.View.AccessibilityDelegate
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            if (!LockIconViewController.this.isClickable()) {
                return;
            }
            if (LockIconViewController.this.mShowLockIcon) {
                accessibilityNodeInfo.addAction(this.mAccessibilityAuthenticateHint);
            } else if (LockIconViewController.this.mShowUnlockIcon) {
                accessibilityNodeInfo.addAction(this.mAccessibilityEnterHint);
            }
        }
    };
    private StatusBarStateController.StateListener mStatusBarStateListener = new StatusBarStateController.StateListener() { // from class: com.android.keyguard.LockIconViewController.2
        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public void onDozingChanged(boolean z) {
            LockIconViewController.this.mIsDozing = z;
            LockIconViewController.this.updateVisibility();
        }

        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public void onStateChanged(int i) {
            LockIconViewController.this.mStatusBarState = i;
            LockIconViewController.this.updateVisibility();
        }
    };
    private final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.keyguard.LockIconViewController.3
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onKeyguardVisibilityChanged(boolean z) {
            LockIconViewController lockIconViewController = LockIconViewController.this;
            lockIconViewController.mIsBouncerShowing = lockIconViewController.mKeyguardViewController.isBouncerShowing();
            LockIconViewController.this.updateVisibility();
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onKeyguardBouncerChanged(boolean z) {
            LockIconViewController.this.mIsBouncerShowing = z;
            LockIconViewController.this.updateVisibility();
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onBiometricRunningStateChanged(boolean z, BiometricSourceType biometricSourceType) {
            LockIconViewController lockIconViewController = LockIconViewController.this;
            lockIconViewController.mUserUnlockedWithBiometric = lockIconViewController.mKeyguardUpdateMonitor.getUserUnlockedWithBiometric(KeyguardUpdateMonitor.getCurrentUser());
            if (biometricSourceType == BiometricSourceType.FINGERPRINT) {
                LockIconViewController.this.mRunningFPS = z;
                if (!LockIconViewController.this.mRunningFPS) {
                    if (LockIconViewController.this.mCancelDelayedUpdateVisibilityRunnable != null) {
                        LockIconViewController.this.mCancelDelayedUpdateVisibilityRunnable.run();
                    }
                    LockIconViewController lockIconViewController2 = LockIconViewController.this;
                    lockIconViewController2.mCancelDelayedUpdateVisibilityRunnable = lockIconViewController2.mExecutor.executeDelayed(new LockIconViewController$3$$ExternalSyntheticLambda0(this), 50);
                    return;
                }
                LockIconViewController.this.updateVisibility();
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBiometricRunningStateChanged$0() {
            LockIconViewController.this.updateVisibility();
        }
    };
    private final KeyguardStateController.Callback mKeyguardStateCallback = new KeyguardStateController.Callback() { // from class: com.android.keyguard.LockIconViewController.4
        @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
        public void onUnlockedChanged() {
            LockIconViewController lockIconViewController = LockIconViewController.this;
            lockIconViewController.mCanDismissLockScreen = lockIconViewController.mKeyguardStateController.canDismissLockScreen();
            LockIconViewController.this.updateKeyguardShowing();
            LockIconViewController.this.updateVisibility();
        }

        @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
        public void onKeyguardShowingChanged() {
            LockIconViewController lockIconViewController = LockIconViewController.this;
            lockIconViewController.mCanDismissLockScreen = lockIconViewController.mKeyguardStateController.canDismissLockScreen();
            LockIconViewController.this.updateKeyguardShowing();
            if (LockIconViewController.this.mIsKeyguardShowing) {
                LockIconViewController lockIconViewController2 = LockIconViewController.this;
                lockIconViewController2.mUserUnlockedWithBiometric = lockIconViewController2.mKeyguardUpdateMonitor.getUserUnlockedWithBiometric(KeyguardUpdateMonitor.getCurrentUser());
            }
            LockIconViewController lockIconViewController3 = LockIconViewController.this;
            lockIconViewController3.mUdfpsEnrolled = lockIconViewController3.mKeyguardUpdateMonitor.isUdfpsEnrolled();
            LockIconViewController.this.updateVisibility();
        }

        @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
        public void onKeyguardFadingAwayChanged() {
            LockIconViewController.this.updateKeyguardShowing();
            LockIconViewController.this.updateVisibility();
        }
    };
    private final ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.keyguard.LockIconViewController.5
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onUiModeChanged() {
            LockIconViewController.this.updateColors();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onThemeChanged() {
            LockIconViewController.this.updateColors();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onOverlayChanged() {
            LockIconViewController.this.updateColors();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onConfigChanged(Configuration configuration) {
            LockIconViewController.this.updateConfiguration();
            LockIconViewController.this.updateColors();
        }
    };
    private final GestureDetector mGestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() { // from class: com.android.keyguard.LockIconViewController.6
        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            LockIconViewController.this.mDetectedLongPress = false;
            if (!LockIconViewController.this.isClickable()) {
                LockIconViewController.this.mDownDetected = false;
                return false;
            }
            LockIconViewController.this.mDownDetected = true;
            if (LockIconViewController.this.mVibrator != null) {
                LockIconViewController.this.mVibrator.vibrate(Process.myUid(), LockIconViewController.this.getContext().getOpPackageName(), UdfpsController.EFFECT_CLICK, "lockIcon-onDown", LockIconViewController.VIBRATION_SONIFICATION_ATTRIBUTES);
            }
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
            if (wasClickableOnDownEvent()) {
                if (LockIconViewController.this.mVibrator != null) {
                    LockIconViewController.this.mVibrator.vibrate(Process.myUid(), LockIconViewController.this.getContext().getOpPackageName(), UdfpsController.EFFECT_CLICK, "lockIcon-onLongPress", LockIconViewController.VIBRATION_SONIFICATION_ATTRIBUTES);
                }
                LockIconViewController.this.mDetectedLongPress = true;
                onAffordanceClick();
            }
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            if (!wasClickableOnDownEvent()) {
                return false;
            }
            onAffordanceClick();
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (!wasClickableOnDownEvent()) {
                return false;
            }
            onAffordanceClick();
            return true;
        }

        private boolean wasClickableOnDownEvent() {
            return LockIconViewController.this.mDownDetected;
        }

        private void onAffordanceClick() {
            if (!LockIconViewController.this.mFalsingManager.isFalseTouch(14)) {
                LockIconViewController.this.mIsBouncerShowing = true;
                LockIconViewController.this.updateVisibility();
                LockIconViewController.this.mKeyguardViewController.showBouncer(true);
            }
        }
    });
    private final Drawable mUnlockIcon = ((LockIconView) this.mView).getContext().getResources().getDrawable(R$drawable.ic_unlock, ((LockIconView) this.mView).getContext().getTheme());
    private final AnimatedVectorDrawable mFpToUnlockIcon = (AnimatedVectorDrawable) ((LockIconView) this.mView).getContext().getResources().getDrawable(R$anim.fp_to_unlock, ((LockIconView) this.mView).getContext().getTheme());

    static {
        float f = ((float) DisplayMetrics.DENSITY_DEVICE_STABLE) / 160.0f;
        sDefaultDensity = f;
        sLockIconRadiusPx = (int) (36.0f * f);
        sDistAboveKgBottomAreaPx = f * 12.0f;
    }

    public LockIconViewController(LockIconView lockIconView, StatusBarStateController statusBarStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardViewController keyguardViewController, KeyguardStateController keyguardStateController, FalsingManager falsingManager, AuthController authController, DumpManager dumpManager, AccessibilityManager accessibilityManager, ConfigurationController configurationController, DelayableExecutor delayableExecutor, Vibrator vibrator) {
        super(lockIconView);
        this.mStatusBarStateController = statusBarStateController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mAuthController = authController;
        this.mKeyguardViewController = keyguardViewController;
        this.mKeyguardStateController = keyguardStateController;
        this.mFalsingManager = falsingManager;
        this.mAccessibilityManager = accessibilityManager;
        this.mConfigurationController = configurationController;
        this.mExecutor = delayableExecutor;
        this.mVibrator = vibrator;
        Context context = lockIconView.getContext();
        Resources resources = ((LockIconView) this.mView).getContext().getResources();
        int i = R$anim.lock_to_unlock;
        this.mLockIcon = resources.getDrawable(i, ((LockIconView) this.mView).getContext().getTheme());
        this.mLockToUnlockIcon = (AnimatedVectorDrawable) ((LockIconView) this.mView).getContext().getResources().getDrawable(i, ((LockIconView) this.mView).getContext().getTheme());
        this.mUnlockedLabel = context.getResources().getString(R$string.accessibility_unlock_button);
        this.mLockedLabel = context.getResources().getString(R$string.accessibility_lock_icon);
        dumpManager.registerDumpable("LockIconViewController", this);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.util.ViewController
    public void onInit() {
        ((LockIconView) this.mView).setAccessibilityDelegate(this.mAccessibilityDelegate);
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewAttached() {
        this.mUdfpsSupported = this.mAuthController.getUdfpsSensorLocation() != null;
        updateConfiguration();
        updateKeyguardShowing();
        this.mUserUnlockedWithBiometric = false;
        this.mIsBouncerShowing = this.mKeyguardViewController.isBouncerShowing();
        this.mIsDozing = this.mStatusBarStateController.isDozing();
        this.mRunningFPS = this.mKeyguardUpdateMonitor.isFingerprintDetectionRunning();
        this.mCanDismissLockScreen = this.mKeyguardStateController.canDismissLockScreen();
        this.mStatusBarState = this.mStatusBarStateController.getState();
        updateColors();
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
        this.mStatusBarStateController.addCallback(this.mStatusBarStateListener);
        this.mKeyguardStateController.addCallback(this.mKeyguardStateCallback);
        this.mDownDetected = false;
        updateVisibility();
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewDetached() {
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardUpdateMonitorCallback);
        this.mStatusBarStateController.removeCallback(this.mStatusBarStateListener);
        this.mKeyguardStateController.removeCallback(this.mKeyguardStateCallback);
        Runnable runnable = this.mCancelDelayedUpdateVisibilityRunnable;
        if (runnable != null) {
            runnable.run();
            this.mCancelDelayedUpdateVisibilityRunnable = null;
        }
    }

    public float getTop() {
        return ((LockIconView) this.mView).getLocationTop();
    }

    public void setQsExpanded(boolean z) {
        this.mQsExpanded = z;
        updateVisibility();
    }

    /* access modifiers changed from: private */
    public void updateVisibility() {
        Runnable runnable = this.mCancelDelayedUpdateVisibilityRunnable;
        if (runnable != null) {
            runnable.run();
            this.mCancelDelayedUpdateVisibilityRunnable = null;
        }
        if (!this.mIsKeyguardShowing) {
            ((LockIconView) this.mView).setVisibility(4);
            return;
        }
        boolean z = true;
        boolean z2 = this.mUdfpsEnrolled && !this.mShowUnlockIcon && !this.mShowLockIcon;
        boolean z3 = this.mShowLockIcon;
        boolean z4 = this.mShowUnlockIcon;
        this.mShowLockIcon = !this.mCanDismissLockScreen && !this.mUserUnlockedWithBiometric && isLockScreen() && (!this.mUdfpsEnrolled || !this.mRunningFPS);
        if (!this.mCanDismissLockScreen || !isLockScreen()) {
            z = false;
        }
        this.mShowUnlockIcon = z;
        CharSequence contentDescription = ((LockIconView) this.mView).getContentDescription();
        if (this.mShowLockIcon) {
            ((LockIconView) this.mView).setImageDrawable(this.mLockIcon);
            ((LockIconView) this.mView).setVisibility(0);
            ((LockIconView) this.mView).setContentDescription(this.mLockedLabel);
        } else if (this.mShowUnlockIcon) {
            if (!z4) {
                if (z2) {
                    ((LockIconView) this.mView).setImageDrawable(this.mFpToUnlockIcon);
                    this.mFpToUnlockIcon.forceAnimationOnUI();
                    this.mFpToUnlockIcon.start();
                } else if (z3) {
                    ((LockIconView) this.mView).setImageDrawable(this.mLockToUnlockIcon);
                    this.mLockToUnlockIcon.forceAnimationOnUI();
                    this.mLockToUnlockIcon.start();
                } else {
                    ((LockIconView) this.mView).setImageDrawable(this.mUnlockIcon);
                }
            }
            ((LockIconView) this.mView).setVisibility(0);
            ((LockIconView) this.mView).setContentDescription(this.mUnlockedLabel);
        } else {
            ((LockIconView) this.mView).setVisibility(4);
            ((LockIconView) this.mView).setContentDescription(null);
        }
        if (!Objects.equals(contentDescription, ((LockIconView) this.mView).getContentDescription()) && ((LockIconView) this.mView).getContentDescription() != null) {
            T t = this.mView;
            ((LockIconView) t).announceForAccessibility(((LockIconView) t).getContentDescription());
        }
    }

    private boolean isLockScreen() {
        return !this.mIsDozing && !this.mIsBouncerShowing && !this.mQsExpanded && this.mStatusBarState == 1;
    }

    /* access modifiers changed from: private */
    public void updateKeyguardShowing() {
        this.mIsKeyguardShowing = this.mKeyguardStateController.isShowing() && !this.mKeyguardStateController.isKeyguardGoingAway();
    }

    /* access modifiers changed from: private */
    public void updateColors() {
        ((LockIconView) this.mView).updateColorAndBackgroundVisibility(this.mUdfpsSupported);
    }

    /* access modifiers changed from: private */
    public void updateConfiguration() {
        DisplayMetrics displayMetrics = ((LockIconView) this.mView).getContext().getResources().getDisplayMetrics();
        this.mWidthPixels = (float) displayMetrics.widthPixels;
        this.mHeightPixels = (float) displayMetrics.heightPixels;
        this.mBottomPadding = ((LockIconView) this.mView).getContext().getResources().getDimensionPixelSize(R$dimen.lock_icon_margin_bottom);
        this.mUnlockedLabel = ((LockIconView) this.mView).getContext().getResources().getString(R$string.accessibility_unlock_button);
        this.mLockedLabel = ((LockIconView) this.mView).getContext().getResources().getString(R$string.accessibility_lock_icon);
        updateLockIconLocation();
    }

    private void updateLockIconLocation() {
        if (this.mUdfpsSupported) {
            FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal = this.mAuthController.getUdfpsProps().get(0);
            ((LockIconView) this.mView).setCenterLocation(new PointF((float) fingerprintSensorPropertiesInternal.sensorLocationX, (float) fingerprintSensorPropertiesInternal.sensorLocationY), fingerprintSensorPropertiesInternal.sensorRadius);
        } else {
            float f = (this.mHeightPixels - ((float) this.mBottomPadding)) - sDistAboveKgBottomAreaPx;
            int i = sLockIconRadiusPx;
            ((LockIconView) this.mView).setCenterLocation(new PointF(this.mWidthPixels / 2.0f, f - ((float) i)), i);
        }
        ((LockIconView) this.mView).getHitRect(this.mSensorTouchLocation);
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("mUdfpsEnrolled: " + this.mUdfpsEnrolled);
        printWriter.println("mIsKeyguardShowing: " + this.mIsKeyguardShowing);
        printWriter.println(" mShowUnlockIcon: " + this.mShowUnlockIcon);
        printWriter.println(" mShowLockIcon: " + this.mShowLockIcon);
        printWriter.println("  mIsDozing: " + this.mIsDozing);
        printWriter.println("  mIsBouncerShowing: " + this.mIsBouncerShowing);
        printWriter.println("  mUserUnlockedWithBiometric: " + this.mUserUnlockedWithBiometric);
        printWriter.println("  mRunningFPS: " + this.mRunningFPS);
        printWriter.println("  mCanDismissLockScreen: " + this.mCanDismissLockScreen);
        printWriter.println("  mStatusBarState: " + StatusBarState.toShortString(this.mStatusBarState));
        printWriter.println("  mQsExpanded: " + this.mQsExpanded);
        T t = this.mView;
        if (t != 0) {
            ((LockIconView) t).dump(fileDescriptor, printWriter, strArr);
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mSensorTouchLocation.contains((int) motionEvent.getX(), (int) motionEvent.getY()) && ((LockIconView) this.mView).getVisibility() == 0) {
            this.mGestureDetector.onTouchEvent(motionEvent);
        }
        if (!this.mDownDetected || !this.mDetectedLongPress) {
            return false;
        }
        if (motionEvent.getAction() == 3 || motionEvent.getAction() == 1) {
            this.mDownDetected = false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public boolean isClickable() {
        return this.mUdfpsSupported || this.mShowUnlockIcon;
    }

    public void setAlpha(float f) {
        ((LockIconView) this.mView).setAlpha(f);
    }
}
