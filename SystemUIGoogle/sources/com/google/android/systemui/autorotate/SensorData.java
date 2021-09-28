package com.google.android.systemui.autorotate;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class SensorData {
    int mSensorIdentifier;
    long mTimestampMillis;
    float mValueX;
    float mValueY;
    float mValueZ;

    /* access modifiers changed from: package-private */
    public SensorData(float f, float f2, float f3, int i, long j) {
        this.mValueX = f;
        this.mValueY = f2;
        this.mValueZ = f3;
        this.mSensorIdentifier = i;
        this.mTimestampMillis = j;
    }
}
