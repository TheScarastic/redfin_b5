package com.android.keyguard;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import com.android.internal.widget.LockscreenCredential;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
/* loaded from: classes.dex */
public abstract class KeyguardPinBasedInputView extends KeyguardAbsKeyInputView {
    private NumPadKey[] mButtons;
    private NumPadButton mDeleteButton;
    private NumPadButton mOkButton;
    protected PasswordTextView mPasswordEntry;

    /* access modifiers changed from: protected */
    public void resetState() {
    }

    public KeyguardPinBasedInputView(Context context) {
        this(context, null);
    }

    public KeyguardPinBasedInputView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mButtons = new NumPadKey[10];
    }

    @Override // android.view.ViewGroup
    protected boolean onRequestFocusInDescendants(int i, Rect rect) {
        return this.mPasswordEntry.requestFocus(i, rect);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public void setPasswordEntryEnabled(boolean z) {
        this.mPasswordEntry.setEnabled(z);
        this.mOkButton.setEnabled(z);
        if (z && !this.mPasswordEntry.hasFocus()) {
            this.mPasswordEntry.requestFocus();
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public void setPasswordEntryInputEnabled(boolean z) {
        this.mPasswordEntry.setEnabled(z);
        this.mOkButton.setEnabled(z);
        if (z && !this.mPasswordEntry.hasFocus()) {
            this.mPasswordEntry.requestFocus();
        }
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputView, android.view.KeyEvent.Callback, android.view.View
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (KeyEvent.isConfirmKey(i)) {
            this.mOkButton.performClick();
            return true;
        } else if (i == 67) {
            this.mDeleteButton.performClick();
            return true;
        } else if (i >= 7 && i <= 16) {
            performNumberClick(i - 7);
            return true;
        } else if (i < 144 || i > 153) {
            return super.onKeyDown(i, keyEvent);
        } else {
            performNumberClick(i - 144);
            return true;
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public int getPromptReasonStringRes(int i) {
        if (i == 0) {
            return 0;
        }
        if (i == 1) {
            return R$string.kg_prompt_reason_restart_pin;
        }
        if (i == 2) {
            return R$string.kg_prompt_reason_timeout_pin;
        }
        if (i == 3) {
            return R$string.kg_prompt_reason_device_admin;
        }
        if (i == 4) {
            return R$string.kg_prompt_reason_user_request;
        }
        if (i != 6) {
            return R$string.kg_prompt_reason_timeout_pin;
        }
        return R$string.kg_prompt_reason_timeout_pin;
    }

    private void performNumberClick(int i) {
        if (i >= 0 && i <= 9) {
            this.mButtons[i].performClick();
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public void resetPasswordText(boolean z, boolean z2) {
        this.mPasswordEntry.reset(z, z2);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public LockscreenCredential getEnteredCredential() {
        return LockscreenCredential.createPinOrNone(this.mPasswordEntry.getText());
    }

    /* access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardAbsKeyInputView, android.view.View
    public void onFinishInflate() {
        PasswordTextView passwordTextView = (PasswordTextView) findViewById(getPasswordTextViewId());
        this.mPasswordEntry = passwordTextView;
        passwordTextView.setSelected(true);
        this.mOkButton = (NumPadButton) findViewById(R$id.key_enter);
        NumPadButton numPadButton = (NumPadButton) findViewById(R$id.delete_button);
        this.mDeleteButton = numPadButton;
        numPadButton.setVisibility(0);
        this.mButtons[0] = (NumPadKey) findViewById(R$id.key0);
        this.mButtons[1] = (NumPadKey) findViewById(R$id.key1);
        this.mButtons[2] = (NumPadKey) findViewById(R$id.key2);
        this.mButtons[3] = (NumPadKey) findViewById(R$id.key3);
        this.mButtons[4] = (NumPadKey) findViewById(R$id.key4);
        this.mButtons[5] = (NumPadKey) findViewById(R$id.key5);
        this.mButtons[6] = (NumPadKey) findViewById(R$id.key6);
        this.mButtons[7] = (NumPadKey) findViewById(R$id.key7);
        this.mButtons[8] = (NumPadKey) findViewById(R$id.key8);
        this.mButtons[9] = (NumPadKey) findViewById(R$id.key9);
        this.mPasswordEntry.requestFocus();
        super.onFinishInflate();
        reloadColors();
    }

    /* access modifiers changed from: package-private */
    public NumPadKey[] getButtons() {
        return this.mButtons;
    }

    public void reloadColors() {
        for (NumPadKey numPadKey : this.mButtons) {
            numPadKey.reloadColors();
        }
        this.mPasswordEntry.reloadColors();
        this.mDeleteButton.reloadColors();
        this.mOkButton.reloadColors();
    }

    @Override // com.android.keyguard.KeyguardInputView
    public CharSequence getTitle() {
        return getContext().getString(17040438);
    }
}
