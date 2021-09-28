package com.android.systemui.navigationbar.gestural;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.View;
import com.android.settingslib.Utils;
import com.android.systemui.R$attr;
import com.android.systemui.R$dimen;
import com.android.systemui.navigationbar.buttons.ButtonInterface;
/* loaded from: classes.dex */
public class NavigationHandle extends View implements ButtonInterface {
    protected final int mBottom;
    private final int mDarkColor;
    private final int mLightColor;
    protected final Paint mPaint;
    protected final int mRadius;
    private boolean mRequiresInvalidate;

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public void abortCurrentGesture() {
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public void setDelayTouchFeedback(boolean z) {
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public void setImageDrawable(Drawable drawable) {
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public void setVertical(boolean z) {
    }

    public NavigationHandle(Context context) {
        this(context, null);
    }

    public NavigationHandle(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint();
        this.mPaint = paint;
        Resources resources = context.getResources();
        this.mRadius = resources.getDimensionPixelSize(R$dimen.navigation_handle_radius);
        this.mBottom = resources.getDimensionPixelSize(R$dimen.navigation_handle_bottom);
        int themeAttr = Utils.getThemeAttr(context, R$attr.darkIconTheme);
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, Utils.getThemeAttr(context, R$attr.lightIconTheme));
        ContextThemeWrapper contextThemeWrapper2 = new ContextThemeWrapper(context, themeAttr);
        int i = R$attr.homeHandleColor;
        this.mLightColor = Utils.getColorAttrDefaultColor(contextThemeWrapper, i);
        this.mDarkColor = Utils.getColorAttrDefaultColor(contextThemeWrapper2, i);
        paint.setAntiAlias(true);
        setFocusable(false);
    }

    @Override // android.view.View
    public void setAlpha(float f) {
        super.setAlpha(f);
        if (f > 0.0f && this.mRequiresInvalidate) {
            this.mRequiresInvalidate = false;
            invalidate();
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int i = this.mRadius * 2;
        int width = getWidth();
        int i2 = (height - this.mBottom) - i;
        float f = (float) (i2 + i);
        int i3 = this.mRadius;
        canvas.drawRoundRect(0.0f, (float) i2, (float) width, f, (float) i3, (float) i3, this.mPaint);
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public void setDarkIntensity(float f) {
        int intValue = ((Integer) ArgbEvaluator.getInstance().evaluate(f, Integer.valueOf(this.mLightColor), Integer.valueOf(this.mDarkColor))).intValue();
        if (this.mPaint.getColor() != intValue) {
            this.mPaint.setColor(intValue);
            if (getVisibility() != 0 || getAlpha() <= 0.0f) {
                this.mRequiresInvalidate = true;
            } else {
                invalidate();
            }
        }
    }
}
