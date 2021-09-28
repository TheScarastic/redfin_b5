package com.android.keyguard;

import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternChecker;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.LockPatternView;
import com.android.internal.widget.LockscreenCredential;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.settingslib.Utils;
import com.android.systemui.R$id;
import com.android.systemui.R$plurals;
import com.android.systemui.R$string;
import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.util.ViewController;
import java.util.List;
/* loaded from: classes.dex */
public class KeyguardPatternViewController extends KeyguardInputViewController<KeyguardPatternView> {
    private CountDownTimer mCountdownTimer;
    private final EmergencyButtonController mEmergencyButtonController;
    private final FalsingCollector mFalsingCollector;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final LatencyTracker mLatencyTracker;
    private final LockPatternUtils mLockPatternUtils;
    private KeyguardMessageAreaController mMessageAreaController;
    private final KeyguardMessageAreaController.Factory mMessageAreaControllerFactory;
    private AsyncTask<?, ?, ?> mPendingLockCheck;
    private EmergencyButtonController.EmergencyButtonCallback mEmergencyButtonCallback = new EmergencyButtonController.EmergencyButtonCallback() { // from class: com.android.keyguard.KeyguardPatternViewController.1
        @Override // com.android.keyguard.EmergencyButtonController.EmergencyButtonCallback
        public void onEmergencyButtonClickedWhenInCall() {
            KeyguardPatternViewController.this.getKeyguardSecurityCallback().reset();
        }
    };
    private Runnable mCancelPatternRunnable = new Runnable() { // from class: com.android.keyguard.KeyguardPatternViewController.2
        @Override // java.lang.Runnable
        public void run() {
            KeyguardPatternViewController.this.mLockPatternView.clearPattern();
        }
    };
    private LockPatternView mLockPatternView = ((KeyguardPatternView) this.mView).findViewById(R$id.lockPatternView);

    @Override // com.android.keyguard.KeyguardSecurityView
    public boolean needsInput() {
        return false;
    }

    /* loaded from: classes.dex */
    private class UnlockPatternListener implements LockPatternView.OnPatternListener {
        public void onPatternCleared() {
        }

        private UnlockPatternListener() {
        }

        public void onPatternStart() {
            KeyguardPatternViewController.this.mLockPatternView.removeCallbacks(KeyguardPatternViewController.this.mCancelPatternRunnable);
            KeyguardPatternViewController.this.mMessageAreaController.setMessage("");
        }

        public void onPatternCellAdded(List<LockPatternView.Cell> list) {
            KeyguardPatternViewController.this.getKeyguardSecurityCallback().userActivity();
            KeyguardPatternViewController.this.getKeyguardSecurityCallback().onUserInput();
        }

        public void onPatternDetected(List<LockPatternView.Cell> list) {
            KeyguardPatternViewController.this.mKeyguardUpdateMonitor.setCredentialAttempted();
            KeyguardPatternViewController.this.mLockPatternView.disableInput();
            if (KeyguardPatternViewController.this.mPendingLockCheck != null) {
                KeyguardPatternViewController.this.mPendingLockCheck.cancel(false);
            }
            final int currentUser = KeyguardUpdateMonitor.getCurrentUser();
            if (list.size() < 4) {
                if (list.size() == 1) {
                    KeyguardPatternViewController.this.mFalsingCollector.updateFalseConfidence(FalsingClassifier.Result.falsed(0.7d, UnlockPatternListener.class.getSimpleName(), "empty pattern input"));
                }
                KeyguardPatternViewController.this.mLockPatternView.enableInput();
                onPatternChecked(currentUser, false, 0, false);
                return;
            }
            KeyguardPatternViewController.this.mLatencyTracker.onActionStart(3);
            KeyguardPatternViewController.this.mLatencyTracker.onActionStart(4);
            KeyguardPatternViewController keyguardPatternViewController = KeyguardPatternViewController.this;
            keyguardPatternViewController.mPendingLockCheck = LockPatternChecker.checkCredential(keyguardPatternViewController.mLockPatternUtils, LockscreenCredential.createPattern(list), currentUser, new LockPatternChecker.OnCheckCallback() { // from class: com.android.keyguard.KeyguardPatternViewController.UnlockPatternListener.1
                public void onEarlyMatched() {
                    KeyguardPatternViewController.this.mLatencyTracker.onActionEnd(3);
                    UnlockPatternListener.this.onPatternChecked(currentUser, true, 0, true);
                }

                public void onChecked(boolean z, int i) {
                    KeyguardPatternViewController.this.mLatencyTracker.onActionEnd(4);
                    KeyguardPatternViewController.this.mLockPatternView.enableInput();
                    KeyguardPatternViewController.this.mPendingLockCheck = null;
                    if (!z) {
                        UnlockPatternListener.this.onPatternChecked(currentUser, false, i, true);
                    }
                }

                public void onCancelled() {
                    KeyguardPatternViewController.this.mLatencyTracker.onActionEnd(4);
                }
            });
            if (list.size() > 2) {
                KeyguardPatternViewController.this.getKeyguardSecurityCallback().userActivity();
                KeyguardPatternViewController.this.getKeyguardSecurityCallback().onUserInput();
            }
        }

