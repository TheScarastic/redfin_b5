package com.google.android.systemui.columbus.sensors;
/* loaded from: classes2.dex */
public class PeakDetector {
    private float mNoiseTolerate;
    private final long mMaxTapDuration = 120000000;
    private int mPeakId = -1;
    private int mNumberPeak = 0;
    private long mTimestamp = 0;
    private float mAmplitude = 0.0f;
    private int mWindowSize = 0;
    private float mMinNoiseTolerate = 0.0f;
    private float mAmplitudeReference = 0.0f;
    private boolean mGotNewHighValue = false;

    public void setMinNoiseTolerate(float f) {
        this.mMinNoiseTolerate = f;
    }

    public void setWindowSize(int i) {
        this.mWindowSize = i;
    }

    public int getPeakId() {
        return this.mPeakId;
    }

    public int getNumberPeaks() {
        return this.mNumberPeak;
    }

    public float getAmplitude() {
        return this.mAmplitude;
    }

    public void reset() {
        this.mAmplitude = 0.0f;
        this.mAmplitudeReference = 0.0f;
        this.mNumberPeak = 0;
        this.mTimestamp = 0;
        this.mPeakId = 0;
    }

    public void update(float f, long j) {
        int i = this.mPeakId - 1;
        this.mPeakId = i;
        if (i < 0) {
            reset();
        }
        this.mNoiseTolerate = this.mMinNoiseTolerate;
        float max = Math.max(this.mAmplitude, f) / 5.0f;
        if (max > this.mNoiseTolerate) {
            this.mNoiseTolerate = max;
        }
        float f2 = this.mAmplitudeReference - f;
        if (f2 < 0.0f) {
            this.mAmplitudeReference = f;
            this.mGotNewHighValue = true;
            long j2 = this.mTimestamp;
            long j3 = j - j2;
            if ((j2 == 0 || (j3 < 120000000 && this.mAmplitude < f)) && f >= this.mNoiseTolerate) {
                this.mPeakId = this.mWindowSize - 1;
                this.mAmplitude = f;
                this.mTimestamp = j;
            }
        } else if (f2 > this.mNoiseTolerate) {
            this.mAmplitudeReference = f;
            if (this.mGotNewHighValue) {
                this.mNumberPeak++;
            }
            this.mGotNewHighValue = false;
        }
    }
}
