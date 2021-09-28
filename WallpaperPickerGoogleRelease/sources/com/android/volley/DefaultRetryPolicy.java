package com.android.volley;
/* loaded from: classes.dex */
public class DefaultRetryPolicy {
    public final float mBackoffMultiplier;
    public int mCurrentRetryCount;
    public int mCurrentTimeoutMs;
    public final int mMaxNumRetries;

    public DefaultRetryPolicy() {
        this.mCurrentTimeoutMs = 2500;
        this.mMaxNumRetries = 1;
        this.mBackoffMultiplier = 1.0f;
    }

    public DefaultRetryPolicy(int i, int i2, float f) {
        this.mCurrentTimeoutMs = i;
        this.mMaxNumRetries = i2;
        this.mBackoffMultiplier = f;
    }
}
