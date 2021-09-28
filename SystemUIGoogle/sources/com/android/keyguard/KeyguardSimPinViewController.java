package com.android.keyguard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.telephony.PinResult;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.systemui.R$id;
import com.android.systemui.R$plurals;
import com.android.systemui.R$string;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.util.ViewController;
/* loaded from: classes.dex */
public class KeyguardSimPinViewController extends KeyguardPinBasedInputViewController<KeyguardSimPinView> {
    private CheckSimPin mCheckSimPinThread;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private int mRemainingAttempts;
    private AlertDialog mRemainingAttemptsDialog;
    private boolean mShowDefaultMessage;
    private ProgressDialog mSimUnlockProgressDialog;
    private final TelephonyManager mTelephonyManager;
    private int mSubId = -1;
    KeyguardUpdateMonitorCallback mUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.keyguard.KeyguardSimPinViewController.1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onSimStateChanged(int i, int i2, int i3) {
            Log.v("KeyguardSimPinView", "onSimStateChanged(subId=" + i + ",state=" + i3 + ")");
            if (i3 == 5) {
                KeyguardSimPinViewController.this.mRemainingAttempts = -1;
                KeyguardSimPinViewController.this.resetState();
                return;
            }
            KeyguardSimPinViewController.this.resetState();
        }
    };
    private ImageView mSimImageView = (ImageView) ((KeyguardSimPinView) this.mView).findViewById(R$id.keyguard_sim);

    @Override // com.android.keyguard.KeyguardInputViewController
    public boolean startDisappearAnimation(Runnable runnable) {
        return false;
    }

    /* access modifiers changed from: protected */
    public KeyguardSimPinViewController(KeyguardSimPinView keyguardSimPinView, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel.SecurityMode securityMode, LockPatternUtils lockPatternUtils, KeyguardSecurityCallback keyguardSecurityCallback, KeyguardMessageAreaController.Factory factory, LatencyTracker latencyTracker, LiftToActivateListener liftToActivateListener, TelephonyManager telephonyManager, FalsingCollector falsingCollector, EmergencyButtonController emergencyButtonController) {
        super(keyguardSimPinView, keyguardUpdateMonitor, securityMode, lockPatternUtils, keyguardSecurityCallback, factory, latencyTracker, liftToActivateListener, emergencyButtonController, falsingCollector);
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mTelephonyManager = telephonyManager;
    }

    @Override // com.android.keyguard.KeyguardPinBasedInputViewController, com.android.keyguard.KeyguardAbsKeyInputViewController, com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    protected void onViewAttached() {
        super.onViewAttached();
    }

    @Override // com.android.keyguard.KeyguardPinBasedInputViewController, com.android.keyguard.KeyguardAbsKeyInputViewController
    void resetState() {
        super.resetState();
        Log.v("KeyguardSimPinView", "Resetting state");
        handleSubInfoChangeIfNeeded();
        this.mMessageAreaController.setMessage("");
        if (this.mShowDefaultMessage) {
            showDefaultMessage();
        }
        T t = this.mView;
        ((KeyguardSimPinView) t).setEsimLocked(KeyguardEsimArea.isEsimLocked(((KeyguardSimPinView) t).getContext(), this.mSubId));
    }

    @Override // com.android.keyguard.KeyguardPinBasedInputViewController, com.android.keyguard.KeyguardAbsKeyInputViewController, com.android.keyguard.KeyguardInputViewController
    public void onResume(int i) {
        super.onResume(i);
        this.mKeyguardUpdateMonitor.registerCallback(this.mUpdateMonitorCallback);
        ((KeyguardSimPinView) this.mView).resetState();
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController, com.android.keyguard.KeyguardInputViewController
    public void onPause() {
        super.onPause();
        this.mKeyguardUpdateMonitor.removeCallback(this.mUpdateMonitorCallback);
        ProgressDialog progressDialog = this.mSimUnlockProgressDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
            this.mSimUnlockProgressDialog = null;
        }
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController
    protected void verifyPasswordAndUnlock() {
        if (this.mPasswordEntry.getText().length() < 4) {
            this.mMessageAreaController.setMessage(R$string.kg_invalid_sim_pin_hint);
            ((KeyguardSimPinView) this.mView).resetPasswordText(true, true);
            getKeyguardSecurityCallback().userActivity();
            return;
        }
        getSimUnlockProgressDialog().show();
        if (this.mCheckSimPinThread == null) {
            AnonymousClass2 r0 = new CheckSimPin(this.mPasswordEntry.getText(), this.mSubId) { // from class: com.android.keyguard.KeyguardSimPinViewController.2
                @Override // com.android.keyguard.KeyguardSimPinViewController.CheckSimPin
                void onSimCheckResponse(PinResult pinResult) {
                    ((KeyguardSimPinView) ((ViewController) KeyguardSimPinViewController.this).mView).post(new KeyguardSimPinViewController$2$$ExternalSyntheticLambda0(this, pinResult));
                }

                /* access modifiers changed from: private */
                public /* synthetic */ void lambda$onSimCheckResponse$0(PinResult pinResult) {
                    KeyguardSimPinViewController.this.mRemainingAttempts = pinResult.getAttemptsRemaining();
                    if (KeyguardSimPinViewController.this.mSimUnlockProgressDialog != null) {
                        KeyguardSimPinViewController.this.mSimUnlockProgressDialog.hide();
                    }
                    ((KeyguardSimPinView) ((ViewController) KeyguardSimPinViewController.this).mView).resetPasswordText(true, pinResult.getResult() != 0);
                    if (pinResult.getResult() == 0) {
                        KeyguardSimPinViewController.this.mKeyguardUpdateMonitor.reportSimUnlocked(KeyguardSimPinViewController.this.mSubId);
                        KeyguardSimPinViewController.this.mRemainingAttempts = -1;
                        KeyguardSimPinViewController.this.mShowDefaultMessage = true;
                        KeyguardSimPinViewController.this.getKeyguardSecurityCallback().dismiss(true, KeyguardUpdateMonitor.getCurrentUser());
                    } else {
                        KeyguardSimPinViewController.this.mShowDefaultMessage = false;
                        if (pinResult.getResult() != 1) {
                            KeyguardSimPinViewController keyguardSimPinViewController = KeyguardSimPinViewController.this;
                            keyguardSimPinViewController.mMessageAreaController.setMessage(((KeyguardSimPinView) ((ViewController) keyguardSimPinViewController).mView).getResources().getString(R$string.kg_password_pin_failed));
                        } else if (pinResult.getAttemptsRemaining() <= 2) {
                            KeyguardSimPinViewController.this.getSimRemainingAttemptsDialog(pinResult.getAttemptsRemaining()).show();
                        } else {
                            KeyguardSimPinViewController keyguardSimPinViewController2 = KeyguardSimPinViewController.this;
                            keyguardSimPinViewController2.mMessageAreaController.setMessage(keyguardSimPinViewController2.getPinPasswordErrorMessage(pinResult.getAttemptsRemaining(), false));
                        }
                        Log.d("KeyguardSimPinView", "verifyPasswordAndUnlock  CheckSimPin.onSimCheckResponse: " + pinResult + " attemptsRemaining=" + pinResult.getAttemptsRemaining());
                    }
                    KeyguardSimPinViewController.this.getKeyguardSecurityCallback().userActivity();
                    KeyguardSimPinViewController.this.mCheckSimPinThread = null;
                }
            };
            this.mCheckSimPinThread = r0;
            r0.start();
        }
    }

    private Dialog getSimUnlockProgressDialog() {
        if (this.mSimUnlockProgressDialog == null) {
            ProgressDialog progressDialog = new ProgressDialog(((KeyguardSimPinView) this.mView).getContext());
            this.mSimUnlockProgressDialog = progressDialog;
            progressDialog.setMessage(((KeyguardSimPinView) this.mView).getResources().getString(R$string.kg_sim_unlock_progress_dialog_message));
            this.mSimUnlockProgressDialog.setIndeterminate(true);
            this.mSimUnlockProgressDialog.setCancelable(false);
            this.mSimUnlockProgressDialog.getWindow().setType(2009);
        }
        return this.mSimUnlockProgressDialog;
    }

    /* access modifiers changed from: private */
    public Dialog getSimRemainingAttemptsDialog(int i) {
        String pinPasswordErrorMessage = getPinPasswordErrorMessage(i, false);
        AlertDialog alertDialog = this.mRemainingAttemptsDialog;
        if (alertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(((KeyguardSimPinView) this.mView).getContext());
            builder.setMessage(pinPasswordErrorMessage);
            builder.setCancelable(false);
            builder.setNeutralButton(R$string.ok, (DialogInterface.OnClickListener) null);
            AlertDialog create = builder.create();
            this.mRemainingAttemptsDialog = create;
            create.getWindow().setType(2009);
        } else {
            alertDialog.setMessage(pinPasswordErrorMessage);
        }
        return this.mRemainingAttemptsDialog;
    }

    /* access modifiers changed from: private */
    public String getPinPasswordErrorMessage(int i, boolean z) {
        String str;
        int i2;
        if (i == 0) {
            str = ((KeyguardSimPinView) this.mView).getResources().getString(R$string.kg_password_wrong_pin_code_pukked);
        } else if (i > 0) {
            if (z) {
                i2 = R$plurals.kg_password_default_pin_message;
            } else {
                i2 = R$plurals.kg_password_wrong_pin_code;
            }
            str = ((KeyguardSimPinView) this.mView).getResources().getQuantityString(i2, i, Integer.valueOf(i));
        } else {
            str = ((KeyguardSimPinView) this.mView).getResources().getString(z ? R$string.kg_sim_pin_instructions : R$string.kg_password_pin_failed);
        }
        if (KeyguardEsimArea.isEsimLocked(((KeyguardSimPinView) this.mView).getContext(), this.mSubId)) {
            str = ((KeyguardSimPinView) this.mView).getResources().getString(R$string.kg_sim_lock_esim_instructions, str);
        }
        Log.d("KeyguardSimPinView", "getPinPasswordErrorMessage: attemptsRemaining=" + i + " displayMessage=" + str);
        return str;
    }

    private void showDefaultMessage() {
        setLockedSimMessage();
        if (this.mRemainingAttempts < 0) {
            new CheckSimPin("", this.mSubId) { // from class: com.android.keyguard.KeyguardSimPinViewController.3
                @Override // com.android.keyguard.KeyguardSimPinViewController.CheckSimPin
                void onSimCheckResponse(PinResult pinResult) {
                    Log.d("KeyguardSimPinView", "onSimCheckResponse  empty One result " + pinResult.toString());
                    if (pinResult.getAttemptsRemaining() >= 0) {
                        KeyguardSimPinViewController.this.mRemainingAttempts = pinResult.getAttemptsRemaining();
                        KeyguardSimPinViewController.this.setLockedSimMessage();
                    }
                }
            }.start();
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public abstract class CheckSimPin extends Thread {
        private final String mPin;
        private int mSubId;

        /* access modifiers changed from: package-private */
        /* renamed from: onSimCheckResponse */
        public abstract void lambda$run$0(PinResult pinResult);

        protected CheckSimPin(String str, int i) {
            this.mPin = str;
            this.mSubId = i;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Log.v("KeyguardSimPinView", "call supplyIccLockPin(subid=" + this.mSubId + ")");
            PinResult supplyIccLockPin = KeyguardSimPinViewController.this.mTelephonyManager.createForSubscriptionId(this.mSubId).supplyIccLockPin(this.mPin);
            Log.v("KeyguardSimPinView", "supplyIccLockPin returned: " + supplyIccLockPin.toString());
            ((KeyguardSimPinView) ((ViewController) KeyguardSimPinViewController.this).mView).post(new KeyguardSimPinViewController$CheckSimPin$$ExternalSyntheticLambda0(this, supplyIccLockPin));
        }
    }

    /* access modifiers changed from: private */
    public void setLockedSimMessage() {
        String str;
        boolean isEsimLocked = KeyguardEsimArea.isEsimLocked(((KeyguardSimPinView) this.mView).getContext(), this.mSubId);
        TelephonyManager telephonyManager = this.mTelephonyManager;
        int activeModemCount = telephonyManager != null ? telephonyManager.getActiveModemCount() : 1;
        Resources resources = ((KeyguardSimPinView) this.mView).getResources();
        TypedArray obtainStyledAttributes = ((KeyguardSimPinView) this.mView).getContext().obtainStyledAttributes(new int[]{16842904});
        int color = obtainStyledAttributes.getColor(0, -1);
        obtainStyledAttributes.recycle();
        if (activeModemCount < 2) {
            str = resources.getString(R$string.kg_sim_pin_instructions);
        } else {
            SubscriptionInfo subscriptionInfoForSubId = this.mKeyguardUpdateMonitor.getSubscriptionInfoForSubId(this.mSubId);
            String string = resources.getString(R$string.kg_sim_pin_instructions_multi, subscriptionInfoForSubId != null ? subscriptionInfoForSubId.getDisplayName() : "");
            if (subscriptionInfoForSubId != null) {
                color = subscriptionInfoForSubId.getIconTint();
            }
            str = string;
        }
        if (isEsimLocked) {
            str = resources.getString(R$string.kg_sim_lock_esim_instructions, str);
        }
        if (((KeyguardSimPinView) this.mView).getVisibility() == 0) {
            this.mMessageAreaController.setMessage(str);
        }
        this.mSimImageView.setImageTintList(ColorStateList.valueOf(color));
    }

    private void handleSubInfoChangeIfNeeded() {
        int nextSubIdForState = this.mKeyguardUpdateMonitor.getNextSubIdForState(2);
        if (nextSubIdForState != this.mSubId && SubscriptionManager.isValidSubscriptionId(nextSubIdForState)) {
            this.mSubId = nextSubIdForState;
            this.mShowDefaultMessage = true;
            this.mRemainingAttempts = -1;
        }
    }
}
