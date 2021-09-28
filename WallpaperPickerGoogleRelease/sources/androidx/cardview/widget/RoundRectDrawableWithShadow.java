package androidx.cardview.widget;

import android.graphics.drawable.Drawable;
/* loaded from: classes.dex */
public class RoundRectDrawableWithShadow extends Drawable {
    public static final double COS_45 = Math.cos(Math.toRadians(45.0d));

    public static float calculateHorizontalPadding(float f, float f2, boolean z) {
        if (!z) {
            return f;
        }
        return (float) (((1.0d - COS_45) * ((double) f2)) + ((double) f));
    }

    public static float calculateVerticalPadding(float f, float f2, boolean z) {
        if (!z) {
            return f * 1.5f;
        }
        return (float) (((1.0d - COS_45) * ((double) f2)) + ((double) (f * 1.5f)));
    }
}
