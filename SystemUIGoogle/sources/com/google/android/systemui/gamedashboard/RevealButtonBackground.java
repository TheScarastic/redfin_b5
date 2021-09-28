package com.google.android.systemui.gamedashboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
/* loaded from: classes2.dex */
public class RevealButtonBackground extends Drawable {
    private final Paint mOvalBgPaint;

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    /* access modifiers changed from: package-private */
    public RevealButtonBackground(Context context) {
        Paint paint = new Paint(3);
        this.mOvalBgPaint = paint;
        paint.setColor(Color.argb(153, 0, 0, 0));
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        this.mOvalBgPaint.setAlpha((i * 153) / 255);
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        float width = ((float) bounds.width()) * 0.5f;
        canvas.drawRoundRect((float) bounds.left, (float) bounds.top, (float) bounds.right, (float) bounds.bottom, width, width, this.mOvalBgPaint);
    }
}
