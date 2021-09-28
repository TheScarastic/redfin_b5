package com.android.systemui.biometrics;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Outline;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.android.systemui.R$dimen;
/* loaded from: classes.dex */
public class AuthPanelController extends ViewOutlineProvider {
    private int mContainerHeight;
    private int mContainerWidth;
    private int mContentHeight;
    private int mContentWidth;
    private final Context mContext;
    private float mCornerRadius;
    private int mMargin;
    private final View mPanelView;
    private int mPosition = 1;
    private boolean mUseFullScreen;

    @Override // android.view.ViewOutlineProvider
    public void getOutline(View view, Outline outline) {
        int leftBound = getLeftBound(this.mPosition);
        int i = leftBound + this.mContentWidth;
        int topBound = getTopBound(this.mPosition);
        outline.setRoundRect(leftBound, topBound, i, Math.min(this.mContentHeight + topBound, this.mContainerHeight - this.mMargin), this.mCornerRadius);
    }

    private int getLeftBound(int i) {
        if (i == 1) {
            return (this.mContainerWidth - this.mContentWidth) / 2;
        }
        if (i == 2) {
            return this.mMargin;
        }
        if (i == 3) {
            return (this.mContainerWidth - this.mContentWidth) - this.mMargin;
        }
        Log.e("BiometricPrompt/AuthPanelController", "Unrecognized position: " + i);
        return getLeftBound(1);
    }

    private int getTopBound(int i) {
        if (i == 1) {
            int i2 = this.mMargin;
            return Math.max((this.mContainerHeight - this.mContentHeight) - i2, i2);
        } else if (i == 2 || i == 3) {
            return Math.max((this.mContainerHeight - this.mContentHeight) / 2, this.mMargin);
        } else {
            Log.e("BiometricPrompt/AuthPanelController", "Unrecognized position: " + i);
            return getTopBound(1);
        }
    }

    public void setContainerDimensions(int i, int i2) {
        this.mContainerWidth = i;
        this.mContainerHeight = i2;
    }

    public void setPosition(int i) {
        this.mPosition = i;
    }

    public void setUseFullScreen(boolean z) {
        this.mUseFullScreen = z;
    }

    public void updateForContentDimensions(int i, int i2, int i3) {
        int i4;
        float f;
        if (this.mContainerWidth == 0 || this.mContainerHeight == 0) {
            Log.w("BiometricPrompt/AuthPanelController", "Not done measuring yet");
            return;
        }
        if (this.mUseFullScreen) {
            i4 = 0;
        } else {
            i4 = (int) this.mContext.getResources().getDimension(R$dimen.biometric_dialog_border_padding);
        }
        if (this.mUseFullScreen) {
            f = 0.0f;
        } else {
            f = this.mContext.getResources().getDimension(R$dimen.biometric_dialog_corner_size);
        }
        if (i3 > 0) {
            ValueAnimator ofInt = ValueAnimator.ofInt(this.mMargin, i4);
            ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.AuthPanelController$$ExternalSyntheticLambda2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    AuthPanelController.this.lambda$updateForContentDimensions$2(valueAnimator);
                }
            });
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.mCornerRadius, f);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.AuthPanelController$$ExternalSyntheticLambda3
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    AuthPanelController.this.lambda$updateForContentDimensions$3(valueAnimator);
                }
            });
            ValueAnimator ofInt2 = ValueAnimator.ofInt(this.mContentHeight, i2);
            ofInt2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.AuthPanelController$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    AuthPanelController.this.lambda$updateForContentDimensions$4(valueAnimator);
                }
            });
            ValueAnimator ofInt3 = ValueAnimator.ofInt(this.mContentWidth, i);
            ofInt3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.AuthPanelController$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    AuthPanelController.this.lambda$updateForContentDimensions$5(valueAnimator);
                }
            });
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration((long) i3);
            animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
            animatorSet.playTogether(ofFloat, ofInt2, ofInt3, ofInt);
            animatorSet.start();
            return;
        }
        this.mMargin = i4;
        this.mCornerRadius = f;
        this.mContentWidth = i;
        this.mContentHeight = i2;
        this.mPanelView.invalidateOutline();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateForContentDimensions$2(ValueAnimator valueAnimator) {
        this.mMargin = ((Integer) valueAnimator.getAnimatedValue()).intValue();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateForContentDimensions$3(ValueAnimator valueAnimator) {
        this.mCornerRadius = ((Float) valueAnimator.getAnimatedValue()).floatValue();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateForContentDimensions$4(ValueAnimator valueAnimator) {
        this.mContentHeight = ((Integer) valueAnimator.getAnimatedValue()).intValue();
        this.mPanelView.invalidateOutline();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateForContentDimensions$5(ValueAnimator valueAnimator) {
        this.mContentWidth = ((Integer) valueAnimator.getAnimatedValue()).intValue();
    }

    /* access modifiers changed from: package-private */
    public int getContainerWidth() {
        return this.mContainerWidth;
    }

    /* access modifiers changed from: package-private */
    public int getContainerHeight() {
        return this.mContainerHeight;
    }

    /* access modifiers changed from: package-private */
    public AuthPanelController(Context context, View view) {
        this.mContext = context;
        this.mPanelView = view;
        this.mCornerRadius = context.getResources().getDimension(R$dimen.biometric_dialog_corner_size);
        this.mMargin = (int) context.getResources().getDimension(R$dimen.biometric_dialog_border_padding);
        view.setOutlineProvider(this);
        view.setClipToOutline(true);
    }
}
