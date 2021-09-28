package androidx.transition;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.util.Property;
import android.view.View;
import androidx.core.view.ViewCompat;
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ViewUtils {
    static final Property<View, Rect> CLIP_BOUNDS;
    private static final ViewUtilsBase IMPL;
    static final Property<View, Float> TRANSITION_ALPHA;

    static {
        int i = Build.VERSION.SDK_INT;
        if (i >= 29) {
            IMPL = new ViewUtilsApi29();
        } else if (i >= 23) {
            IMPL = new ViewUtilsApi23();
        } else if (i >= 22) {
            IMPL = new ViewUtilsApi22();
        } else if (i >= 21) {
            IMPL = new ViewUtilsApi21();
        } else if (i >= 19) {
            IMPL = new ViewUtilsApi19();
        } else {
            IMPL = new ViewUtilsBase();
        }
        TRANSITION_ALPHA = new Property<View, Float>(Float.class, "translationAlpha") { // from class: androidx.transition.ViewUtils.1
            public Float get(View view) {
                return Float.valueOf(ViewUtils.getTransitionAlpha(view));
            }

            public void set(View view, Float f) {
                ViewUtils.setTransitionAlpha(view, f.floatValue());
            }
        };
        CLIP_BOUNDS = new Property<View, Rect>(Rect.class, "clipBounds") { // from class: androidx.transition.ViewUtils.2
            public Rect get(View view) {
                return ViewCompat.getClipBounds(view);
            }

            public void set(View view, Rect rect) {
                ViewCompat.setClipBounds(view, rect);
            }
        };
    }

    /* access modifiers changed from: package-private */
    public static ViewOverlayImpl getOverlay(View view) {
        if (Build.VERSION.SDK_INT >= 18) {
            return new ViewOverlayApi18(view);
        }
        return ViewOverlayApi14.createFrom(view);
    }

    /* access modifiers changed from: package-private */
    public static WindowIdImpl getWindowId(View view) {
        if (Build.VERSION.SDK_INT >= 18) {
            return new WindowIdApi18(view);
        }
        return new WindowIdApi14(view.getWindowToken());
    }

    /* access modifiers changed from: package-private */
    public static void setTransitionAlpha(View view, float f) {
        IMPL.setTransitionAlpha(view, f);
    }

    /* access modifiers changed from: package-private */
    public static float getTransitionAlpha(View view) {
        return IMPL.getTransitionAlpha(view);
    }

    /* access modifiers changed from: package-private */
    public static void saveNonTransitionAlpha(View view) {
        IMPL.saveNonTransitionAlpha(view);
    }

    /* access modifiers changed from: package-private */
    public static void clearNonTransitionAlpha(View view) {
        IMPL.clearNonTransitionAlpha(view);
    }

    /* access modifiers changed from: package-private */
    public static void setTransitionVisibility(View view, int i) {
        IMPL.setTransitionVisibility(view, i);
    }

    /* access modifiers changed from: package-private */
    public static void transformMatrixToGlobal(View view, Matrix matrix) {
        IMPL.transformMatrixToGlobal(view, matrix);
    }

    /* access modifiers changed from: package-private */
    public static void transformMatrixToLocal(View view, Matrix matrix) {
        IMPL.transformMatrixToLocal(view, matrix);
    }

    /* access modifiers changed from: package-private */
    public static void setAnimationMatrix(View view, Matrix matrix) {
        IMPL.setAnimationMatrix(view, matrix);
    }

    /* access modifiers changed from: package-private */
    public static void setLeftTopRightBottom(View view, int i, int i2, int i3, int i4) {
        IMPL.setLeftTopRightBottom(view, i, i2, i3, i4);
    }
}
