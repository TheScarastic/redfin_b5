package com.android.systemui.classifier;

import android.view.MotionEvent;
import com.android.systemui.classifier.FalsingClassifier;
/* loaded from: classes.dex */
class PointerCountClassifier extends FalsingClassifier {
    private int mMaxPointerCount;

    /* access modifiers changed from: package-private */
    public PointerCountClassifier(FalsingDataProvider falsingDataProvider) {
        super(falsingDataProvider);
    }

    @Override // com.android.systemui.classifier.FalsingClassifier
    public void onTouchEvent(MotionEvent motionEvent) {
        int i = this.mMaxPointerCount;
        if (motionEvent.getActionMasked() == 0) {
            this.mMaxPointerCount = motionEvent.getPointerCount();
        } else {
            this.mMaxPointerCount = Math.max(this.mMaxPointerCount, motionEvent.getPointerCount());
        }
        if (i != this.mMaxPointerCount) {
            FalsingClassifier.logDebug("Pointers observed:" + this.mMaxPointerCount);
        }
    }

    @Override // com.android.systemui.classifier.FalsingClassifier
    FalsingClassifier.Result calculateFalsingResult(int i, double d, double d2) {
        int i2 = 2;
        if (!(i == 0 || i == 2)) {
            i2 = 1;
        }
        return this.mMaxPointerCount > i2 ? falsed(1.0d, getReason(i2)) : FalsingClassifier.Result.passed(0.0d);
    }

    private String getReason(int i) {
        return String.format(null, "{pointersObserved=%d, threshold=%d}", Integer.valueOf(this.mMaxPointerCount), Integer.valueOf(i));
    }
}