        /* access modifiers changed from: private */
        public void onPatternChecked(int i, boolean z, int i2, boolean z2) {
            boolean z3 = KeyguardUpdateMonitor.getCurrentUser() == i;
            if (z) {
                KeyguardPatternViewController.this.getKeyguardSecurityCallback().reportUnlockAttempt(i, true, 0);
                if (z3) {
                    KeyguardPatternViewController.this.mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
                    KeyguardPatternViewController.this.mLatencyTracker.onActionStart(11);
                    KeyguardPatternViewController.this.getKeyguardSecurityCallback().dismiss(true, i);
                    return;
                }
                return;
            }
            KeyguardPatternViewController.this.mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
            if (z2) {
                KeyguardPatternViewController.this.getKeyguardSecurityCallback().reportUnlockAttempt(i, false, i2);
                if (i2 > 0) {
                    KeyguardPatternViewController.this.handleAttemptLockout(KeyguardPatternViewController.this.mLockPatternUtils.setLockoutAttemptDeadline(i, i2));
                }
            }
            if (i2 == 0) {
                KeyguardPatternViewController.this.mMessageAreaController.setMessage(R$string.kg_wrong_pattern);
                KeyguardPatternViewController.this.mLockPatternView.postDelayed(KeyguardPatternViewController.this.mCancelPatternRunnable, 2000);
            }
        }
    }

    /* access modifiers changed from: protected */
    public KeyguardPatternViewController(KeyguardPatternView keyguardPatternView, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel.SecurityMode securityMode, LockPatternUtils lockPatternUtils, KeyguardSecurityCallback keyguardSecurityCallback, LatencyTracker latencyTracker, FalsingCollector falsingCollector, EmergencyButtonController emergencyButtonController, KeyguardMessageAreaController.Factory factory) {
        super(keyguardPatternView, securityMode, keyguardSecurityCallback, emergencyButtonController);
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mLockPatternUtils = lockPatternUtils;
        this.mLatencyTracker = latencyTracker;
        this.mFalsingCollector = falsingCollector;
        this.mEmergencyButtonController = emergencyButtonController;
        this.mMessageAreaControllerFactory = factory;
        this.mMessageAreaController = factory.create(KeyguardMessageArea.findSecurityMessageDisplay(this.mView));
    }

