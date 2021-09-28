package com.android.keyguard;

import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.KeyEvent;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternChecker;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.LockscreenCredential;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.KeyguardAbsKeyInputView;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.systemui.R$plurals;
import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.util.ViewController;
/* loaded from: classes.dex */
public abstract class KeyguardAbsKeyInputViewController<T extends KeyguardAbsKeyInputView> extends KeyguardInputViewController<T> {
    private CountDownTimer mCountdownTimer;
    private boolean mDismissing;
    private final EmergencyButtonController mEmergencyButtonController;
    private final FalsingCollector mFalsingCollector;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final LatencyTracker mLatencyTracker;
    private final LockPatternUtils mLockPatternUtils;
    protected KeyguardMessageAreaController mMessageAreaController;
    protected AsyncTask<?, ?, ?> mPendingLockCheck;
    protected boolean mResumed;
    private final KeyguardAbsKeyInputView.KeyDownListener mKeyDownListener = new KeyguardAbsKeyInputView.KeyDownListener() { // from class: com.android.keyguard.KeyguardAbsKeyInputViewController$$ExternalSyntheticLambda0
        @Override // com.android.keyguard.KeyguardAbsKeyInputView.KeyDownListener
        public final boolean onKeyDown(int i, KeyEvent keyEvent) {
            return KeyguardAbsKeyInputViewController.$r8$lambda$jwKsJ0xDLiDNB3cU1kMIoemzMw8(KeyguardAbsKeyInputViewController.this, i, keyEvent);
        }
    };
    private final EmergencyButtonController.EmergencyButtonCallback mEmergencyButtonCallback = new EmergencyButtonController.EmergencyButtonCallback() { // from class: com.android.keyguard.KeyguardAbsKeyInputViewController.1
        @Override // com.android.keyguard.EmergencyButtonController.EmergencyButtonCallback
        public void onEmergencyButtonClickedWhenInCall() {
            KeyguardAbsKeyInputViewController.this.getKeyguardSecurityCallback().reset();
        }
    };

    @Override // com.android.keyguard.KeyguardSecurityView
    public boolean needsInput() {
        return false;
    }

    abstract void resetState();

    protected boolean shouldLockout(long j) {
        return j != 0;
    }

    public /* synthetic */ boolean lambda$new$0(int i, KeyEvent keyEvent) {
        if (i == 0) {
            return false;
        }
        onUserInput();
        return false;
    }

    public KeyguardAbsKeyInputViewController(T t, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel.SecurityMode securityMode, LockPatternUtils lockPatternUtils, KeyguardSecurityCallback keyguardSecurityCallback, KeyguardMessageAreaController.Factory factory, LatencyTracker latencyTracker, FalsingCollector falsingCollector, EmergencyButtonController emergencyButtonController) {
        super(t, securityMode, keyguardSecurityCallback, emergencyButtonController);
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mLockPatternUtils = lockPatternUtils;
        this.mLatencyTracker = latencyTracker;
        this.mFalsingCollector = falsingCollector;
        this.mEmergencyButtonController = emergencyButtonController;
        this.mMessageAreaController = factory.create(KeyguardMessageArea.findSecurityMessageDisplay(this.mView));
    }

