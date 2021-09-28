package com.android.systemui.util;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.InsetDrawable;
import android.util.AttributeSet;
import com.android.systemui.R$styleable;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes2.dex */
public class AlphaTintDrawableWrapper extends InsetDrawable {
    private int[] mThemeAttrs;
    private ColorStateList mTint;

    public AlphaTintDrawableWrapper() {
        super((Drawable) null, 0);
    }

    AlphaTintDrawableWrapper(Drawable drawable, int[] iArr) {
        super(drawable, 0);
        this.mThemeAttrs = iArr;
    }

    @Override // android.graphics.drawable.InsetDrawable, android.graphics.drawable.Drawable, android.graphics.drawable.DrawableWrapper
    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        TypedArray obtainAttributes = InsetDrawable.obtainAttributes(resources, theme, attributeSet, R$styleable.AlphaTintDrawableWrapper);
        super.inflate(resources, xmlPullParser, attributeSet, theme);
        this.mThemeAttrs = obtainAttributes.extractThemeAttrs();
        updateStateFromTypedArray(obtainAttributes);
        obtainAttributes.recycle();
        applyTint();
    }

    @Override // android.graphics.drawable.InsetDrawable, android.graphics.drawable.Drawable, android.graphics.drawable.DrawableWrapper
    public void applyTheme(Resources.Theme theme) {
        super.applyTheme(theme);
        int[] iArr = this.mThemeAttrs;
        if (!(iArr == null || theme == null)) {
            TypedArray resolveAttributes = theme.resolveAttributes(iArr, R$styleable.AlphaTintDrawableWrapper);
            updateStateFromTypedArray(resolveAttributes);
            resolveAttributes.recycle();
        }
        applyTint();
    }

    @Override // android.graphics.drawable.Drawable, android.graphics.drawable.DrawableWrapper
    public boolean canApplyTheme() {
        int[] iArr = this.mThemeAttrs;
        return (iArr != null && iArr.length > 0) || super.canApplyTheme();
    }

    private void updateStateFromTypedArray(TypedArray typedArray) {
        int i = R$styleable.AlphaTintDrawableWrapper_android_tint;
        if (typedArray.hasValue(i)) {
            this.mTint = typedArray.getColorStateList(i);
        }
        int i2 = R$styleable.AlphaTintDrawableWrapper_android_alpha;
        if (typedArray.hasValue(i2)) {
            setAlpha(Math.round(typedArray.getFloat(i2, 1.0f) * 255.0f));
        }
    }

    private void applyTint() {
        if (getDrawable() != null && this.mTint != null) {
            getDrawable().mutate().setTintList(this.mTint);
        }
    }

    @Override // android.graphics.drawable.Drawable, android.graphics.drawable.DrawableWrapper
    public Drawable.ConstantState getConstantState() {
        return new AlphaTintState(super.getConstantState(), this.mThemeAttrs, getAlpha(), this.mTint);
    }

    /* loaded from: classes2.dex */
    static class AlphaTintState extends Drawable.ConstantState {
        private int mAlpha;
        private ColorStateList mColorStateList;
        private int[] mThemeAttrs;
        private Drawable.ConstantState mWrappedState;

        @Override // android.graphics.drawable.Drawable.ConstantState
        public boolean canApplyTheme() {
            return true;
        }

        AlphaTintState(Drawable.ConstantState constantState, int[] iArr, int i, ColorStateList colorStateList) {
            this.mWrappedState = constantState;
            this.mThemeAttrs = iArr;
            this.mAlpha = i;
            this.mColorStateList = colorStateList;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            return newDrawable(null, null);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources resources, Resources.Theme theme) {
            AlphaTintDrawableWrapper alphaTintDrawableWrapper = new AlphaTintDrawableWrapper(((DrawableWrapper) this.mWrappedState.newDrawable(resources, theme)).getDrawable(), this.mThemeAttrs);
            alphaTintDrawableWrapper.setTintList(this.mColorStateList);
            alphaTintDrawableWrapper.setAlpha(this.mAlpha);
            return alphaTintDrawableWrapper;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return this.mWrappedState.getChangingConfigurations();
        }
    }
}
