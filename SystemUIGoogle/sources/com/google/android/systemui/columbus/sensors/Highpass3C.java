package com.google.android.systemui.columbus.sensors;
/* loaded from: classes2.dex */
public class Highpass3C {
    private Highpass1C mHighpassX = new Highpass1C();
    private Highpass1C mHighpassY = new Highpass1C();
    private Highpass1C mHighpassZ = new Highpass1C();

    public void setPara(float f) {
        this.mHighpassX.setPara(f);
        this.mHighpassY.setPara(f);
        this.mHighpassZ.setPara(f);
    }

    public void init(Point3f point3f) {
        this.mHighpassX.init(point3f.x);
        this.mHighpassY.init(point3f.y);
        this.mHighpassZ.init(point3f.z);
    }

    public Point3f update(Point3f point3f) {
        return new Point3f(this.mHighpassX.update(point3f.x), this.mHighpassY.update(point3f.y), this.mHighpassZ.update(point3f.z));
    }
}
