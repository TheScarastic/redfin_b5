package com.android.keyguard;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.VectorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.android.settingslib.Utils;
/* loaded from: classes.dex */
public class NumPadButton extends AlphaOptimizedImageButton {
    private NumPadAnimator mAnimator;
    private int mOrientation;

    public NumPadButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (getBackground() instanceof RippleDrawable) {
            this.mAnimator = new NumPadAnimator(context, (RippleDrawable) getBackground(), attributeSet.getStyleAttribute());
        } else {
            this.mAnimator = null;
        }
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        this.mOrientation = configuration.orientation;
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int measuredWidth = getMeasuredWidth();
        if (this.mAnimator == null || this.mOrientation == 2) {
            measuredWidth = (int) (((float) measuredWidth) * 0.66f);
        }
        setMeasuredDimension(getMeasuredWidth(), measuredWidth);
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        NumPadAnimator numPadAnimator = this.mAnimator;
        if (numPadAnimator != null) {
            numPadAnimator.onLayout(i4 - i2);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        NumPadAnimator numPadAnimator;
        if (motionEvent.getActionMasked() == 0 && (numPadAnimator = this.mAnimator) != null) {
            numPadAnimator.start();
        }
        return super.onTouchEvent(motionEvent);
    }

    public void reloadColors() {
        NumPadAnimator numPadAnimator = this.mAnimator;
        if (numPadAnimator != null) {
            numPadAnimator.reloadColors(getContext());
        }
        ((VectorDrawable) getDrawable()).setTintList(ColorStateList.valueOf(Utils.getColorAttrDefaultColor(getContext(), 16842801)));
    }
}
