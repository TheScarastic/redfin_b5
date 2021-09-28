package com.android.keyguard;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardPinBasedInputView;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.keyguard.PasswordTextView;
import com.android.systemui.R$id;
import com.android.systemui.classifier.FalsingCollector;
/* loaded from: classes.dex */
public abstract class KeyguardPinBasedInputViewController<T extends KeyguardPinBasedInputView> extends KeyguardAbsKeyInputViewController<T> {
    private final FalsingCollector mFalsingCollector;
    private final LiftToActivateListener mLiftToActivateListener;
    protected PasswordTextView mPasswordEntry;
    private final View.OnKeyListener mOnKeyListener = new View.OnKeyListener() { // from class: com.android.keyguard.KeyguardPinBasedInputViewController$$ExternalSyntheticLambda1
        @Override // android.view.View.OnKeyListener
        public final boolean onKey(View view, int i, KeyEvent keyEvent) {
            return KeyguardPinBasedInputViewController.m17$r8$lambda$ycb30UV0rTdlIFYcYY9q1olm_o(KeyguardPinBasedInputViewController.this, view, i, keyEvent);
        }
    };
    private final View.OnTouchListener mActionButtonTouchListener = new View.OnTouchListener() { // from class: com.android.keyguard.KeyguardPinBasedInputViewController$$ExternalSyntheticLambda4
        @Override // android.view.View.OnTouchListener
        public final boolean onTouch(View view, MotionEvent motionEvent) {
            return KeyguardPinBasedInputViewController.this.lambda$new$1(view, motionEvent);
        }
    };

    public /* synthetic */ boolean lambda$new$0(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() == 0) {
            return ((KeyguardPinBasedInputView) this.mView).onKeyDown(i, keyEvent);
        }
        return false;
    }

    public /* synthetic */ boolean lambda$new$1(View view, MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() != 0) {
            return false;
        }
        ((KeyguardPinBasedInputView) this.mView).doHapticKeyClick();
        return false;
    }

    public KeyguardPinBasedInputViewController(T t, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel.SecurityMode securityMode, LockPatternUtils lockPatternUtils, KeyguardSecurityCallback keyguardSecurityCallback, KeyguardMessageAreaController.Factory factory, LatencyTracker latencyTracker, LiftToActivateListener liftToActivateListener, EmergencyButtonController emergencyButtonController, FalsingCollector falsingCollector) {
        super(t, keyguardUpdateMonitor, securityMode, lockPatternUtils, keyguardSecurityCallback, factory, latencyTracker, falsingCollector, emergencyButtonController);
        this.mLiftToActivateListener = liftToActivateListener;
        this.mFalsingCollector = falsingCollector;
        T t2 = this.mView;
        this.mPasswordEntry = (PasswordTextView) ((KeyguardPinBasedInputView) t2).findViewById(((KeyguardPinBasedInputView) t2).getPasswordTextViewId());
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController, com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    public void onViewAttached() {
        super.onViewAttached();
        for (NumPadKey numPadKey : ((KeyguardPinBasedInputView) this.mView).getButtons()) {
            numPadKey.setOnTouchListener(new View.OnTouchListener() { // from class: com.android.keyguard.KeyguardPinBasedInputViewController$$ExternalSyntheticLambda3
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view, MotionEvent motionEvent) {
                    return KeyguardPinBasedInputViewController.this.lambda$onViewAttached$2(view, motionEvent);
                }
            });
        }
        this.mPasswordEntry.setOnKeyListener(this.mOnKeyListener);
        this.mPasswordEntry.setUserActivityListener(new PasswordTextView.UserActivityListener() { // from class: com.android.keyguard.KeyguardPinBasedInputViewController$$ExternalSyntheticLambda5
            @Override // com.android.keyguard.PasswordTextView.UserActivityListener
            public final void onUserActivity() {
                KeyguardPinBasedInputViewController.this.onUserInput();
            }
        });
        View findViewById = ((KeyguardPinBasedInputView) this.mView).findViewById(R$id.delete_button);
        findViewById.setOnTouchListener(this.mActionButtonTouchListener);
        findViewById.setOnClickListener(new View.OnClickListener() { // from class: com.android.keyguard.KeyguardPinBasedInputViewController$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                KeyguardPinBasedInputViewController.m16$r8$lambda$sVhaIBwpegVYYoUq1EphloEbjc(KeyguardPinBasedInputViewController.this, view);
            }
        });
        findViewById.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.keyguard.KeyguardPinBasedInputViewController$$ExternalSyntheticLambda2
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                return KeyguardPinBasedInputViewController.this.lambda$onViewAttached$4(view);
            }
        });
        View findViewById2 = ((KeyguardPinBasedInputView) this.mView).findViewById(R$id.key_enter);
        if (findViewById2 != null) {
            findViewById2.setOnTouchListener(this.mActionButtonTouchListener);
            findViewById2.setOnClickListener(new View.OnClickListener() { // from class: com.android.keyguard.KeyguardPinBasedInputViewController.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (KeyguardPinBasedInputViewController.this.mPasswordEntry.isEnabled()) {
                        KeyguardPinBasedInputViewController.this.verifyPasswordAndUnlock();
                    }
                }
            });
            findViewById2.setOnHoverListener(this.mLiftToActivateListener);
        }
    }

    public /* synthetic */ boolean lambda$onViewAttached$2(View view, MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() != 0) {
            return false;
        }
        this.mFalsingCollector.avoidGesture();
        return false;
    }

    public /* synthetic */ void lambda$onViewAttached$3(View view) {
        if (this.mPasswordEntry.isEnabled()) {
            this.mPasswordEntry.deleteLastChar();
        }
    }

    public /* synthetic */ boolean lambda$onViewAttached$4(View view) {
        if (this.mPasswordEntry.isEnabled()) {
            ((KeyguardPinBasedInputView) this.mView).resetPasswordText(true, true);
        }
        ((KeyguardPinBasedInputView) this.mView).doHapticKeyClick();
        return true;
    }

    @Override // com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    public void onViewDetached() {
        super.onViewDetached();
        for (NumPadKey numPadKey : ((KeyguardPinBasedInputView) this.mView).getButtons()) {
            numPadKey.setOnTouchListener(null);
        }
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController, com.android.keyguard.KeyguardInputViewController
    public void onResume(int i) {
        super.onResume(i);
        this.mPasswordEntry.requestFocus();
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController
    public void resetState() {
        ((KeyguardPinBasedInputView) this.mView).setPasswordEntryEnabled(true);
    }
}