    @Override // com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    public void onInit() {
        super.onInit();
        this.mMessageAreaController.init();
    }

    /* access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    public void onViewAttached() {
        super.onViewAttached();
        this.mLockPatternView.setOnPatternListener(new UnlockPatternListener());
        this.mLockPatternView.setSaveEnabled(false);
        this.mLockPatternView.setInStealthMode(!this.mLockPatternUtils.isVisiblePatternEnabled(KeyguardUpdateMonitor.getCurrentUser()));
        this.mLockPatternView.setTactileFeedbackEnabled(this.mLockPatternUtils.isTactileFeedbackEnabled());
        this.mLockPatternView.setOnTouchListener(new View.OnTouchListener() { // from class: com.android.keyguard.KeyguardPatternViewController$$ExternalSyntheticLambda1
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                return KeyguardPatternViewController.$r8$lambda$9FF4Id9_aeLZ_a7_yDLXSlXa67I(KeyguardPatternViewController.this, view, motionEvent);
            }
        });
        this.mEmergencyButtonController.setEmergencyButtonCallback(this.mEmergencyButtonCallback);
        View findViewById = ((KeyguardPatternView) this.mView).findViewById(R$id.cancel_button);
        if (findViewById != null) {
            findViewById.setOnClickListener(new View.OnClickListener() { // from class: com.android.keyguard.KeyguardPatternViewController$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    KeyguardPatternViewController.$r8$lambda$Ff9KpUhZ1bEpMHY1fStKFXv4_nQ(KeyguardPatternViewController.this, view);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onViewAttached$0(View view, MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() != 0) {
            return false;
        }
        this.mFalsingCollector.avoidGesture();
        return false;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewAttached$1(View view) {
        getKeyguardSecurityCallback().reset();
        getKeyguardSecurityCallback().onCancelClicked();
    }

    /* access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    public void onViewDetached() {
        super.onViewDetached();
        this.mLockPatternView.setOnPatternListener((LockPatternView.OnPatternListener) null);
        this.mLockPatternView.setOnTouchListener((View.OnTouchListener) null);
        this.mEmergencyButtonController.setEmergencyButtonCallback(null);
        View findViewById = ((KeyguardPatternView) this.mView).findViewById(R$id.cancel_button);
        if (findViewById != null) {
            findViewById.setOnClickListener(null);
        }
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public void reset() {
        this.mLockPatternView.setInStealthMode(!this.mLockPatternUtils.isVisiblePatternEnabled(KeyguardUpdateMonitor.getCurrentUser()));
        this.mLockPatternView.enableInput();
        this.mLockPatternView.setEnabled(true);
        this.mLockPatternView.clearPattern();
        long lockoutAttemptDeadline = this.mLockPatternUtils.getLockoutAttemptDeadline(KeyguardUpdateMonitor.getCurrentUser());
        if (lockoutAttemptDeadline != 0) {
            handleAttemptLockout(lockoutAttemptDeadline);
        } else {
            displayDefaultSecurityMessage();
        }
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public void reloadColors() {
        super.reloadColors();
        this.mMessageAreaController.reloadColors();
        int defaultColor = Utils.getColorAttr(this.mLockPatternView.getContext(), 16842806).getDefaultColor();
        this.mLockPatternView.setColors(defaultColor, defaultColor, Utils.getColorError(this.mLockPatternView.getContext()).getDefaultColor());
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public void onPause() {
        super.onPause();
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
        displayDefaultSecurityMessage();
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public void showPromptReason(int i) {
        if (i == 0) {
            return;
        }
        if (i == 1) {
            this.mMessageAreaController.setMessage(R$string.kg_prompt_reason_restart_pattern);
        } else if (i == 2) {
            this.mMessageAreaController.setMessage(R$string.kg_prompt_reason_timeout_pattern);
        } else if (i == 3) {
            this.mMessageAreaController.setMessage(R$string.kg_prompt_reason_device_admin);
        } else if (i == 4) {
            this.mMessageAreaController.setMessage(R$string.kg_prompt_reason_user_request);
        } else if (i != 6) {
            this.mMessageAreaController.setMessage(R$string.kg_prompt_reason_timeout_pattern);
        } else {
            this.mMessageAreaController.setMessage(R$string.kg_prompt_reason_timeout_pattern);
        }
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public void showMessage(CharSequence charSequence, ColorStateList colorStateList) {
        if (colorStateList != null) {
            this.mMessageAreaController.setNextMessageColor(colorStateList);
        }
        this.mMessageAreaController.setMessage(charSequence);
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public void startAppearAnimation() {
        super.startAppearAnimation();
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public boolean startDisappearAnimation(Runnable runnable) {
        return ((KeyguardPatternView) this.mView).startDisappearAnimation(this.mKeyguardUpdateMonitor.needsSlowUnlockTransition(), runnable);
    }

    /* access modifiers changed from: private */
    public void displayDefaultSecurityMessage() {
        this.mMessageAreaController.setMessage("");
    }

    /* access modifiers changed from: private */
    public void handleAttemptLockout(long j) {
        this.mLockPatternView.clearPattern();
        this.mLockPatternView.setEnabled(false);
        this.mCountdownTimer = new CountDownTimer(((long) Math.ceil(((double) (j - SystemClock.elapsedRealtime())) / 1000.0d)) * 1000, 1000) { // from class: com.android.keyguard.KeyguardPatternViewController.3
            @Override // android.os.CountDownTimer
            public void onTick(long j2) {
                int round = (int) Math.round(((double) j2) / 1000.0d);
                KeyguardPatternViewController.this.mMessageAreaController.setMessage(((KeyguardPatternView) ((ViewController) KeyguardPatternViewController.this).mView).getResources().getQuantityString(R$plurals.kg_too_many_failed_attempts_countdown, round, Integer.valueOf(round)));
            }

            @Override // android.os.CountDownTimer
            public void onFinish() {
                KeyguardPatternViewController.this.mLockPatternView.setEnabled(true);
                KeyguardPatternViewController.this.displayDefaultSecurityMessage();
            }
        }.start();
    }
}
