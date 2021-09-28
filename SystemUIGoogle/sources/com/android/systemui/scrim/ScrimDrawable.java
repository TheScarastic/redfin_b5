package com.android.systemui.scrim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.view.animation.DecelerateInterpolator;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.graphics.ColorUtils;
/* loaded from: classes.dex */
public class ScrimDrawable extends Drawable {
    private int mAlpha = 255;
    private int mBottomEdgePosition;
    private ValueAnimator mColorAnimation;
    private ConcaveInfo mConcaveInfo;
    private float mCornerRadius;
    private boolean mCornerRadiusEnabled;
    private int mMainColor;
    private int mMainColorTo;
    private final Paint mPaint;

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    public ScrimDrawable() {
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setStyle(Paint.Style.FILL);
    }

    public void setColor(int i, boolean z) {
        if (i != this.mMainColorTo) {
            ValueAnimator valueAnimator = this.mColorAnimation;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.mColorAnimation.cancel();
            }
            this.mMainColorTo = i;
            if (z) {
                int i2 = this.mMainColor;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                ofFloat.setDuration(2000L);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(i2, i) { // from class: com.android.systemui.scrim.ScrimDrawable$$ExternalSyntheticLambda0
                    public final /* synthetic */ int f$1;
                    public final /* synthetic */ int f$2;

                    {
                        this.f$1 = r2;
                        this.f$2 = r3;
                    }

                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        ScrimDrawable.this.lambda$setColor$0(this.f$1, this.f$2, valueAnimator2);
                    }
                });
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.scrim.ScrimDrawable.1
                    @Override // android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator, boolean z2) {
                        if (ScrimDrawable.this.mColorAnimation == animator) {
                            ScrimDrawable.this.mColorAnimation = null;
                        }
                    }
                });
                ofFloat.setInterpolator(new DecelerateInterpolator());
                ofFloat.start();
                this.mColorAnimation = ofFloat;
                return;
            }
            this.mMainColor = i;
            invalidateSelf();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setColor$0(int i, int i2, ValueAnimator valueAnimator) {
        this.mMainColor = ColorUtils.blendARGB(i, i2, ((Float) valueAnimator.getAnimatedValue()).floatValue());
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        if (i != this.mAlpha) {
            this.mAlpha = i;
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.mAlpha;
    }

    public void setXfermode(Xfermode xfermode) {
        this.mPaint.setXfermode(xfermode);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    @Override // android.graphics.drawable.Drawable
    public ColorFilter getColorFilter() {
        return this.mPaint.getColorFilter();
    }

    public void setRoundedCorners(float f) {
        if (f != this.mCornerRadius) {
            this.mCornerRadius = f;
            ConcaveInfo concaveInfo = this.mConcaveInfo;
            if (concaveInfo != null) {
                concaveInfo.setCornerRadius(f);
                updatePath();
            }
            invalidateSelf();
        }
    }

    public void setRoundedCornersEnabled(boolean z) {
        if (this.mCornerRadiusEnabled != z) {
            this.mCornerRadiusEnabled = z;
            invalidateSelf();
        }
    }

    public void setBottomEdgeConcave(boolean z) {
        if (!z || this.mConcaveInfo == null) {
            if (!z) {
                this.mConcaveInfo = null;
            } else {
                ConcaveInfo concaveInfo = new ConcaveInfo();
                this.mConcaveInfo = concaveInfo;
                concaveInfo.setCornerRadius(this.mCornerRadius);
            }
            invalidateSelf();
        }
    }

    public void setBottomEdgePosition(int i) {
        if (this.mBottomEdgePosition != i) {
            this.mBottomEdgePosition = i;
            if (this.mConcaveInfo != null) {
                updatePath();
                invalidateSelf();
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        this.mPaint.setColor(this.mMainColor);
        this.mPaint.setAlpha(this.mAlpha);
        if (this.mConcaveInfo != null) {
            drawConcave(canvas);
        } else if (!this.mCornerRadiusEnabled || this.mCornerRadius <= 0.0f) {
            canvas.drawRect((float) getBounds().left, (float) getBounds().top, (float) getBounds().right, (float) getBounds().bottom, this.mPaint);
        } else {
            float f = this.mCornerRadius;
            canvas.drawRoundRect((float) getBounds().left, (float) getBounds().top, (float) getBounds().right, ((float) getBounds().bottom) + f, f, f, this.mPaint);
        }
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        updatePath();
    }

    private void drawConcave(Canvas canvas) {
        canvas.clipOutPath(this.mConcaveInfo.mPath);
        canvas.drawRect((float) getBounds().left, (float) getBounds().top, (float) getBounds().right, ((float) this.mBottomEdgePosition) + this.mConcaveInfo.mPathOverlap, this.mPaint);
    }

    private void updatePath() {
        ConcaveInfo concaveInfo = this.mConcaveInfo;
        if (concaveInfo != null) {
            concaveInfo.mPath.reset();
            int i = this.mBottomEdgePosition;
            this.mConcaveInfo.mPath.addRoundRect((float) getBounds().left, (float) i, (float) getBounds().right, ((float) i) + this.mConcaveInfo.mPathOverlap, this.mConcaveInfo.mCornerRadii, Path.Direction.CW);
        }
    }

    @VisibleForTesting
    public int getMainColor() {
        return this.mMainColor;
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ConcaveInfo {
        private float mPathOverlap;
        private final Path mPath = new Path();
        private final float[] mCornerRadii = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};

        ConcaveInfo() {
        }

        public void setCornerRadius(float f) {
            this.mPathOverlap = f;
            float[] fArr = this.mCornerRadii;
            fArr[0] = f;
            fArr[1] = f;
            fArr[2] = f;
            fArr[3] = f;
        }
    }
}
