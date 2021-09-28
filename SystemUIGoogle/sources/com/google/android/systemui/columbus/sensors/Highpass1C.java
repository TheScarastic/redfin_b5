package com.google.android.systemui.columbus.sensors;
/* loaded from: classes2.dex */
public class Highpass1C {
    private float mPara = 1.0f;
    private float mLastX = 0.0f;
    private float mLastY = 0.0f;

    public void setPara(float f) {
        this.mPara = f;
    }

    public void init(float f) {
        this.mLastX = f;
        this.mLastY = f;
    }

    public float update(float f) {
        float f2 = this.mPara;
        if (f2 == 1.0f) {
            return f;
        }
        float f3 = (this.mLastY * f2) + (f2 * (f - this.mLastX));
        this.mLastY = f3;
        this.mLastX = f;
        return f3;
    }
}
