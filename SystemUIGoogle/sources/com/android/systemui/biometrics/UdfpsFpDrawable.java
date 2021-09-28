package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.Canvas;
/* loaded from: classes.dex */
public class UdfpsFpDrawable extends UdfpsDrawable {
    /* access modifiers changed from: package-private */
    public UdfpsFpDrawable(Context context) {
        super(context);
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (!isIlluminationShowing()) {
            this.mFingerprintDrawable.draw(canvas);
        }
    }
}
