package com.google.android.systemui.columbus.sensors;
/* loaded from: classes2.dex */
public class Lowpass1C {
    private float mPara = 1.0f;
    private float mLastX = 0.0f;

    public void setPara(float f) {
        this.mPara = f;
    }

    public void init(float f) {
        this.mLastX = f;
    }

    public float update(float f) {
        float f2 = this.mPara;
        if (f2 == 1.0f) {
            return f;
        }
        float f3 = ((1.0f - f2) * this.mLastX) + (f2 * f);
        this.mLastX = f3;
        return f3;
    }
}
