package com.android.systemui.classifier;

import android.view.MotionEvent;
import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.util.DeviceConfigProxy;
/* loaded from: classes.dex */
class ProximityClassifier extends FalsingClassifier {
    private final DistanceClassifier mDistanceClassifier;
    private long mGestureStartTimeNs;
    private boolean mNear;
    private long mNearDurationNs;
    private final float mPercentCoveredThreshold;
    private float mPercentNear;
    private long mPrevNearTimeNs;

    /* access modifiers changed from: package-private */
    public ProximityClassifier(DistanceClassifier distanceClassifier, FalsingDataProvider falsingDataProvider, DeviceConfigProxy deviceConfigProxy) {
        super(falsingDataProvider);
        this.mDistanceClassifier = distanceClassifier;
        this.mPercentCoveredThreshold = deviceConfigProxy.getFloat("systemui", "brightline_falsing_proximity_percent_covered_threshold", 0.1f);
    }

    /* access modifiers changed from: package-private */
    @Override // com.android.systemui.classifier.FalsingClassifier
    public void onSessionStarted() {
        this.mPrevNearTimeNs = 0;
        this.mPercentNear = 0.0f;
    }

    /* access modifiers changed from: package-private */
    @Override // com.android.systemui.classifier.FalsingClassifier
    public void onSessionEnded() {
        this.mPrevNearTimeNs = 0;
        this.mPercentNear = 0.0f;
    }

    @Override // com.android.systemui.classifier.FalsingClassifier
    public void onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mGestureStartTimeNs = motionEvent.getEventTimeNano();
            if (this.mPrevNearTimeNs > 0) {
                this.mPrevNearTimeNs = motionEvent.getEventTimeNano();
            }
            FalsingClassifier.logDebug("Gesture start time: " + this.mGestureStartTimeNs);
            this.mNearDurationNs = 0;
        }
        if (actionMasked == 1 || actionMasked == 3) {
            update(this.mNear, motionEvent.getEventTimeNano());
            long eventTimeNano = motionEvent.getEventTimeNano() - this.mGestureStartTimeNs;
            FalsingClassifier.logDebug("Gesture duration, Proximity duration: " + eventTimeNano + ", " + this.mNearDurationNs);
            if (eventTimeNano == 0) {
                this.mPercentNear = this.mNear ? 1.0f : 0.0f;
            } else {
                this.mPercentNear = ((float) this.mNearDurationNs) / ((float) eventTimeNano);
            }
        }
    }

    @Override // com.android.systemui.classifier.FalsingClassifier
    public void onProximityEvent(FalsingManager.ProximityEvent proximityEvent) {
        boolean covered = proximityEvent.getCovered();
        long timestampNs = proximityEvent.getTimestampNs();
        FalsingClassifier.logDebug("Sensor is: " + covered + " at time " + timestampNs);
        update(covered, timestampNs);
    }

    @Override // com.android.systemui.classifier.FalsingClassifier
    FalsingClassifier.Result calculateFalsingResult(int i, double d, double d2) {
        if (i == 0 || i == 10 || i == 12 || i == 15) {
            return FalsingClassifier.Result.passed(0.0d);
        }
        if (this.mPercentNear <= this.mPercentCoveredThreshold) {
            return FalsingClassifier.Result.passed(0.5d);
        }
        FalsingClassifier.Result isLongSwipe = this.mDistanceClassifier.isLongSwipe();
        if (isLongSwipe.isFalse()) {
            return falsed(0.5d, getReason(isLongSwipe, this.mPercentNear, this.mPercentCoveredThreshold));
        }
        return FalsingClassifier.Result.passed(0.5d);
    }

    private static String getReason(FalsingClassifier.Result result, float f, float f2) {
        return String.format(null, "{percentInProximity=%f, threshold=%f, distanceClassifier=%s}", Float.valueOf(f), Float.valueOf(f2), result.getReason());
    }

    private void update(boolean z, long j) {
        long j2 = this.mPrevNearTimeNs;
        if (j2 != 0 && j > j2 && this.mNear) {
            this.mNearDurationNs += j - j2;
            FalsingClassifier.logDebug("Updating duration: " + this.mNearDurationNs);
        }
        if (z) {
            FalsingClassifier.logDebug("Set prevNearTimeNs: " + j);
            this.mPrevNearTimeNs = j;
        }
        this.mNear = z;
    }
}
