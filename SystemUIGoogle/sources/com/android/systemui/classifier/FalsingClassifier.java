package com.android.systemui.classifier;

import android.view.MotionEvent;
import com.android.systemui.classifier.FalsingDataProvider;
import com.android.systemui.plugins.FalsingManager;
import java.util.List;
/* loaded from: classes.dex */
public abstract class FalsingClassifier {
    private final FalsingDataProvider mDataProvider;
    private final FalsingDataProvider.MotionEventListener mMotionEventListener;

    abstract Result calculateFalsingResult(int i, double d, double d2);

    /* access modifiers changed from: package-private */
    public void onProximityEvent(FalsingManager.ProximityEvent proximityEvent) {
    }

    /* access modifiers changed from: package-private */
    public void onSessionEnded() {
    }

    /* access modifiers changed from: package-private */
    public void onSessionStarted() {
    }

    /* access modifiers changed from: package-private */
    public void onTouchEvent(MotionEvent motionEvent) {
    }

    /* access modifiers changed from: package-private */
    public FalsingClassifier(FalsingDataProvider falsingDataProvider) {
        FalsingClassifier$$ExternalSyntheticLambda0 falsingClassifier$$ExternalSyntheticLambda0 = new FalsingDataProvider.MotionEventListener() { // from class: com.android.systemui.classifier.FalsingClassifier$$ExternalSyntheticLambda0
            @Override // com.android.systemui.classifier.FalsingDataProvider.MotionEventListener
            public final void onMotionEvent(MotionEvent motionEvent) {
                FalsingClassifier.this.onTouchEvent(motionEvent);
            }
        };
        this.mMotionEventListener = falsingClassifier$$ExternalSyntheticLambda0;
        this.mDataProvider = falsingDataProvider;
        falsingDataProvider.addMotionEventListener(falsingClassifier$$ExternalSyntheticLambda0);
    }

    protected String getFalsingContext() {
        return getClass().getSimpleName();
    }

    /* access modifiers changed from: protected */
    public Result falsed(double d, String str) {
        return Result.falsed(d, getFalsingContext(), str);
    }

    /* access modifiers changed from: package-private */
    public List<MotionEvent> getRecentMotionEvents() {
        return this.mDataProvider.getRecentMotionEvents();
    }

    /* access modifiers changed from: package-private */
    public List<MotionEvent> getPriorMotionEvents() {
        return this.mDataProvider.getPriorMotionEvents();
    }

    /* access modifiers changed from: package-private */
    public MotionEvent getFirstMotionEvent() {
        return this.mDataProvider.getFirstRecentMotionEvent();
    }

    /* access modifiers changed from: package-private */
    public MotionEvent getLastMotionEvent() {
        return this.mDataProvider.getLastMotionEvent();
    }

    /* access modifiers changed from: package-private */
    public boolean isHorizontal() {
        return this.mDataProvider.isHorizontal();
    }

    /* access modifiers changed from: package-private */
    public boolean isRight() {
        return this.mDataProvider.isRight();
    }

    /* access modifiers changed from: package-private */
    public boolean isVertical() {
        return this.mDataProvider.isVertical();
    }

    /* access modifiers changed from: package-private */
    public boolean isUp() {
        return this.mDataProvider.isUp();
    }

    /* access modifiers changed from: package-private */
    public float getAngle() {
        return this.mDataProvider.getAngle();
    }

    /* access modifiers changed from: package-private */
    public int getWidthPixels() {
        return this.mDataProvider.getWidthPixels();
    }

    /* access modifiers changed from: package-private */
    public int getHeightPixels() {
        return this.mDataProvider.getHeightPixels();
    }

    /* access modifiers changed from: package-private */
    public float getXdpi() {
        return this.mDataProvider.getXdpi();
    }

    /* access modifiers changed from: package-private */
    public float getYdpi() {
        return this.mDataProvider.getYdpi();
    }

    /* access modifiers changed from: package-private */
    public void cleanup() {
        this.mDataProvider.removeMotionEventListener(this.mMotionEventListener);
    }

    /* access modifiers changed from: package-private */
    public Result classifyGesture(int i, double d, double d2) {
        return calculateFalsingResult(i, d, d2);
    }

    public static void logDebug(String str) {
        BrightLineFalsingManager.logDebug(str);
    }

    public static void logInfo(String str) {
        BrightLineFalsingManager.logInfo(str);
    }

    /* loaded from: classes.dex */
    public static class Result {
        private final double mConfidence;
        private final String mContext;
        private final boolean mFalsed;
        private final String mReason;

        private Result(boolean z, double d, String str, String str2) {
            this.mFalsed = z;
            this.mConfidence = d;
            this.mContext = str;
            this.mReason = str2;
        }

        public boolean isFalse() {
            return this.mFalsed;
        }

        public double getConfidence() {
            return this.mConfidence;
        }

        public String getReason() {
            return String.format("{context=%s reason=%s}", this.mContext, this.mReason);
        }

        public static Result falsed(double d, String str, String str2) {
            return new Result(true, d, str, str2);
        }

        public static Result passed(double d) {
            return new Result(false, d, null, null);
        }
    }
}