    @Override // com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    public void onInit() {
        super.onInit();
        this.mMessageAreaController.init();
    }

    @Override // com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    public void onViewAttached() {
        super.onViewAttached();
        ((KeyguardAbsKeyInputView) this.mView).setKeyDownListener(this.mKeyDownListener);
        ((KeyguardAbsKeyInputView) this.mView).setEnableHaptics(this.mLockPatternUtils.isTactileFeedbackEnabled());
        this.mEmergencyButtonController.setEmergencyButtonCallback(this.mEmergencyButtonCallback);
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public void reset() {
        this.mDismissing = false;
        ((KeyguardAbsKeyInputView) this.mView).resetPasswordText(false, false);
        long lockoutAttemptDeadline = this.mLockPatternUtils.getLockoutAttemptDeadline(KeyguardUpdateMonitor.getCurrentUser());
        if (shouldLockout(lockoutAttemptDeadline)) {
            handleAttemptLockout(lockoutAttemptDeadline);
        } else {
            resetState();
        }
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public void reloadColors() {
        super.reloadColors();
        this.mMessageAreaController.reloadColors();
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public void showMessage(CharSequence charSequence, ColorStateList colorStateList) {
        if (colorStateList != null) {
            this.mMessageAreaController.setNextMessageColor(colorStateList);
        }
        this.mMessageAreaController.setMessage(charSequence);
    }

    protected void handleAttemptLockout(long j) {
        ((KeyguardAbsKeyInputView) this.mView).setPasswordEntryEnabled(false);
        this.mCountdownTimer = new CountDownTimer(((long) Math.ceil(((double) (j - SystemClock.elapsedRealtime())) / 1000.0d)) * 1000, 1000) { // from class: com.android.keyguard.KeyguardAbsKeyInputViewController.2
            @Override // android.os.CountDownTimer
            public void onTick(long j2) {
                int round = (int) Math.round(((double) j2) / 1000.0d);
                KeyguardAbsKeyInputViewController keyguardAbsKeyInputViewController = KeyguardAbsKeyInputViewController.this;
                keyguardAbsKeyInputViewController.mMessageAreaController.setMessage(((KeyguardAbsKeyInputView) ((ViewController) keyguardAbsKeyInputViewController).mView).getResources().getQuantityString(R$plurals.kg_too_many_failed_attempts_countdown, round, Integer.valueOf(round)));
            }

            @Override // android.os.CountDownTimer
            public void onFinish() {
                KeyguardAbsKeyInputViewController.this.mMessageAreaController.setMessage("");
                KeyguardAbsKeyInputViewController.this.resetState();
            }
        }.start();
    }

    void onPasswordChecked(int i, boolean z, int i2, boolean z2) {
        boolean z3 = KeyguardUpdateMonitor.getCurrentUser() == i;
        if (z) {
            getKeyguardSecurityCallback().reportUnlockAttempt(i, true, 0);
            if (z3) {
                this.mDismissing = true;
                this.mLatencyTracker.onActionStart(11);
                getKeyguardSecurityCallback().dismiss(true, i);
                return;
            }
            return;
        }
        if (z2) {
            getKeyguardSecurityCallback().reportUnlockAttempt(i, false, i2);
            if (i2 > 0) {
                handleAttemptLockout(this.mLockPatternUtils.setLockoutAttemptDeadline(i, i2));
            }
        }
        if (i2 == 0) {
            this.mMessageAreaController.setMessage(((KeyguardAbsKeyInputView) this.mView).getWrongPasswordStringId());
        }
        ((KeyguardAbsKeyInputView) this.mView).resetPasswordText(true, false);
    }

    public void verifyPasswordAndUnlock() {
        if (!this.mDismissing) {
            final LockscreenCredential enteredCredential = ((KeyguardAbsKeyInputView) this.mView).getEnteredCredential();
            ((KeyguardAbsKeyInputView) this.mView).setPasswordEntryInputEnabled(false);
            AsyncTask<?, ?, ?> asyncTask = this.mPendingLockCheck;
            if (asyncTask != null) {
                asyncTask.cancel(false);
            }
            final int currentUser = KeyguardUpdateMonitor.getCurrentUser();
            if (enteredCredential.size() <= 3) {
                ((KeyguardAbsKeyInputView) this.mView).setPasswordEntryInputEnabled(true);
                onPasswordChecked(currentUser, false, 0, false);
                enteredCredential.zeroize();
                return;
            }
            this.mLatencyTracker.onActionStart(3);
            this.mLatencyTracker.onActionStart(4);
            this.mKeyguardUpdateMonitor.setCredentialAttempted();
            this.mPendingLockCheck = LockPatternChecker.checkCredential(this.mLockPatternUtils, enteredCredential, currentUser, new LockPatternChecker.OnCheckCallback() { // from class: com.android.keyguard.KeyguardAbsKeyInputViewController.3
                public void onEarlyMatched() {
                    KeyguardAbsKeyInputViewController.this.mLatencyTracker.onActionEnd(3);
                    KeyguardAbsKeyInputViewController.this.onPasswordChecked(currentUser, true, 0, true);
                    enteredCredential.zeroize();
                }

                public void onChecked(boolean z, int i) {
                    KeyguardAbsKeyInputViewController.this.mLatencyTracker.onActionEnd(4);
                    ((KeyguardAbsKeyInputView) ((ViewController) KeyguardAbsKeyInputViewController.this).mView).setPasswordEntryInputEnabled(true);
                    KeyguardAbsKeyInputViewController keyguardAbsKeyInputViewController = KeyguardAbsKeyInputViewController.this;
                    keyguardAbsKeyInputViewController.mPendingLockCheck = null;
                    if (!z) {
                        keyguardAbsKeyInputViewController.onPasswordChecked(currentUser, false, i, true);
                    }
                    enteredCredential.zeroize();
                }

                public void onCancelled() {
                    KeyguardAbsKeyInputViewController.this.mLatencyTracker.onActionEnd(4);
                    enteredCredential.zeroize();
                }
            });
        }
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public void showPromptReason(int i) {
        int promptReasonStringRes;
        if (i != 0 && (promptReasonStringRes = ((KeyguardAbsKeyInputView) this.mView).getPromptReasonStringRes(i)) != 0) {
            this.mMessageAreaController.setMessage(promptReasonStringRes);
        }
    }

    public void onUserInput() {
        this.mFalsingCollector.updateFalseConfidence(FalsingClassifier.Result.passed(0.6d));
        getKeyguardSecurityCallback().userActivity();
        getKeyguardSecurityCallback().onUserInput();
        this.mMessageAreaController.setMessage("");
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public void onResume(int i) {
        this.mResumed = true;
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public void onPause() {
        this.mResumed = false;
        CountDownTimer countDownTimer = this.mCountdownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            this.mCountdownTimer = null;
        }
        AsyncTask<?, ?, ?> asyncTask = this.mPendingLockCheck;
        if (asyncTask != null) {
            asyncTask.cancel(false);
            this.mPendingLockCheck = null;
        }
        reset();
    }
}
