package androidx.leanback.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import androidx.leanback.R$dimen;
/* loaded from: classes.dex */
public final class SeekBar extends View {
    private int mActiveBarHeight;
    private int mActiveRadius;
    private final Paint mBackgroundPaint;
    private int mBarHeight;
    private final Paint mKnobPaint;
    private int mKnobx;
    private int mMax;
    private int mProgress;
    private final Paint mProgressPaint;
    private int mSecondProgress;
    private final Paint mSecondProgressPaint;
    private final RectF mProgressRect = new RectF();
    private final RectF mSecondProgressRect = new RectF();
    private final RectF mBackgroundRect = new RectF();

    public SeekBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint(1);
        this.mSecondProgressPaint = paint;
        Paint paint2 = new Paint(1);
        this.mProgressPaint = paint2;
        Paint paint3 = new Paint(1);
        this.mBackgroundPaint = paint3;
        Paint paint4 = new Paint(1);
        this.mKnobPaint = paint4;
        setWillNotDraw(false);
        paint3.setColor(-7829368);
        paint.setColor(-3355444);
        paint2.setColor(-65536);
        paint4.setColor(-1);
        this.mBarHeight = context.getResources().getDimensionPixelSize(R$dimen.lb_playback_transport_progressbar_bar_height);
        this.mActiveBarHeight = context.getResources().getDimensionPixelSize(R$dimen.lb_playback_transport_progressbar_active_bar_height);
        this.mActiveRadius = context.getResources().getDimensionPixelSize(R$dimen.lb_playback_transport_progressbar_active_radius);
    }

    @Override // android.view.View
    protected void onFocusChanged(boolean z, int i, Rect rect) {
        super.onFocusChanged(z, i, rect);
        calculate();
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        calculate();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float f = (float) (isFocused() ? this.mActiveRadius : this.mBarHeight / 2);
        canvas.drawRoundRect(this.mBackgroundRect, f, f, this.mBackgroundPaint);
        RectF rectF = this.mSecondProgressRect;
        if (rectF.right > rectF.left) {
            canvas.drawRoundRect(rectF, f, f, this.mSecondProgressPaint);
        }
        canvas.drawRoundRect(this.mProgressRect, f, f, this.mProgressPaint);
        canvas.drawCircle((float) this.mKnobx, (float) (getHeight() / 2), f, this.mKnobPaint);
    }

    private void calculate() {
        int i = isFocused() ? this.mActiveBarHeight : this.mBarHeight;
        int width = getWidth();
        int height = getHeight();
        int i2 = (height - i) / 2;
        RectF rectF = this.mBackgroundRect;
        int i3 = this.mBarHeight;
        float f = (float) i2;
        float f2 = (float) (height - i2);
        rectF.set((float) (i3 / 2), f, (float) (width - (i3 / 2)), f2);
        int i4 = isFocused() ? this.mActiveRadius : this.mBarHeight / 2;
        float f3 = (float) (width - (i4 * 2));
        float f4 = (((float) this.mProgress) / ((float) this.mMax)) * f3;
        RectF rectF2 = this.mProgressRect;
        int i5 = this.mBarHeight;
        rectF2.set((float) (i5 / 2), f, ((float) (i5 / 2)) + f4, f2);
        this.mSecondProgressRect.set(this.mProgressRect.right, f, ((float) (this.mBarHeight / 2)) + ((((float) this.mSecondProgress) / ((float) this.mMax)) * f3), f2);
        this.mKnobx = i4 + ((int) f4);
        invalidate();
    }

    @Override // android.view.View
    public CharSequence getAccessibilityClassName() {
        return android.widget.SeekBar.class.getName();
    }

    @Override // android.view.View
    public boolean performAccessibilityAction(int i, Bundle bundle) {
        return super.performAccessibilityAction(i, bundle);
    }
}
