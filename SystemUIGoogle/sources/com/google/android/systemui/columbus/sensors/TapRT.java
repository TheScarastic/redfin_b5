package com.google.android.systemui.columbus.sensors;

import android.content.res.AssetManager;
import android.util.Log;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
/* loaded from: classes2.dex */
public class TapRT extends EventIMURT {
    private int mResult;
    private final long mMinTimeGapNs = 100000000;
    private final long mMaxTimeGapNs = 500000000;
    private final int mFrameAlignPeak = 12;
    private final int mFramePriorPeak = 6;
    private final boolean mUseEnhancedHeuristic = false;
    private PeakDetector mPeakDetector = new PeakDetector();
    private PeakDetector mValleyDetector = new PeakDetector();
    private Deque<Long> mTimestampsBackTap = new ArrayDeque();
    private boolean mWasPeakApproaching = true;

    /* loaded from: classes2.dex */
    public enum TapClass {
        Front,
        Back,
        Left,
        Right,
        Top,
        Bottom,
        Others
    }

    public boolean checkPeakNumber(int i, int i2) {
        return true;
    }

    public boolean checkTapEnergy(ArrayList<Float> arrayList) {
        return true;
    }

    public TapRT(long j, AssetManager assetManager, String str) {
        String modelFileName = getModelFileName(str);
        Log.d("Columbus", "TapRT loaded " + modelFileName);
        this.mClassifier = new TfClassifier(assetManager, modelFileName);
        this.mSizeWindowNs = j;
        this.mSizeFeatureWindow = 50;
        this.mNumberFeature = 50 * 6;
        this.mLowpassAcc.setPara(1.0f);
        this.mLowpassGyro.setPara(1.0f);
        this.mHighpassAcc.setPara(0.05f);
        this.mHighpassGyro.setPara(0.05f);
    }

    private String getModelFileName(String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -870267800:
                if (str.equals("Pixel 4a (5G)")) {
                    c = 0;
                    break;
                }
                break;
            case 1105847547:
                if (str.equals("Pixel 5")) {
                    c = 1;
                    break;
                }
                break;
            case 1905086331:
                if (str.equals("Pixel 3 XL")) {
                    c = 2;
                    break;
                }
                break;
            case 1905116122:
                if (str.equals("Pixel 4 XL")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return "tap7cls_bramble.tflite";
            case 1:
                return "tap7cls_redfin.tflite";
            case 2:
                return "tap7cls_crosshatch.tflite";
            case 3:
                return "tap7cls_coral.tflite";
            default:
                return "tap7cls_flame.tflite";
        }
    }

    public PeakDetector getPeakDetector() {
        return this.mPeakDetector;
    }

    public PeakDetector getValleyDetection() {
        return this.mValleyDetector;
    }

    public void resetFv() {
        this.mFeatureVector = new ArrayList<>(this.mNumberFeature);
        for (int i = 0; i < this.mNumberFeature; i++) {
            this.mFeatureVector.add(Float.valueOf(0.0f));
        }
    }

    public void reset(boolean z) {
        super.reset();
        if (!z) {
            resetFv();
        } else {
            this.mFeatureVector.clear();
        }
    }

    public void updateData(int i, float f, float f2, float f3, long j, long j2, boolean z) {
        this.mResult = TapClass.Others.ordinal();
        if (z) {
            updateHeuristic(i, f, f2, f3, j, j2);
        } else {
            updateML(i, f, f2, f3, j, j2);
        }
    }

    public void updateHeuristic(int i, float f, float f2, float f3, long j, long j2) {
        if (i != 4) {
            if (0 == this.mSyncTime) {
                this.mSyncTime = j;
                this.mResampleAcc.init(f, f2, f3, j, j2);
                this.mResampleAcc.setSyncTime(this.mSyncTime);
                this.mSlopeAcc.init(this.mResampleAcc.getResults().getPoint());
                return;
            }
            while (this.mResampleAcc.update(f, f2, f3, j)) {
                processKeySignalHeuristic();
            }
        }
    }

    public void processKeySignalHeuristic() {
        Point3f update = this.mHighpassAcc.update(this.mLowpassAcc.update(this.mSlopeAcc.update(this.mResampleAcc.getResults().getPoint(), 2400000.0f / ((float) this.mResampleAcc.getInterval()))));
        this.mPeakDetector.update(update.z, this.mResampleAcc.getResults().getT());
        this.mValleyDetector.update(-update.z, this.mResampleAcc.getResults().getT());
        this.mAccZs.add(Float.valueOf(update.z));
        int interval = (int) (this.mSizeWindowNs / this.mResampleAcc.getInterval());
        while (this.mAccZs.size() > interval) {
            this.mAccZs.removeFirst();
        }
        if (this.mAccZs.size() == interval) {
            recognizeTapHeuristic();
        }
        if (this.mResult == TapClass.Back.ordinal()) {
            this.mTimestampsBackTap.addLast(Long.valueOf(this.mResampleAcc.getResults().getT()));
        }
    }

    public void recognizeTapHeuristic() {
        int peakId = this.mPeakDetector.getPeakId();
        int peakId2 = this.mValleyDetector.getPeakId() - peakId;
        if (peakId == 4) {
            this.mFeatureVector = new ArrayList<>(this.mAccZs);
            if (peakId2 <= 0 || peakId2 >= 3) {
                this.mResult = TapClass.Others.ordinal();
            } else {
                this.mResult = TapClass.Back.ordinal();
            }
        }
    }

