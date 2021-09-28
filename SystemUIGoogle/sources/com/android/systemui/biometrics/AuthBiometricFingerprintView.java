package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
/* loaded from: classes.dex */
public class AuthBiometricFingerprintView extends AuthBiometricView {
    private boolean shouldAnimateForTransition(int i, int i2) {
        return (i2 == 1 || i2 == 2) ? i == 4 || i == 3 : i2 == 3 || i2 == 4;
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    protected int getDelayAfterAuthenticatedDurationMs() {
        return 0;
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    protected int getStateForAfterError() {
        return 2;
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    protected boolean supportsSmallDialog() {
        return false;
    }

    public AuthBiometricFingerprintView(Context context) {
        this(context, null);
    }

    public AuthBiometricFingerprintView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    protected void handleResetAfterError() {
        showTouchSensorString();
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    protected void handleResetAfterHelp() {
        showTouchSensorString();
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public void updateState(int i) {
        updateIcon(this.mState, i);
        super.updateState(i);
    }

    /* access modifiers changed from: package-private */
    @Override // com.android.systemui.biometrics.AuthBiometricView
    public void onAttachedToWindowInternal() {
        super.onAttachedToWindowInternal();
        showTouchSensorString();
    }

    private void showTouchSensorString() {
        this.mIndicatorView.setText(R$string.fingerprint_dialog_touch_sensor);
        this.mIndicatorView.setTextColor(this.mTextColorHint);
    }

    private void updateIcon(int i, int i2) {
        Drawable animationForTransition = getAnimationForTransition(i, i2);
        if (animationForTransition == null) {
            Log.e("BiometricPrompt/AuthBiometricFingerprintView", "Animation not found, " + i + " -> " + i2);
            return;
        }
        AnimatedVectorDrawable animatedVectorDrawable = animationForTransition instanceof AnimatedVectorDrawable ? (AnimatedVectorDrawable) animationForTransition : null;
        this.mIconView.setImageDrawable(animationForTransition);
        CharSequence iconContentDescription = getIconContentDescription(i2);
        if (iconContentDescription != null) {
            this.mIconView.setContentDescription(iconContentDescription);
        }
        if (animatedVectorDrawable != null && shouldAnimateForTransition(i, i2)) {
            animatedVectorDrawable.forceAnimationOnUI();
            animatedVectorDrawable.start();
        }
    }

    private CharSequence getIconContentDescription(int i) {
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 5:
            case 6:
                return ((LinearLayout) this).mContext.getString(R$string.accessibility_fingerprint_dialog_fingerprint_icon);
            case 3:
            case 4:
                return ((LinearLayout) this).mContext.getString(R$string.biometric_dialog_try_again);
            default:
                return null;
        }
    }

    private Drawable getAnimationForTransition(int i, int i2) {
        int i3;
        if (i2 == 1 || i2 == 2) {
            if (i == 4 || i == 3) {
                i3 = R$drawable.fingerprint_dialog_error_to_fp;
            } else {
                i3 = R$drawable.fingerprint_dialog_fp_to_error;
            }
        } else if (i2 == 3 || i2 == 4) {
            i3 = R$drawable.fingerprint_dialog_fp_to_error;
        } else if (i2 != 6) {
            return null;
        } else {
            i3 = R$drawable.fingerprint_dialog_fp_to_error;
        }
        return ((LinearLayout) this).mContext.getDrawable(i3);
    }
}
