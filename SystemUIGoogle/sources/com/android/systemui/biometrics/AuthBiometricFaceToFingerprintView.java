package com.android.systemui.biometrics;

import android.content.Context;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.biometrics.AuthBiometricFaceView;
import com.android.systemui.biometrics.AuthBiometricView;
import com.android.systemui.biometrics.AuthDialog;
/* loaded from: classes.dex */
public class AuthBiometricFaceToFingerprintView extends AuthBiometricFaceView {
    private int mActiveSensorType = 8;
    private FingerprintSensorPropertiesInternal mFingerprintSensorProps;
    private ModalityListener mModalityListener;
    @VisibleForTesting
    UdfpsIconController mUdfpsIconController;
    private UdfpsDialogMeasureAdapter mUdfpsMeasureAdapter;

    @Override // com.android.systemui.biometrics.AuthBiometricFaceView, com.android.systemui.biometrics.AuthBiometricView
    protected boolean supportsManualRetry() {
        return false;
    }

    /* loaded from: classes.dex */
    protected static class UdfpsIconController extends AuthBiometricFaceView.IconController {
        private int mIconState = 0;

        protected UdfpsIconController(Context context, ImageView imageView, TextView textView) {
            super(context, imageView, textView);
        }

        void updateState(int i) {
            updateState(this.mIconState, i);
        }

        @Override // com.android.systemui.biometrics.AuthBiometricFaceView.IconController
        protected void updateState(int i, int i2) {
            boolean z = i == 4 || i == 3;
            switch (i2) {
                case 0:
                case 1:
                case 2:
                case 5:
                case 6:
                    if (z) {
                        animateOnce(R$drawable.fingerprint_dialog_error_to_fp);
                    } else {
                        showStaticDrawable(R$drawable.fingerprint_dialog_fp_to_error);
                    }
                    this.mIconView.setContentDescription(this.mContext.getString(R$string.accessibility_fingerprint_dialog_fingerprint_icon));
                    break;
                case 3:
                case 4:
                    if (!z) {
                        animateOnce(R$drawable.fingerprint_dialog_fp_to_error);
                    } else {
                        showStaticDrawable(R$drawable.fingerprint_dialog_error_to_fp);
                    }
                    this.mIconView.setContentDescription(this.mContext.getString(R$string.biometric_dialog_try_again));
                    break;
                default:
                    Log.e("BiometricPrompt/AuthBiometricFaceToFingerprintView", "Unknown biometric dialog state: " + i2);
                    break;
            }
            this.mState = i2;
            this.mIconState = i2;
        }
    }

    public AuthBiometricFaceToFingerprintView(Context context) {
        super(context);
    }

    public AuthBiometricFaceToFingerprintView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @VisibleForTesting
    AuthBiometricFaceToFingerprintView(Context context, AttributeSet attributeSet, AuthBiometricView.Injector injector) {
        super(context, attributeSet, injector);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.biometrics.AuthBiometricFaceView, com.android.systemui.biometrics.AuthBiometricView, android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mUdfpsIconController = new UdfpsIconController(((LinearLayout) this).mContext, this.mIconView, this.mIndicatorView);
    }

    /* access modifiers changed from: package-private */
    public int getActiveSensorType() {
        return this.mActiveSensorType;
    }

    /* access modifiers changed from: package-private */
    public boolean isFingerprintUdfps() {
        return this.mFingerprintSensorProps.isAnyUdfpsType();
    }

    /* access modifiers changed from: package-private */
    public void setModalityListener(ModalityListener modalityListener) {
        this.mModalityListener = modalityListener;
    }

    /* access modifiers changed from: package-private */
    public void setFingerprintSensorProps(FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal) {
        this.mFingerprintSensorProps = fingerprintSensorPropertiesInternal;
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.biometrics.AuthBiometricFaceView, com.android.systemui.biometrics.AuthBiometricView
    public int getDelayAfterAuthenticatedDurationMs() {
        if (this.mActiveSensorType == 2) {
            return 0;
        }
        return super.getDelayAfterAuthenticatedDurationMs();
    }

    @Override // com.android.systemui.biometrics.AuthBiometricFaceView, com.android.systemui.biometrics.AuthBiometricView
    public void onAuthenticationFailed(int i, String str) {
        super.onAuthenticationFailed(i, checkErrorForFallback(str));
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public void onError(int i, String str) {
        super.onError(i, checkErrorForFallback(str));
    }

    private String checkErrorForFallback(String str) {
        if (this.mActiveSensorType != 8) {
            return str;
        }
        Log.d("BiometricPrompt/AuthBiometricFaceToFingerprintView", "Falling back to fingerprint: " + str);
        this.mCallback.onAction(7);
        return ((LinearLayout) this).mContext.getString(R$string.fingerprint_dialog_use_fingerprint_instead);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.biometrics.AuthBiometricFaceView, com.android.systemui.biometrics.AuthBiometricView
    public int getStateForAfterError() {
        if (this.mActiveSensorType == 8) {
            return 2;
        }
        return super.getStateForAfterError();
    }

    @Override // com.android.systemui.biometrics.AuthBiometricFaceView, com.android.systemui.biometrics.AuthBiometricView
    public void updateState(int i) {
        if (this.mActiveSensorType != 8) {
            this.mUdfpsIconController.updateState(i);
        } else if (i == 3 || i == 4) {
            this.mActiveSensorType = 2;
            setRequireConfirmation(false);
            this.mConfirmButton.setEnabled(false);
            this.mConfirmButton.setVisibility(8);
            ModalityListener modalityListener = this.mModalityListener;
            if (modalityListener != null) {
                modalityListener.onModalitySwitched(8, this.mActiveSensorType);
            }
            this.mFaceIconController.deactivate();
            this.mUdfpsIconController.updateState(0);
        }
        super.updateState(i);
    }

    /* access modifiers changed from: package-private */
    @Override // com.android.systemui.biometrics.AuthBiometricView
    public AuthDialog.LayoutParams onMeasureInternal(int i, int i2) {
        AuthDialog.LayoutParams onMeasureInternal = super.onMeasureInternal(i, i2);
        return isFingerprintUdfps() ? getUdfpsMeasureAdapter().onMeasureInternal(i, i2, onMeasureInternal) : onMeasureInternal;
    }

    private UdfpsDialogMeasureAdapter getUdfpsMeasureAdapter() {
        UdfpsDialogMeasureAdapter udfpsDialogMeasureAdapter = this.mUdfpsMeasureAdapter;
        if (udfpsDialogMeasureAdapter == null || udfpsDialogMeasureAdapter.getSensorProps() != this.mFingerprintSensorProps) {
            this.mUdfpsMeasureAdapter = new UdfpsDialogMeasureAdapter(this, this.mFingerprintSensorProps);
        }
        return this.mUdfpsMeasureAdapter;
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public void onSaveState(Bundle bundle) {
        super.onSaveState(bundle);
        bundle.putInt("sensor_type", this.mActiveSensorType);
        bundle.putParcelable("sensor_props", this.mFingerprintSensorProps);
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public void restoreState(Bundle bundle) {
        super.restoreState(bundle);
        if (bundle != null) {
            this.mActiveSensorType = bundle.getInt("sensor_type", 8);
            this.mFingerprintSensorProps = bundle.getParcelable("sensor_props");
        }
    }
}
