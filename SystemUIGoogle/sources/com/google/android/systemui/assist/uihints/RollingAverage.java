package com.google.android.systemui.assist.uihints;
/* loaded from: classes2.dex */
public class RollingAverage {
    private float[] mSamples;
    private int mSize;
    private float mTotal = 0.0f;
    private int mIndex = 0;

    public RollingAverage(int i) {
        this.mSize = i;
        this.mSamples = new float[i];
        for (int i2 = 0; i2 < i; i2++) {
            this.mSamples[i2] = 0.0f;
        }
    }

    public void add(float f) {
        int i = this.mSize;
        if (i > 0) {
            float f2 = this.mTotal;
            float[] fArr = this.mSamples;
            int i2 = this.mIndex;
            float f3 = f2 - fArr[i2];
            this.mTotal = f3;
            fArr[i2] = f;
            this.mTotal = f3 + f;
            int i3 = i2 + 1;
            this.mIndex = i3;
            if (i3 == i) {
                this.mIndex = 0;
            }
        }
    }

    public double getAverage() {
        return (double) (this.mTotal / ((float) this.mSize));
    }
}
