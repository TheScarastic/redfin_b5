package com.android.launcher3.icons;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Property;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
/* loaded from: classes.dex */
public class FastBitmapDrawable extends Drawable {
    private static final Interpolator ACCEL = new AccelerateInterpolator();
    private static final Interpolator DEACCEL = new DecelerateInterpolator();
    private static final Property<FastBitmapDrawable, Float> SCALE = new Property<FastBitmapDrawable, Float>(Float.TYPE, "scale") { // from class: com.android.launcher3.icons.FastBitmapDrawable.1
        public Float get(FastBitmapDrawable fastBitmapDrawable) {
            return Float.valueOf(fastBitmapDrawable.mScale);
        }

        public void set(FastBitmapDrawable fastBitmapDrawable, Float f) {
            fastBitmapDrawable.mScale = f.floatValue();
            fastBitmapDrawable.invalidateSelf();
        }
    };
    private static ColorFilter sDisabledFColorFilter;
    private int mAlpha;
    protected Bitmap mBitmap;
    private ColorFilter mColorFilter;
    float mDisabledAlpha;
    protected final int mIconColor;
    protected boolean mIsDisabled;
    private boolean mIsPressed;
    protected final Paint mPaint;
    private float mScale;
    private ObjectAnimator mScaleAnimation;

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        return true;
    }

    public FastBitmapDrawable(Bitmap bitmap) {
        this(bitmap, 0);
    }

    protected FastBitmapDrawable(Bitmap bitmap, int i) {
        this(bitmap, i, false);
    }

    protected FastBitmapDrawable(Bitmap bitmap, int i, boolean z) {
        this.mPaint = new Paint(3);
        this.mDisabledAlpha = 1.0f;
        this.mScale = 1.0f;
        this.mAlpha = 255;
        this.mBitmap = bitmap;
        this.mIconColor = i;
        setFilterBitmap(true);
        setIsDisabled(z);
    }

    @Override // android.graphics.drawable.Drawable
    public final void draw(Canvas canvas) {
        if (this.mScale != 1.0f) {
            int save = canvas.save();
            Rect bounds = getBounds();
            float f = this.mScale;
            canvas.scale(f, f, bounds.exactCenterX(), bounds.exactCenterY());
            drawInternal(canvas, bounds);
            canvas.restoreToCount(save);
            return;
        }
        drawInternal(canvas, getBounds());
    }

    protected void drawInternal(Canvas canvas, Rect rect) {
        canvas.drawBitmap(this.mBitmap, (Rect) null, rect, this.mPaint);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.mColorFilter = colorFilter;
        updateFilter();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        if (this.mAlpha != i) {
            this.mAlpha = i;
            this.mPaint.setAlpha(i);
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setFilterBitmap(boolean z) {
        this.mPaint.setFilterBitmap(z);
        this.mPaint.setAntiAlias(z);
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.mAlpha;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.mBitmap.getWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.mBitmap.getHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumWidth() {
        return getBounds().width();
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumHeight() {
        return getBounds().height();
    }

    @Override // android.graphics.drawable.Drawable
    public ColorFilter getColorFilter() {
        return this.mPaint.getColorFilter();
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onStateChange(int[] iArr) {
        boolean z;
        int length = iArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                z = false;
                break;
            } else if (iArr[i] == 16842919) {
                z = true;
                break;
            } else {
                i++;
            }
        }
        if (this.mIsPressed == z) {
            return false;
        }
        this.mIsPressed = z;
        ObjectAnimator objectAnimator = this.mScaleAnimation;
        if (objectAnimator != null) {
            objectAnimator.cancel();
            this.mScaleAnimation = null;
        }
        if (this.mIsPressed) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, SCALE, 1.1f);
            this.mScaleAnimation = ofFloat;
            ofFloat.setDuration(200L);
            this.mScaleAnimation.setInterpolator(ACCEL);
            this.mScaleAnimation.start();
        } else if (isVisible()) {
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, SCALE, 1.0f);
            this.mScaleAnimation = ofFloat2;
            ofFloat2.setDuration(200L);
            this.mScaleAnimation.setInterpolator(DEACCEL);
            this.mScaleAnimation.start();
        } else {
            this.mScale = 1.0f;
            invalidateSelf();
        }
        return true;
    }

    public void setIsDisabled(boolean z) {
        if (this.mIsDisabled != z) {
            this.mIsDisabled = z;
            updateFilter();
        }
    }

    private ColorFilter getDisabledColorFilter() {
        if (sDisabledFColorFilter == null) {
            sDisabledFColorFilter = getDisabledFColorFilter(this.mDisabledAlpha);
        }
        return sDisabledFColorFilter;
    }

    protected void updateFilter() {
        this.mPaint.setColorFilter(this.mIsDisabled ? getDisabledColorFilter() : this.mColorFilter);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        return new FastBitmapConstantState(this.mBitmap, this.mIconColor, this.mIsDisabled);
    }

    public static ColorFilter getDisabledFColorFilter(float f) {
        ColorMatrix colorMatrix = new ColorMatrix();
        ColorMatrix colorMatrix2 = new ColorMatrix();
        colorMatrix2.setSaturation(0.0f);
        float[] array = colorMatrix.getArray();
        array[0] = 0.5f;
        array[6] = 0.5f;
        array[12] = 0.5f;
        float f2 = (float) 127;
        array[4] = f2;
        array[9] = f2;
        array[14] = f2;
        array[18] = f;
        colorMatrix2.preConcat(colorMatrix);
        return new ColorMatrixColorFilter(colorMatrix);
    }

    /* loaded from: classes.dex */
    protected static class FastBitmapConstantState extends Drawable.ConstantState {
        protected final Bitmap mBitmap;
        protected final int mIconColor;
        protected final boolean mIsDisabled;

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return 0;
        }

        public FastBitmapConstantState(Bitmap bitmap, int i, boolean z) {
            this.mBitmap = bitmap;
            this.mIconColor = i;
            this.mIsDisabled = z;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public FastBitmapDrawable newDrawable() {
            return new FastBitmapDrawable(this.mBitmap, this.mIconColor, this.mIsDisabled);
        }
    }
}
