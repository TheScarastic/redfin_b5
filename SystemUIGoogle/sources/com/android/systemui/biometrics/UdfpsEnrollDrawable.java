package com.android.systemui.biometrics;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.android.systemui.R$color;
import com.android.systemui.R$drawable;
/* loaded from: classes.dex */
public class UdfpsEnrollDrawable extends UdfpsDrawable {
    AnimatorSet mAnimatorSet;
    private final Paint mBlueFill;
    float mCurrentScale = 1.0f;
    float mCurrentX;
    float mCurrentY;
    private UdfpsEnrollHelper mEnrollHelper;
    private final Drawable mMovingTargetFpIcon;
    private final UdfpsEnrollProgressBarDrawable mProgressDrawable;
    private final Paint mSensorOutlinePaint;
    private RectF mSensorRect;

    public UdfpsEnrollDrawable(Context context) {
        super(context);
        this.mProgressDrawable = new UdfpsEnrollProgressBarDrawable(context, this);
        Paint paint = new Paint(0);
        this.mSensorOutlinePaint = paint;
        paint.setAntiAlias(true);
        Context context2 = this.mContext;
        int i = R$color.udfps_enroll_icon;
        paint.setColor(context2.getColor(i));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.0f);
        Paint paint2 = new Paint(0);
        this.mBlueFill = paint2;
        paint2.setAntiAlias(true);
        paint2.setColor(context.getColor(R$color.udfps_moving_target_fill));
        paint2.setStyle(Paint.Style.FILL);
        Drawable drawable = context.getResources().getDrawable(R$drawable.ic_fingerprint, null);
        this.mMovingTargetFpIcon = drawable;
        drawable.setTint(-1);
        drawable.mutate();
        this.mFingerprintDrawable.setTint(this.mContext.getColor(i));
    }

    public void setEnrollHelper(UdfpsEnrollHelper udfpsEnrollHelper) {
        this.mEnrollHelper = udfpsEnrollHelper;
    }

    @Override // com.android.systemui.biometrics.UdfpsDrawable
    public void onSensorRectUpdated(RectF rectF) {
        super.onSensorRectUpdated(rectF);
        this.mSensorRect = rectF;
    }

    @Override // com.android.systemui.biometrics.UdfpsDrawable
    public void updateFingerprintIconBounds(Rect rect) {
        super.updateFingerprintIconBounds(rect);
        this.mMovingTargetFpIcon.setBounds(rect);
        invalidateSelf();
    }

    public void onEnrollmentProgress(int i, int i2) {
        this.mProgressDrawable.setEnrollmentProgress(i, i2);
        if (this.mEnrollHelper.isCenterEnrollmentComplete()) {
            AnimatorSet animatorSet = this.mAnimatorSet;
            if (animatorSet != null && animatorSet.isRunning()) {
                this.mAnimatorSet.end();
            }
            PointF nextGuidedEnrollmentPoint = this.mEnrollHelper.getNextGuidedEnrollmentPoint();
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.mCurrentX, nextGuidedEnrollmentPoint.x);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.UdfpsEnrollDrawable$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    UdfpsEnrollDrawable.$r8$lambda$cKQtVJYAHrNydjOgzO4dykW_C1Y(UdfpsEnrollDrawable.this, valueAnimator);
                }
            });
            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(this.mCurrentY, nextGuidedEnrollmentPoint.y);
            ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.UdfpsEnrollDrawable$$ExternalSyntheticLambda2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    UdfpsEnrollDrawable.m80$r8$lambda$nzQudIMiAm7eDVmCx1Z3nOfMjc(UdfpsEnrollDrawable.this, valueAnimator);
                }
            });
            ValueAnimator ofFloat3 = ValueAnimator.ofFloat(0.0f, 3.1415927f);
            ofFloat3.setDuration(800L);
            ofFloat3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.UdfpsEnrollDrawable$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    UdfpsEnrollDrawable.$r8$lambda$Dch01jwrr13MKv6EnoiMNuGdtC4(UdfpsEnrollDrawable.this, valueAnimator);
                }
            });
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.mAnimatorSet = animatorSet2;
            animatorSet2.setInterpolator(new AccelerateDecelerateInterpolator());
            this.mAnimatorSet.setDuration(800L);
            this.mAnimatorSet.playTogether(ofFloat, ofFloat2, ofFloat3);
            this.mAnimatorSet.start();
        }
    }

    public /* synthetic */ void lambda$onEnrollmentProgress$0(ValueAnimator valueAnimator) {
        this.mCurrentX = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidateSelf();
    }

    public /* synthetic */ void lambda$onEnrollmentProgress$1(ValueAnimator valueAnimator) {
        this.mCurrentY = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidateSelf();
    }

    public /* synthetic */ void lambda$onEnrollmentProgress$2(ValueAnimator valueAnimator) {
        this.mCurrentScale = (((float) Math.sin((double) ((Float) valueAnimator.getAnimatedValue()).floatValue())) * 0.25f) + 1.0f;
        invalidateSelf();
    }

    public void onLastStepAcquired() {
        this.mProgressDrawable.onLastStepAcquired();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        this.mProgressDrawable.draw(canvas);
        if (!isIlluminationShowing()) {
            if (this.mEnrollHelper.isCenterEnrollmentComplete()) {
                canvas.save();
                canvas.translate(this.mCurrentX, this.mCurrentY);
                RectF rectF = this.mSensorRect;
                if (rectF != null) {
                    float f = this.mCurrentScale;
                    canvas.scale(f, f, rectF.centerX(), this.mSensorRect.centerY());
                    canvas.drawOval(this.mSensorRect, this.mBlueFill);
                }
                this.mMovingTargetFpIcon.draw(canvas);
                canvas.restore();
                return;
            }
            RectF rectF2 = this.mSensorRect;
            if (rectF2 != null) {
                canvas.drawOval(rectF2, this.mSensorOutlinePaint);
            }
            this.mFingerprintDrawable.draw(canvas);
            this.mFingerprintDrawable.setAlpha(this.mAlpha);
            this.mSensorOutlinePaint.setAlpha(this.mAlpha);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void onBoundsChange(Rect rect) {
        this.mProgressDrawable.setBounds(rect);
    }

    @Override // com.android.systemui.biometrics.UdfpsDrawable, android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        super.setAlpha(i);
        this.mSensorOutlinePaint.setAlpha(i);
        this.mBlueFill.setAlpha(i);
        this.mMovingTargetFpIcon.setAlpha(i);
        invalidateSelf();
    }
}
