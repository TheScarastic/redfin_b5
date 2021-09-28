package com.android.systemui.classifier;

import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.util.DeviceConfigProxy;
/* loaded from: classes.dex */
class DiagonalClassifier extends FalsingClassifier {
    private final float mHorizontalAngleRange;
    private final float mVerticalAngleRange;

    private float normalizeAngle(float f) {
        return f < 0.0f ? (f % 6.2831855f) + 6.2831855f : f > 6.2831855f ? f % 6.2831855f : f;
    }

    /* access modifiers changed from: package-private */
    public DiagonalClassifier(FalsingDataProvider falsingDataProvider, DeviceConfigProxy deviceConfigProxy) {
        super(falsingDataProvider);
        this.mHorizontalAngleRange = deviceConfigProxy.getFloat("systemui", "brightline_falsing_diagonal_horizontal_angle_range", 0.08726646f);
        this.mVerticalAngleRange = deviceConfigProxy.getFloat("systemui", "brightline_falsing_diagonal_horizontal_angle_range", 0.08726646f);
    }

    @Override // com.android.systemui.classifier.FalsingClassifier
    FalsingClassifier.Result calculateFalsingResult(int i, double d, double d2) {
        float angle = getAngle();
        if (angle == Float.MAX_VALUE) {
            return FalsingClassifier.Result.passed(0.0d);
        }
        if (i == 5 || i == 6) {
            return FalsingClassifier.Result.passed(0.0d);
        }
        float f = this.mHorizontalAngleRange;
        float f2 = 0.7853982f - f;
        float f3 = f + 0.7853982f;
        if (isVertical()) {
            float f4 = this.mVerticalAngleRange;
            f2 = 0.7853982f - f4;
            f3 = f4 + 0.7853982f;
        }
        return angleBetween(angle, f2, f3) || angleBetween(angle, f2 + 1.5707964f, f3 + 1.5707964f) || angleBetween(angle, f2 - 1.5707964f, f3 - 1.5707964f) || angleBetween(angle, f2 + 3.1415927f, f3 + 3.1415927f) ? falsed(0.5d, getReason()) : FalsingClassifier.Result.passed(0.5d);
    }

    private String getReason() {
        return String.format(null, "{angle=%f, vertical=%s}", Float.valueOf(getAngle()), Boolean.valueOf(isVertical()));
    }

    private boolean angleBetween(float f, float f2, float f3) {
        float normalizeAngle = normalizeAngle(f2);
        float normalizeAngle2 = normalizeAngle(f3);
        return normalizeAngle > normalizeAngle2 ? f >= normalizeAngle || f <= normalizeAngle2 : f >= normalizeAngle && f <= normalizeAngle2;
    }
}
