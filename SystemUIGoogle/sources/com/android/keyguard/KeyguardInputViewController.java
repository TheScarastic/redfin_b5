package com.android.keyguard;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.KeyguardInputView;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.systemui.R$id;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.concurrency.DelayableExecutor;
/* loaded from: classes.dex */
public abstract class KeyguardInputViewController<T extends KeyguardInputView> extends ViewController<T> implements KeyguardSecurityView {
    private final EmergencyButton mEmergencyButton;
    private final EmergencyButtonController mEmergencyButtonController;
    private final KeyguardSecurityCallback mKeyguardSecurityCallback;
    private KeyguardSecurityCallback mNullCallback = new KeyguardSecurityCallback() { // from class: com.android.keyguard.KeyguardInputViewController.1
        @Override // com.android.keyguard.KeyguardSecurityCallback
        public void dismiss(boolean z, int i) {
        }

        @Override // com.android.keyguard.KeyguardSecurityCallback
        public void dismiss(boolean z, int i, boolean z2) {
        }

        @Override // com.android.keyguard.KeyguardSecurityCallback
        public void onUserInput() {
        }

        @Override // com.android.keyguard.KeyguardSecurityCallback
        public void reportUnlockAttempt(int i, boolean z, int i2) {
        }

        @Override // com.android.keyguard.KeyguardSecurityCallback
        public void reset() {
        }

        @Override // com.android.keyguard.KeyguardSecurityCallback
        public void userActivity() {
        }
    };
    private boolean mPaused;
    private final KeyguardSecurityModel.SecurityMode mSecurityMode;

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.util.ViewController
    public void onViewAttached() {
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.util.ViewController
    public void onViewDetached() {
    }

    public void reset() {
    }

    public void showMessage(CharSequence charSequence, ColorStateList colorStateList) {
    }

    public void showPromptReason(int i) {
    }

    /* access modifiers changed from: protected */
    public KeyguardInputViewController(T t, KeyguardSecurityModel.SecurityMode securityMode, KeyguardSecurityCallback keyguardSecurityCallback, EmergencyButtonController emergencyButtonController) {
        super(t);
        EmergencyButton emergencyButton;
        this.mSecurityMode = securityMode;
        this.mKeyguardSecurityCallback = keyguardSecurityCallback;
        if (t == null) {
            emergencyButton = null;
        } else {
            emergencyButton = (EmergencyButton) t.findViewById(R$id.emergency_call_button);
        }
        this.mEmergencyButton = emergencyButton;
        this.mEmergencyButtonController = emergencyButtonController;
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.util.ViewController
    public void onInit() {
        this.mEmergencyButtonController.init();
    }

    /* access modifiers changed from: package-private */
    public KeyguardSecurityModel.SecurityMode getSecurityMode() {
        return this.mSecurityMode;
    }

    /* access modifiers changed from: protected */
    public KeyguardSecurityCallback getKeyguardSecurityCallback() {
        if (this.mPaused) {
            return this.mNullCallback;
        }
        return this.mKeyguardSecurityCallback;
    }

    public void onPause() {
        this.mPaused = true;
    }

    public void onResume(int i) {
        this.mPaused = false;
    }

    public void reloadColors() {
        EmergencyButton emergencyButton = this.mEmergencyButton;
        if (emergencyButton != null) {
            emergencyButton.reloadColors();
        }
    }

    public void startAppearAnimation() {
        ((KeyguardInputView) this.mView).startAppearAnimation();
    }

    public boolean startDisappearAnimation(Runnable runnable) {
        return ((KeyguardInputView) this.mView).startDisappearAnimation(runnable);
    }

    public int getIndexIn(KeyguardSecurityViewFlipper keyguardSecurityViewFlipper) {
        return keyguardSecurityViewFlipper.indexOfChild(this.mView);
    }

    /* loaded from: classes.dex */
    public static class Factory {
        private final EmergencyButtonController.Factory mEmergencyButtonControllerFactory;
        private final FalsingCollector mFalsingCollector;
        private final InputMethodManager mInputMethodManager;
        private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
        private final LatencyTracker mLatencyTracker;
        private final LiftToActivateListener mLiftToActivateListener;
        private final LockPatternUtils mLockPatternUtils;
        private final DelayableExecutor mMainExecutor;
        private final KeyguardMessageAreaController.Factory mMessageAreaControllerFactory;
        private final Resources mResources;
        private final TelephonyManager mTelephonyManager;

        public Factory(KeyguardUpdateMonitor keyguardUpdateMonitor, LockPatternUtils lockPatternUtils, LatencyTracker latencyTracker, KeyguardMessageAreaController.Factory factory, InputMethodManager inputMethodManager, DelayableExecutor delayableExecutor, Resources resources, LiftToActivateListener liftToActivateListener, TelephonyManager telephonyManager, FalsingCollector falsingCollector, EmergencyButtonController.Factory factory2) {
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
            this.mLockPatternUtils = lockPatternUtils;
            this.mLatencyTracker = latencyTracker;
            this.mMessageAreaControllerFactory = factory;
            this.mInputMethodManager = inputMethodManager;
            this.mMainExecutor = delayableExecutor;
            this.mResources = resources;
            this.mLiftToActivateListener = liftToActivateListener;
            this.mTelephonyManager = telephonyManager;
            this.mEmergencyButtonControllerFactory = factory2;
            this.mFalsingCollector = falsingCollector;
        }

        public KeyguardInputViewController create(KeyguardInputView keyguardInputView, KeyguardSecurityModel.SecurityMode securityMode, KeyguardSecurityCallback keyguardSecurityCallback) {
            EmergencyButtonController create = this.mEmergencyButtonControllerFactory.create((EmergencyButton) keyguardInputView.findViewById(R$id.emergency_call_button));
            if (keyguardInputView instanceof KeyguardPatternView) {
                return new KeyguardPatternViewController((KeyguardPatternView) keyguardInputView, this.mKeyguardUpdateMonitor, securityMode, this.mLockPatternUtils, keyguardSecurityCallback, this.mLatencyTracker, this.mFalsingCollector, create, this.mMessageAreaControllerFactory);
            }
            if (keyguardInputView instanceof KeyguardPasswordView) {
                return new KeyguardPasswordViewController((KeyguardPasswordView) keyguardInputView, this.mKeyguardUpdateMonitor, securityMode, this.mLockPatternUtils, keyguardSecurityCallback, this.mMessageAreaControllerFactory, this.mLatencyTracker, this.mInputMethodManager, create, this.mMainExecutor, this.mResources, this.mFalsingCollector);
            }
            if (keyguardInputView instanceof KeyguardPINView) {
                return new KeyguardPinViewController((KeyguardPINView) keyguardInputView, this.mKeyguardUpdateMonitor, securityMode, this.mLockPatternUtils, keyguardSecurityCallback, this.mMessageAreaControllerFactory, this.mLatencyTracker, this.mLiftToActivateListener, create, this.mFalsingCollector);
            }
            if (keyguardInputView instanceof KeyguardSimPinView) {
                return new KeyguardSimPinViewController((KeyguardSimPinView) keyguardInputView, this.mKeyguardUpdateMonitor, securityMode, this.mLockPatternUtils, keyguardSecurityCallback, this.mMessageAreaControllerFactory, this.mLatencyTracker, this.mLiftToActivateListener, this.mTelephonyManager, this.mFalsingCollector, create);
            }
            if (keyguardInputView instanceof KeyguardSimPukView) {
                return new KeyguardSimPukViewController((KeyguardSimPukView) keyguardInputView, this.mKeyguardUpdateMonitor, securityMode, this.mLockPatternUtils, keyguardSecurityCallback, this.mMessageAreaControllerFactory, this.mLatencyTracker, this.mLiftToActivateListener, this.mTelephonyManager, this.mFalsingCollector, create);
            }
            throw new RuntimeException("Unable to find controller for " + keyguardInputView);
        }
    }
}
