package com.android.wm.shell.draganddrop;

import android.animation.ObjectAnimator;
import android.animation.RectEvaluator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.IntProperty;
import android.util.Property;
import android.view.animation.Interpolator;
import com.android.internal.graphics.ColorUtils;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.wm.shell.R;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
/* loaded from: classes2.dex */
public class DropOutlineDrawable extends Drawable {
    private final IntProperty<DropOutlineDrawable> ALPHA;
    private ObjectAnimator mAlphaAnimator;
    private ObjectAnimator mBoundsAnimator;
    private int mColor;
    private final float mCornerRadius;
    private final int mMaxAlpha;
    private final Property<DropOutlineDrawable, Rect> BOUNDS = new Property<DropOutlineDrawable, Rect>(Rect.class, "bounds") { // from class: com.android.wm.shell.draganddrop.DropOutlineDrawable.2
        public void set(DropOutlineDrawable dropOutlineDrawable, Rect rect) {
            dropOutlineDrawable.setRegionBounds(rect);
        }

        public Rect get(DropOutlineDrawable dropOutlineDrawable) {
            return dropOutlineDrawable.getRegionBounds();
        }
    };
    private final RectEvaluator mRectEvaluator = new RectEvaluator(new Rect());
    private final Paint mPaint = new Paint(1);
    private final Rect mBounds = new Rect();

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public DropOutlineDrawable(Context context) {
        AnonymousClass1 r0 = new IntProperty<DropOutlineDrawable>("alpha") { // from class: com.android.wm.shell.draganddrop.DropOutlineDrawable.1
            public void setValue(DropOutlineDrawable dropOutlineDrawable, int i) {
                dropOutlineDrawable.setAlpha(i);
            }

            public Integer get(DropOutlineDrawable dropOutlineDrawable) {
                return Integer.valueOf(dropOutlineDrawable.getAlpha());
            }
        };
        this.ALPHA = r0;
        this.mCornerRadius = ScreenDecorationsUtils.getWindowCornerRadius(context.getResources());
        int color = context.getColor(R.color.drop_outline_background);
        this.mColor = color;
        this.mMaxAlpha = Color.alpha(color);
        r0.set((AnonymousClass1) this, (Integer) 0);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        int alphaComponent = ColorUtils.setAlphaComponent(this.mColor, i);
        this.mColor = alphaComponent;
        this.mPaint.setColor(alphaComponent);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return Color.alpha(this.mColor);
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Rect rect = this.mBounds;
        float f = this.mCornerRadius;
        canvas.drawRoundRect((float) rect.left, (float) rect.top, (float) rect.right, (float) rect.bottom, f, f, this.mPaint);
    }

    public void setRegionBounds(Rect rect) {
        this.mBounds.set(rect);
        invalidateSelf();
    }

    public Rect getRegionBounds() {
        return this.mBounds;
    }

    /* access modifiers changed from: package-private */
    public ObjectAnimator startBoundsAnimation(Rect rect, Interpolator interpolator) {
        if (ShellProtoLogCache.WM_SHELL_DRAG_AND_DROP_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_DRAG_AND_DROP, -1000962629, 0, null, String.valueOf(this.mBounds), String.valueOf(rect));
        }
        ObjectAnimator objectAnimator = this.mBoundsAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        ObjectAnimator ofObject = ObjectAnimator.ofObject(this, (Property<DropOutlineDrawable, V>) this.BOUNDS, (TypeEvaluator) this.mRectEvaluator, (Object[]) new Rect[]{this.mBounds, rect});
        this.mBoundsAnimator = ofObject;
        ofObject.setDuration(200L);
        this.mBoundsAnimator.setInterpolator(interpolator);
        this.mBoundsAnimator.start();
        return this.mBoundsAnimator;
    }

    /* access modifiers changed from: package-private */
    public ObjectAnimator startVisibilityAnimation(boolean z, Interpolator interpolator) {
        int i = 0;
        if (ShellProtoLogCache.WM_SHELL_DRAG_AND_DROP_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_DRAG_AND_DROP, 274140888, 5, null, Long.valueOf((long) Color.alpha(this.mColor)), Long.valueOf(z ? (long) this.mMaxAlpha : 0));
        }
        ObjectAnimator objectAnimator = this.mAlphaAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        IntProperty<DropOutlineDrawable> intProperty = this.ALPHA;
        int[] iArr = new int[2];
        iArr[0] = Color.alpha(this.mColor);
        if (z) {
            i = this.mMaxAlpha;
        }
        iArr[1] = i;
        ObjectAnimator ofInt = ObjectAnimator.ofInt(this, intProperty, iArr);
        this.mAlphaAnimator = ofInt;
        ofInt.setDuration(135L);
        this.mAlphaAnimator.setInterpolator(interpolator);
        this.mAlphaAnimator.start();
        return this.mAlphaAnimator;
    }
}
