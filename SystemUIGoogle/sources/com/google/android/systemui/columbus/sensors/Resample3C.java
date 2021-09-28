package com.google.android.systemui.columbus.sensors;
/* loaded from: classes2.dex */
public class Resample3C extends Resample1C {
    private float mRawLastY;
    private float mRawLastZ;
    private float mResampledThisY;
    private float mResampledThisZ;

    public void init(float f, float f2, float f3, long j, long j2) {
        init(f, j, j2);
        this.mRawLastY = f2;
        this.mRawLastZ = f3;
        this.mResampledThisY = f2;
        this.mResampledThisZ = f3;
    }

    public boolean update(float f, float f2, float f3, long j) {
        long j2 = this.mRawLastT;
        if (j == j2) {
            return false;
        }
        long j3 = this.mInterval;
        if (j3 <= 0) {
            j3 = j - j2;
        }
        long j4 = this.mResampledLastT + j3;
        if (j < j4) {
            this.mRawLastT = j;
            this.mRawLastX = f;
            this.mRawLastY = f2;
            this.mRawLastZ = f3;
            return false;
        }
        float f4 = ((float) (j4 - j2)) / ((float) (j - j2));
        float f5 = this.mRawLastX;
        this.mResampledThisX = ((f - f5) * f4) + f5;
        float f6 = this.mRawLastY;
        this.mResampledThisY = ((f2 - f6) * f4) + f6;
        float f7 = this.mRawLastZ;
        this.mResampledThisZ = ((f3 - f7) * f4) + f7;
        this.mResampledLastT = j4;
        if (j2 >= j4) {
            return true;
        }
        this.mRawLastT = j;
        this.mRawLastX = f;
        this.mRawLastY = f2;
        this.mRawLastZ = f3;
        return true;
    }

    public Sample3C getResults() {
        return new Sample3C(this.mResampledThisX, this.mResampledThisY, this.mResampledThisZ, this.mResampledLastT);
    }
}
