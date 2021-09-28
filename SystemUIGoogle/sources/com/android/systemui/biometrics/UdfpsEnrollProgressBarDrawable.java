package com.android.systemui.biometrics;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import com.android.systemui.R$color;
/* loaded from: classes.dex */
public class UdfpsEnrollProgressBarDrawable extends Drawable {
    private final Paint mBackgroundCirclePaint;
    private final Context mContext;
    private boolean mLastStepAcquired;
    private final UdfpsEnrollDrawable mParent;
    private float mProgress;
    private ValueAnimator mProgressAnimator;
    private final Paint mProgressPaint;
    private int mRotation;

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return 0;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public UdfpsEnrollProgressBarDrawable(Context context, UdfpsEnrollDrawable udfpsEnrollDrawable) {
        this.mContext = context;
        this.mParent = udfpsEnrollDrawable;
        Paint paint = new Paint();
        this.mBackgroundCirclePaint = paint;
        paint.setStrokeWidth(Utils.dpToPixels(context, 12.0f));
        paint.setColor(context.getColor(R$color.white_disabled));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{16843817});
        paint.setColor(obtainStyledAttributes.getColor(0, paint.getColor()));
        obtainStyledAttributes.recycle();
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(16842803, typedValue, true);
        paint.setAlpha((int) (typedValue.getFloat() * 255.0f));
        Paint paint2 = new Paint();
        this.mProgressPaint = paint2;
        paint2.setStrokeWidth(Utils.dpToPixels(context, 12.0f));
        paint2.setColor(context.getColor(R$color.udfps_enroll_progress));
        paint2.setAntiAlias(true);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeCap(Paint.Cap.ROUND);
    }

    /* access modifiers changed from: package-private */
    public void setEnrollmentProgress(int i, int i2) {
        setEnrollmentProgress(((float) ((i2 - i) + 1)) / ((float) (i2 + 1)));
    }

    private void setEnrollmentProgress(float f) {
        if (!this.mLastStepAcquired) {
            long j = 150;
            if (f == 1.0f) {
                j = 400;
                ValueAnimator ofInt = ValueAnimator.ofInt(0, 400);
                ofInt.setDuration(400L);
                ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.UdfpsEnrollProgressBarDrawable$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        UdfpsEnrollProgressBarDrawable.this.lambda$setEnrollmentProgress$0(valueAnimator);
                    }
                });
                ofInt.start();
            }
            ValueAnimator valueAnimator = this.mProgressAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.mProgressAnimator.cancel();
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.mProgress, f);
            this.mProgressAnimator = ofFloat;
            ofFloat.setDuration(j);
            this.mProgressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.UdfpsEnrollProgressBarDrawable$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    UdfpsEnrollProgressBarDrawable.this.lambda$setEnrollmentProgress$1(valueAnimator2);
                }
            });
            this.mProgressAnimator.start();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setEnrollmentProgress$0(ValueAnimator valueAnimator) {
        Log.d("UdfpsEnrollProgressBarDrawable", "Rotation: " + this.mRotation);
        this.mRotation = ((Integer) valueAnimator.getAnimatedValue()).intValue();
        this.mParent.invalidateSelf();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setEnrollmentProgress$1(ValueAnimator valueAnimator) {
        this.mProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.mParent.invalidateSelf();
    }

    /* access modifiers changed from: package-private */
    public void onLastStepAcquired() {
        setEnrollmentProgress(1.0f);
        this.mLastStepAcquired = true;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.rotate((float) (this.mRotation - 90), (float) getBounds().centerX(), (float) getBounds().centerY());
        float dpToPixels = Utils.dpToPixels(this.mContext, 12.0f) / 2.0f;
        canvas.drawArc(dpToPixels, dpToPixels, ((float) getBounds().right) - dpToPixels, ((float) getBounds().bottom) - dpToPixels, 0.0f, 360.0f, false, this.mBackgroundCirclePaint);
        canvas.drawArc(dpToPixels, dpToPixels, ((float) getBounds().right) - dpToPixels, ((float) getBounds().bottom) - dpToPixels, 0.0f, this.mProgress * 360.0f, false, this.mProgressPaint);
        canvas.restore();
    }
}
