package com.google.android.systemui.columbus.sensors;
/* loaded from: classes2.dex */
public class Lowpass3C extends Lowpass1C {
    private Lowpass1C mLowpassX = new Lowpass1C();
    private Lowpass1C mLowpassY = new Lowpass1C();
    private Lowpass1C mLowpassZ = new Lowpass1C();

    @Override // com.google.android.systemui.columbus.sensors.Lowpass1C
    public void setPara(float f) {
        this.mLowpassX.setPara(f);
        this.mLowpassY.setPara(f);
        this.mLowpassZ.setPara(f);
    }

    public void init(Point3f point3f) {
        this.mLowpassX.init(point3f.x);
        this.mLowpassY.init(point3f.y);
        this.mLowpassZ.init(point3f.z);
    }

    public Point3f update(Point3f point3f) {
        return new Point3f(this.mLowpassX.update(point3f.x), this.mLowpassY.update(point3f.y), this.mLowpassZ.update(point3f.z));
    }
}
