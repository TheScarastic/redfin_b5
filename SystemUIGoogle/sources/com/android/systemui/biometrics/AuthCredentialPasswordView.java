package com.android.systemui.biometrics;

import android.content.Context;
import android.os.UserHandle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImeAwareEditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.widget.LockPatternChecker;
import com.android.internal.widget.LockscreenCredential;
import com.android.internal.widget.VerifyCredentialResponse;
import com.android.systemui.R$id;
/* loaded from: classes.dex */
public class AuthCredentialPasswordView extends AuthCredentialView implements TextView.OnEditorActionListener {
    private final InputMethodManager mImm = (InputMethodManager) ((LinearLayout) this).mContext.getSystemService(InputMethodManager.class);
    private ImeAwareEditText mPasswordField;

    public AuthCredentialPasswordView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.biometrics.AuthCredentialView, android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        ImeAwareEditText findViewById = findViewById(R$id.lockPassword);
        this.mPasswordField = findViewById;
        findViewById.setOnEditorActionListener(this);
        this.mPasswordField.setOnKeyListener(new View.OnKeyListener() { // from class: com.android.systemui.biometrics.AuthCredentialPasswordView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnKeyListener
            public final boolean onKey(View view, int i, KeyEvent keyEvent) {
                return AuthCredentialPasswordView.$r8$lambda$SFBFyvyzFIcRrwQMo5zWG7ZFpks(AuthCredentialPasswordView.this, view, i, keyEvent);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onFinishInflate$0(View view, int i, KeyEvent keyEvent) {
        if (i != 4) {
            return false;
        }
        if (keyEvent.getAction() == 1) {
            this.mContainerView.sendEarlyUserCanceled();
            this.mContainerView.animateAway(1);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.biometrics.AuthCredentialView, android.view.View, android.view.ViewGroup
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mPasswordField.setTextOperationUser(UserHandle.of(this.mUserId));
        if (this.mCredentialType == 1) {
            this.mPasswordField.setInputType(18);
        }
        this.mPasswordField.requestFocus();
        this.mPasswordField.scheduleShowSoftInput();
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        boolean z = keyEvent == null && (i == 0 || i == 6 || i == 5);
        boolean z2 = keyEvent != null && KeyEvent.isConfirmKey(keyEvent.getKeyCode()) && keyEvent.getAction() == 0;
        if (!z && !z2) {
            return false;
        }
        checkPasswordAndUnlock();
        return true;
    }

    private void checkPasswordAndUnlock() {
        LockscreenCredential lockscreenCredential;
        if (this.mCredentialType == 1) {
            lockscreenCredential = LockscreenCredential.createPinOrNone(this.mPasswordField.getText());
        } else {
            lockscreenCredential = LockscreenCredential.createPasswordOrNone(this.mPasswordField.getText());
        }
        try {
            if (lockscreenCredential.isNone()) {
                lockscreenCredential.close();
                return;
            }
            this.mPendingLockCheck = LockPatternChecker.verifyCredential(this.mLockPatternUtils, lockscreenCredential, this.mEffectiveUserId, 1, new LockPatternChecker.OnVerifyCallback() { // from class: com.android.systemui.biometrics.AuthCredentialPasswordView$$ExternalSyntheticLambda1
                public final void onVerified(VerifyCredentialResponse verifyCredentialResponse, int i) {
                    AuthCredentialPasswordView.this.onCredentialVerified(verifyCredentialResponse, i);
                }
            });
            lockscreenCredential.close();
        } catch (Throwable th) {
            if (lockscreenCredential != null) {
                try {
                    lockscreenCredential.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.biometrics.AuthCredentialView
    public void onCredentialVerified(VerifyCredentialResponse verifyCredentialResponse, int i) {
        super.onCredentialVerified(verifyCredentialResponse, i);
        if (verifyCredentialResponse.isMatched()) {
            this.mImm.hideSoftInputFromWindow(getWindowToken(), 0);
        } else {
            this.mPasswordField.setText("");
        }
    }
}
