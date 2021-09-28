package com.android.systemui.statusbar;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.util.AttributeSet;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class ScreenRecordDrawable extends DrawableWrapper {
    private Drawable mFillDrawable;
    private int mHeightPx;
    private int mHorizontalPadding;
    private Drawable mIconDrawable;
    private int mIconRadius;
    private int mLevel;
    private Paint mPaint;
    private float mTextSize;
    private int mWidthPx;

    public ScreenRecordDrawable() {
        super(null);
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        super.inflate(resources, xmlPullParser, attributeSet, theme);
        int i = R$drawable.ic_screen_record_background;
        setDrawable(resources.getDrawable(i, theme).mutate());
        this.mFillDrawable = resources.getDrawable(i, theme).mutate();
        this.mIconDrawable = resources.getDrawable(R$drawable.ic_screenrecord, theme).mutate();
        this.mHorizontalPadding = resources.getDimensionPixelSize(R$dimen.status_bar_horizontal_padding);
        this.mTextSize = (float) resources.getDimensionPixelSize(R$dimen.screenrecord_status_text_size);
        this.mIconRadius = resources.getDimensionPixelSize(R$dimen.screenrecord_status_icon_radius);
        this.mLevel = attributeSet.getAttributeIntValue(null, "level", 0);
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setTextAlign(Paint.Align.CENTER);
        this.mPaint.setColor(-1);
        this.mPaint.setTextSize(this.mTextSize);
        this.mPaint.setFakeBoldText(true);
        this.mWidthPx = resources.getDimensionPixelSize(R$dimen.screenrecord_status_icon_width);
        this.mHeightPx = resources.getDimensionPixelSize(R$dimen.screenrecord_status_icon_height);
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.mWidthPx;
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.mHeightPx;
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public boolean canApplyTheme() {
        return this.mFillDrawable.canApplyTheme() || super.canApplyTheme();
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public void applyTheme(Resources.Theme theme) {
        super.applyTheme(theme);
        this.mFillDrawable.applyTheme(theme);
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.mFillDrawable.setBounds(rect);
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public boolean onLayoutDirectionChanged(int i) {
        this.mFillDrawable.setLayoutDirection(i);
        return super.onLayoutDirectionChanged(i);
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.mFillDrawable.draw(canvas);
        Rect bounds = this.mFillDrawable.getBounds();
        int i = this.mLevel;
        if (i > 0) {
            String valueOf = String.valueOf(i);
            Rect rect = new Rect();
            this.mPaint.getTextBounds(valueOf, 0, valueOf.length(), rect);
            canvas.drawText(valueOf, (float) bounds.centerX(), (float) (bounds.centerY() + (rect.height() / 2)), this.mPaint);
            return;
        }
        this.mIconDrawable.setBounds(new Rect(bounds.centerX() - this.mIconRadius, bounds.centerY() - this.mIconRadius, bounds.centerX() + this.mIconRadius, bounds.centerY() + this.mIconRadius));
        this.mIconDrawable.draw(canvas);
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public boolean getPadding(Rect rect) {
        int i = rect.left;
        int i2 = this.mHorizontalPadding;
        rect.left = i + i2;
        rect.right += i2;
        return true;
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        super.setAlpha(i);
        this.mFillDrawable.setAlpha(i);
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public boolean setVisible(boolean z, boolean z2) {
        this.mFillDrawable.setVisible(z, z2);
        return super.setVisible(z, z2);
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public Drawable mutate() {
        this.mFillDrawable.mutate();
        return super.mutate();
    }
}
