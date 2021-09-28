package com.google.android.systemui.columbus.sensors;
/* loaded from: classes2.dex */
public class Sample3C {
    private Point3f mPoint;
    private long mT;

    public long getT() {
        return this.mT;
    }

    public Point3f getPoint() {
        return this.mPoint;
    }

    public Sample3C(float f, float f2, float f3, long j) {
        Point3f point3f = new Point3f(0.0f, 0.0f, 0.0f);
        this.mPoint = point3f;
        point3f.x = f;
        point3f.y = f2;
        point3f.z = f3;
        this.mT = j;
    }
}
