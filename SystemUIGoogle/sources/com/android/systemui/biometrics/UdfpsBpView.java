package com.android.systemui.biometrics;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
/* loaded from: classes.dex */
public class UdfpsBpView extends UdfpsAnimationView {
    private UdfpsFpDrawable mFingerprintDrawable = new UdfpsFpDrawable(((FrameLayout) this).mContext);

    @Override // com.android.systemui.biometrics.UdfpsAnimationView
    public /* bridge */ /* synthetic */ void onExpansionChanged(float f, boolean z) {
        super.onExpansionChanged(f, z);
    }

    public UdfpsBpView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationView
    UdfpsDrawable getDrawable() {
        return this.mFingerprintDrawable;
    }
}
