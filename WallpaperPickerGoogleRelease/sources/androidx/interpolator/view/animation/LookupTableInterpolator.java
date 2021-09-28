package androidx.interpolator.view.animation;

import androidx.constraintlayout.solver.widgets.analyzer.DependencyGraph$$ExternalSyntheticOutline0;
/* loaded from: classes.dex */
public final class LookupTableInterpolator {
    public static float interpolate(float[] fArr, float f, float f2) {
        if (f2 >= 1.0f) {
            return 1.0f;
        }
        if (f2 <= 0.0f) {
            return 0.0f;
        }
        int min = Math.min((int) (((float) (fArr.length - 1)) * f2), fArr.length - 2);
        return DependencyGraph$$ExternalSyntheticOutline0.m(fArr[min + 1], fArr[min], (f2 - (((float) min) * f)) / f, fArr[min]);
    }
}
