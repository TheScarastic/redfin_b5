package com.android.settingslib.display;

import android.util.MathUtils;
/* loaded from: classes.dex */
public class BrightnessUtils {
    public static final float convertGammaToLinearFloat(int i, float f, float f2) {
        float f3;
        float norm = MathUtils.norm(0.0f, 65535.0f, (float) i);
        if (norm <= 0.5f) {
            f3 = MathUtils.sq(norm / 0.5f);
        } else {
            f3 = MathUtils.exp((norm - 0.5599107f) / 0.17883277f) + 0.28466892f;
        }
        return MathUtils.lerp(f, f2, MathUtils.constrain(f3, 0.0f, 12.0f) / 12.0f);
    }

    public static final int convertLinearToGammaFloat(float f, float f2, float f3) {
        float f4;
        float norm = MathUtils.norm(f2, f3, f) * 12.0f;
        if (norm <= 1.0f) {
            f4 = MathUtils.sqrt(norm) * 0.5f;
        } else {
            f4 = (MathUtils.log(norm - 0.28466892f) * 0.17883277f) + 0.5599107f;
        }
        return Math.round(MathUtils.lerp(0.0f, 65535.0f, f4));
    }
}
