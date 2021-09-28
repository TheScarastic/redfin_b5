package com.google.android.systemui.columbus.sensors;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
/* loaded from: classes2.dex */
public class EventIMURT {
    protected int mNumberFeature;
    protected int mSizeFeatureWindow;
    protected long mSizeWindowNs;
    protected ArrayList<Float> mFeatureVector = new ArrayList<>();
    protected TfClassifier mClassifier = null;
    protected Deque<Float> mAccXs = new ArrayDeque();
    protected Deque<Float> mAccYs = new ArrayDeque();
    protected Deque<Float> mAccZs = new ArrayDeque();
    protected Deque<Float> mGyroXs = new ArrayDeque();
    protected Deque<Float> mGyroYs = new ArrayDeque();
    protected Deque<Float> mGyroZs = new ArrayDeque();
    protected boolean mGotAcc = false;
    protected boolean mGotGyro = false;
    protected long mSyncTime = 0;
    protected Resample3C mResampleAcc = new Resample3C();
    protected Resample3C mResampleGyro = new Resample3C();
    protected Slope3C mSlopeAcc = new Slope3C();
    protected Slope3C mSlopeGyro = new Slope3C();
    protected Lowpass3C mLowpassAcc = new Lowpass3C();
    protected Lowpass3C mLowpassGyro = new Lowpass3C();
    protected Highpass3C mHighpassAcc = new Highpass3C();
    protected Highpass3C mHighpassGyro = new Highpass3C();

    public Lowpass3C getLowpassAcc() {
        return this.mLowpassAcc;
    }

    public Lowpass3C getLowpassGyro() {
        return this.mLowpassGyro;
    }

    public Highpass3C getHighpassAcc() {
        return this.mHighpassAcc;
    }

    public Highpass3C getHighpassGyro() {
        return this.mHighpassGyro;
    }

    public void reset() {
        this.mAccXs.clear();
        this.mAccYs.clear();
        this.mAccZs.clear();
        this.mGyroXs.clear();
        this.mGyroYs.clear();
        this.mGyroZs.clear();
        this.mGotAcc = false;
        this.mGotGyro = false;
        this.mSyncTime = 0;
    }

    public void processAcc() {
        Point3f update = this.mHighpassAcc.update(this.mLowpassAcc.update(this.mSlopeAcc.update(this.mResampleAcc.getResults().getPoint(), 2400000.0f / ((float) this.mResampleAcc.getInterval()))));
        this.mAccXs.add(Float.valueOf(update.x));
        this.mAccYs.add(Float.valueOf(update.y));
        this.mAccZs.add(Float.valueOf(update.z));
        int interval = (int) (this.mSizeWindowNs / this.mResampleAcc.getInterval());
        while (this.mAccXs.size() > interval) {
            this.mAccXs.removeFirst();
            this.mAccYs.removeFirst();
            this.mAccZs.removeFirst();
        }
    }

    public void processGyro() {
        Point3f update = this.mHighpassGyro.update(this.mLowpassGyro.update(this.mSlopeGyro.update(this.mResampleGyro.getResults().getPoint(), 2400000.0f / ((float) this.mResampleGyro.getInterval()))));
        this.mGyroXs.add(Float.valueOf(update.x));
        this.mGyroYs.add(Float.valueOf(update.y));
        this.mGyroZs.add(Float.valueOf(update.z));
        int interval = (int) (this.mSizeWindowNs / this.mResampleGyro.getInterval());
        while (this.mGyroXs.size() > interval) {
            this.mGyroXs.removeFirst();
            this.mGyroYs.removeFirst();
            this.mGyroZs.removeFirst();
        }
    }

    public ArrayList<Float> scaleGyroData(ArrayList<Float> arrayList, float f) {
        for (int size = arrayList.size() / 2; size < arrayList.size(); size++) {
            arrayList.set(size, Float.valueOf(arrayList.get(size).floatValue() * f));
        }
        return arrayList;
    }
}
