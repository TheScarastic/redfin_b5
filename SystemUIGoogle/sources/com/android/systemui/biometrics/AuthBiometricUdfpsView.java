package com.android.systemui.biometrics;

import android.content.Context;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.util.AttributeSet;
import com.android.systemui.biometrics.AuthDialog;
/* loaded from: classes.dex */
public class AuthBiometricUdfpsView extends AuthBiometricFingerprintView {
    private UdfpsDialogMeasureAdapter mMeasureAdapter;

    public AuthBiometricUdfpsView(Context context) {
        this(context, null);
    }

    public AuthBiometricUdfpsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: package-private */
    public void setSensorProps(FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal) {
        UdfpsDialogMeasureAdapter udfpsDialogMeasureAdapter = this.mMeasureAdapter;
        if (udfpsDialogMeasureAdapter == null || udfpsDialogMeasureAdapter.getSensorProps() != fingerprintSensorPropertiesInternal) {
            this.mMeasureAdapter = new UdfpsDialogMeasureAdapter(this, fingerprintSensorPropertiesInternal);
        }
    }

    /* access modifiers changed from: package-private */
    @Override // com.android.systemui.biometrics.AuthBiometricView
    public AuthDialog.LayoutParams onMeasureInternal(int i, int i2) {
        AuthDialog.LayoutParams onMeasureInternal = super.onMeasureInternal(i, i2);
        UdfpsDialogMeasureAdapter udfpsDialogMeasureAdapter = this.mMeasureAdapter;
        return udfpsDialogMeasureAdapter != null ? udfpsDialogMeasureAdapter.onMeasureInternal(i, i2, onMeasureInternal) : onMeasureInternal;
    }
}
