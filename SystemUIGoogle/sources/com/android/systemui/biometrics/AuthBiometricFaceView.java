package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.biometrics.AuthBiometricView;
/* loaded from: classes.dex */
public class AuthBiometricFaceView extends AuthBiometricView {
    @VisibleForTesting
    IconController mFaceIconController;

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.biometrics.AuthBiometricView
    public int getDelayAfterAuthenticatedDurationMs() {
        return 500;
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.biometrics.AuthBiometricView
    public int getStateForAfterError() {
        return 0;
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    protected boolean supportsManualRetry() {
        return true;
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    protected boolean supportsSmallDialog() {
        return true;
    }

    /* loaded from: classes.dex */
    protected static class IconController extends Animatable2.AnimationCallback {
        protected Context mContext;
        protected boolean mDeactivated;
        protected Handler mHandler = new Handler(Looper.getMainLooper());
        protected ImageView mIconView;
        protected boolean mLastPulseLightToDark;
        protected int mState;
        protected TextView mTextView;

        /* access modifiers changed from: protected */
        public IconController(Context context, ImageView imageView, TextView textView) {
            this.mContext = context;
            this.mIconView = imageView;
            this.mTextView = textView;
            showStaticDrawable(R$drawable.face_dialog_pulse_dark_to_light);
        }

        /* access modifiers changed from: protected */
        public void animateOnce(int i) {
            animateIcon(i, false);
        }

        /* access modifiers changed from: protected */
        public void showStaticDrawable(int i) {
            this.mIconView.setImageDrawable(this.mContext.getDrawable(i));
        }

        protected void animateIcon(int i, boolean z) {
            Log.d("BiometricPrompt/AuthBiometricFaceView", "animateIcon, state: " + this.mState + ", deactivated: " + this.mDeactivated);
            if (!this.mDeactivated) {
                AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) this.mContext.getDrawable(i);
                this.mIconView.setImageDrawable(animatedVectorDrawable);
                animatedVectorDrawable.forceAnimationOnUI();
                if (z) {
                    animatedVectorDrawable.registerAnimationCallback(this);
                }
                animatedVectorDrawable.start();
            }
        }

        protected void startPulsing() {
            this.mLastPulseLightToDark = false;
            animateIcon(R$drawable.face_dialog_pulse_dark_to_light, true);
        }

        protected void pulseInNextDirection() {
            int i;
            if (this.mLastPulseLightToDark) {
                i = R$drawable.face_dialog_pulse_dark_to_light;
            } else {
                i = R$drawable.face_dialog_pulse_light_to_dark;
            }
            animateIcon(i, true);
            this.mLastPulseLightToDark = !this.mLastPulseLightToDark;
        }

        @Override // android.graphics.drawable.Animatable2.AnimationCallback
        public void onAnimationEnd(Drawable drawable) {
            super.onAnimationEnd(drawable);
            Log.d("BiometricPrompt/AuthBiometricFaceView", "onAnimationEnd, mState: " + this.mState + ", deactivated: " + this.mDeactivated);
            if (!this.mDeactivated) {
                int i = this.mState;
                if (i == 2 || i == 3) {
                    pulseInNextDirection();
                }
            }
        }

        /* access modifiers changed from: protected */
        public void deactivate() {
            this.mDeactivated = true;
        }

        protected void updateState(int i, int i2) {
            if (this.mDeactivated) {
                Log.w("BiometricPrompt/AuthBiometricFaceView", "Ignoring updateState when deactivated: " + i2);
                return;
            }
            boolean z = i == 4 || i == 3;
            if (i2 == 1) {
                showStaticDrawable(R$drawable.face_dialog_pulse_dark_to_light);
                this.mIconView.setContentDescription(this.mContext.getString(R$string.biometric_dialog_face_icon_description_authenticating));
            } else if (i2 == 2) {
                startPulsing();
                this.mIconView.setContentDescription(this.mContext.getString(R$string.biometric_dialog_face_icon_description_authenticating));
            } else if (i == 5 && i2 == 6) {
                animateOnce(R$drawable.face_dialog_dark_to_checkmark);
                this.mIconView.setContentDescription(this.mContext.getString(R$string.biometric_dialog_face_icon_description_confirmed));
            } else if (z && i2 == 0) {
                animateOnce(R$drawable.face_dialog_error_to_idle);
                this.mIconView.setContentDescription(this.mContext.getString(R$string.biometric_dialog_face_icon_description_idle));
            } else if (z && i2 == 6) {
                animateOnce(R$drawable.face_dialog_dark_to_checkmark);
                this.mIconView.setContentDescription(this.mContext.getString(R$string.biometric_dialog_face_icon_description_authenticated));
            } else if (i2 == 4 && i != 4) {
                animateOnce(R$drawable.face_dialog_dark_to_error);
            } else if (i == 2 && i2 == 6) {
                animateOnce(R$drawable.face_dialog_dark_to_checkmark);
                this.mIconView.setContentDescription(this.mContext.getString(R$string.biometric_dialog_face_icon_description_authenticated));
            } else if (i2 == 5) {
                animateOnce(R$drawable.face_dialog_wink_from_dark);
                this.mIconView.setContentDescription(this.mContext.getString(R$string.biometric_dialog_face_icon_description_authenticated));
            } else if (i2 == 0) {
                showStaticDrawable(R$drawable.face_dialog_idle_static);
                this.mIconView.setContentDescription(this.mContext.getString(R$string.biometric_dialog_face_icon_description_idle));
            } else {
                Log.w("BiometricPrompt/AuthBiometricFaceView", "Unhandled state: " + i2);
            }
            this.mState = i2;
        }
    }

    public AuthBiometricFaceView(Context context) {
        this(context, null);
    }

    public AuthBiometricFaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public AuthBiometricFaceView(Context context, AttributeSet attributeSet, AuthBiometricView.Injector injector) {
        super(context, attributeSet, injector);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.biometrics.AuthBiometricView, android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mFaceIconController = new IconController(((LinearLayout) this).mContext, this.mIconView, this.mIndicatorView);
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    protected void handleResetAfterError() {
        resetErrorView();
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    protected void handleResetAfterHelp() {
        resetErrorView();
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public void updateState(int i) {
        this.mFaceIconController.updateState(this.mState, i);
        if (i == 1 || (i == 2 && getSize() == 2)) {
            resetErrorView();
        }
        super.updateState(i);
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public void onAuthenticationFailed(int i, String str) {
        if (getSize() == 2 && supportsManualRetry()) {
            this.mTryAgainButton.setVisibility(0);
            this.mConfirmButton.setVisibility(8);
        }
        super.onAuthenticationFailed(i, str);
    }

    private void resetErrorView() {
        this.mIndicatorView.setTextColor(this.mTextColorHint);
        this.mIndicatorView.setVisibility(4);
    }
}
