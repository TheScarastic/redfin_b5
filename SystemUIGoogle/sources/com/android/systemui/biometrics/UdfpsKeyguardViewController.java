package com.android.systemui.biometrics;

import android.content.res.Configuration;
import android.util.MathUtils;
import android.view.MotionEvent;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.R$string;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.StatusBarState;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public class UdfpsKeyguardViewController extends UdfpsAnimationViewController<UdfpsKeyguardView> {
    private final ConfigurationController mConfigurationController;
    private final DelayableExecutor mExecutor;
    private boolean mFaceDetectRunning;
    private float mInputBouncerHiddenAmount;
    private boolean mIsBouncerVisible;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final StatusBarKeyguardViewManager mKeyguardViewManager;
    private final KeyguardViewMediator mKeyguardViewMediator;
    private float mLastDozeAmount;
    private final LockscreenShadeTransitionController mLockScreenShadeTransitionController;
    private boolean mQsExpanded;
    private boolean mShowingUdfpsBouncer;
    private int mStatusBarState;
    private float mTransitionToFullShadeProgress;
    private final UdfpsController mUdfpsController;
    private boolean mUdfpsRequested;
    private final StatusBarStateController.StateListener mStateListener = new StatusBarStateController.StateListener() { // from class: com.android.systemui.biometrics.UdfpsKeyguardViewController.1
        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public void onDozeAmountChanged(float f, float f2) {
            if (UdfpsKeyguardViewController.this.mLastDozeAmount < f) {
                UdfpsKeyguardViewController.this.showUdfpsBouncer(false);
            }
            ((UdfpsKeyguardView) ((ViewController) UdfpsKeyguardViewController.this).mView).onDozeAmountChanged(f, f2);
            UdfpsKeyguardViewController.this.mLastDozeAmount = f;
            UdfpsKeyguardViewController.this.updatePauseAuth();
        }

        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public void onStateChanged(int i) {
            UdfpsKeyguardViewController.this.mStatusBarState = i;
            ((UdfpsKeyguardView) ((ViewController) UdfpsKeyguardViewController.this).mView).setStatusBarState(i);
            UdfpsKeyguardViewController.this.updatePauseAuth();
        }
    };
    private final StatusBarKeyguardViewManager.AlternateAuthInterceptor mAlternateAuthInterceptor = new StatusBarKeyguardViewManager.AlternateAuthInterceptor() { // from class: com.android.systemui.biometrics.UdfpsKeyguardViewController.2
        @Override // com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.AlternateAuthInterceptor
        public boolean isAnimating() {
            return false;
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.AlternateAuthInterceptor
        public boolean showAlternateAuthBouncer() {
            return UdfpsKeyguardViewController.this.showUdfpsBouncer(true);
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.AlternateAuthInterceptor
        public boolean hideAlternateAuthBouncer() {
            return UdfpsKeyguardViewController.this.showUdfpsBouncer(false);
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.AlternateAuthInterceptor
        public boolean isShowingAlternateAuthBouncer() {
            return UdfpsKeyguardViewController.this.mShowingUdfpsBouncer;
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.AlternateAuthInterceptor
        public void requestUdfps(boolean z, int i) {
            UdfpsKeyguardViewController.this.mUdfpsRequested = z;
            ((UdfpsKeyguardView) ((ViewController) UdfpsKeyguardViewController.this).mView).requestUdfps(z, i);
            UdfpsKeyguardViewController.this.updatePauseAuth();
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.AlternateAuthInterceptor
        public void setQsExpanded(boolean z) {
            UdfpsKeyguardViewController.this.mQsExpanded = z;
            UdfpsKeyguardViewController.this.updatePauseAuth();
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.AlternateAuthInterceptor
        public boolean onTouch(MotionEvent motionEvent) {
            if (UdfpsKeyguardViewController.this.mTransitionToFullShadeProgress != 0.0f) {
                return false;
            }
            return UdfpsKeyguardViewController.this.mUdfpsController.onTouch(motionEvent);
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.AlternateAuthInterceptor
        public void setBouncerExpansionChanged(float f) {
            UdfpsKeyguardViewController.this.mInputBouncerHiddenAmount = f;
            UdfpsKeyguardViewController.this.updateAlpha();
            UdfpsKeyguardViewController.this.updatePauseAuth();
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.AlternateAuthInterceptor
        public void onBouncerVisibilityChanged() {
            UdfpsKeyguardViewController udfpsKeyguardViewController = UdfpsKeyguardViewController.this;
            udfpsKeyguardViewController.mIsBouncerVisible = udfpsKeyguardViewController.mKeyguardViewManager.isBouncerShowing();
            if (!UdfpsKeyguardViewController.this.mIsBouncerVisible) {
                UdfpsKeyguardViewController.this.mInputBouncerHiddenAmount = 1.0f;
            } else if (UdfpsKeyguardViewController.this.mKeyguardViewManager.isBouncerShowing()) {
                UdfpsKeyguardViewController.this.mInputBouncerHiddenAmount = 0.0f;
            }
            UdfpsKeyguardViewController.this.updateAlpha();
            UdfpsKeyguardViewController.this.updatePauseAuth();
        }

        @Override // com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.AlternateAuthInterceptor
        public void dump(PrintWriter printWriter) {
            printWriter.println(UdfpsKeyguardViewController.this.getTag());
        }
    };
    private final ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.biometrics.UdfpsKeyguardViewController.3
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onUiModeChanged() {
            ((UdfpsKeyguardView) ((ViewController) UdfpsKeyguardViewController.this).mView).updateColor();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onThemeChanged() {
            ((UdfpsKeyguardView) ((ViewController) UdfpsKeyguardViewController.this).mView).updateColor();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onOverlayChanged() {
            ((UdfpsKeyguardView) ((ViewController) UdfpsKeyguardViewController.this).mView).updateColor();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onConfigChanged(Configuration configuration) {
            ((UdfpsKeyguardView) ((ViewController) UdfpsKeyguardViewController.this).mView).updateColor();
        }
    };

    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController
    String getTag() {
        return "UdfpsKeyguardViewController";
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController
    public boolean listenForTouchesOutsideView() {
        return true;
    }

    /* access modifiers changed from: protected */
    public UdfpsKeyguardViewController(UdfpsKeyguardView udfpsKeyguardView, StatusBarStateController statusBarStateController, StatusBar statusBar, StatusBarKeyguardViewManager statusBarKeyguardViewManager, KeyguardUpdateMonitor keyguardUpdateMonitor, DelayableExecutor delayableExecutor, DumpManager dumpManager, KeyguardViewMediator keyguardViewMediator, LockscreenShadeTransitionController lockscreenShadeTransitionController, ConfigurationController configurationController, UdfpsController udfpsController) {
        super(udfpsKeyguardView, statusBarStateController, statusBar, dumpManager);
        this.mKeyguardViewManager = statusBarKeyguardViewManager;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mExecutor = delayableExecutor;
        this.mKeyguardViewMediator = keyguardViewMediator;
        this.mLockScreenShadeTransitionController = lockscreenShadeTransitionController;
        this.mConfigurationController = configurationController;
        this.mUdfpsController = udfpsController;
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController, com.android.systemui.util.ViewController
    public void onViewAttached() {
        super.onViewAttached();
        float dozeAmount = this.mStatusBarStateController.getDozeAmount();
        this.mLastDozeAmount = dozeAmount;
        this.mStateListener.onDozeAmountChanged(dozeAmount, dozeAmount);
        this.mStatusBarStateController.addCallback(this.mStateListener);
        this.mUdfpsRequested = false;
        this.mStatusBarState = this.mStatusBarStateController.getState();
        this.mQsExpanded = this.mKeyguardViewManager.isQsExpanded();
        this.mInputBouncerHiddenAmount = 1.0f;
        this.mIsBouncerVisible = this.mKeyguardViewManager.bouncerIsOrWillBeShowing();
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        updateAlpha();
        updatePauseAuth();
        this.mKeyguardViewManager.setAlternateAuthInterceptor(this.mAlternateAuthInterceptor);
        this.mLockScreenShadeTransitionController.setUdfpsKeyguardViewController(this);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController, com.android.systemui.util.ViewController
    public void onViewDetached() {
        super.onViewDetached();
        this.mFaceDetectRunning = false;
        this.mStatusBarStateController.removeCallback(this.mStateListener);
        this.mKeyguardViewManager.removeAlternateAuthInterceptor(this.mAlternateAuthInterceptor);
        this.mKeyguardUpdateMonitor.requestFaceAuthOnOccludingApp(false);
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        if (this.mLockScreenShadeTransitionController.getUdfpsKeyguardViewController() == this) {
            this.mLockScreenShadeTransitionController.setUdfpsKeyguardViewController(null);
        }
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController, com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(fileDescriptor, printWriter, strArr);
        printWriter.println("mShowingUdfpsBouncer=" + this.mShowingUdfpsBouncer);
        printWriter.println("mFaceDetectRunning=" + this.mFaceDetectRunning);
        printWriter.println("mStatusBarState=" + StatusBarState.toShortString(this.mStatusBarState));
        printWriter.println("mQsExpanded=" + this.mQsExpanded);
        printWriter.println("mIsBouncerVisible=" + this.mIsBouncerVisible);
        printWriter.println("mInputBouncerHiddenAmount=" + this.mInputBouncerHiddenAmount);
        printWriter.println("mAlpha=" + ((UdfpsKeyguardView) this.mView).getAlpha());
        printWriter.println("mUdfpsRequested=" + this.mUdfpsRequested);
        printWriter.println("mView.mUdfpsRequested=" + ((UdfpsKeyguardView) this.mView).mUdfpsRequested);
    }

    /* access modifiers changed from: private */
    public boolean showUdfpsBouncer(boolean z) {
        if (this.mShowingUdfpsBouncer == z) {
            return false;
        }
        this.mShowingUdfpsBouncer = z;
        updatePauseAuth();
        if (this.mShowingUdfpsBouncer) {
            if (this.mStatusBarState == 2) {
                ((UdfpsKeyguardView) this.mView).animateInUdfpsBouncer(null);
            }
            if (this.mKeyguardViewManager.isOccluded()) {
                this.mKeyguardUpdateMonitor.requestFaceAuthOnOccludingApp(true);
            }
            T t = this.mView;
            ((UdfpsKeyguardView) t).announceForAccessibility(((UdfpsKeyguardView) t).getContext().getString(R$string.accessibility_fingerprint_bouncer));
        } else {
            this.mKeyguardUpdateMonitor.requestFaceAuthOnOccludingApp(false);
        }
        return true;
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController
    public boolean shouldPauseAuth() {
        if (this.mShowingUdfpsBouncer) {
            return false;
        }
        if (this.mUdfpsRequested && !this.mNotificationShadeExpanded && (!this.mIsBouncerVisible || this.mInputBouncerHiddenAmount != 0.0f)) {
            return false;
        }
        if (this.mStatusBarState == 1 && !this.mQsExpanded && this.mInputBouncerHiddenAmount >= 0.5f && !this.mIsBouncerVisible) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController
    public void onTouchOutsideView() {
        maybeShowInputBouncer();
    }

    private void maybeShowInputBouncer() {
        if (this.mShowingUdfpsBouncer) {
            this.mKeyguardViewManager.showBouncer(true);
            this.mKeyguardViewManager.resetAlternateAuth(false);
        }
    }

    public void setTransitionToFullShadeProgress(float f) {
        this.mTransitionToFullShadeProgress = f;
        updateAlpha();
    }

    /* access modifiers changed from: private */
    public void updateAlpha() {
        int i;
        if (this.mShowingUdfpsBouncer) {
            i = 255;
        } else {
            i = (int) MathUtils.constrain(MathUtils.map(0.5f, 0.9f, 0.0f, 255.0f, this.mInputBouncerHiddenAmount), 0.0f, 255.0f);
        }
        ((UdfpsKeyguardView) this.mView).setUnpausedAlpha((int) (((float) i) * (1.0f - this.mTransitionToFullShadeProgress)));
    }
}
