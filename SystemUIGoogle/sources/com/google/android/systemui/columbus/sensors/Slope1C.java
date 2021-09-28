package com.google.android.systemui.columbus.sensors;
/* loaded from: classes2.dex */
public class Slope1C {
    private float mDeltaX = 0.0f;
    private float mRawLastX;

    public void init(float f) {
        this.mRawLastX = f;
    }

    public float update(float f, float f2) {
        float f3 = f * f2;
        float f4 = f3 - this.mRawLastX;
        this.mDeltaX = f4;
        this.mRawLastX = f3;
        return f4;
    }
}
