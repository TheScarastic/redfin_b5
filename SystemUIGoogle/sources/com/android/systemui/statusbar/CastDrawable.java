package com.android.systemui.statusbar;

import android.content.res.Resources;
import android.graphics.Canvas;
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
public class CastDrawable extends DrawableWrapper {
    private Drawable mFillDrawable;
    private int mHorizontalPadding;

    public CastDrawable() {
        super(null);
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        super.inflate(resources, xmlPullParser, attributeSet, theme);
        setDrawable(resources.getDrawable(R$drawable.ic_cast, theme).mutate());
        this.mFillDrawable = resources.getDrawable(R$drawable.ic_cast_connected_fill, theme).mutate();
        this.mHorizontalPadding = resources.getDimensionPixelSize(R$dimen.status_bar_horizontal_padding);
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
