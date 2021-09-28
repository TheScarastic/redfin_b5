package com.google.android.systemui.columbus.sensors;
/* loaded from: classes2.dex */
public class Slope3C {
    private Slope1C mSlopeX = new Slope1C();
    private Slope1C mSlopeY = new Slope1C();
    private Slope1C mSlopeZ = new Slope1C();

    public void init(Point3f point3f) {
        this.mSlopeX.init(point3f.x);
        this.mSlopeY.init(point3f.y);
        this.mSlopeZ.init(point3f.z);
    }

    public Point3f update(Point3f point3f, float f) {
        return new Point3f(this.mSlopeX.update(point3f.x, f), this.mSlopeY.update(point3f.y, f), this.mSlopeZ.update(point3f.z, f));
    }
}
