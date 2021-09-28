package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.android.settingslib.Utils;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.doze.util.BurnInHelperKt;
/* loaded from: classes.dex */
public class UdfpsKeyguardView extends UdfpsAnimationView {
    private int mAlpha;
    private LottieAnimationView mAodFp;
    private AnimatorSet mBackgroundInAnimator = new AnimatorSet();
    private ImageView mBgProtection;
    private float mBurnInOffsetX;
    private float mBurnInOffsetY;
    private float mBurnInProgress;
    private UdfpsDrawable mFingerprintDrawable;
    private float mInterpolatedDarkAmount;
    private LottieAnimationView mLockScreenFp;
    private final int mMaxBurnInOffsetX;
    private final int mMaxBurnInOffsetY;
    private int mStatusBarState;
    private int mTextColorPrimary;
    boolean mUdfpsRequested;

    /* access modifiers changed from: package-private */
    @Override // com.android.systemui.biometrics.UdfpsAnimationView
    public void onIlluminationStarting() {
    }

    /* access modifiers changed from: package-private */
    @Override // com.android.systemui.biometrics.UdfpsAnimationView
    public void onIlluminationStopped() {
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationView
    public /* bridge */ /* synthetic */ void onExpansionChanged(float f, boolean z) {
        super.onExpansionChanged(f, z);
    }

    public UdfpsKeyguardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mFingerprintDrawable = new UdfpsFpDrawable(context);
        this.mMaxBurnInOffsetX = context.getResources().getDimensionPixelSize(R$dimen.udfps_burn_in_offset_x);
        this.mMaxBurnInOffsetY = context.getResources().getDimensionPixelSize(R$dimen.udfps_burn_in_offset_y);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mAodFp = (LottieAnimationView) findViewById(R$id.udfps_aod_fp);
        this.mLockScreenFp = (LottieAnimationView) findViewById(R$id.udfps_lockscreen_fp);
        this.mBgProtection = (ImageView) findViewById(R$id.udfps_keyguard_fp_bg);
        updateColor();
        this.mLockScreenFp.addValueCallback(new KeyPath("**"), (KeyPath) LottieProperty.COLOR_FILTER, (SimpleLottieValueCallback<KeyPath>) new SimpleLottieValueCallback() { // from class: com.android.systemui.biometrics.UdfpsKeyguardView$$ExternalSyntheticLambda0
            @Override // com.airbnb.lottie.value.SimpleLottieValueCallback
            public final Object getValue(LottieFrameInfo lottieFrameInfo) {
                return UdfpsKeyguardView.m82$r8$lambda$CJLmLBBLFJ10m6c0LBzrMDl5xw(UdfpsKeyguardView.this, lottieFrameInfo);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ ColorFilter lambda$onFinishInflate$0(LottieFrameInfo lottieFrameInfo) {
        return new PorterDuffColorFilter(this.mTextColorPrimary, PorterDuff.Mode.SRC_ATOP);
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationView
    public UdfpsDrawable getDrawable() {
        return this.mFingerprintDrawable;
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationView
    public boolean dozeTimeTick() {
        updateBurnInOffsets();
        return true;
    }

    private void updateBurnInOffsets() {
        this.mBurnInOffsetX = MathUtils.lerp(0.0f, (float) (BurnInHelperKt.getBurnInOffset(this.mMaxBurnInOffsetX * 2, true) - this.mMaxBurnInOffsetX), this.mInterpolatedDarkAmount);
        this.mBurnInOffsetY = MathUtils.lerp(0.0f, (float) (BurnInHelperKt.getBurnInOffset(this.mMaxBurnInOffsetY * 2, false) - this.mMaxBurnInOffsetY), this.mInterpolatedDarkAmount);
        this.mBurnInProgress = MathUtils.lerp(0.0f, BurnInHelperKt.getBurnInProgressOffset(), this.mInterpolatedDarkAmount);
        this.mAodFp.setTranslationX(this.mBurnInOffsetX);
        this.mAodFp.setTranslationY(this.mBurnInOffsetY);
        this.mAodFp.setProgress(this.mBurnInProgress);
        this.mAodFp.setAlpha(this.mInterpolatedDarkAmount * 255.0f);
        this.mLockScreenFp.setTranslationX(this.mBurnInOffsetX);
        this.mLockScreenFp.setTranslationY(this.mBurnInOffsetY);
        this.mLockScreenFp.setProgress(1.0f - this.mInterpolatedDarkAmount);
        this.mLockScreenFp.setAlpha((1.0f - this.mInterpolatedDarkAmount) * 255.0f);
    }

    /* access modifiers changed from: package-private */
    public void requestUdfps(boolean z, int i) {
        this.mUdfpsRequested = z;
    }

    /* access modifiers changed from: package-private */
    public void setStatusBarState(int i) {
        this.mStatusBarState = i;
    }

    /* access modifiers changed from: package-private */
    public void updateColor() {
        this.mTextColorPrimary = Utils.getColorAttrDefaultColor(((FrameLayout) this).mContext, 16842806);
        this.mBgProtection.setImageDrawable(getContext().getDrawable(R$drawable.fingerprint_bg));
        this.mLockScreenFp.invalidate();
    }

    /* access modifiers changed from: package-private */
    public void setUnpausedAlpha(int i) {
        this.mAlpha = i;
        updateAlpha();
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.biometrics.UdfpsAnimationView
    public int updateAlpha() {
        int updateAlpha = super.updateAlpha();
        float f = ((float) updateAlpha) / 255.0f;
        this.mLockScreenFp.setAlpha(f);
        float f2 = this.mInterpolatedDarkAmount;
        if (f2 != 0.0f) {
            this.mBgProtection.setAlpha(1.0f - f2);
        } else {
            this.mBgProtection.setAlpha(f);
        }
        return updateAlpha;
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationView
    int calculateAlpha() {
        if (this.mPauseAuth) {
            return 0;
        }
        return this.mAlpha;
    }

    /* access modifiers changed from: package-private */
    public void onDozeAmountChanged(float f, float f2) {
        this.mInterpolatedDarkAmount = f2;
        updateAlpha();
        updateBurnInOffsets();
    }

    /* access modifiers changed from: package-private */
    public void animateInUdfpsBouncer(final Runnable runnable) {
        if (!this.mBackgroundInAnimator.isRunning()) {
            AnimatorSet animatorSet = new AnimatorSet();
            this.mBackgroundInAnimator = animatorSet;
            animatorSet.playTogether(ObjectAnimator.ofFloat(this.mBgProtection, View.ALPHA, 0.0f, 1.0f), ObjectAnimator.ofFloat(this.mBgProtection, View.SCALE_X, 0.0f, 1.0f), ObjectAnimator.ofFloat(this.mBgProtection, View.SCALE_Y, 0.0f, 1.0f));
            this.mBackgroundInAnimator.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            this.mBackgroundInAnimator.setDuration(500L);
            this.mBackgroundInAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.biometrics.UdfpsKeyguardView.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    Runnable runnable2 = runnable;
                    if (runnable2 != null) {
                        runnable2.run();
                    }
                }
            });
            this.mBackgroundInAnimator.start();
        }
    }
}
