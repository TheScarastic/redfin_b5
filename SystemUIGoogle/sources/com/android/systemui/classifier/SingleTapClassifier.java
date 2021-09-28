package com.android.systemui.classifier;

import android.view.MotionEvent;
import com.android.systemui.classifier.FalsingClassifier;
import java.util.List;
/* loaded from: classes.dex */
public class SingleTapClassifier extends FalsingClassifier {
    private final float mTouchSlop;

    /* access modifiers changed from: package-private */
    public SingleTapClassifier(FalsingDataProvider falsingDataProvider, float f) {
        super(falsingDataProvider);
        this.mTouchSlop = f;
    }

    @Override // com.android.systemui.classifier.FalsingClassifier
    FalsingClassifier.Result calculateFalsingResult(int i, double d, double d2) {
        return isTap(getRecentMotionEvents(), 0.5d);
    }

    public FalsingClassifier.Result isTap(List<MotionEvent> list, double d) {
        if (list.isEmpty()) {
            return falsed(0.0d, "no motion events");
        }
        float x = list.get(0).getX();
        float y = list.get(0).getY();
        for (MotionEvent motionEvent : list) {
            if (Math.abs(motionEvent.getX() - x) >= this.mTouchSlop) {
                return falsed(d, "dX too big for a tap: " + Math.abs(motionEvent.getX() - x) + "vs " + this.mTouchSlop);
            } else if (Math.abs(motionEvent.getY() - y) >= this.mTouchSlop) {
                return falsed(d, "dY too big for a tap: " + Math.abs(motionEvent.getY() - y) + " vs " + this.mTouchSlop);
            }
        }
        return FalsingClassifier.Result.passed(0.0d);
    }
}
