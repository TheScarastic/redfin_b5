package com.android.systemui;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.view.ContextThemeWrapper;
import com.android.settingslib.Utils;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DualToneHandler.kt */
/* loaded from: classes.dex */
public final class DualToneHandler {
    private Color darkColor;
    private Color lightColor;

    public DualToneHandler(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        setColorsFromContext(context);
    }

    public final void setColorsFromContext(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, Utils.getThemeAttr(context, R$attr.darkIconTheme));
        ContextThemeWrapper contextThemeWrapper2 = new ContextThemeWrapper(context, Utils.getThemeAttr(context, R$attr.lightIconTheme));
        int i = R$attr.singleToneColor;
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(contextThemeWrapper, i);
        int i2 = R$attr.backgroundColor;
        int colorAttrDefaultColor2 = Utils.getColorAttrDefaultColor(contextThemeWrapper, i2);
        int i3 = R$attr.fillColor;
        this.darkColor = new Color(colorAttrDefaultColor, colorAttrDefaultColor2, Utils.getColorAttrDefaultColor(contextThemeWrapper, i3));
        this.lightColor = new Color(Utils.getColorAttrDefaultColor(contextThemeWrapper2, i), Utils.getColorAttrDefaultColor(contextThemeWrapper2, i2), Utils.getColorAttrDefaultColor(contextThemeWrapper2, i3));
    }

    private final int getColorForDarkIntensity(float f, int i, int i2) {
        Object evaluate = ArgbEvaluator.getInstance().evaluate(f, Integer.valueOf(i), Integer.valueOf(i2));
        Objects.requireNonNull(evaluate, "null cannot be cast to non-null type kotlin.Int");
        return ((Integer) evaluate).intValue();
    }

    public final int getSingleColor(float f) {
        Color color = this.lightColor;
        if (color != null) {
            int single = color.getSingle();
            Color color2 = this.darkColor;
            if (color2 != null) {
                return getColorForDarkIntensity(f, single, color2.getSingle());
            }
            Intrinsics.throwUninitializedPropertyAccessException("darkColor");
            throw null;
        }
        Intrinsics.throwUninitializedPropertyAccessException("lightColor");
        throw null;
    }

    public final int getBackgroundColor(float f) {
        Color color = this.lightColor;
        if (color != null) {
            int background = color.getBackground();
            Color color2 = this.darkColor;
            if (color2 != null) {
                return getColorForDarkIntensity(f, background, color2.getBackground());
            }
            Intrinsics.throwUninitializedPropertyAccessException("darkColor");
            throw null;
        }
        Intrinsics.throwUninitializedPropertyAccessException("lightColor");
        throw null;
    }

    public final int getFillColor(float f) {
        Color color = this.lightColor;
        if (color != null) {
            int fill = color.getFill();
            Color color2 = this.darkColor;
            if (color2 != null) {
                return getColorForDarkIntensity(f, fill, color2.getFill());
            }
            Intrinsics.throwUninitializedPropertyAccessException("darkColor");
            throw null;
        }
        Intrinsics.throwUninitializedPropertyAccessException("lightColor");
        throw null;
    }

    /* compiled from: DualToneHandler.kt */
    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class Color {
        private final int background;
        private final int fill;
        private final int single;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Color)) {
                return false;
            }
            Color color = (Color) obj;
            return this.single == color.single && this.background == color.background && this.fill == color.fill;
        }

        public int hashCode() {
            return (((Integer.hashCode(this.single) * 31) + Integer.hashCode(this.background)) * 31) + Integer.hashCode(this.fill);
        }

        public String toString() {
            return "Color(single=" + this.single + ", background=" + this.background + ", fill=" + this.fill + ')';
        }

        public Color(int i, int i2, int i3) {
            this.single = i;
            this.background = i2;
            this.fill = i3;
        }

        public final int getBackground() {
            return this.background;
        }

        public final int getFill() {
            return this.fill;
        }

        public final int getSingle() {
            return this.single;
        }
    }
}
