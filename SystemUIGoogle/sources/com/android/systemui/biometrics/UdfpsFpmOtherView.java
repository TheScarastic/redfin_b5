package com.android.systemui.biometrics;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.android.systemui.R$id;
/* loaded from: classes.dex */
public class UdfpsFpmOtherView extends UdfpsAnimationView {
    private final UdfpsFpDrawable mFingerprintDrawable;
    private ImageView mFingerprintView;

    @Override // com.android.systemui.biometrics.UdfpsAnimationView
    public /* bridge */ /* synthetic */ void onExpansionChanged(float f, boolean z) {
        super.onExpansionChanged(f, z);
    }

    public UdfpsFpmOtherView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mFingerprintDrawable = new UdfpsFpDrawable(context);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        ImageView imageView = (ImageView) findViewById(R$id.udfps_fpm_other_fp_view);
        this.mFingerprintView = imageView;
        imageView.setImageDrawable(this.mFingerprintDrawable);
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationView
    UdfpsDrawable getDrawable() {
        return this.mFingerprintDrawable;
    }
}
