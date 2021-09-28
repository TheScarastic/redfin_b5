package com.android.systemui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import com.android.settingslib.Utils;
/* loaded from: classes.dex */
public class HardwareBgDrawable extends LayerDrawable {
    private final Drawable[] mLayers;
    private final Paint mPaint;
    private int mPoint;
    private boolean mRotatedBackground;
    private final boolean mRoundTop;

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public int getOpacity() {
        return -1;
    }

    public HardwareBgDrawable(boolean z, boolean z2, Context context) {
        this(z, getLayers(context, z, z2));
    }

    public HardwareBgDrawable(boolean z, Drawable[] drawableArr) {
        super(drawableArr);
        this.mPaint = new Paint();
        if (drawableArr.length == 2) {
            this.mRoundTop = z;
            this.mLayers = drawableArr;
            return;
        }
        throw new IllegalArgumentException("Need 2 layers");
    }

    private static Drawable[] getLayers(Context context, boolean z, boolean z2) {
        Drawable[] drawableArr;
        int i;
        int i2 = z2 ? R$drawable.rounded_bg_full : R$drawable.rounded_bg;
        if (z) {
            drawableArr = new Drawable[]{context.getDrawable(i2).mutate(), context.getDrawable(i2).mutate()};
        } else {
            drawableArr = new Drawable[2];
            drawableArr[0] = context.getDrawable(i2).mutate();
            if (z2) {
                i = R$drawable.rounded_full_bg_bottom;
            } else {
                i = R$drawable.rounded_bg_bottom;
            }
            drawableArr[1] = context.getDrawable(i).mutate();
        }
        drawableArr[1].setTintList(Utils.getColorAttr(context, 16843827));
        return drawableArr;
    }

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (this.mPoint < 0 || this.mRotatedBackground) {
            this.mLayers[0].draw(canvas);
            return;
        }
        Rect bounds = getBounds();
        int i = bounds.top;
        int i2 = this.mPoint + i;
        int i3 = bounds.bottom;
        if (i2 > i3) {
            i2 = i3;
        }
        if (this.mRoundTop) {
            this.mLayers[0].setBounds(bounds.left, i, bounds.right, i2);
        } else {
            this.mLayers[1].setBounds(bounds.left, i2, bounds.right, i3);
        }
        if (this.mRoundTop) {
            this.mLayers[1].draw(canvas);
            this.mLayers[0].draw(canvas);
            return;
        }
        this.mLayers[0].draw(canvas);
        this.mLayers[1].draw(canvas);
    }

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        this.mPaint.setAlpha(i);
    }

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }
}
