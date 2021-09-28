package com.android.systemui.shared.recents.utilities;

import android.animation.TypeEvaluator;
import android.graphics.RectF;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyGraph$$ExternalSyntheticOutline0;
/* loaded from: classes.dex */
public class RectFEvaluator implements TypeEvaluator<RectF> {
    private final RectF mRect = new RectF();

    public RectF evaluate(float f, RectF rectF, RectF rectF2) {
        float f2 = rectF.left;
        float m = DependencyGraph$$ExternalSyntheticOutline0.m(rectF2.left, f2, f, f2);
        float f3 = rectF.top;
        float m2 = DependencyGraph$$ExternalSyntheticOutline0.m(rectF2.top, f3, f, f3);
        float f4 = rectF.right;
        float m3 = DependencyGraph$$ExternalSyntheticOutline0.m(rectF2.right, f4, f, f4);
        float f5 = rectF.bottom;
        this.mRect.set(m, m2, m3, DependencyGraph$$ExternalSyntheticOutline0.m(rectF2.bottom, f5, f, f5));
        return this.mRect;
    }
}
