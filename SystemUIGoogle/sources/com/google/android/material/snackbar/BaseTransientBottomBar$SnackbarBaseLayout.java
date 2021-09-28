package com.google.android.material.snackbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.R$attr;
import com.google.android.material.R$dimen;
import com.google.android.material.R$styleable;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
/* loaded from: classes2.dex */
public class BaseTransientBottomBar$SnackbarBaseLayout extends FrameLayout {
    private static final View.OnTouchListener consumeAllTouchListener = new View.OnTouchListener() { // from class: com.google.android.material.snackbar.BaseTransientBottomBar$SnackbarBaseLayout.1
        @Override // android.view.View.OnTouchListener
        @SuppressLint({"ClickableViewAccessibility"})
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return true;
        }
    };
    private final float actionTextColorAlpha;
    private int animationMode;
    private final float backgroundOverlayColorAlpha;
    private ColorStateList backgroundTint;
    private PorterDuff.Mode backgroundTintMode;
    private BaseTransientBottomBar$OnAttachStateChangeListener onAttachStateChangeListener;
    private BaseTransientBottomBar$OnLayoutChangeListener onLayoutChangeListener;

    /* access modifiers changed from: protected */
    public BaseTransientBottomBar$SnackbarBaseLayout(Context context) {
        this(context, null);
    }

    /* access modifiers changed from: protected */
    public BaseTransientBottomBar$SnackbarBaseLayout(Context context, AttributeSet attributeSet) {
        super(MaterialThemeOverlay.wrap(context, attributeSet, 0, 0), attributeSet);
        Context context2 = getContext();
        TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet, R$styleable.SnackbarLayout);
        int i = R$styleable.SnackbarLayout_elevation;
        if (obtainStyledAttributes.hasValue(i)) {
            ViewCompat.setElevation(this, (float) obtainStyledAttributes.getDimensionPixelSize(i, 0));
        }
        this.animationMode = obtainStyledAttributes.getInt(R$styleable.SnackbarLayout_animationMode, 0);
        this.backgroundOverlayColorAlpha = obtainStyledAttributes.getFloat(R$styleable.SnackbarLayout_backgroundOverlayColorAlpha, 1.0f);
        setBackgroundTintList(MaterialResources.getColorStateList(context2, obtainStyledAttributes, R$styleable.SnackbarLayout_backgroundTint));
        setBackgroundTintMode(ViewUtils.parseTintMode(obtainStyledAttributes.getInt(R$styleable.SnackbarLayout_backgroundTintMode, -1), PorterDuff.Mode.SRC_IN));
        this.actionTextColorAlpha = obtainStyledAttributes.getFloat(R$styleable.SnackbarLayout_actionTextColorAlpha, 1.0f);
        obtainStyledAttributes.recycle();
        setOnTouchListener(consumeAllTouchListener);
        setFocusable(true);
        if (getBackground() == null) {
            ViewCompat.setBackground(this, createThemedBackground());
        }
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        setBackgroundDrawable(drawable);
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        if (!(drawable == null || this.backgroundTint == null)) {
            drawable = DrawableCompat.wrap(drawable.mutate());
            DrawableCompat.setTintList(drawable, this.backgroundTint);
            DrawableCompat.setTintMode(drawable, this.backgroundTintMode);
        }
        super.setBackgroundDrawable(drawable);
    }

    @Override // android.view.View
    public void setBackgroundTintList(ColorStateList colorStateList) {
        this.backgroundTint = colorStateList;
        if (getBackground() != null) {
            Drawable wrap = DrawableCompat.wrap(getBackground().mutate());
            DrawableCompat.setTintList(wrap, colorStateList);
            DrawableCompat.setTintMode(wrap, this.backgroundTintMode);
            if (wrap != getBackground()) {
                super.setBackgroundDrawable(wrap);
            }
        }
    }

    @Override // android.view.View
    public void setBackgroundTintMode(PorterDuff.Mode mode) {
        this.backgroundTintMode = mode;
        if (getBackground() != null) {
            Drawable wrap = DrawableCompat.wrap(getBackground().mutate());
            DrawableCompat.setTintMode(wrap, mode);
            if (wrap != getBackground()) {
                super.setBackgroundDrawable(wrap);
            }
        }
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener onClickListener) {
        setOnTouchListener(onClickListener != null ? null : consumeAllTouchListener);
        super.setOnClickListener(onClickListener);
    }

    @Override // android.widget.FrameLayout, android.view.View, android.view.ViewGroup
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        BaseTransientBottomBar$OnLayoutChangeListener baseTransientBottomBar$OnLayoutChangeListener = this.onLayoutChangeListener;
        if (baseTransientBottomBar$OnLayoutChangeListener != null) {
            baseTransientBottomBar$OnLayoutChangeListener.onLayoutChange(this, i, i2, i3, i4);
        }
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        BaseTransientBottomBar$OnAttachStateChangeListener baseTransientBottomBar$OnAttachStateChangeListener = this.onAttachStateChangeListener;
        if (baseTransientBottomBar$OnAttachStateChangeListener != null) {
            baseTransientBottomBar$OnAttachStateChangeListener.onViewAttachedToWindow(this);
        }
        ViewCompat.requestApplyInsets(this);
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        BaseTransientBottomBar$OnAttachStateChangeListener baseTransientBottomBar$OnAttachStateChangeListener = this.onAttachStateChangeListener;
        if (baseTransientBottomBar$OnAttachStateChangeListener != null) {
            baseTransientBottomBar$OnAttachStateChangeListener.onViewDetachedFromWindow(this);
        }
    }

    float getBackgroundOverlayColorAlpha() {
        return this.backgroundOverlayColorAlpha;
    }

    private Drawable createThemedBackground() {
        float dimension = getResources().getDimension(R$dimen.mtrl_snackbar_background_corner_radius);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setCornerRadius(dimension);
        gradientDrawable.setColor(MaterialColors.layer(this, R$attr.colorSurface, R$attr.colorOnSurface, getBackgroundOverlayColorAlpha()));
        if (this.backgroundTint == null) {
            return DrawableCompat.wrap(gradientDrawable);
        }
        Drawable wrap = DrawableCompat.wrap(gradientDrawable);
        DrawableCompat.setTintList(wrap, this.backgroundTint);
        return wrap;
    }
}
