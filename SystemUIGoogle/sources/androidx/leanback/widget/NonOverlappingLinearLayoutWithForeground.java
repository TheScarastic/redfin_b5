package androidx.leanback.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
/* loaded from: classes.dex */
class NonOverlappingLinearLayoutWithForeground extends LinearLayout {
    private Drawable mForeground;
    private boolean mForegroundBoundsChanged;
    private final Rect mSelfBounds;

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public NonOverlappingLinearLayoutWithForeground(Context context) {
        this(context, null);
    }

    public NonOverlappingLinearLayoutWithForeground(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NonOverlappingLinearLayoutWithForeground(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mSelfBounds = new Rect();
        if (context.getApplicationInfo().targetSdkVersion < 23 || Build.VERSION.SDK_INT < 23) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, new int[]{16843017});
            Drawable drawable = obtainStyledAttributes.getDrawable(0);
            if (drawable != null) {
                setForegroundCompat(drawable);
            }
            obtainStyledAttributes.recycle();
        }
    }

    public void setForegroundCompat(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 23) {
            ForegroundHelper.setForeground(this, drawable);
        } else if (this.mForeground != drawable) {
            this.mForeground = drawable;
            this.mForegroundBoundsChanged = true;
            setWillNotDraw(false);
            this.mForeground.setCallback(this);
            if (this.mForeground.isStateful()) {
                this.mForeground.setState(getDrawableState());
            }
        }
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Drawable drawable = this.mForeground;
        if (drawable != null) {
            if (this.mForegroundBoundsChanged) {
                this.mForegroundBoundsChanged = false;
                Rect rect = this.mSelfBounds;
                rect.set(0, 0, getRight() - getLeft(), getBottom() - getTop());
                drawable.setBounds(rect);
            }
            drawable.draw(canvas);
        }
    }

    @Override // android.widget.LinearLayout, android.view.View, android.view.ViewGroup
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mForegroundBoundsChanged = z | this.mForegroundBoundsChanged;
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.mForeground;
    }

    @Override // android.view.View, android.view.ViewGroup
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.mForeground;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = this.mForeground;
        if (drawable != null && drawable.isStateful()) {
            this.mForeground.setState(getDrawableState());
        }
    }
}
