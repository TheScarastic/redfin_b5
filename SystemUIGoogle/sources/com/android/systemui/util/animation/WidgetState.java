package com.android.systemui.util.animation;

import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: TransitionLayout.kt */
/* loaded from: classes2.dex */
public final class WidgetState {
    private float alpha;
    private boolean gone;
    private int height;
    private int measureHeight;
    private int measureWidth;
    private float scale;
    private int width;
    private float x;
    private float y;

    public WidgetState() {
        this(0.0f, 0.0f, 0, 0, 0, 0, 0.0f, 0.0f, false, 511, null);
    }

    public final WidgetState copy(float f, float f2, int i, int i2, int i3, int i4, float f3, float f4, boolean z) {
        return new WidgetState(f, f2, i, i2, i3, i4, f3, f4, z);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WidgetState)) {
            return false;
        }
        WidgetState widgetState = (WidgetState) obj;
        return Intrinsics.areEqual(Float.valueOf(this.x), Float.valueOf(widgetState.x)) && Intrinsics.areEqual(Float.valueOf(this.y), Float.valueOf(widgetState.y)) && this.width == widgetState.width && this.height == widgetState.height && this.measureWidth == widgetState.measureWidth && this.measureHeight == widgetState.measureHeight && Intrinsics.areEqual(Float.valueOf(this.alpha), Float.valueOf(widgetState.alpha)) && Intrinsics.areEqual(Float.valueOf(this.scale), Float.valueOf(widgetState.scale)) && this.gone == widgetState.gone;
    }

    public int hashCode() {
        int hashCode = ((((((((((((((Float.hashCode(this.x) * 31) + Float.hashCode(this.y)) * 31) + Integer.hashCode(this.width)) * 31) + Integer.hashCode(this.height)) * 31) + Integer.hashCode(this.measureWidth)) * 31) + Integer.hashCode(this.measureHeight)) * 31) + Float.hashCode(this.alpha)) * 31) + Float.hashCode(this.scale)) * 31;
        boolean z = this.gone;
        if (z) {
            z = true;
        }
        int i = z ? 1 : 0;
        int i2 = z ? 1 : 0;
        int i3 = z ? 1 : 0;
        return hashCode + i;
    }

    public String toString() {
        return "WidgetState(x=" + this.x + ", y=" + this.y + ", width=" + this.width + ", height=" + this.height + ", measureWidth=" + this.measureWidth + ", measureHeight=" + this.measureHeight + ", alpha=" + this.alpha + ", scale=" + this.scale + ", gone=" + this.gone + ')';
    }

    public WidgetState(float f, float f2, int i, int i2, int i3, int i4, float f3, float f4, boolean z) {
        this.x = f;
        this.y = f2;
        this.width = i;
        this.height = i2;
        this.measureWidth = i3;
        this.measureHeight = i4;
        this.alpha = f3;
        this.scale = f4;
        this.gone = z;
    }

    public /* synthetic */ WidgetState(float f, float f2, int i, int i2, int i3, int i4, float f3, float f4, boolean z, int i5, DefaultConstructorMarker defaultConstructorMarker) {
        this((i5 & 1) != 0 ? 0.0f : f, (i5 & 2) != 0 ? 0.0f : f2, (i5 & 4) != 0 ? 0 : i, (i5 & 8) != 0 ? 0 : i2, (i5 & 16) != 0 ? 0 : i3, (i5 & 32) != 0 ? 0 : i4, (i5 & 64) != 0 ? 1.0f : f3, (i5 & 128) != 0 ? 1.0f : f4, (i5 & 256) != 0 ? false : z);
    }

    public final float getX() {
        return this.x;
    }

    public final void setX(float f) {
        this.x = f;
    }

    public final float getY() {
        return this.y;
    }

    public final void setY(float f) {
        this.y = f;
    }

    public final int getWidth() {
        return this.width;
    }

    public final void setWidth(int i) {
        this.width = i;
    }

    public final int getHeight() {
        return this.height;
    }

    public final void setHeight(int i) {
        this.height = i;
    }

    public final int getMeasureWidth() {
        return this.measureWidth;
    }

    public final void setMeasureWidth(int i) {
        this.measureWidth = i;
    }

    public final int getMeasureHeight() {
        return this.measureHeight;
    }

    public final void setMeasureHeight(int i) {
        this.measureHeight = i;
    }

    public final float getAlpha() {
        return this.alpha;
    }

    public final void setAlpha(float f) {
        this.alpha = f;
    }

    public final float getScale() {
        return this.scale;
    }

    public final void setScale(float f) {
        this.scale = f;
    }

    public final boolean getGone() {
        return this.gone;
    }

    public final void setGone(boolean z) {
        this.gone = z;
    }

    public final void initFromLayout(View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        boolean z = true;
        boolean z2 = view.getVisibility() == 8;
        this.gone = z2;
        if (z2) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type androidx.constraintlayout.widget.ConstraintLayout.LayoutParams");
            ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) layoutParams;
            this.x = (float) layoutParams2.getConstraintWidget().getLeft();
            this.y = (float) layoutParams2.getConstraintWidget().getTop();
            this.width = layoutParams2.getConstraintWidget().getWidth();
            int height = layoutParams2.getConstraintWidget().getHeight();
            this.height = height;
            this.measureHeight = height;
            this.measureWidth = this.width;
            this.alpha = 0.0f;
            this.scale = 0.0f;
            return;
        }
        this.x = (float) view.getLeft();
        this.y = (float) view.getTop();
        this.width = view.getWidth();
        int height2 = view.getHeight();
        this.height = height2;
        this.measureWidth = this.width;
        this.measureHeight = height2;
        if (view.getVisibility() != 8) {
            z = false;
        }
        this.gone = z;
        this.alpha = view.getAlpha();
        this.scale = 1.0f;
    }
}
