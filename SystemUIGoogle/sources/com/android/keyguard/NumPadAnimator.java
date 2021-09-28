package com.android.keyguard;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.view.ContextThemeWrapper;
import com.android.systemui.R$id;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.util.Utils;
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class NumPadAnimator {
    private AnimatorSet mAnimator = new AnimatorSet();
    private GradientDrawable mBackground;
    private ValueAnimator mContractAnimator;
    private ValueAnimator mExpandAnimator;
    private int mHighlightColor;
    private int mNormalColor;
    private RippleDrawable mRipple;
    private int mStyle;

    /* access modifiers changed from: package-private */
    public NumPadAnimator(Context context, RippleDrawable rippleDrawable, int i) {
        this.mStyle = i;
        RippleDrawable rippleDrawable2 = (RippleDrawable) rippleDrawable.mutate();
        this.mRipple = rippleDrawable2;
        this.mBackground = (GradientDrawable) rippleDrawable2.findDrawableByLayerId(R$id.background);
        reloadColors(context);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mExpandAnimator = ofFloat;
        ofFloat.setDuration(50L);
        this.mExpandAnimator.setInterpolator(Interpolators.LINEAR);
        this.mExpandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.keyguard.NumPadAnimator.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                NumPadAnimator.this.mBackground.setCornerRadius(((Float) valueAnimator.getAnimatedValue()).floatValue());
                NumPadAnimator.this.mRipple.invalidateSelf();
            }
        });
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(1.0f, 0.0f);
        this.mContractAnimator = ofFloat2;
        ofFloat2.setStartDelay(33);
        this.mContractAnimator.setDuration(417L);
        this.mContractAnimator.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        this.mContractAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.keyguard.NumPadAnimator.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                NumPadAnimator.this.mBackground.setCornerRadius(((Float) valueAnimator.getAnimatedValue()).floatValue());
                NumPadAnimator.this.mRipple.invalidateSelf();
            }
        });
        this.mAnimator.playSequentially(this.mExpandAnimator, this.mContractAnimator);
    }

    /* access modifiers changed from: package-private */
    public void onLayout(int i) {
        float f = (float) i;
        float f2 = f / 2.0f;
        float f3 = f / 4.0f;
        this.mBackground.setCornerRadius(f2);
        this.mExpandAnimator.setFloatValues(f2, f3);
        this.mContractAnimator.setFloatValues(f3, f2);
    }

    /* access modifiers changed from: package-private */
    public void start() {
        this.mAnimator.cancel();
        this.mAnimator.start();
    }

    /* access modifiers changed from: package-private */
    public void reloadColors(Context context) {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, this.mStyle);
        TypedArray obtainStyledAttributes = contextThemeWrapper.obtainStyledAttributes(new int[]{16843817, 16843820});
        this.mNormalColor = Utils.getPrivateAttrColorIfUnset(contextThemeWrapper, obtainStyledAttributes, 0, 0, 17956909);
        this.mHighlightColor = obtainStyledAttributes.getColor(1, 0);
        obtainStyledAttributes.recycle();
        this.mBackground.setColor(this.mNormalColor);
        this.mRipple.setColor(ColorStateList.valueOf(this.mHighlightColor));
    }
}
