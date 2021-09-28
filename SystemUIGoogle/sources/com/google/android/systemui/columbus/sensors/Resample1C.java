package com.google.android.systemui.columbus.sensors;
/* loaded from: classes2.dex */
public class Resample1C {
    protected long mRawLastT;
    protected float mRawLastX;
    protected long mResampledLastT;
    protected float mResampledThisX = 0.0f;
    protected long mInterval = 0;

    public void init(float f, long j, long j2) {
        this.mRawLastX = f;
        this.mRawLastT = j;
        this.mResampledThisX = f;
        this.mResampledLastT = j;
        this.mInterval = j2;
    }

    public long getInterval() {
        return this.mInterval;
    }

    public void setSyncTime(long j) {
        this.mResampledLastT = j;
    }
}
