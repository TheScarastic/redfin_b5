package com.android.systemui.animation;

import android.util.MathUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;
/* loaded from: classes.dex */
public class Interpolators {
    public static final Interpolator FAST_OUT_SLOW_IN = new PathInterpolator(0.4f, 0.0f, 0.2f, 1.0f);
    public static final Interpolator FAST_OUT_SLOW_IN_REVERSE = new PathInterpolator(0.8f, 0.0f, 0.6f, 1.0f);
    public static final Interpolator FAST_OUT_LINEAR_IN = new PathInterpolator(0.4f, 0.0f, 1.0f, 1.0f);
    public static final Interpolator LINEAR_OUT_SLOW_IN = new PathInterpolator(0.0f, 0.0f, 0.2f, 1.0f);
    public static final Interpolator SLOW_OUT_LINEAR_IN = new PathInterpolator(0.8f, 0.0f, 1.0f, 1.0f);
    public static final Interpolator ALPHA_IN = new PathInterpolator(0.4f, 0.0f, 1.0f, 1.0f);
    public static final Interpolator ALPHA_OUT = new PathInterpolator(0.0f, 0.0f, 0.8f, 1.0f);
    public static final Interpolator LINEAR = new LinearInterpolator();
    public static final Interpolator ACCELERATE = new AccelerateInterpolator();
    public static final Interpolator ACCELERATE_DECELERATE = new AccelerateDecelerateInterpolator();
    public static final Interpolator DECELERATE_QUINT = new DecelerateInterpolator(2.5f);
    public static final Interpolator CUSTOM_40_40 = new PathInterpolator(0.4f, 0.0f, 0.6f, 1.0f);
    public static final Interpolator ICON_OVERSHOT = new PathInterpolator(0.4f, 0.0f, 0.2f, 1.4f);
    public static final Interpolator ICON_OVERSHOT_LESS = new PathInterpolator(0.4f, 0.0f, 0.2f, 1.1f);
    public static final Interpolator PANEL_CLOSE_ACCELERATED = new PathInterpolator(0.3f, 0.0f, 0.5f, 1.0f);
    public static final Interpolator BOUNCE = new BounceInterpolator();
    public static final Interpolator CONTROL_STATE = new PathInterpolator(0.4f, 0.0f, 0.2f, 1.0f);
    public static final Interpolator TOUCH_RESPONSE = new PathInterpolator(0.3f, 0.0f, 0.1f, 1.0f);
    public static final Interpolator TOUCH_RESPONSE_REVERSE = new PathInterpolator(0.9f, 0.0f, 0.7f, 1.0f);

    public static float getOvershootInterpolation(float f, float f2, float f3) {
        if (f2 == 0.0f || f3 == 0.0f) {
            throw new IllegalArgumentException("Invalid values for overshoot");
        }
        float f4 = 1.0f + f2;
        return MathUtils.max(0.0f, ((float) (1.0d - Math.exp((double) ((-(MathUtils.log(f4 / f2) / f3)) * f)))) * f4);
    }

    public static float getOvershootInterpolation(float f) {
        return MathUtils.max(0.0f, (float) (1.0d - Math.exp((double) (f * -4.0f))));
    }

    public static float getNotificationScrimAlpha(float f, boolean z) {
        float f2;
        if (z) {
            f2 = MathUtils.constrainedMap(0.0f, 1.0f, 0.3f, 1.0f, f);
        } else {
            f2 = MathUtils.constrainedMap(0.0f, 1.0f, 0.0f, 0.5f, f);
        }
        float f3 = (f2 * 1.2f) - 0.2f;
        if (f3 <= 0.0f) {
            return 0.0f;
        }
        float f4 = 1.0f - f3;
        return (float) (1.0d - ((1.0d - Math.cos((double) ((3.14159f * f4) * f4))) * 0.5d));
    }
}
