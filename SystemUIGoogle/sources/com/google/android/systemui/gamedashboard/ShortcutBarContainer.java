package com.google.android.systemui.gamedashboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;
/* loaded from: classes2.dex */
public class ShortcutBarContainer extends LinearLayout {
    private final Paint mOvalBgPaint;
    private boolean mUseClearBackground = false;

    public ShortcutBarContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint(3);
        this.mOvalBgPaint = paint;
        setWillNotDraw(false);
        paint.setColor(Color.argb(153, 0, 0, 0));
    }

    public void setUseClearBackground(boolean z) {
        this.mUseClearBackground = z;
        invalidate();
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!this.mUseClearBackground) {
            float width = (float) getWidth();
            float f = width * 0.5f;
            canvas.drawRoundRect(0.0f, 0.0f, width, (float) getHeight(), f, f, this.mOvalBgPaint);
        }
    }
}