    public void updateML(int i, float f, float f2, float f3, long j, long j2) {
        if (i == 1) {
            this.mGotAcc = true;
            if (0 == this.mSyncTime) {
                this.mResampleAcc.init(f, f2, f3, j, j2);
            }
            if (!this.mGotGyro) {
                return;
            }
        } else if (i == 4) {
            this.mGotGyro = true;
            if (0 == this.mSyncTime) {
                this.mResampleGyro.init(f, f2, f3, j, j2);
            }
            if (!this.mGotAcc) {
                return;
            }
        }
        if (0 == this.mSyncTime) {
            this.mSyncTime = j;
            this.mResampleAcc.setSyncTime(j);
            this.mResampleGyro.setSyncTime(this.mSyncTime);
            this.mSlopeAcc.init(this.mResampleAcc.getResults().getPoint());
            this.mSlopeGyro.init(this.mResampleGyro.getResults().getPoint());
            this.mLowpassAcc.init(new Point3f(0.0f, 0.0f, 0.0f));
            this.mLowpassGyro.init(new Point3f(0.0f, 0.0f, 0.0f));
            this.mHighpassAcc.init(new Point3f(0.0f, 0.0f, 0.0f));
            this.mHighpassGyro.init(new Point3f(0.0f, 0.0f, 0.0f));
        } else if (i == 1) {
            while (this.mResampleAcc.update(f, f2, f3, j)) {
                processAcc();
                this.mPeakDetector.update(this.mAccZs.getLast().floatValue(), this.mResampleAcc.getResults().getT());
                this.mValleyDetector.update(-this.mAccZs.getLast().floatValue(), this.mResampleAcc.getResults().getT());
            }
        } else if (i == 4) {
            while (this.mResampleGyro.update(f, f2, f3, j)) {
                processGyro();
                recognizeTapML();
            }
            if (this.mResult == TapClass.Back.ordinal()) {
                this.mTimestampsBackTap.addLast(Long.valueOf(j));
            }
        }
    }

    public void recognizeTapML() {
        int t = (int) ((this.mResampleAcc.getResults().getT() - this.mResampleGyro.getResults().getT()) / this.mResampleAcc.getInterval());
        int max = Math.max(0, this.mPeakDetector.getPeakId());
        int max2 = Math.max(0, this.mValleyDetector.getPeakId());
        if (this.mPeakDetector.getAmplitude() <= this.mValleyDetector.getAmplitude()) {
            max = max2;
        }
        if (max > 12) {
            this.mWasPeakApproaching = true;
        }
        int i = max - 6;
        int i2 = i - t;
        int size = this.mAccZs.size();
        if (i >= 0 && i2 >= 0) {
            int i3 = this.mSizeFeatureWindow;
            if (i + i3 <= size && i3 + i2 <= size && this.mWasPeakApproaching && max <= 12) {
                this.mWasPeakApproaching = false;
                if (checkPeakNumber(this.mPeakDetector.getNumberPeaks(), 4) && checkPeakNumber(this.mValleyDetector.getNumberPeaks(), 4)) {
                    addToFeatureVector(this.mAccXs, i, 0);
                    addToFeatureVector(this.mAccYs, i, this.mSizeFeatureWindow);
                    addToFeatureVector(this.mAccZs, i, this.mSizeFeatureWindow * 2);
                    addToFeatureVector(this.mGyroXs, i2, this.mSizeFeatureWindow * 3);
                    addToFeatureVector(this.mGyroYs, i2, this.mSizeFeatureWindow * 4);
                    addToFeatureVector(this.mGyroZs, i2, this.mSizeFeatureWindow * 5);
                    if (!checkTapEnergy(new ArrayList<>(this.mFeatureVector.subList(100, 150)))) {
                        resetFv();
                        return;
                    }
                    ArrayList<Float> scaleGyroData = scaleGyroData(this.mFeatureVector, 10.0f);
                    this.mFeatureVector = scaleGyroData;
                    ArrayList<ArrayList<Float>> predict = this.mClassifier.predict(scaleGyroData, 7);
                    if (!predict.isEmpty()) {
                        this.mResult = Util.getMaxId(predict.get(0));
                    }
                }
            }
        }
    }

    private void addToFeatureVector(Deque<Float> deque, int i, int i2) {
        Iterator<Float> it = deque.iterator();
        int i3 = 0;
        while (it.hasNext()) {
            if (i3 < i) {
                it.next();
            } else if (i3 < this.mSizeFeatureWindow + i) {
                this.mFeatureVector.set(i2, it.next());
                i2++;
            } else {
                return;
            }
            i3++;
        }
    }

    public int checkDoubleTapTiming(long j) {
        Iterator<Long> it = this.mTimestampsBackTap.iterator();
        while (it.hasNext()) {
            if (j - it.next().longValue() > 500000000) {
                it.remove();
            }
        }
        if (this.mTimestampsBackTap.isEmpty()) {
            return 0;
        }
        for (Long l : this.mTimestampsBackTap) {
            if (this.mTimestampsBackTap.getLast().longValue() - l.longValue() > 100000000) {
                this.mTimestampsBackTap.clear();
                return 2;
            }
        }
        return 1;
    }
}
