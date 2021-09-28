package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.util.PathParser;
import com.android.systemui.R$string;
/* loaded from: classes.dex */
public abstract class UdfpsDrawable extends Drawable {
    int mAlpha = 255;
    final Context mContext;
    final ShapeDrawable mFingerprintDrawable;
    private boolean mIlluminationShowing;
    private final Paint mPaint;

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return 0;
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public UdfpsDrawable(Context context) {
        this.mContext = context;
        ShapeDrawable shapeDrawable = new ShapeDrawable(new PathShape(PathParser.createPathFromPathData(context.getResources().getString(R$string.config_udfpsIcon)), 72.0f, 72.0f));
        this.mFingerprintDrawable = shapeDrawable;
        shapeDrawable.mutate();
        Paint paint = shapeDrawable.getPaint();
        this.mPaint = paint;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        setStrokeWidth(3.0f);
    }

    void setStrokeWidth(float f) {
        this.mPaint.setStrokeWidth(f);
        invalidateSelf();
    }

    public void onSensorRectUpdated(RectF rectF) {
        int height = ((int) rectF.height()) / 8;
        updateFingerprintIconBounds(new Rect(((int) rectF.left) + height, ((int) rectF.top) + height, ((int) rectF.right) - height, ((int) rectF.bottom) - height));
    }

    /* access modifiers changed from: protected */
    public void updateFingerprintIconBounds(Rect rect) {
        this.mFingerprintDrawable.setBounds(rect);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        this.mAlpha = i;
        this.mFingerprintDrawable.setAlpha(i);
        invalidateSelf();
    }

    /* access modifiers changed from: package-private */
    public boolean isIlluminationShowing() {
        return this.mIlluminationShowing;
    }

    /* access modifiers changed from: package-private */
    public void setIlluminationShowing(boolean z) {
        if (this.mIlluminationShowing != z) {
            this.mIlluminationShowing = z;
            invalidateSelf();
        }
    }
}
